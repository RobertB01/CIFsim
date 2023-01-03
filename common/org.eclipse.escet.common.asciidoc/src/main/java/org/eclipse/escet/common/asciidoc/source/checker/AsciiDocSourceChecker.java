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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.escet.common.asciidoc.source.checker.checks.SingleSentencePerLineCheck;
import org.eclipse.escet.common.asciidoc.source.checker.checks.SourceBlockIndentCheck;

import com.google.common.base.Verify;

/** AsciiDoc source file checker. */
public class AsciiDocSourceChecker {
    /** Constructor for the {@link AsciiDocSourceChecker} class. */
    private AsciiDocSourceChecker() {
        // Static class.
    }

    /**
     * Main method of the AsciiDoc source file checker.
     *
     * @param args The command line arguments:
     *     <ul>
     *     <li>The path to the root directory that contains the AsciiDoc source files.</li>
     *     </ul>
     * @throws IOException In case of an I/O error.
     */
    public static void main(String[] args) throws IOException {
        Verify.verify(args.length == 1);
        Path sourceRootPath = Paths.get(args[0]);
        checkSources(sourceRootPath, System.out::println);
    }

    /**
     * Check AsciiDoc sources for common problems.
     *
     * @param sourceRootPath The path to the root directory that contains the AsciiDoc source files.
     * @param logger The logger to use.
     * @return The number of problems found.
     * @throws IOException In case of an I/O error.
     */
    public static int checkSources(Path sourceRootPath, Consumer<String> logger) throws IOException {
        // Search for AsciiDoc files.
        List<Path> asciiDocFiles;
        try {
            Predicate<Path> isAsciiDocFile = path -> {
                String fileName = path.getFileName().toString().toLowerCase(Locale.US);
                return fileName.endsWith(".adoc") || fileName.endsWith(".asciidoc");
            };
            asciiDocFiles = Files.walk(sourceRootPath).filter(Files::isRegularFile).filter(isAsciiDocFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IOException("Failed to search for AsciiDoc source files.", e);
        }

        // Check files.
        return checkSources(asciiDocFiles, logger);
    }

    /**
     * Check AsciiDoc sources for common problems.
     *
     * @param asciiDocFiles The paths to the AsciiDoc source files to check.
     * @param logger The logger to use.
     * @return The number of problems found.
     * @throws IOException In case of an I/O error.
     */
    public static int checkSources(List<Path> asciiDocFiles, Consumer<String> logger) throws IOException {
        // Check AsciiDoc files.
        logger.accept(String.format(Locale.US, "Checking %,d AsciiDoc source file(s).", asciiDocFiles.size()));
        List<AsciiDocSourceProblem> problems = new ArrayList<>();
        for (Path asciiDocFile: asciiDocFiles) {
            // Create check context.
            List<String> lines;
            try {
                lines = Files.readAllLines(asciiDocFile, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IOException("Failed to read AsciiDoc source file: " + asciiDocFile, e);
            }
            AsciiDocSourceCheckContext context = new AsciiDocSourceCheckContext(asciiDocFile, lines, problems);

            // Perform checks.
            new SingleSentencePerLineCheck().check(context);
            new SourceBlockIndentCheck().check(context);
        }

        // Report problems.
        if (problems.isEmpty()) {
            logger.accept("No problems found.");
        } else {
            logger.accept(String.format(Locale.US, "Found %,d problems:", problems.size()));
            Collections.sort(problems);
            for (AsciiDocSourceProblem problem: problems) {
                logger.accept(" - " + problem.toString());
            }
        }
        return problems.size();
    }
}
