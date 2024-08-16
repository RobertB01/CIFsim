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
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/** PLC maximum iterations option. */
public class PlcMaxIterOption extends StringOption {
    /** Default option value text. */
    private static final String DEFAULT_VALUE_TEXT = "resp,resp";

    /** Default fallback iteration limit for controllable events. */
    private static final int DEFAULT_FALLBACK_CONTR_LIMIT = MaxIterLimits.INFINITE_VALUE;

    /** Default fallback iteration limit for uncontrollable events. */
    private static final int DEFAULT_FALLBACK_UNCONTR_LIMIT = MaxIterLimits.INFINITE_VALUE;

    /** Description of the option value. */
    private static final String VALUE_DESCRIPTION_TEXT = """
            The worst case maximum number of iterations to try each uncontrollable event once and the wordt case maximum
            number of iterations to try each controllable event once, for a single execution of the main program body.
            A maximum number of iterations is a positive integer number, the word "resp" or the word "inf".
            An integer number directly states the number of iterations.
            The word "inf" means "infinite", there is no upper bound on the maximum number of iterations.
            The word "resp" means that the maximum number of iterations is derived from the bounded response property
            computed by the CIF controller properties checker application.
            This option takes two maximum numbers, one for uncontrollable events and one for controllable
            events respectively.
            For example "20,resp" means that in a single execution of the main program body,
            each uncontrollable event is tried at most 20 times, and
            each controllable event is tried as often as the bounded response value for controllable events computed
            by the CIF controller properties checker application indicates.
            Note that in any case, an iteration loop is considered to be finished as soon as none of the tried events
            in one iteration was possible.
            If only one value is given to this option, it is used as maximum number of iterations for both
            uncontrollable and controllable events.""";

    /** Constructor for the {@link PlcMaxIterOption} class. */
    public PlcMaxIterOption() {
        super(
                // name
                "PLC maximum iterations",

                // description
                (VALUE_DESCRIPTION_TEXT + " [DEFAULT=\"" + DEFAULT_VALUE_TEXT + "\"]").replace("\n", " "),

                // cmdShort
                'x',

                // cmdLong
                "max-iter",

                // cmdValue
                "ITERS",

                // defaultValue
                DEFAULT_VALUE_TEXT,

                // emptyAsNull
                false,

                // showInDialog
                true,

                // optDialogDescr
                VALUE_DESCRIPTION_TEXT.replace("\n", " "),

                // optDialogLabelText
                "Maximum iterations:");
    }

    /**
     * Returns the maximum number of iterations of the event loops of the main program body, per execution of the main
     * program body.
     *
     * @return The maximum number of iterations for uncontrollable and controllable event transition loops.
     */
    public static MaxIterLimits getMaxIterLimits() {
        String value = Options.get(PlcMaxIterOption.class);

        int sepIndex = value.indexOf(',');
        String unconText, conText;
        if (sepIndex < 0) {
            unconText = value.strip();
            conText = value.strip();
        } else {
            unconText = value.substring(0, sepIndex).strip();
            conText = value.substring(sepIndex + 1).strip();
        }
        return new MaxIterLimits(convertLimit("uncontrollable", unconText), convertLimit("controllable", conText),
                DEFAULT_FALLBACK_UNCONTR_LIMIT, DEFAULT_FALLBACK_CONTR_LIMIT);
    }

    /** Class to store, transport, and express the entered value of the {@link PlcMaxIterOption}. */
    public static class MaxIterLimits {
        /** Value to use to express 'inf' as number. */
        private static final int INFINITE_VALUE = 0;

        /** Value to use to express 'resp' as number. */
        private static final int BOUNDED_RESPONSE_VALUE = -1;

        /** Limit for uncontrollable events, as stated by the user. */
        private final int uncontrollableLimit;

        /** Limit for controllable events, as stated by the user. */
        private final int controllableLimit;

        /**
         * Limit for uncontrollable events, if the user requested using the bounded response property but that is not
         * available.
         */
        private final int fallbackUncontrollableLimit;

        /**
         * Limit for controllable events, if the user requested using the bounded response property but that is not
         * available.
         */
        private final int fallbackControllableLimit;

        /**
         * Constructor of the {@link MaxIterLimits}.
         *
         * @param uncontrollableLimit Limit for uncontrollable events, as stated by the user.
         * @param controllableLimit Limit for controllable events, as stated by the user.
         * @param fallbackUncontrollableLimit Limit for uncontrollable events, if the user requested using the bounded
         *     response property but that is not available.
         * @param fallbackControllableLimit Limit for controllable events, if the user requested using the bounded
         *     response property but that is not available.
         */
        public MaxIterLimits(int uncontrollableLimit, int controllableLimit,
                int fallbackUncontrollableLimit, int fallbackControllableLimit)
        {
            this.uncontrollableLimit = uncontrollableLimit;
            this.controllableLimit = controllableLimit;
            this.fallbackUncontrollableLimit = fallbackUncontrollableLimit;
            this.fallbackControllableLimit = fallbackControllableLimit;

            // Fallback values must not point at bounded response, since they are used when it is not available.
            Assert.check(convertToKind(fallbackUncontrollableLimit) != IterLimitKind.BOUNDED_RESPONSE);
            Assert.check(convertToKind(fallbackControllableLimit) != IterLimitKind.BOUNDED_RESPONSE);

            // Check the suggested values as well.
            Assert.check(convertToKind(INFINITE_VALUE) == IterLimitKind.INFINITE);
            Assert.check(convertToKind(BOUNDED_RESPONSE_VALUE) == IterLimitKind.BOUNDED_RESPONSE);
        }

