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

package org.eclipse.escet.cif.plcgen.targets;

import org.eclipse.escet.cif.plcgen.writers.Iec611313Writer;
import org.eclipse.escet.cif.plcgen.writers.OutputTypeWriter;

/** Code generator for the IEC 61131-3 PLC type. */
public class Iec611313Target extends PlcBaseTarget {
    /** Constructor of the {@link Iec611313Target} class. */
    public Iec611313Target() {
        super(PlcTargetType.IEC_61131_3);
    }

    @Override
    public OutputTypeWriter getPlcCodeWriter() {
        return new Iec611313Writer();
    }

    @Override
    public boolean supportsArrays() {
        return true;
    }

    @Override
    public boolean supportsConstants() {
        return true;
    }

    @Override
    public boolean supportsEnumerations() {
        return true;
    }

    @Override
    public int getMaxIntegerTypeSize() {
        return 64;
    }

    @Override
    public int getMaxRealTypeSize() {
        return 64;
    }

    @Override
    public String getPathSuffixReplacement() {
        return "_plc";
    }
}
