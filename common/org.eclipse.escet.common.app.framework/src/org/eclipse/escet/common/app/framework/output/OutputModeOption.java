//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.output;

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Output mode option. */
public class OutputModeOption extends EnumOption<OutputMode> {
    /**
     * Constructor for the {@link OutputModeOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public OutputModeOption() {
        super("Output mode",
                "The output mode. Specify \"error\" for errors only; \"warning\" for errors and warnings only; "
                        + "\"normal\" (default) for errors, warnings, and 'normal' output; or \"debug\" for errors, "
                        + "warnings, 'normal', and debug output.",
                'm', "output-mode", "OUTMODE", OutputMode.NORMAL, true, "The application's output mode.");
    }

    /**
     * Returns the output mode.
     *
     * @return The output mode.
     */
    public static OutputMode getOutputMode() {
        return Options.get(OutputModeOption.class);
    }

    @Override
    protected String getDialogText(OutputMode value) {
        switch (value) {
            case ERROR:
                return "Error: error output only";
            case WARNING:
                return "Warning: error and warning output only";
            case NORMAL:
                return "Normal: error, warning, and 'normal' output";
            case DEBUG:
                return "Debug: error, warning, 'normal' and debug output";
            default:
                throw new RuntimeException("Unknown value: " + value);
        }
    }
}
