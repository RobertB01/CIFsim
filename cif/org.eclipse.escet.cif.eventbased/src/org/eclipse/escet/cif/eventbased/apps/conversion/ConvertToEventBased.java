//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.eventbased.automata.AutomatonComparator;
import org.eclipse.escet.cif.eventbased.automata.AutomatonKind;
import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.eclipse.escet.cif.eventbased.automata.origin.Origin;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Assert;

/**
 * Converter from CIF specification to the input data of the event-based synthesis tools. Performs checking along with
 * the conversion, and throws an {@link UnsupportedException} when a non-supported feature is found.
 */
public class ConvertToEventBased {
    /** Converted events. */
    public Map<Event, org.eclipse.escet.cif.eventbased.automata.Event> events;

    /** Converted automata. */
    public List<org.eclipse.escet.cif.eventbased.automata.Automaton> automata;

    /**
     * If set, the conversion accepts events without controllability, value {@link EventControllability#PLAIN_EVENT} may
     * be used in the converted result.
     */
    public boolean allowPlainEvents;

    /** Original automata, ordered by their absolute name. */
    public Map<String, Automaton> origAutoms = map();

    /**
     * Check and convert a CIF specification for use by the event-based synthesis tooling. Eliminates component
     * definition/instantiation as a side-effect.
     *
     * @param spec CIF specification to check and convert.
     * @param allowPlainEvents If set, allow events without controllability property.
     * @throws UnsupportedException When the specification is not supported.
     */
    public void convertSpecification(Specification spec, boolean allowPlainEvents) {
        this.allowPlainEvents = allowPlainEvents;
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
     * @param loc Location being converted (used for error reporting).
     * @param kind The kind of predicates (used for error reporting). This is not related to supervisory/invariant
     *     kinds.
     * @return Value of the predicates.
     */
    private static boolean getBooleanValue(List<Expression> preds, boolean defaultValue, Location loc, String kind) {
        if (preds.isEmpty()) {
            return defaultValue;
        }
        if (preds.size() > 1) {
            String msg = fmt("Unsupported %s: multiple %s predicates are currently not supported.",
                    CifTextUtils.getLocationText1(loc), kind);
            throw new UnsupportedException(msg);
        }
        Expression pred = first(preds);
        if (!(pred instanceof BoolExpression)) {
            String aPostfix = kind.startsWith("i") ? "n" : "";
            String msg = fmt("Unsupported %s: a%s %s predicate is not trivially true or false.",
                    CifTextUtils.getLocationText1(loc), aPostfix, kind);
            throw new UnsupportedException(msg);
        }
        return ((BoolExpression)pred).isValue();
    }

    /**
     * Convert an event reference expression.
     *
     * @param expr Event reference expression to convert.
     * @param loc Location containing the expression. May be {@code null}, but only if the event reference expression is
     *     not a 'tau' reference.
     * @return The converted event (referred to by the expression).
     * @throws UnsupportedException When the event is not supported.
     */
    private org.eclipse.escet.cif.eventbased.automata.Event convertEvent(Expression expr, Location loc) {
        // Check for 'tau'.
        if (expr instanceof TauExpression) {
            String msg = fmt("Unsupported %s: edges with event \"tau\" are not supported.",
                    CifTextUtils.getLocationText1(loc));
            throw new UnsupportedException(msg);
        }

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
            if (!allowPlainEvents) {
                String msg = fmt("Unsupported event \"%s\": event is not controllable or uncontrollable.",
                        CifTextUtils.getAbsName(evt));
                throw new UnsupportedException(msg);
            }
            eventContr = EventControllability.PLAIN_EVENT;
        } else {
            eventContr = evt.getControllable() ? EventControllability.CONTR_EVENT : EventControllability.UNCONTR_EVENT;
        }

        // Create event-based event
        autEvt = new org.eclipse.escet.cif.eventbased.automata.Event(CifTextUtils.getAbsName(evt, false), eventContr);

        // Check type.
        if (evt.getType() != null) {
            String msg = fmt("Unsupported event \"%s\": event has a data type.", CifTextUtils.getAbsName(evt));
            throw new UnsupportedException(msg);
        }

        // Store and return converted event.
        events.put(evt, autEvt);
        return autEvt;
    }

