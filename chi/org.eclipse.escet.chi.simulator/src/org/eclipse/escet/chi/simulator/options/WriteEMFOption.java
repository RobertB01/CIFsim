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

/** Option class to allow writing the generated EMF model after type checking. */
public class WriteEMFOption extends BooleanOption {
    /** Constructor for the {@link WriteEMFOption} class. */
    public WriteEMFOption() {
        super("Write EMF model", // name
                "Whether or not to write the generated EMF model after type checking (yes/no) [default=no]", // desc
                null, // cmd short
                "emf", // cmd long
                "BOOL", // cmd value
                false, // default value
                true, // show in dialog
                "Output the type-decorated model to a file (in EMF format).", // opt dialog descr
                "Write the generated model after type checking."); // checkbox text
    }

    /**
     * Get the option value.
     *
     * @return The option value as stated by the user.
     */
    public static boolean getWriteEMF() {
        return Options.get(WriteEMFOption.class);
    }
}
