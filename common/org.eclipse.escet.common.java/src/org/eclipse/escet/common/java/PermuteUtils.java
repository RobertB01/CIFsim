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

/** Permutation related utility methods. */
public class PermuteUtils {
    /** Constructor for the {@link PermuteUtils} class. */
    private PermuteUtils() {
        // Static class.
    }

    /**
     * Returns an array with all the different permutations of the elements of the given array.
     *
     * @param a The given array.
     * @return All the different permutations.
     */
    public static int[][] permute(int[] a) {
        int permCnt = factorial(a.length);
        int[][] perms = new int[permCnt][a.length];
        permute(a, perms);
        return perms;
    }

    /**
     * Fills an array with all the different permutations of the elements of the given array.
     *
     * @param a The given array.
     * @param p The array to fill with all the different permutations. Must be an array of length {@code int[n!][n]},
     *     with {@code n} the length of array {@code a}, and {@code n!} the {@link #factorial} of {@code n}.
     */
    public static void permute(int[] a, int[][] p) {
        // This method implements Heap's algorithm, see
        // https://en.wikipedia.org/wiki/Heap%27s_algorithm.

        // Initialization.
        int n = a.length;
        int[] c = new int[n];

        // Original order is the first permutation.
        int pi = 0;
        System.arraycopy(a, 0, p[pi++], 0, n);

        // Add all the other permutations.
        for (int i = 1; i < n;) {
            if (c[i] < i) {
                // Get swap index.
                int k = i % 2 * c[i];

                // Swap two elements.
                int tmp = a[k];
                a[k] = a[i];
                a[i] = tmp;

                // Fill another permutation of the result.
                System.arraycopy(a, 0, p[pi++], 0, n);

                // Move on to the next permutation.
                c[i]++;
                i = 1;
            } else {
                c[i++] = 0;
            }
        }
    }

    /** The factorials of the numbers 0 to 12. */
    private static final int[] FACTORIALS = { //
            1, // 0
            1, // 1
            2, // 2
            6, // 3
            24, // 4
            120, // 5
            720, // 6
            5_040, // 7
            40_320, // 8
            362_880, // 9
            3_628_800, // 10
            39_916_800, // 11
            479_001_600, // 12
    };

    /**
     * Returns {@code n!}, the factorial of {@code n}, i.e. the product of the numbers in the range {@code [1 .. n]}.
     *
     * @param n The number for which to compute the factorial.
     * @return The factorial of {@code n}.
     * @throws IllegalArgumentException If {@code n} is negative.
     * @throws IllegalArgumentException If {@code n} is larger than {@code 12}, as then the result won't fit within an
     *     integer.
     */
    public static int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n < 0");
        }
        if (n > 12) {
            throw new IllegalArgumentException("n > 12");
        }
        return FACTORIALS[n];
    }
}
