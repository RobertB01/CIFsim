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

package org.eclipse.escet.cif.controllercheck.finiteresponse;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.common.app.framework.AppEnvData;

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
     * @param <Cycle> Class that stores a found cycle. This class should implement hashing and equality under rotation
     *     of its edges. For example, cycle {@code abc} may again be found as cycle {@code bca} or {@code cab}.
     */
    public abstract static class GenericDfsSimpleCyclesFinder<Graph, Cycle> {
        /** Edges currently being searched. */
        List<GraphEdge> stack;

        /** Starting vertex of the edges at the {@link #stack} to their index at the {@link #stack}. */
        Map<Location, Integer> stackIndex;

        /** Vertices that are or were being searched for cycles. */
        Set<Location> visitedVertices;

        /** Found cycles in the graph. */
        private Set<Cycle> foundCycles;

        /**
         * XXX
         *
         * @param graph Graph being searched.
         */
        public Set<Cycle> searchEventLoops(Graph graph, AppEnvData env) {
            List<Location> vertices = getVertices(graph);
            stack = listc(vertices.size() + 1);
            stackIndex = mapc(vertices.size());
            visitedVertices = setc(vertices.size());

            Set<Cycle> foundCycles = set();
            this.foundCycles = foundCycles;

            for (Location loc: vertices) {
                if (visitedVertices.contains(loc)) {
                    continue;
                }
                searchEventLoops(loc, env);

                if (env.isTerminationRequested()) {
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
         * XXX
         */
        private void searchEventLoops(Location rootLoc, AppEnvData env) {
            if (env.isTerminationRequested()) {
                return;
            }

            visitedVertices.add(rootLoc);
            stackIndex.put(rootLoc, stack.size());

            for (GraphEdge edge: getEdges(rootLoc)) {
                Location edgeTargetLoc = edge.destinationVertex;
                Integer loopStartIndex = stackIndex.get(edgeTargetLoc);

                if (loopStartIndex == null) {
                    stack.add(edge);
                    searchEventLoops(edgeTargetLoc, env);
                    stack.remove(stack.size() - 1);
                } else {
                    stack.add(edge);
                    foundCycles.add(makeCycle(stack.subList(loopStartIndex, stack.size())));
                    stack.remove(stack.size() - 1);
                }

                if (env.isTerminationRequested()) {
                    return;
                }
            }
            stackIndex.remove(rootLoc);
        }

        /**
         * Get all the vertices of the graph.
         *
         * @param graph Graph to use.
         * @return All the vertices of the graph.
         */
        public abstract List<Location> getVertices(Graph graph);

        /**
         * Obtain the edge that leaves from the provided vertex in the graph.
         *
         * @param vertex Starting vertex of all returned edges.
         * @return The returned edges.
         */
        public abstract List<GraphEdge> getEdges(Location vertex);

        /**
         * Construct a stored cycle from a sequence of edges.
         *
         * @param edges Edges that form a cycle. The supplied list is not stable, it must be copied to preserve the
         *     result.
         * @return The constructed cycle.
         */
        public abstract Cycle makeCycle(List<GraphEdge> edges);
    }

    /** An edge in the graph. */
    public static class GraphEdge {
        /** Vertex where the edge leaves. */
        public final Location startVertex;

        /** Vertex where the edge arrives. */
        public final Location destinationVertex;

        /**
         * Constructor of the {@link GraphEdge} class.
         *
         * @param startVertex Vertex where the edge leaves.
         * @param destinationVertex Vertex where the edge arrives.
         */
        public GraphEdge(Location startVertex, Location destinationVertex) {
            this.startVertex = startVertex;
            this.destinationVertex = destinationVertex;
        }
    }
}
