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

package org.eclipse.escet.cif.plcgen.model.types;

/** Field in a structure. */
public class PlcStructField {
    /** The name of the field. */
    public final String fieldName;

    /** The type of the field. */
    public final PlcType type;

    /**
     * Constructor of the {@link PlcStructField} class.
     *
     * @param fieldName The name of the field.
     * @param type The type of the field.
     */
    public PlcStructField(String fieldName, PlcType type) {
        this.fieldName = fieldName;
        this.type = type;
    }

    @Override
    public String toString() {
        return fieldName + ": " + type.toString();
    }
}
