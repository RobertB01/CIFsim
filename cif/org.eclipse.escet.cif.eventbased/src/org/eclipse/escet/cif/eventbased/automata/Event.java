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

package org.eclipse.escet.cif.eventbased.automata;

import org.eclipse.escet.common.java.Assert;

/** An event, note that events are singleton objects for fast comparison. */
public class Event {
    /**
     * Name of the event. Events loaded from a CIF specification have the absolute name of the corresponding CIF event,
     * without escaping of CIF keywords.
     */
    public final String name;

    /** Controllability of the event. */
    public final EventControllability contr;

    /**
     * Constructor of the {@link Event} class.
     *
     * @param name Name of the event.
     * @param contr Controllability of the event, {@code true} if controllable.
     */
    public Event(String name, EventControllability contr) {
        this.name = name;
        this.contr = contr;
    }

    /**
     * Retrieve the controllability property of the event.
     *
     * <p>
     * Events without controllability property may not be queried.
     * </p>
     *
     * @return {@code true} if the event is controllable, else {@code false}.
     */
    public boolean isControllable() {
        Assert.check(contr != EventControllability.PLAIN_EVENT);
        return contr == EventControllability.CONTR_EVENT;
    }

    /** Value of the controllability property of an event. */
    public static enum EventControllability {
        /** Controllable event. */
        CONTR_EVENT,

        /** Uncontrollable event. */
        UNCONTR_EVENT,

        /** Simple event. */
        PLAIN_EVENT,
    }
}
