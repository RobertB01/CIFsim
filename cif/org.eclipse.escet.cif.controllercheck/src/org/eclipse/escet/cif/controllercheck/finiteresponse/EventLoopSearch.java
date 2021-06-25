//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** Static class with loop finding functions, based on Tarjan's strongly connected component algorithm. */
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
     * @return The event loops in the specified automaton.
     */
    public static Set<EventLoop> searchEventLoops(Automaton aut, Set<Event> loopEvents) {
        // Stack stores all the edges that we traverse, when searching for loops.
        List<Event> stack = listc(aut.getLocations().size() + 1);

        // Every location that is visited is stored together with its position in the stack, such
        // that we can determine if a location is already visited.
        Map<Location, Integer> stackIndex = mapc(aut.getLocations().size());

        // Store which locations have been visited. If a location can only be reached from the initial location
        // with non-loop events, we have to do multiple searchLoopFromLocation.
        Set<Location> visitedLocations = setc(aut.getLocations().size());

        // Store which loops are found.
        Set<EventLoop> eventLoops = set();
        for (Location loc: aut.getLocations()) {
            if (visitedLocations.contains(loc)) {
                continue;
            }
            searchLoopFromLocation(loc, loopEvents, stackIndex, stack, eventLoops, visitedLocations);
        }
        return eventLoops;
    }

    /**
     * Searches all event loops reachable from a given location, and saves these in {@code EventLoops}. The events to
     * search for in the loops can be specified. Warning: only the loops that can be reached by the specified events are
     * returned.
     *
     * @param rootLoc The root location for searching.
     * @param loopEvents The events that can form a loop (e.g., the forcible events).
     * @param stackIndex Provides the index for a location on the stack.
     * @param stack The stack on which the path from the root location to the current location is saved.
     * @param eventLoops The event loops that have been found in the specified automaton, modified in place.
     * @param visitedLocations The locations that have been visited at least once when searching for loops.
     */
    private static void searchLoopFromLocation(Location rootLoc, Set<Event> loopEvents,
            Map<Location, Integer> stackIndex, List<Event> stack, Set<EventLoop> eventLoops,
            Set<Location> visitedLocations)
    {
        visitedLocations.add(rootLoc); // Add the root location to the set of visited locations.
        stackIndex.put(rootLoc, stack.size()); // Put the root location on top of the stack.
        // Consider successors of rootLoc.
        for (Edge edge: rootLoc.getEdges()) {
            // Only consider successor reachable via the loop events.
            if (isEmptyIntersection(loopEvents, getEvents(edge))) {
                continue;
            }

            Integer loopStartIndex;
            if (edge.getTarget() == null) { // selfloop.
                loopStartIndex = stackIndex.get(rootLoc);
            } else {
                loopStartIndex = stackIndex.get(edge.getTarget());
            }

            for (Event event: getEvents(edge)) {
                if (!loopEvents.contains(event)) {
                    continue;
                }

                if (loopStartIndex == null) { // A new location has been found.
                    stack.add(event);
                    searchLoopFromLocation(edge.getTarget(), loopEvents, stackIndex, stack, eventLoops,
                            visitedLocations);
                    stack.remove(stack.size() - 1);
                } else { // A previously visited location has been found.
                    stack.add(event);
                    eventLoops.add(retrieveLoopFromStack(loopStartIndex, stack));
                    stack.remove(stack.size() - 1);
                }
            }
        }
        stackIndex.remove(rootLoc); // Remove the root location from the stack, as we are finished.
    }

    /**
     * Retrieve the event loop between the specified fromIndex and the top of the supplied stack.
     *
     * @param fromIndex The start index of the loop in the stack.
     * @param stack The stack where the event loop is saved.
     * @return The event loop on top of the stack.
     */
    private static EventLoop retrieveLoopFromStack(Integer fromIndex, List<Event> stack) {
        Set<Event> events = setc(stack.size() - fromIndex);
        events.addAll(stack.subList(fromIndex, stack.size()));
        return new EventLoop(events);
    }
}
