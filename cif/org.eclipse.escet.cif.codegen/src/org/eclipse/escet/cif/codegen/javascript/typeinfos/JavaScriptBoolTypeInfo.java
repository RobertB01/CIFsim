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
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.javascript.JavaScriptDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.BoolTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** JavaScript type information about the boolean type. */
public class JavaScriptBoolTypeInfo extends BoolTypeInfo {
    /**
     * Constructor of the {@link JavaScriptBoolTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     */
    public JavaScriptBoolTypeInfo(CifType cifType) {
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
        code.add(dest.getCode());
        code.add("var %s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public ExprCode convertLiteral(boolean value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(value ? "true" : "false"));
        return result;
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt) {
        // Use 'equalObjs' instead of '==' to avoid object equality for two Boolean objects.
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return fmt("%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return fmt("!%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
        }
        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public ExprCode convertInvert(Expression child, Destination dest, CodeContext ctxt) {
        ExprCode childCode = ctxt.exprToTarget(child, null);
        ExprCode result = new ExprCode();
        result.add(childCode);
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(fmt("!(%s)", childCode.getData())));
        return result;
    }

    @Override
    public ExprCode convertShortCircuit(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        switch (expr.getOperator()) {
            case IMPLICATION: {
                String pattern = "!(${left-value}) || (${right-value})";
                return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
            }

            case DISJUNCTION: {
                String pattern = "(${left-value}) || (${right-value})";
                return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
            }

            case CONJUNCTION: {
                String pattern = "(${left-value}) && (${right-value})";
                return convertBinaryExpressionPattern(expr, pattern, dest, ctxt);
            }

            default:
                throw new RuntimeException("Unsupported short circuit operator: " + expr.getOperator());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof JavaScriptBoolTypeInfo;
    }

    @Override
    public int hashCode() {
        return JavaScriptBoolTypeInfo.class.hashCode();
    }
}
