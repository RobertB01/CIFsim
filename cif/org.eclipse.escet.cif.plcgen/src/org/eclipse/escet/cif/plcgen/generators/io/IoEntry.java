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

import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** An entry in the IO table for the PLC. */
public class IoEntry {
    /** IO address in the PLC. */
    public final IoAddress plcAddress;

    /** Type of the data being transferred at the I/O address. */
    public final PlcType varType;

    /** CIF object linked to the entry. */
    public final PositionObject cifObject;

    /** Kind of IO being performed. */
    public final IoKind ioKind;

    /**
     * Constructor of the {@link IoEntry} class.
     *
     * @param plcAddress IO address in the PLC.
     * @param varType Type of the data being transferred at the I/O address.
     * @param cifObject CIF object linked to the entry.
     * @param ioKind Kind of IO being performed.
     */
    public IoEntry(IoAddress plcAddress, PlcType varType, PositionObject cifObject, IoKind ioKind) {
        this.plcAddress = plcAddress;
        this.varType = varType;
        this.cifObject = cifObject;
        this.ioKind = ioKind;
    }
}
