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

package org.eclipse.escet.cif.eventbased.equivalence;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.common.java.Assert;

/** Data of the language equivalence command. */
public class LangEquivCalculation extends BlockPartitioner {
    /**
     * Constructor of the {@link LangEquivCalculation} class.
     *
     * @param automs Automata to check for language equivalence.
     */
    public LangEquivCalculation(List<Automaton> automs) {
        super(automs, true);
        Assert.check(automs.size() == 2);
    }

    /**
     * Check whether both automata provided in the constructor, have the same language using the block partitioning
     * algorithm by Kanellakis and Smolka.
     *
     * <p>
     * Language equivalence exists if the initial locations of both automata are in the same block.
     * </p>
     *
     * @return A counter example if one was found, else {@code null}.
     */
    public CounterExample checkLanguageEquivalence() {
        return performBlockPartitioning();
    }

    @Override
    protected CounterExample constructCounterExample(Block block, Event finalEvent) {
        List<Event> path = getReversePath(block); // Path is reversed.
        Collections.reverse(path);

        // Find the locations of both automata.
        Location[] locs = new Location[2];
        Location[] newLocs = new Location[2];

        locs[0] = automs.get(0).initial;
        locs[1] = automs.get(1).initial;

        for (int pathIdx = 0; pathIdx < path.size(); pathIdx++) {
            Event evt = path.get(pathIdx);
            newLocs[0] = getNextLocation(locs[0], evt);
            newLocs[1] = getNextLocation(locs[1], evt);

            if (newLocs[0] != null && newLocs[1] != null) {
                Location[] swap = locs;
                locs = newLocs;
                newLocs = swap;
                continue;
            }
            Assert.check(newLocs[0] != null || newLocs[1] != null);
            return new CounterExample(path.subList(0, pathIdx), locs, evt);
        }
        return new CounterExample(path, locs, finalEvent);
    }

    /**
     * Derive a path from locations in the block to the initial state.
     *
     * @param blk Block to walk from.
     * @return Path from the block to the initial state.
     */
    @SuppressWarnings("null")
    private List<Event> getReversePath(Block blk) {
        List<Event> reversePath = list(); // Derive path to initial location in reverse.

        BlockLocation best = null;
        for (BlockLocation bl: blk.locs) {
            if (best == null || bl.depth < best.depth) {
                best = bl;
            }
        }
        while (best.depth != 0) {
            BlockLocation bestPrev = null;
            Edge bestIncEdge = null;
            for (Edge incEdge: best.loc.getIncoming()) {
                BlockLocation prev = blockLocs.get(incEdge.srcLoc);
                if (bestPrev == null || bestPrev.depth > prev.depth) {
                    bestPrev = prev;
                    bestIncEdge = incEdge;
                }
            }
            reversePath.add(bestIncEdge.event);
            best = bestPrev;
        }
        return reversePath;
    }

    /**
     * Get the next location in the path.
     *
     * @param loc Current location in the path.
     * @param evt Event to perform.
     * @return Next location in the path, or {@code null} if the event cannot be taken.
     */
    private Location getNextLocation(Location loc, Event evt) {
        Iterator<Edge> iter = loc.getOutgoing(evt);
        if (!iter.hasNext()) {
            return null;
        }
        return iter.next().dstLoc;
    }
}
