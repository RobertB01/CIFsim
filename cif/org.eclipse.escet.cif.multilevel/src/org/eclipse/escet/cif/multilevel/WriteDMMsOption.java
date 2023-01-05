//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option class to request writing computed DMMs to a file. */
public class WriteDMMsOption extends BooleanOption {
    /** Constructor for the {@link WriteDMMsOption} class. */
    public WriteDMMsOption() {
        super(
                // name
                "Write DMMs",

                // description
                "Whether to write the computed DMMs for multi-level synthesis be written to a file (BOOL=yes) or "
                        + "skip it (BOOL=no). [DEFAULT=no]",

                // cmdShort
                null,

                // cmdLong
                "write-dmms",

                // cmdValue
                "BOOL",

                // defaultValue
                false,

                // showInDialog
                true,

                // optDialogDescr
                "Whether to write computed DMMs for multi-level synthesis to a file.",

                // optDialogCheckboxText
                "Write computed DMMs");
    }

    /**
     * Should computed DMMs be written to a file?
     *
     * @return {@code true} if the DMMs should be written to a file, {@code false} otherwise.
     */
    public static boolean writeDmms() {
        return Options.get(WriteDMMsOption.class);
    }
}
