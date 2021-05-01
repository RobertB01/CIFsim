//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams;

import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option for configuring whether the generated image should be written to a file. */
public class WriteImageOption extends BooleanOption {
    /** Short name of the option. */
    static final String NAME = "Write image";

    /** Description of the option. */
    static final String DESCRIPTION = "Whether to write the generated image. [Default='no'].";

    /** Single letter command option (disabled). */
    static final Character CMD_SHORT = null;

    /** Long option command. */
    static final String CMD_LONG = "write-image";

    /** Default value of the option. */
    static final boolean DEFAULT_VALUE = false;

    /** Whether to show the option in a dialog. */
    static final boolean SHOW_IN_DIALOG = false;

    /** Description of the option in the dialog. */
    static final String OPT_DIALOG_DESCR = null;

    /** Text of the dialog. */
    static final String OPT_DIALOG_TEXT = null;

    /** Constructor of the {@link WriteImageOption} class. */
    public WriteImageOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, "BOOL", DEFAULT_VALUE, SHOW_IN_DIALOG, OPT_DIALOG_DESCR,
                OPT_DIALOG_TEXT);
    }

    /**
     * Whether to write the generated image.
     *
     * @return Whether to write the image to a file.
     */
    public static boolean getWriteImage() {
        return Options.get(WriteImageOption.class);
    }

    /**
     * Get the path to the file for writing the generated image as a PNG file.
     *
     * @param inputFile Name of the file containing the diagram specification.
     * @return {@code null} if the generated image should not be written, else the path to the destination.
     */
    public static String getOutputPath(String inputFile) {
        if (!getWriteImage()) {
            return null;
        }
        String outPath = Paths.pathChangeExtension(inputFile, "rr", "png");
        return Paths.resolve(outPath);
    }
}
