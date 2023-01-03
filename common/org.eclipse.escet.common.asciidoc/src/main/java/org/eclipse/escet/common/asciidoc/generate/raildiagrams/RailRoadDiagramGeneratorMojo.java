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

package org.eclipse.escet.common.asciidoc.generate.raildiagrams;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.escet.common.raildiagrams.OutputFormat;
import org.eclipse.escet.common.raildiagrams.RailRoadDiagramGenerator;

/**
 * Railroad diagram generator Maven mojo.
 *
 * @see RailRoadDiagramGenerator
 */
@Mojo(name = "generate-railroad-diagrams")
public class RailRoadDiagramGeneratorMojo extends AbstractMojo {
    /** The path to the root directory that contains the railroad diagram specification files. */
    @Parameter(required = true)
    private File rootPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();

        // Search for AsciiDoc files.
        List<Path> inputPaths;
        try {
            Predicate<Path> isRailRoadFile = p -> p.getFileName().toString().toLowerCase(Locale.US).endsWith(".rr");
            inputPaths = Files.walk(rootPath.toPath()).filter(Files::isRegularFile).filter(isRailRoadFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error(e);
            throw new MojoExecutionException("Failed to search for railroad diagram specification files.", e);
        }

        // Generate images for the railroad diagram specifications.
        log.info(String.format(Locale.US, "Generating %d railroad diagram image(s).", inputPaths.size()));
        for (Path inputPath: inputPaths) {
            // Derive paths.
            Path configPath = inputPath.resolveSibling(inputPath.getFileName().toString() + ".props");
            if (!Files.isRegularFile(configPath)) {
                configPath = null;
            }
            Path outputPath = inputPath.resolveSibling(inputPath.getFileName().toString() + ".png");

            // Generate image.
            try {
                RailRoadDiagramGenerator.generate(inputPath, configPath, outputPath, OutputFormat.IMAGES,
                        log.isDebugEnabled() ? log::debug : null, log::warn);
            } catch (Throwable e) {
                log.error(e);
                throw new MojoExecutionException("Error while executing Maven plugin.", e);
            }
        }
    }
}
