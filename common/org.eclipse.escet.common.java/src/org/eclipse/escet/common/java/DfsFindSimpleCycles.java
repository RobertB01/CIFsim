//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * Class containing data structures and code for finding simple cycles in a directed graph.
 *
 * <P>
 * A simple cycle is a sequence of edges where each non-last edge arrives at the vertex that is used as starting vertex
 * for the next edge and the last edge arrives at the starting vertex of the first edge, and every edge in the cycle
 * starts from a different vertex.
 * </p>
 */
public class DfsFindSimpleCycles {
    /** Constructor of the static {@link DfsFindSimpleCycles} class. */
    private DfsFindSimpleCycles() {
        // Static class.
    }

    /**
     * Generic class for finding simple cycles using depth-first search.
     *
     * @param <Graph> Graph class being searched.
     * @param <Vertex> Vertex class in the graph.
     * @param <Edge> Edge class in the graph.
     * @param <Cycle> Class that stores a found cycle. This class should implement hashing and equality under rotation
     *     of its edges. For example, cycle {@code abc} may again be found as cycle {@code bca} or {@code cab}.
     */
    public abstract static class GenericDfsSimpleCyclesFinder<Graph, Vertex, Edge extends GraphEdge<Vertex>, Cycle> {
        /** Edges currently being searched. */
        List<Edge> stack;

        /** Starting vertex of the edges at the {@link #stack} to their index at the {@link #stack}. */
        Map<Vertex, Integer> stackIndex;

        /** Vertices that are or were being searched for cycles. */
        Set<Vertex> visitedVertices;

        /** Found cycles in the graph. */
        private Set<Cycle> foundCycles;

        /** Test function to detect a user abort request. */
        private final BooleanSupplier isTerminationRequested;

        /**
         * Constructor of the {@link GenericDfsSimpleCyclesFinder} class.
         *
         * @param isTerminationRequested Test function to detect a user abort request. If {@code null}, termination
         *     detection is disabled.
         */
        public GenericDfsSimpleCyclesFinder(BooleanSupplier isTerminationRequested) {
            this.isTerminationRequested = isTerminationRequested;
        }

        /**
         * Find all simple cycles in the provided graph.
         *
         * @param graph Graph being searched.
         * @return The found simple cycles in the graph.
         */
        public Set<Cycle> findSimpleCycles(Graph graph) {
            // Initialize data fields for the search.
            List<Vertex> vertices = getVertices(graph);
            stack = listc(vertices.size() + 1);
            stackIndex = mapc(vertices.size());
            visitedVertices = setc(vertices.size());

            Set<Cycle> foundCycles = set();
            this.foundCycles = foundCycles;

            // Examine all vertices of the graph. Due to directed edges or limited edges that can be traversed it is
            // possible that some vertices are not always reachable.
            for (Vertex vertex: vertices) {
                if (visitedVertices.contains(vertex)) {
                    continue;
                }
                expandVertexPath(vertex);

                if (isTerminationRequested != null && isTerminationRequested.getAsBoolean()) {
                    return null;
                }
            }

            // Cleanup and return the found cycles.
            visitedVertices = null;
            stackIndex = null;
            stack = null;
            this.foundCycles = null;
            return foundCycles;
        }

        /**
         * Expand the vertex path being searched by the give vertex.
         *
         * @param vertex Next vertex to search.
         */
        private void expandVertexPath(Vertex vertex) {
            if (isTerminationRequested != null && isTerminationRequested.getAsBoolean()) {
                return;
            }

            // Expand the path by the new vertex.
            visitedVertices.add(vertex);
            int vertexStackPos = stack.size();
            stackIndex.put(vertex, vertexStackPos);
            stack.add(null); // Is replaced with proper edges below.

            // Explore the edges from the new vertex for further expansion.
            for (Edge edge: getEdges(vertex)) {
                Vertex edgeTargetLoc = edge.destinationVertex;
                Integer cycleStartIndex = stackIndex.get(edgeTargetLoc);

                if (cycleStartIndex == null) {
                    // New vertex in this search, explore it. Note this ignores the visitedVertices set, so multiple
                    // graph traversals over the same vertex in different searches are possible.
                    stack.set(vertexStackPos, edge);
                    expandVertexPath(edgeTargetLoc);
                } else {
                    // A simple cycle was detected, add it to the collection.
                    stack.set(vertexStackPos, edge);
                    addCycle(stack.subList(cycleStartIndex, vertexStackPos + 1), foundCycles);
                }

                if (isTerminationRequested != null && isTerminationRequested.getAsBoolean()) {
                    return;
                }
            }

            // Done with the vertex, cleanup.
            stack.remove(vertexStackPos);
            stackIndex.remove(vertex);
        }

        /**
         * Get all the vertices of the graph.
         *
         * @param graph Graph to use.
         * @return All the vertices of the graph.
         */
        public abstract List<Vertex> getVertices(Graph graph);

        /**
         * Obtain the edge that leaves from the provided vertex in the graph.
         *
         * @param vertex Starting vertex of all returned edges.
         * @return The returned edges.
         */
        public abstract List<Edge> getEdges(Vertex vertex);

        /**
         * Add a cycle to the collection found cycles.
         *
         * @param edges Edges that form a new cycle. The supplied list is not stable, it must be copied to preserve the
         *     result.
         * @param foundCycles Previously found cycles.
         */
        public abstract void addCycle(List<Edge> edges, Set<Cycle> foundCycles);
    }

    /**
     * An edge in the graph.
     *
     * @param <Vertex> Vertex class in the graph.
     */
    public static class GraphEdge<Vertex> {
        /** Vertex where the edge leaves. */
        public final Vertex startVertex;

        /** Vertex where the edge arrives. */
        public final Vertex destinationVertex;

        /**
         * Constructor of the {@link GraphEdge} class.
         *
         * @param startVertex Vertex where the edge leaves.
         * @param destinationVertex Vertex where the edge arrives.
         */
        public GraphEdge(Vertex startVertex, Vertex destinationVertex) {
            this.startVertex = startVertex;
            this.destinationVertex = destinationVertex;
        }
    }
}
