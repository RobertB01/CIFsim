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

import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.single;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/** AsciiDoc-generated single-page HTML adaptor. */
public class AsciiDocHtmlAdaptor {
    /** Constructor for the {@link AsciiDocHtmlAdaptor} class. */
    private AsciiDocHtmlAdaptor() {
        // Static class.
    }

    /**
     * Adapt single AsciiDoc-generated HTML file to match an AsciiDoc source file.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     * @param sourceFiles All AsciiDoc source files.
     * @param sourceRootPath The absolute path to the root directory that contains all the source files, and includes
     *     the root 'index.asciidoc' file.
     * @param htmlType The HTML type.
     */
    static void adaptGeneratedHtmlForSourceFile(Document doc, AsciiDocSourceFile sourceFile,
            List<AsciiDocSourceFile> sourceFiles, Path sourceRootPath, HtmlType htmlType)
    {
        // Adapt page and TOC titles.
        String docOriginalTitle = adaptPageAndTocTitles(doc, sourceFile, htmlType);

        // Move title/copyright/version from HTML body to footer. Not needed for root index file.
        moveFromHeaderToFooter(doc, sourceFile);

        // Partition HTML file 'content' for the AsciiDoc source files.
        // We do this again for every source file, as each source file has a clone of the original HTML document.
        AsciiDocHtmlAnalyzer.partitionContent(doc, sourceFiles);

        // Remove all content that should not be on this page.
        removeNonPageContent(doc, sourceFile, sourceFiles);

        // Remove empty paragraphs and sections.
        removeEmptyParagraphsAndSections(doc);

        // Normalize content headers. Not needed for root index file.
        normalizeContentHeaders(doc, sourceFile);

        // Highlight current page in TOC. This must be done after partitioning, but before updating TOC entry links.
        if (htmlType == HtmlType.WEBSITE) {
            highlightCurrentPageInToc(doc, sourceFile);
        }

        // Update references. This must be done after partitioning.
        updateReferences(doc, sourceFile, sourceFiles, sourceRootPath);

        // Add home page (root index file) to TOC.
        if (htmlType == HtmlType.WEBSITE) {
            addHomePageToToc(doc, sourceFile, sourceFiles);
        }

        // Add breadcrumbs. This must be done after partitioning. Not added for the root index file.
        addBreadcrumbs(doc, sourceFile, docOriginalTitle);

        // Add link to single-page HTML version.
        if (htmlType == HtmlType.WEBSITE) {
            addLinkToSinglePageHtmlVersion(doc, sourceFile);
        }
    }

    /**
     * Adapt page and TOC titles.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     * @param htmlType The HTML type.
     * @return The original HTML page title.
     */
    private static String adaptPageAndTocTitles(Document doc, AsciiDocSourceFile sourceFile, HtmlType htmlType) {
        // Adapt HTML page title.
        String docOriginalTitle = doc.title();
        doc.title(sourceFile.title + " | " + docOriginalTitle);

        // Adapt TOC title.
        if (htmlType == HtmlType.WEBSITE) {
            Element elemTocTitle = single(doc.select("div#toctitle"));
            elemTocTitle.text(docOriginalTitle);
        }

        // Return the original document title.
        return docOriginalTitle;
    }

    /**
     * Move title/copyright/version from HTML body to footer.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     */
    private static void moveFromHeaderToFooter(Document doc, AsciiDocSourceFile sourceFile) {
        // Skip root index file.
        if (sourceFile.isRootIndexFile) {
            return;
        }

        // Prepare footer.
        Element elemBodyFooterText = single(doc.select("#footer-text"));
        elemBodyFooterText.prependElement("br");

        // Move copyright/version.
        Element elemBodyCopyrightVersion = single(doc.select("#header div.details"));
        elemBodyCopyrightVersion.remove();
        Assert.check(elemBodyCopyrightVersion.children().size() == 3, elemBodyCopyrightVersion.toString());
        Elements elemBodyCopyrightVersionSpans = elemBodyCopyrightVersion.children().select("span");
        Assert.check(elemBodyCopyrightVersionSpans.size() == 2, elemBodyCopyrightVersionSpans.toString());
        elemBodyCopyrightVersionSpans.removeAttr("id");
        elemBodyCopyrightVersionSpans.removeAttr("class");
        for (Element elem: Lists.reverse(elemBodyCopyrightVersionSpans)) {
            elemBodyFooterText.prependChild(elem);
            elemBodyFooterText.prepend(" | ");
        }

        // Move title.
        Element elemBodyTitle = single(doc.select("#header h1"));
        Assert.check(elemBodyTitle.children().isEmpty(), elemBodyTitle.toString());
        elemBodyTitle.tagName("span");
        elemBodyFooterText.prependChild(elemBodyTitle);
    }

