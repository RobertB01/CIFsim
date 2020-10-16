//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Trajectory data option. */
public class TrajDataOption extends BooleanOption {
    /** Constructor for the {@link TrajDataOption} class. */
    public TrajDataOption() {
        super("Trajectory data",
                "Whether to write trajectory data to a file (BOOL=yes), or not (BOOL=no). [DEFAULT=no]", null,
                "trajdata", "BOOL", false, true, "Enable this option to write trajectory data to a file.",
                "Write trajectory data");
    }

    /**
     * Should trajectory data be written to a file?
     *
     * @return {@code true} if trajectory data be written to a file, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(TrajDataOption.class);
    }
}
