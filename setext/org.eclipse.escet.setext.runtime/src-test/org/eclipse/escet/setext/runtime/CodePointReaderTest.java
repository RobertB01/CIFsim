//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.eclipse.escet.common.java.Strings;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link CodePointReader} class. */
public class CodePointReaderTest {
    /** ASCII input, UTF-8 encoding. */
    @Test
    public void testValidAscii() {
        byte[] input = {0, 1, 2, 3, 4, 5};
        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        CodePointReader reader = new CodePointReader(stream, "UTF-8");
        try {
            assertEquals(0, reader.read());
            assertEquals(1, reader.read());
            assertEquals(2, reader.read());
            assertEquals(3, reader.read());
            assertEquals(4, reader.read());
            assertEquals(5, reader.read());
            assertEquals(-1, reader.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Supplementary Multilingual Plane input, UTF-8 encoding. */
    @Test
    public void testValidUtf8() {
        // U+10025 = LINEAR B SYLLABLE B021 QI
        int codePoint = 0x10024;
        String data = Strings.codePointToStr(codePoint);
        byte[] input;
        try {
            input = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        assertEquals(4, input.length);

        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        CodePointReader reader = new CodePointReader(stream, "UTF-8");

        try {
            assertEquals(codePoint, reader.read());
            assertEquals(-1, reader.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Supplementary Multilingual Plane input, UTF-16BE encoding. */
    @Test
    public void testValidUtf16() {
        // U+10025 = LINEAR B SYLLABLE B021 QI
        int codePoint = 0x10024;
        String data = Strings.codePointToStr(codePoint);
        byte[] input;
        try {
            input = data.getBytes("UTF-16BE");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        assertEquals(4, input.length);

        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        CodePointReader reader = new CodePointReader(stream, "UTF-16BE");

        try {
            assertEquals(codePoint, reader.read());
            assertEquals(-1, reader.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Invalid input, UTF-8 encoding. */
    @Test
    public void testInvalidUtf8() {
        // According to "The Unicode Standard, Version 6.1", page 95, "the
        // following byte values are disallowed in UTF-8: C0–C1, F5–FF."

        // According to http://unicode.org/glossary/#replacement_character,
        // U+FFFD is the 'Replacement character', which may be used as a
        // replacement for invalid input.

        byte[] input = {0, 1, (byte)0xFF};
        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        CodePointReader reader = new CodePointReader(stream, "UTF-8");
        try {
            assertEquals(0, reader.read());
            assertEquals(1, reader.read());
            assertEquals(0xFFFD, reader.read());
            assertEquals(-1, reader.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Test that repeatedly reads the end-of-file marker. */
    @Test
    public void testRepeatedlyReadEof() {
        byte[] input = {};
        ByteArrayInputStream stream = new ByteArrayInputStream(input);
        CodePointReader reader = new CodePointReader(stream);
        try {
            assertEquals(-1, reader.read());
            assertEquals(-1, reader.read());
            assertEquals(-1, reader.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
