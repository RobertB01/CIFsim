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

package org.eclipse.escet.cif.plcgen.model.functions;

import java.util.EnumSet;

import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;

/** Cast function with the semantic type conversion performed in a function application. */
public class PlcCastFunction extends PlcBasicFuncDescription {
    /** Parameters of the function. */
    private static final PlcParameterDescription[] FUNCTION_PARAMETERS = {
            new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY)};

    /** Type of the function application child expression. */
    public final PlcElementaryType childType;

    /** Type of the function application result. */
    public final PlcElementaryType resultType;

    /**
     * Constructor of the {@link PlcCastFunction} class.
     *
     * @param childType Type of the function application child expression.
     * @param resultType Type of the function application result.
     * @param notations Notations of the function that are supported by the target.
     */
    public PlcCastFunction(PlcElementaryType childType, PlcElementaryType resultType,
            EnumSet<PlcFuncNotation> notations)
    {
        super(childType.name + "_TO_" + resultType.name, FUNCTION_PARAMETERS, null, ExprBinding.NO_PRIORITY, notations);
        this.childType = childType;
        this.resultType = resultType;
    }
}
