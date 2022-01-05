//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.escet.cif.simulator.input.SvgInputComponent;
import org.eclipse.escet.cif.simulator.options.TestModeOption;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.svg.SvgCanvas;
import org.eclipse.escet.common.svg.SvgVisualizer;
import org.eclipse.escet.common.svg.selector.SvgSelector;

/**
 * Render thread used for SVG visualization.
 *
 * <p>
 * The render thread accepts states from the {@link SvgOutputComponent}, and stores them in a {@link #QUEUE_SIZE limited
 * capacity} queue. Once it receives a new state, it applies the CIF/SVG mappings, processes and empties the
 * {@link SvgSelector} mouse events queue, renders the new image to pixels, processes and empties the
 * {@link SvgSelector} mouse events queue, and forwards the new pixels to the {@link SvgPaintThread}.
 * </p>
 *
 * <p>
 * Together with the simulator, which pre-computes states, the render thread pre-computes rendered images, to ensure
 * they are available in advance for painting, when the time comes. If real-time simulation is disabled, the
 * multi-threaded architecture is still used, but all data is forwarded as soon as possible, and no real-time delays are
 * used.
 * </p>
 *
 * <p>
 * This thread is the only thread that may access and modify the SVG XML tree, render the SVG image, etc. The simulator
 * thread ({@link SvgOutputComponent}, {@link SvgInputComponent}), paint thread, and UI thread may thus not access,
 * modify, or otherwise operate on any SVG data.
 * </p>
 */
public class SvgRenderThread extends Thread {
    /** The maximum size of the state queue. */
    private static final int QUEUE_SIZE = 1;

    /**
     * Queue of unprocessed states, to turn into frames (rendered images). Is modified in-place.
     * {@link ShutdownRuntimeState#INSTANCE} indicates a shutdown request.
     */
    private final BlockingQueue<RuntimeState> stateQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);

    /**
     * SVG selector, for the {@link SvgInputComponent}. Maintained by this thread, as this thread is the only thread
     * that may access the SVG XML tree. Is {@code null} if SVG input is not used, or if not yet provided by the
     * {@link SvgInputComponent}.
     */
    public SvgSelector selector;

    /** The CIF/SVG declarations. */
    private final RuntimeCifSvgDecls cifSvgDecls;

    /** The SVG canvas. */
    private final SvgCanvas canvas;

    /** The SVG paint thread. */
    private final SvgPaintThread paintThread;

    /** Is {@link TestModeOption test mode} enabled? */
    protected final boolean testMode;

    /**
     * The semaphore to use for synchronization, if {@link TestModeOption test mode} is enabled. It is used to ensure
     * synchronous execution, in the multi-threaded design, for consistent/testable output. Is {@code null} if test mode
     * is disabled.
     */
    private final Semaphore testModeSemaphore;

    /** The exception that occurred in this thread, or {@code null} if not applicable. */
    public AtomicReference<Throwable> exception = new AtomicReference<>();

    /**
     * Constructor for the {@link SvgRenderThread} class.
     *
     * @param cifSvgDecls The CIF/SVG declarations.
     * @param visualizer The SVG visualizer.
     * @param paintThread The SVG paint thread.
     */
    public SvgRenderThread(RuntimeCifSvgDecls cifSvgDecls, SvgVisualizer visualizer, SvgPaintThread paintThread) {
        // Set thread name for debugging.
        super(SvgRenderThread.class.getName());

        // Store parameters and other data.
        this.cifSvgDecls = cifSvgDecls;
        this.canvas = visualizer.getSvgCanvas();
        this.paintThread = paintThread;
        this.testMode = TestModeOption.isEnabled();
        this.testModeSemaphore = this.testMode ? new Semaphore(0) : null;
    }

    /**
     * Adds a state that is to be rendered, to the queue. This method blocks for as long as the queue is full.
     *
     * <p>
     * In test mode, waits for the processing of the state to complete, before continuing. See also
     * {@link #testModeSemaphore}.
     * </p>
     *
     * @param state The state to add. {@link ShutdownRuntimeState#INSTANCE} indicates a shutdown request.
     */
    public void addState(RuntimeState state) {
        try {
            // Add state.
            stateQueue.put(state);

            // Wait for the processing of the state that was just added to be
            // completed. This ensures synchronous execution, and thus
            // consistent console output, which is essential for automated
            // testing. Synchronous execution is only performed when
            // test mode is enabled.
            if (testMode) {
                testModeSemaphore.acquire();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Signals that the state has been processed, for synchronous execution in test mode.
     *
     * @see #testModeSemaphore
     */
    private void signalSyncLock() {
        if (testMode) {
            testModeSemaphore.release();
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

            // Signal that the state has been processed.
            signalSyncLock();

            // Perform operation in exception mode.
            runInternal(false);
        }
    }

    /**
     * Internal 'run' method of the thread.
     *
     * @param normalMode Whether to run in normal mode ({@code true}) or exception mode ({@code false}). In exception
     *     mode, the queue and shutdown requests processed, but actual states are ignored. This unblocks the simulator
     *     thread, and allows for 'normal' shutdown. Implementers note: the exception mode should not generate any
     *     exceptions, as they will be uncaught!
     */
    private void runInternal(boolean normalMode) {
        // Keep processing states from the queue.
        boolean first = true;
        while (true) {
            // Take the next state (call blocks while queue is empty).
            RuntimeState state;
            try {
                state = stateQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // If we get a shutdown request, we send a shutdown request to the
            // paint thread as well, and we shutdown ourselves.
            if (state == ShutdownRuntimeState.INSTANCE) {
                // Signal that the state has been processed.
                signalSyncLock();

                // Shutdown.
                paintThread.addData(pair(-1.0, (byte[])null));
                return;
            }

            // If in exception mode, skip actual processing.
            if (!normalMode) {
                // Signal that the state has been processed.
                signalSyncLock();

                // Continue with next state.
                continue;
            }

            // As soon as possible after we get a new state, process the queue,
            // to make clicks responsive.
            if (selector != null) {
                selector.processQueue(false);
            }

            // Apply the CIF/SVG output mappings.
            try {
                cifSvgDecls.applyOutput(state);
            } catch (CifSimulatorException e) {
                // Run-time error while applying output mappings.
                String msg = "Simulation resulted in a runtime error during application of SVG output mappings.";
                throw new CifSimulatorException(msg, e, state);
            }

            // Just before painting, process the queue, to make sure we paint
            // the image for the most recent mouse status. Since we just
            // applied the output mappings, the SVG image may have changed.
            if (selector != null) {
                selector.processQueue(true);
            }

            // Just before the first render, update the canvas size.
            if (first) {
                try {
                    canvas.updateImageSize();
                } catch (CifSimulatorException ex) {
                    String msg = fmt("Failed to update image size for SVG image file \"%s\".",
                            cifSvgDecls.getSvgRelPath());
                    throw new CifSimulatorException(msg, ex, state);
                }
                first = false;
            }

            // Render the SVG image in memory, to obtain the pixel data.
            int width = canvas.getImageWidth();
            int height = canvas.getImageHeight();
            byte[] pixelData = canvas.paintInMemory(width, height);
            Assert.check(pixelData.length > 0);

            // Send model time and pixel data to the paint thread.
            paintThread.addData(pair(state.getTime(), pixelData));

            // Signal that the state has been processed.
            signalSyncLock();
        }
    }
}
