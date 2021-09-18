//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.asciidoc.html.multipage;

import static org.eclipse.escet.common.java.Lists.listc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * AsciiDoc multi-page HTML splitter.
 *
 * <p>
 * Splits a single AsciiDoc-generated HTML file into multiple HTML files, matching the content and directory structure
 * of the AsciiDoc source files from which the single HTML file was generated.
 * </p>
 *
 * <p>
 * This class is meant for application to Eclipse ESCET documentation projects, and may not be suitable for all
 * AsciiDoc-generated HTML files.
 * </p>
 */
public class AsciiDocMultiPageHtmlSplitter {
    /** Constructor for the {@link AsciiDocMultiPageHtmlSplitter} class. */
    private AsciiDocMultiPageHtmlSplitter() {
        // Static class.
    }

    /**
     * Main method of the AsciiDoc HTML splitter.
     *
     * @param args The command line arguments:
     *     <ul>
     *     <li>The path to the root directory that contains the AsciiDoc source files.</li>
     *     <li>The path to the single AsciiDoc-generated HTML file.</li>
     *     <li>The path to the directory in which to write output. Is removed if it already exists. Is created if it
     *     does not yet exist.</li>
     *     <li>{@code --eclipse-help} for Eclipse help HTML or {@code --website} for website HTML.</li>
     *     </ul>
     * @throws IOException In case of an I/O error.
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Command line arguments: " + Arrays.toString(args));
        Assert.check(args.length == 4, args.toString());

        Path sourceRootPath = Paths.get(args[0]);
        Path singleHtmlPagePath = Paths.get(args[1]);
        Path outputRootPath = Paths.get(args[2]);

        String htmlTypeText = args[3];
        Assert.check(htmlTypeText.startsWith("--"));
        htmlTypeText = htmlTypeText.substring(2).replace("-", "_").toUpperCase(Locale.US);
        HtmlType htmlType = HtmlType.valueOf(htmlTypeText);

        splitHtml(sourceRootPath, singleHtmlPagePath, outputRootPath, htmlType);
    }

    /**
     * Split single AsciiDoc-generated HTML file into multiple HTML files, matching the content and directory structure
     * of the AsciiDoc source files from which the single HTML file was generated.
     *
     * @param sourceRootPath The path to the root directory that contains the AsciiDoc source files.
     * @param singleHtmlPagePath The path to the single AsciiDoc-generated HTML file.
     * @param outputRootPath The path to the directory in which to write output. Is removed if it already exists. Is
     *     created if it does not yet exist.
     * @param htmlType The HTML type.
     * @throws IOException In case of an I/O error.
     */
    public static void splitHtml(Path sourceRootPath, Path singleHtmlPagePath, Path outputRootPath, HtmlType htmlType)
            throws IOException
    {
        // Check inputs exist.
        Assert.check(Files.isDirectory(sourceRootPath), sourceRootPath.toString());
        Assert.check(Files.isRegularFile(singleHtmlPagePath), singleHtmlPagePath.toString());

        // Ensure empty directory for output.
        if (Files.isDirectory(outputRootPath)) {
            FileUtils.deleteDirectory(outputRootPath.toFile());
        }
        Files.createDirectories(outputRootPath);

        // Read and parse single AsciiDoc-generated HTML file.
        System.out.println("Reading AsciiDoc-generated single-page HTML file: " + singleHtmlPagePath.toString());
        String generatedHtmlContent = Files.readString(singleHtmlPagePath, StandardCharsets.UTF_8);
        Document generatedHtmlDoc = Jsoup.parse(generatedHtmlContent);

        // Find and analyze AsciiDoc source files.
        System.out.println("Analyzing AsciiDoc source files: " + sourceRootPath.toString());
        sourceRootPath = sourceRootPath.toAbsolutePath().normalize();
        List<Path> sourcePaths = getSourcePaths(sourceRootPath);
        List<AsciiDocSourceFile> sourceFiles = listc(sourcePaths.size());
        for (Path sourcePath: sourcePaths) {
            AsciiDocSourceFile sourceFile;
            try {
                sourceFile = AsciiDocSourceFileAnalyzer.analyzeSourceFile(sourceRootPath, sourcePath);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to analyze AsciiDoc source file: " + sourcePath, e);
            }
            if (sourceFile != null) {
                sourceFiles.add(sourceFile);
            }
        }
        System.out.println(sourceFiles.size() + " AsciiDoc source file(s) analyzed.");

        // Generate multiple HTML files, one per source file.
        System.out.println("Generating adapted/splitted HTML files at: " + outputRootPath.toString());
        for (AsciiDocSourceFile sourceFile: sourceFiles) {
            // Get the adapted HTML.
            Document sourceFileHtmlDoc = generatedHtmlDoc.clone();
            try {
                AsciiDocHtmlAdaptor.adaptGeneratedHtmlForSourceFile(sourceFileHtmlDoc, sourceFile, sourceFiles,
                        sourceRootPath, htmlType);
            } catch (Throwable e) {
                throw new RuntimeException(
                        "Failed to adapt generated HTML for AsciiDoc source file: " + sourceFile.absPath, e);
            }

            // Write HTML.
            Path outputPath = outputRootPath.resolve(sourceFile.relPath);
            outputPath = sourcePathToOutputPath(outputPath);
            Files.createDirectories(outputPath.getParent());
            Files.writeString(outputPath, sourceFileHtmlDoc.outerHtml(), StandardCharsets.UTF_8);
        }

        // Copy single AsciiDoc-generated HTML file to output directory as well.
        if (htmlType == HtmlType.WEBSITE) {
            System.out.println("Copying single-page HTML file to: " + outputRootPath.toString());
            Path singlePathOutputPath = outputRootPath.resolve("index-single-page.html");
            Assert.check(!Files.exists(singlePathOutputPath), singlePathOutputPath.toString());
            Files.copy(singleHtmlPagePath, singlePathOutputPath);
        }

        // Done.
        System.out.println("DONE: AsciiDoc multi-page HTML split completed.");
    }

    /**
     * Returns the AsciiDoc source file paths.
     *
     * @param sourceRootPath The path to the root directory that contains the AsciiDoc source files.
     * @return The AsciiDoc source file paths.
     * @throws IOException In case of an I/O error.
     */
    private static List<Path> getSourcePaths(Path sourceRootPath) throws IOException {
        BiPredicate<Path, BasicFileAttributes> filter = (p, a) -> p.getFileName().toString().endsWith(".asciidoc");
        List<Path> sourcePaths;
        try (Stream<Path> pathStream = Files.find(sourceRootPath, Integer.MAX_VALUE, filter)) {
            sourcePaths = pathStream.collect(Collectors.toList());
        }
        return sourcePaths;
    }

    /**
     * Converts an AsciiDoc source file path into a generated output HTML file path.
     *
     * @param sourcePath The AsciiDoc source file path.
     * @return The generated output HTML file path.
     */
    static Path sourcePathToOutputPath(Path sourcePath) {
        String fileName = sourcePath.getFileName().toString();
        Assert.check(fileName.endsWith(".asciidoc"), fileName);
        fileName = Strings.slice(fileName, 0, -".asciidoc".length());
        fileName += ".html";
        return sourcePath.resolveSibling(fileName);
    }
}
