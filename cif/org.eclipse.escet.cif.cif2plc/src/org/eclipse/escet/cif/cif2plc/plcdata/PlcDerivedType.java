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

package org.eclipse.escet.cif.cif2plc.plcdata;

/** PLC derived type (reference to a user-defined data type or POU). */
public class PlcDerivedType extends PlcType {
    /** PLC 'STATE' type reference. */
    public static final PlcDerivedType STATE_TYPE = new PlcDerivedType("STATE");

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
}
