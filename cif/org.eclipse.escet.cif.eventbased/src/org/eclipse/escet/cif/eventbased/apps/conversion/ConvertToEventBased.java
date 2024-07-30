//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.eventbased.automata.Edge.addEdge;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.eventbased.automata.AutomatonComparator;
import org.eclipse.escet.cif.eventbased.automata.AutomatonKind;
import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.eclipse.escet.cif.eventbased.automata.origin.Origin;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;

/** Converter from CIF specification to the representation used internally in the event-based synthesis tools. */
public class ConvertToEventBased {
    /** Converted events. */
    public Map<Event, org.eclipse.escet.cif.eventbased.automata.Event> events;

    /** Converted automata. */
    public List<org.eclipse.escet.cif.eventbased.automata.Automaton> automata;

    /** Original automata, ordered by their absolute name. */
    public Map<String, Automaton> origAutoms = map();

    /**
     * Convert a CIF specification for use by the event-based synthesis tooling. Eliminates component
     * definition/instantiation as a side-effect.
     *
     * @param spec CIF specification to check and convert.
     */
    public void convertSpecification(Specification spec) {
        events = map();
        automata = list();

        new ElimComponentDefInst().transform(spec);
        convertComponent(spec);
    }

    /** Sort the automata on their name. */
    public void sortAutomata() {
        Collections.sort(automata, AutomatonComparator.INSTANCE);
    }

    /**
     * Get the value of the boolean predicates.
     *
     * @param preds Predicates to evaluate.
     * @param defaultValue Value to assume when no predicates are given.
     * @param initial Whether the predicates apply only to the initial state.
     * @return Value of the predicates.
     */
    private static boolean getBooleanValue(List<Expression> preds, boolean defaultValue, boolean initial) {
        if (preds.isEmpty()) {
            return defaultValue;
        }
        if (CifValueUtils.isTriviallyTrue(preds, initial, true)) {
            return true;
        }
        if (CifValueUtils.isTriviallyFalse(preds, initial, true)) {
            return false;
        }
        throw new RuntimeException("Precondition violation.");
    }

    /**
     * Convert an event reference expression.
     *
     * @param expr Event reference expression to convert.
     * @return The converted event (referred to by the expression).
     */
    private org.eclipse.escet.cif.eventbased.automata.Event convertEvent(Expression expr) {
        // Get CIF event.
        Assert.check(expr instanceof EventExpression);
        Event evt = ((EventExpression)expr).getEvent();

        // If already converted, reuse result.
        org.eclipse.escet.cif.eventbased.automata.Event autEvt;
        autEvt = events.get(evt);
        if (autEvt != null) {
            return autEvt;
        }

        // Check controllability.
        EventControllability eventContr;
        if (evt.getControllable() == null) {
            eventContr = EventControllability.PLAIN_EVENT;
        } else {
            eventContr = evt.getControllable() ? EventControllability.CONTR_EVENT : EventControllability.UNCONTR_EVENT;
        }

        // Create event-based event.
        autEvt = new org.eclipse.escet.cif.eventbased.automata.Event(CifTextUtils.getAbsName(evt, false), eventContr);

        // Store and return converted event.
        events.put(evt, autEvt);
        return autEvt;
    }

    /**
     * Convert a CIF component for use by the event-based synthesis tools.
     *
     * @param comp Component to convert.
     */
    private void convertComponent(ComplexComponent comp) {
        // Automaton.
        if (comp instanceof Automaton) {
            convertAutomaton((Automaton)comp);
            return;
        }

        // Group.
        if (comp instanceof Group) {
            Group group = (Group)comp;
            for (Component cmp: group.getComponents()) {
                convertComponent((ComplexComponent)cmp);
            }
            return;
        }

        // Unknown.
        throw new RuntimeException("Unexpected component: " + comp);
    }

