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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Objects;

/** A class for performing assertion checks. Unlike the Java assert statement, these are always checked. */
public class Assert {
    /** Constructor for the {@link Assert} class. */
    private Assert() {
        // Static class.
    }

    /**
     * Checks a condition and throws an {@link AssertionError} if the condition doesn't hold.
     *
     * @param condition The condition that should hold.
     *
     * @throws AssertionError If the condition doesn't hold.
     */
    public static void check(boolean condition) {
        if (condition) {
            return;
        }
        throw new AssertionError();
    }

    /**
     * Checks a condition and throws an {@link AssertionError}, with the given message, if the condition doesn't hold.
     *
     * @param condition The condition that should hold.
     * @param msg The message to use for the exception. {@link String#valueOf} is used to convert the object to a
     *     message.
     *
     * @throws AssertionError If the condition doesn't hold.
     */
    public static void check(boolean condition, Object msg) {
        if (condition) {
            return;
        }
        throw new AssertionError(String.valueOf(msg));
    }

    /**
     * Checks whether two values are {@link Object#equals equal} and throws an {@link AssertionError} if they are not
     * equal.
     *
     * @param value1 The first value. Maybe {@code null}.
     * @param value2 The second value. Maybe {@code null}.
     *
     * @throws AssertionError If the values are not equal.
     */
    public static void areEqual(Object value1, Object value2) {
        if (Objects.equals(value1, value2)) {
            return;
        }
        throw new AssertionError(fmt("Values are not equal: '%s' and '%s'", value1, value2));
    }

    /**
     * Checks whether two values are {@link Object#equals equal} and throws an {@link AssertionError} if they are not
     * equal.
     *
     * @param value1 The first value. Maybe {@code null}.
     * @param value2 The second value. Maybe {@code null}.
     * @param msg The message to use for the exception. {@link String#valueOf} is used to convert the object to a
     *     message.
     *
     * @throws AssertionError If the values are not equal.
     */
    public static void areEqual(Object value1, Object value2, Object msg) {
        if (Objects.equals(value1, value2)) {
            return;
        }
        throw new AssertionError(String.valueOf(msg),
                new AssertionError(fmt("Values are not equal: '%s' and '%s'", value1, value2)));
    }

    /**
     * Checks an implication and throws an {@link AssertionError} if the implication doesn't hold.
     *
     * @param left The left side of the implication.
     * @param right The right side of the implication.
     *
     * @throws AssertionError If the implication doesn't hold.
     */
    public static void implies(boolean left, boolean right) {
        if (!left || right) {
            return;
        }
        throw new AssertionError(fmt("'%s => %s' doesn't hold", left, right));
    }

    /**
     * Checks an implication and throws an {@link AssertionError}, with the given message, if the implication doesn't
     * hold.
     *
     * @param left The left side of the implication.
     * @param right The right side of the implication.
     * @param msg The message to use for the exception. {@link String#valueOf} is used to convert the object to a
     *     message.
     *
     * @throws AssertionError If the condition doesn't hold.
     */
    public static void implies(boolean left, boolean right, Object msg) {
        if (!left || right) {
            return;
        }
        throw new AssertionError(String.valueOf(msg), new AssertionError(fmt("'%s => %s' doesn't hold", left, right)));
    }

    /**
     * Checks an biimplication (biconditional) and throws an {@link AssertionError} if the biimplication doesn't hold.
     *
     * @param left The left side of the biimplication.
     * @param right The right side of the biimplication.
     *
     * @throws AssertionError If the biimplication doesn't hold.
     */
    public static void ifAndOnlyIf(boolean left, boolean right) {
        if (left == right) {
            return;
        }
        throw new AssertionError(fmt("'%s <=> %s' doesn't hold", left, right));
    }

    /**
     * Checks an biimplication (biconditional) and throws an {@link AssertionError}, with the given message, if the
     * biimplication doesn't hold.
     *
     * @param left The left side of the biimplication.
     * @param right The right side of the biimplication.
     * @param msg The message to use for the exception. {@link String#valueOf} is used to convert the object to a
     *     message.
     *
     * @throws AssertionError If the biimplication doesn't hold.
     */
    public static void ifAndOnlyIf(boolean left, boolean right, Object msg) {
        if (left == right) {
            return;
        }
        throw new AssertionError(String.valueOf(msg), new AssertionError(fmt("'%s <=> %s' doesn't hold", left, right)));
    }

    /**
     * Unconditionally throws an {@link AssertionError}.
     *
     * @throws AssertionError Always thrown.
     */
    public static void fail() {
        throw new AssertionError();
    }

    /**
     * Unconditionally throws an {@link AssertionError}, with the given message.
     *
     * @param msg The message to use for the exception. {@link String#valueOf} is used to convert the object to a
     *     message.
     *
     * @throws AssertionError Always thrown.
     */
    public static void fail(Object msg) {
        throw new AssertionError(String.valueOf(msg));
    }

    /**
     * Checks to make sure the given value is not {@code null} and throws an {@link AssertionError} if it is
     * {@code null}.
     *
     * @param value The value to check.
     *
     * @throws AssertionError If the value is {@code null}.
     */
    public static void notNull(Object value) {
        if (value != null) {
            return;
        }
        throw new AssertionError();
    }

    /**
     * Checks to make sure the given value is not {@code null} and throws an {@link AssertionError}, with the given
     * message, if it is {@code null}.
     *
     * @param value The value to check.
     * @param msg The message to use for the exception. {@link String#valueOf} is used to convert the object to a
     *     message.
     *
     * @throws AssertionError If the value is {@code null}.
     */
    public static void notNull(Object value, Object msg) {
        if (value != null) {
            return;
        }
        throw new AssertionError(String.valueOf(msg));
    }
}
