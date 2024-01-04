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

/** ODE solver integrator minimum step size option. */
public class IntegratorMinStepOption extends DoubleOption {
    /** Constructor for the {@link IntegratorMinStepOption} class. */
    public IntegratorMinStepOption() {
        super("ODE solver integrator minimum step size",
                "The minimum step size to use for the ODE solver integrator. The last step can be smaller than this "
                        + "value. [DEFAULT=1e-15]",
                null, "solver-int-minstep", "MSTEP", 1e-15, 0.0, null, true,
                "The minimum step size to use for the ODE solver integrator. The last step can be smaller than this "
                        + "value.",
                "Value:");
    }

    /**
     * Returns the ODE solver integrator minimum step size.
     *
     * @return The ODE solver integrator minimum step size.
     */
    public static double getIntMinStep() {
        return Options.get(IntegratorMinStepOption.class);
    }

    @Override
    public void verifyValue(Double value) {
        checkValue(value > 0, value + " <= 0");
    }
}