    /**
     * Remove all content that should not be on this page.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     * @param sourceFiles All AsciiDoc source files.
     */
    private static void removeNonPageContent(Document doc, AsciiDocSourceFile sourceFile,
            List<AsciiDocSourceFile> sourceFiles)
    {
        // Remove all content outside the source file content (outside the page). Not needed for root index file.
        Element elemContent = single(doc.select("#content"));
        if (!sourceFile.isRootIndexFile) {
            // Remove all content.
            elemContent.children().remove();

            // Re-add content for this source file.
            for (Node node: sourceFile.nodes) {
                elemContent.appendChild(node);
            }
        }

        // Remove content from other source files. Removes sub-pages.
        for (AsciiDocSourceFile otherSourceFile: sourceFiles) {
            if (otherSourceFile != sourceFile && !otherSourceFile.isRootIndexFile) {
                for (Node node: otherSourceFile.nodes) {
                    node.remove();
                }
            }
        }
    }

    /**
     * Remove empty paragraphs and sections.
     *
     * @param doc The HTML document to modify in-place.
     */
    private static void removeEmptyParagraphsAndSections(Document doc) {
        // Remove empty paragraphs.
        for (Element pElem: doc.select("p")) {
            if (pElem.attributes().size() == 0 && hasNoContent(pElem.childNodes())) {
                pElem.remove();
            }
        }

        for (Element paragraphDivElem: doc.select("div.paragraph")) {
            if (hasNoContent(paragraphDivElem.childNodes())) {
                paragraphDivElem.remove();
            }
        }

        // Remove empty sections.
        Element elemContent = single(doc.select("#content"));
        for (int i = 99; i >= 0; i--) { // Start with most deeply nested sections first.
            for (Element sectElem: elemContent.select("div.sect" + Integer.toString(i))) {
                if (hasNoContent(sectElem.childNodes())) {
                    // Completely empty section.
                    sectElem.remove();
                } else if (sectElem.children().size() == 1) {
                    Element sectChildElem = sectElem.child(0);
                    List<Node> sectChildNodes = copy(sectElem.childNodes());
                    sectChildNodes.remove(sectChildElem);
                    if (sectChildElem.tagName().matches("h\\d+") && hasNoContent(sectChildNodes)) {
                        // Section with only a wrapper header name (all actual content is on other pages).
                        sectElem.remove();
                    }
                }
            }
        }
    }

    /**
     * Normalize content headers.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     */
    private static void normalizeContentHeaders(Document doc, AsciiDocSourceFile sourceFile) {
        // Skip root index file.
        if (sourceFile.isRootIndexFile) {
            return;
        }

        // Find minimum header number.
        Element elemContent = single(doc.select("#content"));
        Pattern headerPattern = Pattern.compile("h(\\d+)");
        int minHeaderNr = Integer.MAX_VALUE;
        for (Element elem: elemContent.getAllElements()) {
            Matcher matcher = headerPattern.matcher(elem.tagName());
            if (matcher.matches()) {
                int headerNr = Integer.parseInt(matcher.group(1), 10);
                minHeaderNr = Math.min(headerNr, minHeaderNr);
            }
        }
        Assert.check(minHeaderNr > 0, String.valueOf(minHeaderNr));
        Assert.check(minHeaderNr < Integer.MAX_VALUE, String.valueOf(minHeaderNr));

        // Normalize header numbers to ensure minimum header number is '2'.
        for (Element elem: elemContent.getAllElements()) {
            Matcher matcher = headerPattern.matcher(elem.tagName());
            if (matcher.matches()) {
                int headerNr = Integer.parseInt(matcher.group(1), 10);
                int newHeaderNr = headerNr - minHeaderNr + 2;
                Assert.check(newHeaderNr <= 6, String.valueOf(newHeaderNr)); // Only h1-h6 are defined in HTML.
                elem.tagName("h" + newHeaderNr);
            }
        }
    }

    /**
     * Highlight current page in TOC. This must be done after partitioning, but before updating TOC entry links.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     */
    private static void highlightCurrentPageInToc(Document doc, AsciiDocSourceFile sourceFile) {
        String curPageHref = "#" + sourceFile.sourceId;
        List<Element> tocLinkElems = doc.select("#toc a");
        int tocLinkCurPageCount = 0;
        for (Element tocLinkElem: tocLinkElems) {
            if (curPageHref.equals(tocLinkElem.attr("href"))) {
                tocLinkElem.addClass("toc-cur-page");
                tocLinkCurPageCount++;
            }
        }
        if (sourceFile.isRootIndexFile) {
            Assert.check(tocLinkCurPageCount == 0, String.valueOf(tocLinkCurPageCount));
        } else {
            // If the TOC level is too limited, the page will not be in the TOC, and this will fail (count is zero).
            Assert.check(tocLinkCurPageCount == 1, String.valueOf(tocLinkCurPageCount));
        }
    }

