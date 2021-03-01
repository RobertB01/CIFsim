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

package org.eclipse.escet.cif.simulator.output.trajdata;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Trajectory data filters option. */
public class TrajDataFiltersOption extends StringOption {
    /** Constructor for the {@link TrajDataFiltersOption} class. */
    public TrajDataFiltersOption() {
        super("Trajectory data filters", "Option to specify which state objects to display in the trajectory data. "
                + "Specify comma separated absolute names of state objects. The \"*\" character can be used as "
                + "wildcard, and indicates zero or more characters. Prefix the name or pattern with a \"-\" for "
                + "an exclusion filter instead of an inclusion filter. [DEFAULT=\"*\"]", null, "trajdata-filter",
                "FILTERS", "*", false, true,
                "Option to specify which state objects to display in the trajectory data. Specify comma separated "
                        + "absolute names of state objects. The \"*\" character can be used as wildcard, and "
                        + "indicates zero or more characters. Prefix the name or pattern with a \"-\" for an "
                        + "exclusion filter instead of an inclusion filter.",
                "Filters:");
    }

    /**
     * Returns the trajectory data filters text.
     *
     * @return The trajectory data filters text.
     */
    public static String getFilters() {
        return Options.get(TrajDataFiltersOption.class);
    }
}
