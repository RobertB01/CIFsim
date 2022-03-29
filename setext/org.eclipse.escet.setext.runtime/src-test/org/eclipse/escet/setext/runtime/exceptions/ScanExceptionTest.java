//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.junit.Assert.assertEquals;

import org.eclipse.escet.common.java.TextPosition;
import org.junit.Test;

/** Unit tests for the {@link ScanException} class. */
@SuppressWarnings("javadoc")
public class ScanExceptionTest {
    @Test
    public void testScanExToStrNoSrc() {
        TextPosition pos = new TextPosition("/location", 1, 2, 1, 2, 1, 1);
        Exception ex = new ScanException(97, pos);
        assertEquals("Scanning failed for character \"a\" (Unicode U+61) at line 1, column 2.", ex.toString());
    }

    @Test
    public void testScanExToStrWithSrc() {
        TextPosition pos = new TextPosition("/location", "File \"file.ext\": ", 1, 2, 1, 2, 1, 1);
        Exception ex = new ScanException(97, pos);
        assertEquals("File \"file.ext\": Scanning failed for character \"a\" (Unicode U+61) at line 1, column 2.",
                ex.toString());
    }

    @Test
    public void testScanExEof() {
        TextPosition pos = new TextPosition("/location", 1, 2, 1, 2, 1, 1);
        Exception ex = new ScanException(-1, pos);
        assertEquals("Scanning failed at line 1, column 2, due to premature end of input.", ex.toString());
    }

    @Test
    public void testScanExCodePoint0() {
        TextPosition pos = new TextPosition("/location", 1, 2, 1, 2, 1, 1);
        Exception ex = new ScanException(0, pos);
        assertEquals("Scanning failed for character \"\" (Unicode U+0) at line 1, column 2.", ex.toString());
    }

    @Test
    public void testScanExCodePoint10025() {
        // U+10025 = LINEAR B SYLLABLE B021 QI
        TextPosition pos = new TextPosition("/location", 1, 2, 1, 2, 1, 1);
        Exception ex = new ScanException(0x10025, pos);
        assertEquals("Scanning failed for character \"\uD800\uDC25\" (Unicode U+10025) at line 1, column 2.",
                ex.toString());
    }
}