    /**
     * Update references. This must be done after partitioning.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     * @param sourceFiles All AsciiDoc source files.
     * @param sourceRootPath The absolute path to the root directory that contains all the source files, and includes
     *     the root 'index.asciidoc' file.
     */
    private static void updateReferences(Document doc, AsciiDocSourceFile sourceFile,
            List<AsciiDocSourceFile> sourceFiles, Path sourceRootPath)
    {
        updateReferences(doc, sourceFile, sourceFiles, sourceRootPath, "a", "href", true, true);
        updateReferences(doc, sourceFile, sourceFiles, sourceRootPath, "img", "src", false, false);
        updateReferences(doc, sourceFile, sourceFiles, sourceRootPath, "link", "href", false, false);
    }

    /**
     * Update references. This must be done after partitioning.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     * @param sourceFiles All AsciiDoc source files.
     * @param sourceRootPath The absolute path to the root directory that contains all the source files, and includes
     *     the root 'index.asciidoc' file.
     * @param tagName The tag name of elements for which to update references.
     * @param attrName The attribute name that contains the reference.
     * @param allowEmptyRefIfNoChildren Whether to allow empty references (attribute values) if the element has no child
     *     nodes ({@code true}), or disallow empty references altogether ({@code false}).
     * @param allowSectionRefs Whether to allow section references ({@code #...}) as references ({@code true}) or not
     *     ({@code false}).
     */
    private static void updateReferences(Document doc, AsciiDocSourceFile sourceFile,
            List<AsciiDocSourceFile> sourceFiles, Path sourceRootPath, String tagName, String attrName,
            boolean allowEmptyRefIfNoChildren, boolean allowSectionRefs)
    {
        ELEMS_LOOP:
        for (Element elem: doc.select(tagName)) {
            // Get attribute value.
            String ref = elem.attr(attrName);
            if (ref == null || ref.isBlank()) {
                if (allowEmptyRefIfNoChildren) {
                    // Occurs for 'a.href' for bibliography entries.
                    // But then they have no child nodes, and are thus not clickable.
                    Assert.check(elem.childNodeSize() == 0, String.valueOf(elem.childNodeSize()));
                    continue;
                } else {
                    throw new RuntimeException(
                            fmt("Undefined '%s.%s': %s", tagName, attrName, sourceFile.relPath.toString()));
                }
            }

            // Handle '#' references, originally pointing to within the single AsciiDoc-generated HTML file.
            if (allowSectionRefs && ref.startsWith("#")) {
                String id = ref.substring(1);
                if (sourceFile.ids.contains(id)) {
                    continue; // Still within this HTML file.
                }
                for (AsciiDocSourceFile otherSourceFile: sourceFiles) {
                    if (otherSourceFile.ids.contains(id)) {
                        String newHref = getFileOrSectionHref(sourceFile, otherSourceFile, id);
                        elem.attr("href", newHref);
                        continue ELEMS_LOOP;
                    }
                }
                Assert.fail(fmt("No source file found that defines '%s.%s' id: %s", tagName, attrName, id));
            }

            // Get referenced URI. Skip 'http' and 'https' references etc.
            URI uri = URI.create(ref);
            String uriScheme = uri.getScheme();
            if ("http".equals(uriScheme) || "https".equals(uriScheme)) {
                continue;
            }

            // Handle relative paths.
            Assert.check(uriScheme == null, uriScheme);
            Assert.check(uri.getUserInfo() == null, uri.getUserInfo());
            Assert.check(uri.getHost() == null, uri.getHost());
            Assert.check(uri.getPort() == -1, String.valueOf(uri.getPort()));
            Assert.check(uri.getAuthority() == null, uri.getAuthority());
            Assert.check(uri.getQuery() == null, uri.getQuery());
            Assert.check(uri.getFragment() == null, uri.getFragment());
            Assert.notNull(uri.getPath());
            Assert.check(ref.equals(uri.getPath()), ref + " / " + uri.getPath());
            String hrefAbsTarget = org.eclipse.escet.common.app.framework.Paths.resolve(ref, sourceRootPath.toString());
            String rootPathForNewRelHref = sourceFile.absPath.getParent().toString();
            String newRelHref = org.eclipse.escet.common.app.framework.Paths.getRelativePath(hrefAbsTarget,
                    rootPathForNewRelHref);
            Assert.check(!newRelHref.contains("\\"), newRelHref);
            elem.attr(attrName, newRelHref);
        }
    }

