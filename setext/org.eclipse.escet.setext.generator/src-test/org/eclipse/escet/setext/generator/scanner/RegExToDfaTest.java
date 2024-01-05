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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.setext.generator.scanner.RegExToDfa.firstpos;
import static org.eclipse.escet.setext.generator.scanner.RegExToDfa.lastpos;
import static org.eclipse.escet.setext.generator.scanner.RegExToDfa.nullable;
import static org.eclipse.escet.setext.generator.scanner.RegExToDfa.simplify;
import static org.eclipse.escet.setext.io.SeTextReader.parseRegEx;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.parser.ast.regex.RegEx;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Units tests for the {@link RegExToDfa} class. */
public class RegExToDfaTest {
    /** Dummy position information for regular expressions. */
    static final TextPosition DUMMY_POS = TextPosition.createDummy("/dummy.file");

    /** {@code @eof} regular expression. */
    private static final RegEx RE_EOF = new RegExChar(-1, DUMMY_POS);

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
    public void testSimplify() {
        testSimplify(RE_EOF, "\u00B6");
        testSimplify(parseRegEx("a"), "a");
        testSimplify(parseRegEx("a?"), "a?");
        testSimplify(parseRegEx("a|b"), "a|b");
        testSimplify(parseRegEx("a|b|c"), "a|b|c");
        testSimplify(parseRegEx("a|(b|c)"), "a|b|c");
        testSimplify(parseRegEx("(a|b)|c"), "a|b|c");
        testSimplify(parseRegEx("a?|b"), "a?|b");
        testSimplify(parseRegEx("a|b?"), "a|b?");
        testSimplify(parseRegEx("a?|b?"), "a?|b?");
        testSimplify(parseRegEx("ab"), "ab");
        testSimplify(parseRegEx("a?b"), "a?b");
        testSimplify(parseRegEx("ab?"), "ab?");
        testSimplify(parseRegEx("a?b?"), "a?b?");
        testSimplify(parseRegEx("a*b?"), "a*b?");
        testSimplify(parseRegEx("a*"), "a*");
        testSimplify(parseRegEx("a*b*"), "a*b*");
        testSimplify(parseRegEx("a*?"), "a*?");
        testSimplify(parseRegEx("a**"), "a**");
        testSimplify(parseRegEx("a*b"), "a*b");

        testSimplify(parseRegEx("a+"), "aa*");
        testSimplify(parseRegEx("a++"), "aa*(aa*)*");
        testSimplify(parseRegEx("[ab]+"), "(a|b)(a|b)*");

        testSimplify(parseRegEx("[a-b]"), "a|b");
        testSimplify(parseRegEx("[^\u0001-\u007f]"), "\u0000");
        testSimplify(parseRegEx("[^\u0002-\u007f]"), "\u0000|\u0001");

        String simplifiedDot = "";
        for (int i = 0; i <= 127; i++) {
            if (i == '\n') {
                continue;
            }
            if (!simplifiedDot.isEmpty()) {
                simplifiedDot += "|";
            }
            RegExChar c = new RegExChar(i, DUMMY_POS);
            simplifiedDot += c.toString();
        }
        testSimplify(parseRegEx("."), simplifiedDot);

        Map<String, String> shortcuts = map();
        shortcuts.put("s", "a");
        testSimplify(parseRegEx("{s}", shortcuts), "a");
    }

