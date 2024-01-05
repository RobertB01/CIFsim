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

package org.eclipse.escet.cif.plcgen.options;

/** The number of bits maximally available for numbers in the PLC. */
public enum PlcNumberBits {
    /** Let the generator decide. */
    AUTO("Automatic", "auto", PlcNumberBits.GENERATOR_SIZE),

    /** 32-bit. */
    BITS_32("32-bit", "32", 32),

    /** 64-bit. */
    BITS_64("64-bit", "64", 64);

    /** The text to use in dialogs. */
    public final String dialogText;

    /** The command line value text. */
    public final String cmdValueTxt;

    /** Value expressing that the generator should decide the size. */
    private static final int GENERATOR_SIZE = -1;

    /** Size in number of bits expressed by the literal, or {@link #GENERATOR_SIZE}. */
    private final int size;

    /**
     * Constructor for the {@link PlcNumberBits} enum.
     *
     * @param dialogText The text to use in dialogs.
     * @param cmdValueTxt The command line value text.
     * @param size Size in number of bits expressed by the literal or {@link #GENERATOR_SIZE}.
     */
    private PlcNumberBits(String dialogText, String cmdValueTxt, int size) {
        this.dialogText = dialogText;
        this.cmdValueTxt = cmdValueTxt;
        this.size = size;
    }

    /**
     * Retrieve the expressed size in bits by the enumeration value.
     *
     * @param generatorSize The size in bits to use in case the generator should decide.
     * @return The size in bits expressed by the enumeration value.
     */
    public int getTypeSize(int generatorSize) {
        return (size == GENERATOR_SIZE) ? generatorSize : size;
    }
}
