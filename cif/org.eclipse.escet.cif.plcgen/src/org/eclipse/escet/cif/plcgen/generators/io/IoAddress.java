//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators.io;

/** Parsed IO address with its properties. */
public interface IoAddress {
    /**
     * Compute whether the address supports reading the value.
     *
     * @return Whether data can be read from the address.
     */
    public boolean supportsInput();

    /**
     * Compute whether the address supports writing the value.
     *
     * @return Whether data can be written to the address.
     */
    public boolean supportsOutput();

    /**
     * Size of the address in number of bits.
     *
     * @return Number of bits read and/or written at the address.
     */
    public int size();

    /**
     * Compose the Io address for the PLC.
     *
     * @return The Io address in a format acceptable to the PLC system..
     */
    public String getAddress();

    @Override
    public String toString();

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();
}
