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

import java.util.List;

/** AsciiDoc Table of Contents (TOC) entry, corresponding to a section in an AsciiDoc document. */
public class AsciiDocTocEntry {
    /** The AsciiDoc source file that defines the section represented by this TOC entry. */
    final AsciiDocSourceFile sourceFile;

    /** The title of the TOC entry. */
    final String title;

    /** The HTML reference ID for the TOC entry, or {@code null} for the root TOC entry. */
    final String refId;

    /** The child TOC entries. */
    final List<AsciiDocTocEntry> children = list();

    /**
     * Constructor for the {@link AsciiDocTocEntry} class.
     *
     * @param sourceFile The AsciiDoc source file that defines the section represented by this TOC entry.
     * @param title The title of the TOC entry.
     * @param refId The HTML reference ID for the TOC entry, or {@code null} for the root TOC entry.
     */
    AsciiDocTocEntry(AsciiDocSourceFile sourceFile, String title, String refId) {
        this.sourceFile = sourceFile;
        this.title = title;
        this.refId = refId;
    }
}
