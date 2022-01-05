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

package org.eclipse.escet.setext.generator.parser;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;

/** LALR(1) automaton. */
public class LALR1Automaton {
    /** The initial state of the automaton. */
    public final LALR1AutomatonState initialState;

    /**
     * The states of the automaton. Each state maps to itself. This allows us to retrieve the representative state given
     * multiple equal states.
     */
    public final List<LALR1AutomatonState> states = list();

    /**
     * Constructor for the {@link LALR1Automaton} class.
     *
     * @param initialState The initial state of the automaton.
     */
    public LALR1Automaton(LALR1AutomatonState initialState) {
        this.initialState = initialState;
        states.add(initialState);
        Assert.check(initialState.id == 0);
    }

    /**
     * Prints the automaton.
     *
     * @param s The stream to which to write the output.
     */
    public void print(AppStream s) {
        int id = 0;
        for (LALR1AutomatonState state: states) {
            Assert.check(state.id == id);
            id++;
            state.print(s, state == initialState);
        }
    }

    /**
     * Prints the parsing table (as automaton).
     *
     * @param s The stream to which to write the output.
     * @param generator The generator used to construct this automaton.
     */
    public void printTable(AppStream s, LALR1ParserGenerator generator) {
        // Print table.
        int id = 0;
        for (LALR1AutomatonState state: states) {
            Assert.check(state.id == id);
            id++;
            s.println();
            state.printTable(s, state == initialState, generator);
        }

        // Print conflicts totals.
        s.println();
        s.println(generator.getConflictsText());
    }
}
