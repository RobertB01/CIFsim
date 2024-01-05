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

package org.eclipse.escet.cif.datasynth.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Supervisor name option. */
public class SupervisorNameOption extends StringOption {
    /** Constructor for the {@link SupervisorNameOption} class. */
    public SupervisorNameOption() {
        super("Supervisor name", "The name of the resulting supervisor automaton. [DEFAULT=\"sup\"]", 's', "sup-name",
                "SNAME", null, true, true, "The name of the resulting supervisor automaton.", "Name:");
    }

    /**
     * Returns the name of the resulting supervisor automaton.
     *
     * @param defaultName The default name to use, if no name is provided.
     * @return The name of the resulting supervisor automaton.
     */
    public static String getSupervisorName(String defaultName) {
        String name = Options.get(SupervisorNameOption.class);
        if (name == null) {
            name = defaultName;
        }
        return name;
    }
}
