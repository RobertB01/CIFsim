//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

/** Number helper methods. */
public class Numbers {
    /** Constructor for the {@link Numbers} class. */
    private Numbers() {
        // Static class.
    }

    /**
     * Returns a formatted number. For instance, for {@code "12345"} as input, {@code "12,345"} is returned as result.
     *
     * @param valueText The non-negative integer number to format. Must match regular expression {@code \d+}.
     * @return The formatted number.
     */
    public static String formatNumber(String valueText) {
        return NumberFormat.getNumberInstance(Locale.US).format(new BigInteger(valueText));
    }

    /**
     * Returns a formatted number. For instance, for {@code "-12345"} as input, {@code "-12,345"} is returned as result.
     *
     * @param value The integer number to format.
     * @return The formatted number.
     */
    public static String formatNumber(int value) {
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }

    /**
     * Converts a number to an ordinal text ({@code 1st}, {@code 2nd}, {@code 3rd}, etc).
     *
     * @param i The number to convert, {@code i >= 0}.
     * @return The ordinal text for the number.
     */
    public static String toOrdinal(int i) {
        // Input checking, and special case for '0'.
        Assert.check(i >= 0);
        if (i == 0) {
            return "zeroth";
        }

        // Generic case.
        String suffix = "th";
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                // Deliberately empty.
                break;
            default:
                switch (i % 10) {
                    case 1:
                        suffix = "st";
                        break;
                    case 2:
                        suffix = "nd";
                        break;
                    case 3:
                        suffix = "rd";
                        break;
                }
                break;
        }
        return String.valueOf(i) + suffix;
    }
}
