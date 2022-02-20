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

/** ODE solver integrator absolute tolerance option. */
public class IntegratorAbsTolOption extends DoubleOption {
    /** Constructor for the {@link IntegratorAbsTolOption} class. */
    public IntegratorAbsTolOption() {
        super("ODE solver integrator absolute tolerance",
                "The absolute tolerance (maximum allowed absolute error) to use for the ODE solver integrator. "
                        + "[DEFAULT=1e-7]",
                null, "solver-int-atol", "INTATOL", 1e-7, null, null, true,
                "The absolute tolerance (maximum allowed absolute error) to use for the ODE solver integrator.",
                "Value:");
    }

    /**
     * Returns the ODE solver integrator absolute tolerance.
     *
     * @return The ODE solver integrator absolute tolerance.
     */
    public static double getIntAbsTol() {
        return Options.get(IntegratorAbsTolOption.class);
    }
}
