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

package org.eclipse.escet.common.raildiagrams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** Generator to generate an image for each railroad diagram file in a directory. */
public class RailRoadDiagramMassGenerator {
    /** Constructor for the {@link RailRoadDiagramMassGenerator} class. */
    private RailRoadDiagramMassGenerator() {
        // Static class.
    }

    /**
     * Main method for the railroad diagram mass generator.
     *
     * @param args The command line arguments:
     *     <ul>
     *     <li>The path to the root directory that contains the railroad diagram specification files.</li>
     *     </ul>
     * @throws IOException In case of an I/O error.
     */
    public static void main(String[] args) throws IOException {
        // Process command line arguments.
        boolean askedHelp = false;
        for (int i = 0; !askedHelp && i < args.length; i++) {
            askedHelp = args[i].equals("-h") || args[i].equals("-help") || args[i].equals("--help");
        }
        if (askedHelp || args.length != 1) {
            System.err.println("Usage: Generator [-h | -help | --help] <diagrams-folder>");
            System.exit(askedHelp ? 0 : 1);
        }
        Path rootPath = Paths.get(args[0]);
        OutputFormat outputFormat = OutputFormat.IMAGES;

        // Generate railroad diagram images.
        Consumer<String> debugOutput = null;
        generate(rootPath, outputFormat, debugOutput, System.out::println, System.err::println);
    }

    /**
     * Generate railroad diagram images.
     *
     * @param rootPath The path to the root directory that contains the railroad diagram specification files.
     * @param outputFormat The requested output format.
     * @param debugLogger The debug logger to use, or {@code null} to not log debug output.
     * @param infoLogger The info logger to use, or {@code null} to not log info output.
     * @param warningLogger The warning logger to use.
     * @throws IOException In case of an I/O error.
     */
    public static void generate(Path rootPath, OutputFormat outputFormat, Consumer<String> debugLogger,
            Consumer<String> infoLogger, Consumer<String> warningLogger) throws IOException
    {
        // Search for railroad diagram specification files.
        List<Path> inputPaths;
        try {
            Predicate<Path> isRailRoadFile = p -> p.getFileName().toString().toLowerCase(Locale.US).endsWith(".rr");
            inputPaths = Files.walk(rootPath).filter(Files::isRegularFile).filter(isRailRoadFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IOException("Failed to search for railroad diagram specification files.", e);
        }

        // Generate images for the railroad diagram specifications.
        infoLogger.accept(String.format(Locale.US, "Generating %d railroad diagram image(s).", inputPaths.size()));
        for (Path inputPath: inputPaths) {
            // Find property files.
            List<Path> configPaths = new ArrayList<>(2);
            Path sharedConfigPath = inputPath.resolveSibling("_shared.rr.props");
            Path specificConfigPath = inputPath.resolveSibling(inputPath.getFileName().toString() + ".props");
            if (Files.isRegularFile(sharedConfigPath)) {
                configPaths.add(sharedConfigPath);
            }
            if (Files.isRegularFile(specificConfigPath)) {
                configPaths.add(specificConfigPath);
            }

            // Set output path.
            Path outputPath = inputPath.resolveSibling(inputPath.getFileName().toString() + ".png");

            // Generate image.
            RailRoadDiagramGenerator.generate(inputPath, configPaths, outputPath, outputFormat, debugLogger,
                    warningLogger);
        }
    }
}
