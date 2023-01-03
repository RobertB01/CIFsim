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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/** {@link ToolDefEqWrap} unit tests. */
@SuppressWarnings("javadoc")
public class ToolDefEqWrapTest {
    @Test
    public void testInt() {
        ToolDefEqWrap<Integer> wrap1 = ToolDefEqWrap.wrap(1);
        ToolDefEqWrap<Integer> wrap2 = ToolDefEqWrap.wrap(1);
        assertNotSame(wrap1, wrap2);

        assertEquals(wrap1, wrap1);
        assertEquals(wrap1, wrap2);
        assertEquals(wrap2, wrap2);
        assertEquals("1", wrap1.toString());
        assertEquals("1", wrap2.toString());
    }

    @Test
    public void testIntLong() {
        ToolDefEqWrap<Integer> wrap1 = ToolDefEqWrap.wrap(1);
        ToolDefEqWrap<Long> wrap2 = ToolDefEqWrap.wrap(1L);
        assertNotSame(wrap1, wrap2);

        assertEquals(wrap1, wrap1);
        assertEquals(wrap1, wrap2);
        assertEquals(wrap2, wrap2);
        assertEquals("1", wrap1.toString());
        assertEquals("1", wrap2.toString());
    }

    @Test
    public void testIntDouble() {
        ToolDefEqWrap<Integer> wrap1 = ToolDefEqWrap.wrap(1);
        ToolDefEqWrap<Double> wrap2 = ToolDefEqWrap.wrap(1.0);
        assertNotSame(wrap1, wrap2);

        assertEquals(wrap1, wrap1);
        assertEquals(wrap1, wrap2);
        assertEquals(wrap2, wrap2);
        assertEquals("1", wrap1.toString());
        assertEquals("1.0", wrap2.toString());
    }

    @Test
    public void testNull() {
        ToolDefEqWrap<Integer> wrap1 = ToolDefEqWrap.wrap(null);
        ToolDefEqWrap<Long> wrap2 = ToolDefEqWrap.wrap(null);
        assertNotSame(wrap1, wrap2);

        assertEquals(wrap1, wrap1);
        assertEquals(wrap1, wrap2);
        assertEquals(wrap2, wrap2);
        assertEquals("null", wrap1.toString());
        assertEquals("null", wrap2.toString());

        assertNotEquals(null, wrap1);
        assertNotEquals(null, wrap2);
        assertNotEquals(wrap1, null);
        assertNotEquals(wrap2, null);
    }

    @Test
    public void testReWrap() {
        Object wrap1 = ToolDefEqWrap.wrap(1);
        ToolDefEqWrap<Object> wrap2 = ToolDefEqWrap.wrap(wrap1);
        assertSame(wrap1, wrap2);
    }
}
