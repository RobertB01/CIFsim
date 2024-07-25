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

/** PLC elementary type. */
public class PlcElementaryType extends PlcType {
    /** PLC BOOL type. */
    public static final PlcElementaryType BOOL_TYPE = new PlcElementaryType("BOOL", 0);

    /** PLC INT type. */
    public static final PlcElementaryType INT_TYPE = new PlcElementaryType("INT", 16);

    /** PLC DINT type. */
    public static final PlcElementaryType DINT_TYPE = new PlcElementaryType("DINT", 32);

    /** PLC LINT type. */
    public static final PlcElementaryType LINT_TYPE = new PlcElementaryType("LINT", 64);

    /** PLC REAL type. */
    public static final PlcElementaryType REAL_TYPE = new PlcElementaryType("REAL", 32);

    /** PLC LREAL type. */
    public static final PlcElementaryType LREAL_TYPE = new PlcElementaryType("LREAL", 64);

    /** PLC TIME type. */
    public static final PlcElementaryType TIME_TYPE = new PlcElementaryType("TIME", 0);

    /** Elementary types that are considered 'integer' for 32 bit systems, ordered by increasing size. */
    public static final List<PlcElementaryType> INTEGER_TYPES_32 = List.of(INT_TYPE, DINT_TYPE);

    /** Elementary types that are considered 'integer' for 64 bit systems, ordered by increasing size. */
    public static final List<PlcElementaryType> INTEGER_TYPES_64 = List.of(INT_TYPE, DINT_TYPE, LINT_TYPE);

    /** All integer types that are considered 'integer', ordered by increasing size. */
    public static final List<PlcElementaryType> INTEGER_TYPES_ALL = INTEGER_TYPES_64;

    /** Elementary types that are considered 'real' for 32 bit systems, ordered by increasing size. */
    public static final List<PlcElementaryType> REAL_TYPES_32 = List.of(REAL_TYPE);

    /** Elementary types that are considered 'real' for 64 bit systems, ordered by increasing size. */
    public static final List<PlcElementaryType> REAL_TYPES_64 = List.of(REAL_TYPE, LREAL_TYPE);

    /** All real types that are considered 'real', ordered by increasing size. */
    public static final List<PlcElementaryType> REAL_TYPES_ALL = REAL_TYPES_64;

    /** The name of the elementary type. */
    public final String name;

    /** Size of the value in bits. {@code 0} means 'unknown'. */
    public final int bitSize;

    /**
     * Constructor for the {@link PlcElementaryType} class.
     *
     * @param name The name of the elementary type.
     * @param bitSize Size of the value in bits. {@code 0} means 'unknown'.
     */
    private PlcElementaryType(String name, int bitSize) {
        this.name = name;
        this.bitSize = bitSize;
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

    @Override
    public String toString() {
        return "PlcElementaryType(\"" + name + "\")";
    }

    /**
     * Retrieve the integer type that uses the given number of bits in PLC memory.
     *
     * @param numBits Wanted length of integer values in bits.
     * @return The PLC integer type with exactly the requested number of bits.
     */
    public static PlcElementaryType getIntTypeBySize(int numBits) {
        for (PlcElementaryType elemType: INTEGER_TYPES_ALL) {
            if (elemType.bitSize == numBits) {
                return elemType;
            }
        }
        throw new AssertionError("Unexpected integer size " + String.valueOf(numBits) + " found.");
    }

    /**
     * Retrieve the real type that uses the given number of bits in PLC memory.
     *
     * @param numBits Wanted length of real values in bits.
     * @return The PLC real type with exactly the requested number of bits.
     */
    public static PlcElementaryType getRealTypeBySize(int numBits) {
        for (PlcElementaryType elemType: REAL_TYPES_ALL) {
            if (elemType.bitSize == numBits) {
                return elemType;
            }
        }
        throw new AssertionError("Unexpected real size " + String.valueOf(numBits) + " found.");
    }

    /**
     * Test whether the given type is an integer type.
     *
     * @param type Type to test.
     * @return Whether the type is a type for integer values.
     */
    public static boolean isIntType(PlcType type) {
        return INTEGER_TYPES_ALL.contains(type);
    }

    /**
     * Test whether the given type is a real type.
     *
     * @param type Type to test.
     * @return Whether the type is a type for real values.
     */
    public static boolean isRealType(PlcType type) {
        return REAL_TYPES_ALL.contains(type);
    }
}
