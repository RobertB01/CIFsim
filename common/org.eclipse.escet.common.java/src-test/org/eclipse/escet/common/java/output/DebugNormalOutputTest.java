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
        DebugNormalOutput dbg = outputProvider.getDebugOutput("DBG::");
        WarnOutput warn = outputProvider.getWarnOutput();
        ErrorOutput err = outputProvider.getErrorOutput();
        out.line("normal");
        dbg.line("debug");
        out.inc();
        warn.line("warning follows indenting");
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
                DBG::debug
                    WARNING: warning follows indenting
                ERROR: error ignores indenting
                    out does indent
                    DBG::debug does indent
                        DBG::debug does more indent
                DBG::debug done
                """;
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMultiLineStoredOutputNoIndentNormal() {
        StoredOutputProvider provider = new StoredOutputProvider();
        DebugNormalOutput out = provider.getNormalOutput("PRE: ");
        out.line("a");
        out.line("b\nc");
        out.line("d\r\ne");
        out.line("f\n\ng");
        out.line("h\n\r\n\ni");
        out.line("j\nk\nl");
        String expected = """
                PRE: a
                PRE: b
                PRE: c
                PRE: d
                PRE: e
                PRE: f
                PRE:
                PRE: g
                PRE: h
                PRE:
                PRE:
                PRE: i
                PRE: j
                PRE: k
                PRE: l
                """;
        assertEquals(expected, provider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMultiLineStoredOutputNoIndentDebug() {
        StoredOutputProvider provider = new StoredOutputProvider();
        DebugNormalOutput dbg = provider.getDebugOutput("PRE: ");
        dbg.line("a");
        dbg.line("b\nc");
        dbg.line("d\r\ne");
        dbg.line("f\n\ng");
        dbg.line("h\n\r\n\ni");
        dbg.line("j\nk\nl");
        String expected = """
                PRE: a
                PRE: b
                PRE: c
                PRE: d
                PRE: e
                PRE: f
                PRE:
                PRE: g
                PRE: h
                PRE:
                PRE:
                PRE: i
                PRE: j
                PRE: k
                PRE: l
                """;
        assertEquals(expected, provider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMultiLineStoredOutputNoIndentWarn() {
        StoredOutputProvider provider = new StoredOutputProvider();
        WarnOutput warn = provider.getWarnOutput("PRE: ");
        warn.line("a");
        warn.line("b\nc");
        warn.line("d\r\ne");
        warn.line("f\n\ng");
        warn.line("h\n\r\n\ni");
        warn.line("j\nk\nl");
        String expected = """
                PRE: a
                PRE: b
                PRE: c
                PRE: d
                PRE: e
                PRE: f
                PRE:
                PRE: g
                PRE: h
                PRE:
                PRE:
                PRE: i
                PRE: j
                PRE: k
                PRE: l
                """;
        assertEquals(expected, provider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMultiLineStoredOutputNoIndentError() {
        StoredOutputProvider provider = new StoredOutputProvider();
        ErrorOutput err = provider.getErrorOutput("PRE: ");
        err.line("a");
        err.line("b\nc");
        err.line("d\r\ne");
        err.line("f\n\ng");
        err.line("h\n\r\n\ni");
        err.line("j\nk\nl");
        String expected = """
                PRE: a
                PRE: b
                PRE: c
                PRE: d
                PRE: e
                PRE: f
                PRE:
                PRE: g
                PRE: h
                PRE:
                PRE:
                PRE: i
                PRE: j
                PRE: k
                PRE: l
                """;
        assertEquals(expected, provider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMultiLineStoredOutputWithIndentNormal() {
        StoredOutputProvider provider = new StoredOutputProvider();
        DebugNormalOutput out = provider.getNormalOutput("PRE: ");
        out.line("a");
        out.inc();
        out.line("b\nc");
        out.inc();
        out.line("d\r\ne");
        out.line("f\n\ng");
        out.inc();
        out.line("h\n\r\n\ni");
        out.dec();
        out.line("j\nk\nl");
        String expected = """
                PRE: a
                    PRE: b
                    PRE: c
                        PRE: d
                        PRE: e
                        PRE: f
                        PRE:
                        PRE: g
                            PRE: h
                            PRE:
                            PRE:
                            PRE: i
                        PRE: j
                        PRE: k
                        PRE: l
                """;
        assertEquals(expected, provider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMultiLineStoredOutputWithIndentDebug() {
        StoredOutputProvider provider = new StoredOutputProvider();
        DebugNormalOutput dbg = provider.getDebugOutput("PRE: ");
        dbg.line("a");
        dbg.inc();
        dbg.line("b\nc");
        dbg.inc();
        dbg.line("d\r\ne");
        dbg.line("f\n\ng");
        dbg.inc();
        dbg.line("h\n\r\n\ni");
        dbg.dec();
        dbg.line("j\nk\nl");
        String expected = """
                PRE: a
                    PRE: b
                    PRE: c
                        PRE: d
                        PRE: e
                        PRE: f
                        PRE:
                        PRE: g
                            PRE: h
                            PRE:
                            PRE:
                            PRE: i
                        PRE: j
                        PRE: k
                        PRE: l
                """;
        assertEquals(expected, provider.toString());
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
        out.line("normal (with indent)");
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
