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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.ArrayUtils.array;
import static org.eclipse.escet.common.java.ArrayUtils.reverse;
import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/** Unit tests for the methods of the {@link ArrayUtils} class. */
public class ArrayUtilsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testArray() {
        Integer[] ai1 = array(1, 2, 3);
        Integer[] ai2 = array();

        assertEquals("[1, 2, 3]", Arrays.deepToString(ai1));
        assertEquals("[]", Arrays.deepToString(ai2));

        String[] as1 = array("a", "b");
        String[] as2 = array();

        assertEquals("[a, b]", Arrays.deepToString(as1));
        assertEquals("[]", Arrays.deepToString(as2));

        List<Integer>[] al1 = array(list(1, 2), list());
        List<Integer>[] al2 = array();

        assertEquals("[[1, 2], []]", Arrays.deepToString(al1));
        assertEquals("[]", Arrays.deepToString(al2));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testReverse() {
        String[] empty = array();
        Integer[] i123 = array(1, 2, 3);
        List<Integer>[] list = array(list(1, 2), list());

        String[] reverseEmpty = reverse(empty);
        Integer[] reverseI123 = reverse(i123);
        List<Integer>[] reverseList = reverse(list);

        assertNotSame(empty, reverseEmpty);
        assertNotSame(i123, reverseI123);
        assertNotSame(list, reverseList);

        assertEquals(0, reverseEmpty.length);
        assertEquals(3, reverseI123.length);
        assertEquals(2, reverseList.length);

        assertEquals(3, (int)reverseI123[0]);
        assertEquals(2, (int)reverseI123[1]);
        assertEquals(1, (int)reverseI123[2]);

        assertEquals(list(), reverseList[0]);
        assertEquals(list(1, 2), reverseList[1]);

        assertSame(i123[0], reverseI123[2]);
        assertSame(i123[1], reverseI123[1]);
        assertSame(i123[2], reverseI123[0]);

        assertSame(list[0], reverseList[1]);
        assertSame(list[1], reverseList[0]);
    }
}
