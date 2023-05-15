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

import org.eclipse.escet.cif.plcgen.writers.AbbWriter;
import org.eclipse.escet.cif.plcgen.writers.OutputTypeWriter;

/** Code generator for the ABB PLC type. */
public class AbbTarget extends PlcBaseTarget {
    /** Constructor of the {@link AbbTarget} class. */
    public AbbTarget() {
        // TODO Verify settings of the ABB target.
        super(PlcTargetType.ABB);
    }

    @Override
    public OutputTypeWriter getPlcCodeWriter() {
        return new AbbWriter();
    }

    @Override
    public boolean supportsArrays() {
        return true;
    }

    @Override
    public boolean supportsConstants() {
        return false;
    }

    @Override
    public boolean supportsEnumerations() {
        return false;
    }

    @Override
    public int getMaxIntegerTypeSize() {
        return 32;
    }

    @Override
    public int getMaxRealTypeSize() {
        return 32;
    }

    @Override
    public String getPathSuffixReplacement() {
        return "_abb";
    }
}
