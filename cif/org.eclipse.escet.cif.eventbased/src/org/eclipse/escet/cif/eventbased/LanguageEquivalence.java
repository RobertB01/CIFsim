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

package org.eclipse.escet.cif.eventbased;

import static org.eclipse.escet.cif.eventbased.automata.AutomatonHelper.reportNonDeterministic;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.equivalence.CounterExample;
import org.eclipse.escet.cif.eventbased.equivalence.LangEquivCalculation;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;

/** Language equivalence check. */
public class LanguageEquivalence {
    /** Constructor of the {@link LanguageEquivalence} class. */
    private LanguageEquivalence() {
        // Static class.
    }

    /**
     * Check whether the provided automata fulfill all preconditions for the language equivalence check.
     *
     * @param auts Automata to check.
     */
    public static void preCheck(List<Automaton> auts) {
        // Check number of provided automata.
        if (auts.size() != 2) {
            String msg = fmt("Expected two automata, found %d automat%s.", auts.size(),
                    ((auts.size() == 1) ? "on" : "a"));
            throw new InvalidInputException(msg);
        }

        // Check that the automata have an initial location.
        for (Automaton aut: auts) {
            if (aut.initial == null) {
                String msg = fmt("Automaton \"%s\" has no initial location.", aut.name);
                throw new InvalidInputException(msg);
            }
        }

        // Check alphabets.
        if (!auts.get(0).alphabet.equals(auts.get(1).alphabet)) {
            String msg = fmt("Automaton \"%s\" has a different alphabet than automaton \"%s\".", auts.get(0).name,
                    auts.get(1).name);
            throw new InvalidInputException(msg);
        }

        // Check that the automata are deterministic.
        for (Automaton aut: auts) {
            reportNonDeterministic(aut);
        }
    }

    /**
     * Perform the language equivalence check on the given automata.
     *
     * <p>
     * The automata must pass the {@link #preCheck} conditions.
     * </p>
     *
     * @param auts Automata to check.
     * @return A counter example showing the automata are not equivalent, or {@code null} if they are language
     *     equivalent.
     */
    public static CounterExample doLanguageEquivalenceCheck(List<Automaton> auts) {
        LangEquivCalculation lec = new LangEquivCalculation(auts);
        return lec.checkLanguageEquivalence();
    }
}
