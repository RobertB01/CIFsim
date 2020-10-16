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

package org.eclipse.escet.cif.codegen.options;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeUppercase;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option class to set Simulink sample time. */
public class SimulinkSampleTimeOption extends StringOption {
    /** Name of the option. */
    private static final String NAME = "Simulink sample time";

    /** Description in the option dialog. */
    private static final String OPT_DIALOG_DESCR = "Set Simulink sample time. Possible values are \"continuous\", "
            + "\"inherited\", \"variable\", or a floating point sample time.";

    /** Default value of the option. */
    private static final String DEFAULT_VALUE = "continuous";

    /** Whether to use {@code null} if no string is empty. */
    private static final boolean EMPTY_AS_NULL = false;

    /** Description of the option. */
    private static final String DESCRIPTION = fmt("%s [DEFAULT=%s]", OPT_DIALOG_DESCR, DEFAULT_VALUE);

    /** Short option name. */
    private static final Character CMD_SHORT = null;

    /** Long option name. */
    private static final String CMD_LONG = "simulink-sample-time";

    /** Name of the option value. */
    private static final String CMD_VALUE = "STIME";

    /** Whether to display the option in the option dialog. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Text next to the dialog. */
    private static final String OPT_DIALOG_TEXT = "Sample time:";

    /** Constructor of the {@link SimulinkSampleTimeOption} class. */
    public SimulinkSampleTimeOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, EMPTY_AS_NULL, SHOW_IN_DIALOG,
                OPT_DIALOG_DESCR, OPT_DIALOG_TEXT);
    }

    /**
     * Retrieve the value of the Simulink sample time option.
     *
     * @return The value of the Simulink sample time option.
     */
    public static String getSampleTime() {
        return makeUppercase(Options.get(SimulinkSampleTimeOption.class));
    }

    /**
     * Get the real value of the sample time, if possible ({@link #sampleIsValidPositiveReal} must be true).
     *
     * @return The value of the sample time if it is a valid number, else {@code null}.
     */
    public static Double sampleGetValue() {
        try {
            Double d = Double.parseDouble(getSampleTime());
            return d;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Return whether the sample offset may be a non-zero floating point value.
     *
     * @return Whether using a non-zero floating point value as offset is allowed.
     */
    public static boolean offsetMayBeNonzero() {
        String value = getSampleTime();
        return !(value.equals("CONTINUOUS") || value.equals("INHERITED") || value.equals("VARIABLE"));
    }

    /**
     * Return whether the sample offset may be 'fixed in minor steps'.
     *
     * @return Whether using 'fixed in minor steps' is allowed.
     */
    public static boolean offsetMayBeFixed() {
        String value = getSampleTime();
        return value.equals("CONTINUOUS") || value.equals("INHERITED");
    }

    /**
     * Return whether the option is a valid positive real number.
     *
     * @return Whether the option is a valid positive real number.
     */
    public static boolean sampleIsValidPositiveReal() {
        Double d = sampleGetValue();
        if (d != null) {
            return d > 0.0;
        }
        return false;
    }

    /**
     * Retrieve the name used by the Simulink S-function for the option value.
     *
     * @return The Simulink S-function value for the option value.
     */
    public static String getSimulinkSampleTimeText() {
        String value = getSampleTime();
        if (value.equals("CONTINUOUS")) {
            return "CONTINUOUS_SAMPLE_TIME";
        } else if (value.equals("INHERITED")) {
            return "INHERITED_SAMPLE_TIME";
        } else if (value.equals("VARIABLE")) {
            return "VARIABLE_SAMPLE_TIME";
        } else if (sampleIsValidPositiveReal()) {
            return value;
        }
        return null;
    }
}
