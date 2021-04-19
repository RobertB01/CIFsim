//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

public class ConfigFileOption extends StringOption {
    /** Name of the option. */
    private static final String NAME = "Config filename";

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "config";

    /** Name of the optional value. */
    private static final String CMD_VALUE = "CFG";

    /** Default value of the option. */
    private static final String DEFAULT_VALUE = "";

    /** Whether to return {@code null} if the option value is empty. */
    private static final boolean EMPTY_AS_NULL = false;

    /** Description of the option. */
    private static final String DESCRIPTION = "Name of the diagram generator configuration file. "
            + "[DEFAULT=not specified]";

    /** Whether to show the option in a dialogue box. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Dialogue description (since {@link #SHOW_IN_DIALOG} holds). */
    private static final String OPT_DIALOG_DESCR = DESCRIPTION;

    /** Label to use as prefix in the dialog box (since {@link #SHOW_IN_DIALOG} holds). */
    private static final String OPT_DIALOG_LABEL_TEXT = "Config file:";

    public ConfigFileOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, EMPTY_AS_NULL, SHOW_IN_DIALOG,
                OPT_DIALOG_DESCR, OPT_DIALOG_LABEL_TEXT);
    }

    /**
     * Get the name of the configuration file that is specified by the user.
     *
     * @return Name of the config file to use in the application.
     */
    public static String getConfigFilename() {
        return Options.get(ConfigFileOption.class);
    }
}
