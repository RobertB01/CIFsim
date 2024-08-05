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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.stream.Collectors;

/** PLC elementary type. */
public class PlcElementaryType extends PlcType {
    /** PLC BOOL type. */
    public static final PlcElementaryType BOOL_TYPE = new PlcElementaryType("BOOL", 1);

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

    /** PLC BYTE type. */
    public static final PlcElementaryType BYTE_TYPE = new PlcElementaryType("BYTE", 8);

    /** PLC WORD type. */
    public static final PlcElementaryType WORD_TYPE = new PlcElementaryType("WORD", 16);

    /** PLC DWORD type. */
    public static final PlcElementaryType DWORD_TYPE = new PlcElementaryType("DWORD", 32);

    /** PLC LWORD type. */
    public static final PlcElementaryType LWORD_TYPE = new PlcElementaryType("LWORD", 64);

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

    /** Elementary types that are considered 'bit string' for 32 bit systems, ordered by increasing size. */
    public static final List<PlcElementaryType> BIT_STRING_TYPES_32 =
            List.of(BOOL_TYPE, BYTE_TYPE, WORD_TYPE, DWORD_TYPE);

    /** Elementary types that are considered 'bit string' for 64 bit systems, ordered by increasing size. */
    public static final List<PlcElementaryType> BIT_STRING_TYPES_64 =
            List.of(BOOL_TYPE, BYTE_TYPE, WORD_TYPE, DWORD_TYPE, LWORD_TYPE);

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
     * Retrieve the smallest numeric type large enough to store the given number of bits as value of an elementary type.
     *
     * @param neededSize Needed number of bits storage for a value.
     * @param availableTypes Types to choose from.
     * @return The smallest large enough type that has sufficient storage size.
     * @throws AssertionError If the request cannot be met.
     */
    public static PlcElementaryType getTypeByRequiredSize(int neededSize, List<PlcElementaryType> availableTypes) {
        int bestType = availableTypes.size() - 1;
        while (bestType > 0 && availableTypes.get(bestType - 1).bitSize >= neededSize) {
            bestType--;
        }
        PlcElementaryType result = availableTypes.get(bestType);

        if (result.bitSize < neededSize) {
            // The request was not feasible.
            String typesText = availableTypes.stream().map(t -> t.name).collect(Collectors.joining(", "));
            String msg = fmt("Requested size of %d bits cannot be resolved with the available %s types.",
                    neededSize, typesText);
            throw new AssertionError(msg);
        }
        return result;
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
