//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.conversion;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcFuncBlockInstanceVar;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFunctionBlockDescription;

/** Results of a instantiated function block. */
public class PlcInstantiatedFunctionBlockData {
    /** The created function block description, useful for calling the function block. */
    public final PlcFunctionBlockDescription funcBlockDescription;

    /** The created function block instance variable, useful for accessing the data fields of the function block. */
    public final PlcFuncBlockInstanceVar blockFunctionVariable;

    /**
     * Constructor of the {@link PlcInstantiatedFunctionBlockData} class.
     *
     * @param funcBlockDescription The created function block description, useful for calling the function block.
     * @param blockFunctionVariable The created function block instance variable, useful for accessing the data fields
     *     of the function block.
     */
    public PlcInstantiatedFunctionBlockData(PlcFunctionBlockDescription funcBlockDescription,
            PlcFuncBlockInstanceVar blockFunctionVariable)
    {
        this.funcBlockDescription = funcBlockDescription;
        this.blockFunctionVariable = blockFunctionVariable;
    }
}
