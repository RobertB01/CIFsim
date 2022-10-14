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

package org.eclipse.escet.cif.plcgen;

import java.util.Locale;

import org.eclipse.escet.cif.plcgen.targets.PlcTargetType;
import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option class for getting the PLC target type to generate code for. */
public class PlcTargetTypeOption extends EnumOption<PlcTargetType> {
    /** Default PLC target. */
    public static final PlcTargetType DEFAULT_TARGET = PlcTargetType.PLC_OPEN_XML;

    /** Constructor for the {@link PlcTargetTypeOption} class. */
    public PlcTargetTypeOption() {
        super("PLC code target type", makeDescription(), 't', "target-type", "TARGET", DEFAULT_TARGET, true,
                "Specifies the PLC code target type.");
    }

    @Override
    protected String getDialogText(PlcTargetType type) {
        return type.dialogText;
    }

    /**
     * Construct the descriptive string of the option.
     *
     * @return The descriptive string.
     */
    private static String makeDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Specifies the PLC code target type. Specify ");
        int last = PlcTargetType.values().length - 1;
        for (int i = 0; i <= last; i++) {
            if (i > 0) {
                sb.append((last > 1) ? ", " : " ");
                if (i == last) {
                    sb.append("or ");
                }
            }
            PlcTargetType targetType = PlcTargetType.values()[i];
            sb.append('"');
            sb.append(targetType.dialogText.toLowerCase(Locale.US).replace(' ', '-'));
            sb.append('"');
            sb.append(" for " + targetType.dialogText + " output");
            if (targetType == DEFAULT_TARGET) {
                sb.append(" (default)");
            }
        }
        sb.append(".");
        return sb.toString();
    }

    /**
     * Returns the PLC code target type.
     *
     * @return The PLC code target type.
     */
    public static PlcTargetType getPlcTargetType() {
        return Options.get(PlcTargetTypeOption.class);
    }
}
