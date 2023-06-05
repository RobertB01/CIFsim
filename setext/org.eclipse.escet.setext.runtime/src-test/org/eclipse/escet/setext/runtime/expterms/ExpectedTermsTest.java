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

package org.eclipse.escet.setext.runtime.expterms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.escet.setext.runtime.exceptions.ParseException;
import org.junit.jupiter.api.Test;

/** Expected terminals on parse error tests. */
@SuppressWarnings("javadoc")
public class ExpectedTermsTest {
    @Test
    public void testNoFailure() {
        test("acb", null);
        test("acdb", null);
        test("bca", null);
        test("bcda", null);
    }

    @Test
    public void testFailure1() {
        test("c", "Parsing failed at line 1, column 1, at or near \"c\" (found \"c\", expected \"a\" or \"b\").");
    }

    @Test
    public void testFailure2() {
        test("ab", "Parsing failed at line 1, column 2, at or near \"b\" (found \"b\", expected \"c\").");
    }

    @Test
    public void testFailure3() {
        test("ba", "Parsing failed at line 1, column 2, at or near \"a\" (found \"a\", expected \"c\").");
    }

    @Test
    public void testFailure4() {
        test("ac", "Parsing failed at line 1, column 3, most likely due to premature end of input "
                + "(found end-of-file, expected \"b\" or \"d\").");
    }

    @Test
    public void testFailure5() {
        test("acc", "Parsing failed at line 1, column 3, at or near \"c\" (found \"c\", expected \"b\" or \"d\").");
    }

    @Test
    public void testFailure6() {
        test("aca", "Parsing failed at line 1, column 3, at or near \"a\" (found \"a\", expected \"b\" or \"d\").");
    }

    @Test
    public void testFailure7() {
        test("bcb", "Parsing failed at line 1, column 3, at or near \"b\" (found \"b\", expected \"a\" or \"d\").");
    }

    @Test
    public void testFailure8() {
        test("acba", "Parsing failed at line 1, column 4, at or near \"a\" (found \"a\", expected end-of-file).");
    }

    @Test
    public void testFailure9() {
        test("acbb", "Parsing failed at line 1, column 4, at or near \"b\" (found \"b\", expected end-of-file).");
    }

    @Test
    public void testFailure10() {
        test("acbc", "Parsing failed at line 1, column 4, at or near \"c\" (found \"c\", expected end-of-file).");
    }

    @Test
    public void testFailure11() {
        test("acbd", "Parsing failed at line 1, column 4, at or near \"d\" (found \"d\", expected end-of-file).");
    }

    /**
     * Performs a test on the expected terminals for a parse error.
     *
     * @param inputText The input text that will fail to parse.
     * @param expectedErrorMsg The expected error message (exception message), or {@code null} if parsing should
     *     succeed.
     */
    private static void test(String inputText, String expectedErrorMsg) {
        ExpectedTermsParser parser = new ExpectedTermsParser();
        try {
            parser.parseString(inputText, "/dummy.file");
            assertEquals(null, expectedErrorMsg);
        } catch (ParseException ex) {
            assertEquals(expectedErrorMsg, ex.toString());
        }
    }
}
