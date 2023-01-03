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

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Plot visualization option. */
public class PlotVisualizationOption extends BooleanOption {
    /** Constructor for the {@link PlotVisualizationOption} class. */
    public PlotVisualizationOption() {
        super("Plot visualization",
                "Whether to graphically plot the values of variables as time progresses, during simulation "
                        + "(BOOL=yes), or not (BOOL=no). [DEFAULT=no]",
                null, "plotviz", "BOOL", false, true, "Enable this option to graphically plot the values of "
                        + "variables as time progresses, during simulation.",
                "Use graphical plots");
    }

    /**
     * Should plot visualization be used?
     *
     * @return {@code true} if plot visualization should be used, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(PlotVisualizationOption.class);
    }
}
