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

package org.eclipse.escet.common.asciidoc.html.multipage;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

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
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.eclipse.escet.common.app.framework.XmlSupport;
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
     *     <li>Name of the parent website to link to, if generating the website.</li>
     *     <li>Relative path of the parent website to link to, if generating the website.</li>
     *     </ul>
     * @throws IOException In case of an I/O error.
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Command line arguments: " + Arrays.toString(args));
        Assert.check(args.length == 4 || args.length == 6, args);

        Path sourceRootPath = Paths.get(args[0]);
        Path singleHtmlPagePath = Paths.get(args[1]);
        Path outputRootPath = Paths.get(args[2]);

        String htmlTypeText = args[3];
        Assert.check(htmlTypeText.startsWith("--"));
        htmlTypeText = htmlTypeText.substring(2).replace("-", "_").toUpperCase(Locale.US);
        HtmlType htmlType = HtmlType.valueOf(htmlTypeText);

        String parentWebsiteName = null;
        String parentWebsiteLink = null;
        switch (htmlType) {
            case ECLIPSE_HELP:
                Assert.check(args.length == 4, args);
                break;
            case WEBSITE:
                Assert.check(args.length == 6, args);
                parentWebsiteName = args[4];
                parentWebsiteLink = args[5];
                break;
            default:
                throw new RuntimeException("Unknown HTML type: " + htmlType);
        }

        String rootBaseName = singleHtmlPagePath.getFileName().toString();
        Assert.check(rootBaseName.endsWith(".html"));
        rootBaseName = Strings.slice(rootBaseName, null, -".html".length());

        splitHtml(sourceRootPath, singleHtmlPagePath, outputRootPath, htmlType, parentWebsiteName, parentWebsiteLink,
                rootBaseName, System.out::println);
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
     * @param parentWebsiteName The name of the parent website to link to, if {@code htmlType} is
     *     {@link HtmlType#WEBSITE}, {@code null} otherwise.
     * @param parentWebsiteLink The relative path of the parent website to link to, if {@code htmlType} is
     *     {@link HtmlType#WEBSITE}, {@code null} otherwise.
     * @param rootBaseName The base name (file name excluding file extension) of the root AsciiDoc file.
     * @param logger The logger to use.
     * @throws IOException In case of an I/O error.
     */
    public static void splitHtml(Path sourceRootPath, Path singleHtmlPagePath, Path outputRootPath, HtmlType htmlType,
            String parentWebsiteName, String parentWebsiteLink, String rootBaseName, Consumer<String> logger)
            throws IOException
    {
        // Check arguments.
        Assert.areEqual(parentWebsiteName != null, htmlType == HtmlType.WEBSITE);
        Assert.areEqual(parentWebsiteLink != null, htmlType == HtmlType.WEBSITE);

        // Check inputs exist.
        Assert.check(Files.isDirectory(sourceRootPath), sourceRootPath);
        Assert.check(Files.isRegularFile(singleHtmlPagePath), singleHtmlPagePath);

        // Ensure empty directory for output.
        if (Files.isDirectory(outputRootPath)) {
            FileUtils.deleteDirectory(outputRootPath.toFile());
        }
        Files.createDirectories(outputRootPath);

        // Read and parse AsciiDoc-generated single-page HTML file.
        logger.accept("Reading AsciiDoc-generated single-page HTML file: " + singleHtmlPagePath.toString());
        String generatedHtmlContent = Files.readString(singleHtmlPagePath, StandardCharsets.UTF_8);
        Document singlePageDoc = Jsoup.parse(generatedHtmlContent);

        // Find AsciiDoc source files.
        logger.accept("Finding and analyzing AsciiDoc source files located in: " + sourceRootPath.toString());
        sourceRootPath = sourceRootPath.toAbsolutePath().normalize();
        List<Path> sourcePaths = getSourcePaths(sourceRootPath);

        // Analyze AsciiDoc source files.
        List<AsciiDocSourceFile> sourceFiles = listc(sourcePaths.size());
        for (Path sourcePath: sourcePaths) {
            AsciiDocSourceFile sourceFile;
            try {
                sourceFile = AsciiDocSourceFileAnalyzer.analyze(sourceRootPath, sourcePath, rootBaseName);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to analyze AsciiDoc source file: " + sourcePath, e);
            }
            if (sourceFile != null) {
                sourceFiles.add(sourceFile);
            }
        }
        logger.accept(fmt("%d AsciiDoc source files found, %d analyzed.", sourcePaths.size(), sourceFiles.size()));

        // Analyze AsciiDoc-generated single-page HTML file.
        logger.accept("Analyzing AsciiDoc-generated single-page HTML file: " + singleHtmlPagePath.toString());
        AsciiDocHtmlPages htmlPages = new AsciiDocHtmlPages(sourceFiles);
        Assert.areEqual(htmlPages.homePage.sourceFile.getBaseName(), rootBaseName);
        AsciiDocHtmlAnalyzer.analyze(singlePageDoc, htmlPages);

        // Generate and write multiple HTML files, one per page.
        logger.accept("Generating multi-page HTML files at: " + outputRootPath.toString());
        AsciiDocHtmlModifier.generateAndWriteModifiedPages(singlePageDoc, htmlPages, sourceRootPath, outputRootPath,
                htmlType, parentWebsiteName, parentWebsiteLink, logger);

        // Copy single AsciiDoc-generated HTML file to output directory, with different name.
        if (htmlType == HtmlType.WEBSITE) {
            logger.accept("Copying single-page HTML file to: " + outputRootPath.toString());
            Path singlePathOutputPath = outputRootPath.resolve(rootBaseName + "-single-page.html");
            Assert.check(!Files.exists(singlePathOutputPath), singlePathOutputPath);
            Files.copy(singleHtmlPagePath, singlePathOutputPath);
        }

        // Generate Eclipse help 'toc.xml' file.
        if (htmlType == HtmlType.ECLIPSE_HELP) {
            Path tocXmlPath = outputRootPath.resolve("toc.xml");
            logger.accept("Generating Eclipse help TOC at: " + tocXmlPath.toString());
            org.w3c.dom.Document tocXmlDoc = AsciiDocEclipseHelpTocUtil.tocToEclipseHelpXml(htmlPages.toc);
            XmlSupport.writeFile(tocXmlDoc, "TOC", tocXmlPath.toString());
        }

        // Done.
        logger.accept("DONE: AsciiDoc multi-page HTML split completed.");
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
}
