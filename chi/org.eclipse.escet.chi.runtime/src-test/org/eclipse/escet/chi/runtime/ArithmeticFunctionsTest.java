//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.chi.runtime.ArithmeticFunctions.floorDivision;
import static org.eclipse.escet.chi.runtime.ArithmeticFunctions.modulus;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests for the arithmetic functions. */
public class ArithmeticFunctionsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision74PosPos() {
        assertEquals(1, floorDivision(7, 4));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision74PosNeg() {
        assertEquals(-2, floorDivision(7, -4));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision74NegPos() {
        assertEquals(-2, floorDivision(-7, 4));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision74NegNeg() {
        assertEquals(1, floorDivision(-7, -4));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision63PosPos() {
        assertEquals(2, floorDivision(6, 3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision63PosNeg() {
        assertEquals(-2, floorDivision(6, -3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision63NegPos() {
        assertEquals(-2, floorDivision(-6, 3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision63NegNeg() {
        assertEquals(2, floorDivision(-6, -3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision03PosPos() {
        assertEquals(0, floorDivision(0, 3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerFloorDivision03PosNeg() {
        assertEquals(0, floorDivision(0, -3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus74PosPos() {
        int a = 7;
        int b = 4;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus74PosNeg() {
        int a = 7;
        int b = -4;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus74NegPos() {
        int a = -7;
        int b = 4;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus74NegNeg() {
        int a = -7;
        int b = -4;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus63PosPos() {
        int a = 6;
        int b = 3;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus63PosNeg() {
        int a = 6;
        int b = -3;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus63NegPos() {
        int a = -6;
        int b = 3;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus63NegNeg() {
        int a = -6;
        int b = -3;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus03PosPos() {
        int a = 0;
        int b = 3;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus03PosNeg() {
        int a = 0;
        int b = -3;
        assertEquals(a, floorDivision(a, b) * b + modulus(a, b));
    }
}
