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

package org.eclipse.escet.cif.eventbased.partitions;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Strings;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class PartitionRefinementTest {
    public Location loc1;

    public Location loc2;

    public Location loc3;

    public Location loc4;

    public Set<Location> sl1;

    public Set<Location> sl12;

    public Set<Location> sl123;

    public Set<Location> sl1234;

    public Set<Location> sl124;

    public Set<Location> sl13;

    public Set<Location> sl134;

    public Set<Location> sl14;

    public Set<Location> sl2;

    public Set<Location> sl23;

    public Set<Location> sl234;

    public Set<Location> sl24;

    public Set<Location> sl3;

    public Set<Location> sl34;

    public Set<Location> sl4;

    public Set<Set<Location>> startList;

    public String toString(Set<Set<Location>> superLocs) {
        List<String> results = list();
        for (Set<Location> locs: superLocs) {
            List<String> r = list();
            for (Location l: locs) {
                if (l == loc1) {
                    r.add("1");
                } else if (l == loc2) {
                    r.add("2");
                } else if (l == loc3) {
                    r.add("3");
                } else if (l == loc4) {
                    r.add("4");
                } else {
                    r.add("?");
                }
            }
            Collections.sort(r, Strings.SORTER);
            String s = "";
            for (String ss: r) {
                if (!s.isEmpty()) {
                    s += ",";
                }
                s += ss;
            }
            results.add("{" + s + "}");
        }
        Collections.sort(results, Strings.SORTER);
        String s = "";
        for (String ss: results) {
            if (!s.isEmpty()) {
                s += ",";
            }
            s += ss;
        }
        return "{" + s + "}";
    }

    @BeforeEach
    public void init() {
        Event e = new Event("e", EventControllability.CONTR_EVENT);
        Automaton aut = new Automaton(set(e));
        loc1 = new Location(aut, null);
        loc2 = new Location(aut, null);
        loc3 = new Location(aut, null);
        loc4 = new Location(aut, null);

        sl1 = set(loc1);
        sl12 = set(loc1, loc2);
        sl123 = set(loc1, loc2, loc3);
        sl1234 = set(loc1, loc2, loc3, loc4);
        sl124 = set(loc1, loc2, loc4);
        sl13 = set(loc1, loc3);
        sl134 = set(loc1, loc3, loc4);
        sl14 = set(loc1, loc4);
        sl2 = set(loc2);
        sl23 = set(loc2, loc3);
        sl234 = set(loc2, loc3, loc4);
        sl24 = set(loc2, loc4);
        sl3 = set(loc3);
        sl34 = set(loc3, loc4);
        sl4 = set(loc4);

        startList = set(sl12, sl3, sl4);
    }

    public void doTest(Set<Location> newLocs) {
        Set<Set<Location>> start = Sets.copy(startList);
        start.add(newLocs);
        Set<Set<Location>> hres = PartitionRefinement.hInfinite(start);
        Set<Set<Location>> expected = PartitionRefinement.q(hres);
        String expStr = toString(expected);

        Set<Set<Location>> actual = PartitionRefinement.addSet(startList, newLocs);
        String actStr = toString(actual);
        Assert.assertEquals(expStr, actStr);
    }

    @Test
    public void testStartlistIsStable() {
        // Q(Hinfinite( set((set(1,2),set(3),set(4)) ) is itself.
        Set<Set<Location>> expected = startList;
        Set<Set<Location>> actual = Sets.copy(startList);
        actual = PartitionRefinement.q(PartitionRefinement.hInfinite(actual));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddSl1() {
        doTest(sl1);
    }

    @Test
    public void testAddSl12() {
        doTest(sl12);
    }

    @Test
    public void testAddSl123() {
        doTest(sl123);
    }

    @Test
    public void testAddSl1234() {
        doTest(sl1234);
    }

    @Test
    public void testAddSl124() {
        doTest(sl124);
    }

    @Test
    public void testAddSl13() {
        doTest(sl13);
    }

    @Test
    public void testAddSl134() {
        doTest(sl134);
    }

    @Test
    public void testAddSl14() {
        doTest(sl14);
    }

    @Test
    public void testAddSl2() {
        doTest(sl2);
    }

    @Test
    public void testAddSl23() {
        doTest(sl23);
    }

    @Test
    public void testAddSl234() {
        doTest(sl234);
    }

    @Test
    public void testAddSl24() {
        doTest(sl24);
    }

    @Test
    public void testAddSl3() {
        doTest(sl3);
    }

    @Test
    public void testAddSl34() {
        doTest(sl34);
    }

    @Test
    public void testAddSl4() {
        doTest(sl4);
    }
}
