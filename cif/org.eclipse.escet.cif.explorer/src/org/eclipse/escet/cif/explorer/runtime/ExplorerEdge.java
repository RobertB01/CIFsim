//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

/** Transition between two states. */
public class ExplorerEdge {
    /** The originating state. */
    public final BaseState prev;

    /** The destination state. */
    public final BaseState next;

    /** Event at the edge, {@code null} means 'tau' event. */
    public final Event event;

    /** Value that is communicated with the event, or {@code null}. */
    public final Object commValue;

    /**
     * Construct an edge between two states.
     *
     * <p>
     * NOTE: Edges of the states are updated during construction of the edge.
     * </p>
     *
     * @param prev The Originating state.
     * @param next The destination state.
     * @param event Event at the edge, {@code null} means 'tau' event.
     * @param commValue Value that is communicated with the event, or {@code null}.
     */
    public ExplorerEdge(BaseState prev, BaseState next, Event event, Object commValue) {
        this.prev = prev;
        this.next = next;
        this.event = event;
        this.commValue = commValue;

        // Insert edge into both states.
        prev.outgoingEdges.add(this);
        next.incomingEdges.add(this);
    }
}
