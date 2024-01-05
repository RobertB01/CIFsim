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

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Locale;

/** A problem in an AsciiDoc source file. */
public class AsciiDocSourceProblem implements Comparable<AsciiDocSourceProblem> {
    /** The path to the AsciiDoc source file with the problem. */
    public final Path path;

    /** The line with the problem, as 1-based line index from the start of the file. */
    public final int line;

    /** The column with the problem, as 1-based column index from the start of the line. */
    public final int column;

    /** The problem message. */
    public final String message;

    /**
     * Constructor for the {@link AsciiDocSourceProblem} class.
     *
     * @param path The path to the AsciiDoc source file with the problem.
     * @param line The line with the problem, as 1-based line index from the start of the file.
     * @param column The column with the problem, as 1-based column index from the start of the line.
     * @param message The problem message.
     */
    public AsciiDocSourceProblem(Path path, int line, int column, String message) {
        this.path = path;
        this.line = line;
        this.column = column;
        this.message = message;
    }

    @Override
    public int compareTo(AsciiDocSourceProblem other) {
        return Comparator.comparing((AsciiDocSourceProblem p) -> p.path.toString()).thenComparing(p -> p.line)
                .thenComparing(p -> p.column).thenComparing(p -> p.message).compare(this, other);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "File %s, line %d, column %d: %s", path, line, column, message);
    }
}
