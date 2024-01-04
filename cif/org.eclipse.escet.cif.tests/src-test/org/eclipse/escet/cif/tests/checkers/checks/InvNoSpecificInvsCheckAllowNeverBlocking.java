//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.tests.checkers.checks;

import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;

/** CIF check that only allows invariants that never block any behavior after static analysis. */
public class InvNoSpecificInvsCheckAllowNeverBlocking extends InvNoSpecificInvsCheck {
    /** Constructor of the {@link InvNoSpecificInvsCheckAllowNeverBlocking} class. */
    public InvNoSpecificInvsCheckAllowNeverBlocking() {
        disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.ALL_KINDS, NoInvariantPlaceKind.ALL_PLACES);
        ignoreNeverBlockingInvariants();
    }
}
