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

package org.eclipse.escet.common.asciidoc;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.single;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Lists;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

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
     *     </ul>
     * @throws IOException In case of an I/O error.
     */
    public static void main(String[] args) throws IOException {
        Assert.check(args.length == 3);

        Path sourceRootPath = Paths.get(args[0]);
        Path singleHtmlPagePath = Paths.get(args[1]);
        Path outputRootPath = Paths.get(args[2]);

        splitHtml(sourceRootPath, singleHtmlPagePath, outputRootPath);
    }

    /**
     * Split single AsciiDoc-generated HTML file into multiple HTML files, matching the content and directory structure
     * of the AsciiDoc source files from which the single HTML file was generated.
     *
     * @param sourceRootPath The path to the root directory that contains the AsciiDoc source files.
     * @param singleHtmlPagePath The path to the single AsciiDoc-generated HTML file.
     * @param outputRootPath The path to the directory in which to write output. Is removed if it already exists. Is
     *     created if it does not yet exist.
     * @throws IOException In case of an I/O error.
     */
    public static void splitHtml(Path sourceRootPath, Path singleHtmlPagePath, Path outputRootPath) throws IOException {
        // Check inputs exist.
        Assert.check(Files.isDirectory(sourceRootPath));
        Assert.check(Files.isRegularFile(singleHtmlPagePath));

        // Ensure empty directory for output.
        if (Files.isDirectory(outputRootPath)) {
            FileUtils.deleteDirectory(outputRootPath.toFile());
        }
        Files.createDirectories(outputRootPath);

        // Read and parse single AsciiDoc-generated HTML file.
        System.out.println("Reading AsciiDoc-generated HTML file: " + singleHtmlPagePath.toString());
        String generatedHtmlContent = Files.readString(singleHtmlPagePath, StandardCharsets.UTF_8);
        Document generatedHtmlDoc = Jsoup.parse(generatedHtmlContent);

        // Find and analyze AsciiDoc source files.
        System.out.println("Analyzing source files: " + sourceRootPath.toString());
        sourceRootPath = sourceRootPath.toAbsolutePath().normalize();
        List<Path> sourcePaths = getSourcePaths(sourceRootPath);
        List<SourceFile> sourceFiles = analyzeSourceFiles(sourceRootPath, sourcePaths);
        System.out.println(sourceFiles.size() + " source file(s) analyzed.");

        // Generate multiple HTML files, one per source file.
        System.out.println("Generating adapted/splitted HTML files at: " + outputRootPath.toString());
        for (SourceFile sourceFile: sourceFiles) {
            // Get the adapted HTML.
            Document sourceFileHtmlDoc = generatedHtmlDoc.clone();
            adaptGeneratedHtmlForSourceFile(sourceFileHtmlDoc, sourceFile, sourceFiles, sourceRootPath);

            // Write HTML.
            Path outputPath = outputRootPath.resolve(sourceFile.relPath);
            outputPath = sourcePathToOutputPath(outputPath);
            Files.createDirectories(outputPath.getParent());
            Files.writeString(outputPath, sourceFileHtmlDoc.outerHtml(), StandardCharsets.UTF_8);
        }

        // Copy single AsciiDoc-generated HTML file to output directory as well.
        System.out.println("Copying single-page HTML file to: " + outputRootPath.toString());
        Path singlePathOutputPath = outputRootPath.resolve("index-single-page.html");
        Assert.check(!Files.exists(singlePathOutputPath));
        Files.copy(singleHtmlPagePath, singlePathOutputPath);

        // Done.
        System.out.println("DONE: HTML file(s) generated.");
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
     * Analyze AsciiDoc source files.
     *
     * @param sourceRootPath The path to the root directory that contains the AsciiDoc source files.
     * @param sourcePaths The AsciiDoc source file paths.
     * @return Information about the AsciiDoc source files.
     * @throws IOException In case of an I/O error.
     */
    private static List<SourceFile> analyzeSourceFiles(Path sourceRootPath, List<Path> sourcePaths) throws IOException {
        List<SourceFile> sourceFiles = listc(sourcePaths.size());
        for (Path sourcePath: sourcePaths) {
            // Debug.
            System.out.println("Analyzing source file: " + sourcePath);

            // Skip special files.
            String fileName = sourcePath.getFileName().toString();
            if (fileName.toString().startsWith("_")) {
                Assert.check(
                        fileName.equals("_part_attributes.asciidoc") || fileName.equals("_root_attributes.asciidoc"));
                continue;
            }

            // Initialize data structure.
            SourceFile sourceFile = new SourceFile();
            sourceFile.absPath = sourcePath.toAbsolutePath().normalize();
            sourceFile.relPath = sourceRootPath.relativize(sourcePath);
            sourceFiles.add(sourceFile);
            sourceFile.isRootIndexFile = sourcePath.getParent().equals(sourceRootPath)
                    && sourcePath.getFileName().toString().equals("index.asciidoc");

            // Read source content.
            List<String> sourceContent = Files.readAllLines(sourcePath, StandardCharsets.UTF_8);

            // Get page title and source id.
            if (sourceFile.isRootIndexFile) {
                sourceFile.sourceId = null;
                sourceFile.title = "Home";
            } else {
                // Get source id.
                int idIndex = IntStream.range(0, sourceContent.size())
                        .filter(i -> sourceContent.get(i).startsWith("[[")).findFirst().getAsInt();
                String idLine = sourceContent.get(idIndex);
                Assert.check(idLine.endsWith("]]"));
                sourceFile.sourceId = Strings.slice(idLine, 2, -2); // [[id]]

                // Get title.
                int titleIndex = IntStream.range(0, sourceContent.size())
                        .filter(i -> sourceContent.get(i).startsWith("=")).findFirst().getAsInt();
                String titleLine = sourceContent.get(titleIndex);
                Assert.check(titleLine.startsWith("== "));
                sourceFile.title = titleLine.substring(3); // == Title

                // Sanity check: source id is the id for the page title header.
                Assert.check(idIndex + 1 == titleIndex);

                // Sanity check: no markup in id/title.
                Assert.check(sourceFile.sourceId.matches("[a-z-]+"), sourceFile.sourceId);
                Assert.check(sourceFile.title.matches("([a-zA-Z0-9 ]|[a-zA-Z][\\-/][a-zA-Z])+"), sourceFile.title);

                // Sanity check: stripped title.
                Assert.check(sourceFile.title.equals(sourceFile.title.strip()), sourceFile.title);
            }
        }

        // Return the information.
        return sourceFiles;
    }

    /**
     * Adapt single AsciiDoc-generated HTML file to match an AsciiDoc source file.
     *
     * @param doc The HTML document to modify in-place.
     * @param sourceFile The AsciiDoc source file for which to modify the HTML document.
     * @param sourceFiles All AsciiDoc source files.
     * @param sourceRootPath The absolute path to the root directory that contains all the source files, and includes
     *     the root 'index.asciidoc' file.
     */
    private static void adaptGeneratedHtmlForSourceFile(Document doc, SourceFile sourceFile,
            List<SourceFile> sourceFiles, Path sourceRootPath)
    {
        // Debug.
        System.out.println("Generating adapted/splitted HTML file for: " + sourceFile.relPath);

        // Adapt HTML page title.
        String docOriginalTitle = doc.title();
        doc.title(sourceFile.title + " | " + docOriginalTitle);

        // Adapt TOC title.
        Element elemTocTitle = single(doc.select("div#toctitle"));
        elemTocTitle.text(docOriginalTitle);

        // Move title/copyright/version from HTML body to footer. Not needed for root index file.
        if (!sourceFile.isRootIndexFile) {
            Element elemBodyFooterText = single(doc.select("#footer-text"));
            elemBodyFooterText.prependElement("br");

            // Move copyright/version.
            Element elemBodyCopyrightVersion = single(doc.select("#header div.details"));
            elemBodyCopyrightVersion.remove();
            Assert.check(elemBodyCopyrightVersion.children().size() == 3);
            Elements elemBodyCopyrightVersionSpans = elemBodyCopyrightVersion.children().select("span");
            Assert.check(elemBodyCopyrightVersionSpans.size() == 2);
            elemBodyCopyrightVersionSpans.removeAttr("id");
            elemBodyCopyrightVersionSpans.removeAttr("class");
            for (Element elem: Lists.reverse(elemBodyCopyrightVersionSpans)) {
                elemBodyFooterText.prependChild(elem);
                elemBodyFooterText.prepend(" | ");
            }

            // Move title.
            Element elemBodyTitle = single(doc.select("#header h1"));
            Assert.check(elemBodyTitle.children().isEmpty());
            elemBodyTitle.tagName("span");
            elemBodyFooterText.prependChild(elemBodyTitle);
        }

        // Partition HTML file 'content' for the AsciiDoc source files.
        // We do this again for every source file, as each source file has a clone of the original HTML document.
        partitionContent(doc, sourceFiles);

        // Remove all content outside the source file content (outside the page). Not needed for root index file.
        Element elemContent = single(doc.select("#content"));
        if (!sourceFile.isRootIndexFile) {
            // Remove all content.
            elemContent.children().remove();

            // Re-add content for this source file.
            for (Node node: sourceFile.nodes) {
                elemContent.appendChild(node);
            }
        }

        // Remove content from other source files. Removes sub-pages.
        for (SourceFile otherSourceFile: sourceFiles) {
            if (otherSourceFile != sourceFile && !otherSourceFile.isRootIndexFile) {
                for (Node node: otherSourceFile.nodes) {
                    node.remove();
                }
            }
        }

        // Remove empty sections.
        for (int i = 0; i < 99; i++) {
            for (Element elem: elemContent.select("div.sect" + Integer.toString(i))) {
                if (elem.children().isEmpty()) {
                    elem.remove();
                }
            }
        }

        // Normalize content headers. Not needed for root index file.
        if (!sourceFile.isRootIndexFile) {
            // Find minimum header number.
            Pattern headerPattern = Pattern.compile("h(\\d+)");
            int minHeaderNr = Integer.MAX_VALUE;
            for (Element elem: elemContent.getAllElements()) {
                Matcher matcher = headerPattern.matcher(elem.tagName());
                if (matcher.matches()) {
                    int headerNr = Integer.parseInt(matcher.group(1), 10);
                    minHeaderNr = Math.min(headerNr, minHeaderNr);
                }
            }
            Assert.check(minHeaderNr > 0);
            Assert.check(minHeaderNr < Integer.MAX_VALUE);

            // Normalize header numbers to ensure minimum header number is '2'.
            for (Element elem: elemContent.getAllElements()) {
                Matcher matcher = headerPattern.matcher(elem.tagName());
                if (matcher.matches()) {
                    int headerNr = Integer.parseInt(matcher.group(1), 10);
                    int newHeaderNr = headerNr - minHeaderNr + 2;
                    elem.tagName("h" + newHeaderNr);
                }
            }
        }

        // Update 'a.href' references. This must be done after partitioning.
        LOOP_A_ELEMS:
        for (Element aElem: doc.select("a")) {
            String href = aElem.attr("href");
            if (href == null || href.isBlank()) {
                throw new RuntimeException("Invalid 'a.href': " + sourceFile.relPath.toString());
            }

            // Handle '#' references, originally pointing to within the single AsciiDoc-generated HTML file.
            if (href.startsWith("#")) {
                String id = href.substring(1);
                if (sourceFile.ids.contains(id)) {
                    continue; // Still within this HTML file.
                }
                for (SourceFile otherSourceFile: sourceFiles) {
                    if (otherSourceFile.ids.contains(id)) {
                        String newHref = getFileOrSectionHref(sourceFile, otherSourceFile, id);
                        aElem.attr("href", newHref);
                        continue LOOP_A_ELEMS;
                    }
                }
                Assert.fail("No source file found that defines 'a.href' id: " + id);
            }

            // Get referenced URI. Skip 'http' and 'https' references etc.
            URI uri = URI.create(href);
            String uriScheme = uri.getScheme();
            if ("http".equals(uriScheme) || "https".equals(uriScheme)) {
                continue;
            }

            // Handle relative paths.
            Assert.check(uriScheme == null, uriScheme);
            Assert.check(uri.getUserInfo() == null, uri.getUserInfo());
            Assert.check(uri.getHost() == null, uri.getHost());
            Assert.check(uri.getPort() == -1, String.valueOf(uri.getPort()));
            Assert.check(uri.getAuthority() == null, uri.getAuthority());
            Assert.check(uri.getQuery() == null, uri.getQuery());
            Assert.check(uri.getFragment() == null, uri.getFragment());
            Assert.notNull(uri.getPath());
            Assert.check(href.equals(uri.getPath()));
            String hrefAbsTarget = org.eclipse.escet.common.app.framework.Paths.resolve(href,
                    sourceRootPath.toString());
            String rootPathForNewRelHref = sourceFile.absPath.getParent().toString();
            String newRelHref = org.eclipse.escet.common.app.framework.Paths.getRelativePath(hrefAbsTarget,
                    rootPathForNewRelHref);
            Assert.check(!newRelHref.contains("\\"));
            aElem.attr("href", newRelHref);
        }

        // Update 'img.src' references. This must be done after partitioning.
        for (Element aElem: doc.select("img")) {
            String src = aElem.attr("src");
            if (src == null || src.isBlank()) {
                throw new RuntimeException("Invalid 'img.src': " + sourceFile.relPath.toString());
            }

            // Handle relative paths.
            URI uri = URI.create(src);
            Assert.check(uri.getScheme() == null, uri.getScheme());
            Assert.check(uri.getUserInfo() == null, uri.getUserInfo());
            Assert.check(uri.getHost() == null, uri.getHost());
            Assert.check(uri.getPort() == -1, String.valueOf(uri.getPort()));
            Assert.check(uri.getAuthority() == null, uri.getAuthority());
            Assert.check(uri.getQuery() == null, uri.getQuery());
            Assert.check(uri.getFragment() == null, uri.getFragment());
            Assert.notNull(uri.getPath());
            Assert.check(src.equals(uri.getPath()));
            String srcAbsTarget = org.eclipse.escet.common.app.framework.Paths.resolve(src, sourceRootPath.toString());
            String rootPathForNewRelSrc = sourceFile.absPath.getParent().toString();
            String newRelSrc = org.eclipse.escet.common.app.framework.Paths.getRelativePath(srcAbsTarget,
                    rootPathForNewRelSrc);
            Assert.check(!newRelSrc.contains("\\"));
            aElem.attr("src", newRelSrc);
        }

        // Check that all 'link.href' references can be ignored.
        for (Element aElem: doc.select("link")) {
            String href = aElem.attr("href");
            if (href == null || href.isBlank()) {
                throw new RuntimeException("Invalid 'link.href': " + sourceFile.relPath.toString());
            }

            URI uri = URI.create(href);
            String uriScheme = uri.getScheme();
            Assert.check("http".equals(uriScheme) || "https".equals(uriScheme), uriScheme);
        }

        // Add root index file to TOC.
        Element elemTocSectLevel1 = single(doc.select("#toc ul.sectlevel1"));
        Element elemTocHomeLi = elemTocSectLevel1.prependElement("li");
        Element elemTocHomeA = elemTocHomeLi.prependElement("a");
        elemTocHomeA.attr("href", getFileOrSectionHref(sourceFile, first(sourceFiles), null));
        elemTocHomeA.appendText(first(sourceFiles).title);
    }

    /**
     * Partition the 'content' of the AsciiDoc-generated HTML file to the AsciiDoc source files. Also collects all ids
     * for the partitions.
     *
     * @param doc The HTML document for which to partition the 'content'.
     * @param sourceFiles The AsciDoc source files. Are modified in-place.
     */
    private static void partitionContent(Document doc, List<SourceFile> sourceFiles) {
        // Clear old partition.
        for (SourceFile sourceFile: sourceFiles) {
            sourceFile.nodes = list();
        }

        // Get root source file and map ids to other source files.
        Map<String, SourceFile> idToSources = mapc(sourceFiles.size());
        SourceFile rootSourceFile = null;
        for (SourceFile sourceFile: sourceFiles) {
            sourceFile.nodes = list();
            if (sourceFile.isRootIndexFile) {
                Assert.check(rootSourceFile == null, sourceFile.relPath.toString());
                rootSourceFile = sourceFile;
            } else {
                SourceFile prev = idToSources.put(sourceFile.sourceId, sourceFile);
                Assert.check(prev == null);
            }
        }
        Assert.notNull(rootSourceFile);
        Assert.check(idToSources.size() + 1 == sourceFiles.size());

        // Walk over HTML 'content' and assign all nodes to a single source file.
        Deque<Pair<SourceFile, Integer>> stack = new LinkedList<>();
        stack.push(pair(rootSourceFile, 0));

        Element elemContent = single(doc.select("#content"));
        elemContent.children().traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                // Detect new source file.
                if (node instanceof Element) {
                    String id = ((Element)node).id();
                    SourceFile elemSourceFile = idToSources.get(id);
                    if (elemSourceFile != null) {
                        stack.push(pair(elemSourceFile, depth));
                    }
                }

                // Collect id. Store for current source file.
                if (node instanceof Element) {
                    String id = ((Element)node).id();
                    if (!id.isBlank()) {
                        stack.peek().left.ids.add(id);
                    }
                }

                // Detect sibling node of other nodes of current source file. This ensures we don't collect all nodes,
                // but only 'root' ones, not all descendants of 'root' nodes.
                if (depth == stack.peek().right) {
                    stack.peek().left.nodes.add(node);
                }
            }

            @Override
            public void tail(Node node, int depth) {
                // Detect end of source file.
                if (depth < stack.peek().right) {
                    stack.pop();
                }
            }
        });
        Assert.check(stack.size() == 1);

        // Ensure content for each source file.
        for (SourceFile sourceFile: sourceFiles) {
            Assert.check(!sourceFile.nodes.isEmpty(), sourceFile.relPath.toString());
        }
    }

    /**
     * Get an 'href' value from a given source file to a section in another source file. If the section in the other
     * source file is the first section, an href to the entire file is returned. Otherwise, a reference to the section
     * within the file is returned.
     *
     * @param sourceFile The source file from which the 'href' is referenced.
     * @param otherSourceFile The other source file to which the 'href' should point.
     * @param id The id of the section in the other source file to which to refer, or {@code null} to refer to the
     *     entire file.
     * @return The 'href' value.
     */
    private static String getFileOrSectionHref(SourceFile sourceFile, SourceFile otherSourceFile, String id) {
        Path relPath = sourceFile.absPath.getParent().relativize(otherSourceFile.absPath);
        relPath = sourcePathToOutputPath(relPath);
        String href = relPath.toString().replace('\\', '/');
        if (id != null) {
            String firstId = otherSourceFile.ids.isEmpty() ? null : otherSourceFile.ids.iterator().next();
            if (!Objects.equals(id, firstId)) { // Add section id if not the page title id.
                href += "#" + id;
            }
        }
        return href;
    }

    /**
     * Converts an AsciiDoc source file path into a generated output HTML file path.
     *
     * @param sourcePath The AsciiDoc source file path.
     * @return The generated output HTML file path.
     */
    private static Path sourcePathToOutputPath(Path sourcePath) {
        String fileName = sourcePath.getFileName().toString();
        Assert.check(fileName.endsWith(".asciidoc"));
        fileName = Strings.slice(fileName, 0, -".asciidoc".length());
        fileName += ".html";
        return sourcePath.resolveSibling(fileName);
    }

    /** Information about an AsciiDoc source file. */
    private static class SourceFile {
        /** The absolute path of the source file. */
        Path absPath;

        /** The relative path of the source file, from the directory that contains the root 'index.asciidoc' file. */
        Path relPath;

        /**
         * The id of the first header in the source file. Is {@code null} for the {@link #isRootIndexFile root index
         * file}.
         */
        String sourceId;

        /**
         * The title of the first header in the source file, and thus the title of the source file itself. Is
         * {@code "Home"} for the {@link #isRootIndexFile root index file}.
         */
        String title;

        /** Is this source file the root 'index.asciidoc' file ({@code true}) or not ({@code false})? */
        boolean isRootIndexFile;

        /** The unique ids defined in the generated HTML 'content' for this source file, in order. */
        Set<String> ids = set();

        /**
         * The 'content' nodes in the single AsciiDoc-generated HTML file, associated with this source file. Is
         * recomputed for each new HTML file, to ensure the proper cloned nodes are present.
         */
        List<Node> nodes;
    }
}
