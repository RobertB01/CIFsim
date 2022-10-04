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

import org.eclipse.escet.cif.cif2plc.options.PlcOutputType;

/**
 * PLC being targeted in code generation.
 *
 * <p>
 * This is pretty much a copy of {@link PlcOutputType}, but Java doesn't allow adding new values to an enumeration by
 * extension.
 * </p>
 */
public enum PlcTargetType {
    /** PLCopen XML output. */
    PLC_OPEN_XML("PLCopen XML"),

    /** IEC 61131-3 output. */
    IEC_61131_3("IEC 61131-3"),

    /** TwinCAT output. */
    TWINCAT("TwinCAT"),

    /** S7-1500 output. */
    S7_1500("S7-1500"),

    /** S7-1200 output. */
    S7_1200("S7-1200"),

    /** S7-400 output. */
    S7_400("S7-400"),

    /** S7-300 output. */
    S7_300("S7-300"),

    /** ABB output. */
    ABB("ABB");

    /** The text to use in dialogs. */
    public final String dialogText;

    /**
     * Constructor for the {@link PlcTargetType} enum.
     *
     * @param dialogText The text to use in dialogs.
     */
    private PlcTargetType(String dialogText) {
        this.dialogText = dialogText;
    }
}
