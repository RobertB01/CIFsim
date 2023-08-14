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
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dodbg;
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
import org.eclipse.escet.cif.multilevel.ciftodmm.CifRelations;
import org.eclipse.escet.common.dsm.ClusterInputData;
import org.eclipse.escet.common.dsm.Dmm;
import org.eclipse.escet.common.dsm.Dsm;
import org.eclipse.escet.common.dsm.DsmClustering;
import org.eclipse.escet.common.dsm.Group;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSetIterator;

/**
 * Construct elementary groups of CIF elements that function as elementary nodes in the multi-level synthesis.
 *
 * <p>
 * This implementation is described in Goorden 2020:
 *
 * M. Goorden, J. v. d. Mortel-Fronczak, M. Reniers, W. Fokkink and J. Rooda, "Structuring Multilevel Discrete-Event
 * Systems With Dependence Structure Matrices", IEEE Transactions on Automatic Control, volume 65, issue 4, pages
 * 1625-1639, 2020, doi:<a href="https://doi.org/10.1109/TAC.2019.2928119">10.1109/TAC.2019.2928119</a>.
 * </p>
 */
public class ComputeMultiLevelTree {
    /** Matrix debug output format. */
    private static final RealMatrixFormat MAT_DEBUG_FORMAT;

    static {
        NumberFormat valueFmt = NumberFormat.getIntegerInstance(Locale.US);
        MAT_DEBUG_FORMAT = new RealMatrixFormat("", "", "  ", "", "\n", " ", valueFmt);
    }

    /** Constructor of the static {@link ComputeMultiLevelTree} class. */
    private ComputeMultiLevelTree() {
        // Static class.
    }

    /**
     * Compute and return the multi-level synthesis tree from the given CIF relations.
     *
     * @param relations Analysis result of the specification with found plant groups, requirement groups, and their
     *     relations.
     * @return The computed multi-level synthesis tree.
     */
    public static TreeNode process(CifRelations relations) {
        Dmm reqsPlantsDmm = relations.relations; // Requirement-group rows against plant-groups columns.

        if (dodbg()) {
            dbg("Plant groups:");
            dbg(relations.plantGroups.toString());
            dbg();
            dbg("Requirement groups:");
            dbg(relations.requirementGroups.toString());
            dbg();
            dbg("Requirement / Plant relations:");
            dbg(relations.relations.toString());
            dbg();
        }

        // Convert to a plant groups DSM on requirement groups relations.
        // Do the standard T . T^-1 conversion, except here T is already transposed.
        RealMatrix plantGroupRels = reqsPlantsDmm.adjacencies.transpose().multiply(reqsPlantsDmm.adjacencies);
        dbg("Unclustered reqsPlants:");
        dbg(MAT_DEBUG_FORMAT.format(plantGroupRels));
        dbg();

        // Cluster the DSM.
        dbg("--- Start of clustering --");
        idbg();
        ClusterInputData clusteringData = new ClusterInputData(plantGroupRels, reqsPlantsDmm.columnLabels);
        Dsm clusterResultDsm = DsmClustering.flowBasedMarkovClustering(clusteringData);
        ddbg();
        dbg("--- End of clustering --");
        dbg();

        // The cluster result contains the found cluster groups with original indices. For debugging however, it seems
        // useful to also dump the clustered DSM, to understand group information.
        dbg("Clustered reqsPlants (for information only, this data is not actually used):");
        dbg(MAT_DEBUG_FORMAT.format(clusterResultDsm.adjacencies));
        dbg();

        // Recursively build the tree nodes.
        //
        // Both Algorithm 1 and Algorithm 2 change the computation for the single node case. In this implementation that
        // distinction is more separated. Both 'transformCluster' and 'calculateGandK' have separate implementations,
        // one for the single group case and one for the multiple groups case.
        return transformCluster(clusterResultDsm.rootGroup, plantGroupRels, reqsPlantsDmm.adjacencies);
    }

    /**
     * Recursively build the tree of multi-level synthesis nodes as described in Algorithm 1 of Goorden 2020, for a
     * cluster group with more than one child group. Expansion of plant groups and requirement groups (lines 18 and 19)
     * is not performed here.
     *
     * @param clusterGroup Group in the clustered result to convert to a tree node.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return Tree node of the cluster group, without expanding the plant and requirement groups to their automata
     *     collection.
     */
    private static TreeNode transformCluster(Group clusterGroup, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());

        // Lines 1, 7, 8:
        //
        // Both 'T' and 'm' exist in the paper to compute the plant and requirement sets from the recursive calls. In
        // the implementation those sets are passed around in the recursion and updated during the recursive calls so
        // the information is immediately available when this function has performed all its recursive calls. In the
        // implementation, expanding the plant and requirement groups to their automata collection is not done, instead
        // the plant and requirement groups of the node are returned.

