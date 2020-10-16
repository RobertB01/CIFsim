//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Ask to confirm simulator termination option. */
public class AskToTerminateOption extends EnumOption<AskToTerminateMode> {
    /** Constructor for the {@link AskToTerminateOption} class. */
    public AskToTerminateOption() {
        super("Ask to confirm simulator termination",
                "Specify \"on\" to ask the user to press <ENTER> at the console to confirm termination of the "
                        + "simulator, \"off\" to terminate the simulator automatically, or \"auto\" (default) "
                        + "to let the simulator decide whether to ask for confirmation or terminate automatically.",
                null, "ask-terminate", "ATMODE", AskToTerminateMode.AUTO, true,
                "Controls whether the user is asked to press <ENTER> at the console to confirm termination of the "
                        + "simulator, or the simulator terminates automatically.");
    }

    @Override
    protected String getDialogText(AskToTerminateMode value) {
        switch (value) {
            case ON:
                return "On: ask the user to confirm termination";
            case OFF:
                return "Off: terminate automatically";
            case AUTO:
                return "Auto: let the simulator decide";
            default:
                throw new RuntimeException("Unknown mode: " + value);
        }
    }

    /**
     * Returns the 'ask to terminate' mode.
     *
     * @return The 'ask to terminate' mode.
     */
    public static AskToTerminateMode getMode() {
        return Options.get(AskToTerminateOption.class);
    }
}
