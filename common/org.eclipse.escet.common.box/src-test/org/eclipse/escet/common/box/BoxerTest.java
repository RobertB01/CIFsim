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

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Unit tests for the {@link Boxer} class. */
public class BoxerTest extends BoxTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testDefaultBoxer() {
        TestClass testClass = new TestClass();
        List<String> lines1 = testClass.toBox().getLines();
        List<String> lines2 = Boxer.DEFAULT.box(testClass).getLines();
        assertEqualLists(lines1, lines2);
    }

    /** Boxable test class. */
    protected class TestClass implements Boxable {
        @Override
        public Box toBox() {
            return new TextBox("TestClass");
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntBoxer() {
        int x = 132;
        List<String> expected = list("Integer(132)");
        List<String> actual = new IntBoxer().box(x).getLines();
        assertEqualLists(expected, actual);
    }

    /** Boxer that can box integers. */
    public static class IntBoxer implements Boxer<Integer> {
        @Override
        public Box box(Integer obj) {
            return new TextBox("Integer(" + obj + ")");
        }
    }
}
