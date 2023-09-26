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
    private void useStream(DebugNormalOutput stream) {
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
        useStream(dbg);
        String expected = "";
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDebugStoredOutput() {
        DebugNormalOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput dbg = outputProvider.getDebugOutput();
        useStream(dbg);
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
        useStream(dbg);
        String expected = "";
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testNormalStoredOutput() {
        DebugNormalOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput out = outputProvider.getNormalOutput();
        useStream(out);
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
        useStream(out);
        out.inc();
        useStream(out);
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
    public void testMultiStreamStoredOutput() {
        StoredOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput out = outputProvider.getNormalOutput();
        DebugNormalOutput dbg = outputProvider.getDebugOutput();
        WarnOutput warn = outputProvider.getWarnOutput();
        ErrorOutput err = outputProvider.getErrorOutput();
        out.line("normal");
        dbg.line("debug");
        out.inc();
        warn.line("warning ignores indenting");
        err.line("error ignores indenting");
        out.line("out does indent");
        dbg.line("debug does indent");
        dbg.inc();
        dbg.line("debug does more indent");
        dbg.dec();
        out.dec();
        dbg.line("debug done");
        String expected = """
                normal
                debug
                WARNING: warning ignores indenting
                ERROR: error ignores indenting
                    out does indent
                    debug does indent
                        debug does more indent
                debug done
                """;
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIncorrectDedentWithDebug() {
        StoredOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput out = outputProvider.getNormalOutput();
        DebugNormalOutput dbg = outputProvider.getDebugOutput();
        out.line("normal");
        out.inc();
        dbg.line("debug (with indent)");
        assertThrows(AssertionError.class, () -> dbg.dec());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIncorrectDedentWithNormal() {
        StoredOutputProvider outputProvider = new StoredOutputProvider();
        DebugNormalOutput out = outputProvider.getNormalOutput();
        DebugNormalOutput dbg = outputProvider.getDebugOutput();
        dbg.line("debug");
        dbg.inc();
        out.line("noral (with indent)");
        assertThrows(AssertionError.class, () -> out.dec());
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
