//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.checks.finiteresponse;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.java.DirectedGraphCycleFinder;
import org.eclipse.escet.common.java.DirectedGraphCycleFinder.GraphEdge;
import org.eclipse.escet.common.java.ListProductIterator;
import org.eclipse.escet.common.java.Sets;

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
     * @param shouldTerminate Callback that indicates whether execution should be terminated on user request.
     * @return The event loops in the specified automaton, or {@code null} if termination was requested.
     */
    public static Set<EventLoop> searchEventLoops(Automaton aut, Set<Event> loopEvents,
            Supplier<Boolean> shouldTerminate)
    {
        EventLoopFinder finder = new EventLoopFinder(loopEvents, shouldTerminate);
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
         * @param shouldTerminate Callback that indicates whether execution should be terminated on user request.
         */
        public EventLoopFinder(Set<Event> loopEvents, Supplier<Boolean> shouldTerminate) {
            super(shouldTerminate);
            this.loopEvents = loopEvents;
        }

        @Override
        protected List<Location> getVertices(Automaton graph) {
            return graph.getLocations();
        }

        @Override
        protected void addCycle(Automaton aut, List<EventLoopEdge> edges, Set<EventLoop> foundCycles) {
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
        protected List<EventLoopEdge> getOutgoingEdges(Automaton aut, Location vertex) {
            List<EventLoopEdge> edges = list();
            for (Edge edge: vertex.getEdges()) {
                if (Sets.isEmptyIntersection(loopEvents, CifEventUtils.getEvents(edge))) {
                    continue;
                }

                Location edgeTargetLoc = CifEdgeUtils.getTarget(edge);
                List<Event> edgeEvents = list();
                for (Event event: CifEventUtils.getEvents(edge)) {
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
