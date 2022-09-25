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

package org.eclipse.escet.cif.cif2plc.plcdata;

/** Base class for PLC objects. */
public abstract class PlcObject {
    @Override
    public String toString() {
        // Make sure that OutputWriter.toBox() isn't silently skipped.
        throw new AssertionError("PLC objects are not printable.");
    }
}
