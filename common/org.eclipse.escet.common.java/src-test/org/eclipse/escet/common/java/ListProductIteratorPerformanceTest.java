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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Performance tests for the {@link ListProductIterator}. Compares the performance for the case where all sub-lists have
 * exactly one element, for a manually computed list with the single iteration result, versus using the
 * {@link ListProductIterator}. That is, it tries to determine the overhead of using the {@link ListProductIterator},
 * when the iterator will only result in one result item.
 *
 * <p>
 * Tests must be manually enabled, since they produce console output.
 * </p>
 */
public class ListProductIteratorPerformanceTest {
    /** Number of tests to perform. */
    public static final int COUNT = 1000 * 1000;

    /** Number of sub-lists to use. Must be even! */
    public static final int LIST_SIZE = 100;

    /** The input. */
    public static final List<List<Integer>> INPUT;

    static {
        // Fill the input data.
        INPUT = listc(LIST_SIZE);
        for (int i = 0; i < LIST_SIZE; i++) {
            INPUT.add(list(i));
        }
    }

    /** Test the list product iterator. */
    // @Test
    public void testProductIterator() {
        long t = System.nanoTime();
        for (int c = 0; c < COUNT; c++) {
            test(new ListProductIterator<>(INPUT));
        }
        System.out.println("proditer: " + ((System.nanoTime() - t) / 1e6));
    }

    /** Test the manually constructed list. */
    // @Test
    public void testListIterator() {
        long t = System.nanoTime();
        for (int c = 0; c < COUNT; c++) {
            List<Integer> lst = listc(INPUT.size());
            for (int i = 0; i < INPUT.size(); i++) {
                lst.add(INPUT.get(i).get(0));
            }
            test(list(lst).iterator());
        }
        System.out.println("listiter: " + ((System.nanoTime() - t) / 1e6));
    }

    /** Dummy test. */
    @Test
    public void testDummy() {
        // Dummy test to avoid warnings about org.junit.Test not being used,
        // when the actual tests are disabled.
    }

    /**
     * Iterate over the iterator, and add the resulting values together. Make sure it gives the expected result.
     *
     * @param iter The iterator to use.
     */
    private void test(Iterator<List<Integer>> iter) {
        // Iterate until depleted. Add resulting values together.
        int rslt = 0;
        while (iter.hasNext()) {
            List<Integer> lst = iter.next();
            for (int i: lst) {
                rslt += i;
            }
        }

        // Compute expected result.
        int half = LIST_SIZE / 2;
        int expected = half * (LIST_SIZE - 1);

        // Compare actual result to expected result.
        assertEquals(expected, rslt);
    }
}
