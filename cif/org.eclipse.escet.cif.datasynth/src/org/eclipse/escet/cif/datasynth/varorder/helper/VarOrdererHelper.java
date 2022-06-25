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

import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.graph.Graph;
import org.eclipse.escet.cif.datasynth.varorder.graph.Node;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSets;
import org.eclipse.escet.common.java.Pair;
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
    private final List<SynthesisVariable> variables;

    /** For each synthesis variable in the original variable order, its 0-based index within that order. */
    private final Map<SynthesisVariable, Integer> origIndices;

    /**
     * The hyper-edges representation of the CIF specification. Each hyper-edge bitset represents related synthesis
     * variables. Each bit in a hyper-edge bitset represents a synthesis variable.
     */
    private final BitSet[] hyperEdges;

    /**
     * The graph representation of the CIF specification. Each node represents a synthesis variable. Each edge
     * represents a weighted relation between two different synthesis variables.
     */
    private final Graph graph;

    /** The number of characters to use for printing the total span metric in debug output. */
    private final int metricLengthTotalSpan;

    /** The number of characters to use for printing the total span metric, as average per edge, in debug output. */
    private final int metricLengthTotalSpanAvg;

    /**
     * Constructor for the {@link VarOrdererHelper} class.
     *
     * @param spec The CIF specification.
     * @param variables The synthesis variables, in their original order, before applying any algorithm on it.
     */
    public VarOrdererHelper(Specification spec, List<SynthesisVariable> variables) {
        // Store the input.
        this.spec = spec;
        this.variables = variables;

        // Compute and store different representations of the specification.
        this.hyperEdges = createHyperEdges();
        this.graph = createGraph();

        // Store additional derivative information used to improve performance of some helper operations.
        this.origIndices = IntStream.range(0, variables.size()).boxed()
                .collect(Collectors.toMap(i -> variables.get(i), i -> i));

        // Store the number of characters to use to print various metrics. We compute the length needed to print the
        // current value of each metric, and allow for two additional characters. Based on the assumption that the
        // metrics won't get a 100 times worse, this should provide enough space to neatly print them. If they do get
        // over a 100 times worse, printing may be slightly less neat, but will still work.
        this.metricLengthTotalSpan = fmt("%,d", computeTotalSpanForVarOrder(variables)).length() + 2;
        this.metricLengthTotalSpanAvg = fmt("%,.2f", (double)computeTotalSpanForVarOrder(variables) / hyperEdges.length)
                .length() + 2;
    }

    /**
     * Create hyper-edges representing relations between variables of the CIF specification. Each hyper-edge bitset
     * represents related synthesis variables. Each bit in a hyper-edge bitset represents a synthesis variable.
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
     * represents related synthesis variables. Each bit in a hyper-edge bitset represents a synthesis variable.
     *
     * @return The hyper-edges.
     */
    public BitSet[] getHyperEdges() {
        return hyperEdges;
    }

    /**
     * Create a weighted undirected adjacency graph representing relations between variables of the CIF specification.
     * Each node represents a synthesis variable. Each edge represents a weighted relation between two different
     * synthesis variables.
     *
     * @return The graph.
     */
    private Graph createGraph() {
        // Compute weighted graph edges. The number of times two variables occur together in a hyper-edge determines
        // the weight of the graph edge between the two variables.
        Map<Pair<Integer, Integer>, Integer> graphEdges = mapc(hyperEdges.length);
        for (BitSet edge: hyperEdges) {
            for (int i: BitSets.iterateTrueBits(edge)) {
                for (int j: BitSets.iterateTrueBits(edge, i + 1)) {
                    graphEdges.merge(pair(i, j), 1, (a, b) -> a + b);
                }
            }
        }

        // Create undirected weighted graph.
        Graph graph = new Graph(variables.size());
        for (Entry<Pair<Integer, Integer>, Integer> graphEdge: graphEdges.entrySet()) {
            Node ni = graph.node(graphEdge.getKey().left);
            Node nj = graph.node(graphEdge.getKey().right);
            int weight = graphEdge.getValue();
            ni.addEdge(nj, weight, true);
        }

        // Return the graph.
        return graph;
    }

    /**
     * Returns a weighted undirected adjacency graph representing relations between variables of the CIF specification.
     * Each node represents a synthesis variable. Each edge represents a weighted relation between two different
     * synthesis variables.
     *
     * @return The graph.
     */
    public Graph getGraph() {
        return graph;
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
     * @param order The variable order.
     * @return The total span.
     */
    public long computeTotalSpanForVarOrder(List<SynthesisVariable> order) {
        int[] newIndices = getNewIndicesForVarOrder(order);
        return computeTotalSpanForNewIndices(newIndices);
    }

    /**
     * Computes the total span metric.
     *
     * @param order The node order.
     * @return The total span.
     */
    public long computeTotalSpanForNodeOrder(List<Node> order) {
        int[] newIndices = getNewIndicesForNodeOrder(order);
        return computeTotalSpanForNewIndices(newIndices);
    }

    /**
     * Computes the total span metric.
     *
     * @param newIndices For each variable, its new 0-based index.
     * @return The total span.
     */
    public long computeTotalSpanForNewIndices(int[] newIndices) {
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
     * Print various metrics as debug output, for the given variable order.
     *
     * @param dbgLevel The debug indentation level.
     * @param order The variable order.
     * @param annotation A human-readable text indicating the reason for printing the metrics.
     */
    public void dbgMetricsForVarOrder(int dbgLevel, List<SynthesisVariable> order, String annotation) {
        int[] newIndices = getNewIndicesForVarOrder(order);
        dbgMetricsForNewIndices(dbgLevel, newIndices, annotation);
    }

    /**
     * Print various metrics as debug output, for the given node order.
     *
     * @param dbgLevel The debug indentation level.
     * @param order The node order.
     * @param annotation A human-readable text indicating the reason for printing the metrics.
     */
    public void dbgMetricsForNodeOrder(int dbgLevel, List<Node> order, String annotation) {
        int[] newIndices = getNewIndicesForNodeOrder(order);
        dbgMetricsForNewIndices(dbgLevel, newIndices, annotation);
    }

    /**
     * Print various metrics as debug output, for the given new indices of the variables.
     *
     * @param dbgLevel The debug indentation level.
     * @param newIndices For each variable, its new 0-based index.
     * @param annotation A human-readable text indicating the reason for printing the metrics.
     */
    public void dbgMetricsForNewIndices(int dbgLevel, int[] newIndices, String annotation) {
        long totalSpan = computeTotalSpanForNewIndices(newIndices);
        dbgMetrics(dbgLevel, totalSpan, annotation);
    }

    /**
     * Print various metrics as debug output.
     *
     * @param dbgLevel The debug indentation level.
     * @param totalSpan The given total span.
     * @param annotation A human-readable text indicating the reason for printing the metrics.
     */
    public void dbgMetrics(int dbgLevel, long totalSpan, String annotation) {
        String fmtTotalSpan = fmt("%," + metricLengthTotalSpan + "d", totalSpan);
        String fmtTotalSpanAvg = fmt("%," + metricLengthTotalSpanAvg + ".2f", (double)totalSpan / hyperEdges.length);
        dbg(dbgLevel, "Total span: %s (total) %s (avg/edge) [%s]", fmtTotalSpan, fmtTotalSpanAvg, annotation);
    }

    /**
     * Get the new variable indices from a new variable order.
     *
     * @param order The new variable order.
     * @return For each variable, its new 0-based index.
     */
    public int[] getNewIndicesForVarOrder(List<SynthesisVariable> order) {
        int[] newIndices = new int[order.size()];
        for (int i = 0; i < order.size(); i++) {
            newIndices[origIndices.get(order.get(i))] = i;
        }
        return newIndices;
    }

    /**
     * Get the new variable indices from a new node order.
     *
     * @param order The new node order.
     * @return For each variable/node, its new 0-based index.
     */
    public int[] getNewIndicesForNodeOrder(List<Node> order) {
        int[] newIndices = new int[order.size()];
        for (int i = 0; i < order.size(); i++) {
            newIndices[order.get(i).index] = i;
        }
        return newIndices;
    }

    /**
     * Reorder the synthesis variables to a new order.
     *
     * @param order The new variable/node order.
     * @return The synthesis variables, in their new order.
     */
    public List<SynthesisVariable> reorderForNodeOrder(List<Node> order) {
        int[] varOrder = getNewIndicesForNodeOrder(order);
        return reorderForNewIndices(varOrder);
    }

    /**
     * Reorder the synthesis variables to a new order.
     *
     * @param newIndices For each variable, its new 0-based index.
     * @return The synthesis variables, in their new order.
     */
    public List<SynthesisVariable> reorderForNewIndices(int[] newIndices) {
        Assert.areEqual(variables.size(), newIndices.length);
        SynthesisVariable[] result = new SynthesisVariable[variables.size()];
        for (int i = 0; i < newIndices.length; i++) {
            result[newIndices[i]] = variables.get(i);
        }
        return Arrays.asList(result);
    }
}
