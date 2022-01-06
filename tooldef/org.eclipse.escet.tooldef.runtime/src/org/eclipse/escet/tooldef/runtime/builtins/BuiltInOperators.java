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

package org.eclipse.escet.tooldef.runtime.builtins;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils.valueToStr;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.tooldef.runtime.ToolDefException;
import org.eclipse.escet.tooldef.runtime.ToolDefList;
import org.eclipse.escet.tooldef.runtime.ToolDefMap;
import org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils;
import org.eclipse.escet.tooldef.runtime.ToolDefSet;

/** ToolDef built-in operators. */
public class BuiltInOperators {
    /** Constructor for the {@link BuiltInOperators} class. */
    private BuiltInOperators() {
        // Static class.
    }

    /**
     * Returns the logical inverse of a boolean value.
     *
     * @param arg The boolean value.
     * @return The logical inverse result.
     */
    public static boolean not(boolean arg) {
        return !arg;
    }

    /**
     * Returns the conjunction of two boolean values.
     *
     * @param left The first boolean value.
     * @param right The second boolean value.
     * @return The conjunction result.
     */
    public static boolean and(boolean left, boolean right) {
        // Must be evaluated in a short circuit manner, as special case.
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the intersection of two sets.
     *
     * @param <T> The type of the elements of the sets.
     * @param left The first set.
     * @param right The second set.
     * @return The intersection result.
     */
    public static <T> Set<T> and(Set<T> left, Set<T> right) {
        Set<T> rslt = new ToolDefSet<>(left);
        rslt.retainAll(right);
        return rslt;
    }

    /**
     * Returns the disjunction of two boolean values.
     *
     * @param left The first boolean value.
     * @param right The second boolean value.
     * @return The disjunction result.
     */
    public static boolean or(boolean left, boolean right) {
        // Must be evaluated in a short circuit manner, as special case.
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the union of two sets.
     *
     * @param <T> The type of the elements of the sets.
     * @param left The first set.
     * @param right The second set.
     * @return The union result.
     */
    public static <T> Set<T> or(Set<T> left, Set<T> right) {
        Set<T> rslt = new ToolDefSet<>(left);
        rslt.addAll(right);
        return rslt;
    }

    /**
     * Returns the unary plus of an integer value. This is essentially the identity function.
     *
     * @param arg The integer value.
     * @return The integer value.
     */
    public static int plus(int arg) {
        return arg;
    }

    /**
     * Returns the unary plus of a long value. This is essentially the identity function.
     *
     * @param arg The long value.
     * @return The long value.
     */
    public static long plus(long arg) {
        return arg;
    }

    /**
     * Returns the unary plus of a double value. This is essentially the identity function.
     *
     * @param arg The double value.
     * @return The double value.
     */
    public static double plus(double arg) {
        return arg;
    }

    /**
     * Returns the addition of two integer numbers.
     *
     * @param left The first integer number.
     * @param right The second integer number.
     * @return The addition result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static int plus(int left, int right) {
        if (right > 0 ? left > Integer.MAX_VALUE - right : left < Integer.MIN_VALUE - right) {
            String msg = fmt("Integer number overflow: %s + %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left + right;
    }

    /**
     * Returns the addition of two long numbers.
     *
     * @param left The first long number.
     * @param right The second long number.
     * @return The addition result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static long plus(long left, long right) {
        if (right > 0 ? left > Long.MAX_VALUE - right : left < Long.MIN_VALUE - right) {
            String msg = fmt("Long number overflow: %s + %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left + right;
    }

    /**
     * Returns the addition of two double numbers.
     *
     * @param left The first double number.
     * @param right The second double number.
     * @return The addition result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static double plus(double left, double right) {
        double rslt = left + right;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Double number overflow: %s + %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }
        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the concatenation of two strings.
     *
     * @param left The first string.
     * @param right The second string.
     * @return The concatenation result.
     */
    public static String plus(String left, String right) {
        return left + right;
    }

    /**
     * Returns the concatenation of two lists.
     *
     * @param <T> The type of the elements of the lists.
     * @param left The first list.
     * @param right The second list.
     * @return The concatenation result.
     */
    public static <T> List<T> plus(List<T> left, List<T> right) {
        List<T> rslt = new ToolDefList<>(left.size() + right.size());
        rslt.addAll(left);
        rslt.addAll(right);
        return rslt;
    }

    /**
     * Returns the update of a first map with the entries of a second map. Essentially, the entries of the first map are
     * overwritten by the entries of the second map, while entries for new keys are added.
     *
     * @param <K> The type of the keys of the maps.
     * @param <V> The type of the values of the maps.
     * @param left The first map.
     * @param right The second map.
     * @return The update result.
     */
    public static <K, V> Map<K, V> plus(Map<K, V> left, Map<K, V> right) {
        Map<K, V> rslt = new ToolDefMap<>(left);
        rslt.putAll(right);
        return rslt;
    }

    /**
     * Returns the negation of an integer number.
     *
     * @param arg The integer number.
     * @return The negation result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static int dash(int arg) {
        if (arg == Integer.MIN_VALUE) {
            String msg = fmt("Integer number overflow: -%s.", valueToStr(arg));
            throw new ToolDefException(msg);
        }

        return -arg;
    }

    /**
     * Returns the negation of a long number.
     *
     * @param arg The long number.
     * @return The negation result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static long dash(long arg) {
        if (arg == Long.MIN_VALUE) {
            String msg = fmt("Long number overflow: -%s.", valueToStr(arg));
            throw new ToolDefException(msg);
        }

        return -arg;
    }

    /**
     * Returns the negation of a double number.
     *
     * @param arg The double number.
     * @return The negation result.
     */
    public static double dash(double arg) {
        return (arg == 0.0) ? 0.0 : -arg;
    }

    /**
     * Returns the subtraction of two integer numbers.
     *
     * @param left The first integer number.
     * @param right The second integer number.
     * @return The subtraction result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static int dash(int left, int right) {
        if (right > 0 ? left < Integer.MIN_VALUE + right : left > Integer.MAX_VALUE + right) {
            String msg = fmt("Integer number overflow: %s - %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left - right;
    }

    /**
     * Returns the subtraction of two long numbers.
     *
     * @param left The first long number.
     * @param right The second long number.
     * @return The subtraction result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static long dash(long left, long right) {
        if (right > 0 ? left < Long.MIN_VALUE + right : left > Long.MAX_VALUE + right) {
            String msg = fmt("Long number overflow: %s - %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left - right;
    }

    /**
     * Returns the subtraction of two double numbers.
     *
     * @param left The first double number.
     * @param right The second double number.
     * @return The subtraction result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static double dash(double left, double right) {
        double rslt = left - right;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Double number overflow: %s - %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the set difference of two sets.
     *
     * @param <T> The type of the elements of the sets.
     * @param left The first set.
     * @param right The second set.
     * @return The set difference result.
     */
    public static <T> Set<T> dash(Set<T> left, Set<T> right) {
        Set<T> rslt = new ToolDefSet<>(left);
        rslt.removeAll(right);
        return rslt;
    }

    /**
     * Returns the map with the keys from the list removed from it.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param left The map.
     * @param right The list.
     * @return The removal result.
     */
    public static <K, V> Map<K, V> dash(Map<K, V> left, List<K> right) {
        Map<K, V> rslt = new ToolDefMap<>(left);
        for (K k: right) {
            rslt.remove(k);
        }
        return rslt;
    }

    /**
     * Returns the map with the keys from the set removed from it.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param left The map.
     * @param right The set.
     * @return The removal result.
     */
    public static <K, V> Map<K, V> dash(Map<K, V> left, Set<K> right) {
        Map<K, V> rslt = new ToolDefMap<>(left);
        for (K k: right) {
            rslt.remove(k);
        }
        return rslt;
    }

    /**
     * Returns the first map with the keys from the second map removed from it.
     *
     * @param <K> The type of the keys of the maps.
     * @param <V> The type of the values of the first map.
     * @param <V2> The type of the values of the second map.
     * @param left The first map.
     * @param right The second map.
     * @return The removal result.
     */
    public static <K, V, V2> Map<K, V> dash(Map<K, V> left, Map<K, V2> right) {
        Map<K, V> rslt = new ToolDefMap<>(left);
        for (K k: right.keySet()) {
            rslt.remove(k);
        }
        return rslt;
    }

    /**
     * Returns the multiplication of two integer numbers.
     *
     * @param left The first integer number.
     * @param right The second integer number.
     * @return The multiplication result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static int star(int left, int right) {
        if (right > 0 ? left > Integer.MAX_VALUE / right || left < Integer.MIN_VALUE / right
                : (right < -1 ? left > Integer.MIN_VALUE / right || left < Integer.MAX_VALUE / right
                        : right == -1 && left == Integer.MIN_VALUE))
        {
            String msg = fmt("Integer number overflow: %s * %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left * right;
    }

    /**
     * Returns the multiplication of two long numbers.
     *
     * @param left The first long number.
     * @param right The second long number.
     * @return The multiplication result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static long star(long left, long right) {
        if (right > 0 ? left > Long.MAX_VALUE / right || left < Long.MIN_VALUE / right
                : (right < -1 ? left > Long.MIN_VALUE / right || left < Long.MAX_VALUE / right
                        : right == -1 && left == Long.MIN_VALUE))
        {
            String msg = fmt("Long number overflow: %s * %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left * right;
    }

    /**
     * Returns the multiplication of two double numbers.
     *
     * @param left The first double number.
     * @param right The second double number.
     * @return The multiplication result.
     * @throws ToolDefException If the operation results in overflow.
     */
    public static double star(double left, double right) {
        double rslt = left * right;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Double number overflow: %s * %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the division of two double numbers.
     *
     * @param left The first double number.
     * @param right The second double number.
     * @return The division result.
     * @throws ToolDefException If the operation results in overflow or division by zero.
     */
    public static double slash(double left, double right) {
        if (right == 0.0) {
            String msg = fmt("Division by zero: %s / %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        double rslt = left / right;
        if (Double.isInfinite(rslt)) {
            String msg = fmt("Double number overflow: %s * %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return (rslt == -0.0) ? 0.0 : rslt;
    }

    /**
     * Returns the integer division of two integer numbers.
     *
     * @param left The first integer number.
     * @param right The second integer number.
     * @return The integer division result.
     * @throws ToolDefException If the operation results in overflow or division by zero.
     */
    public static int div(int left, int right) {
        if (right == 0) {
            String msg = fmt("Division by zero: %s div %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            String msg = fmt("Integer number overflow: %s div %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left / right;
    }

    /**
     * Returns the integer division of two long numbers.
     *
     * @param left The first long number.
     * @param right The second long number.
     * @return The integer division result.
     * @throws ToolDefException If the operation results in overflow or division by zero.
     */
    public static long div(long left, long right) {
        if (right == 0) {
            String msg = fmt("Division by zero: %s div %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }
        if (left == Long.MIN_VALUE && right == -1) {
            String msg = fmt("Long number overflow: %s div %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left / right;
    }

    /**
     * Returns the modulus of two long numbers.
     *
     * @param left The first long number.
     * @param right The second long number.
     * @return The modulus result.
     * @throws ToolDefException If the operation results in division by zero.
     */
    public static int mod(int left, int right) {
        if (right == 0) {
            String msg = fmt("Division by zero: %s mod %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left % right;
    }

    /**
     * Returns the modulus of two integer numbers.
     *
     * @param left The first integer number.
     * @param right The second integer number.
     * @return The modulus result.
     * @throws ToolDefException If the operation results in division by zero.
     */
    public static long mod(long left, long right) {
        if (right == 0) {
            String msg = fmt("Division by zero: %s mod %s.", valueToStr(left), valueToStr(right));
            throw new ToolDefException(msg);
        }

        return left % right;
    }

    /**
     * Returns whether the first integer number is less than the second integer number.
     *
     * @param left The first integer number.
     * @param right The second integer number.
     * @return {@code true} if the first integer number is less than the second integer number, {@code false} otherwise.
     */
    public static boolean lt(int left, int right) {
        return left < right;
    }

    /**
     * Returns whether the first long number is less than the second long number.
     *
     * @param left The first long number.
     * @param right The second long number.
     * @return {@code true} if the first long number is less than the second long number, {@code false} otherwise.
     */
    public static boolean lt(long left, long right) {
        return left < right;
    }

    /**
     * Returns whether the first double number is less than the second double number.
     *
     * @param left The first double number.
     * @param right The second double number.
     * @return {@code true} if the first double number is less than the second double number, {@code false} otherwise.
     */
    public static boolean lt(double left, double right) {
        return left < right;
    }

    /**
     * Returns whether the first integer number is less than or equal to the second integer number.
     *
     * @param left The first integer number.
     * @param right The second integer number.
     * @return {@code true} if the first integer number is less than or equal to the second integer number,
     *     {@code false} otherwise.
     */
    public static boolean le(int left, int right) {
        return left <= right;
    }

    /**
     * Returns whether the first long number is less than or equal to the second long number.
     *
     * @param left The first long number.
     * @param right The second long number.
     * @return {@code true} if the first long number is less than or equal to the second long number, {@code false}
     *     otherwise.
     */
    public static boolean le(long left, long right) {
        return left <= right;
    }

    /**
     * Returns whether the first double number is less than or equal to the second double number.
     *
     * @param left The first double number.
     * @param right The second double number.
     * @return {@code true} if the first double number is less than or equal to the second double number, {@code false}
     *     otherwise.
     */
    public static boolean le(double left, double right) {
        return left <= right;
    }

    /**
     * Returns whether the first integer number is greater than the second integer number.
     *
     * @param left The first integer number.
     * @param right The second integer number.
     * @return {@code true} if the first integer number is greater than the second integer number, {@code false}
     *     otherwise.
     */
    public static boolean gt(int left, int right) {
        return left > right;
    }

    /**
     * Returns whether the first long number is greater than the second long number.
     *
     * @param left The first long number.
     * @param right The second long number.
     * @return {@code true} if the first long number is greater than the second long number, {@code false} otherwise.
     */
    public static boolean gt(long left, long right) {
        return left > right;
    }

    /**
     * Returns whether the first double number is greater than the second long number.
     *
     * @param left The first double number.
     * @param right The second double number.
     * @return {@code true} if the first double number is greater than the second double number, {@code false}
     *     otherwise.
     */
    public static boolean gt(double left, double right) {
        return left > right;
    }

    /**
     * Returns whether the first integer number is greater than or equal to the second integer number.
     *
     * @param left The first integer number.
     * @param right The second integer number.
     * @return {@code true} if the first integer number is greater than or equal to the second integer number,
     *     {@code false} otherwise.
     */
    public static boolean ge(int left, int right) {
        return left >= right;
    }

    /**
     * Returns whether the first long number is greater than or equal to the second long number.
     *
     * @param left The first long number.
     * @param right The second long number.
     * @return {@code true} if the first long number is greater than or equal to the second long number, {@code false}
     *     otherwise.
     */
    public static boolean ge(long left, long right) {
        return left >= right;
    }

    /**
     * Returns whether the first double number is greater than or equal to the second double number.
     *
     * @param left The first double number.
     * @param right The second double number.
     * @return {@code true} if the first double number is greater than or equal to the second double number,
     *     {@code false} otherwise.
     */
    public static boolean ge(double left, double right) {
        return left >= right;
    }

    /**
     * Returns whether the first value is equal to the second value.
     *
     * @param <T> The type of the values.
     * @param left The first value, may be {@code null}.
     * @param right The second value, may be {@code null}.
     * @return {@code true} if the first value is equal to the second value, {@code false} otherwise.
     */
    public static <T> boolean eq(T left, T right) {
        return ToolDefRuntimeUtils.equalValues(left, right);
    }

    /**
     * Returns whether the first value is unequal to the second value.
     *
     * @param <T> The type of the values.
     * @param left The first value, may be {@code null}.
     * @param right The second value, may be {@code null}.
     * @return {@code true} if the first value is unequal to the second value, {@code false} otherwise.
     */
    public static <T> boolean ne(T left, T right) {
        return !ToolDefRuntimeUtils.equalValues(left, right);
    }
}
