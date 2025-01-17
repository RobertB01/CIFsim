//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.clustering;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.java.BitSets.toBitSet;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.BitSet;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.dsm.Group;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSetIterator;

/**
 * Functionality to construct a multi-level synthesis tree, by placing plant and requirement groups at the right tree
 * nodes, based on a given clustering of plant and requirement groups.
 *
 * <p>
 * This implementation is based on the paper Goorden 2020:
 *
 * M. Goorden, J. v. d. Mortel-Fronczak, M. Reniers, W. Fokkink and J. Rooda, "Structuring Multilevel Discrete-Event
 * Systems With Dependence Structure Matrices", IEEE Transactions on Automatic Control, volume 65, issue 4, pages
 * 1625-1639, 2020, doi:<a href="https://doi.org/10.1109/TAC.2019.2928119">10.1109/TAC.2019.2928119</a>.
 * </p>
 */
public class ComputeMultiLevelTree {
    /** Constructor of the static {@link ComputeMultiLevelTree} class. */
    private ComputeMultiLevelTree() {
        // Static class.
    }

    /**
     * Recursively build the tree of multi-level synthesis nodes as described in Algorithm 1 of Goorden 2020. Unlike in
     * the paper, expansion of plant groups and requirement groups to their original plants and requirements is not
     * performed.
     *
     * @param clusterGroup The cluster group to convert to a tree node.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return The new tree node for the cluster group, without expanding the plant and requirement groups to their
     *     elements.
     */
    public static TreeNode transformCluster(Group clusterGroup, RealMatrix p, RealMatrix rp) {
        // Depending on the size of the cluster group, forward the call to the method that deals with it.
        if (clusterGroup.members.cardinality() == 1) {
            return transformClusterSingle(clusterGroup.members.nextSetBit(0), p, rp);
        } else {
            return transformClusterMultiple(clusterGroup, p, rp);
        }
    }

    /**
     * Recursively build the tree of multi-level synthesis nodes as described in Algorithm 1 of Goorden 2020, for a
     * cluster group with more than one node. Unlike in the paper, expansion of plant groups and requirement groups to
     * their original plants and requirements is not performed.
     *
     * @param clusterGroup The cluster group to convert to a tree node.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return The new tree node for the cluster group, without expanding the plant and requirement groups to their
     *     elements.
     */
    private static TreeNode transformClusterMultiple(Group clusterGroup, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());

        // Lines 1, 7, 8:
        //
        // Both 'T' and 'm' exist in the paper to compute the plant and requirement sets from the recursive calls. In
        // this implementation, those sets are passed around in the recursion and updated during the recursive calls, so
        // the information is immediately available when this function has performed all its recursive calls.
        //
        // While the paper expands plant groups and requirement groups to their original plants and requirements, we
        // do not do that. Instead, the plant and requirement groups are kept, and put into tree nodes.

        dbg("Make a multi-level tree node for a cluster group with multiple nodes:");
        idbg();
        clusterGroup.dbgDump(OutputProvider.getDebugOutputStream());
        dbg();

        // Line 2: Compute what the tree node should contain.
        p = p.copy();
        rp = rp.copy();
        TreeNode treeNode = calculateGandKMultiple(clusterGroup, p, rp);

        // Lines 5, 6 (and 7 implicitly): Transform all child cluster groups.

        // Add local nodes of the cluster group.
        if (clusterGroup.localNodes != null) {
            for (int childNode: new BitSetIterator(clusterGroup.localNodes)) {
                treeNode.childNodes.add(transformClusterSingle(childNode, p, rp));
            }
        }

        // Add child groups of the cluster group.
        //
        // Line 7: In the paper collects the plants and requirements from calls to Algorithm 2. Instead, we keep the
        // plant groups and requirement groups.
        for (Group childGroup: clusterGroup.childGroups) {
            treeNode.childNodes.add(transformCluster(childGroup, p, rp));
        }
        ddbg();
        dbg("---------- DONE Created tree node for cluster with multiple nodes.");
        dbg();

