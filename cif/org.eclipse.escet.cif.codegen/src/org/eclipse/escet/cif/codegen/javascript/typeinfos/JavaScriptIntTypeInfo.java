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

import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertFunctionCallPattern;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.getLowerBound;
import static org.eclipse.escet.cif.common.CifTypeUtils.getUpperBound;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprProperties;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.javascript.JavaScriptDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.IntTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Strings;

/** JavaScript type information about the integer type. */
public class JavaScriptIntTypeInfo extends IntTypeInfo {
    /**
     * Constructor of the {@link JavaScriptIntTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     */
    public JavaScriptIntTypeInfo(CifType cifType) {
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
        code.add("%s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public void declareInit(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("var %s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(value));
        return result;
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt) {
        // Use 'equalObjs' instead of '==' to avoid object equality for two Integer objects.
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return fmt("%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return fmt("!%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
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
            result.setDataValue(new JavaScriptDataValue(fmt("-(%s)", childCode.getData())));
        } else {
            result.setDataValue(
                    new JavaScriptDataValue(fmt("%sUtils.negateInt(%s)", ctxt.getPrefix(), childCode.getData())));
        }
        return result;
    }

    @Override
    protected ExprCode convertAddition(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) + (${right-value})"
                : fmt("%sUtils.addInt(${left-value}, ${right-value})", ctxt.getPrefix());
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertSubtraction(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) - (${right-value})"
                : fmt("%sUtils.subtractInt(${left-value}, ${right-value})", ctxt.getPrefix());
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertDiv(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) / (${right-value})"
                : fmt("%sUtils.div(${left-value}, ${right-value})", ctxt.getPrefix());
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertMod(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) % (${right-value})"
                : fmt("%sUtils.mod(${left-value}, ${right-value})", ctxt.getPrefix());
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertMultiplication(BinaryExpression expr, EnumSet<ExprProperties> properties,
            Destination dest, CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) * (${right-value})"
                : fmt("%sUtils.multiplyInt(${left-value}, ${right-value})", ctxt.getPrefix());
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof JavaScriptIntTypeInfo;
    }

    @Override
    public int hashCode() {
        return JavaScriptIntTypeInfo.class.hashCode();
    }

    @Override
    protected ExprCode convertAbsStdLib(Expression expression, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        return convertFunctionCallPattern(fmt("%sUtils.absInt(${args})", ctxt.getPrefix()), list(expression), dest,
                ctxt);
    }

    @Override
    public ExprCode convertMaximumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern(fmt("%sUtils.maxInt(${args})", ctxt.getPrefix()), exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertMinimumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern(fmt("%sUtils.minInt(${args})", ctxt.getPrefix()), exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertSignStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern(fmt("%sUtils.signInt(${args})", ctxt.getPrefix()), list(expression), dest,
                ctxt);
    }

    @Override
    public ExprCode convertPowerStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern(fmt("%sUtils.powInt(${args})", ctxt.getPrefix()), exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertCeilStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern(fmt("%sUtils.ceil(${args})", ctxt.getPrefix()), list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertFloorStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern(fmt("%sUtils.floor(${args})", ctxt.getPrefix()), list(expression), dest,
                ctxt);
    }

    @Override
    public ExprCode convertRoundStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern(fmt("%sUtils.round(${args})", ctxt.getPrefix()), list(expression), dest,
                ctxt);
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

        String indexedVarName = Strings.stringToJava(varName);
        if (!errorTexts.isEmpty()) {
            String indices = "\"";
            for (RangeCheckErrorLevelText errText: errorTexts) {
                if (errText.isIntVariable) {
                    indices += "[\" + (" + errText.text + ").toString() + \"]";
                } else {
                    indices += "[" + errText.text + "]"; // Is always an identifier.
                }
            }
            indices += "\"";
            indexedVarName += " + " + indices;
        }

        code.add("if (%s) {", guard1);
        code.indent();
        code.add("%sUtils.rangeErrInt(%s, %sUtils.valueToStr(%s), %s);", ctxt.getPrefix(), indexedVarName,
                ctxt.getPrefix(), rhsValue.getData(), Strings.stringToJava(typeToStr(varType)));
        code.dedent();
        code.add("}");
    }
}
