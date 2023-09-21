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

package org.eclipse.escet.common.dsm;

import static org.eclipse.escet.common.dsm.BusComputing.computeFixPointBus;
import static org.eclipse.escet.common.dsm.BusComputing.computeTopKBus;
import static org.eclipse.escet.common.dsm.ClusterComputing.hierarchicalClustering;
import static org.eclipse.escet.common.dsm.DsmHelper.shuffleArray;
import static org.eclipse.escet.common.dsm.MatrixHelper.clearDiagonal;
import static org.eclipse.escet.common.java.BitSets.iterateTrueBits;
import static org.eclipse.escet.common.java.BitSets.ones;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.dsm.Group.GroupType;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.output.DebugNormalOutput;

/**
 * Functions for finding a bus and hierarchical clusters.
 *
 * <p>
 * Paper describing the method: T. Wilschut, L.F.P. Etman, J.E. Rooda, and I.J.B.F. Adan: Multi-level flow-based Markov
 * clustering for design structure matrices, Journal of Mechanical Design : Transactions of the ASME, Dec 2017, 139(12):
 * 121402, https://doi.org/10.1115/1.4037626
 * </p>
 *
 * <p>
 * PhD thesis, referred to as [Wilschut 2018]: Wilschut T. System specification and design structuring methods for a
 * lock product platform. Eindhoven: Technische Universiteit Eindhoven, 2018. 178 p.
 * </p>
 */
public class DsmClustering {
    /** Constructor for the static {@link DsmClustering} class. */
    private DsmClustering() {
        // Static class.
    }

    /**
     * Compute flow-based hierarchical Markov clustering of nodes in a graph.
     *
     * @param clusterSettings Settings for the computation.
     * @return The computed clustered DSM.
     */
    public static Dsm flowBasedMarkovClustering(ClusterSettings clusterSettings) {
        RealMatrix adjacencies = clusterSettings.adjacencies;
        double busInclusion = clusterSettings.busInclusion;
        double evap = clusterSettings.evap;
        int stepCount = clusterSettings.stepCount;
        double inflation = clusterSettings.inflation;
        double epsilon = clusterSettings.epsilon;
        DebugNormalOutput dbg = clusterSettings.debugOut;

        final int size = adjacencies.getRowDimension();
        dbg.line("Flow-based Markov clustering for %d nodes.", size);

        List<Group> groups = list();
        RealMatrix adjacenciesOriginal = adjacencies.copy();
        clearDiagonal(adjacencies);
        BitSet potentionalBusNodes = ones(size);
        BitSet busNodes = new BitSet();
        switch (clusterSettings.busDetectionAlgorithm) {
            case NO_BUS: {
                // No bus nodes should be detected.
                break;
            }
            case FIX_POINT: {
                busNodes = computeFixPointBus(adjacencies, busInclusion, potentionalBusNodes);
                break;
            }
            case TOP_K: {
                busNodes = computeTopKBus(adjacencies, busInclusion, potentionalBusNodes);
                break;
            }
        }
        Group busGroup = hierarchicalClustering(adjacencies, busNodes, evap, stepCount, inflation, epsilon,
                GroupType.BUS, dbg);
        if (busGroup != null) {
            dbg.line("Bus-group found:");
            busGroup.dbgDump("  ", dbg);

            Assert.implies(busGroup.childGroups.size() == 1, busGroup.localNodes != null);
            groups.add(busGroup);
        } else {
            dbg.line("No bus found.");
        }

        BitSet nonbusNodes = ones(size);
        nonbusNodes.andNot(busNodes);
        Group nonbusGroup = hierarchicalClustering(adjacencies, nonbusNodes, evap, stepCount, inflation, epsilon,
                GroupType.CLUSTER, dbg);
        if (nonbusGroup != null) {
            dbg.line("Clustering-group found:");
            nonbusGroup.dbgDump("  ", dbg);

            Assert.implies(nonbusGroup.childGroups.size() == 1, nonbusGroup.localNodes != null);
            groups.add(nonbusGroup);
        } else {
            dbg.line("No clustering found.");
        }

        if (groups.isEmpty()) {
            return null;
        }

        Group rootGroup;
        if (groups.size() == 1) {
            rootGroup = first(groups);
        } else {
            rootGroup = new Group(GroupType.COLLECTION, null, groups);
        }

        Dsm dsm = shuffleNodes(adjacenciesOriginal, clusterSettings.labels, rootGroup, dbg);
        dbg.line("Shuffled nodes of groups near each other:");
        dsm.rootGroup.dbgDump("  ", dbg);
        return dsm;
    }

