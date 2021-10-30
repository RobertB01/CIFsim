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

package org.eclipse.escet.common.raildiagrams.config;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Configuration file option. */
public class ConfigFileOption extends StringOption {
    /** Name of the option. */
    private static final String NAME = "Config file";

    /** Short option name. */
    private static final Character CMD_SHORT = 'c';

    /** Long option name. */
    private static final String CMD_LONG = "config";

    /** Name of the optional value. */
    private static final String CMD_VALUE = "CFG";

    /** Default value of the option. */
    private static final String DEFAULT_VALUE = null;

    /** Whether to return {@code null} if the option value is empty. */
    private static final boolean EMPTY_AS_NULL = true;

    /** Description of the option. */
    private static final String DESCRIPTION = "Specifies the absolute or relative local file system path of the "
            + "diagram generator configuration file. Default configuration is used if no configuration file is "
            + "provided.";

    /** Whether to show the option in a dialogue box. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Dialogue description (since {@link #SHOW_IN_DIALOG} holds). */
    private static final String OPT_DIALOG_DESCR = "The absolute or relative local file system path of the diagram "
            + "generator configuration file. Default configuration is used if no configuration file is provided.";

    /** Label to use as prefix in the dialog box (since {@link #SHOW_IN_DIALOG} holds). */
    private static final String OPT_DIALOG_LABEL_TEXT = "File path:";

    /** Constructor of the {@link ConfigFileOption} class. */
    public ConfigFileOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, EMPTY_AS_NULL, SHOW_IN_DIALOG,
                OPT_DIALOG_DESCR, OPT_DIALOG_LABEL_TEXT);
    }

    /**
     * Returns the absolute or relative local file system path to the configuration file, or {@code null} if not
     * specified.
     *
     * @return The absolute or relative local file system path to the configuration file, or {@code null}.
     */
    public static String getFilePath() {
        return Options.get(ConfigFileOption.class);
    }
}
