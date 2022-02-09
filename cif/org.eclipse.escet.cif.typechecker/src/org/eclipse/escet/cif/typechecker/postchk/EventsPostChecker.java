//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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
 * Checker for unique events in alphabets, monitors and on edges. Also checks for valid alphabet, monitors not in the
 * alphabet and dubious specification (alphabet disabling and event, monitoring events not on edges, and monitoring an
 * empty alphabet). Is used for the 'post' type checking phase.
 */
public class EventsPostChecker {
    /** Constructor for the {@link EventsPostChecker} class. */
    private EventsPostChecker() {
        // Static class.
    }

    /**
     * Checks the specification for violations of the 'Alphabet.uniqueEvents', 'Automaton.monitorsUniqueEvents' and
     * 'Edge.uniqueEvents' constraints.
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
     * Checks the automaton for violations of the 'Alphabet.uniqueEvents', 'Automaton.monitorsUniqueEvents' and
     * 'Edge.uniqueEvents' constraints.
     *
     * @param aut The automaton to check.
     * @param env The post check environment to use.
     */
    private static void check(Automaton aut, CifPostCheckEnv env) {
        // Create a set of events that are in the alphabet. First we fill it, then we remove events that are on edges.
        // Remaining events are never used on an edge.
        EventRefSet alphabetSet = null;

        // Check whether there is an explicit alphabet.
        Alphabet alphabet = aut.getAlphabet();
        if (alphabet != null) {
            alphabetSet = new EventRefSet();

            // Add the events in the alphabet to 'alphabetSet' and check for duplicated events in the alphabet.
            for (Expression eventRef: alphabet.getEvents()) {
                Expression duplicate = alphabetSet.add(eventRef);
                if (duplicate != null) {
                    env.addProblem(ErrMsg.ALPHABET_DUPL_EVENT, eventRef.getPosition(), exprToStr(eventRef),
                            getAbsName(aut));
                    env.addProblem(ErrMsg.ALPHABET_DUPL_EVENT, duplicate.getPosition(), exprToStr(eventRef),
                            getAbsName(aut));
                    // Non-fatal error.
                }
            }
        }

        // Create a set of events that are in the alphabet. We use 'alphabetSet' to fill it. We use it to determine
        // whether an events on an edge is in the alphabet.
        EventRefSet alphabetSetFixed = (alphabetSet == null) ? null : new EventRefSet(alphabetSet);

        // Create a set of events that are monitored.
        EventRefSet monitorSet = null;

        // Check whether there is an explicit monitor declaration.
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

        // Check all edges. We create a set of events that are on edges. This is the alphabet if there isn't an explicit
        // alphabet declaration. If there is an explicit alphabet, we check whether the events on the edges are in
        // there.
        EventRefSet defaultAlphabet = new EventRefSet();
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                EventRefSet otherEventRefs = new EventRefSet();
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    Expression eventRef = edgeEvent.getEvent();

                    // Exception for 'tau' and communicating events.
                    boolean isTau = eventRef instanceof TauExpression;
                    boolean isComm = edgeEvent instanceof EdgeSend || edgeEvent instanceof EdgeReceive;

                    // Event in explicit alphabet?
                    if (alphabetSetFixed != null && alphabetSet != null && !isTau && !isComm) {
                        if (!alphabetSetFixed.contains(eventRef)) {
                            PositionObject evt = CifScopeUtils.getRefObjFromRef(eventRef);
                            env.addProblem(ErrMsg.EVENT_NOT_IN_ALPHABET, eventRef.getPosition(),
                                    CifTextUtils.getAbsName(evt), getAbsName(aut));
                            // Non-fatal error.
                        }

                        // Remove event reference from 'alphabetSet', for always blocked
                        // events checking, later on.
                        alphabetSet.remove(eventRef);
                    }

                    // Add to default alphabet.
                    if (!isTau && !isComm) {
                        defaultAlphabet.add(eventRef);
                    }

                    // Duplicate event on edge? Since different uses on a single edge are
                    // checked elsewhere, we only need to report for duplicates within
                    // a single use here. We check duplicate synchronization uses and
                    // duplicate receive uses. Duplicate send uses may be useful, if
                    // different values are being sent.
                    if (!(edgeEvent instanceof EdgeSend)) {
                        Expression duplicate = otherEventRefs.add(eventRef);
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

        // Check for events in the alphabet beyond the default alphabet (the events on the edges, used to synchronize).
        if (alphabetSet != null && !alphabetSet.isEmpty()) {
            // Since 'alphabetSet' is not null, there is an explicit alphabet. Every event we've encountered on an edge
            // is already removed. Thus 'alphabetSet' contains events in the explicit alphabet, but not on an edge.
            for (Expression eventRef: alphabetSet) {
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

        // Get actual alphabet. Either explicitly defined or the events on the edges.
        EventRefSet actualAlphabet = (alphabetSetFixed == null) ? defaultAlphabet : alphabetSetFixed;

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
