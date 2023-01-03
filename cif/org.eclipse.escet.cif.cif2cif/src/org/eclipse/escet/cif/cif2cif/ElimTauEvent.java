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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;

/**
 * In-place transformation that eliminates the 'tau' event.
 *
 * <p>
 * This transformation introduces new events for the 'tau' event, one per automaton where a 'tau' event is used. The new
 * events are named 'tau_' (if that name is not already in use). If the alphabet is specified, it is extended as well.
 * The new events are neither controllable nor uncontrollable, and don't have a data type.
 * </p>
 */
public class ElimTauEvent extends CifWalker implements CifToCifTransformation {
    /** Mapping from automata to their new events. */
    private Map<Automaton, Event> newEventsMap = map();

    @Override
    public void transform(Specification spec) {
        walkSpecification(spec);
    }

    /**
     * Returns the unique new event for the given automaton.
     *
     * @param aut The automaton.
     * @return The unique new event for the given automaton.
     * @see #newEventsMap
     */
    private Event getEvent(Automaton aut) {
        Event rslt = newEventsMap.get(aut);
        if (rslt == null) {
            // Create new event. It is not controllable or uncontrollable.
            // Name is not yet set.
            rslt = newEvent();
            newEventsMap.put(aut, rslt);
        }
        return rslt;
    }

    @Override
    protected void preprocessEdge(Edge edge) {
        // If no events on the edge, it means a single 'tau' event.
        if (!edge.getEvents().isEmpty()) {
            return;
        }

        // Get automaton.
        Location loc = (Location)edge.eContainer();
        Automaton aut = (Automaton)loc.eContainer();

        // Get new event for this automaton.
        Event event = getEvent(aut);

        // Create new event reference expression.
        BoolType type = newBoolType();

        EventExpression refExpr = newEventExpression();
        refExpr.setEvent(event);
        refExpr.setType(type);

        // Add edge event.
        EdgeEvent edgeEvent = newEdgeEvent();
        edgeEvent.setEvent(refExpr);
        edge.getEvents().add(edgeEvent);
    }

    @Override
    protected void preprocessTauExpression(TauExpression tauExpr) {
        // Get automaton.
        EdgeEvent edgeEvent = (EdgeEvent)tauExpr.eContainer();
        Edge edge = (Edge)edgeEvent.eContainer();
        Location loc = (Location)edge.eContainer();
        Automaton aut = (Automaton)loc.eContainer();

        // Get new event for this automaton.
        Event event = getEvent(aut);

        // Create new event reference expression.
        BoolType type = newBoolType();

        EventExpression refExpr = newEventExpression();
        refExpr.setEvent(event);
        refExpr.setType(type);

        // Replace reference expression.
        EMFHelper.updateParentContainment(tauExpr, refExpr);
    }

    @Override
    protected void postprocessAutomaton(Automaton aut) {
        // If no 'tau' events on edges, then no new event, and we are done.
        if (!newEventsMap.containsKey(aut)) {
            return;
        }

        // Get the new event.
        Event tauEvent = getEvent(aut);

        // Set the name for the new event. Make sure it is unique.
        String name = "tau_";
        Set<String> names = CifScopeUtils.getSymbolNamesForScope(aut, null);
        if (names.contains(name)) {
            String oldName = name;
            name = CifScopeUtils.getUniqueName(name, names, Collections.emptySet());
            warn("Event \"%s\", introduced for the elimination of the \"tau\" event, is renamed to \"%s\".", oldName,
                    name);
        }
        tauEvent.setName(name);

        // Add the event to the declarations of the automaton.
        aut.getDeclarations().add(tauEvent);

        // Add the event to the alphabet, if it is specified.
        if (aut.getAlphabet() != null) {
            BoolType type = newBoolType();

            EventExpression refExpr = newEventExpression();
            refExpr.setEvent(tauEvent);
            refExpr.setType(type);

            aut.getAlphabet().getEvents().add(refExpr);
        }
    }
}
