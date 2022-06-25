//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrdererHelper;
import org.eclipse.escet.common.java.BitSets;

/**
 * FORCE variable ordering heuristic.
 *
 * <p>
 * Based on the paper: Fadi A. Aloul, Igor L. Markov, Karem A. Sakallah, "FORCE: A Fast and Easy-To-Implement
 * Variable-Ordering Heuristic", GLSVLSI '03 Proceedings of the 13th ACM Great Lakes symposium on VLSI, pages 116-119,
 * 2003, doi:<a href="https://doi.org/10.1145/764808.764839">10.1145/764808.764839</a>.
 * </p>
 */
public class ForceVarOrderer implements VarOrderer {
    @Override
    public List<SynthesisVariable> order(VarOrdererHelper helper, List<SynthesisVariable> inputOrder,
            boolean dbgEnabled, int dbgLevel)
    {
        // Get hyper-edges.
        int varCnt = inputOrder.size();
        BitSet[] hyperEdges = helper.getHyperEdges();

        // Debug output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying FORCE algorithm.");
        }

        // Create 'locations' storage: per variable/vertex (in their original order), its location, i.e. l[v] in the
        // paper.
        double[] locations = new double[varCnt];

        // Create 'idxLocPairs' storage: pairs of variable indices (from their original order) and locations.
        List<IdxLocPair> idxLocPairs = listc(varCnt);
        for (int i = 0; i < varCnt; i++) {
            idxLocPairs.add(new IdxLocPair());
        }

        // Crate 'cogs' storage: the center of gravity for each hyper-edge.
        double[] cogs = new double[hyperEdges.length];

        // Initialize 'edgeCounts': per variable/vertex, the number of hyper-edges of which it is a part.
        int[] edgeCounts = new int[varCnt];
        for (BitSet edge: hyperEdges) {
            for (int i: BitSets.iterateTrueBits(edge)) {
                edgeCounts[i]++;
            }
        }

        // Initialize variable indices: for each variable (in their original order), its new 0-based index.
        int[] curIndices; // Current indices computed by the algorithm.
        int[] bestIndices; // Best indices computed by the algorithm.
        curIndices = helper.getNewIndicesForVarOrder(inputOrder);
        bestIndices = curIndices.clone();

        // Determine maximum number of iterations.
        int maxIter = (int)Math.ceil(Math.log(varCnt));
        maxIter *= 10;
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Maximum number of iterations: %,d", maxIter);
        }

        // Initialize total span.
        long curTotalSpan = helper.computeTotalSpanForNewIndices(curIndices);
        long bestTotalSpan = curTotalSpan;
        if (dbgEnabled) {
            helper.dbgMetricsForNewIndices(dbgLevel, curIndices, "before");
        }

        // Perform iterations of the algorithm.
        for (int curIter = 0; curIter < maxIter; curIter++) {
            // Compute center of gravity for each edge.
            for (int i = 0; i < hyperEdges.length; i++) {
                BitSet edge = hyperEdges[i];
                double cog = 0;
                for (int j: BitSets.iterateTrueBits(edge)) {
                    cog += curIndices[j];
                }
                cogs[i] = cog / edge.cardinality();
            }

            // Compute (new) locations.
            Arrays.fill(locations, 0.0);
            for (int i = 0; i < hyperEdges.length; i++) {
                BitSet edge = hyperEdges[i];
                for (int j: BitSets.iterateTrueBits(edge)) {
                    locations[j] += cogs[i];
                }
            }
            for (int i = 0; i < varCnt; i++) {
                locations[i] /= edgeCounts[i];
            }

            // Determine a new order, and update the current indices to reflect that order.
            for (int i = 0; i < varCnt; i++) {
                IdxLocPair pair = idxLocPairs.get(i);
                pair.idx = i;
                pair.location = locations[i];
            }
            Collections.sort(idxLocPairs);
            for (int i = 0; i < varCnt; i++) {
                curIndices[idxLocPairs.get(i).idx] = i;
            }

            // Get new total span.
            long newTotalSpan = helper.computeTotalSpanForNewIndices(curIndices);
            if (dbgEnabled) {
                helper.dbgMetricsForNewIndices(dbgLevel, curIndices, fmt("iteration %,d", curIter + 1));
            }

            // Stop when total span stops changing. We could stop as soon as it stops decreasing. However, we may end
            // up in a local optimum. By continuing, and allowing increases, we can get out of the local optimum, and
            // try to find a better local or global optimum. We could potentially get stuck in an oscillation. However,
            // we have a maximum on the number of iterations, so it always terminates. We may spend more iterations
            // than needed, but the FORCE algorithm is fast, so it is not expected to be an issue.
            if (newTotalSpan == curTotalSpan) {
                break;
            }

            // Update best order, if new order is better than the current best order (has lower total span).
            if (newTotalSpan < bestTotalSpan) {
                System.arraycopy(curIndices, 0, bestIndices, 0, varCnt);
                bestTotalSpan = newTotalSpan;
            }

            // Prepare for next iteration.
            curTotalSpan = newTotalSpan;
        }

        // Debug output after applying the algorithm.
        if (dbgEnabled) {
            helper.dbgMetricsForNewIndices(dbgLevel, bestIndices, "after");
        }

        // Return the best order.
        return helper.reorderForNewIndices(bestIndices);
    }

    /**
     * A pair of a variable index (from the original variable order) and a location. Can be compared, first in ascending
     * order based on location, and secondly in ascending order based on variable index.
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
