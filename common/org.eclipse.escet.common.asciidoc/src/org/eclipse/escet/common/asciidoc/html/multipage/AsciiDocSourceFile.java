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

import java.nio.file.Path;

/** Information about an AsciiDoc source file. */
class AsciiDocSourceFile {
    /** The absolute path of the source file. */
    final Path absPath;

    /** The relative path of the source file, from the directory that contains the root 'index.asciidoc' file. */
    final Path relPath;

    /**
     * The id of the first header in the source file. Is {@code null} for the {@link #isRootIndexFile root index file}.
     */
    final String sourceId;

    /**
     * The title of the first header in the source file, and thus the title of the source file itself. Is {@code "Home"}
     * for the {@link #isRootIndexFile root index file}.
     */
    final String title;

    /** Is this source file the root 'index.asciidoc' file ({@code true}) or not ({@code false})? */
    final boolean isRootIndexFile;

    /**
     * Constructor for the {@link AsciiDocSourceFile} class.
     *
     * @param absPath The absolute path of the source file.
     * @param relPath The relative path of the source file, from the directory that contains the root 'index.asciidoc'
     *     file.
     * @param sourceId The id of the first header in the source file. Is {@code null} for the {@link #isRootIndexFile
     *     root index file}.
     * @param title The title of the first header in the source file, and thus the title of the source file itself. Is
     *     {@code "Home"} for the {@link #isRootIndexFile root index file}.
     * @param isRootIndexFile Is this source file the root 'index.asciidoc' file ({@code true}) or not ({@code false})?
     */
    AsciiDocSourceFile(Path absPath, Path relPath, String sourceId, String title, boolean isRootIndexFile) {
        this.absPath = absPath;
        this.relPath = relPath;
        this.sourceId = sourceId;
        this.title = title;
        this.isRootIndexFile = isRootIndexFile;
    }
}
