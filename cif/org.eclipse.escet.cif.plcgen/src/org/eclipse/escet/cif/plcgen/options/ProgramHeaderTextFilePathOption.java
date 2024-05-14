//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.options;

import org.eclipse.escet.cif.plcgen.CifPlcGenApp;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;
import org.eclipse.escet.common.java.PathPair;

/** Option to specify the path to the user-supplied header text file. */
public class ProgramHeaderTextFilePathOption extends StringOption {
    /** The description of the option. */
    private static final String DESCRIPTION = "The path of the text file to include as header in the generated PLC "
            + "code. The file should only contain printable ASCII text. The program recognizes the \""
            + CifPlcGenApp.APP_NAME_PATTERN + "\", \"" + CifPlcGenApp.APP_VERSION_PATTERN + "\", \""
            + CifPlcGenApp.TIME_STAMP_PATTERN + "\" and \"" + CifPlcGenApp.BRIEF_EXPLANATION_PATTERN
            + "\" text patterns in the file, and replaces them with their current value. If no file is provided, the "
            + "program inserts a default header with the above information.";

    /** Constructor of the {@link ProgramHeaderTextFilePathOption} class. */
    public ProgramHeaderTextFilePathOption() {
        super("Program header text file path", // name.
                DESCRIPTION, // description.
                null, // cmdShort.
                "header-file-path", // cmdLong.
                "HEADERPATH", // cmdValue.
                null, // defaultValue.
                true, // emptyAsNull.
                true, // showInDialog.
                DESCRIPTION, // optDialogDescr.
                "Path:"); // optDialogLabelText.
    }

    /**
     * Get the value of the {@link ProgramHeaderTextFilePathOption} option.
     *
     * @return Either {@code null} or paths to the file containing the text to use.
     */
    public static PathPair getProgramHeaderFilePaths() {
        String headerFilePath = Options.get(ProgramHeaderTextFilePathOption.class);
        if (headerFilePath == null) {
            return null;
        }
        return new PathPair(headerFilePath, Paths.resolve(headerFilePath));
    }
}
