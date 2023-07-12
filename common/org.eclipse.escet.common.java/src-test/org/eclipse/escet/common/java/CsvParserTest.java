//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.eclipse.escet.common.java.CsvParser.CsvParseError;
import org.junit.jupiter.api.Test;

/** Tests for the CSV parser. */
@SuppressWarnings("javadoc")
public class CsvParserTest {
    public static final String CR = "\r";

    public static final String LF = "\n";

    public static final String CRLF = "\r\n";

    @Test
    public void testEmptyFileParse() {
        CsvParser p = CsvUtils.makeParser("");
        List<List<String>> lines = p.parseFile(); // 1 empty field, 1 line.
        assertEquals(1, lines.size());
        assertEquals(1, lines.get(0).size());
        assertEquals("", lines.get(0).get(0));
    }

    @Test
    public void testCommaFileParse() {
        CsvParser p = CsvUtils.makeParser(",");
        List<List<String>> lines = p.parseFile(); // 2 empty fields, 1 line.
        assertEquals(1, lines.size());
        assertEquals(2, lines.get(0).size());
        assertEquals("", lines.get(0).get(0));
        assertEquals("", lines.get(0).get(1));
    }

    @Test
    public void testCrlfDelimiter() {
        CsvParser p = CsvUtils.makeParser("a" + CRLF + "b"); // 1 field, 2 lines.
        List<List<String>> lines = p.parseFile();
        assertEquals(2, lines.size());
        assertEquals(1, lines.get(0).size());
    }

    @Test
    public void finalCrlfTest() {
        CsvParser p = CsvUtils.makeParser("a" + CRLF + "b" + CRLF); // 1 field, 2 lines.
        List<List<String>> lines = p.parseFile();
        assertEquals(2, lines.size());
        assertEquals(1, lines.get(0).size());
    }

    @Test
    public void testLfDelimiter() {
        CsvParser p = CsvUtils.makeParser("a" + LF + "b"); // 1 field, 2 lines.
        List<List<String>> lines = p.parseFile();
        assertEquals(2, lines.size());
        assertEquals(1, lines.get(0).size());
    }

    @Test
    public void testFinalLf() {
        CsvParser p = CsvUtils.makeParser("a" + CRLF + "b" + LF); // 1 field, 2 lines.
        List<List<String>> lines = p.parseFile();
        assertEquals(2, lines.size());
        assertEquals(1, lines.get(0).size());
    }

    @Test
    public void testUnquotedFieldWithQuote() {
        CsvParser p = CsvUtils.makeParser("ab\"cd");
        assertThrows(CsvParseError.class, () -> p.parseFile());
    }

    @Test
    public void testEofInQuotedField() {
        CsvParser p = CsvUtils.makeParser("\"ab\"\"cd");
        assertThrows(CsvParseError.class, () -> p.parseFile());
    }

    @Test
    public void testBadNumberOfFields() {
        CsvParser p = CsvUtils.makeParser("ab,cd" + CR + "de,fg,hi");
        assertThrows(CsvParseError.class, () -> p.parseFile());
    }

    @Test
    public void testUnquotedField() {
        CsvParser p = CsvUtils.makeParser("abcd,pq");
        List<List<String>> lines = p.parseFile(); // 2 fields, 1 line.
        assertEquals(1, lines.size());
        assertEquals(2, lines.get(0).size());
    }
}