        dbg("Make tree node for plant groups:");
        idbg();
        clusterGroup.dbgDump();
        dbg();

        // Line 2, compute what the tree node should contain.
        Algo2Data content = calculateGandK(clusterGroup, p, rp);
        p = content.p;
        rp = content.rp;

        // Line 4, we have the M = 1 case but as a child cluster group. All that needs to be done is already computed
        // above. Note you can only arrive here if you start with a single plant group, since the DMM has no local
        // nodes.
        if (clusterGroup.members.cardinality() == 1) {
            ddbg();
            dbg();
            return new TreeNode(content.plantGroups, content.reqGroups);
            //
        } else {
            // Lines 5, 6 (and 7 implicitly), transform all child clusters groups.
            //
            // The paper sees all children of a group as other (child) cluster groups. The clustering implementation
            // however splits between local single node cluster nodes and multi node cluster child groups so here there
            // are two parts to do.
            //
            // Add local nodes.
            List<TreeNode> childNodes = list();
            if (clusterGroup.localNodes != null) {
                for (int childNode: new BitSetIterator(clusterGroup.localNodes)) {
                    childNodes.add(transformCluster(childNode, p, rp));
                }
            }

            // Add child groups.
            for (Group childGroup: clusterGroup.childGroups) {
                childNodes.add(transformCluster(childGroup, p, rp));
            }
            ddbg();
            dbg("---------- DONE transformCluster for group");
            dbg();

            return new TreeNode(content.plantGroups, content.reqGroups, childNodes);
        }
    }

    /**
     * Build the tree of multi-level synthesis nodes as described in Algorithm 1 of Goorden 2020, for a single node
     * cluster. Expansion of plant groups and requirement groups (lines 18 and 19) is not performed here.
     *
     * @param plantGroup Singleton plant group in the clustered result to convert to a tree node.
     * @param p Plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @return Tree node of the cluster group, without expanding the plant and requirement groups to their automata
     *     collection.
     */
    private static TreeNode transformCluster(int plantGroup, RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());

        dbg("Make singleton tree node for plant group %d:", plantGroup);
        idbg();
        Algo2Data content = calculateGandK(plantGroup, p, rp);
        ddbg();
        dbg("---------- DONE transformCluster for singleton");
        dbg();
        return new TreeNode(content.plantGroups, content.reqGroups);
    }

    /**
     * Compute the results of a call to Algorithm 2 as described in Goorden 2020 for more than one group (M > 1 case).
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

        // Line 2, check for cluster with one node.
        // This seems only possible with clustering a single plant group. Let the single group implementation compute
        // the result.
        if (grp.members.cardinality() == 1) {
            Algo2Data gc = calculateGandK(grp.members.nextSetBit(0), p, rp);
            ddbg();
            return gc;
        }

        dbg("Starting search");

        // Line 2 fails, dropping into the 'else' case here.
        //
        // Lines 6-17 perform a search in the matrix and an update of them while collecting the found non-zero
        // matches. Prepare the return value of this call so its data can be passed downwards for copying and updating
        // recursively.
        Algo2Data groupContent = new Algo2Data(p.copy(), rp.copy(), new BitSet(), new BitSet());

        // Lines 6-17, 'M' in the paper contains all child groups. The clustering implementation however moves singleton
        // nodes in the cluster to local nodes. They must be dealt with separately. As a result line 6 expends into
        // three double nested searches: singleton-singleton, singleton-multi, and multi-mult. Note that both loops
        // cover the full range so you get all (a, b) and (b, a) pairs that exist.
        // Having 3 searches also means getting lines 8-14 three times (not all lines are needed). To avoid code
        // duplication this has been factored out to 'update'.
        if (grp.localNodes != null) {
            for (int node1: new BitSetIterator(grp.localNodes)) {
                // Check against other local nodes.
                for (int node2: new BitSetIterator(grp.localNodes)) {
                    update(groupContent, node1, node2);
                }

                // Check local node against child groups.
                for (Group childGroup: grp.childGroups) {
                    for (int node2: new BitSetIterator(childGroup.members)) {
                        update(groupContent, node1, node2);
                    }
                }
            }
        }

        // Compare child groups against each other.
        int numChildGroups = grp.childGroups.size();
        for (int grp1 = 0; grp1 < numChildGroups; grp1++) {
            Group child1 = grp.childGroups.get(grp1);

            for (int grp2 = 0; grp2 < numChildGroups; grp2++) {
                if (grp1 == grp2) {
                    continue;
                }

                Group child2 = grp.childGroups.get(grp2);
                for (int node1: new BitSetIterator(child1.members)) {
                    for (int node2: new BitSetIterator(child2.members)) {
                        update(groupContent, node1, node2);
                    }
                }
            }
        }

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
        // Line 8, check for a non main-diagonal entry.
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
     * Compute the results of a call to Algorithm 2 as described in Goorden 2020 for one group (M = 1 case).
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
