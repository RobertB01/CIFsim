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

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.controllercheck.finiteresponse.DfsFindSimpleCycles.GenericDfsSimpleCyclesFinder;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
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
        EventLoopFinder finder = new EventLoopFinder();
        return finder.searchEventLoops(aut, loopEvents, env);
    }

    /** Cycle finder for finding event loops in an automaton. */
    public static class EventLoopFinder extends GenericDfsSimpleCyclesFinder<Automaton> {
        @Override
        public List<Location> getVertices(Automaton graph) {
            return graph.getLocations();
        }
    }
}
