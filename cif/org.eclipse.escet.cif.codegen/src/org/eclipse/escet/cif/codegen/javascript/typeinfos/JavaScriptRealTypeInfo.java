//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen.javascript.typeinfos;

import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertFunctionCallPattern;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprProperties;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.javascript.JavaScriptDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.RealTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** JavaScript type information about the real type. */
public class JavaScriptRealTypeInfo extends RealTypeInfo {
    /** Conversions from CIF standard library functions to JavaScript functions in the implementation. */
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
     * Constructor of the {@link JavaScriptRealTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     */
    public JavaScriptRealTypeInfo(CifType cifType) {
        super(cifType);
    }

    @Override
    public String getTargetType() {
        return "var";
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        // Nothing to generate.
    }

    @Override
    public void storeValue(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("this.%s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public void declareInit(CodeBox code, DataValue sourceValue, Destination dest) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(value));
        return result;
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertNegate(UnaryExpression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertAddition(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertSubtraction(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertMultiplication(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected ExprCode convertDivision(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertTimeExpression(Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue("time"));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof JavaScriptRealTypeInfo;
    }

    @Override
    public int hashCode() {
        return JavaScriptRealTypeInfo.class.hashCode();
    }

    @Override
    public ExprCode convertAbsStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertMaximumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertMinimumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertSignStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertPowerStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertSqrtStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertCbrtStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertScaleStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
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
