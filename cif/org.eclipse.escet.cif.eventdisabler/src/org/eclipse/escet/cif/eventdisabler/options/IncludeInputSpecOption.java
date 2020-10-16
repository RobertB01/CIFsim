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

package org.eclipse.escet.cif.eventdisabler.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Include input specification option. */
public class IncludeInputSpecOption extends BooleanOption {
    /** Constructor for the {@link IncludeInputSpecOption} class. */
    public IncludeInputSpecOption() {
        super("Include input specification",
                "Whether the output file should contain only the new automaton that is created to disable the "
                        + "events (BOOL=no), or should include the input specification as well (BOOL=yes). "
                        + "[DEFAULT=yes]",
                'i', "include-input", "BOOL", false, true,
                "Should the output file contain only the new automaton that is created to disable the events, "
                        + "or should it include the input specification as well?",
                "Include input specification");
    }

    /**
     * Returns a value indicating whether the input specification should be included in the output.
     *
     * @return {@code true} if the input specification should be included in the output, {@code false} otherwise.
     */
    public static boolean includeInputSpec() {
        return Options.get(IncludeInputSpecOption.class);
    }
}
