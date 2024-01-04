//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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
import org.w3c.dom.svg.SVGStylable;

/** Data about a SVG attribute that should be set. */
public class SvgAttribModification extends SvgModification {
    /** Name of the attribute. */
    public String attribName;

    /** Value to set. */
    public String value;

    @Override
    public void apply(SvgVisualizer svgVis) {
        Element element = svgVis.getDocument().getElementById(nodeName);
        if (element == null) {
            String msg = fmt("Failed to find SVG element \"%s\".", nodeName);
            throw new InputOutputException(msg);
        }
        SVGStylable style = SvgUtils.isCssAttr(element, attribName) ? (SVGStylable)element : null;

        if (style == null) {
            element.setAttribute(attribName, value);
        } else {
            style.getStyle().setProperty(attribName, value, "");
        }
    }

    /**
     * Decode an attribute modification line if it contains one.
     *
     * <p>
     * The generic form of the line is {@code attr [node].[id] = [value]}.
     * </p>
     *
     * @param line Text to decode.
     * @param svgPath The absolute or relative local file system path to the SVG file.
     * @return The data of the decoded line, if all went well, else {@code null}.
     */
    public static SvgAttribModification decode(String line, String svgPath) {
        if (!line.startsWith("attr")) {
            return null;
        }

        SvgAttribModification sad = new SvgAttribModification();
        sad.svgPath = svgPath;

        int j = line.indexOf('=');
        if (j < 0) {
            String msg = fmt("Cannot find \"=\" in attr command \"%s\".", line);
            throw new InputOutputException(msg);
        }

        // Decode node.id .
        int i = line.indexOf('.');
        if (i < 0 || i > j) {
            String msg = fmt("Cannot find \".\" in attr command \"%s\".", line);
            throw new InputOutputException(msg);
        }
        sad.nodeName = getWord(line, 4, i);
        sad.attribName = getWord(line, i + 1, j);
        if (sad.nodeName.isEmpty()) {
            String msg = fmt("Node name of the attr command \"%s\" is empty.", line);
            throw new InputOutputException(msg);
        }
        if (sad.attribName.isEmpty()) {
            String msg = fmt("Attribute name of the attr command \"%s\" is empty.", line);
            throw new InputOutputException(msg);
        }

        sad.value = getWord(line, j + 1, line.length());
        return sad;
    }
}
