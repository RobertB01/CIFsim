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

import org.eclipse.escet.cif.common.checkers.checks.SpecAutomataCounts;

/**
 * Test checker requiring 2 or 3 automata, exactly 0 kindless, at least 1 plant, 1 requirement, and at most 1
 * supervisors.
 */
public class SpecAutomataCounts1 extends SpecAutomataCounts {
    /** Constructor of the {@link SpecAutomataCounts1} class. */
    public SpecAutomataCounts1() {
        super();
        setMinMaxAuts(2, 3);
        setMinMaxKindLessAuts(NO_CHANGE, 0);
        setMinMaxPlantAuts(1, NO_CHANGE);
        setMinMaxRequirementAuts(1, 1);
        setMinMaxSupervisorAuts(0, 1);
    }
}
