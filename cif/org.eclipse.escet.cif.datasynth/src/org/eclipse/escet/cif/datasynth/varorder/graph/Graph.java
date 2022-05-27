//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.varorder.graph;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.list2set;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.java.Assert;

/** A weighted graph with empty diagonal. */
public class Graph {
    /**
     * The list of nodes of this graph, in ascending order of their indices. The first node has index zero and the last
     * node has index {@code n - 1}, assuming there are {@code n} nodes.
     */
    public final List<Node> nodes;

    /**
     * Constructor for the {@link Graph} class.
     *
     * @param nodeCount The number of nodes.
     */
    public Graph(int nodeCount) {
        this.nodes = listc(nodeCount);
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node(i));
        }
    }

    /**
     * Returns the node with the given index.
     *
     * @param index The node index.
     * @return The node.
     */
    public Node node(int index) {
        return nodes.get(index);
    }

    /**
     * Returns the size of the graph.
     *
     * @return The number of nodes in the graph.
     */
    public int size() {
        return nodes.size();
    }

    /**
     * Returns the number of unique edges of the graph.
     *
     * @return The number of unique edges of the graph.
     */
    public long edgeCount() {
        return nodes.stream() // Get all nodes.
                .flatMap(n -> n.neighbours().stream().map(m -> pair(n.index, m.index))) // Get edge node index pairs.
                .map(p -> (p.left > p.right) ? pair(p.right, p.left) : p) // Normalize edge pairs: left < right.
                .distinct().count(); // Count unique edges.
    }

    /**
     * Partition this graph, which may be non-connected, into connected sub-graphs.
     *
     * @return Per partition (connected sub-graphs), the nodes of that partition.
     */
    public List<List<Node>> partition() {
        List<List<Node>> partitions = listc(1); // Optimize for connected graphs.
        Set<Node> unpartitionedNodes = list2set(nodes);
        while (!unpartitionedNodes.isEmpty()) {
            Node unpartitionedNode = unpartitionedNodes.iterator().next();
            Set<Node> partition = set();
            Deque<Node> queue = new LinkedList<>();
            queue.add(unpartitionedNode);
            while (!queue.isEmpty()) {
                Node curNode = queue.remove();
                boolean added = partition.add(curNode);
                if (added) {
                    queue.addAll(curNode.neighbours());
                }
            }
            List<Node> sortedPartition = partition.stream().sorted(Comparator.comparingInt(n -> n.index))
                    .collect(Collectors.toList());
            partitions.add(sortedPartition); // Keep nodes within partition in origin order.
            unpartitionedNodes.removeAll(partition);
        }
        return partitions;
    }

    /**
     * Reorder the graph to the given node order.
     *
     * @param newOrder The new node order.
     * @return The newly created reordered graph.
     */
    public Graph reorder(List<Node> newOrder) {
        // Check that given nodes are the nodes of this graph in some order.
        Assert.areEqual(list2set(newOrder), list2set(nodes));

        // Construct the new graph.
        Graph newGraph = new Graph(nodes.size());

        // Get mapping of old nodes to new nodes.
        Map<Node, Node> nodeMap = IntStream.range(0, newOrder.size()).boxed()
                .collect(Collectors.toMap(newOrder::get, i -> newGraph.nodes.get(i)));

        // Add edges to new graph.
        for (Node node: nodes) {
            Node newNode = nodeMap.get(node);
            for (Entry<Node, Integer> edge: node.edges()) {
                Node newTarget = nodeMap.get(edge.getKey());
                newNode.addEdge(newTarget, edge.getValue());
            }
        }
        return newGraph;
    }

    /**
     * Constructs a graph from the given text.
     *
     * @param text The text. Each line is considered a row. Each character is an element in the row. A '{@code .}' is
     *     interpreted as zero. Any other character must be a single digit positive weight value.
     * @return The graph.
     * @throws AssertionError If the text is not valid.
     * @throws IllegalArgumentException If the text is not valid.
     */
    public static Graph fromString(String text) {
        List<String> lines = Arrays.asList(text.replace("\r", "").split("\n"));
        Graph graph = new Graph(lines.size());
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Assert.areEqual(lines.size(), line.length());
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);

                // Get weight.
                int weight = 0;
                if (c != '.') {
                    try {
                        weight = Integer.parseInt(String.valueOf(c));
                        Assert.check(weight > 0);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(fmt("Invalid weight \"%s\".", c), e);
                    }
                }

                // Add edge if non-zero weight.
                if (i == j) { // Diagonal.
                    Assert.areEqual(0, weight); // Diagonal must be empty.
                } else if (weight > 0) {
                    graph.nodes.get(i).addEdge(graph.nodes.get(j), weight);
                }
            }
        }
        return graph;
    }

    @Override
    public String toString() {
        int minWeight = nodes.stream().flatMap(n -> n.edges().stream()).map(e -> e.getValue())
                .min(Comparator.naturalOrder()).orElse(0);
        int maxWeight = nodes.stream().flatMap(n -> n.edges().stream()).map(e -> e.getValue())
                .max(Comparator.naturalOrder()).orElse(0);
        int separator = (minWeight >= 0 && maxWeight <= 9) ? 0 : 1;
        return toString(separator);
    }

    /**
     * Converts the graph to a textual matrix representation.
     *
     * @param separation The number of spaces separation between columns.
     * @return The textual matrix representation.
     */
    public String toString(int separation) {
        GridBox grid = new GridBox(nodes.size(), nodes.size(), 0, separation);
        for (Node node: nodes) {
            for (Entry<Node, Integer> edge: node.edges()) {
                grid.set(node.index, edge.getKey().index, Integer.toString(edge.getValue()));
            }
        }
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (grid.get(i, j) == null) {
                    grid.set(i, j, ".");
                }
            }
        }
        return String.join("\n", grid.getLines());
    }
}
