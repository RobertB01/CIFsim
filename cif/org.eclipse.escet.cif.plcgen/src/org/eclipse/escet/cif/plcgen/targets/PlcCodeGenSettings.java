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

/** PLC code generation settings. */
public abstract class PlcCodeGenSettings {
    /**
     * Getter for obtaining whether the PLC target type supports named constants.
     *
     * @return The requested value.
     */
    public abstract boolean getSupportConstants();

    /**
     * Getter for obtaining whether the PLC target type supports named constants.
     *
     * @return The requested value.
     */
    public abstract String getOutSuffixReplacement();
}
