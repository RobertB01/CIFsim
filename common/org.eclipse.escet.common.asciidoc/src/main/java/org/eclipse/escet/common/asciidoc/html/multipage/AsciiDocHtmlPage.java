//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

/** Information about a single mulit-page HTML page of a set of multi-page HTML pages. */
public class AsciiDocHtmlPage {
    /** The AsciiDoc source file matching this page. */
    final AsciiDocSourceFile sourceFile;

    /**
     * The breadcrumbs for this page, starting from the root/home page (first element) to this page (last element). Is
     * {@code null} for the {@link AsciiDocSourceFile#isRootAsciiDocFile root/home page}.
     */
    List<AsciiDocHtmlPage> breadcrumbs;

    /** The unique ids defined in the HTML 'content' for this page, in order, for the single-page HTML file. */
    Set<String> singlePageIds;

    /** The unique ids defined in the HTML 'content' for this page, in order, for the multi-page HTML file. */
    Set<String> multiPageIds;

    /**
     * Mapping from single-page section ids (subset of {@link #singlePageIds}) to multi-page section ids (subset of
     * {@link #multiPageIds}).
     */
    Map<String, String> sectionIdRenames;

    /** The 'content' nodes belonging to this page, for the single-page HTML document. */
    List<Node> singlePageNodes;

    /**
     * The 'content' nodes belonging to this page, for the cloned HTML document of one of the multi-page HTML pages. Is
     * {@code null} if not yet or no longer available. Is recomputed for the cloned documents for each of the pages, for
     * the current page that is being processed.
     */
    List<Node> multiPageNodes;

    /**
     * The multi-page HTML document for this page. It is a modified clone of the single-page HTML document. Is
     * {@code null} if not yet or no longer available.
     */
    Document doc;

    /**
     * Constructor for the {@link AsciiDocHtmlPage} class.
     *
     * @param sourceFile The AsciiDoc source file matching this page.
     */
    AsciiDocHtmlPage(AsciiDocSourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }
}
