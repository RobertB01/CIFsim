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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifScopeUtils.isParamRefExpr;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTauExpression;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.automata.impl.EdgeEventImpl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.common.java.Strings;

/** CIF event utility methods. */
public class CifEventUtils {
    /** Constructor for the {@link CifEventUtils} class. */
    private CifEventUtils() {
        // Static class.
    }

    /**
     * Returns all the alphabets for the given automata.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automata The automata.
     * @param syncAlphabets Per automaton, the synchronization alphabet. May be {@code null} if not yet available.
     * @return Per automaton, all the alphabets.
     */
    public static List<Alphabets> getAllAlphabets(List<Automaton> automata, List<Set<Event>> syncAlphabets) {
        List<Alphabets> alphabets = listc(automata.size());
        for (int i = 0; i < automata.size(); i++) {
            Automaton aut = automata.get(i);
            Set<Event> syncAlphabet = (syncAlphabets == null) ? null : syncAlphabets.get(i);
            alphabets.add(getAllAlphabets(aut, syncAlphabet));
        }
        return alphabets;
    }

    /**
     * Returns all the alphabets for the given automaton.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automaton The automaton.
     * @param syncAlphabet The synchronization alphabet of the automaton. May be {@code null} if not yet available.
     * @return All the alphabets of the automaton.
     */
    public static Alphabets getAllAlphabets(Automaton automaton, Set<Event> syncAlphabet) {
        Alphabets alphabets = new Alphabets();
        if (syncAlphabet == null) {
            syncAlphabet = getAlphabet(automaton);
        }
        alphabets.syncAlphabet = syncAlphabet;
        alphabets.sendAlphabet = getSendAlphabet(automaton);
        alphabets.recvAlphabet = getReceiveAlphabet(automaton);
        alphabets.moniAlphabet = getMonitors(automaton, syncAlphabet);
        return alphabets;
    }

    /** All alphabets of a single automaton. */
    public static class Alphabets {
        /** The synchronization alphabet of the automaton. */
        public Set<Event> syncAlphabet;

        /** The send alphabet of the automaton. */
        public Set<Event> sendAlphabet;

        /** The receive alphabet of the automaton. */
        public Set<Event> recvAlphabet;

        /** The monitor alphabet of the automaton. Is a subset of the {@link #syncAlphabet}. */
        public Set<Event> moniAlphabet;
    }

