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

import org.eclipse.escet.common.raildiagrams.config.FontData;
import org.eclipse.escet.common.raildiagrams.config.TextSizeOffset;
import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.railroad.DiagramElement;
import org.eclipse.escet.common.raildiagrams.solver.Solver;

/** Base class for generating output files. */
public abstract class OutputTarget {
    /**
     * Get both the size and the offset of a text.
     *
     * @param text Text to query.
     * @param font Font to use.
     * @return Size and offset of the formatted text.
     */
    public abstract TextSizeOffset getTextSizeOffset(String text, FontData font);

    /**
     * Notification that a new output file will be needed.
     *
     * @param width Width of the diagram.
     * @param height Height of the diagram.
     * @param bgColor Suggested background color of the diagram.
     */
    public abstract void prepareOutputFile(int width, int height, Color bgColor);

    /**
     * Write output file to the provided destination.
     *
     * @param path File system path to write to.
     */
    public abstract void writeOutputFile(String path);

    /**
     * A diagram element is added to the output.
     *
     * @param left Leftmost horizontal coordinate that is available for the element.
     * @param top Topmost vertical coordinate that is available for the element.
     * @param solver Solver for resolving variables to their values.
     * @param element Element being added.
     * @note Diagram elements are just bounding boxes containing the actual graphics. They are only useful for
     *     debugging.
     */
    public void addDiagramElement(int left, int top, Solver solver, DiagramElement element) {
        // Skip by default.
    }

    /**
     * A graphic is added to the output.
     *
     * @param left Leftmost horizontal coordinate that is available for the graphic.
     * @param top Topmost vertical coordinate that is available for the graphic.
     * @param solver Solver for resolving variables to their values.
     * @param graphic Graphic element being added.
     */
    public abstract void addGraphic(int left, int top, Solver solver, Area graphic);

    /**
     * The color to use for the element with the provided name.
     *
     * @param name Name of the element.
     * @return If not {@code null}, the color to use for the indicated name.
     * @note Intended use is for overriding specified colors in the configuration.
     */
    public Color getOverrideColor(String name) {
        return null;
    }
}
