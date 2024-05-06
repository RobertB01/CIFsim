//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.functions;

import org.eclipse.escet.cif.plcgen.model.types.PlcType;

/** Description of an instantiated function block. */
public class PlcFunctionBlockDescription extends PlcBasicFuncDescription {
    /** The name of the type of function block. */
    public final PlcType funcBlockType;

    /**
     * Constructor of the {@link PlcFunctionBlockDescription} class.
     *
     * @param prefixFuncName Name of the function.
     * @param funcBlockType Type of the function block.
     * @param parameters Parameters of the function block type.
     */
    @Deprecated
    public PlcFunctionBlockDescription(String prefixFuncName, PlcType funcBlockType,
            PlcParameterDescription[] parameters)
    {
        super(prefixFuncName, parameters, PlcFuncNotation.FORMAL_ONLY, null);
        this.funcBlockType = funcBlockType;
    }
}
