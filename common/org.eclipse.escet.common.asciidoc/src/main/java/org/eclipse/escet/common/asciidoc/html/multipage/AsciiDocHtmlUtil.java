//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Verify;

/** AsciiDoc multi-page HTML utility methods. */
class AsciiDocHtmlUtil {
    /** Constructor for the {@link AsciiDocHtmlUtil} class. */
    private AsciiDocHtmlUtil() {
        // Static class.
    }

    /**
     * Get an 'href' value from a given multi-page HTML page to (a section in) a(nother) page:
     * <ul>
     * <li>If no section id is given, a reference to the entire file is returned.</li>
     * <li>If the section in the 'to' page is the first section, a reference to the entire file is returned.</li>
     * <li>Otherwise, a reference to the section within the file is returned.</li>
     * </ul>
     *
     * <p>
     * If it concerns a reference to the page itself and it is not a reference to a section on the page, {@code #} is
     * returned to refer to the current page.
     * </p>
     *
     * @param fromPage The page from which the 'href' is referenced.
     * @param toPage The page to which the 'href' should point.
     * @param singlePageId The AsciiDoc-generated single-page HTML id of the section in the 'to' page to which to refer,
     *     or {@code null} to refer to the entire file.
     * @return The 'href' value.
     */
    static String getFileOrSectionHref(AsciiDocHtmlPage fromPage, AsciiDocHtmlPage toPage, String singlePageId) {
        // Get file reference.
        String href;
        if (fromPage == toPage) {
            href = "";
        } else {
            Path relPath = fromPage.sourceFile.absPath.getParent().relativize(toPage.sourceFile.absPath);
            relPath = sourcePathToOutputPath(relPath);
            href = relPath.toString().replace('\\', '/');
        }

        // Add section reference.
        if (singlePageId != null) {
            String multiPageId = toPage.sectionIdRenames.getOrDefault(singlePageId, singlePageId);
            Verify.verify(toPage.multiPageIds.contains(multiPageId), multiPageId);
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

    /**
     * Converts an AsciiDoc source file path into a generated output HTML file path.
     *
     * @param sourcePath The AsciiDoc source file path.
     * @return The generated output HTML file path.
     */
    static Path sourcePathToOutputPath(Path sourcePath) {
        String fileName = sourcePath.getFileName().toString();
        Verify.verify(fileName.endsWith(".asciidoc"), fileName);
        fileName = fileName.substring(0, fileName.length() - ".asciidoc".length());
        fileName += ".html";
        return sourcePath.resolveSibling(fileName);
    }

    /**
     * Returns the single element from the given elements.
     *
     * @param elements The list for which to get the single element.
     * @return The single element.
     * @throws IllegalArgumentException If the list does not have exactly one element.
     */
    static Element single(Elements elements) {
        Verify.verify(elements.size() == 1, elements.toString());
        return elements.get(0);
    }
}
