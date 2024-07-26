//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.explorer;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificStdLibCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificStdLibCheck.NoSpecificStdLib;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.checkers.checks.SpecNoTooManyPossibleInitialStatesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.checkers.checks.VarNoInputCheck;
import org.eclipse.escet.common.java.Termination;

/** CIF explorer precondition checker. */
public class ExplorerPreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link ExplorerPreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public ExplorerPreChecker(Termination termination) {
        super(termination,

                // No distributions.
                new FuncNoSpecificStdLibCheck(NoSpecificStdLib.ALL_STOCHASTIC),
                new TypeNoSpecificTypesCheck(NoSpecificType.DIST_TYPES),

                // No derivatives.
                new ExprNoSpecificExprsCheck(NoSpecificExpr.CONT_VAR_DERIV_REFS),

                // No external user-defined functions.
                new FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc.EXTERNAL),

                // No input variables.
                new VarNoInputCheck(),

                // Specifications with too many potential initial states are not supported.
                new SpecNoTooManyPossibleInitialStatesCheck()

        );
    }
}
