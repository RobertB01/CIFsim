//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.options;

import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option to configure the path for the continuous performance statistics output file. */
public class ContinuousPerformanceStatisticsFileOption extends StringOption {
    /** Option description. */
    private static final String OPTION_DESC = "The path to the continuous performance statistics output file. " +
                                              "If not specified, defaults to the input file path, where the '.cif' " +
                                              "file extension is removed (if present), and a '.stats.txt' file " +
                                              "extension is added.";

    /** Constructor for the {@link ContinuousPerformanceStatisticsFileOption} class. */
    public ContinuousPerformanceStatisticsFileOption() {
        super(
            // name.
            "Continuous performance statistics file",

            // description.
            OPTION_DESC,

            // cmdShort.
            null,

            // cmdLong.
            "statsfile-contperf",

            // cmdValue.
            "FILE",

            // defaultValue.
            null,

            // emptyAsNull.
            true,

            // showInDialog.
            true,

            // optDialogDescr.
            OPTION_DESC,

            // optDialogLabelText.
            "Continuous performance statistics output file path:");
    }

    /**
     * Returns the path of the continuous performance statistics output file. If the continuous performance output file
     * path is not specified, a modified input file path is used. The input path is modified by removing the
     * {@code ".cif"} file extension (if present), and adding the {@code ".stats.txt"} output file extension.
     *
     * @return The path of the continuous performance statistics output file.
     */
    public static String getPath() {
        String rslt = Options.get(ContinuousPerformanceStatisticsFileOption.class);
        if (rslt == null) {
            rslt = InputFileOption.getDerivedPath(".cif", ".stats.txt");
        }
        return rslt;
    }
}
