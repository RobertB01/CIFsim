//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

/** Data about an SVG node that should be copied. */
public class SvgCopyModification extends SvgModification {
    /** Prefix to attach to ID labels in the copy. */
    String prefix = "";

    /** Suffix to attach to the ID labels in the copy. */
    String suffix = "";

    @Override
    public void apply(SvgVisualizer svgVis) {
        SvgUtils.copy(svgVis.getDocument(), nodeName, prefix, suffix, svgPath);
    }

    /**
     * Decode a node copy operation line, if it contains one.
     *
     * <p>
     * The generic form of the line is {@code copy [orig-node] , [opt-prefix] , [opt-suffix]}.
     * </p>
     *
     * @param line Line to decode.
     * @param svgPath The absolute or relative local file system path to the SVG file.
     * @return The decoded copy operation if parsed correctly, else {@code null}.
     */
    public static SvgCopyModification decode(String line, String svgPath) {
        if (!line.startsWith("copy")) {
            return null;
        }

        int i = 4; // Start of the node name to search.
        int j = line.indexOf(',', i); // First separator.
        if (j < 0) {
            String msg = fmt("Cannot find the first \",\" in copy command \"%s\".", line);
            throw new InputOutputException(msg);
        }

        int k = line.indexOf(',', j + 1); // Second separator.
        if (k < 0) {
            String msg = fmt("Cannot find the second \",\" in copy command \"%s\".", line);
            throw new InputOutputException(msg);
        }

        SvgCopyModification scm = new SvgCopyModification();
        scm.svgPath = svgPath;
        scm.nodeName = getWord(line, i, j);
        if (scm.nodeName == null) {
            String msg = fmt("Node name of the copy command \"%s\" is empty.", line);
            throw new InputOutputException(msg);
        }
        scm.prefix = getWord(line, j + 1, k);
        scm.suffix = getWord(line, k + 1, line.length());
        return scm;
    }
}
