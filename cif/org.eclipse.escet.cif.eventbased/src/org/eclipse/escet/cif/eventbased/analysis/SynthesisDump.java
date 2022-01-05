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

package org.eclipse.escet.cif.eventbased.analysis;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;

import java.io.DataOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.apps.conversion.CifOrigin;
import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.builders.State;
import org.eclipse.escet.common.java.Assert;

/** Dumper of what edges and states get removed during synthesis. */
public class SynthesisDump implements SynthesisDumpInterface {
    /** Number of source automata. */
    private int numAuts = -1;

    /** Source information, mapping of locations to its number, for each source automaton. */
    private List<Map<Location, Integer>> stateNumbers = null;

    /** Mapping of events to their number. */
    private Map<Event, Integer> eventNumbers = null;

    /** Number of each location in the result. */
    private Map<Location, Integer> locNumbers = map();

    /** Output file handle to the data file, or {@code null} if not open. */
    private DataOutputStream outHandle = null;

    /**
     * Constructor of the {@link SynthesisDump} class.
     *
     * @param filename Name of the file to dump to.
     */
    public SynthesisDump(String filename) {
        Assert.check(outHandle == null);
        outHandle = SynthesisDumpIO.openOutput(filename);
    }

    @Override
    public void close() {
        if (outHandle != null) {
            SynthesisDumpIO.writeEndOfData(outHandle);
            SynthesisDumpIO.close(outHandle);
        }
        outHandle = null;
    }

    @Override
    public boolean isFake() {
        return false;
    }

    /**
     * Get the name of a location without the automaton prefix.
     *
     * @param loc Location to query.
     * @return Name of the location in the automaton.
     */
    private String getLocationName(Location loc) {
        if (loc.origin instanceof CifOrigin) {
            CifOrigin cifOrigin = (CifOrigin)loc.origin;
            String name = cifOrigin.cifLoc.getName();
            return (name == null) ? "*" : name;
        }

        return loc.toString();
    }

    @Override
    public void storeAutomata(List<Automaton> auts, int numPlants) {
        this.numAuts = auts.size();

        // Write location information.
        List<AutomatonNamesInfo> sourceInfo = listc(numAuts);
        stateNumbers = listc(numAuts);

        for (Automaton aut: auts) {
            int locCount = aut.size();
            Map<Location, Integer> locMap = mapc(locCount);
            List<String> locNames = listc(locCount);

            // Add initial location.
            Assert.notNull(aut.initial);
            int locNumber = 0;
            locMap.put(aut.initial, locNumber);
            locNames.add(getLocationName(aut.initial));
            locNumber++;

            // Add other locations.
            for (Location loc: aut) {
                if (loc == aut.initial) {
                    continue;
                }

                locMap.put(loc, locNumber);
                locNames.add(getLocationName(loc));
                locNumber++;
            }

            sourceInfo.add(new AutomatonNamesInfo(aut.name, locNames));
            stateNumbers.add(locMap);
        }
        SynthesisDumpIO.writeSourceAutomataInformation(sourceInfo, numPlants, outHandle);

        // Write event information.
        Set<Event> eventSet = set();
        for (Automaton aut: auts) {
            eventSet.addAll(aut.alphabet);
        }

        // Fill eventNumbers variable.
        int index = 0;
        List<EventInfo> events = listc(eventSet.size());
        eventNumbers = mapc(eventSet.size());
        for (Event evt: eventSet) {
            eventNumbers.put(evt, index);
            events.add(new EventInfo(evt.name, evt.isControllable()));
            index++;
        }

        SynthesisDumpIO.writeEvents(events, outHandle);
    }

    @Override
    public void newLocation(State state, Location loc) {
        int[] srcLocs = new int[numAuts];
        for (int index = 0; index < state.locs.length; index++) {
            srcLocs[index] = stateNumbers.get(index).get(state.locs[index]);
        }

        int locIndex = locNumbers.size();
        Integer old = locNumbers.put(loc, locIndex);
        Assert.check(old == null);

        SynthesisDumpIO.writeNewLocation(srcLocs, locIndex, loc.marked, outHandle);
    }

    @Override
    public void newEdge(Event evt, Location src, Location dst) {
        int evtNum = eventNumbers.get(evt);
        int srcNum = locNumbers.get(src);
        int dstNum = locNumbers.get(dst);
        AddedEdgeInfo edgeInfo = new AddedEdgeInfo(srcNum, dstNum, evtNum);
        SynthesisDumpIO.writeNewEdge(edgeInfo, outHandle);
    }

    @Override
    public void disabledEvent(Location loc, Event evt, int autIndex) {
        int srcNumber = locNumbers.get(loc);
        int evtNumber = eventNumbers.get(evt);
        RemovedEdgeInfo edgeInfo = new RemovedEdgeInfo(srcNumber, autIndex, evtNumber, true);
        SynthesisDumpIO.writeRemovedEdge(edgeInfo, outHandle);
    }

    @Override
    public void removedDestination(Location loc, Event evt, Location dst) {
        int srcNumber = locNumbers.get(loc);
        int dstNumber = locNumbers.get(dst);
        int evtNumber = eventNumbers.get(evt);
        RemovedEdgeInfo edgeInfo = new RemovedEdgeInfo(srcNumber, dstNumber, evtNumber, false);
        SynthesisDumpIO.writeRemovedEdge(edgeInfo, outHandle);
    }

    @Override
    public void blockingLocation(Location loc) {
        int locNumber = locNumbers.get(loc);
        RemovedLocationInfo locInfo = new RemovedLocationInfo(locNumber, false);
        SynthesisDumpIO.writeRemovedLocation(locInfo, outHandle);
    }

    @Override
    public void nonCoreachableLocation(Location loc) {
        int locNumber = locNumbers.get(loc);
        RemovedLocationInfo locInfo = new RemovedLocationInfo(locNumber, true);
        SynthesisDumpIO.writeRemovedLocation(locInfo, outHandle);
    }
}
