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

import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.controllercheck.finiteresponse.DfsFindSimpleCycles.GenericDfsSimpleCyclesFinder;
import org.eclipse.escet.cif.controllercheck.finiteresponse.DfsFindSimpleCycles.GraphEdge;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.app.framework.AppEnvData;

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
        EventLoopFinder finder = new EventLoopFinder(loopEvents);
        return finder.findSimpleCycles(aut, env);
    }

    /** Cycle finder for finding event loops in an automaton. */
    public static class EventLoopFinder extends GenericDfsSimpleCyclesFinder<Automaton, Location, EventLoop> {
        /** The events that can form an event loop. */
        private final Set<Event> loopEvents;

        /**
         * Constructor of the {@link EventLoopFinder} class.
         *
         * @param loopEvents The events that can form an event loop.
         */
        public EventLoopFinder(Set<Event> loopEvents) {
            this.loopEvents = loopEvents;
        }

        @Override
        public List<Location> getVertices(Automaton graph) {
            return graph.getLocations();
        }

        @Override
        public EventLoop makeCycle(List<GraphEdge<Location>> edges) {
            List<Event> events = listc(edges.size());
            for (GraphEdge<Location> edge: edges) {
                EventLoopEdge evtLoopEdge = (EventLoopEdge)edge;
                events.add(evtLoopEdge.event);
            }
            return new EventLoop(events);
        }

        @Override
        public List<GraphEdge<Location>> getEdges(Location vertex) {
            List<GraphEdge<Location>> edges = list();
            for (Edge edge: vertex.getEdges()) {
                if (isEmptyIntersection(loopEvents, getEvents(edge))) {
                    continue;
                }

                Location edgeTargetLoc = CifEdgeUtils.getTarget(edge);
                for (Event event: getEvents(edge)) {
                    if (loopEvents.contains(event)) {
                        edges.add(new EventLoopEdge(vertex, edgeTargetLoc, event));
                    }
                }
            }
            return edges;
        }
    }

    /** Edge class for finding event loops in an automaton. */
    private static class EventLoopEdge extends GraphEdge<Location> {
        /** The event of the edge. */
        public final Event event;

        /**
         * Constructor of the {@link EventLoopEdge} class.
         *
         * @param startVertex Vertex where the edge leaves.
         * @param destinationVertex Vertex where the edge arrives.
         * @param event The event of the edge,
         */
        public EventLoopEdge(Location startVertex, Location destinationVertex, Event event) {
            super(startVertex, destinationVertex);
            this.event = event;
        }
    }
}
