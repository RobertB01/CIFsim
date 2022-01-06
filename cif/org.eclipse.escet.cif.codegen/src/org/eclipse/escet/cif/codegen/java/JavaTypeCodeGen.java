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

package org.eclipse.escet.cif.codegen.java;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.TypeCodeGen;
import org.eclipse.escet.cif.codegen.java.typeinfos.JavaArrayTypeInfo;
import org.eclipse.escet.cif.codegen.java.typeinfos.JavaBoolTypeInfo;
import org.eclipse.escet.cif.codegen.java.typeinfos.JavaEnumTypeInfo;
import org.eclipse.escet.cif.codegen.java.typeinfos.JavaIntTypeInfo;
import org.eclipse.escet.cif.codegen.java.typeinfos.JavaRealTypeInfo;
import org.eclipse.escet.cif.codegen.java.typeinfos.JavaStringTypeInfo;
import org.eclipse.escet.cif.codegen.java.typeinfos.JavaTupleTypeInfo;
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

/** Type code generator for the Java target language. */
public class JavaTypeCodeGen extends TypeCodeGen {
    @Override
    public TypeInfo typeToTarget(CifType type, CodeContext ctxt) {
        return buildTypeInfo(type, ctxt, false);
    }

    /**
     * Build the Java target language type information object for a given CIF type.
     *
     * <p>
     * This method is used internally, use {@link #typeToTarget} to convert a CIF type.
     * </p>
     *
     * @param type CIF type to convert to the Java language.
     * @param ctxt Code generation context.
     * @param generic Whether the context of the type requires a generic type rather than a primitive type.
     * @return The constructed type information object.
     */
    private TypeInfo buildTypeInfo(CifType type, CodeContext ctxt, boolean generic) {
        type = normalizeType(type);

        // Handle all different types.
        if (type instanceof BoolType) {
            return uniqueTypeInfo(new JavaBoolTypeInfo(type, generic), ctxt);
        } else if (type instanceof IntType) {
            return uniqueTypeInfo(new JavaIntTypeInfo(type, generic), ctxt);
        } else if (type instanceof EnumType) {
            return uniqueTypeInfo(new JavaEnumTypeInfo(ctxt.getPrefix()), ctxt);
        } else if (type instanceof RealType) {
            return uniqueTypeInfo(new JavaRealTypeInfo(type, generic), ctxt);
        } else if (type instanceof StringType) {
            return uniqueTypeInfo(new JavaStringTypeInfo(type), ctxt);
        } else if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            Assert.check(CifTypeUtils.isArrayType(ltype));

            TypeInfo[] elmTIs = new TypeInfo[1];
            elmTIs[0] = buildTypeInfo(ltype.getElementType(), ctxt, true);
            return uniqueTypeInfo(new JavaArrayTypeInfo(type, elmTIs, ltype.getLower()), ctxt);
        } else if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;

            TypeInfo[] fieldTIs = new TypeInfo[ttype.getFields().size()];
            for (int i = 0; i < ttype.getFields().size(); i++) {
                CifType fieldType = ttype.getFields().get(i).getType();
                fieldTIs[i] = buildTypeInfo(fieldType, ctxt, false);
            }
            return uniqueTypeInfo(new JavaTupleTypeInfo(type, fieldTIs), ctxt);
        } else {
            throw new RuntimeException("Unexpected/unsupported type: " + type);
        }
    }
}
