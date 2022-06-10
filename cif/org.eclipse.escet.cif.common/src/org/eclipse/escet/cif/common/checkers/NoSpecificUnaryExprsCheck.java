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

package org.eclipse.escet.cif.common.checkers;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getNamedSelfOrAncestor;
import static org.eclipse.escet.cif.common.CifTextUtils.operatorToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;

/** CIF check that does not allow certain unary expressions. */
public class NoSpecificUnaryExprsCheck extends CifCheck {
    /** Whether to disallow {@link UnaryOperator#INVERSE}. */
    public boolean disallowInverse;

    /** Whether to disallow {@link UnaryOperator#NEGATE}. */
    public boolean disallowNegate;

    /** Whether to disallow {@link UnaryOperator#NEGATE} on integer numbers. */
    public boolean disallowNegateInts;

    /** Whether to disallow {@link UnaryOperator#NEGATE} on ranged integer numbers. */
    public boolean disallowNegateIntsRanged;

    /** Whether to disallow {@link UnaryOperator#NEGATE} on rangeless integer numbers. */
    public boolean disallowNegateIntsRangeless;

    /** Whether to disallow {@link UnaryOperator#NEGATE} on real numbers. */
    public boolean disallowNegateReals;

    /** Whether to disallow {@link UnaryOperator#PLUS}. */
    public boolean disallowPlus;

    /** Whether to disallow {@link UnaryOperator#PLUS} on integer numbers. */
    public boolean disallowPlusInts;

    /** Whether to disallow {@link UnaryOperator#PLUS} on ranged integer numbers. */
    public boolean disallowPlusIntsRanged;

    /** Whether to disallow {@link UnaryOperator#PLUS} on rangeless integer numbers. */
    public boolean disallowPlusIntsRangeless;

    /** Whether to disallow {@link UnaryOperator#PLUS} on real numbers. */
    public boolean disallowPlusReals;

    /** Whether to disallow {@link UnaryOperator#SAMPLE}. */
    public boolean disallowSample;

    @Override
    protected void preprocessUnaryExpression(UnaryExpression unExpr) {
        UnaryOperator op = unExpr.getOperator();
        CifType ctype = CifTypeUtils.normalizeType(unExpr.getChild().getType());
        switch (op) {
            case INVERSE:
                if (disallowInverse) {
                    addExprViolationOperator(unExpr);
                }
                break;
            case NEGATE:
                if (disallowNegate) {
                    addExprViolationOperator(unExpr);
                } else {
                    if (disallowNegateInts) {
                        if (ctype instanceof IntType) {
                            addExprViolationOperand(unExpr);
                        }
                    } else {
                        if (disallowNegateIntsRanged && ctype instanceof IntType
                                && !CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr);
                        }
                        if (disallowNegateIntsRangeless && ctype instanceof IntType
                                && CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr);
                        }
                    }
                    if (disallowNegateReals && ctype instanceof RealType) {
                        addExprViolationOperand(unExpr);
                    }
                }
                break;
            case PLUS:
                if (disallowPlus) {
                    addExprViolationOperator(unExpr);
                } else {
                    if (disallowPlusInts) {
                        if (ctype instanceof IntType) {
                            addExprViolationOperand(unExpr);
                        }
                    } else {
                        if (disallowPlusIntsRanged && ctype instanceof IntType
                                && !CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr);
                        }
                        if (disallowPlusIntsRangeless && ctype instanceof IntType
                                && CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr);
                        }
                    }
                    if (disallowPlusReals && ctype instanceof RealType) {
                        addExprViolationOperand(unExpr);
                    }
                }
                break;
            case SAMPLE:
                if (disallowSample) {
                    addExprViolationOperator(unExpr);
                }
                break;
            default:
                throw new RuntimeException("Unknown unary operator: " + op);
        }
    }

    /**
     * Add a violation for the operator of the given unary expression.
     *
     * @param unExpr The unary expression.
     */
    private void addExprViolationOperator(UnaryExpression unExpr) {
        super.addViolation(getNamedSelfOrAncestor(unExpr), fmt("uses unary operator \"%s\" in unary expression \"%s\"",
                operatorToStr(unExpr.getOperator()), exprToStr(unExpr)));
    }

    /**
     * Add a violation for an operand of the the given unary expression.
     *
     * @param unExpr The unary expression.
     */
    private void addExprViolationOperand(UnaryExpression unExpr) {
        CifType ctype = CifTypeUtils.normalizeType(unExpr.getChild().getType());
        super.addViolation(getNamedSelfOrAncestor(unExpr),
                fmt("uses unary operator \"%s\" on an operand of type \"%s\" in unary expression \"%s\"",
                        operatorToStr(unExpr.getOperator()), typeToStr(ctype), exprToStr(unExpr)));
    }
}
