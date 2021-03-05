//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.parser.ast.types;

import java.util.List;

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Function type. */
public class AFuncType extends ACifType {
    /** The return type of the function type. */
    public final ACifType returnType;

    /** The parameter types of the function type. */
    public final List<ACifType> paramTypes;

    /**
     * Constructor for the {@link AFuncType} class.
     *
     * @param returnType The return type of the function type.
     * @param paramTypes The parameter types of the function type.
     * @param position Position information.
     */
    public AFuncType(ACifType returnType, List<ACifType> paramTypes, Position position) {
        super(position);
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }
}