    /**
     * Re-order nodes such that all found groups are contiguous ranges, and wrap all information in a single object.
     *
     * @param adjacencies Original adjacency information of the graph.
     * @param labels Original labels of each node.
     * @param rootGroup Top-level group to order on.
     * @param dbg Stream for sending debug output.
     * @return All computed information, wrapped in a single object.
     */
    private static Dsm shuffleNodes(RealMatrix adjacencies, Label[] labels, Group rootGroup, DebugNormalOutput dbg) {
        int[] nodeShuffle = computeShuffle(rootGroup);
        if (dbg.isEnabled()) {
            dbg.line();
            dbg.line("Node mapping new <- original:");
            for (int i = 0; i < nodeShuffle.length; i++) {
                dbg.line("  %d <- %d", i, nodeShuffle[i]);
            }
            dbg.line();
        }

        adjacencies = shuffleMatrix(nodeShuffle, adjacencies);
        labels = shuffleArray(labels, nodeShuffle);
        return new Dsm(nodeShuffle, adjacencies, labels, rootGroup);
    }

    /**
     * Compute how to re-order nodes such that all groups are contiguous.
     *
     * @param group Group to re-order.
     * @return Re-ordering of nodes of the group, each entry {@code i}.contains the original index of that node.
     */
    private static int[] computeShuffle(Group group) {
        final int size = group.members.cardinality();
        int[] nodeShuffle = new int[size];
        Arrays.fill(nodeShuffle, -1);

        int nextFree = assignGroups(nodeShuffle, 0, group);
        Assert.check(nextFree == size);

        for (int i = 0; i < size; i++) {
            Assert.check(nodeShuffle[i] >= 0);
        }
        return nodeShuffle;
    }

    /**
     * Assign groups recursively to a contiguous range, such that all groups cover a contiguous range.
     *
     * @param nodeShuffle Map describing how the nodes are re-ordered, modified in-place.
     * @param base First available node for assigning to the given group.
     * @param group Group to re-order recursively.
     * @return First free index after the assigned group.
     */
    private static int assignGroups(int[] nodeShuffle, int base, Group group) {
        group.setShuffledBase(base);

        Collections.sort(group.childGroups, GroupComparator.COMPARATOR);
        // Assign child groups.
        for (Group child: group.childGroups) {
            int nextFree = assignGroups(nodeShuffle, base, child);
            Assert.check(base + child.members.cardinality() == nextFree);
            base = nextFree;
        }
        if (group.localNodes != null) {
            // Assign local nodes.
            for (int i: iterateTrueBits(group.localNodes)) {
                nodeShuffle[base] = i;
                base++;
            }
        }
        return base;
    }

    /** Comparator for sorting groups on type and descending size. */
    private static class GroupComparator implements Comparator<Group> {
        /** Comparator instance. */
        public static final GroupComparator COMPARATOR = new GroupComparator();

        @Override
        public int compare(Group g1, Group g2) {
            // Sort on group type if applicable.
            boolean g1Bus = g1.groupType.equals(GroupType.BUS);
            boolean g2Bus = g2.groupType.equals(GroupType.BUS);
            if (g1Bus && !g2Bus) {
                return -1;
            }
            if (!g1Bus && g2Bus) {
                return 1;
            }

            // Both bus or both non-bus, sort on descending size.
            return Integer.compare(g1.members.cardinality(), g2.members.cardinality());
        }
    }

    /**
     * Shuffle the rows and columns of the adjacency matrix according to how nodes shuffle.
     *
     * @param nodeShuffle Shuffle table, each entry {@code i} contains the original index of the node.
     * @param adjacencies Matrix to shuffle.
     * @return The shuffled matrix.
     */
    private static RealMatrix shuffleMatrix(int[] nodeShuffle, RealMatrix adjacencies) {
        final int size = nodeShuffle.length;
        Assert.check(adjacencies.getColumnDimension() == size);
        Assert.check(adjacencies.getRowDimension() == size);

        RealMatrix result = new BlockRealMatrix(size, size);
        for (int i = 0; i < size; i++) {
            int origRow = nodeShuffle[i];
            for (int j = 0; j < size; j++) {
                result.setEntry(i, j, adjacencies.getEntry(origRow, nodeShuffle[j]));
            }
        }
        return result;
    }
}
