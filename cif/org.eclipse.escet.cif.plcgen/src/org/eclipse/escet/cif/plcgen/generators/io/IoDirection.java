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

/** Available directions of I/O with respect to the PLC. */
public enum IoDirection {
    /** IO address is read to obtain its current value. */
    IO_READ,

    /** IO address is written to change it to the desired value. */
    IO_WRITE;
}
