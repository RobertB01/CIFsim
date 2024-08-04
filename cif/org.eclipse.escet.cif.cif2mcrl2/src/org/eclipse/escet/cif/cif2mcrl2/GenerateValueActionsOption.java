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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option to specify the variables that need to have a 'value' action. */
public class GenerateValueActionsOption extends StringOption {
    /** Name of the option. */
    private static final String NAME = "Generate 'value' actions";

    /** Description in the option dialog. */
    private static final String OPT_DIALOG_DESCR = "Option to specify which variables and automata should get a "
            + "'value' action in the generated mCRL2 model. Specify a comma-separated list of discrete variable and "
            + "automata name patterns. Only automata with at least two locations may be specified. The \"*\" character "
            + "can be used as wildcard in patterns and indicates zero or more characters. Prefixing a pattern with a "
            + "\"+\" adds the variables and automata matching the pattern, while a \"-\" prefix removes the variables "
            + "and automata matching the pattern. If neither a \"+\" nor a \"-\" prefix is given, \"+\" (adding) is "
            + "assumed. The first pattern adds to or removes from an empty list of variables variables and automata.";

    /** Default value of the option. */
    private static final String DEFAULT_VALUE = "+*";

    /** Description of the option. */
    private static final String DESCRIPTION = fmt("%s [DEFAULT=%s]", OPT_DIALOG_DESCR, DEFAULT_VALUE);

    /** Whether to return {@code null} as option value if it was left empty. */
    private static final boolean EMPTY_AS_NULL = false;

    /** Short option name. */
    private static final Character CMD_SHORT = 'r';

    /** Long option name. */
    private static final String CMD_LONG = "read-values";

    /** Name of the option value. */
    private static final String CMD_VALUE = "PATTERNS";

    /** Whether to display the option in the option dialog. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Text next to the label in the option dialog. */
    private static final String OPT_DIALOG_LABEL_TEXT = "Patterns:";

    /** Constructor of the {@link GenerateValueActionsOption} class. */
    public GenerateValueActionsOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, EMPTY_AS_NULL, SHOW_IN_DIALOG,
                OPT_DIALOG_DESCR, OPT_DIALOG_LABEL_TEXT);
    }

    /**
     * Returns the 'value' actions filter patterns.
     *
     * @return The patterns.
     */
    public static String getValue() {
        return Options.get(GenerateValueActionsOption.class);
    }
}
