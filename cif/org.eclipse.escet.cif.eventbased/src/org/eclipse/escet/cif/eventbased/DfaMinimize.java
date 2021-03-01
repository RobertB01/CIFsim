//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased;

import static org.eclipse.escet.cif.eventbased.automata.AutomatonHelper.reportNonDeterministic;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.equivalence.AutomatonMinimizer;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;

/** Minimize the number of locations of a DFA. */
public class DfaMinimize {
    /** Constructor of the {@link DfaMinimize} class. */
    private DfaMinimize() {
        // Static class.
    }

    /**
     * Check whether the provided automaton fulfills all conditions for the DFA minimization function.
     *
     * @param aut Automaton to check.
     */
    public static void preCheck(Automaton aut) {
        if (aut.initial == null) {
            String msg = fmt("Automaton \"%s\" has no initial location.", aut.name);
            throw new InvalidInputException(msg);
        }

        reportNonDeterministic(aut);
    }

    /**
     * Minimize the number of locations of the provided DFA automaton.
     *
     * @param aut Automaton to minimize.
     * @return Equivalent DFA automaton with minimized number of locations.
     */
    public static Automaton minimize(Automaton aut) {
        AutomatonMinimizer autoMini = new AutomatonMinimizer(aut);
        return autoMini.minimize();
    }
}
