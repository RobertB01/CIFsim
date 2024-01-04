//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.runtime.scanner;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.DummyApplication;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.io.MemAppStream;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.app.framework.output.StreamOutputComponent;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.Scanner;
import org.eclipse.escet.setext.runtime.Token;
import org.eclipse.escet.setext.runtime.exceptions.ScanException;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link Scanner} class. */
public class ScannerTest {
    /** Test various valid inputs. */
    @Test
    public void testValid() {
        test("b", "0=\"b\" @1:1-1:1, 4=<null> @1:2-1:2", true);
        test("b", "0=\"b\" @1:1-1:1, 4=<null> @1:2-1:2", false);

        test("aab", "0=\"aab\" @1:1-1:3, 4=<null> @1:4-1:4", true);
        test("aab", "0=\"aab\" @1:1-1:3, 4=<null> @1:4-1:4", false);

        test("abc", "1=\"abz\" @1:1-1:3, 4=<null> @1:4-1:4", true);
        test("abc", "1=\"abz\" @1:1-1:3, 4=<null> @1:4-1:4", false);

        test("abababcbc",
                "0=\"ab\" @1:1-1:2, 0=\"ab\" @1:3-1:4, 1=\"abz\" @1:5-1:7, 1=\"bz\" @1:8-1:9, 4=<null> @1:10-1:10",
                true);
        test("abababcbc",
                "0=\"ab\" @1:1-1:2, 0=\"ab\" @1:3-1:4, 1=\"abz\" @1:5-1:7, 1=\"bz\" @1:8-1:9, 4=<null> @1:10-1:10",
                false);

        test("bb\nb\n\nb", "0=\"b\" @1:1-1:1, 0=\"b\" @1:2-1:2, 0=\"b\" @2:1-2:1, 0=\"b\" @4:1-4:1, 4=<null> @4:2-4:2",
                true);
        test("bb\nb\n\nb", "0=\"b\" @1:1-1:1, 0=\"b\" @1:2-1:2, 2=\"\\n\" @1:3-1:3, 0=\"b\" @2:1-2:1, "
                + "2=\"\\n\\n\" @2:2-3:1, 0=\"b\" @4:1-4:1, 4=<null> @4:2-4:2", false);

        test("b/**/b", "0=\"b\" @1:1-1:1, 3=\"/*\" @1:2-1:3, 5=\"*/\" @1:4-1:5, 0=\"b\" @1:6-1:6, 4=<null> @1:7-1:7",
                true);
        test("b/**/b", "0=\"b\" @1:1-1:1, 3=\"/*\" @1:2-1:3, 5=\"*/\" @1:4-1:5, 0=\"b\" @1:6-1:6, 4=<null> @1:7-1:7",
                false);

        test("/*d\ne\n*/", "3=\"/*\" @1:1-1:2, 5=\"*/\" @3:1-3:2, 4=<null> @3:3-3:3", true);
        test("/*d\ne\n*/", "3=\"/*\" @1:1-1:2, 6=\"d\" @1:3-1:3, 7=\"\\n\" @1:4-1:4, 6=\"e\" @2:1-2:1, "
                + "7=\"\\n\" @2:2-2:2, 5=\"*/\" @3:1-3:2, 4=<null> @3:3-3:3", false);
    }

    /** "c" is not a valid start of a token. */
    @Test
    public void testStartMidToken() {
        for (boolean optimize: new boolean[] {false, true}) {
            try {
                test("c", null, optimize);
            } catch (ScanException e) {
                assertEquals('c', e.getCodePoint());
                assertEquals(0, e.getPosition().startOffset);
                assertEquals(1, e.getPosition().startLine);
                assertEquals(1, e.getPosition().startColumn);
                assertEquals(0, e.getPosition().endOffset);
                assertEquals(1, e.getPosition().endLine);
                assertEquals(1, e.getPosition().endColumn);
            }

            try {
                test("\nc", null, optimize);
            } catch (ScanException e) {
                assertEquals('c', e.getCodePoint());
                assertEquals(1, e.getPosition().startOffset);
                assertEquals(2, e.getPosition().startLine);
                assertEquals(1, e.getPosition().startColumn);
                assertEquals(1, e.getPosition().endOffset);
                assertEquals(2, e.getPosition().endLine);
                assertEquals(1, e.getPosition().endColumn);
            }
        }
    }

