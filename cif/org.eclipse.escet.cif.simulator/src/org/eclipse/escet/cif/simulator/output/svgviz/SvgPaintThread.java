//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.cif.simulator.output.svgviz;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.cif.simulator.options.SimulationSpeedOption;
import org.eclipse.escet.cif.simulator.options.TestModeOption;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.svg.SvgCanvas;
import org.eclipse.escet.common.svg.SvgVisualizer;
import org.eclipse.swt.widgets.Display;

/**
 * Paint thread used for SVG visualization.
 *
 * <p>
 * The paint thread accepts data from the {@link SvgRenderThread}, and stores it in a {@link #QUEUE_SIZE limited
 * capacity} queue. Once it receives new data, it waits until enough real-time time has passed since the last frame
 * (taking into account the frame rate). It then makes the new pixel data available for rendering, and forces a redraw.
 * </p>
 *
 * <p>
 * The {@link SvgOutputComponent} and {@link SvgRenderThread} together pre-compute states and pre-render images, in
 * order to try to always have the next rendered image available for this thread to display, before it is actually
 * needed. This is done to ensure smooth visualization. If real-time simulation is disabled, the multi-threaded
 * architecture is still used, but all data is forwarded as soon as possible, and no real-time delays are used.
 * </p>
 *
 * <p>
 * If real-time simulation is enabled, we only get frames at multiples of the model time delta. We then have a fixed
 * theoretical sleep time between frames. We don't compensate over the absolute simulation time. Instead, we only
 * compensate with respect to the previous frame. This allows for the quickest recovery, if a frame (pixel data) arrives
 * too late.
 * </p>
 *
 * <p>
 * The actual painting of the images is done on the SWT UI thread. This thread simply provides the rendered images at
 * the right times.
 * </p>
 */
public class SvgPaintThread extends Thread {
    /** Whether to output internal/developer debugging information. */
    private static final boolean DEBUG = false;

    /** The maximum size of the pixel data queue. */
    private static final int QUEUE_SIZE = 1;

    /**
     * Queue of unprocessed items (model time and pixel data). Is modified in-place. Model time {@code -1} indicates a
     * shutdown request.
     */
    private final BlockingQueue<Pair<Double, byte[]>> queue = new LinkedBlockingQueue<>(QUEUE_SIZE);

    /** The SVG visualizer. */
    public final SvgVisualizer visualizer;

    /** The SVG canvas. */
    public final SvgCanvas canvas;

    /** Whether {@link TestModeOption test mode} is enabled. */
    private final boolean testMode;

    /** Whether real-time simulation (mode) is enabled. */
    private final boolean realTimeMode;

    /** The {@link SimulationSpeedOption simulation speed}, or {@code null} if real-time simulation is disabled. */
    private final Double simSpeed;

    /** The number of frames encountered so far. Is increased to {@code 1} for the first frame. */
    private int frameCount = 0;

    /**
     * The start time of the real-time timer. The time is in nanoseconds, and is not related to any notion of system or
     * wall-clock time. It is initialized for the first image. Remains zero if real-time simulation is disabled.
     *
     * @see System#nanoTime
     * @see #lastTime
     */
    private long startTime;

    /**
     * The time (in nanoseconds) between two consecutive frames, for real-time simulation, or zero if real-time
     * simulation is disabled.
     */
    private final long frameTime;

    /**
     * The last time of the real-time timer, used to figure out how long ago the last frame was processed. The time is
     * in nanoseconds, and is not related to any notion of system or wall-clock time. It is initialized for the first
     * image. Remains zero if real-time simulation is disabled.
     *
     * @see System#nanoTime
     * @see #startTime
     */
    private long lastTime;

    /**
     * The number of milliseconds that our real-time implementation is behind on the ideal real-time time (which is the
     * model time, compensated for speed), for the previous frame. Remains zero if real-time simulation is disabled.
     */
    private double lastBehindMillis;

    /** The exception that occurred in this thread, or {@code null} if not applicable. */
    public AtomicReference<Throwable> exception = new AtomicReference<>();

    /**
     * Constructor for the {@link SvgPaintThread} class.
     *
     * @param visualizer The SVG visualizer.
     */
    public SvgPaintThread(SvgVisualizer visualizer) {
        // Set thread name for debugging.
        super(SvgPaintThread.class.getName());

        // Store parameters and other data.
        this.visualizer = visualizer;
        this.canvas = visualizer.getSvgCanvas();
        this.testMode = TestModeOption.isEnabled();
        this.realTimeMode = FrameRateOption.isRealTimeEnabled();
        this.simSpeed = SimulationSpeedOption.getSimSpeed();
        this.frameTime = realTimeMode ? (long)(1e9 / FrameRateOption.getFrameRate()) : 0;
    }

