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

package org.eclipse.escet.cif.codegen.c99;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.TypeCodeGen;
import org.eclipse.escet.cif.codegen.c99.typeinfos.C99ArrayTypeInfo;
import org.eclipse.escet.cif.codegen.c99.typeinfos.C99BoolTypeInfo;
import org.eclipse.escet.cif.codegen.c99.typeinfos.C99EnumTypeInfo;
import org.eclipse.escet.cif.codegen.c99.typeinfos.C99IntTypeInfo;
import org.eclipse.escet.cif.codegen.c99.typeinfos.C99RealTypeInfo;
import org.eclipse.escet.cif.codegen.c99.typeinfos.C99StringTypeInfo;
import org.eclipse.escet.cif.codegen.c99.typeinfos.C99TupleTypeInfo;
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

/** C99 type code generator for CIF. */
public class C99TypeCodeGen extends TypeCodeGen {
    @Override
    public TypeInfo typeToTarget(CifType type, CodeContext ctxt) {
        return buildTypeInfo(type, ctxt);
    }

    /**
     * Build the C99 target language type information object for a given CIF type.
     *
     * <p>
     * This method is used internally, use {@link #typeToTarget} to convert a CIF type.
     * </p>
     *
     * @param type CIF type to convert to the Java language.
     * @param ctxt Code generation context.
     * @return The constructed type information object.
     */
    private TypeInfo buildTypeInfo(CifType type, CodeContext ctxt) {
        final boolean genLocalFunctions = false;
        type = normalizeType(type);

        if (type instanceof BoolType) {
            return uniqueTypeInfo(new C99BoolTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof IntType) {
            return uniqueTypeInfo(new C99IntTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof EnumType) {
            return uniqueTypeInfo(new C99EnumTypeInfo(genLocalFunctions, ctxt.getPrefix(), (EnumType)type), ctxt);
        } else if (type instanceof RealType) {
            return uniqueTypeInfo(new C99RealTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof StringType) {
            return uniqueTypeInfo(new C99StringTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            Assert.check(CifTypeUtils.isArrayType(ltype));

            TypeInfo[] elmTIs = new TypeInfo[1];
            elmTIs[0] = buildTypeInfo(ltype.getElementType(), ctxt);
            return uniqueTypeInfo(new C99ArrayTypeInfo(genLocalFunctions, type, elmTIs, ltype.getLower()), ctxt);
        } else if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;

            TypeInfo[] fieldTIs = new TypeInfo[ttype.getFields().size()];
            for (int i = 0; i < ttype.getFields().size(); i++) {
                CifType fieldType = ttype.getFields().get(i).getType();
                fieldTIs[i] = buildTypeInfo(fieldType, ctxt);
            }
            return uniqueTypeInfo(new C99TupleTypeInfo(genLocalFunctions, type, fieldTIs), ctxt);
        } else {
            throw new RuntimeException("Unexpected/unsupported type: " + type);
        }
    }
}
