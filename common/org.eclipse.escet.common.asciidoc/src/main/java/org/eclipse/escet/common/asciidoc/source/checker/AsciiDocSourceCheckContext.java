//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Verify;

/** AsciiDoc source file check context. */
public class AsciiDocSourceCheckContext {
    /** The path to the AsciiDoc source file. */
    public final Path asciiDocFile;

    /** The lines of text of the AsciiDoc source file. */
    public final List<String> lines;

    /** The problems found so far. To be extended with newly found problems. */
    private final List<AsciiDocSourceProblem> problems;

    /** The normal lines, as pairs of 1-based line numbers and the actual lines. */
    public final List<Pair<Integer, String>> normalLines = new ArrayList<>();

    /**
     * The source blocks, as pairs indicating the start of the source block (a 1-based line number) and the lines of the
     * source block (pairs of 1-based line numbers and the actual lines).
     */
    public final List<Pair<Integer, List<Pair<Integer, String>>>> sourceBlocks = new ArrayList<>();

    /**
     * Constructor for the {@link AsciiDocSourceCheckContext} class.
     *
     * @param asciiDocFile The path to the AsciiDoc source file.
     * @param lines The lines of text of the AsciiDoc source file.
     * @param problems The problems found so far. To be extended with newly found problems.
     */
    public AsciiDocSourceCheckContext(Path asciiDocFile, List<String> lines, List<AsciiDocSourceProblem> problems) {
        this.asciiDocFile = asciiDocFile;
        this.lines = lines;
        this.problems = problems;
        analyzeLines();
    }

    /** Analyze the lines of the AsciiDoc source file, partitioning them to various subsets. */
    private void analyzeLines() {
        // Compile regular expression patterns.
        Pattern sourceBlockHeaderPattern = Pattern.compile("^\\[ *source");
        Pattern sourceBlockDashesPattern = Pattern.compile("----+");

        // Analyze all the lines.
        boolean inSourceBlock = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineNr = i + 1;

            if (!inSourceBlock) {
                // In normal text.
                Matcher matcher = sourceBlockHeaderPattern.matcher(line);
                if (matcher.find()) {
                    // Check and process next line, which should have dashes.
                    Verify.verify(i + 1 < lines.size(), Integer.toString(lineNr));
                    String nextLine = lines.get(i + 1);
                    Verify.verify(sourceBlockDashesPattern.matcher(nextLine).matches(), nextLine);
                    i++; // ignore checkstyle ModifiedControlVariableCheck (control variable incremented on purpose)

                    // New source block.
                    inSourceBlock = true;
                    sourceBlocks.add(ImmutablePair.of(lineNr, new ArrayList<>()));
                } else {
                    normalLines.add(ImmutablePair.of(lineNr, line));
                }
            } else {
                // In source block.
                Matcher matcher = sourceBlockDashesPattern.matcher(line);
                if (matcher.matches()) {
                    inSourceBlock = false; // End of source block.
                } else {
                    sourceBlocks.get(sourceBlocks.size() - 1).getRight().add(ImmutablePair.of(lineNr, line));
                }
            }
        }
    }

    /**
     * Adds a problem to this context.
     *
     * @param line The line with the problem, as 1-based line index from the start of the file.
     * @param column The column with the problem, as 1-based column index from the start of the line.
     * @param message The problem message.
     */
    public void addProblem(int line, int column, String message) {
        problems.add(new AsciiDocSourceProblem(asciiDocFile, line, column, message));
    }
}
