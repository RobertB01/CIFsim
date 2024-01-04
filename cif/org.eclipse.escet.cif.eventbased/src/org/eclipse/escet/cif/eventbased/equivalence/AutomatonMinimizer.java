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

package org.eclipse.escet.cif.eventbased.equivalence;

import static org.eclipse.escet.cif.eventbased.automata.Edge.addEdge;
import static org.eclipse.escet.common.java.Lists.list;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.automata.origin.LocationSetOrigin;
import org.eclipse.escet.cif.eventbased.automata.origin.Origin;
import org.eclipse.escet.common.java.Sets;

/** Algorithm to minimize the number of locations in the automaton. */
public class AutomatonMinimizer extends BlockPartitioner {
    /**
     * Constructor of the {@link AutomatonMinimizer} class.
     *
     * @param aut Automaton to minimize.
     */
    public AutomatonMinimizer(Automaton aut) {
        super(list(aut), false);
    }

    /**
     * Minimize the automaton.
     *
     * @return An equivalent DFA automaton as provided in the constructor, but with a minimal number of locations.
     */
    public Automaton minimize() {
        performBlockPartitioning();

        // Get the block containing the initial location.
        Automaton orig = automs.get(0);
        int initialBlk = blockLocs.get(orig.initial).blockNumber;

        // Create a result location for each block, with initial and marked properties, and origin information.
        Automaton result = new Automaton(orig.alphabet);
        Location[] resultLocs = new Location[blocks.size()];
        for (int blkNum = 0; blkNum < blocks.size(); blkNum++) {
            Block block = blocks.get(blkNum);
            Origin origin = new LocationSetOrigin(block.locs.stream().map(b -> b.loc).collect(Sets.toSet()));
            Location loc = new Location(result, origin);
            if (blkNum == initialBlk) {
                result.setInitial(loc);
            }
            loc.marked = block.locs.get(0).loc.marked;

            resultLocs[blkNum] = loc;
        }

        // Add edges between the locations.
        for (int blkNum = 0; blkNum < blocks.size(); blkNum++) {
            Location loc = resultLocs[blkNum];
            Block block = blocks.get(blkNum);
            for (int evtNum = 0; evtNum < events.length; evtNum++) {
                int destBlk = block.outEvents[evtNum];
                if (destBlk != -1) {
                    Location destLoc = resultLocs[destBlk];
                    addEdge(events[evtNum], loc, destLoc);
                }
            }
        }
        return result;
    }

    @Override
    protected CounterExample constructCounterExample(Block block, Event finalEvent) {
        // Only one automaton is provided in the constructor, and a block is
        // always non-empty. Therefore the automaton is always present in a block.
        // A block is thus never a counter-example demonstrating a trace is not
        // present in the automaton.
        throw new AssertionError("Counter-example construction should not be needed.");
    }
}
