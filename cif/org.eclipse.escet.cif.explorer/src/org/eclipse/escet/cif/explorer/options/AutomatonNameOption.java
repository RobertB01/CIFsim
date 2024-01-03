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

package org.eclipse.escet.cif.explorer.options;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/** Resulting statespace automaton name option. */
public class AutomatonNameOption extends StringOption {
    /** Constructor of the {@link AutomatonNameOption} class. */
    public AutomatonNameOption() {
        super("Resulting statespace automaton name",
                "The name of the resulting statespace automaton. [DEFAULT=\"statespace\"]", 'n', "name", "NAME", null,
                true, true, "The name of the resulting statespace automaton.", "Name:");
    }

    /**
     * Returns the name of the resulting statespace automaton.
     *
     * @param defaultName The default name to use, if no name is provided.
     * @return The name of the resulting statespace automaton.
     */
    public static String getAutomatonName(String defaultName) {
        // Get name, and use default if not supplied.
        String name = Options.get(AutomatonNameOption.class);
        if (name == null) {
            name = defaultName;
        }

        // Check name.
        if (!CifValidationUtils.isValidIdentifier(name)) {
            String msg = fmt("Resulting statespace automaton name \"%s\" is not a valid CIF identifier.", name);
            throw new InvalidOptionException(msg);
        }
        return name;
    }
}
