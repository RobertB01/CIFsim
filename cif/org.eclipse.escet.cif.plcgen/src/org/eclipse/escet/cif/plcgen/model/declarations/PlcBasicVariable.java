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

import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;

/** Basic PLC variable or constant. */
public class PlcBasicVariable {
    /** Text to use for accessing the data of the variable non-locally. */
    public final String valueName;

    /** The name of the variable. */
    public final String varName;

    /** The type of the variable. */
    public final PlcType type;

    /** The address of the variable, or {@code null} if not specified. */
    public final String address;

    /** The initial value of the variable, or {@code null} if not specified. */
    public final PlcExpression value;

    /**
     * Constructor for the {@link PlcBasicVariable} class, without address and initial value, and with an empty name prefix.
     *
     * @param varName The name of the variable.
     * @param type The type of the variable.
     */
    public PlcBasicVariable(String varName, PlcType type) {
        this(varName, type, null, null);
    }

    /**
     * Constructor for the {@link PlcBasicVariable} class, with an empty name prefix.
     *
     * @param varName The name of the variable.
     * @param type The type of the variable.
     * @param address The address of the variable, or {@code null} if not specified.
     * @param value The initial value of the variable, or {@code null} if not specified.
     */
    public PlcBasicVariable(String varName, PlcType type, String address, PlcExpression value) {
        this("", varName, type, address, value);
    }

    /**
     * Constructor for the {@link PlcBasicVariable} class.
     *
     * @param storagePrefix Name prefix of the variable, to use when accessing the variable.
     * @param varName The name of the variable.
     * @param type The type of the variable.
     * @param address The address of the variable, or {@code null} if not specified.
     * @param value The initial value of the variable, or {@code null} if not specified.
     */
    public PlcBasicVariable(String storagePrefix, String varName, PlcType type, String address, PlcExpression value) {
        this.valueName = storagePrefix + varName;
        this.varName = varName;
        this.type = type;
        this.address = address;
        this.value = value;
    }
}
