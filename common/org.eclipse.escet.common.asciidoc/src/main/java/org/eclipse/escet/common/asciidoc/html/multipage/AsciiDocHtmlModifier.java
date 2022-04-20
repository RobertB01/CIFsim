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

import static org.eclipse.escet.common.asciidoc.html.multipage.AsciiDocHtmlUtil.single;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.google.common.base.Verify;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

/** AsciiDoc-generated single-page HTML modifier. */
class AsciiDocHtmlModifier {
    /** Whether to print debug output. */
    private static final boolean DEBUG = false;

    /** Constructor for the {@link AsciiDocHtmlModifier} class. */
    private AsciiDocHtmlModifier() {
        // Static class.
    }

    /**
     * Modify the AsciiDoc-generated single-page HTML file for each multi-page HTML page, and write the modified pages
     * to disk.
     *
     * @param singlePageDoc The AsciiDoc-generated single-page HTML document.
     * @param htmlPages The multi-page HTML pages.
     * @param sourceRootPath The absolute path to the root directory that contains all the source files, and includes
     *     the root AsciiDoc file.
     * @param outputRootPath The path to the directory in which to write output.
     * @param htmlType The HTML type.
     * @param parentWebsiteName The name of the parent website to link to, if {@code htmlType} is
     *     {@link HtmlType#WEBSITE}, {@code null} otherwise.
     * @param parentWebsiteLink The relative path of the parent website to link to, if {@code htmlType} is
     *     {@link HtmlType#WEBSITE}, {@code null} otherwise.
     * @param logger The logger to use.
     */
    static void generateAndWriteModifiedPages(Document singlePageDoc, AsciiDocHtmlPages htmlPages, Path sourceRootPath,
            Path outputRootPath, HtmlType htmlType, String parentWebsiteName, String parentWebsiteLink,
            Consumer<String> logger)
    {
        // Determine new section ids.
        determineNewSectionIds(htmlPages);

        // Modify the pages, per page.
        for (AsciiDocHtmlPage page: htmlPages.pages) {
            try {
                // Debug output.
                if (DEBUG) {
                    logger.accept("Modifying page: " + page.sourceFile.relPath);
                }

                // Clone the original single-page HTML file for this page.
                page.doc = singlePageDoc.clone();

                // Determine multi-page HTML nodes for each page, for the cloned document of this page. Do this before
                // any modifications to the document itself.
                for (AsciiDocHtmlPage somePage: htmlPages.pages) {
                    somePage.multiPageNodes = determineClonedNodes(singlePageDoc, page.doc, somePage.singlePageNodes);
                }

                // Modify page title.
                String docOriginalTitle = modifyPageTitle(page);

                // Modify TOC title.
                if (htmlType == HtmlType.WEBSITE) {
                    modifyTocTitle(page.doc, docOriginalTitle);
                }

                // Add parent website link to TOC, for documentation on the website to link back to that website.
                if (htmlType == HtmlType.WEBSITE) {
                    addParentWebsiteLinkToToc(page.doc, parentWebsiteName, parentWebsiteLink);
                }

                // Move title/copyright/version from HTML body to footer.
                if (page != htmlPages.homePage) {
                    moveFromHeaderToFooter(page.doc);
                }

                // Remove all content that should not be on this page.
                removeNonPageContent(page, htmlPages);
                for (AsciiDocHtmlPage somePage: htmlPages.pages) {
                    somePage.multiPageNodes = null;
                }

                // Remove empty paragraphs and sections.
                removeEmptyParagraphsAndSections(page.doc);

                // Normalize content headers.
                if (page != htmlPages.homePage) {
                    normalizeContentHeaders(page.doc);
                }

                // Highlight current page in TOC. This must be done before updating TOC entry links.
                if (htmlType == HtmlType.WEBSITE) {
                    highlightCurrentPageInToc(page, htmlPages);
                }

                // Renamed defined section ids.
                renameDefinedSectionIds(page);

                // Rename section ids in TOC.
                renameSectionIdsInToc(page, htmlPages.toc);

                // Update references.
                updateReferences(page, htmlPages, sourceRootPath);

                // Add home page (root AsciiDoc file) to TOC.
                if (htmlType == HtmlType.WEBSITE) {
                    addHomePageToToc(page, htmlPages.homePage);
                }

                // Add breadcrumbs. Not added for Eclipse help, as Eclipse help already has breadcrumbs built-in. Not
                // added for the home page as it has no ancestors.
                if (htmlType != HtmlType.ECLIPSE_HELP && page != htmlPages.homePage) {
                    addBreadcrumbs(page, htmlPages.homePage, docOriginalTitle);
                }

                // Add link to single-page HTML version.
                if (htmlType == HtmlType.WEBSITE && page == htmlPages.homePage) {
                    addLinkToSinglePageHtmlVersion(page);
                }

                // Write modified page to disk.
                Path sourcePath = outputRootPath.resolve(page.sourceFile.relPath);
                Path outputPath = AsciiDocHtmlUtil.sourcePathToOutputPath(sourcePath);
                Files.createDirectories(outputPath.getParent());
                Files.writeString(outputPath, page.doc.outerHtml(), StandardCharsets.UTF_8);
                page.doc = null;
            } catch (Throwable e) {
                throw new RuntimeException(
                        "Failed to modify HTML document for AsciiDoc multi-html page: " + page.sourceFile.relPath, e);
            }
        }
    }

