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
 * Class for finding simple cycles in a directed graph using depth-first search.
 *
 * <P>
 * A simple cycle is a sequence of edges where each non-last edge arrives at the vertex that is used as starting vertex
 * for the next edge and the last edge arrives at the starting vertex of the first edge, and every edge in the cycle
 * starts from a different vertex.
 * </p>
 *
 * @param <G> Graph class being searched.
 * @param <V> Vertex class in the graph.
 * @param <E> Edge class in the graph.
 * @param <C> Class that stores a found cycle. This class should implement hashing and equality under rotation of its
 *     edges. For example, cycle {@code abc} may again be found as cycle {@code bca} or {@code cab}.
 */
public abstract class DirectedGraphCycleFinder<G, V, E extends DirectedGraphCycleFinder.GraphEdge<V>, C> {
    /** Edges currently being searched. */
    private List<E> stack;

    /** Starting vertex of the edges at the {@link #stack} to their index at the {@link #stack}. */
    private Map<V, Integer> stackIndex;

    /** Vertices that are or were being searched for cycles. */
    private Set<V> visitedVertices;

    /** Found cycles in the graph. */
    private Set<C> foundCycles;

    /** Test function to detect a user abort request. */
    private final BooleanSupplier isTerminationRequested;

    /**
     * Constructor of the {@link DirectedGraphCycleFinder} class.
     *
     * @param isTerminationRequested Test function to detect a user abort request. If {@code null}, termination
     *     detection is disabled.
     */
    public DirectedGraphCycleFinder(BooleanSupplier isTerminationRequested) {
        this.isTerminationRequested = isTerminationRequested;
    }

    /**
     * Find all simple cycles in the provided graph.
     *
     * @param graph Graph being searched.
     * @return The found simple cycles in the graph.
     */
    public Set<C> findSimpleCycles(G graph) {
        // Initialize data fields for the search.
        List<V> vertices = getVertices(graph);
        stack = listc(vertices.size() + 1);
        stackIndex = mapc(vertices.size());
        visitedVertices = setc(vertices.size());

        Set<C> foundCycles = set();
        this.foundCycles = foundCycles;

        // Examine all vertices of the graph. Due to directed edges or limited edges that can be traversed it is
        // possible that some vertices are not always reachable.
        for (V vertex: vertices) {
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
     * Expand the vertex path being searched by the given vertex.
     *
     * @param vertex Next vertex to search.
     */
    private void expandVertexPath(V vertex) {
        if (isTerminationRequested != null && isTerminationRequested.getAsBoolean()) {
            return;
        }

        // Expand the path by the new vertex.
        visitedVertices.add(vertex);
        int vertexStackPos = stack.size();
        stackIndex.put(vertex, vertexStackPos);
        stack.add(null); // Is replaced with proper edges below.

        // Explore the edges from the new vertex for further expansion.
        for (E edge: getOutgoingEdges(vertex)) {
            V edgeTargetVertex = edge.destinationVertex;
            Integer cycleStartIndex = stackIndex.get(edgeTargetVertex);

            if (cycleStartIndex == null) {
                // New vertex in this search, explore it. Note that this ignores the visitedVertices set, so
                // multiple graph traversals over the same vertex in different searches are possible.
                stack.set(vertexStackPos, edge);
                expandVertexPath(edgeTargetVertex);
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
    protected abstract List<V> getVertices(G graph);

    /**
     * Obtain the edges that leave from the provided vertex in the graph.
     *
     * @param vertex Starting vertex of all returned edges.
     * @return The returned edges.
     */
    protected abstract List<E> getOutgoingEdges(V vertex);

    /**
     * Add a cycle to the collection of found cycles.
     *
     * @param edges Edges that form a new cycle. The supplied list is not stable, it must be copied to preserve the
     *     result.
     * @param foundCycles Previously found cycles.
     */
    protected abstract void addCycle(List<E> edges, Set<C> foundCycles);

    /**
     * An edge in the graph.
     *
     * @param <V> Vertex class in the graph.
     */
    public static class GraphEdge<V> {
        /** Vertex where the edge leaves. */
        public final V startVertex;

        /** Vertex where the edge arrives. */
        public final V destinationVertex;

        /**
         * Constructor of the {@link GraphEdge} class.
         *
         * @param startVertex Vertex where the edge leaves.
         * @param destinationVertex Vertex where the edge arrives.
         */
        public GraphEdge(V startVertex, V destinationVertex) {
            this.startVertex = startVertex;
            this.destinationVertex = destinationVertex;
        }
    }
}
