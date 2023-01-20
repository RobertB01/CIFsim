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

/** Extracts information form the CIF input file, and passes it on to other code generators. */
public class CifProcessor {
    /**
     * Perform the transformation.
     *
     * @param settings Configuration of the application.
     * @return Whether the transformation succeeded, termination request is not successful.
     */
    public boolean transform(PlcGenSettings settings) {
        // Read CIF specification.
        new CifReader().init(settings.inputPath, settings.absInputPath, false).read(); // Currently not used.
        return !settings.shouldTerminate.get();
    }
}
