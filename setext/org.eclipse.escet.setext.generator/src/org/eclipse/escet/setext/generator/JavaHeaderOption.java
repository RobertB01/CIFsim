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

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Java output header file option. */
public class JavaHeaderOption extends StringOption {
    /** Constructor for the {@link JavaHeaderOption} class. */
    public JavaHeaderOption() {
        super(
                // name
                "Java output header",

                // description
                "Specify the absolute or relative local file system path of the file that contains the header to "
                        + "insert for generated Java code files. The default is not to generate a header.",

                // cmdShort
                null,

                // cmdLong
                "java-header-file",

                // cmdValue
                "FILE",

                // defaultValue
                null,

                // emptyAsNull
                true,

                // showInDialog
                true,

                // optDialogDescr
                "Specify the absolute or relative local file system path of the file that contains the header to "
                        + "insert for generated Java code files. Leave empty to not generate a header.",

                // optDialogLabelText
                "Header file path:");
    }

    /**
     * Returns the absolute or relative local file system path of the file that contains the header to insert for
     * generated Java code files, or {@code null} if not provided.
     *
     * @return The path of the header file, or {@code null}.
     */
    public static String getPath() {
        return Options.get(JavaHeaderOption.class);
    }
}
