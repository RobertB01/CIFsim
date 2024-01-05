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

package org.eclipse.escet.setext.generator.scanner;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.setext.io.SeTextReader.parseRegEx;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.parser.ast.regex.RegEx;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Regular expression tests. */
public class RegExTest {
    @BeforeEach
    @SuppressWarnings("javadoc")
    public void before() {
        AppEnv.registerSimple();
        Options.set(OutputModeOption.class, OutputMode.ERROR);
    }

    @AfterEach
    @SuppressWarnings("javadoc")
    public void after() {
        AppEnv.unregisterApplication();
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testToString() {
        final TextPosition DUMMY_POS = TextPosition.createDummy("/dummy.file");
        final RegEx RE_EOF = new RegExChar(-1, DUMMY_POS);
        final RegEx RE_MARKER2 = new RegExChar(-2, DUMMY_POS);
        final RegEx RE_MARKER3 = new RegExChar(-3, DUMMY_POS);

        testToString(RE_EOF, "\u00B6");
        testToString(RE_MARKER2, "\u00AB-2\u00BB");
        testToString(RE_MARKER3, "\u00AB-3\u00BB");
        testToString(parseRegEx("a"), "a");
        testToString(parseRegEx("<"), "<");
        testToString(parseRegEx("\\\""), "\\\"");
        testToString(parseRegEx("\\n"), "\\n");
        testToString(parseRegEx("\\t"), "\\t");
        testToString(parseRegEx("\\r"), "\\r");
        testToString(parseRegEx("\\("), "\\(");
        testToString(parseRegEx("\\)"), "\\)");
        testToString(parseRegEx("\\*"), "\\*");
        testToString(parseRegEx("\\+"), "\\+");
        testToString(parseRegEx("\\?"), "\\?");
        testToString(parseRegEx("\\["), "\\[");
        testToString(parseRegEx("\\]"), "\\]");
        testToString(parseRegEx("\\^"), "\\^");
        testToString(parseRegEx("\\-"), "\\-");
        testToString(parseRegEx("\\|"), "\\|");
        testToString(parseRegEx("\\{"), "\\{");
        testToString(parseRegEx("\\}"), "\\}");

        testToString(parseRegEx("a|b"), "a|b");
        testToString(parseRegEx("a|b|c"), "a|b|c");
        testToString(parseRegEx("a|(b|c)"), "a|b|c");
        testToString(parseRegEx("(a|b)|c"), "a|b|c");

        testToString(parseRegEx("ab"), "ab");
        testToString(parseRegEx("abc"), "abc");
        testToString(parseRegEx("a(bc)"), "abc");
        testToString(parseRegEx("(ab)c"), "abc");

        testToString(parseRegEx("[ab]"), "[ab]");
        testToString(parseRegEx("[a\\|]"), "[a\\|]");
        testToString(parseRegEx("[^ab]"), "[^ab]");
        testToString(parseRegEx("[a-b]"), "[a-b]");
        testToString(parseRegEx("[a-z,/A-Z]"), "[a-z,/A-Z]");

        testToString(parseRegEx("."), ".");
        testToString(parseRegEx("a?"), "a?");
        testToString(parseRegEx("a??"), "a??");
        testToString(parseRegEx("a+"), "a+");
        testToString(parseRegEx("a++"), "a++");
        testToString(parseRegEx("a*"), "a*");
        testToString(parseRegEx("a**"), "a**");
        testToString(parseRegEx("a?+*"), "a?+*");

        testToString(parseRegEx("de|ef"), "de|ef");
        testToString(parseRegEx("a*b"), "a*b");
        testToString(parseRegEx("(a|b)(b|c)"), "(a|b)(b|c)");

        Map<String, String> shortcuts = map();
        shortcuts.put("s", "a");
        testToString(parseRegEx("{s}", shortcuts), "{s}");
    }

    /**
     * Test for {@link RegEx#toString}.
     *
     * @param regEx Regular expression to test.
     * @param expected Expected result.
     */
    private void testToString(RegEx regEx, String expected) {
        String actual = regEx.toString();
        assertEquals(expected, actual);
    }
}
