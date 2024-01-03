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

import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.svg.SvgUtils;
import org.eclipse.escet.common.svg.SvgVisualizer;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/** Data about a text entry to change in the SVG. */
public class SvgTextModification extends SvgModification {
    /** Value to set. */
    public String value;

    @Override
    public void apply(SvgVisualizer svgVis) {
        Element element = svgVis.getDocument().getElementById(nodeName);
        if (element == null) {
            String msg = fmt("Failed to find SVG element \"%s\".", nodeName);
            throw new InputOutputException(msg);
        }
        Text text = SvgUtils.getTextNode(element);
        if (text == null) {
            String msg = fmt("Failed to find SVG text element \"%s\".", nodeName);
            throw new InputOutputException(msg);
        }
        text.setNodeValue(value);
    }

    /**
     * Decode a text modification line if it contains one.
     *
     * <p>
     * The generic form of the line is {@code text [node] = [value]}.
     * </p>
     *
     * @param line Text to decode.
     * @param svgPath The absolute or relative local file system path to the SVG file.
     * @return The data of the decoded line, if all went well, else {@code null}.
     */
    public static SvgTextModification decode(String line, String svgPath) {
        if (!line.startsWith("text")) {
            return null;
        }

        SvgTextModification std = new SvgTextModification();
        std.svgPath = svgPath;

        int j = line.indexOf('=');
        if (j < 0) {
            String msg = fmt("Missing \"=\" in text command \"%s\".", line);
            throw new InputOutputException(msg);
        }
        std.nodeName = getWord(line, 4, j);
        if (std.nodeName.isEmpty()) {
            String msg = fmt("Node name is empty in text command \"%s\".", line);
            throw new InputOutputException(msg);
        }

        // Decode value.
        std.value = getWord(line, j + 1, line.length());
        return std;
    }
}
