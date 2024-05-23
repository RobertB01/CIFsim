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

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifAnnotationUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;

/** CIF check that does not allow certain unary expressions. */
public class ExprNoSpecificUnaryExprsCheck extends CifCheck {
    /** The unary operators, or unary operators operating on certain operand types, to disallow. */
    private final EnumSet<NoSpecificUnaryOp> disalloweds;

    /** Whether to disable checking of unary expressions in annotations. */
    private boolean ignoreAnnotations;

    /**
     * Constructor for the {@link ExprNoSpecificUnaryExprsCheck} class.
     *
     * @param disalloweds The unary operators, or unary operators operating on certain operand types, to disallow.
     */
    public ExprNoSpecificUnaryExprsCheck(NoSpecificUnaryOp... disalloweds) {
        this(EnumSet.copyOf(Arrays.asList(disalloweds)));
    }

    /**
     * Constructor for the {@link ExprNoSpecificUnaryExprsCheck} class.
     *
     * @param disalloweds The unary operators, or unary operators operating on certain operand types, to disallow.
     */
    public ExprNoSpecificUnaryExprsCheck(EnumSet<NoSpecificUnaryOp> disalloweds) {
        this.disalloweds = disalloweds;
    }

    /**
     * Disable checking of unary expressions in annotations.
     *
     * @return The check instance, for daisy-chaining.
     */
    public ExprNoSpecificUnaryExprsCheck ignoreAnnotations() {
        return ignoreAnnotations(true);
    }

    /**
     * Configure whether to disable checking of unary expressions in annotations.
     *
     * @param ignore {@code true} to disable, {@code false} to enable.
     * @return The check instance, for daisy-chaining.
     */
    public ExprNoSpecificUnaryExprsCheck ignoreAnnotations(boolean ignore) {
        this.ignoreAnnotations = ignore;
        return this;
    }

    @Override
    protected void preprocessUnaryExpression(UnaryExpression unExpr, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(unExpr)) {
            return;
        }

        // Do the check.
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
                            addExprViolationOperand(unExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificUnaryOp.NEGATE_INTS_RANGED) && ctype instanceof IntType
                                && !CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificUnaryOp.NEGATE_INTS_RANGELESS) && ctype instanceof IntType
                                && CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificUnaryOp.NEGATE_REALS) && ctype instanceof RealType) {
                        addExprViolationOperand(unExpr, "a real typed", violations);
                    }
                }
                break;
            case PLUS:
                if (disalloweds.contains(NoSpecificUnaryOp.PLUS)) {
                    addExprViolationOperator(unExpr, violations);
                } else {
                    if (disalloweds.contains(NoSpecificUnaryOp.PLUS_INTS)) {
                        if (ctype instanceof IntType) {
                            addExprViolationOperand(unExpr, "an integer typed", violations);
                        }
                    } else {
                        if (disalloweds.contains(NoSpecificUnaryOp.PLUS_INTS_RANGED) && ctype instanceof IntType
                                && !CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr, "a ranged integer typed", violations);
                        }
                        if (disalloweds.contains(NoSpecificUnaryOp.PLUS_INTS_RANGELESS) && ctype instanceof IntType
                                && CifTypeUtils.isRangeless((IntType)ctype))
                        {
                            addExprViolationOperand(unExpr, "a rangeless integer typed", violations);
                        }
                    }
                    if (disalloweds.contains(NoSpecificUnaryOp.PLUS_REALS) && ctype instanceof RealType) {
                        addExprViolationOperand(unExpr, "a real typed", violations);
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
        violations.add(unExpr, "Unary operator \"%s\" is used", operatorToStr(unExpr.getOperator()));
    }

    /**
     * Add a violation for an operand of the given unary expression.
     *
     * @param unExpr The unary expression.
     * @param operandTxt A text describing the kind of operand that is a violation.
     * @param violations The violations collected so far. Is modified in-place.
     */
    private void addExprViolationOperand(UnaryExpression unExpr, String operandTxt, CifCheckViolations violations) {
        violations.add(unExpr, "Unary operator \"%s\" is used on %s operand", operatorToStr(unExpr.getOperator()),
                operandTxt);
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
