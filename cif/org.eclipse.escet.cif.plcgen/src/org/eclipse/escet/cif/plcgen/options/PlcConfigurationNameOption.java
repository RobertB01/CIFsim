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

package org.eclipse.escet.cif.plcgen.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** PLC configuration name option. */
public class PlcConfigurationNameOption extends StringOption {
    /** Constructor for the {@link PlcConfigurationNameOption} class. */
    public PlcConfigurationNameOption() {
        super("PLC configuration name",
                "CFGNAME is the name to use for the PLC configuration to generate. [DEFAULT=\"Untitled1\"]", 'c',
                "config-name", "CFGNAME", "Untitled1", false, true,
                "The name to use for the PLC configuration to generate.", "Configuration name:");
    }

    /**
     * Returns the PLC configuration name.
     *
     * @return The PLC configuration name.
     */
    public static String getCfgName() {
        return Options.get(PlcConfigurationNameOption.class);
    }
}
