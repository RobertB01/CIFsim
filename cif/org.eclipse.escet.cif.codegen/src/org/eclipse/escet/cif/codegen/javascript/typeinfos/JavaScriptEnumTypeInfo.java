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

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.javascript.JavaScriptDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.EnumTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.common.box.CodeBox;

/** JavaScript enum type information. */
public class JavaScriptEnumTypeInfo extends EnumTypeInfo {
    /** Prefix of the enumeration type. */
    private final String prefix;

    /**
     * Constructor of the {@link JavaScriptEnumTypeInfo} class.
     *
     * @param prefix Common prefix of all generated code.
     */
    public JavaScriptEnumTypeInfo(String prefix) {
        super(null); // In linearized mode, there is only one enum.
        this.prefix = prefix;
    }

    @Override
    public String getTargetType() {
        return prefix + "Enum";
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
    public String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt) {
        // Uses '==' instead of 'equalsObj' like for the other types, as object equality works for enumerations and may
        // also give slightly better performance.
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return "(${left-value}) == (${right-value})";
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return "(${left-value}) != (${right-value})";
        }
        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public ExprCode convertEnumLiteral(EnumLiteralExpression expr, Destination dest, CodeContext ctxt) {
        EnumLiteral lit = expr.getLiteral();
        String resultText = fmt("this.%s._%s", getTargetType(), lit.getName());

        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(resultText));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof JavaScriptEnumTypeInfo;
    }

    @Override
    public int hashCode() {
        return JavaScriptEnumTypeInfo.class.hashCode();
    }
}
