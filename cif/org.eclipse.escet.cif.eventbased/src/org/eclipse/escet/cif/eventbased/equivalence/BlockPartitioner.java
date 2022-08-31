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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dodbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.common.java.Assert;

/** Block partitioning base class. */
public abstract class BlockPartitioner {
    /** Automata being partitioned. */
    public final List<Automaton> automs;

    /** Events of the automata. */
    public final Event[] events;

    /** Mapping of locations to their block. */
    public final Map<Location, BlockLocation> blockLocs = map();

    /** Blocks found so far. */
    public List<Block> blocks = list();

    /** Queue with blocks that need reviewing on their event destinations (indices in {@link #blocks}). */
    private Queue<Integer> notDone = new ArrayDeque<>();

    /**
     * Whether the algorithm should require that each automaton is represented in each block (by one or more locations).
     */
    private final boolean requireAllAutomata;

    /**
     * Constructor of the {@link BlockPartitioner} class.
     *
     * @param automs Automata participating in the block partitioning.
     * @param requireAllAutomata Whether the algorithm should require that each automaton is represented in each block
     *     (by one or more locations).
     */
    public BlockPartitioner(List<Automaton> automs, boolean requireAllAutomata) {
        this.automs = automs;
        this.requireAllAutomata = requireAllAutomata;

        // Fill 'this.events'.
        Set<Event> events = set();
        for (Automaton aut: automs) {
            events.addAll(aut.alphabet);
        }
        this.events = new Event[events.size()];
        events.toArray(this.events);
    }

    /**
     * Perform the block partition algorithm by Kanellakis and Smolka.
     *
     * <p>
     * The end condition is that for every block, all locations in the block must agree on the successor block for each
     * event (including events that are not available from the locations).
     * </p>
     *
     * <p>
     * The algorithm starts with a two blocks, containing all locations of all automata. One block contains marked
     * locations, the other block contains non-marked locations.
     * </p>
     *
     * <p>
     * In other words, given two states s1 and s2 in the same block B1, and event e, then either both s1 and s2 must not
     * be able to perform the event, or performing event leads to states s1' and s2', which are both in block B2.
     * </p>
     *
     * <p>
     * While the above condition does not hold, block B1 must be split into two blocks, one new block with locations
     * from B1 leading to B2, and one new block with locations from B1 not leading to B2 (for event e). The two new
     * blocks replace the block B1.
     * </p>
     *
     * <p>
     * In the implementation, a block is not split into two new blocks, a new block is created for every different
     * destination block for event e.
     * </p>
     *
     * <p>
     * Also, the implementation has the option to stop when a block is found containing locations from less than all
     * automata. Since a block expresses equivalence between its locations, the path to any location in the found block
     * does not exist in the non-present automata. This property is used to construct a counter example. The divergence
     * of the path happens before reaching any state of the found block, but it may be much earlier.
     * </p>
     *
     * @return The information to construct a human-readable counter example for language equivalence, or {@code null}
     *     if the algorithm terminated without finding a counter example or when no counter example was searched.
     */
    public CounterExample performBlockPartitioning() {
        // First create initial partitioning, based on marking of locations.

        // 'blks[0]' are unmarked locations, while 'blks[1]' are marked locations.
        Block[] blks = {makeBlock(-1, null, null, null),
                makeBlock(-1, null, null, null)};

        // Only the reachable locations are added.
        for (int autNum = 0; autNum < this.automs.size(); autNum++) {
            addAutomaton(this.automs.get(autNum), autNum, blks);
        }

        if (!blks[0].locs.isEmpty()) {
            if (requireAllAutomata && !blks[0].allAutomataPresent(automs.size())) {
                return constructCounterExample(blks[0], null);
            }
            blocks.add(blks[0]);
            markBlockForReview(0);
        }
        if (!blks[1].locs.isEmpty()) {
            if (requireAllAutomata && !blks[1].allAutomataPresent(automs.size())) {
                return constructCounterExample(blks[1], null);
            }
            blocks.add(blks[1]);
            if (blocks.size() == 2) {
                markBlockForReview(1);
            } else {
                // 'blocks' does not have two blocks, thus blks[0] was empty.
                // Renumber the locations of the block.
                for (BlockLocation bl: blks[1].locs) {
                    bl.blockNumber = 0;
                }
                markBlockForReview(0);
            }
        }

        // Initial blocks have no counter-example, perform further partitioning.
        dumpPartition("Initial partition (based on marking)");
        while (!notDone.isEmpty()) {
            int num = notDone.poll();
            if (dodbg()) {
                dbg("Reviewing block %s", num);
                dbg();
            }
            CounterExample example = reviewBlock(num);
            if (example != null) {
                dumpPartition("Partition counter-example");
                return example;
            }
            if (!notDone.isEmpty()) {
                dumpPartition("Intermediate partition");
            }
        }
        dumpPartition("Partition finished (no counter-example)");
        return null;
    }

