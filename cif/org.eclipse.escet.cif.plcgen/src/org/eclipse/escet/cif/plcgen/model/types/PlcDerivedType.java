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

package org.eclipse.escet.cif.plcgen.model.types;

/** PLC derived type (reference to a user-defined data type or POU). */
public class PlcDerivedType extends PlcType {
    /** The name of the derived type. */
    public final String name;

    /**
     * Constructor for the {@link PlcDerivedType} class.
     *
     * @param name The name of the derived type.
     */
    public PlcDerivedType(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PlcDerivedType derivedType)) {
            return false;
        }
        return name.equals(derivedType.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "PlcDeriveDtype(\"" + name + "\")";
    }
}
