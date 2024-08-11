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

package org.eclipse.escet.cif.checkers.checks;

import static org.eclipse.escet.cif.common.CifTextUtils.operatorToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifAnnotationUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;

/** CIF check that does not allow certain binary expressions. */
public class ExprNoSpecificBinaryExprsCheck extends CifCheck {
    /** The binary operators, or binary operators operating on certain operand types, to disallow. */
    private final EnumSet<NoSpecificBinaryOp> disalloweds;

    /** Whether to disable checking of binary expressions in annotations. */
    private boolean ignoreAnnotations;

    /**
     * Constructor for the {@link ExprNoSpecificBinaryExprsCheck} class.
     *
     * @param disalloweds The binary operators, or binary operators operating on certain operand types, to disallow.
     */
    public ExprNoSpecificBinaryExprsCheck(NoSpecificBinaryOp... disalloweds) {
        this(EnumSet.copyOf(Arrays.asList(disalloweds)));
    }

    /**
     * Constructor for the {@link ExprNoSpecificBinaryExprsCheck} class.
     *
     * @param disalloweds The binary operators, or binary operators operating on certain operand types, to disallow.
     */
    public ExprNoSpecificBinaryExprsCheck(EnumSet<NoSpecificBinaryOp> disalloweds) {
        this.disalloweds = disalloweds;
    }

    /**
     * Disable checking of binary expressions in annotations.
     *
     * @return The check instance, for daisy-chaining.
     */
    public ExprNoSpecificBinaryExprsCheck ignoreAnnotations() {
        return ignoreAnnotations(true);
    }

    /**
     * Configure whether to disable checking of binary expressions in annotations.
     *
     * @param ignore {@code true} to disable, {@code false} to enable.
     * @return The check instance, for daisy-chaining.
     */
    public ExprNoSpecificBinaryExprsCheck ignoreAnnotations(boolean ignore) {
        this.ignoreAnnotations = ignore;
        return this;
    }

    @Override
    protected void preprocessBinaryExpression(BinaryExpression binExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(binExpr)) {
            return;
        }

