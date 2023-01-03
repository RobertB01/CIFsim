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

package org.eclipse.escet.cif.eventbased.automata;

import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Numbers.formatNumber;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpInterface;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidModelException;

/** Several routines to inspect or operate at an automaton. */
public class AutomatonHelper {
    /** Constructor of the {@link AutomatonHelper} class. */
    private AutomatonHelper() {
        // Static class.
    }

    /**
     * Check whether an automaton is deterministic. If not, provide a counter example.
     *
     * <p>
     * Code assumes that all locations of the automaton are used. Technically, we could allow non-deterministic edges
     * from locations that are not used in the computation. However, beforehand, it is hard to determine which locations
     * are not used (except non-reachable locations, but such locations are not common).
     * </p>
     *
     * @param aut Automaton to check.
     * @return {@code null} when the automaton is deterministic, else a counter-example in the form of an event at a
     *     location that leads to a non-deterministic location.
     */
    private static EventAtLocation checkDeterministic(Automaton aut) {
        // Set up data structures for non-determinism detection.
        // seenEvent keeps track of seen events at a location,
        // eventMap provides fast access into seenEvent.
        boolean[] seenEvent = new boolean[aut.alphabet.size()];
        Map<Event, Integer> eventMap = mapc(aut.alphabet.size());
        int idx = 0;
        for (Event evt: aut.alphabet) {
            eventMap.put(evt, idx);
            idx++;
        }

        for (Location loc: aut) {
            Arrays.fill(seenEvent, false);
            for (Edge edge: loc.getOutgoing()) {
                // Check event.
                idx = eventMap.get(edge.event);
                if (seenEvent[idx]) {
                    return new EventAtLocation(loc, edge.event);
                }
                seenEvent[idx] = true;
                // Expand locations.
            }
        }
        return null;
    }

    /**
     * Verify that the provided automaton is deterministic. If not, fail with a counter example.
     *
     * @param aut Automaton to check.
     */
    public static void reportNonDeterministic(Automaton aut) {
        EventAtLocation el = checkDeterministic(aut);
        if (el != null) {
            String msg = fmt("Unsupported automaton \"%s\": %s has several outgoing edges for event \"%s\", only "
                    + "deterministic automata are supported.", aut.name, el.loc.toString(), el.evt.name);
            throw new InvalidInputException(msg);
        }
    }

    /**
     * Expand the given set of locations traveling backward over edges.
     *
     * @param locs Initial set of locations to expand. Set is modified in-place.
     * @param notDone If specified, only locations from this set are considered as starting point for further expansion.
     *     Normally, this queue contains all locations. Queue is modified in-place and will be empty afterwards. If
     *     {@code null}, a queue is constructed locally with all locations from 'locs' in it.
     * @param allowed If specified, only edges with events from this set may be used to expand the locations. If
     *     {@code null} all edges may be used.
     * @return The resulting set of locations after expansion. This information is also available in the 'locs' set
     *     initially provided.
     */
    public static Set<Location> expandStatesBackward(Set<Location> locs, Queue<Location> notDone, Set<Event> allowed) {
        if (notDone == null) {
            int sz = locs.size();
            if (sz < 1000) {
                sz = 1000;
            }
            notDone = new ArrayDeque<>(sz);
            notDone.addAll(locs);
        }

        if (allowed == null) {
            // Allow all edges.
            while (!notDone.isEmpty()) {
                Location loc = notDone.remove();
                for (Edge e: loc.getIncoming()) {
                    if (locs.add(e.srcLoc)) {
                        notDone.add(e.srcLoc);
                    }
                }
            }
            return locs;
        } else {
            // Expand while checking for the event.
            // TODO Handle single event as a special case?
            while (!notDone.isEmpty()) {
                Location loc = notDone.remove();
                for (Edge e: loc.getIncoming()) {
                    if (!allowed.contains(e.event)) {
                        continue;
                    }
                    if (locs.add(e.srcLoc)) {
                        notDone.add(e.srcLoc);
                    }
                }
            }
            return locs;
        }
    }

    /**
     * Expand the given set of locations traveling forward over edges.
     *
     * @param locs Initial set of locations to expand. Set is modified in-place.
     * @param notDone If specified, only locations from this set are considered as starting point for further expansion.
     *     Normally, this queue contains all locations. Queue is modified in-place and will be empty afterwards. If
     *     {@code null}, a queue is constructed locally with all locations from 'locs' in it.
     * @param allowed If specified, only edges with events from this set may be used to expand the locations. If
     *     {@code null} all edges may be used.
     * @return The resulting set of locations after expansion. This information is also available in the 'locs' set
     *     initially provided.
     */
    public static Set<Location> expandStatesForward(Set<Location> locs, Queue<Location> notDone, Set<Event> allowed) {
        if (notDone == null) {
            int sz = locs.size();
            if (sz < 1000) {
                sz = 1000;
            }
            notDone = new ArrayDeque<>(sz);
            notDone.addAll(locs);
        }

        if (allowed == null) {
            // Allow all edges.
            while (!notDone.isEmpty()) {
                Location loc = notDone.remove();
                for (Edge e: loc.getOutgoing()) {
                    if (locs.add(e.dstLoc)) {
                        notDone.add(e.dstLoc);
                    }
                }
            }
            return locs;
        } else {
            // Expand while checking for the event.
            // TODO Handle single event as a special case?
            while (!notDone.isEmpty()) {
                Location loc = notDone.remove();
                for (Edge e: loc.getOutgoing()) {
                    if (!allowed.contains(e.event)) {
                        continue;
                    }
                    if (locs.add(e.dstLoc)) {
                        notDone.add(e.dstLoc);
                    }
                }
            }
            return locs;
        }
    }

