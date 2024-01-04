//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

import java.awt.Color;

/**
 * Plot visualizer dark theme.
 *
 * <p>
 * This dark theme extends the {@link PlotVisualizerLightTheme light theme} and should only override its colors. Other
 * changes should be applied to the light theme, to keep the light and dark themes consistent.
 * </p>
 */
public class PlotVisualizerDarkTheme extends PlotVisualizerLightTheme {
    @Override
    public Color getAxisTickLabelsColor() {
        return getChartFontColor();
    }

    @Override
    public Color getAxisTickMarksColor() {
        return getChartFontColor();
    }

    @Override
    public Color getChartBackgroundColor() {
        return new Color(32, 32, 32);
    }

    @Override
    public Color getChartFontColor() {
        return new Color(240, 240, 240);
    }

    @Override
    public Color getLegendBackgroundColor() {
        return getChartBackgroundColor();
    }

    @Override
    public Color getLegendBorderColor() {
        return getChartFontColor();
    }

    @Override
    public Color getPlotBackgroundColor() {
        return new Color(16, 16, 16);
    }

    @Override
    public Color getPlotBorderColor() {
        return getChartFontColor();
    }

    @Override
    public Color getPlotGridLinesColor() {
        return new Color(42, 42, 42);
    }
}
