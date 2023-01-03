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

import java.util.List;

import org.junit.Test;

/** Unit tests for the {@link BoxUtils} class. */
public class BoxUtilsTest extends BoxTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testJoinSep() {
        List<TextBox> elements = list(new TextBox("a"), new TextBox("b"));
        HBox box = BoxUtils.join(elements, ".");
        List<String> expected = list("a.b");
        assertEqualLists(expected, box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testJoinSepEmpty() {
        List<TextBox> elements = list();
        HBox box = BoxUtils.join(elements, ".");
        List<String> expected = list();
        assertEqualLists(expected, box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testJoinPreSepPost() {
        List<TextBox> elements = list(new TextBox("a"), new TextBox("b"));
        HBox box = BoxUtils.join(elements, "(", ".", ")");
        List<String> expected = list("(a.b)");
        assertEqualLists(expected, box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testJoinPreSepPostEmpty() {
        List<TextBox> elements = list();
        HBox box = BoxUtils.join(elements, "(", ".", ")");
        List<String> expected = list("()");
        assertEqualLists(expected, box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBoxAndJoinSep() {
        List<Integer> elements = list(1, 2);
        Boxer<Integer> boxer = new BoxerTest.IntBoxer();
        HBox box = BoxUtils.boxAndJoin(boxer, elements, ".");
        List<String> expected = list("Integer(1).Integer(2)");
        assertEqualLists(expected, box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBoxAndJoinSepEmpty() {
        List<Integer> elements = list();
        Boxer<Integer> boxer = new BoxerTest.IntBoxer();
        HBox box = BoxUtils.boxAndJoin(boxer, elements, ".");
        List<String> expected = list();
        assertEqualLists(expected, box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBoxAndJoinPreSepPost() {
        List<Integer> elements = list(1, 2);
        Boxer<Integer> boxer = new BoxerTest.IntBoxer();
        HBox box = BoxUtils.boxAndJoin(boxer, elements, "(", ".", ")");
        List<String> expected = list("(Integer(1).Integer(2))");
        assertEqualLists(expected, box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBoxAndJoinPreSepPostEmpty() {
        List<Integer> elements = list();
        Boxer<Integer> boxer = new BoxerTest.IntBoxer();
        HBox box = BoxUtils.boxAndJoin(boxer, elements, "(", ".", ")");
        List<String> expected = list("()");
        assertEqualLists(expected, box.getLines());
    }
}
