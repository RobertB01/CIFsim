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

import static org.eclipse.escet.common.java.Lists.cast;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.add;
import static org.eclipse.escet.common.java.Sets.cast;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.difference;
import static org.eclipse.escet.common.java.Sets.filter;
import static org.eclipse.escet.common.java.Sets.intersection;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;
import static org.eclipse.escet.common.java.Sets.list2set;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Sets.union;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/** Unit tests for the methods of the {@link Sets} class. */
public class SetsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testAdd() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        Set<NullPointerException> s1 = set(e1);
        Set<Exception> s2 = set(e2);

        // Add element to set, both same type.
        Set<NullPointerException> se1 = add(s1, e1);
        assertSame(se1, s1);
        assertEquals(1, se1.size());
        assertSame(e1, se1.iterator().next());

        // Add element to set, super/derived type.
        Set<Exception> se2 = add(s2, e1);
        assertSame(se2, s2);
        assertEquals(2, se2.size());
        Iterator<Exception> iter = se2.iterator();
        Exception i1 = iter.next();
        Exception i2 = iter.next();
        assertTrue((i1 == e1 && i2 == e2) || (i1 == e2 && i2 == e1));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFilter1() {
        Number i = 1;
        Number d = 1.0;
        Set<Number> nums = set(i, d);
        assertEquals(2, nums.size());

        Set<Integer> inums = filter(nums, Integer.class);
        assertEquals(1, inums.size());
        assertNotSame(nums, inums);
        assertSame(i, inums.iterator().next());

        Set<Double> dnums = filter(nums, Double.class);
        assertEquals(1, dnums.size());
        assertNotSame(nums, dnums);
        assertSame(d, dnums.iterator().next());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFilter2() {
        Exception e1 = new NullPointerException();
        Exception e2 = new IllegalArgumentException();
        Set<Exception> exs = set(e1, e2);
        assertEquals(2, exs.size());

        Set<NullPointerException> npes;
        npes = filter(exs, NullPointerException.class);
        assertEquals(1, npes.size());
        assertNotSame(exs, npes);
        assertSame(e1, npes.iterator().next());

        Set<IllegalArgumentException> iaes;
        iaes = filter(exs, IllegalArgumentException.class);
        assertEquals(1, iaes.size());
        assertNotSame(exs, iaes);
        assertSame(e2, iaes.iterator().next());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testUnion2() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        Set<NullPointerException> s1 = set(e1);
        Set<IllegalArgumentException> s2 = set(e2);

        Set<NullPointerException> ns11 = union(s1, s1);
        // Set<NullPointerException> ns12 = union(s1, s2);
        // Set<NullPointerException> ns21 = union(s2, s1);
        // Set<NullPointerException> ns22 = union(s2, s2);

        assertEquals(1, ns11.size());

        // Set<IllegalArgumentException> is11 = union(s1, s1);
        // Set<IllegalArgumentException> is12 = union(s1, s2);
        // Set<IllegalArgumentException> is21 = union(s2, s1);
        Set<IllegalArgumentException> is22 = union(s2, s2);

        assertEquals(1, is22.size());

        Set<RuntimeException> rs11 = union(s1, s1);
        Set<RuntimeException> rs12 = union(s1, s2);
        Set<RuntimeException> rs21 = union(s2, s1);
        Set<RuntimeException> rs22 = union(s2, s2);

        assertEquals(1, rs11.size());
        assertEquals(2, rs12.size());
        assertEquals(2, rs21.size());
        assertEquals(1, rs22.size());

        Set<Exception> xs11 = union(s1, s1);
        Set<Exception> xs12 = union(s1, s2);
        Set<Exception> xs21 = union(s2, s1);
        Set<Exception> xs22 = union(s2, s2);

        assertEquals(1, xs11.size());
        assertEquals(2, xs12.size());
        assertEquals(2, xs21.size());
        assertEquals(1, xs22.size());

        Set<Object> os11 = union(s1, s1);
        Set<Object> os12 = union(s1, s2);
        Set<Object> os21 = union(s2, s1);
        Set<Object> os22 = union(s2, s2);

        assertEquals(1, os11.size());
        assertEquals(2, os12.size());
        assertEquals(2, os21.size());
        assertEquals(1, os22.size());

        Set<RuntimeException> s121 = union(s1, s2, s1);
        assertEquals(2, s121.size());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testUnion3() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        Set<NullPointerException> s1 = set(e1);
        Set<IllegalArgumentException> s2 = set(e2);

        Set<NullPointerException> ns11 = union(s1, s1, s1);
        // Set<NullPointerException> ns12 = union(s1, s2, s2);
        // Set<NullPointerException> ns21 = union(s2, s1, s1);
        // Set<NullPointerException> ns22 = union(s2, s2, s2);

        assertEquals(1, ns11.size());

        // Set<IllegalArgumentException> is11 = union(s1, s1, s1);
        // Set<IllegalArgumentException> is12 = union(s1, s2, s2);
        // Set<IllegalArgumentException> is21 = union(s2, s1, s1);
        Set<IllegalArgumentException> is22 = union(s2, s2, s2);

        assertEquals(1, is22.size());

        Set<RuntimeException> rs11 = union(s1, s1, s1);
        Set<RuntimeException> rs12 = union(s1, s2, s2);
        Set<RuntimeException> rs21 = union(s2, s1, s1);
        Set<RuntimeException> rs22 = union(s2, s2, s2);

        assertEquals(1, rs11.size());
        assertEquals(2, rs12.size());
        assertEquals(2, rs21.size());
        assertEquals(1, rs22.size());

        Set<Exception> xs11 = union(s1, s1, s1);
        Set<Exception> xs12 = union(s1, s2, s2);
        Set<Exception> xs21 = union(s2, s1, s1);
        Set<Exception> xs22 = union(s2, s2, s2);

        assertEquals(1, xs11.size());
        assertEquals(2, xs12.size());
        assertEquals(2, xs21.size());
        assertEquals(1, xs22.size());

        Set<Object> os11 = union(s1, s1, s1);
        Set<Object> os12 = union(s1, s2, s2);
        Set<Object> os21 = union(s2, s1, s1);
        Set<Object> os22 = union(s2, s2, s2);

        assertEquals(1, os11.size());
        assertEquals(2, os12.size());
        assertEquals(2, os21.size());
        assertEquals(1, os22.size());

        Set<RuntimeException> s121 = union(s1, s2, s1);
        assertEquals(2, s121.size());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntersection2() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        Set<NullPointerException> s1 = set(e1);
        Set<IllegalArgumentException> s2 = set(e2);

        Set<NullPointerException> ns11 = intersection(s1, s1);
        // Set<NullPointerException> ns12 = intersection(s1, s2);
        // Set<NullPointerException> ns21 = intersection(s2, s1);
        // Set<NullPointerException> ns22 = intersection(s2, s2);

        assertEquals(1, ns11.size());

        // Set<IllegalArgumentException> is11 = intersection(s1, s1);
        // Set<IllegalArgumentException> is12 = intersection(s1, s2);
        // Set<IllegalArgumentException> is21 = intersection(s2, s1);
        Set<IllegalArgumentException> is22 = intersection(s2, s2);

        assertEquals(1, is22.size());

        Set<RuntimeException> rs11 = intersection(s1, s1);
        Set<RuntimeException> rs12 = intersection(s1, s2);
        Set<RuntimeException> rs21 = intersection(s2, s1);
        Set<RuntimeException> rs22 = intersection(s2, s2);

        assertEquals(1, rs11.size());
        assertEquals(0, rs12.size());
        assertEquals(0, rs21.size());
        assertEquals(1, rs22.size());

        Set<Exception> xs11 = intersection(s1, s1);
        Set<Exception> xs12 = intersection(s1, s2);
        Set<Exception> xs21 = intersection(s2, s1);
        Set<Exception> xs22 = intersection(s2, s2);

        assertEquals(1, xs11.size());
        assertEquals(0, xs12.size());
        assertEquals(0, xs21.size());
        assertEquals(1, xs22.size());

        Set<Object> os11 = intersection(s1, s1);
        Set<Object> os12 = intersection(s1, s2);
        Set<Object> os21 = intersection(s2, s1);
        Set<Object> os22 = intersection(s2, s2);

        assertEquals(1, os11.size());
        assertEquals(0, os12.size());
        assertEquals(0, os21.size());
        assertEquals(1, os22.size());

        Set<RuntimeException> s121 = intersection(s1, s2, s1);
        assertEquals(0, s121.size());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntersection3() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        Set<NullPointerException> s1 = set(e1);
        Set<IllegalArgumentException> s2 = set(e2);

        Set<NullPointerException> ns11 = intersection(s1, s1, s1);
        // Set<NullPointerException> ns12 = intersection(s1, s2, s2);
        // Set<NullPointerException> ns21 = intersection(s2, s1, s1);
        // Set<NullPointerException> ns22 = intersection(s2, s2, s2);

        assertEquals(1, ns11.size());

        // Set<IllegalArgumentException> is11 = intersection(s1, s1, s1);
        // Set<IllegalArgumentException> is12 = intersection(s1, s2, s2);
        // Set<IllegalArgumentException> is21 = intersection(s2, s1, s1);
        Set<IllegalArgumentException> is22 = intersection(s2, s2, s2);

        assertEquals(1, is22.size());

        Set<RuntimeException> rs11 = intersection(s1, s1, s1);
        Set<RuntimeException> rs12 = intersection(s1, s2, s2);
        Set<RuntimeException> rs21 = intersection(s2, s1, s1);
        Set<RuntimeException> rs22 = intersection(s2, s2, s2);

        assertEquals(1, rs11.size());
        assertEquals(0, rs12.size());
        assertEquals(0, rs21.size());
        assertEquals(1, rs22.size());

        Set<Exception> xs11 = intersection(s1, s1, s1);
        Set<Exception> xs12 = intersection(s1, s2, s2);
        Set<Exception> xs21 = intersection(s2, s1, s1);
        Set<Exception> xs22 = intersection(s2, s2, s2);

        assertEquals(1, xs11.size());
        assertEquals(0, xs12.size());
        assertEquals(0, xs21.size());
        assertEquals(1, xs22.size());

        Set<Object> os11 = intersection(s1, s1, s1);
        Set<Object> os12 = intersection(s1, s2, s2);
        Set<Object> os21 = intersection(s2, s1, s1);
        Set<Object> os22 = intersection(s2, s2, s2);

        assertEquals(1, os11.size());
        assertEquals(0, os12.size());
        assertEquals(0, os21.size());
        assertEquals(1, os22.size());

        Set<RuntimeException> s121 = intersection(s1, s2, s1);
        assertEquals(0, s121.size());
    }

    /** isEmptyIntersection tests. */
    @Test
    public void testEmptyIntersect() {
        Set<Integer> empty = set();
        Integer i1 = 1;
        Integer i2 = 2;
        Set<Integer> s1 = set(i1);
        Set<Integer> s2 = set(i2);
        Set<Integer> s3 = set(i1, i2);

        assertEquals(0, empty.size());
        assertEquals(1, s1.size());
        assertEquals(1, s2.size());
        assertEquals(2, s3.size());

        assertEquals(true, isEmptyIntersection(empty, s1));
        assertEquals(true, isEmptyIntersection(s2, empty));
        assertEquals(true, isEmptyIntersection(s1, s2));
        assertEquals(false, isEmptyIntersection(s1, s3));
        assertEquals(false, isEmptyIntersection(s3, s2));
        assertEquals(false, isEmptyIntersection(s3, s3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDifference2() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        Set<NullPointerException> s1 = set(e1);
        Set<IllegalArgumentException> s2 = set(e2);

        Set<NullPointerException> ns11 = difference(s1, s1);
        // Set<NullPointerException> ns12 = difference(s1, s2);
        // Set<NullPointerException> ns21 = difference(s2, s1);
        // Set<NullPointerException> ns22 = difference(s2, s2);

        assertEquals(0, ns11.size());

        // Set<IllegalArgumentException> is11 = difference(s1, s1);
        // Set<IllegalArgumentException> is12 = difference(s1, s2);
        // Set<IllegalArgumentException> is21 = difference(s2, s1);
        Set<IllegalArgumentException> is22 = difference(s2, s2);

        assertEquals(0, is22.size());

        Set<RuntimeException> rs11 = difference(s1, s1);
        Set<RuntimeException> rs12 = difference(s1, s2);
        Set<RuntimeException> rs21 = difference(s2, s1);
        Set<RuntimeException> rs22 = difference(s2, s2);

        assertEquals(0, rs11.size());
        assertEquals(1, rs12.size());
        assertEquals(1, rs21.size());
        assertEquals(0, rs22.size());

        Set<Exception> xs11 = difference(s1, s1);
        Set<Exception> xs12 = difference(s1, s2);
        Set<Exception> xs21 = difference(s2, s1);
        Set<Exception> xs22 = difference(s2, s2);

        assertEquals(0, xs11.size());
        assertEquals(1, xs12.size());
        assertEquals(1, xs21.size());
        assertEquals(0, xs22.size());

        Set<Object> os11 = difference(s1, s1);
        Set<Object> os12 = difference(s1, s2);
        Set<Object> os21 = difference(s2, s1);
        Set<Object> os22 = difference(s2, s2);

        assertEquals(0, os11.size());
        assertEquals(1, os12.size());
        assertEquals(1, os21.size());
        assertEquals(0, os22.size());

        Set<RuntimeException> s121 = difference(s1, s2, s1);
        assertEquals(0, s121.size());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDifference3() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        Set<NullPointerException> s1 = set(e1);
        Set<IllegalArgumentException> s2 = set(e2);

        Set<NullPointerException> ns11 = difference(s1, s1, s1);
        // Set<NullPointerException> ns12 = difference(s1, s2, s2);
        // Set<NullPointerException> ns21 = difference(s2, s1, s1);
        // Set<NullPointerException> ns22 = difference(s2, s2, s2);

        assertEquals(0, ns11.size());

        // Set<IllegalArgumentException> is11 = difference(s1, s1, s1);
        // Set<IllegalArgumentException> is12 = difference(s1, s2, s2);
        // Set<IllegalArgumentException> is21 = difference(s2, s1, s1);
        Set<IllegalArgumentException> is22 = difference(s2, s2, s2);

        assertEquals(0, is22.size());

        Set<RuntimeException> rs11 = difference(s1, s1, s1);
        Set<RuntimeException> rs12 = difference(s1, s2, s2);
        Set<RuntimeException> rs21 = difference(s2, s1, s1);
        Set<RuntimeException> rs22 = difference(s2, s2, s2);

        assertEquals(0, rs11.size());
        assertEquals(1, rs12.size());
        assertEquals(1, rs21.size());
        assertEquals(0, rs22.size());

        Set<Exception> xs11 = difference(s1, s1, s1);
        Set<Exception> xs12 = difference(s1, s2, s2);
        Set<Exception> xs21 = difference(s2, s1, s1);
        Set<Exception> xs22 = difference(s2, s2, s2);

        assertEquals(0, xs11.size());
        assertEquals(1, xs12.size());
        assertEquals(1, xs21.size());
        assertEquals(0, xs22.size());

        Set<Object> os11 = difference(s1, s1, s1);
        Set<Object> os12 = difference(s1, s2, s2);
        Set<Object> os21 = difference(s2, s1, s1);
        Set<Object> os22 = difference(s2, s2, s2);

        assertEquals(0, os11.size());
        assertEquals(1, os12.size());
        assertEquals(1, os21.size());
        assertEquals(0, os22.size());

        Set<RuntimeException> s121 = difference(s1, s2, s1);
        assertEquals(0, s121.size());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDifferenceVariousTypes() {
        assertEquals(set(1, 2, 3), difference(set(1, 2, 3), set()));
        assertEquals(set(2, 3), difference(set(1, 2, 3), set(1)));
        assertEquals(set(1), difference(set(1, 1, 2, 2, 3, 3), set(2, 2, 3)));

        assertEquals(set(1, 2, 3), difference(set(1, 2, 3), list()));
        assertEquals(set(2, 3), difference(set(1, 2, 3), list(1)));
        assertEquals(set(1), difference(set(1, 1, 2, 2, 3, 3), list(2, 2, 3)));

        assertEquals(set(1, 2, 3), difference(list(1, 2, 3), set()));
        assertEquals(set(2, 3), difference(list(1, 2, 3), set(1)));
        assertEquals(set(1), difference(list(1, 1, 2, 2, 3, 3), set(2, 2, 3)));

        assertEquals(set(1, 2, 3), difference(list(1, 2, 3), list()));
        assertEquals(set(2, 3), difference(list(1, 2, 3), list(1)));
        assertEquals(set(1), difference(list(1, 1, 2, 2, 3, 3), list(2, 2, 3)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCopy() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();
        Set<RuntimeException> exs = set(e1, e2);
        assertEquals(2, exs.size());

        // Copy to same type.
        Set<RuntimeException> exs2 = copy(exs);
        assertEquals(2, exs2.size());
        assertNotSame(exs, exs2);
        assertTrue(exs2.contains(e1));
        assertTrue(exs2.contains(e2));

        // Copy to super type.
        Set<Exception> exs3 = copy(exs);
        assertEquals(2, exs3.size());
        assertNotSame(exs, exs3);
        assertTrue(exs3.contains(e1));
        assertTrue(exs3.contains(e2));

        // Copy to super-super type.
        Set<Object> exs4 = copy(exs);
        assertEquals(2, exs4.size());
        assertNotSame(exs, exs4);
        assertTrue(exs4.contains(e1));
        assertTrue(exs4.contains(e2));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCast() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();
        Set<RuntimeException> exs = set(e1, e2);
        assertEquals(2, exs.size());

        // Same type.
        Set<RuntimeException> exs2 = cast(exs);
        assertEquals(2, exs2.size());
        assertSame(exs, exs2);
        assertTrue(exs2.contains(e1));
        assertTrue(exs2.contains(e2));

        // Super type.
        Set<Exception> exs3 = cast(exs);
        assertEquals(2, exs3.size());
        assertSame(exs, exs3);
        assertTrue(exs3.contains(e1));
        assertTrue(exs3.contains(e2));

        // Super-super type.
        Set<Object> exs4 = cast(exs);
        assertEquals(2, exs4.size());
        assertSame(exs, exs4);
        assertTrue(exs4.contains(e1));
        assertTrue(exs4.contains(e2));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testToString() {
        Number n1 = 1;
        Number n2 = 2.0;

        Set<Number> nums12 = set(n1, n2);
        Set<Number> nums21 = set(n2, n1);

        assertEquals(2, nums12.size());
        assertEquals(2, nums21.size());

        assertEquals("{1, 2.0}", Sets.toString(nums12));
        assertEquals("{1, 2.0}", Sets.toString(nums21));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSet() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();

        Set<NullPointerException> ns0 = set();
        assertEquals(0, ns0.size());

        Set<NullPointerException> ns1 = set(e1);
        // Set<NullPointerException> ns2 = set(e2);
        // Set<NullPointerException> ns12 = set(e1, e2);

        assertEquals(1, ns1.size());
        assertSame(e1, ns1.iterator().next());

        // Set<IllegalArgumentException> is1 = set(e1);
        Set<IllegalArgumentException> is2 = set(e2);
        // Set<IllegalArgumentException> is12 = set(e1, e2);

        assertEquals(1, is2.size());
        assertSame(e2, is2.iterator().next());

        Set<RuntimeException> rs1 = set(e1);
        Set<RuntimeException> rs2 = set(e2);
        Set<RuntimeException> rs12 = set(e1, e2);

        assertEquals(1, rs1.size());
        assertEquals(1, rs2.size());
        assertEquals(2, rs12.size());
        assertTrue(rs1.contains(e1));
        assertTrue(rs2.contains(e2));
        assertTrue(rs12.contains(e1));
        assertTrue(rs12.contains(e2));

        Set<Exception> xs1 = set(e1);
        Set<Exception> xs2 = set(e2);
        Set<Exception> xs12 = set(e1, e2);

        assertEquals(1, xs1.size());
        assertEquals(1, xs2.size());
        assertEquals(2, xs12.size());
        assertTrue(xs1.contains(e1));
        assertTrue(xs2.contains(e2));
        assertTrue(xs12.contains(e1));
        assertTrue(xs12.contains(e2));

        Set<Object> os1 = set(e1);
        Set<Object> os2 = set(e2);
        Set<Object> os12 = set(e1, e2);

        assertEquals(1, os1.size());
        assertEquals(1, os2.size());
        assertEquals(2, os12.size());
        assertTrue(os1.contains(e1));
        assertTrue(os2.contains(e2));
        assertTrue(os12.contains(e1));
        assertTrue(os12.contains(e2));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSetc() {
        Set<Exception> xl0 = setc(0);
        Set<Exception> xl1 = setc(1);
        Set<Exception> xl2 = setc(2);

        assertEquals(0, xl0.size());
        assertEquals(0, xl1.size());
        assertEquals(0, xl2.size());

        Set<Object> ol0 = setc(0);
        Set<Object> ol1 = setc(1);
        Set<Object> ol2 = setc(2);

        assertEquals(0, ol0.size());
        assertEquals(0, ol1.size());
        assertEquals(0, ol2.size());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testList2Set() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();
        RuntimeException e3 = new RuntimeException();
        List<RuntimeException> exs = list(e1, e2, e2);
        assertEquals(3, exs.size());

        // Convert to same type.
        Set<RuntimeException> exs2 = list2set(exs);
        assertEquals(2, exs2.size());
        assertTrue(exs2.contains(e1));
        assertTrue(exs2.contains(e2));
        assertTrue(!exs2.contains(e3));

        // Convert to super type.
        Set<Exception> exs3 = list2set(exs);
        assertEquals(2, exs3.size());
        assertTrue(exs3.contains(e1));
        assertTrue(exs3.contains(e2));
        assertTrue(!exs3.contains(e3));

        // Convert to super-super type.
        Set<Object> exs4 = list2set(exs);
        assertEquals(2, exs4.size());
        assertTrue(exs4.contains(e1));
        assertTrue(exs4.contains(e2));
        assertTrue(!exs4.contains(e3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSortedGeneric() {
        // Natural order.
        Set<Integer> set1 = set(1, 1, 5, 4, 3, 4, 2, 2, 6, 3);
        List<Integer> list1 = sortedgeneric(set1);
        assertEquals(6, list1.size());
        assertEquals("[1, 2, 3, 4, 5, 6]", list1.toString());

        // Custom order.
        Comparator<Integer> cmp = new Comparator<>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                // Reverse order.
                return -i1.compareTo(i2);
            }
        };
        List<Integer> list2 = sortedgeneric(set1, cmp);
        assertEquals(6, list2.size());
        assertEquals("[6, 5, 4, 3, 2, 1]", list2.toString());
    }

    /** Test enumeration used by {@link #testSortedGenericConversion}. */
    private static enum TestEnum {
        /** First. */
        FIRST,

        /** Second. */
        SECOND,

        /** Third. */
        THIRD,

        /** Fourth. */
        FOURTH;
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSortedGenericConversion() {
        // Natural order.
        Set<TestEnum> set1 = set(TestEnum.FIRST, TestEnum.THIRD, TestEnum.FOURTH, TestEnum.SECOND);
        List<Enum<?>> list1 = cast(sortedgeneric(set1));
        assertEquals(4, list1.size());
        assertSame(TestEnum.FIRST, list1.get(0));
        assertSame(TestEnum.SECOND, list1.get(1));
        assertSame(TestEnum.THIRD, list1.get(2));
        assertSame(TestEnum.FOURTH, list1.get(3));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSortedStrings() {
        // Generic/natural string sort.
        Set<String> set1 = set("a", "c", "B", "A", "b", "e", "D", "D", "d");
        List<String> list1 = sortedgeneric(set1);
        assertEquals(8, list1.size());
        assertEquals("[A, B, D, a, b, c, d, e]", list1.toString());

        // Smart string sort.
        List<String> list2 = sortedstrings(set1);
        assertEquals(8, list2.size());
        assertEquals("[A, a, B, b, c, D, d, e]", list2.toString());
    }
}
