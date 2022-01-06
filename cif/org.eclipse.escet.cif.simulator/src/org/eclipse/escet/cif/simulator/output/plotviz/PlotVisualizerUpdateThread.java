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

package org.eclipse.escet.cif.simulator.output.plotviz;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.swt.widgets.Display;
import org.knowm.xchart.XYChart;

/** Update thread for the {@link PlotVisualizer}. Ensure GUI thread is not overloaded with update requests. */
public class PlotVisualizerUpdateThread extends Thread {
    /** The chart to update. */
    private final XYChart chart;

    /** The canvas to update. */
    private final PlotVisualizerCanvas canvas;

    /**
     * The data for the next update. Must only be set/accessed via locking on {@link #updateRequested}. May be
     * {@code null} to not set new data.
     */
    public final AtomicReference<PlotVisualizerUpdateData> updateData = new AtomicReference<>();

    /**
     * The x-axis range for the next update. Must only be set/accessed via locking on {@link #updateRequested}. May be
     * {@code null} to not set a new range.
     */
    public final AtomicReference<PlotVisualizerRange> updateRangeX = new AtomicReference<>();

    /** Whether an update is requested. Must only be set/accessed via locking on {@link #updateRequested}. */
    public final AtomicBoolean updateRequested = new AtomicBoolean();

    /**
     * Constructor for the {@link PlotVisualizerUpdateThread} class.
     *
     * @param chart The chart to update.
     * @param canvas The canvas to update.
     */
    public PlotVisualizerUpdateThread(XYChart chart, PlotVisualizerCanvas canvas) {
        this.chart = chart;
        this.canvas = canvas;
        setName(getClass().getName());
    }

    @SuppressWarnings("restriction")
    @Override
    public void run() {
        while (true) {
            // Initialization.
            AtomicBoolean canvasDisposed = new AtomicBoolean(false);

            // Wait until update is needed.
            PlotVisualizerUpdateData data = null;
            PlotVisualizerRange rangeX = null;
            synchronized (updateRequested) {
                if (updateRequested.get()) {
                    // Update needed now. Get data to use for the update.
                    data = updateData.get();
                    rangeX = updateRangeX.get();

                    // Reset for next update, and release lock as soon as possible.
                    updateData.set(null);
                    updateRangeX.set(null);
                    updateRequested.set(false);
                } else {
                    // No update requested yet. Wait for request.
                    // Don't wait forever, as we want to terminate the thread if no more requests come in.
                    try {
                        updateRequested.wait(1000);
                    } catch (InterruptedException e) {
                        // Ignore.
                    }

                    // Terminate update thread if visualizer is closed.
                    Display.getDefault().syncExec(() -> { canvasDisposed.set(canvas.isDisposed()); });
                    if (canvasDisposed.get()) {
                        return;
                    }

                    // Visualizer not closed, keep checking for updates.
                    continue;
                }
            }

            // Update chart, on GUI thread.
            PlotVisualizerUpdateData data2 = data;
            PlotVisualizerRange rangeX2 = rangeX;
            Assert.check(data2 != null || rangeX2 != null);
            Display.getDefault().syncExec(() -> {
                // Cancel if visualizer disposed.
                boolean disposed = canvas.isDisposed();
                canvasDisposed.set(disposed);
                if (disposed) {
                    return;
                }

                // Update plot data.
                if (data2 != null) {
                    if (chart.getSeriesMap().isEmpty()) {
                        for (PlotVisualizerUpdateDataEntry entry: data2.entries) {
                            chart.addSeries(entry.name, entry.xValues, entry.yValues);
                        }
                    } else {
                        for (PlotVisualizerUpdateDataEntry entry: data2.entries) {
                            chart.updateXYSeries(entry.name, entry.xValues, entry.yValues, null);
                        }
                    }
                }

                // Update x-axis range.
                if (rangeX2 != null) {
                    chart.getStyler().setXAxisMin(rangeX2.lower);
                    chart.getStyler().setXAxisMax(rangeX2.upper);
                }

                // Update y-axis range.
                if (data2 != null && !Double.isInfinite(data2.minY) && !Double.isInfinite(data2.maxY)) {
                    double yRange = Math.abs(data2.maxY - data2.minY);
                    double yOffset = yRange * 0.05;
                    chart.getStyler().setYAxisMin(data2.minY - yOffset);
                    chart.getStyler().setYAxisMax(data2.maxY + yOffset);
                }
            });
            if (canvasDisposed.get()) {
                return;
            }

            // Render new pixels, in update thread, to prevent locking the GUI for long periods of time.
            canvas.updatePixels();

            // Repaint on GUI thread.
            Display.getDefault().syncExec(() -> {
                boolean disposed = canvas.isDisposed();
                canvasDisposed.set(disposed);
                if (disposed) {
                    return;
                }
                canvas.redraw();
            });
            if (canvasDisposed.get()) {
                return;
            }
        }
    }
}
