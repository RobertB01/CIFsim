//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.common.CifEventUtils.filterAutomata;
import static org.eclipse.escet.cif.common.CifEventUtils.filterMonitorAuts;
import static org.eclipse.escet.cif.common.CifValueUtils.createConjunction;
import static org.eclipse.escet.cif.common.CifValueUtils.createDisjunction;
import static org.eclipse.escet.cif.common.CifValueUtils.makeInverse;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.set2list;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifEventUtils.Alphabets;
import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifSortUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.common.java.ListProductIterator;

/**
 * Linearization transformation that creates the Cartesian product of all edges for non-tau events, combining them in
 * all possible combinations. This results in self loops for all non-tau events, where the combination of all self loops
 * maintains all non-deterministic choices of the original specification. Worst case, the model size of the resulting
 * specification compared to the model size of the original specification could blow up exponentially.
 *
 * <p>
 * See the {@link LinearizeBase} class for further details.
 * </p>
 * .
 */
public class LinearizeProduct extends LinearizeBase {
    @Override
    protected void createEdges(List<Automaton> auts, Automaton mergedAut, Location mergedLoc) {
        // Get all events to process (the merged alphabet).
        List<Event> events = set2list(CifEventUtils.getAlphabet(mergedAut));

        // Linearize the edges.
        linearizeEdges(auts, alphabets, events, lpIntroducer, true, false, mergedLoc.getEdges());
    }

    /**
     * Linearizes edges. See {@link LinearizeProduct}.
     *
     * <p>
     * Event 'tau' is treated as a special case by the {@link #mergeTauEdges} method, since monitors don't affect 'tau'
     * events, and 'tau' doesn't synchronize. That is, this method ignores 'tau' events. This method handles all other
     * events, including channels.
     * </p>
     *
     * @param auts The original automata, sorted in ascending order based on their absolute names (without escaping).
     *     See also {@link CifSortUtils#sortCifObjects}.
     * @param alphabets Per automaton, all the alphabets.
     * @param events The events for which to create linearized edges. Usually the union of the synchronization, send,
     *     and receive alphabets of the given automata.
     * @param locPtrManager Location pointer manager.
     * @param removeMonitors Whether to remove all monitors from the given automata ({@code true}) or leave the monitors
     *     intact ({@code false}).
     * @param addLocPtrUpdates Whether to add location pointer updates.
     * @param linEdges The list to which to add the linearized edges. Is modified in-place.
     */
    public static void linearizeEdges(List<Automaton> auts, List<Alphabets> alphabets, List<Event> events,
            LocationPointerManager locPtrManager, boolean removeMonitors, boolean addLocPtrUpdates, List<Edge> linEdges)
    {
        // Get alphabets.
        List<Set<Event>> syncAlphabets = listc(auts.size());
        List<Set<Event>> sendAlphabets = listc(auts.size());
        List<Set<Event>> recvAlphabets = listc(auts.size());
        List<Set<Event>> moniAlphabets = listc(auts.size());
        for (Alphabets autAlphabets: alphabets) {
            syncAlphabets.add(autAlphabets.syncAlphabet);
            sendAlphabets.add(autAlphabets.sendAlphabet);
            recvAlphabets.add(autAlphabets.recvAlphabet);
            moniAlphabets.add(autAlphabets.moniAlphabet);
        }

        // Filter automata, per event.
        List<List<Automaton>> syncAuts;
        List<List<Automaton>> sendAuts;
        List<List<Automaton>> recvAuts;
        List<Set<Automaton>> moniAuts;
        syncAuts = filterAutomata(auts, syncAlphabets, events);
        sendAuts = filterAutomata(auts, sendAlphabets, events);
        recvAuts = filterAutomata(auts, recvAlphabets, events);
        moniAuts = filterMonitorAuts(auts, moniAlphabets, events);

        // Remove monitors from all automata.
        if (removeMonitors) {
            for (Automaton aut: auts) {
                aut.setMonitors(null);
            }
        }

        // Linearize and add edges, per event.
        for (int i = 0; i < events.size(); i++) {
            linearizeEdges(events.get(i), syncAuts.get(i), sendAuts.get(i), recvAuts.get(i), moniAuts.get(i),
                    locPtrManager, addLocPtrUpdates, linEdges);
        }
    }

