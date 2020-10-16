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

package org.eclipse.escet.cif.cif2mcrl2.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/**
 * Option to enable generation of a dump of the instance tree for debugging (is implied by giving an output filename for
 * it).
 */
public class EnableDebugOutputOption extends BooleanOption {
    /** Constructor for the {@link EnableDebugOutputOption} class. */
    public EnableDebugOutputOption() {
        super("Enable debug output", // name
                "Whether to generate a file containing the debug output of the instantiation tree (yes/no). "
                        + "[DEFAULT=no]", // desc
                null, // cmd short
                "enable-debug", // cmd long
                "BOOL", // cmd value
                false, // default value
                true, // show in dialog
                "Giving a filename for the debug output implies this option.", // opt dialog descr
                "Enable generation of a debug output file."); // checkbox text
    }

    /**
     * Get the option value.
     *
     * @return The option value as stated by the user.
     */
    public static boolean getEnableDebugOutput() {
        boolean enable = Options.get(EnableDebugOutputOption.class);
        enable |= (DebugFileOption.getPath() != null);
        return enable;
    }
}
