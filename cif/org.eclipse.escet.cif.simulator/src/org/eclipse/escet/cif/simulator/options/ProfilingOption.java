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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Profiling option. */
public class ProfilingOption extends BooleanOption {
    /** Constructor for the {@link ProfilingOption} class. */
    public ProfilingOption() {
        super("Profiling",
                "Whether to perform profiling (BOOL=yes), or not (BOOL=no). Profiling provides information about "
                        + "the number of transitions taken, per second. Profiling disables most types of console "
                        + "output and debugging options. [DEFAULT=no]",
                null, "profiling", "BOOL", false, true,
                "Profiling provides information about the number of transitions taken, per second. Profiling "
                        + "disables most types of console output and debugging options.",
                "Enable profiling");
    }

    /**
     * Returns a value indicating whether profiling is enabled.
     *
     * @return {@code true} if profiling is enabled, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(ProfilingOption.class);
    }
}