    /**
     * Convert a CIF automaton to an event-based automaton.
     *
     * @param aut Automaton to convert.
     */
    private void convertAutomaton(Automaton aut) {
        // Convert the alphabet.
        Set<org.eclipse.escet.cif.eventbased.automata.Event> alphabet;
        alphabet = set();
        if (aut.getAlphabet() == null) {
            // If the alphabet is not provided, construct it, by traversing
            // every edge event of every edge from every location in the
            // automaton.
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    // Edges without events (implicit 'tau' event) is disallowed by a precondition.
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        // Add event used to synchronize. No need to check for send/receive, a a precondition check
                        // disallows channels.
                        org.eclipse.escet.cif.eventbased.automata.Event event;
                        event = convertEvent(edgeEvent.getEvent());
                        alphabet.add(event);
                    }
                }
            }
        } else {
            for (Expression evt: aut.getAlphabet().getEvents()) {
                org.eclipse.escet.cif.eventbased.automata.Event event;
                event = convertEvent(evt);
                alphabet.add(event);
            }
        }

        // Get the monitor events.
        Set<org.eclipse.escet.cif.eventbased.automata.Event> monitors = null;
        if (aut.getMonitors() != null) {
            List<Expression> monEvents = aut.getMonitors().getEvents();
            if (monEvents.isEmpty()) {
                monitors = alphabet;
            } else {
                monitors = setc(monEvents.size());
                for (Expression exprevt: monEvents) {
                    org.eclipse.escet.cif.eventbased.automata.Event event;
                    event = convertEvent(exprevt);
                    monitors.add(event);
                }
            }
        }

        // Construct the automaton.
        org.eclipse.escet.cif.eventbased.automata.Automaton resAut;
        resAut = new org.eclipse.escet.cif.eventbased.automata.Automaton(alphabet);
        switch (aut.getKind()) {
            case NONE:
                resAut.kind = AutomatonKind.UNKNOWN;
                break;
            case PLANT:
                resAut.kind = AutomatonKind.PLANT;
                break;
            case REQUIREMENT:
                resAut.kind = AutomatonKind.REQUIREMENT;
                break;
            case SUPERVISOR:
                resAut.kind = AutomatonKind.SUPERVISOR;
                break;
        }
        resAut.name = CifTextUtils.getAbsName(aut);
        origAutoms.put(resAut.name, aut);

        // Keep track of events of outgoing edges at each location.
        Set<org.eclipse.escet.cif.eventbased.automata.Event> seen = set();

        // Convert locations and edges.
        Map<Location, org.eclipse.escet.cif.eventbased.automata.Location> locs;
        locs = map();
        for (Location loc: aut.getLocations()) {
            org.eclipse.escet.cif.eventbased.automata.Location srcLoc, dstLoc;
            // Get source location.
            srcLoc = convertLocation(loc, locs, resAut);
            if (monitors != null) {
                seen.clear();
            }

            // Walk over edges from the source location.
            for (Edge edge: loc.getEdges()) {
                // Get destination location.
                if (edge.getTarget() == null) {
                    dstLoc = srcLoc;
                } else {
                    dstLoc = convertLocation(edge.getTarget(), locs, resAut);
                }

                boolean guard = getBooleanValue(edge.getGuards(), true, false);
                for (EdgeEvent ee: edge.getEvents()) {
                    org.eclipse.escet.cif.eventbased.automata.Event evt;
                    evt = convertEvent(ee.getEvent());
                    if (guard) {
                        addEdge(evt, srcLoc, dstLoc);
                        if (monitors != null) {
                            seen.add(evt);
                        }
                    }
                }
            }
            if (monitors != null) {
                // All monitor events that have no edge should get a self-loop.
                for (org.eclipse.escet.cif.eventbased.automata.Event evt: monitors) {
                    if (!seen.contains(evt)) {
                        addEdge(evt, srcLoc, srcLoc);
                    }
                }
            }
        }

        automata.add(resAut);
    }

    /**
     * Convert a CIF location for use by the event-based synthesis tools.
     *
     * @param loc Location to check and convert.
     * @param locations Location map from CIF locations to event-based locations.
     * @param resAut Output automaton.
     * @return The converted location.
     */
    private org.eclipse.escet.cif.eventbased.automata.Location convertLocation(Location loc,
            Map<Location, org.eclipse.escet.cif.eventbased.automata.Location> locations,
            org.eclipse.escet.cif.eventbased.automata.Automaton resAut)
    {
        // If the location is already converted, return that result.
        org.eclipse.escet.cif.eventbased.automata.Location resLoc;
        resLoc = locations.get(loc);
        if (resLoc != null) {
            return resLoc;
        }

        // Skip edges here.

        // Construct new location, and add it to the map.
        Origin org = new CifOrigin(loc);
        resLoc = new org.eclipse.escet.cif.eventbased.automata.Location(resAut, org);
        resLoc.marked = getBooleanValue(loc.getMarkeds(), false, false);
        locations.put(loc, resLoc);

        // Set initial location.
        if (getBooleanValue(loc.getInitials(), false, true)) {
            Assert.areEqual(resAut.initial, null);
            resAut.setInitial(resLoc);
        }
        return resLoc;
    }
}
