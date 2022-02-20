//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Test;

/** ToolDef list value tests. */
@SuppressWarnings("javadoc")
public class ToolDefListTest {
    @Test
    public void testCreateToString() {
        ToolDefList<Integer> lst1 = new ToolDefList<>();
        assertEquals("[]", lst1.toString());

        ToolDefList<Integer> lst2 = new ToolDefList<>(123);
        assertEquals("[]", lst2.toString());

        ToolDefList<Integer> lst3 = new ToolDefList<>(set(1, 2));
        assertEquals("[1, 2]", lst3.toString());

        ToolDefList<Integer> lst4 = new ToolDefList<>(list(1, 2));
        assertEquals("[1, 2]", lst4.toString());

        ToolDefList<Integer> lst5 = new ToolDefList<>(lst4);
        assertEquals("[1, 2]", lst5.toString());
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInitialCapacityNegative() {
        new ToolDefList<>(-1);
    }

    @Test
    public void testWrap() {
        List<ToolDefEqWrap<Integer>> lst1;
        lst1 = list(ToolDefEqWrap.wrap(1), ToolDefEqWrap.wrap(2));
        assertEquals("[1, 2]", lst1.toString());

        List<Integer> lst2;
        lst2 = ToolDefList.wrap(lst1);
        assertEquals("[1, 2]", lst2.toString());
    }

    @Test
    public void testAdd() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(3, 4));
        assertEquals("[3, 4]", lst.toString());
        assertEquals(2, lst.size());

