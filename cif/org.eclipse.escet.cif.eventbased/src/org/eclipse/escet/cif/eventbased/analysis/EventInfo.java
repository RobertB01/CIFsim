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

package org.eclipse.escet.cif.eventbased.analysis;

/** Information about an event. */
public class EventInfo {
    /** Name of the event. */
    public final String name;

    /** Controllability of the event. */
    public final boolean contr;

    /**
     * Constructor of the {@link EventInfo} class.
     *
     * @param name Name of the event.
     * @param contr Controllability of the event.
     */
    public EventInfo(String name, boolean contr) {
        this.name = name;
        this.contr = contr;
    }
}
