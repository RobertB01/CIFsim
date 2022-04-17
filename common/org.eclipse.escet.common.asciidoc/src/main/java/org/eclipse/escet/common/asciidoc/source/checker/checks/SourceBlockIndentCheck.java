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

package org.eclipse.escet.common.asciidoc.source.checker.checks;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.escet.common.asciidoc.source.checker.AsciiDocSourceCheckContext;

/** Check that source blocks as a whole don't have indentation. */
public class SourceBlockIndentCheck extends AsciiDocSourceFileCheck {
    @Override
    public void check(AsciiDocSourceCheckContext context) {
        // Check each source block.
        for (Pair<Integer, List<Pair<Integer, String>>> sourceBlock: context.sourceBlocks) {
            // Get minimum indentation of the entire block.
            Integer minIndent = null;
            for (Pair<Integer, String> numberedLine: sourceBlock.getRight()) {
                String line = numberedLine.getRight();

                // Skip blank lines.
                if (line.isBlank()) {
                    continue;
                }

                // Update minimum indentation.
                int lineIndent = (int)line.chars().takeWhile(Character::isWhitespace).count();
                minIndent = (minIndent == null) ? lineIndent : Math.min(minIndent, lineIndent);
            }

            // Check for problems.
            if (minIndent == null) {
                context.addProblem(sourceBlock.getLeft(), 1, "Source block does not have any content.");
            } else if (minIndent > 0) {
                context.addProblem(sourceBlock.getRight().get(0).getLeft(), minIndent, "Indented code block.");
            }
        }
    }
}
