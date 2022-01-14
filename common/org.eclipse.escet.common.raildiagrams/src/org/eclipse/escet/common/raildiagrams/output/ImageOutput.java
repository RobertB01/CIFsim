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
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.output.MarchingRectangles.PixelCoverage;
import org.eclipse.escet.common.raildiagrams.solver.Solver;

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
     * @param graphic Arc to paint.
     * @param image Output image to paint at.
     */
    protected void paintArcGraphic(double baseLeft, double baseTop, Solver solver, Arc graphic, Image image) {
        int left = (int)(baseLeft + solver.getVarValue(graphic.left));
        int right = (int)(baseLeft + solver.getVarValue(graphic.right));
        int top = (int)(baseTop + solver.getVarValue(graphic.top));
        int bottom = (int)(baseTop + solver.getVarValue(graphic.bottom));

        double outerRad = right - left + 1;
        double innerRad = outerRad - graphic.lineWidth;
        int fgColor = graphic.railColor.getRGB();
        Assert.check(innerRad > 0);

        Optional<Integer> minX = Optional.empty();
        Optional<Integer> maxX = Optional.empty();
        Optional<Integer> minY = Optional.empty();
        Optional<Integer> maxY = Optional.empty();

        if (graphic instanceof BottomLeftArc) {
            int cx = right;
            int cy = top;
            maxX = Optional.of(cx);
            minY = Optional.of(cy);
            paintCoverage(cx, cy, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        }
        if (graphic instanceof BottomRightArc) {
            int cx = left;
            int cy = top;
            minX = Optional.of(cx);
            minY = Optional.of(cy);
            paintCoverage(cx, cy, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        }
        if (graphic instanceof TopLeftArc) {
            int cx = right;
            int cy = bottom;
            maxX = Optional.of(cx);
            maxY = Optional.of(cy);
            paintCoverage(cx, cy, innerRad, outerRad, minX, maxX, minY, maxY, fgColor, image);
            return;
        }
        if (graphic instanceof TopRightArc) {
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
     * Compute pixel coverage of a partial circle, and apply it to the image.
     *
     * <p>
     * The function blends existing background with the provided foreground color.
     * </p>
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
            int curCol = image.pixels[index];
            int newCol = curCol >> 24;
            for (int i = 16; i >= 0; i -= 8) {
                int curChannel = (curCol >> i) & 0xFF;
                int fgChannel = (fgColor >> i) & 0xFF;
                double coveredFrac = coveredPixel.coverage;
                double newChannel = curChannel * (1 - coveredFrac) + (fgChannel * coveredFrac);
                newCol = (newCol << 8) | (int)(newChannel);
            }
            image.pixels[index] = newCol;
        }
    }

    @Override
    public TextSizeOffset getTextSizeOffset(String text, FontData fontData) {
        return new TextSizeOffset(fontData.getTextOffset(textGd, text), fontData.getTextSize(textGd, text));
    }
}
