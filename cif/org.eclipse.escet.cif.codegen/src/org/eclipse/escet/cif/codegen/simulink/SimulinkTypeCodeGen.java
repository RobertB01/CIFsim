//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen.simulink;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.TypeCodeGen;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89BoolTypeInfo;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89EnumTypeInfo;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89StringTypeInfo;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89TupleTypeInfo;
import org.eclipse.escet.cif.codegen.simulink.typeinfos.SimulinkArrayTypeInfo;
import org.eclipse.escet.cif.codegen.simulink.typeinfos.SimulinkIntTypeInfo;
import org.eclipse.escet.cif.codegen.simulink.typeinfos.SimulinkRealTypeInfo;
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

/** Type code generator for Simulink. */
public class SimulinkTypeCodeGen extends TypeCodeGen {
    @Override
    public TypeInfo typeToTarget(CifType type, CodeContext ctxt) {
        final boolean genLocalFunctions = true;
        type = normalizeType(type);

        if (type instanceof BoolType) {
            return uniqueTypeInfo(new C89BoolTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof IntType) {
            return uniqueTypeInfo(new SimulinkIntTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof EnumType) {
            return uniqueTypeInfo(new C89EnumTypeInfo(genLocalFunctions, ctxt.getPrefix(), (EnumType)type), ctxt);
        } else if (type instanceof RealType) {
            return uniqueTypeInfo(new SimulinkRealTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof StringType) {
            return uniqueTypeInfo(new C89StringTypeInfo(genLocalFunctions, type), ctxt);
        } else if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            Assert.check(CifTypeUtils.isArrayType(ltype));

            TypeInfo[] elmTIs = new TypeInfo[1];
            elmTIs[0] = typeToTarget(ltype.getElementType(), ctxt);
            return uniqueTypeInfo(new SimulinkArrayTypeInfo(genLocalFunctions, type, elmTIs, ltype.getLower()), ctxt);
        } else if (type instanceof TupleType) {
            TupleType ttype = (TupleType)type;

            TypeInfo[] fieldTIs = new TypeInfo[ttype.getFields().size()];
            for (int i = 0; i < ttype.getFields().size(); i++) {
                CifType fieldType = ttype.getFields().get(i).getType();
                fieldTIs[i] = typeToTarget(fieldType, ctxt);
            }
            return uniqueTypeInfo(new C89TupleTypeInfo(genLocalFunctions, type, fieldTIs), ctxt);
        } else {
            throw new RuntimeException("Unexpected/unsupported type: " + type);
        }
    }
}
