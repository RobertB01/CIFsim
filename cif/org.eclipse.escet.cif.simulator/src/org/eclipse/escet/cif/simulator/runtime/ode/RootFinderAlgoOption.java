//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** ODE solver root finding algorithm option. */
public class RootFinderAlgoOption extends EnumOption<RootFinderAlgo> {
    /** Constructor for the {@link RootFinderAlgoOption} class. */
    public RootFinderAlgoOption() {
        super("ODE solver root finding algorithm",
                "The root finding algorithm to use to detect state events for the ODE solver. Specify "
                        + "\"regula-falsi\" for the Regula Falsi (False position) method, \"illinois\" for the "
                        + "Illinois method, or \"pegasus\" (default) for the Pegasus method.",
                null, "solver-root-algo", "ROOTALGO", RootFinderAlgo.PEGASUS, true,
                "The root finding algorithm to use to detect state events for the ODE solver.");
    }

    /**
     * Returns the ODE solver root finding algorithm to use.
     *
     * @return The ODE solver root finding algorithm to use.
     */
    public static RootFinderAlgo getRootAlgo() {
        return Options.get(RootFinderAlgoOption.class);
    }

    @Override
    protected String getDialogText(RootFinderAlgo algorithm) {
        return algorithm.name;
    }
}
