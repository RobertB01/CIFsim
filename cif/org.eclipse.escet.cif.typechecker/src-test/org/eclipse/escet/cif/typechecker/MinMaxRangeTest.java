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

import org.eclipse.escet.cif.common.CifMath;
import org.junit.Test;

/** Minimum/maximum standard library function range tests. */
public class MinMaxRangeTest extends TwoArgsRangeTestsBase {
    @Test
    @SuppressWarnings("javadoc")
    public void testMinimum() {
        test(new Operator() {
            @Override
            public int op(int x, int y) {
                return CifMath.min(x, y);
            }

            @Override
            public int[] getRange(int l1, int u1, int l2, int u2) {
                return new int[] {Math.min(l1, l2), Math.min(u1, u2)};
            }

            @Override
            public String toString(String arg1, String arg2) {
                return fmt("%s min %s", arg1, arg2);
            }
        }, false);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testMaximum() {
        test(new Operator() {
            @Override
            public int op(int x, int y) {
                return CifMath.max(x, y);
            }

            @Override
            public int[] getRange(int l1, int u1, int l2, int u2) {
                return new int[] {Math.max(l1, l2), Math.max(u1, u2)};
            }

            @Override
            public String toString(String arg1, String arg2) {
                return fmt("%s max %s", arg1, arg2);
            }
        }, false);
    }
}
