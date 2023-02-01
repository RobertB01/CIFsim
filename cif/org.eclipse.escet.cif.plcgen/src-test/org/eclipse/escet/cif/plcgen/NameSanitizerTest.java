//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen;

import static org.junit.Assert.assertEquals;

import org.eclipse.escet.cif.plcgen.generators.NameSanitizer;
import org.junit.Before;
import org.junit.Test;

/** Tests for the {@link NameSanitizer} generator. */
public class NameSanitizerTest {
    /** Sanitizer instance under test. */
    public NameSanitizer nameSanitizer;

    @SuppressWarnings("javadoc")
    @Before
    public void setup() {
        PlcGenSettings settings = new PlcGenSettings(null, null, null, null, 0, 0, null, null, null, null, null, null, false, null);
        nameSanitizer = new NameSanitizer(settings);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void nonDuplicateNamesTest() {
        String sName = nameSanitizer.sanitizeName("s", false);
        assertEquals("s", sName);
        String saniName = nameSanitizer.sanitizeName("sani", false);
        assertEquals("sani", saniName);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void keywordAvoidanceTest() {
        String dintName = nameSanitizer.sanitizeName("dint", false);
        assertEquals("dint1", dintName);
        String sintName = nameSanitizer.sanitizeName("SInt", false); // Partial upper case.
        assertEquals("SInt1", sintName);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void duplicateNameTest() {
        String sName = nameSanitizer.sanitizeName("s", false);
        assertEquals("s", sName);
        String sName1 = nameSanitizer.sanitizeName("s", false);
        assertEquals("s1", sName1);
        String sName2 = nameSanitizer.sanitizeName("s", false);
        assertEquals("s2", sName2);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void useHigherSuffixTest() {
        String sName = nameSanitizer.sanitizeName("s", false);
        assertEquals("s", sName);
        String sName1 = nameSanitizer.sanitizeName("s1", false); // Unused suffix.
        assertEquals("s1", sName1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void useLowerSuffixTest() {
        String sName1 = nameSanitizer.sanitizeName("s3", false);
        assertEquals("s3", sName1);
        String sName2 = nameSanitizer.sanitizeName("s1", false); // Use next higher suffix suffix.
        assertEquals("s4", sName2);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void clashingSuffixTest() {
        String sName1 = nameSanitizer.sanitizeName("s1", false); // Unused suffix.
        assertEquals("s1", sName1);
        String sName2 = nameSanitizer.sanitizeName("s1", false); // Duplicate, use next higher suffix.
        assertEquals("s2", sName2);
    }
}
