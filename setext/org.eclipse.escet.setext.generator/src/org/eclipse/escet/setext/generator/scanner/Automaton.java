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

package org.eclipse.escet.setext.generator.scanner;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;

/** Deterministic Finite Automaton (DFA) for a scanner. */
public class Automaton {
    /** The initial state of the automaton. */
    public final AutomatonState initialState;

    /** The unique id for the next state to add. */
    public int nextStateId = 0;

    /**
     * The states of the automaton. Each state maps to itself. This allows us to retrieve the representative state given
     * multiple equal states.
     */
    public final Map<AutomatonState, AutomatonState> states = map();

    /**
     * Constructor for the {@link Automaton} class.
     *
     * @param initialState The initial state of the automaton.
     */
    public Automaton(AutomatonState initialState) {
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
    public AutomatonState addState(AutomatonState state) {
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
     * Prints the DFA.
     *
     * @param s The stream to which to write the output.
     */
    public void print(AppStream s) {
        int id = 0;
        for (AutomatonState state: states.keySet()) {
            Assert.check(state.id == id);
            id++;
            s.println();
            state.print(s, state == initialState);
        }
    }
}
