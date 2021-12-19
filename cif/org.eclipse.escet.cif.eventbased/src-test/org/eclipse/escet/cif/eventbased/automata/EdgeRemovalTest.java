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

import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.junit.Test;

/** Tests for removing edges from the automaton. */
@SuppressWarnings("javadoc")
public class EdgeRemovalTest {
    @Test
    public void testSingleEdgeA() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Set<Event> alphabet = set(a);
        Automaton aut = new Automaton(alphabet);
        Location start = new Location(aut, null);
        Location end = new Location(aut, null);
        Location middle = new Location(aut, null);

        Edge x = Edge.addEdge(a, start, middle);
        Edge e = Edge.addEdge(a, start, end);
        Edge y = Edge.addEdge(a, middle, end);
        // New edges get added at the start of the edge lists, so
        // e is the first edge in 'start' and the last edge in 'end'.
        e.remove();

        assertEquals(x, start.outgoingEdges);
        assertEquals(null, x.nextOutgoing);
        assertEquals(null, start.incomingEdges);

        assertEquals(y, end.incomingEdges);
        assertEquals(null, y.nextIncoming);
        assertEquals(null, end.outgoingEdges);

        assertEquals(x, middle.incomingEdges);
        assertEquals(y, middle.outgoingEdges);
    }

    @Test
    public void testSingleEdgeB() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Set<Event> alphabet = set(a);
        Automaton aut = new Automaton(alphabet);
        Location start = new Location(aut, null);
        Location end = new Location(aut, null);
        Location middle = new Location(aut, null);

        Edge y = Edge.addEdge(a, middle, end);
        Edge e = Edge.addEdge(a, start, end);
        Edge x = Edge.addEdge(a, start, middle);
        // New edges get added at the start of the edge lists, so
        // e is the last edge in 'start' and the first edge in 'end'.
        e.remove();

        assertEquals(x, start.outgoingEdges);
        assertEquals(null, x.nextOutgoing);
        assertEquals(null, start.incomingEdges);

        assertEquals(y, end.incomingEdges);
        assertEquals(null, y.nextIncoming);
        assertEquals(null, end.outgoingEdges);

        assertEquals(x, middle.incomingEdges);
        assertEquals(y, middle.outgoingEdges);
    }
}
