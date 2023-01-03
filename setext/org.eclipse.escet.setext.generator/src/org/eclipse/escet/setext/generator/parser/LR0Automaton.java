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

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.setext.parser.ast.Symbol;

/** LR(0) automaton. */
public class LR0Automaton {
    /** The initial state of the automaton. */
    public final LR0AutomatonState initialState;

    /** The unique id for the next state to add. */
    public int nextStateId = 0;

    /**
     * The states of the automaton. Each state maps to itself. This allows us to retrieve the representative state given
     * multiple equal states.
     */
    public final Map<LR0AutomatonState, LR0AutomatonState> states = map();

    /**
     * Constructor for the {@link LR0Automaton} class.
     *
     * @param initialState The initial state of the automaton.
     */
    public LR0Automaton(LR0AutomatonState initialState) {
        this.initialState = initialState;
        addState(initialState);
    }

    /**
     * Adds a state to the automaton. If the state is already present, the automaton is not changed, and the previously
     * added representative state for the given state is returned. If the state is not yet present, it is added, and
     * becomes a representative state.
     *
     * @param state The state to add.
     * @return The representative state for the state to add.
     */
    public LR0AutomatonState addState(LR0AutomatonState state) {
        // If already present, return representative.
        if (states.containsKey(state)) {
            return states.get(state);
        }

        // Add state as representative, set its id, and return it.
        states.put(state, state);
        state.id = nextStateId;
        nextStateId++;
        return state;
    }

    /**
     * Converts this LR(0) automaton to an LALR(1) automaton, where all kernel grammar items that represent states are
     * converted to lookahead grammar items, without any lookaheads.
     *
     * @return An LALR(1) automaton for this LR(0) automaton.
     */
    public LALR1Automaton toLALR1() {
        // Convert initial state.
        LALR1AutomatonState initialState1 = initialState.toLALR1State();
        LALR1Automaton aut1 = new LALR1Automaton(initialState1);
        Assert.check(initialState.id == initialState1.id);

        // Convert remaining states.
        boolean first = true;
        for (LR0AutomatonState state0: states.keySet()) {
            if (first) {
                first = false;
                continue;
            }
            LALR1AutomatonState state1 = state0.toLALR1State();
            aut1.states.add(state1);
            Assert.check(state0.id == state1.id);
        }

        // Convert edges.
        for (LR0AutomatonState state0: states.keySet()) {
            LALR1AutomatonState state1 = aut1.states.get(state0.id);
            Set<Entry<Symbol, LR0AutomatonState>> edges;
            edges = state0.edges.entrySet();
            for (Entry<Symbol, LR0AutomatonState> edge: edges) {
                state1.addEdge(edge.getKey(), aut1.states.get(edge.getValue().id));
            }
        }

        // Return LALR(1) automaton.
        return aut1;
    }

    /**
     * Prints the automaton.
     *
     * @param s The stream to which to write the output.
     */
    public void print(AppStream s) {
        int id = 0;
        for (LR0AutomatonState state: states.keySet()) {
            Assert.check(state.id == id);
            id++;
            state.print(s, state == initialState, false);
        }
    }
}
