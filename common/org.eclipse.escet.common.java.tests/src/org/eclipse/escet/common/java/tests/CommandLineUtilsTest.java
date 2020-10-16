//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.eclipse.escet.common.java.CommandLineUtils;
import org.junit.Test;

/** Unit tests for the {@link CommandLineUtils} class. */
public class CommandLineUtilsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testParseArgs() {
        // No arguments.
        test("", new String[0], "");
        test(" ", new String[0], "");
        test("  ", new String[0], "");
        test(" \t ", new String[0], "");
        test(" \t\n\f ", new String[0], "");

        // No quoting.
        test("a", new String[] {"a"}, "a");
        test("ab", new String[] {"ab"}, "ab");
        test("a b", new String[] {"a", "b"}, "a b");
        test(" a  b ", new String[] {"a", "b"}, "a b");

        // Entire argument quoting.
        test("'a' b", new String[] {"a", "b"}, "a b");
        test("\"a\" b", new String[] {"a", "b"}, "a b");

        // Entire argument quoting, in parts.
        test("\"a\"\"b\"", new String[] {"ab"}, "ab");
        test("'a\'\"b\"", new String[] {"ab"}, "ab");
        test("\"a\"'b'", new String[] {"ab"}, "ab");
        test("'a b'", new String[] {"a b"}, "\"a b\"");
        test("\"a b\"", new String[] {"a b"}, "\"a b\"");

        // Partial argument quoting.
        test("a'b'c", new String[] {"abc"}, "abc");
        test("a\"b\"c", new String[] {"abc"}, "abc");
        test("a'b'", new String[] {"ab"}, "ab");
        test("a\"b\"", new String[] {"ab"}, "ab");
        test("'b'c", new String[] {"bc"}, "bc");
        test("\"b\"c", new String[] {"bc"}, "bc");

        // Quotes inside quotes.
        test("'a\\'b'", new String[] {"a'b"}, "a\\'b");
        test("\"a\\\"b\"", new String[] {"a\"b"}, "a\\\"b");
        test("\"a'b\"", new String[] {"a'b"}, "a\\'b");
        test("'a\"b'", new String[] {"a\"b"}, "a\\\"b");

        // Whitespace and quotes inside quotes.
        test("'a\\' b'", new String[] {"a' b"}, "\"a' b\"");
        test("\"a\\\" b\"", new String[] {"a\" b"}, "\"a\\\" b\"");
        test("\"a' b\"", new String[] {"a' b"}, "\"a' b\"");
        test("'a\" b'", new String[] {"a\" b"}, "\"a\\\" b\"");

        // Escaping outside quoted parts.
        test("a\\'b", new String[] {"a'b"}, "a\\'b");
        test("a\\\"b", new String[] {"a\"b"}, "a\\\"b");
        test("a\\\\b", new String[] {"a\\b"}, "a\\\\b");
        test("a\\xb", new String[] {"axb"}, "axb");
        test("a\\ b", new String[] {"a b"}, "\"a b\"");
        test("a\\\tb", new String[] {"a\tb"}, "\"a\tb\"");

        // Escaping inside single quoted parts.
        test("'a\\'b'", new String[] {"a'b"}, "a\\'b");
        test("'a\\\"b'", new String[] {"a\"b"}, "a\\\"b");
        test("'a\\\\b'", new String[] {"a\\b"}, "a\\\\b");
        test("'a\\xb'", new String[] {"axb"}, "axb");
        test("'a\\ b'", new String[] {"a b"}, "\"a b\"");
        test("'a\\\tb'", new String[] {"a\tb"}, "\"a\tb\"");

        // Escaping inside double quoted parts.
        test("\"a\\'b\"", new String[] {"a'b"}, "a\\'b");
        test("\"a\\\"b\"", new String[] {"a\"b"}, "a\\\"b");
        test("\"a\\\\b\"", new String[] {"a\\b"}, "a\\\\b");
        test("\"a\\xb\"", new String[] {"axb"}, "axb");
        test("\"a\\ b\"", new String[] {"a b"}, "\"a b\"");
        test("\"a\\\tb\"", new String[] {"a\tb"}, "\"a\tb\"");
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("javadoc")
    public void testParseArgsMissingSingleQuote() {
        test("\'a", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("javadoc")
    public void testParseArgsMissingDoubleQuote() {
        test("\"a", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("javadoc")
    public void testParseArgsMissingEscapeChar() {
        test("\\", null, null);
    }

    /**
     * Tests the {@link CommandLineUtils#parseArgs} and {@link CommandLineUtils#combineArgs} methods.
     *
     * @param txt The arguments text to parse.
     * @param expectedArgs The expected parse result.
     * @param expectedCombi The expected combined text.
     */
    private void test(String txt, String[] expectedArgs, String expectedCombi) {
        // Parse.
        String[] actualArgs = CommandLineUtils.parseArgs(txt);
        assertArrayEquals(expectedArgs, actualArgs);

        // Combine.
        String actualCombi = CommandLineUtils.combineArgs(actualArgs);
        assertEquals(expectedCombi, actualCombi);

        // Parse again.
        actualArgs = CommandLineUtils.parseArgs(actualCombi);
        assertArrayEquals(expectedArgs, actualArgs);

        // Combine again.
        actualCombi = CommandLineUtils.combineArgs(actualArgs);
        assertEquals(expectedCombi, actualCombi);
    }
}
