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

package org.eclipse.escet.cif.simulator.runtime.ode;

import org.eclipse.escet.common.app.framework.options.DoubleOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** ODE solver integrator relative tolerance option. */
public class IntegratorRelTolOption extends DoubleOption {
    /** Constructor for the {@link IntegratorRelTolOption} class. */
    public IntegratorRelTolOption() {
        super("ODE solver integrator relative tolerance",
                "The relative tolerance (maximum allowed relative error) to use for the ODE solver integrator. "
                        + "[DEFAULT=1e-7]",
                null, "solver-int-rtol", "INTRTOL", 1e-7, null, null, true,
                "The relative tolerance (maximum allowed relative error) to use for the ODE solver integrator.",
                "Value:");
    }

    /**
     * Returns the ODE solver integrator relative tolerance.
     *
     * @return The ODE solver integrator relative tolerance.
     */
    public static double getIntRelTol() {
        return Options.get(IntegratorRelTolOption.class);
    }
}
