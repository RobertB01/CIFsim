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

/** Option to only compile the Java code, but not do the actual simulation. */
public class CompileOnlyOption extends BooleanOption {
    /** Constructor for the {@link CompileOnlyOption} class. */
    public CompileOnlyOption() {
        super(
                // name
                "Compile only",

                // description
                "Whether to only compile the model and save the compiled code to a file, to allow repeated "
                        + "simulation later on (BOOL=yes), or also perform actual simulation (BOOL=no). [DEFAULT=no]",

                // cmdShort
                null,

                // cmdLong
                "compile-only",

                // cmdValue
                "BOOL",

                // defaultValue
                false,

                // showInDialog
                true,

                // optDialogDescr
                "If enabled, only compiles the model and saves the compiled code to a file, to allow repeated "
                        + "simulation later on. If disabled, also performs actual simulation.",

                // optDialogCheckboxText
                "Compile only");
    }

    /**
     * Should the model only be compiled, or should it also be simulated?
     *
     * @return {@code true} if the model should only be compiled, {@code false} if it should also be simulated.
     */
    public static boolean isEnabled() {
        return Options.get(CompileOnlyOption.class);
    }
}
