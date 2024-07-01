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

import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;

/** Cast function with the semantic type conversion performed in a function application. */
public class PlcCastFunctionDescription extends PlcBasicFuncDescription {
    /**
     * Constructor of the {@link PlcCastFunctionDescription} class.
     *
     * @param childType Type of the function application child expression.
     * @param resultType Type of the function application result.
     */
    public PlcCastFunctionDescription(PlcElementaryType childType, PlcElementaryType resultType) {
        super(childType.name + "_TO_" + resultType.name,
                new PlcParameterDescription[]
                {new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY, childType)},
                PlcBasicFuncDescription.PlcFuncNotation.NOT_INFIX, resultType, PlcFuncTypeExtension.NEVER);
    }
}
