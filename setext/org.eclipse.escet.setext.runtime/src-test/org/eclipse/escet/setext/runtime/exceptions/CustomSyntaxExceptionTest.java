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

package org.eclipse.escet.setext.runtime.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.escet.common.java.TextPosition;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link CustomSyntaxException} class. */
@SuppressWarnings("javadoc")
public class CustomSyntaxExceptionTest {
    @Test
    public void testCustomSyntaxExToStrNoSrc() {
        TextPosition pos = new TextPosition("/location", 1, 2, 1, 2, 1, 1);
        Exception ex = new CustomSyntaxException("Some msg.", pos);
        assertEquals("Syntax error at line 1, column 2: Some msg.", ex.toString());
    }

    @Test
    public void testCustomSyntaxExToStrWithSrc() {
        TextPosition pos = new TextPosition("/location", "File \"file.ext\": ", 1, 2, 1, 2, 1, 1);
        Exception ex = new CustomSyntaxException("Some msg.", pos);
        assertEquals("File \"file.ext\": Syntax error at line 1, column 2: Some msg.", ex.toString());
    }
}
