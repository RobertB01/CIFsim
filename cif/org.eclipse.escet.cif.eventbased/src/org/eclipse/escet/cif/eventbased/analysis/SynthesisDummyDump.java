//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.builders.State;

/** Dumper of what edges and states get removed during synthesis. */
public class SynthesisDummyDump implements SynthesisDumpInterface {
    @Override
    public boolean isFake() {
        return true;
    }

    @Override
    public void close() {
        // Nothing to do.
    }

    @Override
    public void storeAutomata(List<Automaton> auts, int numPlants) {
        // Nothing to do.
    }

    @Override
    public void newLocation(State state, Location loc) {
        // Nothing to do.
    }

    @Override
    public void newEdge(Event evt, Location src, Location dst) {
        // Nothing to do.
    }

    @Override
    public void disabledEvent(Location loc, Event evt, int autIndex) {
        // Nothing to do.
    }

    @Override
    public void removedDestination(Location loc, Event evt, Location dst) {
        // Nothing to do.
    }

    @Override
    public void blockingLocation(Location loc) {
        // Nothing to do.
    }

    @Override
    public void nonCoreachableLocation(Location loc) {
        // Nothing to do.
    }
}
