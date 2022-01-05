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

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** PLC task name option. */
public class PlcTaskNameOption extends StringOption {
    /** Constructor for the {@link PlcTaskNameOption} class. */
    public PlcTaskNameOption() {
        super("PLC task name", "TASKNAME is the name to use for the PLC task to generate. [DEFAULT=\"PlcTask\"]", 'n',
                "task-name", "TASKNAME", "PlcTask", false, true, "The name to use for the PLC task to generate.",
                "Task name:");
    }

    /**
     * Returns the PLC task name.
     *
     * @return The PLC task name.
     */
    public static String getTaskName() {
        return Options.get(PlcTaskNameOption.class);
    }
}
