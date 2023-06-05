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

package org.eclipse.escet.setext.runtime.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.escet.common.java.TextPosition;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link ParseException} class. */
@SuppressWarnings("javadoc")
public class ParseExceptionTest {
    @Test
    public void testParseExToStrWithSrcWithTokenWithTerminals() {
        TextPosition pos = new TextPosition("/location", "File \"file.ext\": ", 1, 2, 1, 2, 1, 1);
        Exception ex = new ParseException("ID", pos, "an identifier", "a literal");
        assertEquals("File \"file.ext\": Parsing failed at line 1, column 2, at or near \"ID\" "
                + "(found an identifier, expected a literal).", ex.toString());
    }

    @Test
    public void testParseExToStrWithSrcWithTokenNoTerminals() {
        TextPosition pos = new TextPosition("/location", "File \"file.ext\": ", 1, 2, 1, 2, 1, 1);
        Exception ex = new ParseException("ID", pos);
        assertEquals("File \"file.ext\": Parsing failed at line 1, column 2, at or near \"ID\".", ex.toString());
    }

    @Test
    public void testParseExToStrWithSrcNoTokenWithTerminals() {
        TextPosition pos = new TextPosition("/location", "File \"file.ext\": ", 1, 2, 1, 2, 1, 1);
        Exception ex = new ParseException(null, pos, "an identifier", "a literal");
        assertEquals("File \"file.ext\": Parsing failed at line 1, column 2, most likely due to premature end of input "
                + "(found an identifier, expected a literal).", ex.toString());
    }

    @Test
    public void testParseExToStrWithSrcNoTokenNoTerminals() {
        TextPosition pos = new TextPosition("/location", "File \"file.ext\": ", 1, 2, 1, 2, 1, 1);
        Exception ex = new ParseException(null, pos);
        assertEquals("File \"file.ext\": Parsing failed at line 1, column 2, most likely due to premature end of "
                + "input.", ex.toString());
    }

    @Test
    public void testParseExToStrNoSrcWithTokenWithTerminals() {
        TextPosition pos = new TextPosition("/location", 1, 2, 1, 2, 1, 1);
        Exception ex = new ParseException("ID", pos, "an identifier", "a literal");
        assertEquals("Parsing failed at line 1, column 2, at or near \"ID\" (found an identifier, expected a literal).",
                ex.toString());
    }

    @Test
    public void testParseExToStrNoSrcWithTokenNoTerminals() {
        TextPosition pos = new TextPosition("/location", 1, 2, 1, 2, 1, 1);
        Exception ex = new ParseException("ID", pos);
        assertEquals("Parsing failed at line 1, column 2, at or near \"ID\".", ex.toString());
    }

    @Test
    public void testParseExToStrNoSrcNoTokenWithTerminals() {
        TextPosition pos = new TextPosition("/location", 1, 2, 1, 2, 1, 1);
        Exception ex = new ParseException(null, pos, "an identifier", "a literal");
        assertEquals("Parsing failed at line 1, column 2, most likely due to premature end of input (found an "
                + "identifier, expected a literal).", ex.toString());
    }

    @Test
    public void testParseExToStrNoSrcNoTokenNoTerminals() {
        TextPosition pos = new TextPosition("/location", 1, 2, 1, 2, 1, 1);
        Exception ex = new ParseException(null, pos);
        assertEquals("Parsing failed at line 1, column 2, most likely due to premature end of input.", ex.toString());
    }
}
