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

/**
 * Simulation end time option.
 *
 * <p>
 * Also affects the {@link MaxDelayOption maximum delay option}.
 * </p>
 */
public class EndTimeOption extends DoubleOption {
    /** Constructor for the {@link EndTimeOption} class. */
    public EndTimeOption() {
        super("Simulation end time",
                "The simulation end time. Specify \"inf\" (default) for infinite end time, or a numeric value to be "
                        + "interpreted as the simulation end time.",
                't', "end-time", "ENDTIME", null, 0.0, null, true, "The simulation end time.", "End time:", true, 10.0,
                "inf", "Infinite end time", "Finite end time");
    }

    /**
     * Returns the simulation end time, or {@code null} if the simulation end time is infinite.
     *
     * @return The simulation end time, or {@code null}.
     */
    public static Double getEndTime() {
        return Options.get(EndTimeOption.class);
    }
}
