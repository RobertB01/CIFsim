//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.app;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option whether to output resulting DSM cluster and bus group structures in the output. */
public class OutputGroupsOption extends BooleanOption {
    /** Name of the option. */
    private static final String NAME = "Output groups";

    /** Description of the option. */
    private static final String DESCRIPTION = "Whether to output bus and cluster node groups (BOOL=yes) or not "
            + "(BOOL=no). [DEFAULT=yes].";

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "output-groups";

    /** Name of the option value. */
    private static final String CMD_VALUE = "BOOL";

    /** Default value of the option. */
    private static final boolean DEFAULT_VALUE = true;

    /** Whether to show the option in a dialog box. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Description of the option in the dialog box. */
    private static final String DIALOG_DESCR = "Whether to output bus and cluster node groups.";

    /** Label to use as prefix in the dialog box. */
    private static final String DIALOG_OPTION_TEXT = "Output bus and cluster node groups";

    /** Constructor of the {@link OutputGroupsOption} class. */
    public OutputGroupsOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, SHOW_IN_DIALOG, DIALOG_DESCR,
                DIALOG_OPTION_TEXT);
    }

    /**
     * Get the option value of the {@link OutputGroupsOption}.
     *
     * @return Value of the 'output-groups' option.
     */
    public static boolean getOutputGroupsOptionValue() {
        return Options.get(OutputGroupsOption.class);
    }
}
