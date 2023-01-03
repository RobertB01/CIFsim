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

package org.eclipse.escet.cif.cif2mcrl2.tree;

import org.eclipse.escet.cif.cif2mcrl2.InstanceTreeHelper;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.junit.Assert;
import org.junit.Test;

/** Tests of the instance tree parser. */
public class InstanceTreeTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testOk1() {
        TextNode n = InstanceTreeHelper.parseTreeText("( ( Ph19 fork19) Ph20)");
        CombinedTextNode cn = (CombinedTextNode)n;
        Assert.assertEquals(2, cn.children.size());
        ElementaryTextNode en = (ElementaryTextNode)cn.children.get(1);
        Assert.assertEquals("Ph20", en.name);

        cn = (CombinedTextNode)cn.children.get(0);
        Assert.assertEquals(2, cn.children.size());
        en = (ElementaryTextNode)cn.children.get(0);
        Assert.assertEquals("Ph19", en.name);
        en = (ElementaryTextNode)cn.children.get(1);
        Assert.assertEquals("fork19", en.name);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testOk2() {
        TextNode n = InstanceTreeHelper.parseTreeText("Ph19");
        ElementaryTextNode en = (ElementaryTextNode)n;
        Assert.assertEquals("Ph19", en.name);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testOk3() {
        TextNode n = InstanceTreeHelper.parseTreeText("(Ph19)");
        ElementaryTextNode en = (ElementaryTextNode)n;
        Assert.assertEquals("Ph19", en.name);
    }

    @Test(expected = InvalidInputException.class)
    @SuppressWarnings("javadoc")
    public void testFail1() { // No ')' at the end.
        InstanceTreeHelper.parseTreeText("(Ph19");
    }

    @Test(expected = InvalidInputException.class)
    @SuppressWarnings("javadoc")
    public void testFail2() { // No '(' at the start.
        InstanceTreeHelper.parseTreeText("Ph19)");
    }

    @Test(expected = InvalidInputException.class)
    @SuppressWarnings("javadoc")
    public void testFail3() { // '()' pair.
        InstanceTreeHelper.parseTreeText("Ph19 ()");
    }
}
