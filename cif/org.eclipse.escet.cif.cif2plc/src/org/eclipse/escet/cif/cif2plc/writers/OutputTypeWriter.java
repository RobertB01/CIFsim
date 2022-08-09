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

package org.eclipse.escet.cif.cif2plc.writers;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcProject;

/** Base class for writing PLC code for a given output type. */
public abstract class OutputTypeWriter {
    /**
     * Convert the project contents to output acceptable for a PLC output type.
     *
     * @param project PLC program code to convert.
     * @param outputPath The absolute local file system destination to write the converted output.
     */
    public abstract void write(PlcProject project, String outputPath);
}
