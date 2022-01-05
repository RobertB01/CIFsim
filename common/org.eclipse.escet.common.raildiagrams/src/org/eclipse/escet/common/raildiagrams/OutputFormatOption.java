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

/**
 * Option for specifying the desired output format of the rail diagram program.
 */
public class OutputFormatOption extends EnumOption<OutputFormat> {
    /** Short name of the option. */
    static final String NAME = "Output format";

    /** Command line description of the option. */
    static final String DESCRIPTION = "Type of the output, either \"images\" or \"dbg-images\"  [DEFAULT=images].";

    /** Single letter command option (disabled). */
    static final Character CMD_SHORT = 'f';

    /** Long option command. */
    static final String CMD_LONG = "format";

    /** Default value of the option. */
    static final OutputFormat DEFAULT_VALUE = OutputFormat.IMAGES;

    /** Whether to show the option in a dialog. */
    static final boolean SHOW_IN_DIALOG = true;

    /** Description of the option in the dialog. */
    static final String OPT_DIALOG_DESCR = "Type of output.";

    /** Text of the dialog. */
    static final String OPT_DIALOG_TEXT = NAME;

    /**
     * Constructor of the OutputFormatOption.java class.
     */
    public OutputFormatOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, "FORMAT", DEFAULT_VALUE, SHOW_IN_DIALOG, OPT_DIALOG_DESCR);
    }

    @Override
    public String getDialogText(OutputFormat value) {
        switch (value) {
        case IMAGES:
            return "images";
        case DBG_IMAGES:
            return "dbg-images";
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