    /**
     * Check whether the automaton is trim (all states reachable and coreachable).
     *
     * @param aut Automaton to examine.
     * @return Whether the automaton is trim.
     */
    public static boolean trimCheck(Automaton aut) {
        Set<Location> locs = setc(10000);
        int count = getNonCoreachableCount(aut, locs);
        if (count != 0) {
            return false;
        }
        count = locs.size(); // Number of locations in the automaton.
        locs = getReachables(aut);
        return locs.size() == count;
    }

    /**
     * Make the automaton trim.
     *
     * <p>
     * Remove all non-reachable and non-coreachable states of the automaton (in-place).
     * </p>
     *
     * <p>
     * TODO: Location removal in an automaton is currently quite expensive (it constructs maps with neighboring
     * locations and edges). Performance of this operation can be improved by making location removal faster.
     * </p>
     *
     * @param aut Automaton to make trim (modified in-place).
     */
    public static void computeTrim(Automaton aut) {
        Set<Location> coreachables = setc(10000);
        int count = getNonCoreachableCount(aut, coreachables);
        boolean isTrim = (count == 0);
        count += coreachables.size(); // Number of locations in the automaton.

        Set<Location> reachables = getReachables(aut);
        isTrim &= (reachables.size() == count);

        if (!isTrim) {
            Location loc = aut.locations;
            while (loc != null) {
                Location next = loc.nextLoc;
                if (!coreachables.contains(loc) || !reachables.contains(loc)) {
                    if (loc == aut.initial) {
                        // Removing initial state: Game over.
                        String msg = "Initial state is not coreachable, trimming results in an empty automaton.";
                        throw new InvalidModelException(msg);
                    }
                    aut.removeLocation(loc);
                }
                loc = next;
            }
        }
    }

    /**
     * Find coreachable locations and the number of non-coreachable locations in an automaton.
     *
     * @param aut Automaton to search.
     * @param coreachables Coreachable locations of the automaton, modified in-place.
     * @return The number of non-coreachable locations in the automaton.
     */
    public static int getNonCoreachableCount(Automaton aut, Set<Location> coreachables) {
        Queue<Location> toExpand = new ArrayDeque<>(1000);
        coreachables.clear();

        // Add all marked states.
        int locCount = 0;
        for (Location loc = aut.locations; loc != null; loc = loc.nextLoc) {
            locCount++;
            if (loc.marked) {
                toExpand.add(loc);
                coreachables.add(loc);
            }
        }

        // Expand the marked states to all coreachable states.
        while (!toExpand.isEmpty()) {
            Location loc = toExpand.remove();
            for (Edge e: loc.getIncoming()) {
                if (coreachables.add(e.srcLoc)) {
                    toExpand.add(e.srcLoc);
                }
            }
        }
        return locCount - coreachables.size();
    }

    /**
     * Remove the non-reachable part of an automaton.
     *
     * <p>
     * TODO: Location removal in an automaton is currently quite expensive (it constructs maps with neighboring
     * locations and edges). Performance of this phase can be improved by making location removal faster.
     * </p>
     *
     * @param aut Automaton to prune.
     * @param synDump Object dumping information about the performed synthesis, or {@code null} if no information needs
     *     to be saved.
     */
    public static void removeNonReachables(Automaton aut, SynthesisDumpInterface synDump) {
        if (aut.initial == null) {
            // Automaton without initial location.
            aut.clear();
            return;
        }

        Set<Location> reachables = getReachables(aut);
        removeExcludedLocations(aut, reachables, synDump);
    }

    /**
     * Find reachable locations of an automaton.
     *
     * @param aut Automaton to search.
     * @return The reachable locations in the automaton.
     */
    public static Set<Location> getReachables(Automaton aut) {
        Queue<Location> toExpand = new ArrayDeque<>(1000);
        Set<Location> reachables = setc(10000);

        // Add initial location if it exists.
        if (aut.initial != null) {
            toExpand.add(aut.initial);
            reachables.add(aut.initial);
        }

        // Expand the initial location to reachable locations.
        while (!toExpand.isEmpty()) {
            Location loc = toExpand.remove();
            for (Edge e: loc.getOutgoing()) {
                if (reachables.add(e.dstLoc)) {
                    toExpand.add(e.dstLoc);
                }
            }
        }
        return reachables;
    }