    /**
     * Adds data to the queue. This method blocks for as long as the queue is full.
     *
     * @param data The data (model time and pixel data) to add. Model time {@code -1} indicates a shutdown request.
     */
    public void addData(Pair<Double, byte[]> data) {
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            // Perform operation in normal mode.
            runInternal(true);
        } catch (Throwable ex) {
            // Store exception for main simulator thread.
            exception.set(ex);

            // Perform operation in exception mode.
            runInternal(false);
        }
    }

    /**
     * Internal 'run' method of the thread.
     *
     * @param normalMode Whether to run in normal mode ({@code true}) or exception mode ({@code false}). In exception
     *     mode, the queue and shutdown requests processed, but actual states are ignored. This unblocks the render
     *     thread, and allows for 'normal' shutdown. Implementers note: the exception mode should not generate any
     *     exceptions, as they will be uncaught!
     */
    public void runInternal(boolean normalMode) {
        // Initialization.
        boolean first = true;

        // Keep processing images from the queue.
        while (true) {
            // Take the data of the next image (call blocks while queue is
            // empty).
            Pair<Double, byte[]> data;
            try {
                data = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            double modelTime = data.left;
            byte[] pixelData = data.right;

            // If we get a shutdown request, we shutdown ourselves. The
            // visualizer remains open, to be closed by the user.
            if (modelTime == -1.0) {
                return;
            }

            // If in exception mode, skip actual processing.
            if (!normalMode) {
                continue;
            }

            // Update frame count for normal/non-special state (frame).
            frameCount++;

            // Sleep if needed for real-time simulation, to try to keep frames
            // evenly spaces over time.
            if (realTimeMode) {
                if (first) {
                    // First image. Initialize real-time times.
                    startTime = System.nanoTime();
                    lastTime = startTime;
                    if (DEBUG) {
                        debug("no sleep for first frame...");
                    }
                } else {
                    // Non-first image. Calculate how long ago we had the
                    // previous image, and after how much time we should
                    // provide the next image. This is purely based on the
                    // frame rate and time since the last image.
                    long curTime = System.nanoTime();
                    long elapsedNanos = curTime - lastTime;
                    long remainingNanos = frameTime - elapsedNanos;
                    long remainingMillis = remainingNanos / 1000 / 1000;

                    // Sleep for the remaining milliseconds until the next
                    // image is to be displayed. If that is now, or we are
                    // behind on real-time, don't delay.
                    boolean skipSleep = (remainingMillis <= 0);
                    if (!skipSleep && !testMode) {
                        try {
                            Thread.sleep(remainingMillis);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // Developer debug information.
                    if (DEBUG) {
                        long rtNanos = System.nanoTime() - startTime;
                        double rtMillis = rtNanos / 1e6;
                        double modelMillis = modelTime * 1e3;
                        double behindMillis = rtMillis - (modelMillis / simSpeed);
                        double behindDelta = behindMillis - lastBehindMillis;
                        debug("slept for (ms): " + remainingMillis + ", real-time time (ms): " + rtMillis
                                + ", model time (ms): " + modelMillis + ", behind on rt (ms): " + behindMillis
                                + ", behind delta (ms): " + behindDelta + (skipSleep ? " (sleep skipped)" : ""));
                        lastBehindMillis = behindMillis;
                    }

                    // Update for next iteration. Note that 'freezes' during
                    // the calculation of the sleep time, and during the sleep
                    // time itself, are not compensated for, as we reset the
                    // clock here, and we don't compare 'curTime' to the new
                    // 'lastTime'. That is, we don't compensate for past
                    // 'mistakes', as that gets us the quickest recovery.
                    lastTime = System.nanoTime();
                }
            }

            // Set new current pixel data (of the rendered image).
            synchronized (canvas.pixelDataLock) {
                canvas.pixelData = pixelData;
            }

            // Force repaint, on UI thread.
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (canvas.isDisposed()) {
                        return;
                    }
                    int width = canvas.getImageWidth();
                    int height = canvas.getImageHeight();
                    canvas.redraw(0, 0, width, height, true);
                }
            });

            // If this is the first image, let the visualizer know that it may
            // now show the image, as it has just been rendered.
            if (first) {
                visualizer.initDone();
                first = false;
            }
        }
    }

    /**
     * Prints a line of internal/developer debug output. Should only be invoked if {@link #DEBUG} is {@code true}.
     *
     * @param txt The line of text to print.
     */
    private void debug(String txt) {
        System.out.println(getClass().getSimpleName() + ": frame#" + frameCount + ": " + txt);
    }
}
