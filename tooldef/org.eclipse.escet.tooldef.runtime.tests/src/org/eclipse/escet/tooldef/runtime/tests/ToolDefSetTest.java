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

package org.eclipse.escet.tooldef.runtime.tests;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.escet.tooldef.runtime.ToolDefEqWrap;
import org.eclipse.escet.tooldef.runtime.ToolDefSet;
import org.junit.Test;

/** ToolDef set value tests. */
@SuppressWarnings("javadoc")
public class ToolDefSetTest {
    @Test
    public void testCreateToString() {
        ToolDefSet<Integer> set1 = new ToolDefSet<>();
        assertEquals("{}", set1.toString());

        ToolDefSet<Integer> set2 = new ToolDefSet<>(123);
        assertEquals("{}", set2.toString());

        ToolDefSet<Integer> set3 = new ToolDefSet<>(set(1, 2));
        assertEquals("{1, 2}", set3.toString());

        ToolDefSet<Integer> set4 = new ToolDefSet<>(list(1, 2, 1, 2));
        assertEquals("{1, 2}", set4.toString());

        ToolDefSet<Integer> set5 = new ToolDefSet<>(set4);
        assertEquals("{1, 2}", set5.toString());
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInitialCapacityNegative() {
        new ToolDefSet<>(-1);
    }

    @Test
    public void testWrap() {
        Set<ToolDefEqWrap<Integer>> set1;
        set1 = set(ToolDefEqWrap.wrap(1), ToolDefEqWrap.wrap(2));
        assertEquals("[1, 2]", set1.toString());

        Set<Integer> set2;
        set2 = ToolDefSet.wrap(set1);
        assertEquals("{1, 2}", set2.toString());
    }

    @Test
    public void testAdd() {
        ToolDefSet<Integer> set = new ToolDefSet<>(set(3, 4));
        assertEquals("{3, 4}", set.toString());
        assertEquals(2, set.size());

        boolean added = set.add(5);
        assertEquals("{3, 4, 5}", set.toString());
        assertEquals(3, set.size());
        assertTrue(added);

        added = set.add(5);
        assertEquals("{3, 4, 5}", set.toString());
        assertEquals(3, set.size());
        assertFalse(added);
    }

    @Test
    public void testAddAll() {
        ToolDefSet<Integer> set = new ToolDefSet<>(set(3, 4));
        assertEquals("{3, 4}", set.toString());
        assertEquals(2, set.size());

        Set<Integer> empty = set();

        boolean added = set.addAll(empty);
        assertEquals("{3, 4}", set.toString());
        assertEquals(2, set.size());
        assertFalse(added);

        added = set.addAll(list(3, 4, 5, 6));
        assertEquals("{3, 4, 5, 6}", set.toString());
        assertEquals(4, set.size());
        assertTrue(added);
    }

    @Test
    public void testClear() {
        ToolDefSet<Integer> set = new ToolDefSet<>(list(3, 4));
        assertEquals("{3, 4}", set.toString());
        assertEquals(2, set.size());
        assertFalse(set.isEmpty());

        set.clear();
        assertEquals("{}", set.toString());
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testContains() {
        ToolDefSet<String> set = new ToolDefSet<>(list("a", "b"));
        assertTrue(set.contains("a"));
        assertTrue(set.contains("b"));
        assertFalse(set.contains("c"));
        assertTrue(set.contains(ToolDefEqWrap.wrap("a")));
        assertTrue(set.contains(ToolDefEqWrap.wrap("b")));
        assertFalse(set.contains(ToolDefEqWrap.wrap("c")));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testContainsAll() {
        ToolDefSet<String> set = new ToolDefSet<>(list("a", "b"));
        assertTrue(set.containsAll(list("a")));
        assertTrue(set.containsAll(list("a", "b")));
        assertFalse(set.containsAll(list("c")));
        assertFalse(set.containsAll(list("a", "c")));
        assertFalse(set.containsAll(list("c", "a")));

        assertTrue(set.containsAll(list(ToolDefEqWrap.wrap("a"))));
        assertTrue(set.containsAll(list(ToolDefEqWrap.wrap("a"), ToolDefEqWrap.wrap("b"))));
        assertFalse(set.containsAll(list(ToolDefEqWrap.wrap("c"))));
        assertFalse(set.containsAll(list(ToolDefEqWrap.wrap("a"), ToolDefEqWrap.wrap("c"))));
        assertFalse(set.containsAll(list(ToolDefEqWrap.wrap("c"), ToolDefEqWrap.wrap("a"))));
    }

    @Test
    public void testEqualsHashCode() {
        Set<ToolDefSet<Object>> x = set();
        ToolDefSet<Object> empty1 = new ToolDefSet<>();
        ToolDefSet<Object> empty2 = new ToolDefSet<>();
        ToolDefSet<Object> single1a = new ToolDefSet<>(list((Object)1));
        ToolDefSet<Object> single1b = new ToolDefSet<>(list((Object)1.0));
        ToolDefSet<Object> single2a = new ToolDefSet<>(list((Object)2));
        ToolDefSet<Object> single2b = new ToolDefSet<>(list((Object)2));
        ToolDefSet<Object> two1a = new ToolDefSet<>(list((Object)1, 2));
        ToolDefSet<Object> two1b = new ToolDefSet<>(list((Object)1, 2));
        ToolDefSet<Object> two2a = new ToolDefSet<>(list((Object)1, 3));
        ToolDefSet<Object> two2b = new ToolDefSet<>(list((Object)1, 3));
        x.add(empty1);
        x.add(empty2);
        x.add(single1a);
        x.add(single1b);
        x.add(single2a);
        x.add(single2b);
        x.add(two1a);
        x.add(two1b);
        x.add(two2a);
        x.add(two2b);
        assertEquals(5, x.size());
        assertEquals("[{}, {1}, {2}, {1, 2}, {1, 3}]", x.toString());
    }

    @Test
    public void testEmptySize() {
        ToolDefSet<Integer> set1 = new ToolDefSet<>();
        assertEquals(0, set1.size());
        assertEquals(true, set1.isEmpty());

        ToolDefSet<Integer> set2 = new ToolDefSet<>(123);
        assertEquals(0, set2.size());
        assertEquals(true, set2.isEmpty());

        ToolDefSet<Integer> set3 = new ToolDefSet<>(set(1, 2));
        assertEquals(2, set3.size());
        assertEquals(false, set3.isEmpty());

        ToolDefSet<Integer> set4 = new ToolDefSet<>(list(1, 1, 2, 2));
        assertEquals(2, set4.size());
        assertEquals(false, set4.isEmpty());

        ToolDefSet<Integer> set5 = new ToolDefSet<>(set4);
        assertEquals(2, set5.size());
        assertEquals(false, set5.isEmpty());
    }

    @Test
    public void testIterator() {
        ToolDefSet<String> set = new ToolDefSet<>(list("a", "b"));
        assertEquals("{\"a\", \"b\"}", set.toString());

        Iterator<String> iter = set.iterator();
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
        assertTrue(iter.hasNext());
        assertEquals("b", iter.next());
        assertFalse(iter.hasNext());
        assertEquals("{\"a\", \"b\"}", set.toString());

        iter = set.iterator();
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
        iter.remove();
        assertEquals("{\"b\"}", set.toString());
        assertTrue(iter.hasNext());
        assertEquals("b", iter.next());
        iter.remove();
        assertEquals("{}", set.toString());
        assertFalse(iter.hasNext());
        assertEquals("{}", set.toString());
    }

    @Test
    public void testRemove() {
        ToolDefSet<Double> set = new ToolDefSet<>(list(3.0, 4.0, 3.0, 4.0));
        assertEquals("{3.0, 4.0}", set.toString());
        assertEquals(2, set.size());

        boolean changed = set.remove(4.0);
        assertEquals("{3.0}", set.toString());
        assertEquals(1, set.size());
        assertTrue(changed);

        changed = set.remove(4.0);
        assertEquals("{3.0}", set.toString());
        assertEquals(1, set.size());
        assertFalse(changed);
    }

    @Test
    public void testRemoveAll() {
        ToolDefSet<Integer> set = new ToolDefSet<>(list(1, 2, 3, 4, 1, 2, 3, 4));
        assertEquals("{1, 2, 3, 4}", set.toString());
        assertEquals(4, set.size());

        boolean changed = set.removeAll(list(2, 4));
        assertEquals("{1, 3}", set.toString());
        assertEquals(2, set.size());
        assertTrue(changed);

        changed = set.removeAll(list(2, 4));
        assertEquals("{1, 3}", set.toString());
        assertEquals(2, set.size());
        assertFalse(changed);
    }

    @Test
    public void testRetainAll() {
        ToolDefSet<Integer> set = new ToolDefSet<>(list(1, 2, 3, 4, 1, 2, 3, 4));
        assertEquals("{1, 2, 3, 4}", set.toString());
        assertEquals(4, set.size());

        boolean changed = set.retainAll(list(2, 4));
        assertEquals("{2, 4}", set.toString());
        assertEquals(2, set.size());
        assertTrue(changed);

        changed = set.retainAll(list(2, 4));
        assertEquals("{2, 4}", set.toString());
        assertEquals(2, set.size());
        assertFalse(changed);
    }
}
