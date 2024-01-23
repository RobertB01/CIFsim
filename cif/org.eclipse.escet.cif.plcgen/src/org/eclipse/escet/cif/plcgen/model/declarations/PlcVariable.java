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

/** PLC variable (or constant). */
public class PlcVariable {
    /** Text to use for accessing the data of the variable non-locally. */
    public final String valueName;

    /** The name of the variable. */
    public final String varName;

    /** The name of the structure field. */
    public final String fieldName;

    /** The type of the variable. */
    public final PlcType type;

    /** The address of the variable, or {@code null} if not specified. */
    public final String address;

    /** The initial value of the variable, or {@code null} if not specified. */
    public final PlcExpression value;

    /**
     * Constructor for the {@link PlcVariable} class, without address and initial value, and with an empty name prefix.
     *
     * @param name The name of the variable.
     * @param type The type of the variable.
     */
    public PlcVariable(String name, PlcType type) {
        this(name, type, null, null);
    }

    /**
     * Constructor for the {@link PlcVariable} class, with an empty name prefix.
     *
     * @param name The name of the variable.
     * @param type The type of the variable.
     * @param address The address of the variable, or {@code null} if not specified.
     * @param value The initial value of the variable, or {@code null} if not specified.
     */
    public PlcVariable(String name, PlcType type, String address, PlcExpression value) {
        this("", name, type, address, value);
    }

    /**
     * Constructor for the {@link PlcVariable} class.
     *
     * @param prefix Name prefix of the variable, to use when accessing the variable.
     * @param name The name of the variable.
     * @param type The type of the variable.
     * @param address The address of the variable, or {@code null} if not specified.
     * @param value The initial value of the variable, or {@code null} if not specified.
     */
    public PlcVariable(String prefix, String name, PlcType type, String address, PlcExpression value) {
        this.valueName = prefix + name;
        this.varName = name;
        this.fieldName = name;
        this.type = type;
        this.address = address;
        this.value = value;
    }
}
