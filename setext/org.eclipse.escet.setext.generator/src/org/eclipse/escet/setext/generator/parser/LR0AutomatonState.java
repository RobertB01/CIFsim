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

package org.eclipse.escet.setext.generator.parser;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.setext.parser.ast.Symbol;

/** A state of a {@link LR0Automaton}. */
public class LR0AutomatonState {
    /** The grammar items that this state represents. Contains both the kernel and non-kernel grammar items. */
    public final Set<GrammarItem> items;

    /** The unique id of this state in the automaton. Value is {@code -1} if not yet part of an automaton. */
    public int id = -1;

    /**
     * The outgoing edges of this state. Mapping from the symbols that must be recognized before the edge may be taken,
     * to the target state of the edge. Mapping is filled in-place.
     */
    public final Map<Symbol, LR0AutomatonState> edges = map();

    /**
     * Constructor for the {@link LR0AutomatonState} class.
     *
     * @param items The grammar items that this state represents.
     */
    public LR0AutomatonState(Set<GrammarItem> items) {
        this.items = items;
        Assert.check(!items.isEmpty());
    }

    /**
     * Adds an outgoing edge to this state.
     *
     * @param symbol The symbol that must be recognized before this edge may be taken.
     * @param state The target state of the edge.
     * @throws IllegalStateException If an edge with that symbol already exists.
     */
    public void addEdge(Symbol symbol, LR0AutomatonState state) {
        if (edges.containsKey(symbol)) {
            throw new IllegalStateException("Duplicate edge.");
        }
        edges.put(symbol, state);
    }

    @Override
    public int hashCode() {
        // We only consider the items for value equality.
        return LR0AutomatonState.class.hashCode() ^ items.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // We only consider the items for value equality.
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LR0AutomatonState)) {
            return false;
        }
        LR0AutomatonState other = (LR0AutomatonState)obj;
        return items.equals(other.items);
    }

    /**
     * Returns the kernel items representing this state.
     *
     * @return The kernel items representing this state.
     */
    public Set<GrammarItem> getKernelItems() {
        Set<GrammarItem> rslt = set();
        for (GrammarItem item: items) {
            if (item.isKernelItem()) {
                rslt.add(item);
            }
        }
        return rslt;
    }

    /**
     * Converts this LR(0) automaton state to an LALR(1) automaton state. All kernel grammar items that represent this
     * state are converted to lookahead grammar items without lookaheads. The non-kernel items are omitted.
     *
     * <p>
     * This method does not copy the edges.
     * </p>
     *
     * @return An LALR(1) automaton state for this LR(0) automaton state.
     */
    public LALR1AutomatonState toLALR1State() {
        Map<GrammarItem, LookaheadItem> newItems = map();
        for (GrammarItem item: items) {
            if (!item.isKernelItem()) {
                continue;
            }
            newItems.put(item, item.toLookaheadItem());
        }
        Assert.check(!newItems.isEmpty());
        return new LALR1AutomatonState(newItems, id);
    }

    /**
     * Returns the outgoing edges of this state, sorted first in ascending order by target state id, and then in
     * ascending order by name of the symbol.
     *
     * @return The sorted, outgoing edges of this state.
     */
    public List<Pair<Symbol, LR0AutomatonState>> getSortedEdges() {
        // Note that all edges have unique code points.
        List<Pair<Symbol, LR0AutomatonState>> sortedEdges = list();
        for (Entry<Symbol, LR0AutomatonState> entry: edges.entrySet()) {
            sortedEdges.add(pair(entry.getKey(), entry.getValue()));
        }

        Comparator<Pair<Symbol, LR0AutomatonState>> cmp;
        cmp = new Comparator<>() {
            @Override
            public int compare(Pair<Symbol, LR0AutomatonState> t1, Pair<Symbol, LR0AutomatonState> t2) {
                // Ascending order by target state id.
                int targetId1 = t1.right.id;
                int targetId2 = t2.right.id;
                if (targetId1 < targetId2) {
                    return -1;
                }
                if (targetId1 > targetId2) {
                    return 1;
                }

                // Ascending order by name of the symbol.
                return Strings.SORTER.compare(t1.left.name, t2.left.name);
            }
        };
        Collections.sort(sortedEdges, cmp);

        return sortedEdges;
    }

    /**
     * Prints the state.
     *
     * @param s The stream to which to write the output.
     * @param initial Is this the initial state?
     * @param skipEdges Whether to skip printing the edges.
     */
    public void print(AppStream s, boolean initial, boolean skipEdges) {
        // Get item texts.
        List<String> charTxts = list();
        for (GrammarItem item: items) {
            charTxts.add(item.toString());
        }
        Collections.sort(charTxts, Strings.SORTER);

        // Print state header.
        s.println(fmt("%sstate %d (%s):", initial ? "initial " : "", id, charTxts));

        // Print edges.
        if (skipEdges) {
            return;
        }

        List<Pair<Symbol, LR0AutomatonState>> edges = getSortedEdges();
        for (Pair<Symbol, LR0AutomatonState> edge: edges) {
            Symbol symbol = edge.left;
            LR0AutomatonState target = edge.right;
            String edgeTxt = symbol.name + " -> " + target.id;
            s.println("  " + edgeTxt);
        }
    }
}
