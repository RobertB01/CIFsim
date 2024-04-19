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

import java.util.List;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcDeclaredType;

/** PLC struct type. */
public class PlcStructType extends PlcType implements PlcDeclaredType {
    /** Name of the struct type. */
    public final String typeName;

    /** The fields of the struct type. */
    public List<PlcStructField> fields;

    /**
     * Constructor of the {@link PlcStructType} class.
     *
     * @param typeName Name of the struct type.
     * @param fields The fields of the struct type.
     */
    public PlcStructType(String typeName, List<PlcStructField> fields) {
        this.typeName = typeName;
        this.fields = fields;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PlcStructType otherStructType) || !typeName.equals((otherStructType.typeName))
                || fields.size() != otherStructType.fields.size())
        {
            return false;
        }
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).fieldName.equals(otherStructType.fields.get(i).fieldName)) {
                return false;
            }
            if (!fields.get(i).type.equals(otherStructType.fields.get(i).type)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (PlcStructField field: fields) {
            h = h + field.fieldName.hashCode() + field.type.hashCode() * 23;
        }
        return h;
    }
}
