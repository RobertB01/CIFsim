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

package org.eclipse.escet.cif.eventbased.builders;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class CombinedEdgesTest {
    @SuppressWarnings("unused")
    @Test(expected = AssertionError.class)
    public void testEmptyAlphabet1() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Event b = new Event("b", EventControllability.CONTR_EVENT);
        Event c = new Event("c", EventControllability.CONTR_EVENT);

        // aut1: Automaton with empty alphabet.
        Set<Event> alph1 = set();
        Automaton aut1 = new Automaton(alph1);

        CombinedEdges ce = new CombinedEdges(a, list(aut1));
        assertEquals(a, ce.event);
        assertEquals(1, ce.sourceEdges.size());
        assertNull(ce.sourceEdges.get(0));

        ce.clear(); // Should not crash.
    }

    @SuppressWarnings("unused")
    @Test
    public void testEmptyAlphabet2() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Event b = new Event("b", EventControllability.CONTR_EVENT);
        Event c = new Event("c", EventControllability.CONTR_EVENT);

        // aut1: Automaton with empty alphabet.
        Set<Event> alph1 = set();
        Automaton aut1 = new Automaton(alph1);

        Set<Event> alph2 = set(a, b);
        Automaton aut2 = new Automaton(alph2);
        CombinedEdges ce = new CombinedEdges(b, list(aut1, aut2));
        assertEquals(b, ce.event);
        assertEquals(2, ce.sourceEdges.size());
        assertNull(ce.sourceEdges.get(0));
        assertNotNull(ce.sourceEdges.get(1));

        ce.clear(); // Should not crash.
    }

    // Tests of usage are in EdgeBuilderTest.
}
