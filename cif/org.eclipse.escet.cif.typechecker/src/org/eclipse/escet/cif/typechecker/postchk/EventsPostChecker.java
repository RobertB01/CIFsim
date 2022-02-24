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

package org.eclipse.escet.cif.typechecker.postchk;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.EventRefSet;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * Events type checker which is used for the 'post' type checking phase. It checks the following:
 * <ul>
 * <li>'Alphabet.uniqueEvents' constraint.</li>
 * <li>'Automaton.monitorsUniqueEvents' constraint.</li>
 * <li>'Edge.uniqueEvents' constraint.</li>
 * <li>'Automaton.validAlphabet' constraint.</li>
 * <li>'Automaton.monitorsSubsetAlphabet' constraint.</li>
 * </ul>
 * Also warns about the following dubious situations:
 * <ul>
 * <li>Event in explicit alphabet not on edge.</li>
 * <li>Monitored event not on edge.</li>
 * <li>Monitoring an empty alphabet.</li>
 * </ul>
 */
public class EventsPostChecker {
    /** Constructor for the {@link EventsPostChecker} class. */
    private EventsPostChecker() {
        // Static class.
    }

    /**
     * Checks the specification for various constraints and dubious situations (see {@link EventsPostChecker}).
     *
     * <p>
     * We need to check this after the elimination of component definition/instantiation to ensure proper equality
     * checking of events (mostly related to event parameters and component parameters).
     * </p>
     *
     * @param comp The component to check, recursively. The component must not include any component
     *     definitions/instantiations.
     * @param env The post check environment to use.
     */
    public static void check(ComplexComponent comp, CifPostCheckEnv env) {
        // Recursively check for groups.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                check((ComplexComponent)child, env);
            }
            return;
        }

        // Check for automaton.
        check((Automaton)comp, env);
    }

    /**
     * Checks the automaton for various constraints and dubious situations (see {@link EventsPostChecker}).
     *
     * @param aut The automaton to check.
     * @param env The post check environment to use.
     */
    private static void check(Automaton aut, CifPostCheckEnv env) {
        // Check whether there is an explicit alphabet declaration. If there is, collect the events in a set and check
        // if there are duplicated events in there.
        EventRefSet explicitAlphabetSet = null;
        Alphabet explicitAalphabet = aut.getAlphabet();
        if (explicitAalphabet != null) {
            explicitAlphabetSet = new EventRefSet();

            // Add the events in the alphabet to 'explicitAlphabetSet' and check for duplicated events in the alphabet.
            for (Expression eventRef: explicitAalphabet.getEvents()) {
                Expression duplicate = explicitAlphabetSet.add(eventRef);
                if (duplicate != null) {
                    env.addProblem(ErrMsg.ALPHABET_DUPL_EVENT, eventRef.getPosition(), exprToStr(eventRef),
                            getAbsName(aut));
                    env.addProblem(ErrMsg.ALPHABET_DUPL_EVENT, duplicate.getPosition(), exprToStr(eventRef),
                            getAbsName(aut));
                    // Non-fatal error.
                }
            }
        }

        // Check whether there is an explicit monitor declaration. If there is, collect the events in a set and check if
        // there are duplicated events in there.
        EventRefSet monitorSet = null;
        Monitors monitors = aut.getMonitors();
        if (monitors != null) {
            monitorSet = new EventRefSet();

            // Add the events in the monitor to 'monitorSet' and check for duplicated events in the monitor.
            for (Expression eventRef: monitors.getEvents()) {
                Expression duplicate = monitorSet.add(eventRef);
                if (duplicate != null) {
                    env.addProblem(ErrMsg.MONITORS_DUPL_EVENT, eventRef.getPosition(), exprToStr(eventRef),
                            getAbsName(aut));
                    env.addProblem(ErrMsg.MONITORS_DUPL_EVENT, duplicate.getPosition(), exprToStr(eventRef),
                            getAbsName(aut));
                    // Non-fatal error.
                }
            }
        }

        // Check all edges. We create a set of events that are on edges. This is the implicit alphabet. If there is an
        // explicit alphabet, we check whether the events in the implicit alphabet are also in the explicit alphabet.
        // Also checks for duplicated events on an edge.
        EventRefSet implicitAlphabetSet = new EventRefSet();
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                EventRefSet edgeEventRefs = new EventRefSet();
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    Expression eventRef = edgeEvent.getEvent();

                    // Exception for 'tau' and communicating events.
                    boolean isTau = eventRef instanceof TauExpression;
                    boolean isComm = edgeEvent instanceof EdgeSend || edgeEvent instanceof EdgeReceive;

                    // Event in explicit alphabet?
                    if (explicitAlphabetSet != null && !isTau && !isComm) {
                        if (!explicitAlphabetSet.contains(eventRef)) {
                            PositionObject evt = CifScopeUtils.getRefObjFromRef(eventRef);
                            env.addProblem(ErrMsg.EVENT_NOT_IN_ALPHABET, eventRef.getPosition(),
                                    CifTextUtils.getAbsName(evt), getAbsName(aut));
                            // Non-fatal error.
                        }
                    }

                    // Add to implicit alphabet.
                    if (!isTau && !isComm) {
                        implicitAlphabetSet.add(eventRef);
                    }

                    // Duplicate event on edge? Since different uses on a single edge are checked elsewhere, we only
                    // need to report for duplicates within a single use here. We check duplicate synchronization uses
                    // and duplicate receive uses. Duplicate send uses may be useful, if different values are being
                    // sent.
                    if (!(edgeEvent instanceof EdgeSend)) {
                        Expression duplicate = edgeEventRefs.add(eventRef);
                        if (duplicate != null) {
                            env.addProblem(ErrMsg.EDGE_DUPL_EVENT, eventRef.getPosition(), exprToStr(eventRef),
                                    getAbsName(aut));
                            env.addProblem(ErrMsg.EDGE_DUPL_EVENT, duplicate.getPosition(), exprToStr(eventRef),
                                    getAbsName(aut));
                            // Non-fatal error.
                        }
                    }
                }
            }
        }

        // Warn for events in the explicit alphabet but not in the implicit alphabet.
        if (explicitAlphabetSet != null) {
            // Determine the set difference of the explicit alphabet and the implicit alphabet.
            EventRefSet unusedExplicitAlphabetEvents = new EventRefSet(explicitAlphabetSet);
            unusedExplicitAlphabetEvents.removeAll(implicitAlphabetSet);

            // Warn for event in the explicit alphabet but not in the implicit alphabet.
            for (Expression eventRef: unusedExplicitAlphabetEvents) {
                boolean monitored = monitorSet != null && (monitorSet.isEmpty() || monitorSet.contains(eventRef));
                if (monitored) {
                    // Monitoring event that is not on any edge (essentially self looped in every location).
                    env.addProblem(ErrMsg.MONITOR_EVENT_NO_EDGE, eventRef.getPosition(), getAbsName(aut),
                            exprToStr(eventRef));
                    // Non-fatal error.
                } else {
                    // Globally disabled.
                    env.addProblem(ErrMsg.ALPHABET_DISABLED_EVENT, eventRef.getPosition(), getAbsName(aut),
                            exprToStr(eventRef));
                    // Non-fatal error.
                }
            }
        }

        // Get actual alphabet. Either explicitly defined or implicitly as the events on the edges.
        EventRefSet actualAlphabet = (explicitAlphabetSet == null) ? implicitAlphabetSet : explicitAlphabetSet;

        // Check for monitor events not in the actual alphabet.
        if (monitorSet != null) {
            for (Expression eventRef: monitorSet) {
                if (!actualAlphabet.contains(eventRef)) {
                    env.addProblem(ErrMsg.MONITOR_EVENT_NOT_IN_ALPHABET, eventRef.getPosition(), exprToStr(eventRef),
                            getAbsName(aut));
                    // Non-fatal error.
                }
            }
        }

        // Check for useless monitoring (we have a monitor, and we monitor the entire alphabet, but the alphabet is
        // empty).
        if (monitorSet != null && monitorSet.isEmpty() && actualAlphabet.isEmpty()) {
            env.addProblem(ErrMsg.MONITOR_EMPTY_ALPHABET, aut.getMonitors().getPosition(), getAbsName(aut));
            // Non-fatal problem.
        }
    }
}