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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.escet.common.raildiagrams.config.FontData;
import org.eclipse.escet.common.raildiagrams.config.TextSizeOffset;
import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.solver.Solver;

/** Output class for generating PNG image files. */
public class ImageOutput extends OutputTarget {
    /** Graphics driver for text layout. */
    private Graphics2D textGd;

    /** output space for the next diagram. */
    private BufferedImage currentDiagram;

    /** Graphics driver for the diagram. */
    private Graphics2D diagramGd;

    /**
     * Constructor of the {@link ImageOutput} class.
     */
    public ImageOutput() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        textGd = image.createGraphics();
    }

    /** {@inheritDoc} */
    @Override
    public TextSizeOffset getTextSizeOffset(String text, FontData fontData) {
        return new TextSizeOffset(fontData.getTextOffset(textGd, text), fontData.getTextSize(textGd, text));
    }

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
        try {
            ImageIO.write(currentDiagram, "png", new File(path));
        } catch (IOException ex) {
            String msg = fmt("Failed to write PNG image file \"%s\".", path);
            throw new RuntimeException(msg, ex);
        }
    }
}
