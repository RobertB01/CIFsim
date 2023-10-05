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

import static org.eclipse.escet.common.java.Maps.map;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for the {@link DefaultNameGenerator} class. */
public class NameGeneratorTest {
    /** Generator instance under test. */
    public DefaultNameGenerator nameGenerator;

    @SuppressWarnings("javadoc")
    @BeforeEach
    public void setup() {
        PlcGenSettings settings = new PlcGenSettings(null, null, null, null, 0, 0, null, null, null, null, null,
                PlcNumberBits.AUTO, PlcNumberBits.AUTO, false, null, null, false, null);
        nameGenerator = new DefaultNameGenerator(settings);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void nonDuplicateNamesTest() {
        assertEquals("s", nameGenerator.generateGlobalName("s", false));
        assertEquals("sani", nameGenerator.generateGlobalName("sani", false));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void keywordAvoidanceTest() {
        assertEquals("sint__1", nameGenerator.generateGlobalName("sint", false)); // PLC keyword.
        assertEquals("SInt__2", nameGenerator.generateGlobalName("SInt", false)); // Partial upper case.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void duplicateNameTest() {
        assertEquals("s", nameGenerator.generateGlobalName("s", false));
        assertEquals("s__1", nameGenerator.generateGlobalName("s", false));
        assertEquals("s__2", nameGenerator.generateGlobalName("s", false));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void useNumberSuffixTest() {
        assertEquals("s", nameGenerator.generateGlobalName("s", false));
        assertEquals("s1", nameGenerator.generateGlobalName("s1", false)); // Different name, no suffix appended.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void clashingSuffixTest() {
        assertEquals("s1", nameGenerator.generateGlobalName("s1", false)); // Unused suffix.
        assertEquals("s1__1", nameGenerator.generateGlobalName("s1", false)); // Duplicate, use next higher suffix.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void garbageTest() {
        // Completely garbage.
        assertEquals("x", nameGenerator.generateGlobalName("", false)); // Empty input.
        assertEquals("x__1", nameGenerator.generateGlobalName(".", false)); // Garbage input.
        assertEquals("x__2", nameGenerator.generateGlobalName("_", false)); // Another garbage input produces a new
                                                                            // name.

        // Leading garbage.
        assertEquals("t5", nameGenerator.generateGlobalName(".t5", false)); // Skip garbage before identifier.
        assertEquals("x55", nameGenerator.generateGlobalName(".55", false)); // Insert letter to make it an identifier.

        assertEquals("x55__1", nameGenerator.generateGlobalName("x55", false)); // x55 was already generated.

        // Trailing garbage does not change output.
        assertEquals("t4", nameGenerator.generateGlobalName("t4.", false));
        assertEquals("x44", nameGenerator.generateGlobalName("44.", false));

        // Internal garbage is compressed to a single underscore.
        assertEquals("x4_4", nameGenerator.generateGlobalName("4._4", false));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void oneLocalScopeTest() {
        // Local names avoid other local names.
        Map<String, Integer> localSuffixes = map();
        assertEquals("s", nameGenerator.generateLocalName("s", localSuffixes));
        assertEquals("s__1", nameGenerator.generateLocalName("s", localSuffixes));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void twoLocalScopesTest() {
        // Local names in a scope avoid other local names in the same scope.
        Map<String, Integer> localSuffixes = map();
        assertEquals("s", nameGenerator.generateLocalName("s", localSuffixes));
        assertEquals("s__1", nameGenerator.generateLocalName("s", localSuffixes));

        // Local names between different scopes may be duplicate.
        localSuffixes = map(); // Use a different map for a different scope.
        assertEquals("s", nameGenerator.generateLocalName("s", localSuffixes));
        assertEquals("s__1", nameGenerator.generateLocalName("s", localSuffixes));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void globalAndLocalTest() {
        // Local names avoid pre-existing global names.
        assertEquals("s", nameGenerator.generateGlobalName("s", false));
        Map<String, Integer> localSuffixes = map();
        assertEquals("s__1", nameGenerator.generateLocalName("s", localSuffixes));
        assertEquals("s__2", nameGenerator.generateLocalName("s", localSuffixes));
    }
}
