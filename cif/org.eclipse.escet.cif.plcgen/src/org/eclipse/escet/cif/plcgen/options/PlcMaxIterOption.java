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

package org.eclipse.escet.cif.plcgen.options;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Locale;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** PLC maximum iterations option. */
public class PlcMaxIterOption extends StringOption {
    /** Default option value. */
    private static final String DEFAULT_VALUE = "inf,inf";

    /** Constructor for the {@link PlcMaxIterOption} class. */
    public PlcMaxIterOption() {
        super(
                // name
                "PLC maximum iterations",

                // description
                """
                        The maximum number of iterations for uncontrollable and controllable events
                        for a single execution of the main program body, given as two values separated by a comma.
                        Each value is either a positive integer number, or "inf" to allow an infinite maximum
                        number of iterations. For example "20,inf" means that in a single execution of the main program body
                        at most 20 uncontrollable events and an unrestricted number of controllable events can be executed.
                        If only one value is given, it is used as maximum for both uncontrollable and controllable events.
                        [DEFAULT=inf,inf]"""
                        .replace("\n", " "),

                // cmdShort
                'x',

                // cmdLong
                "max-iter",

                // cmdValue
                "ITERS",

                // defaultValue
                DEFAULT_VALUE,

                // defaultAsNull
                true,

                // showInDialog
                true,

                // optDialogDescr
                "The maximum number of iterations of the main loop of the main program body, per execution of the "
                        + "main program body.",

                // optDialogLabelText
                "Maximum iterations:");
    }

    /**
     * Iteration limits for the uncontrollable and controllable event transition loops.
     *
     * @param uncontrollableLimit Maximum number of iterations over the uncontrollalble events. Either a positive number
     *     or {@code null}. The latter means 'infinite'.
     * @param controllableLimit Maximum number of iterations over the controllalble events. Either a positive number or
     *     {@code null}. The latter means 'infinite'.
     */
    public static record MaxIterLimits(Integer uncontrollableLimit, Integer controllableLimit) {
    }

    /**
     * Returns the maximum number of iterations of the event loops of the main program body, per execution of the main
     * program body.
     *
     * @return The maximum number of iterations for uncontrollable and controllable event transition loops.
     */
    public static MaxIterLimits getMaxIterLimits() {
        String value = Options.get(PlcMaxIterOption.class);
        if (value == null) {
            value = DEFAULT_VALUE;
        }

        int sepIndex = value.indexOf(',');
        String unconText, conText;
        if (sepIndex < 0) {
            unconText = value.strip();
            conText = value.strip();
        } else {
            unconText = value.substring(0, sepIndex).strip();
            conText = value.substring(sepIndex + 1).strip();
        }
        return new MaxIterLimits(convertLimit("uncontrollable", unconText), convertLimit("controllable", conText));
    }

    /**
     * Convert the option value string of a limit either to a positive integer number, or to {@code null} for infinite.
     *
     * @param context Event context for the user of the number being converted.
     * @param numberText Text to convert.
     * @return The converted limit value.
     */
    private static Integer convertLimit(String context, String numberText) {
        if (numberText.toLowerCase(Locale.US).equals("inf")) {
            return null;
        } else {
            int value;
            try {
                value = Integer.parseInt(numberText);
            } catch (NumberFormatException ex) {
                throw new RuntimeException(fmt("Iteration limit option value \"%s\" for %s events is not recognized.",
                        numberText, context), ex);
            }
            if (value <= 0) {
                throw new RuntimeException(fmt(
                        "Iteration limit option value \"%d\" for %s events is not a positive value.", value, context));
            }
            return value;
        }
    }
}
