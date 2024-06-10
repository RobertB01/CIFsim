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

package org.eclipse.escet.cif.plcgen.targets;

import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.writers.TwinCatWriter;
import org.eclipse.escet.cif.plcgen.writers.Writer;

/** Code generator for the TwinCAT PLC type. */
public class TwinCatTarget extends PlcBaseTarget {
    /** Constructor of the {@link TwinCatTarget} class. */
    public TwinCatTarget() {
        super(PlcTargetType.TWINCAT, ConvertEnums.KEEP);
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
    public boolean supportsConstant(Constant constant) {
        return commonSupportedConstants(constant);
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
