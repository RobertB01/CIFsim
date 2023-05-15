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

import java.util.List;

/** PLC enum type. */
public class PlcEnumType extends PlcType {
    /** The literals of the enum type. */
    public final List<String> literals;

    /**
     * Constructor for the {@link PlcEnumType} class.
     *
     * @param literals The literals of the enum type.
     */
    public PlcEnumType(List<String> literals) {
        this.literals = literals;
    }
}