    /**
     * Check and convert a CIF component for use by the event-based synthesis tools.
     *
     * @param comp Component to check and convert.
     * @throws UnsupportedException When the component is not supported.
     */
    private void convertComponent(ComplexComponent comp) {
        // Component.
        if (!comp.getInvariants().isEmpty()) {
            String msg = fmt("Unsupported %s: invariants in components are currently not supported.",
                    CifTextUtils.getComponentText1(comp));
            throw new UnsupportedException(msg);
        }
        if (!comp.getMarkeds().isEmpty()) {
            String msg = fmt("Unsupported %s: marker predicates in components are currently not supported.",
                    CifTextUtils.getComponentText1(comp));
            throw new UnsupportedException(msg);
        }
        if (!comp.getInitials().isEmpty()) {
            String msg = fmt("Unsupported %s: initialization predicates in components are currently not supported.",
                    CifTextUtils.getComponentText1(comp));
            throw new UnsupportedException(msg);
        }

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
     * Check and convert a CIF automaton to an event-based automaton.
     *
     * @param aut Automaton to check and convert.
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
                    // Edges without events (implicit 'tau' event) is already checked elsewhere.
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        // Add event used to synchronize. No need to check for
                        // send/receive, as the conversion of the event already
                        // disallows channels.
                        org.eclipse.escet.cif.eventbased.automata.Event event;
                        event = convertEvent(edgeEvent.getEvent(), loc);
                        alphabet.add(event);
                    }
                }
            }
        } else {
            for (Expression evt: aut.getAlphabet().getEvents()) {
                org.eclipse.escet.cif.eventbased.automata.Event event;
                event = convertEvent(evt, null);
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
                    event = convertEvent(exprevt, null);
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
            srcLoc = convertLocation(loc, aut, locs, resAut);
            if (monitors != null) {
                seen.clear();
            }

            // Walk over edges from the source location.
            for (Edge edge: loc.getEdges()) {
                // Get destination location.
                if (edge.getTarget() == null) {
                    dstLoc = srcLoc;
                } else {
                    dstLoc = convertLocation(edge.getTarget(), aut, locs, resAut);
                }

                if (edge.getEvents().isEmpty()) {
                    String msg = fmt("Unsupported %s: edges with event \"tau\" are not supported.",
                            CifTextUtils.getLocationText1(loc));
                    throw new UnsupportedException(msg);
                }

                boolean guard = checkEdge(edge, loc);
                for (EdgeEvent ee: edge.getEvents()) {
                    org.eclipse.escet.cif.eventbased.automata.Event evt;
                    evt = convertEvent(ee.getEvent(), loc);
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
     * Check and convert a CIF location for use by the event-based synthesis tools.
     *
     * @param loc Location to check and convert.
     * @param aut Automaton being converted.
     * @param locations Location map from CIF locations to event-based locations.
     * @param resAut Output automaton.
     * @return The converted location.
     * @throws UnsupportedException When the location is not supported.
     */
    private org.eclipse.escet.cif.eventbased.automata.Location convertLocation(Location loc, Automaton aut,
            Map<Location, org.eclipse.escet.cif.eventbased.automata.Location> locations,
            org.eclipse.escet.cif.eventbased.automata.Automaton resAut)
    {
        // If the location is already converted, return that result.
        org.eclipse.escet.cif.eventbased.automata.Location resLoc;
        resLoc = locations.get(loc);
        if (resLoc != null) {
            return resLoc;
        }

        // New location. Check invariants.
        List<Invariant> invs = loc.getInvariants();
        if (!invs.isEmpty()) {
            List<Expression> preds = listc(invs.size());
            for (Invariant inv: loc.getInvariants()) {
                if (inv.getInvKind() != InvKind.STATE) {
                    String msg = fmt("Unsupported %s: state/event exclusion invariants are not supported.",
                            CifTextUtils.getLocationText1(loc));
                    throw new UnsupportedException(msg);
                }
                preds.add(inv.getPredicate());
            }
            if (!getBooleanValue(preds, true, loc, "invariant")) {
                String msg = fmt("Unsupported %s: the state invariants are not trivially true.",
                        CifTextUtils.getLocationText1(loc));
                throw new UnsupportedException(msg);
            }
        }

        // Check urgency.
        if (loc.isUrgent()) {
            String msg = fmt("Unsupported %s: urgent locations are not supported.", CifTextUtils.getLocationText1(loc));
            throw new UnsupportedException(msg);
        }

        // Get marked state.
        boolean marked = getBooleanValue(loc.getMarkeds(), false, loc, "marker");

        // Skip edges here.

        // Construct new location, and add it to the map.
        Origin org = new CifOrigin(loc);
        resLoc = new org.eclipse.escet.cif.eventbased.automata.Location(resAut, org);
        resLoc.marked = marked;
        locations.put(loc, resLoc);

        // Check and set initial location.
        if (getBooleanValue(loc.getInitials(), false, loc, "initialization")) {
            if (resAut.initial != null) {
                String msg = fmt("Unsupported automaton \"%s\": the automaton has multiple initial locations.",
                        CifTextUtils.getAbsName(aut));
                throw new UnsupportedException(msg);
            }
            resAut.setInitial(resLoc);
        }
        return resLoc;
    }

    /**
     * Check whether a CIF edge is supported for the event-based synthesis tools, and get the value of the guard if the
     * edge is found to be supported.
     *
     * @param edge Edge to check.
     * @param loc Source location of the edge.
     * @return Value of the guard.
     * @throws UnsupportedException When the edge is not supported.
     */
    private boolean checkEdge(Edge edge, Location loc) {
        // No updates allowed.
        if (!edge.getUpdates().isEmpty()) {
            String msg = fmt("Unsupported %s: edges with updates are currently not supported.",
                    CifTextUtils.getLocationText1(loc));
            throw new UnsupportedException(msg);
        }

        // No urgency allowed.
        if (edge.isUrgent()) {
            String msg = fmt("Unsupported %s: urgent edges are currently not supported.",
                    CifTextUtils.getLocationText1(loc));
            throw new UnsupportedException(msg);
        }

        // Check the guard and return its value.
        return getBooleanValue(edge.getGuards(), true, loc, "guard");
    }
}
