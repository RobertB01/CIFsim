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

package org.eclipse.escet.setext.runtime.parser;

import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

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
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.exceptions.ParseException;
import org.junit.jupiter.api.Test;

/** Parser unit tests. */
public class ParserTest {
    /** Dummy source file location. */
    private static final String LOC = "/dummy.file";

    /** Test various valid calculator inputs, without variables. */
    @Test
    public void testCalcSimple() {
        CalcTestParser parser = new CalcTestParser();

        assertEquals(list(123.0), parser.parseString("123;", LOC));
        assertEquals(list(123.0), parser.parseString("(123);", LOC));
        assertEquals(list(123.0), parser.parseString("((123));", LOC));
        assertEquals(list(Math.PI), parser.parseString("pi;", LOC));

        assertEquals(list(-123.0), parser.parseString("-123;", LOC));
        assertEquals(list(123.0), parser.parseString("--123;", LOC));

        assertEquals(list(8.0), parser.parseString("3+5;", LOC));
        assertEquals(list(5.0), parser.parseString("12-7;", LOC));
        assertEquals(list(28.0), parser.parseString("4*7;", LOC));
        assertEquals(list(2.5), parser.parseString("10/4;", LOC));

        assertEquals(list(14.0), parser.parseString("2+3*4;", LOC));
        assertEquals(list(20.0), parser.parseString("(2+3)*4;", LOC));
        assertEquals(list(14.0), parser.parseString("2+(3*4);", LOC));

        assertEquals(list(-4.0), parser.parseString("-5+1;", LOC));
        assertEquals(list(-6.0), parser.parseString("-(5+1);", LOC));
        assertEquals(list(-4.0), parser.parseString("(-5)+1;", LOC));

        assertEquals(list(), parser.parseString("", LOC));
        assertEquals(list(), parser.parseString(";;;;", LOC));
        assertEquals(list(1.0), parser.parseString(";;1;;", LOC));
        assertEquals(list(1.0, 2.0), parser.parseString("1;2\n; \n ;", LOC));
    }

    /** Test various valid calculator inputs, with variables. */
    @Test
    public void testCalcVariables() {
        CalcTestParser parser = new CalcTestParser();

        assertEquals(list(5.0), parser.parseString("x=5;", LOC));
        assertEquals(list(5.0), parser.parseString("x;", LOC));
        assertEquals(list(10.0), parser.parseString("x*2;", LOC));

        assertEquals(list(6.0), parser.parseString("x=6;", LOC));
        assertEquals(list(6.0), parser.parseString("x;", LOC));
        assertEquals(list(12.0), parser.parseString("x*2;", LOC));
    }

    /** Test various valid expression inputs, without variables. */
    @Test
    public void testExprSimple() {
        CalcTestExpressionParser parser = new CalcTestExpressionParser();

        assertEquals(123.0, parser.parseString("123", LOC), 0.0);
        assertEquals(5.0, parser.parseString("1+4", LOC), 0.0);
    }

    /** Test normal parse error (wrong terminal encountered). */
    @Test
    public void testParseError() {
        CalcTestParser parser = new CalcTestParser();

        try {
            parser.parseString("+", LOC);
        } catch (ParseException e) {
            assertEquals("+", e.getTokenText());
            assertEquals(0, e.getPosition().startOffset);
            assertEquals(1, e.getPosition().startLine);
            assertEquals(1, e.getPosition().startColumn);
            assertEquals(0, e.getPosition().endOffset);
            assertEquals(1, e.getPosition().endLine);
            assertEquals(1, e.getPosition().endColumn);
        }

        try {
            parser.parseString("\n+", LOC);
        } catch (ParseException e) {
            assertEquals("+", e.getTokenText());
            assertEquals(1, e.getPosition().startOffset);
            assertEquals(2, e.getPosition().startLine);
            assertEquals(1, e.getPosition().startColumn);
            assertEquals(1, e.getPosition().endOffset);
            assertEquals(2, e.getPosition().endLine);
            assertEquals(1, e.getPosition().endColumn);
        }
    }

    /** Test premature end-of-input. */
    @Test
    public void testPrematureEOF() {
        CalcTestParser parser = new CalcTestParser();

        try {
            parser.parseString("5", LOC);
        } catch (ParseException e) {
            assertEquals(null, e.getTokenText());
            assertEquals(1, e.getPosition().startOffset);
            assertEquals(1, e.getPosition().startLine);
            assertEquals(2, e.getPosition().startColumn);
            assertEquals(1, e.getPosition().endOffset);
            assertEquals(1, e.getPosition().endLine);
            assertEquals(2, e.getPosition().endColumn);
        }

        try {
            parser.parseString("\n5", LOC);
        } catch (ParseException e) {
            assertEquals(null, e.getTokenText());
            assertEquals(2, e.getPosition().startOffset);
            assertEquals(2, e.getPosition().startLine);
            assertEquals(2, e.getPosition().startColumn);
            assertEquals(2, e.getPosition().endOffset);
            assertEquals(2, e.getPosition().endLine);
            assertEquals(2, e.getPosition().endColumn);
        }
    }

    /** Test parser debug output, in case of successful parsing. */
    @SuppressWarnings("unused")
    @Test
    public void testDebugOutputOk() {
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

            // Parse input.
            CalcTestParser parser = new CalcTestParser();
            parser.parseString("(1+3)*4;;", LOC, DebugMode.PARSER);

            // Get expected output.
            String resourcePath = getClass().getPackage().getName();
            resourcePath = resourcePath.replace('.', '/');
            resourcePath += "/" + getClass().getSimpleName() + "_ok.out";
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

    /** Test parser debug output, in case of parse error. */
    @SuppressWarnings("unused")
    @Test
    public void testDebugOutputErr() {
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

            // Parse input.
            CalcTestParser parser = new CalcTestParser();
            try {
                parser.parseString("(1+3));", LOC, DebugMode.PARSER);
                fail(); // Parsing should fail.
            } catch (ParseException ex) {
                // Parse failure expected.
            }

            // Get expected output.
            String resourcePath = getClass().getPackage().getName();
            resourcePath = resourcePath.replace('.', '/');
            resourcePath += "/" + getClass().getSimpleName() + "_err.out";
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
