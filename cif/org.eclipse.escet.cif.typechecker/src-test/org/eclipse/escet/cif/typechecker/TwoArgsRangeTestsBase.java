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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.Assert.assertEquals;

import org.eclipse.escet.cif.common.CifEvalException;

/** Base class for range tests on operators/functions with two arguments. */
public abstract class TwoArgsRangeTestsBase {
    /** Binary operator or function with two arguments. */
    public abstract class Operator {
        /**
         * Calculate the result of using the operator/function.
         *
         * @param x The left child value.
         * @param y The right child value.
         * @return The result of {@code x op y} or {@code op(x, y)}.
         * @throws CifEvalException In case of evaluation failures.
         */
        public abstract int op(int x, int y) throws CifEvalException;

        /**
         * Calculate the result range from the input ranges.
         *
         * @param l1 The left child lower bound.
         * @param u1 The left child upper bound.
         * @param l2 The right child lower bound.
         * @param u2 The right child upper bound.
         * @return An array with the result range lower and upper bound values.
         */
        public abstract int[] getRange(int l1, int u1, int l2, int u2);

        @Override
        public String toString() {
            throw new UnsupportedOperationException();
        }

        /**
         * Returns a textual representation of the application of this operator/function to the given arguments.
         *
         * @param arg1 The first argument.
         * @param arg2 The second argument.
         * @return The textual representation.
         */
        public abstract String toString(String arg1, String arg2);
    }

    /**
     * Test binary operator/function result range.
     *
     * @param op The operator to test.
     * @param debug Whether to print debugging output.
     */
    protected void test(Operator op, boolean debug) {
        int cnt = 0;
        int success = 0;
        int failure = 0;
        for (int l1 = -5; l1 <= 5; l1++) {
            for (int u1 = -5; u1 <= 5; u1++) {
                for (int l2 = -5; l2 <= 5; l2++) {
                    for (int u2 = -5; u2 <= 5; u2++) {
                        // Skip invalid ranges.
                        if (u1 < l1) {
                            continue;
                        }
                        if (u2 < l2) {
                            continue;
                        }

                        // Init result range.
                        int lRslt = Integer.MAX_VALUE;
                        int uRslt = Integer.MIN_VALUE;

                        // Determine result range (experimental).
                        for (int x = l1; x <= u1; x++) {
                            for (int y = l2; y <= u2; y++) {
                                int rslt;
                                try {
                                    rslt = op.op(x, y);
                                } catch (CifEvalException e) {
                                    // Skip divide by zero etc exceptions.
                                    continue;
                                }
                                lRslt = Math.min(lRslt, rslt);
                                uRslt = Math.max(uRslt, rslt);
                            }
                        }

                        // Determine result range (formula).
                        int[] rangeFormula = op.getRange(l1, u1, l2, u2);
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
                            String range1 = fmt("[%d..%d]", l1, u1);
                            String range2 = fmt("[%d..%d]", l2, u2);
                            System.out.format("%s = [%d..%d] %s= [%d..%d]\n", op.toString(range1, range2), lRslt, uRslt,
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
            }
        }
        assertEquals(66 * 66, cnt);

        if (debug) {
            System.out.println("success: " + success);
            System.out.println("failure: " + failure);
        }
    }
}
