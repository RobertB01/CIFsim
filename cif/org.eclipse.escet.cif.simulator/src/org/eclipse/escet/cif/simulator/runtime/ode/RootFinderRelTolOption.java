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

/** ODE solver root finder relative tolerance option. */
public class RootFinderRelTolOption extends DoubleOption {
    /** Constructor for the {@link RootFinderRelTolOption} class. */
    public RootFinderRelTolOption() {
        super("ODE solver root finder relative tolerance",
                "The relative tolerance (maximum allowed relative error) to use for the ODE solver root finder. "
                        + "[DEFAULT=1e-15]",
                null, "solver-root-rtol", "ROOTRTOL", 1e-15, null, null, true,
                "The relative tolerance (maximum allowed relative error) to use for the ODE solver root finder.",
                "Value:");
    }

    /**
     * Returns the ODE solver root finder relative tolerance.
     *
     * @return The ODE solver root finder relative tolerance.
     */
    public static double getRootRelTol() {
        return Options.get(RootFinderRelTolOption.class);
    }
}
