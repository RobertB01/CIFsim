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

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option to specify the file in which to store the compiled code. */
public class CompiledCodeFileOption extends StringOption {
    /** Constructor for the {@link CompiledCodeFileOption} class. */
    public CompiledCodeFileOption() {
        super(
                // name
                "Compiled code file",

                // description
                "The path to the compiled code file (\".cifcode\" file) in which to store the compiled Java code for "
                        + "the model, if applicable. If not specified, the input file path is used, where the "
                        + "\".cif\" file extension is removed if present, and a \".cifcode\" file extension is added.",

                // cmdShort
                null,

                // cmdLong
                "compile-file",

                // cmdValue
                "PATH",

                // defaultValue
                null,

                // emptyAsNull
                true,

                // showInDialog
                true,

                // optDialogDescr
                "The path to the \".cifcode\" file in which to store the compiled Java code for the model, if "
                        + "applicable. If not specified, the input file path is used, where the \".cif\" file "
                        + "extension is removed if present, and a \".cifcode\" file extension is added.",

                // optDialogCheckboxText
                "Path:");
    }

    /**
     * Returns the path of the compiled code file.
     *
     * @return The path of the compiled code file.
     */
    public static String getPath() {
        String path = Options.get(CompiledCodeFileOption.class);
        if (path == null) {
            path = CifSpecOption.getDerivedPath(".cifcode");
        }
        return path;
    }
}
