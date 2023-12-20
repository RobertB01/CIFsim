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

package org.eclipse.escet.cif.codegen.c89;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.TypeCodeGen;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89ArrayTypeInfo;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89BoolTypeInfo;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89EnumTypeInfo;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89IntTypeInfo;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89RealTypeInfo;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89StringTypeInfo;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89TupleTypeInfo;
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

/** C89 type code generator for CIF. */
public class C89TypeCodeGen extends TypeCodeGen {
    @Override
    public TypeInfo typeToTarget(CifType type, CodeContext ctxt) {
        return buildTypeInfo(type, ctxt);
    }

    /**
     * Build the C89 target language type information object for a given CIF type.
     *
     * <p>
     * This method is used internally, use {@link #typeToTarget} to convert a CIF type.
     * </p>
     *
     * @param type CIF type to convert to the Java language.
     * @param ctxt The code generation context.
     * @return The constructed type information object.
     */
    private TypeInfo buildTypeInfo(CifType type, CodeContext ctxt) {
        final boolean genLocalFunctions = false;
        type = normalizeType(type);

        if (type instanceof BoolType) {
            return uniqueTypeInfo(new C89BoolTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof IntType) {
            return uniqueTypeInfo(new C89IntTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof EnumType) {
            return uniqueTypeInfo(new C89EnumTypeInfo(genLocalFunctions, ctxt.getPrefix(), (EnumType)type), ctxt);
        } else if (type instanceof RealType) {
            return uniqueTypeInfo(new C89RealTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof StringType) {
            return uniqueTypeInfo(new C89StringTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            Assert.check(CifTypeUtils.isArrayType(ltype));

            TypeInfo[] elmTIs = new TypeInfo[1];
            elmTIs[0] = buildTypeInfo(ltype.getElementType(), ctxt);
            return uniqueTypeInfo(new C89ArrayTypeInfo(genLocalFunctions, type, elmTIs, ltype.getLower()), ctxt);
        } else if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;

            TypeInfo[] fieldTIs = new TypeInfo[ttype.getFields().size()];
            for (int i = 0; i < ttype.getFields().size(); i++) {
                CifType fieldType = ttype.getFields().get(i).getType();
                fieldTIs[i] = buildTypeInfo(fieldType, ctxt);
            }
            return uniqueTypeInfo(new C89TupleTypeInfo(genLocalFunctions, type, fieldTIs), ctxt);
        } else {
            throw new RuntimeException("Unexpected/unsupported type: " + type);
        }
    }
}
