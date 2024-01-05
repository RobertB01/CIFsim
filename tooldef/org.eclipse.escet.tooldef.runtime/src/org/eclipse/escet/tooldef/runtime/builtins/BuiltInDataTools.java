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

package org.eclipse.escet.tooldef.runtime.builtins;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils.valueToStr;
import static org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils.valueToTypeStr;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.java.FormatDescription.Conversion;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.tooldef.runtime.ToolDefException;
import org.eclipse.escet.tooldef.runtime.ToolDefList;
import org.eclipse.escet.tooldef.runtime.ToolDefMap;
import org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils;
import org.eclipse.escet.tooldef.runtime.ToolDefSet;
import org.eclipse.escet.tooldef.runtime.ToolDefTuplePair;

/** ToolDef built-in data tools. */
public class BuiltInDataTools {
    /** Constructor for the {@link BuiltInDataTools} class. */
    private BuiltInDataTools() {
        // Static class.
    }

    /**
     * Returns the absolute value of an integer number.
     *
     * @param x The integer number.
     * @return The absolute value.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static int abs(int x) {
        if (x == Integer.MIN_VALUE) {
            String msg = fmt("Integer number overflow: abs(%s).", valueToStr(x));
            throw new ToolDefException(msg);
        }

        return Math.abs(x);
    }

    /**
     * Returns the absolute value of a long number.
     *
     * @param x The long number.
     * @return The absolute value.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static long abs(long x) {
        if (x == Long.MIN_VALUE) {
            String msg = fmt("Long number overflow: abs(%s).", valueToStr(x));
            throw new ToolDefException(msg);
        }

        return Math.abs(x);
    }

    /**
     * Returns the absolute value of a double number.
     *
     * @param x The double number.
     * @return The absolute value.
     */
    public static double abs(double x) {
        return Math.abs(x);
    }

    /**
     * Returns the given double number rounded to a whole long number, towards positive infinity.
     *
     * @param x The double number.
     * @return The double number rounded to a whole long number, towards positive infinity.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static long ceil(double x) {
        double rslt = Math.ceil(x);
        if (rslt < Long.MIN_VALUE || rslt > Long.MAX_VALUE) {
            String msg = fmt("Long number overflow: ceil(%s).", valueToStr(x));
            throw new ToolDefException(msg);
        }
        return (long)rslt;
    }

    /**
     * Returns whether a given string is contained in another given string.
     *
     * @param whole The whole string.
     * @param part The part string (potentially contained in the whole string).
     * @return {@code true} if the part string is contained in the whole string, {@code false} otherwise.
     */
    public static boolean contains(String whole, String part) {
        return whole.contains(part);
    }

    /**
     * Returns whether a given value is contained in a given list.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @param elem The value (potential element).
     * @return {@code true} if the value is contained in the list, {@code false} otherwise.
     */
    public static <T> boolean contains(List<T> list, T elem) {
        return list.contains(elem);
    }

    /**
     * Returns whether a given value is contained in a given set.
     *
     * @param <T> The type of the elements of the set.
     * @param set The set.
     * @param elem The value (potential element).
     * @return {@code true} if the value is contained in the set, {@code false} otherwise.
     */
    public static <T> boolean contains(Set<T> set, T elem) {
        return set.contains(elem);
    }

    /**
     * Returns whether a given value is a key of a given map.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param map The map.
     * @param key The value (potential key).
     * @return {@code true} if the value is a key of the map, {@code false} otherwise.
     */
    public static <K, V> boolean contains(Map<K, V> map, K key) {
        return map.containsKey(key);
    }

    /**
     * Returns the given list, with all occurrences of the given element removed from it. If the element does not exist,
     * the list is returned unmodified.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @param elem The element to remove. May be {@code null}.
     * @return The list, with all occurrences of the element removed from it.
     */
    public static <T> List<T> del(List<T> list, T elem) {
        List<T> rslt = new ToolDefList<>(list.size());
        for (T x: list) {
            if (!ToolDefRuntimeUtils.equalValues(x, elem)) {
                rslt.add(x);
            }
        }
        return rslt;
    }

