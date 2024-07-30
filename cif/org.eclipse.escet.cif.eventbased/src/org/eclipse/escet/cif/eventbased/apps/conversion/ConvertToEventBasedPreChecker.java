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

package org.eclipse.escet.cif.eventbased.apps.conversion;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithCertainNumberOfInitLocsCheck;
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithCertainNumberOfInitLocsCheck.AllowedNumberOfInitLocs;
import org.eclipse.escet.cif.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.CompNoMarkerPredsCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeNoUpdatesCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeOnlyStaticEvalGuardPredsCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoChannelsCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoTauCheck;
import org.eclipse.escet.cif.checkers.checks.EventOnlyWithControllabilityCheck;
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.LocNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.LocOnlyStaticEvalInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.LocOnlyStaticEvalMarkerPredsCheck;
import org.eclipse.escet.cif.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.common.java.Termination;

/** CIF to event-based conversion precondition checker. */
public class ConvertToEventBasedPreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link ConvertToEventBasedPreChecker} class.
     *
     * @param allowPlainEvents Whether to allow events without controllability.
     * @param allowNonDeterminism Whether to allow non-deterministic automata.
     * @param expectedNumberOfAutomata The expected automata, or {@code null} for no constraint.
     * @param termination Cooperative termination query function.
     */
    public ConvertToEventBasedPreChecker(boolean allowPlainEvents, boolean allowNonDeterminism,
            ExpectedNumberOfAutomata expectedNumberOfAutomata, Termination termination)
    {
        super(termination, getChecks(allowPlainEvents, allowNonDeterminism, expectedNumberOfAutomata));
    }

    /**
     * Get the checks to use.
     *
     * @param allowPlainEvents Whether to allow events without controllability.
     * @param allowNonDeterminism Whether to allow non-deterministic automata.
     * @param expectedNumberOfAutomata The expected automata, or {@code null} for no constraint.
     * @return The checks to use.
     */
    private static List<CifCheck> getChecks(boolean allowPlainEvents, boolean allowNonDeterminism,
            ExpectedNumberOfAutomata expectedNumberOfAutomata)
    {
        List<CifCheck> checks = list();

        // Events without controllability may not be supported. Event 'tau' is never supported.
        if (allowPlainEvents) {
            checks.add(new EventNoTauCheck());
        } else {
            checks.add(new EventOnlyWithControllabilityCheck());
        }

        // Channels are not supported.
        checks.add(new EventNoChannelsCheck());

        // Automata with multiple initial locations are not supported.
        checks.add(new AutOnlyWithCertainNumberOfInitLocsCheck(AllowedNumberOfInitLocs.AT_MOST_ONE));

        // Edges with updates are not supported.
        checks.add(new EdgeNoUpdatesCheck());

        // Urgent locations and edges are not supported.
        checks.add(new LocNoUrgentCheck());
        checks.add(new EdgeNoUrgentCheck());

        // Initialization and marker predicates in components are not supported.
        checks.add(new CompNoInitPredsCheck());
        checks.add(new CompNoMarkerPredsCheck());

        // Initialization and marker predicates in locations only if they are trivially true/false.
        checks.add(new LocOnlyStaticEvalInitPredsCheck());
        checks.add(new LocOnlyStaticEvalMarkerPredsCheck());

        // Invariants are not supported, unless they do not restrict any behavior.
        checks.add(new InvNoSpecificInvsCheck()
                .disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.ALL_KINDS, NoInvariantPlaceKind.ALL_PLACES)
                .ignoreNeverBlockingInvariants());

        // Edge guards only if they are trivially true/false.
        checks.add(new EdgeOnlyStaticEvalGuardPredsCheck());

        // Non-deterministic automata may not be supported.
        if (!allowNonDeterminism) {
            checks.add(new AutOnlyDeterministicCheck());
        }

        // Only certain number of automata may be supported.
        if (expectedNumberOfAutomata != null) {
            switch (expectedNumberOfAutomata) {
                case EXACTLY_ONE_AUTOMATON:
                    checks.add(new SpecAutomataCountsCheck().setMinMaxAuts(1, 1));
                    break;
                case EXACTLY_TWO_AUTOMATA:
                    checks.add(new SpecAutomataCountsCheck().setMinMaxAuts(2, 2));
                    break;
                case AT_LEAST_ONE_AUTOMATON:
                    checks.add(new SpecAutomataCountsCheck().setMinMaxAuts(1, SpecAutomataCountsCheck.NO_CHANGE));
                    break;
                case AT_LEAST_ONE_PLANT_AUTOMATON:
                    checks.add(new SpecAutomataCountsCheck().setMinMaxPlantAuts(1, SpecAutomataCountsCheck.NO_CHANGE));
                    break;
                case AT_LEAST_TWO_AUTOMATA:
                    checks.add(new SpecAutomataCountsCheck().setMinMaxAuts(2, SpecAutomataCountsCheck.NO_CHANGE));
                    break;
                case AT_LEAST_ONE_PLANT_EXACTLY_ONE_SUPERVISOR:
                    checks.add(new SpecAutomataCountsCheck()
                            .setMinMaxPlantAuts(1, SpecAutomataCountsCheck.NO_CHANGE)
                            .setMinMaxSupervisorAuts(1, 1));
                    break;
            }
        }

        // Return all the checks.
        return checks;
    }

    /** Expected number of automata. */
    public static enum ExpectedNumberOfAutomata {
        /** Exactly one automaton. */
        EXACTLY_ONE_AUTOMATON,

        /** Exactly two automata. */
        EXACTLY_TWO_AUTOMATA,

        /** At least one automaton. */
        AT_LEAST_ONE_AUTOMATON,

        /** At least one plant automaton. */
        AT_LEAST_ONE_PLANT_AUTOMATON,

        /** At least two automata. */
        AT_LEAST_TWO_AUTOMATA,

        /** At least one plant automaton and exactly one supervisor automaton. */
        AT_LEAST_ONE_PLANT_EXACTLY_ONE_SUPERVISOR;
    }
}
