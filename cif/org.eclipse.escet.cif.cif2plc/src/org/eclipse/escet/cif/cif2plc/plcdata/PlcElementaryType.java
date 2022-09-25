//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

/** PLC elementary type. */
public class PlcElementaryType extends PlcType {
    /** PLC BOOL type. */
    public static final PlcElementaryType BOOL_TYPE = new PlcElementaryType("BOOL");

    /** PLC INT type. */
    public static final PlcElementaryType INT_TYPE = new PlcElementaryType("INT");

    /** PLC DINT type. */
    public static final PlcElementaryType DINT_TYPE = new PlcElementaryType("DINT");

    /** PLC LINT type. */
    public static final PlcElementaryType LINT_TYPE = new PlcElementaryType("LINT");

    /** PLC REAL type. */
    public static final PlcElementaryType REAL_TYPE = new PlcElementaryType("REAL");

    /** PLC LREAL type. */
    public static final PlcElementaryType LREAL_TYPE = new PlcElementaryType("LREAL");

    /** PLC TIME type. */
    public static final PlcElementaryType TIME_TYPE = new PlcElementaryType("TIME");

    /** The name of the elementary type. */
    public final String name;

    /**
     * Constructor for the {@link PlcElementaryType} class.
     *
     * @param name The name of the elementary type.
     */
    public PlcElementaryType(String name) {
        this.name = name;
    }
}
