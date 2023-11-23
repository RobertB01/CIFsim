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
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper;
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
    /** Whether this real is contained in a generic class. */
    public final boolean contained;

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
     * @param contained Whether this real is contained in a generic class.
     */
    public JavaScriptRealTypeInfo(CifType cifType, boolean contained) {
        super(cifType);
        this.contained = contained;
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
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
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
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertNegate(UnaryExpression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertAddition(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertSubtraction(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertMultiplication(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected ExprCode convertDivision(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
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
        if (!(other instanceof JavaScriptRealTypeInfo)) {
            return false;
        }
        JavaScriptRealTypeInfo otherReal = (JavaScriptRealTypeInfo)other;
        return contained == otherReal.contained;
    }

    @Override
    public int hashCode() {
        return JavaScriptRealTypeInfo.class.hashCode() + (contained ? 1 : 0);
    }

    @Override
    public ExprCode convertAbsStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertMaximumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertMinimumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertSignStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertPowerStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertSqrtStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertCbrtStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertScaleStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
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

    // The following methods were adapted from TypeInfoHelper, which returns JavaDataValue, rather than
    // JavaScriptDataValue. Other platforms all use TypeInfoHelper though, possibly a bug/oversight?

    /**
     * Generate a function call in the JavaScript language using the supplied pattern.
     *
     * @param args Arguments of the function being called.
     * @param pattern Pattern to use in the conversion. Contains '${args}' where the translated argument value list is
     *     expected.
     * @param dest Storage destination of the result.
     * @param ctxt Code context of the expression.
     * @return The generated expression.
     */
    private static ExprCode convertFunctionCallPattern(String pattern, List<Expression> args, Destination dest,
            CodeContext ctxt)
    {
        // Convert the arguments.
        ExprCode result = new ExprCode();
        StringBuilder argsList = new StringBuilder();
        boolean first = true;
        for (Expression arg: args) {
            ExprCode argCode = ctxt.exprToTarget(arg, null);
            result.add(argCode);
            if (!first) {
                argsList.append(", ");
            }
            first = false;
            argsList.append(argCode.getData());
        }

        pattern = pattern.replace("${args}", argsList.toString());
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(pattern));
        return result;
    }

    /**
     * Generate a binary expression in the JavaScript language using the supplied pattern.
     *
     * @param expr Expression to convert to the target language
     * @param target Text to use in the conversion. Contains '${left-value}' or '${left-ref}' and '${right-value}' or
     *     '${right-ref}' at the position where the left respectively right sub-expression is expected.
     * @param dest Storage destination of the result.
     * @param ctxt Code context of the expression.
     * @return The generated expression.
     */
    private static ExprCode convertBinaryExpressionPattern(BinaryExpression expr, String target, Destination dest,
            CodeContext ctxt)
    {
        ExprCode leftCode = ctxt.exprToTarget(expr.getLeft(), null);
        ExprCode rightCode = ctxt.exprToTarget(expr.getRight(), null);
        return convertBinaryExpressionPattern(leftCode, rightCode, target, dest, ctxt);
    }

    /**
     * Generate a binary expression in the JavaScript language using the supplied pattern.
     *
     * @param leftCode Result of the left side conversion.
     * @param rightCode Result of the right side conversion.
     * @param target Text to use in the conversion. Contains '${left-value}' or '${left-ref}' and '${right-value}' or
     *     '${right-ref}' at the position where the left respectively right sub-expression is expected.
     * @param dest Storage destination of the result.
     * @param ctxt Code context of the expression.
     * @return The generated expression.
     */
    private static ExprCode convertBinaryExpressionPattern(ExprCode leftCode, ExprCode rightCode, String target,
            Destination dest, CodeContext ctxt)
    {
        ExprCode result = new ExprCode();
        result.add(leftCode);
        result.add(rightCode);

        target = TypeInfoHelper.convertBinaryExpressionValues(leftCode.getRawDataValue(), rightCode.getRawDataValue(),
                target);
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(target));
        return result;
    }
}
