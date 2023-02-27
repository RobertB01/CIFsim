//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.expressions;

import java.util.List;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType;

/** Cast function with the semantic type conversion performed in a function application. */
public class PlcCastFunction extends PlcBasicFuncDescription {
    /** Parameters of the function. */
    private static final List<PlcParameterDescription> FUNCTION_PARAMETERS;

    /** Type of the function application child expression. */
    public final PlcElementaryType childType;

    /** Type of the function application result. */
    public final PlcElementaryType resultType;

    /**
     * Constructor of the {@link PlcCastFunction} class.
     *
     * @param childType  Type of the function application child expression.
     * @param resultType Type of the function application result.
     */
    public PlcCastFunction(PlcElementaryType childType, PlcElementaryType resultType) {
        super(childType.name + "_TO_" + resultType.name, FUNCTION_PARAMETERS, null, ExprBinding.NO_PRIORITY);
        this.childType = childType;
        this.resultType = resultType;
    }

    static {
        FUNCTION_PARAMETERS = List.of(new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY),
                new PlcParameterDescription("OUT", PlcParamDirection.OUTPUT_ONLY));
    }
}
