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
    /** Name prefix of the variable. */
    public final String prefix;

    /** The name of the variable. */
    public final String name;

    /** The type of the variable. */
    public final PlcType type;

    /** The address of the variable, or {@code null} if not specified. */
    public final String address;

    /** The initial value of the variable, or {@code null} if not specified. */
    public final PlcExpression value;

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
    public PlcVariable(String name, PlcType type, String address, PlcExpression value) {
        this("", name, type, address, value);
    }

    /**
     * Constructor for the {@link PlcVariable} class.
     *
     * @param prefix Name prefix of the variable.
     * @param name The name of the variable.
     * @param type The type of the variable.
     * @param address The address of the variable, or {@code null} if not specified.
     * @param value The initial value of the variable, or {@code null} if not specified.
     */
    public PlcVariable(String prefix, String name, PlcType type, String address, PlcExpression value) {
        this.prefix = prefix;
        this.name = name;
        this.type = type;
        this.address = address;
        this.value = value;
    }

    /**
     * Get the full name of the variable to access it.
     *
     * <p>
     * The full name of a state variable includes the prefix. This may be used for example to denote where it is stored.
     * </p>
     *
     * @return The full name of the variable.
     */
    public String getFullName() {
        return prefix + name;
    }
}
