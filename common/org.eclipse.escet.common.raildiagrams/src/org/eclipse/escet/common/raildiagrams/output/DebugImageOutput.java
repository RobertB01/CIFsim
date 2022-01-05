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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.util.Arrays;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.graphics.Arc;
import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.railroad.DiagramElement;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.util.Position2D;

/**
 * Class for generating images for debugging pixels.
 *
 * <p>
 * Colors copied from https://en.wikipedia.org/wiki/List_of_colors_by_shade
 * </p>
 *
 * <p>
 * The priority of the element concepts displayed in the result is as follows (lower numbers take priority).
 * <ol>
 * <li>Rail (also includes all other visible items such as text).</li>
 * <li>Connect points, positions that should be covered by rail. If visible, obviously that didn't happen.</li>
 * <li>Arc graphic box corner points.</li>
 * <li>Rectangle box corners of sub-elements.</li>
 * <li>Background.</li>
 * </ol>
 * </p>
 */
public class DebugImageOutput extends ImageOutput {
    /** ARGB color for background. */
    private static final int BACKGROUND = 0xFF_00_00_00; // Black.

    /** ARGB color for one layer of rail. */
    private static final int SINGLE_RAIL = 0xFF_B8860B; // Dark goldenrod.

    /** ARGB color for two layers of rail. */
    private static final int DOUBLE_RAIL = 0xFF_E3FF00; // Lemon lime.

    /** ARGB color for three or more layers of rail. */
    private static final int TRIPLE_RAIL = 0xFF_F3E5AB; // Vanilla.

    /** ARGB color of expected attach-point by another graphic. */
    private static final int CONNECT_POINT = 0xFF_50C878; // Paris green.

    /** ARGB color of arc graphics corner points. */
    private static final int GRAPHICS_CORNER = 0xFF_BA160C; // Engineering orange.

    /** ARGB color of box corner points. */
    private static final int BOX_CORNER = 0xFF_0D98BA; // Blue-green.

    /** Scratch image for a file. */
    private BufferedImage scratchImage = null;

    /** Width of the current image. */
    private int width;

    /** Height of the current image. */
    private int height;

    /**
     * Result image as a flat array of ARGB values.
     *
     * <p>
     * {@code (x, y)} is at index {@code x + y * width}.
     * </p>
     */
    private int[] resultData;

    @Override
    public void prepareOutputFile(int outputWidth, int outputHeight, Color bgColor) {
        width = outputWidth;
        height = outputHeight;

        boolean firstImage = (scratchImage == null);
        scratchImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        if (firstImage) {
            // First time an image is created. Do some checking to ensure code below will
            // work.
            Raster r = scratchImage.getData();
            Assert.check(r.getTransferType() == DataBuffer.TYPE_INT);
            Assert.check(r.getNumDataElements() == 1); // One pixel in one integer.

            int[] sampleSizes = r.getSampleModel().getSampleSize();
            Assert.check(sampleSizes.length == 3);
            Assert.check(sampleSizes[0] == 8);
            Assert.check(sampleSizes[1] == 8);
            Assert.check(sampleSizes[2] == 8);
        }

        resultData = new int[width * height * 1]; // numDataElements == 1 .
        initializeResult();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Override diagram background color to full white, and all other colors to black.
     * </p>
     */
    @Override
    public Color getOverrideColor(String name) {
        if (name.equals("diagram.background.color")) {
            return Color.WHITE;
        }
        return Color.BLACK;
    }

    /** Clear the {@link #scratchImage} to full white. */
    private void clearScratchImage() {
        Graphics2D tempGd = scratchImage.createGraphics();
        tempGd.setBackground(Color.WHITE);
        tempGd.fillRect(0, 0, width, height);
    }

    @Override
    public void addDiagramElement(double baseLeft, double baseTop, Solver solver, DiagramElement element) {
        int left = (int)(solver.getVarValue(element.left) + baseLeft);
        int right = (int)(solver.getVarValue(element.right) + baseLeft - 1);
        int top = (int)(solver.getVarValue(element.top) + baseTop);
        int bottom = (int)(solver.getVarValue(element.bottom) + baseTop - 1);
        addBoxCorners(left, right, top, bottom);
    }

    @Override
    public void addGraphic(double baseLeft, double baseTop, Solver solver, Area graphic) {
        if (graphic instanceof Arc) {
            int left = (int)(solver.getVarValue(graphic.left) + baseLeft);
            int right = (int)(solver.getVarValue(graphic.right) + baseLeft - 1);
            int top = (int)(solver.getVarValue(graphic.top) + baseTop);
            int bottom = (int)(solver.getVarValue(graphic.bottom) + baseTop - 1);
            addGraphicCorners(left, right, top, bottom);
        }

        // Paint the graphic at the scratch image first.
        clearScratchImage();
        graphic.paint(baseLeft, baseTop, solver, scratchImage.createGraphics());

        // Check all scratch pixels and copy anything painted over to the result image
        // as 'rail'.
        int[] scratchData = new int[width * height * 1]; // numDataElements == 1 .
        scratchData = (int[])scratchImage.getRaster().getDataElements(0, 0, width, height, scratchData);

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = scratchData[index];

                if (pixel != Color.WHITE.getRGB()) {
                    addRailLayer(index);
                }

                index++;
            }
        }

