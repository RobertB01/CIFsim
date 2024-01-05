//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2plc.options;

import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.DINT_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.LINT_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.LREAL_TYPE;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType.REAL_TYPE;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcElementaryType;

/** PLC code output type. */
public enum PlcOutputType {
    /** PLCopen XML output. */
    PLC_OPEN_XML("PLCopen XML", ".plcopen.xml", LINT_TYPE, LREAL_TYPE),

    /** IEC 61131-3 output. */
    IEC_61131_3("IEC 61131-3", "_plc", LINT_TYPE, LREAL_TYPE),

    /** TwinCAT output. */
    TWINCAT("TwinCAT", "_twincat", LINT_TYPE, LREAL_TYPE),

    /** S7-1500 output. */
    S7_1500("S7-1500", "_s7_1500", LINT_TYPE, LREAL_TYPE),

    /** S7-1200 output. */
    S7_1200("S7-1200", "_s7_1200", DINT_TYPE, LREAL_TYPE),

    /** S7-400 output. */
    S7_400("S7-400", "_s7_400", DINT_TYPE, REAL_TYPE),

    /** S7-300 output. */
    S7_300("S7-300", "_s7_300", DINT_TYPE, REAL_TYPE);

    /** The text to use in dialogs. */
    public final String dialogText;

    /** The output file extension (including the dot) or directory postfix to use. */
    public final String outFileExtOrDirPostfix;

    /** The largest integer type supported by this output type. */
    public final PlcElementaryType largeIntType;

    /** The largest real type supported by this output type. */
    public final PlcElementaryType largeRealType;

    /**
     * Constructor for the {@link PlcOutputType} enum.
     *
     * @param dialogText The text to use in dialogs.
     * @param outFileExtOrDirPostfix The output file extension (including the dot) or directory postfix to use.
     * @param largeIntType The largest integer type supported by this output type.
     * @param largeRealType The largest real type supported by this output type.
     */
    private PlcOutputType(String dialogText, String outFileExtOrDirPostfix, PlcElementaryType largeIntType,
            PlcElementaryType largeRealType)
    {
        this.dialogText = dialogText;
        this.outFileExtOrDirPostfix = outFileExtOrDirPostfix;
        this.largeIntType = largeIntType;
        this.largeRealType = largeRealType;
    }
}
