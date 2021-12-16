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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListType;
import static org.junit.Assert.assertEquals;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.junit.Test;

/** Range compatibility tests, for list type ranges. */
public class RangeCompatListTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testEqual() {
        RangeCompatTestImpl testImpl = new RangeCompatTestImpl() {
            @Override
            public boolean checkTypeCompat(ListType type1, ListType type2) {
                for (int i = -2; i <= 7; i++) {
                    int l1 = type1.getLower();
                    int l2 = type2.getLower();
                    int u1 = type1.getUpper();
                    int u2 = type2.getUpper();
                    boolean inFirst = l1 <= i && i <= u1;
                    boolean inSecond = l2 <= i && i <= u2;
                    if (inSecond != inFirst) {
                        return false;
                    }
                }
                return true;
            }
        };
        testRangeCompat(RangeCompat.EQUAL, testImpl, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testOverlap() {
        RangeCompatTestImpl testImpl = new RangeCompatTestImpl() {
            @Override
            public boolean checkTypeCompat(ListType type1, ListType type2) {
                for (int i = -2; i <= 7; i++) {
                    int l1 = type1.getLower();
                    int l2 = type2.getLower();
                    int u1 = type1.getUpper();
                    int u2 = type2.getUpper();
                    if (l1 <= i && i <= u1 && l2 <= i && i <= u2) {
                        return true;
                    }
                }
                return false;
            }
        };
        testRangeCompat(RangeCompat.OVERLAP, testImpl, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testContained() {
        RangeCompatTestImpl testImpl = new RangeCompatTestImpl() {
            @Override
            public boolean checkTypeCompat(ListType type1, ListType type2) {
                for (int i = -2; i <= 7; i++) {
                    int l1 = type1.getLower();
                    int l2 = type2.getLower();
                    int u1 = type1.getUpper();
                    int u2 = type2.getUpper();
                    boolean inFirst = l1 <= i && i <= u1;
                    boolean inSecond = l2 <= i && i <= u2;
                    if (inSecond && !inFirst) {
                        return false;
                    }
                }
                return true;
            }
        };
        testRangeCompat(RangeCompat.CONTAINED, testImpl, false);
    }

    /**
     * Generic range compatibility unit test.
     *
     * @param rangeCompat The type of range compatibility to check.
     * @param testImpl The naive/simple range compatibility test implementation to use.
     * @param debug Whether to output debugging information to the console.
     */
    public void testRangeCompat(RangeCompat rangeCompat, RangeCompatTestImpl testImpl, boolean debug) {
        for (int l1 = 0; l1 <= 5; l1++) {
            for (int l2 = 0; l2 <= 5; l2++) {
                for (int u1 = 0; u1 <= 5; u1++) {
                    for (int u2 = 0; u2 <= 5; u2++) {
                        // Skip empty/invalid ranges.
                        if (l1 > u1) {
                            continue;
                        }
                        if (l2 > u2) {
                            continue;
                        }

                        // Construct list types.
                        ListType type1 = newListType();
                        ListType type2 = newListType();
                        type1.setLower(l1);
                        type2.setLower(l2);
                        type1.setUpper(u1);
                        type2.setUpper(u2);
                        type1.setElementType(newBoolType());
                        type2.setElementType(newBoolType());

                        // Check answers.
                        boolean rslt1 = CifTypeUtils.checkTypeCompat(type1, type2, rangeCompat);
                        boolean rslt2 = testImpl.checkTypeCompat(type1, type2);
                        if (debug) {
                            System.out.format("%s, [%d..%d], [%d..%d], rslt1 = %s, rslt2 = %s, %s\n", rangeCompat, l1,
                                    u1, l2, u2, rslt1, rslt2, (rslt1 == rslt2) ? "OK" : "WRONG");
                        }
                        assertEquals(rslt2, rslt1);
                    }
                }
            }
        }
    }

    /** Naive/simple range compatibility test implementation. */
    interface RangeCompatTestImpl {
        /**
         * Checks range compatibility in the simplest way possible, for unit testing only. Uses naive, inefficient
         * implementation.
         *
         * @param type1 The first type.
         * @param type2 The second type.
         * @return {@code true} if the ranges are compatible, {@code false} otherwise.
         */
        public boolean checkTypeCompat(ListType type1, ListType type2);
    }
}
