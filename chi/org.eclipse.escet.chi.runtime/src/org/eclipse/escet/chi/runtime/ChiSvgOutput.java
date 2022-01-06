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

package org.eclipse.escet.chi.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.data.io.ChiFileHandle;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.svg.SvgCanvas;
import org.eclipse.escet.common.svg.SvgException;
import org.eclipse.escet.common.svg.SvgVisualizer;
import org.eclipse.swt.widgets.Display;

/** Chi SVG output. */
public class ChiSvgOutput extends ChiFileHandle {
    /** Debug flag. */
    private boolean debug = false;

    /** Number of times the file is opened. */
    private int openCount = 0;

    /** Coordinator of the simulation. */
    private final ChiCoordinator coord;

    /** The absolute or relative local file system path to the SVG file. */
    private final String svgPath;

    /** Collected partial line of text. */
    private String text = "";

    /** The SVG visualizer. */
    public final SvgVisualizer svgVis;

    /** The canvas to draw on. */
    private final SvgCanvas canvas;

    /** Whether the first image has been rendered. */
    private boolean firstDone = false;

    /** Current model time (time of the updates being applied). */
    private double curModelTime = 0;

    /** Has the image been updated (since the last display)? */
    private boolean isChanged = true;

    /** Previous model time (currently displayed if {@link #firstDone}). */
    private double prevModelTime;

    /** Real time when {@link #prevModelTime} was displayed (in nanos). */
    private long prevRealTime;

    /**
     * Constructor of the {@link ChiSvgOutput} class.
     *
     * @param coord Simulation coordinator.
     * @param filename Filename of the SVG file to open.
     */
    public ChiSvgOutput(ChiCoordinator coord, String filename) {
        super(filename, "w");
        this.coord = coord;

        Assert.check(filename.startsWith("SVG:"));
        this.svgPath = filename.substring(4);
        String svgAbsPath = Paths.resolve(svgPath);

        // Open the SVG visualizer.
        svgVis = ControlEditor.show(svgAbsPath, SvgVisualizer.class, "SVG visualizer");

        // If loading the SVG image failed, simulation fails as well.
        SvgException svgLoadErr = svgVis.getSvgLoadError();
        if (svgLoadErr != null) {
            throw svgLoadErr;
        }

        // Store canvas, now that we know that loading succeeded.
        canvas = svgVis.getSvgCanvas();
        canvas.path = svgPath;
    }

    @Override
    public int read() {
        String msg = "Cannot read from a SVG output.";
        throw new RuntimeException(msg);
    }

    @Override
    public void markStream(int count) {
        String msg = "Cannot mark the stream of a SVG output.";
        throw new RuntimeException(msg);
    }

    @Override
    public void resetStream() {
        String msg = "Cannot reset the stream of a SVG output.";
        throw new RuntimeException(msg);
    }

    @Override
    public void write(String line) {
        if (openCount == 0) {
            return;
        }
        if (!svgVis.isAvailable()) {
            openCount = 0; // Like "closeDown", but don't display final state.
            coord.setTerminateAll(null);
            return;
        }

        // Collect text fragments until a complete line.
        text = text + line;
        int eolIndex = text.indexOf('\n');
        if (eolIndex < 0) {
            return;
        }

        // Process complete lines.
        while (eolIndex >= 0) {
            line = text.substring(0, eolIndex);
            text = text.substring(eolIndex + 1);
            eolIndex = text.indexOf('\n');

            // Decode line.
            SvgModification sm = SvgAttribModification.decode(line, svgPath);
            if (sm == null) {
                sm = SvgTextModification.decode(line, svgPath);
            }
            if (sm == null) {
                sm = SvgCopyModification.decode(line, svgPath);
            }
            if (sm == null) {
                sm = SvgAbsmoveModification.decode(line, svgPath);
            }
            boolean redraw = (sm == null && line.equals("redraw"));

            if (sm == null && !redraw) {
                String msg = fmt("SVG command \"%s\" was not understood.", line);
                throw new InputOutputException(msg);
            }

            if (redraw || curModelTime != coord.getCurrentTime()) {
                // The image is being redrawn when a redraw is forced, or
                // after a jump in time.
                // In both cases, a change must have been made, or nothing has
                // been drawn yet. The latter is controlled with
                // {@link #isChanged}, which is initially {@code true}.
                if (isChanged) {
                    displaySvg();
                }
                isChanged = false;
            }
            curModelTime = coord.getCurrentTime();

            // Update the tree with the change, if available.
            if (sm != null) {
                sm.apply(svgVis);
                isChanged = true;
            }
        }
    }

    /** Display the SVG image. */
    private void displaySvg() {
        if (!firstDone) {
            try {
                canvas.updateImageSize();
            } catch (InvalidInputException ex) {
                String msg = fmt("Failed to update image size for SVG image file \"%s\".", svgPath);
                throw new InvalidInputException(msg, ex);
            }
        }

        int width = canvas.getImageWidth();
        int height = canvas.getImageHeight();
        byte[] pixelData = canvas.paintInMemory(width, height);
        Assert.check(pixelData.length > 0);

        synchronized (canvas.pixelDataLock) {
            canvas.pixelData = pixelData;
        }

        if (!firstDone) {
            prevModelTime = curModelTime;
            prevRealTime = System.nanoTime();
        } else {
            // Delay until curModelTime has arrived in real-time.
            double modelStep = curModelTime - prevModelTime;
            if (debug) {
                System.out.printf("modelStep = %f - %f = %f units\n", curModelTime, prevModelTime, modelStep);
            }

            long elapsedRealTime = System.nanoTime() - prevRealTime;
            if (debug) {
                System.out.printf("elapsed = %d ns = %f s\n", elapsedRealTime, elapsedRealTime / 1e9);
            }

            long delayNanos = (long)(modelStep * 1e9) - elapsedRealTime;
            long delayMillis = delayNanos / 1000 / 1000;
            if (debug) {
                System.out.printf("delay = %d ns = %d ms = %f s\n", delayNanos, delayMillis, delayMillis / 1e3);
            }

            if (delayMillis > 0) {
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            prevModelTime = curModelTime;
            prevRealTime = System.nanoTime();
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

        if (!firstDone) {
            svgVis.initDone();
            firstDone = true;
        }
    }

    /** 'open' has been called on this handle. */
    public void openCalled() {
        openCount++;
    }

    @Override
    public boolean isClosed() {
        return openCount == 0;
    }

    @Override
    public void close() {
        openCount--;
        if (openCount == 0) {
            displaySvg(); // Display final state.
        }
    }

    /** Simulation has ended, close down completely. */
    public void closeDown() {
        if (openCount != 0) {
            displaySvg(); // Display final state.
        }
        openCount = 0;
    }
}
