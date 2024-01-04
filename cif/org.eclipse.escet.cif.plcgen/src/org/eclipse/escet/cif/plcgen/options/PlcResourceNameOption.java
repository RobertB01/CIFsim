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

/** PLC resource name option. */
public class PlcResourceNameOption extends StringOption {
    /** Constructor for the {@link PlcResourceNameOption} class. */
    public PlcResourceNameOption() {
        super("PLC resource name",
                "RESNAME is the name to use for the PLC resource to generate. [DEFAULT=\"Untitled1\"]", 'r', "res-name",
                "RESNAME", "Untitled1", false, true, "The name to use for the PLC resource to generate.",
                "Resource name:");
    }

    /**
     * Returns the PLC resource name.
     *
     * @return The PLC resource name.
     */
    public static String getResName() {
        return Options.get(PlcResourceNameOption.class);
    }
}
