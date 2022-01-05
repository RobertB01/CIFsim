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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.DoubleOption;
import org.eclipse.escet.common.app.framework.options.Options;

/**
 * Maximum delay option.
 *
 * <p>
 * This option can be used to force new states are never more than a certain amount of time apart from each other. Note
 * that this option does not (necessarily) result in states with multiples of the maximum delay value as time values.
 * </p>
 *
 * <p>
 * Setting this option to zero, disallows passage of time.
 * </p>
 *
 * <p>
 * If this option sets the maximum to infinite, and the simulation {@link EndTimeOption end time} is not specified, then
 * a maximum of {@code 100.0} is used implicitly.
 * </p>
 */
public class MaxDelayOption extends DoubleOption {
    /** Constructor for the {@link MaxDelayOption} class. */
    public MaxDelayOption() {
        super("Maximum delay",
                "The maximum delay for a single time transition. Specify \"inf\" (default) to allow infinite time "
                        + "transitions. Specify a positive numeric value to allow time transitions with a maximum "
                        + "delay amount up to and including that value. Specify \"0\" to disallow time passage.",
                null, "max-delay", "MAXDELAY", null, 0.0, null, true,
                "The maximum delay for a single time transition. Finite positive numeric values allow for time "
                        + "transitions with a maximum delay amount up to and including that value. A finite maximum "
                        + "delay of \"0\" disallows time passage.",
                "Maximum delay:", true, 10.0, "inf", "Infinite maximum delay", "Finite maximum delay");
    }

    /**
     * Returns the maximum delay for a single time transition, or {@code null} for infinite. If {@code 0.0}, then time
     * passage is disallowed.
     *
     * @return The maximum delay for a single time transition, or {@code null}.
     */
    public static Double getMaxDelay() {
        return Options.get(MaxDelayOption.class);
    }
}
