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

package org.eclipse.escet.cif.eventbased.equivalence;

import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;

/** Counter example for the language equivalence check. */
public class CounterExample {
    /** Path to the counter example locations in both automata. */
    public final List<Event> path;

    /** Locations at the end of the path. */
    public final Location[] locs;

    /** Event of the counter example, {@code null} means the marker information of both locations was different. */
    public final Event event;

    /**
     * Constructor of the {@link CounterExample} class.
     *
     * @param path Path to the counter example locations in both automata.
     * @param locs Locations at the end of the path.
     * @param event Event of the counter example.
     */
    public CounterExample(List<Event> path, Location[] locs, Event event) {
        this.path = path;
        this.locs = locs;
        this.event = event;
    }
}
