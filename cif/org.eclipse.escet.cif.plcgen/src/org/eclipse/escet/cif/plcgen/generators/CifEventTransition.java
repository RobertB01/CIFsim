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

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/** CIF data for generating a transition for an event. */
public class CifEventTransition {
    /** Event of the transition. */
    public final Event event;

    /** Transitions of automata that send a value with the event. */
    public final List<TransitionAutomaton> senders;

    /** Transitions of automata that receive a value with the event. */
    public final List<TransitionAutomaton> receivers;

    /** Transitions of automata that always participate with the event. */
    public final List<TransitionAutomaton> syncers;

    /** Transitions of automata that may or may not participate with the event. */
    public final List<TransitionAutomaton> monitors;

    /**
     * Constructor of the {@link CifEventTransition} class.
     *
     * @param event Event of the transition.
     * @param senders Transitions of automata that send a value with the event.
     * @param receivers Transitions of automata that receive a value with the event.
     * @param syncers Transitions of automata that always participate with the event.
     * @param monitors Transitions of automata that may or may not participate with the event.
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
    }

    /** An edge of an automaton that may be executed with the event. */
    public final class TransitionEdge {
        /** Start location of the edge. */
        public final Location sourceLoc;

        /** Destination location of the edge. */
        public final Location destinationLoc;

        /** Value being sent by the send automaton in the edge, {@code null} otherwise. */
        public final Expression sendValue;

        /** Guards of the edge. */
        public final List<Expression> guards;

        /** Updates of the edge. */
        public final List<Update> updates;

        /**
         * Constructor of the {@link TransitionEdge} class.
         *
         * @param sourceLoc Start location of the edge.
         * @param destinationLoc Destination location of the edge.
         * @param sendValue Value being sent by the send automaton in the edge, {@code null} otherwise.
         * @param guards Guards of the edge.
         * @param updates Updates of the edge.
         */
        public TransitionEdge(Location sourceLoc, Location destinationLoc, Expression sendValue,
                List<Expression> guards, List<Update> updates)
        {
            this.sourceLoc = sourceLoc;
            this.destinationLoc = destinationLoc;
            this.sendValue = sendValue;
            this.guards = guards;
            this.updates = updates;
        }
    }
}
