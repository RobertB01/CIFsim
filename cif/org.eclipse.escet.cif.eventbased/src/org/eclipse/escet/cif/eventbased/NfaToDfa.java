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

package org.eclipse.escet.cif.eventbased;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.automata.origin.LocationSetOrigin;
import org.eclipse.escet.cif.eventbased.automata.origin.Origin;

/** Conversion to deterministic automaton. */
public class NfaToDfa {
    /** Constructor of the {@link NfaToDfa} class. */
    private NfaToDfa() {
        // Static class.
    }

    /**
     * Convert an automaton to its deterministic equivalent.
     *
     * @param aut Source automaton.
     * @param preservedEvents Events that should be kept in the result.
     * @return Equivalent deterministic automaton, with merging sets of states connected through events from
     *     'skipEvents'.
     */
    public static Automaton toDFA(Automaton aut, Set<Event> preservedEvents) {
        Map<Set<Location>, Location> locMap = map();
        Queue<Set<Location>> notDone = new ArrayDeque<>(1000);

        // Alphabet of the result is the original set minus the skipped events.
        Automaton result = new Automaton(preservedEvents);
        result.kind = aut.kind;

        if (aut.initial == null) {
            return result;
        }

        Set<Event> skipEvents = copy(aut.alphabet);
        skipEvents.removeAll(preservedEvents);

        Set<Location> locSet;
        locSet = AutomatonHelper.expandLocations(set(aut.initial), null, skipEvents);
        Origin org = new LocationSetOrigin(locSet);
        Location loc = new Location(result, org);
        loc.marked = AutomatonHelper.hasMarkedLocation(locSet);
        locMap.put(locSet, loc);
        notDone.add(locSet);
        result.setInitial(loc);
        while (!notDone.isEmpty()) {
            locSet = notDone.poll();
            loc = locMap.get(locSet);
            for (Event evt: preservedEvents) {
                Set<Location> destSet;
                destSet = AutomatonHelper.expandLocations(locSet, evt, skipEvents);
                if (destSet.isEmpty()) {
                    continue;
                }
                Location dest = locMap.get(destSet);
                if (dest == null) {
                    Origin destOrg = new LocationSetOrigin(destSet);
                    dest = new Location(result, destOrg);
                    dest.marked = AutomatonHelper.hasMarkedLocation(destSet);
                    locMap.put(destSet, dest);
                    notDone.add(destSet);
                }
                Edge.addEdge(evt, loc, dest);
            }
        }
        return result;
    }
}
