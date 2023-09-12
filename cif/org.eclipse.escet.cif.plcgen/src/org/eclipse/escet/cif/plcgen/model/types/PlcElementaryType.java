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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PlcElementaryType elementaryType)) {
            return false;
        }
        return name.equals(elementaryType.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Retrieve the integer type that uses the given number of bits in PLC memory.
     *
     * @param numBits Wanted length of integer values in bits.
     * @return The PLC integer type with exactly the requested number of bits.
     */
    public static PlcElementaryType getIntTypeBySize(int numBits) {
        switch (numBits) {
            case 16:
                return INT_TYPE;
            case 32:
                return DINT_TYPE;
            case 64:
                return LINT_TYPE;
            default:
                throw new AssertionError("Unexpected integer size " + String.valueOf(numBits) + " found.");
        }
    }

    /**
     * Retrieve the real type that uses the given number of bits in PLC memory.
     *
     * @param numBits Wanted length of real values in bits.
     * @return The PLC real type with exactly the requested number of bits.
     */
    public static PlcElementaryType getRealTypeBySize(int numBits) {
        switch (numBits) {
            case 32:
                return REAL_TYPE;
            case 64:
                return LREAL_TYPE;
            default:
                throw new AssertionError("Unexpected real size " + String.valueOf(numBits) + " found.");
        }
    }
}