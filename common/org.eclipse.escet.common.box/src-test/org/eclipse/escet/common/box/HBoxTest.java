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

package org.eclipse.escet.common.box;

import static org.eclipse.escet.common.java.Lists.list;

import org.junit.jupiter.api.Test;

/** Unit tests for the {@link HBox} class. */
public class HBoxTest extends BoxTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testDefaultSeparator() {
        HBox box = new HBox();
        box.add("a");
        box.add("b");
        assertEqualLists(list("ab"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSimpleText() {
        HBox box = new HBox(" ");
        box.add("word1");
        box.add("word2");
        box.add("word3 word4");
        assertEqualLists(list("word1 word2 word3 word4"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSimpleBox() {
        HBox box = new HBox(" ");
        box.add(new TextBox("word1"));
        box.add(new TextBox("word2"));
        assertEqualLists(list("word1 word2"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCustomSeparator() {
        HBox box = new HBox("_");
        box.add("word1");
        box.add("word2");
        assertEqualLists(list("word1_word2"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testNewLines() {
        HBox box = new HBox(" ");
        box.add(new MultiLineTextBox("word1\na"));
        box.add(new MultiLineTextBox("word2\nb"));
        assertEqualLists(list("word1", "a word2", "  b"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testLastLongest() {
        HBox h = new HBox(" ");
        h.add("a");
        h.add("b");
        h.add(new MultiLineTextBox("c\nd"));
        assertEqualLists(list("a b c", "    d"), h.getLines());
    }
}
