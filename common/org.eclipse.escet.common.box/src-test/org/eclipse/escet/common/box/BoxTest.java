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

package org.eclipse.escet.common.box;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.escet.common.app.framework.io.MemAppStream;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link Box} class. */
public class BoxTest extends BoxTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testToBuilder() {
        VBox box = new VBox(2);
        box.add("a", "", "b");
        assertEquals("  a\n\n  b", box.toBuilder().toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testToString() {
        VBox box = new VBox(2);
        box.add("a", "", "b");
        assertEquals("  a\n\n  b", box.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testToStringEmptyFirstLine() {
        CodeBox box = new MemoryCodeBox();
        box.add();
        box.add("a");
        assertEquals("\na", box.toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testWrite() {
        VBox box = new VBox(2);
        box.add("a", "", "b");

        MemAppStream stream = new MemAppStream();
        box.write(stream);
        String txt = stream.toString();

        assertEquals("  a\n\n  b\n", txt.replaceAll("\r", ""));
    }
}
