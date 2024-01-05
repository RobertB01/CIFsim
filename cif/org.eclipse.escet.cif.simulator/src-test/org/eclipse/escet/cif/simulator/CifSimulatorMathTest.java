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

package org.eclipse.escet.cif.simulator;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.eclipse.escet.cif.common.CifEvalUtils.objToStr;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.common.java.Assert;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link CifSimulatorMath} class. */
@SuppressWarnings("javadoc")
public class CifSimulatorMathTest {
    @Test
    public void testAndOr() {
        Boolean t1 = true;
        Boolean t2 = true;
        Boolean f1 = false;
        Boolean f2 = false;

        assertEquals(true, t1 || t2);
        assertEquals(true, t1 || f1);
        assertEquals(true, t1 || f2);
        assertEquals(true, t2 || t1);
        assertEquals(true, t2 || f1);
        assertEquals(true, t2 || f2);
        assertEquals(true, f1 || t1);
        assertEquals(true, f1 || t2);
        assertEquals(false, f1 || f2);
        assertEquals(true, f2 || t1);
        assertEquals(true, f2 || t2);
        assertEquals(false, f2 || f1);

        assertEquals(true, t1 && t2);
        assertEquals(false, t1 && f1);
        assertEquals(false, t1 && f2);
        assertEquals(true, t2 && t1);
        assertEquals(false, t2 && f1);
        assertEquals(false, t2 && f2);
        assertEquals(false, f1 && t1);
        assertEquals(false, f1 && t2);
        assertEquals(false, f1 && f2);
        assertEquals(false, f2 && t1);
        assertEquals(false, f2 && t2);
        assertEquals(false, f2 && f1);
    }

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

