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
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.LocNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.LocOnlyStaticEvalInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.LocOnlyStaticEvalMarkerPredsCheck;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.common.java.Termination;

/** CIF to event-based conversion precondition checker. */
public class ConvertToEventBasedPreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link ConvertToEventBasedPreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public ConvertToEventBasedPreChecker(Termination termination) {
        super(termination, getChecks());
    }

    /**
     * Get the checks to use.
     *
     * @return The checks to use.
     */
    private static List<CifCheck> getChecks() {
        List<CifCheck> checks = list();

        // Channels are not supported.
        checks.add(new EventNoChannelsCheck());

        // Event 'tau' is not supported.
        checks.add(new EventNoTauCheck());

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

        // Return all the checks.
        return checks;
    }
}
