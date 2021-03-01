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

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisAutomaton;
import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.common.java.Assert;

/** Base class for algorithms for automatic variable ordering. */
public abstract class AutoVarOrderer {
    // Bits in the hyperedges represent whole CIF variables, not BDD variables.
    // Also, for CIF variable, the old and new variables are represented using
    // a single bit in the hyperedges.
    //
    // Multiple types of indices play a role in the variable ordering algorithm
    // below:
    // - Variable index.
    // - Order index.
    //
    // The variable index is the index of each CIF variable (old and new
    // combined) in the order before the automatic variable ordering algorithm
    // is started.
    //
    // The order index is the index of a CIF variable (old and new combined)
    // in a newly computed order.
    /** Whether debug output is enabled. */
    protected boolean dbgEnabled;

    /** The number of variables. Counts a CIF variable and its new variant as one variable. */
    protected int varCnt;

    /**
     * For each variable, in increasing order of their variable indices, the order index of that variable, for the
     * current order. Values are in range [0 .. {@link #varCnt}).
     */
    protected int[] curIndices;

    /**
     * For each variable, in increasing order of their variable indices, the order index of that variable, for a new
     * order computed by the algorithm. Values are in range [0 .. {@link #varCnt}).
     */
    protected int[] newIndices;

    /**
     * For each variable, in increasing order of their variable indices, the order index of that variable, for the best
     * order computed by the algorithm so far. Values are in range [0 .. {@link #varCnt}).
     */
    protected int[] bestIndices;

    /**
     * The hyperedges (edges of the hypergraph), i.e. 'E' in the paper. Each edge represents a set of related variables.
     * For each hyperedge, the bitset indicates for each variable, in increasing order of their variable indices,
     * whether the variable is a part of (i.e. occurs on) the hyperedge. Each bitset has length {@link #varCnt}.
     */
    protected BitSet[] edges;

    /**
     * Apply the variable ordering algorithm, to order the variables.
     *
     * @param aut The synthesis automaton.
     * @param edges The hyperedges on which to apply the algorithm.
     * @param dbgEnabled Whether debug output is enabled.
     * @return Whether the algorithm was applied and resulted in a different order ({@code true}), or was not applied or
     *     not did not result in a different order ({@code false}).
     */
    public boolean order(SynthesisAutomaton aut, List<BitSet> edges, boolean dbgEnabled) {
        // No need to reorder if less than two variables.
        if (aut.variables.length < 2) {
            return false;
        }

        // Debug output: starting.
        this.dbgEnabled = dbgEnabled;
        if (dbgEnabled) {
            dbg();
            dbg("Applying automatic variable ordering:");
        }

        // Initialize data structures.
        boolean initialized = initializeCommon(aut, edges);
        if (initialized) {
            initializeAlgo();
        }

        // Compute the new order using the algorithm.
        if (initialized) {
            computeOrder();
        }

        // Reorder based on the computed variable order.
        boolean reordered = false;
        if (initialized) {
            reordered = reorder(aut);
        }

        // Free memory.
        cleanupAlgo();
        cleanupCommon();

        // Return whether order has changed.
        return reordered;
    }

    /**
     * Initialize common data structures.
     *
     * @param aut The synthesis automaton.
     * @param hyperEdges The hyperedges on which to apply the algorithm.
     * @return Whether initialization succeeded ({@code true}) or failed ({@code false}).
     */
    private boolean initializeCommon(SynthesisAutomaton aut, List<BitSet> hyperEdges) {
        // Initialize number of CIF variables (old and new variants counted as
        // single variable).
        varCnt = aut.variables.length;

        // Initialize 'curIndices'.
        curIndices = new int[varCnt];
        for (int i = 0; i < varCnt; i++) {
            curIndices[i] = i;
        }

        // Initialize 'newIndices'.
        newIndices = new int[varCnt];
        System.arraycopy(curIndices, 0, newIndices, 0, varCnt);

        // Initialize 'bestIndices'.
        bestIndices = new int[varCnt];
        System.arraycopy(curIndices, 0, bestIndices, 0, varCnt);

        // Initialized 'edges'.
        edges = hyperEdges.toArray(new BitSet[hyperEdges.size()]);
        hyperEdges.clear();
        for (BitSet edge: edges) {
            Assert.check(!edge.isEmpty());
        }

        // Debug output: number of hyperedges.
        if (dbgEnabled) {
            dbg("  Number of hyperedges: %,d", edges.length);
        }

        // Nothing to do if no edges.
        if (edges.length == 0) {
            if (dbgEnabled) {
                dbg();
                dbg("  Skipping automatic ordering: no hyperedges.");
            }
            return false;
        }

        // Successfully initialized.
        return true;
    }

    /** Initialize algorithm-specific data structures. */
    protected abstract void initializeAlgo();

    /**
     * Compute the order, by applying the algorithm. The current order is in {@link #curIndices}, {@link #newIndices},
     * and {@link #bestIndices}. The order in {@link #curIndices} may not be modified. Temporary solutions may be stored
     * in {@link #newIndices}. The resulting best order must be in {@link #bestIndices}.
     */
    protected abstract void computeOrder();

    /**
     * Computes the total span of the edges, for a given order.
     *
     * @param indices The given order. For each variable, in increasing order of their variable indices, the order index
     *     of that variable. Values are in range [0 .. {@link #varCnt}).
     * @return The total span of the edges.
     */
    protected long computeTotalSpan(int[] indices) {
        // Total span is the sum of the span of the edges.
        long totalSpan = 0;
        for (int i = 0; i < edges.length; i++) {
            // Get minimum and maximum index of the vertices of the edge.
            BitSet edge = edges[i];
            int minIdx = Integer.MAX_VALUE;
            int maxIdx = 0;
            for (int j = 0; j < varCnt; j++) {
                if (!edge.get(j)) {
                    continue;
                }
                int idx = indices[j];
                minIdx = Math.min(minIdx, idx);
                maxIdx = Math.max(maxIdx, idx);
            }

            // Get span of the edge and update total span.
            int span = maxIdx - minIdx;
            totalSpan += span;
        }
        return totalSpan;
    }

    /**
     * Reorder based on the {@link #bestIndices computed variable order}, if it is different from {@link #curIndices}.
     *
     * @param aut The synthesis automaton. Is modified in-place.
     * @return Whether the order was changed.
     */
    private boolean reorder(SynthesisAutomaton aut) {
        // Detect variable ordering change.
        boolean changed = !Arrays.equals(curIndices, bestIndices);
        if (dbgEnabled) {
            dbg();
            dbg("  Variable order %schanged.", changed ? "" : "un");
        }

        // Set new order, if changed.
        if (changed) {
            SynthesisVariable[] newVars = new SynthesisVariable[varCnt];
            for (int varIdx = 0; varIdx < varCnt; varIdx++) {
                int orderIdx = bestIndices[varIdx];
                SynthesisVariable var = aut.variables[varIdx];
                var.group = orderIdx;
                newVars[orderIdx] = var;
            }
            aut.variables = newVars;
        }

        // Return whether the order has changed.
        return changed;
    }

    /** Clean up common data structures. */
    private void cleanupCommon() {
        varCnt = -1;
        curIndices = null;
        newIndices = null;
        bestIndices = null;
        edges = null;
    }

    /** Clean up algorithm-specific data structures. */
    protected abstract void cleanupAlgo();
}
