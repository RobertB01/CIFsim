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

package org.eclipse.escet.cif.plcgen.generators;

import static org.junit.Assert.assertEquals;

import org.eclipse.escet.cif.cif2plc.options.PlcNumberBits;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.junit.Before;
import org.junit.Test;

/** Tests for the {@link NameGenerator} class. */
public class NameGeneratorTest {
    /** Generator instance under test. */
    public NameGenerator nameGenerator;

    @SuppressWarnings("javadoc")
    @Before
    public void setup() {
        PlcGenSettings settings = new PlcGenSettings(null, null, null, null, 0, 0, null, null, null, PlcNumberBits.AUTO,
                PlcNumberBits.AUTO, false, null, null, false, null);
        nameGenerator = new NameGenerator(settings);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void nonDuplicateNamesTest() {
        String sName = nameGenerator.generateName("s", false);
        assertEquals("s", sName);
        String saniName = nameGenerator.generateName("sani", false);
        assertEquals("sani", saniName);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void keywordAvoidanceTest() {
        String sintName = nameGenerator.generateName("sint", false); // PLC keyword.
        assertEquals("sint__1", sintName);
        sintName = nameGenerator.generateName("SInt", false); // Partial upper case.
        assertEquals("SInt__2", sintName);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void duplicateNameTest() {
        String sName = nameGenerator.generateName("s", false);
        assertEquals("s", sName);
        String sName1 = nameGenerator.generateName("s", false);
        assertEquals("s__1", sName1);
        String sName2 = nameGenerator.generateName("s", false);
        assertEquals("s__2", sName2);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void useNumberSuffixTest() {
        String sName = nameGenerator.generateName("s", false);
        assertEquals("s", sName);
        String sName1 = nameGenerator.generateName("s1", false); // Different name, no suffix appended.
        assertEquals("s1", sName1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void clashingSuffixTest() {
        String sName1 = nameGenerator.generateName("s1", false); // Unused suffix.
        assertEquals("s1", sName1);
        String sName2 = nameGenerator.generateName("s1", false); // Duplicate, use next higher suffix.
        assertEquals("s1__1", sName2);
    }
}
