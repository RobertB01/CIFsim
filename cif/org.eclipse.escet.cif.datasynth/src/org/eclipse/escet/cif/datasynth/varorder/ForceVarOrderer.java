//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.varorder;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the FORCE algorithm for automatic variable ordering.
 *
 * <p>
 * Based on the paper: Fadi A. Aloul, Igor L. Markov, Karem A. Sakallah, FORCE: A Fast and Easy-To-Implement
 * Variable-Ordering Heuristic, GLSVLSI '03 Proceedings of the 13th ACM Great Lakes symposium on VLSI, Pages 116-119,
 * ACM New York, April 2003.
 * </p>
 */
public class ForceVarOrderer extends AutoVarOrderer {
    /**
     * The location of each vertex, i.e. l<sub>v</sub> and l<sub>v</sub>' in the paper. The vertices are variables, in
     * increasing order of the their variable indices.
     */
    private double[] locations;

    /** Pairs of {@link #newIndices variable indices} and {@link #locations}. */
    private List<IdxLocPair> idxLocPairs;

    /** The center of gravity for each {@link #edges edge}. */
    private double[] cogs;

    /**
     * Per vertex, the number of {@link #edges} of which it is a part. The vertices are the variables, in increasing
     * order of their variable indices.
     */
    private int[] edgeCounts;

    @Override
    protected void initializeAlgo() {
        // Initialize 'locations'.
        locations = new double[varCnt];

        // Initialize 'idxLocPairs'.
        idxLocPairs = listc(varCnt);
        for (int i = 0; i < varCnt; i++) {
            idxLocPairs.add(new IdxLocPair());
        }

        // Initialize 'cogs'.
        cogs = new double[edges.length];

        // Initialize 'edgeCounts'.
        edgeCounts = new int[varCnt];
        for (BitSet edge: edges) {
            for (int i = 0; i < varCnt; i++) {
                if (edge.get(i)) {
                    edgeCounts[i]++;
                }
            }
        }
    }

    @Override
    protected void cleanupAlgo() {
        locations = null;
        idxLocPairs = null;
        cogs = null;
        edgeCounts = null;
    }

    @Override
    protected void computeOrder() {
        // Debug output: starting.
        if (dbgEnabled) {
            dbg();
            dbg("  Applying FORCE algorithm:");
        }

        // Determine maximum number of iterations.
        int maxIter = (int)Math.ceil(Math.log(varCnt));
        maxIter *= 10;
        if (dbgEnabled) {
            dbg("    Maximum number of iterations: %,d", maxIter);
        }

        // Initialize total span.
        long curTotalSpan = computeTotalSpan(newIndices);
        long bestTotalSpan = curTotalSpan;
        if (dbgEnabled) {
            dbg();
            dbg("    Total span: %,20d (total) %,20.2f (avg/edge) [before]", curTotalSpan,
                    (double)curTotalSpan / edges.length);
        }

        // Perform iterations of the algorithm.
        for (int curIter = 0; curIter < maxIter; curIter++) {
            // Compute center of gravity for each edge.
            for (int i = 0; i < edges.length; i++) {
                BitSet edge = edges[i];
                double cog = 0;
                int cnt = 0;
                for (int j = 0; j < varCnt; j++) {
                    if (edge.get(j)) {
                        cog += newIndices[j];
                        cnt++;
                    }
                }
                cogs[i] = cog / cnt;
            }

            // Compute (new) locations.
            Arrays.fill(locations, 0.0);
            for (int i = 0; i < edges.length; i++) {
                BitSet edge = edges[i];
                for (int j = 0; j < varCnt; j++) {
                    if (edge.get(j)) {
                        locations[j] += cogs[i];
                    }
                }
            }
            for (int i = 0; i < varCnt; i++) {
                locations[i] /= edgeCounts[i];
            }

            // Determine new indices.
            for (int i = 0; i < varCnt; i++) {
                IdxLocPair pair = idxLocPairs.get(i);
                pair.idx = i;
                pair.location = locations[i];
            }
            Collections.sort(idxLocPairs);
            for (int i = 0; i < varCnt; i++) {
                newIndices[idxLocPairs.get(i).idx] = i;
            }

            // Get new total span.
            long newTotalSpan = computeTotalSpan(newIndices);
            if (dbgEnabled) {
                dbg("    Total span: %,20d (total) %,20.2f (avg/edge) [iteration %,d]", newTotalSpan,
                        (double)newTotalSpan / edges.length, curIter + 1);
            }

            // Stop when total span stops changing. We could stop as soon as
            // it stops decreasing. However, we may end up in a local optimum.
            // By continuing, and allowing increases, we can get out of the
            // local optimum, and try to find a better local or global optimum.
            // We could potentially get stuck in an oscillation. However, we
            // have a maximum on the number of iterations, so it always
            // terminates. We may spend more iterations than needed, but the
            // FORCE algorithm is fast, so it is not expected to be an issue.
            if (newTotalSpan == curTotalSpan) {
                break;
            }

            // Update best order, if new order is better than current best.
            if (newTotalSpan < bestTotalSpan) {
                System.arraycopy(newIndices, 0, bestIndices, 0, varCnt);
                bestTotalSpan = newTotalSpan;
            }

            // Prepare for next iteration.
            curTotalSpan = newTotalSpan;
        }

        // Debug output for result.
        if (dbgEnabled) {
            dbg("    Total span: %,20d (total) %,20.2f (avg/edge) [after]", bestTotalSpan,
                    (double)bestTotalSpan / edges.length);
        }
    }

    /**
     * A pair of an {@link ForceVarOrderer#newIndices variable index} and a {@link ForceVarOrderer#locations location}.
     * Can be compared, first in ascending order based on location, and secondly in ascending order based on variable
     * index.
     */
    private static class IdxLocPair implements Comparable<IdxLocPair> {
        /** The variable index. */
        public int idx;

        /** The location. */
        public double location;

        @Override
        public int compareTo(IdxLocPair other) {
            // First compare location.
            int rslt = Double.compare(this.location, other.location);
            if (rslt != 0) {
                return rslt;
            }

            // Locations the same, so compare variable indices.
            return Integer.compare(this.idx, other.idx);
        }

        @Override
        public String toString() {
            return fmt("(%s, %s)", idx, location);
        }
    }
}
