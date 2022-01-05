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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.reverse;
import static org.eclipse.escet.common.java.Lists.single;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

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
            Assert.areEqual(htmlPage.breadcrumbs, null);
            Assert.areEqual(htmlPage.singlePageIds, null);
            Assert.areEqual(htmlPage.singlePageNodes, null);
            htmlPage.singlePageIds = set();
            htmlPage.singlePageNodes = list();
        }

        // Map ids of non-home pages to their pages.
        Map<String, AsciiDocHtmlPage> idToPages = mapc(htmlPages.pages.size());
        for (AsciiDocHtmlPage htmlPage: htmlPages.pages) {
            if (!htmlPage.sourceFile.isRootAsciiDocFile) {
                AsciiDocHtmlPage prev = idToPages.put(htmlPage.sourceFile.sourceId, htmlPage);
                if (prev != null) {
                    Assert.fail(fmt("Duplicate source id: %s, for sources: %s and %s", htmlPage.sourceFile.sourceId,
                            prev.sourceFile.relPath, htmlPage.sourceFile.relPath));
                }
            }
        }
        Assert.areEqual(idToPages.size() + 1, htmlPages.pages.size());

        // Walk over HTML 'content' and assign all nodes to a single page. Also create the TOC.
        Deque<Pair<AsciiDocHtmlPage, Integer>> pageStack = new LinkedList<>();
        pageStack.push(pair(htmlPages.homePage, 0));

        Deque<Pair<AsciiDocTocEntry, Integer>> tocStack = new LinkedList<>();
        tocStack.push(pair(new AsciiDocTocEntry(htmlPages.homePage, doc.title(), null), 0));

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
                        Assert.check(elemPage.sourceFile.sourceId.equals(id));
                        pageStack.push(pair(elemPage, depth));

                        AsciiDocTocEntry curTocEntry = tocStack.peek().left;
                        AsciiDocTocEntry newTocEntry = new AsciiDocTocEntry(elemPage, elemPage.sourceFile.title,
                                elemPage.sourceFile.sourceId);
                        curTocEntry.children.add(newTocEntry);
                        tocStack.push(pair(newTocEntry, depth));

                        // Store reversed stack as breadcrumbs.
                        elemPage.breadcrumbs = reverse(
                                pageStack.stream().map(e -> e.left).collect(Collectors.toList()));
                    } else {
                        // Same page.
                        if (elem.tagName().matches("h\\d+")) {
                            String entryTitle = elem.text();
                            AsciiDocTocEntry curTocEntry = tocStack.peek().left;
                            AsciiDocTocEntry newTocEntry = new AsciiDocTocEntry(curTocEntry.page, entryTitle, id);
                            curTocEntry.children.add(newTocEntry);
                            tocStack.push(pair(newTocEntry, depth));
                        }
                    }
                }

                // Collect id. Store for current page.
                if (node instanceof Element) {
                    String id = ((Element)node).id();
                    if (!id.isBlank()) {
                        pageStack.peek().left.singlePageIds.add(id);
                    }
                }

                // Detect sibling node of other nodes of current page. This ensures we don't collect all nodes, but
                // only 'root' ones, not all descendants of 'root' nodes.
                if (depth == pageStack.peek().right) {
                    pageStack.peek().left.singlePageNodes.add(node);
                }
            }

            @Override
            public void tail(Node node, int depth) {
                // Detect end of page.
                if (depth < pageStack.peek().right) {
                    pageStack.pop();
                }

                // Detect end of TOC entry.
                if (depth < tocStack.peek().right) {
                    tocStack.pop();
                }
            }
        });
        Assert.areEqual(pageStack.size(), 1);
        Assert.areEqual(tocStack.size(), 1);

        // Ensure content for each page.
        for (AsciiDocHtmlPage page: htmlPages.pages) {
            Assert.check(!page.singlePageNodes.isEmpty(), "No nodes for page: " + page.sourceFile.relPath.toString());
        }

        // Return the TOC.
        Assert.areEqual(tocStack.peek().right, 0);
        htmlPages.toc = tocStack.pop().left;
    }
}
