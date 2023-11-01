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

package org.eclipse.escet.cif.codegen.javascript;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.TypeCodeGen;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/** Type code generator for the JavaScript target language. */
public class JavaScriptTypeCodeGen extends TypeCodeGen {
    @Override
    public TypeInfo typeToTarget(CifType type, CodeContext ctxt) {
        return buildTypeInfo(type, ctxt, false);
    }

    /**
     * Build the JavaScript target language type information object for a given CIF type.
     *
     * <p>
     * This method is used internally, use {@link #typeToTarget} to convert a CIF type.
     * </p>
     *
     * @param type CIF type to convert to the JavaScript language.
     * @param ctxt Code generation context.
     * @param generic Whether the context of the type requires a generic type rather than a primitive type.
     * @return The constructed type information object.
     */
    private TypeInfo buildTypeInfo(CifType type, CodeContext ctxt, boolean generic) {
        // TODO To be implemented.
        return null;
    }
}
