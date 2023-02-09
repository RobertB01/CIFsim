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
        assertEquals("s", nameGenerator.generateName("s", false));
        assertEquals("sani", nameGenerator.generateName("sani", false));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void keywordAvoidanceTest() {
        assertEquals("sint__1", nameGenerator.generateName("sint", false)); // PLC keyword.
        assertEquals("SInt__2", nameGenerator.generateName("SInt", false)); // Partial upper case.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void duplicateNameTest() {
        assertEquals("s", nameGenerator.generateName("s", false));
        assertEquals("s__1", nameGenerator.generateName("s", false));
        assertEquals("s__2", nameGenerator.generateName("s", false));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void useNumberSuffixTest() {
        assertEquals("s", nameGenerator.generateName("s", false));
        assertEquals("s1", nameGenerator.generateName("s1", false)); // Different name, no suffix appended.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void clashingSuffixTest() {
        assertEquals("s1", nameGenerator.generateName("s1", false)); // Unused suffix.
        assertEquals("s1__1", nameGenerator.generateName("s1", false)); // Duplicate, use next higher suffix.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void garbageTest() {
        // Completely garbage.
        assertEquals("x", nameGenerator.generateName("", false)); // Empty input.
        assertEquals("x__1", nameGenerator.generateName(".", false)); // Garbage input.
        assertEquals("x__2", nameGenerator.generateName("_", false)); // Another garbage input produces a new name.

        // Leading garbage
        assertEquals("t5", nameGenerator.generateName(".t5", false)); // Skip garbage before identifier.
        assertEquals("x55", nameGenerator.generateName(".55", false)); // Insert letter to make it an identifier.

        assertEquals("x55__1", nameGenerator.generateName("x55", false)); // x55 was already generated.

        // Trailing garbage does not change output.
        assertEquals("t4", nameGenerator.generateName("t4.", false));
        assertEquals("x44", nameGenerator.generateName("44.", false));

        // Internal garbage is compressed to a single underscore.
        assertEquals("x4_4", nameGenerator.generateName("4._4", false));
    }
}