    /**
     * Determines the cloned nodes of the given cloned multi-page document, matching the single-page nodes from the
     * single-page document.
     *
     * @param singlePageDoc The single-page document.
     * @param multiPageDoc The cloned multi-page document, that is a clone of the single-page document, without
     *     modifications.
     * @param singlePageNodes The single-page nodes.
     * @return The nodes from the cloned multi-page document.
     */
    private static List<Node> determineClonedNodes(Document singlePageDoc, Document multiPageDoc,
            List<Node> singlePageNodes)
    {
        // Sanity check.
        Verify.verify(singlePageDoc != multiPageDoc);

        // Determine the cloned nodes, per node.
        List<Node> multiPageNodes = new ArrayList<>(singlePageNodes.size());
        for (Node singlePageNode: singlePageNodes) {
            // Get path.
            LinkedList<Node> path = new LinkedList<>();
            Node curNode = singlePageNode;
            while (!(curNode instanceof Document)) {
                if (curNode == null) {
                    throw new AssertionError("No document for node.");
                }
                path.addFirst(curNode);
                curNode = curNode.parentNode();
            }

            // Re-play path on clone.
            curNode = multiPageDoc;
            for (Node pathNode: path) {
                int idx = pathNode.siblingIndex();
                curNode = curNode.childNode(idx);
            }

            // Sanity checks.
            if (singlePageNode.getClass() != curNode.getClass()) {
                throw new RuntimeException(singlePageNode.getClass() + " / " + curNode.getClass());
            }
            if (singlePageNode instanceof Element) {
                Element singlePageElem = (Element)singlePageNode;
                Element curElem = (Element)curNode;
                if (!singlePageElem.tagName().equals(curElem.tagName())) {
                    throw new RuntimeException(singlePageElem.tagName() + " / " + curElem.tagName());
                }
            }

            // Add node.
            multiPageNodes.add(curNode);
        }
        return multiPageNodes;
    }

