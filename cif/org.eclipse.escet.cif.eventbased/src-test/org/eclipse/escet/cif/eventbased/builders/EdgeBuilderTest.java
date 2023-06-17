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

package org.eclipse.escet.cif.eventbased.builders;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class EdgeBuilderTest {
    @Test
    public void testConstruction() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Event b = new Event("b", EventControllability.CONTR_EVENT);
        Event c = new Event("c", EventControllability.CONTR_EVENT);

        Set<Event> alph1 = set(b);
        Automaton aut1 = new Automaton(alph1);
        Location loc1a = new Location(aut1, null);
        Location loc1b = new Location(aut1, null);
        Edge.addEdge(b, loc1a, loc1b);

        Set<Event> alph2 = set(a, b);
        Automaton aut2 = new Automaton(alph2);
        Location loc2a = new Location(aut2, null);
        Location loc2b = new Location(aut2, null);
        Edge.addEdge(a, loc2a, loc2b);

        EdgeBuilder eb = new EdgeBuilder(list(aut1, aut2), set(a, b, c));
        assertEquals(2, eb.edgeInfo.size()); // c is silently discarded.

        CombinedEdges ceA = eb.edgeInfo.get(a);
        CombinedEdges ceB = eb.edgeInfo.get(b);
        CombinedEdges ceC = eb.edgeInfo.get(c);
        assertNotNull(ceA);
        assertNotNull(ceB);
        assertNull(ceC);
    }

    @Test
    public void testDeadlock() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Event b = new Event("b", EventControllability.CONTR_EVENT);
        Event c = new Event("c", EventControllability.CONTR_EVENT);

        Set<Event> alph1 = set(a, b, c);
        Automaton aut1 = new Automaton(alph1);
        Location loc1a = new Location(aut1, null);
        Location loc1b = new Location(aut1, null);
        Edge.addEdge(a, loc1a, loc1b);

        Set<Event> alph2 = set(a, b, c);
        Automaton aut2 = new Automaton(alph2);
        Location loc2a = new Location(aut2, null);
        Location loc2b = new Location(aut2, null);
        Edge.addEdge(b, loc2a, loc2b);

        EdgeBuilder eb = new EdgeBuilder(list(aut1, aut2), set(a, b, c));
        assertEquals(3, eb.edgeInfo.size());

        CombinedEdges ceA = eb.edgeInfo.get(a);
        CombinedEdges ceB = eb.edgeInfo.get(b);
        CombinedEdges ceC = eb.edgeInfo.get(c);
        assertNotNull(ceA);
        assertNotNull(ceB);
        assertNotNull(ceC);

        // a cannot be done (aut2 is missing it)
        // b cannot be done (aut1 is missing it)
        // c cannot be done (no-one uses it)
        Location[] locs = new Location[2];
        locs[0] = loc1a;
        locs[1] = loc2a;
        State state = new State(locs);
        int count = 0;
        eb.setupStateEdges(state);
        for (StateEdges re: eb.getStateEdges()) {
            assertEquals(true, re.event == a || re.event == b || re.event == c);
            assertEquals(state, re.srcState);
            assertFalse(re.edgePossible());
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testDoStep() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);

        Set<Event> alph1 = set(a);
        Automaton aut1 = new Automaton(alph1);
        Location loc1a = new Location(aut1, null);
        Location loc1b = new Location(aut1, null);
        Edge.addEdge(a, loc1a, loc1b);

        Set<Event> alph2 = set(a);
        Automaton aut2 = new Automaton(alph2);
        Location loc2a = new Location(aut2, null);
        Location loc2b = new Location(aut2, null);
        Edge.addEdge(a, loc2a, loc2b);

        EdgeBuilder eb = new EdgeBuilder(list(aut1, aut2), set(a));

        Location[] locs = new Location[2];
        locs[0] = loc1a;
        locs[1] = loc2a;
        State state = new State(locs);
        int count = 0;
        eb.setupStateEdges(state);
        for (StateEdges re: eb.getStateEdges()) {
            assertTrue(re.edgePossible());
            int targetCount = 0;
            for (State s: re) {
                assertEquals(loc1b, s.locs[0]);
                assertEquals(loc2b, s.locs[1]);
                targetCount++;
            }
            assertEquals(1, targetCount);
            count++;
        }
        assertEquals(1, count);
    }

    @Test
    public void testDoTwoStep() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);

        Set<Event> alph1 = set(a);
        Automaton aut1 = new Automaton(alph1);
        Location loc1a = new Location(aut1, null);
        Location loc1b = new Location(aut1, null);
        Edge.addEdge(a, loc1a, loc1b);
        Edge.addEdge(a, loc1a, loc1b);

        Set<Event> alph2 = set(a);
        Automaton aut2 = new Automaton(alph2);
        Location loc2a = new Location(aut2, null);
        Location loc2b = new Location(aut2, null);
        Edge.addEdge(a, loc2a, loc2b);

        EdgeBuilder eb = new EdgeBuilder(list(aut1, aut2), set(a));

        Location[] locs = new Location[2];
        locs[0] = loc1a;
        locs[1] = loc2a;
        State state = new State(locs);
        int count = 0;
        eb.setupStateEdges(state);
        for (StateEdges re: eb.getStateEdges()) {
            assertTrue(re.edgePossible());
            int targetCount = 0;
            for (State s: re) {
                assertEquals(loc1b, s.locs[0]);
                assertEquals(loc2b, s.locs[1]);
                targetCount++;
            }
            assertEquals(2, targetCount);
            count++;
        }
        assertEquals(1, count);
    }

    @Test
    public void testDoTwoStepDifferent() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Event b = new Event("b", EventControllability.CONTR_EVENT);

        Set<Event> alph1 = set(a, b);
        Automaton aut1 = new Automaton(alph1);
        Location loc1a = new Location(aut1, null);
        Location loc1b = new Location(aut1, null);
        Edge.addEdge(a, loc1a, loc1b);

        Set<Event> alph2 = set(a, b);
        Automaton aut2 = new Automaton(alph2);
        Location loc2a = new Location(aut2, null);
        Location loc2b = new Location(aut2, null);
        Location loc2c = new Location(aut2, null);
        Edge.addEdge(a, loc2a, loc2b);
        Edge.addEdge(a, loc2a, loc2c);

        EdgeBuilder eb = new EdgeBuilder(list(aut1, aut2), set(a, b));

        Location[] locs = new Location[2];
        locs[0] = loc1a;
        locs[1] = loc2a;
        State state = new State(locs);
        int count = 0;
        int loc2bCount = 0;
        int loc2cCount = 0;
        eb.setupStateEdges(state);
        for (StateEdges re: eb.getStateEdges()) {
            if (re.event == a) {
                assertTrue(re.edgePossible());
                int targetCount = 0;
                for (State s: re) {
                    assertEquals(loc1b, s.locs[0]);
                    if (s.locs[1] == loc2b) {
                        loc2bCount++;
                    }
                    if (s.locs[1] == loc2c) {
                        loc2cCount++;
                    }
                    targetCount++;
                }
                assertEquals(2, targetCount);
            } else {
                assertFalse(re.edgePossible());
            }
            count++;
        }
        assertEquals(2, count);
        assertEquals(1, loc2bCount);
        assertEquals(1, loc2cCount);
    }

    @Test
    public void testDoFourStep() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);

        Set<Event> alph1 = set(a);
        Automaton aut1 = new Automaton(alph1);
        Location loc1a = new Location(aut1, null);
        Location loc1b = new Location(aut1, null);
        Edge.addEdge(a, loc1a, loc1b);
        Edge.addEdge(a, loc1a, loc1b);

        Set<Event> alph2 = set(a);
        Automaton aut2 = new Automaton(alph2);
        Location loc2a = new Location(aut2, null);
        Location loc2b = new Location(aut2, null);
        Edge.addEdge(a, loc2a, loc2b);
        Edge.addEdge(a, loc2a, loc2b);

        EdgeBuilder eb = new EdgeBuilder(list(aut1, aut2), set(a));

        Location[] locs = new Location[2];
        locs[0] = loc1a;
        locs[1] = loc2a;
        State state = new State(locs);
        int count = 0;
        eb.setupStateEdges(state);
        for (StateEdges re: eb.getStateEdges()) {
            assertTrue(re.edgePossible());
            int targetCount = 0;
            for (State s: re) {
                assertEquals(loc1b, s.locs[0]);
                assertEquals(loc2b, s.locs[1]);
                targetCount++;
            }
            assertEquals(4, targetCount);
            count++;
        }
        assertEquals(1, count);
    }
}
