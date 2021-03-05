//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

/** Option class to allow writing the compiled Java classes to a JAR file with a .cchi (compiled Chi) extension. */
public class WriteJAROption extends BooleanOption {
    /** Constructor for the {@link WriteJAROption} class. */
    public WriteJAROption() {
        super("Write CChi file", // name
                "Whether or not to write the compiled Java classes (yes/no) [default=no]", // desc
                null, // cmd short
                "jar", // cmd long
                "BOOL", // cmd value
                false, // default value
                true, // show in dialog
                "Enabling this option ends the application after writing the compiled Java code.", // opt dialog descr
                "Write the compiled Java code to a .cchi file."); // checkbox text
    }

    /**
     * Get the option value.
     *
     * @return The option value as stated by the user.
     */
    public static boolean getWriteJAR() {
        return Options.get(WriteJAROption.class);
    }
}
