//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;

/** Stores generated PLC code. */
public class PlcCodeStorage {
    /** PLC target to generate code for. */
    private PlcBaseTarget target;

    /**
     * Constructor of the {@link PlcCodeStorage} class.
     *
     * @param target PLC target to generate code for.
     */
    public PlcCodeStorage(PlcBaseTarget target) {
        this.target = target;
    }
}
