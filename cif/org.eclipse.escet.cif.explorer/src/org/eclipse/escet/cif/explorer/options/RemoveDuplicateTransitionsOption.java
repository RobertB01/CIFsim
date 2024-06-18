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

package org.eclipse.escet.cif.explorer.options;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Remove duplicate transitions option. */
public class RemoveDuplicateTransitionsOption extends BooleanOption {
    /** Name of the option. */
    private static final String NAME = "Remove duplicate transitions";

    /** Description in the option dialog. */
    private static final String OPT_DIALOG_DESCR = "Remove duplicate transitions from the state space.";

    /** Default value of the option. */
    private static final boolean DEFAULT_VALUE = true;

    /** Description of the option. */
    private static final String DESCRIPTION = fmt("%s [DEFAULT=%s]", OPT_DIALOG_DESCR, DEFAULT_VALUE ? "yes" : "no");

    /** Short option name. */
    private static final Character CMD_SHORT = 'z';

    /** Long option name. */
    private static final String CMD_LONG = "remove-dupl-trans";

    /** Name of the option value. */
    private static final String CMD_VALUE = "BOOL";

    /** Whether to display the option in the option dialog. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Text next to the check box in the option dialog. */
    private static final String OPT_DIALOG_CHECKBOX_TEXT = "Remove duplicate transitions";

    /** Constructor of the {@link RemoveDuplicateTransitionsOption} class. */
    public RemoveDuplicateTransitionsOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, SHOW_IN_DIALOG, OPT_DIALOG_DESCR,
                OPT_DIALOG_CHECKBOX_TEXT);
    }

    /**
     * Is removing of duplicate transitions enabled?
     *
     * @return {@code true} if removing duplicate transitions is enabled, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(RemoveDuplicateTransitionsOption.class);
    }
}
