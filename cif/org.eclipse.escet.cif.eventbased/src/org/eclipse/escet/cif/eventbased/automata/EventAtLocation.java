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

package org.eclipse.escet.cif.eventbased.automata;

import static org.eclipse.escet.common.java.Strings.fmt;

/** Class denoting an event at a location. */
public class EventAtLocation {
    /** Location of the event. */
    public final Location loc;

    /** Event itself. */
    public final Event evt;

    /**
     * Constructor of the {@link EventAtLocation} class.
     *
     * @param loc Location of the event.
     * @param evt Event itself.
     */
    public EventAtLocation(Location loc, Event evt) {
        this.loc = loc;
        this.evt = evt;
    }

    @Override
    public String toString() {
        return fmt("event \"%s\" at %s", evt.name, loc);
    }
}
