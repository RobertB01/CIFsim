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

package org.eclipse.escet.cif.eventbased.apps.options;

import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option for specifying a dump file. */
@SuppressWarnings("javadoc")
public class DumpFileOption extends StringOption {
    private static final String NAME = "Synthesis dump file";

    private static final String OPTDIALOGDESCR = "The path to the synthesis dump file.";

    private static final String DESCRIPTION = OPTDIALOGDESCR;

    private static final Character CMDSHORT = 'd';

    private static final String CMDLONG = "dump-file";

    private static final String CMDVALUE = "FILE";

    private static final String DEFAULTVALUE = null;

    private static final boolean EMPTYASNULL = true;

    private static final boolean SHOWINDIALOG = true;

    private static final String OPTDIALOGLABELTEXT = "Dump file path:";

    /**
     * Constructor for the {@link DumpFileOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public DumpFileOption() {
        super(NAME, DESCRIPTION, CMDSHORT, CMDLONG, CMDVALUE, DEFAULTVALUE, EMPTYASNULL, SHOWINDIALOG, OPTDIALOGDESCR,
                OPTDIALOGLABELTEXT);
    }

    /**
     * Returns the path of the synthesis dump file, or {@code null} if not provided.
     *
     * @return The path of the synthesis dump file, or {@code null}.
     */
    public static String getPath() {
        return Options.get(DumpFileOption.class);
    }

    /**
     * Returns the path of the synthesis dump file. If the synthesis dump file path is not specified, a modified input
     * file path is used. The input path is modified by removing the given input file extension (if present), and adding
     * the given output file extension.
     *
     * @param inExt Input file extension (including the dot).
     * @param outExt Output file extension (including the dot).
     * @return The path of the synthesis dump file.
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
