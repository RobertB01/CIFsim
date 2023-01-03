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

package org.eclipse.escet.cif.cif2plc.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** PLC project name option. */
public class PlcProjectNameOption extends StringOption {
    /** Constructor for the {@link PlcProjectNameOption} class. */
    public PlcProjectNameOption() {
        super("PLC project name", "PRJNAME is the name to use for the PLC project to generate. [DEFAULT=\"Untitled1\"]",
                'j', "proj-name", "PRJNAME", "Untitled1", false, true,
                "The name to use for the PLC project to generate.", "Project name:");
    }

    /**
     * Returns the PLC project name.
     *
     * @return The PLC project name.
     */
    public static String getProjName() {
        return Options.get(PlcProjectNameOption.class);
    }
}
