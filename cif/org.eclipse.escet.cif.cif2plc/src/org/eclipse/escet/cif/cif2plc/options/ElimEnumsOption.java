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

package org.eclipse.escet.cif.cif2plc.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Eliminate enumerations option. */
public class ElimEnumsOption extends BooleanOption {
    /** Constructor for the {@link ElimEnumsOption} class. */
    public ElimEnumsOption() {
        super("Eliminate enumerations",
                "Whether the eliminate enumerations (BOOL=yes), or keep them (BOOL=no). [DEFAULT=no]", null,
                "elim-enums", "BOOL", false, true, "Should enumerations be eliminated?", "Eliminate enumerations");
    }

    /**
     * Returns a value indicating whether enumerations should be eliminated.
     *
     * @return {@code true} if enumerations should be eliminated, {@code false} otherwise.
     */
    public static boolean elimEnums() {
        return Options.get(ElimEnumsOption.class);
    }
}
