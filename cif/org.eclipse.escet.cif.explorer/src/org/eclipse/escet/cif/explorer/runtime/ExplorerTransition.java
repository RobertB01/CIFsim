//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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
public class ExplorerTransition {
    /** The originating state. */
    public final BaseState prev;

    /** The destination state. */
    public final BaseState next;

    /** Event at the transition, {@code null} means 'tau' event. */
    public final Event event;

    /** Value that is communicated with the event, or {@code null}. */
    public final Object commValue;

    /**
     * Construct a transition between two states.
     *
     * <p>
     * NOTE: Transitions of the states are updated during construction of the transition.
     * </p>
     *
     * @param prev The Originating state.
     * @param next The destination state.
     * @param event Event at the transition, {@code null} means 'tau' event.
     * @param commValue Value that is communicated with the event, or {@code null}.
     */
    public ExplorerTransition(BaseState prev, BaseState next, Event event, Object commValue) {
        this.prev = prev;
        this.next = next;
        this.event = event;
        this.commValue = commValue;

        // Insert transition into both states.
        prev.outgoingTransitions.add(this);
        next.incomingTransitions.add(this);
    }
}
