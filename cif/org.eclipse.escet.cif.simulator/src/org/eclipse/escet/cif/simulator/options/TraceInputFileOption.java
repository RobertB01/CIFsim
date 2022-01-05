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

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option to specify the file trace input file to use. */
public class TraceInputFileOption extends StringOption {
    /** Constructor for the {@link TraceInputFileOption} class. */
    public TraceInputFileOption() {
        super(
                // name
                "Trace input file",

                // description
                "The path to the trace input file to use, if trace input mode is used. If not specified, the input "
                        + "CIF file path is used, where the \".cif\" file extension is removed if present, and a "
                        + "\".trace\" file extension is added.",

                // cmdShort
                null,

                // cmdLong
                "trace-input-file",

                // cmdValue
                "PATH",

                // defaultValue
                null,

                // emptyAsNull
                true,

                // showInDialog
                true,

                // optDialogDescr
                "The path to the trace input file to use, if trace input mode is used. If not specified, the input "
                        + "CIF file path is used, where the \".cif\" file extension is removed if present, and a "
                        + "\".trace\" file extension is added.",

                // optDialogCheckboxText
                "Path:");
    }

    /**
     * Returns the path of the trace input file.
     *
     * @return The path of the trace input file.
     */
    public static String getPath() {
        String path = Options.get(TraceInputFileOption.class);
        if (path == null) {
            path = CifSpecOption.getDerivedPath(".trace");
        }
        return path;
    }
}
