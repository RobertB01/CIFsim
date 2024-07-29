//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Unit tests for {@link CsvUtils}. */
public class CsvUtilsTest {
    /** Test {@link CsvUtils#rowsToString}. */
    @Test
    public void testRowsToString() {
        List<List<String>> rows = List.of(
                List.of("a", "b", "cde"),
                List.of("f\"g", "h i", "j,k"),
                List.of("l\r\nm", "n\ro", "p\nq"));
        List<String> expectedLines = list(
                "a,b,cde",
                "\"f\"\"g\",h i,\"j,k\"",
                "\"l\r\nm\",\"n\ro\",\"p\nq\"");
        String expected = String.join("\n", expectedLines);
        assertEquals(expected, CsvUtils.rowsToString(rows));
    }
}