    /**
     * Determine new section ids, based on the section titles.
     *
     * <p>
     * Examples of sub-optimal section ids in AsciiDoc-generated single-page HTML files:
     * <ul>
     * <li>For multiple sections that are named 'Exercises', and with a configured prefix 'tut', the section ids are
     * named 'tut-exercises', 'tut-exercises-2', etc. If they are from different source files, it is not necessary to
     * postfix them with numbers to ensure uniqueness per multi-page HTML file.</li>
     * <li>To ensure globally unique ids, sections are given hierarchical ids when specifying them manually. E.g.
     * 'lang-tut-basics-automata-events'. For multi-page HTML files, these prefixes are not necessary.</li>
     * </ul>
     * </p>
     *
     * @param htmlPages The multi-page HTML pages.
     */
    private static void determineNewSectionIds(AsciiDocHtmlPages htmlPages) {
        // Determine section id renames, for the entire TOC (all pages).
        Map<String, String> renames = new LinkedHashMap<>();
        fillSectionIdRenameMap(htmlPages.toc, renames);

        // We need unique ids, per page.
        for (AsciiDocHtmlPage htmlPage: htmlPages.pages) {
            // Get page renames.
            Map<String, String> pageRenames = new LinkedHashMap<>(renames);
            pageRenames.keySet().retainAll(htmlPage.singlePageIds);

            // Ensure no duplicate new ids for this page.
            boolean duplicateFound;
            do {
                duplicateFound = false;
                ListMultimap<String, String> inversePageRenames = Multimaps.invertFrom(Multimaps.forMap(pageRenames),
                        ArrayListMultimap.create());
                for (Entry<String, Collection<String>> entry: inversePageRenames.asMap().entrySet()) {
                    Collection<String> oldIds = entry.getValue();
                    if (oldIds.size() > 1) {
                        // Duplicate new id, as new id is used for multiple old ids.
                        duplicateFound = true;
                        String newId = entry.getKey();
                        int nr = 0;
                        for (String oldId: oldIds) {
                            nr++;
                            pageRenames.put(oldId, newId + Integer.toString(nr));
                        }
                        break;
                    }
                }
            } while (duplicateFound);

            // Check no overlap between remaining existing ids and new ids for this page.
            Set<String> remainingExistingIds = Sets.difference(htmlPage.singlePageIds, pageRenames.keySet());
            Set<String> pageNewIds = pageRenames.values().stream()
                    .collect(Collectors.toCollection(() -> new LinkedHashSet<>()));
            Set<String> overlapRemainingVsNewIds = Sets.intersection(remainingExistingIds, pageNewIds);
            Verify.verify(overlapRemainingVsNewIds.isEmpty(),
                    "Overlap between new section IDs and remaining non-section IDs: " + overlapRemainingVsNewIds
                            + " on page: " + htmlPage.sourceFile.relPath);

            // Set the new page ids.
            htmlPage.sectionIdRenames = pageRenames;
            htmlPage.multiPageIds = htmlPage.singlePageIds.stream().map(id -> pageRenames.getOrDefault(id, id))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    /**
     * Fill the given section id rename mapping.
     *
     * @param tocEntry The TOC entry to process.
     * @param renames The mapping of old section ids to new section ids. Is modified in-place.
     */
    private static void fillSectionIdRenameMap(AsciiDocTocEntry tocEntry, Map<String, String> renames) {
        // Determine new id.
        String newId = tocEntry.title;
        newId = newId.strip();
        newId = newId.replaceAll("[^a-zA-Z0-9]", "-");
        newId = newId.toLowerCase(Locale.US);
        while (newId.contains("--")) {
            newId = newId.replace("--", "-");
        }
        while (newId.startsWith("-")) {
            newId = newId.substring(1);
        }
        while (newId.endsWith("-")) {
            newId = newId.substring(0, newId.length() - 1);
        }
        Verify.verify(!newId.isEmpty());

        // Store new id.
        String previous = renames.put(tocEntry.refId, newId);
        Verify.verify(previous == null);

        // Process children.
        for (AsciiDocTocEntry childEntry: tocEntry.children) {
            fillSectionIdRenameMap(childEntry, renames);
        }
    }

    /**
     * Modify page title.
     *
     * @param page The multi-page HTML page to modify in-place.
     * @return The original page title.
     */
    private static String modifyPageTitle(AsciiDocHtmlPage page) {
        String docOriginalTitle = page.doc.title();
        page.doc.title(page.sourceFile.title + " | " + docOriginalTitle);
        return docOriginalTitle;
    }

    /**
     * Modify TOC title.
     *
     * @param doc The HTML document to modify in-place.
     * @param docOriginalTitle The original page title.
     */
    private static void modifyTocTitle(Document doc, String docOriginalTitle) {
        Element elemTocTitle = single(doc.select("div#toctitle"));
        elemTocTitle.text(docOriginalTitle);
    }

    /**
     * Add website link to TOC.
     *
     * @param doc The HTML document to modify in-place.
     * @param parentWebsiteName The name of the website to link to.
     * @param parentWebsiteLink The relative path of the website to link to.
     */
    private static void addParentWebsiteLinkToToc(Document doc, String parentWebsiteName, String parentWebsiteLink) {
        Element elemTocDiv = single(doc.select("div#toc"));
        Element elemWebsiteLinkP = elemTocDiv.prependElement("p");
        elemWebsiteLinkP.addClass("website-link");
        elemWebsiteLinkP.appendText("Part of: ");
        Element elemWebsiteLinkA = elemWebsiteLinkP.appendElement("a");
        elemWebsiteLinkA.attr("href", parentWebsiteLink);
        elemWebsiteLinkA.text(parentWebsiteName);
    }

    /**
     * Move title/copyright/version from HTML body to footer.
     *
     * @param doc The HTML document to modify in-place.
     */
    private static void moveFromHeaderToFooter(Document doc) {
        // Prepare footer.
        Element elemBodyFooterText = single(doc.select("#footer-text"));
        elemBodyFooterText.prependElement("br");

        // Move copyright/version.
        Element elemBodyCopyrightVersion = single(doc.select("#header div.details"));
        elemBodyCopyrightVersion.remove();
        Verify.verify(elemBodyCopyrightVersion.children().size() == 3, elemBodyCopyrightVersion.toString());
        Elements elemBodyCopyrightVersionSpans = elemBodyCopyrightVersion.children().select("span");
        Verify.verify(elemBodyCopyrightVersionSpans.size() == 2, elemBodyCopyrightVersionSpans.toString());
        elemBodyCopyrightVersionSpans.removeAttr("id");
        elemBodyCopyrightVersionSpans.removeAttr("class");
        List<Element> elemBodyCopyrightVersionSpansReversed = new ArrayList<>(elemBodyCopyrightVersionSpans);
        Collections.reverse(elemBodyCopyrightVersionSpansReversed);
        for (Element elem: elemBodyCopyrightVersionSpansReversed) {
            elemBodyFooterText.prependChild(elem);
            elemBodyFooterText.prepend(" | ");
        }

        // Move title.
        Element elemBodyTitle = single(doc.select("#header h1"));
        Verify.verify(elemBodyTitle.children().isEmpty(), elemBodyTitle.toString());
        elemBodyTitle.tagName("span");
        elemBodyFooterText.prependChild(elemBodyTitle);
    }

    /**
     * Remove all content that should not be on this page.
     *
     * @param page The multi-page HTML page to modify in-place.
     * @param htmlPages The multi-page HTML pages.
     */
    private static void removeNonPageContent(AsciiDocHtmlPage page, AsciiDocHtmlPages htmlPages) {
        // Remove all content outside the page. Not needed for home page.
        if (page != htmlPages.homePage) {
            // Remove all content.
            Element elemContent = single(page.doc.select("#content"));
            elemContent.children().remove();

            // Re-add content for this source file.
            for (Node node: page.multiPageNodes) {
                elemContent.appendChild(node);
            }
        }

        // Remove content from other pages. Removes sub-pages.
        for (AsciiDocHtmlPage otherPage: htmlPages.pages) {
            if (otherPage != page && otherPage != htmlPages.homePage) {
                for (Node node: otherPage.multiPageNodes) {
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
        boolean modified;
        do {
            // Initialization.
            modified = false;

            // Remove empty paragraphs.
            for (Element pElem: doc.select("p")) {
                if (pElem.attributes().size() == 0 && haveNoContent(pElem.childNodes())) {
                    pElem.remove();
                    modified = true;
                }
            }

            for (Element paragraphDivElem: doc.select("div.paragraph")) {
                if (haveNoContent(paragraphDivElem.childNodes())) {
                    paragraphDivElem.remove();
                    modified = true;
                }
            }

            // Remove empty section bodies.
            for (Element sectionBodyDivElem: doc.select("div.sectionbody")) {
                if (haveNoContent(sectionBodyDivElem.childNodes())) {
                    sectionBodyDivElem.remove();
                    modified = true;
                }
            }

            // Remove empty sections.
            Element elemContent = single(doc.select("#content"));
            for (int i = 99; i >= 0; i--) { // Start with most deeply nested sections first.
                for (Element sectElem: elemContent.select("div.sect" + Integer.toString(i))) {
                    if (haveNoContent(sectElem.childNodes())) {
                        // Completely empty section.
                        sectElem.remove();
                        modified = true;
                    } else if (sectElem.children().size() == 1) {
                        Element sectChildElem = sectElem.child(0);
                        List<Node> sectChildNodes = new ArrayList<>(sectElem.childNodes());
                        sectChildNodes.remove(sectChildElem);
                        if (sectChildElem.tagName().matches("h\\d+") && haveNoContent(sectChildNodes)) {
                            // Section with only a wrapper header name (all actual content is on other pages).
                            sectElem.remove();
                            modified = true;
                        }
                    }
                }
            }
        } while (modified);
    }

    /**
     * Do the given nodes contain no content? That is, are they all {@link TextNode#isBlank blank} {@link TextNode}s?
     *
     * @param nodes The nodes to check.
     * @return {@code true} if all the nodes have no content, {@code false} otherwise.
     */
    private static boolean haveNoContent(List<Node> nodes) {
        for (Node node: nodes) {
            if (node instanceof Element) {
                return false;
            } else if (node instanceof TextNode) {
                if (!((TextNode)node).isBlank()) {
                    return false;
                }
            } else {
                throw new RuntimeException("Unexpected node: " + node.getClass().getName());
            }
        }
        return true;
    }

    /**
     * Normalize content headers.
     *
     * @param doc The HTML document to modify in-place.
     */
    private static void normalizeContentHeaders(Document doc) {
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
        Verify.verify(minHeaderNr > 0, Integer.toString(minHeaderNr));
        Verify.verify(minHeaderNr < Integer.MAX_VALUE, Integer.toString(minHeaderNr));

        // Normalize header numbers to ensure minimum header number is '2'.
        for (Element elem: elemContent.getAllElements()) {
            Matcher matcher = headerPattern.matcher(elem.tagName());
            if (matcher.matches()) {
                int headerNr = Integer.parseInt(matcher.group(1), 10);
                int newHeaderNr = headerNr - minHeaderNr + 2;
                Verify.verify(newHeaderNr <= 6, Integer.toString(newHeaderNr)); // Only h1-h6 are defined in HTML.
                elem.tagName("h" + newHeaderNr);
            }
        }
    }

    /**
     * Highlight current page in TOC.
     *
     * @param page The multi-page HTML page to modify in-place.
     * @param htmlPages The multi-page HTML pages.
     */
    private static void highlightCurrentPageInToc(AsciiDocHtmlPage page, AsciiDocHtmlPages htmlPages) {
        // Add extra class to current page in TOC.
        String curPageHref = "#" + page.sourceFile.sourceId; // Section id renaming has not yet been applied.
        List<Element> tocLinkElems = page.doc.select("#toc a");
        int tocLinkCurPageCount = 0;
        for (Element tocLinkElem: tocLinkElems) {
            if (curPageHref.equals(tocLinkElem.attr("href"))) {
                tocLinkElem.addClass("toc-cur-page");
                tocLinkCurPageCount++;
            }
        }

        // Sanity checks.
        if (htmlPages.homePage == page) {
            Verify.verify(tocLinkCurPageCount == 0);
        } else {
            // If the TOC level setting used to generate the single page HTML file is too limited, the page will not be
            // in the TOC, and this will fail (count is zero).
            Verify.verify(tocLinkCurPageCount == 1);
        }
    }

    /**
     * Rename defined section id.
     *
     * @param page The multi-page HTML page to modify in-place.
     */
    private static void renameDefinedSectionIds(AsciiDocHtmlPage page) {
        int i = 2;
        while (true) {
            Elements sectionHeaderElements = page.doc.select("#content h" + i);
            for (Element elem: sectionHeaderElements) {
                // Get current section id.
                String elemId = elem.attr("id");
                Verify.verifyNotNull(elemId, elem.tagName());

                // Get new section id.
                String newId = page.sectionIdRenames.get(elemId);
                Verify.verifyNotNull(newId, elemId);

                // Rename.
                elem.attr("id", newId);
            }
            if (sectionHeaderElements.isEmpty()) {
                break;
            }
            i++;
        }
    }

    /**
     * Rename section ids in TOC.
     *
     * @param page The multi-page HTML page to modify in-place.
     * @param tocEntry The TOC entry to modify in-place.
     */
    private static void renameSectionIdsInToc(AsciiDocHtmlPage page, AsciiDocTocEntry tocEntry) {
        // Rename this TOC entry.
        if (tocEntry.page == page && tocEntry.refId != null) {
            String newRefId = page.sectionIdRenames.get(tocEntry.refId);
            Verify.verifyNotNull(newRefId, tocEntry.refId);
            tocEntry.refId = newRefId;
            Verify.verify(tocEntry.page.multiPageIds.contains(newRefId), newRefId);
        }

        // Rename children.
        for (AsciiDocTocEntry childEntry: tocEntry.children) {
            renameSectionIdsInToc(page, childEntry);
        }
    }

    /**
     * Update references.
     *
     * @param page The multi-page HTML page to modify in-place.
     * @param htmlPages The multi-page HTML pages.
     * @param sourceRootPath The absolute path to the root directory that contains all the source files, and includes
     *     the root AsciiDoc file.
     */
    private static void updateReferences(AsciiDocHtmlPage page, AsciiDocHtmlPages htmlPages, Path sourceRootPath) {
        updateReferences(page, htmlPages, sourceRootPath, "a", "href", true, true);
        updateReferences(page, htmlPages, sourceRootPath, "img", "src", false, false);
        updateReferences(page, htmlPages, sourceRootPath, "link", "href", false, false);
    }

    /**
     * Update references. This must be done after partitioning.
     *
     * @param page The multi-page HTML page to modify in-place.
     * @param htmlPages The multi-page HTML pages.
     * @param sourceRootPath The absolute path to the root directory that contains all the source files, and includes
     *     the root AsciiDoc file.
     * @param tagName The tag name of elements for which to update references.
     * @param attrName The attribute name that contains the reference.
     * @param allowEmptyRefIfNoChildren Whether to allow empty references (attribute values) if the element has no child
     *     nodes ({@code true}), or disallow empty references altogether ({@code false}).
     * @param allowSectionRefs Whether to allow section references ({@code #...}) as references ({@code true}) or not
     *     ({@code false}).
     */
    private static void updateReferences(AsciiDocHtmlPage page, AsciiDocHtmlPages htmlPages, Path sourceRootPath,
            String tagName, String attrName, boolean allowEmptyRefIfNoChildren, boolean allowSectionRefs)
    {
        ELEMS_LOOP:
        for (Element elem: page.doc.select(tagName)) {
            // Get attribute value.
            String ref = elem.attr(attrName);
            if (ref == null || ref.isBlank()) {
                if (allowEmptyRefIfNoChildren) {
                    // Occurs for 'a.href' for bibliography entries.
                    // But then they have no child nodes, and are thus not clickable.
                    Verify.verify(elem.childNodeSize() == 0);
                    continue;
                } else {
                    throw new RuntimeException(
                            String.format("Undefined '%s.%s' for %s", tagName, attrName, page.sourceFile.relPath));
                }
            }

            // Handle '#' references, originally pointing to within the single AsciiDoc-generated HTML file.
            if (allowSectionRefs && ref.startsWith("#")) {
                String id = ref.substring(1);
                for (AsciiDocHtmlPage targetPage: htmlPages.pages) {
                    if (targetPage.singlePageIds.contains(id)) {
                        String newHref = AsciiDocHtmlUtil.getFileOrSectionHref(page, targetPage, id);
                        elem.attr("href", newHref);
                        continue ELEMS_LOOP;
                    }
                }
                throw new RuntimeException(
                        String.format("No page found that defines '%s.%s' id: %s", tagName, attrName, id));
            }

            // Get referenced URI. Skip 'http', 'https' and 'mailto' references.
            URI uri = URI.create(ref);
            String uriScheme = uri.getScheme();
            if ("http".equals(uriScheme) || "https".equals(uriScheme) || "mailto".equals(uriScheme)) {
                continue;
            }

            // Ensure it is a relative path to an entire file.
            Verify.verify(uriScheme == null);
            Verify.verify(uri.getUserInfo() == null);
            Verify.verify(uri.getHost() == null);
            Verify.verify(uri.getPort() == -1);
            Verify.verify(uri.getAuthority() == null);
            Verify.verify(uri.getQuery() == null);
            Verify.verify(uri.getFragment() == null);
            Verify.verifyNotNull(uri.getPath());
            Verify.verify(ref.equals(uri.getPath()));

            // Get absolute path of file referred to by href.
            Path hrefAbsTarget = sourceRootPath.resolve(ref);

            // Relativize it against the source file.
            Path rootPathForNewRelHref = page.sourceFile.absPath.getParent();
            String newRelHref = rootPathForNewRelHref.relativize(hrefAbsTarget).toString();
            if (newRelHref.isEmpty()) {
                newRelHref = ".";
            }

            // Ensure the correct path separators.
            Verify.verify(!ref.contains("\\"), ref);
            newRelHref = newRelHref.replace("\\", "/");
            Verify.verify(!newRelHref.contains("\\"), newRelHref);

            // Set new href attribute value.
            elem.attr(attrName, newRelHref);
        }
    }

    /**
     * Add home page to TOC.
     *
     * @param page The multi-page HTML page to modify in-place.
     * @param homePage The multi-page HTML home page.
     */
    private static void addHomePageToToc(AsciiDocHtmlPage page, AsciiDocHtmlPage homePage) {
        Element elemTocSectLevel1 = single(page.doc.select("#toc ul.sectlevel1"));
        Element elemTocHomeLi = elemTocSectLevel1.prependElement("li");
        Element elemTocHomeA = elemTocHomeLi.prependElement("a");
        elemTocHomeA.attr("href", AsciiDocHtmlUtil.getFileOrSectionHref(page, homePage, null));
        if (page == homePage) {
            elemTocHomeA.addClass("toc-cur-page");
        }
        elemTocHomeA.appendText(homePage.sourceFile.title);
    }

    /**
     * Add breadcrumbs.
     *
     * @param page The multi-page HTML page to modify in-place.
     * @param homePage The multi-page HTML home page.
     * @param docOriginalTitle The original HTML page title.
     */
    private static void addBreadcrumbs(AsciiDocHtmlPage page, AsciiDocHtmlPage homePage, String docOriginalTitle) {
        // Prepare breadcrumbs element.
        Element elemContent = single(page.doc.select("#content"));
        Element elemBreadcrumbsDiv = elemContent.prependElement("div");
        elemBreadcrumbsDiv.attr("id", "breadcrumbs");

        // Add breadcrumbs.
        for (AsciiDocHtmlPage breadcrumb: page.breadcrumbs) {
            if (elemBreadcrumbsDiv.childNodeSize() > 0) {
                elemBreadcrumbsDiv.appendText(" > ");
            }
            boolean isSelfBreadcrumb = breadcrumb == page;
            Element elemBreadcrumb = elemBreadcrumbsDiv.appendElement(isSelfBreadcrumb ? "span" : "a");
            elemBreadcrumb.addClass("breadcrumb");
            if (!isSelfBreadcrumb) {
                elemBreadcrumb.attr("href", AsciiDocHtmlUtil.getFileOrSectionHref(page, breadcrumb, null));
            }
            elemBreadcrumb.text(breadcrumb == homePage ? docOriginalTitle : breadcrumb.sourceFile.title);
        }
    }

    /**
     * Add link to single-page HTML version.
     *
     * @param homePage The multi-page HTML home page to modify in-place.
     */
    private static void addLinkToSinglePageHtmlVersion(AsciiDocHtmlPage homePage) {
        Element elemPdfTip = single(homePage.doc.select("div.tip td.content:contains(as a PDF as well)"));
        elemPdfTip.appendText("Or use the ");
        Element elemPdfTipA = elemPdfTip.appendElement("a");
        elemPdfTipA.attr("href", homePage.sourceFile.getBaseName() + "-single-page.html");
        elemPdfTipA.text("single-page HTML");
        elemPdfTip.appendText(" version.");
    }
}