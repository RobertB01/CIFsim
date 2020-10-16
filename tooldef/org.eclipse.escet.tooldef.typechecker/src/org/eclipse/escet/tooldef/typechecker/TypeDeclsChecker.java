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

package org.eclipse.escet.tooldef.typechecker;

import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;

/** ToolDef type declaration type checker. */
public class TypeDeclsChecker {
    /** Constructor for the {@link TypeDeclsChecker} class. */
    private TypeDeclsChecker() {
        // Static class.
    }

    /**
     * Type check a type declaration.
     *
     * @param typeDecl The type declaration.
     * @param ctxt The type checker context.
     */
    public static void tcheck(TypeDecl typeDecl, CheckerContext ctxt) {
        // Type check the type of the type declaration. Can't have self cycles,
        // as the type declaration itself isn't added yet.
        TypesChecker.tcheck(typeDecl.getType(), ctxt);

        // Add to context.
        ctxt.addDecl(typeDecl);
    }
}
