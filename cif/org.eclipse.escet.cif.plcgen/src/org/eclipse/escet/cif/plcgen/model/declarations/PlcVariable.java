//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.plcgen.model.expressions.PlcValue;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;

/** PLC variable (or constant). */
public class PlcVariable {
    /** The name of the variable. */
    public final String name;

    /** The type of the variable. */
    public final PlcType type;

    /** The address of the variable, or {@code null} if not specified. */
    public final String address;

    /** The initial value of the variable, or {@code null} if not specified. */
    public final PlcValue value;

    /**
     * Constructor for the {@link PlcVariable} class, without address and initial value.
     *
     * @param name The name of the variable.
     * @param type The type of the variable.
     */
    public PlcVariable(String name, PlcType type) {
        this(name, type, null, null);
    }

    /**
     * Constructor for the {@link PlcVariable} class.
     *
     * @param name The name of the variable.
     * @param type The type of the variable.
     * @param address The address of the variable, or {@code null} if not specified.
     * @param value The initial value of the variable, or {@code null} if not specified.
     */
    public PlcVariable(String name, PlcType type, String address, PlcValue value) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.value = value;
    }
}