    /**
     * Linearizes edges for the given event to all possible combinations, and adds them to the given merged location.
     *
     * @param event The event.
     * @param syncAuts The automata with synchronize over the event, sorted in ascending order based on their absolute
     *     names (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     * @param sendAuts The automata that send over the event, sorted in ascending order based on their absolute names
     *     (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     * @param recvAuts The automata that receive over the event, sorted in ascending order based on their absolute names
     *     (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     * @param moniAuts The automata that monitor the event, sorted in ascending order based on their absolute names
     *     (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     * @param locPtrManager Location pointer manager.
     * @param addLocPtrUpdates Whether to add location pointer updates.
     * @param linEdges The list to which to add the linearized edges. Is modified in-place.
     */
    private static void linearizeEdges(Event event, List<Automaton> syncAuts, List<Automaton> sendAuts,
            List<Automaton> recvAuts, Set<Automaton> moniAuts, LocationPointerManager locPtrManager,
            boolean addLocPtrUpdates, List<Edge> linEdges)
    {
        // Is the event a channel?
        boolean isChannel = event.getType() != null;
        boolean isVoid = isChannel && (event.getType() instanceof VoidType);

        // Get edges that can participate, per synchronizing automaton.
        List<List<EdgeEvent>> syncEdges = listc(syncAuts.size());
        for (Automaton aut: syncAuts) {
            // Initialization.
            boolean monitor = moniAuts.contains(aut);
            List<EdgeEvent> edges = list();

            // Get edges and monitored locations, for the event.
            List<Location> monitoredLocs = listc(aut.getLocations().size());
            for (Location loc: aut.getLocations()) {
                boolean trueGuard = false;
                for (Edge edge: loc.getEdges()) {
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        Event evt = CifEventUtils.getEventFromEdgeEvent(edgeEvent);
                        if (evt == event) {
                            // If 'true' guard, monitor has 'false' guard.
                            if (edge.getGuards().isEmpty()) {
                                trueGuard = true;
                            }

                            // Add synchronization edge.
                            edges.add(edgeEvent);
                        }
                    }
                }

                // Mark location as needing monitor edge, if event is
                // monitored, and there is no outgoing edge for the event with
                // a trivially 'true' guard.
                if (monitor && !trueGuard) {
                    monitoredLocs.add(loc);
                }
            }

            // Add monitor edge, if needed for at least one location.
            if (!monitoredLocs.isEmpty()) {
                edges.add(new MonitorEdgeEvent(monitoredLocs, event));
            }

            // Store edges for this automaton.
            syncEdges.add(edges);
        }

