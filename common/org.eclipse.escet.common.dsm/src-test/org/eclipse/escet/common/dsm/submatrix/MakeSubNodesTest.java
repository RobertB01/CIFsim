//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.dsm.submatrix.SubMatrixFunctions.makeSubNodes;
import static org.eclipse.escet.common.java.BitSets.bitset;
import static org.eclipse.escet.common.java.BitSets.makeBitset;
import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;
import java.util.List;

import org.eclipse.escet.common.dsm.Group;
import org.eclipse.escet.common.dsm.Group.GroupType;
import org.junit.jupiter.api.Test;

/** Tests for construction a sub nodes array. */
public class MakeSubNodesTest {
    /** Constant group covering parent nodes 1 and 2. */
    public static final Group GROUP12 = new Group(GroupType.BUS, makeBitset(1, 2));

    /** Constant group covering parent nodes 0, 5, 6, and 8. */
    public static final Group GROUP0568 = new Group(GroupType.BUS, makeBitset(0, 5, 6, 8));

    /** Constant group covering parent node 7. (Is unrealistic, but code does not check soundness of groups.) */
    public static final Group GROUP7 = new Group(GroupType.BUS, makeBitset(7));

    /** Constant group which is {@link #GROUP12} combined with {@link #GROUP7}. */
    public static final Group MERGED = new Group(GroupType.COLLECTION, null, list(GROUP12, GROUP7));

    /** Test adding all non-group nodes. */
    @Test
    public void allNongroupNodesTest() {
        int[] indices = {0, 1, 2, 3};
        BitSet availNodes = makeBitset(indices);
        SubNode[] subNodes = makeSubNodes(list(), availNodes);
        assertEquals(indices.length, subNodes.length);
        int idx = 0;
        for (SubNode sn: subNodes) {
            assertTrue(sn instanceof SingleParentNode);
            assertEquals(indices[idx], sn.firstParentNode(0));
            idx++;
        }
    }

    /** Test skipping a few non-group nodes. */
    @Test
    public void someNongroupNodesTest() {
        int[] indices = {0, 2, 3, 7, 8, 11};
        BitSet availNodes = makeBitset(indices);
        SubNode[] subNodes = makeSubNodes(list(), availNodes);
        assertEquals(indices.length, subNodes.length);
        int idx = 0;
        for (SubNode sn: subNodes) {
            assertTrue(sn instanceof SingleParentNode);
            assertEquals(indices[idx], sn.firstParentNode(0));
            idx++;
        }
    }

    /** Test with a single group. */
    @Test
    public void singleGroupTest() {
        List<Group> prevGroups = list(GROUP12);
        BitSet availNodes = bitset();
        SubNode[] subNodes = makeSubNodes(prevGroups, availNodes);
        assertEquals(1, subNodes.length); // Each group gets 1 node in the sub matrix.
        assertTrue(subNodes[0] instanceof ParentGroupNode);
        assertEquals(GROUP12, subNodes[0].getGroup());
    }

    /** Test with a nested group. */
    @Test
    public void nestedGroupTest() {
        List<Group> prevGroups = list(MERGED);
        BitSet availNodes = bitset();
        SubNode[] subNodes = makeSubNodes(prevGroups, availNodes);
        assertEquals(1, subNodes.length); // Each group gets 1 node in the sub matrix.
        assertTrue(subNodes[0] instanceof ParentGroupNode);
        assertEquals(MERGED, subNodes[0].getGroup());
    }

    /** Test with a multiple groups. */
    @Test
    public void multiGroupTest() {
        List<Group> prevGroups = list(GROUP12, GROUP0568);
        BitSet availNodes = bitset();
        SubNode[] subNodes = makeSubNodes(prevGroups, availNodes);
        assertEquals(2, subNodes.length); // Each group gets 1 node in the sub matrix.
        assertTrue(subNodes[0] instanceof ParentGroupNode);
        assertEquals(GROUP12, subNodes[0].getGroup());

        assertTrue(subNodes[1] instanceof ParentGroupNode);
        assertEquals(GROUP0568, subNodes[1].getGroup());
    }

    /** Test with both a group and some nodes. */
    @Test
    public void combinedGroupNodesTest() {
        List<Group> prevGroups = list(GROUP12);
        BitSet availNodes = makeBitset(0, 7);
        SubNode[] subNodes = makeSubNodes(prevGroups, availNodes);
        assertEquals(3, subNodes.length);
        assertTrue(subNodes[0] instanceof ParentGroupNode);
        assertEquals(GROUP12, subNodes[0].getGroup());

        assertTrue(subNodes[1] instanceof SingleParentNode);
        assertEquals(0, subNodes[1].firstParentNode(0));

        assertTrue(subNodes[2] instanceof SingleParentNode);
        assertEquals(7, subNodes[2].firstParentNode(0));
    }
}
