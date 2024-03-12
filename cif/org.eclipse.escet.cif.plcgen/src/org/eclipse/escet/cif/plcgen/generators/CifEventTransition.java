//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.cif.plcgen.generators.CifProcessor.AutomatonRole;

/**
 * CIF data for generating a transition for an event.
 *
 * <p>
 * <ul>
 * <li>A channel event can be performed if and only if:
 * <ul>
 * <li>At least one of the {@link #senders} has an enabled edge,</li>
 * <li>at least one of the {@link #receivers} has an enabled edge, and</li>
 * <li>every automaton in {@link #syncers} has at least one enabled edge.</li>
 * </ul>
 * Performing an event transition for a channel event is done by performing an edge transition with one of the
 * {@link #senders} with an enabled edge, one of the {@link #receivers} with an enabled edge, all {@link #syncers}, and
 * all {@link #monitors} that have an enabled edge. Automata in {@link #monitors} without an enabled edge don't
 * explicitly participate in the performed event transition and keep their state.</li>
 *
 * <li>A non-channel event ignores the {@link #senders} and {@link #receivers}. It is generally recommended not to use
 * these variables for such events. A non-channel event can be performed if and only if:
 * <ul>
 * <li>All {@link #syncers} have at least one enabled edge.</li>
 * </ul>
 * Performing an event transition for a non-channel event is done by performing an edge transition with all
 * {@link #syncers}, and all {@link #monitors} that have an enabled edge. Automata in {@link #monitors} without an
 * enabled edge don't explicitly participate in the performed event transition and keep their state.</li>
 * </ul>
 * </p>
 */
public class CifEventTransition {
    /** Event of the transition. */
    public final Event event;

    /** Transitions of {@link AutomatonRole#SENDER sender automata}, for the {@link #event}. */
    public final List<TransitionAutomaton> senders;

    /** Transitions of {@link AutomatonRole#RECEIVER receiver automata}, for the {@link #event}. */
    public final List<TransitionAutomaton> receivers;

    /** Transitions of {@link AutomatonRole#SYNCER syncer automata}, for the {@link #event}. */
    public final List<TransitionAutomaton> syncers;

    /** Transitions of {@link AutomatonRole#MONITOR monitor automata}, for the {@link #event}. */
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
     * @param senders Transitions of {@link AutomatonRole#SENDER sender automata}, for the event.
     * @param receivers Transitions of {@link AutomatonRole#RECEIVER receiver automata}, for the event.
     * @param syncers Transitions of {@link AutomatonRole#SYNCER syncer automata}, for the event.
     * @param monitors Transitions of {@link AutomatonRole#MONITOR monitor automata}, for the event.
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
     * @return The collected variables that are assigned at least once in one of the edges of the participating
     *     automata.
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

        /** Reason for having the transition automaton. */
        public final TransAutPurpose purpose;

        /** Edges of the automaton that can be executed for the event. */
        public final List<TransitionEdge> transitionEdges;

        /**
         * Constructor of the {@link TransitionAutomaton} class.
         *
         * @param aut Automaton described in the instance.
         * @param purpose Reason for having the transition automaton.
         * @param transitionEdges Edges of the automaton that can be executed for the event.
         */
        public TransitionAutomaton(Automaton aut, TransAutPurpose purpose, List<TransitionEdge> transitionEdges) {
            this.aut = aut;
            this.purpose = purpose;
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

    /** Reason for having the transition automaton. */
    public static enum TransAutPurpose {
        /** The automaton is a sender automaton, that sends values over the channel. */
        SENDER,

        /** The automaton is a receiver automaton, that receives values from the channel. */
        RECEIVER,

        /** The automaton is a syncer automaton, that synchronizes on the event, but does not monitor it. */
        SYNCER,

        /** The automaton is a monitor automaton, that synchronizes on the event, and monitors it. */
        MONITOR;
    }

    /** An edge of an automaton that may be executed with the event. */
    public static class TransitionEdge {
        /** One-based index number to identify an edge within a location. */
        public final int edgeNumber;

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
         * @param edgeNumber One-based index number to identify an edge within a location.
         * @param sourceLoc Source location of the edge.
         * @param targetLoc Target location of the edge.
         * @param sendValue Value being sent by the send automaton in the edge, {@code null} otherwise.
         * @param guards Guards of the edge.
         * @param updates Updates of the edge.
         */
        public TransitionEdge(int edgeNumber, Location sourceLoc, Location targetLoc, Expression sendValue,
                List<Expression> guards, List<Update> updates)
        {
            this.edgeNumber = edgeNumber;
            this.sourceLoc = sourceLoc;
            this.targetLoc = targetLoc;
            this.sendValue = sendValue;
            this.guards = guards;
            this.updates = updates;
        }
    }
}
