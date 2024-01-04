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

package org.eclipse.escet.cif.codegen.typeinfos;

import static org.eclipse.escet.cif.codegen.ExprProperties.RANGE_FAILURE;
import static org.eclipse.escet.cif.codegen.ExprProperties.ZERO_DIVIDE_FAILURE;
import static org.eclipse.escet.cif.codegen.ExpressionAnalysisSupport.checkExprIntType;
import static org.eclipse.escet.cif.codegen.ExpressionAnalysisSupport.typeAllowsMinInt;
import static org.eclipse.escet.cif.codegen.ExpressionAnalysisSupport.typeAllowsMinusOne;
import static org.eclipse.escet.cif.codegen.ExpressionAnalysisSupport.typeAllowsZero;
import static org.eclipse.escet.cif.codegen.ExpressionAnalysisSupport.typeIsRanged;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprProperties;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.java.Assert;

/** Generic type info about the integer type. */
public abstract class IntTypeInfo extends TypeInfo implements NegateOperation, OrderingOperations {
    /**
     * Constructor for {@link IntTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     */
    public IntTypeInfo(CifType cifType) {
        super(cifType);
    }

    @Override
    public String makeTypeName() {
        return "I";
    }

    @Override
    public ExprCode convertLessThan(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.LESS_THAN, ctxt), dest,
                ctxt);
    }

    @Override
    public ExprCode convertLessEqual(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.LESS_EQUAL, ctxt), dest,
                ctxt);
    }

    @Override
    public ExprCode convertGreaterEqual(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.GREATER_EQUAL, ctxt),
                dest, ctxt);
    }

    @Override
    public ExprCode convertGreaterThan(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.GREATER_THAN, ctxt),
                dest, ctxt);
    }

    /**
     * Convert the literal value expression to the target language.
     *
     * @param value Literal value.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    public abstract ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'addition' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    public ExprCode convertAddition(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        EnumSet<ExprProperties> properties;
        properties = typeIsRanged(expr.getType()) ? EnumSet.noneOf(ExprProperties.class)
                : EnumSet.of(ExprProperties.RANGE_FAILURE);
        return convertAddition(expr, properties, dest, ctxt);
    }

    /**
     * Convert the 'addition' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param properties Properties of the operation.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    protected abstract ExprCode convertAddition(BinaryExpression expr, EnumSet<ExprProperties> properties,
            Destination dest, CodeContext ctxt);

    /**
     * Convert the 'subtraction' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    public ExprCode convertSubtraction(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        EnumSet<ExprProperties> properties;
        properties = typeIsRanged(expr.getType()) ? EnumSet.noneOf(ExprProperties.class)
                : EnumSet.of(ExprProperties.RANGE_FAILURE);
        return convertSubtraction(expr, properties, dest, ctxt);
    }

    /**
     * Convert the 'subtraction' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param properties Properties of the operation.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    protected abstract ExprCode convertSubtraction(BinaryExpression expr, EnumSet<ExprProperties> properties,
            Destination dest, CodeContext ctxt);

    /**
     * Convert the 'multiplication' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    public ExprCode convertMultiplication(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        EnumSet<ExprProperties> properties;
        properties = typeIsRanged(expr.getType()) ? EnumSet.noneOf(ExprProperties.class)
                : EnumSet.of(ExprProperties.RANGE_FAILURE);
        return convertMultiplication(expr, properties, dest, ctxt);
    }

    /**
     * Convert the 'multiplication' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param properties Properties of the operation.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    protected abstract ExprCode convertMultiplication(BinaryExpression expr, EnumSet<ExprProperties> properties,
            Destination dest, CodeContext ctxt);

    /**
     * Convert the 'div' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    public ExprCode convertDiv(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        CifType ltype = normalizeType(expr.getLeft().getType());
        CifType rtype = normalizeType(expr.getRight().getType());
        Assert.check(ltype instanceof IntType && rtype instanceof IntType && checkExprIntType(expr));

        EnumSet<ExprProperties> properties = EnumSet.noneOf(ExprProperties.class);
        if (typeAllowsMinInt(ltype) && typeAllowsMinusOne(rtype)) {
            properties.add(RANGE_FAILURE);
        }

        if (typeAllowsZero(rtype)) {
            properties.add(ZERO_DIVIDE_FAILURE);
        }

        return convertDiv(expr, properties, dest, ctxt);
    }

    /**
     * Convert the 'div' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param properties Properties of the operation.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    protected abstract ExprCode convertDiv(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt);

    /**
     * Convert the 'mod' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    public ExprCode convertMod(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        CifType rtype = normalizeType(expr.getRight().getType());
        Assert.check(checkExprIntType(expr.getLeft()) && rtype instanceof IntType && checkExprIntType(expr));

        EnumSet<ExprProperties> properties = EnumSet.noneOf(ExprProperties.class);
        if (typeAllowsZero(rtype)) {
            properties.add(ZERO_DIVIDE_FAILURE);
        }

        return convertMod(expr, properties, dest, ctxt);
    }

    /**
     * Convert the 'mod' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param properties Properties of the operation.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return Result of the conversion.
     */
    protected abstract ExprCode convertMod(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt);

    @Override
    public ExprCode convertNegate(UnaryExpression expr, Destination dest, CodeContext ctxt) {
        CifType resType = normalizeType(expr.getType());
        Assert.check(checkExprIntType(expr));

        EnumSet<ExprProperties> properties = EnumSet.noneOf(ExprProperties.class);
        if (typeAllowsMinInt(resType)) {
            properties.add(RANGE_FAILURE);
        }
        return convertIntNegate(expr.getChild(), properties, dest, ctxt);
    }

    /**
     * Convert a 'negate' (a '-x') expression.
     *
     * @param child Child of the negate expression.
     * @param properties Properties of the operation.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated expression in the target language.
     */
    protected abstract ExprCode convertIntNegate(Expression child, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt);

    /**
     * Convert the 'abs' standard library function call to the target language.
     *
     * @param expression Argument of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public ExprCode convertAbsStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        EnumSet<ExprProperties> properties = EnumSet.noneOf(ExprProperties.class);
        if (typeAllowsMinInt(expression.getType())) {
            properties.add(RANGE_FAILURE);
        }
        return convertAbsStdLib(expression, properties, dest, ctxt);
    }

    /**
     * Convert the 'abs' standard library function call to the target language.
     *
     * @param expression Argument of the call.
     * @param properties Properties of the operation.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    protected abstract ExprCode convertAbsStdLib(Expression expression, EnumSet<ExprProperties> properties,
            Destination dest, CodeContext ctxt);

    /**
     * Convert the 'max' standard library function call to the target language.
     *
     * @param exprs Arguments of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertMaximumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'min' standard library function call to the target language.
     *
     * @param exprs Arguments of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertMinimumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'sign' standard library function call to the target language.
     *
     * @param expression Argument of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertSignStdLib(Expression expression, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'power' standard library function call to the target language.
     *
     * @param exprs Arguments of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertPowerStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'ceil' standard library function call to the target language.
     *
     * @param expression Argument of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertCeilStdLib(Expression expression, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'floor' standard library function call to the target language.
     *
     * @param expression Argument of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertFloorStdLib(Expression expression, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'round' standard library function call to the target language.
     *
     * @param expression Argument of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertRoundStdLib(Expression expression, Destination dest, CodeContext ctxt);
}
