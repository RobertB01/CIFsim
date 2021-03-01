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

package org.eclipse.escet.cif.eventbased;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.automata.origin.LocationSetOrigin;
import org.eclipse.escet.cif.eventbased.automata.origin.Origin;
import org.eclipse.escet.cif.eventbased.partitions.Partition;
import org.eclipse.escet.cif.eventbased.partitions.PartitionRefinement;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/**
 * Perform automaton abstraction to an abstracted alphabet by partitioning.
 *
 * <p>
 * The procedure first refines the partitioning {markerStates, nonMarkerStats} using the Ω(Σ', V<sub>0</sub>) function.
 * In the resulting automaton, the alphabet is the set of observable events, and each refined partition becomes a
 * location in the abstraction.
 * </p>
 *
 * <p>
 * An edge with an observable event in the abstracted automaton exists, if there is a path from a location in the source
 * partition to a location in the destination partition, containing one single edge with the same observable event, and
 * zero or more non-observable events before and/or after it.
 * </p>
 *
 * <p>
 * In the implementation the latter step is implemented using the f function code, except you add incoming edges instead
 * of outgoing edges (since the f code expands locations backward).
 * </p>
 */
public class AutomatonAbstraction extends PartitionRefinement {
    /** Mapping of resulting partitions to locations in the abstracted automaton. */
    private Map<Partition, Location> partitionMap = map();

    /** Automaton to abstract. */
    private final Automaton aut;

    /** Resulting abstracted automaton. */
    private final Automaton newAut;

    /**
     * Construct initial partitions for automaton abstraction (namely the set of marked locations, and the set of
     * non-marked locations).
     *
     * @param aut Automaton containing the locations.
     * @return Initial partitions for the automaton abstraction calculation.
     */
    private static Set<Set<Location>> makePartitions(Automaton aut) {
        Set<Location> markers = set();
        Set<Location> nonMarkers = set();

        for (Location loc = aut.locations; loc != null; loc = loc.nextLoc) {
            if (loc.marked) {
                markers.add(loc);
            } else {
                nonMarkers.add(loc);
            }
        }
        Set<Set<Location>> partitions = set();
        partitions.add(markers);
        partitions.add(nonMarkers);
        return partitions;
    }

    /**
     * Constructor code to create a set of non-observable events from an automaton and a set of observable events.
     *
     * @param aut Automaton containing the complete alphabet.
     * @param observables The set observable events.
     * @return The set of non-observable events in the alphabet.
     */
    private static Set<Event> makeNonObservables(Automaton aut, Set<Event> observables) {
        // This is set difference, except it does not modify aut.alphabet.
        int size = aut.alphabet.size() - observables.size();
        if (size <= 0) {
            size = 8;
        }
        Set<Event> nonObservables = setc(size);
        for (Event evt: aut.alphabet) {
            if (!observables.contains(evt)) {
                nonObservables.add(evt);
            }
        }
        return nonObservables;
    }

    /**
     * Constructor of the {@link AutomatonAbstraction} class.
     *
     * @param aut Automaton to abstract.
     * @param observables Set of observable events (to abstract to).
     */
    private AutomatonAbstraction(Automaton aut, Set<Event> observables) {
        super(observables, makeNonObservables(aut, observables), makePartitions(aut));
        this.aut = aut;
        this.newAut = new Automaton(observableEvents);
        this.newAut.kind = this.aut.kind;
    }

