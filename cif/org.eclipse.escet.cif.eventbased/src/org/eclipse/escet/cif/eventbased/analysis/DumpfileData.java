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

package org.eclipse.escet.cif.eventbased.analysis;

import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Data of the dump file. */
public class DumpfileData {
    /** Source automata information. */
    public SourceReadInfo sourceInfo = null;

    /** Events of the synthesis. */
    public List<EventInfo> events = null;

    /** States of the synthesis (from state to collection of locations). */
    public List<StateInfo> states = listc(100 * 1000);

    /** States of the synthesis (from collection of locations to state). */
    public Map<int[], Integer> sortedStates = new TreeMap<>(LocationArrayComparator.INSTANCE);

    /** Events that happened in the calculation. */
    public List<Object> calculationEvents = listc(1000 * 1000);

    /**
     * Get the number of automata in the synthesis.
     *
     * @return Number of automata in the synthesis.
     * @note Source information must be available.
     */
    public int getNumberAutomata() {
        return sourceInfo.sourceInfo.size();
    }

    /**
     * Get the number of plants in the synthesis.
     *
     * @return Number of plants in the synthesis.
     * @note Source information must be available.
     */
    public int getNumberPlants() {
        return sourceInfo.numPlants;
    }

    /**
     * Count the number of removed states.
     *
     * @return The number of states that were created and removed.
     */
    public int getRemovedCount() {
        int numPlants = getNumberPlants();
        boolean[] removed = new boolean[states.size()];

        int count = 0;
        for (Object calcEvent: calculationEvents) {
            if (calcEvent instanceof RemovedEdgeInfo) {
                RemovedEdgeInfo removedEdge = (RemovedEdgeInfo)calcEvent;
                if (removed[removedEdge.from]) {
                    continue;
                }
                if (removedEdge.toIsAutomaton && removedEdge.to < numPlants) {
                    continue;
                }
                EventInfo evtInfo = events.get(removedEdge.event);
                if (!evtInfo.contr) {
                    removed[removedEdge.from] = true;
                    count++;
                }
            }
            if (calcEvent instanceof RemovedLocationInfo) {
                RemovedLocationInfo removedLoc = (RemovedLocationInfo)calcEvent;
                if (!removed[removedLoc.loc]) {
                    removed[removedLoc.loc] = true;
                    count++;
                }
            }
        }
        return count;
    }
}
