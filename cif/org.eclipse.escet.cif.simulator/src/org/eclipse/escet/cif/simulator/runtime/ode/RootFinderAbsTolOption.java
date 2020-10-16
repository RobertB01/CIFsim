//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

/** ODE solver root finder absolute tolerance option. */
public class RootFinderAbsTolOption extends DoubleOption {
    /** Constructor for the {@link RootFinderAbsTolOption} class. */
    public RootFinderAbsTolOption() {
        super("ODE solver root finder absolute tolerance",
                "The absolute tolerance (maximum allowed absolute error) to use for the ODE solver root finder. "
                        + "[DEFAULT=1e-15]",
                null, "solver-root-atol", "ROOTATOL", 1e-15, null, null, true,
                "The absolute tolerance (maximum allowed absolute error) to use for the ODE solver root finder.",
                "Value:");
    }

    /**
     * Returns the ODE solver root finder absolute tolerance.
     *
     * @return The ODE solver root finder absolute tolerance.
     */
    public static double getRootAbsTol() {
        return Options.get(RootFinderAbsTolOption.class);
    }
}
