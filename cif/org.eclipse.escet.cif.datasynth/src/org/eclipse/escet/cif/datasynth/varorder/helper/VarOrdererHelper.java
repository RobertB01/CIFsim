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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.datasynth.options.BddHyperEdgeAlgoOption;
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
    /** The synthesis variables, in their original order, before applying any algorithm on it. */
    private final List<SynthesisVariable> variables;

    /** For each synthesis variable in the original variable order, its 0-based index within that order. */
    private final Map<SynthesisVariable, Integer> origIndices;

    /**
     * For each {@link RelationsKind} (outer list), the hyper-edges representing relations from the CIF specification
     * (inner list). Each hyper-edge bitset represents related synthesis variables. Each bit in a hyper-edge bitset
     * represents a synthesis variable.
     */
    private final List<List<BitSet>> hyperEdges;

    /**
     * For each {@link RelationsKind}, the graph representing relations from the CIF specification. Each node represents
     * a synthesis variable. Each edge represents a weighted relation between two different synthesis variables.
     */
    private final List<Graph> graphs;

    /** The number of characters to use for printing the total span metric in debug output. */
    private final int metricLengthTotalSpan;

    /** The number of characters to use for printing the total span metric, as average per edge, in debug output. */
    private final int metricLengthTotalSpanAvg;

    /** The number of characters to use for printing the Weighted Event Span (WES) metric in debug output. */
    private final int metricLengthWes;

    /**
     * The number of characters to use for printing the Weighted Event Span (WES) metric, as average per edge, in debug
     * output.
     */
    private final int metricLengthWesAvg;

    /**
     * Constructor for the {@link VarOrdererHelper} class.
     *
     * @param spec The CIF specification.
     * @param variables The synthesis variables, in their original order, before applying any algorithm on it.
     */
    public VarOrdererHelper(Specification spec, List<SynthesisVariable> variables) {
        // Store the variables.
        this.variables = variables;

        // Compute and store different representations of the relations from the specification.
        List<BitSet> legacyHyperEdges = createHyperEdges(new LegacyHyperEdgeCreator(spec, variables));
        List<BitSet> linearizedHyperEdges = createHyperEdges(new LinearizedHyperEdgeCreator(spec, variables));
        List<BitSet> configuredHyperEdges = switch (BddHyperEdgeAlgoOption.getAlgo()) {
            case LEGACY -> legacyHyperEdges;
            case LINEARIZED -> linearizedHyperEdges;
        };
        this.hyperEdges = list(configuredHyperEdges, legacyHyperEdges, linearizedHyperEdges);

        Graph legacyGraph = createGraph(legacyHyperEdges);
        Graph linearizedGraph = createGraph(linearizedHyperEdges);
        Graph configuredGraph = switch (BddHyperEdgeAlgoOption.getAlgo()) {
            case LEGACY -> legacyGraph;
            case LINEARIZED -> linearizedGraph;
        };
        this.graphs = list(configuredGraph, legacyGraph, linearizedGraph);

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
        this.metricLengthWes = fmt("%,.6f", computeWesForVarOrder(variables)).length() + 2;
        this.metricLengthWesAvg = fmt("%,.6f", computeWesForVarOrder(variables) / hyperEdges.length).length() + 2;
    }

    /**
     * Create hyper-edges representing relations between variables of the CIF specification. Each hyper-edge bitset
     * represents related synthesis variables. Each bit in a hyper-edge bitset represents a synthesis variable.
     *
     * @param creator The hyper-edge creator to use to create the hyper-edges.
     * @return The hyper-edges.
     */
    private List<BitSet> createHyperEdges(HyperEdgeCreator creator) {
        List<BitSet> hyperEdges = creator.getHyperEdges();
        Assert.check(hyperEdges.stream().allMatch(edge -> !edge.isEmpty()));
        return Collections.unmodifiableList(hyperEdges);
    }

    /**
     * Returns hyper-edges representing relations between variables of the CIF specification. Each hyper-edge bitset
     * represents related synthesis variables. Each bit in a hyper-edge bitset represents a synthesis variable.
     *
     * @param relationsKind The kind of relations for which to return the hyper-edges.
     * @return The hyper-edges.
     */
    public List<BitSet> getHyperEdges(RelationsKind relationsKind) {
        return hyperEdges.get(relationsKind.ordinal());
    }

    /**
     * Create a weighted undirected adjacency graph representing relations between variables of the CIF specification.
     * Each node represents a synthesis variable. Each edge represents a weighted relation between two different
     * synthesis variables.
     *
     * @param hyperEdges The hyper-edges from which to create the graph.
     * @return The graph.
     */
    private Graph createGraph(List<BitSet> hyperEdges) {
        // Compute weighted graph edges. The number of times two variables occur together in a hyper-edge determines
        // the weight of the graph edge between the two variables.
        Map<Pair<Integer, Integer>, Integer> graphEdges = mapc(hyperEdges.size());
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
     * @param relationsKind The kind of relations for which to return the graph.
     * @return The graph.
     */
    public Graph getGraph(RelationsKind relationsKind) {
        return graphs.get(relationsKind.ordinal());
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
     * Compute the total span metric.
     *
     * @param order The variable order.
     * @return The total span.
     */
    public long computeTotalSpanForVarOrder(List<SynthesisVariable> order) {
        int[] newIndices = getNewIndicesForVarOrder(order);
        return computeTotalSpanForNewIndices(newIndices);
    }

    /**
     * Compute the total span metric.
     *
     * @param order The node order.
     * @return The total span.
     */
    public long computeTotalSpanForNodeOrder(List<Node> order) {
        int[] newIndices = getNewIndicesForNodeOrder(order);
        return computeTotalSpanForNewIndices(newIndices);
    }

    /**
     * Compute the Weighted Event Span (WES) metric.
     *
     * @param order The variable order.
     * @return The Weighted Event Span (WES).
     */
    public double computeWesForVarOrder(List<SynthesisVariable> order) {
        int[] newIndices = getNewIndicesForVarOrder(order);
        return computeWesForNewIndices(newIndices);
    }

    /**
     * Compute the Weighted Event Span (WES) metric.
     *
     * @param order The node order.
     * @return The Weighted Event Span (WES).
     */
    public double computeWesForNodeOrder(List<Node> order) {
        int[] newIndices = getNewIndicesForNodeOrder(order);
        return computeWesForNewIndices(newIndices);
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
        String msg = fmtMetrics(newIndices, annotation);
        dbg(dbgLevel, msg);
    }

    /**
     * Format various metrics, for the given new indices of the variables.
     *
     * @param newIndices For each variable, its new 0-based index.
     * @param annotation A human-readable text indicating the reason for formatting the metrics.
     * @return The formatted metrics.
     */
    public String fmtMetrics(int[] newIndices, String annotation) {
        long totalSpan = computeTotalSpanForNewIndices(newIndices);
        double wes = computeWesForNewIndices(newIndices);
        String fmtTotalSpan = fmt("%," + metricLengthTotalSpan + "d", totalSpan);
        String fmtTotalSpanAvg = fmt("%," + metricLengthTotalSpanAvg + ".2f", (double)totalSpan / hyperEdges.length);
        String fmtWes = fmt("%," + metricLengthWes + ".6f", wes);
        String fmtWesAvg = fmt("%," + metricLengthWesAvg + ".6f", wes / hyperEdges.length);
        return fmt("Total span: %s (total) %s (avg/edge) / WES: %s (total) %s (avg/edge) [%s]", fmtTotalSpan,
                fmtTotalSpanAvg, fmtWes, fmtWesAvg, annotation);
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
