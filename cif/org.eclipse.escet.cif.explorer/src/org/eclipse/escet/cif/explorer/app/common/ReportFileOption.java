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

package org.eclipse.escet.cif.explorer.app.common;

import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Report file option. */
public class ReportFileOption extends StringOption {
    /**
     * Constructor for the {@link ReportFileOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public ReportFileOption() {
        super("Report file", "The path to the report file.", 'r', "report", "FILE", null, true, true,
                "The report file path.", "Report file path:");
    }

    /**
     * Returns the path of the report file, or {@code null} if not provided.
     *
     * @return The path of the report file, or {@code null}.
     */
    public static String getPath() {
        return Options.get(ReportFileOption.class);
    }

    /**
     * Returns the path of the report file. If the report file path is not specified, a modified input file path is
     * used. The input path is modified by removing the given input file extension (if present), and adding the given
     * output file extension.
     *
     * @param inExt Input file extension (including the dot).
     * @param outExt Output file extension (including the dot).
     * @return The path of the report file.
     * @see InputFileOption
     */
    public static String getDerivedPath(String inExt, String outExt) {
        String rslt = getPath();
        if (rslt == null) {
            rslt = InputFileOption.getDerivedPath(inExt, outExt);
        }
        return rslt;
    }
}
