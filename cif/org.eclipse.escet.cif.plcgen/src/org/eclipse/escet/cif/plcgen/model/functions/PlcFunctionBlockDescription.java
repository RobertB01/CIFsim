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

import org.eclipse.escet.cif.plcgen.model.types.PlcDerivedType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;

/** Description of an instantiated function block. */
public class PlcFunctionBlockDescription extends PlcBasicFuncDescription {
    /** The name of the type of function block. */
    public final PlcType funcBlockType;

    /**
     * Constructor of the {@link PlcFunctionBlockDescription} class.
     *
     * @param instanceName Name of the instance.
     * @param funcBlockType Type of the function block.
     * @param parameters Parameters of the function block type.
     *
     */
    public PlcFunctionBlockDescription(String instanceName, PlcType funcBlockType,
            PlcParameterDescription[] parameters)
    {
        super(instanceName, parameters, null, ExprBinding.NO_PRIORITY, PlcFuncNotation.FORMAL_ONLY);
        this.funcBlockType = funcBlockType;
    }

    /**
     * Instantiate a TON function block.
     *
     * @param instanceName Name of the instance.
     * @return The function block instance description.
     */
    public static PlcFunctionBlockDescription makeTonBlock(String instanceName) {
        // TODO: Should be instantiated by the target like the normal functions to allow customization by the target,
        // but no need currently.
        // TODO: Distinguish between type declaration name, instance name, and function in its use.
        PlcParameterDescription[] params = { //
                new PlcParameterDescription("IN", PlcParamDirection.INPUT_ONLY), // Boolean, false resets the timer,
                                                                                 // true allows measuring time.
                new PlcParameterDescription("PT", PlcParamDirection.INPUT_ONLY), // Real, end-time, should be positive.
                new PlcParameterDescription("Q", PlcParamDirection.OUTPUT_ONLY), // Boolean whether end-time has been
                                                                                 // reached.
                new PlcParameterDescription("ET", PlcParamDirection.OUTPUT_ONLY)}; // Real, amount of measured time
                                                                                   // since last reset, caps at PT.
        return new PlcFunctionBlockDescription(instanceName, new PlcDerivedType("TON"), params);
    }
}
