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

package org.eclipse.escet.tooldef.runtime.tests;

import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.escet.tooldef.runtime.ToolDefList;
import org.eclipse.escet.tooldef.runtime.ToolDefRuntimeUtils;
import org.eclipse.escet.tooldef.runtime.ToolDefTuple;
import org.junit.Test;

/** {@link ToolDefRuntimeUtils} unit tests. */
@SuppressWarnings("javadoc")
public class ToolDefRuntimeUtilsTest {
    @Test
    public void testTupleMakeUnpack() {
        List<Integer> intsA = new ToolDefList<>(list(1, 2));
        List<Double> doublesA = new ToolDefList<>(list(1.0, 2.0));
        List<String> stringsA = new ToolDefList<>(list("a", "b"));
        List<Boolean> boolsA = new ToolDefList<>(list(true, false));
        List<Object> values2A = list(intsA, doublesA);
        List<Object> values3A = list(intsA, doublesA, stringsA);
        List<Object> values4A = list(intsA, doublesA, stringsA, boolsA);
        ToolDefTuple tuple2A = ToolDefRuntimeUtils.makeTuple(values2A);
        ToolDefTuple tuple3A = ToolDefRuntimeUtils.makeTuple(values3A);
        ToolDefTuple tuple4A = ToolDefRuntimeUtils.makeTuple(values4A);
        assertEquals("([1, 2], [1.0, 2.0])", tuple2A.toString());
        assertEquals("([1, 2], [1.0, 2.0], [\"a\", \"b\"])", tuple3A.toString());
        assertEquals("([1, 2], [1.0, 2.0], [\"a\", \"b\"], [true, false])", tuple4A.toString());

        List<Integer> intsB = new ToolDefList<>(list(1, 2));
        List<Double> doublesB = new ToolDefList<>(list(1.0, 2.0));
        List<String> stringsB = new ToolDefList<>(list("a", "b"));
        List<Boolean> boolsB = new ToolDefList<>(list(true, false));
        List<Object> values2B = list(doublesB, intsB);
        List<Object> values3B = list(doublesB, intsB, stringsB);
        List<Object> values4B = list(doublesB, intsB, stringsB, boolsB);
        ToolDefTuple tuple2B = ToolDefRuntimeUtils.makeTuple(values2B);
        ToolDefTuple tuple3B = ToolDefRuntimeUtils.makeTuple(values3B);
        ToolDefTuple tuple4B = ToolDefRuntimeUtils.makeTuple(values4B);
        assertEquals("([1.0, 2.0], [1, 2])", tuple2B.toString());
        assertEquals("([1.0, 2.0], [1, 2], [\"a\", \"b\"])", tuple3B.toString());
        assertEquals("([1.0, 2.0], [1, 2], [\"a\", \"b\"], [true, false])", tuple4B.toString());

        assertEquals(tuple2A, tuple2B);
        assertEquals(tuple3A, tuple3B);
        assertEquals(tuple4A, tuple4B);

        List<Object> values2C = ToolDefRuntimeUtils.unpackTuple(tuple2A);
        List<Object> values3C = ToolDefRuntimeUtils.unpackTuple(tuple3A);
        List<Object> values4C = ToolDefRuntimeUtils.unpackTuple(tuple4A);
        List<Object> values2D = ToolDefRuntimeUtils.unpackTuple(tuple2B);
        List<Object> values3D = ToolDefRuntimeUtils.unpackTuple(tuple3B);
        List<Object> values4D = ToolDefRuntimeUtils.unpackTuple(tuple4B);
        assertEquals(values2A, values2C);
        assertEquals(values3A, values3C);
        assertEquals(values4A, values4C);
        assertEquals(values2B, values2D);
        assertEquals(values3B, values3D);
        assertEquals(values4B, values4D);
    }
}
