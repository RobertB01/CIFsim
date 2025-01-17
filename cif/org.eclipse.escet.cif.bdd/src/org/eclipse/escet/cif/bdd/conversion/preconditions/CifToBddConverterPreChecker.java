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

package org.eclipse.escet.cif.bdd.conversion.preconditions;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.AutOnlySpecificSupKindsCheck;
import org.eclipse.escet.cif.checkers.checks.AutReqNoChannelCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeOnlySimpleAssignmentsCheck;
import org.eclipse.escet.cif.checkers.checks.EventOnlyWithControllabilityCheck;
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoContinuousCheck;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.common.java.Termination;

/** CIF to BDD conversion precondition checker. */
public class CifToBddConverterPreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link CifToBddConverterPreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public CifToBddConverterPreChecker(Termination termination) {
        super(termination,

                // Only plant and requirement automata are supported.
                new AutOnlySpecificSupKindsCheck(SupKind.PLANT, SupKind.REQUIREMENT),

                // Specifications without plant automata are not supported.
                new SpecAutomataCountsCheck().setMinMaxPlantAuts(1, SpecAutomataCountsCheck.NO_CHANGE),

                // Events not declared as controllable or uncontrollable are not supported.
                new EventOnlyWithControllabilityCheck(),

                // Requirement automata that are senders or receivers for channels are not supported.
                new AutReqNoChannelCheck(),

                // Only plant and requirement invariants are supported.
                new InvNoSpecificInvsCheck()
                        .disallow(NoInvariantSupKind.KINDLESS, NoInvariantKind.ALL_KINDS,
                                NoInvariantPlaceKind.ALL_PLACES)
                        .disallow(NoInvariantSupKind.SUPERVISOR, NoInvariantKind.ALL_KINDS,
                                NoInvariantPlaceKind.ALL_PLACES),

                // Continuous variables are not supported.
                new VarNoContinuousCheck(),

                // Discrete and input variables are only supported if they have a boolean, non-negative ranged integer
                // or enumeration type.
                new CifToBddVarOnlySpecificTypesCheck(),

                // Only allow supported expressions/predicates:
                // - Only limited forms of predicates (for markers, invariants, initialization, guards, initial values
                //   of boolean variables, right hand sides of assignments, and send values) are supported.
                // - Only limited forms of integer and enumeration expressions (for binary comparisons, initial values
                //   of variables, right hand sides of assignments, and send values) are supported.
                new CifToBddExprOnlySupportedExprsCheck(),

                // Conditional updates (if updates), multi-assignments, and partial variable assignments are not
                // supported.
                new EdgeOnlySimpleAssignmentsCheck()

        );
    }
}
