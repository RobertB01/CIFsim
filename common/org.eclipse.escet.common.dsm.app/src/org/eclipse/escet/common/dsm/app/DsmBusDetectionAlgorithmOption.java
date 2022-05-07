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

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.dsm.BusDetectionAlgorithm;

/** DSM option to specify the type of bus to detect. */
public class DsmBusDetectionAlgorithmOption extends EnumOption<BusDetectionAlgorithm> {
    /** Name of the option. */
    private static final String NAME = "Bus detection algorithm";

    /** Description of the option. */
    private static final String DESCRIPTION = "Which bus detection algorithm to use. Use 'no-bus' to skip bus detection, 'fix-point' for using the fixed-point algorithm, and 'top-k' for selecting the K top-nodes. [DEFAULT=no-bus].";

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "bus-algorithm";

    /** Name of the option value. */
    private static final String CMD_VALUE = "BUSALG";

    /** Default value of the option. */
    private static final BusDetectionAlgorithm DEFAULT_VALUE = BusDetectionAlgorithm.NO_BUS;

    /** Whether to show the option in a dialog box. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Description of the option in the dialog box. */
    private static final String DIALOG_DESCR = NAME + ":";

    /** Constructor of the {@link DsmBusDetectionAlgorithmOption} class. */
    public DsmBusDetectionAlgorithmOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, SHOW_IN_DIALOG, DIALOG_DESCR);
    }

    @Override
    protected String getDialogText(BusDetectionAlgorithm alg) {
        switch (alg) {
            case NO_BUS:
                return "No bus";
            case FIX_POINT:
                return "Fixed point";
            case TOP_K:
                return "Top k nodes";
        }
        throw new RuntimeException("Unknown bus detection algorithm: " + alg);
    }

    /**
     * Returns the bus detection algorithm.
     *
     * @return The bus detection algorithm.
     */
    public static BusDetectionAlgorithm getBusAlgorithm() {
        return Options.get(DsmBusDetectionAlgorithmOption.class);
    }
}
