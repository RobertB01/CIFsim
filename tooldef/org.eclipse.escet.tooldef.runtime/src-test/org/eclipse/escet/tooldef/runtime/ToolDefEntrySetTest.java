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

package org.eclipse.escet.tooldef.runtime;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

/** ToolDef set value tests. */
@SuppressWarnings("javadoc")
public class ToolDefEntrySetTest {
    @Test
    public void testCreateToString() {
        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        Set<Entry<Integer, Integer>> set = new ToolDefMap<>(x).entrySet();
        assertEquals("{(1: 2), (3: 4)}", set.toString());
    }

    @Test
    public void testEqualsHashCode() {
        Map<Object, Object> a = map();
        a.put(1, 2);

        Map<Object, Object> b = map();
        b.put(1, 3);

        Map<Object, Object> c = map();
        c.put(1, 1);
        c.put(2, 2);

        Map<Object, Object> d = map();
        d.put(1, 1);
        d.put(3, 3);

        Set<Set<Entry<Object, Object>>> x = set();
        Set<Entry<Object, Object>> empty1 = new ToolDefMap<>().entrySet();
        Set<Entry<Object, Object>> empty2 = new ToolDefMap<>().entrySet();
        Set<Entry<Object, Object>> single1a = new ToolDefMap<>(a).entrySet();
        Set<Entry<Object, Object>> single1b = new ToolDefMap<>(a).entrySet();
        Set<Entry<Object, Object>> single2a = new ToolDefMap<>(b).entrySet();
        Set<Entry<Object, Object>> single2b = new ToolDefMap<>(b).entrySet();
        Set<Entry<Object, Object>> two1a = new ToolDefMap<>(c).entrySet();
        Set<Entry<Object, Object>> two1b = new ToolDefMap<>(c).entrySet();
        Set<Entry<Object, Object>> two2a = new ToolDefMap<>(d).entrySet();
        Set<Entry<Object, Object>> two2b = new ToolDefMap<>(d).entrySet();
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
        assertEquals("[{}, {(1: 2)}, {(1: 3)}, {(1: 1), (2: 2)}, {(1: 1), (3: 3)}]", x.toString());
    }

    @Test
    public void testEmptySize() {
        Set<Entry<Object, Object>> set1 = new ToolDefMap<>().entrySet();
        assertEquals(0, set1.size());
        assertEquals(true, set1.isEmpty());

        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        Set<Entry<Integer, Integer>> set2 = new ToolDefMap<>(x).entrySet();
        assertEquals(2, set2.size());
        assertEquals(false, set2.isEmpty());
    }

    @Test
    public void testIterator() {
        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        Set<Entry<Integer, Integer>> set = new ToolDefMap<>(x).entrySet();
        assertEquals("{(1: 2), (3: 4)}", set.toString());

        Iterator<Entry<Integer, Integer>> iter = set.iterator();
        assertTrue(iter.hasNext());
        assertEquals("1: 2", iter.next().toString());
        assertTrue(iter.hasNext());
        assertEquals("3: 4", iter.next().toString());
        assertFalse(iter.hasNext());
        assertEquals("{(1: 2), (3: 4)}", set.toString());
    }
}
