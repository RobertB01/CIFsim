//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option to generate a 'marked' action. */
public class GenerateMarkedActionOption extends BooleanOption {
    /** Name of the option. */
    private static final String NAME = "Generate 'marked' action";

    /** Default value of the option. */
    private static final boolean DEFAULT_VALUE = true;

    /** Short option name. */
    private static final Character CMD_SHORT = 'a';

    /** Long option name. */
    private static final String CMD_LONG = "marked-action";

    /** Name of the option value. */
    private static final String CMD_VALUE = "BOOL";

    /** Whether to display the option in the option dialog. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Description in the option dialog. */
    private static final String OPT_DIALOG_DESCR = "Whether to generate a 'marked' action in the generated mCRL2 model.";

    /** Description of the option. */
    private static final String CMD_LINE_DESCRIPTION = OPT_DIALOG_DESCR
            + fmt(" [DEFAULT=%s]", DEFAULT_VALUE ? "yes" : "no");

    /** Text next to the check box in the option dialog. */
    private static final String OPT_DIALOG_CHECKBOX_TEXT = "Generate 'marked' action";

    /** Constructor of the {@link GenerateMarkedActionOption} class. */
    public GenerateMarkedActionOption() {
        super(NAME, CMD_LINE_DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, SHOW_IN_DIALOG,
                OPT_DIALOG_DESCR, OPT_DIALOG_CHECKBOX_TEXT);
    }

    /**
     * Get whether generating a 'marked' action is enabled.
     *
     * @return {@code true} if generating a 'marked' action is enabled, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(GenerateMarkedActionOption.class);
    }
}
