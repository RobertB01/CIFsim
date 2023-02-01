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

import org.eclipse.escet.cif.cif2plc.writers.OutputTypeWriter;
import org.eclipse.escet.cif.plcgen.AbbWriter;

/** Code generator for the ABB PLC type. */
public class AbbTarget extends PlcTarget {
    /** Constructor of the {@link AbbTarget} class. */
    public AbbTarget() {
        super(PlcTargetType.ABB);
    }

    @Override
    public OutputTypeWriter getPlcCodeWriter() {
        return new AbbWriter();
    }

    @Override
    public boolean supportsConstants() {
        return false;
    }

    @Override
    protected int getMaxIntegerTypeSize() {
        return 32;
    }

    @Override
    protected int getMaxFloatTypeSize() {
        return 32;
    }

    @Override
    public String getPathSuffixReplacement() {
        return "_abb";
    }
}
