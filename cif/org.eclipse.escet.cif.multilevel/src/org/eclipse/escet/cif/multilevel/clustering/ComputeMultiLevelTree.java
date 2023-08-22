//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.text.NumberFormat;
import java.util.BitSet;
import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;
import org.eclipse.escet.common.dsm.Group;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSetIterator;

/**
 * Construct elementary groups of CIF elements that function as elementary nodes in the multi-level synthesis.
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
    /** Matrix debug output format. */
    public static final RealMatrixFormat MAT_DEBUG_FORMAT;

    static {
        NumberFormat valueFmt = NumberFormat.getIntegerInstance(Locale.US);
        MAT_DEBUG_FORMAT = new RealMatrixFormat("", "", "  ", "", "\n", " ", valueFmt);
    }

    /** Constructor of the static {@link ComputeMultiLevelTree} class. */
    private ComputeMultiLevelTree() {
        // Static class.
    }

    /**
     * Recursively build the tree of multi-level synthesis nodes as described in Algorithm 1 of Goorden 2020. Expansion
     * of plant groups and requirement groups is not performed.
     *
     * @param clusterGroup Group in the clustered result to convert to a tree node.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return Tree node of the cluster group, without expanding the plant and requirement groups to their automata
     *     collection.
     */
    public static TreeNode transformCluster(Group clusterGroup, RealMatrix p, RealMatrix rp) {
        if (clusterGroup.members.cardinality() == 1) {
            // Edge case of a specification with one event.
            return transformClusterInternal(clusterGroup.members.nextSetBit(0), p, rp);
        } else {
            return transformClusterInternal(clusterGroup, p, rp);
        }
    }

    /**
     * Recursively build the tree of multi-level synthesis nodes as described in Algorithm 1 of Goorden 2020, for a
     * cluster group with more than one child group. Expansion of plant groups and requirement groups is not performed.
     *
     * <p>
     * The implementation for the single child group is at
     * {@link #transformClusterInternal(int, RealMatrix, RealMatrix)},
     * </p>
     *
     * @param clusterGroup Group in the clustered result to convert to a tree node.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return Tree node of the cluster group, without expanding the plant and requirement groups to their automata
     *     collection.
     */
    private static TreeNode transformClusterInternal(Group clusterGroup, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());

        // Lines 1, 7, 8:
        //
        // Both 'T' and 'm' exist in the paper to compute the plant and requirement sets from the recursive calls. In
        // this implementation, those sets are passed around in the recursion and updated during the recursive calls so
        // the information is immediately available when this function has performed all its recursive calls.
        //
        // Also, in this implementation, expanding the plant and requirement groups to their automata collection is not
        // done. Instead, the plant and requirement groups of the node are returned.

        dbg("Make tree node for plant groups:");
        idbg();
        clusterGroup.dbgDump();
        dbg();

        // Line 2, compute what the tree node should contain.
        Algo2Data content = calculateGandK(clusterGroup, p, rp);
        p = content.p;
        rp = content.rp;

        // Lines 5, 6 (and 7 implicitly), transform all child clusters groups.
        //
        // The paper sees all children of a group as other (child) cluster groups. The clustering implementation
        // however splits between local single node cluster nodes and multi node cluster child groups so here there
        // are two parts to do.
        // Add local nodes.
        List<TreeNode> childNodes = list();
        if (clusterGroup.localNodes != null) {
            for (int childNode: new BitSetIterator(clusterGroup.localNodes)) {
                childNodes.add(transformClusterInternal(childNode, p, rp));
            }
        }

        // Add child groups.
        //
        // Line 7 in the paper collects the plant automata automata and requirement automata from calls to
        // Algorithm 2. In this implementation of that algorithm, it returns found plant groups and requirement
        // groups instead. Here we also collect and eventually return those groups.
        for (Group childGroup: clusterGroup.childGroups) {
            childNodes.add(transformCluster(childGroup, p, rp));
        }
        ddbg();
        dbg("---------- DONE transformCluster for group");
        dbg();

        return new TreeNode(content.plantGroups, content.reqGroups, childNodes);
    }

    /**
     * Build the tree of multi-level synthesis nodes as described in Algorithm 1 of Goorden 2020, for a single-node
     * cluster. Expansion of plant groups and requirement groups is not performed.
     *
     * <p>
     * The implementation for multiple child groups is at
     * {@link #transformClusterInternal(Group, RealMatrix, RealMatrix)}.
     * </p>
     *
     * @param plantGroup Singleton plant group in the clustered result to convert to a tree node.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return Tree node of the cluster group, without expanding the plant and requirement groups to their automata
     *     collection.
     */
    private static TreeNode transformClusterInternal(int plantGroup, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());

        // Since 'size(M) = 1' here, lines 4 to 10 are never done. Also in line with the transformCluster method
        // for a group above, expanding back to plant automata is not performed either.
        // Thus this only implements line 2, and then immediately builds a result that fits for the recursion used in
        // the implementation.
        dbg("Make singleton tree node for plant group %d:", plantGroup);
        idbg();
        Algo2Data content = calculateGandK(plantGroup, p, rp);
        ddbg();
        dbg("---------- DONE transformCluster for singleton");
        dbg();
        return new TreeNode(content.plantGroups, content.reqGroups);
    }

    /**
     * Compute the results of a call to Algorithm 2 as described in Goorden 2020 for a group (size(M) > 1
     * case).
     *
     * <p>
     * The implementation for a single node is at {@link #calculateGandK(int, RealMatrix, RealMatrix)}.
     * </p>
     *
     * @param grp Cluster group to analyze.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return The computed content of the tree node.
     */
    private static Algo2Data calculateGandK(Group grp, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());

        // Line 18 and 19 is not performed in this implementation. Instead the collected results of line 11 and 12 are
        // returned.
        dbg("Starting computing group content.");
        idbg();
        dbgDumpPmatrix(p);
        dbgDumpRPmatrix(rp);
        dbg();
        dbg("Starting search");

        // Line 2 fails, dropping into the 'else' case here.
        //
        // Lines 6-17 perform a search in the matrix and an update of them while collecting the found non-zero
        // matches. Prepare the return value of this call so its data can be passed downwards for copying and updating
        // recursively.
        Algo2Data groupContent = new Algo2Data(p.copy(), rp.copy(), new BitSet(), new BitSet());

        // Lines 6-17, 'M' in the paper contains all child groups. The clustering implementation however moves singleton
        // nodes in the cluster to local nodes. They must be dealt with separately. As a result line 6 expands into
        // four double nested searches: singleton-singleton, singleton-multi, and multi-multi and multi-singleton. Note
        // that both loops cover the full range so you get all (a, b) and (b, a) pairs that exist.
        // Having 4 searches also means getting lines 8-14 four times (not all lines are needed). To avoid code
        // duplication this has been factored out to 'update'.

        // Compare local nodes.
        if (grp.localNodes != null) {
            for (int node1: new BitSetIterator(grp.localNodes)) {
                // Compare local nodes against each other.
                for (int node2: new BitSetIterator(grp.localNodes)) {
                    update(groupContent, node1, node2);
                }

                // Compare local nodes against nodes of child groups.
                for (Group childGroup: grp.childGroups) {
                    for (int node2: new BitSetIterator(childGroup.members)) {
                        update(groupContent, node1, node2); // Compare local nodes against nodes of child groups.
                    }
                }
            }
        }

        // Compare child groups.
        int numChildGroups = grp.childGroups.size();
        for (int grp1 = 0; grp1 < numChildGroups; grp1++) {
            Group child1 = grp.childGroups.get(grp1);

            if (grp.localNodes != null) {
                for (int node1: new BitSetIterator(child1.members)) {
                    // Compare nodes of child groups against local nodes.
                    for (int node2: new BitSetIterator(grp.localNodes)) {
                        update(groupContent, node1, node2);
                    }
                }
            }

            for (int grp2 = 0; grp2 < numChildGroups; grp2++) {
                if (grp1 != grp2) {
                    Group child2 = grp.childGroups.get(grp2);
                    for (int node1: new BitSetIterator(child1.members)) {
                        // Compare nodes of child groups against each other.
                        for (int node2: new BitSetIterator(child2.members)) {
                            update(groupContent, node1, node2);
                        }
                    }
                }
            }
        }

        // Dump and return result.
        dbg("computeGroupContents RESULT for plant group members %s: %s plant groups, %s req groups", grp.members,
                groupContent.plantGroups, groupContent.reqGroups);
        dbg("Updated matrices:");
        idbg();
        dbgDumpPmatrix(groupContent.p);
        dbgDumpRPmatrix(groupContent.rp);
        ddbg();
        dbg();
        ddbg();
        return groupContent;
    }

    /**
     * Update the group content for the given pair of plant groups by searching the shared cells of both groups for
     * non-zero entries.
     *
     * @param groupContent Group content to update.
     * @param plantGroup1 First plant group to use.
     * @param plantGroup2 Second plant group to use.
     */
    private static void update(Algo2Data groupContent, int plantGroup1, int plantGroup2) {
        // Line 8, check for a non-zero and non main-diagonal entry.
        if (plantGroup1 == plantGroup2 || groupContent.p.getEntry(plantGroup1, plantGroup2) == 0) {
            return;
        }

        // Line 9, find requirement groups for the pair of plant groups.
        BitSet reqGroups = groupContent.collectRequirementsForPlantGroupPair(plantGroup1, plantGroup2);
        if (reqGroups.isEmpty()) {
            dbg("Found P cell (%d, %d), but no requirements", plantGroup1, plantGroup2);
            return;
        }

        dbg("Found P cell (%d, %d), with %s requirements ", plantGroup1, plantGroup2, reqGroups);

        // Line 10, remove the above found requirement groups from the plant group relations. Code cannot subtract in
        // the matrix but can add a negative value.
        idbg();
        dbg("(%d, %d): add %d", plantGroup1, plantGroup2, -reqGroups.cardinality());
        groupContent.p.addToEntry(plantGroup1, plantGroup2, -reqGroups.cardinality());
        ddbg();

        // Line 11, find all plant groups that need at least one of the above requirements, and add them to the group
        // content.
        BitSet plantGroups = groupContent.collectPlantGroupsForRequirementGroups(reqGroups);
        groupContent.plantGroups.or(plantGroups);

        // Line 12, add the above requirements to the group content.
        groupContent.reqGroups.or(reqGroups);

        // Line 13, clear out the above requirement to plant group relations so the requirements are not added again.
        groupContent.clearPlantGroupsOfRequirementGroups(reqGroups);
    }

    /**
     * Compute the results of a call to Algorithm 2 as described in Goorden 2020 for one group (size(M) = 1 case).
     *
     * <p>
     * The implementation for multiple groups is at {@link #calculateGandK(Group, RealMatrix, RealMatrix)}.
     * </p>
     *
     * @param plantGroup Plant group to use.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return The computed content of the tree node.
     */
    private static Algo2Data calculateGandK(int plantGroup, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());
        dbgDumpPmatrix(p);
        dbgDumpRPmatrix(rp);

        // Line 2 is already established to hold.

        // Line 3 is trivial here, as there is exactly one given plant group.
        BitSet plantGroups = new BitSet();
        plantGroups.set(plantGroup);

        // Line 4, collect its requirement groups.
        BitSet reqGroups = reqGroupsOnlyUsedBy(rp, plantGroup);
        dbg("computeGroupContents RESULT for plant singleton group %d: %s plant groups, %s req groups", plantGroup,
                plantGroups, reqGroups);
        dbg();

        // Lines 18 and 19 are not performed in this implementation, the plant and requirement groups are returned.
        return new Algo2Data(p, rp, plantGroups, reqGroups);
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
     * Dump the P matrix at the debugging output stream.
     *
     * @param m P matrix to dump.
     */
    public static void dbgDumpPmatrix(RealMatrix m) {
        Assert.check(m.isSquare());
        dbg("Dumping P:");
        idbg();
        String headerLine = IntStream.range(0, m.getColumnDimension()).mapToObj(c -> fmt("%2d", c))
                .collect(Collectors.joining(" "));
        dbg("   : " + headerLine);
        for (int row = 0; row < m.getRowDimension(); row++) {
            final int finalRow = row;
            IntFunction<String> convertValue = col -> {
                double v = m.getEntry(finalRow, col);
                return (v == 0) ? " ." : fmt("%2d", (int)v);
            };
            String line = fmt("%3d: ", row) + IntStream.range(0, m.getColumnDimension()).mapToObj(convertValue)
                    .collect(Collectors.joining(" "));
            dbg(line);
        }
        ddbg();
        dbg();
    }

    /**
     * Dump the RP matrix at the debugging output stream.
     *
     * @param m RP matrix to dump.
     */
    public static void dbgDumpRPmatrix(RealMatrix m) {
        dbg("Dumping RP:");
        idbg();
        for (int row = 0; row < m.getRowDimension(); row++) {
            final int finalRow = row;
            IntPredicate nonzero = col -> m.getEntry(finalRow, col) != 0;
            String line = fmt("%3d: ", row)
                    + IntStream.range(0, m.getColumnDimension()).filter(nonzero).boxed().collect(toBitSet()).toString();
            dbg(line);
        }
        ddbg();
        dbg();
    }
}
