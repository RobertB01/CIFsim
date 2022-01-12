//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.PLC_OPEN_XML;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_1200;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_1500;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_300;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_400;

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** PLC code output type option. */
public class PlcOutputTypeOption extends EnumOption<PlcOutputType> {
    /** Constructor for the {@link PlcOutputTypeOption} class. */
    public PlcOutputTypeOption() {
        super("PLC code output type",
                "Specifies the PLC code output type. Specify \"plc-open-xml\" for PLCopen XML output (default), "
                        + "\"iec-61131-3\" for IEC 61131-3 output, \"twincat\" for \"TwinCAT\" output, or \"s7-1200\" "
                        + "for \"Siemens S7-1200\" output.",
                't', "output-type", "OTYPE", PLC_OPEN_XML, true, "Specifies the PLC code output type.");
    }

    @Override
    protected String getDialogText(PlcOutputType type) {
        return type.dialogText;
    }

    /**
     * Returns the PLC code output type.
     *
     * @return The PLC code output type.
     */
    public static PlcOutputType getPlcOutputType() {
        return Options.get(PlcOutputTypeOption.class);
    }

    /**
     * Is the PLC code output an S7 variant (S7-1200, S7-1500, S7-300 or S7-400)?
     *
     * @return {@code true} if the output is an S7 variant, {@code false} otherwise.
     */
    public static boolean isS7Output() {
        PlcOutputType type = getPlcOutputType();
        return type == S7_1200 || type == S7_1500 || type == S7_300 || type == S7_400;
    }
}