        // Get send edges that can participate.
        List<EdgeEvent> sendEdges = list();
        for (Automaton aut: sendAuts) {
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        Event evt = CifEventUtils.getEventFromEdgeEvent(edgeEvent);
                        if (evt == event) {
                            sendEdges.add(edgeEvent);
                        }
                    }
                }
            }
        }

        // Get receive edges that can participate.
        List<EdgeEvent> recvEdges = list();
        for (Automaton aut: recvAuts) {
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        Event evt = CifEventUtils.getEventFromEdgeEvent(edgeEvent);
                        if (evt == event) {
                            recvEdges.add(edgeEvent);
                        }
                    }
                }
            }
        }

        // Process all combinations.
        List<List<EdgeEvent>> possibilities = syncEdges;
        if (isChannel) {
            possibilities.add(sendEdges);
            possibilities.add(recvEdges);
        }

        ListProductIterator<EdgeEvent> iter;
        iter = new ListProductIterator<>(possibilities);
        while (iter.hasNext()) {
            List<EdgeEvent> edgeEvents = iter.next();

            // Create combined linearized guard.
            List<Expression> guards = listc(2 * edgeEvents.size());
            for (EdgeEvent edgeEvent: edgeEvents) {
                addGuards(edgeEvent, guards, locPtrManager);
            }
            Expression guard = createConjunction(guards);

            // Get combined updates.
            List<Update> updates = list();
            List<Update> rcvUpdates = list();
            for (int i = 0; i < edgeEvents.size(); i++) {
                EdgeEvent edgeEvent = edgeEvents.get(i);

                if (isChannel && !isVoid && i == edgeEvents.size() - 1) {
                    addUpdates(edgeEvent, rcvUpdates, addLocPtrUpdates ? locPtrManager : null);
                } else {
                    addUpdates(edgeEvent, updates, addLocPtrUpdates ? locPtrManager : null);
                }
            }

            // Handle non-void communication.
            if (isChannel && !isVoid) {
                // Get send value.
                int sendIdx = edgeEvents.size() - 2;
                EdgeSend sendEdgeEvt = (EdgeSend)edgeEvents.get(sendIdx);
                Expression sendValue = sendEdgeEvt.getValue();

                // Replace received values by the send value.
                rcvUpdates = replaceUpdates(rcvUpdates, sendValue);
                updates.addAll(rcvUpdates);
            }

            // Ignore target locations, as location pointer updates already
            // handle that.

            // Create new self loop edge.
            EventExpression eventRef = newEventExpression();
            eventRef.setEvent(event);
            eventRef.setType(newBoolType());

            EdgeEvent edgeEvent = newEdgeEvent();
            edgeEvent.setEvent(eventRef);

            Edge edge = newEdge();
            edge.getEvents().add(edgeEvent);
            edge.getGuards().add(guard);
            edge.getUpdates().addAll(updates);

            // Add new linearized edge.
            linEdges.add(edge);
        }
    }

    /**
     * Adds guards for the given edge event.
     *
     * @param edgeEvent The edge event for which to add guards.
     * @param guards The guards so far. Is extended in-place.
     * @param locPtrManager Location pointer manager.
     */
    private static void addGuards(EdgeEvent edgeEvent, List<Expression> guards, LocationPointerManager locPtrManager) {
        if (edgeEvent instanceof MonitorEdgeEvent) {
            // Get source locations and event.
            MonitorEdgeEvent monitorEdgeEvent = (MonitorEdgeEvent)edgeEvent;
            List<Location> srcLocs = monitorEdgeEvent.locs;
            Event event = monitorEdgeEvent.monitoredEvent;

            // Add guards, per location.
            List<Expression> locsGuards = listc(srcLocs.size());
            for (Location srcLoc: srcLocs) {
                // Initialization.
                List<Expression> locGuards = listc(4);

                // Add source location reference as guard.
                Automaton aut = CifLocationUtils.getAutomaton(srcLoc);
                if (aut.getLocations().size() > 1) {
                    Expression srcRef = locPtrManager.createLocRef(srcLoc);
                    locGuards.add(srcRef);
                }

                // Add monitor edge guards.
                for (Edge srcEdge: srcLoc.getEdges()) {
                    for (EdgeEvent srcEdgeEvent: srcEdge.getEvents()) {
                        Event evt = CifEventUtils.getEventFromEdgeEvent(srcEdgeEvent);
                        if (evt == event) {
                            List<Expression> edgeGuards = deepclone(srcEdge.getGuards());
                            Expression edgeGuard = createConjunction(edgeGuards);
                            locGuards.add(makeInverse(edgeGuard));
                        }
                    }
                }

                // Combine guards for the location.
                locsGuards.add(createConjunction(locGuards));
            }

            // Combine guards for the locations.
            guards.add(createDisjunction(locsGuards));
        } else {
            // Get source location.
            Edge edge = (Edge)edgeEvent.eContainer();
            Location src = CifEdgeUtils.getSource(edge);

            // Add source location reference as guard.
            Automaton aut = (Automaton)src.eContainer();
            if (aut.getLocations().size() > 1) {
                Expression srcRef = locPtrManager.createLocRef(src);
                guards.add(srcRef);
            }

            // Add edge guards.
            guards.addAll(deepclone(edge.getGuards()));
        }
    }

    /**
     * Adds updates for the given edge event.
     *
     * @param edgeEvent The edge event for which to add updates.
     * @param updates The updates so far. Is extended in-place.
     * @param locPtrManager The location pointer manager to use to add location pointer updates, or {@code null} to not
     *     add any location pointer updates.
     */
    private static void addUpdates(EdgeEvent edgeEvent, List<Update> updates, LocationPointerManager locPtrManager) {
        if (edgeEvent instanceof MonitorEdgeEvent) {
            // No updates.
        } else {
            // Add original edge updates.
            Edge edge = (Edge)edgeEvent.eContainer();
            updates.addAll(deepclone(edge.getUpdates()));

            // Add location pointer updates.
            if (locPtrManager != null) {
                Location srcLoc = CifEdgeUtils.getSource(edge);
                Location tgtLoc = CifEdgeUtils.getTarget(edge);
                if (srcLoc != tgtLoc) {
                    updates.add(locPtrManager.createLocUpdate(tgtLoc));
                }
            }
        }
    }

    /** Monitor edge event. */
    private static class MonitorEdgeEvent extends EdgeEventImpl {
        /** The source locations of the edge event. */
        public final List<Location> locs;

        /** The event of the edge event. */
        public final Event monitoredEvent;

        /**
         * Constructor for the {@link MonitorEdgeEvent} class.
         *
         * @param locs The source locations of the edge event.
         * @param monitoredEvent The event of the edge event.
         */
        public MonitorEdgeEvent(List<Location> locs, Event monitoredEvent) {
            this.locs = locs;
            this.monitoredEvent = monitoredEvent;
        }
    }
}
