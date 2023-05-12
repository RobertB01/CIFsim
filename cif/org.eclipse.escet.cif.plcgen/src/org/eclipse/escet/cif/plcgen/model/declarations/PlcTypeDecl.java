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

package org.eclipse.escet.cif.plcgen.model.declarations;

import org.eclipse.escet.cif.plcgen.model.types.PlcType;

/** PLC type declaration. */
public class PlcTypeDecl {
    /** The name of the type declaration. */
    public final String name;

    /** The type of the type declaration. */
    public final PlcType type;

    /**
     * Constructor for the {@link PlcTypeDecl} class.
     *
     * @param name The name of the type declaration.
     * @param type The type of the type declaration.
     */
    public PlcTypeDecl(String name, PlcType type) {
        this.name = name;
        this.type = type;
    }
}
