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

package org.eclipse.escet.cif.plcgen.generators;

import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;

/** Extracts information from the CIF input file, to be used during PLC code generation. */
public class CifProcessor {
    /**
     * Process the input CIF specification, reading it, and extracting the relevant information for PLC code generation.
     *
     * @param settings Configuration to use.
     */
    public void process(PlcGenSettings settings) {
        // Read CIF specification.
        new CifReader().init(settings.inputPath, settings.absInputPath, false).read(); // Currently not used.
    }
}
