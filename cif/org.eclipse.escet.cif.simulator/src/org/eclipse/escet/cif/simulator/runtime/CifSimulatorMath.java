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

package org.eclipse.escet.cif.simulator.runtime;

import static org.eclipse.escet.cif.simulator.runtime.io.RuntimeValueToString.runtimeToString;
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.copy;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.FastMath;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Lists;
import org.eclipse.escet.common.java.Strings;

/** CIF simulator math functions. See also the {@link CifMath} class. */
public class CifSimulatorMath {
    /** Constructor for the {@link CifSimulatorMath} class. */
    private CifSimulatorMath() {
        // Static class.
    }

    /**
     * Returns the absolute value of an integer number.
     *
     * @param x The integer number.
     * @return {@code abs(x)}.
     * @throws CifSimulatorException If the operation results in integer overflow.
     */
    public static int abs(int x) {
        if (x == Integer.MIN_VALUE) {
            String msg = fmt("Integer overflow: abs(%d).", x);
            throw new CifSimulatorException(msg);
        }
        return Math.abs(x);
    }

    /**
     * Returns the absolute value of a real number.
     *
     * @param x The real number.
     * @return {@code abs(x)}.
     */
    public static double abs(double x) {
        return Math.abs(x);
    }

    /**
     * Returns the arc cosine of a real number.
     *
     * @param x The real number.
     * @return {@code acos(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double acos(double x) {
        double rslt = Math.acos(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: acos(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: acos(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc hyperbolic cosine of a real number.
     *
     * @param x The real number.
     * @return {@code acosh(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double acosh(double x) {
        double rslt = FastMath.acosh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: acosh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: acosh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc sine of a real number.
     *
     * @param x The real number.
     * @return {@code asin(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double asin(double x) {
        double rslt = Math.asin(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: asin(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: asin(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc hyperbolic sine of a real number.
     *
     * @param x The real number.
     * @return {@code asinh(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double asinh(double x) {
        double rslt = FastMath.asinh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: asinh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: asinh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc tangent of a real number.
     *
     * @param x The real number.
     * @return {@code atan(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double atan(double x) {
        double rslt = Math.atan(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: atan(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: atan(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the arc hyperbolic tangent of a real number.
     *
     * @param x The real number.
     * @return {@code atanh(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double atanh(double x) {
        double rslt = FastMath.atanh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: atanh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: atanh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the addition of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @return {@code x + y}.
     * @throws CifSimulatorException If the operation results in integer overflow.
     */
    public static int addInt(int x, int y) {
        long rslt = (long)x + (long)y;
        if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
            return (int)rslt;
        }

