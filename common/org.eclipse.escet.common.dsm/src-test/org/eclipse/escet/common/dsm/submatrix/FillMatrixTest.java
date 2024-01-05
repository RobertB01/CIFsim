//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.submatrix;

import static org.eclipse.escet.common.dsm.submatrix.SubMatrixFunctions.fillSubMatrix;
import static org.eclipse.escet.common.java.BitSets.makeBitset;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.dsm.Group;
import org.eclipse.escet.common.dsm.Group.GroupType;
import org.junit.jupiter.api.Test;

/** Tests for the {@link SubMatrixFunctions#fillSubMatrix} function. */
public class FillMatrixTest {
    /** Value that should not get copied. */
    public static final double BAD = 100000;

    /** Constant group covering parent nodes 0 and 3. */
    public static final Group GROUP03 = new Group(GroupType.BUS, makeBitset(0, 3));

    /** Constant group covering parent nodes 1 and 2. */
    public static final Group GROUP12 = new Group(GroupType.BUS, makeBitset(1, 2));

    @SuppressWarnings("javadoc")
    @Test
    public void nodeNodeFillTest() {
        SubNode[] subNodes = {new SingleParentNode(1), new SingleParentNode(2), new SingleParentNode(3)};

        double[][] matValues = {{BAD, BAD, BAD, BAD}, {BAD, BAD, 1, 4}, {BAD, 10, BAD, 200}, {BAD, 100, 40, BAD}};
        double[][] expected = {{0, 1, 4}, {10, 0, 200}, {100, 40, 0}};
        RealMatrix parent = new BlockRealMatrix(matValues);
        RealMatrix sub = fillSubMatrix(parent, subNodes);

        assertEquals(3, sub.getRowDimension());
        assertTrue(sub.isSquare());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals((int)expected[i][j], (int)sub.getEntry(i, j), fmt("entry (%d, %d)", i, j));
            }
        }
    }

    @SuppressWarnings("javadoc")
    @Test
    public void nodeGroupFillTest() {
        SubNode[] subNodes = {new ParentGroupNode(GROUP12), new SingleParentNode(3)};

        double[][] matValues = {{BAD, BAD, BAD, BAD}, {BAD, BAD, 200, 1}, {BAD, 100, BAD, 2}, {BAD, 10, 20, BAD}};
        double[][] expected = {{0, 3}, {30, 0}};
        RealMatrix parent = new BlockRealMatrix(matValues);
        RealMatrix sub = fillSubMatrix(parent, subNodes);

        assertEquals(2, sub.getRowDimension());
        assertTrue(sub.isSquare());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals((int)expected[i][j], (int)sub.getEntry(i, j), fmt("entry (%d, %d)", i, j));
            }
        }
    }

    @SuppressWarnings("javadoc")
    @Test
    public void groupGroupFillTest() {
        SubNode[] subNodes = {new ParentGroupNode(GROUP12), new ParentGroupNode(GROUP03)};

        double[][] matValues = {{BAD, 100, 200, BAD}, {10, BAD, 1, 40}, {20, 2, BAD, 80}, {BAD, 400, 800, BAD}};
        double[][] expected = {{0, 150}, {1500, 0}};
        RealMatrix parent = new BlockRealMatrix(matValues);
        RealMatrix sub = fillSubMatrix(parent, subNodes);

        assertEquals(2, sub.getRowDimension());
        assertTrue(sub.isSquare());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals((int)expected[i][j], (int)sub.getEntry(i, j), fmt("entry (%d, %d)", i, j));
            }
        }
    }
}
