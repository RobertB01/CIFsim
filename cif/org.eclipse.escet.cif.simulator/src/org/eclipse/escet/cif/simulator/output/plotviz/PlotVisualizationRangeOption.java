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

import org.eclipse.escet.common.app.framework.options.DoubleOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Plot visualization range option. */
public class PlotVisualizationRangeOption extends DoubleOption {
    /** Constructor for the {@link PlotVisualizationRangeOption} class. */
    public PlotVisualizationRangeOption() {
        super("Plot visualization range",
                "The positive length of the range of time values to display for plot visualization. May be \"inf\" "
                        + "for infinite. [DEFAULT=50]",
                null, "plotviz-range", "RANGE", 50.0, Double.MIN_VALUE, Double.MAX_VALUE, true,
                "The positive length of the range of time values to display for plot visualization.", "Range length:",
                true, 50.0, "inf", "Infinite range", "Finite range");
    }

    /**
     * Returns the length of the plot visualization range, or {@code null} for infinite.
     *
     * @return The length of the plot visualization range, or {@code null}.
     */
    public static Double getRange() {
        return Options.get(PlotVisualizationRangeOption.class);
    }
}
