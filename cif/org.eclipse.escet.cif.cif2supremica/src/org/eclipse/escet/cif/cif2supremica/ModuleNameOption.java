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

package org.eclipse.escet.cif.cif2supremica;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Supremica module name option. */
public class ModuleNameOption extends StringOption {
    /** Constructor for the {@link ModuleNameOption} class. */
    public ModuleNameOption() {
        super("Supremica module name",
                "Specifies the name of the Supremica module. The default is the name of the output file, "
                        + "after removal of the \".wmod\" extension (if present).",
                'n', "name", "NAME", null, true, true,
                "The name of the Supremica module. If empty, defaults to the name of the output file, "
                        + "after removal of the \".wmod\" extension (if present).",
                "Module name:");
    }

    /**
     * Returns the name of the Supremica module. May be {@code null} to indicate the default name should be used.
     *
     * @return The name of the Supremica module, or {@code null}.
     */
    public static String getModuleName() {
        return Options.get(ModuleNameOption.class);
    }
}
