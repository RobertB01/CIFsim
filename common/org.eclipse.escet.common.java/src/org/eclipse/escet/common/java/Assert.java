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

package org.eclipse.escet.common.java;

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
        throw new AssertionError();
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
        throw new AssertionError(String.valueOf(msg));
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
        throw new AssertionError(left + " != " + right);
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
        throw new AssertionError(String.valueOf(msg), new AssertionError(left + " != " + right));
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
