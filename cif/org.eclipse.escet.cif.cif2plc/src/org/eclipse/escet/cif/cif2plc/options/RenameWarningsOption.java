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

package org.eclipse.escet.cif.cif2plc.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Rename warnings option. */
public class RenameWarningsOption extends BooleanOption {
    /** Constructor for the {@link RenameWarningsOption} class. */
    public RenameWarningsOption() {
        super(
                // name
                "Rename warnings",

                // description
                "Whether to print warnings to the console when a PLC name is renamed due to a conflict with another "
                        + "PLC name (BOOL=yes), or omit the warnings (BOOL=no). [DEFAULT=no]",

                // cmdShort
                null,

                // cmdLong
                "warn-rename",

                // cmdValue
                "BOOL",

                // defaultValue
                false,

                // showInDialog
                true,

                // optDialogDescr
                "Whether to print warnings to the console when a PLC name is renamed due to a conflict with another "
                        + "PLC name, or omit the warnings.",

                // optDialogCheckboxText
                "Print rename warnings");
    }

    /**
     * Should rename warnings be printed to the console?
     *
     * @return {@code true} if rename warnings be printed to the console, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(RenameWarningsOption.class);
    }
}
