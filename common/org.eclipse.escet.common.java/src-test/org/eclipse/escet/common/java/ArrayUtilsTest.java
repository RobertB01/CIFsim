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

import static org.eclipse.escet.common.java.ArrayUtils.array;
import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

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
}
