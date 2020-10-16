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

package org.eclipse.escet.cif.parser.ast.declarations;

import java.util.List;

import org.eclipse.escet.cif.parser.ast.ADecl;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Type definitions declaration. */
public class ATypeDefDecl extends ADecl {
    /** The type definitions that are part of this type definitions declaration. */
    public final List<ATypeDef> typeDefs;

    /**
     * Constructor for the {@link ATypeDefDecl} class.
     *
     * @param typeDefs The type definitions that are part of this type definitions declaration.
     * @param position Position information.
     */
    public ATypeDefDecl(List<ATypeDef> typeDefs, Position position) {
        super(position);
        this.typeDefs = typeDefs;
    }
}
