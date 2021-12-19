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

import static org.eclipse.escet.cif.common.CifTypeUtils.checkTypeCompat;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListType;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.junit.Test;

/** Range compatibility corner cases tests, for list type ranges. */
public class RangeCompatListCornerTest {
    /** {@code false}. */
    private static final Boolean F = false;

    /** {@code true}. */
    private static final Boolean T = true;

    /** The list type range bounds to use for the tests. */
    private static final Integer[][] BOUNDS = { //
            {null, null}, //

            {0, Integer.MAX_VALUE}, //
            {0, Integer.MAX_VALUE - 1}, //
            {0, Integer.MAX_VALUE - 2}, //

            {1, Integer.MAX_VALUE}, //
            {1, Integer.MAX_VALUE - 1}, //
            {1, Integer.MAX_VALUE - 2}, //

            {2, Integer.MAX_VALUE}, //
            {2, Integer.MAX_VALUE - 1}, //
            {2, Integer.MAX_VALUE - 2}, //
    };

    /**
     * Tests the {@link CifTypeUtils#checkTypeCompat} method, for all combinations of the test {@link #BOUNDS bounds}.
     *
     * @param rangeCompat The type of range compatibility to use.
     * @return The range compatibility result for each combination of bounds.
     */
    private Boolean[][] test(RangeCompat rangeCompat) {
        Boolean[][] rslt = new Boolean[BOUNDS.length][BOUNDS.length];
        for (int i1 = 0; i1 < BOUNDS.length; i1++) {
            for (int i2 = 0; i2 < BOUNDS.length; i2++) {
                Integer[] bound1 = BOUNDS[i1];
                Integer[] bound2 = BOUNDS[i2];

                assertEquals(2, bound1.length);
                assertEquals(2, bound2.length);

                BoolType bt1 = newBoolType();
                BoolType bt2 = newBoolType();

                ListType type1 = newListType(bt1, bound1[0], null, bound1[1]);
                ListType type2 = newListType(bt2, bound2[0], null, bound2[1]);

                rslt[i1][i2] = checkTypeCompat(type1, type2, rangeCompat);
            }
        }

        return rslt;
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIgnore() {
        Boolean[][] actual = test(RangeCompat.IGNORE);
        Boolean[][] expected = {
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
        Boolean[][] actual = test(RangeCompat.EQUAL);
        Boolean[][] expected = {
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
        Boolean[][] actual = test(RangeCompat.CONTAINED);
        Boolean[][] expected = {
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
        Boolean[][] actual = test(RangeCompat.OVERLAP);
        Boolean[][] expected = {
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
