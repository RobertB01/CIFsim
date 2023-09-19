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

/** PLC IO table path option. */
public class IoTablePathOption extends StringOption {
    /** Constructor for the {@link IoTablePathOption} class. */
    public IoTablePathOption() {
        super("Io table name", // name.
                "Path of the CSV file that contains the type and PLC memory address "
                        + "of the input/output variables.", // description.
                null, // cmdShort.
                "io-table-path", // cmdLong.
                "TABLEPATH", // cmdValue.
                null, // defaultValue.
                true, // emptyAsNull.
                true, // showInDialog.
                "The name of the file with input and output variables.", // optDialogDescr.
                "Table filename:"); // optDialogLabelText.
    }

    /**
     * Returns the table filename if it was entered.
     *
     * @return The table filename, or {@code null} if not entered or left blank.
     */
    public static String getTableFilename() {
        return Options.get(IoTablePathOption.class);
    }

    /**
     * Returns the path of the table file. If the table file path is not specified, a modified input file path is used.
     *
     * @return The path of the output file.
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
