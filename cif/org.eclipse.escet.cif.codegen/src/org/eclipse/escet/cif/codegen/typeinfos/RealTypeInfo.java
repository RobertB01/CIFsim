//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.codegen.ExprProperties.INT_INT_DIVIDE;
import static org.eclipse.escet.cif.codegen.ExprProperties.RANGE_FAILURE;
import static org.eclipse.escet.cif.codegen.ExprProperties.ZERO_DIVIDE_FAILURE;
import static org.eclipse.escet.cif.codegen.ExpressionAnalysisSupport.typeAllowsZero;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprProperties;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.common.box.CodeBox;

/** Generic type info about the real type. */
public abstract class RealTypeInfo extends TypeInfo implements NegateOperation, OrderingOperations {
    /**
     * Constructor for {@link RealTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     */
    public RealTypeInfo(CifType cifType) {
        super(cifType);
    }

    @Override
    protected String makeTypeName() {
        return "R";
    }

    @Override
    public ExprCode convertLessThan(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.LESS_THAN), dest, ctxt);
    }

    @Override
    public ExprCode convertLessEqual(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.LESS_EQUAL), dest, ctxt);
    }

    @Override
    public ExprCode convertGreaterEqual(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.GREATER_EQUAL), dest,
                ctxt);
    }

    @Override
    public ExprCode convertGreaterThan(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.GREATER_THAN), dest,
                ctxt);
    }

    /**
     * Convert the literal value expression to the target language.
     *
     * @param value Literal value.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public abstract ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'time' expression to the target language.
     *
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public abstract ExprCode convertTimeExpression(Destination dest, CodeContext ctxt);

    /**
     * Convert the 'addition' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public abstract ExprCode convertAddition(BinaryExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'subtraction' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public abstract ExprCode convertSubtraction(BinaryExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'multiplication' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public abstract ExprCode convertMultiplication(BinaryExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'divide' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public ExprCode convertDivision(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        CifType ltype = normalizeType(expr.getLeft().getType());
        CifType rtype = normalizeType(expr.getRight().getType());
        EnumSet<ExprProperties> properties = EnumSet.noneOf(ExprProperties.class);
        if (ltype instanceof IntType && rtype instanceof IntType) {
            properties.add(INT_INT_DIVIDE);
        } else {
            properties.add(RANGE_FAILURE);
        }

        if (rtype instanceof RealType || typeAllowsZero(rtype)) {
            properties.add(ZERO_DIVIDE_FAILURE);
        }

        return convertDivision(expr, properties, dest, ctxt);
    }

    /**
     * Convert the 'divide' operation to the target language.
     *
     * @param expr Expression to convert to the target language.
     * @param properties Properties of the operation.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    protected abstract ExprCode convertDivision(BinaryExpression expr, EnumSet<ExprProperties> properties,
            Destination dest, CodeContext ctxt);

    /**
     * Convert the 'abs' standard library function call to the target language.
     *
     * @param expression Argument of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertAbsStdLib(Expression expression, Destination dest, CodeContext ctxt);

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
     * Convert the 'sqrt' standard library function call to the target language.
     *
     * @param expression Argument of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertSqrtStdLib(Expression expression, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'cbrt' standard library function call to the target language.
     *
     * @param expression Argument of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertCbrtStdLib(Expression expression, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'scale' standard library function call to the target language.
     *
     * @param exprs Arguments of the call.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertScaleStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt);

    /**
     * Convert a trigonometric function call to the target language.
     *
     * @param stdLib Function being called ({@link StdLibFunction#COS}, {@link StdLibFunction#ASIN},
     *     {@link StdLibFunction#ATAN}, {@link StdLibFunction#COS}, {@link StdLibFunction#SIN}, or
     *     {@link StdLibFunction#TAN}).
     *
     * @param expr Argument of the function.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertTrigonometryStdLib(StdLibFunction stdLib, Expression expr, Destination dest,
            CodeContext ctxt);

    /**
     * Convert a logarithmic function call to the target language.
     *
     * @param stdLib Function being called ({@link StdLibFunction#LN}, {@link StdLibFunction#LOG},
     *     {@link StdLibFunction#EXP}).
     * @param expr Argument of the function.
     * @param dest Storage destination if it exists, else {@code null}.
     * @param ctxt Code context of the expression.
     * @return The generated call in the target language.
     */
    public abstract ExprCode convertLogarithmicStdLib(StdLibFunction stdLib, Expression expr, Destination dest,
            CodeContext ctxt);

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        // Nothing to do, range is always valid.
    }
}
