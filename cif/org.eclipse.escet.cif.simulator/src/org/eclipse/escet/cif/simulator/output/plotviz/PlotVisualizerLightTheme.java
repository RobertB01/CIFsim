//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import org.knowm.xchart.style.MatlabTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;

/** Plot visualizer light theme. */
public class PlotVisualizerLightTheme extends MatlabTheme {
    @Override
    public int getChartPadding() {
        return 15;
    }

    @Override
    public Color getLegendBorderColor() {
        return ChartColor.getAWTColor(ChartColor.DARK_GREY);
    }

    @Override
    public Font getLegendFont() {
        return getAxisTitleFont();
    }

    @Override
    public int getLegendPadding() {
        return 7;
    }

    @Override
    public LegendPosition getLegendPosition() {
        return LegendPosition.OutsideS;
    }

    @Override
    public double getPlotContentSize() {
        return 1.0;
    }

    @Override
    public Color getPlotGridLinesColor() {
        return new Color(245, 245, 245);
    }

    @Override
    public Stroke getPlotGridLinesStroke() {
        return new BasicStroke();
    }

    @Override
    public Color getPlotBackgroundColor() {
        return new Color(252, 252, 252);
    }

    @Override
    public Marker[] getSeriesMarkers() {
        return new Marker[] {SeriesMarkers.NONE};
    }

    @Override
    public BasicStroke[] getSeriesLines() {
        return new BasicStroke[] {SeriesLines.SOLID};
    }

    @Override
    public Color[] getSeriesColors() {
        return MaterialUiColors600.COLORS;
    }
}