    /** "d" is invalid character, wherever it occurs. */
    @Test
    public void testInvalidChar() {
        for (boolean optimize: new boolean[] {false, true}) {
            try {
                test("d", null, optimize);
            } catch (ScanException e) {
                assertEquals('d', e.getCodePoint());
                assertEquals(0, e.getPosition().startOffset);
                assertEquals(1, e.getPosition().startLine);
                assertEquals(1, e.getPosition().startColumn);
                assertEquals(0, e.getPosition().endOffset);
                assertEquals(1, e.getPosition().endLine);
                assertEquals(1, e.getPosition().endColumn);
            }

            try {
                test("bd", null, optimize);
            } catch (ScanException e) {
                assertEquals('d', e.getCodePoint());
                assertEquals(1, e.getPosition().startOffset);
                assertEquals(1, e.getPosition().startLine);
                assertEquals(2, e.getPosition().startColumn);
                assertEquals(1, e.getPosition().endOffset);
                assertEquals(1, e.getPosition().endLine);
                assertEquals(2, e.getPosition().endColumn);
            }

            try {
                test("\nd", null, optimize);
            } catch (ScanException e) {
                assertEquals('d', e.getCodePoint());
                assertEquals(1, e.getPosition().startOffset);
                assertEquals(2, e.getPosition().startLine);
                assertEquals(1, e.getPosition().startColumn);
                assertEquals(1, e.getPosition().endOffset);
                assertEquals(2, e.getPosition().endLine);
                assertEquals(1, e.getPosition().endColumn);
            }

            try {
                test("b\nd", null, optimize);
            } catch (ScanException e) {
                assertEquals('d', e.getCodePoint());
                assertEquals(2, e.getPosition().startOffset);
                assertEquals(2, e.getPosition().startLine);
                assertEquals(1, e.getPosition().startColumn);
                assertEquals(2, e.getPosition().endOffset);
                assertEquals(2, e.getPosition().endLine);
                assertEquals(1, e.getPosition().endColumn);
            }

            try {
                test("b\nbd", null, optimize);
            } catch (ScanException e) {
                assertEquals('d', e.getCodePoint());
                assertEquals(3, e.getPosition().startOffset);
                assertEquals(2, e.getPosition().startLine);
                assertEquals(2, e.getPosition().startColumn);
                assertEquals(3, e.getPosition().endOffset);
                assertEquals(2, e.getPosition().endLine);
                assertEquals(2, e.getPosition().endColumn);
            }

            try {
                test("b\nb\n\nd", null, optimize);
            } catch (ScanException e) {
                assertEquals('d', e.getCodePoint());
                assertEquals(5, e.getPosition().startOffset);
                assertEquals(4, e.getPosition().startLine);
                assertEquals(1, e.getPosition().startColumn);
                assertEquals(5, e.getPosition().endOffset);
                assertEquals(4, e.getPosition().endLine);
                assertEquals(1, e.getPosition().endColumn);
            }
        }
    }

