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

package org.eclipse.escet.cif.typechecker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifMath;
import org.junit.jupiter.api.Test;

/**
 * Standard library function range tests. Note that the 'pow' function is tested in the {@link PowerRangeTest} class,
 * and 'min' and 'max' are tested in the {@link BinOpRangeTest} class.
 */
public class StdLibFuncRangeTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testAbs() {
        testFunc(new Function() {
            @Override
            public int f(int x) throws CifEvalException {
                return CifMath.abs(x, null);
            }

            @Override
            public int[] getRange(int l, int u) {
                return CifExprsTypeChecker.getAbsResultRange(l, u);
            }

            @Override
            public String toString() {
                return "abs";
            }
        }, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSign() {
        testFunc(new Function() {
            @Override
            public int f(int x) {
                return CifMath.sign(x);
            }

            @Override
            public int[] getRange(int l, int u) {
                return new int[] {f(l), f(u)};
            }

            @Override
            public String toString() {
                return "sign";
            }
        }, false);
    }

    /** Function. */
    public abstract class Function {
        /**
         * Calculate the result of using the function.
         *
         * @param x The argument value.
         * @return The result of {@code f(x)}.
         * @throws CifEvalException In case of evaluation failure.
         */
        public abstract int f(int x) throws CifEvalException;

        /**
         * Calculate the result range from the input range.
         *
         * @param l The argument lower bound.
         * @param u The argument upper bound.
         * @return An array with the result range lower and upper bound values.
         */
        public abstract int[] getRange(int l, int u);

        @Override
        public abstract String toString();
    }

    /**
     * Test function operator result range.
     *
     * @param f The function to test.
     * @param debug Whether to print debugging output.
     */
    private void testFunc(Function f, boolean debug) {
        int cnt = 0;
        int success = 0;
        int failure = 0;
        for (int l = -5; l <= 5; l++) {
            for (int u = -5; u <= 5; u++) {
                // Skip invalid ranges.
                if (u < l) {
                    continue;
                }

                // Init result range.
                int lRslt = Integer.MAX_VALUE;
                int uRslt = Integer.MIN_VALUE;

                // Determine result range (experimental).
                for (int x = l; x <= u; x++) {
                    int rslt;
                    try {
                        rslt = f.f(x);
                    } catch (CifEvalException e) {
                        // Skip divide by zero exceptions, etc.
                        continue;
                    }
                    lRslt = Math.min(lRslt, rslt);
                    uRslt = Math.max(uRslt, rslt);
                }

                // Determine result range (formula).
                int[] rangeFormula = f.getRange(l, u);
                assertEquals(2, rangeFormula.length);
                int lFormula = rangeFormula[0];
                int uFormula = rangeFormula[1];

                // Success or failure?
                boolean match = lRslt == lFormula && uRslt == uFormula;
                if (match) {
                    success++;
                }
                if (!match) {
                    failure++;
                }

                // Print test case, for debugging.
                if (debug) {
                    System.out.format("%s([%d..%d]) = [%d..%d] %s= [%d..%d]\n", f.toString(), l, u, lRslt, uRslt,
                            match ? "" : "!", lFormula, uFormula);
                }

                // Check ranges.
                if (!debug) {
                    assertEquals(lRslt, lFormula);
                    assertEquals(uRslt, uFormula);
                }

                // Count number of ranges checked.
                cnt++;
            }
        }
        assertEquals(66, cnt);

        if (debug) {
            System.out.println("success: " + success);
            System.out.println("failure: " + failure);
        }
    }
}
