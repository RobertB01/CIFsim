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

import static org.eclipse.escet.common.java.Maps.copy;
import static org.eclipse.escet.common.java.Maps.invert;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Maps.put;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

/** Unit tests for the methods of the {@link Maps} class. */
public class MapsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testPut() {
        NullPointerException e1 = new NullPointerException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        NullPointerException npe = new NullPointerException();
        IllegalArgumentException iae = new IllegalArgumentException();

        Map<NullPointerException, NullPointerException> m1 = map();
        m1.put(e1, e1);

        Map<Exception, Exception> m2 = map();
        m2.put(e2, e2);

        // Add pair to map, all same type.
        Map<NullPointerException, NullPointerException> mp1;
        mp1 = put(m1, npe, npe);
        assertSame(m1, mp1);
        assertEquals(2, mp1.size());
        assertSame(e1, mp1.get(e1));
        assertSame(npe, mp1.get(npe));

        // Add pair to map, super/derived types.
        Map<Exception, Exception> mp2;
        mp2 = put(m2, iae, npe);
        assertSame(m2, mp2);
        assertEquals(2, mp2.size());
        assertSame(e2, mp2.get(e2));
        assertSame(npe, mp2.get(iae));

        // Add pair to map, override.
        Map<Exception, Exception> mp3;
        mp3 = put(mp2, e2, e1);
        assertSame(m2, mp3);
        assertSame(mp2, mp3);
        assertEquals(2, mp3.size());
        assertSame(e1, mp3.get(e2));
        assertSame(npe, mp2.get(iae));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMaps() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();

        Map<RuntimeException, RuntimeException> exs = map();
        exs.put(e1, e2);

        assertEquals(1, exs.size());
        assertTrue(exs.containsKey(e1));
        assertTrue(exs.containsValue(e2));
        assertSame(e2, exs.get(e1));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMapsc() {
        Map<Exception, Exception> xm0 = mapc(0);
        Map<Exception, Exception> xm1 = mapc(1);
        Map<Exception, Exception> xm2 = mapc(2);

        assertEquals(0, xm0.size());
        assertEquals(0, xm1.size());
        assertEquals(0, xm2.size());

        Map<Object, Object> om0 = mapc(0);
        Map<Object, Object> om1 = mapc(1);
        Map<Object, Object> om2 = mapc(2);

        assertEquals(0, om0.size());
        assertEquals(0, om1.size());
        assertEquals(0, om2.size());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCopy() {
        RuntimeException e1 = new NullPointerException();
        RuntimeException e2 = new IllegalArgumentException();

        Map<RuntimeException, RuntimeException> exs = map();
        exs.put(e1, e2);

        assertEquals(1, exs.size());
        assertTrue(exs.containsKey(e1));
        assertTrue(exs.containsValue(e2));
        assertSame(e2, exs.get(e1));

        // Copy to same type.
        Map<RuntimeException, RuntimeException> exs2 = copy(exs);
        assertNotSame(exs, exs2);

        assertEquals(1, exs2.size());
        assertTrue(exs2.containsKey(e1));
        assertTrue(exs2.containsValue(e2));
        assertSame(e2, exs2.get(e1));

        // Copy to super type.
        Map<Exception, Exception> exs3 = copy(exs);
        assertNotSame(exs, exs3);

        assertEquals(1, exs3.size());
        assertTrue(exs3.containsKey(e1));
        assertTrue(exs3.containsValue(e2));
        assertSame(e2, exs3.get(e1));

        // Copy to super-super type.
        Map<Object, Object> exs4 = copy(exs);
        assertNotSame(exs, exs4);

        assertEquals(1, exs4.size());
        assertTrue(exs4.containsKey(e1));
        assertTrue(exs4.containsValue(e2));
        assertSame(e2, exs4.get(e1));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testInvert() {
        NullPointerException npe1 = new NullPointerException();
        NullPointerException npe2 = new NullPointerException();
        IllegalArgumentException iae1 = new IllegalArgumentException();
        IllegalArgumentException iae2 = new IllegalArgumentException();

        Map<RuntimeException, IllegalArgumentException> exs = map();
        exs.put(npe1, iae1);
        exs.put(npe2, iae2);

        assertEquals(2, exs.size());
        assertTrue(exs.containsKey(npe1));
        assertTrue(exs.containsKey(npe2));
        assertTrue(exs.containsValue(iae1));
        assertTrue(exs.containsValue(iae2));
        assertSame(iae1, exs.get(npe1));
        assertSame(iae2, exs.get(npe2));

        // Invert one-to-one mapping.
        Map<IllegalArgumentException, RuntimeException> invExs = invert(exs);
        assertTrue(invExs.containsKey(iae1));
        assertTrue(invExs.containsKey(iae2));
        assertTrue(invExs.containsValue(npe1));
        assertTrue(invExs.containsValue(npe2));
        assertSame(npe1, invExs.get(iae1));
        assertSame(npe2, invExs.get(iae2));

        // Invert many-to-one mapping.
        NullPointerException npe3 = new NullPointerException();
        exs.put(npe3, iae2);
        assertEquals(3, exs.size());
        assertTrue(exs.containsKey(npe3));
        assertSame(iae2, exs.get(npe3));

        Map<IllegalArgumentException, RuntimeException> invExs2 = invert(exs);
        assertTrue(invExs2.containsKey(iae1));
        assertTrue(invExs2.containsKey(iae2));
        assertTrue(invExs2.containsValue(npe1));
        assertTrue(invExs2.containsValue(npe2) || invExs2.containsValue(npe3));
        assertFalse(invExs2.containsValue(npe2) && invExs2.containsValue(npe3));
    }
}
