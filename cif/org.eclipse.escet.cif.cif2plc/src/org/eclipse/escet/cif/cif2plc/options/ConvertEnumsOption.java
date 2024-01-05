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

/** Convert enumerations option. */
public class ConvertEnumsOption extends EnumOption<ConvertEnums> {
    /** Constructor for the {@link ConvertEnumsOption} class. */
    public ConvertEnumsOption() {
        super("Convert enumerations", "Specify whether enumerations should be converted. Specify \"no\" to preserve "
                + "enumerations, \"ints\" for conversion to integers, or \"consts\" for conversion to constants. "
                + "[DEFAULT=no]", null, "convert-enums", "CONVERT", ConvertEnums.NO, true,
                "Should enumerations be converted?");
    }

    @Override
    protected String getDialogText(ConvertEnums value) {
        switch (value) {
            case NO:
                return "Keep enumerations";
            case INTS:
                return "Convert enumerations to integers";
            case CONSTS:
                return "Convert enumerations to constants";
            default:
                throw new RuntimeException("Unknown value: " + value);
        }
    }

    /**
     * Should enumerations be converted?
     *
     * @return Value indicating whether enumerations are preserved, converted to integers, or converted to constants.
     */
    public static ConvertEnums getValue() {
        return Options.get(ConvertEnumsOption.class);
    }
}