    /**
     * Add home page (root index file) to TOC.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     * @param sourceFiles All AsciiDoc source files.
     */
    private static void addHomePageToToc(Document doc, AsciiDocSourceFile sourceFile,
            List<AsciiDocSourceFile> sourceFiles)
    {
        AsciiDocSourceFile rootSourceFile = single(
                sourceFiles.stream().filter(s -> s.isRootIndexFile).collect(Collectors.toList()));
        Element elemTocSectLevel1 = single(doc.select("#toc ul.sectlevel1"));
        Element elemTocHomeLi = elemTocSectLevel1.prependElement("li");
        Element elemTocHomeA = elemTocHomeLi.prependElement("a");
        elemTocHomeA.attr("href", getFileOrSectionHref(sourceFile, rootSourceFile, null));
        if (sourceFile.isRootIndexFile) {
            elemTocHomeA.addClass("toc-cur-page");
        }
        elemTocHomeA.appendText(rootSourceFile.title);
    }

    /**
     * Add breadcrumbs. This must be done after partitioning.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     * @param docOriginalTitle The original HTML page title.
     */
    private static void addBreadcrumbs(Document doc, AsciiDocSourceFile sourceFile, String docOriginalTitle) {
        // Skip root index file.
        if (sourceFile.isRootIndexFile) {
            return;
        }

        // Prepare breadcrumbs element.
        Element elemContent = single(doc.select("#content"));
        Element elemBreadcrumbsDiv = elemContent.prependElement("div");
        elemBreadcrumbsDiv.attr("id", "breadcrumbs");

        // Add breadcrumbs.
        for (AsciiDocSourceFile breadcrumb: sourceFile.breadcrumbs) {
            if (elemBreadcrumbsDiv.childNodeSize() > 0) {
                elemBreadcrumbsDiv.appendText(" > ");
            }
            boolean isSelfBreadcrumb = sourceFile == breadcrumb;
            Element elemBreadcrumb = elemBreadcrumbsDiv.appendElement(isSelfBreadcrumb ? "span" : "a");
            elemBreadcrumb.addClass("breadcrumb");
            if (!isSelfBreadcrumb) {
                elemBreadcrumb.attr("href", getFileOrSectionHref(sourceFile, breadcrumb, null));
            }
            elemBreadcrumb.text(breadcrumb.isRootIndexFile ? docOriginalTitle : breadcrumb.title);
        }
    }

    /**
     * Add link to single-page HTML version.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     */
    private static void addLinkToSinglePageHtmlVersion(Document doc, AsciiDocSourceFile sourceFile) {
        if (sourceFile.isRootIndexFile) {
            Element elemPdfTip = single(doc.select("div.tip td.content:contains(as a PDF as well)"));
            elemPdfTip.appendText("Or use the ");
            Element elemPdfTipA = elemPdfTip.appendElement("a");
            elemPdfTipA.attr("href", "index-single-page.html");
            elemPdfTipA.text("single-page HTML");
            elemPdfTip.appendText(" version.");
        }
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
     * @param sourceFile The source file from which the 'href' is referenced.
     * @param otherSourceFile The other source file to which the 'href' should point.
     * @param id The id of the section in the other source file to which to refer, or {@code null} to refer to the
     *     entire file.
     * @return The 'href' value.
     */
    private static String getFileOrSectionHref(AsciiDocSourceFile sourceFile, AsciiDocSourceFile otherSourceFile,
            String id)
    {
        Path relPath = sourceFile.absPath.getParent().relativize(otherSourceFile.absPath);
        relPath = AsciiDocMultiPageHtmlSplitter.sourcePathToOutputPath(relPath);
        String href = relPath.toString().replace('\\', '/');
        if (id != null) {
            String firstId = otherSourceFile.ids.isEmpty() ? null : otherSourceFile.ids.iterator().next();
            if (!Objects.equals(id, firstId)) { // Add section id if not the page title id.
                href += "#" + id;
            }
        }
        return href;
    }

    /**
     * Do the given nodes contain no content? That is, are they all {@link TextNode#isBlank blank} {@link TextNode}s?
     *
     * @param nodes The nodes to check.
     * @return {@code true} if all the nodes have no content, {@code false} otherwise.
     */
    private static boolean hasNoContent(List<Node> nodes) {
        for (Node node: nodes) {
            if (node instanceof Element) {
                return false;
            } else if (node instanceof TextNode) {
                if (!((TextNode)node).isBlank()) {
                    return false;
                }
            } else {
                Assert.fail("Unexpected node: " + node.getClass().getName());
            }
        }
        return true;
    }
}
