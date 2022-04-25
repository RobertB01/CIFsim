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

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** DSM option to specify the number of visited nodes in an iteration. */
public class DsmStepCountOption extends IntegerOption {
    /** Name of the option. */
    private static final String NAME = "Step count";

    /** Description of the option. */
    private static final String DESCRIPTION = "Number of visited nodes in an iteration."
            + "Between 1 and 4, is rarely modified. [DEFAULT=2].";

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "stepcount";

    /** Name of the optional value. */
    private static final String CMD_VALUE = NAME;

    /** Default value of the option. */
    private static final int DEFAULT_VALUE = 2;

    /** Minimum value of the option. */
    private static final int MIN_VALUE = 1;

    /** Maximum value of the option. */
    private static final int MAX_VALUE = 4;

    /** Increment of the value in the dialog box. */
    private static final int PAGE_INC_VALUE = 1;

    /** Whether to show the option in a dialogue box. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Description of the option in the dialogue box. */
    private static final String DIALOG_DESCR = DESCRIPTION;

    /** Label to use as prefix in the dialog box. */
    private static final String DIALOG_OPTION_TEXT = CMD_VALUE + ":";

    /** Constructor of the {@link DsmStepCountOption} class. */
    public DsmStepCountOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, MIN_VALUE, MAX_VALUE, PAGE_INC_VALUE,
                SHOW_IN_DIALOG, DIALOG_DESCR, DIALOG_OPTION_TEXT);
    }

    /**
     * Retrieve the value of the step count option.
     *
     * @return The specified number of visited nodes in an iteration.
     */
    public static int getStepCountValue() {
        return Options.get(DsmStepCountOption.class);
    }
}
