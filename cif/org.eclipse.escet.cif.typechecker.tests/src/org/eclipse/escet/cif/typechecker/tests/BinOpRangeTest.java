//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.max;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.min;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.typechecker.CifExprsTypeChecker;
import org.junit.Test;

/** Binary operator range tests. */
public class BinOpRangeTest extends TwoArgsRangeTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testAddition() {
        test(new Operator() {
            @Override
            public int op(int x, int y) throws CifEvalException {
                return CifMath.add(x, y, null);
            }

            @Override
            public int[] getRange(int l1, int u1, int l2, int u2) {
                return new int[] {l1 + l2, u1 + u2};
            }

            @Override
            public String toString(String arg1, String arg2) {
                return fmt("%s + %s", arg1, arg2);
            }
        }, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSubtraction() {
        test(new Operator() {
            @Override
            public int op(int x, int y) throws CifEvalException {
                return CifMath.subtract(x, y, null);
            }

            @Override
            public int[] getRange(int l1, int u1, int l2, int u2) {
                return new int[] {l1 - u2, u1 - l2};
            }

            @Override
            public String toString(String arg1, String arg2) {
                return fmt("%s - %s", arg1, arg2);
            }
        }, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMultiplication() {
        test(new Operator() {
            @Override
            public int op(int x, int y) throws CifEvalException {
                return CifMath.multiply(x, y, null);
            }

            @Override
            public int[] getRange(int l1, int u1, int l2, int u2) {
                return new int[] {(int)min(l1 * l2, l1 * u2, u1 * l2, u1 * u2),
                        (int)max(l1 * l2, l1 * u2, u1 * l2, u1 * u2)};
            }

            @Override
            public String toString(String arg1, String arg2) {
                return fmt("%s * %s", arg1, arg2);
            }
        }, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerDivision() {
        test(new Operator() {
            @Override
            public int op(int x, int y) throws CifEvalException {
                return CifMath.div(x, y, null);
            }

            @Override
            public int[] getRange(int l1, int u1, int l2, int u2) {
                return CifExprsTypeChecker.getDivResultRange(l1, u1, l2, u2);
            }

            @Override
            public String toString(String arg1, String arg2) {
                return fmt("%s div %s", arg1, arg2);
            }
        }, false);
    }
}
