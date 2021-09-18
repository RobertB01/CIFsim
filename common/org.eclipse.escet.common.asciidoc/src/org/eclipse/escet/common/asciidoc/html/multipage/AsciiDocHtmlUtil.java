//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.asciidoc.html.multipage;

import java.nio.file.Path;
import java.util.Objects;

/** AsciiDoc HTML utility methods. */
public class AsciiDocHtmlUtil {
    /** Constructor for the {@link AsciiDocHtmlUtil} class. */
    private AsciiDocHtmlUtil() {
        // Static class.
    }

    /**
     * Get an 'href' value from a given source file to (a section in) another source file:
     * <ul>
     * <li>If no section id is given, a reference to the entire file is returned.</li>
     * <li>If the section in the other source file is the first section, a reference to the entire file is
     * returned.</li>
     * <li>Otherwise, a reference to the section within the file is returned.</li>
     * </ul>
     *
     * @param fromSourceFile The source file from which the 'href' is referenced.
     * @param toSourceFile The other source file to which the 'href' should point.
     * @param id The id of the section in the other source file to which to refer, or {@code null} to refer to the
     *     entire file.
     * @return The 'href' value.
     */
    static String getFileOrSectionHref(AsciiDocSourceFile fromSourceFile, AsciiDocSourceFile toSourceFile, String id) {
        Path relPath = fromSourceFile.absPath.getParent().relativize(toSourceFile.absPath);
        relPath = AsciiDocMultiPageHtmlSplitter.sourcePathToOutputPath(relPath);
        String href = relPath.toString().replace('\\', '/');
        if (id != null) {
            String firstId = toSourceFile.ids.isEmpty() ? null : toSourceFile.ids.iterator().next();
            if (!Objects.equals(id, firstId)) { // Add section id if not the page title id.
                href += "#" + id;
            }
        }
        return href;
    }
}
