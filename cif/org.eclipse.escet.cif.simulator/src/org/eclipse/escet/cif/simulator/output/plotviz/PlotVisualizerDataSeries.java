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

import java.util.Deque;
import java.util.LinkedList;

import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateObjectMeta;

/** Data for a single series. */
public class PlotVisualizerDataSeries {
    /** The metadata for the variable represented by this series. */
    public final RuntimeStateObjectMeta meta;

    /** The data points values. */
    public final Deque<PlotVisualizerDataPoint> points = new LinkedList<>();

    /**
     * Constructor for the {@link PlotVisualizerDataSeries} class.
     *
     * @param meta The metadata for the variable represented by this series.
     */
    public PlotVisualizerDataSeries(RuntimeStateObjectMeta meta) {
        this.meta = meta;
    }

    /**
     * Returns the x values as an array.
     *
     * @return The x values array.
     */
    public double[] getXValuesArray() {
        double[] array = new double[points.size()];
        int i = 0;
        for (PlotVisualizerDataPoint point: points) {
            array[i] = point.x;
            i++;
        }
        return array;
    }

    /**
     * Returns the y values as an array.
     *
     * @return The y values array.
     */
    public double[] getYValuesArray() {
        double[] array = new double[points.size()];
        int i = 0;
        for (PlotVisualizerDataPoint point: points) {
            array[i] = point.y;
            i++;
        }
        return array;
    }
}
