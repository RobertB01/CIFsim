//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2021 Contributors to the Eclipse Foundation
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

/** Option class to obtain the value of the inflation factor to use in the clustering algorithm. */
public class DsmInflationOption extends DoubleOption {
    /** Name of the option. */
    private static final String NAME = "Inflation factor";

    /** Description of the option. */
    private static final String DESCRIPTION = "Inflation factor to increase gap between low and high values. "
            + "Usually between 1.5 and 3.5. [DEFAULT=2.0].";

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "inflation";

    /** Name of the optional value. */
    private static final String CMD_VALUE = NAME;

    /** Default value of the option. */
    private static final double DEFAULT_VALUE = 2.0;

    /** Minimum value of the option. */
    private static final Double MIN_VALUE = 1.0;

    /** Maximum value of the option. */
    private static final Double MAX_VALUE = 4.0;

    /** Whether to show the option in a dialogue box. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Description of the option in the dialogue box. */
    private static final String DIALOG_DESCR = DESCRIPTION;

    /** Label to use as prefix in the dialog box. */
    private static final String DIALOG_OPTION_TEXT = CMD_VALUE + ":";

    /** Constructor of the {@link DsmInflationOption} class. */
    public DsmInflationOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, MIN_VALUE, MAX_VALUE, SHOW_IN_DIALOG,
                DIALOG_DESCR, DIALOG_OPTION_TEXT);
    }

    /**
     * Retrieve the value of the inflation factor option.
     *
     * @return Specified value of the inflation factor.
     */
    public static double getInflationFactor() {
        return Options.get(DsmInflationOption.class);
    }
}
