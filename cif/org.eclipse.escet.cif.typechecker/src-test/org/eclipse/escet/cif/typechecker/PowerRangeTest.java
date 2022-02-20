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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifMath;
import org.junit.Test;

/** Power standard library function range tests. */
public class PowerRangeTest extends TwoArgsRangeTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testPower() {
        test(new Operator() {
            @Override
            public int op(int x, int y) throws CifEvalException {
                if (y < 0) {
                    throw new CifEvalException("pow(x, y), y < 0", null);
                }
                return CifMath.pow(x, y, null);
            }

            @Override
            public int[] getRange(int l1, int u1, int l2, int u2) {
                List<Integer> range1 = list(l1, u1);
                List<Integer> range2 = list(l2, u2);

                if (l1 <= 0 && 0 <= u1) {
                    range1.add(0);
                }
                if (l2 <= 0 && 0 <= u2) {
                    range2.add(0);
                }

                if (l1 + 1 <= u1) {
                    range1.add(l1 + 1);
                }
                if (l2 + 1 <= u2) {
                    range2.add(l2 + 1);
                }

                if (l1 <= u1 - 1) {
                    range1.add(u1 - 1);
                }
                if (l2 <= u2 - 1) {
                    range2.add(u2 - 1);
                }

                int minimum = Integer.MAX_VALUE;
                int maximum = Integer.MIN_VALUE;
                for (int x: range1) {
                    for (int y: range2) {
                        if (y < 0) {
                            continue;
                        }
                        int rslt;
                        try {
                            rslt = op(x, y);
                        } catch (CifEvalException e) {
                            throw new RuntimeException(e);
                        }
                        minimum = Math.min(minimum, rslt);
                        maximum = Math.max(maximum, rslt);
                    }
                }
                return new int[] {minimum, maximum};
            }

            @Override
            public String toString(String arg1, String arg2) {
                return fmt("pow(%s, %s)", arg1, arg2);
            }
        }, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testJavaPowMethod() {
        // Test to see whether the Math.pow method has the 'correct' semantics.
        assertEquals(1, (int)Math.pow(0, 0));
        assertEquals(0, (int)Math.pow(0, 1));
        assertEquals(0, (int)Math.pow(0, 2));

        assertEquals(1, (int)Math.pow(-2, 0));
        assertEquals(1, (int)Math.pow(-1, 0));
        assertEquals(1, (int)Math.pow(0, 0));
        assertEquals(1, (int)Math.pow(1, 0));
        assertEquals(1, (int)Math.pow(2, 0));
    }

    /** Allowed integer range values for the 'pow' function. */
    private static final int[][] POW_RANGES = { //
            {0, Integer.MIN_VALUE, Integer.MAX_VALUE}, //
            {1, Integer.MIN_VALUE, Integer.MAX_VALUE}, //
            {32, -1, 1}, //
            {33, -1, 1}, //
            {34, -1, 1}, //
            {35, -1, 1}, //
    };

    @Test
    @SuppressWarnings("javadoc")
    public void testPowOverflowNormal() {
        testPowOverflow(CifExprsTypeChecker.POW_RANGES);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testPowOverflowBoundaries() {
        testPowOverflow(POW_RANGES);
    }

    /**
     * Tests overflow ranges for the 'pow' function.
     *
     * @param ranges The ranges to check.
     */
    public void testPowOverflow(int[][] ranges) {
        final boolean DEBUG = false;

        final int MAX = Integer.MAX_VALUE;
        final int MIN = Integer.MIN_VALUE;

        for (int[] range: ranges) {
            int exp = range[0];
            int minBase = range[1];
            int maxBase = range[2];

            long minRslt = (long)Math.pow(minBase, exp);
            long maxRslt = (long)Math.pow(maxBase, exp);
            if (DEBUG) {
                System.out.format("pow(%s, %s) = %s, pow(%s, %s) = %s\n", minBase, exp, minRslt, maxBase, exp, maxRslt);
            }
            assertTrue(MIN <= minRslt && minRslt <= MAX);
            assertTrue(MIN <= maxRslt && maxRslt <= MAX);
        }

        for (int[] range: ranges) {
            int exp = range[0];
            int minBase = range[1];
            int maxBase = range[2];

            if (exp == 0) {
                continue;
            }

            long minRslt2 = (long)Math.pow((long)minBase - 1, exp);
            long maxRslt2 = (long)Math.pow((long)maxBase + 1, exp);
            if (DEBUG) {
                System.out.format("pow(%s, %s) = %s, pow(%s, %s) = %s\n", minBase - 1, exp, minRslt2, maxBase + 1, exp,
                        maxRslt2);
            }
            assertFalse(MIN <= minRslt2 && minRslt2 <= MAX);
            assertFalse(MIN <= maxRslt2 && maxRslt2 <= MAX);
        }
    }
}