    /**
     * Returns the alphabet for the given automaton. If the alphabet is not specified for the automaton, the alphabet is
     * derived from the non-communication uses of events on the edges of the automaton.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automaton The automaton for which to return the alphabet.
     * @return The alphabet of the automaton.
     */
    public static EventRefSet getAlphabetRefSet(Automaton automaton) {
        EventRefSet alphabet = new EventRefSet();
        if (automaton.getAlphabet() != null) {
            // There can not be a 'tau' event in the alphabet.
            for (Expression eventRef: automaton.getAlphabet().getEvents()) {
                alphabet.add(eventRef);
            }
        } else {
            // Add the events occurring on the edges of the automaton,
            // excluding any 'tau' events and communication uses.
            for (Location loc: automaton.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        // Skip send/receive uses.
                        if (edgeEvent.getClass() != EdgeEventImpl.class) {
                            continue;
                        }

                        // Skip tau.
                        Expression eventRef = edgeEvent.getEvent();
                        if (eventRef instanceof TauExpression) {
                            continue;
                        }

                        // Add event used as synchronization.
                        alphabet.add(eventRef);
                    }
                }
            }
        }
        return alphabet;
    }

    /**
     * Returns the alphabets for the given automata. If the alphabet is not specified for an automaton, the alphabet is
     * derived from the non-communication uses of events on the edges of the automaton.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automata The automata for which to return the alphabets.
     * @return The alphabets of the automata.
     */
    public static List<Set<Event>> getAlphabets(List<Automaton> automata) {
        List<Set<Event>> alphabets = listc(automata.size());
        for (Automaton aut: automata) {
            alphabets.add(getAlphabet(aut));
        }
        return alphabets;
    }

    /**
     * Returns the alphabet for the given automaton. If the alphabet is not specified for the automaton, the alphabet is
     * derived from the non-communication uses of events on the edges of the automaton.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automaton The automaton for which to return the alphabet.
     * @return The alphabet of the automaton.
     */
    public static Set<Event> getAlphabet(Automaton automaton) {
        Set<Event> alphabet;
        if (automaton.getAlphabet() != null) {
            // There can not be a 'tau' event in the alphabet.
            List<Expression> eventRefs = automaton.getAlphabet().getEvents();
            alphabet = setc(eventRefs.size());
            for (Expression eventRef: eventRefs) {
                Event event = ((EventExpression)eventRef).getEvent();
                alphabet.add(event);
            }
        } else {
            // Add the events occurring on the edges of the automaton,
            // excluding any 'tau' events and communication uses.
            alphabet = set();
            for (Location loc: automaton.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        // Skip send/receive uses.
                        if (edgeEvent.getClass() != EdgeEventImpl.class) {
                            continue;
                        }

                        // Skip tau.
                        Expression eventRef = edgeEvent.getEvent();
                        if (eventRef instanceof TauExpression) {
                            continue;
                        }

                        // Add event used as synchronization.
                        Event event = ((EventExpression)eventRef).getEvent();
                        alphabet.add(event);
                    }
                }
            }
        }
        return alphabet;
    }

    /**
     * Returns the send alphabets for the given automata, derived from the send uses of events on the edges of the
     * automata.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automata The automata for which to return the send alphabets.
     * @return The send alphabets of the automata.
     */
    public static List<Set<Event>> getSendAlphabets(List<Automaton> automata) {
        List<Set<Event>> alphabets = listc(automata.size());
        for (Automaton aut: automata) {
            alphabets.add(getSendAlphabet(aut));
        }
        return alphabets;
    }

    /**
     * Returns the send alphabet for the given automaton, derived from the send uses of events on the edges of the
     * automaton.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automaton The automaton for which to return the send alphabet.
     * @return The send alphabet of the automaton.
     */
    public static Set<Event> getSendAlphabet(Automaton automaton) {
        Set<Event> alphabet = set();
        for (Location loc: automaton.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    if (!(edgeEvent instanceof EdgeSend)) {
                        continue;
                    }
                    Expression eventRef = edgeEvent.getEvent();
                    Event event = ((EventExpression)eventRef).getEvent();
                    alphabet.add(event);
                }
            }
        }
        return alphabet;
    }

    /**
     * Returns the receive alphabets for the given automata, derived from the receive uses of events on the edges of the
     * automata.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automata The automata for which to return the receive alphabets.
     * @return The receive alphabets of the automata.
     */
    public static List<Set<Event>> getReceiveAlphabets(List<Automaton> automata) {
        List<Set<Event>> alphabets = listc(automata.size());
        for (Automaton aut: automata) {
            alphabets.add(getReceiveAlphabet(aut));
        }
        return alphabets;
    }

    /**
     * Returns the receive alphabet for the given automaton, derived from the receive uses of events on the edges of the
     * automaton.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automaton The automaton for which to return the receive alphabet.
     * @return The receive alphabet of the automaton.
     */
    public static Set<Event> getReceiveAlphabet(Automaton automaton) {
        Set<Event> alphabet = set();
        for (Location loc: automaton.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    if (!(edgeEvent instanceof EdgeReceive)) {
                        continue;
                    }
                    Expression eventRef = edgeEvent.getEvent();
                    Event event = ((EventExpression)eventRef).getEvent();
                    alphabet.add(event);
                }
            }
        }
        return alphabet;
    }

    /**
     * Returns the monitor events for the given automaton.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automaton The automaton for which to return the monitor events.
     * @param alphabet The alphabet of the automaton, as obtained using the {@link #getAlphabetRefSet(Automaton)}
     *     method, for that same automaton. May be {@code null} to let this method compute the alphabet, if needed.
     * @return The monitor events of the automaton.
     */
    public static EventRefSet getMonitorsRefSet(Automaton automaton, EventRefSet alphabet) {
        // Handle no monitors.
        Monitors monitors = automaton.getMonitors();
        if (monitors == null) {
            return new EventRefSet();
        }

        // Handle monitors implicitly equal to alphabet.
        List<Expression> eventRefs = monitors.getEvents();
        if (eventRefs.isEmpty()) {
            return (alphabet != null) ? alphabet : getAlphabetRefSet(automaton);
        }

        // Handle explicitly specified monitors.
        EventRefSet rslt = new EventRefSet();
        for (Expression eventRef: eventRefs) {
            rslt.add(eventRef);
        }
        return rslt;
    }

    /**
     * Returns the monitor events (monitor alphabets) for the given automata.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automata The automata for which to return the monitor events.
     * @param alphabets The alphabets of the automata, as obtained using the {@link #getAlphabets(List)} method, for the
     *     same automata. May be {@code null} to let this method compute the alphabets, if needed.
     * @return The monitor events of the automata.
     */
    public static List<Set<Event>> getMonitors(List<Automaton> automata, List<Set<Event>> alphabets) {
        if (alphabets == null) {
            alphabets = getAlphabets(automata);
        }

        List<Set<Event>> monitorAlphabets = listc(automata.size());
        for (int i = 0; i < automata.size(); i++) {
            Automaton aut = automata.get(i);
            Set<Event> alphabet = alphabets.get(i);
            monitorAlphabets.add(getMonitors(aut, alphabet));
        }
        return monitorAlphabets;
    }

    /**
     * Returns the monitor events for the given automaton.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param automaton The automaton for which to return the monitor events.
     * @param alphabet The alphabet of the automaton, as obtained using the {@link #getAlphabet(Automaton)} method, for
     *     that same automaton. May be {@code null} to let this method compute the alphabet, if needed.
     * @return The monitor events of the automaton.
     */
    public static Set<Event> getMonitors(Automaton automaton, Set<Event> alphabet) {
        // Handle no monitors.
        Monitors monitors = automaton.getMonitors();
        if (monitors == null) {
            return set();
        }

        // Handle monitors implicitly equal to alphabet.
        List<Expression> eventRefs = monitors.getEvents();
        if (eventRefs.isEmpty()) {
            return (alphabet != null) ? alphabet : getAlphabet(automaton);
        }

        // Handle explicitly specified monitors.
        Set<Event> rslt = setc(eventRefs.size());
        for (Expression eventRef: eventRefs) {
            rslt.add(((EventExpression)eventRef).getEvent());
        }
        return rslt;
    }

    /**
     * Filters automata, for each of the given events. The automata that have the event in their alphabet are kept.
     *
     * @param automata The automata.
     * @param alphabets Per automaton, the alphabet.
     * @param events The events.
     * @return Per event, the filtered (kept) automata.
     */
    public static List<List<Automaton>> filterAutomata(List<Automaton> automata, List<Set<Event>> alphabets,
            List<Event> events)
    {
        List<List<Automaton>> rslt = listc(events.size());
        for (Event event: events) {
            rslt.add(filterAutomata(automata, alphabets, event));
        }
        return rslt;
    }

    /**
     * Filters automata for a given event. The automata that have the event in their alphabet are kept.
     *
     * @param automata The automata.
     * @param alphabets Per automaton, the alphabet.
     * @param event The event.
     * @return The filtered (kept) automata.
     */
    public static List<Automaton> filterAutomata(List<Automaton> automata, List<Set<Event>> alphabets, Event event) {
        List<Automaton> rslt = list();
        for (int i = 0; i < automata.size(); i++) {
            Automaton aut = automata.get(i);
            Set<Event> alphabet = alphabets.get(i);
            if (alphabet.contains(event)) {
                rslt.add(aut);
            }
        }
        return rslt;
    }

    /**
     * Filters automata, for each of the given events. The automata that monitor the given event are kept.
     *
     * @param automata The automata.
     * @param alphabets Per automaton, the monitor alphabet.
     * @param events The events.
     * @return Per event, the filtered (kept) automata.
     */
    public static List<Set<Automaton>> filterMonitorAuts(List<Automaton> automata, List<Set<Event>> alphabets,
            List<Event> events)
    {
        List<Set<Automaton>> rslt = listc(events.size());
        for (Event event: events) {
            rslt.add(filterMonitorAuts(automata, alphabets, event));
        }
        return rslt;
    }

    /**
     * Filters automata for a given event. The automata that monitor the given event are kept.
     *
     * @param automata The automata.
     * @param alphabets Per automaton, the monitor alphabet.
     * @param event The event.
     * @return The filtered (kept) automata.
     */
    public static Set<Automaton> filterMonitorAuts(List<Automaton> automata, List<Set<Event>> alphabets, Event event) {
        Set<Automaton> rslt = set();
        for (int i = 0; i < automata.size(); i++) {
            Automaton aut = automata.get(i);
            Set<Event> alphabet = alphabets.get(i);
            if (alphabet.contains(event)) {
                rslt.add(aut);
            }
        }
        return rslt;
    }

    /**
     * Returns the events for the given edge. This includes events used to synchronize, send, and receive.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param edge The edge for which to return the events.
     * @return The events of the edge.
     */
    public static EventRefSet getEventsRefSet(Edge edge) {
        EventRefSet events = new EventRefSet();

        // Add events from the edge.
        for (EdgeEvent edgeEvent: edge.getEvents()) {
            events.add(edgeEvent.getEvent());
        }

        // If no events on the edge, add 'tau'.
        if (edge.getEvents().isEmpty()) {
            TauExpression tauRef = newTauExpression();
            tauRef.setType(newBoolType());
            events.add(tauRef);
        }

        return events;
    }

    /**
     * Returns the events for the given edge. This includes events used to synchronize, send, and receive.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * <p>
     * This method does not support tau events.
     * </p>
     *
     * @param edge The edge for which to return the events.
     * @return The events of the edge.
     */
    public static Set<Event> getEvents(Edge edge) {
        Set<Event> events = setc(edge.getEvents().size());

        for (EdgeEvent edgeEvent: edge.getEvents()) {
            Expression eventRef = edgeEvent.getEvent();
            Event event = ((EventExpression)eventRef).getEvent();
            events.add(event);
        }

        return events;
    }

    /**
     * Returns the event referred to by the edge event, or {@code null} in case of a 'tau' event.
     *
     * @param edgeEvent The edge event. Wrapping expression for the event reference are not supported.
     * @return The event referred to by the edge event, or {@code null}.
     */
    public static Event getEventFromEdgeEvent(EdgeEvent edgeEvent) {
        Expression eventRef = edgeEvent.getEvent();
        if (eventRef instanceof TauExpression) {
            return null;
        }
        return ((EventExpression)eventRef).getEvent();
    }

    /**
     * Sorts events in ascending order, by name.
     *
     * @param events The events to sort.
     * @return The sorted events.
     * @see Strings#SORTER
     */
    public static List<Event> sortEvents(Set<Event> events) {
        Comparator<Event> cmp = new Comparator<>() {
            @Override
            public int compare(Event e1, Event e2) {
                return Strings.SORTER.compare(e1.getName(), e2.getName());
            }
        };
        return sortedgeneric(events, cmp);
    }

    /**
     * Returns a value indicating whether the two event reference expressions refer to the same event. Supports 'tau'
     * events.
     *
     * <p>
     * This method does not support specifications that have component definitions/instantiations. In particular, it
     * can't handle wrapping expressions for event references.
     * </p>
     *
     * @param ref1 The first event reference expression.
     * @param ref2 The second event reference expression.
     * @return {@code true} if the event reference expressions refer to the same event, {@code false} otherwise.
     */
    public static boolean areSameEventRefs(Expression ref1, Expression ref2) {
        // Doesn't support via component parameter reference expression or event parameter reference expression.
        if (isParamRefExpr(ref1) || isParamRefExpr(ref2)) {
            throw new RuntimeException("Unexpected parameter reference.");
        }

        // Doesn't support via component instantiation expression.
        if (ref1 instanceof CompInstWrapExpression || ref2 instanceof CompInstWrapExpression) {
            throw new RuntimeException("Unexpected via component instantiation reference.");
        }

        // Tau events.
        if (ref1 instanceof TauExpression && ref2 instanceof TauExpression) {
            return true;
        }

        // Event references.
        if (ref1 instanceof EventExpression && ref2 instanceof EventExpression) {
            Event event1 = ((EventExpression)ref1).getEvent();
            Event event2 = ((EventExpression)ref2).getEvent();
            return event1 == event2;
        }

        // The two event references don't refer to the same event.
        return false;
    }

    /**
     * Does the given event parameter support sending use?
     *
     * @param param The event parameter.
     * @return {@code true} if the event parameter support sending use, {@code false} otherwise.
     */
    public static boolean eventParamSupportsSend(EventParameter param) {
        // Sending only supported for channels.
        if (param.getEvent().getType() == null) {
            return false;
        }

        // Sending supported for channels if no flags.
        if (!param.isSendFlag() && !param.isRecvFlag() && !param.isSyncFlag()) {
            return true;
        }

        // Sending supported for channels with send (!) flag.
        if (param.isSendFlag()) {
            return true;
        }

        // Sending not supported for channels without send (!) flag.
        return false;
    }

    /**
     * Does the given event parameter support receiving use?
     *
     * @param param The event parameter.
     * @return {@code true} if the event parameter support receiving use, {@code false} otherwise.
     */
    public static boolean eventParamSupportsRecv(EventParameter param) {
        // Receiving only supported for channels.
        if (param.getEvent().getType() == null) {
            return false;
        }

        // Receiving supported for channels if no flags.
        if (!param.isSendFlag() && !param.isRecvFlag() && !param.isSyncFlag()) {
            return true;
        }

        // Receiving supported for channels with receive (?) flag.
        if (param.isRecvFlag()) {
            return true;
        }

        // Receiving not supported for channels without receive (?) flag.
        return false;
    }

    /**
     * Does the given event parameter support synchronizing use?
     *
     * @param param The event parameter.
     * @return {@code true} if the event parameter support synchronizing use, {@code false} otherwise.
     */
    public static boolean eventParamSupportsSync(EventParameter param) {
        // Synchronizing always supported for non-channels.
        if (param.getEvent().getType() == null) {
            return true;
        }

        // Synchronizing supported for channels if no flags.
        if (!param.isSendFlag() && !param.isRecvFlag() && !param.isSyncFlag()) {
            return true;
        }

        // Synchronizing supported for channels with synchronize (~) flag.
        if (param.isSyncFlag()) {
            return true;
        }

        // Synchronizing not supported for channels without synchronize (~)
        // flag.
        return false;
    }
}
