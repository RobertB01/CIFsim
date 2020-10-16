//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Maps.map;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;

/**
 * Explorer of the locations of an automaton by traversing edges.
 *
 * <p>
 * This class only keeps track of visited locations and visited locations that need further expansion. Policies of
 * traversal and expansion are dealt with in the derived class.
 * </p>
 *
 * @param <V> Type of data associated with each location.
 */
public class LocationExplorer<V> implements Iterable<Location> {
    /** Source automaton. */
    public final Automaton autom;

    /** Mapping of source location to value in the destination. */
    private Map<Location, V> destLocs = map();

    /** Locations that are not yet fully explored. */
    protected Queue<Location> unfinished = new ArrayDeque<>();

    /**
     * Constructor of the {@link LocationExplorer} class.
     *
     * @param autom Automaton to explore.
     */
    public LocationExplorer(Automaton autom) {
        this.autom = autom;
    }

    /**
     * Add initial location as starting location.
     *
     * @param value Value associated with the initial location.
     */
    public void addInitialLocation(V value) {
        if (autom.initial != null) {
            destLocs.put(autom.initial, value);
            unfinished.add(autom.initial);
        }
    }

    /**
     * Add marked locations as starting locations.
     *
     * @param value Value associated with the marked locations.
     */
    public void addMarkedLocations(V value) {
        for (Location loc: autom) {
            if (loc.marked) {
                destLocs.put(loc, value);
                unfinished.add(loc);
            }
        }
    }

    /**
     * Associate a value with a location.
     *
     * @param loc Location to use.
     * @param value Value to add.
     * @param checkNew Verify whether the location had a previous value (does not work for previously added {@code null}
     *     values).
     */
    public void addValue(Location loc, V value, boolean checkNew) {
        V old = destLocs.put(loc, value);
        unfinished.add(loc);
        if (!checkNew || old != null) {
            return;
        }
        throw new ApplicationException("Double assignment to a location.");
    }

    /**
     * Does the given location have a value associated with it?
     *
     * @param loc Location to query.
     * @return Whether the location has a value.
     */
    public boolean locationHasValue(Location loc) {
        return destLocs.containsKey(loc);
    }

    /**
     * Get the value associated with a location (or {@code null}).
     *
     * @param loc Location to query.
     * @return {@code null} if the location has no associated value, or the stored value (which may also be
     *     {@code null}).
     */
    public V getValue(Location loc) {
        return destLocs.get(loc);
    }

    /** Iterator over unexplored locations. */
    @Override
    public Iterator<Location> iterator() {
        return new UnfinishedIterator();
    }

    /**
     * Iterate over unexplored locations from the source automaton (that is, the {@link #unfinished} variable of the
     * parent class).
     *
     * <p>
     * Note that the queue is modified while iterating.
     * </p>
     */
    final class UnfinishedIterator implements Iterator<Location> {
        @Override
        public boolean hasNext() {
            return !unfinished.isEmpty();
        }

        @Override
        public Location next() {
            return unfinished.remove();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
