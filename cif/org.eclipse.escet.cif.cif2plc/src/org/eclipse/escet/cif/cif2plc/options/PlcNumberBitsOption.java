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

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/** PLC number bits option. */
public class PlcNumberBitsOption extends EnumOption<PlcNumberBits> {
    /** Constructor for the {@link PlcNumberBitsOption} class. */
    public PlcNumberBitsOption() {
        super("PLC number bits",
                "BITS is the maximum number of bits supported by the PLC for representing numeric values. Specify "
                        + "\"auto\" to automatically determine the maximum number of bits, \"32\" for 32-bits, or "
                        + "\"64\" for 64-bits. [DEFAULT=auto]",
                'b', "number-bits", "BITS", PlcNumberBits.AUTO, true,
                "The maximum number of bits supported by the PLC for representing numeric values.");
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
     * Returns the maximum number of bits supported by the PLC for representing numeric values.
     *
     * @return The maximum number of bits supported by the PLC for representing numeric values.
     */
    public static PlcNumberBits getNumberBits() {
        return Options.get(PlcNumberBitsOption.class);
    }
}
