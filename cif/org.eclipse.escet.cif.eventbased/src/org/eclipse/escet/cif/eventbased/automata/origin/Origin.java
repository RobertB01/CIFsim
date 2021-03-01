//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.automata.origin;

/** Class implementing origin tracking of a location. */
public abstract class Origin {
    /** Allow CIF location. */
    public static final int ALLOW_CIF_LOCATION = 1;

    /** Allow partition. */
    public static final int ALLOW_PARTITION = 2;

    /** Allow state. */
    public static final int ALLOW_STATE = 4;

    /** Add prefix text. */
    public static final int ADD_PREFIX = 8;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Currently, only
        // - A single CIF location,
        // - A partition of CIF locations, or
        // - A state of CIF locations
        // are allowed, with prefixes "location ", "partition ", respectively
        // "state ".
        // The bits given in the 2nd parameter control construction of the
        // prefix, and check that only one of the above list is printed.
        toString(sb, ALLOW_CIF_LOCATION | ALLOW_PARTITION | ALLOW_STATE | ADD_PREFIX);
        return sb.toString();
    }

    /**
     * Create a human-readable representation of a location origin.
     *
     * @param sb Builder for storing string parts while constructing the name.
     * @param flags Flags of the location name.
     * @see Origin
     */
    public abstract void toString(StringBuilder sb, int flags);
}
