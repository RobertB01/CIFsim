//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.escet.common.raildiagrams.graphics.Arc;
import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.railroad.DiagramElement;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.util.Position2D;

/**
 * Class for generating images for debugging pixels.
 *
 * <p>
 * Colors copied from <a href="https://en.wikipedia.org/wiki/List_of_colors_by_shade">List of colors by shade</a>
 * (Wikipedia).
 * </p>
 *
 * <p>
 * The priority of the element concepts displayed in the result is as follows (lower numbers take priority):
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
    public static final int BACKGROUND = 0xFF_00_00_00; // Black.

    /** ARGB color for one layer of rail. */
    public static final int SINGLE_RAIL = 0xFF_666666; // 40% White.

    /** ARGB color for two layers of rail. */
    public static final int DOUBLE_RAIL = 0xFF_DECC9C; // Lion (dark-yellow).

    /** ARGB color for three or more layers of rail. */
    public static final int TRIPLE_RAIL = 0xFF_E49B0F; // Gamboge (average-yellow).

    /** ARGB color of expected attach-point by another graphic. */
    public static final int CONNECT_POINT = 0xFF_CC33CC; // Steel pink.

    /** ARGB color of arc graphics corner points. */
    public static final int GRAPHICS_CORNER = 0xFF_00FF00; // 100% Green.

    /** ARGB color of box corner points. */
    public static final int BOX_CORNER = 0xFF_007FFF; // Azure (blue).

    /** Scratch image for a file. */
    private Image scratchImage = null;

    /** Width of the current image. */
    private int width;

    /** Height of the current image. */
    private int height;

    /** Result image. */
    private Image result;

    @Override
    public void prepareOutputFile(int outputWidth, int outputHeight, Color bgColor) {
        width = outputWidth;
        height = outputHeight;

        scratchImage = new Image(width, height);
        result = new Image(width, height);
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
        Graphics2D tempGd = getGraphics(scratchImage.image);
        tempGd.setBackground(Color.WHITE);
        tempGd.fillRect(0, 0, width, height);
    }

    @Override
    public void addDiagramElement(int baseLeft, int baseTop, Solver solver, DiagramElement element) {
        int left = solver.getVarValue(element.left) + baseLeft;
        int right = solver.getVarValue(element.right) + baseLeft;
        int top = solver.getVarValue(element.top) + baseTop;
        int bottom = solver.getVarValue(element.bottom) + baseTop;
        addBoxCorners(left, right, top, bottom);
    }

    @Override
    public void addGraphic(int baseLeft, int baseTop, Solver solver, Area graphic) {
        if (graphic instanceof Arc) {
            int left = solver.getVarValue(graphic.left) + baseLeft;
            int right = solver.getVarValue(graphic.right) + baseLeft;
            int top = solver.getVarValue(graphic.top) + baseTop;
            int bottom = solver.getVarValue(graphic.bottom) + baseTop;
            addGraphicCorners(left, right, top, bottom);
        }

        // Paint the graphic at the scratch image first.
        clearScratchImage();
        paintGraphic(baseLeft, baseTop, solver, graphic, scratchImage);

        // Check all scratch pixels and copy anything painted over to the result image as 'rail'.
        int[] scratchData = scratchImage.pixels;

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
            addConnectPoint(pos.x, pos.y);
        }
    }

    @Override
    public BufferedImage getOutput() {
        return result.image;
    }

    @Override
    public void writeOutputFile(Path path) throws IOException {
        result.saveImage(path);
    }

    /** Fill the result image with background. */
    private void initializeResult() {
        result.fill(BACKGROUND);
    }

    /**
     * Add a rail layer to the indicated position in the result image.
     *
     * <p>
     * Rail overrides everything and tracks number of rail layers.
     * </p>
     *
     * @param index Position to change.
     */
    private void addRailLayer(int index) {
        if (result.pixels[index] == TRIPLE_RAIL) {
            return;
        }
        if (result.pixels[index] == DOUBLE_RAIL) {
            result.pixels[index] = TRIPLE_RAIL;
            return;
        }
        if (result.pixels[index] == SINGLE_RAIL) {
            result.pixels[index] = DOUBLE_RAIL;
            return;
        }
        result.pixels[index] = SINGLE_RAIL;
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
        int index = result.getIndex(x, y);
        if (result.pixels[index] == BACKGROUND || result.pixels[index] == GRAPHICS_CORNER
                || result.pixels[index] == BOX_CORNER)
        {
            result.pixels[index] = CONNECT_POINT;
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
        int index = result.getIndex(x, y);
        if (result.pixels[index] == BACKGROUND || result.pixels[index] == BOX_CORNER) {
            result.pixels[index] = GRAPHICS_CORNER;
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
        int index = result.getIndex(x, y);
        if (result.pixels[index] == BACKGROUND) {
            result.pixels[index] = BOX_CORNER;
        }
    }
}
