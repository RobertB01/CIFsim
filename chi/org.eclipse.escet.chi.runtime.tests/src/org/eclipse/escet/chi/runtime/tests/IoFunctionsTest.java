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

package org.eclipse.escet.chi.runtime.tests;

import static org.eclipse.escet.chi.runtime.IoFunctions.readInt;
import static org.eclipse.escet.chi.runtime.IoFunctions.readString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.escet.chi.runtime.ChiSimulatorException;
import org.eclipse.escet.chi.runtime.data.io.ChiReadMemoryFile;
import org.junit.Test;

/** Tests for the I/O functions. */
@SuppressWarnings("javadoc")
public class IoFunctionsTest {
    @Test
    public void testReadUnsignedInt1() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile("0");
        assertEquals(0, readInt(rdr));
    }

    @Test
    public void testReadUnsignedInt2() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile("136");
        assertEquals(136, readInt(rdr));
    }

    @Test
    public void testReadUnsignedInt3() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile("-136");
        assertEquals(-136, readInt(rdr));
    }

    @Test
    public void testReadUnsignedInt4() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile("--136");
        assertEquals(136, readInt(rdr));
    }

    @Test
    public void testReadUnsignedInt5() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile("-a");
        try {
            readInt(rdr);
            assertTrue(false); // Should never be reached
        } catch (ChiSimulatorException e) {
            assertEquals("Expected integer number but found no digits while reading input.", e.getMessage());
        }
        assertEquals('a', rdr.read());
    }

    @Test
    public void testReadUnsignedInt6() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile("10a");
        assertEquals(10, readInt(rdr));
        assertEquals('a', rdr.read());
    }

    @Test
    public void testReadQuotedString1() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile(" \"abc def\" foo");
        assertEquals("abc def", readString(rdr));
    }

    @Test
    public void testReadQuotedString2() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile(" \"abc \\\" def\" foo");
        assertEquals("abc \" def", readString(rdr));
    }

    @Test
    public void testReadQuotedString3() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile(" \"abc \\n def\" foo");
        assertEquals("abc \n def", readString(rdr));
    }

    @Test
    public void testReadQuotedString4() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile(" \"abc \\n def foo");
        try {
            readString(rdr);
            assertTrue(false); // Should never be reached
        } catch (ChiSimulatorException e) {
            assertEquals("Encountered end-of-file while reading a string literal, perhaps a closing quote is missing.",
                    e.getMessage());
        }
        assertEquals(-1, rdr.read());
    }

    @Test
    public void testReadUnquotedString1() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile(" abc def foo");
        assertEquals("abc", readString(rdr));
    }

    @Test
    public void testReadUnquotedString2() {
        ChiReadMemoryFile rdr = new ChiReadMemoryFile(" abc");
        assertEquals("abc", readString(rdr));
    }
}
