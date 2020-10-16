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

package org.eclipse.escet.chi.simulator.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option class to allow suppressing the info-line at the end of a simulation. */
public class OutputInfoOption extends BooleanOption {
    /** Constant for the dialogue description. */
    static final String DIALOGUE_DESC = "Output information about the simulation exit state and used seed. "
            + "Model simulations also report the endtime.";

    /** Constructor for the {@link OutputInfoOption} class. */
    public OutputInfoOption() {
        super("Output simulation info", // name
                "Whether or not to output the endtime and the used seed as final line in the simulation output "
                        + "(yes/no). [default=yes]", // desc
                'p', // cmd short
                "output-info", // cmd long
                "BOOL", // cmd value
                true, // default value
                true, // show in dialog
                DIALOGUE_DESC, // opt dialog descr
                "Output info at the end of the simulation."); // checkbox text
    }

    /**
     * Get the option value.
     *
     * @return The option value as stated by the user.
     */
    public static boolean getOutputInfo() {
        return Options.get(OutputInfoOption.class);
    }
}