    /**
     * Verify whether the automaton and the set of events are valid for performing automaton abstraction. Generates a
     * fatal error if the input is not valid.
     *
     * @param aut Input automaton to abstract.
     * @param observables Set of observable events (to abstract to).
     */
    public static void automatonAbstractionPreCheck(Automaton aut, Set<Event> observables) {
        for (Event evt: observables) {
            if (aut.alphabet.contains(evt)) {
                continue;
            }
            String msg = fmt(
                    "Event \"%s\" is in the observable event set, but is not in the alphabet of the automaton.",
                    evt.name);
            throw new InvalidInputException(msg);
        }

        boolean seenMarked = false;
        boolean seenUnmarked = false;
        for (Location loc: aut) {
            if (loc.marked) {
                seenMarked = true;
                if (seenUnmarked) {
                    return;
                }
            } else {
                seenUnmarked = true;
                if (seenMarked) {
                    return;
                }
            }
        }
        // Missing marked or unmarked locations (or both, but we report only
        // one missing type in that case).
        String msg = seenMarked ? "unmarked" : "marked";
        msg = fmt("Automaton \"%s\" cannot be partitioned because it has no %s locations.", aut.name, msg);
        throw new InvalidInputException(msg);
    }

    /**
     * Perform automaton abstraction. Input should be correct for the pre-check ({@link #automatonAbstractionPreCheck}).
     *
     * @param aut Input automaton to abstract.
     * @param observables Set of observable events (to abstract to).
     * @return The abstracted automaton.
     */
    public static Automaton automatonAbstraction(Automaton aut, Set<Event> observables) {
        AutomatonAbstraction aa = new AutomatonAbstraction(aut, observables);
        aa.refinePartitions();
        Automaton rslt = aa.createAbstractedAutomaton();

        if (OutputProvider.dodbg()) {
            OutputProvider.dbg("Automaton abstraction finished (%s).", AutomatonHelper.getAutStatistics(rslt));
        }
        return rslt;
    }

    /**
     * Construct the abstracted automaton from the computed refined partitions.
     *
     * <p>
     * Note that edges are built backward, that is, from destination partition to source partition.
     * </p>
     *
     * @return The abstracted automaton.
     */
    private Automaton createAbstractedAutomaton() {
        // Incoming edges of these partitions are already done.
        Set<Partition> edgesDone = set();

        for (Location oldDst = aut.locations; oldDst != null; oldDst = oldDst.nextLoc) {
            Partition dstPart = locationMapping.get(oldDst).partition;
            // 'getLocation' should be called for every original location.
            Location dstLoc = getLocation(oldDst, dstPart);
            if (edgesDone.contains(dstPart)) {
                continue;
            }

            Set<Location> oldDstLocs;
            oldDstLocs = f1(dstPart); // Expand partition backward.

            Set<Location> oldSrcLocs = set();
            for (Event evt: observableEvents) {
                // Keep track which source partitions already have an edge.
                Set<Partition> srcParts = set();
                for (Location oldSrc: f2(oldDstLocs, evt, oldSrcLocs)) {
                    Partition srcPart = locationMapping.get(oldSrc).partition;
                    if (srcParts.add(srcPart)) {
                        Location srcLoc = getLocation(null, srcPart);
                        Edge.addEdge(evt, srcLoc, dstLoc);
                    }
                }
            }
            edgesDone.add(dstPart);
        }
        return newAut;
    }

    /**
     * Get or construct a location in the new automaton using the previously computed refined partitions. It is assumed
     * this function is called for all locations in the original automaton (for setting the properties of the locations
     * in the new automaton).
     *
     * @param oldLoc Location in the original automaton. May be {@code null} if the location in the 'partitionMap' does
     *     not need to be updated from an location.
     * @param p The partition containing 'oldLoc'.
     * @return Location representing the partition in the abstracted automaton.
     */
    private Location getLocation(Location oldLoc, Partition p) {
        Location newLoc = partitionMap.get(p);
        if (newLoc == null) {
            Origin org = new LocationSetOrigin(p.getLocations());
            newLoc = new Location(newAut, org);
            partitionMap.put(p, newLoc);
        }
        // Update the properties of the new location with properties of the
        // old location.
        if (oldLoc != null) {
            if (oldLoc.marked) {
                newLoc.marked = true;
            }
            if (aut.initial == oldLoc) {
                newAut.setInitial(newLoc);
            }
        }
        return newLoc;
    }
}
