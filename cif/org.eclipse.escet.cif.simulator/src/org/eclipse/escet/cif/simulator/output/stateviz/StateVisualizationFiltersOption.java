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

package org.eclipse.escet.cif.simulator.output.stateviz;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** State visualization filters option. */
public class StateVisualizationFiltersOption extends StringOption {
    /** Constructor for the {@link StateVisualizationFiltersOption} class. */
    public StateVisualizationFiltersOption() {
        super("State visualization filters",
                "Option to specify which state objects to display in the state visualizer. Specify comma separated "
                        + "absolute names of state objects. The \"*\" character can be used as wildcard, and "
                        + "indicates zero or more characters. Prefix the name or pattern with a \"-\" for an "
                        + "exclusion filter instead of an inclusion filter. The comma separated filters can be "
                        + "separated by semicolons to specify filters for multiple state visualizers. [DEFAULT=\"*\"]",
                null, "stateviz-filter", "FILTERS", "*", false, true,
                "Option to specify which state objects to display in the state visualizer. Specify comma separated "
                        + "absolute names of state objects. The \"*\" character can be used as wildcard, and "
                        + "indicates zero or more characters. Prefix the name or pattern with a \"-\" for an "
                        + "exclusion filter instead of an inclusion filter. The comma separated filters can be "
                        + "separated by semicolons to specify filters for multiple state visualizers.",
                "Filters:");
    }

    /**
     * Returns the state objects filters text, per visualizer.
     *
     * @return The state objects filters text, per visualizer.
     */
    public static String[] getFiltersPerViz() {
        String filtersTxt = Options.get(StateVisualizationFiltersOption.class);
        return filtersTxt.split(";");
    }
}