    /**
     * Dump the current partitioning onto the debug output stream.
     *
     * @param name Name of the partition state.
     */
    private void dumpPartition(String name) {
        if (!dodbg()) {
            return;
        }

        dbg(name + ":");
        idbg();
        for (int blkNum = 0; blkNum < blocks.size(); blkNum++) {
            Block blk = blocks.get(blkNum);

            // Dump block number + location names.
            String s = fmt("Block %d:", blkNum);
            for (BlockLocation bloc: blk.locs) {
                Location loc = bloc.loc;
                s += fmt(" %s", loc.origin);
            }
            dbg(s);

            // Dump inputs.
            s = "";
            for (int evtNum = 0; evtNum < events.length; evtNum++) {
                String t = "";
                for (Integer inBlk: blk.inEvents.get(evtNum)) {
                    if (!t.isEmpty()) {
                        t += ", ";
                    }
                    t += fmt("%s", inBlk);
                }
                if (!s.isEmpty()) {
                    s += ", ";
                }
                s += fmt("%s -> [%s]", events[evtNum].name, t);
            }
            dbg("Input: " + s);

            // Dump outputs.
            s = "";
            for (int evtNum = 0; evtNum < events.length; evtNum++) {
                if (!s.isEmpty()) {
                    s += ", ";
                }
                Integer out = blk.outEvents[evtNum];
                if (out == null) {
                    s += fmt("%s -> ?", events[evtNum].name);
                } else {
                    s += fmt("%s -> %s", events[evtNum].name, out);
                }
            }
            dbg("Output: " + s);
            dbg();
        }
        ddbg();
    }

    /**
     * Add the reachable locations of the automaton to the initial blocks.
     *
     * @param aut Automaton to add.
     * @param autNum Index number of the automaton.
     * @param blks Initial blocks, {@code 0} is unmarked locations, {@code 1} is marked locations.
     */
    private void addAutomaton(Automaton aut, int autNum, Block[] blks) {
        Assert.check(aut.initial != null);
        Queue<BlockLocation> toAdd = new ArrayDeque<>();

        int blknum = aut.initial.marked ? 1 : 0;
        BlockLocation initialBlkLoc = new BlockLocation(aut.initial, autNum, 0, blknum);
        blockLocs.put(initialBlkLoc.loc, initialBlkLoc);
        blks[blknum].locs.add(initialBlkLoc);
        toAdd.add(initialBlkLoc);

        while (!toAdd.isEmpty()) {
            BlockLocation blkLoc = toAdd.poll();
            Edge edge = blkLoc.loc.outgoingEdges;
            int nxtDepth = blkLoc.depth + 1;
            while (edge != null) {
                Location nxt = edge.dstLoc;
                if (blockLocs.containsKey(nxt)) {
                    edge = edge.nextOutgoing;
                    continue;
                }

                blknum = nxt.marked ? 1 : 0;
                BlockLocation nxtBlkLoc = new BlockLocation(nxt, autNum, nxtDepth, blknum);
                blockLocs.put(nxt, nxtBlkLoc);
                blks[blknum].locs.add(nxtBlkLoc);
                toAdd.add(nxtBlkLoc);

                edge = edge.nextOutgoing;
            }
        }
    }

