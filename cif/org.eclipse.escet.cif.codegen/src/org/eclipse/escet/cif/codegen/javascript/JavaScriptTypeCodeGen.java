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

package org.eclipse.escet.cif.codegen.javascript;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.TypeCodeGen;
import org.eclipse.escet.cif.codegen.javascript.typeinfos.JavaScriptArrayTypeInfo;
import org.eclipse.escet.cif.codegen.javascript.typeinfos.JavaScriptBoolTypeInfo;
import org.eclipse.escet.cif.codegen.javascript.typeinfos.JavaScriptEnumTypeInfo;
import org.eclipse.escet.cif.codegen.javascript.typeinfos.JavaScriptIntTypeInfo;
import org.eclipse.escet.cif.codegen.javascript.typeinfos.JavaScriptRealTypeInfo;
import org.eclipse.escet.cif.codegen.javascript.typeinfos.JavaScriptStringTypeInfo;
import org.eclipse.escet.cif.codegen.javascript.typeinfos.JavaScriptTupleTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.java.Assert;

/** Type code generator for the JavaScript target language. */
public class JavaScriptTypeCodeGen extends TypeCodeGen {
    @Override
    public TypeInfo typeToTarget(CifType type, CodeContext ctxt) {
        return buildTypeInfo(type, ctxt);
    }

    /**
     * Build the JavaScript target language type information object for a given CIF type.
     *
     * <p>
     * This method is used internally, use {@link #typeToTarget} to convert a CIF type.
     * </p>
     *
     * @param type CIF type to convert to the JavaScript language.
     * @param ctxt The code generation context.
     * @return The constructed type information object.
     */
    private TypeInfo buildTypeInfo(CifType type, CodeContext ctxt) {
        type = normalizeType(type);

        // Handle all different types.
        if (type instanceof BoolType) {
            return uniqueTypeInfo(new JavaScriptBoolTypeInfo(type), ctxt);
        } else if (type instanceof IntType) {
            return uniqueTypeInfo(new JavaScriptIntTypeInfo(type), ctxt);
        } else if (type instanceof EnumType) {
            return uniqueTypeInfo(new JavaScriptEnumTypeInfo(ctxt.getPrefix()), ctxt);
        } else if (type instanceof RealType) {
            return uniqueTypeInfo(new JavaScriptRealTypeInfo(type), ctxt);
        } else if (type instanceof StringType) {
            return uniqueTypeInfo(new JavaScriptStringTypeInfo(type), ctxt);
        } else if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            Assert.check(CifTypeUtils.isArrayType(ltype));

            TypeInfo[] elmTIs = new TypeInfo[1];
            elmTIs[0] = buildTypeInfo(ltype.getElementType(), ctxt);
            return uniqueTypeInfo(new JavaScriptArrayTypeInfo(type, elmTIs, ltype.getLower()), ctxt);
        } else if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;

            TypeInfo[] fieldTIs = new TypeInfo[ttype.getFields().size()];
            for (int i = 0; i < ttype.getFields().size(); i++) {
                CifType fieldType = ttype.getFields().get(i).getType();
                fieldTIs[i] = buildTypeInfo(fieldType, ctxt);
            }
            return uniqueTypeInfo(new JavaScriptTupleTypeInfo(type, fieldTIs), ctxt);
        } else {
            throw new RuntimeException("Unexpected/unsupported type: " + type);
        }
    }
}
