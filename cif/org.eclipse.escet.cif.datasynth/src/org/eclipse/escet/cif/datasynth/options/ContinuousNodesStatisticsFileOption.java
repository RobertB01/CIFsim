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
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option for getting the path for the continuous nodes statistics output file. */
public class ContinuousNodesStatisticsFileOption extends StringOption {
    /** Constant describing the option. */
    static final String OPTION_DESC = "The path to the continuous nodes statistics output file.";

    /** Constructor for the {@link ContinuousNodesStatisticsFileOption} class. */
    public ContinuousNodesStatisticsFileOption() {
        super(
            // name.
            "Synthesis statistics file",

            // description.
            OPTION_DESC,

            // cmdShort.
            null,

            // cmdLong.
            "statsfile",

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
            "Continuous nodes statistics output file path:");
    }

    /**
     * Returns the path of the output file, or {@code null} if not provided.
     *
     * @return The path of the output file, or {@code null}.
     */
    public static String getPath() {
        return Options.get(ContinuousNodesStatisticsFileOption.class);
    }

    /**
     * Returns the path of the continuous nodes statistics output file. If the continuous nodes output file path is
     * not specified, a modified input file path is used. The input path is modified by removing the given input
     * file extension (if present), and adding the given output file extension.
     *
     * @param inExt Input file extension (including the dot).
     * @param outExt Output file extension (including the dot).
     * @return The path of the continuous nodes statistics output file.
     * @see OutputFileOption
     */
    public static String getDerivedPath(String inExt, String outExt) {
        String rslt = getPath();
        if (rslt == null) {
            rslt = InputFileOption.getDerivedPath(inExt, outExt);
        }
        return rslt;
    }
}
