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

import org.junit.jupiter.api.Test;

/** Warning output tests. */
public class WarnOutputTest {
    /**
     * Add some output to the stream.
     *
     * @param stream Output stream to use.
     */
    private void useWarnStream(WarnOutput stream) {
        stream.line("hello world");
        stream.line();
        stream.line(">%s<", 123);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBlackHoleOutput() {
        WarnOutputProvider outputProvider = new BlackHoleOutputProvider();
        WarnOutput warn = outputProvider.getWarnOutput();
        useWarnStream(warn);
        assertEquals("", outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEnabledStoredOutput() {
        WarnOutputProvider outputProvider = new StoredOutputProvider();
        WarnOutput warn = outputProvider.getWarnOutput();
        useWarnStream(warn);
        String expected = "WARNING: hello world\nWARNING:\nWARNING: >123<\n";
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDisabledStoredOutput() {
        WarnOutputProvider outputProvider = new StoredOutputProvider(true, true, false);
        WarnOutput warn = outputProvider.getWarnOutput();
        useWarnStream(warn);
        String expected = "";
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIndentedStoredOutput() {
        StoredOutputProvider outputProvider = new StoredOutputProvider(true, true, true);
        DebugNormalOutput out = outputProvider.getNormalOutput();
        WarnOutput warn = outputProvider.getWarnOutput();

        out.line("non-indented output");
        out.inc();
        out.line("indented output");
        warn.line("indented warning");
        out.dec();
        out.line("non-indented output");
        warn.line("non-indented warning");
        String expected = """
                non-indented output
                    indented output
                    WARNING: indented warning
                non-indented output
                WARNING: non-indented warning
                """;
        assertEquals(expected, outputProvider.toString());
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
}
