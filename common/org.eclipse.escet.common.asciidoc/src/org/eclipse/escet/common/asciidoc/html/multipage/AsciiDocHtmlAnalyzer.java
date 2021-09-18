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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.reverse;
import static org.eclipse.escet.common.java.Lists.single;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Pair.pair;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
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
     * Partition the 'content' of the AsciiDoc-generated HTML file to the AsciiDoc source files.
     *
     * @param doc The HTML document for which to partition the 'content'.
     * @param sourceFiles The AsciDoc source files. Are modified in-place.
     * @return The TOC.
     * @see AsciiDocSourceFile#nodes
     * @see AsciiDocSourceFile#ids
     * @see AsciiDocSourceFile#breadcrumbs
     */
    static AsciiDocTocEntry partitionContent(Document doc, List<AsciiDocSourceFile> sourceFiles) {
        // Clear old partition.
        for (AsciiDocSourceFile sourceFile: sourceFiles) {
            sourceFile.nodes = list();
        }

        // Get root source file and map ids to other source files.
        Map<String, AsciiDocSourceFile> idToSources = mapc(sourceFiles.size());
        AsciiDocSourceFile rootSourceFile = null;
        for (AsciiDocSourceFile sourceFile: sourceFiles) {
            sourceFile.nodes = list();
            if (sourceFile.isRootIndexFile) {
                Assert.check(rootSourceFile == null, sourceFile.relPath.toString());
                rootSourceFile = sourceFile;
            } else {
                AsciiDocSourceFile prev = idToSources.put(sourceFile.sourceId, sourceFile);
                Assert.check(prev == null);
            }
        }
        if (rootSourceFile == null) {
            throw new AssertionError("rootSourceFile == nul;");
        }
        Assert.check(idToSources.size() + 1 == sourceFiles.size(), idToSources.size() + " / " + sourceFiles.size());

        // Walk over HTML 'content' and assign all nodes to a single source file. Also create the TOC.
        Deque<Pair<AsciiDocSourceFile, Integer>> sourceFileStack = new LinkedList<>();
        sourceFileStack.push(pair(rootSourceFile, 0));

        Deque<Pair<AsciiDocTocEntry, Integer>> tocStack = new LinkedList<>();
        tocStack.push(pair(new AsciiDocTocEntry(rootSourceFile, rootSourceFile.title, null), 0));

        Element elemContent = single(doc.select("#content"));
        elemContent.children().traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                // Detect new source file. Add TOC entry.
                if (node instanceof Element) {
                    Element elem = (Element)node;
                    String id = elem.id();
                    AsciiDocSourceFile elemSourceFile = idToSources.get(id);
                    if (elemSourceFile != null) {
                        // New source file.
                        Assert.check(elemSourceFile.sourceId.equals(id));
                        sourceFileStack.push(pair(elemSourceFile, depth));

                        AsciiDocTocEntry curTocEntry = tocStack.peek().left;
                        AsciiDocTocEntry newTocEntry = new AsciiDocTocEntry(elemSourceFile, elemSourceFile.title,
                                elemSourceFile.sourceId);
                        curTocEntry.children.add(newTocEntry);
                        tocStack.push(pair(newTocEntry, depth));

                        // Store reversed stack as breadcrumbs.
                        elemSourceFile.breadcrumbs = reverse(
                                sourceFileStack.stream().map(e -> e.left).collect(Collectors.toList()));
                    } else {
                        // Same source file.
                        if (elem.tagName().matches("h\\d+")) {
                            String entryTitle = elem.text();
                            AsciiDocTocEntry curTocEntry = tocStack.peek().left;
                            AsciiDocTocEntry newTocEntry = new AsciiDocTocEntry(curTocEntry.sourceFile, entryTitle, id);
                            curTocEntry.children.add(newTocEntry);
                            tocStack.push(pair(newTocEntry, depth));
                        }
                    }
                }

                // Collect id. Store for current source file.
                if (node instanceof Element) {
                    String id = ((Element)node).id();
                    if (!id.isBlank()) {
                        sourceFileStack.peek().left.ids.add(id);
                    }
                }

                // Detect sibling node of other nodes of current source file. This ensures we don't collect all nodes,
                // but only 'root' ones, not all descendants of 'root' nodes.
                if (depth == sourceFileStack.peek().right) {
                    sourceFileStack.peek().left.nodes.add(node);
                }
            }

            @Override
            public void tail(Node node, int depth) {
                // Detect end of source file.
                if (depth < sourceFileStack.peek().right) {
                    sourceFileStack.pop();
                }

                // Detect end of TOC entry.
                if (depth < tocStack.peek().right) {
                    tocStack.pop();
                }
            }
        });
        Assert.check(sourceFileStack.size() == 1, String.valueOf(sourceFileStack.size()));
        Assert.check(tocStack.size() == 1, String.valueOf(tocStack.size()));

        // Ensure content for each source file.
        for (AsciiDocSourceFile sourceFile: sourceFiles) {
            Assert.check(!sourceFile.nodes.isEmpty(), sourceFile.relPath.toString());
        }

        // Return the TOC.
        Assert.check(tocStack.peek().right == 0, String.valueOf(tocStack.peek().right));
        return tocStack.pop().left;
    }
}
