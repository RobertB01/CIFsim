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

package org.eclipse.escet.cif.plcgen.model.expressions;

/** An value with a name. */
public class PlcNamedValue {
    /** Name of the value. */
    public final String name;

    /** The value itself. */
    public final PlcExpression value;

    /**
     * Constructor of the {@link PlcNamedValue} class.
     *
     * @param name Name of the value.
     * @param value The value itself.
     */
    public PlcNamedValue(String name, PlcExpression value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Make an independent copy of this.
     *
     * @return The copied this.
     */
    public PlcNamedValue copy() {
        return new PlcNamedValue(name, value.copy());
    }
}
