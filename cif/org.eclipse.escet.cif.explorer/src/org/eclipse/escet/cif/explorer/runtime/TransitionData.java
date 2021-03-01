//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** Temporary storage related to computing a transition. */
public class TransitionData {
    /** Originating state. */
    public final BaseState origState;

    /** Event being performed, {@code null} means 'tau' event. */
    public Event event;

    /** Send edge, or {@code null} if no send edge available. */
    public ChosenEdge sendEdge = null;

    /** Index of the automaton doing the send (negative if no sender). */
    public int sendIdx = -1;

    /** Chosen edges (excluding the send automaton). */
    public ChosenEdge[] edges;

    /**
     * Constructor of the {@link TransitionData} class.
     *
     * @param origState Originating state.
     * @param numAuts Number of automata in the system.
     */
    public TransitionData(BaseState origState, int numAuts) {
        this.origState = origState;
        edges = new ChosenEdge[numAuts];
    }
}
