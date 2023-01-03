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

package org.eclipse.escet.setext.generator;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Output debug files option. */
public class OutputDebugFilesOption extends BooleanOption {
    /** Constructor for the {@link OutputDebugFilesOption} class. */
    public OutputDebugFilesOption() {
        super(
                // name
                "Output debug files",

                // description
                "Whether to output files with the scanner/parser debug information. [DEFAULT=yes]",

                // cmdShort
                'd',

                // cmdLong
                "output-debug",

                // cmdValue
                "BOOL",

                // defaultValue
                true,

                // showInDialog
                true,

                // optDialogDescr
                "Whether to output files with the scanner/parsers debug information.",

                // optDialogCheckboxText
                "Output debug files");
    }

    /**
     * Should the scanner/parsers be written to debug information files?
     *
     * @return {@code true} if the scanner/parsers should be written to debug information files, {@code false}
     *     otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(OutputDebugFilesOption.class);
    }
}
