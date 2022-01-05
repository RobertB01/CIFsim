//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.output;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.solver.Solver;

/** Output class for generating PNG image files. */
public class NormalImageOutput extends ImageOutput {
    /** output space for the next diagram. */
    private BufferedImage currentDiagram;

    /** Graphics driver for the diagram. */
    private Graphics2D diagramGd;

    /** {@inheritDoc} */
    @Override
    public void prepareOutputFile(int width, int height, Color bgColor) {
        currentDiagram = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        diagramGd = currentDiagram.createGraphics();
        diagramGd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        diagramGd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        diagramGd.setColor(bgColor);
        diagramGd.fillRect(0, 0, width, height);
    }

    /** {@inheritDoc} */
    @Override
    public void addGraphic(double left, double top, Solver solver, Area graphic) {
        graphic.paint(left, top, solver, diagramGd);
    }

    /** {@inheritDoc} */
    @Override
    public void writeOutputFile(String path) {
        saveImage(currentDiagram, path);
    }
}
