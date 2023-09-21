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

package org.eclipse.escet.common.java.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Debug and normal output tests. */
public class DebugNormalOutputTest {
    /**
     * Add some output to the stream.
     *
     * @param stream Output stream to use.
     */
    private void useWarnStream(DebugNormalOutput stream) {
        stream.line("hello world");
        stream.inc();
        stream.line();
        stream.line(">%s<", 123);
        stream.dec();
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDebugBlackHoleOutput() {
        DebugNormalOutputProvider outputProvider = new BlackHoleOutputProvider();
        DebugNormalOutput dbg = outputProvider.getDebugOutput();
        useWarnStream(dbg);
        String expected = "";
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDebugStoredOutput() {
        DebugNormalOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput dbg = outputProvider.getDebugOutput();
        useWarnStream(dbg);
        String expected = """
                hello world

                    >123<
                """;
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDisabledDebugStoredOutput() {
        DebugNormalOutputProvider outputProvider = new StoredOutputProvider(false, true, true);
        DebugNormalOutput dbg = outputProvider.getDebugOutput();
        useWarnStream(dbg);
        String expected = "";
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testNormalStoredOutput() {
        DebugNormalOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput out = outputProvider.getNormalOutput();
        useWarnStream(out);
        String expected = """
                hello world

                    >123<
                """;
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMultiIndentNormalStoredOutput() {
        DebugNormalOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput out = outputProvider.getNormalOutput();
        useWarnStream(out);
        out.inc();
        useWarnStream(out);
        out.dec();
        String expected = """
                hello world

                    >123<
                    hello world

                        >123<
                """;
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEmptyBlankOutput() {
        DebugNormalOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput out = outputProvider.getNormalOutput();
        out.line("Empty lines");
        out.line("");
        out.inc();
        out.line("");
        out.dec();
        out.line("Blank lines");
        out.line(" ");
        out.inc();
        out.line(" ");
        out.dec();
        String expected = "Empty lines\n\n\nBlank lines\n \n     \n";
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEnabledBadDec() {
        DebugNormalOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput out = outputProvider.getNormalOutput();
        assertThrows(AssertionError.class, () -> out.dec());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDisabledBadDec() {
        DebugNormalOutputProvider outputProvider = new StoredOutputProvider(true, false, true);
        DebugNormalOutput out = outputProvider.getNormalOutput();
        out.dec();
        String expected = "";
        assertEquals(expected, outputProvider.toString());
    }
}
