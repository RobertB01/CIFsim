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

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** PLC task priority option. */
public class PlcTaskPriorityOption extends IntegerOption {
    /** Constructor for the {@link PlcTaskPriorityOption} class. */
    public PlcTaskPriorityOption() {
        super("PLC task priority",
                "TASKPRIO is the priority of the task to generate, and must be in the range [0..65535]. [DEFAULT=20]",
                'p', "task-prio", "TASKPRIO", 20, 0, 65535, 1, true, "The priority of the task to generate.",
                "Priority:");
    }

    /**
     * Returns the PLC task priority.
     *
     * @return The PLC task priority.
     */
    public static int getTaskPrio() {
        return Options.get(PlcTaskPriorityOption.class);
    }
}