        assertEquals(false, Double.NaN == Double.NaN);
        assertEquals(true, POSITIVE_INFINITY == POSITIVE_INFINITY);
        assertEquals(true, NEGATIVE_INFINITY == NEGATIVE_INFINITY);
    }

    @Test
    public void testAbs() {
        assertEquals(5, CifSimulatorMath.abs(-5));
        assertEquals(1, CifSimulatorMath.abs(-1));
        assertEquals(0, CifSimulatorMath.abs(0));
        assertEquals(1, CifSimulatorMath.abs(1));
        assertEquals(5, CifSimulatorMath.abs(5));

        assertEquals(5.0, CifSimulatorMath.abs(-5.0), 0.0);
        assertEquals(1.0, CifSimulatorMath.abs(-1.0), 0.0);
        assertEquals(0.5, CifSimulatorMath.abs(-0.5), 0.0);
        assertEquals(0.0, CifSimulatorMath.abs(-0.0), 0.0);
        assertEquals(0.0, CifSimulatorMath.abs(0.0), 0.0);
        assertEquals(0.5, CifSimulatorMath.abs(0.5), 0.0);
        assertEquals(1.0, CifSimulatorMath.abs(1.0), 0.0);
        assertEquals(5.0, CifSimulatorMath.abs(5.0), 0.0);

        assertEquals("0.0", Double.toString(CifSimulatorMath.abs(-0.0)));
    }

    @Test
    public void testAbsOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.abs(Integer.MIN_VALUE));
    }

    @Test
    public void testAddInt() {
        assertEquals(0, CifSimulatorMath.addInt(-5, 5));
        assertEquals(3, CifSimulatorMath.addInt(1, 2));
    }

    @Test
    public void testAddReal() {
        assertEquals(0.0, CifSimulatorMath.addReal(-5.0, 5.0), 0.0);
        assertEquals(3.0, CifSimulatorMath.addReal(1.0, 2.0), 0.0);
        assertEquals(0.33, CifSimulatorMath.addReal(0.1, 0.23), 0.0);
    }

    @Test
    public void testAddIntOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.addInt(2147000000, 1000000));
    }

    @Test
    public void testAddRealOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.addReal(Double.MAX_VALUE, 1e299));
    }

    @Test
    public void testAddList() {
        assertEquals(list(1, 2, 3), CifSimulatorMath.addList(list(1), list(2, 3)));
        assertEquals(list(1, 2), CifSimulatorMath.addList(emptyList(), list(1, 2)));
        assertEquals(list(1, 2), CifSimulatorMath.addList(list(1, 2), emptyList()));
        assertEquals(emptyList(), CifSimulatorMath.addList(emptyList(), emptyList()));
    }

    @Test
    public void testAddString() {
        assertEquals("abc", CifSimulatorMath.addString("a", "bc"));
        assertEquals("ab", CifSimulatorMath.addString("", "ab"));
        assertEquals("ab", CifSimulatorMath.addString("ab", ""));
        assertEquals("", CifSimulatorMath.addString("", ""));
    }

    @Test
    public void testAddDict() {
        Map<Integer, Double> empty = map();

        Map<Integer, Double> d1 = map();
        d1.put(1, 1.0);

        Map<Integer, Double> d23 = map();
        d23.put(2, 2.0);
        d23.put(3, 3.0);

        Map<Integer, Double> d123 = map();
        d123.put(1, 1.0);
        d123.put(2, 2.0);
        d123.put(3, 3.0);

        Map<Integer, Double> d1m = map();
        d1m.put(1, 4.0);

        Map<Integer, Double> d123m = map();
        d123m.put(1, 4.0);
        d123m.put(2, 2.0);
        d123m.put(3, 3.0);

        assertEquals(d123, CifSimulatorMath.addDict(d1, d23));
        assertEquals(d23, CifSimulatorMath.addDict(empty, d23));
        assertEquals(d23, CifSimulatorMath.addDict(d23, empty));
        assertEquals(d123, CifSimulatorMath.addDict(d1, d23));
        assertEquals(d123m, CifSimulatorMath.addDict(d123, d1m));
    }

    @Test
    public void testAddPair() {
        Map<Integer, Double> d = map();

        Integer[] ks = {1, 2};
        Double[] vs = {1.0, 2.0};

        Map<Integer, Double> d2 = CifSimulatorMath.addpairs(d, ks, vs);
        assertSame(d, d2);
        assertEquals(2, d.size());
        assertEquals(vs[0], d.get(ks[0]), 0.0);
        assertEquals(vs[1], d.get(ks[1]), 0.0);
    }

    @Test
    public void testMakeDictDuplKey() {
        Map<Integer, Double> d = map();

        Integer[] ks = {1, 1};
        Double[] vs = {1.0, 1.0};

        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.addpairs(d, ks, vs));
    }

    @Test
    public void testBoolToStr() {
        assertEquals("true", CifSimulatorMath.boolToStr(true));
        assertEquals("false", CifSimulatorMath.boolToStr(false));
    }

    @Test
    public void testCbrt() {
        double sqrtMax = 5.643803094122362E102;
        assertEquals(0.0, CifSimulatorMath.cbrt(0.0), 0.0);
        assertEquals(3.0, CifSimulatorMath.cbrt(27.0), 0.0);
        assertEquals(sqrtMax, CifSimulatorMath.cbrt(Double.MAX_VALUE), 0.0);
        assertEquals(-sqrtMax, CifSimulatorMath.cbrt(-Double.MAX_VALUE), 0.0);
    }

    @Test
    public void testCeil() {
        assertEquals(-1, CifSimulatorMath.ceil(-1.0));
        assertEquals(0, CifSimulatorMath.ceil(-0.9));
        assertEquals(0, CifSimulatorMath.ceil(-0.5));
        assertEquals(0, CifSimulatorMath.ceil(-0.1));
        assertEquals(0, CifSimulatorMath.ceil(0.0));
        assertEquals(1, CifSimulatorMath.ceil(0.1));
        assertEquals(1, CifSimulatorMath.ceil(0.5));
        assertEquals(1, CifSimulatorMath.ceil(0.9));
        assertEquals(1, CifSimulatorMath.ceil(1.0));
    }

    @Test
    public void testCeilOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.ceil(1e99));
    }

    @Test
    public void testDelete() {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        assertEquals("[2, 3, 4]", objToStr(CifSimulatorMath.delete(l, 0)));
        assertEquals("[1, 3, 4]", objToStr(CifSimulatorMath.delete(l, 1)));
        assertEquals("[1, 2, 4]", objToStr(CifSimulatorMath.delete(l, 2)));
        assertEquals("[1, 2, 3]", objToStr(CifSimulatorMath.delete(l, 3)));
        assertEquals("[1, 2, 3]", objToStr(CifSimulatorMath.delete(l, -1)));
        assertEquals("[1, 2, 4]", objToStr(CifSimulatorMath.delete(l, -2)));
        assertEquals("[1, 3, 4]", objToStr(CifSimulatorMath.delete(l, -3)));
        assertEquals("[2, 3, 4]", objToStr(CifSimulatorMath.delete(l, -4)));
    }

    @Test
    public void testDeleteOutOfRange() {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.delete(l, 4));
    }

    @Test
    public void testDeleteOutOfRange2() {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.delete(l, -5));
    }

    @Test
    public void testDiv() {
        assertEquals(1, CifSimulatorMath.div(7, 4));
        assertEquals(-1, CifSimulatorMath.div(7, -4));
        assertEquals(-1, CifSimulatorMath.div(-7, 4));
        assertEquals(1, CifSimulatorMath.div(-7, -4));
    }

    @Test
    public void testDiv2() {
        // Test that 'a div b' is the same as
        // 'if (a / b) >= 0: floor(a / b) else ceil(a / b) end'.
        for (int a = -10; a < 10; a++) {
            for (int b = -10; b < 10; b++) {
                if (b == 0) {
                    continue;
                }
                assertEquals(div2(a, b), CifSimulatorMath.div(a, b));
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

    @Test
    public void testDivByZero() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.div(1, 0));
    }

    @Test
    public void testDivOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.div(Integer.MIN_VALUE, -1));
    }

    @Test
    public void testDivide() {
        assertEquals(0.5, CifSimulatorMath.divide(1.0, 2.0), 0.0);

        assertEquals("0.0", Double.toString(CifSimulatorMath.divide(-1e-99, Double.MAX_VALUE)));
    }

    @Test
    public void testDivideByZero() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.divide(1.0, 0.0));
    }

    @Test
    public void testDivideOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.divide(1e99, Double.MIN_VALUE));
    }

    @Test
    public void testEmptyList() {
        assertEquals(true, CifSimulatorMath.empty(emptyList()));
        assertEquals(false, CifSimulatorMath.empty(list(1)));
        assertEquals(false, CifSimulatorMath.empty(list(1, 2)));
    }

    @Test
    public void testEmptySet() {
        assertEquals(true, CifSimulatorMath.empty(emptySet()));
        assertEquals(false, CifSimulatorMath.empty(set(1)));
        assertEquals(false, CifSimulatorMath.empty(set(1, 2)));
    }

    @Test
    public void testEmptyDict() {
        Map<Integer, Double> d1 = map();
        d1.put(1, 1.0);

        Map<Integer, Double> d23 = map();
        d23.put(2, 2.0);
        d23.put(3, 3.0);

        assertEquals(true, CifSimulatorMath.empty(emptyMap()));
        assertEquals(false, CifSimulatorMath.empty(d1));
        assertEquals(false, CifSimulatorMath.empty(d23));
    }

    @SuppressWarnings("removal")
    @Test
    public void testEqualBool() {
        assertEquals(true, CifSimulatorMath.equal(true, true));
        assertEquals(false, CifSimulatorMath.equal(false, true));
        assertEquals(false, CifSimulatorMath.equal(true, false));
        assertEquals(true, CifSimulatorMath.equal(false, false));

        Boolean t = true;
        Boolean f = false;
        assertEquals(true, CifSimulatorMath.equal(false, f));
        assertEquals(true, CifSimulatorMath.equal(f, false));
        assertEquals(true, CifSimulatorMath.equal(true, t));
        assertEquals(true, CifSimulatorMath.equal(t, true));
        assertEquals(false, CifSimulatorMath.equal(true, f));
        assertEquals(false, CifSimulatorMath.equal(f, true));
        assertEquals(false, CifSimulatorMath.equal(false, t));
        assertEquals(false, CifSimulatorMath.equal(t, false));
        assertEquals(false, CifSimulatorMath.equal(t, f));

        Boolean t2 = first(list(true));
        assertEquals(true, CifSimulatorMath.equal(true, t2));
        assertEquals(true, CifSimulatorMath.equal(t2, true));
        assertEquals(false, CifSimulatorMath.equal(false, t2));
        assertEquals(false, CifSimulatorMath.equal(t2, false));
        assertEquals(true, CifSimulatorMath.equal(t, t2));
        assertEquals(true, CifSimulatorMath.equal(t2, t));

        Boolean t3 = new Boolean(true);
        Boolean t4 = new Boolean(true);
        assertEquals(true, CifSimulatorMath.equal(true, t2));
        assertEquals(true, CifSimulatorMath.equal(true, t3));
        assertEquals(true, CifSimulatorMath.equal(true, t4));
        assertEquals(false, CifSimulatorMath.equal(false, t2));
        assertEquals(false, CifSimulatorMath.equal(false, t3));
        assertEquals(false, CifSimulatorMath.equal(false, t4));
        assertEquals(true, CifSimulatorMath.equal(t2, true));
        assertEquals(true, CifSimulatorMath.equal(t3, true));
        assertEquals(true, CifSimulatorMath.equal(t4, true));
        assertEquals(false, CifSimulatorMath.equal(t2, false));
        assertEquals(false, CifSimulatorMath.equal(t3, false));
        assertEquals(false, CifSimulatorMath.equal(t4, false));
        assertEquals(true, CifSimulatorMath.equal(t3, t4));
        assertEquals(true, CifSimulatorMath.equal(t4, t3));
    }

    @SuppressWarnings("removal")
    @Test
    public void testEqualInt() {
        assertEquals(true, CifSimulatorMath.equal(-0, 0));
        assertEquals(true, CifSimulatorMath.equal(0, -0));
        assertEquals(true, CifSimulatorMath.equal(0, 0));
        assertEquals(true, CifSimulatorMath.equal(1, 1));
        assertEquals(true, CifSimulatorMath.equal(-1, -1));
        assertEquals(true, CifSimulatorMath.equal(12345, 12345));
        assertEquals(false, CifSimulatorMath.equal(0, 1));
        assertEquals(false, CifSimulatorMath.equal(1, 0));
        assertEquals(false, CifSimulatorMath.equal(12345, 12346));

        Integer i = 12345;
        Integer j = 12346;
        assertEquals(true, CifSimulatorMath.equal(12346, j));
        assertEquals(true, CifSimulatorMath.equal(j, 12346));
        assertEquals(true, CifSimulatorMath.equal(12345, i));
        assertEquals(true, CifSimulatorMath.equal(i, 12345));
        assertEquals(false, CifSimulatorMath.equal(12345, j));
        assertEquals(false, CifSimulatorMath.equal(j, 12345));
        assertEquals(false, CifSimulatorMath.equal(12346, i));
        assertEquals(false, CifSimulatorMath.equal(i, 12346));
        assertEquals(false, CifSimulatorMath.equal(i, j));

        Integer k = first(list(12345));
        assertEquals(true, CifSimulatorMath.equal(12345, k));
        assertEquals(true, CifSimulatorMath.equal(k, 12345));
        assertEquals(false, CifSimulatorMath.equal(12346, k));
        assertEquals(false, CifSimulatorMath.equal(k, 12346));
        assertEquals(true, CifSimulatorMath.equal(i, k));
        assertEquals(true, CifSimulatorMath.equal(k, i));

        Integer l = new Integer(12345);
        Integer m = new Integer(12345);
        assertEquals(true, CifSimulatorMath.equal(12345, k));
        assertEquals(true, CifSimulatorMath.equal(12345, l));
        assertEquals(true, CifSimulatorMath.equal(12345, m));
        assertEquals(false, CifSimulatorMath.equal(12346, k));
        assertEquals(false, CifSimulatorMath.equal(12346, l));
        assertEquals(false, CifSimulatorMath.equal(12346, m));
        assertEquals(true, CifSimulatorMath.equal(k, 12345));
        assertEquals(true, CifSimulatorMath.equal(l, 12345));
        assertEquals(true, CifSimulatorMath.equal(m, 12345));
        assertEquals(false, CifSimulatorMath.equal(k, 12346));
        assertEquals(false, CifSimulatorMath.equal(l, 12346));
        assertEquals(false, CifSimulatorMath.equal(m, 12346));
        assertEquals(true, CifSimulatorMath.equal(l, m));
        assertEquals(true, CifSimulatorMath.equal(m, l));
    }

    @SuppressWarnings("removal")
    @Test
    public void testEqualReal() {
        assertEquals(true, CifSimulatorMath.equal(-0.0, 0.0));
        assertEquals(true, CifSimulatorMath.equal(0.0, -0.0));
        assertEquals(true, CifSimulatorMath.equal(0.0, 0.0));
        assertEquals(true, CifSimulatorMath.equal(0.45, 0.45));
        assertEquals(true, CifSimulatorMath.equal(1.0, 1.0));
        assertEquals(true, CifSimulatorMath.equal(-1.0, -1.0));
        assertEquals(true, CifSimulatorMath.equal(12345.0, 12345.0));
        assertEquals(false, CifSimulatorMath.equal(0.0, 1.0));
        assertEquals(false, CifSimulatorMath.equal(1.0, 0.0));
        assertEquals(false, CifSimulatorMath.equal(12345.0, 12346.0));

        Double i = 12345.0;
        Double j = 12346.0;
        assertEquals(true, CifSimulatorMath.equal(12346.0, j));
        assertEquals(true, CifSimulatorMath.equal(j, 12346.0));
        assertEquals(true, CifSimulatorMath.equal(12345.0, i));
        assertEquals(true, CifSimulatorMath.equal(i, 12345.0));
        assertEquals(false, CifSimulatorMath.equal(12345.0, j));
        assertEquals(false, CifSimulatorMath.equal(j, 12345.0));
        assertEquals(false, CifSimulatorMath.equal(12346.0, i));
        assertEquals(false, CifSimulatorMath.equal(i, 12346.0));
        assertEquals(false, CifSimulatorMath.equal(i, j));

        Double k = first(list(12345.0));
        assertEquals(true, CifSimulatorMath.equal(12345.0, k));
        assertEquals(true, CifSimulatorMath.equal(k, 12345.0));
        assertEquals(false, CifSimulatorMath.equal(12346.0, k));
        assertEquals(false, CifSimulatorMath.equal(k, 12346.0));
        assertEquals(true, CifSimulatorMath.equal(i, k));
        assertEquals(true, CifSimulatorMath.equal(k, i));

        Double l = new Double(12345.0);
        Double m = new Double(12345.0);
        assertEquals(true, CifSimulatorMath.equal(12345.0, k));
        assertEquals(true, CifSimulatorMath.equal(12345.0, l));
        assertEquals(true, CifSimulatorMath.equal(12345.0, m));
        assertEquals(false, CifSimulatorMath.equal(12346.0, k));
        assertEquals(false, CifSimulatorMath.equal(12346.0, l));
        assertEquals(false, CifSimulatorMath.equal(12346.0, m));
        assertEquals(true, CifSimulatorMath.equal(k, 12345.0));
        assertEquals(true, CifSimulatorMath.equal(l, 12345.0));
        assertEquals(true, CifSimulatorMath.equal(m, 12345.0));
        assertEquals(false, CifSimulatorMath.equal(k, 12346.0));
        assertEquals(false, CifSimulatorMath.equal(l, 12346.0));
        assertEquals(false, CifSimulatorMath.equal(m, 12346.0));
        assertEquals(true, CifSimulatorMath.equal(l, m));
        assertEquals(true, CifSimulatorMath.equal(m, l));
    }

    @Test
    public void testEqualOthers() {
        assertEquals(true, CifSimulatorMath.equal(list(1), list(1)));
        assertEquals(false, CifSimulatorMath.equal(list(1), list(2)));
    }

    @Test
    public void testExp() {
        assertEquals(Math.E, CifSimulatorMath.exp(1.0), 1e-15);
        assertEquals(Math.E * Math.E, CifSimulatorMath.exp(2.0), 1e-15);
    }

    @Test
    public void testExpOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.exp(1e99));
    }

    @Test
    public void testFloor() {
        assertEquals(-1, CifSimulatorMath.floor(-1.0));
        assertEquals(-1, CifSimulatorMath.floor(-0.9));
        assertEquals(-1, CifSimulatorMath.floor(-0.5));
        assertEquals(-1, CifSimulatorMath.floor(-0.1));
        assertEquals(0, CifSimulatorMath.floor(0.0));
        assertEquals(0, CifSimulatorMath.floor(0.1));
        assertEquals(0, CifSimulatorMath.floor(0.5));
        assertEquals(0, CifSimulatorMath.floor(0.9));
        assertEquals(1, CifSimulatorMath.floor(1.0));
    }

    @Test
    public void testFloorOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.floor(-1e99));
    }

    @Test
    public void testHash() {
        assertEquals(1231, CifSimulatorMath.hash(true));
        assertEquals(1237, CifSimulatorMath.hash(false));

        assertEquals(-123, CifSimulatorMath.hash(-123));
        assertEquals(0, CifSimulatorMath.hash(0));
        assertEquals(124, CifSimulatorMath.hash(124));

        assertEquals(0, CifSimulatorMath.hash(0.0));
        assertEquals(-1507852285, CifSimulatorMath.hash(0.1));
        assertEquals(1072693248, CifSimulatorMath.hash(1.0));
        assertEquals(-1074790400, CifSimulatorMath.hash(-1.0));
        assertEquals(1419533177, CifSimulatorMath.hash(1.23e99));

        List<Integer> li = list(3, 4, 5);
        int liHash = ((((1 * 31) + 3) * 31) + 4) * 31 + 5;
        assertEquals(liHash, CifSimulatorMath.hash(li));
    }

    @Test
    public void testInList() {
        assertEquals(true, CifSimulatorMath.in(1, list(1)));
        assertEquals(true, CifSimulatorMath.in(1, list(1, 2)));
        assertEquals(true, CifSimulatorMath.in(1, list(1, 1, 2)));

        assertEquals(false, CifSimulatorMath.in(3, emptyList()));
        assertEquals(false, CifSimulatorMath.in(3, list(1)));
        assertEquals(false, CifSimulatorMath.in(3, list(1, 2)));
        assertEquals(false, CifSimulatorMath.in(3, list(1, 1, 2)));
    }

    @Test
    public void testInSet() {
        assertEquals(true, CifSimulatorMath.in(1, set(1)));
        assertEquals(true, CifSimulatorMath.in(1, set(1, 2)));
        assertEquals(true, CifSimulatorMath.in(1, set(1, 1, 2)));

        assertEquals(false, CifSimulatorMath.in(3, emptySet()));
        assertEquals(false, CifSimulatorMath.in(3, set(1)));
        assertEquals(false, CifSimulatorMath.in(3, set(1, 2)));
        assertEquals(false, CifSimulatorMath.in(3, set(1, 1, 2)));
    }

    @Test
    public void testInDict() {
        Map<Integer, Double> d1 = map();
        d1.put(1, 1.0);

        Map<Integer, Double> d12 = map();
        d12.put(1, 1.0);
        d12.put(2, 2.0);

        assertEquals(true, CifSimulatorMath.in(1, d1));
        assertEquals(true, CifSimulatorMath.in(1, d12));

        assertEquals(false, CifSimulatorMath.in(3, d1));
        assertEquals(false, CifSimulatorMath.in(3, d12));
    }

    @Test
    public void testIntersection() {
        assertEquals(emptySet(), CifSimulatorMath.intersection(emptySet(), emptySet()));

        assertEquals(emptySet(), CifSimulatorMath.intersection(emptySet(), set(1)));

        assertEquals(emptySet(), CifSimulatorMath.intersection(set(1), emptySet()));

        assertEquals(set(1), CifSimulatorMath.intersection(set(1), set(1)));

        assertEquals(set(1), CifSimulatorMath.intersection(set(1), set(1, 2)));

        assertEquals(set(1), CifSimulatorMath.intersection(set(1, 2), set(1)));

        assertEquals(set(2, 3), CifSimulatorMath.intersection(set(1, 2, 3), set(2, 3, 4)));
    }

    @Test
    public void testIntToReal() {
        assertEquals(-2147483648.0, CifSimulatorMath.intToReal(Integer.MIN_VALUE), 0.0);
        assertEquals(-1.0, CifSimulatorMath.intToReal(-1), 0.0);
        assertEquals(0.0, CifSimulatorMath.intToReal(0), 0.0);
        assertEquals(1.0, CifSimulatorMath.intToReal(1), 0.0);
        assertEquals(2147483647.0, CifSimulatorMath.intToReal(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testIntToStr() {
        assertEquals("-2147483648", CifSimulatorMath.intToStr(Integer.MIN_VALUE));
        assertEquals("-1", CifSimulatorMath.intToStr(-1));
        assertEquals("0", CifSimulatorMath.intToStr(0));
        assertEquals("1", CifSimulatorMath.intToStr(1));
        assertEquals("2147483647", CifSimulatorMath.intToStr(Integer.MAX_VALUE));
    }

    @Test
    public void testLn() {
        assertEquals(1.0, CifSimulatorMath.ln(Math.E), 0.0);
        assertEquals(2.0, CifSimulatorMath.ln(Math.E * Math.E), 0.0);
    }

    @Test
    public void testLnNonPos() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.ln(0.0));
    }

    @Test
    public void testLog() {
        assertEquals(1.0, CifSimulatorMath.log(10.0), 0.0);
        assertEquals(2.0, CifSimulatorMath.log(100.0), 0.0);
    }

    @Test
    public void testLogNonPos() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.log(0.0));
    }

    @Test
    public void testMakeList() {
        assertEquals(list(), CifSimulatorMath.makelist(list()));
        assertEquals(list(1), CifSimulatorMath.makelist(list(), 1));
        assertEquals(list(1, 1), CifSimulatorMath.makelist(list(), 1, 1));
        assertEquals(list(1, 2), CifSimulatorMath.makelist(list(), 1, 2));

        List<Integer> l1 = list();
        List<Integer> l2 = CifSimulatorMath.makelist(l1);
        assertSame(l1, l2);
        assertEquals(0, l2.size());

        List<Integer> l3 = CifSimulatorMath.makelist(l1, 3, 4);
        assertSame(l1, l3);
        assertSame(l2, l3);
        assertEquals(list(3, 4), l3);
    }

    @Test
    public void testMakeSet() {
        assertEquals(set(), CifSimulatorMath.makeset(set()));
        assertEquals(set(1), CifSimulatorMath.makeset(set(), 1));
        assertEquals(set(1), CifSimulatorMath.makeset(set(), 1, 1));
        assertEquals(set(1, 2), CifSimulatorMath.makeset(set(), 1, 2));

        Set<Integer> s1 = set();
        Set<Integer> s2 = CifSimulatorMath.makeset(s1);
        assertSame(s1, s2);
        assertEquals(0, s2.size());

        Set<Integer> s3 = CifSimulatorMath.makeset(s1, 3, 4);
        assertSame(s1, s3);
        assertSame(s2, s3);
        assertEquals(set(3, 4), s3);
    }

    @Test
    public void testMax() {
        assertEquals(5, CifSimulatorMath.max(-5, 5));
        assertEquals(-5, CifSimulatorMath.max(-5, -5));
        assertEquals(5, CifSimulatorMath.max(5, 5));
        assertEquals(5, CifSimulatorMath.max(5, -5));

        assertEquals(5.0, CifSimulatorMath.max(-5.0, 5.0), 0.0);
        assertEquals(-5.0, CifSimulatorMath.max(-5.0, -5.0), 0.0);
        assertEquals(5.0, CifSimulatorMath.max(5.0, 5.0), 0.0);
        assertEquals(5.0, CifSimulatorMath.max(5.0, -5.0), 0.0);
    }

    @Test
    public void testMin() {
        assertEquals(-5, CifSimulatorMath.min(-5, 5));
        assertEquals(-5, CifSimulatorMath.min(-5, -5));
        assertEquals(5, CifSimulatorMath.min(5, 5));
        assertEquals(-5, CifSimulatorMath.min(5, -5));

        assertEquals(-5.0, CifSimulatorMath.min(-5.0, 5.0), 0.0);
        assertEquals(-5.0, CifSimulatorMath.min(-5.0, -5.0), 0.0);
        assertEquals(5.0, CifSimulatorMath.min(5.0, 5.0), 0.0);
        assertEquals(-5.0, CifSimulatorMath.min(5.0, -5.0), 0.0);
    }

    @Test
    public void testMod() {
        assertEquals(3, CifSimulatorMath.mod(7, 4));
        assertEquals(3, CifSimulatorMath.mod(7, -4));
        assertEquals(-3, CifSimulatorMath.mod(-7, 4));
        assertEquals(-3, CifSimulatorMath.mod(-7, -4));
    }

    @Test
    public void testMod2() {
        // Test that 'a mod b' is the same as 'a - b * (a div b)'.
        for (int a = -10; a < 10; a++) {
            for (int b = -10; b < 10; b++) {
                if (b == 0) {
                    continue;
                }
                assertEquals(mod2(a, b), CifSimulatorMath.mod(a, b));
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

    @Test
    public void testModByZero() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.mod(1, 0));
    }

    @Test
    public void testModifyList() {
        Object o0 = new Object();
        Object o1 = new Object();
        Object o2 = new Object();
        Object o9 = new Object();
        List<Object> lst1 = list(o0, o1, o2);
        List<Object> lst2 = CifSimulatorMath.modify(lst1, 1, o9);
        assertNotSame(lst1, lst2);
        assertEquals(3, lst2.size());
        assertSame(o0, lst2.get(0));
        assertSame(o9, lst2.get(1));
        assertSame(o2, lst2.get(2));
    }

    @Test
    public void testModifyDict() {
        Object k0 = new Object();
        Object k1 = new Object();
        Object k2 = new Object();
        Object k9 = new Object();
        Object v0 = new Object();
        Object v1 = new Object();
        Object v2 = new Object();
        Object v9 = new Object();

        Map<Object, Object> d1 = map();
        d1.put(k0, v0);
        d1.put(k1, v1);
        d1.put(k2, v2);

        Map<Object, Object> d2 = CifSimulatorMath.modify(d1, k9, v9);
        assertNotSame(d1, d2);
        assertEquals(4, d2.size());
        assertSame(v0, d2.get(k0));
        assertSame(v1, d2.get(k1));
        assertSame(v2, d2.get(k2));
        assertSame(v9, d2.get(k9));

        d2 = CifSimulatorMath.modify(d2, k9, v0);
        assertNotSame(d2, d1);
        assertEquals(4, d2.size());
        assertSame(v0, d2.get(k0));
        assertSame(v1, d2.get(k1));
        assertSame(v2, d2.get(k2));
        assertSame(v0, d2.get(k9));
    }

    @Test
    public void testModifyListOutOfBounds() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.modify(list(1), 2, 5));
    }

    @Test
    public void testMultiply() {
        assertEquals(-25, CifSimulatorMath.multiply(-5, 5));
        assertEquals(2, CifSimulatorMath.multiply(1, 2));

        assertEquals(-25.0, CifSimulatorMath.multiply(-5.0, 5.0), 0.0);
        assertEquals(2.0, CifSimulatorMath.multiply(1.0, 2.0), 0.0);
        assertEquals(0.023, CifSimulatorMath.multiply(0.1, 0.23), 1e-15);
    }

    @Test
    public void testMultiplyIntOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.multiply(111111, 222222));
    }

    @Test
    public void testMultiplyRealOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.multiply(1e199, 1e199));
    }

    @Test
    public void testNegate() {
        assertEquals(5, CifSimulatorMath.negate(-5));
        assertEquals(1, CifSimulatorMath.negate(-1));
        assertEquals(0, CifSimulatorMath.negate(0));
        assertEquals(-1, CifSimulatorMath.negate(1));
        assertEquals(-5, CifSimulatorMath.negate(5));

        assertEquals(5.0, CifSimulatorMath.negate(-5.0), 0.0);
        assertEquals(1.0, CifSimulatorMath.negate(-1.0), 0.0);
        assertEquals(0.5, CifSimulatorMath.negate(-0.5), 0.0);
        assertEquals(0.0, CifSimulatorMath.negate(-0.0), 0.0);
        assertEquals(0.0, CifSimulatorMath.negate(0.0), 0.0);
        assertEquals(-0.5, CifSimulatorMath.negate(0.5), 0.0);
        assertEquals(-1.0, CifSimulatorMath.negate(1.0), 0.0);
        assertEquals(-5.0, CifSimulatorMath.negate(5.0), 0.0);

        assertEquals("0.0", Double.toString(CifSimulatorMath.negate(0.0)));
    }

    @Test
    public void testNegateIntOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.negate(Integer.MIN_VALUE));
    }

    @Test
    public void testPowInt() {
        assertEquals(8, CifSimulatorMath.powInt(2, 3));
        assertEquals(-8, CifSimulatorMath.powInt(-2, 3));
        assertEquals(1, CifSimulatorMath.powInt(1, 0));
        assertEquals(1, CifSimulatorMath.powInt(0, 0));
    }

    @Test
    public void testPowIntOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.powInt(12345, 99999));
    }

    @Test
    public void testPowReal() {
        assertEquals(8.0, CifSimulatorMath.powReal(2.0, 3.0), 0.0);
        assertEquals(-8.0, CifSimulatorMath.powReal(-2.0, 3.0), 0.0);
        assertEquals(1.0, CifSimulatorMath.powReal(1.0, 0.0), 0.0);
        assertEquals(1.0, CifSimulatorMath.powReal(0.0, 0.0), 0.0);
        assertEquals(3.0, CifSimulatorMath.powReal(9.0, 0.5), 0.0);

        assertEquals(0.125, CifSimulatorMath.powReal(2, -3), 0.0);
        assertEquals(0.125, CifSimulatorMath.powReal(2.0, -3), 0.0);
        assertEquals(0.125, CifSimulatorMath.powReal(2, -3.0), 0.0);
        assertEquals(0.125, CifSimulatorMath.powReal(2.0, -3.0), 0.0);
    }

    @Test
    public void testPowRealOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.powReal(1e99, 1e99));
    }

    @Test
    public void testPowZeroToNeg() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.powReal(0.0, -1.0));
    }

    @Test
    public void testPowNegToNonInt() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.powReal(-1.0, 0.5));
    }

    @Test
    public void testRealToStr() {
        assertEquals("0.0", CifSimulatorMath.realToStr(0.0));
        assertEquals("0.1", CifSimulatorMath.realToStr(0.1));
        assertEquals("1.0", CifSimulatorMath.realToStr(1.0));
        assertEquals("1.1", CifSimulatorMath.realToStr(1.1));
        assertEquals("1.0e99", CifSimulatorMath.realToStr(1e99));
        assertEquals("0.123456789", CifSimulatorMath.realToStr(0.123456789));
        assertEquals("1.23456789e-5", CifSimulatorMath.realToStr(0.0000123456789));
    }

    @Test
    public void testProjectList() {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        assertEquals(1, CifSimulatorMath.project(l, 0));
        assertEquals(2, CifSimulatorMath.project(l, 1));
        assertEquals(3, CifSimulatorMath.project(l, 2));
        assertEquals(4, CifSimulatorMath.project(l, 3));
        assertEquals(4, CifSimulatorMath.project(l, -1));
        assertEquals(3, CifSimulatorMath.project(l, -2));
        assertEquals(2, CifSimulatorMath.project(l, -3));
        assertEquals(1, CifSimulatorMath.project(l, -4));
    }

    @Test
    public void testProjectString() {
        String s = "1234";
        assertEquals("1", CifSimulatorMath.project(s, 0));
        assertEquals("2", CifSimulatorMath.project(s, 1));
        assertEquals("3", CifSimulatorMath.project(s, 2));
        assertEquals("4", CifSimulatorMath.project(s, 3));
        assertEquals("4", CifSimulatorMath.project(s, -1));
        assertEquals("3", CifSimulatorMath.project(s, -2));
        assertEquals("2", CifSimulatorMath.project(s, -3));
        assertEquals("1", CifSimulatorMath.project(s, -4));
    }

    @Test
    public void testProjectDict() {
        Map<Object, Object> d = map();
        d.put(5, 6);
        d.put(7, 8);
        assertEquals(6, CifSimulatorMath.project(d, 5));
        assertEquals(8, CifSimulatorMath.project(d, 7));
    }

    @Test
    public void testProjectListOutOfRange1() {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.project(l, 4));
    }

    @Test
    public void testProjectListOutOfRange2() {
        List<Object> l = list();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.project(l, -5));
    }

    @Test
    public void testProjectStringOutOfRange1() {
        String s = "1234";
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.project(s, 4));
    }

    @Test
    public void testProjectStringOutOfRange2() {
        String s = "1234";
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.project(s, -5));
    }

    @Test
    public void testProjectDictKeyNotFound() {
        Map<Integer, Integer> d = map();
        d.put(5, 6);
        d.put(7, 8);
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.project(d, 9));
    }

    @Test
    public void testRound() {
        assertEquals(-1, CifSimulatorMath.round(-1.0));
        assertEquals(-1, CifSimulatorMath.round(-0.9));
        assertEquals(0, CifSimulatorMath.round(-0.5));
        assertEquals(0, CifSimulatorMath.round(-0.1));
        assertEquals(0, CifSimulatorMath.round(0.0));
        assertEquals(0, CifSimulatorMath.round(0.1));
        assertEquals(1, CifSimulatorMath.round(0.5));
        assertEquals(1, CifSimulatorMath.round(0.9));
        assertEquals(1, CifSimulatorMath.round(1.0));

        assertEquals(-2147483648, CifSimulatorMath.round(-2147483648.5));
        assertEquals(-2147483648, CifSimulatorMath.round(-2147483648));
        assertEquals(2147483647, CifSimulatorMath.round(2147483647));
        assertEquals(2147483647, CifSimulatorMath.round(2147483647.49));
    }

    @Test
    public void testRoundOverflow1() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.round(-2147483648.51));
    }

    @Test
    public void testRoundOverflow2() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.round(2147483647.50));
    }

    @Test
    public void testScale() {
        // [0..1] -> [0..3]
        assertEquals(0.0, CifSimulatorMath.scale(0.0, 0, 1, 0, 3), 1e-15);
        assertEquals(0.3, CifSimulatorMath.scale(0.1, 0, 1, 0, 3), 1e-15);
        assertEquals(0.6, CifSimulatorMath.scale(0.2, 0, 1, 0, 3), 1e-15);
        assertEquals(1.5, CifSimulatorMath.scale(0.5, 0, 1, 0, 3), 1e-15);
        assertEquals(3.0, CifSimulatorMath.scale(1.0, 0, 1, 0, 3), 1e-15);

        // [1..2] -> [5..8]
        assertEquals(5.0, CifSimulatorMath.scale(1.0, 1, 2, 5, 8), 1e-15);
        assertEquals(5.3, CifSimulatorMath.scale(1.1, 1, 2, 5, 8), 1e-15);
        assertEquals(5.6, CifSimulatorMath.scale(1.2, 1, 2, 5, 8), 1e-15);
        assertEquals(6.5, CifSimulatorMath.scale(1.5, 1, 2, 5, 8), 1e-15);
        assertEquals(8.0, CifSimulatorMath.scale(2.0, 1, 2, 5, 8), 1e-15);

        // [1..2] -> [10..8] (increasing -> decreasing)
        assertEquals(9.5, CifSimulatorMath.scale(1.25, 1, 2, 10, 8), 1e-15);

        // [1..2] -> [3..7] (outside input interval)
        assertEquals(-1.0, CifSimulatorMath.scale(0, 1, 2, 3, 7), 1e-15);
        assertEquals(3.0, CifSimulatorMath.scale(1, 1, 2, 3, 7), 1e-15);
        assertEquals(7.0, CifSimulatorMath.scale(2, 1, 2, 3, 7), 1e-15);
        assertEquals(11.0, CifSimulatorMath.scale(3, 1, 2, 3, 7), 1e-15);
    }

    @Test
    public void testScaleOverflow() {
        // Overflow in multiplication.
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.scale(1e99, 0, 1e-10, -1e299, 1e299));
    }

    @Test
    public void testScaleEmptyInputInterval() {
        // Division by zero in division.
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.scale(123.0, 1, 1, 3, 4));
    }

    @Test
    public void testSign() {
        assertEquals(-1, CifSimulatorMath.sign(-99));
        assertEquals(-1, CifSimulatorMath.sign(-2));
        assertEquals(-1, CifSimulatorMath.sign(-1));
        assertEquals(0, CifSimulatorMath.sign(0));
        assertEquals(1, CifSimulatorMath.sign(1));
        assertEquals(1, CifSimulatorMath.sign(2));
        assertEquals(1, CifSimulatorMath.sign(99));

        assertEquals(-1, CifSimulatorMath.sign(-99.0));
        assertEquals(-1, CifSimulatorMath.sign(-2.0));
        assertEquals(-1, CifSimulatorMath.sign(-1.0));
        assertEquals(-1, CifSimulatorMath.sign(-0.5));
        assertEquals(-1, CifSimulatorMath.sign(-0.1));
        assertEquals(0, CifSimulatorMath.sign(0.0));
        assertEquals(1, CifSimulatorMath.sign(0.1));
        assertEquals(1, CifSimulatorMath.sign(0.5));
        assertEquals(1, CifSimulatorMath.sign(1.0));
        assertEquals(1, CifSimulatorMath.sign(2.0));
        assertEquals(1, CifSimulatorMath.sign(99.0));
    }

    @Test
    public void testSizeString() {
        assertEquals(0, CifSimulatorMath.size(""));
        assertEquals(1, CifSimulatorMath.size("a"));
        assertEquals(2, CifSimulatorMath.size("ab"));
        assertEquals(3, CifSimulatorMath.size("a\nb"));
        assertEquals(3, CifSimulatorMath.size("a\tb"));
        assertEquals(3, CifSimulatorMath.size("a\\b"));
        assertEquals(3, CifSimulatorMath.size("a\"b"));
    }

    @Test
    public void testSizeList() {
        assertEquals(0, CifSimulatorMath.size(emptyList()));
        assertEquals(1, CifSimulatorMath.size(list(1)));
        assertEquals(2, CifSimulatorMath.size(list(1, 1)));
        assertEquals(2, CifSimulatorMath.size(list(1, 2)));
        assertEquals(3, CifSimulatorMath.size(list(1, 1, 1)));
        assertEquals(3, CifSimulatorMath.size(list(1, 2, 1)));
        assertEquals(3, CifSimulatorMath.size(list(1, 2, 3)));
    }

    @Test
    public void testSizeSet() {
        assertEquals(0, CifSimulatorMath.size(emptySet()));
        assertEquals(1, CifSimulatorMath.size(set(1)));
        assertEquals(1, CifSimulatorMath.size(set(1, 1)));
        assertEquals(2, CifSimulatorMath.size(set(1, 2)));
        assertEquals(1, CifSimulatorMath.size(set(1, 1, 1)));
        assertEquals(2, CifSimulatorMath.size(set(1, 2, 1)));
        assertEquals(3, CifSimulatorMath.size(set(1, 2, 3)));
    }

    @Test
    public void testSizeDict() {
        Map<Integer, Double> empty = map();

        Map<Integer, Double> d1 = map();
        d1.put(1, 1.0);

        Map<Integer, Double> d12 = map();
        d12.put(1, 1.0);
        d12.put(2, 2.0);

        Map<Integer, Double> d11 = map();
        d11.put(1, 1.0);
        d11.put(1, 2.0);

        assertEquals(0, CifSimulatorMath.size(empty));
        assertEquals(1, CifSimulatorMath.size(d1));
        assertEquals(2, CifSimulatorMath.size(d12));
        assertEquals(1, CifSimulatorMath.size(d11));
    }

    @Test
    public void testSlice() {
        // Implementation redirects to common.java project, which already has
        // extensive tests.
    }

    @Test
    public void testSqrt() {
        assertEquals(3.0, CifSimulatorMath.sqrt(9.0), 0.0);
        assertEquals(4.0, CifSimulatorMath.sqrt(16.0), 0.0);
    }

    @Test
    public void testSqrtNeg() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.sqrt(-Double.MIN_VALUE));
    }

    @Test
    public void testStrToBool() {
        assertEquals(true, CifSimulatorMath.strToBool("true"));
        assertEquals(false, CifSimulatorMath.strToBool("false"));
    }

    @Test
    public void testStrToBoolInvalid() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.strToBool("True"));
    }

    @Test
    public void testStrToInt() {
        assertEquals(0, CifSimulatorMath.strToInt("0"));
        assertEquals(0, CifSimulatorMath.strToInt("-0"));
        assertEquals(1, CifSimulatorMath.strToInt("1"));
        assertEquals(-1, CifSimulatorMath.strToInt("-1"));
        assertEquals(123, CifSimulatorMath.strToInt("123"));
        assertEquals(-123, CifSimulatorMath.strToInt("-123"));
        assertEquals(2147483647, CifSimulatorMath.strToInt("2147483647"));
        assertEquals(-2147483648, CifSimulatorMath.strToInt("-2147483648"));
    }

    @Test
    public void testStrToIntOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.strToInt("2147483648"));
    }

    @Test
    public void testStrToIntInvalid() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.strToInt("abc"));
    }

    @Test
    public void testStrToReal() {
        assertEquals(0.0, CifSimulatorMath.strToReal("0.0"), 0.0);
        assertEquals(0.0, CifSimulatorMath.strToReal("-0.0"), 0.0);
        assertEquals(300, CifSimulatorMath.strToReal("3e2"), 0.0);
        assertEquals(0.00001, CifSimulatorMath.strToReal("1e-5"), 0.0);
        assertEquals(100000, CifSimulatorMath.strToReal("1E+05"), 0.0);
        assertEquals(123.5e-5, CifSimulatorMath.strToReal("123.5e-5"), 0.0);
        assertEquals(123.4, CifSimulatorMath.strToReal("123.4"), 0.0);

        assertEquals("0.0", Double.toString(CifSimulatorMath.strToReal("-0.0")));
    }

    @Test
    public void testStrToRealOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.strToReal("1e9999"));
    }

    @Test
    public void testStrToRealInvalid() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.strToReal("abc"));
    }

    @Test
    public void testSubset() {
        assertEquals(true, CifSimulatorMath.subset(emptySet(), emptySet()));
        assertEquals(true, CifSimulatorMath.subset(emptySet(), set(1)));
        assertEquals(false, CifSimulatorMath.subset(set(1), emptySet()));
        assertEquals(true, CifSimulatorMath.subset(set(1), set(1)));

        assertEquals(false, CifSimulatorMath.subset(set(1, 2), set(1)));
        assertEquals(true, CifSimulatorMath.subset(set(1), set(1, 2)));

        assertEquals(true, CifSimulatorMath.subset(set(1, 1), set(1)));
        assertEquals(true, CifSimulatorMath.subset(set(1), set(1, 1)));
    }

    @Test
    public void testSubtractInt() {
        assertEquals(0, CifSimulatorMath.subtract(5, 5));
        assertEquals(-1, CifSimulatorMath.subtract(1, 2));
    }

    @Test
    public void testSubtractReal() {
        assertEquals(0.0, CifSimulatorMath.subtract(5.0, 5.0), 0.0);
        assertEquals(-1.0, CifSimulatorMath.subtract(1.0, 2.0), 0.0);
        assertEquals(-0.13, CifSimulatorMath.subtract(0.1, 0.23), 0.0);
    }

    @Test
    public void testSubtractIntOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.subtract(-2147000000, 1000000));
    }

    @Test
    public void testSubtractRealOverflow() {
        assertThrows(CifSimulatorException.class, () -> CifSimulatorMath.subtract(-Double.MAX_VALUE, 1e299));
    }

    @Test
    public void testSubtractSet() {
        assertEquals(emptySet(), CifSimulatorMath.subtract(emptySet(), emptySet()));
        assertEquals(emptySet(), CifSimulatorMath.subtract(emptySet(), set(1)));
        assertEquals(set(1), CifSimulatorMath.subtract(set(1), emptySet()));
        assertEquals(emptySet(), CifSimulatorMath.subtract(set(1), set(1)));

        assertEquals(set(2), CifSimulatorMath.subtract(set(1, 2), set(1)));
        assertEquals(emptySet(), CifSimulatorMath.subtract(set(1), set(1, 2)));

        assertEquals(emptySet(), CifSimulatorMath.subtract(set(1, 1), set(1)));
        assertEquals(emptySet(), CifSimulatorMath.subtract(set(1), set(1, 1)));

        assertEquals(set(1, 3), CifSimulatorMath.subtract(set(1, 2, 3, 4), set(5, 4, 2)));
    }

    @Test
    public void testSubtractDictDict() {
        Map<Integer, Double> empty = map();

        Map<Integer, Double> d1 = map();
        d1.put(1, 1.0);

        Map<Integer, Double> d2 = map();
        d2.put(2, 2.0);

        Map<Integer, Double> d12 = map();
        d12.put(1, 1.0);
        d12.put(2, 2.0);

        Map<Integer, Double> d1m = map();
        d1m.put(1, 5.0);

        Map<Integer, Double> d12m = map();
        d12m.put(1, 5.0);
        d12m.put(2, 2.0);

        Map<Integer, Double> d1234 = map();
        d1234.put(1, 1.0);
        d1234.put(2, 2.0);
        d1234.put(3, 3.0);
        d1234.put(4, 4.0);

        Map<Integer, Double> d25 = map();
        d25.put(2, 2.0);
        d25.put(5, 5.0);

        Map<Integer, Double> d134 = map();
        d134.put(1, 1.0);
        d134.put(3, 3.0);
        d134.put(4, 4.0);

        assertEquals(empty, CifSimulatorMath.subtract(empty, empty));
        assertEquals(empty, CifSimulatorMath.subtract(empty, d1));
        assertEquals(d1, CifSimulatorMath.subtract(d1, empty));
        assertEquals(empty, CifSimulatorMath.subtract(d1, d1));

        assertEquals(empty, CifSimulatorMath.subtract(d1, d1));
        assertEquals(empty, CifSimulatorMath.subtract(d1, d12));
        assertEquals(empty, CifSimulatorMath.subtract(d1, d1m));
        assertEquals(empty, CifSimulatorMath.subtract(d1, d12m));

        assertEquals(d2, CifSimulatorMath.subtract(d12, d1));
        assertEquals(empty, CifSimulatorMath.subtract(d12, d12));
        assertEquals(d2, CifSimulatorMath.subtract(d12, d1m));
        assertEquals(empty, CifSimulatorMath.subtract(d12, d12m));

        assertEquals(empty, CifSimulatorMath.subtract(d1m, d1));
        assertEquals(empty, CifSimulatorMath.subtract(d1m, d12));
        assertEquals(empty, CifSimulatorMath.subtract(d1m, d1m));
        assertEquals(empty, CifSimulatorMath.subtract(d1m, d12m));

        assertEquals(d2, CifSimulatorMath.subtract(d12m, d1));
        assertEquals(empty, CifSimulatorMath.subtract(d12m, d12));
        assertEquals(d2, CifSimulatorMath.subtract(d12m, d1m));
        assertEquals(empty, CifSimulatorMath.subtract(d12m, d12m));

        assertEquals(d134, CifSimulatorMath.subtract(d1234, d25));

        assertEquals(0, empty.size());
        assertEquals(1, d1.size());
        assertEquals(1, d2.size());
        assertEquals(2, d12.size());
        assertEquals(1, d1m.size());
        assertEquals(2, d12m.size());
        assertEquals(4, d1234.size());
        assertEquals(2, d25.size());
        assertEquals(3, d134.size());
    }

    @Test
    public void testSubtractDictList() {
        Map<Integer, Double> empty = map();

        Map<Integer, Double> d1 = map();
        d1.put(1, 1.0);

        Map<Integer, Double> d2 = map();
        d2.put(2, 2.0);

        Map<Integer, Double> d12 = map();
        d12.put(1, 1.0);
        d12.put(2, 2.0);

        Map<Integer, Double> d1234 = map();
        d1234.put(1, 1.0);
        d1234.put(2, 2.0);
        d1234.put(3, 3.0);
        d1234.put(4, 4.0);

        Map<Integer, Double> d134 = map();
        d134.put(1, 1.0);
        d134.put(3, 3.0);
        d134.put(4, 4.0);

        assertEquals(empty, CifSimulatorMath.subtract(empty, emptyList()));
        assertEquals(empty, CifSimulatorMath.subtract(empty, list(1)));
        assertEquals(d1, CifSimulatorMath.subtract(d1, emptyList()));
        assertEquals(empty, CifSimulatorMath.subtract(d1, list(1)));

        assertEquals(empty, CifSimulatorMath.subtract(d1, list(1)));
        assertEquals(empty, CifSimulatorMath.subtract(d1, list(1, 2)));

        assertEquals(d2, CifSimulatorMath.subtract(d12, list(1)));
        assertEquals(empty, CifSimulatorMath.subtract(d12, list(1, 2)));

        assertEquals(d134, CifSimulatorMath.subtract(d1234, list(2, 5)));

        assertEquals(0, empty.size());
        assertEquals(1, d1.size());
        assertEquals(1, d2.size());
        assertEquals(2, d12.size());
        assertEquals(4, d1234.size());
        assertEquals(3, d134.size());
    }

    @Test
    public void testSubtractDictSet() {
        Map<Integer, Double> empty = map();

        Map<Integer, Double> d1 = map();
        d1.put(1, 1.0);

        Map<Integer, Double> d2 = map();
        d2.put(2, 2.0);

        Map<Integer, Double> d12 = map();
        d12.put(1, 1.0);
        d12.put(2, 2.0);

        Map<Integer, Double> d1234 = map();
        d1234.put(1, 1.0);
        d1234.put(2, 2.0);
        d1234.put(3, 3.0);
        d1234.put(4, 4.0);

        Map<Integer, Double> d134 = map();
        d134.put(1, 1.0);
        d134.put(3, 3.0);
        d134.put(4, 4.0);

        assertEquals(empty, CifSimulatorMath.subtract(empty, emptySet()));
        assertEquals(empty, CifSimulatorMath.subtract(empty, set(1)));
        assertEquals(d1, CifSimulatorMath.subtract(d1, emptySet()));
        assertEquals(empty, CifSimulatorMath.subtract(d1, set(1)));

        assertEquals(empty, CifSimulatorMath.subtract(d1, set(1)));
        assertEquals(empty, CifSimulatorMath.subtract(d1, set(1, 2)));

        assertEquals(d2, CifSimulatorMath.subtract(d12, set(1)));
        assertEquals(empty, CifSimulatorMath.subtract(d12, set(1, 2)));

        assertEquals(d134, CifSimulatorMath.subtract(d1234, set(2, 5)));

        assertEquals(0, empty.size());
        assertEquals(1, d1.size());
        assertEquals(1, d2.size());
        assertEquals(2, d12.size());
        assertEquals(4, d1234.size());
        assertEquals(3, d134.size());
    }

    @Test
    public void testTrigSimple() {
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

        assertEquals(0.479425538604, CifSimulatorMath.sin(0.5), 1e-10);
        assertEquals(0.521095305494, CifSimulatorMath.sinh(0.5), 1e-10);
        assertEquals(0.87758256189, CifSimulatorMath.cos(0.5), 1e-10);
        assertEquals(1.12762596521, CifSimulatorMath.cosh(0.5), 1e-10);
        assertEquals(0.546302489844, CifSimulatorMath.tan(0.5), 1e-10);
        assertEquals(0.46211715726, CifSimulatorMath.tanh(0.5), 1e-10);

        assertEquals(0.523598775598, CifSimulatorMath.asin(0.5), 1e-10);
        assertEquals(0.48121182506, CifSimulatorMath.asinh(0.5), 1e-10);
        assertEquals(1.0471975512, CifSimulatorMath.acos(0.5), 1e-10);
        assertEquals(1.81152627246, CifSimulatorMath.acosh(Math.PI), 1e-10);
        assertEquals(0.463647609001, CifSimulatorMath.atan(0.5), 1e-10);
        assertEquals(0.549306144334, CifSimulatorMath.atanh(0.5), 1e-10);
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
                values[0] = CifSimulatorMath.sin(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[1] = CifSimulatorMath.sinh(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[2] = CifSimulatorMath.asin(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[3] = CifSimulatorMath.asinh(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[4] = CifSimulatorMath.cos(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[5] = CifSimulatorMath.cosh(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[6] = CifSimulatorMath.acos(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[7] = CifSimulatorMath.acosh(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[8] = CifSimulatorMath.tan(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[9] = CifSimulatorMath.tanh(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[10] = CifSimulatorMath.atan(x);
            } catch (CifSimulatorException e) {
                // Ignore.
            }

            try {
                values[11] = CifSimulatorMath.atanh(x);
            } catch (CifSimulatorException e) {
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

    @Test
    public void testUnion() {
        assertEquals(emptySet(), CifSimulatorMath.union(emptySet(), emptySet()));
        assertEquals(set(1), CifSimulatorMath.union(emptySet(), set(1)));
        assertEquals(set(1), CifSimulatorMath.union(set(1), emptySet()));
        assertEquals(set(1), CifSimulatorMath.union(set(1), set(1)));

        assertEquals(set(1, 2), CifSimulatorMath.union(set(1, 2), set(1)));
        assertEquals(set(1, 2), CifSimulatorMath.union(set(1), set(1, 2)));

        assertEquals(set(1), CifSimulatorMath.union(set(1, 1), set(1)));
        assertEquals(set(1), CifSimulatorMath.union(set(1), set(1, 1)));

        assertEquals(set(1, 2, 3, 4, 5), CifSimulatorMath.union(set(1, 2, 3, 4), set(5, 4, 2)));
    }
}
