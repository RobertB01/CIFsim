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

import static org.eclipse.escet.common.java.FormatDescription.Conversion.ERROR;
import static org.eclipse.escet.common.java.FormatDescription.Conversion.INTEGER;
import static org.eclipse.escet.common.java.FormatDescription.Conversion.LITERAL;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.junit.Test;

/** Unit tests for the {@link FormatDecoder} class. */
public class FormatDecoderTest {
    /** Expected format descriptions. */
    private List<FormatDescription> expecteds = list();

    /**
     * Tests the given text against the {@link #expecteds}.
     *
     * @param text The text to decode.
     */
    private void test(String text) {
        List<FormatDescription> actuals = new FormatDecoder().decode(text);
        if (expecteds.size() != actuals.size()) {
            String msg = fmt("test(%s): expected: %s (size %d), actual: %s (size %d)", text, expecteds,
                    expecteds.size(), actuals, actuals.size());
            System.err.println(msg);
            assertEquals(expecteds.size(), actuals.size());
        }
        for (int i = 0; i < expecteds.size(); i++) {
            if (!expecteds.get(i).equals(actuals.get(i))) {
                String msg = fmt("test(%s): expected: %s (index %d = %s), actual: %s (index %d = %s)", text, expecteds,
                        i, expecteds.get(i), actuals, i, actuals.get(i));
                System.err.println(msg);
                assertEquals(expecteds.get(i), actuals.get(i));
            }
        }
    }

    /**
     * Adds an error to the expected output.
     *
     * @param msg The expected error message.
     * @param offset The expected 0-based offset.
     * @param length The expected length.
     */
    private void err(String msg, int offset, int length) {
        expecteds.add(new FormatDescription(ERROR, null, null, msg, null, null, offset, length));
    }

    /**
     * Adds a literal to the expected output.
     *
     * @param txt The literal text.
     * @param offset The expected 0-based offset.
     * @param length The expected length.
     */
    private void literal(String txt, int offset, int length) {
        expecteds.add(new FormatDescription(LITERAL, null, null, txt, null, null, offset, length));
    }

    /**
     * Returns all combinations of all the formatting flags. Since we have 5 flags, and all can be either enabled or
     * disabled, this results in 32 combinations. This includes invalid combinations. The order of the different flags
     * is fixed.
     *
     * @return All combinations of all the formatting flags.
     */
    private String[] getAllFlags() {
        // Get flags, and corresponding bit representation.
        String[] flags = {"-", "+", " ", "0", ","};
        int[] bits = new int[flags.length];
        for (int i = 0; i < flags.length; i++) {
            bits[i] = 1 << i;
        }

        // Generate all combinations.
        int cnt = 1 << flags.length;
        String[] rslt = new String[cnt];
        for (int i = 0; i < cnt; i++) {
            String combi = "";
            for (int j = 0; j < flags.length; j++) {
                if ((i & bits[j]) > 0) {
                    combi += flags[j];
                }
            }
            rslt[i] = combi;
        }
        return rslt;
    }

    /**
     * Returns all tests, for combinations with(out) an explicit index, with(out) each of the flags, with(out) a width,
     * with(out) a precision, and with all different conversion characters. Includes invalid tests.
     *
     * @return All tests.
     * @see #getAllFlags
     */
    private String[] getAllTests() {
        String[] indexAlts = {"", "1$"};
        String[] flagsAlts = getAllFlags();
        String[] widthAlts = {"", "5"};
        String[] precisionAlts = {"", ".3"};
        String[] conversionAlts = {"b", "B", "d", "x", "X", "e", "E", "f", "g", "G", "s", "S"};

        List<String> rslt = list();
        for (String index: indexAlts) {
            for (String flags: flagsAlts) {
                for (String width: widthAlts) {
                    for (String precision: precisionAlts) {
                        for (String conv: conversionAlts) {
                            String test = "%" + index + flags + width + precision + conv;
                            rslt.add(test);
                        }
                    }
                }
            }
        }
        return rslt.toArray(new String[0]);
    }

