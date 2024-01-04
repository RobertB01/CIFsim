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

/** Output Java files option. */
public class OutputJavaFilesOption extends BooleanOption {
    /** Constructor for the {@link OutputJavaFilesOption} class. */
    public OutputJavaFilesOption() {
        super(
                // name
                "Output Java files",

                // description
                "Whether to output files with the scanner/parsers/hooks Java code. [DEFAULT=yes]",

                // cmdShort
                'j',

                // cmdLong
                "output-java",

                // cmdValue
                "BOOL",

                // defaultValue
                true,

                // showInDialog
                true,

                // optDialogDescr
                "Whether to output files with the scanner/parsers/hooks Java code.",

                // optDialogCheckboxText
                "Output Java files");
    }

    /**
     * Should the scanner/parsers/hooks be written to Java code files?
     *
     * @return {@code true} if the scanner/parsers/hooks should be written to Java code files, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(OutputJavaFilesOption.class);
    }
}
