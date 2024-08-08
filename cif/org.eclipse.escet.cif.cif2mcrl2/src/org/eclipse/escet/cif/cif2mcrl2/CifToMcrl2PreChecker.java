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

package org.eclipse.escet.cif.cif2mcrl2;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithCertainNumberOfInitLocsCheck;
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithCertainNumberOfInitLocsCheck.AllowedNumberOfInitLocs;
import org.eclipse.escet.cif.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeOnlySimpleAssignmentsCheck;
import org.eclipse.escet.cif.checkers.checks.EqnNotAllowedCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
import org.eclipse.escet.cif.checkers.checks.LocNoUrgentCheck;
import org.eclipse.escet.cif.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.checkers.checks.VarDiscOnlyStaticEvalInitCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoContinuousCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoInputCheck;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.common.java.Termination;

/** CIF to mCRL2 transformation precondition checker. */
public class CifToMcrl2PreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link CifToMcrl2PreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public CifToMcrl2PreChecker(Termination termination) {
        super(termination,

                // Only boolean, integer and enumeration types are supported.
                new TypeNoSpecificTypesCheck(
                        NoSpecificType.COMP_TYPES,
                        NoSpecificType.DICT_TYPES,
                        NoSpecificType.DIST_TYPES,
                        NoSpecificType.FUNC_TYPES,
                        NoSpecificType.LIST_TYPES,
                        NoSpecificType.REAL_TYPES,
                        NoSpecificType.SET_TYPES,
                        NoSpecificType.STRING_TYPES,
                        NoSpecificType.TUPLE_TYPES),

                // Discrete variables must not have multiple potential initial values.
                new VarNoDiscWithMultiInitValuesCheck(),

                // Initial values of discrete variables must be statically evaluable.
                new VarDiscOnlyStaticEvalInitCheck(),

                // Continuous variables are not supported.
                new VarNoContinuousCheck(),

                // Input variables are not supported.
                new VarNoInputCheck(),

                // Equations are not supported.
                new EqnNotAllowedCheck(),

                // There must be at least one automaton.
                new SpecAutomataCountsCheck().setMinMaxAuts(1, SpecAutomataCountsCheck.NO_CHANGE),

                // Exactly one initial location per automaton.
                new AutOnlyWithCertainNumberOfInitLocsCheck(AllowedNumberOfInitLocs.EXACTLY_ONE),

                // No conditional updates and multi-assignments.
                new EdgeOnlySimpleAssignmentsCheck(),

                // Only certain expression:
                // - For expressions that produce a boolean value, only boolean literals, constants (eliminated
                //   already), discrete variables, algebraic variables (eliminated already), received values (already
                //   eliminated), binary operators 'and', 'or', '=>', '<=>', '=', '!=', '<', '<=', '>' and '>=', unary
                //   operator 'not', location references (already eliminated), 'if' expressions, and casts that do not
                //   change the type are supported.
                // - For expressions that produce an enumeration value, only enumeration literals, constants (eliminated
                //   already), discrete variables, algebraic variables (eliminated already), received values (already
                //   eliminated), 'if' expressions, and casts that do not change the type are supported.
                // - For expressions that produce an integer value, only integer literals, constants (eliminated
                //   already), discrete variables, algebraic variables (eliminated already), received values (already
                //   eliminated), operators '+', '*' and '-', unary operators '-' and '+', 'if' expressions, and casts
                //   that do not change the type are supported.
                // - Unary and binary expressions are only supported with boolean, integer and enumeration operands.
                new ExprNoSpecificExprsCheck(
                        NoSpecificExpr.FUNC_REFS,
                        NoSpecificExpr.CAST_EXPRS_NON_EQUAL_TYPE,
                        NoSpecificExpr.COMP_REFS,
                        NoSpecificExpr.CONT_VAR_REFS,
                        NoSpecificExpr.DICT_LITS,
                        NoSpecificExpr.TUPLE_FIELD_REFS,
                        NoSpecificExpr.FUNC_CALLS,
                        NoSpecificExpr.INPUT_VAR_REFS,
                        NoSpecificExpr.LIST_LITS,
                        NoSpecificExpr.PROJECTION_EXPRS,
                        NoSpecificExpr.REAL_LITS,
                        NoSpecificExpr.SET_LITS,
                        NoSpecificExpr.SLICE_EXPRS,
                        NoSpecificExpr.STRING_LITS,
                        NoSpecificExpr.SWITCH_EXPRS,
                        NoSpecificExpr.TIME_VAR_REFS,
                        NoSpecificExpr.TUPLE_LITS),
                new ExprNoSpecificBinaryExprsCheck(
                        NoSpecificBinaryOp.ADDITION_DICTS,
                        NoSpecificBinaryOp.ADDITION_LISTS,
                        NoSpecificBinaryOp.ADDITION_REALS,
                        NoSpecificBinaryOp.ADDITION_STRINGS,
                        NoSpecificBinaryOp.CONJUNCTION_SETS,
                        NoSpecificBinaryOp.DISJUNCTION_SETS,
                        NoSpecificBinaryOp.DIVISION,
                        NoSpecificBinaryOp.ELEMENT_OF,
                        NoSpecificBinaryOp.EQUAL_DICT,
                        NoSpecificBinaryOp.EQUAL_LIST,
                        NoSpecificBinaryOp.EQUAL_REAL,
                        NoSpecificBinaryOp.EQUAL_SET,
                        NoSpecificBinaryOp.EQUAL_STRING,
                        NoSpecificBinaryOp.EQUAL_TUPLE,
                        NoSpecificBinaryOp.GREATER_EQUAL_REALS,
                        NoSpecificBinaryOp.GREATER_THAN_REALS,
                        NoSpecificBinaryOp.INTEGER_DIVISION,
                        NoSpecificBinaryOp.LESS_EQUAL_REALS,
                        NoSpecificBinaryOp.LESS_THAN_REALS,
                        NoSpecificBinaryOp.MODULUS,
                        NoSpecificBinaryOp.MULTIPLICATION_REALS,
                        NoSpecificBinaryOp.SUBSET,
                        NoSpecificBinaryOp.SUBTRACTION_DICTS,
                        NoSpecificBinaryOp.SUBTRACTION_LISTS,
                        NoSpecificBinaryOp.SUBTRACTION_REALS,
                        NoSpecificBinaryOp.SUBTRACTION_SETS,
                        NoSpecificBinaryOp.UNEQUAL_DICT,
                        NoSpecificBinaryOp.UNEQUAL_LIST,
                        NoSpecificBinaryOp.UNEQUAL_REAL,
                        NoSpecificBinaryOp.UNEQUAL_SET,
                        NoSpecificBinaryOp.UNEQUAL_STRING,
                        NoSpecificBinaryOp.UNEQUAL_TUPLE),
                new ExprNoSpecificUnaryExprsCheck(
                        NoSpecificUnaryOp.NEGATE_REALS,
                        NoSpecificUnaryOp.PLUS_REALS,
                        NoSpecificUnaryOp.SAMPLE),

                // No initialization predicates in components, unless they are trivially 'true'.
                new CompNoInitPredsCheck(true),

                // Only non-restrictive invariants.
                new InvNoSpecificInvsCheck()
                        .disallow(
                                NoInvariantSupKind.ALL_KINDS, NoInvariantKind.ALL_KINDS,
                                NoInvariantPlaceKind.ALL_PLACES)
                        .ignoreNeverBlockingInvariants(),

                // No urgency.
                new LocNoUrgentCheck(),
                new EdgeNoUrgentCheck()

        );
    }
}
