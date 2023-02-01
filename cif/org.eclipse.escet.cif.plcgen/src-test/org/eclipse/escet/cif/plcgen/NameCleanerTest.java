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

import java.util.Locale;

import org.eclipse.escet.cif.plcgen.generators.NameSanitizer.NameCleaner;
import org.junit.Test;

/** Tests for the {@link NameCleaner} class. */
public class NameCleanerTest {
    /** Default name returned by the cleaner if nothing was available for the name. */
    private static final String DEFAULT_PREFIX = Character.toString(NameCleaner.DEFAULT_CHAR);

    /**
     * Run a {@link NameCleaner} test.
     *
     * @param input Input text to clean.
     * @param prefix Expected prefix output.
     * @param hasSuffix Whether a numeric suffix is expected.
     * @param numericSuffix Expected value of the numeric suffix (always exists).
     */
    private void runTest(String input, String prefix, boolean hasSuffix, int numericSuffix) {
        NameCleaner nc = new NameCleaner(input);
        assertEquals(prefix, nc.getNamePrefix());
        assertEquals(prefix.toLowerCase(Locale.US), nc.getLowerCaseNamePrefix());
        assertEquals(hasSuffix, nc.hasSuffix());
        assertEquals(numericSuffix, nc.getNumericSuffix());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void nameCleanerTest() {
        runTest("", DEFAULT_PREFIX, false, 0); // Empty string.
        runTest(".", DEFAULT_PREFIX, false, 0); // Garbage only.
        runTest("..", DEFAULT_PREFIX, false, 0); // Garbage only.
        runTest("Y", "Y", false, 0); // An upper-case name.
        runTest("y", "y", false, 0); // A lower-case name.
        runTest("..y", "y", false, 0); // Prefix garbage (is ignored).
        runTest("y,", "y", false, 0); // Suffix garbage (is ignored).
        runTest("123", DEFAULT_PREFIX, true, 123); // Only a number.
        runTest("..48", DEFAULT_PREFIX, true, 48); // Garbage and a number.
        runTest("piet12345678901234567890", "piet", true, 123456); // Name and a long number.
        runTest("48q", "x48q", false, 0); // Bad identifier.
        runTest("q.z48q21", "q_z48q", true, 21); // Garbage inside the name.
        runTest("q.48q21", "q_48q", true, 21); // Garbage between the name and the number.
        runTest("q48.q21", "q48_q", true, 21); // Garbage between the number and the name.
        runTest("q.48.21", "q_48_", true, 21); // Garbage inside the number.
    }
}
