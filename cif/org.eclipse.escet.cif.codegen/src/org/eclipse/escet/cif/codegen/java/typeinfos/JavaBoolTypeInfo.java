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

package org.eclipse.escet.cif.codegen.java.typeinfos;

import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.java.JavaDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.BoolTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** Java type information about the boolean type. */
public class JavaBoolTypeInfo extends BoolTypeInfo {
    /** Whether this boolean is contained in a generic class. */
    public final boolean contained;

    /**
     * Constructor of the {@link JavaBoolTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param contained Whether this boolean is contained in a generic class.
     */
    public JavaBoolTypeInfo(CifType cifType, boolean contained) {
        super(cifType);
        this.contained = contained;
    }

    @Override
    public String getTargetType() {
        return contained ? "Boolean" : "boolean";
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
    public ExprCode convertLiteral(boolean value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(value ? "true" : "false"));
        return result;
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt) {
        // Use 'equalObjs' instead of '==' to avoid object equality for two Boolean objects.
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return "equalObjs(${left-value}, ${right-value})";
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return "!equalObjs(${left-value}, ${right-value})";
        }
        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public ExprCode convertInvert(Expression child, Destination dest, CodeContext ctxt) {
        ExprCode childCode = ctxt.exprToTarget(child, null);
        ExprCode result = new ExprCode();
        result.add(childCode);
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(fmt("!(%s)", childCode.getData())));
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
        if (!(other instanceof JavaBoolTypeInfo)) {
            return false;
        }
        JavaBoolTypeInfo otherBool = (JavaBoolTypeInfo)other;
        return contained == otherBool.contained;
    }

    @Override
    public int hashCode() {
        return JavaBoolTypeInfo.class.hashCode() + (contained ? 1 : 0);
    }
}
