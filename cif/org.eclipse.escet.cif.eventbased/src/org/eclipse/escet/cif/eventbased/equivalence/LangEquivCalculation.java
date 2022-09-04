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

import static org.eclipse.escet.common.java.Lists.last;
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
     * Check whether both automata provided in the constructor, have the same language using a {@link BlockPartitioner
     * block partitioning algorithm}.
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

    /**
     * Finds the path towards the location that explains why the two locations are not part of the same block.
     *
     * @param loc1 The first location.
     * @param loc2 The second location.
     * @return The path, starting in {@code loc1} and {@code loc2} that explains why these locations are not part of the
     *     same block. The last event of the path may be enabled only from one the locations. The last event of the path
     *     may be {@code null} to indicate that the markings are different.
     */
    private List<Event> getSplitExplanationPath(Location loc1, Location loc2) {
        // Initialize split explanation path.
        List<Event> splitPath = list();

        // Explore the path until a conclusive reason is found that explains why these locations are not part of the
        // same block.
        Location curLoc1 = loc1;
        Location curLoc2 = loc2;
        while (true) {
            // Get lowest block (depth) where these locations are a part of.
            Block block1 = blocks.get(blockLocs.get(curLoc1).blockNumber);
            Block block2 = blocks.get(blockLocs.get(curLoc2).blockNumber);

            // Check that the locations are indeed part of two different blocks.
            Assert.check(block1 != block2);

            // Get first two blocks where 'curLoc1' and 'curLoc2' are no longer in the same block.
            while (block1.parent != block2.parent) {
                int maxDepth = Math.max(block1.depth, block2.depth);
                if (block1.depth == maxDepth) {
                    block1 = block1.parent;
                }
                if (block2.depth == maxDepth) {
                    block2 = block2.parent;
                }
            }

            // Ensure that the blocks are different children.
            Assert.check(block1 != block2);

            // Ensure that indeed both blocks are split because of the same event.
            Assert.check(block1.splitEvent == block2.splitEvent);

            // Add an entry to the split explanation path.
            splitPath.add(block1.splitEvent);

            // Find the reason why these two blocks where created. There are three possibilities:
            // 1) splitEvent == null: The splitting reason was because of markings. Conclusive.
            // 2) splitEvent != null, and the event is not enabled in one location. Conclusive.
            // 3) splitEvent != null, and the event is enabled in both locations. Not conclusive. Continue searching
            // from the two new locations. The search will terminate, as the splitting tree has no cycles.
            if (block1.splitEvent != null) {
                Location nextLoc1 = getNextLocation(curLoc1, block1.splitEvent);
                Location nextLoc2 = getNextLocation(curLoc2, block2.splitEvent);

                // We must have found at least one new location. Otherwise we've hit a dead end.
                Assert.check(nextLoc1 != null || nextLoc2 != null);

                if (nextLoc1 != null && nextLoc2 != null) {
                    // Possibility 3. The reason is not conclusive, keep searching.
                    curLoc1 = nextLoc1;
                    curLoc2 = nextLoc2;
                    continue;
                } else {
                    // Possibility 2. The reason is conclusive, stop searching.
                    break;
                }
            } else {
                // Possibility 1. The reason is conclusive, stop searching.
                break;
            }
        }

        // Return the path that explains the splitting reason.
        return splitPath;
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

        // Execute the path towards the location in the block.
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
            return new CounterExample(path.subList(0, pathIdx), locs, evt);
        }

        // If no final event is given, the reason is conclusive (because of markings).
        if (finalEvent == null) {
            return new CounterExample(path, locs, null);
        }

        // We found two equivalent locations that are part of different blocks. Figure out why.
        List<Event> splitExplanationPath = getSplitExplanationPath(locs[0], locs[1]);

        // Execute the path towards the conclusive states. The last event is excluded, as that contains the conclusive
        // reason.
        for (int i = 0; i < splitExplanationPath.size() - 1; i++) {
            Event evt = splitExplanationPath.get(i);
            path.add(evt);
            locs[0] = getNextLocation(locs[0], evt);
            locs[1] = getNextLocation(locs[1], evt);

            // The event should lead to a new location. Only the last event of the path may not be enabled, and that is
            // excluded from the loop.
            Assert.notNull(locs[0]);
            Assert.notNull(locs[1]);
        }

        // Get the last explanation event to determine the conclusive reason.
        Event lastExplanationEvent = last(splitExplanationPath);

        // If no final event is given, the reason is already conclusive (because of markings).
        if (lastExplanationEvent == null) {
            return new CounterExample(path, locs, null);
        }

        // Determine for how many states the final event is enabled.
        int numEventsEnabled = (getNextLocation(locs[0], lastExplanationEvent) == null ? 0 : 1)
                + (getNextLocation(locs[1], lastExplanationEvent) == null ? 0 : 1);

        if (numEventsEnabled != 1) {
            // If the event is enabled for both states, then the reason is not conclusive. That should not happen.
            // If the event is disabled for both states, then the reason is not conclusive. That should not happen.
            Assert.fail();
        }

        // The event is enabled for one state, but not for the other. This is a conclusive reason.
        return new CounterExample(path, locs, lastExplanationEvent);
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
