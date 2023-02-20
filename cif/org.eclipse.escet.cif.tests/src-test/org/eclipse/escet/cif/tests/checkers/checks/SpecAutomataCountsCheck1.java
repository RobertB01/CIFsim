//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.common.checkers.checks.SpecAutomataCountsCheck;

/**
 * Test checker requiring 2 or 3 automata, exactly 0 kindless, at least 1 plant, 1 requirement, and at most 1
 * supervisors.
 */
public class SpecAutomataCountsCheck1 extends SpecAutomataCountsCheck {
    /** Constructor of the {@link SpecAutomataCountsCheck1} class. */
    public SpecAutomataCountsCheck1() {
        setMinMaxAuts(2, 3);
        setMinMaxKindlessAuts(NO_CHANGE, 0);
        setMinMaxPlantAuts(1, NO_CHANGE);
        setMinMaxRequirementAuts(1, 1);
        setMinMaxSupervisorAuts(0, 1);
    }
}
