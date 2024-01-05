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

import static org.eclipse.escet.common.java.BitSets.iterateTrueBits;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.BitSet;
import java.util.List;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.dsm.Group;
import org.eclipse.escet.common.dsm.Group.GroupType;
import org.eclipse.escet.common.java.Assert;

/**
 * Functions around constructing sub matrices from a parent matrix, and converting grouped sub matrix results back to
 * the parent.
 *
 * <p>
 * The {@link #makeSubNodes} function takes the top-level already existing groups of the parent matrix as well as the
 * set of nodes available for grouping (which should at least exclude nodes in the groups), and builds a {@link SubNode}
 * array that describes each node in the sub-matrix.
 * </p>
 *
 * <p>
 * The {@link #fillSubMatrix} constructs the sub-matrix from the {@link SubNode} array.
 * </p>
 *
 * <p>
 * For the back conversion, {@link #convertSubGroups} takes the sub-nodes description and newly found groups as
 * {@link BitSet}, with a group type to apply when converting them to groups in the parent matrix. Groups that were
 * given during construction of the sub nodes are all returned as well, possibly as a child of a newly found group.
 * </p>
 *
 * <p>
 * Finally, the new set of available parent nodes can be computed from the sub-nodes description using
 * {@link #computeAvailNodes} that takes again the sub-nodes description and the newly found groups.
 * </p>
 */
public class SubMatrixFunctions {
    /** Constructor of the static {@link SubMatrixFunctions} class. */
    private SubMatrixFunctions() {
        // Static class.
    }

    /**
     * Construct sub-nodes representing parent nodes or previously found groups.
     *
     * @param prevGroups Groups from the previous iteration to add to the sub-nodes as single sub node.
     * @param availParentNodes Available parent nodes that are not in a group.
     * @return Representation of one or more parent nodes for each sub-node.
     */
    public static SubNode[] makeSubNodes(List<Group> prevGroups, BitSet availParentNodes) {
        final int subSize = prevGroups.size() + availParentNodes.cardinality();
        SubNode[] subNodes = new SubNode[subSize];

        int subIndex = 0;
        // Add groups to the subNodes.
        for (Group grp: prevGroups) {
            // Verify that the available parent nodes are disjunct with the groups.
            Assert.check(!availParentNodes.intersects(grp.members));

            subNodes[subIndex++] = new ParentGroupNode(grp);
        }

        // Add parent nodes to the subNodes.
        for (int parentNode: iterateTrueBits(availParentNodes)) {
            subNodes[subIndex++] = new SingleParentNode(parentNode);
        }

        Assert.check(subIndex == subSize);
        return subNodes;
    }

    /**
     * Copy data from the parent matrix into a new sub matrix as described by the {@code subNodes}.
     *
     * @param parentMatrix Data to copy.
     * @param subNodes Description how and what to copy.
     * @return The constructed sub matrix.
     */
    public static RealMatrix fillSubMatrix(RealMatrix parentMatrix, SubNode[] subNodes) {
        final int subSize = subNodes.length;
        RealMatrix subMatrix = new BlockRealMatrix(subSize, subSize);

        for (int si = 0; si < subSize; si++) {
            for (int sj = 0; sj < subSize; sj++) {
                if (si == sj) {
                    subMatrix.setEntry(sj, sj, 0);
                    continue;
                }
                // si != sj, not on main diagonal.

                double total = 0;
                for (int pi = subNodes[si].firstParentNode(0); pi >= 0; pi = subNodes[si].firstParentNode(pi + 1)) {
                    for (int pj = subNodes[sj].firstParentNode(0); pj >= 0; pj = subNodes[sj].firstParentNode(pj + 1)) {
                        total += parentMatrix.getEntry(pi, pj);
                    }
                }
                subMatrix.setEntry(si, sj, total);
            }
        }
        return subMatrix;
    }

    /**
     * Convert subsets of sub matrix nodes to parent node groups.
     *
     * @param subNodes Description of the sub matrix.
     * @param subSets Subsets of the sub matrix to convert.
     * @param groupType Type of group to construct.
     * @return The parent node groups.
     */
    public static List<Group> convertSubGroups(SubNode[] subNodes, List<BitSet> subSets, GroupType groupType) {
        List<Group> groups = listc(subSets.size());

        BitSet usedSubNodes = new BitSet(subNodes.length); // Processed sub nodes.

        // Process newly found sets in the sub matrix, constructing a new group for each.
        for (BitSet subSet: subSets) {
            BitSet parentNodes = new BitSet();
            List<Group> prevGroups = list();

            for (int subNodeIndex: iterateTrueBits(subSet)) {
                Assert.check(!usedSubNodes.get(subNodeIndex)); // Each subnode should be in one group at most.
                usedSubNodes.set(subNodeIndex);

                Group prevGroup = subNodes[subNodeIndex].getGroup();
                if (prevGroup != null) {
                    prevGroups.add(prevGroup);
                } else {
                    // Non-group node in the parent.
                    // Should be a single value.
                    int parentNodeIndex = getSingleNode(subNodes[subNodeIndex]);
                    parentNodes.set(parentNodeIndex);
                }
            }

            Group newGroup;
            if (!parentNodes.isEmpty()) {
                newGroup = new Group(groupType, parentNodes, prevGroups);
            } else {
                newGroup = new Group(groupType, null, prevGroups);
            }
            groups.add(newGroup);
        }

        // Search the remaining sub nodes for groups to add.
        for (int subNodeIndex = 0; subNodeIndex < subNodes.length; subNodeIndex++) {
            if (usedSubNodes.get(subNodeIndex)) {
                continue;
            }
            Group prevGroup = subNodes[subNodeIndex].getGroup();
            if (prevGroup != null) {
                groups.add(prevGroup);
            }
        }
        return groups;
    }

    /**
     * Compute which parent nodes are still available for grouping.
     *
     * @param subNodes Nodes and groups that were available.
     * @param subSets Newly created groups containing nodes that are not available any more.
     * @return Parent nodes still available for grouping.
     */
    public static BitSet computeAvailNodes(SubNode[] subNodes, List<BitSet> subSets) {
        BitSet groupedNodes = new BitSet(); // Nodes that are in a new group.
        for (BitSet subSet: subSets) {
            groupedNodes.or(subSet);
        }

        BitSet newAvail = new BitSet();
        for (int subIndex = 0; subIndex < subNodes.length; subIndex++) {
            if (groupedNodes.get(subIndex)) {
                continue; // Node has been grouped and is not available.
            }
            SubNode subNode = subNodes[subIndex];
            if (subNode.getGroup() != null) {
                continue; // Ignore groups of the previous iteration.
            }
            int parentNode = getSingleNode(subNode);
            newAvail.set(parentNode);
        }
        return newAvail;
    }

    /**
     * Retrieve the single parent node (index) from the sub node.
     *
     * @param subNode Node to inspect.
     * @return The contained single parent node from the {@code subNode}.
     */
    private static int getSingleNode(SubNode subNode) {
        Assert.check(subNode.getGroup() == null);

        int parentNodeIndex = subNode.firstParentNode(0);
        Assert.check(parentNodeIndex >= 0);
        Assert.check(subNode.firstParentNode(parentNodeIndex + 1) < 0);
        return parentNodeIndex;
    }
}
