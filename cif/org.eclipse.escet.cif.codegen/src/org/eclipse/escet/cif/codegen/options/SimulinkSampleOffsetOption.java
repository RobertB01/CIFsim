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

package org.eclipse.escet.cif.codegen.options;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeUppercase;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option class to set Simulink sample offset. */
public class SimulinkSampleOffsetOption extends StringOption {
    /** Name of the option. */
    private static final String NAME = "Simulink sample offset";

    /** Description in the option dialog. */
    private static final String OPT_DIALOG_DESCR = "Set Simulink sample offset. Possible values are \"fixed\" or a floating point sample offset.";

    /** Default value of the option. */
    private static final String DEFAULT_VALUE = "0.0";

    /** Whether to use {@code null} if no string is empty. */
    private static final boolean EMPTY_AS_NULL = false;

    /** Description of the option. */
    private static final String DESCRIPTION = fmt("%s [DEFAULT=%s]", OPT_DIALOG_DESCR, DEFAULT_VALUE);

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "simulink-sample-offset";

    /** Name of the option value. */
    private static final String CMD_VALUE = "OFFSET";

    /** Whether to display the option in the option dialog. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Text next to the dialog. */
    private static final String OPT_DIALOG_TEXT = "Sample offset:";

    /** Constructor of the {@link SimulinkSampleOffsetOption} class. */
    public SimulinkSampleOffsetOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, EMPTY_AS_NULL, SHOW_IN_DIALOG,
                OPT_DIALOG_DESCR, OPT_DIALOG_TEXT);
    }

    /**
     * Retrieve the value of the Simulink sample offset option.
     *
     * @return The value of the Simulink sample offset option.
     */
    public static String getSampleOffset() {
        return makeUppercase(Options.get(SimulinkSampleOffsetOption.class));
    }

    /**
     * Return whether the offset is 'fixed in minor steps'.
     *
     * @return Whether the offset is 'fixed in minor steps'.
     */
    public static boolean sampleOffsetIsFixed() {
        String value = getSampleOffset();
        return value.equals("FIXED");
    }

    /**
     * Get the real value of the offset, if possible ({@link #sampleOffsetIsZero} or {@link #sampleOffsetIsValidReal}
     * must be true).
     *
     * @return The value of the offset if it is a valid number, else {@code null}.
     */
    public static Double offsetGetValue() {
        try {
            Double d = Double.parseDouble(getSampleOffset());
            return d;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Return whether the offset is 0 or 0.0.
     *
     * @return Whether the offset is 0 or 0.0.
     */
    public static boolean sampleOffsetIsZero() {
        Double d = offsetGetValue();
        if (d != null) {
            return d == 0.0;
        }
        return false;
    }

    /**
     * Return whether the option is a valid real number.
     *
     * @return Whether the option is a valid real number.
     */
    public static boolean sampleOffsetIsValidReal() {
        Double d = offsetGetValue();
        if (d != null) {
            return d >= 0.0;
        }
        return false;
    }
}
