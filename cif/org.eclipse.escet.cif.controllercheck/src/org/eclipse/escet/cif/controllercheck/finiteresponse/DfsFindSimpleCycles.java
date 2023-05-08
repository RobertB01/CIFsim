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
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.app.framework.AppEnvData;

/** XXX */
public class DfsFindSimpleCycles {
    /** Constructor of the static {@link DfsFindSimpleCycles} class. */
    private DfsFindSimpleCycles() {
        // Static class.
    }

    /**
     * XXX
     */
    public static Set<EventLoop> searchEventLoops(Automaton aut, Set<Event> loopEvents, AppEnvData env) {
        List<Event> stack = listc(aut.getLocations().size() + 1);

        Map<Location, Integer> stackIndex = mapc(aut.getLocations().size());

        Set<Location> visitedLocations = setc(aut.getLocations().size());

        Set<EventLoop> eventLoops = set();
        for (Location loc: aut.getLocations()) {
            if (visitedLocations.contains(loc)) {
                continue;
            }
            searchEventLoops(loc, loopEvents, stackIndex, stack, eventLoops, visitedLocations, env);

            if (env.isTerminationRequested()) {
                return null;
            }
        }
        return eventLoops;
    }

    /**
     * XXX
     */
    private static void searchEventLoops(Location rootLoc, Set<Event> loopEvents, Map<Location, Integer> stackIndex,
            List<Event> stack, Set<EventLoop> eventLoops, Set<Location> visitedLocations, AppEnvData env)
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
                    searchEventLoops(edgeTargetLoc, loopEvents, stackIndex, stack, eventLoops, visitedLocations, env);
                    stack.remove(stack.size() - 1);
                } else {
                    stack.add(event);
                    eventLoops.add(retrieveLoopFromStack(loopStartIndex, stack));
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
     * XXX
     */
    private static EventLoop retrieveLoopFromStack(Integer fromIndex, List<Event> stack) {
        List<Event> events = listc(stack.size() - fromIndex);
        events.addAll(stack.subList(fromIndex, stack.size()));
        return new EventLoop(events);
    }
}
