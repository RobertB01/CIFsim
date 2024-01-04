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

package org.eclipse.escet.cif.simulator.output.plotviz;

/** Single data point in a {@link PlotVisualizerDataSeries}. */
public class PlotVisualizerDataPoint {
    /** The x value. */
    public final double x;

    /** The y value. */
    public final double y;

    /**
     * Constructor for the {@link PlotVisualizerDataPoint} class.
     *
     * @param x The x value.
     * @param y The y value.
     */
    public PlotVisualizerDataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
