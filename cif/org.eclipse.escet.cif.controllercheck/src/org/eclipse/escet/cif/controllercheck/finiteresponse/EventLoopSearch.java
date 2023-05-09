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
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;

import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.app.framework.AppEnvData;
import org.eclipse.escet.common.java.DirectedGraphCycleFinder;
import org.eclipse.escet.common.java.DirectedGraphCycleFinder.GraphEdge;
import org.eclipse.escet.common.java.ListProductIterator;

/** Static class for finding event loops. */
public class EventLoopSearch {
    /** Constructor of the static {@link EventLoopSearch} class. */
    private EventLoopSearch() {
        // Static class.
    }

    /**
     * Search for event loops in an automaton, where the loop consists of events from a specific set of events. The loop
     * can be reachable from any location of the automaton (also locations that are not reachable from the initial
     * location).
     *
     * @param aut The automaton in which to search for the event loops.
     * @param loopEvents The events that can form an event loop.
     * @param env The application context to use.
     * @return The event loops in the specified automaton.
     */
    public static Set<EventLoop> searchEventLoops(Automaton aut, Set<Event> loopEvents, AppEnvData env) {
        BooleanSupplier isTerminationRequested = () -> env.isTerminationRequested();
        EventLoopFinder finder = new EventLoopFinder(loopEvents, isTerminationRequested);
        return finder.findSimpleCycles(aut);
    }

    /** Cycle finder for finding event loops in an automaton. */
    public static class EventLoopFinder
            extends DirectedGraphCycleFinder<Automaton, Location, EventLoopEdge, EventLoop>
    {
        /** The events that can form an event loop. */
        private final Set<Event> loopEvents;

        /**
         * Constructor of the {@link EventLoopFinder} class.
         *
         * @param loopEvents The events that can form an event loop.
         * @param isTerminationRequested Test function to detect a user abort request. If {@code null}, termination
         *     detection is disabled.
         */
        public EventLoopFinder(Set<Event> loopEvents, BooleanSupplier isTerminationRequested) {
            super(isTerminationRequested);
            this.loopEvents = loopEvents;
        }

        @Override
        protected List<Location> getVertices(Automaton graph) {
            return graph.getLocations();
        }

        @Override
        protected void addCycle(List<EventLoopEdge> edges, Set<EventLoop> foundCycles) {
            // Collect the events of each edge in the cycle.
            List<List<Event>> eventCollections = listc(edges.size());
            for (EventLoopEdge edge: edges) {
                eventCollections.add(edge.events);
            }

            // And expand to all combinations of one event for each edge.
            ListProductIterator<Event> iter = new ListProductIterator<>(eventCollections);
            while (iter.hasNext()) {
                List<Event> selectedEvents = iter.next();
                foundCycles.add(new EventLoop(selectedEvents));
            }
        }

        @Override
        protected List<EventLoopEdge> getOutgoingEdges(Location vertex) {
            List<EventLoopEdge> edges = list();
            for (Edge edge: vertex.getEdges()) {
                if (isEmptyIntersection(loopEvents, getEvents(edge))) {
                    continue;
                }

                Location edgeTargetLoc = CifEdgeUtils.getTarget(edge);
                List<Event> edgeEvents = list();
                for (Event event: getEvents(edge)) {
                    if (loopEvents.contains(event)) {
                        edgeEvents.add(event);
                    }
                    edges.add(new EventLoopEdge(vertex, edgeTargetLoc, edgeEvents));
                }
            }
            return edges;
        }
    }

    /** Edge class for finding event loops in an automaton. */
    private static class EventLoopEdge extends GraphEdge<Location> {
        /** The events of the edge. */
        public final List<Event> events;

        /**
         * Constructor of the {@link EventLoopEdge} class.
         *
         * @param startVertex Vertex where the edge leaves.
         * @param destinationVertex Vertex where the edge arrives.
         * @param events The events of the edge.
         */
        public EventLoopEdge(Location startVertex, Location destinationVertex, List<Event> events) {
            super(startVertex, destinationVertex);
            this.events = events;
        }
    }
}
