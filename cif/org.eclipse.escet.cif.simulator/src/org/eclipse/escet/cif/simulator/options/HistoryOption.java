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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** History option. */
public class HistoryOption extends BooleanOption {
    /** Constructor for the {@link HistoryOption} class. */
    public HistoryOption() {
        super(
                // name
                "History",

                // description
                "Whether to enable history for (semi-)interactive input modes, to allow undo of transitions, "
                        + "and reset of the simulation to the initial state (BOOL=yes), or disable history to "
                        + "disable undo/reset. [DEFAULT=yes]",

                // cmdShort
                null,

                // cmdLong
                "history",

                // cmdValue
                "BOOL",

                // defaultValue
                true,

                // showInDialog
                true,

                // optDialogDescr
                "Enable history for (semi-)interactive input modes, to allow undo of transitions, and reset of the "
                        + "simulation to the initial state, or disable history to disable undo/reset.",

                // optDialogCheckboxText
                "Enable history (undo/reset)");
    }

    /**
     * Returns a value indicating whether history is enabled. This method takes into account that history is only
     * enabled for (semi-)automatic input modes.
     *
     * @return A value indicating whether history is enabled.
     */
    public static boolean isEnabled() {
        boolean enabled = Options.get(HistoryOption.class);
        return enabled && InputModeOption.isInteractive();
    }
}
