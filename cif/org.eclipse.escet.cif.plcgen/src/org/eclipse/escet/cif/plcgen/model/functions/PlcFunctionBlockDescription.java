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

import org.eclipse.escet.cif.plcgen.model.types.PlcAbstractType;

/** Description of an instantiated function block. */
public class PlcFunctionBlockDescription extends PlcBasicFuncDescription {
    /** Name of the function block type. */
    public final String typeName;

    /**
     * Constructor of the {@link PlcFunctionBlockDescription} class.
     *
     * @param operation The semantic operation performed by the function.
     * @param typeName Name of the function block type.
     * @param prefixFuncName Name of the function in prefix notation, the empty string if the function name should not
     *     be used, or {@code null} if the prefix form does not exist.
     * @param parameters Parameters of the function block type.
     * @param resultType Type of the result of the function.
     */
    public PlcFunctionBlockDescription(PlcFuncOperation operation, String typeName, String prefixFuncName,
            PlcParameterDescription[] parameters, PlcAbstractType resultType)
    {
        super(operation, prefixFuncName, parameters, PlcFuncNotation.FORMAL_ONLY, resultType,
                PlcFuncTypeExtension.NEVER);
        this.typeName = typeName;
    }
}
