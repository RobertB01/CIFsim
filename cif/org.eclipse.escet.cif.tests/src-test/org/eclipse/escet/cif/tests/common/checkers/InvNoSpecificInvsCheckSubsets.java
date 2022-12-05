//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.tests.common.checkers;

import org.eclipse.escet.cif.common.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantSupKind;

/** CIF check that disallows state requirement invariants in locations and 'need' state/event exclusion invariants. */
public class InvNoSpecificInvsCheckSubsets extends InvNoSpecificInvsCheck {
    /** Constructor of the {@InvNoSpecificInvsCheckAllowHarmless} class. */
    public InvNoSpecificInvsCheckSubsets() {
        super();

        disallow(NoInvariantSupKind.REQUIREMENT, NoInvariantKind.STATE, NoInvariantPlaceKind.LOCATIONS);
        disallow(NoInvariantSupKind.PLANT, NoInvariantKind.STATE_EVENT_NEEDS, NoInvariantPlaceKind.ALL_PLACES);
    }
}
