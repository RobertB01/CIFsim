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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.escet.cif.common.CifMath;
import org.junit.Test;

/** Slicing range tests. */
public class SlicingRangeTest {
    /** List that is (partly) used as input for the slicing. */
    private List<Object> inputList;

    /** Test slicing result range. */
    @Test
    public void testExplicit() {
        // Enable/disable debug output.
        boolean debug = false;

        // Configure test case.
        int maxSize = 32;

        // Check test case.
        inputList = listc(maxSize);
        for (int i = 0; i <= maxSize; i++) {
            inputList.add(i);
        }
        int maxIdx = maxSize + 2;
        int minIdx = -maxIdx;

        // Get test values.
        List<Integer> sizes = range(0, maxSize);
        List<Integer> begins = concat(range(minIdx, maxIdx), (Integer)null);
        List<Integer> ends = concat(range(minIdx, maxIdx), (Integer)null);

        // Test for all value combinations.
        int success = 0;
        int failure = 0;
        for (int ll: sizes) {
            for (int lu: sizes) {
                // Skip invalid ranges.
                if (lu < ll) {
                    continue;
                }

                for (Integer b: begins) {
                    for (Integer e: ends) {
                        // list[l] t, int ([y]/omitted), int ([z]/omitted) -> list[w] t
                        // (with w the only possible result size)
                        if (ll == lu) {
                            boolean rslt = test(ll, lu, b, e, debug);
                            if (rslt) {
                                success++;
                            }
                            if (!rslt) {
                                failure++;
                            }
                            continue;
                        }

                        // list[l..u] t, int ([y]/omitted, 0 <= y <= l), int ([z]/omitted, 0 <= z <= l)
                        // -> list[v..w] t (with as narrow as possible v and w)
                        if (((b == null) || (0 <= b && b <= ll)) && ((e == null) || (0 <= e && e <= ll))) {
                            boolean rslt = test(ll, lu, b, e, debug);
                            if (rslt) {
                                success++;
                            }
                            if (!rslt) {
                                failure++;
                            }
                            continue;
                        }

                        // Remaining cases are trivial, and are not tested by
                        // this unit test.
                    }
                }
            }
        }

        // Print result, if debugging enabled.
        if (debug) {
            System.out.println("success: " + success);
            System.out.println("failure: " + failure);
        }
    }

    /**
     * Test slicing result range.
     *
     * @param l The lower bound on the size of the list (non-negative).
     * @param u The upper bound on the size of the list (non-negative).
     * @param b The begin index value, or {@code null}.
     * @param e The end index value, or {@code null}.
     * @param debug Whether to produce debug output ({@code true}) or not ({@code false}).
     * @return {@code true} if the test succeeded, {@code false} otherwise.
     */
    private boolean test(int l, int u, Integer b, Integer e, boolean debug) {
        // Initialize result range (experimental).
        int rl = Integer.MAX_VALUE;
        int ru = Integer.MIN_VALUE;

        // Determine result range (experimental).
        for (int ls = l; ls <= u; ls++) {
            List<Object> lst = inputList.subList(0, ls);
            List<Object> rslt = CifMath.slice(lst, b, e);
            int rs = rslt.size();
            rl = Math.min(rl, rs);
            ru = Math.max(ru, rs);
        }

        // Determine result range (formula).
        int lFormula;
        int uFormula;
        if (l == u) {
            int s = CifExprsTypeChecker.getSliceResultSize(l, b, e);
            lFormula = s;
            uFormula = s;
        } else {
            int[] rangeFormula;
            rangeFormula = CifExprsTypeChecker.getSliceResultRange(l, u, b, e);
            assertEquals(2, rangeFormula.length);
            lFormula = rangeFormula[0];
            uFormula = rangeFormula[1];
        }

        // Success or failure?
        boolean match = rl == lFormula && ru == uFormula;

        // Print test case, for debugging.
        if (debug) {
            String lstTxt = fmt("lst[%d..%d]", l, u);
            String bgnTxt = fmt("begin[%d]", b);
            String endTxt = fmt("end[%d]", e);
            System.out.format("%s %s %s = [%d..%d] %s= [%d..%d]\n", lstTxt, bgnTxt, endTxt, rl, ru, match ? "" : "!",
                    lFormula, uFormula);
        }

        // Check result.
        if (!debug) {
            assertTrue(match);
        }

        // Return result (success or failure).
        return match;
    }

    /**
     * Returns a list with all values in the range [lower .. upper].
     *
     * @param lower The lower bound (inclusive).
     * @param upper The upper bound (inclusive).
     * @return A list with all values in the range [lower .. upper].
     */
    private static List<Integer> range(int lower, int upper) {
        List<Integer> rslt = listc(upper - lower + 1);
        for (int i = lower; i <= upper; i++) {
            rslt.add(i);
        }
        return rslt;
    }
}
