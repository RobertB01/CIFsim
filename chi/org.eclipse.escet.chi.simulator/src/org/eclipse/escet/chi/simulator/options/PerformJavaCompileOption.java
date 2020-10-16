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

package org.eclipse.escet.chi.simulator.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option class to allow skipping the Java compilation step in the compiler. */
public class PerformJavaCompileOption extends BooleanOption {
    /** Constructor for the {@link PerformJavaCompileOption} class. */
    public PerformJavaCompileOption() {
        super("Perform Java Compilation", // name
                "Whether or not to perform compilation of the generated Java code (yes/no). [default=yes]", // desc
                null, // cmd short
                "java-compile", // cmd long
                "BOOL", // cmd value
                true, // default value
                true, // show in dialog
                "Disabling this option aborts simulation after type checking.", // opt dialog descr
                "Perform compilation of the generated Java code."); // checkbox text
    }

    /**
     * Get the option value.
     *
     * @return The option value as stated by the user.
     */
    public static boolean getPerformCompilation() {
        return Options.get(PerformJavaCompileOption.class);
    }
}
