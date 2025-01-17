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

import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;

/** Concrete variable storing plain old data (POD), useful for declaring the variable. */
public class PlcDataVariable extends PlcBasicVariable {
    /** The address of the variable, or {@code null} if not specified. */
    public final String address;

    /** The initial value of the variable, or {@code null} if not specified. */
    public final PlcExpression value;

    /**
     * Constructor for the {@link PlcDataVariable} class, without address and initial value.
     *
     * @param varName The name of the variable.
     * @param type The type of the variable.
     */
    public PlcDataVariable(String varName, PlcType type) {
        this(varName, varName, type, null, null);
    }

    /**
     * Constructor for the {@link PlcDataVariable} class.
     *
     * @param targetText Name of the variable in the target code, to use when accessing the variable.
     * @param varName The name of the variable.
     * @param type The type of the variable.
     * @param address The address of the variable, or {@code null} if not specified.
     * @param value The initial value of the variable, or {@code null} if not specified.
     */
    public PlcDataVariable(String targetText, String varName, PlcType type, String address, PlcExpression value) {
        super(targetText, varName, type);
        this.address = address;
        this.value = value;
    }
}
