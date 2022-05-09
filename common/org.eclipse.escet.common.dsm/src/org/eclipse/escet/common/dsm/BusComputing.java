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

package org.eclipse.escet.common.dsm;

import static org.eclipse.escet.common.java.BitSets.bitset;
import static org.eclipse.escet.common.java.BitSets.copy;
import static org.eclipse.escet.common.java.BitSets.iterateTrueBits;
import static org.eclipse.escet.common.java.BitSets.makeBitset;
import static org.eclipse.escet.common.java.BitSets.ones;

import java.util.Arrays;
import java.util.BitSet;

import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;

/**
 * Functions for computing buses.
 *
 * <p>
 * PhD thesis, referred to as [Wilschut 2018]: Wilschut T. System specification and design structuring methods for a
 * lock product platform. Eindhoven: Technische Universiteit Eindhoven, 2018. 178 p.
 * </p>
 */
public class BusComputing {
    /** Constructor of the static {@link BusComputing} class. */
    private BusComputing() {
        // Static class.
    }

    /**
     * Compute which nodes of the matrix should be considered a bus node using the top-k algorithm.
     *
     * <p>
     * Implements the bus-detection algorithm where the top gamma nodes are selected with the highest connection degree.
     * Note that it might be possible that the first node outside the top gamma nodes has the same connection degree as
     * the last node inside the top gamma nodes.
     * </p>
     *
     * @param dependencies Matrix with dependencies between the nodes.
     * @param gamma Tuning factor for selecting bus nodes based on the connection degree.
     * @return Set of nodes that should be part of the bus.
     */
    public static BitSet computeTopKBus(RealMatrix dependencies, int gamma) {
        BitSet all = ones(dependencies.getRowDimension());
        return internalComputeTopKBus(dependencies, gamma, all);
    }

    /**
     * Compute which nodes of the matrix should be considered a bus node using the top-k algorithm.
     *
     * <p>
     * Implements the bus-detection algorithm where the top gamma nodes are selected with the highest connection degree.
     * Note that it might be possible that the first node outside the top gamma nodes has the same connection degree as
     * the last node inside the top gamma nodes.
     * </p>
     *
     * @param dependencies Matrix with dependencies between the nodes.
     * @param gamma Tuning factor for selecting bus nodes based on the connection degree. The truncated integer value is
     *     used.
     * @return Set of nodes that should be part of the bus.
     */
    public static BitSet computeTopKBus(RealMatrix dependencies, double gamma) {
        BitSet all = ones(dependencies.getRowDimension());
        return internalComputeTopKBus(dependencies, (int)gamma, all);
    }

    /**
     * Compute which nodes of the matrix should be considered a bus node using the top-k algorithm.
     *
     * <p>
     * Implements the bus-detection algorithm where the top gamma nodes are selected with the highest connection degree.
     * Note that it might be possible that the first node outside the top gamma nodes has the same connection degree as
     * the last node inside the top gamma nodes.
     * </p>
     *
     * @param dependencies Matrix with dependencies between the nodes.
     * @param gamma Tuning factor for selecting bus nodes based on the connection degree.
     * @param availableNodes Nodes that may be considered to become part of the bus.
     * @return Set of nodes that should be part of the bus.
     */
    public static BitSet computeTopKBus(RealMatrix dependencies, int gamma, BitSet availableNodes) {
        return internalComputeTopKBus(dependencies, gamma, copy(availableNodes));
    }

    /**
     * Compute which nodes of the matrix should be considered a bus node using the top-k algorithm.
     *
     * <p>
     * Implements the bus-detection algorithm where the top gamma nodes are selected with the highest connection degree.
     * Note that it might be possible that the first node outside the top gamma nodes has the same connection degree as
     * the last node inside the top gamma nodes.
     * </p>
     *
     * @param dependencies Matrix with dependencies between the nodes.
     * @param gamma Tuning factor for selecting bus nodes based on the connection degree. The truncated integer value is
     *     used.
     * @param availableNodes Nodes that may be considered to become part of the bus.
     * @return Set of nodes that should be part of the bus.
     */
    public static BitSet computeTopKBus(RealMatrix dependencies, double gamma, BitSet availableNodes) {
        return internalComputeTopKBus(dependencies, (int)gamma, copy(availableNodes));
    }

