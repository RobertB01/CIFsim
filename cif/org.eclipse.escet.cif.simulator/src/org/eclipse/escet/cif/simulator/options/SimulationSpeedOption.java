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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.DoubleOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Simulation speed option. */
public class SimulationSpeedOption extends DoubleOption {
    /** Constructor for the {@link SimulationSpeedOption} class. */
    public SimulationSpeedOption() {
        super("Simulation speed",
                "Controls the simulation speed. Specify any positive real value SPEED to use as scaling factor. "
                        + "A scaling factor of 1.0 interprets model time as seconds, 2.0 is twice as fast (1 unit "
                        + "of model time takes 0.5 seconds), etc. Specify \"auto\" (default) for a simulation speed "
                        + "of 1.0 if a frame rate is given, and for infinite speed (as fast as possible, which means "
                        + "that time transitions take no time), if no frame rate is given.",
                null, "speed", "SPEED", null, 0.0, null, true,
                "Controls the simulation speed. A scaling factor of 1.0 interprets model time as seconds, 2.0 is "
                        + "twice as fast (1 unit of model time takes 0.5 seconds), etc.",
                "Scaling factor:", true, 1.0, "auto", "Automatic (1.0 if frame rate is given, infinite otherwise)",
                "Specific speed (use a scaling factor)");
    }

    /**
     * Returns the simulation speed, or {@code null} for infinite speed.
     *
     * @return The simulation speed, or {@code null} for infinite speed.
     */
    public static Double getSimSpeed() {
        Double rslt = Options.get(SimulationSpeedOption.class);
        if (rslt == null && Options.get(FrameRateOption.class) != null) {
            return 1.0;
        }
        return rslt;
    }

    @Override
    public void verifyValue(Double value) {
        if (value == null) {
            return;
        }
        checkValue(value > 0, value + " <= 0");
    }
}
