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

/** CIF check that disallows requirement state invariants in locations and 'needs' state/event exclusion invariants. */
public class InvNoSpecificInvsCheckSubsets extends InvNoSpecificInvsCheck {
    /** Constructor of the {@link InvNoSpecificInvsCheckSubsets} class. */
    public InvNoSpecificInvsCheckSubsets() {
        disallow(NoInvariantSupKind.REQUIREMENT, NoInvariantKind.STATE, NoInvariantPlaceKind.LOCATIONS);
        disallow(NoInvariantSupKind.PLANT, NoInvariantKind.STATE_EVENT_NEEDS, NoInvariantPlaceKind.ALL_PLACES);
    }
}
