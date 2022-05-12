//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.output.DebugImageOutput;
import org.eclipse.escet.common.raildiagrams.output.NormalImageOutput;
import org.eclipse.escet.common.raildiagrams.output.OutputTarget;
import org.eclipse.escet.common.raildiagrams.parser.RailRoadDiagramParser;
import org.eclipse.escet.common.raildiagrams.railroad.RailRule;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;
import org.eclipse.escet.common.raildiagrams.util.Size2D;
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.SyntaxWarning;

/** Generator to generate railroad diagram images. */
public class RailRoadDiagramGenerator {
    /** Constructor for the {@link RailRoadDiagramGenerator} class. */
    private RailRoadDiagramGenerator() {
        // Static class.
    }

    /**
     * Main method for the railroad diagram generator.
     *
     * @param args The command line arguments:
     *     <ul>
     *     <li>The path to the input file with the railroad diagram specification.</li>
     *     <li>The path to the output image file.</li>
     *     <li>The requested output format, per {@link OutputFormat#toString}.</li>
     *     <li>Optionally, the path to the configuration file to use. If not provided, the default configuration is
     *     used.</li>
     *     </ul>
     * @throws IOException In case of an I/O error.
     */
    public static void main(String[] args) throws IOException {
        // Process command line arguments.
        if (args.length != 3 && args.length != 4) {
            System.err.printf("Expected 3 or 4 command line arguments, but got %d.%n", args.length);
            System.exit(1);
        }
        Path inputPath = Paths.get(args[0]);
        Path outputPath = Paths.get(args[1]);
        OutputFormat outputFormat;
        try {
            outputFormat = OutputFormat.valueOf(Strings.makeUppercase(args[2]).replace('-', '_'));
        } catch (IllegalArgumentException e) {
            System.err.printf("Invalid output type: %s%n", args[2]);
            System.exit(1);
            return; // Never reached.
        }
        Path configPath = (args.length == 3) ? null : Paths.get(args[3]);

        // Generate railroad diagram image.
        generate(inputPath, configPath, outputPath, outputFormat, null, System.err::println);
    }

    /**
     * Generate railroad diagram image.
     *
     * @param inputPath The path to the input file with the railroad diagram specification.
     * @param configPath The path to the configuration file to use, or {@code null} to use the default configuration.
     * @param outputPath The path to the output image file.
     * @param outputFormat The requested output format.
     * @param debugLogger The debug logger to use, or {@code null} to not log debug output.
     * @param warningLogger The warning logger to use.
     * @throws IOException In case of an I/O error.
     */
    public static void generate(Path inputPath, Path configPath, Path outputPath, OutputFormat outputFormat,
            Consumer<String> debugLogger, Consumer<String> warningLogger) throws IOException
    {
        // Setup output target and configuration.
        OutputTarget outputTarget;
        switch (outputFormat) {
            case DBG_IMAGES:
                outputTarget = new DebugImageOutput();
                break;
            case IMAGES:
                outputTarget = new NormalImageOutput();
                break;
            default:
                throw new AssertionError("Unknown output format found: " + outputFormat);
        }
        Configuration config = new Configuration(outputTarget, debugLogger);

        // Load the configuration file.
        if (configPath != null) {
            try {
                config.loadPropertiesFile(configPath);
            } catch (IOException e) {
                throw new IOException(fmt("Failed to read configuration file \"%s\".", configPath), e);
            }
        }

        // Read the input.
        String inputText;
        try {
            inputText = Files.readString(inputPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException(fmt("Failed to read railroad diagram specification \"%s\".", inputPath), e);
        }

        // Parse the input.
        // A file may contain several rules, which are assumed to belong together in one diagram.
        RailRoadDiagramParser parser = new RailRoadDiagramParser();
        List<RailRule> rules = parser.parseString(inputText, inputPath.toAbsolutePath().toString(),
                inputPath.getFileName().toString(), DebugMode.NONE);
        for (SyntaxWarning warning: parser.getWarnings()) {
            warningLogger.accept(warning.toString());
        }

        // Generate a graphic collection for it.
        //
        // First, compute required size for all boxes in the diagram.
        // Boxes are put under each other.
        int diagramWidth = 0;
        int diagramHeight = 0;
        for (RailRule rule: rules) {
            rule.create(config, 1);
            Size2D size = rule.getSize();
            diagramWidth = Math.max(diagramWidth, size.width);
            diagramHeight += Math.ceil(size.height);
        }

        // Second, position everything and generate the graphic elements.
        int width = (int)Math.ceil(diagramWidth);
        int height = (int)Math.ceil(diagramHeight);

        Color bgColor = config.getRgbColor("diagram.background.color");
        outputTarget.prepareOutputFile(width, height, bgColor);

        // Paint graphics to the image.
        boolean dumpAbsCoords = config.getDebugSetting(DebugDisplayKind.ABS_COORDINATES);
        int top = 0;
        for (RailRule rule: rules) {
            rule.paint(0, top, outputTarget, dumpAbsCoords, config);
            Size2D size = rule.getSize();
            top += Math.ceil(size.height);
        }

        // Write the image.
        outputTarget.writeOutputFile(outputPath);
    }
}
