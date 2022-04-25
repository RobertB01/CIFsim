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

import static org.eclipse.escet.common.dsm.MatrixHelper.normalizeColumns;
import static org.eclipse.escet.common.dsm.MatrixHelper.prune;
import static org.eclipse.escet.common.dsm.MatrixHelper.scalarPower;
import static org.eclipse.escet.common.java.BitSets.bitset;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;

/** Class implementing the Markov Clustering algorithm. */
public class MarkovClustering {
    /** Constructor of the static {@link MarkovClustering} class. */
    private MarkovClustering() {
        // Static class.
    }

    /**
     * Compute the Markov clustering of the provided matrix.
     *
     * <p>Implements<br>
     * S. van Dongen, "Graph Clustering via a Discrete Uncoupling Process",
     * SIAM journal on Matrix Analysis and Applications,
     * 30(1): 121-141, 2008.</p>
     *
     * @param m Probability matrix to cluster, contents is destroyed during the call.
     * @param stepCount Matrix exponentiation factor (number of steps taken each iteration).
     * @param inflation Inflation coefficient.
     * @param pruningLimit Pruning values.
     * @param epsilon Convergence limit.
     * @return Clusters of the provided matrix.
     */
    public static List<BitSet> markovClustering(RealMatrix m, int stepCount, double inflation,
                                                double[] pruningLimit, double epsilon)
    {
        Assert.check(stepCount > 0);
        Assert.check(m.isSquare());

        final int size = m.getRowDimension();

        OutputProvider.dbg();
        OutputProvider.dbg("Input to Markov");
        OutputProvider.dbg(m.toString());

        // Iterate to stable probabilities.
        int k = 0; // Avoid infinite looping.
        RealMatrix prev = null; // Previous matrix for checking convergence.
        while (prev == null || !convergenceReached(m, prev, epsilon)) {
            k = k + 1;
            Assert.check(k < 50, "Max number of iterations exceeded.");

            prev = m.copy();
            m = m.power(stepCount);
            m = scalarPower(m, inflation);
            normalizeColumns(m, false);
            prune(m, pruningLimit);
        }

        OutputProvider.dbg("Output from Markov");
        OutputProvider.dbg(m.toString());
        OutputProvider.dbg();

        // Collect clusters.
        Map<Integer, BitSet> clusterMap = map(); // Set columns for each non-empty row.
        List<BitSet> clusters = list();
        for (int j = 0; j < size; j++) {
            double [] col = m.getColumn(j);

            int largestRow = 0;
            double largest = 0;
            for (int i = 0; i < size; i++) {
                if (i == 0 || largest <= col[i]) {
                    largest = col[i];
                    largestRow = i;
                }
            }
            BitSet row = clusterMap.get(largestRow);
            if (row == null) {
                row = bitset(size);
                row.set(j);
                clusterMap.put(largestRow, row);
                clusters.add(row);
            } else {
                row.set(j);
            }
        }
        return clusters;
    }

    /**
     * Compute whether convergence has been reached between two matrices.
     *
     * <p>Both matrices must have the same dimensions.</p>
     *
     * @param matP First matrix to use.
     * @param matQ Second matrix to use.
     * @param epsilon Convergence limit.
     * @return Whether convergence has been reached.
     */
    private static boolean convergenceReached(RealMatrix matP, RealMatrix matQ, double epsilon) {
        Assert.check(matP.getColumnDimension() == matQ.getColumnDimension());
        Assert.check(matP.getRowDimension() == matQ.getRowDimension());

        for (int i = 0; i < matP.getRowDimension(); i++) {
            for (int j = 0; j < matP.getColumnDimension(); j++) {
                double absDiff = Math.abs(matP.getEntry(i, j) - matQ.getEntry(i, j));
                if (absDiff > epsilon) {
                    return false;
                }
            }
        }
        return true;
    }
}
