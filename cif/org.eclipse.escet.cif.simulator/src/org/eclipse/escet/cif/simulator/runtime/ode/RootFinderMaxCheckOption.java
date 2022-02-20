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

/** ODE solver root finder maximum check interval option. */
public class RootFinderMaxCheckOption extends DoubleOption {
    /** Constructor for the {@link RootFinderMaxCheckOption} class. */
    public RootFinderMaxCheckOption() {
        super("ODE solver root finder maximum check interval",
                "The maximum check interval to use for the ODE solver root finders. [DEFAULT=0.5]", null,
                "solver-root-maxchk", "MAXCHK", 0.5, 0.0, null, true,
                "The maximum check interval to use for the ODE solver root finders.", "Value:");
    }

    /**
     * Returns the ODE solver root finder maximum check interval.
     *
     * @return The ODE solver root finder maximum check interval.
     */
    public static double getRootMaxChk() {
        return Options.get(RootFinderMaxCheckOption.class);
    }

    @Override
    public void verifyValue(Double value) {
        checkValue(value > 0, value + " <= 0");
    }
}
