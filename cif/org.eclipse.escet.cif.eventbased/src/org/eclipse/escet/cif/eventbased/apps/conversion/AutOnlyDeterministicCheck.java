//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.apps.conversion;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;

/**
 * Check that only allows deterministic automata. Note that:
 *
 * <ul>
 * <li>If an edge has guards that cannot be statically evaluated, this check ignores the edge. 'Tau' events are ignored
 * as well. It may thus not report all non-determinism. This is OK for the event-based toolset, since we have a separate
 * precondition check for the guards and for 'tau'.</li>
 * <li>Non-determinism is reported for unreachable locations.</li>
 * </ul>
 */
public class AutOnlyDeterministicCheck extends CifCheckNoCompDefInst {
    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        // Check each location separately.
        for (Location loc: aut.getLocations()) {
            // Initialize already seen events with their first encountered edge event of an edge with a 'true' guard.
            Map<Event, EdgeEvent> seenEvents = map();

            // Check the edges for non-determinism.
            for (Edge edge: loc.getEdges()) {
                // Consider only edges with 'true' guards.
                if (edge.getGuards().isEmpty()) {
                    // Implicitly 'true'.
                } else if (CifValueUtils.isTriviallyTrue(edge.getGuards(), false, true)) {
                    // Explicitly 'true'.
                } else if (CifValueUtils.isTriviallyFalse(edge.getGuards(), false, true)) {
                    // Edge can never be taken.
                    continue;
                } else {
                    // Guards are not statically evaluable.
                    continue;
                }

                // Check each edge event as being a separate edge.
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    // Check only non-'tau' edge events.
                    Expression eventRef = edgeEvent.getEvent();
                    if (eventRef instanceof TauExpression) {
                        continue;
                    }
                    Event event = ((EventExpression)eventRef).getEvent();

                    // Check for non-determinism.
                    EdgeEvent previousEdgeEvent = seenEvents.get(event);
                    if (previousEdgeEvent == null) {
                        // New event.
                        seenEvents.put(event, edgeEvent);
                    } else {
                        // Non-determinism.
                        violations.add(previousEdgeEvent, "An automaton is non-deterministic");
                        violations.add(edgeEvent, "An automaton is non-deterministic");
                    }
                }
            }
        }
    }
}
