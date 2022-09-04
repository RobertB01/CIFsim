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
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.common.java.Assert;

/**
 * Block in the language equivalence check.
 *
 * <p>
 * The goal in the algorithm is to assign a non-null value to every element in {@link #outEvents}, that is, for every
 * event, all locations in the block must point to a location in the same 'next' block.
 * </p>
 *
 * <p>
 * The {@link #inEvents} is used to quickly propagate splits of this block backwards to predecessor blocks.
 * </p>
 */
public class Block {
    /** If set, the block is in the queue to be reviewed. */
    boolean needsReview = false;

    /** Blocks pointing to this block, ordered by event. */
    public final List<List<Integer>> inEvents;

    /** Block that it was split from. May be {@code null} if was not split from another block. */
    public final Block parent;

    /**
     * The event that initiated the split from the parent block. May be {@code null} if the split was based on markings.
     */
    public final Event splitEvent;

    /** The depth of this block in the tree. */
    public final int depth;

    /**
     * For each event, the successor block it points to. Special entry value {@code null} means 'undecided', entry value
     * {@code -1} means 'nowhere' (that is, the locations don't have this event).
     */
    public Integer[] outEvents;

    /** Locations in the block. */
    public List<BlockLocation> locs;

    /**
     * Constructor of the {@link Block} class.
     *
     * @param numEvents Number of events in the combined alphabet.
     * @param numLocs Expected number of locations in this block, or {@code -1} for 'unknown'.
     * @param outgoing For each event, the successor block it points to. Special entry value {@code null} means
     *     'undecided', entry value {@code -1} means 'nowhere' (that is, the locations don't have this event).
     * @param parent The block where this block was split from. May be {@code null} if was not split from another block.
     * @param splitEvent The event that initiated the split from the parent block. May be {@code null} if the split was
     *     based on markings.
     */
    public Block(int numEvents, int numLocs, Integer[] outgoing, Block parent, Event splitEvent) {
        outEvents = outgoing;
        this.parent = parent;
        this.splitEvent = splitEvent;
        Assert.implies(splitEvent != null, parent != null);

        // Determine depth.
        depth = (parent == null) ? 0 : (parent.depth + 1);

        // Initialize 'inEvents' to 'nobody points to here'.
        inEvents = listc(numEvents);
        for (int i = 0; i < numEvents; i++) {
            List<Integer> inc = list();
            inEvents.add(inc);
        }

        // Setup 'locs'.
        if (numLocs >= 0) {
            locs = listc(numLocs);
        } else {
            locs = list();
        }
    }

    @SuppressWarnings("javadoc")
    private String strList(List<Integer> inblocks) {
        String s = "";
        for (Integer i: inblocks) {
            if (!s.isEmpty()) {
                s += ", ";
            }
            s += (i == null) ? "<null>" : String.valueOf(i);
        }
        return "[" + s + "]";
    }

    /**
     * Verify whether the block contains locations from all automata.
     *
     * @param numAuts Number of automata being partitioned.
     * @return Whether at least one location of each automaton is present in the block.
     */
    public boolean allAutomataPresent(int numAuts) {
        if (numAuts == 1) {
            return true; // Block is never empty, so one automaton is always present.
        }

        // Check the locations in the block, but stop as soon as all automata have been found.
        int toCheck = 0; // Automaton number to check.
        boolean[] seen = new boolean[numAuts];
        for (BlockLocation loc: locs) {
            seen[loc.autNumber] = true;
            if (loc.autNumber == toCheck) { // seen[toCheck] became true.
                while (true) {
                    toCheck++;
                    if (toCheck == numAuts) {
                        return true;
                    }
                    if (!seen[toCheck]) {
                        break;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String inText = "";
        for (int i = 0; i < inEvents.size(); i++) {
            if (i > 0) {
                inText += ", ";
            }
            inText += "evt " + String.valueOf(i) + ": " + strList(inEvents.get(i));
        }

        String s = fmt("Block(%d locations, review %srequired)", locs.size(), needsReview ? "" : "not ");
        String outText = "";
        for (int i = 0; i < outEvents.length; i++) {
            if (i > 0) {
                outText += ", ";
            }
            outText += fmt("evt %d: part %s", i, outEvents[i] == null ? "<undecided>" : String.valueOf(outEvents[i]));
        }
        return "inEvents: {" + inText + "}\n" + s + "\noutEvents: {" + outText + "}";
    }
}
