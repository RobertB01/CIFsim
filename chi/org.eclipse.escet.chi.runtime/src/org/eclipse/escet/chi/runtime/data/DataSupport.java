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

package org.eclipse.escet.chi.runtime.data;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/** Class for holding static support functions. */
public class DataSupport {
    /** Dummy constructor for the {@link DataSupport} class. */
    private DataSupport() {
        // Not used
    }

    /**
     * Check an index value for its boundaries, and normalize it to a positive index between {@code 0} and the length.
     *
     * @param value Index value to check and normalize.
     * @param name Name of the index value, used in errors.
     * @param length Length of the collection.
     * @param exclusive Whether the index value is exclusive.
     * @return Normalized index value.
     * @throws ChiSimulatorException If the index is out of bounds.
     */
    private static int normalizeIndex(int value, String name, int length, boolean exclusive) {
        int offset = exclusive ? 1 : 0;
        if (value >= length + offset) {
            String msg = fmt(
                    "%s is out of range, index is %d while the length of the string is %d (maximum index is %d).", name,
                    value, length, length + offset - 1);
            throw new ChiSimulatorException(msg);
        } else if (value < 0) {
            value += length;
            if (value + offset < 0) {
                String msg = fmt("%s is out of range, value is %d, while the lowest allowed value is %d.", name,
                        value - length, -(length + offset));
                throw new ChiSimulatorException(msg);
            }
        }
        return value;
    }

    /**
     * Perform projection in a string.
     *
     * @param str Source string to select a character from.
     * @param value Index denoting which character to take. The index may be negative, meaning to count from the back of
     *     the string.
     * @return The character at the given position.
     * @throws ChiSimulatorException If the index parameter is out of bounds.
     */
    public static String stringProjection(String str, int value) {
        value = normalizeIndex(value, "Index of string projection", str.length(), false);
        return str.substring(value, value + 1);
    }

    /**
     * Take a slice from a string.
     *
     * @param str Source string.
     * @param start Start index of the slice. The index may be negative, meaning to count from the back of the string.
     * @param end Exclusive end index of the slice. The index may be negative, meaning to count from the back of the
     *     string.
     * @param step Step size of the slice.
     * @return The sliced string.
     * @throws ChiSimulatorException If the parameters are incorrect.
     */
    public static String stringSlice(String str, int start, int end, int step) {
        start = normalizeIndex(start, "Start index of string slice", str.length(), false);
        end = normalizeIndex(end, "End index of string slice", str.length(), true);
        if (step == 0) {
            String msg = "Step size of string slice must be non-zero.";
            throw new ChiSimulatorException(msg);
        }
        // Simple, common cases.
        if (step == 1) {
            if (start == 0 && end == str.length()) {
                return str;
            }
            if (start <= end) {
                return str.substring(start, end);
            }
        }

        StringBuilder result = new StringBuilder(2 + Math.abs(start - end) / Math.abs(step));
        if (step > 0) {
            while (start < end) {
                result.append(str.substring(start, start + 1));
                start += step;
            }
        } else {
            while (start > end) {
                result.append(str.substring(start, start + 1));
                start += step;
            }
        }
        return result.toString();
    }
}
