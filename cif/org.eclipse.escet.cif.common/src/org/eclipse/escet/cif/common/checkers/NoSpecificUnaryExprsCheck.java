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

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;

/** CIF check that does not allow certain unary expressions. */
public class NoSpecificUnaryExprsCheck extends CifCheck {
    /** The unary operators, or unary operators operating on certain operand types, to disallow. */
    private final EnumSet<NoSpecificUnaryOp> disalloweds;

    /**
     * Constructor for the {@link NoSpecificUnaryExprsCheck} class.
     *
     * @param disalloweds The unary operators, or unary operators operating on certain operand types, to disallow.
     */
    public NoSpecificUnaryExprsCheck(NoSpecificUnaryOp... disalloweds) {
        this(EnumSet.copyOf(Arrays.asList(disalloweds)));
    }

    /**
     * Constructor for the {@link NoSpecificUnaryExprsCheck} class.
     *
     * @param disalloweds The unary operators, or unary operators operating on certain operand types, to disallow.
     */
    public NoSpecificUnaryExprsCheck(EnumSet<NoSpecificUnaryOp> disalloweds) {
        this.disalloweds = disalloweds;
    }

    @Override
    protected void preprocessUnaryExpression(UnaryExpression unExpr, CifCheckViolations violations) {
        UnaryOperator op = unExpr.getOperator();
        CifType ctype = CifTypeUtils.normalizeType(unExpr.getChild().getType());
        switch (op) {
            case INVERSE:
                if (disalloweds.contains(NoSpecificUnaryOp.INVERSE)) {
                    addExprViolationOperator(unExpr, violations);
                }
                break;
            case NEGATE:
                if (disalloweds.contains(NoSpecificUnaryOp.NEGATE)) {
                    addExprViolationOperator(unExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificUnaryOp.NEGATE_INTS)) {
                        if (ctype instanceof IntType) {
                            addExprViolationOperand(unExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificUnaryOp.NEGATE_INTS_RANGED) && ctype instanceof IntType
                                && !CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificUnaryOp.NEGATE_INTS_RANGELESS) && ctype instanceof IntType
                                && CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr, violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificUnaryOp.NEGATE_REALS) && ctype instanceof RealType) {
                        addExprViolationOperand(unExpr, violations);
                    }
                }
                break;
            case PLUS:
                if (disalloweds.contains(NoSpecificUnaryOp.PLUS)) {
                    addExprViolationOperator(unExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificUnaryOp.PLUS_INTS)) {
                        if (ctype instanceof IntType) {
                            addExprViolationOperand(unExpr, violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificUnaryOp.PLUS_INTS_RANGED) && ctype instanceof IntType
                                && !CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr, violations);
                        }
                        if (disalloweds.contains(NoSpecificUnaryOp.PLUS_INTS_RANGELESS) && ctype instanceof IntType
                                && CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr, violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificUnaryOp.PLUS_REALS) && ctype instanceof RealType) {
                        addExprViolationOperand(unExpr, violations);
                    }
                }
                break;
            case SAMPLE:
                if (disalloweds.contains(NoSpecificUnaryOp.SAMPLE)) {
                    addExprViolationOperator(unExpr, violations);
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
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addExprViolationOperator(UnaryExpression unExpr, CifCheckViolations violations) {
        violations.add(getNamedSelfOrAncestor(unExpr), fmt("uses unary operator \"%s\" in unary expression \"%s\"",
                operatorToStr(unExpr.getOperator()), exprToStr(unExpr)));
    }

    /**
     * Add a violation for an operand of the the given unary expression.
     *
     * @param unExpr The unary expression.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addExprViolationOperand(UnaryExpression unExpr, CifCheckViolations violations) {
        CifType ctype = CifTypeUtils.normalizeType(unExpr.getChild().getType());
        violations.add(getNamedSelfOrAncestor(unExpr),
                fmt("uses unary operator \"%s\" on an operand of type \"%s\" in unary expression \"%s\"",
                        operatorToStr(unExpr.getOperator()), typeToStr(ctype), exprToStr(unExpr)));
    }

    /** The unary operator, or unary operator operating on certain operand types, to disallow. */
    public static enum NoSpecificUnaryOp {
        /** Disallow {@link UnaryOperator#INVERSE}. */
        INVERSE,

        /** Disallow {@link UnaryOperator#NEGATE}. */
        NEGATE,

        /** Disallow {@link UnaryOperator#NEGATE} on integer numbers. */
        NEGATE_INTS,

        /** Disallow {@link UnaryOperator#NEGATE} on ranged integer numbers. */
        NEGATE_INTS_RANGED,

        /** Disallow {@link UnaryOperator#NEGATE} on rangeless integer numbers. */
        NEGATE_INTS_RANGELESS,

        /** Disallow {@link UnaryOperator#NEGATE} on real numbers. */
        NEGATE_REALS,

        /** Disallow {@link UnaryOperator#PLUS}. */
        PLUS,

        /** Disallow {@link UnaryOperator#PLUS} on integer numbers. */
        PLUS_INTS,

        /** Disallow {@link UnaryOperator#PLUS} on ranged integer numbers. */
        PLUS_INTS_RANGED,

        /** Disallow {@link UnaryOperator#PLUS} on rangeless integer numbers. */
        PLUS_INTS_RANGELESS,

        /** Disallow {@link UnaryOperator#PLUS} on real numbers. */
        PLUS_REALS,

        /** Disallow {@link UnaryOperator#SAMPLE}. */
        SAMPLE,
    }
}
