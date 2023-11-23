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

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.javascript.JavaScriptDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.codegen.typeinfos.TupleTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** JavaScript type information about the tuple type. */
public class JavaScriptTupleTypeInfo extends TupleTypeInfo {
    /**
     * Constructor of the {@link JavaScriptTupleTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param fieldTIs Fields type information.
     */
    public JavaScriptTupleTypeInfo(CifType cifType, TypeInfo[] fieldTIs) {
        super(cifType, fieldTIs);
    }

    @Override
    public String getTargetType() {
        return "var";
    }

    @Override
    public int getSize() {
        return childInfos.length;
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
    public String getBinaryExpressionTemplate(BinaryOperator binOp) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode getProjectedValue(ExprCode childCode, int index, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.add(childCode);
        String resultText = appendProjection(childCode.getData(), false, index);
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(resultText));
        return result;
    }

    @Override
    public String appendProjection(String value, boolean safe, int index) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public CodeBox modifyContainer(VariableInformation containerInfo, ExprCode partCode, int index, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertLiteral(TupleExpression expr, Destination dest, CodeContext ctxt) {
        // Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof JavaScriptTupleTypeInfo)) {
            return false;
        }
        JavaScriptTupleTypeInfo otherTuple = (JavaScriptTupleTypeInfo)other;
        if (childInfos.length != otherTuple.childInfos.length) {
            return false;
        }
        for (int i = 0; i < childInfos.length; i++) {
            if (!childInfos[i].equals(otherTuple.childInfos[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = JavaScriptTupleTypeInfo.class.hashCode() + childInfos.length;
        for (int i = 0; i < childInfos.length; i++) {
            h = h + childInfos[i].hashCode();
        }
        return h;
    }
}