    /** Test formatted output versus simple output. */
    @Test
    public void testSimple() {
        String[] tests = getAllTests();

        TESTS:
        for (String test: tests) {
            // Decode and skip invalid tests.
            List<FormatDescription> fds = new FormatDecoder().decode(test);
            for (FormatDescription fd: fds) {
                if (fd.conversion == ERROR) {
                    continue TESTS;
                }
            }

            // Get value.
            Object[] values = new Object[fds.size()];
            for (int i = 0; i < fds.size(); i++) {
                FormatDescription fd = fds.get(i);
                switch (fd.conversion) {
                    case BOOLEAN:
                        values[i] = true;
                        break;
                    case INTEGER:
                        values[i] = 1234;
                        break;
                    case REAL:
                        values[i] = 1.23;
                        break;
                    case STRING:
                        values[i] = "ab";
                        break;

                    case LITERAL:
                    case ERROR:
                        throw new RuntimeException("Unexpected: " + fd);
                }
            }

            // Full pattern.
            String rsltFull = fmt(test, values);

            // Parts.
            String rsltParts = "";
            for (int i = 0; i < fds.size(); i++) {
                rsltParts += fmt(fds.get(i).toString(), values[i]);
            }

            // Simple.
            String rsltSimple = "";
            for (int i = 0; i < fds.size(); i++) {
                FormatDescription fd = fds.get(i);
                if (fd.isSimple()) {
                    rsltSimple += values[i].toString();
                } else {
                    rsltSimple += fmt(fds.get(i).toString(), values[i]);
                }
            }

            // Check same results.
            assertEquals(rsltFull, rsltParts);
            assertEquals(rsltFull, rsltSimple);
        }
    }

    /** Test 'b' and 'B'. */
    @Test
    public void testBool() {
        expecteds.clear();
        err("Invalid format specifier: a \"%b\" specifier can not have a \"+\" flag.", 0, 3);
        test("%+b");

        expecteds.clear();
        err("Invalid format specifier: a \"%b\" specifier can not have a \" \" flag.", 0, 3);
        test("% b");

        expecteds.clear();
        err("Invalid format specifier: a \"%b\" specifier can not have a \"0\" flag.", 0, 5);
        test("%010b");

        expecteds.clear();
        err("Invalid format specifier: a \"%b\" specifier can not have a \",\" flag.", 0, 3);
        test("%,b");

        expecteds.clear();
        err("Invalid format specifier: a \"%b\" specifier can not have a formatting precision.", 0, 4);
        test("%.3b");

        ///

        expecteds.clear();
        err("Invalid format specifier: a \"%B\" specifier can not have a \"+\" flag.", 0, 3);
        test("%+B");

        expecteds.clear();
        err("Invalid format specifier: a \"%B\" specifier can not have a \" \" flag.", 0, 3);
        test("% B");

        expecteds.clear();
        err("Invalid format specifier: a \"%B\" specifier can not have a \"0\" flag.", 0, 5);
        test("%010B");

        expecteds.clear();
        err("Invalid format specifier: a \"%B\" specifier can not have a \",\" flag.", 0, 3);
        test("%,B");

        expecteds.clear();
        err("Invalid format specifier: a \"%B\" specifier can not have a formatting precision.", 0, 4);
        test("%.3B");
    }

    /** Test 's' and 'S'. */
    @Test
    public void testString() {
        expecteds.clear();
        err("Invalid format specifier: a \"%s\" specifier can not have a \"+\" flag.", 0, 3);
        test("%+s");

        expecteds.clear();
        err("Invalid format specifier: a \"%s\" specifier can not have a \" \" flag.", 0, 3);
        test("% s");

        expecteds.clear();
        err("Invalid format specifier: a \"%s\" specifier can not have a \"0\" flag.", 0, 5);
        test("%010s");

        expecteds.clear();
        err("Invalid format specifier: a \"%s\" specifier can not have a \",\" flag.", 0, 3);
        test("%,s");

        expecteds.clear();
        err("Invalid format specifier: a \"%s\" specifier can not have a formatting precision.", 0, 4);
        test("%.3s");

        ///

        expecteds.clear();
        err("Invalid format specifier: a \"%S\" specifier can not have a \"+\" flag.", 0, 3);
        test("%+S");

        expecteds.clear();
        err("Invalid format specifier: a \"%S\" specifier can not have a \" \" flag.", 0, 3);
        test("% S");

        expecteds.clear();
        err("Invalid format specifier: a \"%S\" specifier can not have a \"0\" flag.", 0, 5);
        test("%010S");

        expecteds.clear();
        err("Invalid format specifier: a \"%S\" specifier can not have a \",\" flag.", 0, 3);
        test("%,S");

        expecteds.clear();
        err("Invalid format specifier: a \"%S\" specifier can not have a formatting precision.", 0, 4);
        test("%.3S");
    }

    /** Test 'd'. */
    @Test
    public void testIntDec() {
        err("Invalid format specifier: a \"%d\" specifier can not have a formatting precision.", 0, 4);
        test("%.3d");
    }

