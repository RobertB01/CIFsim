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

package org.eclipse.escet.cif.plcgen.model.declarations;

import org.eclipse.escet.cif.plcgen.model.functions.PlcFunctionBlockDescription;

/** Variable holding the data of an instantiated function block. */
public class PlcFuncBlockInstanceVar extends PlcBasicVariable {
    /** Description of the function block. */
    public final PlcFunctionBlockDescription funcBlockDesc;

    /**
     * Constructor of the {@link PlcFuncBlockInstanceVar} class.
     *
     * @param declName The name of the variable declaration.
     * @param funcBlockDesc Description of the function block.
     */
    public PlcFuncBlockInstanceVar(String declName, PlcFunctionBlockDescription funcBlockDesc) {
        super("", declName, funcBlockDesc.funcBlockType);
        this.funcBlockDesc = funcBlockDesc;
    }

    /**
     * Constructor of the {@link PlcFuncBlockInstanceVar} class.
     *
     * @param storagePrefix Name prefix of the variable, to use when accessing the variable.
     * @param declName The name of the variable declaration.
     * @param funcBlockDesc Description of the function block.
     */
    public PlcFuncBlockInstanceVar(String storagePrefix, String declName, PlcFunctionBlockDescription funcBlockDesc) {
        super(storagePrefix, declName, funcBlockDesc.funcBlockType);
        this.funcBlockDesc = funcBlockDesc;
    }
}
