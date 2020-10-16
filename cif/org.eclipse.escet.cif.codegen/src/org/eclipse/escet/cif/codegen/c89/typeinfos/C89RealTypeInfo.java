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

package org.eclipse.escet.cif.codegen.c89.typeinfos;

import static org.eclipse.escet.cif.codegen.ExprProperties.INT_INT_DIVIDE;
import static org.eclipse.escet.cif.codegen.ExprProperties.RANGE_FAILURE;
import static org.eclipse.escet.cif.codegen.ExprProperties.ZERO_DIVIDE_FAILURE;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeLiteral;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeValue;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertFunctionCallPattern;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprProperties;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.typeinfos.RealTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Type information about the CIF real type for the C89 target language. */
public class C89RealTypeInfo extends RealTypeInfo implements C89TypeInfo {
    /** Conversions from CIF standard library functions to Java functions in the implementation. */
    private static final Map<StdLibFunction, String> STANDARD_FUNCTIONS;

    static {
        STANDARD_FUNCTIONS = mapc(9);
        STANDARD_FUNCTIONS.put(StdLibFunction.ACOS, "RealAcos(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.ASIN, "RealAsin(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.ATAN, "RealAtan(${args})");

        STANDARD_FUNCTIONS.put(StdLibFunction.COS, "RealCos(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.SIN, "RealSin(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.TAN, "RealTan(${args})");

        STANDARD_FUNCTIONS.put(StdLibFunction.EXP, "RealExp(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.LOG, "RealLog(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.LN, "RealLn(${args})");
    }

    /**
     * If set, generate functions only available in the current source file, else generate globally accessible
     * functions.
     */
    public final boolean genLocalFunctions;

    /**
     * Constructor for {@link C89RealTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param cifType The CIF type used for creating this type information object.
     */
    public C89RealTypeInfo(boolean genLocalFunctions, CifType cifType) {
        super(cifType);
        this.genLocalFunctions = genLocalFunctions;
    }

    @Override
    public boolean supportRawMemCmp() {
        return true;
    }

    @Override
    public boolean useValues() {
        return true;
    }

    @Override
    public String getTypePrintName(boolean rawString) {
        return "RealTypePrint";
    }

    @Override
    public ExprCode convertNegate(UnaryExpression expr, Destination dest, CodeContext ctxt) {
        ExprCode childCode = ctxt.exprToTarget(expr.getChild(), null);

        ExprCode result = new ExprCode();
        result.add(childCode);
        result.setDestination(dest);
        result.setDataValue(makeValue(fmt("RealNegate(%s)", childCode.getData())));
        return result;
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp) {
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return "(${left-value}) == (${right-value})";
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return "(${left-value}) != (${right-value})";
        }

        if (binOp.equals(BinaryOperator.LESS_THAN)) {
            return "(${left-value}) < (${right-value})";
        } else if (binOp.equals(BinaryOperator.LESS_EQUAL)) {
            return "(${left-value}) <= (${right-value})";
        } else if (binOp.equals(BinaryOperator.GREATER_THAN)) {
            return "(${left-value}) > (${right-value})";
        } else if (binOp.equals(BinaryOperator.GREATER_EQUAL)) {
            return "(${left-value}) >= (${right-value})";
        }

        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(makeLiteral(value));
        return result;
    }

    @Override
    public ExprCode convertTimeExpression(Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(makeValue("model_time"));
        return result;
    }

    @Override
    public ExprCode convertAddition(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, "RealAdd(${left-value}, ${right-value})", dest, ctxt);
    }

    @Override
    public ExprCode convertSubtraction(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, "RealSubtract(${left-value}, ${right-value})", dest, ctxt);
    }

    @Override
    public ExprCode convertMultiplication(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, "RealMultiply(${left-value}, ${right-value})", dest, ctxt);
    }

    @Override
    protected ExprCode convertDivision(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern;
        if (properties.contains(RANGE_FAILURE) || properties.contains(ZERO_DIVIDE_FAILURE)) {
            pattern = "RealDivision(${left-value}, ${right-value})";
        } else {
            Assert.check(properties.size() == 1 && properties.contains(INT_INT_DIVIDE));
            pattern = "(double)(${left-value}) / (${right-value})";
        }
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    public ExprCode convertAbsStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("RealAbs(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertMaximumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("RealMax(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertMinimumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("RealMin(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertSignStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("RealSign(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertPowerStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("RealMax(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertSqrtStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("RealSqrt(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertCbrtStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("RealCbrt(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertScaleStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("ScaleFunction(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertTrigonometryStdLib(StdLibFunction stdLib, Expression expr, Destination dest,
            CodeContext ctxt)
    {
        String pattern = STANDARD_FUNCTIONS.get(stdLib);
        Assert.notNull(pattern);
        return convertFunctionCallPattern(pattern, list(expr), dest, ctxt);
    }

    @Override
    public ExprCode convertLogarithmicStdLib(StdLibFunction stdLib, Expression expr, Destination dest,
            CodeContext ctxt)
    {
        String pattern = STANDARD_FUNCTIONS.get(stdLib);
        Assert.notNull(pattern);
        return convertFunctionCallPattern(pattern, list(expr), dest, ctxt);
    }

    @Override
    public String getTargetType() {
        return "RealType";
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        // Nothing to generate (definition is in library.h).
    }

    @Override
    public void storeValue(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("%s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public void declareInit(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("%s %s = %s;", getTargetType(), dest.getData(), sourceValue.getData());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof C89RealTypeInfo;
    }

    @Override
    public int hashCode() {
        return C89RealTypeInfo.class.hashCode();
    }
}
