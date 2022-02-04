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

import org.eclipse.escet.cif.common.EventEquality;
import org.eclipse.escet.cif.common.EventRefSet;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.typechecker.ErrMsg;

/** Checker for unique events in alphabets, monitors, and on edges, for the 'post' type checking phase. */
public class UniqueEventsPostChecker {
    /** Constructor for the {@link UniqueEventsPostChecker} class. */
    private UniqueEventsPostChecker() {
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
        // Check for duplicated events in alphabet.
        Alphabet alphabet = aut.getAlphabet();
        if (alphabet != null) {
            EventEquality equality = null;
            EventRefSet otherEventRefs = new EventRefSet(equality);
            for (Expression eventRef: alphabet.getEvents()) {
                Expression duplicate = otherEventRefs.add(eventRef);
                if (duplicate != null) {
                    env.addProblem(ErrMsg.ALPHABET_DUPL_EVENT, eventRef.getPosition(), exprToStr(eventRef),
                            getAbsName(aut));
                    env.addProblem(ErrMsg.ALPHABET_DUPL_EVENT, duplicate.getPosition(), exprToStr(eventRef),
                            getAbsName(aut));
                    // Non-fatal error.
                }
            }
        }

        // Check for duplicated events in monitors.
        EventRefSet monitorSet = null;
        Monitors monitors = aut.getMonitors();
        if (monitors != null) {
            EventEquality equality = null;
            monitorSet = new EventRefSet(equality);
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

        // Check for duplicated events on edges.
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                EventEquality equality = null;
                EventRefSet otherEventRefs = new EventRefSet(equality);
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    Expression eventRef = edgeEvent.getEvent();

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
    }
}
