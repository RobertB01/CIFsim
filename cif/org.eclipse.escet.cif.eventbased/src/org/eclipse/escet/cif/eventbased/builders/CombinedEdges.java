//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.common.java.Assert;

/**
 * Information storage of source edges for a single event. These edges should be combined to construct edges in the
 * destination automation.
 */
public class CombinedEdges {
    /** Event on the edges. */
    public final Event event;

    /**
     * Edges for each source automaton with event {@link #event}. Entry is {@code null} if it is not used by the
     * automaton.
     */
    public final List<List<Edge>> sourceEdges;

    /**
     * Constructor of the {@link CombinedEdges} class.
     *
     * @param event Event where the edge construction information is for.
     * @param automs Automata being combined.
     */
    public CombinedEdges(Event event, List<Automaton> automs) {
        this.event = event;

        sourceEdges = listc(automs.size());
        boolean isUsed = false;
        for (int i = 0; i < automs.size(); i++) {
            List<Edge> le;
            if (automs.get(i).alphabet.contains(event)) {
                le = list();
                isUsed = true;
            } else {
                le = null;
            }
            sourceEdges.add(le);
        }
        Assert.check(isUsed); // Otherwise the event is ignored by all.
    }

    /**
     * Can the event happen at all (here)?
     *
     * @return Whether the event can take place.
     */
    public boolean edgePossible() {
        return disabledIndex() < 0;
    }

    /**
     * At which automaton gets the event disabled?
     *
     * @return Lowest index of the automaton participating in the event that disables it, or {@code -1} if it is not
     *     disabled.
     */
    public int disabledIndex() {
        for (int i = 0; i < sourceEdges.size(); i++) {
            List<Edge> edges = sourceEdges.get(i);
            if (edges != null && edges.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    /** Clear the currently stored edges from the object. */
    public void clear() {
        for (List<Edge> edges: sourceEdges) {
            if (edges != null) {
                edges.clear();
            }
        }
    }
}
