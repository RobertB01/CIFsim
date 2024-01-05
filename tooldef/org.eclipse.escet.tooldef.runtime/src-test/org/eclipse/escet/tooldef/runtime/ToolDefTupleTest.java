//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/** ToolDef tuple value tests. */
@SuppressWarnings("javadoc")
public class ToolDefTupleTest {
    @Test
    public void testSize() {
        ToolDefTuplePair<Integer, Boolean> p;
        ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>> n1;
        ToolDefTupleNary<List<Integer>, ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>>> n2;

        List<Integer> list = new ToolDefList<>(2);
        list.add(2);
        list.add(3);

        p = new ToolDefTuplePair<>(1, true);
        n1 = new ToolDefTupleNary<>("a", p);
        n2 = new ToolDefTupleNary<>(list, n1);

        assertEquals(2, p.size());
        assertEquals(3, n1.size());
        assertEquals(4, n2.size());
    }

    @Test
    public void testGetValues() {
        ToolDefTuplePair<Integer, Boolean> p;
        ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>> n1;
        ToolDefTupleNary<List<Integer>, ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>>> n2;

        List<Integer> list = new ToolDefList<>(2);
        list.add(2);
        list.add(3);

        p = new ToolDefTuplePair<>(1, true);
        n1 = new ToolDefTupleNary<>("a", p);
        n2 = new ToolDefTupleNary<>(list, n1);

        List<Object> valuesP = p.getValues();
        List<Object> valuesN1 = n1.getValues();
        List<Object> valuesN2 = n2.getValues();

        assertEquals(2, valuesP.size());
        assertEquals(1, valuesP.get(0));
        assertEquals(true, valuesP.get(1));

        assertEquals(3, valuesN1.size());
        assertEquals("a", valuesN1.get(0));
        assertEquals(1, valuesN1.get(1));
        assertEquals(true, valuesN1.get(2));

        assertEquals(4, valuesN2.size());
        assertEquals(list, valuesN2.get(0));
        assertEquals("a", valuesN2.get(1));
        assertEquals(1, valuesN2.get(2));
        assertEquals(true, valuesN2.get(3));

        assertSame(list, valuesN2.get(0));
    }

    @Test
    public void testCreateToString() {
        ToolDefTuplePair<Integer, Boolean> p;
        ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>> n1;
        ToolDefTupleNary<List<Integer>, ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>>> n2;

        List<Integer> list = new ToolDefList<>(2);
        list.add(2);
        list.add(3);

        p = new ToolDefTuplePair<>(1, true);
        n1 = new ToolDefTupleNary<>("a", p);
        n2 = new ToolDefTupleNary<>(list, n1);

        assertEquals("(1, true)", p.toString());
        assertEquals("(\"a\", 1, true)", n1.toString());
        assertEquals("([2, 3], \"a\", 1, true)", n2.toString());
    }

    @Test
    public void testEquals() {
        ToolDefTuplePair<Integer, Boolean> v0;
        ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>> v1;
        ToolDefTupleNary<List<Integer>, ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>>> v2;

        ToolDefTuplePair<Integer, Boolean> w0;
        ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>> w1;
        ToolDefTupleNary<List<Integer>, ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>>> w2;

        List<Integer> listv = new ToolDefList<>(2);
        listv.add(2);
        listv.add(3);

        List<Integer> listw = new ToolDefList<>(2);
        listw.add(2);
        listw.add(3);

        v0 = new ToolDefTuplePair<>(1, true);
        v1 = new ToolDefTupleNary<>("a", v0);
        v2 = new ToolDefTupleNary<>(listv, v1);

        w0 = new ToolDefTuplePair<>(1, true);
        w1 = new ToolDefTupleNary<>("a", w0);
        w2 = new ToolDefTupleNary<>(listw, w1);

        assertTrue(v0.equals(w0));
        assertTrue(v1.equals(w1));
        assertTrue(v2.equals(w2));

        listv = new ToolDefList<>(2);
        listv.add(2);
        listv.add(3);

        v0 = new ToolDefTuplePair<>(5, true);
        v1 = new ToolDefTupleNary<>("a", v0);
        v2 = new ToolDefTupleNary<>(listv, v1);

        assertFalse(v0.equals(w0));
        assertFalse(v1.equals(w1));
        assertFalse(v2.equals(w2));
    }

    @Test
    public void testEqualsHash() {
        Set<ToolDefTuple> tuples = new ToolDefSet<>();

        ToolDefTuplePair<Integer, Boolean> v0;
        ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>> v1;
        ToolDefTupleNary<List<Integer>, ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>>> v2;

        ToolDefTuplePair<Integer, Boolean> w0;
        ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>> w1;
        ToolDefTupleNary<List<Integer>, ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>>> w2;

        List<Integer> listv = new ToolDefList<>(2);
        listv.add(2);
        listv.add(3);

        List<Integer> listw = new ToolDefList<>(2);
        listw.add(2);
        listw.add(3);

        v0 = new ToolDefTuplePair<>(1, true);
        v1 = new ToolDefTupleNary<>("a", v0);
        v2 = new ToolDefTupleNary<>(listv, v1);

        w0 = new ToolDefTuplePair<>(1, true);
        w1 = new ToolDefTupleNary<>("a", w0);
        w2 = new ToolDefTupleNary<>(listw, w1);

        tuples.add(v0);
        tuples.add(v1);
        tuples.add(v2);
        tuples.add(w0);
        tuples.add(w1);
        tuples.add(w2);

        assertEquals(3, tuples.size());
        assertEquals("{(1, true), (\"a\", 1, true), ([2, 3], \"a\", 1, true)}", tuples.toString());

        List<ToolDefTuple> tuples2 = new ToolDefList<>(tuples);
        assertEquals(3, tuples.size());
        assertSame(v0, tuples2.get(0));
        assertSame(v1, tuples2.get(1));
        assertSame(v2, tuples2.get(2));
    }

    @Test
    public void testGetValue() {
        ToolDefTuplePair<Integer, Boolean> p;
        ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>> n1;
        ToolDefTupleNary<List<Integer>, ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>>> n2;

        List<Integer> list = new ToolDefList<>(2);
        list.add(2);
        list.add(3);

        p = new ToolDefTuplePair<>(1, true);
        n1 = new ToolDefTupleNary<>("a", p);
        n2 = new ToolDefTupleNary<>(list, n1);

        assertEquals(1, p.getValue(0));
        assertEquals(true, p.getValue(1));

        assertEquals("a", n1.getValue(0));
        assertEquals(1, n1.getValue(1));
        assertEquals(true, n1.getValue(2));

        assertEquals(list, n2.getValue(0));
        assertEquals("a", n2.getValue(1));
        assertEquals(1, n2.getValue(2));
        assertEquals(true, n2.getValue(3));
    }

    @Test
    public void testGetValuePairErr() {
        ToolDefTuplePair<Integer, Boolean> p;
        p = new ToolDefTuplePair<>(1, true);

        assertThrows(IllegalArgumentException.class, () -> p.getValue(2));
    }

    @Test
    public void testGetValueNaryErr() {
        ToolDefTuplePair<Integer, Boolean> p;
        ToolDefTupleNary<String, ToolDefTuplePair<Integer, Boolean>> n1;
        p = new ToolDefTuplePair<>(1, true);
        n1 = new ToolDefTupleNary<>("a", p);

        assertThrows(IllegalArgumentException.class, () -> n1.getValue(3));
    }
}
