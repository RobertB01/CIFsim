//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output.plotviz;

import java.awt.Graphics2D;

import org.eclipse.escet.common.eclipse.ui.G2dSwtCanvas;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.knowm.xchart.XYChart;

/** Plot visualizer canvas. */
public class PlotVisualizerCanvas extends G2dSwtCanvas {
    /** The chart to display on the canvas. */
    private final XYChart chart;

    /** The lock to use to synchronize on {@link #pixels} to ensure thread safety. */
    private final Object pixelsLock = new Object();

    /** The last rendered pixels, or {@code null} if not available. */
    private byte[] pixels;

    /** The last known size of the canvas, or {@code null} if not available. */
    private Point lastSize;

    /**
     * Constructor for the {@link PlotVisualizerCanvas} class.
     *
     * @param parent The parent of the canvas.
     * @param chart The chart to display on the canvas.
     */
    public PlotVisualizerCanvas(Composite parent, XYChart chart) {
        super(parent);
        this.chart = chart;

        setupCanvas();
    }

    /**
     * Returns the last known size of the canvas, or {@code null} if not available.
     *
     * @return The last known size of the canvas, or {@code null}.
     */
    public Point getLastSize() {
        return lastSize;
    }

    /** Sets up the canvas. */
    private void setupCanvas() {
        // Store current size.
        lastSize = getSize();

        // Ensure proper updating when canvas is resized.
        addControlListener(new ControlListener() {
            @Override
            public void controlResized(ControlEvent e) {
                // Set new size, create new image and force redraw.
                lastSize = getSize();
                updatePixels();
                redraw();
            }

            @Override
            public void controlMoved(ControlEvent e) {
                // Ignore.
            }
        });
    }

    @Override
    public byte[] getImageToPaint(int width, int height) {
        // Use pre-rendered pixel data, if available.
        synchronized (pixelsLock) {
            if (pixels != null) {
                return pixels;
            }
        }

        // Render image on the spot.
        return super.getImageToPaint(width, height);
    }

    /** Renders the chart and updates the pre-rendered pixel data. */
    public void updatePixels() {
        byte[] newPixels = paintInMemory(lastSize.x, lastSize.y);
        synchronized (pixelsLock) {
            pixels = newPixels;
        }
    }

    @Override
    public void paint(Graphics2D g, int width, int height) {
        chart.paint(g, width, height);
    }
}