        String msg = fmt("Integer overflow: %d + %d.", x, y);
        throw new CifSimulatorException(msg);
    }

    /**
     * Returns the addition of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @return {@code x + y}.
     * @throws CifSimulatorException If the operation results in real overflow.
     */
    public static double addReal(double x, double y) {
        double rslt = x + y;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: %s + %s.", realToStr(x), realToStr(y));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the addition of two lists.
     *
     * @param <T> The type of the elements of the lists.
     * @param x The first list.
     * @param y The second list.
     * @return {@code x + y}.
     */
    public static <T> List<T> addList(List<T> x, List<T> y) {
        List<T> rslt = listc(x.size() + y.size());
        rslt.addAll(x);
        rslt.addAll(y);
        return rslt;
    }

    /**
     * Returns the addition of two strings.
     *
     * @param x The first string.
     * @param y The second string.
     * @return {@code x + y}.
     */
    public static String addString(String x, String y) {
        return x + y;
    }

    /**
     * Returns the addition of two dictionaries.
     *
     * @param <K> The type of the keys of the dictionaries.
     * @param <V> The type of the values of the dictionaries.
     * @param x The first dictionary.
     * @param y The second dictionary.
     * @return {@code x + y}.
     */
    public static <K, V> Map<K, V> addDict(Map<K, V> x, Map<K, V> y) {
        Map<K, V> rslt = copy(x);
        rslt.putAll(y);
        return rslt;
    }

    /**
     * Adds the given key/value pairs to the given dictionary, modifying the dictionary in-place, and returning that
     * same dictionary. This method is only used for the construction of dictionary literals.
     *
     * @param <K> The type of the keys of the dictionary.
     * @param <V> The type of the values of the dictionary.
     * @param dict The dictionary.
     * @param ks The keys of the key/value pairs to add.
     * @param vs The values of the key/value pairs to add.
     * @return The input dictionary.
     * @throws CifSimulatorException If one of the keys already exists in the dictionary, and the dictionary literal
     *     thus has a duplicate key.
     */
    public static <K, V> Map<K, V> addpairs(Map<K, V> dict, K[] ks, V[] vs) {
        // Add pairs.
        Assert.check(ks.length == vs.length);
        for (int i = 0; i < ks.length; i++) {
            // Add pair.
            K k = ks[i];
            V v = vs[i];
            V previous = dict.put(k, v);

            // Check for duplicate key.
            if (previous != null) {
                String msg = fmt("Duplicate key in dictionary value: %s.", runtimeToString(k));
                throw new CifSimulatorException(msg);
            }
        }

        // Return in-place modified dictionary.
        return dict;
    }

    /**
     * Converts a Java {@link Boolean} to a CIF boolean value literal, in the CIF ASCII representation.
     *
     * @param x The Java {@link Boolean} value.
     * @return The CIF boolean value literal, in the CIF ASCII representation.
     */
    public static String boolToStr(boolean x) {
        return x ? "true" : "false";
    }

    /**
     * Returns the cube root of a real number.
     *
     * @param x The real number.
     * @return {@code cbrt(x)}.
     */
    public static double cbrt(double x) {
        double rslt = Math.cbrt(x);
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the ceil of a real number.
     *
     * @param x The real number.
     * @return {@code ceil(x)}.
     * @throws CifSimulatorException If the operation results in integer overflow.
     */
    public static int ceil(double x) {
        double rslt = Math.ceil(x);
        if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
            String msg = fmt("Integer overflow: ceil(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (int)rslt;
    }

    /**
     * Returns the cosine of a real number.
     *
     * @param x The real number.
     * @return {@code cos(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double cos(double x) {
        double rslt = Math.cos(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: cos(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: cos(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the hyperbolic cosine of a real number.
     *
     * @param x The real number.
     * @return {@code cosh(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double cosh(double x) {
        double rslt = Math.cosh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: cosh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: cosh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Removes an element from a list, and returns the list without that element.
     *
     * @param <T> The type of the elements of the list.
     * @param lst The list.
     * @param origIdx The 0-based index into the list of the element to remove. Negative indices are allowed, and count
     *     from the right.
     * @return {@code del(lst, origIdx)}.
     * @throws CifSimulatorException If the index is out of bounds for the list.
     */
    public static <T> List<T> delete(List<T> lst, int origIdx) {
        int idx = origIdx;
        if (idx < 0) {
            idx = lst.size() + idx;
        }
        if (idx < 0 || idx >= lst.size()) {
            String msg = fmt("Index out of bounds: del(%s, %s).", runtimeToString(lst), origIdx);
            throw new CifSimulatorException(msg);
        }

        List<T> rslt = copy(lst);
        rslt.remove(idx);
        return rslt;
    }

    /**
     * Returns the integer division of two integer numbers.
     *
     * @param x The dividend.
     * @param y The divisor.
     * @return {@code x div y}.
     * @throws CifSimulatorException If the operation results in integer overflow, or division by zero.
     */
    public static int div(int x, int y) {
        if (y == 0) {
            String msg = fmt("Division by zero: %d div %d.", x, y);
            throw new CifSimulatorException(msg);
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            String msg = fmt("Integer overflow: %d div %d.", x, y);
            throw new CifSimulatorException(msg);
        }
        return x / y;
    }

    /**
     * Returns the real division of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @return {@code x / y}.
     * @throws CifSimulatorException If the operation results in real overflow, or division by zero.
     */
    public static double divide(double x, double y) {
        if (y == 0.0) {
            String msg = fmt("Division by zero: %s / %s.", realToStr(x), realToStr(y));
            throw new CifSimulatorException(msg);
        }
        double rslt = x / y;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: %s * %s.", realToStr(x), realToStr(y));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns {@code empty(x)} for the given list.
     *
     * @param x The list.
     * @return {@code empty(x)}
     */
    public static boolean empty(List<?> x) {
        return x.isEmpty();
    }

    /**
     * Returns {@code empty(x)} for the given set.
     *
     * @param x The set.
     * @return {@code empty(x)}
     */
    public static boolean empty(Set<?> x) {
        return x.isEmpty();
    }

    /**
     * Returns {@code empty(x)} for the given dictionary.
     *
     * @param x The dictionary.
     * @return {@code empty(x)}
     */
    public static boolean empty(Map<?, ?> x) {
        return x.isEmpty();
    }

    /**
     * Returns {@code x = y} for two boolean values.
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first boolean value.
     * @param y The second boolean value.
     * @return {@code x = y}
     */
    public static boolean equal(boolean x, boolean y) {
        return x == y;
    }

    /**
     * Returns {@code x = y} for two boolean values.
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first boolean value.
     * @param y The second boolean value.
     * @return {@code x = y}
     */
    public static boolean equal(Boolean x, boolean y) {
        return x == y;
    }

    /**
     * Returns {@code x = y} for two boolean values.
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first boolean value.
     * @param y The second boolean value.
     * @return {@code x = y}
     */
    public static boolean equal(boolean x, Boolean y) {
        return x == y;
    }

    /**
     * Returns {@code x = y} for two integer values (ignoring their integer type ranges).
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first integer value.
     * @param y The second integer value.
     * @return {@code x = y}
     */
    public static boolean equal(int x, int y) {
        return x == y;
    }

    /**
     * Returns {@code x = y} for two integer values (ignoring their integer type ranges).
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first integer value.
     * @param y The second integer value.
     * @return {@code x = y}
     */
    public static boolean equal(Integer x, int y) {
        return x == y;
    }

    /**
     * Returns {@code x = y} for two integer values (ignoring their integer type ranges).
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first integer value.
     * @param y The second integer value.
     * @return {@code x = y}
     */
    public static boolean equal(int x, Integer y) {
        return x == y;
    }

    /**
     * Returns {@code x = y} for two real values.
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first real value.
     * @param y The second real value.
     * @return {@code x = y}
     */
    public static boolean equal(double x, double y) {
        return x == y;
    }

    /**
     * Returns {@code x = y} for two real values.
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first real value.
     * @param y The second real value.
     * @return {@code x = y}
     */
    public static boolean equal(Double x, double y) {
        return x == y;
    }

    /**
     * Returns {@code x = y} for two real values.
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first real value.
     * @param y The second real value.
     * @return {@code x = y}
     */
    public static boolean equal(double x, Double y) {
        return x == y;
    }

    /**
     * Returns {@code x = y} for two values.
     *
     * <p>
     * This method is named {@code equal} instead of {@code equals} to avoid conflicts with {@link Object#equals} when
     * statically importing this method.
     * </p>
     *
     * @param x The first value.
     * @param y The second value.
     * @return {@code x = y}
     */
    public static boolean equal(Object x, Object y) {
        return x.equals(y);
    }

    /**
     * Returns {@code e}<sup>{@code x}</sup> of a real number {@code x}.
     *
     * @param x The real number.
     * @return {@code exp(x)}.
     * @throws CifSimulatorException If the operation results in real overflow.
     */
    public static double exp(double x) {
        double rslt = Math.exp(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: exp(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the floor of a real number.
     *
     * @param x The real number.
     * @return {@code floor(x)}.
     * @throws CifSimulatorException If the operation results in integer overflow.
     */
    public static int floor(double x) {
        double rslt = Math.floor(x);
        if (rslt < Integer.MIN_VALUE || rslt > Integer.MAX_VALUE) {
            String msg = fmt("Integer overflow: floor(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (int)rslt;
    }

    /**
     * Hashes a given value.
     *
     * @param x The value.
     * @return The hash of the value.
     */
    public static int hash(Object x) {
        return x.hashCode();
    }

    /**
     * Returns {@code x in y} for a value and a list.
     *
     * @param <T> The type of the elements of the list.
     * @param x The value.
     * @param y The list.
     * @return {@code x in y}.
     */
    public static <T> boolean in(T x, List<T> y) {
        return y.contains(x);
    }

    /**
     * Returns {@code x in y} for a value and a set.
     *
     * @param <T> The type of the elements of the set.
     * @param x The value.
     * @param y The set.
     * @return {@code x in y}.
     */
    public static <T> boolean in(T x, Set<T> y) {
        return y.contains(x);
    }

    /**
     * Returns {@code x in y} for a key and a dictionary.
     *
     * @param <K> The type of the keys of the dictionary.
     * @param x The key.
     * @param y The dictionary.
     * @return {@code x in y}.
     */
    public static <K> boolean in(K x, Map<K, ?> y) {
        return y.containsKey(x);
    }

    /**
     * Returns the intersection of two sets.
     *
     * @param <T> The type of the elements of the sets.
     * @param set1 The first set.
     * @param set2 The second set.
     * @return The intersection of the two sets.
     */
    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> rslt = copy(set1);
        rslt.retainAll(set2);
        return rslt;
    }

    /**
     * Converts a Java {@link Integer} to a Java {@link Double}.
     *
     * @param x The Java {@link Integer} value.
     * @return The Java {@link Double} value.
     */
    public static double intToReal(int x) {
        // The 'int' to 'double' conversion is lossless. See
        // https://www.securecoding.cert.org/confluence/display/java/NUM13-J.+Avoid+loss+of+precision+when+converting+primitive+integers+to+floating-point
        return x;
    }

    /**
     * Converts a Java {@link Integer} to a CIF integer value literal, in the CIF ASCII representation.
     *
     * @param x The Java {@link Integer} value.
     * @return The CIF integer value literal, in the CIF ASCII representation.
     */
    public static String intToStr(int x) {
        return Integer.toString(x);
    }

    /**
     * Returns the natural logarithm of a real number.
     *
     * @param x The real number.
     * @return {@code ln(x)}.
     * @throws CifSimulatorException If the real number is non-positive.
     */
    public static double ln(double x) {
        if (x <= 0.0) {
            String msg = fmt("Invalid operation: ln(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return Math.log(x);
    }

    /**
     * Returns the logarithm (base 10) of a real number.
     *
     * @param x The real number.
     * @return {@code log(x)}.
     * @throws CifSimulatorException If the real number is non-positive.
     */
    public static double log(double x) {
        if (x <= 0.0) {
            String msg = fmt("Invalid operation: log(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return Math.log10(x);
    }

    /**
     * Fills the given empty list with the given elements, modifying the list in-place, and returning that same list.
     * This method is only used for the construction of list literals.
     *
     * @param <T> The type of the elements of the list.
     * @param lst The list.
     * @param elems The elements.
     * @return The input list.
     */
    @SafeVarargs
    public static <T> List<T> makelist(List<T> lst, T... elems) {
        for (T elem: elems) {
            lst.add(elem);
        }
        return lst;
    }

    /**
     * Fills the given empty set with the given elements, modifying the set in-place, and returning that same set. This
     * method is only used for the construction of set literals.
     *
     * @param <T> The type of the elements of the set.
     * @param lst The set.
     * @param elems The elements.
     * @return The input set.
     */
    @SafeVarargs
    public static <T> Set<T> makeset(Set<T> lst, T... elems) {
        for (T elem: elems) {
            lst.add(elem);
        }
        return lst;
    }

    /**
     * Returns the maximum of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @return {@code max(x, y)}.
     */
    public static int max(int x, int y) {
        return Math.max(x, y);
    }

    /**
     * Returns the maximum of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @return {@code max(x, y)}.
     */
    public static double max(double x, double y) {
        return Math.max(x, y);
    }

    /**
     * Returns the minimum of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @return {@code min(x, y)}.
     */
    public static int min(int x, int y) {
        return Math.min(x, y);
    }

    /**
     * Returns the minimum of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @return {@code min(x, y)}.
     */
    public static double min(double x, double y) {
        // Assumes that the arguments are never -0.0.
        return Math.min(x, y);
    }

    /**
     * Returns the modulus of two integer numbers.
     *
     * @param x The dividend.
     * @param y The divisor.
     * @return {@code x mod y}.
     * @throws CifSimulatorException If the operation results in division by zero.
     */
    public static int mod(int x, int y) {
        if (y == 0) {
            String msg = fmt("Division by zero: %d mod %d.", x, y);
            throw new CifSimulatorException(msg);
        }
        return x % y;
    }

    /**
     * Creates a shallow copy of the given list, and replaces the element at the given index with a new value.
     *
     * @param <T> The type of the elements of the list.
     * @param lst The list.
     * @param origIdx The 0-based index into the list of the element to replace. Negative indices are allowed, and count
     *     from the right.
     * @param newValue The new value.
     * @return The modified list.
     * @throws CifSimulatorException If the index is out of range for the list.
     */
    public static <T> List<T> modify(List<T> lst, int origIdx, T newValue) {
        // Normalize index and check for out of bounds.
        int idx = origIdx;
        if (idx < 0) {
            idx = lst.size() + idx;
        }
        if (idx < 0 || idx >= lst.size()) {
            String msg = fmt("Index out of bounds: %s[%s].", runtimeToString(lst), origIdx);
            throw new CifSimulatorException(msg);
        }

        // Create a shallow copy, replace the element, and return the list.
        List<T> rslt = copy(lst);
        rslt.set(idx, newValue);
        return rslt;
    }

    /**
     * Creates a shallow copy of the given dictionary, and adds/replaces the pair with the given key and value.
     *
     * @param <K> The type of the keys of the dictionary.
     * @param <V> The type of the values of the dictionary.
     * @param dict The dictionary.
     * @param key The key.
     * @param value The value.
     * @return The modified dictionary.
     */
    public static <K, V> Map<K, V> modify(Map<K, V> dict, K key, V value) {
        // Create a shallow copy, add/replace the pair, and return the
        // dictionary.
        Map<K, V> rslt = copy(dict);
        rslt.put(key, value);
        return rslt;
    }

    /**
     * Returns the multiplication of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @return {@code x * y}.
     * @throws CifSimulatorException If the operation results in integer overflow.
     */
    public static int multiply(int x, int y) {
        long rslt = (long)x * (long)y;
        if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
            return (int)rslt;
        }

        String msg = fmt("Integer overflow: %d * %d.", x, y);
        throw new CifSimulatorException(msg);
    }

    /**
     * Returns the multiplication of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @return {@code x * y}.
     * @throws CifSimulatorException If the operation results in real overflow.
     */
    public static double multiply(double x, double y) {
        double rslt = x * y;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: %s * %s.", realToStr(x), realToStr(y));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the negation of an integer number.
     *
     * @param x The integer number.
     * @return {@code -value}.
     * @throws CifSimulatorException If the operation results in integer overflow.
     */
    public static int negate(int x) {
        if (x == Integer.MIN_VALUE) {
            String msg = fmt("Integer overflow: -%d.", x);
            throw new CifSimulatorException(msg);
        }
        return -x;
    }

    /**
     * Returns the negation of a real number.
     *
     * @param x The real number.
     * @return {@code -value}.
     */
    public static double negate(double x) {
        return (x == 0.0) ? 0.0 : -x;
    }

    /**
     * Returns the exponentiation (power) of two integer numbers.
     *
     * @param x The base integer number.
     * @param y The exponent integer number, {@code y >= 0}.
     * @return {@code pow(x, y)}.
     * @throws CifSimulatorException If the operation results in integer overflow.
     */
    public static int powInt(int x, int y) {
        Assert.check(y >= 0);
        double rslt = Math.pow(x, y);
        if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
            return (int)rslt;
        }
        String msg = fmt("Integer overflow: pow(%d, %d).", x, y);
        throw new CifSimulatorException(msg);
    }

    /**
     * Returns the exponentiation (power) of two real numbers.
     *
     * @param x The base real number.
     * @param y The exponent real number.
     * @return {@code pow(x, y)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double powReal(double x, double y) {
        // Assumes that the arguments are valid doubles (no inf/NaN/-0.0).
        double rslt = Math.pow(x, y);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: pow(%s, %s).", realToStr(x), realToStr(y));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: pow(%s, %s).", realToStr(x), realToStr(y));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Converts a Java {@link Double} to a CIF real value literal, in the CIF ASCII representation.
     *
     * <p>
     * Note that the {@link Double} values may be negative, and the resulting textual representation may thus have a
     * {@code "-"} prefix, unlike real value literals in the CIF ASCII representation, where the {@code "-"} character
     * is a unary operator.
     * </p>
     *
     * @param x The Java {@link Double} value.
     * @return The CIF real value literal, in the CIF ASCII representation.
     */
    public static String realToStr(double x) {
        // Double.toString always results in valid CIF ASCII representations
        // of real values. It also is round-trip compatible (up to fixed point
        // behavior) with strToReal.
        Assert.check(!Double.isNaN(x));
        Assert.check(!Double.isInfinite(x));
        return Double.toString(x).replace('E', 'e');
    }

    /**
     * Projects a list, using a zero-based index.
     *
     * @param <T> The type of the elements of the list.
     * @param lst The list.
     * @param origIdx The 0-based index into the list of the element to return. Negative indices are allowed, and count
     *     from the right.
     * @return {@code lst[origIdx]}.
     * @throws CifSimulatorException If the index is out of range for the list.
     */
    public static <T> T project(List<T> lst, int origIdx) {
        // Normalize index and check for out of bounds.
        int idx = origIdx;
        if (idx < 0) {
            idx = lst.size() + idx;
        }
        if (idx < 0 || idx >= lst.size()) {
            String msg = fmt("Index out of bounds: %s[%s].", runtimeToString(lst), origIdx);
            throw new CifSimulatorException(msg);
        }

        // Return the element.
        return lst.get(idx);
    }

    /**
     * Projects a dictionary, using a key.
     *
     * @param <K> The type of the keys of the dictionary.
     * @param <V> The type of the values of the dictionary.
     * @param dict The dictionary.
     * @param key The key.
     * @return {@code dict[key]}.
     * @throws CifSimulatorException If there is no key/value pair in the dictionary for the given key.
     */
    public static <K, V> V project(Map<K, V> dict, K key) {
        V rslt = dict.get(key);
        if (rslt == null) {
            String msg = fmt("Key not found: %s[%s].", runtimeToString(dict), key);
            throw new CifSimulatorException(msg);
        }
        return rslt;
    }

    /**
     * Projects a string, using a zero-based index.
     *
     * @param str The string.
     * @param origIdx The 0-based index into the string of the character to return. Negative indices are allowed, and
     *     count from the right.
     * @return {@code str[origIdx]}.
     * @throws CifSimulatorException If the index is out of range for the string.
     */
    public static String project(String str, int origIdx) {
        int idx = origIdx;
        if (idx < 0) {
            idx = str.length() + idx;
        }
        if (idx < 0 || idx >= str.length()) {
            String msg = fmt("Index out of bounds: \"%s\"[%s].", Strings.escape(str), origIdx);
            throw new CifSimulatorException(msg);
        }

        return str.substring(idx, idx + 1);
    }

    /**
     * Returns the round of a real number.
     *
     * @param x The real number.
     * @return {@code round(x)}.
     * @throws CifSimulatorException If the operation results in integer overflow.
     */
    public static int round(double x) {
        if (x < Integer.MIN_VALUE - 0.5 || x >= Integer.MAX_VALUE + 0.5) {
            String msg = fmt("Integer overflow: round(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        long rslt = Math.round(x);
        return (int)rslt;
    }

    /**
     * Returns a linearly scaled value.
     *
     * @param v The value to scale.
     * @param inmin The minimum of the input interval.
     * @param inmax The maximum of the input interval.
     * @param outmin The minimum of the output interval.
     * @param outmax The maximum of the output interval.
     * @return {@code scale(v, inmin, inmax, outmin, outmax)}.
     * @throws CifSimulatorException If the input interval is empty, or the operation results in real overflow.
     */
    public static double scale(double v, double inmin, double inmax, double outmin, double outmax) {
        // fraction = (v - inmin) / (inmax - inmin);
        // result = outmin + fraction * (outmax - outmin);
        double inrange = subtract(inmax, inmin);
        if (inrange == 0) {
            String msg = fmt("Empty input interval: scale(%s, %s, %s, %s, %s).", realToStr(v), realToStr(inmin),
                    realToStr(inmax), realToStr(outmin), realToStr(outmax));
            throw new CifSimulatorException(msg);
        }
        double fraction = divide(subtract(v, inmin), inrange);
        return addReal(outmin, multiply(fraction, subtract(outmax, outmin)));
    }

    /**
     * Returns the sign of an integer number.
     *
     * @param x The integer number.
     * @return {@code sign(x)}.
     */
    public static int sign(int x) {
        return (x == 0) ? 0 : (x < 0) ? -1 : 1;
    }

    /**
     * Returns the sign of a real number.
     *
     * @param x The real number.
     * @return {@code sign(x)}.
     */
    public static int sign(double x) {
        return (x == 0.0) ? 0 : (x < 0.0) ? -1 : 1;
    }

    /**
     * Returns the sine of a real number.
     *
     * @param x The real number.
     * @return {@code sin(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double sin(double x) {
        double rslt = Math.sin(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: sin(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: sin(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the hyperbolic sine of a real number.
     *
     * @param x The real number.
     * @return {@code sinh(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double sinh(double x) {
        double rslt = Math.sinh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: sinh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: sinh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns {@code size(x)} for the given string.
     *
     * @param x The string.
     * @return {@code size(x)}
     */
    public static int size(String x) {
        return x.length();
    }

    /**
     * Returns {@code size(x)} for the given list.
     *
     * @param x The list.
     * @return {@code size(x)}
     */
    public static int size(List<?> x) {
        return x.size();
    }

    /**
     * Returns {@code size(x)} for the given set.
     *
     * @param x The set.
     * @return {@code size(x)}
     */
    public static int size(Set<?> x) {
        return x.size();
    }

    /**
     * Returns {@code size(x)} for the given dictionary.
     *
     * @param x The dictionary.
     * @return {@code size(x)}
     */
    public static int size(Map<?, ?> x) {
        return x.size();
    }

    /**
     * Slices a list. A slice is a sub-list of the original (input) list. The begin and end indices can be thought of as
     * the range of elements that should be maintained. That is, in principle, the range of element indexes
     * {@code [beginIndex .. endIndex - 1]} is maintained.
     *
     * <p>
     * Note that:
     * <ul>
     * <li>{@code slice(lst, null, 2) + slice(lst, 2, null) == lst}, for any {@code lst}.</li>
     * <li>Out of range slice indices are handled gracefully. An index that is too large is replaced by the list size. A
     * lower bound larger than the upper bound results in an empty list. This applies to negative indices as well.</li>
     * <li>Indices may be negative numbers, to start counting from the right instead of from the left. That is,
     * {@code -1} can be used instead of {@code lst.size() - 1}, {@code -2} can be used instead of
     * {@code lst.size() - 2}, etc. However, since {@code -0} is really the same as {@code 0}, it does not count from
     * the right.</li>
     * </ul>
     * </p>
     *
     * @param <T> The type of the elements of the list.
     * @param lst The list.
     * @param beginIndex The 0-based begin index (inclusive), or {@code null} for index {@code 0}.
     * @param endIndex The 0-based end index (exclusive), or {@code null} for the size of the input list.
     * @return {@code lst[beginIndex:endIndex]}.
     */
    public static <T> List<T> slice(List<T> lst, Integer beginIndex, Integer endIndex) {
        return Lists.slice(lst, beginIndex, endIndex);
    }

    /**
     * Slices a string. A slice is a sub-string of the original (input) string. The begin and end indices can be thought
     * of as the range of characters that should be maintained. That is, in principle, the range of character indexes
     * {@code [beginIndex .. endIndex - 1]} is maintained.
     *
     * <p>
     * Note that:
     * <ul>
     * <li>{@code slice(str, null, 2) + slice(str, 2, null) == str}, for any {@code str}.</li>
     * <li>Out of range slice indices are handled gracefully. An index that is too large is replaced by the string
     * length. A lower bound larger than the upper bound results in an empty string. This applies to negative indices as
     * well.</li>
     * <li>Indices may be negative numbers, to start counting from the right instead of from the left. That is,
     * {@code -1} can be used instead of {@code str.length() - 1}, {@code -2} can be used instead of
     * {@code str.length() - 2}, etc. However, since {@code -0} is really the same as {@code 0}, it does not count from
     * the right.</li>
     * </ul>
     * </p>
     *
     * @param str The string.
     * @param beginIndex The 0-based begin index (inclusive), or {@code null} for index {@code 0}.
     * @param endIndex The 0-based end index (exclusive), or {@code null} for the length of the input string.
     * @return {@code str[beginIndex:endIndex]}.
     */
    public static String slice(String str, Integer beginIndex, Integer endIndex) {
        return Strings.slice(str, beginIndex, endIndex);
    }

    /**
     * Returns the square root of a real number.
     *
     * @param x The real number.
     * @return {@code sqrt(x)}.
     * @throws CifSimulatorException If the real number is negative.
     */
    public static double sqrt(double x) {
        // Assumes that the argument is never -0.0.
        if (x < 0.0) {
            String msg = fmt("Invalid operation: sqrt(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return Math.sqrt(x);
    }

    /**
     * Converts a CIF boolean value literal, in the CIF ASCII representation, to a Java {@link Boolean}.
     *
     * @param x The CIF boolean value literal, in the CIF ASCII representation.
     * @return The Java {@link Boolean} value.
     * @throws CifSimulatorException If the string value does not represent a boolean value.
     */
    public static boolean strToBool(String x) {
        if (x.equals("true")) {
            return true;
        }
        if (x.equals("false")) {
            return false;
        }

        String msg = fmt("Cast from type \"string\" to type \"bool\" failed: the string value does not represent a "
                + "boolean value: \"%s\".", Strings.escape(x));
        throw new CifSimulatorException(msg);
    }

    /**
     * Converts a CIF integer value literal, in the CIF ASCII representation, to a Java {@link Integer}.
     *
     * <p>
     * See also the {@code CifTypeChecker.transIntExpression} method.
     * </p>
     *
     * @param x The CIF integer value literal, in the CIF ASCII representation.
     * @return The Java {@link Integer} value.
     * @throws CifSimulatorException If the string value does not represent an integer value.
     */
    public static int strToInt(String x) {
        // Integer.parseInt allows all valid integer values in CIF ASCII
        // syntax, as well as negative values (which are unary operator '-'
        // with an integer value in CIF ASCII syntax), and integer values
        // with arbitrary '0' prefixes (not allowed in CIF ASCII syntax).
        try {
            return Integer.parseInt(x);
        } catch (NumberFormatException e) {
            String msg = fmt(
                    "Cast from type \"string\" to type \"int\" failed: the string value does not represent an "
                            + "integer value, or the integer value resulted in integer overflow: \"%s\".",
                    Strings.escape(x));
            throw new CifSimulatorException(msg);
        }
    }

    /**
     * Converts a CIF real value literal, in the CIF ASCII representation, to a Java {@link Double}.
     *
     * <p>
     * See also the {@code CifTypeChecker.transRealExpression} method.
     * </p>
     *
     * @param x The CIF real value literal, in the CIF ASCII representation.
     * @return The Java {@link Double} value.
     * @throws CifSimulatorException If the string value does not represent an integer value.
     */
    public static double strToReal(String x) {
        // Double.parseDouble allows all valid real values in CIF ASCII syntax,
        // as well a whole bunch of additional representations, such as
        // negative values (which are unary operator '-' with an real value
        // in CIF ASCII syntax), signed integers (not allowed in the CIF
        // ASCII syntax), hexadecimal floating point notation (not allowed in
        // CIF ASCII syntax), etc.

        double rslt;
        try {
            rslt = Double.parseDouble(x);
            if (Double.isNaN(rslt)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            String msg = fmt("Cast from type \"string\" to type \"real\" failed: the string value does not "
                    + "represent a real value: \"%s\".", Strings.escape(x));
            throw new CifSimulatorException(msg);
        }

        if (Double.isInfinite(rslt)) {
            String msg = fmt("Cast from type \"string\" to type \"real\" failed, due to real overflow: \"%s\".",
                    Strings.escape(x));
            throw new CifSimulatorException(msg);
        }

        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns {@code x sub y} for two sets.
     *
     * @param <T> The type of the elements of the sets.
     * @param x The first set.
     * @param y The second set.
     * @return {@code x sub y}.
     */
    public static <T> boolean subset(Set<T> x, Set<T> y) {
        return y.containsAll(x);
    }

    /**
     * Returns the subtraction of two integer numbers.
     *
     * @param x The first integer number.
     * @param y The second integer number.
     * @return {@code x - y}.
     * @throws CifSimulatorException If the operation results in integer overflow.
     */
    public static int subtract(int x, int y) {
        long rslt = (long)x - (long)y;
        if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
            return (int)rslt;
        }

        String msg = fmt("Integer overflow: %d - %d.", x, y);
        throw new CifSimulatorException(msg);
    }

    /**
     * Returns the subtraction of two real numbers.
     *
     * @param x The first real number.
     * @param y The second real number.
     * @return {@code x - y}.
     * @throws CifSimulatorException If the operation results in real overflow.
     */
    public static double subtract(double x, double y) {
        double rslt = x - y;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: %s - %s.", realToStr(x), realToStr(y));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the subtraction of two sets.
     *
     * @param <T> The type of the elements of the sets.
     * @param x The first set.
     * @param y The second set.
     * @return {@code x - y}.
     */
    public static <T> Set<T> subtract(Set<T> x, Set<T> y) {
        Set<T> rslt = copy(x);
        rslt.removeAll(y);
        return rslt;
    }

    /**
     * Returns the subtraction of two dictionaries.
     *
     * @param <K> The type of the keys of the dictionaries.
     * @param <V> The type of the values of the dictionaries.
     * @param x The first dictionary.
     * @param y The second dictionary.
     * @return {@code x - y}.
     */
    public static <K, V> Map<K, V> subtract(Map<K, V> x, Map<K, V> y) {
        Map<K, V> rslt = copy(x);
        for (K key: y.keySet()) {
            rslt.remove(key);
        }
        return rslt;
    }

    /**
     * Returns the subtraction of a dictionaries and a list.
     *
     * @param <K> The type of the keys of the dictionary and the elements of the list.
     * @param <V> The type of the values of the dictionary.
     * @param x The dictionary.
     * @param y The list.
     * @return {@code x - y}.
     */
    public static <K, V> Map<K, V> subtract(Map<K, V> x, List<K> y) {
        Map<K, V> rslt = copy(x);
        for (K key: y) {
            rslt.remove(key);
        }
        return rslt;
    }

    /**
     * Returns the subtraction of a dictionaries and a set.
     *
     * @param <K> The type of the keys of the dictionary and the elements of the set.
     * @param <V> The type of the values of the dictionary.
     * @param x The dictionary.
     * @param y The set.
     * @return {@code x - y}.
     */
    public static <K, V> Map<K, V> subtract(Map<K, V> x, Set<K> y) {
        Map<K, V> rslt = copy(x);
        for (K key: y) {
            rslt.remove(key);
        }
        return rslt;
    }

    /**
     * Returns the tangent of a real number.
     *
     * @param x The real number.
     * @return {@code tan(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double tan(double x) {
        double rslt = Math.tan(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: tan(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: tan(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the hyperbolic tangent of a real number.
     *
     * @param x The real number.
     * @return {@code tanh(x)}.
     * @throws CifSimulatorException If the operation results in real overflow, or {@code NaN}.
     */
    public static double tanh(double x) {
        double rslt = Math.tanh(x);
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Real overflow: tanh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        if (Double.isNaN(rslt)) {
            String msg = fmt("Invalid operation: tanh(%s).", realToStr(x));
            throw new CifSimulatorException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the union of two sets.
     *
     * @param <T> The type of the elements of the sets.
     * @param set1 The first set.
     * @param set2 The second set.
     * @return The union of the two sets.
     */
    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> rslt = copy(set1);
        rslt.addAll(set2);
        return rslt;
    }
}
