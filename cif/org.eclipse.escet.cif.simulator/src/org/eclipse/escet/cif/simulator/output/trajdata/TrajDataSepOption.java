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

package org.eclipse.escet.cif.simulator.output.trajdata;

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Trajectory data file separation amount option. */
public class TrajDataSepOption extends IntegerOption {
    /** Constructor for the {@link TrajDataSepOption} class. */
    public TrajDataSepOption() {
        super("Trajectory data separation amount",
                "Specify the number of spaces to use to separate columns in the trajectory data file, or \"off\" "
                        + "to disable prettifying. Enable to prettify the file after simulation, making it more "
                        + "readable. [DEFAULT=3].",
                null, "trajdata-sep", "TDSEP", 3, 1, 100, 10, true,
                "Trajectory data prettifying can be used to make the trajectory data file more readable. If "
                        + "enabled, the file is prettified after the simulation is completed. The number of "
                        + "spaces to use to separate columns can be configured.",
                "Number of spaces:", true, 3, "off", "Disable prettifying", "Enable prettifying");
    }

    /**
     * Returns the number of spaces to use to separate columns in the trajectory data file, or {@code null} to disable
     * prettifying.
     *
     * @return The number of spaces to use to separate columns in the trajectory data file, or {@code null}.
     */
    public static Integer getSep() {
        return Options.get(TrajDataSepOption.class);
    }
}
