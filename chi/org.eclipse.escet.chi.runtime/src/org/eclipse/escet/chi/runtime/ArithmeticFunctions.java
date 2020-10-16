//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.runtime;

/** Arithmetic functions library. */
public abstract class ArithmeticFunctions {
    /**
     * Compute the floor division of a/b.
     *
     * @param a Numerator of the division.
     * @param b Denominator of the division.
     * @return Result of the floor division.
     */
    public static int floorDivision(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Integer floor division by zero.");
        }

        if (a >= 0) {
            if (b >= 0) {
                return a / b;
            } else {
                // a >= 0, b < 0
                int res = -(a / (-b));
                return (res * b == a) ? res : res - 1;
            }
        } else {
            // a < 0
            if (b >= 0) {
                // a < 0, b > 0
                int res = -((-a) / b);
                return (res * b == a) ? res : res - 1;
            } else {
                // a < 0, b < 0
                return a / b;
            }
        }
    }

    /**
     * Compute the remainder of a/b.
     *
     * @param a Numerator of the division.
     * @param b Denominator of the division.
     * @return Remainder of the floor division.
     */
    public static int modulus(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Integer modulus division by zero.");
        }

        if (a >= 0) {
            if (b >= 0) {
                return a % b;
            } else {
                // a >= 0, b < 0
                int res = -(a / (-b));
                return (res * b == a) ? 0 : a + b - res * b;
            }
        } else {
            // a < 0
            if (b >= 0) {
                // a < 0, b > 0
                int res = -((-a) / b);
                return (res * b == a) ? 0 : a + b - res * b;
            } else {
                // a < 0, b < 0
                return a % b;
            }
        }
    }

    /**
     * Compute power function in case the second operand is an integer number (where the case that it is a natural
     * number is optimized further).
     *
     * @param x First operand.
     * @param y Second operand.
     * @return The equivalent of Math.pow(x, y)
     */
    public static double intPower(double x, int y) {
        if (y < 0) {
            return Math.pow(x, y);
        }

        // Compute x^y. Optimized version for natural number exponent.
        double z = 1;
        while (y > 0) {
            while ((y & 1) == 0) {
                y /= 2;
                x *= x;
            }
            y--;
            z *= x;
        }
        return z;
    }
}
