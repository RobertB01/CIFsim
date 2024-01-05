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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Test mode option. */
public class TestModeOption extends BooleanOption {
    /** Constructor for the {@link TestModeOption} class. */
    public TestModeOption() {
        super("Test mode",
                "Whether to enable test mode (BOOL=yes), or not (BOOL=no). Test modes disables visual interfaces for "
                        + "all output components (incl. visualizers), and causes all real-time delays to be skipped. "
                        + "[DEFAULT=no]",
                null, "test-mode", "BOOL", false, true,
                "Test modes disables visual interfaces for all output components (incl. visualizers), and causes all "
                        + "real-time delays to be skipped.",
                "Enable test mode");
    }

    /**
     * Is test mode enabled?
     *
     * @return {@code true} if test mode is enabled, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(TestModeOption.class);
    }
}
