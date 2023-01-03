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

package org.eclipse.escet.cif.cif2uppaal;

import org.eclipse.escet.cif.common.CifPreconditionChecker;
import org.eclipse.escet.cif.common.checkers.checks.AutOnlyWithOneInitLocCheck;
import org.eclipse.escet.cif.common.checkers.checks.CompNoInitPredsCheck;
import org.eclipse.escet.cif.common.checkers.checks.EdgeNoUrgentCheck;
import org.eclipse.escet.cif.common.checkers.checks.EdgeOnlySimpleAssignmentsCheck;
import org.eclipse.escet.cif.common.checkers.checks.EventNoChannelsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.common.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.common.checkers.checks.SpecAutomataCountsCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.common.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.common.checkers.checks.VarDiscOnlyStaticEvalInitCheck;
import org.eclipse.escet.cif.common.checkers.checks.VarNoContinuousCheck;
import org.eclipse.escet.cif.common.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
import org.eclipse.escet.cif.common.checkers.checks.VarNoInputCheck;

/** CIF to UPPAAL transformation precondition checker. */
public class CifToUppaalPreChecker extends CifPreconditionChecker {
    /** Constructor for the {@link CifToUppaalPreChecker} class. */
    public CifToUppaalPreChecker() {
        super(
                // Specifications without automata are not supported.
                new SpecAutomataCountsCheck().setMinMaxAuts(1, Integer.MAX_VALUE),

                // Initialization predicates outside of locations are not supported.
                new CompNoInitPredsCheck(),

                // Automata that do not have exactly one initial location are not supported.
                new AutOnlyWithOneInitLocCheck(),

                // Discrete variables with multiple initial values are not supported.
                new VarNoDiscWithMultiInitValuesCheck(),

                // Discrete variables with initial values that cannot be evaluated statically are not supported. The
                // transformation statically evaluates the initial values, as variables in the initial values are not
                // supported by UPPAAL.
                new VarDiscOnlyStaticEvalInitCheck(),

                // Continuous variables are not supported.
                new VarNoContinuousCheck(),

                // Input variables are not supported.
                new VarNoInputCheck(),

                // Multi-assignments, partial variable assignments and conditional updates on edges are not supported.
                new EdgeOnlySimpleAssignmentsCheck(),

                // Urgent edges are not supported.
                new EdgeNoUrgentCheck(),

                // User-defined functions are not supported.
                new FuncNoSpecificUserDefCheck( //
                        NoSpecificUserDefFunc.INTERNAL, //
                        NoSpecificUserDefFunc.EXTERNAL),

                // Channels are not supported.
                new EventNoChannelsCheck(),

                // Only the following data types are supported: boolean types and integer types, and enumeration types.
                new TypeNoSpecificTypesCheck( //
                        NoSpecificType.DICT_TYPES, //
                        NoSpecificType.DIST_TYPES, //
                        NoSpecificType.FUNC_TYPES, //
                        NoSpecificType.LIST_TYPES, //
                        NoSpecificType.REAL_TYPES, //
                        NoSpecificType.SET_TYPES, //
                        NoSpecificType.STRING_TYPES, //
                        NoSpecificType.TUPLE_TYPES),

                // Only the following expressions are supported: boolean literal values, integer literal values,
                // enumeration literal values, binary expressions (partially, see below), unary expressions (partially,
                // see below), casts that don’t change the type, if expressions, and references to constants,
                // discrete variables, algebraic variables, and locations.
                new ExprNoSpecificExprsCheck( //
                        NoSpecificExpr.CAST_EXPRS_NON_EQUAL_TYPE, //
                        NoSpecificExpr.DICT_LITS, //
                        NoSpecificExpr.FUNC_CALLS, //
                        NoSpecificExpr.LIST_LITS, //
                        NoSpecificExpr.PROJECTION_EXPRS, //
                        NoSpecificExpr.REAL_LITS, //
                        NoSpecificExpr.SET_LITS, //
                        NoSpecificExpr.SLICE_EXPRS, //
                        NoSpecificExpr.STRING_LITS, //
                        NoSpecificExpr.SWITCH_EXPRS, //
                        NoSpecificExpr.TIME_VAR_REFS, //
                        NoSpecificExpr.TUPLE_LITS),

                // Only the following binary operators are supported: logical equivalence (<=>), logical implication
                // (=>), conjunction (and) on boolean operands, disjunction (or) on boolean operands, addition (+) on
                // integer operands, subtraction (-) on integer operands, multiplication (*) on integer operands,
                // integer division (div) on integer operands, integer modulus (mod) on integer operands, equality (=),
                // on integer and enumeration operands, inequality (!=) on integer and enumeration operands, less than
                // (<) on integer operands, less than or equal to (<=) on integer operands, greater than (>) on integer
                // operands, and greater than or equal to (>=) on integer operands.
                new ExprNoSpecificBinaryExprsCheck( //
                        NoSpecificBinaryOp.CONJUNCTION_SETS, //
                        NoSpecificBinaryOp.DISJUNCTION_SETS, //
                        NoSpecificBinaryOp.ADDITION_DICTS, //
                        NoSpecificBinaryOp.ADDITION_LISTS, //
                        NoSpecificBinaryOp.ADDITION_REALS, //
                        NoSpecificBinaryOp.ADDITION_STRINGS, //
                        NoSpecificBinaryOp.SUBTRACTION_DICTS, //
                        NoSpecificBinaryOp.SUBTRACTION_LISTS, //
                        NoSpecificBinaryOp.SUBTRACTION_REALS, //
                        NoSpecificBinaryOp.SUBTRACTION_SETS, //
                        NoSpecificBinaryOp.MULTIPLICATION_REALS, //
                        NoSpecificBinaryOp.EQUAL_BOOL, //
                        NoSpecificBinaryOp.EQUAL_DICT, //
                        NoSpecificBinaryOp.EQUAL_LIST, //
                        NoSpecificBinaryOp.EQUAL_REAL, //
                        NoSpecificBinaryOp.EQUAL_SET, //
                        NoSpecificBinaryOp.EQUAL_STRING, //
                        NoSpecificBinaryOp.EQUAL_TUPLE, //
                        NoSpecificBinaryOp.UNEQUAL_BOOL, //
                        NoSpecificBinaryOp.UNEQUAL_DICT, //
                        NoSpecificBinaryOp.UNEQUAL_LIST, //
                        NoSpecificBinaryOp.UNEQUAL_REAL, //
                        NoSpecificBinaryOp.UNEQUAL_SET, //
                        NoSpecificBinaryOp.UNEQUAL_STRING, //
                        NoSpecificBinaryOp.UNEQUAL_TUPLE, //
                        NoSpecificBinaryOp.GREATER_EQUAL_REALS, //
                        NoSpecificBinaryOp.GREATER_THAN_REALS, //
                        NoSpecificBinaryOp.LESS_EQUAL_REALS, //
                        NoSpecificBinaryOp.LESS_THAN_REALS, //
                        NoSpecificBinaryOp.DIVISION, //
                        NoSpecificBinaryOp.ELEMENT_OF, //
                        NoSpecificBinaryOp.SUBSET),

                // Only the following unary operators are supported: logical inverse (not), negation (-) on an integer
                // operand, and plus (+) on an integer operand.
                new ExprNoSpecificUnaryExprsCheck( //
                        NoSpecificUnaryOp.NEGATE_REALS, //
                        NoSpecificUnaryOp.PLUS_REALS, //
                        NoSpecificUnaryOp.SAMPLE)
        //
        );
    }
}
