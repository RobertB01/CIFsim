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

/** PLC value. */
public class PlcValue extends PlcObject {
    /** The 'simple' value in IEC 61131-3 Structured Text syntax. */
    public final String value;

    /**
     * Constructor for the {@link PlcValue} class.
     *
     * @param value The 'simple' value in IEC 61131-3 Structured Text syntax.
     */
    public PlcValue(String value) {
        this.value = value;
    }
}
