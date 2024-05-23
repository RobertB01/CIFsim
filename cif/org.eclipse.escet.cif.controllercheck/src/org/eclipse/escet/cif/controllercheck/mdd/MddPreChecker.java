//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.mdd;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.EdgeNoMultiAssignCheck;
import org.eclipse.escet.cif.checkers.checks.EdgeNoPartialVarAssignCheck;
import org.eclipse.escet.cif.checkers.checks.EqnNotAllowedCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoChannelsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificBinaryExprsCheck.NoSpecificBinaryOp;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck.NoSpecificExpr;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck.NoSpecificUnaryOp;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck;
import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificUserDefCheck.NoSpecificUserDefFunc;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.checkers.checks.VarNoContinuousCheck;

/**
 * Pre-condition checker for the controller properties checker, for checks that use an MDD representation of the CIF
 * specification.
 */
public class MddPreChecker extends CifPreconditionChecker {
    /** Constructor for the {@link MddPreChecker} class. */
    public MddPreChecker() {
        super(
                // Channels (events with data types) are not supported.
                new EventNoChannelsCheck(),

                // Continuous variables are not supported.
                new VarNoContinuousCheck(),

                // Functions are not supported.
                new FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc.INTERNAL, NoSpecificUserDefFunc.INTERNAL),

                // Multi-assignments and partial variable assignments are not supported.
                new EdgeNoMultiAssignCheck(),
                new EdgeNoPartialVarAssignCheck(),

                // Equations are not supported.
                new EqnNotAllowedCheck(),

                // Restrict allowed types:
                // - Only boolean, ranged integer, and enumeration types are supported.
                // - Component types (in switch expressions) are allowed as well.
                // - No need to disallow component definition types, as component definitions are already eliminated.
                // - No need to disallow void types of channel uses, as channel declarations are already disallowed.
                new TypeNoSpecificTypesCheck(
                        NoSpecificType.DICT_TYPES,
                        NoSpecificType.DIST_TYPES,
                        NoSpecificType.FUNC_TYPES,
                        NoSpecificType.INT_TYPES_RANGELESS,
                        NoSpecificType.LIST_TYPES,
                        NoSpecificType.REAL_TYPES,
                        NoSpecificType.SET_TYPES,
                        NoSpecificType.STRING_TYPES,
                        NoSpecificType.TUPLE_TYPES)
                                .ignoreAnnotations(),

                // Restrict allowed expressions:
                // - Only the following expressions are supported: boolean literal values ('true' and 'false'), integer
                //   literal values, enumeration literal values, binary expressions (partially, see other check), unary
                //   expressions (partially, see other check), casts that don't change the type, 'if' expressions,
                //   'switch' expressions, and references to constants, discrete variables, input variables, algebraic
                //   variables, and locations.
                new ExprNoSpecificExprsCheck(
                        NoSpecificExpr.CAST_EXPRS_NON_EQUAL_TYPE,
                        NoSpecificExpr.DICT_LITS,
                        NoSpecificExpr.FUNC_CALLS,
                        NoSpecificExpr.LIST_LITS,
                        NoSpecificExpr.PROJECTION_EXPRS,
                        NoSpecificExpr.REAL_LITS,
                        NoSpecificExpr.SET_LITS,
                        NoSpecificExpr.SLICE_EXPRS,
                        NoSpecificExpr.STRING_LITS,
                        NoSpecificExpr.TIME_VAR_REFS,
                        NoSpecificExpr.TUPLE_LITS)
                                .ignoreAnnotations(),

                // Restrict allowed binary expressions:
                // - Only the following binary operators are supported: logical equivalence (<=>), logical implication
                //   (=>), logical conjunction ('and') on boolean operands, logical disjunction ('or') on boolean
                //   operands), addition (+) on integer operands, subtraction (-) on integer operands, multiplication
                //   (*) on integer operands, integer division (div), integer modulus (mod), equality (=) on boolean,
                //   integer or enumeration operands, inequality (!=) on boolean, integer or enumeration operands, less
                //   than (<) on integer operands, less than or equal to (<=) on integer operands, greater than (>) on
                //   integer operands, and greater than or equal to (>=) on integer operands.
                new ExprNoSpecificBinaryExprsCheck(
                        NoSpecificBinaryOp.ADDITION_INTS_RANGELESS,
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
                        NoSpecificBinaryOp.GREATER_EQUAL_INTS_RANGELESS,
                        NoSpecificBinaryOp.GREATER_EQUAL_REALS,
                        NoSpecificBinaryOp.GREATER_THAN_INTS_RANGELESS,
                        NoSpecificBinaryOp.GREATER_THAN_REALS,
                        NoSpecificBinaryOp.INTEGER_DIVISION_INTS_RANGELESS,
                        NoSpecificBinaryOp.LESS_EQUAL_INTS_RANGELESS,
                        NoSpecificBinaryOp.LESS_EQUAL_REALS,
                        NoSpecificBinaryOp.LESS_THAN_INTS_RANGELESS,
                        NoSpecificBinaryOp.LESS_THAN_REALS,
                        NoSpecificBinaryOp.MODULUS_INTS_RANGELESS,
                        NoSpecificBinaryOp.MULTIPLICATION_INTS_RANGELESS,
                        NoSpecificBinaryOp.MULTIPLICATION_REALS,
                        NoSpecificBinaryOp.SUBSET,
                        NoSpecificBinaryOp.SUBTRACTION_INTS_RANGELESS,
                        NoSpecificBinaryOp.SUBTRACTION_REALS,
                        NoSpecificBinaryOp.SUBTRACTION_LISTS,
                        NoSpecificBinaryOp.SUBTRACTION_SETS,
                        NoSpecificBinaryOp.SUBTRACTION_DICTS,
                        NoSpecificBinaryOp.UNEQUAL_DICT,
                        NoSpecificBinaryOp.UNEQUAL_LIST,
                        NoSpecificBinaryOp.UNEQUAL_REAL,
                        NoSpecificBinaryOp.UNEQUAL_SET,
                        NoSpecificBinaryOp.UNEQUAL_STRING,
                        NoSpecificBinaryOp.UNEQUAL_TUPLE)
                                .ignoreAnnotations(),

                // Restrict allowed unary expressions:
                // - Only the following unary operators are supported: logical inverse (not), negation (-) on an integer
                //   operand, and plus (+) on an integer operand.
                new ExprNoSpecificUnaryExprsCheck(
                        NoSpecificUnaryOp.NEGATE_INTS_RANGELESS,
                        NoSpecificUnaryOp.NEGATE_REALS,
                        NoSpecificUnaryOp.PLUS_INTS_RANGELESS,
                        NoSpecificUnaryOp.PLUS_REALS,
                        NoSpecificUnaryOp.SAMPLE)
                                .ignoreAnnotations()
        //
        );
    }
}
