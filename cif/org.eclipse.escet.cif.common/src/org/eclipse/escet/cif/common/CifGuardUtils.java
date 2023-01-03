//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifEventUtils.filterAutomata;
import static org.eclipse.escet.cif.common.CifEventUtils.filterMonitorAuts;
import static org.eclipse.escet.cif.common.CifValueUtils.createConjunction;
import static org.eclipse.escet.cif.common.CifValueUtils.createDisjunction;
import static org.eclipse.escet.cif.common.CifValueUtils.makeTrue;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationExpression;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl;
import org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeReceiveImpl;
import org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeSendImpl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;

/** CIF guard utility methods. */
public class CifGuardUtils {
    /** Constructor for the {@link CifGuardUtils} class. */
    private CifGuardUtils() {
        // Static class.
    }

    /**
     * Merges guards of the given automata for the edges for the given events, to create a combined guard indicating
     * when the events are enabled in the given automata, and thus the entire specification (if there are no other
     * automata in the specification).
     *
     * @param automata The automata.
     * @param syncAlphabets Per automaton, the synchronization alphabet.
     * @param sendAlphabets Per automaton, the send alphabet.
     * @param recvAlphabets Per automaton, the receive alphabet.
     * @param moniAlphabets Per automaton, the monitor alphabet.
     * @param events The events.
     * @param creator The location reference expression creator to use to create location reference expressions.
     * @return Per event, the merged guard.
     */
    public static List<Expression> mergeGuards(List<Automaton> automata, List<Set<Event>> syncAlphabets,
            List<Set<Event>> sendAlphabets, List<Set<Event>> recvAlphabets, List<Set<Event>> moniAlphabets,
            List<Event> events, LocRefExprCreator creator)
    {
        // Merge guards, per event.
        List<Expression> guards = listc(events.size());
        for (Event event: events) {
            // Filter automata for the event, based on alphabets.
            List<Automaton> syncAuts;
            List<Automaton> sendAuts;
            List<Automaton> recvAuts;
            Set<Automaton> moniAuts;
            syncAuts = filterAutomata(automata, syncAlphabets, event);
            sendAuts = filterAutomata(automata, sendAlphabets, event);
            recvAuts = filterAutomata(automata, recvAlphabets, event);
            moniAuts = filterMonitorAuts(automata, moniAlphabets, event);

            // Get merged guard.
            Expression guard = mergeGuards(syncAuts, sendAuts, recvAuts, moniAuts, event, creator);
            guards.add(guard);
        }

        // Return merged guard, per event.
        return guards;
    }

    /**
     * Merges guards of the given automata for the edges with the given event, to create a combined guard indicating
     * when the event is enabled in the given automata, and thus the entire specification (if there are no other
     * automata in the specification).
     *
     * @param syncAuts The original automata that synchronize over the given event.
     * @param sendAuts The original automata that send over the given event.
     * @param recvAuts The original automata that receive over the given event.
     * @param moniAuts The original automata that monitor the given event.
     * @param event The event for which to create a combined guard.
     * @param creator The location reference expression creator to use to create location reference expressions.
     * @return The combined guard indicating when the event is enabled in the given automata.
     */
    public static Expression mergeGuards(List<Automaton> syncAuts, List<Automaton> sendAuts, List<Automaton> recvAuts,
            Set<Automaton> moniAuts, Event event, LocRefExprCreator creator)
    {
        // Initialize synchronization guards, per automaton.
        int count = syncAuts.size();
        if (event.getType() != null) {
            count += 2;
        }
        List<Expression> syncGuards = listc(count);

        // For channels, take send/receive into account.
        if (event.getType() != null) {
            // Merge guards from send edges with the given event.
            List<Expression> sendGuards = listc(sendAuts.size());
            for (Automaton aut: sendAuts) {
                sendGuards.add(mergeGuards(aut, event, EdgeSendImpl.class, creator));
            }

            // Merge guards from receive edges with the given event.
            List<Expression> recvGuards = listc(recvAuts.size());
            for (Automaton aut: recvAuts) {
                recvGuards.add(mergeGuards(aut, event, EdgeReceiveImpl.class, creator));
            }

            // One sender must be enabled, and one receiver must be enabled.
            // Let them 'synchronize' with the synchronizing automata.
            syncGuards.add(createDisjunction(sendGuards));
            syncGuards.add(createDisjunction(recvGuards));
        }

        // Add combined guard for each of the original synchronizing automata.
        for (Automaton aut: syncAuts) {
            if (moniAuts.contains(aut)) {
                // The automaton is a monitor for the event. The guard is
                // 'true' by definition.
                syncGuards.add(makeTrue());
            } else {
                // Merge guards from edges with the given event.
                syncGuards.add(mergeGuards(aut, event, EdgeEventImpl.class, creator));
            }
        }

        // Return combined guard for all automata. All events synchronize, so
        // use a conjunction to combine the guards from the different automata.
        // The combined send guard and the combined receive guard, are also
        // taken as synchronization partners.
        return createConjunction(syncGuards);
    }

