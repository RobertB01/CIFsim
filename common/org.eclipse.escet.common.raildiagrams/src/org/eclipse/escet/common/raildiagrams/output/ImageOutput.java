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

package org.eclipse.escet.common.raildiagrams.output;

import static org.eclipse.escet.common.raildiagrams.graphics.PaintSupport.getGraphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.config.FontData;
import org.eclipse.escet.common.raildiagrams.config.TextSizeOffset;
import org.eclipse.escet.common.raildiagrams.graphics.Arc;
import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.PaintSupport;
import org.eclipse.escet.common.raildiagrams.graphics.TextArea;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.VertLine;
import org.eclipse.escet.common.raildiagrams.output.MarchingRectangles.PixelCoverage;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.util.Position2D;

/** Base class for generating images. */
public abstract class ImageOutput extends OutputTarget {
    /** Graphics driver for text layout. */
    private final Graphics2D textGd;

    /** Computing class for deciding coverage fraction of pixels by arcs. */
    private final MarchingRectangles arcCoverage = new MarchingRectangles();

    /** Constructor of the {@link ImageOutput} class. */
    public ImageOutput() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        textGd = getGraphics(image);
    }

    /**
     * Paint the provided arc relative to the indicated top-left base position.
     *
     * @param baseLeft X coordinate of the base position.
     * @param baseTop Y coordinate of the base position.
     * @param solver Solver keeping variable values of the arc.
     * @param graphic Graphic segment to draw.
     * @param image Output image to paint at.
     */
    protected void paintGraphic(double baseLeft, double baseTop, Solver solver, Area graphic, Image image) {
        if (graphic instanceof HorLine) {
            paintHorLine(baseLeft, baseTop, solver, graphic, image);
            return;
        }
        if (graphic instanceof VertLine) {
            paintVertLine(baseLeft, baseTop, solver, graphic, image);
            return;
        }
        if (graphic instanceof TextArea) {
            TextArea ta = (TextArea)graphic;
            paintText(baseLeft, baseTop, solver, ta, image);
            return;
        }

        // Arcs have a common setup.
        Assert.check(graphic instanceof Arc);
        Arc arc = (Arc)graphic;

        int left = (int)(baseLeft + solver.getVarValue(graphic.left));
        int right = (int)(baseLeft + solver.getVarValue(graphic.right));
        int top = (int)(baseTop + solver.getVarValue(graphic.top));
        int bottom = (int)(baseTop + solver.getVarValue(graphic.bottom));

        double outerRad = right - left;
        double innerRad = outerRad - arc.lineWidth;
        int fgColor = arc.railColor.getRGB();
        Assert.check(innerRad > 0);

        Optional<Integer> minX = Optional.empty();
        Optional<Integer> maxX = Optional.empty();
        Optional<Integer> minY = Optional.empty();
        Optional<Integer> maxY = Optional.empty();

        if (arc instanceof BottomLeftArc) {
            int cx = right;
            int cy = top;
            maxX = Optional.of(cx);
            minY = Optional.of(cy);
            paintCoverage(cx, cy, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        }
        if (arc instanceof BottomRightArc) {
            int cx = left;
            int cy = top;
            minX = Optional.of(cx);
            minY = Optional.of(cy);
            paintCoverage(cx, cy, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        }
        if (arc instanceof TopLeftArc) {
            int cx = right;
            int cy = bottom;
            maxX = Optional.of(cx);
            maxY = Optional.of(cy);
            paintCoverage(cx, cy, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        }
        if (arc instanceof TopRightArc) {
            int cx = left;
            int cy = bottom;
            minX = Optional.of(cx);
            maxY = Optional.of(cy);
            paintCoverage(cx, cy, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        }
        throw new AssertionError("Unexpected graphic encountered.");
    }

    /**
     * Draw a horizontal line.
     *
     * @param baseLeft X coordinate of the base position.
     * @param baseTop Y coordinate of the base position.
     * @param solver Solver keeping variable values of the arc.
     * @param graphic Graphic segment to draw.
     * @param image Output image to paint at.
     */
    private void paintHorLine(double baseLeft, double baseTop, Solver solver, Area graphic, Image image) {
        double top = solver.getVarValue(graphic.top) + baseTop;
        double bottom = solver.getVarValue(graphic.bottom) + baseTop - 1;
        int left = (int)(solver.getVarValue(graphic.left) + baseLeft);
        int right = (int)(solver.getVarValue(graphic.right) + baseLeft - 1);

        int fgColor = ((HorLine)graphic).railColor.getRGB();

        // Compute coverage of each pixel row.
        double topPixel = Math.floor(top);
        double bottomPixel = Math.ceil(bottom);
        double topUncovered = top - topPixel;
        double bottomUncovered = bottomPixel - bottom;
        double[] yCoverage = new double[(int)(bottomPixel - topPixel + 1)];

        int yIndex = 0;
        for (double yPos = topPixel; yPos <= bottomPixel; yPos++) {
            double coverage = 1.0;
            if (yPos == topPixel) {
                coverage -= topUncovered;
            }
            if (yPos + 1 == bottomPixel) {
                coverage -= bottomUncovered;
            }
            yCoverage[yIndex] = Math.max(0, coverage);
            yIndex++;
        }

        // Paint the pixels.
        for (int yOffset = 0; yOffset < yIndex; yOffset++) {
            int index = image.getIndex(left, (int)(topPixel + yOffset));
            for (int x = left; x <= right; x++) {
                paintPixel(index, fgColor, image, yCoverage[yOffset]);
                index++;
            }
        }
    }

    /**
     * Draw a vertical line.
     *
     * @param baseLeft X coordinate of the base position.
     * @param baseTop Y coordinate of the base position.
     * @param solver Solver keeping variable values of the arc.
     * @param graphic Graphic segment to draw.
     * @param image Output image to paint at.
     */
    private void paintVertLine(double baseLeft, double baseTop, Solver solver, Area graphic, Image image) {
        int top = (int)(solver.getVarValue(graphic.top) + baseTop);
        int bottom = (int)(solver.getVarValue(graphic.bottom) + baseTop - 1);
        double left = solver.getVarValue(graphic.left) + baseLeft;
        double right = solver.getVarValue(graphic.right) + baseLeft - 1;

        int fgColor = ((VertLine)graphic).railColor.getRGB();

        // Compute coverage of each pixel column.
        double leftPixel = Math.floor(left);
        double rightPixel = Math.ceil(right);
        double leftUncovered = left - leftPixel;
        double rightUncovered = rightPixel - right;
        double[] xCoverage = new double[(int)(rightPixel - leftPixel + 1)];

        int xIndex = 0;
        for (double xPos = leftPixel; xPos <= rightPixel; xPos++) {
            double coverage = 1.0;
            if (xPos == leftPixel) {
                coverage -= leftUncovered;
            }
            if (xPos + 1 == rightPixel) {
                coverage -= rightUncovered;
            }
            xCoverage[xIndex] = Math.max(0, coverage);
            xIndex++;
        }

        // Paint the pixels.
        int index = image.getIndex((int)leftPixel, top);
        for (int y = top; y <= bottom; y++) {
            for (int i = 0; i < xIndex; i++) {
                paintPixel(index + i, fgColor, image, xCoverage[i]);
            }
            index += image.width;
        }
    }

    /**
     * Compute pixel coverage of a partial circle, and apply it to the image.
     *
     * @param cx X coordinate of the circle center.
     * @param cy Y coordinate of the circle center.
     * @param innerRad Radius of the inner circle.
     * @param outerRad Radius of the outer circle.
     * @param minX Minimum X coordinate of pixels that may be included in the coverage.
     * @param maxX Maximum X coordinate of pixels that may be included in the coverage.
     * @param minY Minimum Y coordinate of pixels that may be included in the coverage.
     * @param maxY Maximum Y coordinate of pixels that may be included in the coverage.
     * @param fgColor Foreground color to use.
     * @param image Image to paint at.
     */
    private void paintCoverage(int cx, int cy, double innerRad, double outerRad, Optional<Integer> minX,
            Optional<Integer> maxX, Optional<Integer> minY, Optional<Integer> maxY, int fgColor, Image image)
    {
        List<PixelCoverage> coveredPixels = arcCoverage.getCoverage(cx, cy, innerRad, outerRad, minX, maxX, minY, maxY);
        for (PixelCoverage coveredPixel: coveredPixels) {
            int index = image.getIndex(coveredPixel.x, coveredPixel.y);
            paintPixel(index, fgColor, image, coveredPixel.coverage);
        }
    }

    /**
     * Change the color of a pixel by blending existing background with the provided foreground color.
     *
     * @param index Index in the pixel data array.
     * @param fgColor Color to paint.
     * @param image Image to modify.
     * @param coverage Fraction denoting amount of applying the foreground color.
     */
    private void paintPixel(int index, int fgColor, Image image, double coverage) {
        int curCol = image.pixels[index];
        int newCol = curCol >> 24;
        for (int i = 16; i >= 0; i -= 8) {
            int curChannel = (curCol >> i) & 0xFF;
            int fgChannel = (fgColor >> i) & 0xFF;
            double newChannel = curChannel * (1 - coverage) + (fgChannel * coverage);
            newCol = (newCol << 8) | (int)(newChannel);
        }
        image.pixels[index] = newCol;
    }

    /**
     * Paint the text of the {@link TextArea} graphic.
     *
     * @param baseLeft X coordinate of the base position.
     * @param baseTop Y coordinate of the base position.
     * @param solver Solver keeping variable values of the arc.
     * @param textGraphic Text graphic to draw.
     * @param image Output image to paint at.
     */
    private void paintText(double baseLeft, double baseTop, Solver solver, TextArea textGraphic, Image image) {
        double top = solver.getVarValue(textGraphic.top) + baseTop;
        double left = solver.getVarValue(textGraphic.left) + baseLeft;

        FontData fd = textGraphic.font;
        Position2D offset = textGraphic.offset;
        Graphics2D gd = PaintSupport.getGraphics(image.image);
        fd.paint(left + offset.x, top + offset.y, textGraphic.color, gd, textGraphic.text);
    }

    @Override
    public TextSizeOffset getTextSizeOffset(String text, FontData fontData) {
        return fontData.getTextSizeOffset(textGd, text);
    }

    /**
     * Construct the resulting image.
     *
     * @return The created result.
     * @note This method is mostly used for testing.
     * @see #writeOutputFile
     */
    public abstract BufferedImage getOutput();
}