    /**
     * Returns the given set, with the given element removed from it. If the element does not exist, the set is returned
     * unmodified.
     *
     * @param <T> The type of the elements of the set.
     * @param set The set.
     * @param elem The element to remove. May be {@code null}.
     * @return The set, with the element removed from it.
     */
    public static <T> Set<T> del(Set<T> set, T elem) {
        Set<T> rslt = new ToolDefSet<>(set);
        rslt.remove(elem);
        return rslt;
    }

    /**
     * Returns the given map, with the given entry with the given key removed from it. If the key does not exist, the
     * map is returned unmodified.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param map The map.
     * @param key The key to remove. May be {@code null}.
     * @return The map, with the entry with the given key removed from it.
     */
    public static <K, V> Map<K, V> del(Map<K, V> map, K key) {
        Map<K, V> rslt = new ToolDefMap<>(map);
        rslt.remove(key);
        return rslt;
    }

    /**
     * Removes an element from a list, and returns the list without that element.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @param index The 0-based index into the list of the element to remove. Negative indices are allowed, and count
     *     from the right.
     * @return The list with the element removed from it.
     * @throws ToolDefException If the index is out of bounds for the list.
     */
    public static <T> List<T> delidx(List<T> list, int index) {
        // Normalize index.
        int normalizedIdx = index;
        if (normalizedIdx < 0) {
            normalizedIdx = list.size() + normalizedIdx;
        }

        // Check for out of bounds index.
        if (normalizedIdx < 0 || normalizedIdx >= list.size()) {
            String msg = fmt("Index out of bounds: delidx(%s, %s).", valueToStr(list), index);
            throw new ToolDefException(msg);
        }

        // Perform the operation.
        List<T> rslt = new ToolDefList<>(list);
        rslt.remove(normalizedIdx);
        return rslt;
    }

    /**
     * Returns whether a given string is empty (length {@code 0}).
     *
     * @param x The string.
     * @return {@code true} if the given string is empty, {@code false} otherwise.
     */
    public static boolean empty(String x) {
        return x.isEmpty();
    }

    /**
     * Returns whether a given list is empty.
     *
     * @param <T> The type of the elements of the list.
     * @param x The list.
     * @return {@code true} if the given list is empty, {@code false} otherwise.
     */
    public static <T> boolean empty(List<T> x) {
        return x.isEmpty();
    }

    /**
     * Returns whether a given set is empty.
     *
     * @param <T> The type of the elements of the set.
     * @param x The set.
     * @return {@code true} if the given set is empty, {@code false} otherwise.
     */
    public static <T> boolean empty(Set<T> x) {
        return x.isEmpty();
    }

    /**
     * Returns whether a given map is empty.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param x The map.
     * @return {@code true} if the given map is empty, {@code false} otherwise.
     */
    public static <K, V> boolean empty(Map<K, V> x) {
        return x.isEmpty();
    }

    /**
     * Returns whether the first given string ends with the second given string. For an empty suffix, always returns
     * {@code true}.
     *
     * @param whole The first given string.
     * @param suffix The second given string.
     * @return {@code true} if the first given string ends with the second given string, {@code false} otherwise.
     */
    public static boolean endswith(String whole, String suffix) {
        return whole.endsWith(suffix);
    }

    /**
     * Returns a list with 2-tuples of keys and values for all entries of the given map.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param map The map.
     * @return The list with 2-tuples of keys and values for all entries of the given map.
     */
    public static <K, V> List<ToolDefTuplePair<K, V>> entries(Map<K, V> map) {
        List<ToolDefTuplePair<K, V>> rslt = new ToolDefList<>(map.size());
        for (Entry<K, V> entry: map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            rslt.add(new ToolDefTuplePair<>(key, value));
        }
        return rslt;
    }