    /**
     * Review a block.
     *
     * @param blkIndex Index of the block to review.
     * @return Counter example if one is found, or {@code null}.
     */
    private CounterExample reviewBlock(int blkIndex) {
        Block blk = blocks.get(blkIndex);
        Assert.check(blk.needsReview);
        blk.needsReview = false; // Later we check that it stayed unset.

        Map<Integer, List<BlockLocation>> succBlocks = map();
        for (int evtIndex = 0; evtIndex < events.length; evtIndex++) {
            if (blk.outEvents[evtIndex] != null) {
                continue;
            }

            // Assign each location to the next block after performing the event.
            Event evt = events[evtIndex];
            succBlocks.clear();
            for (BlockLocation bl: blk.locs) {
                int nextBlock = -1;
                for (Edge e: bl.loc.getOutgoing(evt)) {
                    BlockLocation dstBlkLoc = blockLocs.get(e.dstLoc);
                    nextBlock = dstBlkLoc.blockNumber;
                    break; // Deterministic automaton, this is the last edge.
                }
                List<BlockLocation> succBlkLocs = succBlocks.get(nextBlock);
                if (succBlkLocs == null) {
                    succBlkLocs = list();
                    succBlocks.put(nextBlock, succBlkLocs);
                }
                succBlkLocs.add(bl);
            }
            if (succBlocks.size() == 1) {
                // All locations point to the same block after doing event 'evt'.
                // Block doesn't need splitting on this event. Update the outgoing event,
                // and the incoming event at the successor block.
                Integer succBlk = succBlocks.keySet().iterator().next();
                blk.outEvents[evtIndex] = succBlk;
                if (succBlk >= 0) {
                    blocks.get(succBlk).inEvents.get(evtIndex).add(blkIndex);
                }
                continue; // Check the other events.
            }

            // Block needs splitting, this block is discarded and new blocks are added.
            Assert.check(succBlocks.size() > 1);

            // Remove incoming and outgoing connections. Predecessor blocks
            // need to be reviewed again. Successor blocks must be reconnected
            // after splitting.
            invalidateIncoming(blk, blkIndex);
            unlinkOutgoing(blk, blkIndex); // All self-loops of 'blkIndex' are dropped here.

            boolean first = true;
            Block counterExample = null;
            for (Entry<Integer, List<BlockLocation>> succBlkEntry: succBlocks.entrySet()) {
                // Make new block and add it to the 'blocks'.
                Block newBlock = makeBlock(succBlkEntry.getValue().size(),
                        Arrays.copyOf(blk.outEvents, blk.outEvents.length), blk, evt);
                int newBlocknum;
                if (first) {
                    blocks.set(blkIndex, newBlock);
                    newBlocknum = blkIndex;
                    first = false;
                } else {
                    newBlocknum = blocks.size();
                    blocks.add(newBlock);
                }

                // Add locations to the new block.
                for (BlockLocation bl: succBlkEntry.getValue()) {
                    bl.blockNumber = newBlocknum; // Move the location to the new block.
                    newBlock.locs.add(bl);
                }
                if (requireAllAutomata && !newBlock.allAutomataPresent(automs.size())) {
                    // Found a block where not locations from all automata were present.
                    // Save the block, but continue to create the additional blocks for counterexample generation.
                    if (counterExample == null) {
                        counterExample = newBlock;
                    }
                }

                // The 'succBlkEntry' variable contains the destination block to point to, for the event we split on.
                // For all blocks but the reviewed block (at 'blkIndex'), this information is still valid, since those
                // blocks have been not changed by the split.
                int destBlk = succBlkEntry.getKey();
                if (destBlk != blkIndex) {
                    newBlock.outEvents[evtIndex] = destBlk;
                }

                // Reconnect the dangling outgoing connections, and mark the
                // block for review (of the other events).
                reconnectOutgoing(newBlock, newBlocknum);
                markBlockForReview(newBlocknum);
            }

            // Check whether a violation block was found.
            if (counterExample != null) {
                // A violation has been found, construct a counter example.
                return constructCounterExample(counterExample, evt);
            }

            Assert.check(!blk.needsReview);
            return null; // Dump the block (new blocks have been added to the queue).
        }
        return null;
    }

