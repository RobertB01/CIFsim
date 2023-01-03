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

package org.eclipse.escet.common.asciidoc.source.checker.checks;

import org.eclipse.escet.common.asciidoc.source.checker.AsciiDocSourceCheckContext;
import org.eclipse.escet.common.asciidoc.source.checker.AsciiDocSourceCodeBlock;
import org.eclipse.escet.common.asciidoc.source.checker.AsciiDocSourceLine;

/** Check that source blocks as a whole don't have indentation. */
public class SourceBlockIndentCheck extends AsciiDocSourceFileCheck {
    @Override
    public void check(AsciiDocSourceCheckContext context) {
        // Check each source block.
        for (AsciiDocSourceCodeBlock sourceBlock: context.sourceBlocks) {
            // Get minimum indentation of the entire block.
            Integer minIndent = null;
            for (AsciiDocSourceLine numberedLine: sourceBlock.lines) {
                // Skip blank lines.
                if (numberedLine.line.isBlank()) {
                    continue;
                }

                // Update minimum indentation.
                int lineIndent = (int)numberedLine.line.chars().takeWhile(Character::isWhitespace).count();
                minIndent = (minIndent == null) ? lineIndent : Math.min(minIndent, lineIndent);
            }

            // Check for problems.
            if (minIndent == null) {
                context.addProblem(sourceBlock.lineNr, 1, "Source block does not have any content.");
            } else if (minIndent > 0) {
                context.addProblem(sourceBlock.lines.get(0).lineNr, minIndent, "Indented code block.");
            }
        }
    }
}
