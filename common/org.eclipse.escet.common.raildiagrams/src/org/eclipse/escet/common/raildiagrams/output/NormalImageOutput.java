//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.solver.Solver;

/** Output class for generating PNG image files. */
public class NormalImageOutput extends ImageOutput {
    /** Output space for the next diagram. */
    private Image currentDiagram;

    @Override
    public void prepareOutputFile(int width, int height, Color bgColor) {
        currentDiagram = new Image(width, height);
        currentDiagram.fill(bgColor);
    }

    @Override
    public void addGraphic(int left, int top, Solver solver, Area graphic) {
        paintGraphic(left, top, solver, graphic, currentDiagram);
    }

    @Override
    public BufferedImage getOutput() {
        return currentDiagram.image;
    }

    @Override
    public void writeOutputFile(Path path) throws IOException {
        currentDiagram.saveImage(path);
    }
}
