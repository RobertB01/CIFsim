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
        List<List<String>> rows = list(
                list("a", "b", "c"),
                list("d", "e", "fgh"),
                list("i\"j", "k l", "m,n"),
                list("o\r\np", "q\rr", "s\nt"));
        String expected = "a,b,c\nd,e,fgh\n\"i\"\"j\",k l,\"m,n\"\n\"o\r\np\",\"q\rr\",\"s\nt\"";
        assertEquals(expected, CsvUtils.rowsToString(rows));
    }
}
