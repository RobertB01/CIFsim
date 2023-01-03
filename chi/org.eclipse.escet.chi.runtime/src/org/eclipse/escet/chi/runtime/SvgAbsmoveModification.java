//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.svg.SvgUtils;
import org.eclipse.escet.common.svg.SvgVisualizer;

/** Data about moving an SVG node to an absolute position. */
public class SvgAbsmoveModification extends SvgModification {
    /** X position of the destination. */
    public double xDest;

    /** Y position of the destination. */
    public double yDest;

    @Override
    public void apply(SvgVisualizer svgVis) {
        SvgUtils.move(svgVis.getDocument(), svgVis.getSvgCanvas().getBridgeContext(), nodeName, xDest, yDest, svgPath);
    }

    /**
     * Decode a line to an absmove modification, if it contains one.
     *
     * <p>
     * The generic form of the line is {@code absmove [node] ( [xpos], [ypos] )}.
     * </p>
     *
     * @param line Line to decode.
     * @param svgPath The absolute or relative local file system path to the SVG file.
     * @return The data of the absolute move modification, or {@code null} if it is another type of modification.
     */
    public static SvgAbsmoveModification decode(String line, String svgPath) {
        if (!line.startsWith("absmove")) {
            return null;
        }
        int i = 7;
        int j = line.indexOf('(', i);
        if (j < 0) {
            String msg = fmt("Cannot find \"(\" in the move command \"%s\".", line);
            throw new InputOutputException(msg);
        }
        int k = line.indexOf(',', j + 1);
        if (k < 0) {
            String msg = fmt("Cannot find \",\" in the move command \"%s\".", line);
            throw new InputOutputException(msg);
        }
        int m = line.indexOf(')', k + 1);
        if (m < 0) {
            String msg = fmt("Cannot find \")\" in the move command \"%s\".", line);
            throw new InputOutputException(msg);
        }
        // Remainder of 'line' is ignored.

        SvgAbsmoveModification sam = new SvgAbsmoveModification();
        sam.svgPath = svgPath;
        sam.nodeName = getWord(line, i, j);
        if (sam.nodeName.isEmpty()) {
            String msg = fmt("Node name is empty in the move command \"%s\".", line);
            throw new InputOutputException(msg);
        }
        try {
            sam.xDest = Double.parseDouble(line.substring(j + 1, k));
        } catch (NumberFormatException e) {
            String msg = fmt("Cannot convert the x destination in the move command \"%s\".", line);
            throw new InputOutputException(msg, e);
        }
        try {
            sam.yDest = Double.parseDouble(line.substring(k + 1, m));
        } catch (NumberFormatException e) {
            String msg = fmt("Cannot convert the y destination in the move command \"%s\".", line);
            throw new InputOutputException(msg, e);
        }
        return sam;
    }
}
