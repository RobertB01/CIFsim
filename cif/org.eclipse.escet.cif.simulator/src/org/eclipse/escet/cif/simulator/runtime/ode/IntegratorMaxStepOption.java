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

package org.eclipse.escet.cif.simulator.runtime.ode;

import org.eclipse.escet.common.app.framework.options.DoubleOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** ODE solver integrator maximum step size option. */
public class IntegratorMaxStepOption extends DoubleOption {
    /** Constructor for the {@link IntegratorMaxStepOption} class. */
    public IntegratorMaxStepOption() {
        super("ODE solver integrator maximum step size",
                "The maximum step size to use for the ODE solver integrator. [DEFAULT=0.5]", null, "solver-int-maxstep",
                "MAXSTEP", 0.5, 0.0, null, true, "The maximum step size to use for the ODE solver integrator.",
                "Value:");
    }

    /**
     * Returns the ODE solver integrator maximum step size.
     *
     * @return The ODE solver integrator maximum step size.
     */
    public static double getIntMaxStep() {
        return Options.get(IntegratorMaxStepOption.class);
    }

    @Override
    public void verifyValue(Double value) {
        checkValue(value > 0, value + " <= 0");
    }
}
