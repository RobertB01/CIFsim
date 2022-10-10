//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.cif2plc.writers.PlcOpenXmlWriter;

/** Code generator for the PLCopen XML PLC type. */
public class PlcOpenXmlTarget extends PlcBaseTarget {
    /** Constructor of the {@link PlcOpenXmlTarget} class. */
    public PlcOpenXmlTarget() {
        super(PlcTargetType.PLC_OPEN_XML);
    }

    @Override
    public void writeOutput(String outputPath) {
        new PlcOpenXmlWriter().write(project, outputPath);
    }

    @Override
    public boolean getSupportConstants() {
        return true;
    }

    @Override
    public String getOutSuffixReplacement() {
        return ".plcopen.xml";
    }
}
