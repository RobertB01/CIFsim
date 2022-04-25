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

import static org.apache.commons.math3.linear.MatrixUtils.inverse;
import static org.eclipse.escet.common.dsm.MarkovClustering.markovClustering;
import static org.eclipse.escet.common.dsm.MatrixHelper.getColumnMax;
import static org.eclipse.escet.common.dsm.MatrixHelper.normalizeArray;
import static org.eclipse.escet.common.dsm.MatrixHelper.normalizeColumns;
import static org.eclipse.escet.common.dsm.MatrixHelper.prune;
import static org.eclipse.escet.common.dsm.submatrix.SubMatrixFunctions.computeAvailNodes;
import static org.eclipse.escet.common.dsm.submatrix.SubMatrixFunctions.convertSubGroups;
import static org.eclipse.escet.common.dsm.submatrix.SubMatrixFunctions.fillSubMatrix;
import static org.eclipse.escet.common.dsm.submatrix.SubMatrixFunctions.makeSubNodes;
import static org.eclipse.escet.common.java.BitSets.copy;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.BitSet;
import java.util.List;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.dsm.Group.GroupType;
import org.eclipse.escet.common.dsm.submatrix.SubNode;
import org.eclipse.escet.common.java.Assert;

/** Functions for hierarchical clustering. */
public class ClusterComputing {
    /** Constructor of the static {link ClusterComputing} class. */
    private ClusterComputing() {
        // Static class.
    }

    /**
     * Perform hierarchical flow-based clustering.
     *
     * @param parentAdjacencies Adjacency matrix for the nodes.
     * @param parentAvailable Nodes of the matrix available for clustering.
     * @param evap Evaporation constant.
     * @param stepCount Matrix exponentiation factor (number of steps taken each iteration).
     * @param inflation Inflation coefficient.
     * @param epsilon Convergence limit.
     * @param topLevelGroupType Type of group for the top-level cluster.
     * @return Hierarchical clustering, or {@code null} if there is nothing to cluster.
     */
    public static Group hierarchicalClustering(RealMatrix parentAdjacencies, BitSet parentAvailable,
                                               double evap, int stepCount, double inflation, double epsilon,
                                               GroupType topLevelGroupType)
    {
        if (parentAvailable.isEmpty()) {
            return null; // No nodes for clustering available.
        }

        // Bottom-up group construction by clustering.
        List<Group> groups = list();
        BitSet availableNodes = copy(parentAvailable);
        while (true) {
            // Build sub-matrix.
            SubNode[] subNodes = makeSubNodes(groups, parentAvailable);
            RealMatrix subAdjacencies = fillSubMatrix(parentAdjacencies, subNodes);

            OutputProvider.dbg();
            OutputProvider.dbg("subjAdjacencies:");
            OutputProvider.dbg(subAdjacencies.toString());

            // Perform clustering on the sub-matrix.
            ProbabilityData probData = convertAdjacencyToProbabilities(subAdjacencies, evap);

            OutputProvider.dbg();
            OutputProvider.dbg("probData:");
            OutputProvider.dbg(probData.probabilities.toString());

            List<BitSet> clusters = markovClustering(probData.probabilities, stepCount, inflation,
                                                     probData.pruningLimits, epsilon);

            // Merge new clusters into the parent nodes.
            groups = convertSubGroups(subNodes, clusters, clusters.size() == 1 ? topLevelGroupType : GroupType.CLUSTER);
            BitSet newNodes = computeAvailNodes(subNodes, clusters);
            if (newNodes.isEmpty()) {
                // Nothing left to cluster.
                if (groups.size() == 1) {
                    return groups.get(0);
                }
                return new Group(topLevelGroupType, null, groups);
            }
            if (newNodes.cardinality() >= availableNodes.cardinality()) {
                // No progress!
                return new Group(topLevelGroupType, newNodes, groups);
            }
            availableNodes = newNodes;
        }
    }

    /** Temporary storage of results of the adjacency to probabilities conversion. */
    private static class ProbabilityData {
        /** The computed probabilities matrix. */
        public final RealMatrix probabilities;

        /** Pruning limit for each node. */
        public final double[] pruningLimits;

        /**
         * Constructor of the {@link ProbabilityData} class.
         *
         * @param probabilities The computed probabilities matrix.
         * @param pruningLimits Pruning limit for each node.
         */
        public ProbabilityData(RealMatrix probabilities, double[] pruningLimits) {
            this.probabilities = probabilities;
            this.pruningLimits = pruningLimits;
        }
    }

    /**
     * Convert adjacency matrix to probability matrix.
     *
     * @param m Matrix to convert.
     * @param evap Evaporation constant.
     * @return The created probability matrix.
     */
    private static ProbabilityData convertAdjacencyToProbabilities(RealMatrix m, double evap) {
        final int n = m.getRowDimension();
        Assert.check(m.isSquare());

        // Compute Win.
        double[] kins = new double[n]; // Sum of each column, or 1 of sum is 0.
        for (int j = 0; j < n; j++) {
            double sum = 0;
            for (int i = 0; i < n; i++) {
                sum += m.getEntry(i, j);
            }
            kins[j] = (sum > 0) ? sum : 1.0;
        }
        RealMatrix winMat = new DiagonalMatrix(kins);

        // Compute Wout.
        double[] kouts = new double[n]; // Sum of each row, or 1 if sum is 0.
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                sum += m.getEntry(i, j);
            }
            kouts[i] = (sum > 0) ? sum : 1.0;
        }
        RealMatrix woutMat = new DiagonalMatrix(kouts);

        // Compute B1 and B2.
        double[] b1s = new double[n];
        for (int i = 0; i < n; i++) {
            b1s[i] = 1 / (kouts[i] * evap);
        }
        RealMatrix matB1 = new DiagonalMatrix(b1s, false);
        b1s = null; // Avoid further use of the array.

        double[] b2s = new double[n];
        for (int i = 0; i < n; i++) {
            b2s[i] = 1 / (kins[i] * evap);
        }
        RealMatrix matB2 = new DiagonalMatrix(b2s, false);
        b2s = null; // Avoid further use of the array.

        RealMatrix rimMat = inverse(m.multiply(matB1).subtract(winMat)).scalarMultiply(-1);
        rimMat = rimMat.subtract(inverse(winMat));
        RealMatrix rdmMat = inverse(m.transpose().multiply(matB2).subtract(woutMat)).scalarMultiply(-1);
        rdmMat = rdmMat.subtract(inverse(woutMat));

        // Column wise scaling of both matrices.
        normalizeColumns(rimMat, false);
        normalizeColumns(rdmMat, false);

        // Calculation of cut value per node.
        double[] pruningLimits = new double[n];
        for (int idx = 0; idx < n; idx++) {
            double v = evap * (kouts[idx] + kins[idx]);
            Assert.check(v != 0);
            pruningLimits[idx] = 1 / (v * v);
        }

        // Construct probability matrix.
        RealMatrix matP = new BlockRealMatrix(n, n);
        double [] column = new double[n];
        // Element-wise sum columns of rim and rdm, and normalize.
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                column[i] = rimMat.getEntry(i, j) + rdmMat.getEntry(i, j);
            }
            normalizeArray(column);
            matP.setColumn(j, column);
        }

        prune(matP, pruningLimits);

        // Add self-loops.
        for (int idx = 0; idx < n; idx++) {
            double max = getColumnMax(matP, idx);
            if (Double.isNaN(max) || max <= 0) {
                max = 1;
            }
            matP.setEntry(idx, idx, max);
        }

        normalizeColumns(matP, false);
        return new ProbabilityData(matP, pruningLimits);
    }
}
