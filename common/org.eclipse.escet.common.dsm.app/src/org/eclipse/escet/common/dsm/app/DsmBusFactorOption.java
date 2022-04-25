//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.app.framework.options.DoubleOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option to specify the factor for detecting a bus in a DSM. */
public class DsmBusFactorOption extends DoubleOption {
    /** Name of the option. */
    private static final String NAME = "Bus factor";

    /** Description of the option. */
    private static final String DESCRIPTION = "Bus factor for the bus detection algorithms. [DEFAULT=2].";

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "bus-factor";

    /** Name of the optional value. */
    private static final String CMD_VALUE = NAME;

    /** Default value of the option. */
    private static final double DEFAULT_VALUE = 2;

    /** Minimum value of the option. */
    private static final Double MIN_VALUE = 0.0;

    /** Maximum value of the option. */
    private static final Double MAX_VALUE = 10000.0;

    /** Whether to show the option in a dialogue box. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Description of the option in the dialogue box. */
    private static final String DIALOG_DESCR = DESCRIPTION;

    /** Label to use as prefix in the dialog box. */
    private static final String DIALOG_OPTION_TEXT = CMD_VALUE + ":";

    /** Constructor of the {@link DsmBusFactorOption} class. */
    public DsmBusFactorOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, MIN_VALUE, MAX_VALUE, SHOW_IN_DIALOG,
                DIALOG_DESCR, DIALOG_OPTION_TEXT);
    }

    /**
     * Retrieve the value of the bus factor option.
     *
     * @return The value of the factor for detecting a bus in a DSM.
     */
    public static double getBusFactor() {
        return Options.get(DsmBusFactorOption.class);
    }
}
