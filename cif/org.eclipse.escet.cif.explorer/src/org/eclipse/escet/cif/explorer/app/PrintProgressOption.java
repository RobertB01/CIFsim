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

package org.eclipse.escet.cif.explorer.app;

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Print progress option. */
public class PrintProgressOption extends IntegerOption {
    /** Constructor for the {@link PrintProgressOption} class. */
    public PrintProgressOption() {
        super(
                // name
                "Print progress",

                // description
                "The number of states to process before printing progress information. "
                        + "Must be a non-negative integer number. May be \"off\" to disable progress information. "
                        + "[DEFAULT=1000]",

                // cmdShort
                'p',

                // cmdLong
                "progress",

                // cmdValue
                "CNT",

                // defaultValue
                1000,

                // minimumValue
                1,

                // maximumValue
                Integer.MAX_VALUE,

                // pageIncrementValue
                100,

                // showInDialog
                true,

                // optDialogDescr
                "The number of states to process before printing progress information.",

                // optDialogLabelText
                "Number of states:",

                // hasSpecialValue
                true,

                // defaultNormalValue
                1000,

                // specialValueSyntax
                "off",

                // optDialogSpecialText
                "Disable progress information",

                // optDialogNormalText
                "Enable progress information");
    }

    /**
     * Get the number of states to process before printing progress information, or {@code null} to disable progress
     * information.
     *
     * @return The number of states (non-negative), or {@code null}.
     */
    public static Integer getProgressCount() {
        return Options.get(PrintProgressOption.class);
    }
}
