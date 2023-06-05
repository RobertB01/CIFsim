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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.eclipse.escet.common.box.GridBox.GridBoxLayout;
import org.eclipse.escet.common.java.Strings;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link GridBox} class. */
public class GridBoxTest extends BoxTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testSimple() {
        GridBox box = new GridBox(2, 3, 1, 2);
        box.set(0, 0, new TextBox("abc"));
        box.set(0, 1, new TextBox("d"));
        box.set(0, 2, new TextBox("ef"));
        box.set(1, 0, new TextBox("w"));
        box.set(1, 1, new TextBox("xy"));
        box.set(1, 2, new TextBox("z"));
        assertEqualLists(list("abc  d   ef", "", "w    xy  z"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGet() {
        GridBox box = new GridBox(2, 3, 1, 2);
        box.set(0, 0, new TextBox("abc"));
        box.set(0, 2, new TextBox("ef"));
        box.set(1, 0, new TextBox("w"));
        box.set(1, 2, new TextBox("z"));

        assertEquals(null, box.get(0, 1));
        assertEquals(null, box.get(1, 1));

        assertEquals(list("abc"), box.get(0, 0).getLines());
        assertEquals(list("ef"), box.get(0, 2).getLines());
        assertEquals(list("w"), box.get(1, 0).getLines());
        assertEquals(list("z"), box.get(1, 2).getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testNulls() {
        GridBox box = new GridBox(2, 3, 1, 2);
        box.set(0, 0, new TextBox("abc"));
        box.set(0, 2, new TextBox("ef"));
        box.set(1, 1, new TextBox("xy"));
        box.set(1, 2, new TextBox("z"));
        assertEqualLists(list("abc      ef", "", "     xy  z"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCompute() {
        GridBox box = new GridBox(2, 3, 1, 2);
        box.set(0, 0, new TextBox("abc"));
        box.set(0, 2, new TextBox("ef"));
        box.set(1, 1, new TextBox("xy"));
        box.set(1, 2, new TextBox("z"));
        GridBoxLayout layout = box.computeLayout();
        assertEquals(2, layout.numRows);
        assertEquals(3, layout.numCols);
        assertEquals(3, layout.totalHeight);
        assertEquals(11, layout.totalWidth);
        assertArrayEquals(new int[] {3, 2, 2}, layout.widths);
        assertArrayEquals(new int[] {1, 1}, layout.heights);
        String[][][] txts = {{{"abc"}, null, {"ef"}}, /**/ {null, {"xy"}, {"z"}}};
        assertTrue(Arrays.deepEquals(txts, layout.txts));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testComputeModifyResult() {
        GridBox box = new GridBox(3, 3, 1, 2);
        box.set(0, 0, new TextBox("abc"));
        box.set(0, 2, new TextBox("ef"));
        box.set(2, 1, new TextBox("xy"));
        box.set(2, 2, new TextBox("z"));
        GridBoxLayout layout = box.computeLayout();
        for (int i = 0; i < layout.numCols; i++) {
            box.set(1, i, Strings.duplicate("-", layout.widths[i]));
        }
        assertEqualLists(list("abc      ef", "", "---  --  --", "", "     xy  z"), box.getLines());
    }
}
