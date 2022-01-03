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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Sets.setc;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

/** Unit tests for the {@link PermuteUtils} class. */
public class PermuteUtilsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testPermute() {
        int[] values = {2, 4, 6, 8};
        for (int i = 0; i <= 4; i++) {
            // Get input array for which to compute permutations.
            int[] input = new int[i];
            System.arraycopy(values, 0, input, 0, i);

            // Get permutations.
            int[][] perms = PermuteUtils.permute(input);

            // Get expected permutations.
            int[][] expecteds;
            switch (i) {
                case 0:
                    expecteds = new int[][] {{}};
                    break;

                case 1:
                    expecteds = new int[][] {{values[0]}};
                    break;

                case 2:
                    expecteds = new int[][] {{values[0], values[1]}, {values[1], values[0]}};
                    break;

                case 3:
                    expecteds = new int[][] { //
                            {values[0], values[1], values[2]},
                            {values[0], values[2], values[1]},

                            {values[1], values[0], values[2]},
                            {values[1], values[2], values[0]},

                            {values[2], values[0], values[1]},
                            {values[2], values[1], values[0]}};
                    break;

                case 4:
                    expecteds = new int[][] { //
                            {values[0], values[1], values[2], values[3]},

                            {values[0], values[1], values[3], values[2]},
                            {values[0], values[2], values[1], values[3]},
                            {values[0], values[2], values[3], values[1]},
                            {values[0], values[3], values[1], values[2]},
                            {values[0], values[3], values[2], values[1]},

                            {values[1], values[0], values[2], values[3]},
                            {values[1], values[0], values[3], values[2]},
                            {values[1], values[2], values[0], values[3]},
                            {values[1], values[2], values[3], values[0]},
                            {values[1], values[3], values[0], values[2]},
                            {values[1], values[3], values[2], values[0]},

                            {values[2], values[0], values[1], values[3]},
                            {values[2], values[0], values[3], values[1]},
                            {values[2], values[1], values[0], values[3]},
                            {values[2], values[1], values[3], values[0]},
                            {values[2], values[3], values[0], values[1]},
                            {values[2], values[3], values[1], values[0]},

                            {values[3], values[0], values[1], values[2]},
                            {values[3], values[0], values[2], values[1]},
                            {values[3], values[1], values[0], values[2]},
                            {values[3], values[1], values[2], values[0]},
                            {values[3], values[2], values[0], values[1]},
                            {values[3], values[2], values[1], values[0]}};
                    break;

                default:
                    throw new RuntimeException("Unexpected i: " + i);
            }

            // Check actual against expected, as set, as order doesn't matter.
            Set<List<Integer>> expectedSet = setc(perms.length);
            for (int[] perm: expecteds) {
                expectedSet.add(Arrays.asList(ArrayUtils.toObject(perm)));
            }

            Set<List<Integer>> actualSet = setc(perms.length);
            for (int[] perm: perms) {
                actualSet.add(Arrays.asList(ArrayUtils.toObject(perm)));
            }

            assertEquals(expectedSet, actualSet);
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testPermuteUnique() {
        // Test for range [0..9] instead of [0..12], as otherwise takes to
        // long. We assume it then also works for larger arrays, with length in
        // range [10..12].
        int[] values = {2, 4, 6, 8, 10, 12, 14, 16, 18};
        for (int i = 0; i <= 9; i++) {
            // Get input array for which to compute permutations.
            int[] input = new int[i];
            System.arraycopy(values, 0, input, 0, i);

            // Get permutations.
            int[][] perms = PermuteUtils.permute(input);

            // Check number of permutations.
            assertEquals(PermuteUtils.factorial(i), perms.length);

            // Check unique numbers per permutation, and only those numbers
            // from the input. Since the input numbers are unique, removing
            // the input numbers from the set of numbers of the permutation
            // and checking for empty is enough.
            for (int[] perm: perms) {
                Set<Integer> numbers = setc(perm.length);
                for (int nr: perm) {
                    numbers.add(nr);
                }
                for (int nr: input) {
                    numbers.remove(nr);
                }
                assertEquals(true, numbers.isEmpty());
            }

            // Check for unique permutations.
            Set<List<Integer>> uniquePerms = setc(perms.length);
            for (int[] perm: perms) {
                uniquePerms.add(Arrays.asList(ArrayUtils.toObject(perm)));
            }
            assertEquals(perms.length, uniquePerms.size());
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFactorial() {
        for (int i = 0; i <= 12; i++) {
            assertEquals(factorial(i), PermuteUtils.factorial(i));
        }
    }

    /**
     * Slow but simple recursive implementation of the factorial function.
     *
     * @param n The number for which to compute the factorial. Must be in range {@code [0 .. 12]}.
     * @return The factorial.
     */
    private static int factorial(int n) {
        return (n == 0) ? 1 : n * factorial(n - 1);
    }
}
