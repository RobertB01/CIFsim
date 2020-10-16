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

package org.eclipse.escet.cif.eventbased.builders.tests;

import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.builders.State;
import org.junit.Test;

/** Tests for the {@link State} class. */
public class StateTest {
    @SuppressWarnings("javadoc")
    @Test
    public void testEqual() {
        Event a = new Event("a", EventControllability.UNCONTR_EVENT);
        Event b = new Event("b", EventControllability.UNCONTR_EVENT);
        Set<Event> alphx = set(a);
        Set<Event> alphy = set(a, b);
        Automaton autx = new Automaton(alphx);
        Automaton auty = new Automaton(alphy);

        Location lxa = new Location(autx, null);
        autx.setInitial(lxa);

        Location lya = new Location(auty, null);
        auty.setInitial(lya);
        Location lyb = new Location(auty, null);

        Location[] locs;

        locs = new Location[2];
        locs[0] = lxa;
        locs[1] = lya;
        State s1 = new State(locs);

        locs = new Location[2];
        locs[0] = lxa;
        locs[1] = lyb;
        State s2 = new State(locs);

        locs = new Location[2];
        locs[0] = lxa;
        locs[1] = lya;
        State s3 = new State(locs);

        assertEquals(false, s1.equals(null));
        assertEquals(true, s1.equals(s1));

        assertEquals(false, s1.equals(s2));
        assertEquals(true, s1.equals(s3));

        assertEquals(false, s2.equals(s1));
        assertEquals(false, s2.equals(s3));

        assertEquals(false, s3.equals(s2));
        assertEquals(true, s3.equals(s1));
    }
}
