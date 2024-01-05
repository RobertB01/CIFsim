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

package org.eclipse.escet.cif.eventbased.apps.options;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option to configure whether to add state annotations to the locations of the CIF output file. */
public class AddStateAnnosOption extends BooleanOption {
    /** Name of the option. */
    private static final String NAME = "Add state annotations";

    /** Default value of the option. */
    private static final boolean DEFAULT_VALUE = true;

    /** Description of the option. */
    private static final String DESCRIPTION = fmt(
            "Whether to add state annotations to the locations of the CIF output file. [DEFAULT=%s]",
            DEFAULT_VALUE ? "yes" : "no");

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "add-state-annos";

    /** Name of the option value. */
    private static final String CMD_VALUE = "BOOL";

    /** Whether to display the option in the option dialog. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Description in the option dialog. */
    private static final String OPT_DIALOG_DESCR = "Add state annotations to the locations of the CIF output file.";

    /** Text next to the check box in the option dialog. */
    private static final String OPT_DIALOG_CHECKBOX_TEXT = "Add state annotations";

    /** Constructor of the {@link AddStateAnnosOption} class. */
    public AddStateAnnosOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, SHOW_IN_DIALOG, OPT_DIALOG_DESCR,
                OPT_DIALOG_CHECKBOX_TEXT);
    }

    /**
     * Get whether state annotations are enabled.
     *
     * @return {@code true} if state annotations are enabled, {@code false} otherwise.
     */
    public static boolean getStateAnnotationsEnabled() {
        return Options.get(AddStateAnnosOption.class);
    }
}
