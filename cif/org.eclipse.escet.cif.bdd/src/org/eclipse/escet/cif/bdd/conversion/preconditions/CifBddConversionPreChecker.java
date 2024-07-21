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

import java.util.function.BooleanSupplier;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.AutOnlySpecificSupKindsCheck;
import org.eclipse.escet.cif.checkers.checks.EventOnlyWithControllabilityCheck;
import org.eclipse.escet.cif.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.metamodel.cif.SupKind;

/** CIF to BDD conversion precondition checker. */
public class CifBddConversionPreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link CifBddConversionPreChecker} class.
     *
     * @param shouldTerminate Callback that indicates whether execution should be terminated on user request.
     */
    public CifBddConversionPreChecker(BooleanSupplier shouldTerminate) {
        super(shouldTerminate,

                // Only plant and requirement automata are supported.
                new AutOnlySpecificSupKindsCheck(SupKind.PLANT, SupKind.REQUIREMENT),

                // Specifications without plant automata are not supported.
                new SpecAutomataCountsCheck().setMinMaxPlantAuts(1, SpecAutomataCountsCheck.NO_CHANGE),

                // Events not declared as controllable or uncontrollable are not supported.
                new EventOnlyWithControllabilityCheck()

        );
    }
}
