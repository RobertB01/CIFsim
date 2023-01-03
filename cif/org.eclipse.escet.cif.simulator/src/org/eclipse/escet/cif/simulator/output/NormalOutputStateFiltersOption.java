//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Normal output state filters option. */
public class NormalOutputStateFiltersOption extends StringOption {
    /** Constructor for the {@link NormalOutputStateFiltersOption} class. */
    public NormalOutputStateFiltersOption() {
        super("Normal output state filters",
                "Option to specify which state objects to print to the console as part of normal output. Specify "
                        + "comma separated absolute names of state objects. The \"*\" character can be used as "
                        + "wildcard, and indicates zero or more characters. Prefix the name or pattern with a \"-\" "
                        + "for an exclusion filter instead of an inclusion filter. [DEFAULT=\"*\"]",
                null, "output-state-filter", "FILTERS", "*", false, true,
                "Option to specify which state objects to print to the console as part of normal output. Specify "
                        + "comma separated absolute names of state objects. The \"*\" character can be used as "
                        + "wildcard, and indicates zero or more characters. Prefix the name or pattern with a \"-\" "
                        + "for an exclusion filter instead of an inclusion filter.",
                "Filters:");
    }

    /**
     * Returns the normal output state filters text.
     *
     * @return The normal output state filters text.
     */
    public static String getFilters() {
        return Options.get(NormalOutputStateFiltersOption.class);
    }
}