        return treeNode;
    }

    /**
     * Build the tree of multi-level synthesis nodes as described in Algorithm 1 of Goorden 2020, for a single-node
     * cluster. Unlike in the paper, expansion of plant groups and requirement groups to their original plants and
     * requirements is not performed.
     *
     * @param clusterGroup The cluster group to convert to a tree node. Contains a single plant group.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return The new tree node for the cluster group, without expanding the plant and requirement groups to their
     *     elements.
     */
    private static TreeNode transformClusterSingle(int clusterGroup, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());

        // Since 'size(M) = 1' here, lines 4 to 10 are never done. Also here, expanding the plant and requirement groups
        // back to their elements is not performed. That eliminates line 3. Thus this only implements line 2, and then
        // immediately returns the tree node.
        dbg("Make a multi-level tree node for a cluster group with a single node (plant group %d):", clusterGroup);
        idbg();
        TreeNode treeNode = calculateGandKSingle(clusterGroup, p, rp);
        ddbg();
        dbg("---------- DONE Created tree node for cluster with a single node.");
        dbg();
        return treeNode;
    }

    /**
     * Compute the results of a call to Algorithm 2 as described in Goorden 2020, for a cluster group with multiple
     * nodes (the {@code size(M) > 1} case). Unlike in the paper, expansion of plant groups and requirement groups to
     * their original plants and requirements is not performed.
     *
     * @param clusterGroup Cluster group to consider.
     * @param p Plant group relations. Is modified in-place.
     * @param rp Requirement group rows to plant group columns. Is modified in-place.
     * @return The new tree node for the cluster group, without expanding the plant and requirement groups to their
     *     elements.
     */
    private static TreeNode calculateGandKMultiple(Group clusterGroup, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());

        // Start by dumping the input.
        dbg("Starting Algorithm 2 by searching and modifying the matrices based on group information.");
        idbg();
        dbgDumpPmatrix(p);
        dbgDumpRPmatrix(rp);
        dbg();

        // This method handles the 'else' case, starting at line 5.
        //
        // Lines 6-17: Perform a search in the cells of the matrix and an update of them while collecting the found
        // non-zero matches. Start by creating the new tree node that will hold the results.
        TreeNode treeNode = new TreeNode();

        // For lines 6-17 in the paper, 'M' contains all child cluster groups. Our clustering implementation however
        // moves singleton nodes in the cluster to local nodes, so they must be dealt with separately. As a result
        // line 6 expands into four double nested loops: singleton-singleton, singleton-multi, and multi-multi and
        // multi-singleton. Note that both loops cover the full range so you get all (a, b) and (b, a) pairs that exist.
        // Having four cases also means getting lines 8-14 four times (not all lines are needed). To avoid code
        // duplication this has been factored out to 'update'.

        // Compare local nodes.
        if (clusterGroup.localNodes != null) {
            for (int node1: new BitSetIterator(clusterGroup.localNodes)) {
                // Compare local nodes against each other.
                for (int node2: new BitSetIterator(clusterGroup.localNodes)) {
                    update(p, rp, treeNode, node1, node2);
                }

                // Compare local nodes against nodes of child groups.
                for (Group childGroup: clusterGroup.childGroups) {
                    for (int node2: new BitSetIterator(childGroup.members)) {
                        update(p, rp, treeNode, node1, node2); // Compare local nodes against nodes of child groups.
                    }
                }
            }
        }

        // Compare child groups.
        int numChildGroups = clusterGroup.childGroups.size();
        for (int grp1 = 0; grp1 < numChildGroups; grp1++) {
            Group child1 = clusterGroup.childGroups.get(grp1);

            if (clusterGroup.localNodes != null) {
                for (int node1: new BitSetIterator(child1.members)) {
                    // Compare nodes of child groups against local nodes.
                    for (int node2: new BitSetIterator(clusterGroup.localNodes)) {
                        update(p, rp, treeNode, node1, node2);
                    }
                }
            }

            for (int grp2 = 0; grp2 < numChildGroups; grp2++) {
                if (grp1 != grp2) {
                    Group child2 = clusterGroup.childGroups.get(grp2);
                    for (int node1: new BitSetIterator(child1.members)) {
                        // Compare nodes of child groups against each other.
                        for (int node2: new BitSetIterator(child2.members)) {
                            update(p, rp, treeNode, node1, node2);
                        }
                    }
                }
            }
        }

        // Dump result.
        dbg("Updated Algorithm 2 data for cluster group members %s: %s plant groups, %s req groups.",
                clusterGroup.members, treeNode.plantGroups, treeNode.requirementGroups);
        dbg("Updated matrices:");
        idbg();
        dbgDumpPmatrix(p);
        dbgDumpRPmatrix(rp);
        ddbg();
        dbg();
        ddbg();

        // Lines 18 and 19 are not performed in this implementation. Instead the collected results of lines 11 and 12
        // are used. Since have already been put in the new tree node, we simply return that tree node.
        return treeNode;
    }

    /**
     * Update the Algorithm 2 matrices and tree node, for the given pair of plant groups, by searching the shared cells
     * of both plant groups for non-zero entries.
     *
     * @param p Plant group relations. Is modified in-place.
     * @param rp Requirement group rows to plant group columns. Is modified in-place.
     * @param treeNode The tree node to update. Is modified in-place.
     * @param plantGroup1 First plant group to use.
     * @param plantGroup2 Second plant group to use.
     */
    private static void update(RealMatrix p, RealMatrix rp, TreeNode treeNode, int plantGroup1, int plantGroup2) {
        // Line 6: Check for non main-diagonal, i.e., no self-relation. Continue only with distinct plant groups.
        // Line 8: Check for non-zero entry. Continue only with related plant groups.
        if (plantGroup1 == plantGroup2 || p.getEntry(plantGroup1, plantGroup2) == 0) {
            return;
        }

        // Line 9: Find requirement groups related to the pair of plant groups.
        BitSet reqGroups = collectRequirementsForPlantGroupPair(rp, plantGroup1, plantGroup2);
        if (reqGroups.isEmpty()) {
            dbg("Found P cell (%d, %d), but no requirements.", plantGroup1, plantGroup2);
            return;
        }

        dbg("Found P cell (%d, %d), with %s requirements:", plantGroup1, plantGroup2, reqGroups);

        // Line 10: Remove the above found requirement groups from the plant group relations. The matrix representation
        // that we use does not allow for subtraction. Hence, we add a negative value instead.
        idbg();
        dbg("(%d, %d): add %d", plantGroup1, plantGroup2, -reqGroups.cardinality());
        p.addToEntry(plantGroup1, plantGroup2, -reqGroups.cardinality());
        ddbg();

        // Line 11: Find all plant groups that need at least one of the above requirement groups, and add them to the
        // tree node.
        BitSet plantGroups = collectPlantGroupsForRequirementGroups(rp, reqGroups);
        treeNode.plantGroups.or(plantGroups);

        // Line 12: Add the above requirement groups to the tree node.
        treeNode.requirementGroups.or(reqGroups);

        // Line 13: Clear out the above requirement groups from the requirement groups to plant group relations, to
        // ensure that those requirement groups are not added to any other tree node.
        clearPlantGroupsOfRequirementGroups(rp, reqGroups);
    }

    /**
     * Find all requirement groups that are related to both plant groups.
     *
     * @param rp Requirement group rows to plant group columns.
     * @param plantGrp1 First plant group.
     * @param plantGrp2 Second plant group.
     * @return Requirement groups that relate to both plant groups.
     */
    private static BitSet collectRequirementsForPlantGroupPair(RealMatrix rp, int plantGrp1, int plantGrp2) {
        BitSet reqGroups = new BitSet();
        for (int row = 0; row < rp.getRowDimension(); row++) {
            if (rp.getEntry(row, plantGrp1) != 0 && rp.getEntry(row, plantGrp2) != 0) {
                reqGroups.set(row);
            }
        }
        return reqGroups;
    }

    /**
     * Collect the plant groups that relate to at least one of the given requirement groups.
     *
     * @param rp Requirement group rows to plant group columns.
     * @param reqGroups Requirement groups to match with.
     * @return The plant groups that relate to at least one of the given requirement groups.
     */
    private static BitSet collectPlantGroupsForRequirementGroups(RealMatrix rp, BitSet reqGroups) {
        BitSet plantGroups = new BitSet();
        for (int col = 0; col < rp.getColumnDimension(); col++) {
            for (int reqGrp: new BitSetIterator(reqGroups)) {
                if (rp.getEntry(reqGrp, col) != 0) {
                    plantGroups.set(col);
                    break;
                }
            }
        }
        return plantGroups;
    }

    /**
     * Clear the given requirement group rows in the given {@code rp} matrix.
     *
     * @param rp Requirement group rows to plant group columns. Is modified in-place.
     * @param reqGroups Requirement group rows to clear.
     */
    private static void clearPlantGroupsOfRequirementGroups(RealMatrix rp, BitSet reqGroups) {
        for (int row: new BitSetIterator(reqGroups)) {
            for (int col = 0; col < rp.getColumnDimension(); col++) {
                rp.setEntry(row, col, 0);
            }
        }
    }

    /**
     * Compute the results of a call to Algorithm 2 as described in Goorden 2020, for a cluster group with one node (the
     * {@code size(M) = 1} case). Unlike in the paper, expansion of plant groups and requirement groups to their
     * original plants and requirements is not performed.
     *
     * @param clusterGroup The cluster group to consider. Contains a single plant group.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return The new tree node for the plant group.
     */
    private static TreeNode calculateGandKSingle(int clusterGroup, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());
        dbgDumpPmatrix(p);
        dbgDumpRPmatrix(rp);

        // Line 2 is already established to hold.

        // Line 3 is trivial here, as there is exactly one given plant group, and expansion of plant groups to their
        // plants is not performed.
        BitSet plantGroups = new BitSet();
        plantGroups.set(clusterGroup);

        // Line 4: Collect the requirement groups only used by the plant group.
        BitSet reqGroups = reqGroupsOnlyUsedBy(rp, clusterGroup);
        dbg("Tree node for singleton cluster group %d: %s plant groups, %s req groups.", clusterGroup, plantGroups,
                reqGroups);
        dbg();

        // Lines 18 and 19 are not performed in this implementation. Instead the plant groups and requirement groups are
        // kept, and put into a new tree node.
        return new TreeNode(plantGroups, reqGroups);
    }

    /**
     * Collect the requirement groups exclusively used by the provided plant group.
     *
     * @param rp Requirement group rows to plant group columns.
     * @param plantGroup Plant group that must use the requirement groups.
     * @return Requirement groups that only have the given plant group as relation.
     */
    private static BitSet reqGroupsOnlyUsedBy(RealMatrix rp, int plantGroup) {
        BitSet reqGroups = new BitSet();

        for (int row = 0; row < rp.getRowDimension(); row++) {
            if (rp.getEntry(row, plantGroup) == 1) {
                double sum = 0;
                for (int col = 0; col < rp.getColumnDimension(); col++) {
                    sum += rp.getEntry(row, col);
                }
                if (sum == 1) {
                    reqGroups.set(row);
                }
            }
        }
        return reqGroups;
    }

    /**
     * Dump the 'P' matrix at the debugging output stream.
     *
     * @param p The 'P' matrix to dump.
     */
    public static void dbgDumpPmatrix(RealMatrix p) {
        Assert.check(p.isSquare());
        dbg("Dumping P:");
        idbg();
        String headerLine = IntStream.range(0, p.getColumnDimension()).mapToObj(c -> fmt("%2d", c))
                .collect(Collectors.joining(" "));
        dbg("   : " + headerLine);
        for (int row = 0; row < p.getRowDimension(); row++) {
            final int finalRow = row;
            IntFunction<String> convertValue = col -> {
                double v = p.getEntry(finalRow, col);
                return (v == 0) ? " ." : fmt("%2d", (int)v);
            };
            String line = fmt("%3d: ", row) + IntStream.range(0, p.getColumnDimension()).mapToObj(convertValue)
                    .collect(Collectors.joining(" "));
            dbg(line);
        }
        ddbg();
        dbg();
    }

    /**
     * Dump the 'RP' matrix at the debugging output stream.
     *
     * @param rp The 'RP' matrix to dump.
     */
    public static void dbgDumpRPmatrix(RealMatrix rp) {
        dbg("Dumping RP:");
        idbg();
        for (int row = 0; row < rp.getRowDimension(); row++) {
            final int finalRow = row;
            IntPredicate nonzero = col -> rp.getEntry(finalRow, col) != 0;
            String line = fmt("%3d: ", row) + IntStream.range(0, rp.getColumnDimension()).filter(nonzero).boxed()
                    .collect(toBitSet()).toString();
            dbg(line);
        }
        ddbg();
        dbg();
    }
}
