//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static java.util.Collections.EMPTY_LIST;
import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.escet.common.box.CodeBox;
import org.junit.Test;

/** Unit tests for the {@link CodeBox} class. */
public abstract class CodeBoxTest extends BoxTestsBase {
    /**
     * Creates a new {@link CodeBox} with default indentation amount.
     *
     * @return The newly created {@link CodeBox}.
     */
    protected abstract CodeBox createCodeBox();

    /**
     * Creates a new {@link CodeBox} with custom indentation amount.
     *
     * @param indentAmount The indentation amount, the amount of spaces to indent per indentation level.
     * @return The newly created {@link CodeBox}.
     */
    protected abstract CodeBox createCodeBox(int indentAmount);

    @Test
    @SuppressWarnings("javadoc")
    public void testEmpty() {
        CodeBox box = createCodeBox();
        assertTrue(box.isEmpty());
        assertEqualLists(EMPTY_LIST, box.getLines());
        assertTrue(box.isEmpty());
        box.add();
        assertFalse(box.isEmpty());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSimpleLines() {
        CodeBox box = createCodeBox();
        box.add("abc");
        box.add();
        box.add("%s%s%s", 1, "2", true);
        assertEqualLists(list("abc", "", "12true"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMultipleLines() {
        CodeBox box = createCodeBox();
        box.add(list("a", "b"));
        box.add(new String[] {"c", "d"});
        box.indent();
        box.add(list("e", "f"));
        box.add(new String[] {"g", "h"});
        assertEqualLists(list("a", "b", "c", "d", "    e", "    f", "    g", "    h"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIndentation() {
        CodeBox box = createCodeBox();
        assertEquals("", box.getIndentation());
        box.indent();
        assertEquals("    ", box.getIndentation());
        box.indent();
        assertEquals("        ", box.getIndentation());
        box.dedent();
        assertEquals("    ", box.getIndentation());
        box.indent();
        assertEquals("        ", box.getIndentation());
        box.dedent();
        assertEquals("    ", box.getIndentation());
        box.dedent();
        assertEquals("", box.getIndentation());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIndentDedent() {
        CodeBox box = createCodeBox();
        box.add("a");
        box.indent();
        box.add("b");
        box.indent();
        box.add("c");
        box.dedent();
        box.add("d");
        box.dedent();
        box.add("e");
        assertEqualLists(list("a", "    b", "        c", "    d", "e"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIndentEmpty() {
        CodeBox box = createCodeBox();
        box.add("a");
        box.indent();
        box.add("b");
        box.add();
        box.add("c");
        assertEqualLists(list("a", "    b", "", "    c"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIndentAmount() {
        CodeBox box = createCodeBox(2);
        box.add("a");
        box.indent();
        box.add("b");
        box.setIndentAmount(1);
        box.add("c");
        assertEqualLists(list("a", "  b", " c"), box.getLines());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEmptyFirstLine() {
        CodeBox box = createCodeBox();
        box.add();
        box.add("a");
        assertEqualLists(list("", "a"), box.getLines());
    }

    @Test(expected = IllegalStateException.class)
    @SuppressWarnings("javadoc")
    public void testIllegalDedent() {
        CodeBox box = createCodeBox();
        box.dedent();
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("javadoc")
    public void testIllegalIndentAmount1() {
        createCodeBox(0);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("javadoc")
    public void testIllegalIndentAmount2() {
        createCodeBox(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("javadoc")
    public void testIllegalIndentAmount3() {
        createCodeBox().setIndentAmount(0);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("javadoc")
    public void testIllegalIndentAmount4() {
        createCodeBox().setIndentAmount(-1);
    }
}
