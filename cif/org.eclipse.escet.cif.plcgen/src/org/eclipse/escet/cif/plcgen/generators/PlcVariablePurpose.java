//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators;

/** Purpose of PLC variables in the code generator. */
public enum PlcVariablePurpose {
    /** Variable is used as a constant value. */
    CONSTANT,

    /** Variable is used for reading from external input. */
    INPUT_VAR,

    /** Variable is local to a POU. */
    LOCAL_VAR,

    /** Variable is used for writing to external output. */
    OUTPUT_VAR,

    /** Variable is used to store system state. */
    STATE_VAR,

    /** Variable is used for timing. */
    TIMER_VAR;
}
