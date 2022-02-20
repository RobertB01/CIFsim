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

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** ODE solver integrator number of steps option. */
public class IntegratorNumStepsOption extends IntegerOption {
    /** Constructor for the {@link IntegratorNumStepsOption} class. */
    public IntegratorNumStepsOption() {
        super("ODE solver integrator number of steps",
                "The number of steps to use for the Adams-Bashforth and Adams-Moulton ODE solver integrators. "
                        + "Must be greater or equal to two. [DEFAULT=5]",
                null, "solver-int-nsteps", "NSTEPS", 5, 2, 64, 1, true,
                "The number of steps to use for the Adams-Bashforth and Adams-Moulton ODE solver integrators.",
                "Value:");
    }

    /**
     * Returns the ODE solver integrator number of steps.
     *
     * @return The ODE solver integrator number of steps.
     */
    public static int getIntNumSteps() {
        return Options.get(IntegratorNumStepsOption.class);
    }
}
