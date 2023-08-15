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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.add;
import static org.eclipse.escet.common.java.Lists.areEqualOrShifted;
import static org.eclipse.escet.common.java.Lists.cast;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.reverse;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Lists.single;
import static org.eclipse.escet.common.java.Lists.slice;
import static org.eclipse.escet.common.java.Lists.toList;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

/** Unit tests for the methods of the {@link Lists} class. */
public class ListsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testAdd() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        List<NullPointerException> l1 = list(e1);
        List<Exception> l2 = list(e2);

        // Add element to list, both same type.
        List<NullPointerException> le1 = add(l1, e1);
        assertSame(le1, l1);
        assertEquals(2, le1.size());
        assertSame(e1, le1.get(0));
        assertSame(e1, le1.get(1));

        // Add element to list, super/derived type.
        List<Exception> le2 = add(l2, e1);
        assertSame(le2, l2);
        assertEquals(2, le2.size());
        assertSame(e2, le2.get(0));
        assertSame(e1, le2.get(1));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFilter1() {
        Number i = 1;
        Number d = 1.0;
        List<Number> nums = list(i, d);
        assertEquals(2, nums.size());

        List<Integer> inums = filter(nums, Integer.class);
        assertEquals(1, inums.size());
        assertNotSame(nums, inums);
        assertSame(nums.get(0), inums.get(0));

        List<Double> dnums = filter(nums, Double.class);
        assertEquals(1, dnums.size());
        assertNotSame(nums, dnums);
        assertSame(nums.get(1), dnums.get(0));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFilter2() {
        Exception e1 = new NullPointerException();
        Exception e2 = new IllegalArgumentException();
        List<Exception> exs = list(e1, e2);
        assertEquals(2, exs.size());

        List<NullPointerException> npes;
        npes = filter(exs, NullPointerException.class);
        assertEquals(1, npes.size());
        assertNotSame(exs, npes);
        assertSame(exs.get(0), npes.get(0));

        List<IllegalArgumentException> iaes;
        iaes = filter(exs, IllegalArgumentException.class);
        assertEquals(1, iaes.size());
        assertNotSame(exs, iaes);
        assertSame(exs.get(1), iaes.get(0));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCopy() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();
        List<RuntimeException> exs = list(e1, e2);
        assertEquals(2, exs.size());

        // Copy to same type.
        List<RuntimeException> exs2 = copy(exs);
        assertEquals(2, exs2.size());
        assertNotSame(exs, exs2);
        assertSame(exs.get(0), exs2.get(0));
        assertSame(exs.get(1), exs2.get(1));

        // Copy to super type.
        List<Exception> exs3 = copy(exs);
        assertEquals(2, exs3.size());
        assertNotSame(exs, exs3);
        assertSame(exs.get(0), exs3.get(0));
        assertSame(exs.get(1), exs3.get(1));

        // Copy to super-super type.
        List<Object> exs4 = copy(exs);
        assertEquals(2, exs4.size());
        assertNotSame(exs, exs4);
        assertSame(exs.get(0), exs4.get(0));
        assertSame(exs.get(1), exs4.get(1));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCast() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();
        List<RuntimeException> exs = list(e1, e2);
        assertEquals(2, exs.size());

        // Same type.
        List<RuntimeException> exs2 = cast(exs);
        assertEquals(2, exs2.size());
        assertSame(exs, exs2);
        assertSame(exs.get(0), exs2.get(0));
        assertSame(exs.get(1), exs2.get(1));

        // Super type.
        List<Exception> exs3 = cast(exs);
        assertEquals(2, exs3.size());
        assertSame(exs, exs3);
        assertSame(exs.get(0), exs3.get(0));
        assertSame(exs.get(1), exs3.get(1));

        // Super-super type.
        List<Object> exs4 = cast(exs);
        assertEquals(2, exs4.size());
        assertSame(exs, exs4);
        assertSame(exs.get(0), exs4.get(0));
        assertSame(exs.get(1), exs4.get(1));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testConcat() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        IllegalStateException e3 = new IllegalStateException();
        List<NullPointerException> l1 = list(e1);
        List<IllegalArgumentException> l2 = list(e2);
        List<IllegalStateException> l3 = list(e3);

        // Concat 2 lists of same type.
        List<NullPointerException> ll1 = concat(l1, l1);
        assertEquals(2, ll1.size());
        assertNotSame(ll1, l1);
        assertSame(l1.get(0), ll1.get(0));
        assertSame(l1.get(0), ll1.get(1));

        // Concat 3 lists of same type.
        List<NullPointerException> lll1 = concat(l1, l1, l1);
        assertEquals(3, lll1.size());
        assertNotSame(lll1, ll1);
        assertSame(l1.get(0), lll1.get(0));
        assertSame(l1.get(0), lll1.get(1));
        assertSame(l1.get(0), lll1.get(2));

        // Concat list and element of same type.
        List<NullPointerException> ll2 = concat(l1, e1);
        assertEquals(2, ll2.size());
        assertNotSame(ll2, l1);
        assertSame(l1.get(0), ll2.get(0));
        assertSame(e1, ll2.get(1));

        // Concat list and element of same type.
        List<NullPointerException> ll3 = concat(e1, l1);
        assertEquals(2, ll3.size());
        assertNotSame(ll3, l1);
        assertSame(e1, ll2.get(0));
        assertSame(l1.get(0), ll2.get(1));

        // Copy 2 lists of different types to super type.
        List<RuntimeException> combi2 = concat(l1, l2);
        assertEquals(2, combi2.size());
        assertNotSame(combi2, l1);
        assertNotSame(combi2, l2);
        assertSame(l1.get(0), combi2.get(0));
        assertSame(l2.get(0), combi2.get(1));

        // Copy 3 lists of different types to super type.
        List<RuntimeException> combi3 = concat(l1, l2, l3);
        assertEquals(3, combi3.size());
        assertNotSame(combi3, l1);
        assertNotSame(combi3, l2);
        assertNotSame(combi3, l3);
        assertSame(l1.get(0), combi3.get(0));
        assertSame(l2.get(0), combi3.get(1));
        assertSame(l3.get(0), combi3.get(2));

        // Copy list and element of different types to super type.
        List<RuntimeException> combiLe1 = concat(l1, e2);
        assertEquals(2, combiLe1.size());
        assertNotSame(combiLe1, l1);
        assertSame(l1.get(0), combiLe1.get(0));
        assertSame(e2, combiLe1.get(1));

        // Copy list and element of different types to super type.
        List<RuntimeException> combiLe2 = concat(e1, l2);
        assertEquals(2, combiLe2.size());
        assertNotSame(combiLe2, l2);
        assertSame(e1, combiLe2.get(0));
        assertSame(l2.get(0), combiLe2.get(1));

        // All combinations.
        List<NullPointerException> nl1l1 = concat(l1, l1);
        // List<NullPointerException> nl1l2 = concat(l1, l2);
        List<NullPointerException> nl1e1 = concat(l1, e1);
        // List<NullPointerException> nl1e2 = concat(l1, e2);
        // List<NullPointerException> nl2l1 = concat(l2, l1);
        // List<NullPointerException> nl2l2 = concat(l2, l2);
        // List<NullPointerException> nl2e1 = concat(l2, e1);
        // List<NullPointerException> nl2e2 = concat(l2, e2);
        List<NullPointerException> ne1l1 = concat(e1, l1);
        // List<NullPointerException> ne1l2 = concat(e1, l2);
        // List<NullPointerException> ne2l1 = concat(e2, l1);
        // List<NullPointerException> ne2l2 = concat(e2, l2);

        assertEquals(2, nl1l1.size());
        assertEquals(2, nl1e1.size());
        assertEquals(2, ne1l1.size());

        // List<IllegalArgumentException> il1l1 = concat(l1, l1);
        // List<IllegalArgumentException> il1l2 = concat(l1, l2);
        // List<IllegalArgumentException> il1e1 = concat(l1, e1);
        // List<IllegalArgumentException> il1e2 = concat(l1, e2);
        // List<IllegalArgumentException> il2l1 = concat(l2, l1);
        List<IllegalArgumentException> il2l2 = concat(l2, l2);
        // List<IllegalArgumentException> il2e1 = concat(l2, e1);
        List<IllegalArgumentException> il2e2 = concat(l2, e2);
        // List<IllegalArgumentException> ie1l1 = concat(e1, l1);
        // List<IllegalArgumentException> ie1l2 = concat(e1, l2);
        // List<IllegalArgumentException> ie2l1 = concat(e2, l1);
        List<IllegalArgumentException> ie2l2 = concat(e2, l2);

        assertEquals(2, il2l2.size());
        assertEquals(2, il2e2.size());
        assertEquals(2, ie2l2.size());

        List<RuntimeException> rl1l1 = concat(l1, l1);
        List<RuntimeException> rl1l2 = concat(l1, l2);
        List<RuntimeException> rl1e1 = concat(l1, e1);
        List<RuntimeException> rl1e2 = concat(l1, e2);
        List<RuntimeException> rl2l1 = concat(l2, l1);
        List<RuntimeException> rl2l2 = concat(l2, l2);
        List<RuntimeException> rl2e1 = concat(l2, e1);
        List<RuntimeException> rl2e2 = concat(l2, e2);
        List<RuntimeException> re1l1 = concat(e1, l1);
        List<RuntimeException> re1l2 = concat(e1, l2);
        List<RuntimeException> re2l1 = concat(e2, l1);
        List<RuntimeException> re2l2 = concat(e2, l2);

        assertEquals(2, rl1l1.size());
        assertEquals(2, rl1l2.size());
        assertEquals(2, rl1e1.size());
        assertEquals(2, rl1e2.size());
        assertEquals(2, rl2l1.size());
        assertEquals(2, rl2l2.size());
        assertEquals(2, rl2e1.size());
        assertEquals(2, rl2e2.size());
        assertEquals(2, re1l1.size());
        assertEquals(2, re1l2.size());
        assertEquals(2, re2l1.size());
        assertEquals(2, re2l2.size());

        List<Exception> el1l1 = concat(l1, l1);
        List<Exception> el1l2 = concat(l1, l2);
        List<Exception> el1e1 = concat(l1, e1);
        List<Exception> el1e2 = concat(l1, e2);
        List<Exception> el2l1 = concat(l2, l1);
        List<Exception> el2l2 = concat(l2, l2);
        List<Exception> el2e1 = concat(l2, e1);
        List<Exception> el2e2 = concat(l2, e2);
        List<Exception> ee1l1 = concat(e1, l1);
        List<Exception> ee1l2 = concat(e1, l2);
        List<Exception> ee2l1 = concat(e2, l1);
        List<Exception> ee2l2 = concat(e2, l2);

        assertEquals(2, el1l1.size());
        assertEquals(2, el1l2.size());
        assertEquals(2, el1e1.size());
        assertEquals(2, el1e2.size());
        assertEquals(2, el2l1.size());
        assertEquals(2, el2l2.size());
        assertEquals(2, el2e1.size());
        assertEquals(2, el2e2.size());
        assertEquals(2, ee1l1.size());
        assertEquals(2, ee1l2.size());
        assertEquals(2, ee2l1.size());
        assertEquals(2, ee2l2.size());

        List<Object> ol1l1 = concat(l1, l1);
        List<Object> ol1l2 = concat(l1, l2);
        List<Object> ol1e1 = concat(l1, e1);
        List<Object> ol1e2 = concat(l1, e2);
        List<Object> ol2l1 = concat(l2, l1);
        List<Object> ol2l2 = concat(l2, l2);
        List<Object> ol2e1 = concat(l2, e1);
        List<Object> ol2e2 = concat(l2, e2);
        List<Object> oe1l1 = concat(e1, l1);
        List<Object> oe1l2 = concat(e1, l2);
        List<Object> oe2l1 = concat(e2, l1);
        List<Object> oe2l2 = concat(e2, l2);

        assertEquals(2, ol1l1.size());
        assertEquals(2, ol1l2.size());
        assertEquals(2, ol1e1.size());
        assertEquals(2, ol1e2.size());
        assertEquals(2, ol2l1.size());
        assertEquals(2, ol2l2.size());
        assertEquals(2, ol2e1.size());
        assertEquals(2, ol2e2.size());
        assertEquals(2, oe1l1.size());
        assertEquals(2, oe1l2.size());
        assertEquals(2, oe2l1.size());
        assertEquals(2, oe2l2.size());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testList() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();

        List<NullPointerException> nl0 = list();
        assertEquals(0, nl0.size());

        List<NullPointerException> nl1 = list(e1);
        // List<NullPointerException> nl2 = list(e2);
        // List<NullPointerException> nl12 = list(e1, e2);

        assertEquals(1, nl1.size());
        assertSame(e1, nl1.get(0));

        // List<IllegalArgumentException> il1 = list(e1);
        List<IllegalArgumentException> il2 = list(e2);
        // List<IllegalArgumentException> il12 = list(e1, e2);

        assertEquals(1, il2.size());
        assertSame(e2, il2.get(0));

        List<RuntimeException> rl1 = list(e1);
        List<RuntimeException> rl2 = list(e2);
        List<RuntimeException> rl12 = list(e1, e2);

        assertEquals(1, rl1.size());
        assertEquals(1, rl2.size());
        assertEquals(2, rl12.size());
        assertSame(e1, rl1.get(0));
        assertSame(e2, rl2.get(0));
        assertSame(e1, rl12.get(0));
        assertSame(e2, rl12.get(1));

        List<Exception> xl1 = list(e1);
        List<Exception> xl2 = list(e2);
        List<Exception> xl12 = list(e1, e2);

        assertEquals(1, xl1.size());
        assertEquals(1, xl2.size());
        assertEquals(2, xl12.size());
        assertSame(e1, xl1.get(0));
        assertSame(e2, xl2.get(0));
        assertSame(e1, xl12.get(0));
        assertSame(e2, xl12.get(1));

        List<Object> ol1 = list(e1);
        List<Object> ol2 = list(e2);
        List<Object> ol12 = list(e1, e2);

        assertEquals(1, ol1.size());
        assertEquals(1, ol2.size());
        assertEquals(2, ol12.size());
        assertSame(e1, ol1.get(0));
        assertSame(e2, ol2.get(0));
        assertSame(e1, ol12.get(0));
        assertSame(e2, ol12.get(1));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testListc() {
        List<Exception> xl0 = listc(0);
        List<Exception> xl1 = listc(1);
        List<Exception> xl2 = listc(2);

        assertEquals(0, xl0.size());
        assertEquals(0, xl1.size());
        assertEquals(0, xl2.size());

        List<Object> ol0 = listc(0);
        List<Object> ol1 = listc(1);
        List<Object> ol2 = listc(2);

        assertEquals(0, ol0.size());
        assertEquals(0, ol1.size());
        assertEquals(0, ol2.size());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testLast() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();

        List<RuntimeException> rl = list(e1, e2);
        List<Exception> xl = list(e1, e2);
        List<Object> ol = list(e1, e2);

        RuntimeException lastr = last(rl);
        Exception lastx = last(xl);
        Object lasto = last(ol);

        assertSame(e2, lastr);
        assertSame(e2, lastx);
        assertSame(e2, lasto);

        RuntimeException lastrr = last(rl);
        Exception lastrx = last(rl);
        Object lastro = last(rl);

        // RuntimeException lastxr = last(xl);
        Exception lastxx = last(xl);
        Object lastxo = last(xl);

        // RuntimeException lastor = last(ol);
        // Exception lastox = last(ol);
        Object lastoo = last(ol);

        assertSame(e2, lastrr);
        assertSame(e2, lastrx);
        assertSame(e2, lastro);
        assertSame(e2, lastxx);
        assertSame(e2, lastxo);
        assertSame(e2, lastoo);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testLastEmpty() {
        List<Object> l = list();
        assertThrows(IndexOutOfBoundsException.class, () -> last(l));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFirst() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();

        List<RuntimeException> rl = list(e1, e2);
        List<Exception> xl = list(e1, e2);
        List<Object> ol = list(e1, e2);

        RuntimeException firstr = first(rl);
        Exception firstx = first(xl);
        Object firsto = first(ol);

        assertSame(e1, firstr);
        assertSame(e1, firstx);
        assertSame(e1, firsto);

        RuntimeException firstrr = first(rl);
        Exception firstrx = first(rl);
        Object firstro = first(rl);

        // RuntimeException firstxr = first(xl);
        Exception firstxx = first(xl);
        Object firstxo = first(xl);

        // RuntimeException firstor = first(ol);
        // Exception firstox = first(ol);
        Object firstoo = first(ol);

        assertSame(e1, firstrr);
        assertSame(e1, firstrx);
        assertSame(e1, firstro);
        assertSame(e1, firstxx);
        assertSame(e1, firstxo);
        assertSame(e1, firstoo);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFirstEmpty() {
        List<Object> l = list();
        assertThrows(IndexOutOfBoundsException.class, () -> first(l));
    }

    /**
     * Verify that the {@link Lists#areEqualOrShifted areEqualOrShifted} check is commutative.
     *
     * @param a First argument of the {@link Lists#areEqualOrShifted areEqualOrShifted} function.
     * @param b Second argument of the {@link Lists#areEqualOrShifted areEqualOrShifted} function.
     * @return The result of the {@link Lists#areEqualOrShifted areEqualOrShifted} check.
     */
    private boolean commutativeAreEqualOrShiftedCheck(List<RuntimeException> a, List<RuntimeException> b) {
        boolean expected = areEqualOrShifted(a, b);
        assertEquals(expected, areEqualOrShifted(b, a));
        return expected;
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testAreEqualOrShifted1() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();
        RuntimeException e3 = new RuntimeException();
        List<RuntimeException> shortList = list(e1, e2);
        List<RuntimeException> exs1 = list(e1, e2, e3);
        List<RuntimeException> exs2 = list(e3, e1, e2);
        List<RuntimeException> exs3 = list(e1, e3, e2);

        assertFalse(areEqualOrShifted(exs1, shortList));
        assertTrue(areEqualOrShifted(exs1, exs1));
        assertTrue(areEqualOrShifted(exs1, copy(exs1)));
        assertTrue(commutativeAreEqualOrShiftedCheck(exs1, exs2));
        assertFalse(commutativeAreEqualOrShiftedCheck(exs1, exs3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testAreEqualOrShifted2() {
        RuntimeException p = new NullPointerException();
        RuntimeException x = new IllegalArgumentException();
        List<RuntimeException> ppp = list(p, p, p);
        List<RuntimeException> ppx = list(p, p, x);
        List<RuntimeException> pxp = list(p, x, p);
        List<RuntimeException> xpp = list(x, p, p);

        assertFalse(commutativeAreEqualOrShiftedCheck(ppp, ppx));
        assertFalse(commutativeAreEqualOrShiftedCheck(ppp, pxp));
        assertFalse(commutativeAreEqualOrShiftedCheck(ppp, xpp));

        assertTrue(commutativeAreEqualOrShiftedCheck(pxp, ppx));
        assertTrue(commutativeAreEqualOrShiftedCheck(xpp, ppx));
        assertTrue(commutativeAreEqualOrShiftedCheck(xpp, pxp));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testReverse() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();
        RuntimeException e3 = new RuntimeException();
        List<RuntimeException> exs = list(e1, e2, e3);
        assertEquals(3, exs.size());

        // Reverse to same type.
        List<RuntimeException> exs2 = reverse(exs);
        assertEquals(3, exs2.size());
        assertNotSame(exs, exs2);
        assertSame(exs.get(0), exs2.get(2));
        assertSame(exs.get(1), exs2.get(1));
        assertSame(exs.get(2), exs2.get(0));

        // Reverse to super type.
        List<Exception> exs3 = reverse(exs);
        assertEquals(3, exs3.size());
        assertNotSame(exs, exs3);
        assertSame(exs.get(0), exs3.get(2));
        assertSame(exs.get(1), exs3.get(1));
        assertSame(exs.get(2), exs3.get(0));

        // Reverse to super-super type.
        List<Object> exs4 = reverse(exs);
        assertEquals(3, exs4.size());
        assertNotSame(exs, exs4);
        assertSame(exs.get(0), exs4.get(2));
        assertSame(exs.get(1), exs4.get(1));
        assertSame(exs.get(2), exs4.get(0));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSet2List() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();
        RuntimeException e3 = new RuntimeException();
        Set<RuntimeException> exs = set(e1, e2, e3);
        assertEquals(3, exs.size());

        // Convert to same type.
        List<RuntimeException> exs2 = set2list(exs);
        assertEquals(3, exs2.size());
        assertTrue(exs2.contains(e1));
        assertTrue(exs2.contains(e2));
        assertTrue(exs2.contains(e3));

        // Convert to super type.
        List<Exception> exs3 = set2list(exs);
        assertEquals(3, exs3.size());
        assertTrue(exs3.contains(e1));
        assertTrue(exs3.contains(e2));
        assertTrue(exs3.contains(e3));

        // Convert to super-super type.
        List<Object> exs4 = set2list(exs);
        assertEquals(3, exs4.size());
        assertTrue(exs4.contains(e1));
        assertTrue(exs4.contains(e2));
        assertTrue(exs4.contains(e3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSlice() {
        final boolean DEBUG = false;

        final String[][] EXPECTED_NEG = {
                // -5 = 0, -4 = 0, -3 = 1, -2 = 2, -1 = 3
                {"", "", "", "", ""}, // -5 = 0
                {"", "", "", "", ""}, // -4 = 0
                {"a", "a", "", "", ""}, // -3 = 1
                {"ab", "ab", "b", "", ""}, // -2 = 2
                {"abc", "abc", "bc", "c", ""}, // -1 = 3
                {"", "", "", "", ""}, // 0
                {"a", "a", "", "", ""}, // 1
                {"ab", "ab", "b", "", ""}, // 2
                {"abc", "abc", "bc", "c", ""}, // 3
                {"abcd", "abcd", "bcd", "cd", "d"}, // 4
                {"abcd", "abcd", "bcd", "cd", "d"}, // 5
                {"abcd", "abcd", "bcd", "cd", "d"}, // null=4
        };

        final String[][] EXPECTED_POS = {
                // 0, 1, 2, 3, 4, 5, null=0
                {"", "", "", "", "", "", ""}, // -5 = 0
                {"", "", "", "", "", "", ""}, // -4 = 0
                {"a", "", "", "", "", "", "a"}, // -3 = 1
                {"ab", "b", "", "", "", "", "ab"}, // -2 = 2
                {"abc", "bc", "c", "", "", "", "abc"}, // -1 = 3
                {"", "", "", "", "", "", ""}, // 0
                {"a", "", "", "", "", "", "a"}, // 1
                {"ab", "b", "", "", "", "", "ab"}, // 2
                {"abc", "bc", "c", "", "", "", "abc"}, // 3
                {"abcd", "bcd", "cd", "d", "", "", "abcd"}, // 4
                {"abcd", "bcd", "cd", "d", "", "", "abcd"}, // 5
                {"abcd", "bcd", "cd", "d", "", "", "abcd"}, // null=4
        };

        List<String> input = list("a", "b", "c", "d");

        for (int b = -5; b <= 6; b++) {
            Integer beginIndex = (b == 6) ? null : b;
            for (int e = -5; e <= 6; e++) {
                Integer endIndex = (e == 6) ? null : e;

                String expected = (b < 0) ? EXPECTED_NEG[e + 5][b + 5] : EXPECTED_POS[e + 5][b];
                List<String> actualLst = slice(input, beginIndex, endIndex);
                String actual = String.join("", actualLst);

                String msg = fmt("slice(\"abcd\", %s, %s)", beginIndex, endIndex);
                if (DEBUG) {
                    System.out.format("%s = \"%s\" %s= \"%s\"\n", msg, expected, expected.equals(actual) ? "" : "!",
                            actual);
                } else {
                    assertEquals(expected, actual, msg);
                }
            }
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSliceEmpty() {
        List<Integer> input = list();

        for (int b = -5; b <= 6; b++) {
            Integer beginIndex = (b == 6) ? null : b;
            for (int e = -5; e <= 6; e++) {
                Integer endIndex = (e == 6) ? null : e;

                String expected = "";
                List<Integer> actualLst = slice(input, beginIndex, endIndex);
                String actual = actualLst.stream().map(String::valueOf).collect(Collectors.joining());

                assertEquals(expected, actual);
            }
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSingle() {
        assertEquals(false, single(list(false)));
        assertEquals(123, (int)single(list(123)));
        assertEquals("abc", single(list("abc")));
        assertEquals(list(), single(list(list())));
        assertEquals(list(456), single(list(list(456))));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSingleNotOneElement() {
        assertThrows(IllegalArgumentException.class, () -> single(list()));
        assertThrows(IllegalArgumentException.class, () -> single(list(1, 2)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testToList() {
        assertEquals(list(), list().stream().collect(toList()));
        assertEquals(list(1), list(1).stream().collect(toList()));
        assertEquals(list(1, 2), list(1, 2).stream().collect(toList()));
        assertEquals(list("a", "b"), list("a", "b").stream().collect(toList()));
        assertEquals(list(1, 3, 5), IntStream.rangeClosed(1, 5).filter(i -> i % 2 != 0).boxed().collect(toList()));
    }
}
