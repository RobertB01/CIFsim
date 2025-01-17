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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Verify;

/** AsciiDoc Table of Contents (TOC) entry, corresponding to a section in an AsciiDoc document. */
class AsciiDocTocEntry {
    /** The AsciiDoc multi-page HTML page that contains the section represented by this TOC entry. */
    final AsciiDocHtmlPage page;

    /** The title of the TOC entry. */
    final String title;

    /** The original HTML reference ID for the TOC entry, or {@code null} for the root TOC entry. */
    final String origRefId;

    /** The original or modified HTML reference ID for the TOC entry, or {@code null} for the root TOC entry. */
    String refId;

    /** The parent TOC entry, or {@code null} for the root TOC entry. */
    final AsciiDocTocEntry parent;

    /** The child TOC entries. */
    final List<AsciiDocTocEntry> children = new ArrayList<>();

    /**
     * Constructor for the {@link AsciiDocTocEntry} class.
     *
     * @param page The AsciiDoc multi-page HTML page that contains the section represented by this TOC entry.
     * @param title The title of the TOC entry.
     * @param refId The HTML reference ID for the TOC entry, or {@code null} for the root TOC entry.
     * @param parent The parent TOC entry, or {@code null} for the root TOC entry.
     */
    AsciiDocTocEntry(AsciiDocHtmlPage page, String title, String refId, AsciiDocTocEntry parent) {
        this.page = page;
        this.title = title;
        this.origRefId = refId;
        this.refId = refId;
        this.parent = parent;

        Verify.verify((refId == null) == (parent == null));
    }

    /**
     * Returns a trail from the root TOC entry to this TOC entry.
     *
     * @return The trail, with the root TOC entry first, and this TOC entry last.
     */
    List<AsciiDocTocEntry> getTrail() {
        List<AsciiDocTocEntry> trail = new ArrayList<>();
        AsciiDocTocEntry entry = this;
        while (entry != null) {
            trail.add(entry);
            entry = entry.parent;
        }
        Collections.reverse(trail);
        return trail;
    }
}
