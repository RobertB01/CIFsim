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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeElementsChoiceText;
import static org.eclipse.escet.common.java.Strings.makeFixedLengthNumberText;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Unit tests for the methods of the {@link Strings} class. */
public class StringsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testFormatNoWrap() {
        String[] inputs = {"", "1", "12", "123", "1234", "12345"};
        for (int i = 0; i < inputs.length; i++) {
            String[] actual = Strings.wrap(5, inputs[i]);
            String[] expected = new String[] {inputs[i]};
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFormatWrap() {
        String[] inputs = {"12345 6", "1234 5", "12345 "};
        String[][] outputs = {{"12345", "6"}, {"1234", "5"}, {"12345", ""}};
        for (int i = 0; i < inputs.length; i++) {
            String[] actual = Strings.wrap(5, inputs[i]);
            String[] expected = outputs[i];
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFormatWrapNoSpace1() {
        assertThrows(IllegalArgumentException.class, () -> Strings.wrap(5, "123456789"));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFormatWrapNoSpace2() {
        assertThrows(IllegalArgumentException.class, () -> Strings.wrap(5, "123456"));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFormatWrapNoSpace3() {
        assertThrows(IllegalArgumentException.class, () -> Strings.wrap(5, "123456 "));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFormatWrapNoSpaceMsg() {
        try {
            Strings.wrap(5, "123456789");
            assertTrue(false, "Exception expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("No whitespace in \"123456\".", e.getMessage());
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testStringEscaping() {
        String[] inputs = {"a\nb", "c\td", "e\"f", "g\\h", "a\n", "c\t", "e\"", "g\\", "a\\nb", "c\\td", "e\\\"f",
                "g\\\\h", "a\\n", "c\\t", "e\\\"", "g\\\\", "a\\\nb", "c\\\td", "e\\\\\"f", "g\\\\\\h", "a\\\n",
                "c\\\t", "e\\\\\"", "g\\\\\\"};
        for (String s: inputs) {
            assertEquals(s, Strings.unescape(Strings.escape(s)));
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testStringEscapingInvalidEscapeSequence() {
        assertThrows(IllegalArgumentException.class, () -> Strings.unescape("\\k"));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testStringEscapingPrematureEnd() {
        assertThrows(IllegalArgumentException.class, () -> Strings.unescape("abc\\"));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSlice() {
        final boolean DEBUG = false;

        final String[][] EXPECTED_NEG = {
                // -5 = 0, -4 = 0, -3 = 1, -2 = 2, -1 = 3
                {"", "", "", "", ""}, // -5 = 0
                {"", "", "", "", ""}, // -4 = 0
                {"a", "a", "", "", ""}, // -3 = 1
                {"ab", "ab", "b", "", ""}, // -2 = 2
                {"abc", "abc", "bc", "c", ""}, // -1 = 3
                {"", "", "", "", ""}, // 0
                {"a", "a", "", "", ""}, // 1
                {"ab", "ab", "b", "", ""}, // 2
                {"abc", "abc", "bc", "c", ""}, // 3
                {"abcd", "abcd", "bcd", "cd", "d"}, // 4
                {"abcd", "abcd", "bcd", "cd", "d"}, // 5
                {"abcd", "abcd", "bcd", "cd", "d"}, // null=4
        };

        final String[][] EXPECTED_POS = {
                // 0, 1, 2, 3, 4, 5, null=0
                {"", "", "", "", "", "", ""}, // -5 = 0
                {"", "", "", "", "", "", ""}, // -4 = 0
                {"a", "", "", "", "", "", "a"}, // -3 = 1
                {"ab", "b", "", "", "", "", "ab"}, // -2 = 2
                {"abc", "bc", "c", "", "", "", "abc"}, // -1 = 3
                {"", "", "", "", "", "", ""}, // 0
                {"a", "", "", "", "", "", "a"}, // 1
                {"ab", "b", "", "", "", "", "ab"}, // 2
                {"abc", "bc", "c", "", "", "", "abc"}, // 3
                {"abcd", "bcd", "cd", "d", "", "", "abcd"}, // 4
                {"abcd", "bcd", "cd", "d", "", "", "abcd"}, // 5
                {"abcd", "bcd", "cd", "d", "", "", "abcd"}, // null=4
        };

        for (int b = -5; b <= 6; b++) {
            Integer beginIndex = (b == 6) ? null : b;
            for (int e = -5; e <= 6; e++) {
                Integer endIndex = (e == 6) ? null : e;

                String expected = (b < 0) ? EXPECTED_NEG[e + 5][b + 5] : EXPECTED_POS[e + 5][b];
                String actual = Strings.slice("abcd", beginIndex, endIndex);

                String msg = fmt("slice(\"abcd\", %s, %s)", beginIndex, endIndex);
                if (DEBUG) {
                    System.out.format("%s = \"%s\" %s= \"%s\"\n", msg, expected, expected.equals(actual) ? "" : "!",
                            actual);
                } else {
                    assertEquals(expected, actual, msg);
                }
            }
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSliceEmpty() {
        for (int b = -5; b <= 6; b++) {
            Integer beginIndex = (b == 6) ? null : b;
            for (int e = -5; e <= 6; e++) {
                Integer endIndex = (e == 6) ? null : e;

                String expected = "";
                String actual = Strings.slice("", beginIndex, endIndex);

                assertEquals(expected, actual);
            }
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIndent() {
        String text = "abc";
        assertEquals(text, Strings.indent(text, -100));
        assertEquals(text, Strings.indent(text, -10));
        assertEquals(text, Strings.indent(text, -1));
        assertEquals(text, Strings.indent(text, 0));
        assertEquals(" " + text, Strings.indent(text, 1));
        assertEquals("     " + text, Strings.indent(text, 5));

        String longIndent = Strings.indent(text, 95);
        assertTrue(longIndent.endsWith(text));
        assertEquals(95 + text.length(), longIndent.length());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSpaces() {
        assertEquals("", Strings.spaces(-10));
        assertEquals("", Strings.spaces(-1));
        assertEquals("", Strings.spaces(0));
        assertEquals(" ", Strings.spaces(1));
        assertEquals("   ", Strings.spaces(3));

        for (int length = -100; length < 120; length += 4) {
            String text = Strings.spaces(length);
            long expectedLength = (length <= 0) ? 0 : length;
            assertEquals(expectedLength, text.chars().count());
            assertTrue(text.chars().allMatch(v -> v == 32));
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMakeElementsChoiceTextNone() {
        assertThrows(AssertionError.class, () -> assertEquals("", makeElementsChoiceText(List.of(), null)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMakeElementsChoiceTextConverted() {
        assertEquals("**1**", makeElementsChoiceText(List.of(1), n -> fmt("**%d**", n)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMakeElementsChoiceTextSingle() {
        assertEquals("abc", makeElementsChoiceText(List.of("abc"), null));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMakeElementsChoiceTextDual() {
        assertEquals("abc or def", makeElementsChoiceText(List.of("abc", "def"), null));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMakeElementsChoiceTextTriple() {
        assertEquals("abc, def or ghi", makeElementsChoiceText(List.of("abc", "def", "ghi"), null));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMakeElementsChoiceTextQuadrupleWithSorting() {
        assertEquals("abc, def, ghi or jkl", makeElementsChoiceText(List.of("abc", "jkl", "ghi", "def"), null));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSplitLines() {
        String input = """
                a
                b\nc
                d\r\ne
                f\n\ng
                h\n\r\n\ni
                j\nk\nl
                """;
        String[] expected = {
                "a",
                "b",
                "c",
                "d",
                "e",
                "f",
                "",
                "g",
                "h",
                "",
                "",
                "i",
                "j",
                "k",
                "l",
        };
        assertArrayEquals(expected, Strings.splitLines(input));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTruncate() {
        assertEquals("", Strings.truncate("", 10));
        assertEquals("0123456789", Strings.truncate("0123456789", 10));
        assertEquals("0123456...", Strings.truncate("012345678910", 10));

        assertThrows(AssertionError.class, () -> assertEquals("", Strings.truncate("", 1)));
        assertThrows(AssertionError.class, () -> assertEquals("", Strings.truncate("", -10)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMakeFixedLengthNumberText() {
        assertEquals("0", makeFixedLengthNumberText(0, 0));
        assertEquals("0", makeFixedLengthNumberText(0, 9));
        assertEquals("00", makeFixedLengthNumberText(0, 10));
        assertEquals("09", makeFixedLengthNumberText(9, 10));
        assertEquals("10", makeFixedLengthNumberText(10, 10));

        assertEquals("099", makeFixedLengthNumberText(99, 100));
        assertEquals("100", makeFixedLengthNumberText(100, 100));

        assertEquals("123", makeFixedLengthNumberText(123, 200));
        assertEquals("002", makeFixedLengthNumberText(2, 200));
        assertEquals("099", makeFixedLengthNumberText(99, 200));
        assertEquals("199", makeFixedLengthNumberText(199, 200));
        assertEquals("200", makeFixedLengthNumberText(200, 200));

        int num = 1073741824; // '0.5' larger than 'MAX_VALUE / 2.0'.
        assertEquals(String.valueOf(num), makeFixedLengthNumberText(num, Integer.MAX_VALUE));
    }
}
