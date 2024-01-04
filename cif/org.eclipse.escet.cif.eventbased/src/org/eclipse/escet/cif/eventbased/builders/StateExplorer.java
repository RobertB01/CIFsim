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

package org.eclipse.escet.cif.eventbased.builders;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.ApplicationException;

/**
 * Helper class to iteratively explore states of a combined set of automata.
 *
 * <p>
 * The {@link StateEdges} class constructs new {@link State} objects without notifying anyone. This helper class assists
 * with the exploration process, by creating starting state(s), and storing states found during the exploration.
 * </p>
 *
 * <p>
 * {@link #destLocs} contains states that are already seen. The map associates some user-defined information with each
 * state, to use when the state is encountered again. The {@link #hasValue} method can be used to query existence of a
 * state, while {@link #getValue} is used to get the associated value. The latter can also be used to detect existence
 * if {@code null} is not used as stored value. {@link #addValue} sets a new value for a state.
 * </p>
 *
 * <p>
 * {@link #unfinished} is a queue containing states that need further exploration. This queue is filled with states
 * added through {@link #addValue}. The iterator retrieves the added states, until the queue becomes empty.
 * </p>
 *
 * <p>
 * Expected use is to use the {@link #makeInitialState} to construct the first state (may be {@code null} if there is no
 * initial state). This state should be added, together with its associated value. Once that is done, the exploration
 * process can be performed by iterating over this object.
 * </p>
 *
 * <p>
 * For ease of referencing, {@link #automs} and {@link #destAlphabet} are the collection of input automata and the
 * combined alphabet. {@link #edgeBuilder} can be used to explore a state, and obtain its successor states.
 * </p>
 *
 * @param <V> Class to use as value in the {@link #destLocs} map, used to store new information for each source state.
 */
public class StateExplorer<V> implements Iterable<State> {
    /** Source automata. */
    public final List<Automaton> automs;

    /** Destination alphabet. */
    public final Set<Event> destAlphabet;

    /** Mapping of source state to value in the destination. */
    protected Map<State, V> destLocs = map();

    /** Edge builder. */
    public EdgeBuilder edgeBuilder;

    /** States which are not yet fully explored. */
    protected Queue<State> unfinished = new ArrayDeque<>();

    /**
     * Constructor for the {@link StateExplorer} class.
     *
     * @param automs Originating automata.
     */
    public StateExplorer(List<Automaton> automs) {
        Assert.check(!automs.isEmpty());
        this.automs = automs;

        destAlphabet = getCombinedAlphabet();
        edgeBuilder = new EdgeBuilder(automs, destAlphabet);
    }

    /**
     * Associate a value with a location.
     *
     * @param state State to use.
     * @param value Value to add.
     * @param checkNew Verify whether the location had a previous value (does not work for previously added {@code null}
     *     values).
     */
    public void addValue(State state, V value, boolean checkNew) {
        V old = destLocs.put(state, value);
        unfinished.add(state);
        if (!checkNew || old != null) {
            return;
        }
        throw new ApplicationException("Double assignment to a state.");
    }

    /**
     * Does the given state have a value associated with it?
     *
     * @param state State to query.
     * @return Whether the state has a value.
     */
    public boolean hasValue(State state) {
        return destLocs.containsKey(state);
    }

    /**
     * Get the value associated with a state (or {@code null}).
     *
     * @param state State to query.
     * @return {@code null} if the state has no associated value, or the stored value (which may also be {@code null}).
     */
    public V getValue(State state) {
        return destLocs.get(state);
    }

    /**
     * Compute initial state of the destination automaton.
     *
     * @return The initial state of the destination automaton, or {@code null} if there is no initial state.
     */
    public State makeInitialState() {
        Location[] locs = new Location[automs.size()];
        for (int i = 0; i < automs.size(); i++) {
            locs[i] = automs.get(i).initial;
            if (locs[i] == null) {
                return null;
            }
        }
        return new State(locs);
    }

    /**
     * Compute and return the alphabet of the combined source automata by merging their alphabets.
     *
     * @return The alphabet of the combined source automata.
     */
    private Set<Event> getCombinedAlphabet() {
        Set<Event> destAlphabet = set();
        for (Automaton aut: automs) {
            destAlphabet.addAll(aut.alphabet);
        }
        return destAlphabet;
    }

    /** Iterator over unexplored states. */
    @Override
    public Iterator<State> iterator() {
        return new UnfinishedIterator();
    }

    /**
     * Iterate over unexplored state combinations from the source automata (that is, the {@link #unfinished} variable of
     * the parent class).
     *
     * <p>
     * Note that the list is modified while iterating.
     * </p>
     */
    private final class UnfinishedIterator implements Iterator<State> {
        /** Constructor of the {@link UnfinishedIterator} class. */
        public UnfinishedIterator() {
            // Nothing to do.
        }

        @Override
        public boolean hasNext() {
            return !unfinished.isEmpty();
        }

        @Override
        public State next() {
            return unfinished.remove();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