        for (Position2D pos: graphic.getConnectPoints(baseLeft, baseTop, solver)) {
            addConnectPoint((int)pos.x, (int)pos.y);
        }
    }

    @Override
    public void writeOutputFile(String path) {
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        resultImage.setRGB(0, 0, width, height, resultData, 0, width);
        saveImage(resultImage, path);
    }

    /** Fill the result image with background. */
    private void initializeResult() {
        Arrays.fill(resultData, BACKGROUND);
    }

    /**
     * Add a rail layer to the indicated position in the result image.
     *
     * <p>
     * Rail overrides everything.
     * </p>
     *
     * @param index Position to change.
     */
    private void addRailLayer(int index) {
        if (resultData[index] == TRIPLE_RAIL) {
            return;
        }
        if (resultData[index] == DOUBLE_RAIL) {
            resultData[index] = TRIPLE_RAIL;
            return;
        }
        if (resultData[index] == SINGLE_RAIL) {
            resultData[index] = DOUBLE_RAIL;
            return;
        }
        resultData[index] = SINGLE_RAIL;
    }

    /**
     * Add an expected connect point to the indicated position.
     *
     * <p>
     * Connect points only disappear behind rail.
     * </p>
     *
     * @param x X coordinate of the position.
     * @param y Y coordinate of the position.
     */
    private void addConnectPoint(int x, int y) {
        int index = x + y * width;
        if (resultData[index] == BACKGROUND || resultData[index] == GRAPHICS_CORNER
                || resultData[index] == BOX_CORNER)
        {
            resultData[index] = CONNECT_POINT;
        }
    }

    /**
     * Add pixels indicating corners of the arc graphic.
     *
     * @param left Left edge of the graphic.
     * @param right Right edge of the graphic.
     * @param top Top edge of the graphic.
     * @param bottom Bottom edge of the graphic.
     */
    private void addGraphicCorners(int left, int right, int top, int bottom) {
        addGraphicCorner(left, top);
        addGraphicCorner(left, bottom);
        addGraphicCorner(right, top);
        addGraphicCorner(right, bottom);
    }

    /**
     * Add a graphics corner point at the indicated position.
     *
     * <p>
     * Box corners are only painted if the position is not used or used by a box corner.
     * </p>
     *
     * @param x X coordinate of the position.
     * @param y Y coordinate of the position.
     */
    private void addGraphicCorner(int x, int y) {
        int index = x + y * width;
        if (resultData[index] == BACKGROUND || resultData[index] == BOX_CORNER) {
            resultData[index] = CONNECT_POINT;
        }
    }

    /**
     * Add pixels indicating corners of the arc graphic.
     *
     * @param left Left edge of the graphic.
     * @param right Right edge of the graphic.
     * @param top Top edge of the graphic.
     * @param bottom Bottom edge of the graphic.
     */
    private void addBoxCorners(int left, int right, int top, int bottom) {
        addBoxCorner(left, top);
        addBoxCorner(left, bottom);
        addBoxCorner(right, top);
        addBoxCorner(right, bottom);
    }

    /**
     * Add a box corner point at the indicated position.
     *
     * <p>
     * Box corners are only painted if the position is not used by anything else.
     * </p>
     *
     * @param x X coordinate of the position.
     * @param y Y coordinate of the position.
     */
    private void addBoxCorner(int x, int y) {
        int index = x + y * width;
        if (resultData[index] == BACKGROUND) {
            resultData[index] = BOX_CORNER;
        }
    }
}
