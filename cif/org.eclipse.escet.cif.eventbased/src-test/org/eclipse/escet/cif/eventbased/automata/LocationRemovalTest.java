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

import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.junit.Test;

/** Tests for edge and location removal from an automaton. */
@SuppressWarnings("javadoc")
public class LocationRemovalTest {
    /** Remove the single location of an automaton without edges. */
    @Test
    public void testRemoveSingleLocation() {
        Set<Event> alphabet = set();
        Automaton aut = new Automaton(alphabet);
        Location loc = new Location(aut, null);

        aut.removeLocation(loc);
    }

    /** Try to remove the initial location of an automaton without edges. */
    @Test(expected = AssertionError.class)
    public void testRemoveInitialLocation() {
        Set<Event> alphabet = set();
        Automaton aut = new Automaton(alphabet);
        Location loc = new Location(aut, null);
        aut.setInitial(loc);

        aut.removeLocation(loc);
    }

    private void checkStart123End(boolean remove1, boolean remove2, boolean remove3) {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Set<Event> alphabet = set(a);
        Automaton aut = new Automaton(alphabet);
        Location start = new Location(aut, null);
        Location loc1 = new Location(aut, null);
        Location loc2 = new Location(aut, null);
        Location loc3 = new Location(aut, null);
        Location end = new Location(aut, null);

        Edge.addEdge(a, start, loc3);
        Edge.addEdge(a, start, loc2);
        Edge.addEdge(a, start, loc1);

        Edge.addEdge(a, loc3, end);
        Edge.addEdge(a, loc2, end);
        Edge.addEdge(a, loc1, end);

        Set<Location> remaining123 = set();
        if (remove1) {
            aut.removeLocation(loc1);
        } else {
            remaining123.add(loc1);
        }
        if (remove2) {
            aut.removeLocation(loc2);
        } else {
            remaining123.add(loc2);
        }
        if (remove3) {
            aut.removeLocation(loc3);
        } else {
            remaining123.add(loc3);
        }

        int count = aut.size();
        if (remove1) {
            count++;
        }
        if (remove2) {
            count++;
        }
        if (remove3) {
            count++;
        }
        assertEquals(5, count);

        // There should be two outgoing edges.
        Set<Location> outs = set();
        for (Edge e: start.getOutgoing()) {
            assertEquals(true, outs.add(e.dstLoc));
        }
        assertEquals(remaining123, outs);

        // There should be two incoming edges.
        Set<Location> ins = set();
        for (Edge e: end.getIncoming()) {
            assertEquals(true, ins.add(e.srcLoc));
        }
        assertEquals(remaining123, ins);
    }

    @Test
    public void testRemove1() {
        checkStart123End(true, false, false);
    }

    @Test
    public void testRemove2() {
        checkStart123End(false, true, false);
    }

    @Test
    public void testRemove3() {
        checkStart123End(false, false, true);
    }
}
