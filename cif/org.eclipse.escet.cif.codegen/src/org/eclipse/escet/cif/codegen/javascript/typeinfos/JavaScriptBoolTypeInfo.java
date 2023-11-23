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
    /** Whether this boolean is contained in a generic class. */
    public final boolean contained;

    /**
     * Constructor of the {@link JavaScriptBoolTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param contained Whether this boolean is contained in a generic class.
     */
    public JavaScriptBoolTypeInfo(CifType cifType, boolean contained) {
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
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public void declareInit(CodeBox code, DataValue sourceValue, Destination dest) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertLiteral(boolean value, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertInvert(Expression child, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertShortCircuit(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof JavaScriptBoolTypeInfo)) {
            return false;
        }
        JavaScriptBoolTypeInfo otherBool = (JavaScriptBoolTypeInfo)other;
        return contained == otherBool.contained;
    }

    @Override
    public int hashCode() {
        return JavaScriptBoolTypeInfo.class.hashCode() + (contained ? 1 : 0);
    }
}
