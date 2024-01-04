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

package org.eclipse.escet.setext.generator;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Output BNF file option. */
public class OutputBnfFileOption extends BooleanOption {
    /** Constructor for the {@link OutputBnfFileOption} class. */
    public OutputBnfFileOption() {
        super(
                // name
                "Output BNF file",

                // description
                "Whether to output a file with the grammar in BNF syntax. [DEFAULT=no]",

                // cmdShort
                'b',

                // cmdLong
                "output-bnf",

                // cmdValue
                "BOOL",

                // defaultValue
                false,

                // showInDialog
                true,

                // optDialogDescr
                "Whether to output a file with the grammar in BNF syntax.",

                // optDialogCheckboxText
                "Output BNF file");
    }

    /**
     * Should the grammar be written to a BNF file?
     *
     * @return {@code true} if the grammar should be written to a BNF file, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(OutputBnfFileOption.class);
    }
}