    /** Test 'x' and 'x'. */
    @Test
    public void testIntHex() {
        expecteds.clear();
        err("Invalid format specifier: a \"%x\" specifier can not have a \"+\" flag.", 0, 3);
        test("%+x");

        expecteds.clear();
        err("Invalid format specifier: a \"%x\" specifier can not have a \" \" flag.", 0, 3);
        test("% x");

        expecteds.clear();
        err("Invalid format specifier: a \"%x\" specifier can not have a \",\" flag.", 0, 3);
        test("%,x");

        expecteds.clear();
        err("Invalid format specifier: a \"%x\" specifier can not have a formatting precision.", 0, 4);
        test("%.3x");

        ///

        expecteds.clear();
        err("Invalid format specifier: a \"%X\" specifier can not have a \"+\" flag.", 0, 3);
        test("%+X");

        expecteds.clear();
        err("Invalid format specifier: a \"%X\" specifier can not have a \" \" flag.", 0, 3);
        test("% X");

        expecteds.clear();
        err("Invalid format specifier: a \"%X\" specifier can not have a \",\" flag.", 0, 3);
        test("%,X");

        expecteds.clear();
        err("Invalid format specifier: a \"%X\" specifier can not have a formatting precision.", 0, 4);
        test("%.3X");
    }

    /** Test 'e' and 'E'. */
    @Test
    public void testReal() {
        expecteds.clear();
        err("Invalid format specifier: a \"%e\" specifier can not have a \",\" flag.", 0, 3);
        test("%,e");

        ///

        expecteds.clear();
        err("Invalid format specifier: a \"%E\" specifier can not have a \",\" flag.", 0, 3);
        test("%,E");
    }

    /** Test decoding and encoding round trip behavior. */
    @Test
    public void testRoundTrip() {
        String[] tests = getAllTests();

        TESTS:
        for (String test: tests) {
            List<FormatDescription> fds = new FormatDecoder().decode(test);
            for (FormatDescription fd: fds) {
                if (fd.conversion == ERROR) {
                    continue TESTS;
                }
            }
            String rslt = StringUtils.join(fds, "");
            assertEquals(test, rslt);
        }
    }

    /** Test escaping. */
    @Test
    public void testEscaping() {
        literal("a", 0, 1);
        literal("%", 1, 2);
        literal("b", 3, 1);
        literal("%", 4, 2);
        literal("%", 6, 2);
        literal("c", 8, 1);
        literal("%", 9, 2);
        expecteds.add(new FormatDescription(INTEGER, "", "", "d", "", null, 11, 2));
        literal("\"e\\f\ng\th", 13, 12);
        test("a%%b%%%%c%%%d\"e\\f\ng\th");
    }

    /** Test offset and length. */
    @Test
    public void testOffsetLength() {
        String[] tests = getAllTests();
        String combi = "";
        List<Integer> offsets = list();
        List<Integer> lengths = list();
        int offset = 0;

        TESTS:
        for (String test: tests) {
            List<FormatDescription> fds = new FormatDecoder().decode(test);
            for (FormatDescription fd: fds) {
                if (fd.conversion == ERROR) {
                    continue TESTS;
                }
            }
            combi += "_" + test + "_";

            if (offsets.isEmpty()) {
                offsets.add(offset);
                offset += 1;
                offsets.add(offset);
                offset += test.length();
                lengths.add(1);
                lengths.add(test.length());
            } else {
                offsets.add(offset);
                offset += 2;
                offsets.add(offset);
                offset += test.length();
                lengths.add(2);
                lengths.add(test.length());
            }
        }
        offsets.add(offset);
        lengths.add(1);

        List<FormatDescription> fds = new FormatDecoder().decode(combi);
        assertEquals(fds.size(), offsets.size());
        assertEquals(fds.size(), lengths.size());
        int length;
        for (int i = 0; i < fds.size(); i++) {
            FormatDescription fd = fds.get(i);
            offset = offsets.get(i);
            length = lengths.get(i);
            assertEquals(offset, fd.offset);
            assertEquals(length, fd.length);
        }
    }

    /** Test literal and specifier mixes. */
    @Test
    public void testLiteralsWithSpecifiers() {
        String[] tests = getAllTests();
        String combi = "";

        TESTS:
        for (String test: tests) {
            List<FormatDescription> fds = new FormatDecoder().decode(test);
            for (FormatDescription fd: fds) {
                if (fd.conversion == ERROR) {
                    continue TESTS;
                }
            }
            combi += "_" + test + "_";
        }

        List<FormatDescription> fds = new FormatDecoder().decode(combi);
        String rslt = StringUtils.join(fds, "");
        assertEquals(combi, rslt);
    }

    /** Test precision. */
    @Test
    public void testPrecision() {
        String[] precisions = {"0", "00", "000", "01", "1", "2"};
        String[] expecteds = {"1", "1", "1", "1.2", "1.2", "1.23"};
        assertEquals(precisions.length, expecteds.length);

        for (int i = 0; i < precisions.length; i++) {
            String precision = precisions[i];
            String expected = expecteds[i];
            String test = "%." + precision + "f";
            List<FormatDescription> fds = new FormatDecoder().decode(test);
            assertEquals(1, fds.size());
            String actual = fmt(fds.get(0).toString(), 1.2345);
            assertEquals(expected, actual);
        }
    }

