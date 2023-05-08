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

import static org.eclipse.escet.cif.common.CifEventUtils.getEvents;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
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
        /** Found cycles in the graph. */
        private Set<Cycle> foundCycles;

        /**
         * XXX
         *
         * @param graph Graph being searched.
         */
        public Set<Cycle> searchEventLoops(Graph graph, Set<Event> loopEvents, AppEnvData env) {
            List<Location> vertices = getVertices(graph);
            List<Event> stack = listc(vertices.size() + 1);

            Map<Location, Integer> stackIndex = mapc(vertices.size());

            Set<Location> visitedLocations = setc(vertices.size());

            Set<Cycle> foundCycles = set();
            this.foundCycles = foundCycles;

            for (Location loc: vertices) {
                if (visitedLocations.contains(loc)) {
                    continue;
                }
                searchEventLoops(loc, loopEvents, stackIndex, stack, visitedLocations, env);

                if (env.isTerminationRequested()) {
                    return null;
                }
            }
            this.foundCycles = null;
            return foundCycles;
        }

        /**
         * XXX
         */
        private void searchEventLoops(Location rootLoc, Set<Event> loopEvents, Map<Location, Integer> stackIndex,
                List<Event> stack, Set<Location> visitedLocations, AppEnvData env)
        {
            if (env.isTerminationRequested()) {
                return;
            }

            visitedLocations.add(rootLoc);

            stackIndex.put(rootLoc, stack.size());

            for (Edge edge: rootLoc.getEdges()) {
                if (isEmptyIntersection(loopEvents, getEvents(edge))) {
                    continue;
                }

                Location edgeTargetLoc = CifEdgeUtils.getTarget(edge);
                Integer loopStartIndex = stackIndex.get(edgeTargetLoc);

                for (Event event: getEvents(edge)) {
                    if (!loopEvents.contains(event)) {
                        continue;
                    }

                    if (loopStartIndex == null) {
                        stack.add(event);
                        searchEventLoops(edgeTargetLoc, loopEvents, stackIndex, stack, visitedLocations,
                                env);
                        stack.remove(stack.size() - 1);
                    } else {
                        stack.add(event);
                        foundCycles.add(makeCycle(stack.subList(loopStartIndex, stack.size())));
                        stack.remove(stack.size() - 1);
                    }

                    if (env.isTerminationRequested()) {
                        return;
                    }
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
         * Construct a stored cycle from a sequence of edges.
         *
         * @param edges Edges that form a cycle. The supplied list is not stable, it must be copied to preserve the
         *     result.
         * @return The constructed cycle.
         */
        public abstract Cycle makeCycle(List<Event> edges);
    }
}