        /**
         * Get the kind of iteration limit for uncontrollable events.
         *
         * @param fallback If {@code false} the limit as specified by the user is returned. If {@code true}, the limit
         *     to use instead of the bounded response limit, if the user requested it but it is not available.
         * @return The kind of iteration limit for uncontrollable events.
         */
        public IterLimitKind getUncontrollableLimitKind(boolean fallback) {
            Assert.implies(fallback, convertToKind(uncontrollableLimit) == IterLimitKind.BOUNDED_RESPONSE);

            int v = fallback ? fallbackUncontrollableLimit : uncontrollableLimit;
            return convertToKind(v);
        }

        /**
         * Get the kind of iteration limit for controllable events.
         *
         * @param fallback If {@code false} the limit as specified by the user is returned. If {@code true}, the limit
         *     to use instead of the bounded response limit, if the user requested it but it is not available.
         * @return The kind of iteration limit for controllable events.
         */
        public IterLimitKind getControllableLimitKind(boolean fallback) {
            Assert.implies(fallback, convertToKind(controllableLimit) == IterLimitKind.BOUNDED_RESPONSE);

            int v = fallback ? fallbackControllableLimit : controllableLimit;
            return convertToKind(v);
        }

        /**
         * The maximum number of iterations to allow for uncontrollable events, in case
         * {@code getUncontrollableLimitKind(fallback)} returned {@link IterLimitKind#INTEGER}.
         *
         * @param fallback If {@code false} the limit as specified by the user is returned. If {@code true}, the limit
         *     to use instead of the bounded response limit, if the user requested it but it is not available.
         * @return The maximum number of iterations to allow for controllable events if the limit is an integer.
         * @throws AssertionError If the limit is not an integer.
         * @see #getUncontrollableLimitKind
         */
        public int getUncontrollableLimitNumber(boolean fallback) {
            Assert.implies(fallback, convertToKind(uncontrollableLimit) == IterLimitKind.BOUNDED_RESPONSE);

            int v = fallback ? fallbackUncontrollableLimit : uncontrollableLimit;
            Assert.areEqual(convertToKind(v), IterLimitKind.INTEGER);
            return v;
        }

        /**
         * The maximum number of iterations to allow for controllable events, in case
         * {@code getControllableLimitKind(fallback)} returned {@link IterLimitKind#INTEGER}.
         *
         * @param fallback If {@code false} the limit as specified by the user is returned. If {@code true}, the limit
         *     to use instead of the bounded response limit, if the user requested it but it is not available.
         * @return The maximum number of iterations to allow for controllable events if the limit is an integer.
         * @throws AssertionError If the limit is not an integer.
         * @see #getControllableLimitKind
         */
        public int getControllableLimitNumber(boolean fallback) {
            Assert.implies(fallback, convertToKind(controllableLimit) == IterLimitKind.BOUNDED_RESPONSE);

            int v = fallback ? fallbackControllableLimit : controllableLimit;
            Assert.areEqual(convertToKind(v), IterLimitKind.INTEGER);
            return v;
        }

        /**
         * Authoritative conversion from number to kind of limit.
         *
         * @param value Value to judge.
         * @return How to interpret the given value.
         */
        private static IterLimitKind convertToKind(int value) {
            if (value > 0) {
                return IterLimitKind.INTEGER;
            }
            if (value < 0) {
                return IterLimitKind.BOUNDED_RESPONSE;
            }
            return IterLimitKind.INFINITE;
        }
    }

    /** Available kinds of iteration limits. */
    public enum IterLimitKind {
        /** Limit is a positive integer number. */
        INTEGER,

        /** Limit is infinite. */
        INFINITE,

        /** Limit is decided by bounded response property. */
        BOUNDED_RESPONSE;
    }

    /**
     * Convert the option value string of a limit either to a positive integer number, or to {@code null} for infinite.
     *
     * @param eventKind The kind of events being considered.
     * @param numberText Text to convert.
     * @return The converted limit value.
     */
    private static int convertLimit(String eventKind, String numberText) {
        if (numberText.toLowerCase(Locale.US).equals("inf")) {
            return MaxIterLimits.INFINITE_VALUE;
        } else if (numberText.toLowerCase(Locale.US).equals("resp")) {
            return MaxIterLimits.BOUNDED_RESPONSE_VALUE;
        } else {
            int value;
            try {
                value = Integer.parseInt(numberText);
            } catch (NumberFormatException ex) {
                throw new InvalidOptionException(fmt("PLC maximum iterations option value \"%s\" for %s events "
                        + "is neither recognized as infinite (\"inf\" without quotes) nor as positive integer number.",
                        numberText, eventKind), ex);
            }
            if (value <= 0) {
                throw new InvalidOptionException(
                        fmt("PLC maximum iterations option value \"%d\" for %s events is not a positive number.", value,
                                eventKind));
            }
            return value;
        }
    }
}
