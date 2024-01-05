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

/** Plot visualizer x-axis display range. */
public class PlotVisualizerRange {
    /** The lower bound of the range. */
    public final double lower;

    /** The upper bound of the range. */
    public final double upper;

    /**
     * Constructor for the {@link PlotVisualizerRange} class.
     *
     * @param lower The lower bound of the range.
     * @param upper The upper bound of the range.
     */
    public PlotVisualizerRange(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }
}
