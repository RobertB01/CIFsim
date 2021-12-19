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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifEvalUtils.objToStr;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.java.Assert;
import org.junit.Test;

/** Unit tests for the {@link CifMath} class. */
@SuppressWarnings("javadoc")
public class CifMathTest {
    @Test
    public void testDouble() {
        double zero = 0.0;
        double negzero = -0.0;
        assertEquals(true, zero == zero);
        assertEquals(true, zero == negzero);
        assertEquals(false, zero != zero);
        assertEquals(false, zero != negzero);
        assertEquals(false, zero > zero);
        assertEquals(false, zero > negzero);
        assertEquals(false, zero < zero);
        assertEquals(false, zero < negzero);
        assertEquals(true, zero >= zero);
        assertEquals(true, zero >= negzero);
        assertEquals(true, zero <= zero);
        assertEquals(true, zero <= negzero);
    }

    @Test
    public void testAbs() throws CifEvalException {
        assertEquals(5, CifMath.abs(-5, null));
        assertEquals(1, CifMath.abs(-1, null));
        assertEquals(0, CifMath.abs(0, null));
        assertEquals(1, CifMath.abs(1, null));
        assertEquals(5, CifMath.abs(5, null));

        assertEquals(5.0, CifMath.abs(-5.0), 0.0);
        assertEquals(1.0, CifMath.abs(-1.0), 0.0);
        assertEquals(0.5, CifMath.abs(-0.5), 0.0);
        assertEquals(0.0, CifMath.abs(-0.0), 0.0);
        assertEquals(0.0, CifMath.abs(0.0), 0.0);
        assertEquals(0.5, CifMath.abs(0.5), 0.0);
        assertEquals(1.0, CifMath.abs(1.0), 0.0);
        assertEquals(5.0, CifMath.abs(5.0), 0.0);

        assertEquals("0.0", Double.toString(CifMath.abs(-0.0)));
    }

    @Test(expected = CifEvalException.class)
    public void testAbsOverflow() throws CifEvalException {
        CifMath.abs(Integer.MIN_VALUE, null);
    }

    @Test
    public void testAdd() throws CifEvalException {
        assertEquals(0, CifMath.add(-5, 5, null));
        assertEquals(3, CifMath.add(1, 2, null));

        assertEquals(0.0, CifMath.add(-5.0, 5.0, null), 0.0);
        assertEquals(3.0, CifMath.add(1.0, 2.0, null), 0.0);
        assertEquals(0.33, CifMath.add(0.1, 0.23, null), 0.0);
    }

    @Test(expected = CifEvalException.class)
    public void testAddIntOverflow() throws CifEvalException {
        CifMath.add(2147000000, 1000000, null);
    }

    @Test(expected = CifEvalException.class)
    public void testAddRealOverflow() throws CifEvalException {
        CifMath.add(Double.MAX_VALUE, 1e299, null);
    }

    @Test
    public void testBoolToStr() {
        assertEquals("true", CifMath.boolToStr(true));
        assertEquals("false", CifMath.boolToStr(false));
    }

    @Test
    public void testCbrt() {
        double sqrtMax = 5.643803094122362E102;
        assertEquals(0.0, CifMath.cbrt(0.0), 0.0);
        assertEquals(3.0, CifMath.cbrt(27.0), 0.0);
        assertEquals(sqrtMax, CifMath.cbrt(Double.MAX_VALUE), 0.0);
        assertEquals(-sqrtMax, CifMath.cbrt(-Double.MAX_VALUE), 0.0);
    }

    @Test
    public void testCeil() throws CifEvalException {
        assertEquals(-1, CifMath.ceil(-1.0, null));
        assertEquals(0, CifMath.ceil(-0.9, null));
        assertEquals(0, CifMath.ceil(-0.5, null));
        assertEquals(0, CifMath.ceil(-0.1, null));
        assertEquals(0, CifMath.ceil(0.0, null));
        assertEquals(1, CifMath.ceil(0.1, null));
        assertEquals(1, CifMath.ceil(0.5, null));
        assertEquals(1, CifMath.ceil(0.9, null));
        assertEquals(1, CifMath.ceil(1.0, null));
    }