    /** "a" is partial (beginning of) token. Thus premature end-of-file. */
    @Test
    public void testPartialToken() {
        for (boolean optimize: new boolean[] {false, true}) {
            try {
                test("a", null, optimize);
            } catch (ScanException e) {
                assertEquals(-1, e.getCodePoint());
                assertEquals(1, e.getPosition().startOffset);
                assertEquals(1, e.getPosition().startLine);
                assertEquals(2, e.getPosition().startColumn);
                assertEquals(1, e.getPosition().endOffset);
                assertEquals(1, e.getPosition().endLine);
                assertEquals(2, e.getPosition().endColumn);
            }

            try {
                test("\na", null, optimize);
            } catch (ScanException e) {
                assertEquals(-1, e.getCodePoint());
                assertEquals(2, e.getPosition().startOffset);
                assertEquals(2, e.getPosition().startLine);
                assertEquals(2, e.getPosition().startColumn);
                assertEquals(2, e.getPosition().endOffset);
                assertEquals(2, e.getPosition().endLine);
                assertEquals(2, e.getPosition().endColumn);
            }
        }
    }

    /** Test scanner debug output. */
    @SuppressWarnings("unused")
    @Test
    public void testDebugOutput() {
        for (boolean optimize: new boolean[] {false, true}) {
            // Create in-memory stream to hold the debug output.
            MemAppStream stream = new MemAppStream();
            AppStreams streams = new AppStreams(System.in, stream, stream, stream);

            // Create dummy application. Registers the application with the
            // application framework as well.
            new DummyApplication(streams);

            // Register output component.
            IOutputComponent output = new StreamOutputComponent(stream, stream, stream);
            OutputProvider.register(output);

            try {
                // Enable output.
                Options.set(OutputModeOption.class, OutputMode.NORMAL);

                // Scan input.
                Scanner scanner = new TestScanner();
                String loc = "/dummy.file";
                try {
                    scanner.scanString("/*d\ne\n*/ababc", loc, DebugMode.SCANNER, optimize);
                } catch (IOException e) {
                    throw new RuntimeException("I/O error.", e);
                }

                // Get expected output.
                String resourcePath = getClass().getPackage().getName();
                resourcePath = resourcePath.replace('.', '/');
                resourcePath += "/" + getClass().getSimpleName() + ".out";
                InputStream expectedStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
                StringWriter writer = new StringWriter();
                try {
                    IOUtils.copy(expectedStream, writer, "UTF-8");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String expected = writer.toString().replace("\r", "");

                // Compare expected/actual.
                String actual = stream.toString().replace("\r", "");
                assertEquals(expected, actual);
            } finally {
                AppEnv.unregisterApplication();
            }
        }
    }

    /**
     * Test the scanner.
     *
     * @param input Input string.
     * @param expected Expected (formatted) tokens.
     * @param optimize Whether to optimize the scanner to skip tokens if they are not needed.
     */
    public static void test(String input, String expected, boolean optimize) {
        Scanner scanner = new TestScanner();
        String loc = "/dummy.file";
        List<Token> tokens;
        try {
            tokens = scanner.scanString(input, loc, DebugMode.NONE, optimize);
        } catch (IOException e) {
            throw new RuntimeException("I/O error.", e);
        }

        List<String> tokenTxts = list();
        for (Token token: tokens) {
            String posTxt;
            TextPosition pos = token.position;
            if (pos == null) {
                posTxt = "";
            } else {
                posTxt = pos.startLine + ":" + pos.startColumn + "-" + pos.endLine + ":" + pos.endColumn;
            }
            tokenTxts.add(fmt("%d=%s @%s", token.id, Strings.stringToJava(token.text), posTxt));
        }

        String actual = String.join(", ", tokenTxts);
        assertEquals(expected, actual);
    }

    /** Test {@link StringReader}. */
    @Test
    public void testStringReader() {
        // U+10025 = LINEAR B SYLLABLE B021 QI
        int codePoint = 0x10025;
        String str = Strings.codePointToStr(codePoint);
        assertEquals(2, str.length());

        StringReader reader = new StringReader(str);
        try {
            char high = (char)reader.read();
            char low = (char)reader.read();
            int eof = reader.read();

            assertEquals(str.charAt(0), high);
            assertEquals(str.charAt(1), low);

            assertEquals(true, Character.isSurrogatePair(high, low));
            assertEquals(codePoint, Character.toCodePoint(high, low));

            assertEquals(-1, eof);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