    /**
     * Compute which nodes of the matrix should be considered a bus node using the top-k algorithm.
     *
     * <p>
     * Implements the bus-detection algorithm where the top gamma nodes are selected with the highest connection degree.
     * Note that it might be possible that the first node outside the top gamma nodes has the same connection degree as
     * the last node inside the top gamma nodes.
     * </p>
     *
     * @param dependencies Matrix with dependencies between the nodes.
     * @param gamma Tuning factor for selecting bus nodes based on the connection degree.
     * @param nodeSet Nodes that may be considered to become part of the bus.
     * @return Set of nodes that should be part of the bus.
     */
    private static BitSet internalComputeTopKBus(RealMatrix dependencies, int gamma, BitSet nodeSet) {
        // Calculate the threshold.
        int[] degrees = computeInOutDegrees(dependencies, nodeSet);
        return makeBitset(indicesOfLargestElements(degrees, gamma));
    }

    /**
     * Identify the largest {@code k} integers from a list.
     *
     * <p>
     * If the value of the {@code k}-th and the {@code k+1}-th largest integers are the same, the {@code k+1}-th index
     * is not included in the result. This ensures that always {@code k} indices are returned.
     * </p>
     *
     * @param array The integer array.
     * @param k The number of top indices to select. Must be a value between 0 and size of the provided array, including
     *     the boundary values.
     * @return Array with the indices of the top {@code k} elements.
     */
    private static int[] indicesOfLargestElements(int[] array, int k) {
        // Safety checks.
        Assert.check(k >= 0);
        Assert.check(k <= array.length);

        @SuppressWarnings("unchecked")
        Pair<Integer, Integer>[] pairs = new Pair[array.length];
        for (int i = 0; i < array.length; i++) {
            pairs[i] = new Pair<>(i, array[i]);
        }

        // Sort the array of pairs in descending order of the original array values.
        Arrays.sort(pairs, (p1, p2) -> Integer.compare(p2.right, p1.right));

        // Extract the original indices, which are located in the left-hand side of each pairs.
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = pairs[i].left;
        }

