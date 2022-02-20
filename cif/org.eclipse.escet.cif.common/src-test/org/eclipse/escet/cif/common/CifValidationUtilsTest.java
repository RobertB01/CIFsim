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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifValidationUtils.isValidIdentifier;
import static org.eclipse.escet.cif.common.CifValidationUtils.isValidName;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Unit tests for the {@link CifValidationUtils} class. */
@SuppressWarnings("javadoc")
public class CifValidationUtilsTest {
    @Test
    public void testIsValidIdentifier() {
        assertEquals(true, isValidIdentifier("a"));
        assertEquals(true, isValidIdentifier("aB7b_c9D"));
        assertEquals(true, isValidIdentifier("_a"));
        assertEquals(true, isValidIdentifier("_1"));
        assertEquals(true, isValidIdentifier("_"));
        assertEquals(true, isValidIdentifier("a1"));

        assertEquals(false, isValidIdentifier(""));
        assertEquals(false, isValidIdentifier("0"));
        assertEquals(false, isValidIdentifier("1"));
        assertEquals(false, isValidIdentifier("9"));
        assertEquals(false, isValidIdentifier("a.b"));
        assertEquals(false, isValidIdentifier("a%b"));
        assertEquals(false, isValidIdentifier("a b"));
    }

    @Test
    public void testIsValidName() {
        assertEquals(true, isValidName("a"));
        assertEquals(true, isValidName("a.b"));
        assertEquals(true, isValidName("a.b.c"));
        assertEquals(true, isValidName("a.b_c"));
        assertEquals(true, isValidName("a_b.c"));
        assertEquals(true, isValidName("a._b"));
        assertEquals(true, isValidName("_._"));

        assertEquals(false, isValidName(""));
        assertEquals(false, isValidName("."));
        assertEquals(false, isValidName("a."));
        assertEquals(false, isValidName(".a"));
        assertEquals(false, isValidName("a.a."));
        assertEquals(false, isValidName(".a.a"));
        assertEquals(false, isValidName("a..b"));
        assertEquals(false, isValidName("a.b..c"));
        assertEquals(false, isValidName("a..b.c"));
        assertEquals(false, isValidName("a.b c"));
        assertEquals(false, isValidName("a.1"));
    }
}
