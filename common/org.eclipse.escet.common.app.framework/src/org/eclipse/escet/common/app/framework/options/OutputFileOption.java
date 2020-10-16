//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.options;

/** Output file option. */
public class OutputFileOption extends StringOption {
    /**
     * Constructor for the {@link OutputFileOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public OutputFileOption() {
        super("Output file", "The path to the output file.", 'o', "output", "FILE", null, true, true,
                "The output file path.", "Output file path:");
    }

    /**
     * Returns the path of the output file, or {@code null} if not provided.
     *
     * @return The path of the output file, or {@code null}.
     */
    public static String getPath() {
        return Options.get(OutputFileOption.class);
    }

    /**
     * Returns the path of the output file. If the output file path is not specified, a modified input file path is
     * used. The input path is modified by removing the given input file extension (if present), and adding the given
     * output file extension.
     *
     * @param inExt Input file extension (including the dot).
     * @param outExt Output file extension (including the dot).
     * @return The path of the output file.
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
