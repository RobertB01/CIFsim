//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.finiteresponse;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;

import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** Class representing the events in a forcible-event loop. */
public class EventLoop {
    /** All events that occur in the forcible-event loop. */
    public final Set<Event> events;

    /**
     * Constructor of the {@link EventLoop} class.
     *
     * @param events All events that occur in the forcible-event loop.
     */
    public EventLoop(Set<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Event evt: events) {
            sb.append((sb.length() == 0) ? "(" : " ");
            sb.append(getAbsName(evt));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EventLoop)) {
            return false;
        }
        EventLoop otherLoop = (EventLoop)other;
        return events.equals(otherLoop.events);
    }

    @Override
    public int hashCode() {
        return events.hashCode();
    }
}
