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

package org.eclipse.escet.cif.cif2plc.options;

/** PLC code output type. */
public enum PlcOutputType {
    /** PLCopen XML output. */
    PLC_OPEN_XML,

    /** IEC 61131-3 output. */
    IEC_61131_3,

    /** TwinCAT output. */
    TWINCAT,

    /** Siemens S7-1200 output. */
    S7_1200,

    /** Siemens S7-1500 output. */
    S7_1500,

    /** Siemens S7-300 output. */
    S7_300,

    /** Siemens S7-400 output. */
    S7_400,
}
