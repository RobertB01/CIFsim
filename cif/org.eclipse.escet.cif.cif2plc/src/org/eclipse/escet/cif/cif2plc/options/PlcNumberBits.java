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

package org.eclipse.escet.cif.cif2plc.options;

/** The number of bits maximally available for numbers in the PLC. */
public enum PlcNumberBits {
    /** Let the transformator decide. */
    AUTO("Automatic", "auto"),

    /** 32-bit. */
    BITS_32("32-bit", "32"),

    /** 64-bit. */
    BITS_64("64-bit", "64");

    /** The text to use in dialogs. */
    public final String dialogText;

    /** The command line value text. */
    public final String cmdValueTxt;

    /**
     * Constructor for the {@link PlcNumberBits} enum.
     *
     * @param dialogText The text to use in dialogs.
     * @param cmdValueTxt The command line value text.
     */
    private PlcNumberBits(String dialogText, String cmdValueTxt) {
        this.dialogText = dialogText;
        this.cmdValueTxt = cmdValueTxt;
    }
}
