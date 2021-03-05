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

package org.eclipse.escet.cif.cif2plc.options;

/** PLC code output type. */
public enum PlcOutputType {
    /** PLCopen XML output. */
    PLC_OPEN_XML,

    /** IEC 61131-3 output. */
    IEC_61131_3,

    /** TwinCAT output. */
    TWINCAT;
}
