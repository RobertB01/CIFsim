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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Verify;

/** AsciiDoc source file check context. */
public class AsciiDocSourceCheckContext {
    /** The path to the AsciiDoc source file. */
    public final Path asciiDocFile;

    /** The lines of text of the AsciiDoc source file. */
    public final List<String> lines;

    /** The problems found so far. To be extended with newly found problems. */
    private final List<AsciiDocSourceProblem> problems;

    /** The normal lines. */
    public final List<AsciiDocSourceLine> normalLines = new ArrayList<>();

    /** The source (code) blocks. */
    public final List<AsciiDocSourceCodeBlock> sourceBlocks = new ArrayList<>();

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
        int lineIdx = 0;
        LINES:
        while (lineIdx < lines.size()) {
            String line = lines.get(lineIdx);

            // Detect and handle source block.
            Matcher matcher = sourceBlockHeaderPattern.matcher(line);
            if (matcher.find()) {
                // Store line index for later.
                int blockLineIdx = lineIdx;

                // Check and process next line, which should have dashes.
                lineIdx++;
                Verify.verify(lineIdx < lines.size(), Integer.toString(lineIdx));
                line = lines.get(lineIdx);
                Verify.verify(sourceBlockDashesPattern.matcher(line).matches(), line);

                // Collect lines of the source block.
                List<AsciiDocSourceLine> blockLines = new ArrayList<>();
                while (true) {
                    // Get next line.
                    lineIdx++;
                    Verify.verify(lineIdx < lines.size(), Integer.toString(lineIdx));
                    line = lines.get(lineIdx);

                    // Check for end of source block.
                    matcher = sourceBlockDashesPattern.matcher(line);
                    if (matcher.matches()) {
                        // Add the source block.
                        sourceBlocks.add(new AsciiDocSourceCodeBlock(blockLineIdx + 1, blockLines));
                        lineIdx++;
                        continue LINES;
                    }

                    // Just another line in the source block.
                    blockLines.add(new AsciiDocSourceLine(lineIdx + 1, line));
                }
            }

            // Normal line.
            normalLines.add(new AsciiDocSourceLine(lineIdx + 1, line));
            lineIdx++;
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
