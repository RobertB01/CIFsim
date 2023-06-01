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

import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificIntUserDefFuncStatsCheck;

/** Test class for {@link FuncNoSpecificIntUserDefFuncStatsCheck} to check the partial assignments check. */
public class FuncNoSpecificIntUserDefFuncStatsCheckAssign extends FuncNoSpecificIntUserDefFuncStatsCheck {
    /** Constructor of the {@link FuncNoSpecificIntUserDefFuncStatsCheckAssign} class. */
    public FuncNoSpecificIntUserDefFuncStatsCheckAssign() {
        super(NoSpecificStatement.ASSIGN_MULTI_PARTS_SAME_VAR);
    }
}
