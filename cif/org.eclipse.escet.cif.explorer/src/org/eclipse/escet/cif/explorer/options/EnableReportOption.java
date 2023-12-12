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

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Enable report option. */
public class EnableReportOption extends BooleanOption {
    /** Name of the option. */
    private static final String NAME = "Enable report";

    /** Description in the option dialog. */
    private static final String OPT_DIALOG_DESCR = "Enable output of states and edges in a format suitable for "
            + "close inspection.";

    /** Default value of the option. */
    private static final boolean DEFAULT_VALUE = false;

    /** Description of the option. */
    private static final String DESCRIPTION = fmt("%s [DEFAULT=%s]", OPT_DIALOG_DESCR, DEFAULT_VALUE ? "yes" : "no");

    /** Short option name. */
    private static final Character CMD_SHORT = 'e';

    /** Long option name. */
    private static final String CMD_LONG = "enable-report";

    /** Name of the option value. */
    private static final String CMD_VALUE = "BOOL";

    /** Whether to display the option in the option dialog. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Text next to the check box in the option dialog. */
    private static final String OPT_DIALOG_CHECKBOX_TEXT = "Enable reporting";

    /** Constructor of the {@link EnableReportOption} class. */
    public EnableReportOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, SHOW_IN_DIALOG, OPT_DIALOG_DESCR,
                OPT_DIALOG_CHECKBOX_TEXT);
    }

    /**
     * Retrieve the value of the enable report option.
     *
     * @return The value of the enable report option.
     */
    public static boolean getReport() {
        return Options.get(EnableReportOption.class);
    }
}