    /**
     * Returns a list with 2-tuples of 0-based indices and elements for all elements of the given list.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @return The list with 2-tuples of 0-based indices and elements for all elements of the given list.
     */
    public static <T> List<ToolDefTuplePair<Integer, T>> enumerate(List<T> list) {
        List<ToolDefTuplePair<Integer, T>> rslt = new ToolDefList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            T elem = list.get(i);
            rslt.add(new ToolDefTuplePair<>(i, elem));
        }
        return rslt;
    }

    /**
     * Returns the given double number rounded to a whole long number, towards negative infinity.
     *
     * @param x The double number.
     * @return The double number rounded to a whole long number, towards negative infinity.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static long floor(double x) {
        double rslt = Math.floor(x);
        if (rslt < Long.MIN_VALUE || rslt > Long.MAX_VALUE) {
            String msg = fmt("Long number overflow: floor(%s).", valueToStr(x));
            throw new ToolDefException(msg);
        }
        return (long)rslt;
    }

    /**
     * Formats a text based on a pattern and arguments.
     *
     * @param pattern The format pattern.
     * @param args The arguments.
     * @return The formatted text.
     * @throws ToolDefException If the format pattern is invalid or if the pattern and arguments don't match.
     */
    public static String fmtToolDef(String pattern, Object... args) {
        // Decode pattern.
        FormatDecoder decoder = new FormatDecoder();
        List<FormatDescription> parts = decoder.decode(pattern);

        // Handle errors.
        for (FormatDescription part: parts) {
            if (part.conversion != Conversion.ERROR) {
                continue;
            }

            String msg = fmt("Invalid format pattern: %s (problem at position %d..%d).", valueToStr(pattern),
                    part.offset + 1, part.offset + part.length);
            throw new ToolDefException(msg, new ToolDefException(part.text));
        }

        // Process all parts.
        StringBuilder rslt = new StringBuilder();
        int implicitIndex = 0;
        for (FormatDescription part: parts) {
            // Literal.
            if (part.conversion == Conversion.LITERAL) {
                rslt.append(part.text);
                continue;
            }

            // Get 0-based index of specifier.
            int idx;
            if (!part.index.isEmpty()) {
                idx = part.getExplicitIndex();
                if (idx == -1) {
                    String msg = "Invalid format specifier: the explicit index causes integer overflow.";
                    ToolDefException cause = new ToolDefException(msg);

                    msg = fmt("Invalid format pattern: %s (problem at position %d..%d).", valueToStr(pattern),
                            part.offset + 1, part.offset + part.length);
                    throw new ToolDefException(msg, cause);
                }
                idx--;
            } else {
                idx = implicitIndex;
                implicitIndex++;
            }

            // Check 0-based index against available arguments.
            if (idx < 0 || idx >= args.length) {
                String msg = fmt("Invalid format specifier: the %s value is used, which does not exist.",
                        Numbers.toOrdinal(idx + 1));
                ToolDefException cause = new ToolDefException(msg);

                msg = fmt("Invalid format pattern: %s (problem at position %d..%d).", valueToStr(pattern),
                        part.offset + 1, part.offset + part.length);
                throw new ToolDefException(msg, cause);
            }

            // Get value.
            Object value = args[idx];

            // Add to formatted result.
            switch (part.conversion) {
                case BOOLEAN: {
                    if (value != null && !(value instanceof Boolean)) {
                        String msg = fmt(
                                "Invalid \"%%%s\" format specifier: a value of type \"bool\" or \"bool?\" "
                                        + "is required, but the %s value of type \"%s\" is used.",
                                part.text, Numbers.toOrdinal(idx + 1), valueToTypeStr(value));
                        ToolDefException cause = new ToolDefException(msg);

                        msg = fmt("Invalid format pattern: %s (problem at position %d..%d).", valueToStr(pattern),
                                part.offset + 1, part.offset + part.length);
                        throw new ToolDefException(msg, cause);
                    }

                    String txt = Strings.fmt(part.toString(false), value);
                    rslt.append(txt);
                    break;
                }

                case INTEGER: {
                    if (!(value instanceof Integer) && !(value instanceof Long)) {
                        String msg = fmt(
                                "Invalid \"%%%s\" format specifier: a value of type \"int\" or \"long\" "
                                        + "is required, but the %s value of type \"%s\" is used.",
                                part.text, Numbers.toOrdinal(idx + 1), valueToTypeStr(value));
                        ToolDefException cause = new ToolDefException(msg);

                        msg = fmt("Invalid format pattern: %s (problem at position %d..%d).", valueToStr(pattern),
                                part.offset + 1, part.offset + part.length);
                        throw new ToolDefException(msg, cause);
                    }

                    String txt = Strings.fmt(part.toString(false), value);
                    rslt.append(txt);
                    break;
                }

                case REAL: {
                    if (!(value instanceof Integer) && !(value instanceof Long) && !(value instanceof Double)) {
                        String msg = fmt(
                                "Invalid \"%%%s\" format specifier: a value of type \"int\", \"long\", "
                                        + "or \"double\" is required, but the %s value of type \"%s\" is used.",
                                part.text, Numbers.toOrdinal(idx + 1), valueToTypeStr(value));
                        ToolDefException cause = new ToolDefException(msg);

                        msg = fmt("Invalid format pattern: %s (problem at position %d..%d).", valueToStr(pattern),
                                part.offset + 1, part.offset + part.length);
                        throw new ToolDefException(msg, cause);
                    }

                    double v;
                    if (value instanceof Integer) {
                        v = (Integer)value;
                    } else if (value instanceof Long) {
                        v = (Long)value;
                    } else if (value instanceof Double) {
                        v = (Double)value;
                    } else {
                        throw new RuntimeException("Unexpected value: " + value);
                    }
                    String txt = Strings.fmt(part.toString(false), v);
                    rslt.append(txt);
                    break;
                }

                case STRING: {
                    if (!(value instanceof String)) {
                        value = valueToStr(value);
                    }
                    String txt = Strings.fmt(part.toString(false), value);
                    rslt.append(txt);
                    break;
                }

                default:
                    String msg = "Unexpected: " + part.conversion;
                    throw new RuntimeException(msg);
            }
        }
        return rslt.toString();
    }

    /**
     * Returns the 0-based index of the first occurrence of the second given string in the first given string, from left
     * to right of the first given string. Returns {@code -1} if the second given string doesn't occur in the first
     * given string. For an empty second string, always returns {@code 0}.
     *
     * @param whole The first given string.
     * @param part The second given string.
     * @return The 0-based index of the first occurrence, or {@code -1}.
     */
    public static int indexof(String whole, String part) {
        return whole.indexOf(part);
    }

    /**
     * Returns the 0-based index of the first occurrence of the second given string in the first given string, from left
     * to right of the first given string, at or after the given 0-based offset. Returns {@code -1} if the second given
     * string doesn't occur in the first given string, at or after the given offset. For an empty second string, always
     * returns the given offset, unless there is no character at the given offset, in which case {@code -1} is always
     * returned.
     *
     * @param whole The first given string.
     * @param part The second given string.
     * @param offset The 0-based offset.
     * @return The 0-based index of the first occurrence at or after the given 0-based offset, or {@code -1}.
     */
    public static int indexof(String whole, String part, int offset) {
        return whole.indexOf(part, offset);
    }

    /**
     * Returns the 0-based index of the first occurrence of the given value in the first list, from left to right of the
     * elements of the list. Returns {@code -1} if the given value doesn't occur in the list.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @param elem The value.
     * @return The 0-based index of the first occurrence, or {@code -1}.
     */
    public static <T> int indexof(List<T> list, T elem) {
        return list.indexOf(elem);
    }

    /**
     * Returns the 0-based index of the first occurrence of the given value in the first list, from left to right of the
     * elements of the list, at or after the given 0-based offset. Returns {@code -1} if the given value doesn't occur
     * in the list, at or after the given offset.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @param elem The value.
     * @param offset The 0-based offset.
     * @return The 0-based index of the first occurrence at or after the given 0-based offset, or {@code -1}.
     */
    public static <T> int indexof(List<T> list, T elem, int offset) {
        int size = list.size();
        if (offset < 0) {
            offset = 0;
        }
        if (offset > size) {
            offset = size;
        }
        int rslt = list.subList(offset, size).indexOf(elem);
        if (rslt >= 0) {
            rslt += offset;
        }
        return rslt;
    }

    /**
     * Returns the given texts joined together, without any separator.
     *
     * @param texts The texts to join.
     * @return The joined texts.
     */
    public static String join(List<String> texts) {
        return String.join("", texts);
    }

    /**
     * Returns the given texts joined together, using a given separator.
     *
     * @param texts The texts to join.
     * @param separator The separator to use.
     * @return The joined texts.
     */
    public static String join(List<String> texts, String separator) {
        return String.join(separator, texts);
    }

    /**
     * Returns a set with the keys of the given map.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param map The map.
     * @return The set with the keys of the map.
     */
    public static <K, V> Set<K> keys(Map<K, V> map) {
        Set<K> keys = new ToolDefSet<>(map.size());
        for (Entry<K, V> entry: map.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    /**
     * Returns the 0-based index of the last occurrence of the second given string in the first given string, from left
     * to right of the first given string. Returns {@code -1} if the second given string doesn't occur in the first
     * given string. For an empty second string, always returns the size of the first given string.
     *
     * @param whole The first given string.
     * @param part The second given string.
     * @return The 0-based index of the last occurrence, or {@code -1}.
     */
    public static int lastindexof(String whole, String part) {
        return whole.lastIndexOf(part);
    }

    /**
     * Returns the 0-based index of the last occurrence of the second given string in the first given string, from left
     * to right of the first given string, at or before the given 0-based offset. Returns {@code -1} if the second given
     * string doesn't occur in the first given string, at or before the given offset. For an empty second string, always
     * returns the given offset, unless there is no character at the given offset, in which case {@code -1} is always
     * returned.
     *
     * @param whole The first given string.
     * @param part The second given string.
     * @param offset The 0-based offset.
     * @return The 0-based index of the last occurrence at or before the given 0-based offset, or {@code -1}.
     */
    public static int lastindexof(String whole, String part, int offset) {
        return whole.lastIndexOf(part, offset);
    }

    /**
     * Returns the 0-based index of the last occurrence of the given value in the first list, from left to right of the
     * elements of the list. Returns {@code -1} if the given value doesn't occur in the list.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @param elem The value.
     * @return The 0-based index of the last occurrence, or {@code -1}.
     */
    public static <T> int lastindexof(List<T> list, T elem) {
        return list.lastIndexOf(elem);
    }

    /**
     * Returns the 0-based index of the last occurrence of the given value in the first list, from left to right of the
     * elements of the list, at or before the given 0-based offset. Returns {@code -1} if the given value doesn't occur
     * in the list, at or before the given offset.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @param elem The value.
     * @param offset The 0-based offset.
     * @return The 0-based index of the last occurrence at or before the given 0-based offset, or {@code -1}.
     */
    public static <T> int lastindexof(List<T> list, T elem, int offset) {
        int size = list.size();
        offset++; // Include the element at the offset.
        if (offset < 0) {
            offset = 0;
        }
        if (offset > size) {
            offset = size;
        }
        return list.subList(0, offset).lastIndexOf(elem);
    }

    /**
     * Returns the natural logarithm of a double number.
     *
     * @param x The double number.
     * @return The natural logarithm of the double number.
     * @throws ToolDefException If the double number is not positive.
     */
    public static double ln(double x) {
        if (x <= 0.0) {
            String msg = fmt("Invalid operation: ln(%s).", valueToStr(x));
            throw new ToolDefException(msg);
        }
        return Math.log(x);
    }

    /**
     * Returns the logarithm (base 10) of a double number.
     *
     * @param x The double number.
     * @return The logarithm (base 10) of the double number.
     * @throws ToolDefException If the double number is not positive.
     */
    public static double log(double x) {
        if (x <= 0.0) {
            String msg = fmt("Invalid operation: log(%s).", valueToStr(x));
            throw new ToolDefException(msg);
        }
        return Math.log10(x);
    }

    /**
     * Returns the given text, converted to lower case. Uses {@link Locale#US}.
     *
     * @param text The text.
     * @return The text, in lower case.
     */
    public static String lower(String text) {
        return text.toLowerCase(Locale.US);
    }

    /**
     * Returns the given text with whitespace at the left/start of the text removed.
     *
     * @param text The text.
     * @return The text with whitespace at the left/start of the text removed.
     */
    public static String ltrim(String text) {
        return StringUtils.stripStart(text, null);
    }

    /**
     * Returns the maximum of some integer numbers. If no numbers are given, {@link Integer#MIN_VALUE} is returned.
     *
     * @param x The integer numbers.
     * @return The maximum number.
     */
    public static int max(int... x) {
        int rslt = Integer.MIN_VALUE;
        for (int y: x) {
            if (y > rslt) {
                rslt = y;
            }
        }
        return rslt;
    }

    /**
     * Returns the maximum of some long numbers. If no numbers are given, {@link Long#MIN_VALUE} is returned.
     *
     * @param x The long numbers.
     * @return The maximum number.
     */
    public static long max(long... x) {
        long rslt = Long.MIN_VALUE;
        for (long y: x) {
            if (y > rslt) {
                rslt = y;
            }
        }
        return rslt;
    }

    /**
     * Returns the maximum of some double numbers. If no numbers are given, '- {@link Double#MAX_VALUE}' is returned.
     *
     * @param x The double numbers.
     * @return The maximum number.
     */
    public static double max(double... x) {
        double rslt = -Double.MAX_VALUE;
        for (double y: x) {
            if (y > rslt) {
                rslt = y;
            }
        }
        return rslt;
    }

    /**
     * Returns the minimum of some integer numbers. If no numbers are given, {@link Integer#MAX_VALUE} is returned.
     *
     * @param x The integer numbers.
     * @return The minimum number.
     */
    public static int min(int... x) {
        int rslt = Integer.MAX_VALUE;
        for (int y: x) {
            if (y < rslt) {
                rslt = y;
            }
        }
        return rslt;
    }

    /**
     * Returns the minimum of some long numbers. If no numbers are given, {@link Long#MAX_VALUE} is returned.
     *
     * @param x The long numbers.
     * @return The minimum number.
     */
    public static long min(long... x) {
        long rslt = Long.MAX_VALUE;
        for (long y: x) {
            if (y < rslt) {
                rslt = y;
            }
        }
        return rslt;
    }

    /**
     * Returns the minimum of some double numbers. If no numbers are given, {@link Double#MAX_VALUE} is returned.
     *
     * @param x The double numbers.
     * @return The minimum number.
     */
    public static double min(double... x) {
        double rslt = Double.MAX_VALUE;
        for (double y: x) {
            if (y < rslt) {
                rslt = y;
            }
        }
        return rslt;
    }

    /**
     * Returns the exponentiation (power) of two double numbers.
     *
     * @param base The base double number.
     * @param exponent The exponent double number.
     * @return The exponentiation result.
     * @throws ToolDefException If the operation results in double overflow, or {@code NaN}.
     */
    public static double pow(double base, double exponent) {
        // Assumes that the arguments are valid doubles (no inf/NaN/-0.0).
        double rslt = Math.pow(base, exponent);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Double number overflow: pow(%s, %s).", valueToStr(base), valueToStr(exponent));
            throw new ToolDefException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: pow(%s, %s).", valueToStr(base), valueToStr(exponent));
            throw new ToolDefException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns a list of numbers representing the range [0..count-1]. That is, for {@code count} = {@code 5}, list
     * {@code [0, 1, 2, 3, 4]} is returned.
     *
     * @param count The number of elements in the resulting list. Negative counts are treated as zero.
     * @return The range [0..count-1].
     */
    public static List<Integer> range(int count) {
        List<Integer> rslt = new ToolDefList<>(count >= 0 ? count : 0);
        for (int i = 0; i < count; i++) {
            rslt.add(i);
        }
        return rslt;
    }

    /**
     * Returns a list of numbers representing the range [begin..end]. That is, for {@code begin} = {@code -3} and
     * {@code end} = {@code 2}, list {@code [-3, -2, -1, 0, 1, 2]} is returned.
     *
     * @param begin The lower bound of the range.
     * @param end The upper bound of the range.
     * @return The range [begin..end].
     */
    public static List<Integer> range(int begin, int end) {
        int cnt = end - begin + 1;
        if (cnt < 0) {
            cnt = 0;
        }
        List<Integer> rslt = new ToolDefList<>(cnt);

        for (int i = begin; i <= end; i++) {
            rslt.add(i);
        }
        return rslt;
    }

    /**
     * Returns a list of numbers representing the range [0..size(list)-1]. That is, for a list with {@code 5} elements,
     * list {@code [0, 1, 2, 3, 4]} is returned.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @return The range [0..size(list)-1].
     */
    public static <T> List<Integer> range(List<T> list) {
        return range(list.size());
    }

    /**
     * Returns the string with old sub strings replaced by new text. The replacement proceeds from the beginning of the
     * string to the end. That is, replacing {@code "aa"} with {@code "b"} in the string {@code "aaa"} will result in
     * {@code "ba"} rather than {@code "ab"}.
     *
     * @param text The text (string) in which to replace text.
     * @param oldtext The text (sub string) to replace.
     * @param newtext The replacement text.
     * @return The text with all replacements applied.
     */
    public static String replace(String text, String oldtext, String newtext) {
        return text.replace(oldtext, newtext);
    }

    /**
     * Returns the list with old elements replaced by new elements. The replacement is performed for all matching old
     * elements.
     *
     * @param <T> The type of the elements of the lists.
     * @param list The list in which to replace elements.
     * @param oldelem The element to replace. May be {@code null}.
     * @param newelem The replacement element. May be {@code null}.
     * @return The list with all replacements applied.
     */
    public static <T> List<T> replace(List<T> list, T oldelem, T newelem) {
        List<T> rslt = new ToolDefList<>(list);
        for (int i = 0; i < rslt.size(); i++) {
            if (ToolDefRuntimeUtils.equalValues(rslt.get(i), oldelem)) {
                rslt.set(i, newelem);
            }
        }
        return rslt;
    }

    /**
     * Returns the reverse of the given string.
     *
     * @param text The string.
     * @return The reverse string.
     */
    public static String reverse(String text) {
        return StringUtils.reverse(text);
    }

    /**
     * Returns the reverse of the given list.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @return The reverse list.
     */
    public static <T> List<T> reverse(List<T> list) {
        List<T> rslt = new ToolDefList<>(list);
        Collections.reverse(rslt);
        return rslt;
    }

    /**
     * Returns the given double number rounded to the closest whole long number, with ties being rounded toward positive
     * infinity.
     *
     * @param x The double number.
     * @return The rounded number.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static long round(double x) {
        if (x < Long.MIN_VALUE - 0.5 || x >= Long.MAX_VALUE + 0.5) {
            String msg = fmt("Long number overflow: round(%s).", valueToStr(x));
            throw new ToolDefException(msg);
        }
        long rslt = Math.round(x);
        return (int)rslt;
    }

    /**
     * Returns the given text with whitespace at the right/end of the text removed.
     *
     * @param text The text.
     * @return The text with whitespace at the right/end of the text removed.
     */
    public static String rtrim(String text) {
        return StringUtils.stripEnd(text, null);
    }

    /**
     * Returns the size (number of characters) of the given string.
     *
     * @param x The string.
     * @return The size.
     */
    public static int size(String x) {
        return x.length();
    }

    /**
     * Returns the size (number of elements) of the given list.
     *
     * @param <T> The type of the elements of the list.
     * @param x The list.
     * @return The size.
     */
    public static <T> int size(List<T> x) {
        return x.size();
    }

    /**
     * Returns the size (number of elements) of the given set.
     *
     * @param <T> The type of the elements of the set.
     * @param x The set.
     * @return The size.
     */
    public static <T> int size(Set<T> x) {
        return x.size();
    }

    /**
     * Returns the size (number of entries) of the given map.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param x The map.
     * @return The size.
     */
    public static <K, V> int size(Map<K, V> x) {
        return x.size();
    }

    /**
     * Returns the given list, with the elements sorted in ascending order.
     *
     * @param <T> The type of the elements of the list.
     * @param list The list.
     * @return The sorted list.
     */
    public static <T> List<T> sorted(List<T> list) {
        List<T> rslt = new ToolDefList<>(list);
        Collections.sort(rslt, ToolDefRuntimeUtils::compareValues);
        return rslt;
    }

    /**
     * Returns the given set as a list, with the elements sorted in ascending order.
     *
     * @param <T> The type of the elements of the set/list.
     * @param set The set.
     * @return The sorted list.
     */
    public static <T> List<T> sorted(Set<T> set) {
        List<T> rslt = new ToolDefList<>(set);
        Collections.sort(rslt, ToolDefRuntimeUtils::compareValues);
        return rslt;
    }

    /**
     * Splits the given text at all non-overlapping occurrences of the given separator.
     *
     * @param text The text to split.
     * @param separator The separator text. May be empty or {@code null} to split on whitespace.
     * @param removeEmpty Whether to remove empty parts (in case the text starts or ends with the separator, or in case
     *     the text contains consecutive separators), or not.
     * @return The parts of the original text resulting from splitting the original text at the separators.
     */
    public static List<String> split(String text, String separator, boolean removeEmpty) {
        String[] parts;
        if (removeEmpty) {
            parts = StringUtils.splitByWholeSeparator(text, separator);
        } else {
            parts = StringUtils.splitByWholeSeparatorPreserveAllTokens(text, separator);
        }
        List<String> rslt = new ToolDefList<>(parts.length);
        for (String part: parts) {
            rslt.add(part);
        }
        return rslt;
    }

    /**
     * Returns the square root of a double number.
     *
     * @param x The double number.
     * @return The square root.
     * @throws ToolDefException If the double number is negative.
     */
    public static double sqrt(double x) {
        // Assumes that the argument is never -0.0.
        if (x < 0.0) {
            String msg = fmt("Invalid operation: sqrt(%s).", valueToStr(x));
            throw new ToolDefException(msg);
        }
        return Math.sqrt(x);
    }

    /**
     * Returns whether the first given string starts with the second given string. For an empty prefix, always returns
     * {@code true}.
     *
     * @param whole The first given string.
     * @param prefix The second given string.
     * @return {@code true} if the first given string starts with the second given string, {@code false} otherwise.
     */
    public static boolean startswith(String whole, String prefix) {
        return whole.startsWith(prefix);
    }

    /**
     * Converts the given ToolDef value into a textual representation, closely resembling the ToolDef ASCII syntax.
     *
     * <p>
     * {@code str(value) = fmt("%s", value)}
     * </p>
     *
     * <p>
     * Values of type string as returned as provided. Values of type string included in containers such as lists are
     * escaped and surrounded by double quotes, as in the ToolDef ASCII syntax.
     * </p>
     *
     * @param value The value to convert to a textual representation.
     * @return The textual representation.
     */
    public static String str(Object value) {
        if (value instanceof String) {
            return (String)value;
        }
        return ToolDefRuntimeUtils.valueToStr(value);
    }

    /**
     * Duplicates a string a given number of times.
     *
     * @param text The string to duplicate.
     * @param count The number of times that the input string should occur in the output.
     * @return The concatenation of {@code count} times the {@code text}.
     * @throws ToolDefException If the count is negative.
     */
    public static String strdup(String text, int count) {
        if (count < 0) {
            String msg = fmt("Invalid negative count for strdup: %s.", valueToStr(count));
            throw new ToolDefException(msg);
        }
        return Strings.duplicate(text, count);
    }

    /**
     * Returns whether the first given set is a subset (proper subset or equal) of the second given set.
     *
     * @param <T> The type of the elements of the sets.
     * @param part The first set.
     * @param whole The second set.
     * @return {@code true} if the first given set is a subset of the second given set, {@code false} otherwise.
     */
    public static <T> boolean subset(Set<T> part, Set<T> whole) {
        return whole.containsAll(part);
    }

    /**
     * Returns the given text with whitespace at the left/start and right/end of the text removed.
     *
     * @param text The text.
     * @return The text with whitespace at the left/start and right/end of the text removed.
     */
    public static String trim(String text) {
        return StringUtils.strip(text, null);
    }

    /**
     * Returns the given text, converted to upper case. Uses {@link Locale#US}.
     *
     * @param text The text.
     * @return The text, in upper case.
     */
    public static String upper(String text) {
        return text.toUpperCase(Locale.US);
    }

    /**
     * Returns a set with the values of the given map.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param map The map.
     * @return The set with the values of the map.
     */
    public static <K, V> Set<V> values(Map<K, V> map) {
        // Initial size optimized for all unique values. If there are
        // duplicate values, the set could have been allocated smaller.
        Set<V> values = new ToolDefSet<>(map.size());
        for (Entry<K, V> entry: map.entrySet()) {
            values.add(entry.getValue());
        }
        return values;
    }
}
