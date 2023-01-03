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

import java.util.Iterator;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;

/** Basic automaton, providing iteration capability over its locations. */
public class Automaton implements Iterable<Location> {
    /** Kind of automaton. By default {@link AutomatonKind#UNKNOWN}. */
    public AutomatonKind kind = AutomatonKind.UNKNOWN;

    /**
     * Name of the automaton, by default "&lt;none&gt;". Automata loaded from a CIF specification have the absolute name
     * of the corresponding CIF automaton, with escaping of CIF keywords.
     */
    public String name = "<none>";

    /** Initial state, may be {@code null} meaning no initial location exists. */
    public Location initial = null;

    /** Alphabet of the automaton. */
    public final Set<Event> alphabet;

    /**
     * Locations of the automaton (points to the first location, and uses the {@link Location#nextLoc} and
     * {@link Location#prevLoc} double linked list. An automaton without locations is represented with {@code null}
     * here.
     */
    public Location locations = null;

    /** Reference to the last added location. */
    private Location lastLocation = null;

    /**
     * Constructor of the {@link Automaton} class.
     *
     * @param alphabet Alphabet of the automaton.
     */
    public Automaton(Set<Event> alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * Get an iterator to get the locations of the automaton.
     *
     * @return Iterator over the locations of the automaton.
     */
    @Override
    public Iterator<Location> iterator() {
        return new LocationIterator(locations);
    }

    /**
     * Set a location as initial location, or use {@code null} to remove the initial location.
     *
     * @param initial Location to set as initial location, {@code null} to remove the initial location.
     */
    public void setInitial(Location initial) {
        Assert.check(this.initial == null || initial == null);
        this.initial = initial;
    }

    /**
     * Count the number of locations in the automaton (O(n) in locations).
     *
     * @return Number of locations.
     */
    public int size() {
        int count = 0;
        Location loc = locations;
        while (loc != null) {
            count++;
            loc = loc.nextLoc;
        }
        return count;
    }

    /**
     * Returns the number of locations and edges in the automaton.
     *
     * @return The number of locations and edges in the automaton.
     */
    public int[] getLocEdgeCounts() {
        int locCount = 0;
        int edgeCount = 0;
        Location loc = locations;
        while (loc != null) {
            locCount++;
            Edge edge = loc.outgoingEdges;
            while (edge != null) {
                edgeCount++;
                edge = edge.nextOutgoing;
            }
            loc = loc.nextLoc;
        }
        return new int[] {locCount, edgeCount};
    }

    /**
     * Register a location as belonging to the automaton. Should only be called from a new location.
     *
     * @param loc Location to register.
     */
    public void registerLocation(Location loc) {
        if (lastLocation == null) {
            locations = loc;
        } else {
            lastLocation.nextLoc = loc;
            loc.prevLoc = lastLocation;
        }
        lastLocation = loc;
    }

    /**
     * Remove a location from the automaton.
     *
     * @param loc Location to remove.
     */
    public void removeLocation(Location loc) {
        Assert.check(initial != loc); // Cannot remove the initial state.
        if (locations == loc) {
            locations = loc.nextLoc;
        }
        if (lastLocation == loc) {
            lastLocation = loc.prevLoc;
        }
        loc.removeInternal();
    }

    /** Iterator class for iterating over the locations of the automaton. */
    public final class LocationIterator implements Iterator<Location> {
        /** Next location to return. */
        private Location loc;

        /**
         * Constructor of the {@link LocationIterator} class.
         *
         * @param loc First location of the automaton.
         */
        public LocationIterator(Location loc) {
            this.loc = loc;
        }

        @Override
        public boolean hasNext() {
            return loc != null;
        }

        @Override
        public Location next() {
            Location lc = loc;
            loc = loc.nextLoc;
            return lc;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Is the automaton empty?
     *
     * @return {@code true} if the automaton has no locations, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return locations == null;
    }

    /** Remove all locations from the automaton. */
    public void clear() {
        locations = null;
        lastLocation = null;
        initial = null;
    }

    /**
     * Check whether the automaton has a marked location.
     *
     * @return {@code true} if the automaton has a marked location, {@code false} otherwise.
     */
    public boolean hasMarkedLoc() {
        for (Location loc: this) {
            if (loc.marked) {
                return true;
            }
        }
        return false;
    }
}