        return result;
    }

    /**
     * Compute which nodes of the matrix should be considered a bus node using the fixed-point algorithm.
     *
     * <p>
     * Implements the bus-detection as described in [Wilschut 2018], Section 2.2.4, Page 21.
     * </p>
     *
     * @param dependencies Matrix with dependencies between the nodes.
     * @param gamma Tuning factor for selecting bus nodes relative to the median connection degree.
     * @return Set of nodes that should be a bus node.
     */
    public static BitSet computeFixPointBus(RealMatrix dependencies, double gamma) {
        BitSet all = ones(dependencies.getRowDimension());
        return internalComputeFixPointBus(dependencies, gamma, all);
    }

    /**
     * Compute which nodes of the matrix should be considered a bus node using the fixed-point algorithm.
     *
     * <p>
     * Implements the bus-detection as described in [Wilschut 2018], Section 2.2.4, Page 21.
     * </p>
     *
     * @param dependencies Matrix with dependencies between the nodes.
     * @param gamma Tuning factor for selecting bus nodes relative to the median connection degree.
     * @param availableNodes Nodes that may be considered to become part of the bus.
     * @return Set of nodes that should be a bus node.
     */
    public static BitSet computeFixPointBus(RealMatrix dependencies, double gamma, BitSet availableNodes) {
        return internalComputeFixPointBus(dependencies, gamma, copy(availableNodes));
    }

    /**
     * Compute which nodes of the matrix should be considered a bus node using the fixed-point algorithm.
     *
     * <p>
     * Implements the bus-detection as described in [Wilschut 2018], Section 2.2.4, Page 21.
     * </p>
     *
     * @param dependencies Matrix with dependencies between the nodes.
     * @param gamma Tuning factor for selecting bus nodes relative to the median connection degree.
     * @param nodeSet Nodes that may be considered to become part of the bus, is destroyed during the call.
     * @return Set of nodes that should be part of the bus.
     */
    private static BitSet internalComputeFixPointBus(RealMatrix dependencies, double gamma, BitSet nodeSet) {
        BitSet busNodes = new BitSet();

        // Calculate the threshold.
        int[] degrees = computeInOutDegrees(dependencies, nodeSet);

        while (!nodeSet.isEmpty()) {
            // Compute which nodes qualify for being bus nodes.
            double median = computeMedianOfPositives(degrees);
            BitSet newBusNodes = selectBusNodes(degrees, gamma * median, nodeSet);
            if (newBusNodes.isEmpty()) {
                break;
            }

            // Update set of found bus nodes.
            busNodes.or(newBusNodes);

            // Set bus node degrees to zero.
            for (int i: iterateTrueBits(busNodes)) {
                degrees[i] = 0;
            }

            // Update available nodes to consider as bus node.
            nodeSet.andNot(newBusNodes);
        }
        return busNodes;
    }

    /**
     * Count the number of incoming and outgoing edges for each node within the available nodes.
     *
     * @param m Matrix containing the dependencies.
     * @param availableNodes Nodes available for inclusion into the bus.
     * @return Number of input and output connections for all available nodes.
     */
    private static int[] computeInOutDegrees(RealMatrix m, BitSet availableNodes) {
        int[] result = new int[availableNodes.cardinality()];
        int resultIndex = 0;
        for (int nodeIdx: iterateTrueBits(availableNodes)) {
            int degrees = 0;
            // Count incoming edges.
            for (int col: iterateTrueBits(availableNodes)) {
                if (m.getEntry(nodeIdx, col) > 0) {
                    degrees++;
                }
            }

            // Count outgoing edges.
            for (int row: iterateTrueBits(availableNodes)) {
                if (m.getEntry(row, nodeIdx) > 0) {
                    degrees++;
                }
            }

            result[resultIndex] = degrees;
            resultIndex++;
        }
        return result;
    }

    /**
     * Compute median of the given counts, only considering positive elements.
     *
     * @param counts Counts to get the median from.
     * @return Median of the counts.
     */
    private static double computeMedianOfPositives(int[] counts) {
        int size = counts.length;
        Assert.check(size > 0);

        int[] copiedArray = Arrays.copyOf(counts, size);
        Arrays.sort(copiedArray);

        Integer firstNonZero = null;
        for (int i = 0; i < size; i++) {
            if (copiedArray[i] > 0) {
                firstNonZero = i;
                break;
            }
        }

        if (firstNonZero == null) {
            return 0;
        }
        int nonZeroSize = size - firstNonZero;

        if (nonZeroSize % 2 == 0) {
            return (copiedArray[firstNonZero + nonZeroSize / 2] + copiedArray[firstNonZero + nonZeroSize / 2 - 1]) / 2;
        } else {
            return copiedArray[firstNonZero + nonZeroSize / 2];
        }
    }

    /**
     * Compute the set of nodes from the available nodes that qualify for becoming a bus node.
     *
     * @param degrees In and out degrees of the available nodes.
     * @param threshold Minimum amount of connections required for becoming a bus node.
     * @param availableNodes Available nodes for selection.
     * @return Nodes from the available nodes that qualify for becoming a bus node.
     */
    private static BitSet selectBusNodes(int[] degrees, double threshold, BitSet availableNodes) {
        BitSet busNodes = bitset(availableNodes.size());

        for (int nodeIdx: iterateTrueBits(availableNodes)) {
            if (degrees[nodeIdx] >= threshold) {
                busNodes.set(nodeIdx);
            }
        }
        return busNodes;
    }
}
