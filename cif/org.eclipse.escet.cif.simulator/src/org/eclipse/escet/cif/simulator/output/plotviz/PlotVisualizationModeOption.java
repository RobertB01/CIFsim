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

import static org.eclipse.escet.cif.simulator.output.plotviz.PlotVisualizationMode.AUTO;
import static org.eclipse.escet.cif.simulator.output.plotviz.PlotVisualizationMode.LIVE;
import static org.eclipse.escet.cif.simulator.output.plotviz.PlotVisualizationMode.POSTPONED;

import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.cif.simulator.options.InputModeOption;
import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Plot visualization mode option. */
public class PlotVisualizationModeOption extends EnumOption<PlotVisualizationMode> {
    /** Constructor for the {@link PlotVisualizationModeOption} class. */
    public PlotVisualizationModeOption() {
        super("Plot visualization mode",
                "The plot visualization mode indicates when the plot should appear. Specify \"live\" for live "
                        + "plotting during the simulation, \"postponed\" to postpone plotting until after the "
                        + "simulation, or \"auto\" (default) for postponed plotting in case of interactive "
                        + "simulation without real-time mode, and live plotting otherwise.",
                null, "plotviz-mode", "MODE", AUTO, true,
                "The plot visualization mode indicates when the plot should appear. The automatic choice uses "
                        + "postponed plotting in case of interactive simulation without real-time mode, and live "
                        + "plotting otherwise.");
    }

    /**
     * Returns the plot visualization mode.
     *
     * @return The plot visualization mode.
     */
    public static PlotVisualizationMode getPlotVizMode() {
        // Get specified mode.
        PlotVisualizationMode rslt = Options.get(PlotVisualizationModeOption.class);
        if (rslt != AUTO) {
            return rslt;
        }

        // Handle automatic mode.
        if (FrameRateOption.isRealTimeEnabled()) {
            return LIVE;
        }
        if (InputModeOption.isPurelyInteractive()) {
            return LIVE;
        }
        return POSTPONED;
    }

    @Override
    protected String getDialogText(PlotVisualizationMode value) {
        switch (value) {
            case AUTO:
                return "Automatically choose between live and postponed";

            case LIVE:
                return "Live plotting during simulation";

            case POSTPONED:
                return "Postpone plotting until after the simulation";
        }
        throw new RuntimeException("Unknown mode: " + value);
    }
}
