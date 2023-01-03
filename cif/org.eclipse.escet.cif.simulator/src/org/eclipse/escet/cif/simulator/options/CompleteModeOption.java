//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/**
 * Complete mode option.
 *
 * <P>
 * Complete mode may only be disabled for the {@link InputMode#AUTO automatic} and {@link InputMode#SVG SVG}
 * {@link InputMode input modes} with 'first' transition {@link AutoAlgoOption automatic choice algorithm}.
 * </p>
 *
 * @see InputModeOption
 * @see AutoAlgoOption
 */
public class CompleteModeOption extends EnumOption<CompleteMode> {
    /** Constructor for the {@link CompleteModeOption} class. */
    public CompleteModeOption() {
        super("Complete mode", "If complete mode is enabled, all possible transitions are computed. If it is "
                + "disabled, computation stops as soon as a possible transition is found. Specify \"on\" to enable, "
                + "\"off\" to disable, or \"auto\" (default) to disable if possible, and enable otherwise.", null,
                "complete", "CMODE", CompleteMode.AUTO, true,
                "If complete mode is enabled, all possible transitions are computed. If it is disabled, computation "
                        + "stops as soon as a possible transition is found.");
    }

    @Override
    protected String getDialogText(CompleteMode value) {
        switch (value) {
            case AUTO:
                return "Auto: disable if possible, enable otherwise";
            case ON:
                return "On: forced enabled";
            case OFF:
                return "Off: forced disabled";
            default:
                throw new RuntimeException("Unknown complete mode: " + value);
        }
    }

    /**
     * Returns a value indicating whether complete mode is enabled.
     *
     * @return A value indicating whether complete mode is enabled.
     */
    public static boolean isEnabled() {
        // Determine complete mode based on input mode and automatic mode
        // choice algorithm.
        InputMode inputMode = InputModeOption.getInputMode();
        boolean disableAllowed = false;
        switch (inputMode) {
            case AUTO:
            case SVG:
                int autoAlgo = AutoAlgoOption.getAutoAlgo();
                disableAllowed = (autoAlgo == -2);
                break;

            case TRACE:
            case CONSOLE:
            case GUI:
                break;
        }

        // Get complete mode.
        CompleteMode completeMode = Options.get(CompleteModeOption.class);
        boolean enabled;
        switch (completeMode) {
            case AUTO:
                enabled = !disableAllowed;
                break;
            case OFF:
                enabled = false;
                break;
            case ON:
                enabled = true;
                break;
            default:
                String msg = "Unknown complete mode: " + completeMode;
                throw new RuntimeException(msg);
        }

        // Check for valid complete mode.
        if (!enabled && !disableAllowed) {
            // Complete mode may only be disabled if that is allowed by
            // the input mode and automatic mode choice algorithm.
            String msg = "Disabling complete mode is only allowed for the automatic and SVG input modes, together "
                    + "with the first transition automatic mode choice algorithm.";
            throw new InvalidOptionException(msg);
        }

        // Is complete mode enabled?
        return enabled;
    }
}
