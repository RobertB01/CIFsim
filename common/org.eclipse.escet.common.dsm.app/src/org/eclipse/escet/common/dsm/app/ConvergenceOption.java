//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2024 Contributors to the Eclipse Foundation
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

/** DSM Application option to specify convergence tolerance. */
public class ConvergenceOption extends DoubleOption {
    /** Name of the option. */
    private static final String NAME = "Convergence limit";

    /** Description of the option. */
    private static final String DESCRIPTION = "Allowed remaining numerical error for convergence of the algorithm. "
            + "Between 0 and 1. [DEFAULT=1e-4].";

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "convergence";

    /** Name of the optional value. */
    private static final String CMD_VALUE = "LIMIT";

    /** Default value of the option. */
    private static final double DEFAULT_VALUE = 1e-4;

    /** Minimum value of the option. */
    private static final double MIN_VALUE = 0.0;

    /** Maximum value of the option. */
    private static final double MAX_VALUE = 1.0;

    /** Whether to show the option in a dialog box. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Description of the option in the dialog box. */
    private static final String DIALOG_DESCR = "Allowed remaining numerical error for convergence of the algorithm.";

    /** Label to use as prefix in the dialog box. */
    private static final String DIALOG_OPTION_TEXT = NAME + ":";

    /** Constructor of the {@link ConvergenceOption} class. */
    public ConvergenceOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, MIN_VALUE, MAX_VALUE, SHOW_IN_DIALOG,
                DIALOG_DESCR, DIALOG_OPTION_TEXT);
    }

    /**
     * Retrieve the value of the convergence value option.
     *
     * @return The convergence tolerance.
     */
    public static double getConvergenceValue() {
        return Options.get(ConvergenceOption.class);
    }
}
