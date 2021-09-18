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

import static org.eclipse.escet.common.java.Sets.set;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Node;

/** Information about an AsciiDoc source file. */
class AsciiDocSourceFile {
    /** The absolute path of the source file. */
    Path absPath;

    /** The relative path of the source file, from the directory that contains the root 'index.asciidoc' file. */
    Path relPath;

    /**
     * The id of the first header in the source file. Is {@code null} for the {@link #isRootIndexFile root index file}.
     */
    String sourceId;

    /**
     * The title of the first header in the source file, and thus the title of the source file itself. Is {@code "Home"}
     * for the {@link #isRootIndexFile root index file}.
     */
    String title;

    /** Is this source file the root 'index.asciidoc' file ({@code true}) or not ({@code false})? */
    boolean isRootIndexFile;

    /**
     * The breadcrumbs for this source file, starting from the home page (first element) to the given source file (last
     * element). Remains {@code null} for the {@link #isRootIndexFile root index file}.
     */
    List<AsciiDocSourceFile> breadcrumbs;

    /** The unique ids defined in the generated HTML 'content' for this source file, in order. */
    Set<String> ids = set();

    /**
     * The 'content' nodes in the single AsciiDoc-generated HTML file, associated with this source file. Is recomputed
     * for each new HTML file, to ensure the proper cloned nodes are present.
     */
    List<Node> nodes;
}
