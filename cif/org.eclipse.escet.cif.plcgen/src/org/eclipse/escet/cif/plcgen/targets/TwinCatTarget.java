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

package org.eclipse.escet.cif.plcgen.targets;

import org.eclipse.escet.cif.cif2plc.writers.TwinCatWriter;

/** Code generator for the TwinCAT PLC type. */
public class TwinCatTarget extends PlcBaseTarget {
    /** Constructor of the {@link TwinCatTarget} class. */
    public TwinCatTarget() {
        super(PlcTargetType.TWINCAT);
    }

    @Override
    public void writeOutput(String outputPath) {
        new TwinCatWriter().write(project, outputPath);
    }

    @Override
    public boolean supportsConstants() {
        return true;
    }

    @Override
    public String pathSuffixReplacement() {
        return "_twincat";
    }
}