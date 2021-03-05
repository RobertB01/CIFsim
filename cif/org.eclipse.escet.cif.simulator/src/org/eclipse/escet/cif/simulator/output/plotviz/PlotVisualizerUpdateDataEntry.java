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

/** Data to update the plot visualizer, for a single state object. */
public class PlotVisualizerUpdateDataEntry {
    /** The name of the chart series. */
    public final String name;

    /** The x values. */
    public final double[] xValues;

    /** The y values. */
    public final double[] yValues;

    /**
     * Constructor for the {@link PlotVisualizerUpdateDataEntry} class.
     *
     * @param name The name of the chart series.
     * @param xValues The x values.
     * @param yValues The y values.
     */
    public PlotVisualizerUpdateDataEntry(String name, double[] xValues, double[] yValues) {
        this.name = name;
        this.xValues = xValues;
        this.yValues = yValues;
    }
}
