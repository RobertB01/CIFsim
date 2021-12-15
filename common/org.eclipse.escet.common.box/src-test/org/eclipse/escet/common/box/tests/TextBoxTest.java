//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.box.tests;

import static org.eclipse.escet.common.java.Lists.list;

import org.eclipse.escet.common.box.TextBox;
import org.junit.Test;

/** Unit tests for the {@link TextBox} class. */
public class TextBoxTest extends BoxTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testSimple() {
        TextBox box = new TextBox("some words");
        assertEqualLists(list("some words"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testNewLines() {
        // New lines are not allowed in TextBox. Compare this to
        // MultiLineTextBoxTests to see why.
        TextBox box = new TextBox("some\nmore\nwords");
        assertEqualLists(list("some\nmore\nwords"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFormat() {
        TextBox box = new TextBox("a%sc", "b");
        assertEqualLists(list("abc"), box.getLines());

        box = new TextBox("a%sc", "");
        assertEqualLists(list("ac"), box.getLines());

        box = new TextBox("a%sc", 0);
        assertEqualLists(list("a0c"), box.getLines());

        box = new TextBox("a%sc", true);
        assertEqualLists(list("atruec"), box.getLines());

        box = new TextBox("a%d%sc", 0, true);
        assertEqualLists(list("a0truec"), box.getLines());
    }
}
