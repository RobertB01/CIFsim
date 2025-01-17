//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.generators.names.NameScope;
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
                null, PlcNumberBits.AUTO, PlcNumberBits.AUTO, false, null, null, null, false, null);
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
        assertEquals("sint_1", nameGenerator.generateGlobalName("sint", false)); // PLC keyword.
        assertEquals("SInt_2", nameGenerator.generateGlobalName("SInt", false)); // Partial upper case.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void duplicateNameTest() {
        assertEquals("s", nameGenerator.generateGlobalName("s", false));
        assertEquals("s_1", nameGenerator.generateGlobalName("s", false));
        assertEquals("s_2", nameGenerator.generateGlobalName("s", false));
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
        assertEquals("s1_1", nameGenerator.generateGlobalName("s1", false)); // Duplicate, use next higher suffix.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void garbageTest() {
        // Completely garbage.
        assertEquals("x", nameGenerator.generateGlobalName("", false)); // Empty input.
        assertEquals("x_1", nameGenerator.generateGlobalName(".", false)); // Garbage input.
        assertEquals("x_2", nameGenerator.generateGlobalName("_", false)); // Another garbage input produces a new name.

        // Leading garbage.
        assertEquals("t5", nameGenerator.generateGlobalName(".t5", false)); // Skip garbage before identifier.
        assertEquals("x55", nameGenerator.generateGlobalName(".55", false)); // Insert letter to make it an identifier.

        assertEquals("x55_1", nameGenerator.generateGlobalName("x55", false)); // x55 was already generated.

        // Trailing garbage does not change output.
        assertEquals("t4", nameGenerator.generateGlobalName("t4.", false));
        assertEquals("x44", nameGenerator.generateGlobalName("44.", false));

        // Internal garbage is compressed to a single underscore.
        assertEquals("x4_x4", nameGenerator.generateGlobalName("4._4", false));
        assertEquals("x4_x4_1", nameGenerator.generateGlobalName("4._4", false)); // Append an index number the second
                                                                                  // time.
    }

    @SuppressWarnings("javadoc")
    @Test
    public void oneLocalScopeTest() {
        // Local names avoid other local names.
        NameScope localScope = new NameScope();
        assertEquals("s", nameGenerator.generateLocalName("s", localScope));
        assertEquals("s_1", nameGenerator.generateLocalName("s", localScope));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void twoLocalScopesTest() {
        // Local names in a scope avoid other local names in the same scope.
        NameScope localScope = new NameScope();
        assertEquals("s", nameGenerator.generateLocalName("s", localScope));
        assertEquals("s_1", nameGenerator.generateLocalName("s", localScope));

        // Local names between different scopes may be duplicate.
        localScope = new NameScope();
        assertEquals("s", nameGenerator.generateLocalName("s", localScope));
        assertEquals("s_1", nameGenerator.generateLocalName("s", localScope));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void globalAndLocalTest() {
        // Local names avoid pre-existing global names.
        assertEquals("s", nameGenerator.generateGlobalName("s", false));
        NameScope localScope = new NameScope();
        assertEquals("s_1", nameGenerator.generateLocalName("s", localScope));
        assertEquals("s_2", nameGenerator.generateLocalName("s", localScope));

        // Global scope should not use names of the local scope.
        assertEquals("s_3", nameGenerator.generateGlobalName("s", false));

    }
}
