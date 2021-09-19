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

import org.eclipse.escet.common.java.Assert;

/** AsciiDoc multi-page HTML utility methods. */
class AsciiDocHtmlUtil {
    /** Constructor for the {@link AsciiDocHtmlUtil} class. */
    private AsciiDocHtmlUtil() {
        // Static class.
    }

    /**
     * Get an 'href' value from a given multi-page HTML page to (a section in) another page:
     * <ul>
     * <li>If no section id is given, a reference to the entire file is returned.</li>
     * <li>If the section in the other page is the first section, a reference to the entire file is returned.</li>
     * <li>Otherwise, a reference to the section within the file is returned.</li>
     * </ul>
     *
     * @param fromPage The page from which the 'href' is referenced.
     * @param toPage The other page to which the 'href' should point.
     * @param singlePageId The AsciiDoc-generated single-page HTML id of the section in the other page to which to
     *     refer, or {@code null} to refer to the entire file.
     * @return The 'href' value.
     */
    static String getFileOrSectionHref(AsciiDocHtmlPage fromPage, AsciiDocHtmlPage toPage, String singlePageId) {
        // Get file reference.
        String href;
        if (fromPage == toPage) {
            href = "";
        } else {
            Path relPath = fromPage.sourceFile.absPath.getParent().relativize(toPage.sourceFile.absPath);
            relPath = AsciiDocMultiPageHtmlSplitter.sourcePathToOutputPath(relPath);
            href = relPath.toString().replace('\\', '/');
        }

        // Add section reference.
        if (singlePageId != null) {
            String multiPageId = toPage.sectionIdRenames.getOrDefault(singlePageId, singlePageId);
            Assert.check(toPage.multiPageIds.contains(multiPageId), multiPageId);
            String firstId = toPage.multiPageIds.isEmpty() ? null : toPage.multiPageIds.iterator().next();
            if (!Objects.equals(multiPageId, firstId)) { // Add section id if not the page title id.
                href += "#" + multiPageId;
            }
        }

        // Return href.
        if (href.isEmpty()) {
            href = "#";
        }
        return href;
    }
}
