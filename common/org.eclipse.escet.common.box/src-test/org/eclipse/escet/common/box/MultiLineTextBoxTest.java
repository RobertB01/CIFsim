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

package org.eclipse.escet.common.box;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.junit.Test;

/** Unit tests for the {@link MultiLineTextBox} class. */
public class MultiLineTextBoxTest extends BoxTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testSimple() {
        MultiLineTextBox box = new MultiLineTextBox("some words");
        assertEqualLists(list("some words"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testNewLines() {
        MultiLineTextBox box = new MultiLineTextBox("some\nmore\nwords");
        assertEqualLists(list("some", "more", "words"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFormat() {
        MultiLineTextBox box = new MultiLineTextBox("a%s\nc", "b");
        assertEqualLists(list("ab", "c"), box.getLines());

        box = new MultiLineTextBox("a%sc", "\n\n");
        assertEqualLists(list("a", "", "c"), box.getLines());

        box = new MultiLineTextBox("a%sc", 0);
        assertEqualLists(list("a0c"), box.getLines());

        box = new MultiLineTextBox("a%sc", true);
        assertEqualLists(list("atruec"), box.getLines());

        box = new MultiLineTextBox("a%d%sc", 0, true);
        assertEqualLists(list("a0truec"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testLinesArray() {
        List<String> lines = list("first line", "second line", "third line");
        MultiLineTextBox box = new MultiLineTextBox(lines.toArray(EMPTY));
        assertEqualLists(lines, box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testLinesList() {
        List<String> lines = list("first line", "second line", "third line");
        MultiLineTextBox box = new MultiLineTextBox(lines);
        assertEqualLists(lines, box.getLines());
    }
}
