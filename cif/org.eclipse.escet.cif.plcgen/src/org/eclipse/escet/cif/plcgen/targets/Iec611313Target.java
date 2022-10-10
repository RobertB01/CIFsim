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

import org.eclipse.escet.cif.cif2plc.writers.Iec611313Writer;

/** Code generator for the IEC 61131-3 PLC type. */
public class Iec611313Target extends PlcBaseTarget {
    /** Constructor of the {@link Iec611313Target} class. */
    public Iec611313Target() {
        super(PlcTargetType.IEC_61131_3);
    }

    @Override
    public void writeOutput(String outputPath) {
        new Iec611313Writer().write(project, outputPath);
    }

    @Override
    public boolean getSupportConstants() {
        return true;
    }

    @Override
    public String getOutSuffixReplacement() {
        return "_plc";
    }
}
