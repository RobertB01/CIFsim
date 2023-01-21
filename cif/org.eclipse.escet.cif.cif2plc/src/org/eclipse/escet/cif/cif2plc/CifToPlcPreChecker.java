//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2plc;

import org.eclipse.escet.cif.cif2plc.options.PlcOutputTypeOption;
import org.eclipse.escet.cif.common.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.common.checkers.checks.AutOnlyWithOneInitLocCheck;
import org.eclipse.escet.cif.common.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.common.checkers.checks.EdgeNoUrgentCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificIntUserDefFuncStatsCheck;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificIntUserDefFuncStatsCheck.NoSpecificStatement;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificStdLibCheck;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificStdLibCheck.NoSpecificStdLib;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.common.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.common.checkers.checks.LocNoUrgentCheck;
import org.eclipse.escet.cif.common.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.common.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantSupKind;

/** CIF PLC code generator precondition checker. Does not support component definition/instantiation. */
public class CifToPlcPreChecker extends CifPreconditionChecker {
    /** Constructor of the {@link CifToPlcPreChecker} class. */
    public CifToPlcPreChecker() {
        super(
                // At least one automaton.
                new SpecAutomataCountsCheck().setMinMaxAuts(1, SpecAutomataCountsCheck.NO_CHANGE),

                // No initialization predicates in components, ignoring initialization predicates that trivially hold.
                new CompNoInitPredsCheck(true),

                // Discrete variables must have a single initial value.
                new VarNoDiscWithMultiInitValuesCheck(),

                // Automata must have a single initial location.
                new AutOnlyWithOneInitLocCheck(),

                // Disallow state invariants, except ones that never block behavior.
                new InvNoSpecificInvsCheck() //
                        .ignoreNeverBlockingInvariants() //
                        .disallow(NoInvariantSupKind.ALL_KINDS, NoInvariantKind.STATE, NoInvariantPlaceKind.ALL_PLACES),

                // No urgency.
                new LocNoUrgentCheck(), //
                new EdgeNoUrgentCheck(),

                // Disallow external user-defined functions, and only allow internal user-defined functions with at
                // least one parameter.
                new FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc.EXTERNAL, NoSpecificUserDefFunc.NO_PARAMETER),

                // Limit internal user-defined function assignments and disallow the 'continue' statement.
                //
                // We use CifAddressableUtils.getRefs in the code generation, which doesn't properly handle
                // multi-assignments to different non-overlapping parts of the same variable.
                new FuncNoSpecificIntUserDefFuncStatsCheck( //
                        NoSpecificStatement.ASSIGN_MULTI_PARTS_SAME_VAR, //
                        NoSpecificStatement.CONTINUE),

                // Disallow various types completely and function types in non-call context.
                new TypeNoSpecificTypesCheck( //
                        NoSpecificType.DICT_TYPES, //
                        NoSpecificType.DIST_TYPES, //
                        NoSpecificType.FUNC_TYPES_AS_DATA, //
                        NoSpecificType.SET_TYPES, //
                        NoSpecificType.STRING_TYPES, //
                        // S7 transformation doesn't support list types. That is because S7 doesn't support functions
                        // returning arrays and doesn't support arrays of arrays.
                        (PlcOutputTypeOption.isS7Output() ? NoSpecificType.LIST_TYPES
                                : NoSpecificType.LIST_TYPES_NON_ARRAY)),

                // Allow only casting to the same type and int to real, allow projection only on tuples and arrays,
                // forbid string, set, and dictionary literals, forbid slicing, and function references outside call
                // context.
                new ExprNoSpecificExprsCheck( //
                        NoSpecificExpr.CAST_EXPRS_FROM_STRING, //
                        NoSpecificExpr.CAST_EXPRS_TO_STRING, //
                        NoSpecificExpr.DICT_LITS, //
                        NoSpecificExpr.FUNC_REFS_USER_DEF_AS_DATA, //
                        NoSpecificExpr.PROJECTION_EXPRS_DICTS, //
                        NoSpecificExpr.PROJECTION_EXPRS_LISTS_NON_ARRAY, //
                        NoSpecificExpr.PROJECTION_EXPRS_STRINGS, //
                        NoSpecificExpr.SET_LITS, //
                        NoSpecificExpr.STRING_LITS, //
                        NoSpecificExpr.SLICE_EXPRS),

                // Disallow sampling.
                new ExprNoSpecificUnaryExprsCheck(NoSpecificUnaryOp.SAMPLE),

                // Disallow element of, and subset operators. Allow conjunction and disjunction only on booleans, allow
                // equality only on booleans, integers, reals and enums, allow addition and subtraction only on integers
                // and reals.
                new ExprNoSpecificBinaryExprsCheck( //
                        NoSpecificBinaryOp.ADDITION_LISTS, //
                        NoSpecificBinaryOp.ADDITION_STRINGS, //
                        NoSpecificBinaryOp.ADDITION_DICTS, //
                        NoSpecificBinaryOp.ELEMENT_OF, //
                        NoSpecificBinaryOp.EQUAL_DICT, //
                        NoSpecificBinaryOp.EQUAL_LIST, //
                        NoSpecificBinaryOp.EQUAL_SET, //
                        NoSpecificBinaryOp.EQUAL_STRING, //
                        NoSpecificBinaryOp.EQUAL_TUPLE, //
                        NoSpecificBinaryOp.SUBSET, //
                        NoSpecificBinaryOp.SUBTRACTION_DICTS, //
                        NoSpecificBinaryOp.SUBTRACTION_LISTS, //
                        NoSpecificBinaryOp.SUBTRACTION_SETS, //
                        NoSpecificBinaryOp.CONJUNCTION_SETS, //
                        NoSpecificBinaryOp.DISJUNCTION_SETS, //
                        NoSpecificBinaryOp.UNEQUAL_DICT, //
                        NoSpecificBinaryOp.UNEQUAL_LIST, //
                        NoSpecificBinaryOp.UNEQUAL_SET, //
                        NoSpecificBinaryOp.UNEQUAL_STRING, //
                        NoSpecificBinaryOp.UNEQUAL_TUPLE),

                // Limit standard library functions.
                new FuncNoSpecificStdLibCheck( //
                        NoSpecificStdLib.STD_LIB_STOCHASTIC_GROUP, //
                        NoSpecificStdLib.STD_LIB_ACOSH, //
                        NoSpecificStdLib.STD_LIB_ASINH, //
                        NoSpecificStdLib.STD_LIB_ATANH, //
                        NoSpecificStdLib.STD_LIB_COSH, //
                        NoSpecificStdLib.STD_LIB_SINH, //
                        NoSpecificStdLib.STD_LIB_TANH, //
                        NoSpecificStdLib.STD_LIB_CEIL, //
                        NoSpecificStdLib.STD_LIB_DELETE, //
                        NoSpecificStdLib.STD_LIB_EMPTY, //
                        NoSpecificStdLib.STD_LIB_FLOOR, //
                        NoSpecificStdLib.STD_LIB_FORMAT, //
                        NoSpecificStdLib.STD_LIB_POP, //
                        NoSpecificStdLib.STD_LIB_ROUND, //
                        NoSpecificStdLib.STD_LIB_SCALE, //
                        NoSpecificStdLib.STD_LIB_SIGN, //
                        NoSpecificStdLib.STD_LIB_SIZE)
        //
        );
    }
}
