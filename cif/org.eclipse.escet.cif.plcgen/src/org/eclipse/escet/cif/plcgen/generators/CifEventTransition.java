//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.cif.common.CifAddressableUtils.collectAddrVars;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * CIF data for generating a transition for an event.
 *
 * <p>
 * <ul>
 * <li>A channel event can be performed if and only if:
 * <ul>
 * <li>At least one of the {@link #senders} automata has an enabled edge,</li>
 * <li>at least one of the {@link #receivers} automata has an enabled edge, and</li>
 * <li>every automaton in {@link #syncers} has at least one enabled edge.</li>
 * </ul>
 * Performing an event transition for a channel event is done by performing an edge transition with one of the
 * {@link #senders} with an enabled edge, one of the {@link #receivers} with an enabled edge, all {@link #syncers}, and
 * all {@link #monitors} that have an enabled edge. Automata in {@link #monitors} without enabled edge don't synchronize
 * in the performed event transition and keep their state.</li>
 *
 * <li>A normal event (that is, not a channel) ignores the {@link #senders} and {@link #receivers} automata. It is
 * generally recommended not to use these variables for normal events. A normal event can be performed if and only if:
 * <ul>
 * <li>Every automaton in {@link #syncers} has at least one enabled edge.</li>
 * </ul>
 * Performing an event transition for a normal event is done by performing an edge transition with all {@link #syncers}
 * automata, and all {@link #monitors} that have an enabled edge. Automata in {@link #monitors} without enabled edge
 * don't synchronize in the performed event transition and keep their state.</li>
 * </ul>
 * </p>
 */
public class CifEventTransition {
    /** Event of the transition. */
    public final Event event;

    /** Transitions of automata that send a value with the event. */
    public final List<TransitionAutomaton> senders;

    /** Transitions of automata that receive a value with the event. */
    public final List<TransitionAutomaton> receivers;

    /** Transitions of automata that always synchronize with the event. */
    public final List<TransitionAutomaton> syncers;

    /** Transitions of automata that may or may not synchronize with the event. */
    public final List<TransitionAutomaton> monitors;

    /**
     * Constructor of the {@link CifEventTransition} class.
     *
     * @param event Event of the transition.
     */
    public CifEventTransition(Event event) {
        this(event, list(), list(), list(), list());
    }

    /**
     * Constructor of the {@link CifEventTransition} class.
     *
     * @param event Event of the transition.
     * @param senders Transitions of automata that send a value with the event.
     * @param receivers Transitions of automata that receive a value with the event.
     * @param syncers Transitions of automata that always synchronize with the event.
     * @param monitors Transitions of automata that may or may not synchronize with the event.
     */
    public CifEventTransition(Event event, List<TransitionAutomaton> senders, List<TransitionAutomaton> receivers,
            List<TransitionAutomaton> syncers, List<TransitionAutomaton> monitors)
    {
        this.event = event;
        this.senders = senders;
        this.receivers = receivers;
        this.syncers = syncers;
        this.monitors = monitors;
    }

    /**
     * Collect the variables that may be assigned when performing the event.
     *
     * @return The collected variables that are assigned at least once in one of the edges of the participating automata.
     */
    public Set<Declaration> collectAssignedVariables() {
        Set<Declaration> assignedVariables = set();
        for (TransitionAutomaton sender: senders) {
            sender.collectAssignedVariables(assignedVariables);
        }
        for (TransitionAutomaton receiver: receivers) {
            receiver.collectAssignedVariables(assignedVariables);
        }
        for (TransitionAutomaton syncer: syncers) {
            syncer.collectAssignedVariables(assignedVariables);
        }
        for (TransitionAutomaton monitor: monitors) {
            monitor.collectAssignedVariables(assignedVariables);
        }
        return assignedVariables;
    }

    /** CIF data of an automaton for the event. */
    public static class TransitionAutomaton {
        /** Automaton described in the instance. */
        public final Automaton aut;

        /** Edges of the automaton that can be executed for the event. */
        public final List<TransitionEdge> transitionEdges;

        /**
         * Constructor of the {@link TransitionAutomaton} class.
         *
         * @param aut Automaton described in the instance.
         * @param transitionEdges Edges of the automaton that can be executed for the event.
         */
        public TransitionAutomaton(Automaton aut, List<TransitionEdge> transitionEdges) {
            this.aut = aut;
            this.transitionEdges = transitionEdges;
        }

        /**
         * Collect the assigned variables from the edges of the automaton.
         *
         * @param assignedVariables The collected assigned variables. Is updated in-place.
         */
        public void collectAssignedVariables(Set<Declaration> assignedVariables) {
            for (TransitionEdge transEdge: transitionEdges) {
                collectAddrVars(transEdge.updates, assignedVariables);
            }
        }
    }

    /** An edge of an automaton that may be executed with the event. */
    public static class TransitionEdge {
        /** Source location of the edge. */
        public final Location sourceLoc;

        /** Target location of the edge. */
        public final Location targetLoc;

        /** Value being sent by the send automaton in the edge, {@code null} otherwise. */
        public final Expression sendValue;

        /** Guards of the edge. */
        public final List<Expression> guards;

        /** Updates of the edge. */
        public final List<Update> updates;

        /**
         * Constructor of the {@link TransitionEdge} class.
         *
         * @param sourceLoc Source location of the edge.
         * @param targetLoc Target location of the edge.
         * @param sendValue Value being sent by the send automaton in the edge, {@code null} otherwise.
         * @param guards Guards of the edge.
         * @param updates Updates of the edge.
         */
        public TransitionEdge(Location sourceLoc, Location targetLoc, Expression sendValue, List<Expression> guards,
                List<Update> updates)
        {
            this.sourceLoc = sourceLoc;
            this.targetLoc = targetLoc;
            this.sendValue = sendValue;
            this.guards = guards;
            this.updates = updates;
        }
    }
}
