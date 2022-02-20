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

/** ODE solver root finder maximum iterations option. */
public class RootFinderMaxIterOption extends IntegerOption {
    /** Constructor for the {@link RootFinderMaxIterOption} class. */
    public RootFinderMaxIterOption() {
        super("ODE solver root finder maximum iterations",
                "The maximum number of iterations to use for the ODE solver root finders. Value must be at least "
                        + "one. [DEFAULT=1000]",
                null, "solver-root-maxiter", "MAXITER", 1000, 1, Integer.MAX_VALUE, 1000, true,
                "The maximum number of iterations to use for the ODE solver root-finders.", "Value:");
    }

    /**
     * Returns the ODE solver root finder maximum iterations.
     *
     * @return The ODE solver root-finder maximum iterations.
     */
    public static int getRootMaxIter() {
        return Options.get(RootFinderMaxIterOption.class);
    }
}
