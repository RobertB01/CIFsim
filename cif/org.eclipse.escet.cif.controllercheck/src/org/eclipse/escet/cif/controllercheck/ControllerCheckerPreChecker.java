//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.AutOnlySpecificSupKindsCheck;
import org.eclipse.escet.cif.checkers.checks.EventOnlyWithControllabilityCheck;
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.common.java.Termination;

/** Precondition checker for the CIF controller properties checker. */
public class ControllerCheckerPreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link ControllerCheckerPreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public ControllerCheckerPreChecker(Termination termination) {
        super(termination,

                // Controllers should not have only plants and supervisors. Requirement are not allowed, and neither is
                // not defining the supervisory kind.
                new AutOnlySpecificSupKindsCheck(SupKind.PLANT, SupKind.SUPERVISOR), //
                new InvNoSpecificInvsCheck() //
                        .disallow(NoInvariantSupKind.KINDLESS, NoInvariantKind.ALL_KINDS,
                                NoInvariantPlaceKind.ALL_PLACES) //
                        .disallow(NoInvariantSupKind.REQUIREMENT, NoInvariantKind.ALL_KINDS,
                                NoInvariantPlaceKind.ALL_PLACES),

                // Disallow state invariants, as code generators do not support them, due to imposing constraints on the
                // target states of transitions, which can't be pre-computed on the source states.
                new InvNoSpecificInvsCheck() //
                        .ignoreNeverBlockingInvariants()
                        .disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.STATE, NoInvariantPlaceKind.ALL_PLACES),

                // Allow only controllable and uncontrollable events. Disallow kindless events and 'tau'.
                new EventOnlyWithControllabilityCheck(),

                // Require at least one automaton. Without any automata, the specification's alphabet is empty, and it
                // is not a (sensible) controller.
                new SpecAutomataCountsCheck().setMinMaxAuts(1, SpecAutomataCountsCheck.NO_CHANGE)
        //
        );
    }
}
