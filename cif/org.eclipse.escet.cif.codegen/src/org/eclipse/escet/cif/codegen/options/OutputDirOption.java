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

package org.eclipse.escet.cif.codegen.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Output directory option. */
public class OutputDirOption extends StringOption {
    /** Constructor for the {@link OutputDirOption} class. */
    public OutputDirOption() {
        super(
                // name
                "Output directory",

                // description
                "The path to the output directory to which the generated code will be written. "
                        + "By default the current directory is used.",

                // cmdShort
                'o',

                // cmdLong
                "output-dir",

                // cmdValue
                "PATH",

                // defaultValue
                ".",

                // emptyAsNull
                false,

                // showInDialog
                true,

                // optDialogDescr
                "The path to the output directory to which the generated code will be written.",

                // optDialogLabelText
                "Path:");
    }

    /**
     * Returns the path of the output directory.
     *
     * @return The path of the output directory.
     */
    public static String getPath() {
        return Options.get(OutputDirOption.class);
    }
}
