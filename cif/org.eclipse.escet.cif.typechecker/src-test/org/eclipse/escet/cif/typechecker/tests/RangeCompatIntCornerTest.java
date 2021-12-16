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

package org.eclipse.escet.cif.typechecker.tests;

import static org.eclipse.escet.cif.common.CifTypeUtils.checkTypeCompat;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.junit.Test;

/** Range compatibility corner cases tests, for integer type ranges. */
public class RangeCompatIntCornerTest {
    /** {@code false}. */
    private static final boolean F = false;

    /** {@code true}. */
    private static final boolean T = true;

    /** The integer type range bounds to use for the tests. */
    private static final Integer[][] BOUNDS = { //
            {null, null}, //

            {Integer.MIN_VALUE, Integer.MAX_VALUE}, //
            {Integer.MIN_VALUE, Integer.MAX_VALUE - 1}, //
            {Integer.MIN_VALUE, Integer.MAX_VALUE - 2}, //

            {Integer.MIN_VALUE + 1, Integer.MAX_VALUE}, //
            {Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1}, //
            {Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 2}, //

            {Integer.MIN_VALUE + 2, Integer.MAX_VALUE}, //
            {Integer.MIN_VALUE + 2, Integer.MAX_VALUE - 1}, //
            {Integer.MIN_VALUE + 2, Integer.MAX_VALUE - 2}, //
    };

    /**
     * Tests the {@link CifTypeUtils#checkTypeCompat} method, for all combinations of the test {@link #BOUNDS bounds}.
     *
     * @param rangeCompat The type of range compatibility to use.
     * @return The range compatibility result for each combination of bounds.
     */
    private boolean[][] test(RangeCompat rangeCompat) {
        boolean[][] rslt = new boolean[BOUNDS.length][BOUNDS.length];
        for (int i1 = 0; i1 < BOUNDS.length; i1++) {
            for (int i2 = 0; i2 < BOUNDS.length; i2++) {
                Integer[] bound1 = BOUNDS[i1];
                Integer[] bound2 = BOUNDS[i2];

                assertEquals(2, bound1.length);
                assertEquals(2, bound2.length);

                IntType type1 = newIntType(bound1[0], null, bound1[1]);
                IntType type2 = newIntType(bound2[0], null, bound2[1]);

                rslt[i1][i2] = checkTypeCompat(type1, type2, rangeCompat);
            }
        }

        return rslt;
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIgnore() {
        boolean[][] actual = test(RangeCompat.IGNORE);
        boolean[][] expected = {
                // nn 00 01 02 10 11 12 20 21 22
                {T, T, T, T, T, T, T, T, T, T}, // nn
                {T, T, T, T, T, T, T, T, T, T}, // 00
                {T, T, T, T, T, T, T, T, T, T}, // 01
                {T, T, T, T, T, T, T, T, T, T}, // 02
                {T, T, T, T, T, T, T, T, T, T}, // 10
                {T, T, T, T, T, T, T, T, T, T}, // 11
                {T, T, T, T, T, T, T, T, T, T}, // 12
                {T, T, T, T, T, T, T, T, T, T}, // 20
                {T, T, T, T, T, T, T, T, T, T}, // 21
                {T, T, T, T, T, T, T, T, T, T}, // 22
        };
        assertArrayEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEqual() {
        boolean[][] actual = test(RangeCompat.EQUAL);
        boolean[][] expected = {
                // nn 00 01 02 10 11 12 20 21 22
                {T, T, F, F, F, F, F, F, F, F}, // nn
                {T, T, F, F, F, F, F, F, F, F}, // 00
                {F, F, T, F, F, F, F, F, F, F}, // 01
                {F, F, F, T, F, F, F, F, F, F}, // 02
                {F, F, F, F, T, F, F, F, F, F}, // 10
                {F, F, F, F, F, T, F, F, F, F}, // 11
                {F, F, F, F, F, F, T, F, F, F}, // 12
                {F, F, F, F, F, F, F, T, F, F}, // 20
                {F, F, F, F, F, F, F, F, T, F}, // 21
                {F, F, F, F, F, F, F, F, F, T}, // 22
        };
        assertArrayEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testContained() {
        boolean[][] actual = test(RangeCompat.CONTAINED);
        boolean[][] expected = {
                // nn 00 01 02 10 11 12 20 21 22 (second range)
                {T, T, T, T, T, T, T, T, T, T}, // nn (first range)
                {T, T, T, T, T, T, T, T, T, T}, // 00
                {F, F, T, T, F, T, T, F, T, T}, // 01
                {F, F, F, T, F, F, T, F, F, T}, // 02
                {F, F, F, F, T, T, T, T, T, T}, // 10
                {F, F, F, F, F, T, T, F, T, T}, // 11
                {F, F, F, F, F, F, T, F, F, T}, // 12
                {F, F, F, F, F, F, F, T, T, T}, // 20
                {F, F, F, F, F, F, F, F, T, T}, // 21
                {F, F, F, F, F, F, F, F, F, T}, // 22
        };
        assertArrayEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testOverlap() {
        boolean[][] actual = test(RangeCompat.OVERLAP);
        boolean[][] expected = {
                // nn 00 01 02 10 11 12 20 21 22
                {T, T, T, T, T, T, T, T, T, T}, // nn
                {T, T, T, T, T, T, T, T, T, T}, // 00
                {T, T, T, T, T, T, T, T, T, T}, // 01
                {T, T, T, T, T, T, T, T, T, T}, // 02
                {T, T, T, T, T, T, T, T, T, T}, // 10
                {T, T, T, T, T, T, T, T, T, T}, // 11
                {T, T, T, T, T, T, T, T, T, T}, // 12
                {T, T, T, T, T, T, T, T, T, T}, // 20
                {T, T, T, T, T, T, T, T, T, T}, // 21
                {T, T, T, T, T, T, T, T, T, T}, // 22
        };
        assertArrayEquals(expected, actual);
    }
}
