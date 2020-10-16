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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;

/**
 * Class containing all information to construct edges in the destination automaton for an event.
 *
 * <p>
 * TODO: This class seems to be just an extension of {@link CombinedEdges}. Perhaps they should be combined?
 * </p>
 */
public class StateEdges implements Iterable<State> {
    /** Constructor of the {@link StateEdges} class. */
    public StateEdges() {
        // Nothing to do, the owner of the object initializes the fields.
    }

    /** Event used at the edges. */
    public Event event;

    /** Combined locations in the source automata. */
    public State srcState;

    /** Edges in the source automata. */
    public CombinedEdges combinedEdges;

    /**
     * Does the event have any target states at all?
     *
     * @return Whether an edge for this event exists in the destination automaton.
     */
    public boolean edgePossible() {
        return combinedEdges.disabledIndex() < 0;
    }

    /**
     * At which automaton gets the event disabled?
     *
     * @return Lowest index of the automaton participating in the event that disables it, or {@code -1} if it is not
     *     disabled.
     */
    public int disabledIndex() {
        return combinedEdges.disabledIndex();
    }

    /**
     * Get the destination states reachable with edges labeled with {@link #event}.
     *
     * @return Iterator over the destination states of the edges.
     */
    @Override
    public Iterator<State> iterator() {
        return new StateIterator();
    }

    /**
     * Iterator that generates destination states of the edges labeled with {@link #event}.
     *
     * <p>
     * The target location of automata that do not participate in the event is the same as the source location. The
     * target location of the automata that do participate, changes with the selected edge of each automaton.
     * </p>
     *
     * <p>
     * This iterator generates destination states for all combinations of edges of the participating automata. The first
     * time it uses the first edge of all automata that participate. The second time, it only changes the edge of the
     * last automaton (it uses the second edge of the last automaton instead), the third time the last edge is changed
     * again, etc, until the edges of the last automaton have been exhausted. At that point, it switches to the second
     * edge of the second to last automaton, and resets the edges of the last automaton back to the first edge. In this
     * way, it generates the destination state for all combinations of edges.
     * </p>
     *
     * <p>
     * The iterator ends when the edges of the first automaton have been exhausted.
     * </p>
     */
    private class StateIterator implements Iterator<State> {
        /** Current index in the edges of each automaton. */
        int[] indices;

        /** Constructor of the {@link StateIterator} class. */
        public StateIterator() {
            indices = new int[combinedEdges.sourceEdges.size()];
            // Counters are all 0, ready for the first destination state.
            // However, if the event is not possible, it should end
            // immediately ({@link #hasNext} should return {@code false}).
            int first = -1;
            int sizeFirst = 0; // Dummy assignment to keep Java happy.
            for (int i = 0; i < indices.length; i++) {
                List<Edge> autEdge = combinedEdges.sourceEdges.get(i);
                if (autEdge == null) {
                    continue;
                }
                if (first == -1) { // Keep the first non-null entry.
                    first = i;
                    sizeFirst = autEdge.size();
                }
                if (autEdge.isEmpty()) { // Event is blocked.
                    // At least one participating automaton has no edges from
                    // its source location for this event. This means no
                    // destination states should be returned by this generator.
                    //
                    // Mark the entire iteration as 'finished' by setting the
                    // value of the current edge index of the first automaton
                    // to a value larger or equal than its number of edges.
                    indices[first] = sizeFirst;
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            for (int i = 0; i < indices.length; i++) {
                List<Edge> autEdge = combinedEdges.sourceEdges.get(i);
                if (autEdge != null) {
                    return indices[i] < autEdge.size();
                }
            }
            return false;
        }

        @Override
        public State next() {
            // Construct current state.
            Location[] locs = new Location[indices.length];
            for (int i = 0; i < indices.length; i++) {
                List<Edge> autEdge = combinedEdges.sourceEdges.get(i);
                if (autEdge == null) {
                    // Copy current location of non-participating automaton.
                    locs[i] = srcState.locs[i];
                } else {
                    if (indices[i] == autEdge.size()) {
                        throw new NoSuchElementException();
                    }
                    // Copy destination of the currently selected edge.
                    locs[i] = autEdge.get(indices[i]).dstLoc;
                }
            }
            State s = new State(locs);

            // Advance to next state.
            int i = indices.length - 1;
            while (i >= 0) {
                List<Edge> autEdge = combinedEdges.sourceEdges.get(i);
                if (autEdge == null) { // Automaton does not participate.
                    i--;
                    continue;
                }
                indices[i]++;
                if (indices[i] == autEdge.size()) {
                    // Reached the end of edge list 'i'. Go up further.
                    i--;
                    continue;
                } else {
                    // Increment succeeded. Reset other counters.
                    i++;
                    while (i < indices.length) {
                        indices[i] = 0;
                        i++;
                    }
                    break;
                }
            }

            return s; // Return the constructed state.
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