    /**
     * Remove all locations not in 'locs' from the automaton.
     *
     * @param aut Automaton to prune.
     * @param locs Locations to keep.
     * @param synDump Object dumping information about the performed synthesis, or {@code null} if no information needs
     *     to be saved.
     */
    private static void removeExcludedLocations(Automaton aut, Set<Location> locs, SynthesisDumpInterface synDump) {
        Location loc = aut.locations;
        while (loc != null) {
            if (locs.contains(loc)) {
                loc = loc.nextLoc;
                continue;
            }
            Location locDel = loc;
            loc = loc.nextLoc;
            if (synDump != null) {
                synDump.nonReachableLocation(locDel);
            }
            aut.removeLocation(locDel);
        }
    }

    /**
     * Expand a set of locations.
     *
     * <p>
     * If 'firstEvent' is not {@code null}, the starting set of locations becomes all locations that have an incoming
     * edge from 'locs' using the event 'firstEvent'. If 'firstEvent' is {@code null}, the starting set is 'locs'.
     * </p>
     *
     * <p>
     * The starting set of locations is expanded with all locations that can be reached by performing zero or more
     * events in 'events'.
     * </p>
     *
     * @param locs Set of starting locations.
     * @param firstEvent If not {@code null}, first take an edge with this event.
     * @param events Set of events that may be taken.
     * @return The expanded set locations.
     */
    public static Set<Location> expandLocations(Set<Location> locs, Event firstEvent, Set<Event> events) {
        Queue<Location> notDone = new ArrayDeque<>(1000);
        Set<Location> reachables = setc(2 * locs.size());

        if (firstEvent != null) {
            for (Location loc: locs) {
                for (Edge e: loc.getOutgoing(firstEvent)) {
                    if (reachables.add(e.dstLoc)) {
                        notDone.add(e.dstLoc);
                    }
                }
            }
        } else {
            notDone.addAll(locs);
            reachables.addAll(locs);
        }

        if (events.isEmpty() || reachables.isEmpty()) {
            return reachables;
        }

        while (!notDone.isEmpty()) {
            Location loc = notDone.poll();
            for (Edge e: loc.getOutgoing()) {
                if (events.contains(e.event) && !reachables.contains(e.dstLoc)) {
                    reachables.add(e.dstLoc);
                    notDone.add(e.dstLoc);
                }
            }
        }
        return reachables;
    }

    /**
     * Is a collection of locations marked?
     *
     * @param locs Locations to examine.
     * @return Whether at least one location from 'locs' is a marked location.
     */
    public static boolean hasMarkedLocation(Set<Location> locs) {
        for (Location loc: locs) {
            if (loc.marked) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the statistics (counts) of the automaton as human-readable text.
     *
     * @param aut The automaton.
     * @return The human-readable text.
     */
    public static Object getAutStatistics(Automaton aut) {
        int[] statistics = aut.getLocEdgeCounts();
        String locTxt = (statistics[0] == 1) ? "1 location" : fmt("%s locations", formatNumber(statistics[0]));
        String edgeTxt = (statistics[1] == 1) ? "1 edge" : fmt("%s edges", formatNumber(statistics[1]));
        return locTxt + ", " + edgeTxt;
    }

    /**
     * Try to split the automata in groups, where the events of one group are disjunct from the events of another group.
     *
     * <p>
     * Finding a disjunct sub-set is to start a group with any automaton, and repeatedly add other automata with overlap
     * in the alphabet with respect to the union of the alphabets of the automata already added. Either all automata get
     * added, which leads to the conclusion there is no subset, or some automata are not added, which leads to the
     * conclusion it is possible to split the set automata between 'added' and 'not added' at least. The 'not added'
     * automata may have further disjunct groups (which is investigated next).
     * </p>
     *
     * @param auts Automata to split into groups.
     * @return Groups of automata with disjunct event collections. Note that the method will always return at least one
     *     group.
     */
    public static List<List<Automaton>> findDisjunctGroups(List<Automaton> auts) {
        List<List<Automaton>> groups = list();

        auts = copy(auts); // Make shallow copy of the list so it can be changed.
        int avail = auts.size();
        while (avail > 0) {
            // Make a new group, starting with any automaton.
            Automaton aut = auts.get(avail - 1);
            Set<Event> groupAlphabet = copy(aut.alphabet);
            List<Automaton> group = listc(avail);
            group.add(aut);
            avail--;

            // Add new automata with overlapping alphabet to the group, until
            // all automata have been added, or until no overlap exists.
            while (avail > 0) {
                boolean progress = false;
                int index = 0;
                while (index < avail) {
                    aut = auts.get(index);
                    if (isEmptyIntersection(groupAlphabet, aut.alphabet)) {
                        index++;
                    } else {
                        groupAlphabet.addAll(aut.alphabet);
                        group.add(aut);
                        if (index < avail - 1) {
                            auts.set(index, auts.get(avail - 1));
                        }
                        avail--;
                        progress = true;
                    }
                }
                if (!progress) {
                    break;
                }
            }

            // Created a disjunct group, add it to the other groups.
            groups.add(group);
        }
        return groups;
    }
}
