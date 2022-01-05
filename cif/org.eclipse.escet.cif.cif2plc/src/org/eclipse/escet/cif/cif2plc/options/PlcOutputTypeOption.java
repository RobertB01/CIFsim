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

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** PLC code output type option. */
public class PlcOutputTypeOption extends EnumOption<PlcOutputType> {
    /** Constructor for the {@link PlcOutputTypeOption} class. */
    public PlcOutputTypeOption() {
        super("PLC code output type",
                "Specifies the PLC code output type. Specify \"plc-open-xml\" for PLCopen XML output (default), "
                        + "\"iec-61131-3\" for IEC 61131-3 output, or \"twincat\" for \"TwinCAT\" output.",
                't', "output-type", "OTYPE", PlcOutputType.PLC_OPEN_XML, true, "Specifies the PLC code output type.");
    }

    @Override
    protected String getDialogText(PlcOutputType type) {
        switch (type) {
            case PLC_OPEN_XML:
                return "PLCopen XML";
            case IEC_61131_3:
                return "IEC 61131-3";
            case TWINCAT:
                return "TwinCAT";
            default:
                throw new RuntimeException("Unknown PLC output type: " + type);
        }
    }

    /**
     * Returns the PLC code output type.
     *
     * @return The PLC code output type.
     */
    public static PlcOutputType getPlcOutputType() {
        return Options.get(PlcOutputTypeOption.class);
    }
}
