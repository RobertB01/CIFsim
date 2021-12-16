//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.runtime.exceptions.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionFactory;
import org.eclipse.escet.setext.runtime.exceptions.ParseException;
import org.junit.Test;

/** Unit tests for the {@link ParseException} class. */
@SuppressWarnings("javadoc")
public class ParseExceptionTest {
    @Test
    public void testParseExToStrWithSrcWithTokenWithTerminals() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setSource("File \"file.ext\": ");
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new ParseException("ID", pos, "an identifier", "a literal");
        assertEquals("File \"file.ext\": Parsing failed at line 1, column 2, at or near \"ID\" "
                + "(found an identifier, expected a literal).", ex.toString());
    }

    @Test
    public void testParseExToStrWithSrcWithTokenNoTerminals() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setSource("File \"file.ext\": ");
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new ParseException("ID", pos);
        assertEquals("File \"file.ext\": Parsing failed at line 1, column 2, at or near \"ID\".", ex.toString());
    }

    @Test
    public void testParseExToStrWithSrcNoTokenWithTerminals() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setSource("File \"file.ext\": ");
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new ParseException(null, pos, "an identifier", "a literal");
        assertEquals("File \"file.ext\": Parsing failed at line 1, column 2, most likely due to premature end of input "
                + "(found an identifier, expected a literal).", ex.toString());
    }

    @Test
    public void testParseExToStrWithSrcNoTokenNoTerminals() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setSource("File \"file.ext\": ");
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new ParseException(null, pos);
        assertEquals("File \"file.ext\": Parsing failed at line 1, column 2, most likely due to premature end of "
                + "input.", ex.toString());
    }

    @Test
    public void testParseExToStrNoSrcWithTokenWithTerminals() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new ParseException("ID", pos, "an identifier", "a literal");
        assertEquals("Parsing failed at line 1, column 2, at or near \"ID\" (found an identifier, expected a literal).",
                ex.toString());
    }

    @Test
    public void testParseExToStrNoSrcWithTokenNoTerminals() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new ParseException("ID", pos);
        assertEquals("Parsing failed at line 1, column 2, at or near \"ID\".", ex.toString());
    }

    @Test
    public void testParseExToStrNoSrcNoTokenWithTerminals() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new ParseException(null, pos, "an identifier", "a literal");
        assertEquals("Parsing failed at line 1, column 2, most likely due to premature end of input (found an "
                + "identifier, expected a literal).", ex.toString());
    }

    @Test
    public void testParseExToStrNoSrcNoTokenNoTerminals() {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setStartLine(1);
        pos.setStartColumn(2);
        Exception ex = new ParseException(null, pos);
        assertEquals("Parsing failed at line 1, column 2, most likely due to premature end of input.", ex.toString());
    }
}
