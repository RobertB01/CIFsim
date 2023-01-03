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

package org.eclipse.escet.common.app.framework.options;

/**
 * Developer mode option. Controls whether exceptional situations return limited and user friendly information
 * ({@code false}), or extended developer oriented information ({@code true}).
 */
public class DevModeOption extends BooleanOption {
    /**
     * Constructor for the {@link DevModeOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public DevModeOption() {
        super("Developer mode",
                "Whether exceptional situations return limited and user friendly information (BOOL=no), "
                        + "or extended developer oriented information (BOOL=yes). [DEFAULT=no]",
                null, "devmode", "BOOL", false, true, "Developer mode.", "Enable developer mode");
    }

    /**
     * Returns a value indicating whether the option is enabled.
     *
     * @return A value indicating whether the option is enabled.
     */
    public static boolean isEnabled() {
        return Options.get(DevModeOption.class);
    }
}
