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

package org.eclipse.escet.cif.codegen.c99.typeinfos;

import static org.eclipse.escet.cif.codegen.c99.C99DataValue.makeComputed;
import static org.eclipse.escet.cif.codegen.c99.C99DataValue.makeLiteral;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.typeinfos.BoolTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** Type information object for the CIF boolean type in the C99 target language. */
public class C99BoolTypeInfo extends BoolTypeInfo implements C99TypeInfo {
    /**
     * If set, generate functions only available in the current source file, else generate globally accessible
     * functions.
     */
    public final boolean genLocalFunctions;

    /**
     * Constructor for {@link C99BoolTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param cifType The CIF type used for creating this type information object.
     */
    public C99BoolTypeInfo(boolean genLocalFunctions, CifType cifType) {
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
        return "BoolTypePrint";
    }

    @Override
    public ExprCode convertLiteral(boolean value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(makeLiteral(value ? "TRUE" : "FALSE"));
        return result;
    }

    @Override
    public ExprCode convertInvert(Expression child, Destination dest, CodeContext ctxt) {
        ExprCode childCode = ctxt.exprToTarget(child, null);
        ExprCode result = new ExprCode();
        result.add(childCode);
        result.setDestination(dest);
        result.setDataValue(makeComputed(fmt("!(%s)", childCode.getData())));
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
    public String getTargetType() {
        return "BoolType";
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
    public String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt) {
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return "(${left-value}) == (${right-value})";
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return "(${left-value}) != (${right-value})";
        }

        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof C99BoolTypeInfo;
    }

    @Override
    public int hashCode() {
        return C99BoolTypeInfo.class.hashCode();
    }
}
