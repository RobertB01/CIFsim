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

package org.eclipse.escet.cif.plcgen.model.types;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;

/** PLC struct type. */
public class PlcStructType extends PlcType {
    /** The fields of the struct type. */
    public List<PlcVariable> fields = list();

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PlcStructType structType) || fields.size() != structType.fields.size()) {
            return false;
        }
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).name.equals(structType.fields.get(i).name)) {
                return false;
            }
            if (!fields.get(i).type.equals(structType.fields.get(i).type)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (PlcVariable field: fields) {
            h = h + field.name.hashCode() + field.type.hashCode() * 23;
        }
        return h;
    }
}
