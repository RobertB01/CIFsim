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

package org.eclipse.escet.cif.simulator.output.svgviz;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** SVG visualization option. */
public class SvgVisualizationOption extends BooleanOption {
    /** Constructor for the {@link SvgVisualizationOption} class. */
    public SvgVisualizationOption() {
        super("SVG visualization",
                "Whether to enable (BOOL=on) SVG visualization (if present in the CIF specification), or to disable "
                        + "it (BOOL=off). [DEFAULT=on]",
                null, "svg", "BOOL", true, true,
                "Whether to enable SVG visualization (if present in the CIF specification), or to disable it.",
                "Enable SVG visualization");
    }

    /**
     * Is SVG visualization enabled?
     *
     * @return {@code true} if SVG visualization is enabled, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(SvgVisualizationOption.class);
    }
}
