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

import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.solver.Solver;

/** Output class for generating PNG image files. */
public class NormalImageOutput extends ImageOutput {
    /** Output space for the next diagram. */
    private Image currentDiagram;

    /** Graphics driver for the diagram. */
    private Graphics2D diagramGd;

    @Override
    public void prepareOutputFile(int width, int height, Color bgColor) {
        currentDiagram = new Image(width, height);
        currentDiagram.fill(bgColor);
    }

    @Override
    public void addGraphic(double left, double top, Solver solver, Area graphic) {
        graphic.paint(left, top, solver, diagramGd);
    }

    @Override
    public void writeOutputFile(String path) {
        currentDiagram.saveImage(path);
    }
}
