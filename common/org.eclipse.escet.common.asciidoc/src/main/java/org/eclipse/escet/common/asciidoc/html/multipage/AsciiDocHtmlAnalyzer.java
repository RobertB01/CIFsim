//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

import com.google.common.base.Verify;

/** AsciiDoc HTML analyzer. */
class AsciiDocHtmlAnalyzer {
    /** Constructor for the {@link AsciiDocHtmlAnalyzer} class. */
    private AsciiDocHtmlAnalyzer() {
        // Static class.
    }

    /**
     * Analyzes the AsciiDoc-generated single-page HTML:
     * <ul>
     * <li>Partition the 'content' to the various multi-page HTML pages.</li>
     * <li>Build a Table of Contents (TOC).</li>
     * </ul>
     *
     * @param doc The AsciiDoc-generated single-page HTML document for which to partition the 'content'.
     * @param htmlPages The multi-page HTML pages. Are modified in-place.
     */
    static void analyze(Document doc, AsciiDocHtmlPages htmlPages) {
        // Check that not yet partitioned, and initialize.
        for (AsciiDocHtmlPage htmlPage: htmlPages.pages) {
            Verify.verify(htmlPage.breadcrumbs == null);
            Verify.verify(htmlPage.singlePageIds == null);
            Verify.verify(htmlPage.singlePageNodes == null);
            htmlPage.singlePageIds = new LinkedHashSet<>();
            htmlPage.singlePageNodes = new ArrayList<>();
        }

        // Map ids of non-home pages to their pages.
        Map<String, AsciiDocHtmlPage> idToPages = new HashMap<>(htmlPages.pages.size());
        for (AsciiDocHtmlPage htmlPage: htmlPages.pages) {
            if (!htmlPage.sourceFile.isRootAsciiDocFile) {
                AsciiDocHtmlPage prev = idToPages.put(htmlPage.sourceFile.sourceId, htmlPage);
                if (prev != null) {
                    throw new RuntimeException(String.format("Duplicate source id: %s, for sources: %s and %s",
                            htmlPage.sourceFile.sourceId, prev.sourceFile.relPath, htmlPage.sourceFile.relPath));
                }
            }
        }
        Verify.verify(idToPages.size() + 1 == htmlPages.pages.size());

        // Walk over HTML 'content' and assign all nodes to a single page. Also create the TOC.
        Deque<Pair<AsciiDocHtmlPage, Integer>> pageStack = new LinkedList<>();
        pageStack.push(ImmutablePair.of(htmlPages.homePage, 0));

        Deque<Pair<AsciiDocTocEntry, Integer>> tocStack = new LinkedList<>();
        tocStack.push(ImmutablePair.of(new AsciiDocTocEntry(htmlPages.homePage, doc.title(), null), 0));

        Element elemContent = single(doc.select("#content"));
        elemContent.children().traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                // Detect new page. Add TOC entry.
                if (node instanceof Element) {
                    Element elem = (Element)node;
                    String id = elem.id();
                    AsciiDocHtmlPage elemPage = idToPages.get(id);
                    if (elemPage != null) {
                        // New page.
                        Verify.verify(elemPage.sourceFile.sourceId.equals(id));
                        pageStack.push(ImmutablePair.of(elemPage, depth));

                        AsciiDocTocEntry curTocEntry = tocStack.peek().getLeft();
                        AsciiDocTocEntry newTocEntry = new AsciiDocTocEntry(elemPage, elemPage.sourceFile.title,
                                elemPage.sourceFile.sourceId);
                        curTocEntry.children.add(newTocEntry);
                        tocStack.push(ImmutablePair.of(newTocEntry, depth));

                        // Store reversed stack as breadcrumbs.
                        elemPage.breadcrumbs = pageStack.stream().map(e -> e.getLeft()).collect(Collectors.toList());
                        Collections.reverse(elemPage.breadcrumbs);
                    } else {
                        // Same page.
                        if (elem.tagName().matches("h\\d+")) {
                            String entryTitle = elem.text();
                            AsciiDocTocEntry curTocEntry = tocStack.peek().getLeft();
                            AsciiDocTocEntry newTocEntry = new AsciiDocTocEntry(curTocEntry.page, entryTitle, id);
                            curTocEntry.children.add(newTocEntry);
                            tocStack.push(ImmutablePair.of(newTocEntry, depth));
                        }
                    }
                }

                // Collect id. Store for current page.
                if (node instanceof Element) {
                    String id = ((Element)node).id();
                    if (!id.isBlank()) {
                        pageStack.peek().getLeft().singlePageIds.add(id);
                    }
                }

                // Detect sibling node of other nodes of current page. This ensures we don't collect all nodes, but
                // only 'root' ones, not all descendants of 'root' nodes.
                if (depth == pageStack.peek().getRight()) {
                    pageStack.peek().getLeft().singlePageNodes.add(node);
                }
            }

            @Override
            public void tail(Node node, int depth) {
                // Detect end of page.
                if (depth < pageStack.peek().getRight()) {
                    pageStack.pop();
                }

                // Detect end of TOC entry.
                if (depth < tocStack.peek().getRight()) {
                    tocStack.pop();
                }
            }
        });
        Verify.verify(pageStack.size() == 1);
        Verify.verify(tocStack.size() == 1);

        // Ensure content for each page.
        for (AsciiDocHtmlPage page: htmlPages.pages) {
            Verify.verify(!page.singlePageNodes.isEmpty(), "No nodes for page: " + page.sourceFile.relPath.toString());
        }

        // Return the TOC.
        Verify.verify(tocStack.peek().getRight() == 0);
        htmlPages.toc = tocStack.pop().getLeft();
    }
}
