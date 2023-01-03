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

package org.eclipse.escet.cif.tests.common.checkers;

import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificIntUserDefFuncStatsCheck;

/** Test class for {@link FuncNoSpecificIntUserDefFuncStatsCheck} to check the 'break' statements check. */
public class FuncNoSpecificIntUserDefFuncStatsCheckBreak extends FuncNoSpecificIntUserDefFuncStatsCheck {
    /** Constructor of the {@link FuncNoSpecificIntUserDefFuncStatsCheckBreak} class. */
    public FuncNoSpecificIntUserDefFuncStatsCheckBreak() {
        super(NoSpecificStatement.BREAK);
    }
}