        // Do the check.
        BinaryOperator op = binExpr.getOperator();
        CifType ltype = CifTypeUtils.normalizeType(binExpr.getLeft().getType());
        CifType rtype = CifTypeUtils.normalizeType(binExpr.getRight().getType());
        switch (op) {
            case ADDITION:
                if (disalloweds.contains(NoSpecificBinaryOp.ADDITION)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, "a real typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_LISTS)
                            && (ltype instanceof ListType || rtype instanceof ListType))
                    {
                        addExprViolationOperand(binExpr, "a list typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_STRINGS)
                            && (ltype instanceof StringType || rtype instanceof StringType))
                    {
                        addExprViolationOperand(binExpr, "a string typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_DICTS)
                            && (ltype instanceof DictType || rtype instanceof DictType))
                    {
                        addExprViolationOperand(binExpr, "a dictionary typed", violations);
                    }
                }
                break;
            case BI_CONDITIONAL:
                if (disalloweds.contains(NoSpecificBinaryOp.BI_CONDITIONAL)) {
                    addExprViolationOperator(binExpr, violations);
                }
                break;
            case CONJUNCTION:
                if (disalloweds.contains(NoSpecificBinaryOp.CONJUNCTION)) {
                    addExprViolationOperator(binExpr, violations);
                } else if (disalloweds.contains(NoSpecificBinaryOp.CONJUNCTION_SETS)
                        && (ltype instanceof SetType || rtype instanceof SetType))
                {
                    addExprViolationOperand(binExpr, "a set typed", violations);
                }
                break;
            case DISJUNCTION:
                if (disalloweds.contains(NoSpecificBinaryOp.DISJUNCTION)) {
                    addExprViolationOperator(binExpr, violations);
                } else if (disalloweds.contains(NoSpecificBinaryOp.DISJUNCTION_SETS)
                        && (ltype instanceof SetType || rtype instanceof SetType))
                {
                    addExprViolationOperand(binExpr, "a set typed", violations);
                }
                break;
            case DIVISION:
                if (disalloweds.contains(NoSpecificBinaryOp.DIVISION)) {
                    addExprViolationOperator(binExpr, violations);
                }
                break;
            case ELEMENT_OF:
                if (disalloweds.contains(NoSpecificBinaryOp.ELEMENT_OF)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.ELEMENT_OF_LISTS)
                            && (ltype instanceof ListType || rtype instanceof ListType))
                    {
                        addExprViolationOperand(binExpr, "a list typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ELEMENT_OF_SETS)
                            && (ltype instanceof SetType || rtype instanceof SetType))
                    {
                        addExprViolationOperand(binExpr, "a set typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ELEMENT_OF_DICTS)
                            && (ltype instanceof DictType || rtype instanceof DictType))
                    {
                        addExprViolationOperand(binExpr, "a dictionary typed", violations);
                    }
                }
                break;
            case EQUAL:
                if (disalloweds.contains(NoSpecificBinaryOp.EQUAL)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_BOOL)) {
                        if (ltype instanceof BoolType || rtype instanceof BoolType) {
                            addExprViolationOperand(binExpr, "a boolean typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_DICT)) {
                        if (ltype instanceof DictType || rtype instanceof DictType) {
                            addExprViolationOperand(binExpr, "a dictionary typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_ENUM)) {
                        if (ltype instanceof EnumType || rtype instanceof EnumType) {
                            addExprViolationOperand(binExpr, "an enumeration typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_INT)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_INT_RANGED)) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType) && !CifTypeUtils.isRangeless((IntType)rtype))
                            {
                                addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                            }
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_INT_RANGELESS)) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType) && CifTypeUtils.isRangeless((IntType)rtype))
                            {
                                addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                            }
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_LIST)) {
                        if (ltype instanceof ListType || rtype instanceof ListType) {
                            addExprViolationOperand(binExpr, "a list typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_LIST_ARRAY)) {
                            if ((ltype instanceof ListType && CifTypeUtils.isArrayType((ListType)ltype))
                                    || (rtype instanceof ListType && CifTypeUtils.isArrayType((ListType)rtype)))
                            {
                                addExprViolationOperand(binExpr, "an array list typed", violations);
                            }
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_LIST_NON_ARRAY)) {
                            if ((ltype instanceof ListType && !CifTypeUtils.isArrayType((ListType)ltype))
                                    || (rtype instanceof ListType && !CifTypeUtils.isArrayType((ListType)rtype)))
                            {
                                addExprViolationOperand(binExpr, "a non-array list typed", violations);
                            }
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_REAL)) {
                        if (ltype instanceof RealType || rtype instanceof RealType) {
                            addExprViolationOperand(binExpr, "a real typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_SET)) {
                        if (ltype instanceof SetType || rtype instanceof SetType) {
                            addExprViolationOperand(binExpr, "a set typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_STRING)) {
                        if (ltype instanceof StringType || rtype instanceof StringType) {
                            addExprViolationOperand(binExpr, "a string typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.EQUAL_TUPLE)) {
                        if (ltype instanceof TupleType || rtype instanceof TupleType) {
                            addExprViolationOperand(binExpr, "a tuple typed", violations);
                        }
                    }
                }
                break;
            case GREATER_EQUAL:
                if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, "a real typed", violations);
                    }
                }
                break;
            case GREATER_THAN:
                if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, "a real typed", violations);
                    }
                }
                break;
            case IMPLICATION:
                if (disalloweds.contains(NoSpecificBinaryOp.IMPLICATION)) {
                    addExprViolationOperator(binExpr, violations);
                }
                break;
            case INTEGER_DIVISION:
                if (disalloweds.contains(NoSpecificBinaryOp.INTEGER_DIVISION)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.INTEGER_DIVISION_INTS_RANGED)
                            && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                    {
                        addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.INTEGER_DIVISION_INTS_RANGELESS)
                            && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                    {
                        addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.INTEGER_DIVISION_NON_POSITIVE_DIVISOR)
                            && (rtype instanceof IntType && CifTypeUtils.getLowerBound((IntType)rtype) < 1))
                    {
                        boolean alwaysNonPositive = CifTypeUtils.getUpperBound((IntType)rtype) < 1;
                        String operandTxt = fmt("a %snon-positive divisor", alwaysNonPositive ? "" : "possibly ");
                        addExprViolationOperand(binExpr, operandTxt, violations);
                    }
                }
                break;
            case LESS_EQUAL:
                if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, "a real typed", violations);
                    }
                }
                break;
            case LESS_THAN:
                if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, "a real typed", violations);
                    }
                }
                break;
            case MODULUS:
                if (disalloweds.contains(NoSpecificBinaryOp.MODULUS)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.MODULUS_INTS_RANGED)
                            && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                    {
                        addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.MODULUS_INTS_RANGELESS)
                            && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                    {
                        addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.MODULUS_NON_POSITIVE_DIVISOR)
                            && (rtype instanceof IntType && CifTypeUtils.getLowerBound((IntType)rtype) < 1))
                    {
                        boolean alwaysNonPositive = CifTypeUtils.getUpperBound((IntType)rtype) < 1;
                        String operandTxt = fmt("a %snon-positive divisor", alwaysNonPositive ? "" : "possibly ");
                        addExprViolationOperand(binExpr, operandTxt, violations);
                    }
                }
                break;
            case MULTIPLICATION:
                if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, "a real typed", violations);
                    }
                }
                break;
            case SUBSET:
                if (disalloweds.contains(NoSpecificBinaryOp.SUBSET)) {
                    addExprViolationOperator(binExpr, violations);
                }
                break;
            case SUBTRACTION:
                if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, "a real typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_LISTS)
                            && (ltype instanceof ListType || rtype instanceof ListType))
                    {
                        addExprViolationOperand(binExpr, "a list typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_SETS)
                            && (ltype instanceof SetType || rtype instanceof SetType))
                    {
                        addExprViolationOperand(binExpr, "a set typed", violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_DICTS)
                            && (ltype instanceof DictType || rtype instanceof DictType))
                    {
                        addExprViolationOperand(binExpr, "a dictionary typed", violations);
                    }
                }
                break;
            case UNEQUAL:
                if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_BOOL)) {
                        if (ltype instanceof BoolType || rtype instanceof BoolType) {
                            addExprViolationOperand(binExpr, "a boolean typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_DICT)) {
                        if (ltype instanceof DictType || rtype instanceof DictType) {
                            addExprViolationOperand(binExpr, "a dictionary typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_ENUM)) {
                        if (ltype instanceof EnumType || rtype instanceof EnumType) {
                            addExprViolationOperand(binExpr, "an enumeration typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_INT)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_INT_RANGELESS)) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType) && CifTypeUtils.isRangeless((IntType)rtype))
                            {
                                addExprViolationOperand(binExpr, "a rangeless integer typed", violations);
                            }
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_INT_RANGED)) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType) && !CifTypeUtils.isRangeless((IntType)rtype))
                            {
                                addExprViolationOperand(binExpr, "a ranged integer typed", violations);
                            }
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_LIST)) {
                        if (ltype instanceof ListType || rtype instanceof ListType) {
                            addExprViolationOperand(binExpr, "a list typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_LIST_ARRAY)) {
                            if ((ltype instanceof ListType && CifTypeUtils.isArrayType((ListType)ltype))
                                    || (rtype instanceof ListType && CifTypeUtils.isArrayType((ListType)rtype)))
                            {
                                addExprViolationOperand(binExpr, "an array list typed", violations);
                            }
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_LIST_NON_ARRAY)) {
                            if ((ltype instanceof ListType && !CifTypeUtils.isArrayType((ListType)ltype))
                                    || (rtype instanceof ListType && !CifTypeUtils.isArrayType((ListType)rtype)))
                            {
                                addExprViolationOperand(binExpr, "a non-array list typed", violations);
                            }
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_REAL)) {
                        if (ltype instanceof RealType || rtype instanceof RealType) {
                            addExprViolationOperand(binExpr, "a real typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_SET)) {
                        if (ltype instanceof SetType || rtype instanceof SetType) {
                            addExprViolationOperand(binExpr, "a set typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_STRING)) {
                        if (ltype instanceof StringType || rtype instanceof StringType) {
                            addExprViolationOperand(binExpr, "a string typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL_TUPLE)) {
                        if (ltype instanceof TupleType || rtype instanceof TupleType) {
                            addExprViolationOperand(binExpr, "a tuple typed", violations);
                        }
                    }
                }
                break;
            default:
                throw new RuntimeException("Unknown binary operator: " + op);
        }
    }

    /**
     * Add a violation for the operator of the given binary expression.
     *
     * @param binExpr The binary expression.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addExprViolationOperator(BinaryExpression binExpr, CifCheckViolations violations) {
        violations.add(binExpr, "Binary operator \"%s\" is used", operatorToStr(binExpr.getOperator()));
    }

    /**
     * Add a violation for an operand of the given binary expression.
     *
     * @param binExpr The binary expression.
     * @param operandTxt A text describing the kind of operand that is a violation.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addExprViolationOperand(BinaryExpression binExpr, String operandTxt, CifCheckViolations violations) {
        violations.add(binExpr, "Binary operator \"%s\" is used on %s operand", operatorToStr(binExpr.getOperator()),
                operandTxt);
    }

    /** The binary operator, or binary operator operating on certain operand types, to disallow. */
    public static enum NoSpecificBinaryOp {
        /** Disallow {@link BinaryOperator#ADDITION}. */
        ADDITION,

        /** Disallow {@link BinaryOperator#ADDITION} on integers. */
        ADDITION_INTS,

        /** Disallow {@link BinaryOperator#ADDITION} on ranged integers. */
        ADDITION_INTS_RANGED,

        /** Disallow {@link BinaryOperator#ADDITION} on rangeless integers. */
        ADDITION_INTS_RANGELESS,

        /** Disallow {@link BinaryOperator#ADDITION} on reals. */
        ADDITION_REALS,

        /** Disallow {@link BinaryOperator#ADDITION} on lists. */
        ADDITION_LISTS,

        /** Disallow {@link BinaryOperator#ADDITION} on strings. */
        ADDITION_STRINGS,

        /** Disallow {@link BinaryOperator#ADDITION} on dictionaries. */
        ADDITION_DICTS,

        /** Disallow {@link BinaryOperator#BI_CONDITIONAL}. */
        BI_CONDITIONAL,

        /** Disallow {@link BinaryOperator#CONJUNCTION}. */
        CONJUNCTION,

        /** Disallow {@link BinaryOperator#CONJUNCTION} on sets. */
        CONJUNCTION_SETS,

        /** Disallow {@link BinaryOperator#DISJUNCTION}. */
        DISJUNCTION,

        /** Disallow {@link BinaryOperator#DISJUNCTION} on sets. */
        DISJUNCTION_SETS,

        /** Disallow {@link BinaryOperator#DIVISION}. */
        DIVISION,

        /** Disallow {@link BinaryOperator#ELEMENT_OF}. */
        ELEMENT_OF,

        /** Disallow {@link BinaryOperator#ELEMENT_OF} on lists. */
        ELEMENT_OF_LISTS,

        /** Disallow {@link BinaryOperator#ELEMENT_OF} on sets. */
        ELEMENT_OF_SETS,

        /** Disallow {@link BinaryOperator#ELEMENT_OF} on dictionaries. */
        ELEMENT_OF_DICTS,

        /** Disallow {@link BinaryOperator#EQUAL}. */
        EQUAL,

        /** Disallow {@link BinaryOperator#EQUAL} on booleans. */
        EQUAL_BOOL,

        /** Disallow {@link BinaryOperator#EQUAL} on dictionaries. */
        EQUAL_DICT,

        /** Disallow {@link BinaryOperator#EQUAL} on enumerations. */
        EQUAL_ENUM,

        /** Disallow {@link BinaryOperator#EQUAL} on both ranged and rangeless integers. */
        EQUAL_INT,

        /** Disallow {@link BinaryOperator#EQUAL} on ranged integers. */
        EQUAL_INT_RANGED,

        /** Disallow {@link BinaryOperator#EQUAL} on rangeless integers. */
        EQUAL_INT_RANGELESS,

        /** Disallow {@link BinaryOperator#EQUAL} on both array and non-array lists. */
        EQUAL_LIST,

        /** Disallow {@link BinaryOperator#EQUAL} on array lists. */
        EQUAL_LIST_ARRAY,

        /** Disallow {@link BinaryOperator#EQUAL} on non-array lists. */
        EQUAL_LIST_NON_ARRAY,

        /** Disallow {@link BinaryOperator#EQUAL} on reals. */
        EQUAL_REAL,

        /** Disallow {@link BinaryOperator#EQUAL} on sets. */
        EQUAL_SET,

        /** Disallow {@link BinaryOperator#EQUAL} on strings. */
        EQUAL_STRING,

        /** Disallow {@link BinaryOperator#EQUAL} on tuples. */
        EQUAL_TUPLE,

        /** Disallow {@link BinaryOperator#GREATER_EQUAL}. */
        GREATER_EQUAL,

        /** Disallow {@link BinaryOperator#GREATER_EQUAL} on integers. */
        GREATER_EQUAL_INTS,

        /** Disallow {@link BinaryOperator#GREATER_EQUAL} on ranged integers. */
        GREATER_EQUAL_INTS_RANGED,

        /** Disallow {@link BinaryOperator#GREATER_EQUAL} on rangeless integers. */
        GREATER_EQUAL_INTS_RANGELESS,

        /** Disallow {@link BinaryOperator#GREATER_EQUAL} on reals. */
        GREATER_EQUAL_REALS,

        /** Disallow {@link BinaryOperator#GREATER_THAN}. */
        GREATER_THAN,

        /** Disallow {@link BinaryOperator#GREATER_THAN} on integers. */
        GREATER_THAN_INTS,

        /** Disallow {@link BinaryOperator#GREATER_THAN} on ranged integers. */
        GREATER_THAN_INTS_RANGED,

        /** Disallow {@link BinaryOperator#GREATER_THAN} on rangeless integers. */
        GREATER_THAN_INTS_RANGELESS,

        /** Disallow {@link BinaryOperator#GREATER_THAN} on reals. */
        GREATER_THAN_REALS,

        /** Disallow {@link BinaryOperator#IMPLICATION}. */
        IMPLICATION,

        /** Disallow {@link BinaryOperator#INTEGER_DIVISION}. */
        INTEGER_DIVISION,

        /** Disallow {@link BinaryOperator#INTEGER_DIVISION} on ranged integers. */
        INTEGER_DIVISION_INTS_RANGED,

        /** Disallow {@link BinaryOperator#INTEGER_DIVISION} on rangeless integers. */
        INTEGER_DIVISION_INTS_RANGELESS,

        /** Disallow {@link BinaryOperator#INTEGER_DIVISION} on non-positive divisors. */
        INTEGER_DIVISION_NON_POSITIVE_DIVISOR,

        /** Disallow {@link BinaryOperator#LESS_EQUAL}. */
        LESS_EQUAL,

        /** Disallow {@link BinaryOperator#LESS_EQUAL} on integers. */
        LESS_EQUAL_INTS,

        /** Disallow {@link BinaryOperator#LESS_EQUAL} on ranged integers. */
        LESS_EQUAL_INTS_RANGED,

        /** Disallow {@link BinaryOperator#LESS_EQUAL} on rangeless integers. */
        LESS_EQUAL_INTS_RANGELESS,

        /** Disallow {@link BinaryOperator#LESS_EQUAL} on reals. */
        LESS_EQUAL_REALS,

        /** Disallow {@link BinaryOperator#LESS_THAN}. */
        LESS_THAN,

        /** Disallow {@link BinaryOperator#LESS_THAN} on integers. */
        LESS_THAN_INTS,

        /** Disallow {@link BinaryOperator#LESS_THAN} on ranged integers. */
        LESS_THAN_INTS_RANGED,

        /** Disallow {@link BinaryOperator#LESS_THAN} on rangeless integers. */
        LESS_THAN_INTS_RANGELESS,

        /** Disallow {@link BinaryOperator#LESS_THAN} on reals. */
        LESS_THAN_REALS,

        /** Disallow {@link BinaryOperator#MODULUS}. */
        MODULUS,

        /** Disallow {@link BinaryOperator#MODULUS} on ranged integers. */
        MODULUS_INTS_RANGED,

        /** Disallow {@link BinaryOperator#MODULUS} on rangeless integers. */
        MODULUS_INTS_RANGELESS,

        /** Disallow {@link BinaryOperator#MODULUS} on non-positive divisors. */
        MODULUS_NON_POSITIVE_DIVISOR,

        /** Disallow {@link BinaryOperator#MULTIPLICATION}. */
        MULTIPLICATION,

        /** Disallow {@link BinaryOperator#MULTIPLICATION} on integers. */
        MULTIPLICATION_INTS,

        /** Disallow {@link BinaryOperator#MULTIPLICATION} on ranged integers. */
        MULTIPLICATION_INTS_RANGED,

        /** Disallow {@link BinaryOperator#MULTIPLICATION} on rangeless integers. */
        MULTIPLICATION_INTS_RANGELESS,

        /** Disallow {@link BinaryOperator#MULTIPLICATION} on reals. */
        MULTIPLICATION_REALS,

        /** Disallow {@link BinaryOperator#SUBSET}. */
        SUBSET,

        /** Disallow {@link BinaryOperator#SUBTRACTION}. */
        SUBTRACTION,

        /** Disallow {@link BinaryOperator#SUBTRACTION} on integers. */
        SUBTRACTION_INTS,

        /** Disallow {@link BinaryOperator#SUBTRACTION} on ranged integers. */
        SUBTRACTION_INTS_RANGED,

        /** Disallow {@link BinaryOperator#SUBTRACTION} on rangeless integers. */
        SUBTRACTION_INTS_RANGELESS,

        /** Disallow {@link BinaryOperator#SUBTRACTION} on reals. */
        SUBTRACTION_REALS,

        /** Disallow {@link BinaryOperator#SUBTRACTION} on lists. */
        SUBTRACTION_LISTS,

        /** Disallow {@link BinaryOperator#SUBTRACTION} on sets. */
        SUBTRACTION_SETS,

        /** Disallow {@link BinaryOperator#SUBTRACTION} on dictionaries. */
        SUBTRACTION_DICTS,

        /** Disallow {@link BinaryOperator#UNEQUAL}. */
        UNEQUAL,

        /** Disallow {@link BinaryOperator#UNEQUAL} on booleans. */
        UNEQUAL_BOOL,

        /** Disallow {@link BinaryOperator#UNEQUAL} on dictionaries. */
        UNEQUAL_DICT,

        /** Disallow {@link BinaryOperator#UNEQUAL} on enumerations. */
        UNEQUAL_ENUM,

        /** Disallow {@link BinaryOperator#UNEQUAL} on both ranged and rangeless integers. */
        UNEQUAL_INT,

        /** Disallow {@link BinaryOperator#UNEQUAL} on ranged integers. */
        UNEQUAL_INT_RANGED,

        /** Disallow {@link BinaryOperator#UNEQUAL} on rangeless integers. */
        UNEQUAL_INT_RANGELESS,

        /** Disallow {@link BinaryOperator#UNEQUAL} on both array and non-array lists. */
        UNEQUAL_LIST,

        /** Disallow {@link BinaryOperator#UNEQUAL} on array lists. */
        UNEQUAL_LIST_ARRAY,

        /** Disallow {@link BinaryOperator#UNEQUAL} on non-array lists. */
        UNEQUAL_LIST_NON_ARRAY,

        /** Disallow {@link BinaryOperator#UNEQUAL} on reals. */
        UNEQUAL_REAL,

        /** Disallow {@link BinaryOperator#UNEQUAL} on sets. */
        UNEQUAL_SET,

        /** Disallow {@link BinaryOperator#UNEQUAL} on strings. */
        UNEQUAL_STRING,

        /** Disallow {@link BinaryOperator#UNEQUAL} on tuples. */
        UNEQUAL_TUPLE,
    }
}
