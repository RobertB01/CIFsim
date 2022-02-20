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

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** ODE solver integrator algorithm option. */
public class IntegratorAlgoOption extends EnumOption<IntegratorAlgo> {
    /** Constructor for the {@link IntegratorAlgoOption} class. */
    public IntegratorAlgoOption() {
        super("ODE solver integrator algorithm",
                "The integrator algorithm to use for the ODE solver. Specify \"higham-hall\" for the Higham and Hall "
                        + "5(4) integrator, \"dormand-prince-54\" for the Dormand-Prince 5(4) integrator, "
                        + "\"dormand-prince-853\" for the Dormand-Prince 8(5,3) integrator (default), "
                        + "\"gragg-bulirsch-stoer\" for the Gragg-Bulirsch-Stoer integrator, \"adams-bashforth\" for "
                        + "the Adams-Bashforth integrator, or \"adams-moulton\" for the Adams-Moulton integrator. "
                        + "The last two are experimental.",
                null, "solver-int-algo", "INTALGO", IntegratorAlgo.DORMAND_PRINCE_853, true,
                "The integrator algorithm to use for the ODE solver.");
    }

    /**
     * Returns the ODE solver integrator algorithm to use.
     *
     * @return The ODE solver integrator algorithm to use.
     */
    public static IntegratorAlgo getIntAlgo() {
        return Options.get(IntegratorAlgoOption.class);
    }

    @Override
    protected String getDialogText(IntegratorAlgo algorithm) {
        return algorithm.name;
    }
}
