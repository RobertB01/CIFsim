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

package org.eclipse.escet.cif.cif2mcrl2.options;

import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option for getting a debug output filename. */
public class DebugFileOption extends StringOption {
    /**
     * Constructor for the {@link DebugFileOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public DebugFileOption() {
        super("Debug file", "The path to the debug output file.", 'x', "debug", "FILE", null, true, true,
                "The debug output file path.", "Debug output file path:");
    }

    /**
     * Returns the path of the debug output file, or {@code null} if not provided.
     *
     * @return The path of the debug output file, or {@code null}.
     */
    public static String getPath() {
        return Options.get(DebugFileOption.class);
    }

    /**
     * Returns the path of the debug output file. If the file path is not specified, a modified input file path is used.
     * The input path is modified by removing the given input file extension (if present), and adding the given output
     * file extension.
     *
     * @param inExt Input file extension (including the dot).
     * @param outExt Output file extension (including the dot).
     * @return The path of the debug output file.
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
