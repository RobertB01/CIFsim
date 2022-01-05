//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.svg.SvgVisualizer;

/** Base class describing a modification in an SVG tree. */
public abstract class SvgModification {
    /** The absolute or relative local file system path to the SVG file. */
    public String svgPath;

    /** Name of the node to operate on. */
    public String nodeName = "";

    /**
     * Get sub-string of a string by trimming leading and trailing spaces.
     *
     * @param line Line of text to examine.
     * @param start First character of the string to use (inclusive boundary).
     * @param end Last character of the string to use (exclusive boundary).
     * @return The sub-string between both boundaries.
     */
    protected static String getWord(String line, int start, int end) {
        while (start < end && line.charAt(start) == ' ') {
            start++;
        }
        while (end > start && line.charAt(end - 1) == ' ') {
            end--;
        }
        return line.substring(start, end);
    }

    /**
     * Apply the modification to an SVG tree.
     *
     * @param svgVis Visualizer containing the tree to modify.
     */
    public abstract void apply(SvgVisualizer svgVis);
}
