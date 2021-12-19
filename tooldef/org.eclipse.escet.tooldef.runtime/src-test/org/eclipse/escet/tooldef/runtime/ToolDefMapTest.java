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

package org.eclipse.escet.tooldef.runtime;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

/** ToolDef map value tests. */
@SuppressWarnings("javadoc")
public class ToolDefMapTest {
    @Test
    public void testCreateToString() {
        ToolDefMap<Integer, Integer> map1 = new ToolDefMap<>();
        assertEquals("{}", map1.toString());

        ToolDefMap<Integer, Integer> map2 = new ToolDefMap<>(123);
        assertEquals("{}", map2.toString());

        Map<Integer, Integer> map3a = map();
        map3a.put(1, 2);
        map3a.put(3, 4);

        ToolDefMap<Integer, Integer> map3 = new ToolDefMap<>(map3a);
        assertEquals("{1: 2, 3: 4}", map3.toString());

        ToolDefMap<Integer, Integer> map4 = new ToolDefMap<>(map3);
        assertEquals("{1: 2, 3: 4}", map4.toString());
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInitialCapacityNegative() {
        new ToolDefMap<>(-1);
    }

    @Test
    public void testClear() {
        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Integer, Integer> map = new ToolDefMap<>(x);
        assertEquals("{1: 2, 3: 4}", map.toString());
        assertEquals(2, map.size());
        assertFalse(map.isEmpty());

        map.clear();
        assertEquals("{}", map.toString());
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testContainsKey() {
        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Integer, Integer> map = new ToolDefMap<>(x);
        assertTrue(map.containsKey(1));
        assertTrue(map.containsKey(3));
        assertFalse(map.containsKey(2));
        assertTrue(map.containsKey(ToolDefEqWrap.wrap(1)));
        assertTrue(map.containsKey(ToolDefEqWrap.wrap(3)));
        assertFalse(map.containsKey(ToolDefEqWrap.wrap(2)));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testContainsValue() {
        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Integer, Integer> map = new ToolDefMap<>(x);
        assertTrue(map.containsValue(2));
        assertTrue(map.containsValue(4));
        assertFalse(map.containsValue(3));
        assertTrue(map.containsValue(ToolDefEqWrap.wrap(2)));
        assertTrue(map.containsValue(ToolDefEqWrap.wrap(4)));
        assertFalse(map.containsValue(ToolDefEqWrap.wrap(3)));
    }

    @Test
    public void testEntrySet() {
        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Integer, Integer> map = new ToolDefMap<>(x);
        Set<Entry<Integer, Integer>> entries = map.entrySet();
        assertEquals(2, entries.size());
        assertEquals("{(1: 2), (3: 4)}", entries.toString());
    }

    @Test
    public void testEqualsHashCode() {
        Map<Object, Object> _empty1 = map();
        Map<Object, Object> _empty2 = map();
        Map<Object, Object> _single1a = map();
        Map<Object, Object> _single1b = map();
        Map<Object, Object> _single2a = map();
        Map<Object, Object> _single2b = map();
        Map<Object, Object> _two1a = map();
        Map<Object, Object> _two1b = map();
        Map<Object, Object> _two2a = map();
        Map<Object, Object> _two2b = map();
        _single1a.put(1, 1);
        _single1b.put(1.0, 1.0);
        _single2a.put(2, 2);
        _single2b.put(2, 2.0);
        _two1a.put(1, 1);
        _two1b.put(1, 1);
        _two2a.put(1, 1);
        _two2b.put(1, 1);
        _two1a.put(2, 2);
        _two1b.put(2, 2);
        _two2a.put(3, 3);
        _two2b.put(3, 3);

        Set<ToolDefMap<Object, Object>> x = set();
        ToolDefMap<Object, Object> empty1 = new ToolDefMap<>(_empty1);
        ToolDefMap<Object, Object> empty2 = new ToolDefMap<>(_empty2);
        ToolDefMap<Object, Object> single1a = new ToolDefMap<>(_single1a);
        ToolDefMap<Object, Object> single1b = new ToolDefMap<>(_single1b);
        ToolDefMap<Object, Object> single2a = new ToolDefMap<>(_single2a);
        ToolDefMap<Object, Object> single2b = new ToolDefMap<>(_single2b);
        ToolDefMap<Object, Object> two1a = new ToolDefMap<>(_two1a);
        ToolDefMap<Object, Object> two1b = new ToolDefMap<>(_two1b);
        ToolDefMap<Object, Object> two2a = new ToolDefMap<>(_two2a);
        ToolDefMap<Object, Object> two2b = new ToolDefMap<>(_two2b);
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
        assertEquals("[{}, {1: 1}, {2: 2}, {1: 1, 2: 2}, {1: 1, 3: 3}]", x.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testGet() {
        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Integer, Integer> map = new ToolDefMap<>(x);
        assertEquals(2, (int)map.get(1));
        assertEquals(4, (int)map.get(3));
        assertEquals(null, map.get(2));
        assertEquals(2, (int)map.get(ToolDefEqWrap.wrap(1)));
        assertEquals(4, (int)map.get(ToolDefEqWrap.wrap(3)));
        assertEquals(null, map.get(ToolDefEqWrap.wrap(2)));
    }

    @Test
    public void testEmptySize() {
        ToolDefMap<Integer, Integer> map1 = new ToolDefMap<>();
        assertEquals(0, map1.size());
        assertEquals(true, map1.isEmpty());

        ToolDefMap<Integer, Integer> map2 = new ToolDefMap<>(123);
        assertEquals(0, map2.size());
        assertEquals(true, map2.isEmpty());

        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Integer, Integer> map3 = new ToolDefMap<>(x);
        assertEquals(2, map3.size());
        assertEquals(false, map3.isEmpty());

        ToolDefMap<Integer, Integer> map4 = new ToolDefMap<>(map3);
        assertEquals(2, map4.size());
        assertEquals(false, map4.isEmpty());
    }

    @Test
    public void testKeySet() {
        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Integer, Integer> map = new ToolDefMap<>(x);
        Set<Integer> keySet = map.keySet();
        assertEquals("{1, 3}", keySet.toString());
    }

    @Test
    public void testPut() {
        Map<Object, Object> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Object, Object> map = new ToolDefMap<>(x);
        assertEquals("{1: 2, 3: 4}", map.toString());

        Object prev = map.put(1, 5);
        assertEquals("{1: 5, 3: 4}", map.toString());
        assertEquals(2, prev);

        prev = map.put(1.0, 6);
        assertEquals("{1: 6, 3: 4}", map.toString());
        assertEquals(5, prev);

        prev = map.put(1, 7.0);
        assertEquals("{1: 7.0, 3: 4}", map.toString());
        assertEquals(6, prev);

        prev = map.put(1.0, 8.0);
        assertEquals("{1: 8.0, 3: 4}", map.toString());
        assertEquals(7.0, prev);

        prev = map.put(12, 34);
        assertEquals("{1: 8.0, 3: 4, 12: 34}", map.toString());
        assertEquals(null, prev);
    }

    @Test
    public void testPutAll() {
        Map<Object, Object> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Object, Object> map1 = new ToolDefMap<>(x);
        assertEquals("{1: 2, 3: 4}", map1.toString());

        Map<Object, Object> y = map();
        y.put(1, 6);
        y.put(7, 8);

        ToolDefMap<Object, Object> map2 = new ToolDefMap<>(y);
        assertEquals("{1: 6, 7: 8}", map2.toString());

        map1.putAll(map2);
        assertEquals("{1: 6, 3: 4, 7: 8}", map1.toString());
        assertEquals("{1: 6, 7: 8}", map2.toString());
    }

    @Test
    public void testRemove() {
        Map<Object, Object> x = map();
        x.put(1, 2);
        x.put(3, 4);

        ToolDefMap<Object, Object> map = new ToolDefMap<>(x);
        assertEquals("{1: 2, 3: 4}", map.toString());

        Object prev = map.remove(1);
        assertEquals("{3: 4}", map.toString());
        assertEquals(2, prev);

        prev = map.remove(9);
        assertEquals("{3: 4}", map.toString());
        assertEquals(null, prev);
    }

    @Test
    public void testValues() {
        Map<Integer, Integer> x = map();
        x.put(1, 2);
        x.put(3, 4);
        x.put(5, 2);

        ToolDefMap<Integer, Integer> map = new ToolDefMap<>(x);
        Collection<Integer> values = map.values();
        assertEquals("[2, 4, 2]", values.toString());
    }
}
