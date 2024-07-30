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
import org.eclipse.escet.cif.checkers.checks.AutOnlyWithOneInitLocCheck;
import org.eclipse.escet.cif.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeOnlySimpleAssignmentsCheck;
import org.eclipse.escet.cif.checkers.checks.EqnNotAllowedCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoChannelsCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoTauCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.checkers.checks.InvNoSpecificInvsCheck;
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

                // Channels are not supported.
                new EventNoChannelsCheck(),

                // 'Tau' events are not supported.
                new EventNoTauCheck(),

                // There must be at least one automaton.
                new SpecAutomataCountsCheck().setMinMaxAuts(1, SpecAutomataCountsCheck.NO_CHANGE),

                // Exactly one initial location per automaton.
                new AutOnlyWithOneInitLocCheck(),

                // No conditional updates and multi-assignments.
                new EdgeOnlySimpleAssignmentsCheck(),

                // Only certain expression:
                // - For expressions that produce a boolean value, only boolean literals, constants (eliminated
                //   already), discrete variables, algebraic variables (eliminated already), binary operators 'and',
                //   'or', '=>', '=', '!=', '<', '<=', '>' and '>=', and unary operator 'not' are supported.
                // - For expressions that produce an enumeration value, only enumeration literals, constants (eliminated
                //   already), discrete variables, and algebraic variables (eliminated already) are supported.
                // - For expressions that produce an integer value, only integer literals, constants (eliminated
                //   already), discrete variables, algebraic variables (eliminated already), binary operators '+', '*'
                //   and '-', and unary operators '-' and '+' are supported.
                // - Unary and binary expressions are only supported with boolean, integer and enumeration operands.
                new ExprNoSpecificExprsCheck(
                        NoSpecificExpr.FUNC_REFS,
                        NoSpecificExpr.CAST_EXPRS,
                        NoSpecificExpr.COMP_REFS,
                        NoSpecificExpr.CONT_VAR_REFS,
                        NoSpecificExpr.DICT_LITS,
                        NoSpecificExpr.TUPLE_FIELD_REFS,
                        NoSpecificExpr.FUNC_CALLS,
                        NoSpecificExpr.IF_EXPRS,
                        NoSpecificExpr.INPUT_VAR_REFS,
                        NoSpecificExpr.LIST_LITS,
                        NoSpecificExpr.LOC_REFS,
                        NoSpecificExpr.PROJECTION_EXPRS,
                        NoSpecificExpr.REAL_LITS,
                        NoSpecificExpr.RECEIVE_EXPRS,
                        NoSpecificExpr.SET_LITS,
                        NoSpecificExpr.SLICE_EXPRS,
                        NoSpecificExpr.STRING_LITS,
                        NoSpecificExpr.SWITCH_EXPRS,
                        NoSpecificExpr.TIME_VAR_REFS,
                        NoSpecificExpr.TUPLE_LITS),
                new ExprNoSpecificBinaryExprsCheck(
                        NoSpecificBinaryOp.ADDITION_REALS,
                        NoSpecificBinaryOp.ADDITION_LISTS,
                        NoSpecificBinaryOp.ADDITION_STRINGS,
                        NoSpecificBinaryOp.ADDITION_DICTS,
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
                        NoSpecificBinaryOp.SUBTRACTION_REALS,
                        NoSpecificBinaryOp.SUBTRACTION_LISTS,
                        NoSpecificBinaryOp.SUBTRACTION_SETS,
                        NoSpecificBinaryOp.SUBTRACTION_DICTS,
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

                // No initialization predicates in components.
                new CompNoInitPredsCheck(),

                // No invariants.
                new InvNoSpecificInvsCheck().disallow(
                        NoInvariantSupKind.ALL_KINDS, NoInvariantKind.ALL_KINDS, NoInvariantPlaceKind.ALL_PLACES)

        );
    }
}
