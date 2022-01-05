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

package org.eclipse.escet.cif.explorer.runtime;

import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** How each event is being used by the automata. */
public class EventUsage {
    /** Event of the usage. */
    public final Event event;

    /** Whether the event is a channel (it needs a sender and receiver). */
    public final boolean isChannel;

    /** Indices of automata that have the event in their alphabet. */
    public final int[] alphabetIndices;

    /** Per automaton, whether the automaton monitors the event. */
    public final boolean[] monitorAuts;

    /** Indices of automata that send data with the event, {@code null} if the event cannot be used as channel. */
    public final int[] sendIndices;

    /** Indices of automata that receive data with the event. {@code null} if the event cannot be used as channel. */
    public final int[] recvIndices;

    /**
     * State/event exclusion invariants ordered by location. The {@code null} location is used for invariants of
     * components.
     */
    public final Map<Location, CollectedInvariants> stateEvtExclInvs;

    /**
     * Constructor of the {@link EventUsage} class.
     *
     * @param event Event that has its usage described.
     * @param alphabetIndices Indices of automata that have the event in their alphabet.
     * @param monitorAuts Per automaton, whether the automaton monitors the event.
     * @param sendIndices Indices of automata that send data with the event, ignored if the event is not a channel.
     * @param recvIndices Indices of automata that receive data with the event, ignored if the event is not a channel.
     * @param stateEvtExclInvs State/event exclusion invariants ordered by location. The {@code null} location is used
     *     for invariants of components.
     */
    public EventUsage(Event event, int[] alphabetIndices, boolean[] monitorAuts, int[] sendIndices, int[] recvIndices,
            Map<Location, CollectedInvariants> stateEvtExclInvs)
    {
        this.event = event;
        isChannel = (event.getType() != null);
        this.alphabetIndices = alphabetIndices;
        this.monitorAuts = monitorAuts;
        this.sendIndices = isChannel ? sendIndices : null;
        this.recvIndices = isChannel ? recvIndices : null;
        this.stateEvtExclInvs = stateEvtExclInvs;
    }
}
