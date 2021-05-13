//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

/** Input mode option. */
public class InputModeOption extends EnumOption<InputMode> {
    /** Constructor for the {@link InputModeOption} class. */
    public InputModeOption() {
        super("Input mode",
                "The input mode that determines the method by which the simulator can request input, such as which "
                        + "transition to choose. Specify \"console\" for interactive console input mode, \"gui\" "
                        + "(default) for interactive GUI input mode, \"auto\" for automatic input mode, \"trace\" for "
                        + "trace input mode, or \"svg\" for SVG input mode (requires the use of SVG visualization).",
                'i', "input-mode", "INPUTMODE", InputMode.GUI, true,
                "The input mode that determines the method by which the simulator can request input, such as which "
                        + "transition to choose.");
    }

    /**
     * Returns the input mode.
     *
     * @return The input mode.
     */
    public static InputMode getInputMode() {
        InputMode rslt = Options.get(InputModeOption.class);
        return rslt;
    }

    /**
     * Is the input mode interactive? The interactive console and GUI input modes are considered interactive. This
     * method ignores semi-automatic mode.
     *
     * @return {@code true} if the input mode is interactive, {@code false} otherwise.
     */
    public static boolean isInteractive() {
        switch (getInputMode()) {
            case AUTO:
            case TRACE:
            case SVG:
                return false;

            case CONSOLE:
            case GUI:
                return true;
        }
        throw new RuntimeException("Unknown input mode: " + getInputMode());
    }

    /**
     * Is the input mode purely interactive? Pure interactive means interactive mode without semi-automatic mode.
     *
     * @return {@code true} if the input mode is purely interactive, {@code false} otherwise.
     */
    public static boolean isPurelyInteractive() {
        return isInteractive() && !InteractiveAutoChooseOption.isSpecified();
    }

    @Override
    protected String getDialogText(InputMode value) {
        switch (value) {
            case CONSOLE:
                return "Interactive console input mode";
            case GUI:
                return "Interactive GUI input mode";
            case AUTO:
                return "Automatic input mode";
            case TRACE:
                return "Trace input mode";
            case SVG:
                return "SVG input mode (requires the use of SVG visualization)";
        }
        throw new RuntimeException("Unknown mode: " + value);
    }
}