    /**
     * Test for {@link RegExToDfa#simplify}.
     *
     * @param regEx Regular expression to test.
     * @param expected Expected result.
     */
    private void testSimplify(RegEx regEx, String expected) {
        regEx = simplify(regEx);
        String actual = regEx.toString();
        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testNullable() {
        testNullable(RE_EOF, false);
        testNullable(parseRegEx("a"), false);
        testNullable(parseRegEx("a?"), true);
        testNullable(parseRegEx("a|b"), false);
        testNullable(parseRegEx("a|b|c"), false);
        testNullable(parseRegEx("a|(b|c)"), false);
        testNullable(parseRegEx("(a|b)|c"), false);
        testNullable(parseRegEx("a?|b"), true);
        testNullable(parseRegEx("a|b?"), true);
        testNullable(parseRegEx("a?|b?"), true);
        testNullable(parseRegEx("ab"), false);
        testNullable(parseRegEx("a?b"), false);
        testNullable(parseRegEx("ab?"), false);
        testNullable(parseRegEx("a?b?"), true);
        testNullable(parseRegEx("a*b?"), true);
        testNullable(parseRegEx("a*"), true);
        testNullable(parseRegEx("a*b*"), true);
        testNullable(parseRegEx("a*?"), true);
        testNullable(parseRegEx("a**"), true);
        testNullable(parseRegEx("a*b"), false);
        testNullable(parseRegEx("ab*"), false);
    }

    /**
     * Test for {@link RegExToDfa#nullable}.
     *
     * @param regEx Regular expression to test.
     * @param expected Expected result.
     */
    private void testNullable(RegEx regEx, boolean expected) {
        regEx = simplify(regEx);
        boolean actual = nullable(regEx);
        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFirst() {
        testFirst(RE_EOF, "{\u00B6}");
        testFirst(parseRegEx("a"), "{a}");
        testFirst(parseRegEx("a?"), "{a}");
        testFirst(parseRegEx("a|b"), "{a, b}");
        testFirst(parseRegEx("a|b|c"), "{a, b, c}");
        testFirst(parseRegEx("a|(b|c)"), "{a, b, c}");
        testFirst(parseRegEx("(a|b)|c"), "{a, b, c}");
        testFirst(parseRegEx("a?|b"), "{a, b}");
        testFirst(parseRegEx("a|b?"), "{a, b}");
        testFirst(parseRegEx("a?|b?"), "{a, b}");
        testFirst(parseRegEx("ab"), "{a}");
        testFirst(parseRegEx("a?b"), "{a, b}");
        testFirst(parseRegEx("ab?"), "{a}");
        testFirst(parseRegEx("a?b?"), "{a, b}");
        testFirst(parseRegEx("a*b?"), "{a, b}");
        testFirst(parseRegEx("a*"), "{a}");
        testFirst(parseRegEx("a*b*"), "{a, b}");
        testFirst(parseRegEx("a*?"), "{a}");
        testFirst(parseRegEx("a**"), "{a}");
        testFirst(parseRegEx("a*b"), "{a, b}");
        testFirst(parseRegEx("ab*"), "{a}");
    }

    /**
     * Test for {@link RegExToDfa#firstpos}.
     *
     * @param regEx Regular expression to test.
     * @param expected Expected result.
     */
    private void testFirst(RegEx regEx, String expected) {
        regEx = simplify(regEx);
        Set<RegExChar> actualSet = firstpos(regEx);
        String actual = set2str(actualSet);
        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testLast() {
        testLast(RE_EOF, "{\u00B6}");
        testLast(parseRegEx("a"), "{a}");
        testLast(parseRegEx("a?"), "{a}");
        testLast(parseRegEx("a|b"), "{a, b}");
        testLast(parseRegEx("a|b|c"), "{a, b, c}");
        testLast(parseRegEx("a|(b|c)"), "{a, b, c}");
        testLast(parseRegEx("(a|b)|c"), "{a, b, c}");
        testLast(parseRegEx("a?|b"), "{a, b}");
        testLast(parseRegEx("a|b?"), "{a, b}");
        testLast(parseRegEx("a?|b?"), "{a, b}");
        testLast(parseRegEx("ab"), "{b}");
        testLast(parseRegEx("a?b"), "{b}");
        testLast(parseRegEx("ab?"), "{a, b}");
        testLast(parseRegEx("a?b?"), "{a, b}");
        testLast(parseRegEx("a*b?"), "{a, b}");
        testLast(parseRegEx("a*"), "{a}");
        testLast(parseRegEx("a*b*"), "{a, b}");
        testLast(parseRegEx("a*?"), "{a}");
        testLast(parseRegEx("a**"), "{a}");
        testLast(parseRegEx("a*b"), "{b}");
        testLast(parseRegEx("ab*"), "{a, b}");
    }

    /**
     * Test for {@link RegExToDfa#lastpos}.
     *
     * @param regEx Regular expression to test.
     * @param expected Expected result.
     */
    private void testLast(RegEx regEx, String expected) {
        regEx = simplify(regEx);
        Set<RegExChar> actualSet = lastpos(regEx);
        String actual = set2str(actualSet);
        assertEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFollow() {
        testFollow(RE_EOF, "{\u00B6: {}}");
        testFollow(parseRegEx("a"), "{a: {}}");
        testFollow(parseRegEx("ab"), "{a: {b}, b: {}}");
        testFollow(parseRegEx("abc"), "{a: {b}, b: {c}, c: {}}");
        testFollow(parseRegEx("a?"), "{a: {}}");
        testFollow(parseRegEx("a?b"), "{a: {b}, b: {}}");
        testFollow(parseRegEx("a?b?"), "{a: {b}, b: {}}");
        testFollow(parseRegEx("ab?c"), "{a: {b, c}, b: {c}, c: {}}");
        testFollow(parseRegEx("a?b?c"), "{a: {b, c}, b: {c}, c: {}}");
        testFollow(parseRegEx("a*"), "{a: {a}}");
        testFollow(parseRegEx("a*b"), "{a: {a, b}, b: {}}");
        testFollow(parseRegEx("a*b?"), "{a: {a, b}, b: {}}");
        testFollow(parseRegEx("ab*c"), "{a: {b, c}, b: {b, c}, c: {}}");
        testFollow(parseRegEx("a*b*c"), "{a: {a, b, c}, b: {b, c}, c: {}}");
        testFollow(parseRegEx("a|b"), "{a: {}, b: {}}");
        testFollow(parseRegEx("a|b|c"), "{a: {}, b: {}, c: {}}");
        testFollow(parseRegEx("a|(b|c)"), "{a: {}, b: {}, c: {}}");
        testFollow(parseRegEx("(a|b)|c"), "{a: {}, b: {}, c: {}}");
        testFollow(parseRegEx("[ab]*c"), "{a: {a, b, c}, b: {a, b, c}, c: {}}");
        testFollow(parseRegEx("ab|ac"), "{a: {b}, a: {c}, b: {}, c: {}}");
        testFollow(parseRegEx("a(b?|c)d"), "{a: {b, c, d}, b: {d}, c: {d}, d: {}}");
    }

    /**
     * Test for {@link RegExToDfa#followpos}.
     *
     * @param regEx Regular expression to test.
     * @param expected Expected result.
     */
    private void testFollow(RegEx regEx, String expected) {
        // Simplify to allow extended regular expressions.
        regEx = simplify(regEx);

        // Calculate follow sets.
        RegExToDfa converter = new RegExToDfa();
        converter.calcFollowpos(regEx);

        // Convert follow sets to string.
        Set<RegExChar> chars = regEx.getChars();
        List<String> txts = list();
        for (RegExChar c: chars) {
            Set<RegExChar> followSet = converter.followpos(c);
            txts.add(c.toString() + ": " + set2str(followSet));
        }
        Collections.sort(txts, Strings.SORTER);
        String actual = "{" + String.join(", ", txts) + "}";

        // Check actual against expected.
        assertEquals(expected, actual);
    }

    /**
     * Converts a set of regular expression to a deterministic textual representation.
     *
     * @param s The set of regular expressions.
     * @return The deterministic textual representation.
     */
    private String set2str(Set<? extends RegEx> s) {
        List<String> txts = listc(s.size());
        for (RegEx re: s) {
            txts.add(re.toString());
        }
        Collections.sort(txts, Strings.SORTER);
        return "{" + String.join(", ", txts) + "}";
    }
}
