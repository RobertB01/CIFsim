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

package org.eclipse.escet.common.java.tests;

import static java.util.Collections.EMPTY_LIST;
import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.escet.common.java.ListProductIterator;
import org.junit.Test;

/** Unit tests for the {@link ListProductIterator} class. */
@SuppressWarnings("unchecked")
public class ListProductIteratorTest {
    /** Test input. */
    public List<List<Integer>> input;

    /** Expected test output. */
    public List<List<Integer>> output;

    /** Test empty input. */
    @Test
    public void testProductIteratorEmpty() {
        input = list();
        output = list();
        test();
    }

    /** Test sub-lists with single elements. */
    @Test
    public void testProductIteratorSingle() {
        input = list(list(1), list(2), list(3));
        output = list(list(1, 2, 3));
        test();
    }

    /** Test with empty and non-empty sub-lists. */
    @Test
    public void testProductIteratorEmptyNonEmpty() {
        input = list(list(1, 2), EMPTY_LIST, list(3));
        output = list();
        test();
    }

    /** Test reasonably complex/interesting input. */
    @Test
    public void testProductIteratorComplex() {
        input = list(list(1, 2, 3), list(4), list(5, 6));
        output = list(list(1, 4, 5), list(1, 4, 6), list(2, 4, 5), list(2, 4, 6), list(3, 4, 5), list(3, 4, 6));
        test();
    }

    /** Test the {@link ListProductIterator} on {@link #input} and make sure it has the expected {@link #output}. */
    private void test() {
        // Construct iterator.
        ListProductIterator<Integer> iter;
        iter = new ListProductIterator<>(input);

        // Query iterator until completion, and store obtained results.
        List<List<Integer>> rslt = list();
        while (iter.hasNext()) {
            List<Integer> lst = iter.next();
            rslt.add(lst);
        }

        // Check expected output against actual output.
        assertEquals(output, rslt);
    }
}
