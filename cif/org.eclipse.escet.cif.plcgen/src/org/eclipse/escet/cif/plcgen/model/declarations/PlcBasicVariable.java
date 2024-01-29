//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

/** Basic PLC variable or constant. */
public abstract class PlcBasicVariable {
    /** Text to use for accessing the data of the variable non-locally. */
    public final String valueName;

    /** The name of the variable. */
    public final String varName;

    /** The type of the variable. */
    public final PlcType type;

    /**
     * Constructor for the {@link PlcBasicVariable} class.
     *
     * @param stateVariablePrefix Name prefix of the variable, to use when accessing the variable.
     * @param varName The name of the variable.
     * @param type The type of the variable.
     */
    public PlcBasicVariable(String stateVariablePrefix, String varName, PlcType type) {
        this.valueName = stateVariablePrefix + varName;
        this.varName = varName;
        this.type = type;
    }
}
