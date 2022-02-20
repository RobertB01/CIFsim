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

package org.eclipse.escet.cif.codegen.java.typeinfos;

import static org.eclipse.escet.cif.codegen.ExprProperties.INT_INT_DIVIDE;
import static org.eclipse.escet.cif.codegen.ExprProperties.RANGE_FAILURE;
import static org.eclipse.escet.cif.codegen.ExprProperties.ZERO_DIVIDE_FAILURE;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertFunctionCallPattern;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
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
import org.eclipse.escet.cif.codegen.java.JavaDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.RealTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Java type information about the real type. */
public class JavaRealTypeInfo extends RealTypeInfo {
    /** Whether this real is contained in a generic class. */
    public final boolean contained;

    /** Conversions from CIF standard library functions to Java functions in the implementation. */
    private static final Map<StdLibFunction, String> STANDARD_FUNCTIONS;

    static {
        STANDARD_FUNCTIONS = map();
        STANDARD_FUNCTIONS.put(StdLibFunction.ACOS, "acos(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.ASIN, "asin(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.ATAN, "atan(${args})");

        STANDARD_FUNCTIONS.put(StdLibFunction.COS, "cos(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.SIN, "sin(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.TAN, "tan(${args})");

        STANDARD_FUNCTIONS.put(StdLibFunction.EXP, "exp(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.LOG, "log(${args})");
        STANDARD_FUNCTIONS.put(StdLibFunction.LN, "ln(${args})");
    }

    /**
     * Constructor of the {@link JavaRealTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param contained Whether this real is contained in a generic class.
     */
    public JavaRealTypeInfo(CifType cifType, boolean contained) {
        super(cifType);
        this.contained = contained;
    }

    @Override
    public String getTargetType() {
        return contained ? "Double" : "double";
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        // Nothing to generate.
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
    public ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(value));
        return result;
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp) {
        // Use 'equalObjs' instead of '==' to avoid object equality for two Double objects.
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return "equalObjs(${left-value}, ${right-value})";
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return "!equalObjs(${left-value}, ${right-value})";
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
    public ExprCode convertNegate(UnaryExpression expr, Destination dest, CodeContext ctxt) {
        ExprCode childCode = ctxt.exprToTarget(expr.getChild(), null);

        ExprCode result = new ExprCode();
        result.add(childCode);
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(fmt("negate(%s)", childCode.getData())));
        return result;
    }

    @Override
    public ExprCode convertAddition(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, "addReal(${left-value}, ${right-value})", dest, ctxt);
    }

    @Override
    public ExprCode convertSubtraction(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, "subtract(${left-value}, ${right-value})", dest, ctxt);
    }

    @Override
    public ExprCode convertMultiplication(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, "multiply(${left-value}, ${right-value})", dest, ctxt);
    }

    @Override
    protected ExprCode convertDivision(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern;
        if (properties.contains(RANGE_FAILURE) || properties.contains(ZERO_DIVIDE_FAILURE)) {
            pattern = "divide(${left-value}, ${right-value})";
        } else {
            Assert.check(properties.size() == 1 && properties.contains(INT_INT_DIVIDE));
            pattern = "((double)(${left-value})) / (${right-value})";
        }
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    public ExprCode convertTimeExpression(Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue("time"));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof JavaRealTypeInfo)) {
            return false;
        }
        JavaRealTypeInfo otherReal = (JavaRealTypeInfo)other;
        return contained == otherReal.contained;
    }

    @Override
    public int hashCode() {
        return JavaRealTypeInfo.class.hashCode() + (contained ? 1 : 0);
    }

    @Override
    public ExprCode convertAbsStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("abs(${args})", list(expr), dest, ctxt);
    }

    @Override
    public ExprCode convertMaximumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("max(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertMinimumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("min(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertSignStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("sign(${args})", list(expr), dest, ctxt);
    }

    @Override
    public ExprCode convertPowerStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("powReal(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertSqrtStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("sqrt(${args})", list(expr), dest, ctxt);
    }

    @Override
    public ExprCode convertCbrtStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("cbrt(${args})", list(expr), dest, ctxt);
    }

    @Override
    public ExprCode convertScaleStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("scale(${args})", exprs, dest, ctxt);
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
}
