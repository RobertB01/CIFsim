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

import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.writers.Writer;
import org.eclipse.escet.cif.plcgen.writers.TwinCatWriter;

/** Code generator for the TwinCAT PLC type. */
public class TwinCatTarget extends PlcBaseTarget {
    /** Constructor of the {@link TwinCatTarget} class. */
    public TwinCatTarget() {
        super(PlcTargetType.TWINCAT);
    }

    @Override
    public Writer getPlcCodeWriter() {
        return new TwinCatWriter(this);
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
    public boolean supportsInfixNotation(PlcFuncOperation funcOper) {
        // The 'a ** b' syntax seemed not to work in TwinCAT 3.1. Use the named function instead.
        return funcOper != PlcFuncOperation.POWER_OP;
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
        return "_twincat";
    }
}
