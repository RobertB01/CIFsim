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

/** DSM option to specify the evaporation factor. */
public class DsmEvaporationOption extends DoubleOption {
    /** Name of the option. */
    private static final String NAME = "Evaporation factor";

    /** Description of the option. */
    private static final String DESCRIPTION = "Evaporation factor to increase number of clusters. "
            + "Usually between 1.5 and 3.5. [DEFAULT=2.0].";

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "evaporation";

    /** Name of the optional value. */
    private static final String CMD_VALUE = NAME;

    /** Default value of the option. */
    private static final double DEFAULT_VALUE = 2.0;

    /** Minimum value of the option. */
    private static final Double MIN_VALUE = 1.0;

    /** Maximum value of the option. */
    private static final Double MAX_VALUE = 10.0;

    /** Whether to show the option in a dialog box. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Description of the option in the dialog box. */
    private static final String DIALOG_DESCR = "Evaporation factor.";

    /** Label to use as prefix in the dialog box. */
    private static final String DIALOG_OPTION_TEXT = NAME + ":";

    /** Constructor of the {@link DsmEvaporationOption} class. */
    public DsmEvaporationOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, MIN_VALUE, MAX_VALUE, SHOW_IN_DIALOG,
                DIALOG_DESCR, DIALOG_OPTION_TEXT);
    }

    /**
     * Retrieve the value of the evaporation factor option.
     *
     * @return The specified evaporation factor.
     */
    public static double getEvaporationFactor() {
        return Options.get(DsmEvaporationOption.class);
    }
}
