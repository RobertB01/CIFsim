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
    protected List<SplitReason> getSplitReason(Location loc1, Location loc2) {
        // Get lowest block (depth) where these locations are a part of.
        Block block1 = blocks.get(blockLocs.get(loc1).blockNumber);
        Block block2 = blocks.get(blockLocs.get(loc2).blockNumber);

        // Check that the locations are indeed part of two different blocks.
        Assert.check(block1 != block2);

        // Get first common anchestor.
        while (block1.parent != block2.parent) {
            int maxDepth = Math.max(block1.depth, block2.depth);
            if (block1.depth == maxDepth) {
                block1 = block1.parent;
            }
            if (block2.depth == maxDepth) {
                block2 = block2.parent;
            }
        }

        // Found the first blocks where these two locations are no longer part of the same block. Find the reason why
        // these two blocks where created.
        List<SplitReason> followUpReasons = list();
        if (block1.parentReason.block != null && block2.parentReason.block != null) {
            // We found the reason. As these blocks both point to another block, the reason is not conclusive.
            Assert.check(block1.parentReason.event == block2.parentReason.event);

            // Get a location from the two blocks that are pointed towards.
            Event reasonEvent = block1.parentReason.event;
            Location nextLoc1 = getNextLocation(loc1, reasonEvent);
            Location nextLoc2 = getNextLocation(loc2, reasonEvent);

            // Figure out why the two new blocks differ.
            followUpReasons = getSplitReason(nextLoc1, nextLoc2);
        }

        // Return the reason and the follow-up reasoning.
        List<SplitReason> splitReasons = list();
        splitReasons.add(block1.parentReason);
        splitReasons.add(block2.parentReason);
        splitReasons.addAll(followUpReasons);
        return splitReasons;
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

            // Path found that exists in one automaton but not in the other.
            return new CounterExample(path.subList(0, pathIdx), locs, finalEvent);
        }

        // If no final event is given, the reason is already conclusive (because of markings).
        if (finalEvent == null) {
            return new CounterExample(path, locs, finalEvent);
        }

        // We found two equivalent locations that are part of different blocks. Figure out why.
        List<SplitReason> splitReason = getSplitReason(locs[0], locs[1]);

        // Get the intermediate splitting reasons. Add the events to the path and update the locations. Skip odd reasons
        // as the event for the even and odd reason must be the same.
        for (int i = 0; i < splitReason.size() - 2; i += 2) {
            Event evt = splitReason.get(i).event;
            path.add(evt);
            locs[0] = getNextLocation(locs[0], evt);
            locs[1] = getNextLocation(locs[1], evt);
        }

        // Get the last splitting reasons, these are conclusive.
        SplitReason lastReaon1 = splitReason.get(splitReason.size() - 2);
        SplitReason lastReaon2 = splitReason.get(splitReason.size() - 1);

        // Determine what the exact reason is.
        int numEventsEnabled = (lastReaon1.block == null ? 0 : 1) + (lastReaon2.block == null ? 0 : 1);
        switch (numEventsEnabled) {
            case 0:
                // both reasons don't point to a block. The reason must be because of markings.
                finalEvent = null;
                break;

            case 1:
                // One reason points to a block, the other doesn't. This means the event is enabled in one location, but
                // not in the other.
                finalEvent = lastReaon1.event;
                break;

            default:
                // If both point towards a block, then the reason is not conclusive. That should not happen.
                Assert.fail();
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
