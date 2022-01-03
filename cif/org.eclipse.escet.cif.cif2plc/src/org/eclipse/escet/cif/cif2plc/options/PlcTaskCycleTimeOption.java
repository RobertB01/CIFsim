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

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** PLC task cycle time option. */
public class PlcTaskCycleTimeOption extends IntegerOption {
    /** Constructor for the {@link PlcTaskCycleTimeOption} class. */
    public PlcTaskCycleTimeOption() {
        super("PLC task cycle time",
                "TASKTIME is the cycle time in milliseconds for the task to generate. Specifying a cycle time enables "
                        + "periodic task scheduling. The cycle time must be positive. Use \"off\" to disable periodic "
                        + "task scheduling. [DEFAULT=10]",
                'i', "task-time", "TASKTIME", 10, 1, Integer.MAX_VALUE, 1, true,
                "The cycle time in milliseconds for the task to generate.", "Cycle time:", true, 10, "off",
                "Disable periodic task scheduling", "Enable period task scheduling");
    }

    /**
     * Returns the PLC task cycle time (>= 1), or 0 to disable periodic task scheduling.
     *
     * @return The PLC task cycle time (>= 1), or 0 to disable periodic task scheduling.
     */
    public static int getTaskCycleTime() {
        Integer rslt = Options.get(PlcTaskCycleTimeOption.class);
        return (rslt == null) ? 0 : rslt;
    }
}
