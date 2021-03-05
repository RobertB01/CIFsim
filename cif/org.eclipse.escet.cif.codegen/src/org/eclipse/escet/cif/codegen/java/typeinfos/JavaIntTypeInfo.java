//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertFunctionCallPattern;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.getLowerBound;
import static org.eclipse.escet.cif.common.CifTypeUtils.getUpperBound;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;
import static org.eclipse.escet.common.java.Strings.stringToJava;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprProperties;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.java.JavaDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.IntTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.box.CodeBox;

/** Java type information about the integer type. */
public class JavaIntTypeInfo extends IntTypeInfo {
    /** Whether this integer is contained in a generic class. */
    public final boolean contained;

    /**
     * Constructor of the {@link JavaIntTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param contained Whether this integer is contained in a generic class.
     */
    public JavaIntTypeInfo(CifType cifType, boolean contained) {
        super(cifType);
        this.contained = contained;
    }

    @Override
    public String getTargetType() {
        return contained ? "Integer" : "int";
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
        // Use 'equalObjs' instead of '==' to avoid object equality for two Boolean objects.
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
    protected ExprCode convertIntNegate(Expression child, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        ExprCode childCode = ctxt.exprToTarget(child, null);

        ExprCode result = new ExprCode();
        result.add(childCode);
        result.setDestination(dest);
        if (properties.isEmpty()) {
            result.setDataValue(new JavaDataValue(fmt("-(%s)", childCode.getData())));
        } else {
            result.setDataValue(new JavaDataValue(fmt("negate(%s)", childCode.getData())));
        }
        return result;
    }

    @Override
    protected ExprCode convertAddition(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) + (${right-value})"
                : "addInt(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertSubtraction(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) - (${right-value})"
                : "subtract(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertDiv(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) / (${right-value})"
                : "div(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertMod(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) % (${right-value})"
                : "mod(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertMultiplication(BinaryExpression expr, EnumSet<ExprProperties> properties,
            Destination dest, CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) * (${right-value})"
                : "multiply(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof JavaIntTypeInfo)) {
            return false;
        }
        JavaIntTypeInfo otherInt = (JavaIntTypeInfo)other;
        return contained == otherInt.contained;
    }

    @Override
    public int hashCode() {
        return JavaIntTypeInfo.class.hashCode() + (contained ? 1 : 0);
    }

    @Override
    protected ExprCode convertAbsStdLib(Expression expression, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        return convertFunctionCallPattern("abs(${args})", list(expression), dest, ctxt);
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
    public ExprCode convertSignStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("sign(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertPowerStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("powInt(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertCeilStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("ceil(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertFloorStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("floor(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertRoundStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("round(${args})", list(expression), dest, ctxt);
    }

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        IntType lhsInt = (IntType)lhsType;
        IntType rhsInt = (IntType)rhsType;
        int lhsLower = getLowerBound(lhsInt);
        int rhsLower = getLowerBound(rhsInt);
        String guard1 = null;
        if (rhsLower < lhsLower) {
            guard1 = fmt("(%s) < %s", rhsValue.getData(), lhsLower);
        }

        int lhsUpper = getUpperBound(lhsInt);
        int rhsUpper = getUpperBound(rhsInt);
        String guard2 = null;
        if (rhsUpper > lhsUpper) {
            guard2 = fmt("(%s) > %s", rhsValue.getData(), lhsUpper);
        }

        if (guard1 != null) {
            if (guard2 != null) {
                guard1 = fmt("%s || %s", guard1, guard2);
            }
        } else {
            if (guard2 == null) {
                return;
            }
            guard1 = guard2;
        }

        String indexedVarName = stringToJava(varName);
        if (!errorTexts.isEmpty()) {
            String indices = "\"";
            for (RangeCheckErrorLevelText errText: errorTexts) {
                if (errText.isIntVariable) {
                    indices += "[\" + Integer.toString(" + errText.text + ") + \"]";
                } else {
                    indices += "[" + errText.text + "]"; // Is always an identifier.
                }
            }
            indices += "\"";
            indexedVarName += " + " + indices;
        }

        code.add("if (%s) {", guard1);
        code.indent();
        code.add("rangeErrInt(%s, valueToStr(%s), %s);", indexedVarName, rhsValue.getData(),
                stringToJava(typeToStr(varType)));
        code.dedent();
        code.add("}");
    }
}
