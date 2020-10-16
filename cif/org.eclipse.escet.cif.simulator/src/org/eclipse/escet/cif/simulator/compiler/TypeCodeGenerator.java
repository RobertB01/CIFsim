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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;

/** Type code generator. */
public class TypeCodeGenerator {
    /** Constructor for the {@link TypeCodeGenerator} class. */
    private TypeCodeGenerator() {
        // Static class.
    }

    /**
     * Generate a Java code fragment for the given type, and may use primitive types at the top-level.
     *
     * @param type The type.
     * @param ctxt The compiler context to use.
     * @return The Java code that represents the given type.
     */
    public static String gencodeType(CifType type, CifCompilerContext ctxt) {
        return gencodeType(type, ctxt, false);
    }

    /**
     * Generate a Java code fragment for the given type.
     *
     * @param type The type.
     * @param ctxt The compiler context to use.
     * @param generic Whether the type code is used as a generic type parameter (and may thus not contain primitive
     *     types).
     * @return The Java code that represents the given type.
     */
    public static String gencodeType(CifType type, CifCompilerContext ctxt, boolean generic) {
        if (type instanceof BoolType) {
            return generic ? "Boolean" : "boolean";
        } else if (type instanceof IntType) {
            return generic ? "Integer" : "int";
        } else if (type instanceof TypeRef) {
            return gencodeType(((TypeRef)type).getType().getType(), ctxt, generic);
        } else if (type instanceof EnumType) {
            EnumDecl enumDecl = ((EnumType)type).getEnum();
            return ctxt.getEnumClassName(enumDecl);
        } else if (type instanceof RealType) {
            return generic ? "Double" : "double";
        } else if (type instanceof StringType) {
            return "String";
        } else if (type instanceof ListType) {
            CifType etype = ((ListType)type).getElementType();
            return fmt("List<%s>", gencodeType(etype, ctxt, true));
        } else if (type instanceof SetType) {
            CifType etype = ((SetType)type).getElementType();
            return fmt("Set<%s>", gencodeType(etype, ctxt, true));
        } else if (type instanceof FuncType) {
            return ctxt.getFuncTypeClassName((FuncType)type);
        } else if (type instanceof DictType) {
            CifType ktype = ((DictType)type).getKeyType();
            CifType vtype = ((DictType)type).getValueType();
            return fmt("Map<%s, %s>", gencodeType(ktype, ctxt, true), gencodeType(vtype, ctxt, true));
        } else if (type instanceof TupleType) {
            return ctxt.getTupleTypeClassName((TupleType)type);
        } else if (type instanceof DistType) {
            DistType dtype = (DistType)type;
            CifType nstype = normalizeType(dtype.getSampleType());

            if (nstype instanceof BoolType) {
                return "BooleanDistribution";
            } else if (nstype instanceof IntType) {
                return "IntegerDistribution";
            } else if (nstype instanceof RealType) {
                return "RealDistribution";
            } else {
                String msg = "Unknown distribution sample type: " + nstype;
                throw new RuntimeException(msg);
            }
        } else if (type instanceof VoidType) {
            throw new RuntimeException("Unexpected void type: " + type);
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }
}
