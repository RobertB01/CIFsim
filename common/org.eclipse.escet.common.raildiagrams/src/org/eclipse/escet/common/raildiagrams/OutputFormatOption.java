//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option for specifying the desired output format of the rail diagram program. */
public class OutputFormatOption extends EnumOption<OutputFormat> {
    /** Short name of the option. */
    private static final String NAME = "Output format";

    /** Description of the option in the dialog. */
    private static final String OPT_DIALOG_DESCR = "The type of output to generate.";

    /** Command line description of the option. */
    private static final String DESCRIPTION = OPT_DIALOG_DESCR
            + " Specify \"images\" (default) for PNG images, or \"dbg-images\" for debug PNG images.";

    /** Single letter command option. */
    private static final Character CMD_SHORT = 'f';

    /** Long option command. */
    private static final String CMD_LONG = "format";

    /** The name of the option value, for command line processing. */
    private static final String CMD_VALUE = "FORMAT";

    /** Default value of the option. */
    private static final OutputFormat DEFAULT_VALUE = OutputFormat.IMAGES;

    /** Whether to show the option in a dialog. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Constructor of the {@link OutputFormatOption} class. */
    public OutputFormatOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, SHOW_IN_DIALOG, OPT_DIALOG_DESCR);
    }

    @Override
    public String getDialogText(OutputFormat value) {
        switch (value) {
            case IMAGES:
                return "PNG images";
            case DBG_IMAGES:
                return "Debug PNG images";
            default:
                throw new AssertionError("Unrecognized enum value.");
        }
    }

    /**
     * Return the selected output format.
     *
     * @return Output type to generate.
     */
    public static OutputFormat getFormat() {
        return Options.get(OutputFormatOption.class);
    }
}
