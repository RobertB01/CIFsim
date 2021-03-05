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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Maximum time point tolerance option. */
public class MaxTimePointTolOption extends IntegerOption {
    /** Constructor for the {@link MaxTimePointTolOption} class. */
    public MaxTimePointTolOption() {
        super("Maximum time point tolerance",
                "The maximum tolerance of time points in ulps, for the retrieval of values from trajectories. "
                        + "Value must be in the range [1 .. 2^30]. [DEFAULT=2^10=1024]",
                null, "max-time-point-tol", "TPTOL", 1024, 1, 1 << 30, 1, true,
                "The maximum tolerance of time points in ulps, for the retrieval of values from trajectories.",
                "Tolerance:");
    }

    /**
     * Returns the maximum tolerance of time points in ulps, for the retrieval of values from trajectories.
     *
     * @return The maximum tolerance in ulps.
     */
    public static Integer getMaxTimePointTol() {
        return Options.get(MaxTimePointTolOption.class);
    }
}
