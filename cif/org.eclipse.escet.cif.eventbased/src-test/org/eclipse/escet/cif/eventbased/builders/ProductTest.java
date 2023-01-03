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

import org.eclipse.escet.cif.eventbased.SynchronousProduct;
import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ProductTest {
    private int edgeCount(Location s) {
        int count = 0;
        Edge e = s.outgoingEdges;
        while (e != null) {
            e = e.nextOutgoing;
            count++;
        }
        return count;
    }

    @Test
    public void generalProductTest() {
        Event a = new Event("a", EventControllability.CONTR_EVENT);
        Event b = new Event("b", EventControllability.CONTR_EVENT);
        Event c = new Event("c", EventControllability.CONTR_EVENT);
        Event p = new Event("p", EventControllability.CONTR_EVENT);
        Event q = new Event("q", EventControllability.CONTR_EVENT);

        Automaton aa = new Automaton(set(a, b, p, c));
        Location x0 = new Location(aa, null);
        Location x1 = new Location(aa, null);
        Location x2 = new Location(aa, null);
        Location x3 = new Location(aa, null);

        aa.setInitial(x0);
        x3.marked = true;

        Edge.addEdge(a, x0, x1);
        Edge.addEdge(b, x1, x2);
        Edge.addEdge(p, x1, x1);
        Edge.addEdge(c, x2, x3);

        Automaton bb = new Automaton(set(p, q, b));
        Location y0 = new Location(bb, null);
        Location y1 = new Location(bb, null);
        Location y3 = new Location(bb, null);

        bb.setInitial(y0);
        y3.marked = true;

        Edge.addEdge(p, y0, y1);
        Edge.addEdge(q, y1, y3);
        Edge.addEdge(b, y3, y1);

        Automaton result = SynchronousProduct.product(list(aa, bb));
        Assert.assertEquals(8, result.size());
        Location s0 = result.initial;
        Edge e01a = s0.getOutgoing(a).next(); // (state-0, state-1, a)
        Location s1 = e01a.dstLoc;
        Edge e12p = s1.getOutgoing(p).next(); // (state-1, state-2, p)
        Location s2 = e12p.dstLoc;
        Edge e23q = s2.getOutgoing(q).next(); // (state-2, state-3, q),
        Location s3 = e23q.dstLoc;
        Edge e34b = s3.getOutgoing(b).next(); // (state-3, state-4, b),
        Location s4 = e34b.dstLoc;
        Edge e45c = s4.getOutgoing(c).next(); // (state-4, state-5, c),
        Location s5 = e45c.dstLoc;
        Edge e46q = s4.getOutgoing(q).next(); // (state-4, state-6, q),
        Location s6 = e46q.dstLoc;
        Edge e57q = s5.getOutgoing(q).next(); // (state-5, state-7, q),
        Location s7 = e57q.dstLoc;
        Edge e67c = s6.getOutgoing(c).next(); // (state-6, state-7, c)
        Assert.assertEquals(s7, e67c.dstLoc);

        // Verify location inequalities
        Assert.assertFalse(s0 == s1);
        Assert.assertFalse(s0 == s2);
        Assert.assertFalse(s0 == s3);
        Assert.assertFalse(s0 == s4);
        Assert.assertFalse(s0 == s5);
        Assert.assertFalse(s0 == s6);
        Assert.assertFalse(s0 == s7);

        Assert.assertFalse(s1 == s2);
        Assert.assertFalse(s1 == s3);
        Assert.assertFalse(s1 == s4);
        Assert.assertFalse(s1 == s5);
        Assert.assertFalse(s1 == s6);
        Assert.assertFalse(s1 == s7);

        Assert.assertFalse(s2 == s3);
        Assert.assertFalse(s2 == s4);
        Assert.assertFalse(s2 == s5);
        Assert.assertFalse(s2 == s6);
        Assert.assertFalse(s2 == s7);

        Assert.assertFalse(s3 == s4);
        Assert.assertFalse(s3 == s5);
        Assert.assertFalse(s3 == s6);
        Assert.assertFalse(s3 == s7);

        Assert.assertFalse(s4 == s5);
        Assert.assertFalse(s4 == s6);
        Assert.assertFalse(s4 == s7);

        Assert.assertFalse(s5 == s6);
        Assert.assertFalse(s5 == s7);

        Assert.assertFalse(s6 == s7);

        // Verify markers.
        Assert.assertFalse(s0.marked);
        Assert.assertFalse(s1.marked);
        Assert.assertFalse(s2.marked);
        Assert.assertFalse(s3.marked);
        Assert.assertFalse(s4.marked);
        Assert.assertFalse(s5.marked);
        Assert.assertFalse(s6.marked);
        Assert.assertTrue(s7.marked);

        // Verify edge count.
        Assert.assertEquals(1, edgeCount(s0));
        Assert.assertEquals(1, edgeCount(s1));
        Assert.assertEquals(1, edgeCount(s2));
        Assert.assertEquals(1, edgeCount(s3));
        Assert.assertEquals(2, edgeCount(s4));
        Assert.assertEquals(1, edgeCount(s5));
        Assert.assertEquals(1, edgeCount(s6));
        Assert.assertEquals(0, edgeCount(s7));
    }
}
