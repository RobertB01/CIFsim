//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** History size option. */
public class HistorySizeOption extends IntegerOption {
    /** Constructor for the {@link HistorySizeOption} class. */
    public HistorySizeOption() {
        super(
                // name
                "History size",

                // description
                "The maximum number of states to remember as history, excluding the initial state. Only has an "
                        + "effect if history is enabled. Value must be in the range [0 .. 2^31-1]. Specify \"inf\" to "
                        + "not set a maximum. [DEFAULT=100]",

                // cmdShort
                null,

                // cmdLong
                "history-size",

                // cmdValue
                "SIZE",

                // defaultValue
                100,

                // minimumValue
                0,

                // maximumValue
                Integer.MAX_VALUE,

                // pageIncrementValue
                100,

                // showInDialog
                true,

                // optDialogDescr
                "The maximum number of states to remember as history, excluding the initial state. Only has an effect "
                        + "if history is enabled.",

                // optDialogLabelText
                "Maximum:",

                // hasSpecialValue
                true,

                // defaultNormalValue
                100,

                // specialValueSyntax
                "inf",

                // optDialogSpecialText
                "Infinite maximum (no maximum)",

                // optDialogNormalText
                "Finite maximum");
    }

    /**
     * The maximum number of states to remember as history, excluding the initial state. Only has an effect if history
     * is enabled.
     *
     * @return The maximum or {@code null} for infinite (no maximum).
     */
    public static Integer getMaximum() {
        return Options.get(HistorySizeOption.class);
    }

    /**
     * Is undo enabled? Undo is enabled if history is enabled, and the history size is larger than zero.
     *
     * @return {@code true} if undo is enabled, {@code false} otherwise.
     */
    public static boolean isUndoEnabled() {
        if (!HistoryOption.isEnabled()) {
            return false;
        }
        Integer maxSize = getMaximum();
        return maxSize == null || maxSize > 0;
    }
}
