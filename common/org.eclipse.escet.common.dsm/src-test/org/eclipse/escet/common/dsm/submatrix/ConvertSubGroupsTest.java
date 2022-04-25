//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.dsm.submatrix.SubMatrixFunctions.computeAvailNodes;
import static org.eclipse.escet.common.dsm.submatrix.SubMatrixFunctions.convertSubGroups;
import static org.eclipse.escet.common.java.BitSets.makeBitset;
import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertEquals;

import java.util.BitSet;
import java.util.List;

import org.eclipse.escet.common.dsm.Group;
import org.eclipse.escet.common.dsm.Group.GroupType;
import org.junit.Test;

/**
 * Tests for the {@link SubMatrixFunctions#convertSubGroups} and {@link SubMatrixFunctions#computeAvailNodes} functions.
 */
public class ConvertSubGroupsTest {
    /** Constant group covering parent nodes 0 and 3. */
    public static final Group GROUP03 = new Group(GroupType.BUS, makeBitset(0, 3));

    /** Constant group covering parent nodes 1 and 2. */
    public static final Group GROUP12 = new Group(GroupType.BUS, makeBitset(1, 2));

    /** New group from a few nodes. */
    @Test
    public void newGroup() {
        SubNode[] subNodes = {new SingleParentNode(1), new SingleParentNode(2), new SingleParentNode(5),
                new SingleParentNode(6)};
        List<BitSet> subSets = list(makeBitset(0, 1), makeBitset(3)); // parent {1, 2} and parent {6} as subsets.
        List<Group> subGroups = convertSubGroups(subNodes, subSets, GroupType.CLUSTER);
        assertEquals(2, subGroups.size());

        assertEquals(0, subGroups.get(0).childGroups.size());
        assertEquals(makeBitset(1, 2), subGroups.get(0).localNodes);

        assertEquals(0, subGroups.get(1).childGroups.size());
        assertEquals(makeBitset(6), subGroups.get(1).localNodes);

        BitSet remaining = computeAvailNodes(subNodes, subSets);
        assertEquals(makeBitset(5), remaining);
    }

    /** An additional group is found. */
    @Test
    public void secondGroup() {
        SubNode[] subNodes = {new ParentGroupNode(GROUP12), new SingleParentNode(3), new SingleParentNode(5),
                new SingleParentNode(6)};
        List<BitSet> subSets = list(makeBitset(1, 2)); // parent {3, 5} as subset.
        List<Group> subGroups = convertSubGroups(subNodes, subSets, GroupType.CLUSTER);
        assertEquals(2, subGroups.size());

        assertEquals(0, subGroups.get(0).childGroups.size());
        assertEquals(makeBitset(3, 5), subGroups.get(0).localNodes);

        // Non-used parent groups are appended.
        assertEquals(0, subGroups.get(1).childGroups.size());
        assertEquals(makeBitset(1, 2), subGroups.get(1).localNodes);

        BitSet remaining = computeAvailNodes(subNodes, subSets);
        assertEquals(makeBitset(6), remaining);
    }

    /** New group, with previous parent group nested in it. */
    @Test
    public void nestingGroup() {
        SubNode[] subNodes = {new ParentGroupNode(GROUP12), new SingleParentNode(3), new SingleParentNode(5),
                new SingleParentNode(6)};
        List<BitSet> subSets = list(makeBitset(0, 1, 2)); // parent {{1, 2}, 3, 5} as subset.
        List<Group> subGroups = convertSubGroups(subNodes, subSets, GroupType.CLUSTER);
        assertEquals(1, subGroups.size());

        assertEquals(1, subGroups.get(0).childGroups.size());
        assertEquals(makeBitset(1, 2), subGroups.get(0).childGroups.get(0).members);
        assertEquals(makeBitset(3, 5), subGroups.get(0).localNodes);
        assertEquals(makeBitset(1, 2, 3, 5), subGroups.get(0).members);

        BitSet remaining = computeAvailNodes(subNodes, subSets);
        assertEquals(makeBitset(6), remaining);
    }
}
