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

/** DMM output file option. */
public class DmmOutputFileOption extends StringOption {
    /**
     * Constructor for the {@link DmmOutputFileOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public DmmOutputFileOption() {
        super("DMM output file",
                "The path to the DMM output file. DMM output file is not written if this option is left empty. "
                        + "Is empty by default.",
                null, "dmm-file", "PATH", null, true, true,
                "The path to the DMM output file. DMM output file is not written if this option is left empty.",
                "File path:");
    }

    /**
     * Returns the path of the DMM output file, or {@code null} to not output the DMM to a file.
     *
     * @return The path of the DMM output file, or {@code null}.
     */
    public static String getDmmOutputFilePath() {
        return Options.get(DmmOutputFileOption.class);
    }
}
