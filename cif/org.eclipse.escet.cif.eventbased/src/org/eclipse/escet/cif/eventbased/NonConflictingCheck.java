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

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;

/**
 * Class implementing the non-conflicting check.
 *
 * <p>
 * Mathematically, you compute the prefix-closure of the marked plant behavior and the prefix closure of the marked
 * supervisor behavior, and verify whether the intersection of both is equal to the prefix closure of the marked
 * behavior of the synchronous product of the plant and supervisor.
 * </p>
 *
 * <p>
 * Computationally, it boils down to verifying that all states in the result are coreachable. (Suppose that common state
 * s is on a path to a marked state A in the plant and at a path to a marked state B in the supervisor, except A != B.
 * At s, the paths diverge, which means that s is a deadlock state in the synchronous product.)
 * </p>
 */
public class NonConflictingCheck {
    /** Constructor of the {@link NonConflictingCheck} class. */
    private NonConflictingCheck() {
        // Static class.
    }

    /**
     * Check the pre-conditions for applying the non-conflicting check.
     *
     * @param automs Automata for which to check the pre-conditions.
     */
    public static void nonconflictingPreCheck(List<Automaton> automs) {
        for (Automaton aut: automs) {
            AutomatonHelper.reportNonDeterministic(aut);
            if (!AutomatonHelper.trimCheck(aut)) {
                String msg = fmt("Unsupported automaton \"%s\": the automaton is not trim.", aut.name);
                throw new InvalidInputException(msg);
            }
        }
    }

    /**
     * Perform non-conflicting check on the automata.
     *
     * <p>
     * The {@link #nonconflictingPreCheck} function should not report any problems.
     * </p>
     *
     * @param automs Automata to use for the check.
     * @return Conflicting states in the combined result.
     */
    public static List<Location> nonconflictingCheck(List<Automaton> automs) {
        Automaton combined = SynchronousProduct.product(automs);
        Set<Location> coreachables = setc(10000);
        int count = AutomatonHelper.getNonCoreachableCount(combined, coreachables);
        List<Location> counterExamples = listc(count);
        if (count > 0) {
            for (Location loc: combined) {
                if (!coreachables.contains(loc)) {
                    counterExamples.add(loc);
                    count--;
                    if (count == 0) {
                        break;
                    }
                }
            }
        }
        return counterExamples;
    }
}
