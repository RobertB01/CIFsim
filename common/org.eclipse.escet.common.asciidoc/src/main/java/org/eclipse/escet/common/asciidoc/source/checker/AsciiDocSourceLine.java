//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

/** A numbered AsciiDoc source line. */
public class AsciiDocSourceLine {
    /** The 1-based line number of the line within its AsciiDoc source file. */
    public final int lineNr;

    /** The content of the line. */
    public final String line;

    /**
     * Constructor for the {@link AsciiDocSourceLine} class.
     *
     * @param lineNr The 1-based line number of the line within its AsciiDoc source file.
     * @param line The content of the line.
     */
    public AsciiDocSourceLine(int lineNr, String line) {
        this.lineNr = lineNr;
        this.line = line;
    }
}
