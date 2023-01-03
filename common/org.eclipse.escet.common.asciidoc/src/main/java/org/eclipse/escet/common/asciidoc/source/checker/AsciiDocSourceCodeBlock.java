//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.asciidoc.source.checker;

import java.util.Collections;
import java.util.List;

/** An AsciiDoc source (code) block. */
public class AsciiDocSourceCodeBlock {
    /** The 1-based line number of the source code block header within its AsciiDoc source file. */
    public final int lineNr;

    /** The content of source code block. May be empty. */
    public final List<AsciiDocSourceLine> lines;

    /**
     * Constructor for the {@link AsciiDocSourceCodeBlock} class.
     *
     * @param lineNr The 1-based line number of the source code block header within its AsciiDoc source file.
     * @param lines The content of source code block. May be empty.
     */
    public AsciiDocSourceCodeBlock(int lineNr, List<AsciiDocSourceLine> lines) {
        this.lineNr = lineNr;
        this.lines = Collections.unmodifiableList(lines);
    }
}