        boolean added = lst.add(5);
        assertEquals("[3, 4, 5]", lst.toString());
        assertEquals(3, lst.size());
        assertTrue(added);
    }

    @Test
    public void testAddIndex() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(3, 4));
        assertEquals("[3, 4]", lst.toString());
        assertEquals(2, lst.size());

        lst.add(1, 5);
        assertEquals("[3, 5, 4]", lst.toString());
        assertEquals(3, lst.size());

        lst.add(0, 6);
        assertEquals("[6, 3, 5, 4]", lst.toString());
        assertEquals(4, lst.size());

        lst.add(4, 7);
        assertEquals("[6, 3, 5, 4, 7]", lst.toString());
        assertEquals(5, lst.size());
    }

    @Test
    public void testAddAll() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(3, 4));
        assertEquals("[3, 4]", lst.toString());
        assertEquals(2, lst.size());

        List<Integer> empty = list();

        boolean added = lst.addAll(empty);
        assertEquals("[3, 4]", lst.toString());
        assertEquals(2, lst.size());
        assertFalse(added);

        added = lst.addAll(list(5, 6));
        assertEquals("[3, 4, 5, 6]", lst.toString());
        assertEquals(4, lst.size());
        assertTrue(added);
    }

    @Test
    public void testAddAllIndex() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(3, 4));
        assertEquals("[3, 4]", lst.toString());
        assertEquals(2, lst.size());

        List<Integer> empty = list();

        boolean added = lst.addAll(1, empty);
        assertEquals("[3, 4]", lst.toString());
        assertEquals(2, lst.size());
        assertFalse(added);

        added = lst.addAll(1, list(5, 6));
        assertEquals("[3, 5, 6, 4]", lst.toString());
        assertEquals(4, lst.size());
        assertTrue(added);
    }

    @Test
    public void testClear() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(3, 4));
        assertEquals("[3, 4]", lst.toString());
        assertEquals(2, lst.size());
        assertFalse(lst.isEmpty());

        lst.clear();
        assertEquals("[]", lst.toString());
        assertEquals(0, lst.size());
        assertTrue(lst.isEmpty());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testContains() {
        ToolDefList<String> lst = new ToolDefList<>(list("a", "b"));
        assertTrue(lst.contains("a"));
        assertTrue(lst.contains("b"));
        assertFalse(lst.contains("c"));
        assertTrue(lst.contains(ToolDefEqWrap.wrap("a")));
        assertTrue(lst.contains(ToolDefEqWrap.wrap("b")));
        assertFalse(lst.contains(ToolDefEqWrap.wrap("c")));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testContainsAll() {
        ToolDefList<String> lst = new ToolDefList<>(list("a", "b"));
        assertTrue(lst.containsAll(list("a")));
        assertTrue(lst.containsAll(list("a", "b")));
        assertFalse(lst.containsAll(list("c")));
        assertFalse(lst.containsAll(list("a", "c")));
        assertFalse(lst.containsAll(list("c", "a")));

        assertTrue(lst.containsAll(list(ToolDefEqWrap.wrap("a"))));
        assertTrue(lst.containsAll(list(ToolDefEqWrap.wrap("a"), ToolDefEqWrap.wrap("b"))));
        assertFalse(lst.containsAll(list(ToolDefEqWrap.wrap("c"))));
        assertFalse(lst.containsAll(list(ToolDefEqWrap.wrap("a"), ToolDefEqWrap.wrap("c"))));
        assertFalse(lst.containsAll(list(ToolDefEqWrap.wrap("c"), ToolDefEqWrap.wrap("a"))));
    }

    @Test
    public void testEqualsHashCode() {
        Set<ToolDefList<Object>> x = set();
        ToolDefList<Object> empty1 = new ToolDefList<>();
        ToolDefList<Object> empty2 = new ToolDefList<>();
        ToolDefList<Object> single1a = new ToolDefList<>(list((Object)1));
        ToolDefList<Object> single1b = new ToolDefList<>(list((Object)1.0));
        ToolDefList<Object> single2a = new ToolDefList<>(list((Object)2));
        ToolDefList<Object> single2b = new ToolDefList<>(list((Object)2));
        ToolDefList<Object> two1a = new ToolDefList<>(list((Object)1, 1));
        ToolDefList<Object> two1b = new ToolDefList<>(list((Object)1, 1));
        ToolDefList<Object> two2a = new ToolDefList<>(list((Object)1, 2));
        ToolDefList<Object> two2b = new ToolDefList<>(list((Object)1, 2));
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
        assertEquals("[[], [1], [2], [1, 1], [1, 2]]", x.toString());
    }

    @Test
    public void testGet() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(3, 4));
        assertEquals("[3, 4]", lst.toString());
        assertEquals(3, (int)lst.get(0));
        assertEquals(4, (int)lst.get(1));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testIndexOf() {
        ToolDefList<String> lst = new ToolDefList<>(list("a", "b", "a", "b"));
        assertEquals(0, lst.indexOf("a"));
        assertEquals(1, lst.indexOf("b"));
        assertEquals(-1, lst.indexOf("c"));
        assertEquals(0, lst.indexOf(ToolDefEqWrap.wrap("a")));
        assertEquals(1, lst.indexOf(ToolDefEqWrap.wrap("b")));
        assertEquals(-1, lst.indexOf(ToolDefEqWrap.wrap("c")));
    }

    @Test
    public void testEmptySize() {
        ToolDefList<Integer> lst1 = new ToolDefList<>();
        assertEquals(0, lst1.size());
        assertEquals(true, lst1.isEmpty());

        ToolDefList<Integer> lst2 = new ToolDefList<>(123);
        assertEquals(0, lst2.size());
        assertEquals(true, lst2.isEmpty());

        ToolDefList<Integer> lst3 = new ToolDefList<>(set(1, 2));
        assertEquals(2, lst3.size());
        assertEquals(false, lst3.isEmpty());

        ToolDefList<Integer> lst4 = new ToolDefList<>(list(1, 2));
        assertEquals(2, lst4.size());
        assertEquals(false, lst4.isEmpty());

        ToolDefList<Integer> lst5 = new ToolDefList<>(lst4);
        assertEquals(2, lst5.size());
        assertEquals(false, lst5.isEmpty());
    }

    @Test
    public void testIterator() {
        ToolDefList<String> lst = new ToolDefList<>(list("a", "b"));
        assertEquals("[\"a\", \"b\"]", lst.toString());

        Iterator<String> iter = lst.iterator();
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
        assertTrue(iter.hasNext());
        assertEquals("b", iter.next());
        assertFalse(iter.hasNext());
        assertEquals("[\"a\", \"b\"]", lst.toString());

        iter = lst.iterator();
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
        iter.remove();
        assertEquals("[\"b\"]", lst.toString());
        assertTrue(iter.hasNext());
        assertEquals("b", iter.next());
        iter.remove();
        assertEquals("[]", lst.toString());
        assertFalse(iter.hasNext());
        assertEquals("[]", lst.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testLastIndexOf() {
        ToolDefList<String> lst = new ToolDefList<>(list("a", "b", "a", "b"));
        assertEquals(2, lst.lastIndexOf("a"));
        assertEquals(3, lst.lastIndexOf("b"));
        assertEquals(-1, lst.lastIndexOf("c"));
        assertEquals(2, lst.lastIndexOf(ToolDefEqWrap.wrap("a")));
        assertEquals(3, lst.lastIndexOf(ToolDefEqWrap.wrap("b")));
        assertEquals(-1, lst.lastIndexOf(ToolDefEqWrap.wrap("c")));
    }

    @Test
    public void testListIterator() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(1, 2));
        assertEquals("[1, 2]", lst.toString());

        ListIterator<Integer> iter = lst.listIterator();
        assertTrue(iter.hasNext());
        assertFalse(iter.hasPrevious());
        assertEquals(0, iter.nextIndex());
        assertEquals(-1, iter.previousIndex());

        assertEquals(1, (int)iter.next());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(1, iter.nextIndex());
        assertEquals(0, iter.previousIndex());

        assertEquals(2, (int)iter.next());
        assertFalse(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(2, iter.nextIndex());
        assertEquals(1, iter.previousIndex());

        assertEquals(2, (int)iter.previous());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(1, iter.nextIndex());
        assertEquals(0, iter.previousIndex());

        assertEquals(1, (int)iter.previous());
        assertTrue(iter.hasNext());
        assertFalse(iter.hasPrevious());
        assertEquals(0, iter.nextIndex());
        assertEquals(-1, iter.previousIndex());

        assertEquals("[1, 2]", lst.toString());

        assertEquals(1, (int)iter.next());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(1, iter.nextIndex());
        assertEquals(0, iter.previousIndex());

        iter.add(3);
        assertEquals("[1, 3, 2]", lst.toString());

        assertEquals(2, (int)iter.next());
        iter.set(4);
        assertEquals("[1, 3, 4]", lst.toString());

        iter = lst.listIterator();

        assertTrue(iter.hasNext());
        assertEquals(1, (int)iter.next());
        iter.remove();
        assertEquals("[3, 4]", lst.toString());

        assertTrue(iter.hasNext());
        assertEquals(3, (int)iter.next());
        iter.remove();
        assertEquals("[4]", lst.toString());

        assertTrue(iter.hasNext());
        assertEquals(4, (int)iter.next());
        iter.remove();
        assertEquals("[]", lst.toString());

        assertFalse(iter.hasNext());
        assertEquals("[]", lst.toString());
    }

    @Test
    public void testListIteratorIndex() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(1, 2));
        assertEquals("[1, 2]", lst.toString());

        ListIterator<Integer> iter = lst.listIterator(1);
        assertEquals(2, (int)iter.next());
        assertFalse(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(2, iter.nextIndex());
        assertEquals(1, iter.previousIndex());

        assertEquals(2, (int)iter.previous());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(1, iter.nextIndex());
        assertEquals(0, iter.previousIndex());

        assertEquals(1, (int)iter.previous());
        assertTrue(iter.hasNext());
        assertFalse(iter.hasPrevious());
        assertEquals(0, iter.nextIndex());
        assertEquals(-1, iter.previousIndex());

        assertEquals("[1, 2]", lst.toString());

        assertEquals(1, (int)iter.next());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(1, iter.nextIndex());
        assertEquals(0, iter.previousIndex());

        iter.add(3);
        assertEquals("[1, 3, 2]", lst.toString());

        assertEquals(2, (int)iter.next());
        iter.set(4);
        assertEquals("[1, 3, 4]", lst.toString());

        iter = lst.listIterator(1);
        iter.previous();

        assertTrue(iter.hasNext());
        assertEquals(1, (int)iter.next());
        iter.remove();
        assertEquals("[3, 4]", lst.toString());

        assertTrue(iter.hasNext());
        assertEquals(3, (int)iter.next());
        iter.remove();
        assertEquals("[4]", lst.toString());

        assertTrue(iter.hasNext());
        assertEquals(4, (int)iter.next());
        iter.remove();
        assertEquals("[]", lst.toString());

        assertFalse(iter.hasNext());
        assertEquals("[]", lst.toString());
    }

    @Test
    public void testRemoveIndex() {
        ToolDefList<Double> lst = new ToolDefList<>(list(3.0, 4.0, 3.0, 4.0));
        assertEquals("[3.0, 4.0, 3.0, 4.0]", lst.toString());
        assertEquals(4, lst.size());

        double removed = lst.remove(2);
        assertEquals("[3.0, 4.0, 4.0]", lst.toString());
        assertEquals(3, lst.size());
        assertEquals(3.0, removed, 0.0);

        removed = lst.remove(0);
        assertEquals("[4.0, 4.0]", lst.toString());
        assertEquals(2, lst.size());
        assertEquals(3.0, removed, 0.0);
    }

    @Test
    public void testRemoveObject() {
        ToolDefList<Double> lst = new ToolDefList<>(list(3.0, 4.0, 3.0, 4.0));
        assertEquals("[3.0, 4.0, 3.0, 4.0]", lst.toString());
        assertEquals(4, lst.size());

        boolean changed = lst.remove(4.0);
        assertEquals("[3.0, 3.0, 4.0]", lst.toString());
        assertEquals(3, lst.size());
        assertTrue(changed);

        changed = lst.remove(4.0);
        assertEquals("[3.0, 3.0]", lst.toString());
        assertEquals(2, lst.size());
        assertTrue(changed);

        changed = lst.remove(4.0);
        assertEquals("[3.0, 3.0]", lst.toString());
        assertEquals(2, lst.size());
        assertFalse(changed);
    }

    @Test
    public void testRemoveAll() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(1, 2, 3, 4, 1, 2, 3, 4));
        assertEquals("[1, 2, 3, 4, 1, 2, 3, 4]", lst.toString());
        assertEquals(8, lst.size());

        boolean changed = lst.removeAll(list(2, 4));
        assertEquals("[1, 3, 1, 3]", lst.toString());
        assertEquals(4, lst.size());
        assertTrue(changed);

        changed = lst.removeAll(list(2, 4));
        assertEquals("[1, 3, 1, 3]", lst.toString());
        assertEquals(4, lst.size());
        assertFalse(changed);
    }

    @Test
    public void testRetainAll() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(1, 2, 3, 4, 1, 2, 3, 4));
        assertEquals("[1, 2, 3, 4, 1, 2, 3, 4]", lst.toString());
        assertEquals(8, lst.size());

        boolean changed = lst.retainAll(list(2, 4));
        assertEquals("[2, 4, 2, 4]", lst.toString());
        assertEquals(4, lst.size());
        assertTrue(changed);

        changed = lst.retainAll(list(2, 4));
        assertEquals("[2, 4, 2, 4]", lst.toString());
        assertEquals(4, lst.size());
        assertFalse(changed);
    }

    @Test
    public void testSet() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(1, 2, 3, 4));
        assertEquals("[1, 2, 3, 4]", lst.toString());

        int prev = lst.set(2, 7);
        assertEquals("[1, 2, 7, 4]", lst.toString());
        assertEquals(3, prev);
    }

    @Test
    public void testSubList() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(1, 2, 3, 4));
        assertEquals("[1, 2, 3, 4]", lst.toString());

        List<Integer> sub = lst.subList(1, 3);
        assertEquals("[2, 3]", sub.toString());

        sub.remove(1);
        assertEquals("[1, 2, 4]", lst.toString());
        assertEquals("[2]", sub.toString());
    }

    @Test
    public void testToArray() {
        ToolDefList<Integer> lst = new ToolDefList<>(list(3, 4));
        assertEquals("[3, 4]", lst.toString());

        Object[] array1 = lst.toArray();
        assertEquals(2, array1.length);
        assertEquals(3, array1[0]);
        assertEquals(4, array1[1]);

        Integer[] array2 = lst.toArray(new Integer[2]);
        assertEquals(2, array2.length);
        assertEquals(3, (int)array2[0]);
        assertEquals(4, (int)array2[1]);

        Object[] array3 = lst.toArray(new Object[2]);
        assertEquals(2, array3.length);
        assertEquals(3, array3[0]);
        assertEquals(4, array3[1]);
    }
}
