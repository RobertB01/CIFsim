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
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/** PLC maximum iterations option. */
public class PlcMaxIterOption extends StringOption {
    /** Default option value. */
    private static final String DEFAULT_VALUE = "inf,inf";

    /** Description of the option value. */
    private static final String VALUE_DESCRIPTION_TEXT = """
            The maximum number of iterations to try each uncontrollable event once and the maximum number of
            iterations to try each controllable event once, for a single execution of the main program body.
            A maximum number of iterations is either a positive integer number or "inf" to state
            there is no upper bound on the maximum number of iterations.
            This option takes two maximum numbers, one for uncontrollable events and one for controllable
            events.
            For example "20,inf" means that in a single execution of the main program body at most 20 times
            every uncontrollable event is tried, and at most an unrestricted number of times every
            controllable event is tried. Note that an iteration loop is always considered finished as soon
            as none of the tried events in one iteration was possible.
            If only one value is given to this option, it is used as maximum iteration count for both
            uncontrollable and controllable events.""";

    /** Constructor for the {@link PlcMaxIterOption} class. */
    public PlcMaxIterOption() {
        super(
                // name
                "PLC maximum iterations",

                // description
                (VALUE_DESCRIPTION_TEXT + " [DEFAULT=\"" + DEFAULT_VALUE + "\"]").replace("\n", " "),

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
                VALUE_DESCRIPTION_TEXT.replace("\n", " "),

                // optDialogLabelText
                "Maximum iterations:");
    }

    /**
     * Iteration limits for the uncontrollable and controllable event transition loops.
     *
     * @param uncontrollableLimit Maximum number of iterations over the uncontrollable events. Either a positive number
     *     or {@code null}. The latter means 'infinite'.
     * @param controllableLimit Maximum number of iterations over the controllable events. Either a positive number or
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
     * @param eventKind The kind of events being considered.
     * @param numberText Text to convert.
     * @return The converted limit value.
     */
    private static Integer convertLimit(String eventKind, String numberText) {
        if (numberText.toLowerCase(Locale.US).equals("inf")) {
            return null;
        } else {
            int value;
            try {
                value = Integer.parseInt(numberText);
            } catch (NumberFormatException ex) {
                throw new InvalidOptionException(fmt("PLC maximum iteration limits option value \"%s\" for %s events "
                        + "is neither recognized as infinite (\"inf\" without quotes) nor as positive integer number.",
                        numberText, eventKind), ex);
            }
            if (value <= 0) {
                throw new InvalidOptionException(
                        fmt("Iteration limit option value \"%d\" for %s events is not a positive number.", value,
                                eventKind));
            }
            return value;
        }
    }
}
