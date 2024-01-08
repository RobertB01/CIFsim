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

package org.eclipse.escet.cif.multilevel.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Output directory option for the partial specifications. */
public class PartialSpecsOutputDirectoryOption extends StringOption {
    /**
     * Constructor for the {@link PartialSpecsOutputDirectoryOption} class. Don't directly create instances of this
     * class. Use the {@link Options#getInstance} method instead.
     */
    public PartialSpecsOutputDirectoryOption() {
        super("Partial specifications output directory",
                "The path to the partial specifications output directory. Partial specifications are not written if "
                        + "this option is left empty. Is empty by default.",
                null, "specs-dir", "PATH", null, true, true,
                "The path to the partial specifications output directory. Partial specifications are not written if "
                        + "this option is left empty.",
                "Directory path:");
    }

    /**
     * Returns the path of the partial specifications output directory, or {@code null} to not output the partial
     * specifications to a directory.
     *
     * @return The path of the partial specifications output directory, or {@code null}.
     */
    public static String getPath() {
        return Options.get(PartialSpecsOutputDirectoryOption.class);
    }
}
