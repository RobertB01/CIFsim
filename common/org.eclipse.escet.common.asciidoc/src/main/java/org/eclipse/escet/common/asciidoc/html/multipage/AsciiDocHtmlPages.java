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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Verify;

/** Information about all HTML page of a set of multi-page HTML files. */
public class AsciiDocHtmlPages {
    /** All pages, including the home page. */
    final List<AsciiDocHtmlPage> pages;

    /** The home page (root page). */
    final AsciiDocHtmlPage homePage;

    /** The Table of Contents (TOC). */
    AsciiDocTocEntry toc;

    /**
     * Constructor for the {@link AsciiDocHtmlPages} class.
     *
     * @param sourceFiles The AsciiDoc source files.
     */
    AsciiDocHtmlPages(List<AsciiDocSourceFile> sourceFiles) {
        // Create a page per source file.
        this.pages = Collections
                .unmodifiableList(sourceFiles.stream().map(f -> new AsciiDocHtmlPage(f)).collect(Collectors.toList()));

        // Find the home page.
        List<AsciiDocHtmlPage> homePages = pages.stream().filter(p -> p.sourceFile.isRootAsciiDocFile)
                .collect(Collectors.toList());
        Verify.verify(homePages.size() == 1);
        this.homePage = homePages.get(0);
    }
}
