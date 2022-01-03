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

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;

/**
 * Helper class to construct edges in a destination automaton.
 *
 * <p>
 * Calling {@link #setupStateEdges} followed by (one or more times) calling {@link #getStateEdges} gives access to the
 * edges of the state, for each event. The method also returns disabled events, since a disabled event may be relevant
 * to a computation.
 * </p>
 */
public class EdgeBuilder {
    /** Edges storage for each event. */
    public Map<Event, CombinedEdges> edgeInfo;

    /** Same information as {@link #edgeInfo}, except accessible by index. */
    protected List<CombinedEdges> edgeConstructions;

    /** State used to setup {@link #edgeInfo}. */
    protected State state;

    /**
     * Constructor for the {@link EdgeBuilder} class.
     *
     * @param automs Automata being combined.
     * @param events Events in the alphabet of the target automaton.
     */
    public EdgeBuilder(List<Automaton> automs, Set<Event> events) {
        edgeInfo = mapc(events.size());
        edgeConstructions = listc(events.size());

        // Fill the edgeInfo and edgeConstructions, silently discarding
        // unused events.
        for (Event evt: events) {
            for (Automaton aut: automs) {
                if (aut.alphabet.contains(evt)) {
                    CombinedEdges eci = new CombinedEdges(evt, automs);
                    edgeConstructions.add(eci);
                    edgeInfo.put(evt, eci);
                    break;
                }
            }
        }
    }

    /**
     * Fill the {@link #edgeInfo} with information to traverse from the given source state.
     *
     * @param state Combined locations of the source automata to compute edges for.
     */
    public void setupStateEdges(State state) {
        clearEdgeInfo();

        for (int li = 0; li < state.locs.length; li++) {
            Location loc = state.locs[li];
            Edge edge = loc.outgoingEdges;
            while (edge != null) {
                CombinedEdges ce = edgeInfo.get(edge.event);
                ce.sourceEdges.get(li).add(edge);
                edge = edge.nextOutgoing;
            }
        }
        this.state = state;
    }

    /**
     * Get the iterator for expanding the outgoing state edges previously set up with {@link #setupStateEdges}.
     *
     * @return Iterator to walk over the outgoing (combined) state edges.
     */
    public Iterable<StateEdges> getStateEdges() {
        return new EdgeBuilderIterator();
    }

    /** Clear the stored edge information. */
    protected void clearEdgeInfo() {
        for (CombinedEdges ce: edgeInfo.values()) {
            ce.clear();
        }
    }

    /** Iterator class to walk over events from a state. */
    private class EdgeBuilderIterator implements Iterable<StateEdges>, Iterator<StateEdges> {
        /** Object to return to the user. */
        private StateEdges res;

        /**
         * Next index in {@link #edgeConstructions} to return. Contains length of the {@link #edgeConstructions} list to
         * denote there are no next entries to return.
         */
        int next;

        /** Constructor of the {@link EdgeBuilderIterator} class. */
        public EdgeBuilderIterator() {
            res = new StateEdges();
            next = 0;
        }

        @Override
        public boolean hasNext() {
            return next < edgeConstructions.size();
        }

        @Override
        public StateEdges next() {
            int index = next;
            next = index + 1; // Update the 'next' counter.

            if (index >= edgeConstructions.size()) {
                throw new NoSuchElementException();
            }

            CombinedEdges ce = edgeConstructions.get(index);
            res.event = ce.event;
            res.combinedEdges = ce;
            res.srcState = state;
            return res;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<StateEdges> iterator() {
            return this;
        }
    }
}
