//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.junit.Test;

/** Tests for the automaton construction data structures. */
@SuppressWarnings("javadoc")
public class AutomatonDataStructureTest {
    @Test
    public void testEmptyAutomaton() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Event b = new Event("b", EventControllability.UNCONTR_EVENT);
        Set<Event> alphabet = set(a);
        Automaton aut = new Automaton(alphabet);

        // 1 event in the alphabet, namely 'a'.
        assertEquals(1, aut.alphabet.size());
        assertEquals(true, aut.alphabet.contains(a));
        assertEquals(false, aut.alphabet.contains(b));

        // No locations in the automaton, and no initial location.
        assertEquals(0, aut.size());
        assertEquals(null, aut.initial);
    }

    @SuppressWarnings("unused")
    @Test
    public void testSingleNoInitialAutomaton() {
        Set<Event> alphabet = set();
        Automaton aut = new Automaton(alphabet);
        Location loc = new Location(aut, null);

        // No events in the alphabet.
        assertEquals(0, aut.alphabet.size());

        // 1 location in the automaton, and no initial location.
        assertEquals(1, aut.size());
        assertEquals(null, aut.initial);
    }

    @Test
    public void testSingleWithInitialAutomaton() {
        Set<Event> alphabet = set();
        Automaton aut = new Automaton(alphabet);
        Location loc = new Location(aut, null);
        aut.setInitial(loc);

        // No event in the alphabet.
        assertEquals(true, aut.alphabet.isEmpty());

        // 1 location in the automaton, which is also the initial location.
        assertEquals(1, aut.size());
        assertEquals(loc, aut.initial);
    }

    @Test(expected = AssertionError.class)
    public void testOnlyOneInitialState() {
        Set<Event> alphabet = set();
        Automaton aut = new Automaton(alphabet);
        Location loc = new Location(aut, null);
        aut.setInitial(loc);
        assertEquals(loc, aut.initial);

        // Try to set the initial location again (and fail).
        aut.setInitial(loc);
    }

    /** Count the number of edges returned by the iterator. */
    private int countEdges(Iterator<Edge> iter) {
        int counter = 0;
        while (iter.hasNext()) {
            counter++;
            if (counter > 100) {
                fail();
            }
            iter.next();
        }
        return counter;
    }

    @Test
    public void testSingleEdge() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Event b = new Event("b", EventControllability.CONTR_EVENT);
        Set<Event> alphabet = set(a);
        Automaton aut = new Automaton(alphabet);
        Location loc = new Location(aut, null);
        aut.setInitial(loc);
        Location loc2 = new Location(aut, null);

        assertEquals(2, aut.size()); // 2 locations in the automaton.
        assertEquals(loc, aut.initial);

        Edge e1 = Edge.addEdge(a, loc, loc2);

        assertEquals(null, loc.incomingEdges);
        assertEquals(e1, loc.outgoingEdges);
        assertEquals(e1, loc2.incomingEdges);
        assertEquals(null, loc2.outgoingEdges);

        assertEquals(0, countEdges(loc.getIncoming()));
        assertEquals(1, countEdges(loc.getOutgoing()));
        assertEquals(1, countEdges(loc2.getIncoming()));
        assertEquals(0, countEdges(loc2.getOutgoing()));

        assertEquals(0, countEdges(loc.getIncoming(a)));
        assertEquals(1, countEdges(loc.getOutgoing(a)));
        assertEquals(1, countEdges(loc2.getIncoming(a)));
        assertEquals(0, countEdges(loc2.getOutgoing(a)));

        assertEquals(0, countEdges(loc.getIncoming(b)));
        assertEquals(0, countEdges(loc.getOutgoing(b)));
        assertEquals(0, countEdges(loc2.getIncoming(b)));
        assertEquals(0, countEdges(loc2.getOutgoing(b)));
    }

    @Test
    public void testSelfloop() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Set<Event> alphabet = set(a);
        Automaton aut = new Automaton(alphabet);
        Location loc = new Location(aut, null);
        aut.setInitial(loc);

        assertEquals(1, aut.size()); // 1 locations in the automaton.
        assertEquals(loc, aut.initial);

        Edge e1 = Edge.addEdge(a, loc, loc);

        assertEquals(e1, loc.incomingEdges);
        assertEquals(e1, loc.outgoingEdges);

        assertEquals(a, loc.incomingEdges.event);

        assertEquals(1, countEdges(loc.getIncoming()));
        assertEquals(1, countEdges(loc.getOutgoing()));

        assertEquals(1, countEdges(loc.getIncoming(a)));
        assertEquals(1, countEdges(loc.getOutgoing(a)));
    }

    @Test
    public void testMultipleEdges() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Event b = new Event("b", EventControllability.CONTR_EVENT);
        Set<Event> alphabet = set(a, b);
        Automaton aut = new Automaton(alphabet);
        Location loc = new Location(aut, null);
        aut.setInitial(loc);
        Location loc2 = new Location(aut, null);
        loc2.marked = true;

        Edge.addEdge(a, loc, loc); // Self-loop initial.
        Edge.addEdge(b, loc, loc2); // initial -> loc2.
        Edge.addEdge(b, loc2, loc); // loc2 -> initial.
        Edge.addEdge(a, loc2, loc2); // Self loop loc2.

        assertEquals(2, countEdges(loc.getIncoming()));
        assertEquals(2, countEdges(loc.getOutgoing()));
        assertEquals(2, countEdges(loc2.getIncoming()));
        assertEquals(2, countEdges(loc2.getOutgoing()));

        assertEquals(1, countEdges(loc.getIncoming(a)));
        assertEquals(1, countEdges(loc.getOutgoing(a)));
        assertEquals(1, countEdges(loc2.getIncoming(a)));
        assertEquals(1, countEdges(loc2.getOutgoing(a)));

        assertEquals(1, countEdges(loc.getIncoming(b)));
        assertEquals(1, countEdges(loc.getOutgoing(b)));
        assertEquals(1, countEdges(loc2.getIncoming(b)));
        assertEquals(1, countEdges(loc2.getOutgoing(b)));
    }
}
