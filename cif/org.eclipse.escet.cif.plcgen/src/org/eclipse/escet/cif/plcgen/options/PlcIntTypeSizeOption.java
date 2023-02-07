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

package org.eclipse.escet.cif.plcgen.options;

import org.eclipse.escet.cif.cif2plc.options.PlcNumberBits;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option for defining the integer type size in the PLC. */
public class PlcIntTypeSizeOption extends EnumOption<PlcNumberBits> {
    /** Constructor for the {@link PlcIntTypeSizeOption} class. */
    public PlcIntTypeSizeOption() {
        super("PLC integer type size",
                "BITS is the number of bits for an integer value in the PLC. Specify "
                        + "\"auto\" to automatically determine the size, \"32\" for 32-bits integer values, or "
                        + "\"64\" for 64-bits integer values. [DEFAULT=auto]",
                null, "int-size", "BITS", PlcNumberBits.AUTO, true,
                "The number of bits of an integer value in PLC.");
    }

    @Override
    protected String getDialogText(PlcNumberBits value) {
        return value.dialogText;
    }

    @Override
    public PlcNumberBits parseValue(String optName, String value) {
        for (PlcNumberBits option: PlcNumberBits.values()) {
            if (value.equals(option.cmdValueTxt)) {
                return option;
            }
        }
        throw new InvalidOptionException("Unknown option value: " + value);
    }

    @Override
    public String[] getCmdLine(Object value) {
        String valueTxt = ((PlcNumberBits)value).cmdValueTxt;
        return new String[] {"--" + cmdLong + "=" + valueTxt};
    }

    /**
     * Returns the number of bits in an integer value in the PLC.
     *
     * @return The number of bits in an integer value in the PLC.
     */
    public static PlcNumberBits getNumberBits() {
        return Options.get(PlcIntTypeSizeOption.class);
    }
}
