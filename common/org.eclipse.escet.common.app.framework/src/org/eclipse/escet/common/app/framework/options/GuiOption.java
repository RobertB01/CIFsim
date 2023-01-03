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

package org.eclipse.escet.common.app.framework.options;

/** GUI option. */
public class GuiOption extends EnumOption<GuiMode> {
    /** Constructor for the {@link GuiOption} class. */
    public GuiOption() {
        super(
                // name
                "GUI",

                // description
                "The GUI mode. Specify \"on\" to enable GUI functionality, \"off\" to run in headless mode (for "
                        + "systems without a display) with all GUI functionality disabled, or \"auto\" to enable "
                        + "the GUI if possible and disable it otherwise. [DEFAULT=auto]",

                // cmdShort
                null,

                // cmdLong
                "gui",

                // cmdValue
                "GMODE",

                // defaultValue
                GuiMode.AUTO,

                // showInDialog
                false,

                // optDialogDescr
                null);
    }

    @Override
    protected String getDialogText(GuiMode mode) {
        switch (mode) {
            case AUTO:
                return "Automatic mode: enable GUI if possible";
            case ON:
                return "On: enable GUI";
            case OFF:
                return "Off: disable GUI";
        }
        throw new RuntimeException("Unknown GUI mode: " + mode);
    }

    /**
     * Returns the GUI mode.
     *
     * @return The GUI mode.
     */
    public static GuiMode getGuiMode() {
        return Options.get(GuiOption.class);
    }
}
