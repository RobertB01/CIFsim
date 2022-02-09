//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifEventUtils.getMonitors;
import static org.eclipse.escet.cif.common.CifValueUtils.createConjunction;
import static org.eclipse.escet.cif.common.CifValueUtils.makeInverse;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;

/**
 * In-place transformation that eliminates monitor events.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported. This is to make
 * sure we don't suffer from the false negatives of the {@link CifEventUtils#areSameEventRefs} method.
 * </p>
 *
 * <p>
 * For each monitor event, additional self loops are added as needed, to make sure the automaton doesn't block the
 * event. Furthermore, all monitor sets are removed.
 * </p>
 *
 * <p>
 * All edges created by this transformation have no guards, no communication, no urgency, and no updates.
 * </p>
 *
 * <p>
 * This transformation generates non-optimized predicates. Apply the {@link SimplifyValues} transformation after this
 * transformation, to obtain the best results.
 * </p>
 */
public class ElimMonitors extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating monitors from a CIF specification with component definitions is currently not "
                    + "supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Perform actual transformation.
        walkSpecification(spec);
    }

    /**
     * Performs the in-place transformation.
     *
     * @param aut The CIF automaton for which to perform the transformation. The automaton is modified in-place.
     */
    public void transform(Automaton aut) {
        // Check no component definition/instantiation precondition.
        Specification spec = CifScopeUtils.getSpecRoot(aut);
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating monitors from a CIF specification with component definitions is currently not "
                    + "supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Perform actual transformation.
        walkAutomaton(aut);
    }

    @Override
    protected void walkAutomaton(Automaton aut) {
        // Skip automata that do not monitor anything.
        Monitors monitors = aut.getMonitors();
        if (monitors == null) {
            return;
        }

        // Get monitor events.
        Set<Event> alphabet = null;
        Set<Event> monitorEvents = getMonitors(aut, alphabet);

        // Process each location.
        for (Location loc: aut.getLocations()) {
            for (Event monitorEvent: monitorEvents) {
                // Find edges for this monitor event.
                List<Edge> edges = list();
                for (Edge edge: loc.getEdges()) {
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        // Skip communication uses.
                        if (edgeEvent.getClass() != EdgeEventImpl.class) {
                            continue;
                        }

                        // Skip tau.
                        Expression eventRef = edgeEvent.getEvent();
                        if (eventRef instanceof TauExpression) {
                            continue;
                        }

                        // Skip other events.
                        Event event = ((EventExpression)eventRef).getEvent();
                        if (event != monitorEvent) {
                            continue;
                        }

                        // Add synchronization use.
                        edges.add(edge);
                        break;
                    }
                }

                // Create guard expressions for new edge.
                List<Expression> guards = edgesToGuards(edges);
                if (guards == null) {
                    continue;
                }

                // Add new edge.
                EventExpression eventRef = newEventExpression();
                eventRef.setType(newBoolType());
                eventRef.setEvent(monitorEvent);

                EdgeEvent edgeEvent = newEdgeEvent();
                edgeEvent.setEvent(eventRef);

                Edge edge = newEdge();
                edge.getEvents().add(edgeEvent);
                edge.getGuards().addAll(guards);

                loc.getEdges().add(edge);
            }
        }

        // Remove monitor set from automaton.
        aut.setMonitors(null);
    }

    /**
     * Creates guard expressions from the edges, where the new guards are the inverse of the guards of the original
     * edges.
     *
     * @param edges The edges.
     * @return The new guard expressions, or {@code null} to indicate that the new guard is {@code false}, and thus we
     *     don't need a new edge.
     */
    private List<Expression> edgesToGuards(List<Edge> edges) {
        // If no edges, then event is disabled. Enable it with a single edge
        // without guards.
        if (edges.isEmpty()) {
            return list();
        }

        // Create inverses of the conjunctions of the guards of the edges.
        List<Expression> rslt = list();
        for (Edge edge: edges) {
            // If edge has true guard, it is already always enabled, and the
            // inverse is thus false.
            if (edge.getGuards().isEmpty()) {
                return null;
            }

            // Add inverse of the edge's guards.
            Expression guard = createConjunction(deepclone(edge.getGuards()));
            guard = makeInverse(guard);
            rslt.add(guard);
        }
        return rslt;
    }
}
