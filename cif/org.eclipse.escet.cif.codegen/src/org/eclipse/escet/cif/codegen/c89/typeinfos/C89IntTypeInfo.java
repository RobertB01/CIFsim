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

package org.eclipse.escet.cif.codegen.c89.typeinfos;

import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeComputed;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeLiteral;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertFunctionCallPattern;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.getLowerBound;
import static org.eclipse.escet.cif.common.CifTypeUtils.getUpperBound;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.escape;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;
import static org.eclipse.escet.common.java.Strings.stringToJava;

import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprProperties;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.typeinfos.IntTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.box.CodeBox;

/**
 * Type information object for the CIF int type in the C89 target language.
 *
 * <p>
 * Integer ranges are ignored in the C89 target.
 * </p>
 */
public class C89IntTypeInfo extends IntTypeInfo implements C89TypeInfo {
    /**
     * If set, generate functions only available in the current source file, else generate globally accessible
     * functions.
     */
    public final boolean genLocalFunctions;

    /**
     * Constructor for {@link C89IntTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param cifType The CIF type used for creating this type information object.
     */
    public C89IntTypeInfo(boolean genLocalFunctions, CifType cifType) {
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
        return "IntTypePrint";
    }

    @Override
    public ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(makeLiteral(value));
        return result;
    }

    @Override
    protected ExprCode convertAddition(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) + (${right-value})"
                : "IntegerAdd(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertSubtraction(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) - (${right-value})"
                : "IntegerSubtract(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertMultiplication(BinaryExpression expr, EnumSet<ExprProperties> properties,
            Destination dest, CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) * (${right-value})"
                : "IntegerMultiply(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertDiv(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) / (${right-value})"
                : "IntegerDiv(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
    }

    @Override
    protected ExprCode convertMod(BinaryExpression expr, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        String pattern = properties.isEmpty() ? "(${left-value}) % (${right-value})"
                : "IntegerMod(${left-value}, ${right-value})";
        return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
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
            result.setDataValue(makeComputed(fmt("-(%s)", childCode.getData())));
        } else {
            result.setDataValue(makeComputed(fmt("IntegerNegate(%s)", childCode.getData())));
        }
        return result;
    }

    @Override
    protected ExprCode convertAbsStdLib(Expression expression, EnumSet<ExprProperties> properties, Destination dest,
            CodeContext ctxt)
    {
        return convertFunctionCallPattern("IntegerAbs(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertMaximumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("IntegerMax(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertMinimumStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("IntegerMin(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertSignStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("IntegerSign(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertPowerStdLib(List<Expression> exprs, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("IntegerPower(${args})", exprs, dest, ctxt);
    }

    @Override
    public ExprCode convertCeilStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("CeilFunction(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertFloorStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("FloorFunction(${args})", list(expression), dest, ctxt);
    }

    @Override
    public ExprCode convertRoundStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern("RoundFunction(${args})", list(expression), dest, ctxt);
    }

    @Override
    public String getTargetType() {
        return "IntType";
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
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof C89IntTypeInfo;
    }

    @Override
    public int hashCode() {
        return C89IntTypeInfo.class.hashCode();
    }

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        // Construct guard test code.
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

        // Construct fprintf strings for outputting the error.
        String indexedVarName = stringToJava(varName);
        List<String> arguments = list();
        if (!errorTexts.isEmpty()) {
            String indices = "\"";
            for (RangeCheckErrorLevelText errText: errorTexts) {
                if (errText.isIntVariable) {
                    indices += "[%d]";
                    arguments.add(errText.text);
                } else {
                    indices += "[" + errText.text + "]"; // Is always an identifier.
                }
            }
            indices += "\"";
            indexedVarName += " " + indices;
        }

        // Output the generated check.
        code.add("if (%s) {", guard1);
        code.indent();
        code.add("fprintf(stderr, \"RangeError: Writing %%d into \\\"%s\\\"\\n\", %s);", escape(typeToStr(varType)),
                rhsValue.getData());
        if (arguments.isEmpty()) {
            code.add("fprintf(stderr, \"            at \" %s \"\\n\");", indexedVarName);
        } else {
            code.add("fprintf(stderr, \"            at \" %s \"\\n\", %s);", indexedVarName,
                    StringUtils.join(arguments, ", "));
        }
        code.add("RangeErrorDetected();");
        code.dedent();
        code.add("}");
    }
}
