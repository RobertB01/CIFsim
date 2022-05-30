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

package org.eclipse.escet.cif.datasynth.varorder.helper;

import java.util.BitSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSets;
import org.eclipse.escet.common.java.Strings;

/**
 * Helper for variable ordering algorithms. It provides:
 * <ul>
 * <li>Various representations of the CIF specification that algorithms may operate upon.</li>
 * <li>Various utility methods.</li>
 * </ul>
 */
public class VarOrdererHelper {
    /** The CIF specification. */
    private final Specification spec;

    /** The synthesis variables, in their original order, before applying any algorithm on it. */
    private final SynthesisVariable[] variables;

    /** For each synthesis variable in the original variable order, its 0-based index within that order. */
    private final Map<SynthesisVariable, Integer> origIndices;

    /**
     * The hyper-edges representation of the CIF specification. Each hyper-edge bitset represents related variables.
     * Each bit in a hyper-edge bitset represents a synthesis variable (CIF variable in both its old and new version).
     */
    private final BitSet[] hyperEdges;

    /**
     * Constructor for the {@link VarOrdererHelper} class.
     *
     * @param spec The CIF specification.
     * @param variables The synthesis variables, in their original order, before applying any algorithm on it.
     */
    public VarOrdererHelper(Specification spec, SynthesisVariable[] variables) {
        this.spec = spec;
        this.variables = variables;
        this.origIndices = IntStream.range(0, variables.length).boxed()
                .collect(Collectors.toMap(i -> variables[i], i -> i));
        this.hyperEdges = createHyperEdges();
    }

    /**
     * Returns the number of synthesis variables of the CIF specification.
     *
     * @return The number of synthesis variables.
     */
    public int getVarCnt() {
        return variables.length;
    }

    /**
     * Create hyper-edges representing relations between variables of the CIF specification. Each hyper-edge bitset
     * represents related variables. Each bit in a hyper-edge bitset represents a synthesis variable (CIF variable in
     * both its old and new version).
     *
     * @return The hyper-edges.
     */
    private BitSet[] createHyperEdges() {
        HyperEdgeCreator creator = new HyperEdgeCreator();
        BitSet[] hyperEdges = creator.getHyperEdges(spec, variables).toArray(n -> new BitSet[n]);
        for (BitSet hyperEdge: hyperEdges) {
            Assert.check(!hyperEdge.isEmpty());
        }
        return hyperEdges;
    }

    /**
     * Returns hyper-edges representing relations between variables of the CIF specification. Each hyper-edge bitset
     * represents related variables. Each bit in a hyper-edge bitset represents a synthesis variable (CIF variable in
     * both its old and new version).
     *
     * @return The hyper-edges.
     */
    public BitSet[] getHyperEdges() {
        return hyperEdges;
    }

    /**
     * Print an empty line of debugging output.
     *
     * @see OutputProvider#dbg()
     */
    public void dbg() {
        OutputProvider.dbg();
    }

    /**
     * Print formatted debugging output, with the given indentation.
     *
     * @param dbgLevel The debug indentation level.
     * @param msg The debug output (pattern) to forward.
     * @param args The arguments of the debug output pattern.
     * @see OutputProvider#dbg(String, Object...)
     */
    public void dbg(int dbgLevel, String msg, Object... args) {
        OutputProvider.dbg(Strings.spaces(dbgLevel * 2) + msg, args);
    }

    /**
     * Computes the total span metric.
     *
     * @param newIndices For each variable, its new 0-based index.
     * @return The total span.
     */
    public long computeTotalSpan(int[] newIndices) {
        // Total span is the sum of the span of the edges.
        long totalSpan = 0;
        for (BitSet edge: hyperEdges) {
            // Get minimum and maximum index of the vertices of the edge.
            int minIdx = Integer.MAX_VALUE;
            int maxIdx = 0;
            for (int i: BitSets.iterateTrueBits(edge)) {
                int newIdx = newIndices[i];
                minIdx = Math.min(minIdx, newIdx);
                maxIdx = Math.max(maxIdx, newIdx);
            }

            // Get span of the edge and update total span.
            int span = maxIdx - minIdx;
            totalSpan += span;
        }
        return totalSpan;
    }

    /**
     * Prints the given total span as debug output.
     *
     * @param dbgLevel The debug indentation level.
     * @param totalSpan The given total span.
     * @param annotation A human-readable text indicating the reason for printing the total span.
     */
    public void dbgTotalSpan(int dbgLevel, long totalSpan, String annotation) {
        dbg(dbgLevel, "Total span: %,20d (total) %,20.2f (avg/edge) [%s]", totalSpan,
                (double)totalSpan / hyperEdges.length, annotation);
    }

    /**
     * Get the new variable indices from a new variable order.
     *
     * @param order The new variable order.
     * @return For each variable, its new 0-based index.
     */
    public int[] getNewIndices(SynthesisVariable[] order) {
        int[] newIndices = new int[order.length];
        for (int i = 0; i < order.length; i++) {
            newIndices[origIndices.get(order[i])] = i;
        }
        return newIndices;
    }

    /**
     * Reorder the synthesis variables to a new order.
     *
     * @param newIndices For each variable, its new 0-based index.
     * @return The synthesis variables, in their new order.
     */
    public SynthesisVariable[] reorder(int[] newIndices) {
        Assert.areEqual(variables.length, newIndices.length);
        SynthesisVariable[] result = new SynthesisVariable[variables.length];
        for (int i = 0; i < newIndices.length; i++) {
            result[newIndices[i]] = variables[i];
        }
        return result;
    }
}