    /**
     * Merges guards of the given automaton for the edges with the given event, to create a combined guard indicating
     * when the event is enabled in the given automaton.
     *
     * <p>
     * Combines guards of edges for the given event using conjunctions. Combines guards of multiple outgoing edges of a
     * single location using disjunctions, resulting in the combined guard of the location. Makes the combined guards of
     * a location conditional on being in the location, by combining it with a location predicate using conjunctions.
     * Combines guards of multiple locations using disjunctions.
     * </p>
     *
     * <p>
     * Employs deep cloning on the low level guard predicates, to ensure that the guards remain contained in the edges,
     * for processing of other events.
     * </p>
     *
     * @param aut The automaton. Must not monitor the given event.
     * @param event The event.
     * @param edgeClass The {@link EdgeEvent} class to use to check edge events for inclusion. Can be used to for
     *     instance only take synchronization edge events or only send edge events into account.
     * @param creator The location reference expression creator to use to create location reference expressions.
     * @return The combined guard indicating when the event is enabled in the given automaton.
     */
    public static Expression mergeGuards(Automaton aut, Event event, Class<? extends EdgeEvent> edgeClass,
            LocRefExprCreator creator)
    {
        // Get combined guards for the locations.
        List<Expression> locsGuards = list();
        for (Location loc: aut.getLocations()) {
            // Get combined guards for the outgoing edges.
            List<Expression> edgesGuards = list();
            for (Edge edge: loc.getEdges()) {
                // If not a synchronization edge for this event, skip it.
                boolean match = false;
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    // Check edge event class.
                    if (edgeEvent.getClass() != edgeClass) {
                        continue;
                    }

                    // Event must match.
                    Event e = CifEventUtils.getEventFromEdgeEvent(edgeEvent);
                    if (e == event) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    continue;
                }

                // Combine guards for single edge.
                Expression edgeGuard = createConjunction(deepclone(edge.getGuards()));
                edgesGuards.add(edgeGuard);
            }

            // Combine guards from the edges.
            Expression edgesGuard = createDisjunction(edgesGuards);

            // Add final result for the location, where location guard depends
            // on the location.
            Expression locRef = creator.create(loc);
            locsGuards.add(createConjunction(list(locRef, edgesGuard)));
        }

        // Combine guards from the locations.
        return createDisjunction(locsGuards);
    }

    /** Location reference expression creator. */
    public static interface LocRefExprCreator {
        /** The default location reference expression creator. Creates {@link LocationExpression} instances. */
        public static final LocRefExprCreator DEFAULT = new CifLocRefExprCreator();

        /**
         * Creates an expression that serves as a location reference expression for the given location.
         *
         * @param loc The location.
         * @return The created expression.
         */
        public Expression create(Location loc);
    }

    /** Location reference expression creator that creates {@link LocationExpression} instances. */
    private static class CifLocRefExprCreator implements LocRefExprCreator {
        @Override
        public Expression create(Location loc) {
            // Special case for nameless locations. Can't create a reference
            // to them, as we can only reference named objects. However,
            // nameless locations are only locations in their automata. So,
            // use value 'true' for them.
            if (loc.getName() == null) {
                return CifValueUtils.makeTrue();
            }

            // Named location. Create location reference expression.
            LocationExpression locRef = newLocationExpression();
            locRef.setLocation(loc);
            locRef.setType(newBoolType());
            return locRef;
        }
    }
}
