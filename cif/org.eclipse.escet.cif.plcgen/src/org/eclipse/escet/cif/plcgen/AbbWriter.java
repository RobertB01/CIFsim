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

package org.eclipse.escet.cif.plcgen;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;
import org.eclipse.escet.cif.cif2plc.writers.OutputTypeWriter;

/** Writer for generated PLC code for the ABB PLC target type. */
public class AbbWriter extends OutputTypeWriter {

    @Override
    public void write(PlcProject project, String outputPath) {
        ensureDirectory(outputPath);
    }
}