    @Test(expected = CifEvalException.class)
    public void testCeilOverflow() throws CifEvalException {
        CifMath.ceil(1e99, null);
    }

    @Test
    public void testDelete() throws CifEvalException {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        assertEquals("[2, 3, 4]", objToStr(CifMath.delete(l, 0, null)));
        assertEquals("[1, 3, 4]", objToStr(CifMath.delete(l, 1, null)));
        assertEquals("[1, 2, 4]", objToStr(CifMath.delete(l, 2, null)));
        assertEquals("[1, 2, 3]", objToStr(CifMath.delete(l, 3, null)));
        assertEquals("[1, 2, 3]", objToStr(CifMath.delete(l, -1, null)));
        assertEquals("[1, 2, 4]", objToStr(CifMath.delete(l, -2, null)));
        assertEquals("[1, 3, 4]", objToStr(CifMath.delete(l, -3, null)));
        assertEquals("[2, 3, 4]", objToStr(CifMath.delete(l, -4, null)));
    }

    @Test(expected = CifEvalException.class)
    public void testDeleteOutOfRange() throws CifEvalException {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        CifMath.delete(l, 4, null);
    }

    @Test(expected = CifEvalException.class)
    public void testDeleteOutOfRange2() throws CifEvalException {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        CifMath.delete(l, -5, null);
    }

    @Test
    public void testDiv() throws CifEvalException {
        assertEquals(1, CifMath.div(7, 4, null));
        assertEquals(-1, CifMath.div(7, -4, null));
        assertEquals(-1, CifMath.div(-7, 4, null));
        assertEquals(1, CifMath.div(-7, -4, null));
    }

    @Test
    public void testDiv2() throws CifEvalException {
        // Test that 'a div b' is the same as
        // 'if (a / b) >= 0: floor(a / b) else ceil(a / b) end'.
        for (int a = -10; a < 10; a++) {
            for (int b = -10; b < 10; b++) {
                if (b == 0) {
                    continue;
                }
                assertEquals(div2(a, b), CifMath.div(a, b, null));
            }
        }
    }

    /**
     * Alternative implementation of 'div'.
     *
     * @param a First argument.
     * @param b Second argument.
     * @return {@code a div b}
     */
    private int div2(int a, int b) {
        Assert.check(b != 0);
        double r = a / b;
        return (r >= 0) ? (int)Math.floor(r) : (int)Math.ceil(r);
    }

    @Test
    public void testDivNoOverflow() {
        // Test to show that integer division can overflow on only one
        // combination of values.
        int[] inputs = {Integer.MIN_VALUE, Integer.MAX_VALUE, 1, -1};

        for (int i1: inputs) {
            for (int i2: inputs) {
                if (i1 == Integer.MIN_VALUE && i2 == -1) {
                    // This case overflows.
                    continue;
                }
                int mod1 = i1 / i2;
                long mod2 = (long)i1 / (long)i2;
                assertEquals(mod1, mod2);
            }
        }
    }

    @Test(expected = CifEvalException.class)
    public void testDivByZero() throws CifEvalException {
        CifMath.div(1, 0, null);
    }

    @Test(expected = CifEvalException.class)
    public void testDivOverflow() throws CifEvalException {
        CifMath.div(Integer.MIN_VALUE, -1, null);
    }

    @Test
    public void testDivide() throws CifEvalException {
        assertEquals(0.5, CifMath.divide(1.0, 2.0, null), 0.0);

        assertEquals("0.0", Double.toString(CifMath.divide(-1e-99, Double.MAX_VALUE, null)));
    }

    @Test(expected = CifEvalException.class)
    public void testDivideByZero() throws CifEvalException {
        CifMath.divide(1.0, 0.0, null);
    }

    @Test(expected = CifEvalException.class)
    public void testDivideOverflow() throws CifEvalException {
        CifMath.divide(1e99, Double.MIN_VALUE, null);
    }

    @Test
    public void testExp() throws CifEvalException {
        assertEquals(Math.E, CifMath.exp(1.0, null), 1e-15);
        assertEquals(Math.E * Math.E, CifMath.exp(2.0, null), 1e-15);
    }

