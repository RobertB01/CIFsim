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

import org.eclipse.escet.cif.plcgen.AbbWriter;

/** Code generator for the ABB PLC type. */
public class AbbTarget extends PlcBaseTarget {
    /** Constructor of the {@link AbbTarget} class. */
    public AbbTarget() {
        super(PlcTargetType.ABB);

        setOutSuffixReplacement("_abb");
        setSupportconstants(false);
    }

    @Override
    public void writeOutput(String outputPath) {
        new AbbWriter().write(project, outputPath);
    }
}
