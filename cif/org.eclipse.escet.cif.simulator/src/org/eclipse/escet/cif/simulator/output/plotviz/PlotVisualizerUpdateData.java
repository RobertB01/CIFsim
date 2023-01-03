//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import java.util.List;

/** Data to update the plot visualizer, for all state objects. */
public class PlotVisualizerUpdateData {
    /** The entries with data per state object. */
    public final List<PlotVisualizerUpdateDataEntry> entries;

    /** The minimum y-axis value. */
    public final double minY;

    /** The maximum y-axis value. */
    public final double maxY;

    /**
     * Constructor for the {@link PlotVisualizerUpdateData} class.
     *
     * @param entries The entries with data per state object.
     */
    public PlotVisualizerUpdateData(List<PlotVisualizerUpdateDataEntry> entries) {
        this.entries = entries;

        // Compute min/max y-axis values.
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (PlotVisualizerUpdateDataEntry entry: entries) {
            for (double value: entry.yValues) {
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        }
        minY = min;
        maxY = max;
    }
}