    /** Test flag order. */
    @Test
    public void testFlagOrder() {
        List<String> flags = list("-", "+", ","); // "0", " "
        Random random = new Random(123);
        Set<String> results = set();
        for (int i = 0; i < 1000; i++) {
            Collections.shuffle(flags, random);
            String test = fmt("%%%s10d", StringUtils.join(flags, ""));
            List<FormatDescription> fds = new FormatDecoder().decode(test);
            String result = StringUtils.join(fds, "");
            results.add(result);
        }
        assertEquals(1, results.size());
    }

    /** Test duplicate flags. */
    @Test
    public void testDuplFlag() {
        String[] flags = {"-", "+", " ", "0", ","};
        for (String flag: flags) {
            expecteds.clear();
            String test = "%" + flag + flag + "10d";
            err(fmt("Invalid format specifier: duplicate \"%s\" flag.", flag), 0, test.length());
            test(test);
        }
    }

    /** Test incomplete specifier (premature EOF). */
    @Test
    public void testIncompleteSpecifier() {
        String[] tests = {"%", "%1$", "%+", "%2", "%.3"};
        for (String test: tests) {
            expecteds.clear();
            err("Invalid format specifier: the specifier is incomplete.", 0, test.length());
            test(test);
        }
    }

    /** Test invalid conversion character. */
    @Test
    public void testInvalidConversionChar() {
        String[] tests = {"%z", "%1$z", "%+z", "%2z", "%.3z", "%1$+2.3z"};
        for (String test: tests) {
            expecteds.clear();
            err("Invalid format specifier: unknown \"%z\" formatting conversion.", 0, test.length());
            test(test);
        }
    }

    /** Test invalid flags combination for '-' and '0'. */
    @Test
    public void testMinusZero() {
        err("Invalid format specifier: flags \"-\" and \"0\" cannot be combined.", 0, 6);
        test("%-010s");
    }

    /** Test '-' flag with missing width. */
    @Test
    public void testMinusNoWidth() {
        err("Invalid format specifier: flag \"-\" requires a width.", 0, 3);
        test("%-s");
    }

    /** Test '0' flag with missing width. */
    @Test
    public void testZeroNoWidth() {
        err("Invalid format specifier: flag \"0\" requires a width.", 0, 3);
        test("%0s");
    }

    /** Test invalid flags combination for '+' and ' ' (space). */
    @Test
    public void testPlusSpace() {
        err("Invalid format specifier: flags \"+\" and \" \" (space) cannot be combined.", 0, 6);
        test("%+ 10s");
    }

    /** Test missing explicit index before '$'. */
    @Test
    public void testMissingIndex() {
        err("Invalid format specifier: missing explicit index before \"$\".", 0, 3);
        test("%$s");
    }

    /** Test missing precision after '.'. */
    @Test
    public void testMissingPrecision() {
        err("Invalid format specifier: missing format precision after \".\".", 0, 3);
        test("%.f");
    }

    /** Test escaped percentage with other things. */
    @Test
    public void testPercentageWithOthers() {
        expecteds.clear();
        err("Invalid format specifier: a \"%%\" specifier can not have an index.", 0, 4);
        test("%1$%");

        expecteds.clear();
        err("Invalid format specifier: a \"%%\" specifier can not have a formatting flag.", 0, 3);
        test("%+%");

        expecteds.clear();
        err("Invalid format specifier: a \"%%\" specifier can not have a formatting width.", 0, 3);
        test("%1%");

        expecteds.clear();
        err("Invalid format specifier: a \"%%\" specifier can not have a formatting precision.", 0, 4);
        test("%.1%");
    }

    /** Test that flag order is irrelevant for Java formatting. */
    @Test
    public void testJavaFlagOrder() {
        List<String> flags = list("-", "+", ","); // "0", " "
        Random random = new Random(123);
        Set<String> results = set();
        for (int i = 0; i < 1000; i++) {
            Collections.shuffle(flags, random);
            String pattern = fmt("%%%s10d", StringUtils.join(flags, ""));
            String result = fmt(pattern, 12345);
            results.add(result);
        }
        assertEquals(1, results.size());
    }

    /** Test that duplicate flags are not allowed for Java formatting. */
    @Test(expected = DuplicateFormatFlagsException.class)
    public void testJavaDuplFlag() {
        fmt("%--10s", 12345);
    }

    /**
     * Test that {@link Double#toString} is different from all floating point format specifiers, for Java formatting.
     */
    @Test
    public void testJavaDoubleToString() {
        Set<String> results = set();
        double value = 1.23456789e-10;
        results.add(fmt("%s", value)); // 1.23456789E-10
        results.add(fmt("%e", value)); // 1.234568e-10
        results.add(fmt("%f", value)); // 0.000000
        results.add(fmt("%g", value)); // 1.23457e-10
        assertEquals(4, results.size());
    }
}