    @Test(expected = CifEvalException.class)
    public void testExpOverflow() throws CifEvalException {
        CifMath.exp(1e99, null);
    }

    @Test
    public void testFloor() throws CifEvalException {
        assertEquals(-1, CifMath.floor(-1.0, null));
        assertEquals(-1, CifMath.floor(-0.9, null));
        assertEquals(-1, CifMath.floor(-0.5, null));
        assertEquals(-1, CifMath.floor(-0.1, null));
        assertEquals(0, CifMath.floor(0.0, null));
        assertEquals(0, CifMath.floor(0.1, null));
        assertEquals(0, CifMath.floor(0.5, null));
        assertEquals(0, CifMath.floor(0.9, null));
        assertEquals(1, CifMath.floor(1.0, null));
    }

    @Test(expected = CifEvalException.class)
    public void testFloorOverflow() throws CifEvalException {
        CifMath.floor(-1e99, null);
    }

    @Test
    public void testIntToReal() {
        assertEquals(-2147483648.0, CifMath.intToReal(Integer.MIN_VALUE), 0.0);
        assertEquals(-1.0, CifMath.intToReal(-1), 0.0);
        assertEquals(0.0, CifMath.intToReal(0), 0.0);
        assertEquals(1.0, CifMath.intToReal(1), 0.0);
        assertEquals(2147483647.0, CifMath.intToReal(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testIntToStr() {
        assertEquals("-2147483648", CifMath.intToStr(Integer.MIN_VALUE));
        assertEquals("-1", CifMath.intToStr(-1));
        assertEquals("0", CifMath.intToStr(0));
        assertEquals("1", CifMath.intToStr(1));
        assertEquals("2147483647", CifMath.intToStr(Integer.MAX_VALUE));
    }

    @Test
    public void testLn() throws CifEvalException {
        assertEquals(1.0, CifMath.ln(Math.E, null), 0.0);
        assertEquals(2.0, CifMath.ln(Math.E * Math.E, null), 0.0);
    }

    @Test(expected = CifEvalException.class)
    public void testLnNonPos() throws CifEvalException {
        CifMath.ln(0.0, null);
    }

    @Test
    public void testLog() throws CifEvalException {
        assertEquals(1.0, CifMath.log(10.0, null), 0.0);
        assertEquals(2.0, CifMath.log(100.0, null), 0.0);
    }

    @Test(expected = CifEvalException.class)
    public void testLogNonPos() throws CifEvalException {
        CifMath.log(0.0, null);
    }

    @Test
    public void testMax() {
        assertEquals(5, CifMath.max(-5, 5));
        assertEquals(-5, CifMath.max(-5, -5));
        assertEquals(5, CifMath.max(5, 5));
        assertEquals(5, CifMath.max(5, -5));

        assertEquals(5.0, CifMath.max(-5.0, 5.0), 0.0);
        assertEquals(-5.0, CifMath.max(-5.0, -5.0), 0.0);
        assertEquals(5.0, CifMath.max(5.0, 5.0), 0.0);
        assertEquals(5.0, CifMath.max(5.0, -5.0), 0.0);
    }

    @Test
    public void testMin() {
        assertEquals(-5, CifMath.min(-5, 5));
        assertEquals(-5, CifMath.min(-5, -5));
        assertEquals(5, CifMath.min(5, 5));
        assertEquals(-5, CifMath.min(5, -5));

        assertEquals(-5.0, CifMath.min(-5.0, 5.0), 0.0);
        assertEquals(-5.0, CifMath.min(-5.0, -5.0), 0.0);
        assertEquals(5.0, CifMath.min(5.0, 5.0), 0.0);
        assertEquals(-5.0, CifMath.min(5.0, -5.0), 0.0);
    }

    @Test
    public void testMod() throws CifEvalException {
        assertEquals(3, CifMath.mod(7, 4, null));
        assertEquals(3, CifMath.mod(7, -4, null));
        assertEquals(-3, CifMath.mod(-7, 4, null));
        assertEquals(-3, CifMath.mod(-7, -4, null));
    }

    @Test
    public void testMod2() throws CifEvalException {
        // Test that 'a mod b' is the same as 'a - b * (a div b)'.
        for (int a = -10; a < 10; a++) {
            for (int b = -10; b < 10; b++) {
                if (b == 0) {
                    continue;
                }
                assertEquals(mod2(a, b), CifMath.mod(a, b, null));
            }
        }
    }

    /**
     * Alternative implementation of 'mod'.
     *
     * @param a First argument.
     * @param b Second argument.
     * @return {@code a mod b}
     */
    private int mod2(int a, int b) {
        Assert.check(b != 0);
        return a - b * div2(a, b);
    }

    @Test
    public void testModNoOverflow() {
        // Test to show that modulus can't overflow.
        int[] inputs = {Integer.MIN_VALUE, Integer.MAX_VALUE, 1, -1};

        for (int i1: inputs) {
            for (int i2: inputs) {
                int mod1 = i1 % i2;
                int mod2 = i1 - i2 * (i1 / i2);
                long mod3 = (long)i1 % (long)i2;
                assertEquals(mod1, mod2);
                assertEquals(mod1, mod3);
            }
        }
    }

    @Test(expected = CifEvalException.class)
    public void testModByZero() throws CifEvalException {
        CifMath.mod(1, 0, null);
    }

    @Test
    public void testMultiply() throws CifEvalException {
        assertEquals(-25, CifMath.multiply(-5, 5, null));
        assertEquals(2, CifMath.multiply(1, 2, null));

        assertEquals(-25.0, CifMath.multiply(-5.0, 5.0, null), 0.0);
        assertEquals(2.0, CifMath.multiply(1.0, 2.0, null), 0.0);
        assertEquals(0.023, CifMath.multiply(0.1, 0.23, null), 1e-15);
    }

    @Test(expected = CifEvalException.class)
    public void testMultiplyIntOverflow() throws CifEvalException {
        CifMath.multiply(111111, 222222, null);
    }

    @Test(expected = CifEvalException.class)
    public void testMultiplyRealOverflow() throws CifEvalException {
        CifMath.multiply(1e199, 1e199, null);
    }

    @Test
    public void testNegate() throws CifEvalException {
        assertEquals(5, CifMath.negate(-5, null));
        assertEquals(1, CifMath.negate(-1, null));
        assertEquals(0, CifMath.negate(0, null));
        assertEquals(-1, CifMath.negate(1, null));
        assertEquals(-5, CifMath.negate(5, null));

        assertEquals(5.0, CifMath.negate(-5.0), 0.0);
        assertEquals(1.0, CifMath.negate(-1.0), 0.0);
        assertEquals(0.5, CifMath.negate(-0.5), 0.0);
        assertEquals(0.0, CifMath.negate(-0.0), 0.0);
        assertEquals(0.0, CifMath.negate(0.0), 0.0);
        assertEquals(-0.5, CifMath.negate(0.5), 0.0);
        assertEquals(-1.0, CifMath.negate(1.0), 0.0);
        assertEquals(-5.0, CifMath.negate(5.0), 0.0);

        assertEquals("0.0", Double.toString(CifMath.negate(0.0)));
    }

    @Test(expected = CifEvalException.class)
    public void testNegateIntOverflow() throws CifEvalException {
        CifMath.negate(Integer.MIN_VALUE, null);
    }

    @Test
    public void testPop() throws CifEvalException {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        assertEquals("(1, [2])", objToStr(CifMath.pop(l, null)));
    }

    @Test(expected = CifEvalException.class)
    public void testPopEmptyList() throws CifEvalException {
        List<Object> l = list();
        objToStr(CifMath.pop(l, null));
    }

    @Test
    public void testPow() throws CifEvalException {
        assertEquals(8, CifMath.pow(2, 3, null));
        assertEquals(-8, CifMath.pow(-2, 3, null));
        assertEquals(1, CifMath.pow(1, 0, null));
        assertEquals(1, CifMath.pow(0, 0, null));

        assertEquals(8.0, CifMath.pow(2.0, 3.0, null), 0.0);
        assertEquals(-8.0, CifMath.pow(-2.0, 3.0, null), 0.0);
        assertEquals(1.0, CifMath.pow(1.0, 0.0, null), 0.0);
        assertEquals(1.0, CifMath.pow(0.0, 0.0, null), 0.0);
        assertEquals(3.0, CifMath.pow(9.0, 0.5, null), 0.0);
    }

    @Test(expected = CifEvalException.class)
    public void testPowIntOverflow() throws CifEvalException {
        CifMath.pow(12345, 99999, null);
    }

    @Test(expected = CifEvalException.class)
    public void testPowRealOverflow() throws CifEvalException {
        CifMath.pow(1e99, 1e99, null);
    }

    @Test(expected = CifEvalException.class)
    public void testPowZeroToNeg() throws CifEvalException {
        CifMath.pow(0.0, -1.0, null);
    }

    @Test(expected = CifEvalException.class)
    public void testPowNegToNonInt() throws CifEvalException {
        CifMath.pow(-1.0, 0.5, null);
    }

    @Test
    public void testRealToStr() {
        assertEquals("0.0", CifMath.realToStr(0.0));
        assertEquals("0.1", CifMath.realToStr(0.1));
        assertEquals("1.0", CifMath.realToStr(1.0));
        assertEquals("1.1", CifMath.realToStr(1.1));
        assertEquals("1.0e99", CifMath.realToStr(1e99));
        assertEquals("0.123456789", CifMath.realToStr(0.123456789));
        assertEquals("1.23456789e-5", CifMath.realToStr(0.0000123456789));
    }

    @Test
    public void testProjectList() throws CifEvalException {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        assertEquals(1, CifMath.project(l, 0, null));
        assertEquals(2, CifMath.project(l, 1, null));
        assertEquals(3, CifMath.project(l, 2, null));
        assertEquals(4, CifMath.project(l, 3, null));
        assertEquals(4, CifMath.project(l, -1, null));
        assertEquals(3, CifMath.project(l, -2, null));
        assertEquals(2, CifMath.project(l, -3, null));
        assertEquals(1, CifMath.project(l, -4, null));
    }

    @Test
    public void testProjectString() throws CifEvalException {
        String s = "1234";
        assertEquals("1", CifMath.project(s, 0, null));
        assertEquals("2", CifMath.project(s, 1, null));
        assertEquals("3", CifMath.project(s, 2, null));
        assertEquals("4", CifMath.project(s, 3, null));
        assertEquals("4", CifMath.project(s, -1, null));
        assertEquals("3", CifMath.project(s, -2, null));
        assertEquals("2", CifMath.project(s, -3, null));
        assertEquals("1", CifMath.project(s, -4, null));
    }

    @Test
    public void testProjectDict() throws CifEvalException {
        Map<Object, Object> d = map();
        d.put(5, 6);
        d.put(7, 8);
        assertEquals(6, CifMath.project(d, 5, null));
        assertEquals(8, CifMath.project(d, 7, null));
    }

    @Test
    public void testProjectTuple() throws CifEvalException {
        CifTuple t = new CifTuple();
        t.add(1);
        t.add(2);
        t.add(3);
        t.add(4);
        assertEquals(1, CifMath.project(t, 0, null));
        assertEquals(2, CifMath.project(t, 1, null));
        assertEquals(3, CifMath.project(t, 2, null));
        assertEquals(4, CifMath.project(t, 3, null));
    }

    @Test(expected = CifEvalException.class)
    public void testProjectListOutOfRange1() throws CifEvalException {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        CifMath.project(l, 4, null);
    }

    @Test(expected = CifEvalException.class)
    public void testProjectListOutOfRange2() throws CifEvalException {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        CifMath.project(l, -5, null);
    }

    @Test(expected = CifEvalException.class)
    public void testProjectStringOutOfRange1() throws CifEvalException {
        String s = "1234";
        CifMath.project(s, 4, null);
    }

    @Test(expected = CifEvalException.class)
    public void testProjectStringOutOfRange2() throws CifEvalException {
        String s = "1234";
        CifMath.project(s, -5, null);
    }

    @Test(expected = CifEvalException.class)
    public void testProjectDictKeyNotFound() throws CifEvalException {
        Map<Object, Object> d = map();
        d.put(5, 6);
        d.put(7, 8);
        CifMath.project(d, 9, null);
    }

    @Test(expected = CifEvalException.class)
    public void testProjectTupleOutOfRange1() throws CifEvalException {
        CifTuple t = new CifTuple();
        t.add(1);
        t.add(2);
        t.add(3);
        t.add(4);
        CifMath.project(t, -1, null);
    }

    @Test(expected = CifEvalException.class)
    public void testProjectTupleOutOfRange2() throws CifEvalException {
        CifTuple t = new CifTuple();
        t.add(1);
        t.add(2);
        t.add(3);
        t.add(4);
        CifMath.project(t, 4, null);
    }

    @Test
    public void testRound() throws CifEvalException {
        assertEquals(-1, CifMath.round(-1.0, null));
        assertEquals(-1, CifMath.round(-0.9, null));
        assertEquals(0, CifMath.round(-0.5, null));
        assertEquals(0, CifMath.round(-0.1, null));
        assertEquals(0, CifMath.round(0.0, null));
        assertEquals(0, CifMath.round(0.1, null));
        assertEquals(1, CifMath.round(0.5, null));
        assertEquals(1, CifMath.round(0.9, null));
        assertEquals(1, CifMath.round(1.0, null));

        assertEquals(-2147483648, CifMath.round(-2147483648.5, null));
        assertEquals(-2147483648, CifMath.round(-2147483648, null));
        assertEquals(2147483647, CifMath.round(2147483647, null));
        assertEquals(2147483647, CifMath.round(2147483647.49, null));
    }

    @Test(expected = CifEvalException.class)
    public void testRoundOverflow1() throws CifEvalException {
        CifMath.round(-2147483648.51, null);
    }

    @Test(expected = CifEvalException.class)
    public void testRoundOverflow2() throws CifEvalException {
        CifMath.round(2147483647.50, null);
    }

    @Test
    public void testScale() throws CifEvalException {
        // [0..1] -> [0..3]
        assertEquals(0.0, CifMath.scale(0.0, 0, 1, 0, 3, null), 1e-15);
        assertEquals(0.3, CifMath.scale(0.1, 0, 1, 0, 3, null), 1e-15);
        assertEquals(0.6, CifMath.scale(0.2, 0, 1, 0, 3, null), 1e-15);
        assertEquals(1.5, CifMath.scale(0.5, 0, 1, 0, 3, null), 1e-15);
        assertEquals(3.0, CifMath.scale(1.0, 0, 1, 0, 3, null), 1e-15);

        // [1..2] -> [5..8]
        assertEquals(5.0, CifMath.scale(1.0, 1, 2, 5, 8, null), 1e-15);
        assertEquals(5.3, CifMath.scale(1.1, 1, 2, 5, 8, null), 1e-15);
        assertEquals(5.6, CifMath.scale(1.2, 1, 2, 5, 8, null), 1e-15);
        assertEquals(6.5, CifMath.scale(1.5, 1, 2, 5, 8, null), 1e-15);
        assertEquals(8.0, CifMath.scale(2.0, 1, 2, 5, 8, null), 1e-15);

        // [1..2] -> [10..8] (increasing -> decreasing)
        assertEquals(9.5, CifMath.scale(1.25, 1, 2, 10, 8, null), 1e-15);

        // [1..2] -> [3..7] (outside input interval)
        assertEquals(-1.0, CifMath.scale(0, 1, 2, 3, 7, null), 1e-15);
        assertEquals(3.0, CifMath.scale(1, 1, 2, 3, 7, null), 1e-15);
        assertEquals(7.0, CifMath.scale(2, 1, 2, 3, 7, null), 1e-15);
        assertEquals(11.0, CifMath.scale(3, 1, 2, 3, 7, null), 1e-15);
    }

    @Test(expected = CifEvalException.class)
    public void testScaleOverflow() throws CifEvalException {
        // Overflow in multiplication.
        CifMath.scale(1e99, 0, 1e-10, -1e299, 1e299, null);
    }

    @Test(expected = CifEvalException.class)
    public void testScaleEmptyInputInterval() throws CifEvalException {
        // Division by zero in division.
        CifMath.scale(123.0, 1, 1, 3, 4, null);
    }

    @Test
    public void testSign() {
        assertEquals(-1, CifMath.sign(-99));
        assertEquals(-1, CifMath.sign(-2));
        assertEquals(-1, CifMath.sign(-1));
        assertEquals(0, CifMath.sign(0));
        assertEquals(1, CifMath.sign(1));
        assertEquals(1, CifMath.sign(2));
        assertEquals(1, CifMath.sign(99));

        assertEquals(-1, CifMath.sign(-99.0));
        assertEquals(-1, CifMath.sign(-2.0));
        assertEquals(-1, CifMath.sign(-1.0));
        assertEquals(-1, CifMath.sign(-0.5));
        assertEquals(-1, CifMath.sign(-0.1));
        assertEquals(0, CifMath.sign(0.0));
        assertEquals(1, CifMath.sign(0.1));
        assertEquals(1, CifMath.sign(0.5));
        assertEquals(1, CifMath.sign(1.0));
        assertEquals(1, CifMath.sign(2.0));
        assertEquals(1, CifMath.sign(99.0));
    }

    @Test
    public void testSlice() {
        // Implementation redirects to common.java project, which already has
        // extensive tests.
    }

    @Test
    public void testSqrt() throws CifEvalException {
        assertEquals(3.0, CifMath.sqrt(9.0, null), 0.0);
        assertEquals(4.0, CifMath.sqrt(16.0, null), 0.0);
    }

    @Test(expected = CifEvalException.class)
    public void testSqrtNeg() throws CifEvalException {
        CifMath.sqrt(-Double.MIN_VALUE, null);
    }

    @Test
    public void testStrToBool() throws CifEvalException {
        assertEquals(true, CifMath.strToBool("true", null));
        assertEquals(false, CifMath.strToBool("false", null));
    }

    @Test(expected = CifEvalException.class)
    public void testStrToBoolInvalid() throws CifEvalException {
        CifMath.strToBool("True", null);
    }

    @Test
    public void testStrToInt() throws CifEvalException {
        assertEquals(0, CifMath.strToInt("0", null));
        assertEquals(0, CifMath.strToInt("-0", null));
        assertEquals(1, CifMath.strToInt("1", null));
        assertEquals(-1, CifMath.strToInt("-1", null));
        assertEquals(123, CifMath.strToInt("123", null));
        assertEquals(-123, CifMath.strToInt("-123", null));
        assertEquals(2147483647, CifMath.strToInt("2147483647", null));
        assertEquals(-2147483648, CifMath.strToInt("-2147483648", null));
    }

    @Test(expected = CifEvalException.class)
    public void testStrToIntOverflow() throws CifEvalException {
        CifMath.strToInt("2147483648", null);
    }

    @Test(expected = CifEvalException.class)
    public void testStrToIntInvalid() throws CifEvalException {
        CifMath.strToInt("abc", null);
    }

    @Test
    public void testStrToReal() throws CifEvalException {
        assertEquals(0.0, CifMath.strToReal("0.0", null), 0.0);
        assertEquals(0.0, CifMath.strToReal("-0.0", null), 0.0);
        assertEquals(300, CifMath.strToReal("3e2", null), 0.0);
        assertEquals(0.00001, CifMath.strToReal("1e-5", null), 0.0);
        assertEquals(100000, CifMath.strToReal("1E+05", null), 0.0);
        assertEquals(123.5e-5, CifMath.strToReal("123.5e-5", null), 0.0);
        assertEquals(123.4, CifMath.strToReal("123.4", null), 0.0);

        assertEquals("0.0", Double.toString(CifMath.strToReal("-0.0", null)));
    }

    @Test(expected = CifEvalException.class)
    public void testStrToRealOverflow() throws CifEvalException {
        CifMath.strToReal("1e9999", null);
    }

    @Test(expected = CifEvalException.class)
    public void testStrToRealInvalid() throws CifEvalException {
        CifMath.strToReal("abc", null);
    }

    @Test
    public void testSubtract() throws CifEvalException {
        assertEquals(0, CifMath.subtract(5, 5, null));
        assertEquals(-1, CifMath.subtract(1, 2, null));

        assertEquals(0.0, CifMath.subtract(5.0, 5.0, null), 0.0);
        assertEquals(-1.0, CifMath.subtract(1.0, 2.0, null), 0.0);
        assertEquals(-0.13, CifMath.subtract(0.1, 0.23, null), 0.0);
    }

    @Test(expected = CifEvalException.class)
    public void testSubtractIntOverflow() throws CifEvalException {
        CifMath.subtract(-2147000000, 1000000, null);
    }

    @Test(expected = CifEvalException.class)
    public void testSubtractRealOverflow() throws CifEvalException {
        CifMath.subtract(-Double.MAX_VALUE, 1e299, null);
    }

    @Test
    public void testTrigSimple() throws CifEvalException {
        // From Python 2.7.1:
        // import math
        //
        // print math.sin(0.5)
        // print math.sinh(0.5)
        // print math.cos(0.5)
        // print math.cosh(0.5)
        // print math.tan(0.5)
        // print math.tanh(0.5)
        //
        // print math.asin(0.5)
        // print math.asinh(0.5)
        // print math.acos(0.5)
        // print math.acosh(math.pi)
        // print math.atan(0.5)
        // print math.atanh(0.5)
        //
        // 0.523598775598
        // 0.48121182506
        // 1.0471975512
        // 1.81152627246
        // 0.463647609001
        // 0.549306144334

        assertEquals(0.479425538604, CifMath.sin(0.5, null), 1e-10);
        assertEquals(0.521095305494, CifMath.sinh(0.5, null), 1e-10);
        assertEquals(0.87758256189, CifMath.cos(0.5, null), 1e-10);
        assertEquals(1.12762596521, CifMath.cosh(0.5, null), 1e-10);
        assertEquals(0.546302489844, CifMath.tan(0.5, null), 1e-10);
        assertEquals(0.46211715726, CifMath.tanh(0.5, null), 1e-10);

        assertEquals(0.523598775598, CifMath.asin(0.5, null), 1e-10);
        assertEquals(0.48121182506, CifMath.asinh(0.5, null), 1e-10);
        assertEquals(1.0471975512, CifMath.acos(0.5, null), 1e-10);
        assertEquals(1.81152627246, CifMath.acosh(Math.PI, null), 1e-10);
        assertEquals(0.463647609001, CifMath.atan(0.5, null), 1e-10);
        assertEquals(0.549306144334, CifMath.atanh(0.5, null), 1e-10);
    }

    @Test
    public void testTrigValidValues() {
        double d1 = Math.pow(2.0, 128);
        double d2 = Math.pow(2.0, 128 * 2);
        double d3 = Math.pow(2.0, 128 * 3);
        double d4 = Math.pow(2.0, 128 * 4);
        double d5 = Math.pow(2.0, 128 * 5);
        double d6 = Math.pow(2.0, 128 * 6);
        double d7 = Math.pow(2.0, 128 * 7);
        double[] inputs = {-Double.MAX_VALUE, -Double.MAX_VALUE / 2, -d7, -d6, -d5, -d4, -d3, -d2, -d1, -1000, -100,
                -10, -1, -0.5, -0.1, -0.0, 0.0, 0.1, 0.5, 1, 10, 100, 1000, d1, d2, d3, d4, d5, d6, d7,
                Double.MAX_VALUE / 2, Double.MAX_VALUE};

        for (double x: inputs) {
            Double[] values = new Double[12];

            try {
                values[0] = CifMath.sin(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[1] = CifMath.sinh(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[2] = CifMath.asin(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[3] = CifMath.asinh(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[4] = CifMath.cos(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[5] = CifMath.cosh(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[6] = CifMath.acos(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[7] = CifMath.acosh(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[8] = CifMath.tan(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[9] = CifMath.tanh(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[10] = CifMath.atan(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            try {
                values[11] = CifMath.atanh(x, null);
            } catch (CifEvalException e) {
                // Ignore.
            }

            for (int i = 0; i < values.length; i++) {
                if (values[i] == null) {
                    continue;
                }
                Assert.check(!Double.isNaN(values[i]));
                Assert.check(!Double.isInfinite(values[i]));
            }
        }
    }
}
