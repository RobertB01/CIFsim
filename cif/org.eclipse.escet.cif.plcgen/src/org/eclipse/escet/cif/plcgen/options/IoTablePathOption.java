//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** PLC I/O table path option. */
public class IoTablePathOption extends StringOption {
    /** The description of the option. */
    private static final String DESCRIPTION = "The path of the CSV file that contains the types and PLC memory "
            + "addresses of the input/output variables. By default, the path to the input CIF file is used, with its "
            + "\".cif\" file extension removed if present, and the \".csv\" file extension added.";

    /** Constructor for the {@link IoTablePathOption} class. */
    public IoTablePathOption() {
        super("I/O table path", // name.
                DESCRIPTION, // description.
                null, // cmdShort.
                "io-table-path", // cmdLong.
                "TABLEPATH", // cmdValue.
                null, // defaultValue.
                true, // emptyAsNull.
                true, // showInDialog.
                DESCRIPTION, // optDialogDescr.
                "Path:"); // optDialogLabelText.
    }

    /**
     * Returns the I/O table path if it was entered.
     *
     * @return The I/O table path, or {@code null} if not entered or left blank.
     */
    public static String getTableFilename() {
        return Options.get(IoTablePathOption.class);
    }

    /**
     * Returns the path of the I/O table file. If the file path is not specified, a modified input file path is used.
     *
     * @return The path of the I/O table file.
     * @see InputFileOption
     */
    public static String getDerivedPath() {
        String rslt = getTableFilename();
        if (rslt == null) {
            rslt = InputFileOption.getDerivedPath(".cif", ".csv");
        }
        return rslt;
    }
}
