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
                new VarNoInputCheck()

        );
    }

//    /** Found problems in the specification. */
//    private Set<String> problems = null;
//
//    /**
//     * Constructor of the {@link ExplorerPreChecker} class.
//     *
//     * @param params Set of configurable parameters for the explorer pre-checker.
//     */
//    public ExplorerPreChecker(EnumSet<CheckParameters> params) {
//        this.params = params;
//    }
//
//    /**
//     * Check the specification for the requirements.
//     *
//     * @param spec Specification to check.
//     */
//    public void checkSpec(Specification spec) {
//        problems = set();
//        walkSpecification(spec);
//
//        if (problems.isEmpty()) {
//            return;
//        }
//
//        // If we have any problems, the specification is unsupported.
//        String msg = "State space exploration failed due to unsatisfied preconditions:\n - "
//                + String.join("\n - ", sortedstrings(problems));
//        throw new UnsupportedException(msg);
//    }
//
//    @Override
//    protected void preprocessDiscVariable(DiscVariable var) {
//        // Single initial value is always OK.
//        if (var.getValue() == null) {
//            return;
//        }
//        List<Expression> initVals = var.getValue().getValues();
//        if (initVals.size() == 1) {
//            return;
//        }
//
//        // Finite number of potential initial values is always OK.
//        if (!initVals.isEmpty()) {
//            return;
//        }
//
//        // Any value in its domain. Check for finite type. That is, the number
//        // of possible values must fit within the range of 'int', to ensure we
//        // can store the potential values in a list.
//        double cnt = CifValueUtils.getPossibleValueCount(var.getType());
//        if (cnt <= Integer.MAX_VALUE) {
//            return;
//        }
//
//        // Unsupported type for multiple initial values.
//        String cntTxt = Double.isInfinite(cnt) ? "infinite" : CifMath.realToStr(cnt);
//        String msg = fmt("Discrete variable \"%s\" of type \"%s\" with %s potential initial values is not supported.",
//                CifTextUtils.getAbsName(var), CifTextUtils.typeToStr(var.getType()), cntTxt);
//        problems.add(msg);
//    }
}
