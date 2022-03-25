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

package org.eclipse.escet.cif.simulator.runtime.io;

import static org.eclipse.escet.common.java.Lists.listc;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/**
 * Converter to turn runtime values into textual representations of those runtime values. The textual representations
 * closely resemble the CIF ASCII syntax.
 */
public class RuntimeValueToString {
    /** Constructor for the {@link RuntimeValueToString} class. */
    private RuntimeValueToString() {
        // Static class.
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static String runtimeToString(Object value) {
        if (value instanceof RuntimeToStringable) {
            return runtimeToString((RuntimeToStringable)value);
        } else if (value instanceof Boolean) {
            boolean bvalue = (Boolean)value;
            return runtimeToString(bvalue);
        } else if (value instanceof Integer) {
            int ivalue = (Integer)value;
            return runtimeToString(ivalue);
        } else if (value instanceof Double) {
            double dvalue = (Double)value;
            return runtimeToString(dvalue);
        } else if (value instanceof String) {
            return runtimeToString((String)value);
        } else if (value instanceof List<?>) {
            return runtimeToString((List<?>)value);
        } else if (value instanceof Set<?>) {
            return runtimeToString((Set<?>)value);
        } else if (value instanceof Map<?, ?>) {
            return runtimeToString((Map<?, ?>)value);
        } else {
            throw new RuntimeException("Unknown runtime value: " + value);
        }
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static String runtimeToString(RuntimeToStringable value) {
        return value.toString();
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static String runtimeToString(boolean value) {
        return CifSimulatorMath.boolToStr(value);
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static String runtimeToString(int value) {
        return CifSimulatorMath.intToStr(value);
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static String runtimeToString(double value) {
        return CifSimulatorMath.realToStr(value);
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static String runtimeToString(String value) {
        return "\"" + Strings.escape(value) + "\"";
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param <T> The type of the elements of the list.
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static <T> String runtimeToString(List<T> value) {
        List<String> elems = listc(value.size());
        for (T elem: value) {
            elems.add(runtimeToString(elem));
        }
        return "[" + String.join(", ", elems) + "]";
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param <T> The type of the elements of the set.
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static <T> String runtimeToString(Set<T> value) {
        // Make sure the result will be deterministic.
        Assert.check(value instanceof LinkedHashSet<?>);

        // Return textual representation.
        List<String> elems = listc(value.size());
        for (T elem: value) {
            elems.add(runtimeToString(elem));
        }
        return "{" + String.join(", ", elems) + "}";
    }

    /**
     * Converts the given runtime value into a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param <K> The type of the keys of the dictionary.
     * @param <V> The type of the values of the dictionary.
     * @param value The runtime value.
     * @return The textual representation.
     */
    public static <K, V> String runtimeToString(Map<K, V> value) {
        // Make sure the result will be deterministic.
        Assert.check(value instanceof LinkedHashMap<?, ?>);

        // Return textual representation.
        List<String> elems = listc(value.size());
        for (Entry<K, V> pair: value.entrySet()) {
            elems.add(runtimeToString(pair.getKey()) + ": " + runtimeToString(pair.getValue()));
        }
        return "{" + String.join(", ", elems) + "}";
    }
}
