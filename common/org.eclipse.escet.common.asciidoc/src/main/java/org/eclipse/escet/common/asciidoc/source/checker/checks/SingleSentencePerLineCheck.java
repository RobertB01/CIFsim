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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.escet.common.asciidoc.source.checker.AsciiDocSourceCheckContext;
import org.eclipse.escet.common.asciidoc.source.checker.AsciiDocSourceLine;

/** Checks that there is a single sentence per line. */
public class SingleSentencePerLineCheck extends AsciiDocSourceFileCheck {
    @Override
    public void check(AsciiDocSourceCheckContext context) {
        // Compile regular expression patterns.
        Pattern endOfSentencePattern = Pattern.compile("[a-z](?<eos>[.!?]) +[A-Z]"); // Heuristic pattern.
        Pattern commentLinePattern = Pattern.compile("^ *(//)");

        // Check each normal line.
        for (AsciiDocSourceLine numberedLine: context.normalLines) {
            // Ignore comment lines.
            if (commentLinePattern.matcher(numberedLine.line).find()) {
                continue;
            }

            // Check line.
            Matcher matcher = endOfSentencePattern.matcher(numberedLine.line);
            while (matcher.find()) {
                int column = matcher.start("eos") + 1;
                context.addProblem(numberedLine.lineNr, column, "Multiple sentences on the same source line.");
            }
        }
    }
}