    /**
     * Construct a new block.
     *
     * @param numLocs Expected number of locations, or {@code -1} for 'unknown'.
     * @param outgoing Outgoing events to successor blocks. Use {@code null} for creating a new block with 'unknown'
     *     entries.
     * @param parent The block where this block was split from. May be {@code null} if was not split from another block.
     * @param splitEvent The event that initiated the split from the parent block. May be {@code null} if the split was
     *     based on markings.
     * @return The created block.
     */
    private Block makeBlock(int numLocs, Integer[] outgoing, Block parent, Event splitEvent) {
        if (outgoing == null) {
            outgoing = new Integer[events.length];
        }
        return new Block(events.length, numLocs, outgoing, parent, splitEvent);
    }

    /**
     * Invalidate the predecessor blocks of the given block.
     *
     * @param blk Block with (now) invalid incoming blocks.
     * @param skipReview Block number to skip for marking for review.
     */
    private void invalidateIncoming(Block blk, int skipReview) {
        for (int evtIndex = 0; evtIndex < events.length; evtIndex++) {
            for (int pred: blk.inEvents.get(evtIndex)) {
                Block predBlock = blocks.get(pred);
                predBlock.outEvents[evtIndex] = null;
                if (pred != skipReview) {
                    markBlockForReview(pred);
                }
            }
        }
    }

    /**
     * Unlink the outgoing events of the bad block by removing the block from the incoming side of its successor. If the
     * block successor is the bad block itself, don't bother making the event dangling, just drop the outgoing side.
     *
     * @param badBlk Block with outgoing events to make dangling.
     * @param badBlocknum Number of the bad block (should just be dropped).
     */
    public void unlinkOutgoing(Block badBlk, int badBlocknum) {
        for (int evtIndex = 0; evtIndex < events.length; evtIndex++) {
            Integer succBlkIndex = badBlk.outEvents[evtIndex];
            if (succBlkIndex == null) {
                continue; // Needed update.
            }
            int succBlkIdx = succBlkIndex;
            if (succBlkIdx == -1) {
                continue; // Is not pointing to a valid block.
            }
            if (succBlkIdx == badBlocknum) {
                badBlk.outEvents[evtIndex] = null;
                continue; // At the bad block, drop the outgoing side.
            }

            // Remove 'badBlockNum' at the incoming side (and make the event dangling).
            Block succBlk = blocks.get(succBlkIdx);
            List<Integer> incomingBlknums = succBlk.inEvents.get(evtIndex);
            int idx;
            final int lastIndex = incomingBlknums.size() - 1;
            for (idx = 0; idx <= lastIndex; idx++) {
                if (incomingBlknums.get(idx) == badBlocknum) {
                    break;
                }
            }
            Assert.check(idx <= lastIndex);
            if (idx < lastIndex) {
                incomingBlknums.set(idx, incomingBlknums.get(lastIndex));
            }
            incomingBlknums.remove(lastIndex);
        }
    }

    /**
     * Reconnect the outgoing events to their successor blocks.
     *
     * @param newBlk New block.
     * @param newBlocknum Index number of the new block.
     */
    private void reconnectOutgoing(Block newBlk, int newBlocknum) {
        for (int evtIndex = 0; evtIndex < events.length; evtIndex++) {
            Integer succBlkIndex = newBlk.outEvents[evtIndex];
            if (succBlkIndex == null || succBlkIndex == -1) {
                continue;
            }
            Block succBlk = blocks.get(succBlkIndex);
            succBlk.inEvents.get(evtIndex).add(newBlocknum);
        }
    }

    /**
     * Mark a block for review/update. Before calling this method, the outgoing events must have been modified to
     * reflect what needs to be updated.
     *
     * @param blkIndex Index of the block to review and update.
     */
    private void markBlockForReview(int blkIndex) {
        Block blk = blocks.get(blkIndex);
        if (!blk.needsReview) {
            notDone.add(blkIndex);
            blk.needsReview = true;
        }
    }

    /**
     * Construct a counter example for a location in the block.
     *
     * @param block Block with locations of less than all automata.
     * @param finalEvent Event being used for splitting, creating this block. {@code null} means the difference is
     *     caused by marking of the locations instead.
     * @return A counter example.
     */
    protected abstract CounterExample constructCounterExample(Block block, Event finalEvent);
}
