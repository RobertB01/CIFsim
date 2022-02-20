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

package org.eclipse.escet.cif.codegen;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/** Type code generator for the target language. */
public abstract class TypeCodeGen {
    /**
     * Available authorative type information objects.
     *
     * <p>
     * To add a new type, use {@link #typeToTarget}.
     * </p>
     */
    private Map<TypeInfo, TypeInfo> knownTypes;

    /** Initialize the type code generator. */
    protected void init() {
        knownTypes = map();
    }

    /** Cleanup the data of the type code generator. */
    protected void cleanup() {
        knownTypes = null;
    }

    /**
     * Retrieve the authorative type information object for the given type information, during conversion from a CIF
     * type, by {@link #typeToTarget}.
     *
     * <p>
     * If the type information is new, the given type information object is taken as authorative type. Also, its
     * {@link TypeInfo#generateCode} method is called to allow generation of type code.
     * </p>
     *
     * @param ti Type information object to search for.
     * @param ctxt Code generation context.
     * @return The authorative type information object.
     */
    protected TypeInfo uniqueTypeInfo(TypeInfo ti, CodeContext ctxt) {
        TypeInfo knownTI = knownTypes.get(ti);
        if (knownTI != null) {
            return knownTI;
        }

        knownTypes.put(ti, ti);
        ti.generateCode(ctxt);
        return ti;
    }

    /**
     * Convert a CIF type to a target language type information object.
     *
     * <p>
     * The implementation should construct the given type bottom up, in type information objects, and pass them through
     * {@link #uniqueTypeInfo} to make them authorative, and avoid duplicate types in the target language.
     * </p>
     *
     * @param type CIF type to convert to the target language.
     * @param ctxt Code generation context.
     * @return The converted target language type information object.
     */
    public abstract TypeInfo typeToTarget(CifType type, CodeContext ctxt);
}
