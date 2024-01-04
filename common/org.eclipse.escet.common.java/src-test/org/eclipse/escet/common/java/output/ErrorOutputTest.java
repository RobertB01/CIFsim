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

/** Error output tests. */
public class ErrorOutputTest {
    /**
     * Add some output to the stream.
     *
     * @param stream Output stream to use.
     */
    private void useErrorStream(ErrorOutput stream) {
        stream.line("hello world");
        stream.line();
        stream.line(">%s<", 123);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBlackHoleOutput() {
        ErrorOutputProvider outputProvider = new BlackHoleOutputProvider();
        ErrorOutput err = outputProvider.getErrorOutput();
        useErrorStream(err);
        assertEquals("", outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEnabledStoredOutput() {
        ErrorOutputProvider outputProvider = new StoredOutputProvider();
        ErrorOutput err = outputProvider.getErrorOutput();
        useErrorStream(err);
        String expected = "ERROR: hello world\nERROR: \nERROR: >123<\n";
        assertEquals(expected, outputProvider.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testDisabledStoredOutput() {
        ErrorOutputProvider outputProvider = new StoredOutputProvider(false, false, false);
        ErrorOutput err = outputProvider.getErrorOutput();
        useErrorStream(err);
        String expected = "ERROR: hello world\nERROR: \nERROR: >123<\n";
        assertEquals(expected, outputProvider.toString());
    }
}
