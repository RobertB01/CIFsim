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

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/** Enable edge minimization option. */
public class EnableEdgeMinimizationOption extends BooleanOption {
    /** Message to indicate the option is unsupported. */
    private static final String UNSUPPORTED_MESSAGE = "This option is no longer supported. "
            + "It will be removed in a future version of the tool. "
            + "Use the 'Remove duplicate transitions' option instead.";

    /** Name of the option. */
    private static final String NAME = "Enable edge minimization";

    /** Default value of the option. */
    private static final boolean DEFAULT_VALUE = true;

    /** Description of the option. */
    private static final String DESCRIPTION = "Enable edge minimization to remove duplicate edges. "
            + UNSUPPORTED_MESSAGE;

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "edge-minimization";

    /** Name of the option value. */
    private static final String CMD_VALUE = "BOOL";

    /** Whether to display the option in the option dialog. */
    private static final boolean SHOW_IN_DIALOG = false;

    /** Description in the option dialog. */
    private static final String OPT_DIALOG_DESCR = null;

    /** Text next to the check box in the option dialog. */
    private static final String OPT_DIALOG_CHECKBOX_TEXT = null;

    /** Constructor of the {@link EnableEdgeMinimizationOption} class. */
    public EnableEdgeMinimizationOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, SHOW_IN_DIALOG, OPT_DIALOG_DESCR,
                OPT_DIALOG_CHECKBOX_TEXT);
    }

    @Override
    public Boolean parseValue(String optName, String value) {
        throw new InvalidOptionException("The '" + NAME + "' option is used. " + UNSUPPORTED_MESSAGE);
    }
}
