//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.operatorToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;

/** CIF check that does not allow certain binary expressions. */
public class ExprNoSpecificBinaryExprsCheck extends CifCheck {
    /** The binary operators, or binary operators operating on certain operand types, to disallow. */
    private final EnumSet<NoSpecificBinaryOp> disalloweds;

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

    @Override
    protected void preprocessBinaryExpression(BinaryExpression binExpr, CifCheckViolations violations) {
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
                            addExprViolationOperand(binExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_LISTS)
                            && (ltype instanceof ListType || rtype instanceof ListType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_STRINGS)
                            && (ltype instanceof StringType || rtype instanceof StringType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ADDITION_DICTS)
                            && (ltype instanceof DictType || rtype instanceof DictType))
                    {
                        addExprViolationOperand(binExpr, violations);
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
                    addExprViolationOperand(binExpr, violations);
                }
                break;
            case DISJUNCTION:
                if (disalloweds.contains(NoSpecificBinaryOp.DISJUNCTION)) {
                    addExprViolationOperator(binExpr, violations);
                } else if (disalloweds.contains(NoSpecificBinaryOp.DISJUNCTION_SETS)
                        && (ltype instanceof SetType || rtype instanceof SetType))
                {
                    addExprViolationOperand(binExpr, violations);
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
                        addExprViolationOperand(binExpr, violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ELEMENT_OF_SETS)
                            && (ltype instanceof SetType || rtype instanceof SetType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.ELEMENT_OF_DICTS)
                            && (ltype instanceof DictType || rtype instanceof DictType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                }
                break;
            case EQUAL:
                if (disalloweds.contains(NoSpecificBinaryOp.EQUAL)) {
                    addExprViolationOperator(binExpr, violations);
                }
                break;
            case GREATER_EQUAL:
                if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.GREATER_EQUAL_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                }
                break;
            case GREATER_THAN:
                if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.GREATER_THAN_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, violations);
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
                    if (disalloweds.contains(NoSpecificBinaryOp.INTEGER_DIVISION_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.INTEGER_DIVISION_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.INTEGER_DIVISION_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                    }
                }
                break;
            case LESS_EQUAL:
                if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.LESS_EQUAL_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                }
                break;
            case LESS_THAN:
                if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.LESS_THAN_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                }
                break;
            case MODULUS:
                if (disalloweds.contains(NoSpecificBinaryOp.MODULUS)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.MODULUS_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.MODULUS_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.MODULUS_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                    }
                }
                break;
            case MULTIPLICATION:
                if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION)) {
                    addExprViolationOperator(binExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION_INTS)) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.MULTIPLICATION_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, violations);
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
                            addExprViolationOperand(binExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_INTS_RANGED)
                                && ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_INTS_RANGELESS)
                                && ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                        || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype))))
                        {
                            addExprViolationOperand(binExpr, violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_REALS)
                            && (ltype instanceof RealType || rtype instanceof RealType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_LISTS)
                            && (ltype instanceof ListType || rtype instanceof ListType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_SETS)
                            && (ltype instanceof SetType || rtype instanceof SetType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                    if (disalloweds.contains(NoSpecificBinaryOp.SUBTRACTION_DICTS)
                            && (ltype instanceof DictType || rtype instanceof DictType))
                    {
                        addExprViolationOperand(binExpr, violations);
                    }
                }
                break;
            case UNEQUAL:
                if (disalloweds.contains(NoSpecificBinaryOp.UNEQUAL)) {
                    addExprViolationOperator(binExpr, violations);
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
        violations.add(binExpr, new LiteralMessage("uses binary operator \"%s\" in binary expression \"%s\"",
                operatorToStr(binExpr.getOperator()), exprToStr(binExpr)));
    }

    /**
     * Add a violation for an operand of the the given binary expression.
     *
     * @param binExpr The binary expression.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addExprViolationOperand(BinaryExpression binExpr, CifCheckViolations violations) {
        CifType ltype = CifTypeUtils.normalizeType(binExpr.getLeft().getType());
        CifType rtype = CifTypeUtils.normalizeType(binExpr.getRight().getType());
        violations.add(binExpr, new LiteralMessage(
                "uses binary operator \"%s\" on operands of types \"%s\" and \"%s\" in binary expression \"%s\"",
                operatorToStr(binExpr.getOperator()), typeToStr(ltype), typeToStr(rtype), exprToStr(binExpr)));
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

        /** Disallow {@link BinaryOperator#INTEGER_DIVISION} on integers. */
        INTEGER_DIVISION_INTS,

        /** Disallow {@link BinaryOperator#INTEGER_DIVISION} on ranged integers. */
        INTEGER_DIVISION_INTS_RANGED,

        /** Disallow {@link BinaryOperator#INTEGER_DIVISION} on rangeless integers. */
        INTEGER_DIVISION_INTS_RANGELESS,

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

        /** Disallow {@link BinaryOperator#MODULUS} on integers. */
        MODULUS_INTS,

        /** Disallow {@link BinaryOperator#MODULUS} on ranged integers. */
        MODULUS_INTS_RANGED,

        /** Disallow {@link BinaryOperator#MODULUS} on rangeless integers. */
        MODULUS_INTS_RANGELESS,

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
    }
}
