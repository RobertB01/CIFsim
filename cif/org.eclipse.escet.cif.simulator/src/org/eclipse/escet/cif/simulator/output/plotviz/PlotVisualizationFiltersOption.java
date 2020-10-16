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

package org.eclipse.escet.cif.simulator.output.plotviz;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Plot visualization filters option. */
public class PlotVisualizationFiltersOption extends StringOption {
    /** Constructor for the {@link PlotVisualizationFiltersOption} class. */
    public PlotVisualizationFiltersOption() {
        super("Plot visualization filters",
                "Option to specify which state objects to display in the plot visualizer. Specify comma separated "
                        + "absolute names of state objects. The \"*\" character can be used as wildcard, and "
                        + "indicates zero or more characters. Prefix the name or pattern with a \"-\" for an "
                        + "exclusion filter instead of an inclusion filter. The comma separated filters can be "
                        + "separated by semicolons to specify filters for multiple plot visualizers. "
                        + "[DEFAULT=\"*,-time\"]",
                null, "plotviz-filter", "FILTERS", "*,-time", false, true,
                "Option to specify which state objects to display in the plot visualizer. Specify comma separated "
                        + "absolute names of state objects. The \"*\" character can be used as wildcard, and "
                        + "indicates zero or more characters. Prefix the name or pattern with a \"-\" for an "
                        + "exclusion filter instead of an inclusion filter. The comma separated filters can be "
                        + "separated by semicolons to specify filters for multiple plot visualizers.",
                "Filters:");
    }

    /**
     * Returns the plot objects filters text, per visualizer.
     *
     * @return The plot objects filters text, per visualizer.
     */
    public static String[] getFiltersPerViz() {
        String filtersTxt = Options.get(PlotVisualizationFiltersOption.class);
        return filtersTxt.split(";");
    }
}
