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

package org.eclipse.escet.cif.cif2mcrl2.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.escet.cif.cif2mcrl2.InstanceTreeHelper;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;

/** Tests of the instance tree parser. */
public class InstanceTreeTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testOk1() {
        TextNode n = InstanceTreeHelper.parseTreeText("( ( Ph19 fork19) Ph20)");
        CombinedTextNode cn = (CombinedTextNode)n;
        assertEquals(2, cn.children.size());
        ElementaryTextNode en = (ElementaryTextNode)cn.children.get(1);
        assertEquals("Ph20", en.name);

        cn = (CombinedTextNode)cn.children.get(0);
        assertEquals(2, cn.children.size());
        en = (ElementaryTextNode)cn.children.get(0);
        assertEquals("Ph19", en.name);
        en = (ElementaryTextNode)cn.children.get(1);
        assertEquals("fork19", en.name);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testOk2() {
        TextNode n = InstanceTreeHelper.parseTreeText("Ph19");
        ElementaryTextNode en = (ElementaryTextNode)n;
        assertEquals("Ph19", en.name);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testOk3() {
        TextNode n = InstanceTreeHelper.parseTreeText("(Ph19)");
        ElementaryTextNode en = (ElementaryTextNode)n;
        assertEquals("Ph19", en.name);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFail1() { // No ')' at the end.
        assertThrows(InvalidInputException.class, () -> InstanceTreeHelper.parseTreeText("(Ph19"));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFail2() { // No '(' at the start.
        assertThrows(InvalidInputException.class, () -> InstanceTreeHelper.parseTreeText("Ph19)"));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFail3() { // '()' pair.
        assertThrows(InvalidInputException.class, () -> InstanceTreeHelper.parseTreeText("Ph19 ()"));
    }
}
