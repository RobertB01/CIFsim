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

import org.junit.Test;

/** Unit tests for the {@link VBox} class. */
public class VBoxTest extends BoxTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testSimpleText() {
        VBox box = new VBox();
        box.add("word1");
        box.add("word2");
        box.add("word3 word4");
        assertEqualLists(list("word1", "word2", "word3 word4"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSimpleTextAddEmtpy() {
        VBox box = new VBox();
        box.add("word1", true);
        box.add("word2", true);
        box.add("word3 word4", true);
        assertEqualLists(list("word1", "", "word2", "", "word3 word4"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSimpleBox() {
        VBox box = new VBox();
        box.add(new TextBox("word1"));
        box.add(new TextBox("word2"));
        assertEqualLists(list("word1", "word2"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIndent() {
        VBox box = new VBox(2);
        box.add("word1");
        box.add("word2");
        assertEqualLists(list("  word1", "  word2"), box.getLines());
    }
}
