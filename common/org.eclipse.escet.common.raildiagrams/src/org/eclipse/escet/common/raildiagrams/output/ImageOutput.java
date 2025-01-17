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

package org.eclipse.escet.common.raildiagrams.output;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
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
import org.eclipse.escet.common.raildiagrams.graphics.TextArea;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.VertLine;
import org.eclipse.escet.common.raildiagrams.output.MarchingRectangles.PixelCoverage;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.util.Position2D;
import org.eclipse.escet.common.raildiagrams.util.Size2D;

/** Base class for generating images. */
public abstract class ImageOutput extends OutputTarget {
    /** Graphics driver for text layout. */
    private final Graphics2D textGd;

    /** Computing class for deciding coverage fraction of pixels by arcs. */
    private final MarchingRectangles arcCoverage = new MarchingRectangles();

    /** Constructor of the {@link ImageOutput} class. */
    public ImageOutput() {
        BufferedImage dummyImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        textGd = Image.getGraphics(dummyImage);
    }

    /**
     * Paint the provided graphic relative to the indicated top-left base position.
     *
     * @param baseLeft X coordinate of the base position.
     * @param baseTop Y coordinate of the base position.
     * @param solver Solver keeping variable values.
     * @param graphic Graphic segment to draw.
     * @param image Output image to paint at.
     */
    protected void paintGraphic(int baseLeft, int baseTop, Solver solver, Area graphic, Image image) {
        if (graphic instanceof HorLine) {
            paintHorLine(baseLeft, baseTop, solver, (HorLine)graphic, image);
            return;
        } else if (graphic instanceof VertLine) {
            paintVertLine(baseLeft, baseTop, solver, (VertLine)graphic, image);
            return;
        } else if (graphic instanceof TextArea) {
            TextArea ta = (TextArea)graphic;
            paintText(baseLeft, baseTop, solver, ta, image);
            return;
        }

        // Arcs have a common setup.
        Assert.check(graphic instanceof Arc);
        Arc arc = (Arc)graphic;

        int left = baseLeft + solver.getVarValue(graphic.left);
        int right = baseLeft + solver.getVarValue(graphic.right);
        int top = baseTop + solver.getVarValue(graphic.top);
        int bottom = baseTop + solver.getVarValue(graphic.bottom);

        int outerRad = right - left + 1; // At the very edge of the arc bounding box.
        int innerRad = outerRad - arc.lineWidth;
        int fgColor = arc.railColor.getRGB();
        Assert.check(innerRad > 0);

        Optional<Integer> minX = Optional.empty();
        Optional<Integer> maxX = Optional.empty();
        Optional<Integer> minY = Optional.empty();
        Optional<Integer> maxY = Optional.empty();

        if (arc instanceof BottomLeftArc) {
            int cx = right + 1;
            int cy = top;
            Position2D center = new Position2D(cx, cy);
            Position2D relativeInitial = new Position2D(-1, 1);
            maxX = Optional.of(cx);
            minY = Optional.of(cy);
            paintCoverage(center, relativeInitial, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        } else if (arc instanceof BottomRightArc) {
            int cx = left;
            int cy = top;
            Position2D center = new Position2D(cx, cy);
            Position2D relativeInitial = new Position2D(1, 1);
            minX = Optional.of(cx);
            minY = Optional.of(cy);
            paintCoverage(center, relativeInitial, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        } else if (arc instanceof TopLeftArc) {
            int cx = right + 1;
            int cy = bottom + 1;
            Position2D center = new Position2D(cx, cy);
            Position2D relativeInitial = new Position2D(-1, -1);
            maxX = Optional.of(cx);
            maxY = Optional.of(cy);
            paintCoverage(center, relativeInitial, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        } else if (arc instanceof TopRightArc) {
            int cx = left;
            int cy = bottom + 1;
            Position2D center = new Position2D(cx, cy);
            Position2D relativeInitial = new Position2D(1, -1);
            minX = Optional.of(cx);
            maxY = Optional.of(cy);
            paintCoverage(center, relativeInitial, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        }
        throw new AssertionError("Unexpected arc encountered.");
    }

    /**
     * Draw a horizontal line.
     *
     * @param baseLeft X coordinate of the base position.
     * @param baseTop Y coordinate of the base position.
     * @param solver Solver keeping variable values.
     * @param horLine Horizontal line to draw.
     * @param image Output image to paint at.
     */
    private void paintHorLine(int baseLeft, int baseTop, Solver solver, HorLine horLine, Image image) {
        int top = solver.getVarValue(horLine.top) + baseTop;
        int bottom = solver.getVarValue(horLine.bottom) + baseTop;
        int left = solver.getVarValue(horLine.left) + baseLeft;
        int right = solver.getVarValue(horLine.right) + baseLeft;

        int fgColor = horLine.railColor.getRGB();

        // Paint the pixels.
        for (int y = top; y <= bottom; y++) {
            int index = image.getIndex(left, y);
            for (int x = left; x <= right; x++) {
                paintPixel(index, fgColor, image, 1.0);
                index++;
            }
        }
    }

    /**
     * Draw a vertical line.
     *
     * @param baseLeft X coordinate of the base position.
     * @param baseTop Y coordinate of the base position.
     * @param solver Solver keeping variable values.
     * @param vertLine Vertical line to draw.
     * @param image Output image to paint at.
     */
    private void paintVertLine(int baseLeft, int baseTop, Solver solver, VertLine vertLine, Image image) {
        int top = solver.getVarValue(vertLine.top) + baseTop;
        int bottom = solver.getVarValue(vertLine.bottom) + baseTop;
        int left = solver.getVarValue(vertLine.left) + baseLeft;
        int right = solver.getVarValue(vertLine.right) + baseLeft;

        int fgColor = vertLine.railColor.getRGB();

        // Paint the pixels.
        int horLength = right - left + 1;
        int index = image.getIndex(left, top);
        for (int y = top; y <= bottom; y++) {
            for (int i = 0; i < horLength; i++) {
                paintPixel(index + i, fgColor, image, 1.0);
            }
            index += image.width;
        }
    }

    /**
     * Compute pixel coverage of a partial circle, and apply it to the image.
     *
     * @param center Position of the circle center.
     * @param relativeInitial Position of the initial pixel relative to circle center.
     * @param innerRad Radius of the inner circle.
     * @param outerRad Radius of the outer circle.
     * @param minX Minimum X coordinate of pixels that may be included in the coverage.
     * @param maxX Maximum X coordinate of pixels that may be included in the coverage.
     * @param minY Minimum Y coordinate of pixels that may be included in the coverage.
     * @param maxY Maximum Y coordinate of pixels that may be included in the coverage.
     * @param fgColor Foreground color to use.
     * @param image Image to paint at.
     */
    private void paintCoverage(Position2D center, Position2D relativeInitial, int innerRad, int outerRad,
            Optional<Integer> minX, Optional<Integer> maxX, Optional<Integer> minY, Optional<Integer> maxY, int fgColor,
            Image image)
    {
        Position2D initialPos = new Position2D(center.x + relativeInitial.x, center.y + relativeInitial.y);
        List<PixelCoverage> coveredPixels = arcCoverage.getCoverage(center, initialPos, innerRad, outerRad, minX, maxX,
                minY, maxY);
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
     * @param solver Solver keeping variable values.
     * @param textGraphic Text graphic to draw.
     * @param image Output image to paint at.
     */
    private void paintText(int baseLeft, int baseTop, Solver solver, TextArea textGraphic, Image image) {
        int top = solver.getVarValue(textGraphic.top) + baseTop;
        int left = solver.getVarValue(textGraphic.left) + baseLeft;

        FontData fd = textGraphic.font;
        FontRenderContext renderContext = textGd.getFontRenderContext();
        TextLayout layout = new TextLayout(textGraphic.text, fd.font, renderContext);

        Graphics2D gd = image.getGraphics();
        gd.setColor(textGraphic.color);

        Position2D offset = textGraphic.offset;
        layout.draw(gd, left + offset.x, top + offset.y);
    }

    @Override
    public TextSizeOffset getTextSizeOffset(String text, FontData fontData) {
        FontMetrics fm = textGd.getFontMetrics(fontData.font);
        int height = fm.getAscent() + fm.getDescent();
        int vertOffset = fm.getAscent();

        FontRenderContext renderContext = textGd.getFontRenderContext();
        TextLayout layout = new TextLayout(text, fontData.font, renderContext);
        Rectangle2D area = layout.getPixelBounds(renderContext, 0, 0);

        Position2D offset = new Position2D(-(int)area.getX(), vertOffset);
        Size2D size = new Size2D((int)area.getWidth(), height);
        return new TextSizeOffset(offset, size);
    }

    /**
     * Return the resulting image.
     *
     * @return The created result.
     * @note This method is mostly used for testing.
     * @see #writeOutputFile
     */
    public abstract BufferedImage getOutput();
}
