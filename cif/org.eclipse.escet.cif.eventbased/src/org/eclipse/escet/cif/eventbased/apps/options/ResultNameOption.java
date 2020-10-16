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

package org.eclipse.escet.cif.eventbased.apps.options;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Result name option. */
public class ResultNameOption extends StringOption {
    /**
     * Constructor for the {@link ResultNameOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public ResultNameOption() {
        super("Result name",
                "The name to use for the resulting automaton. If not specified, a default name is used. "
                        + "Also affects the default output file name.",
                'n', "rslt-name", "RNAME", null, true, true,
                "The name to use for the resulting automaton. "
                        + "If not specified, a default name is used. Also affects the default output file name.",
                "Name:");
    }

    /**
     * Returns the name to use for the resulting automaton. Also affects the default output file name.
     *
     * @param defaultName The default name to use, if no name is provided.
     * @return The name to use for the resulting automaton.
     */
    public static String getRsltName(String defaultName) {
        // Get name, and use default if not supplied.
        String name = Options.get(ResultNameOption.class);
        if (name == null) {
            name = defaultName;
        }

        // Check name.
        if (!CifValidationUtils.isValidIdentifier(name)) {
            String msg = fmt("Result name \"%s\" is not a valid CIF identifier.", name);
            throw new InvalidOptionException(msg);
        }
        return name;
    }
}
