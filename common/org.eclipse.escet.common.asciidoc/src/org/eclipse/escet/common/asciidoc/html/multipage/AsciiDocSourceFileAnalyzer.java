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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** AsciiDoc source file analyzer. */
class AsciiDocSourceFileAnalyzer {
    /** Constructor for the {@link AsciiDocSourceFileAnalyzer} class. */
    private AsciiDocSourceFileAnalyzer() {
        // Static class.
    }

    /**
     * Analyze AsciiDoc source file.
     *
     * @param sourceRootPath The path to the root directory that contains the AsciiDoc source files.
     * @param sourcePath The AsciiDoc source file path.
     * @param rootBaseName The base name (file name without file extension) of the root AsciiDoc file.
     * @return Information about the AsciiDoc source file, or {@code null} if source file was skipped.
     * @throws IOException In case of an I/O error.
     */
    static AsciiDocSourceFile analyze(Path sourceRootPath, Path sourcePath, String rootBaseName) throws IOException {
        // Skip special files.
        String fileName = sourcePath.getFileName().toString();
        if (fileName.toString().startsWith("_")) {
            Assert.check(fileName.equals("_root_attributes.asciidoc") || fileName.equals("_part_attributes.asciidoc")
                    || fileName.equals("_local_attributes.asciidoc"), fileName);
            return null;
        }

        // Read source content.
        List<String> sourceContent = Files.readAllLines(sourcePath, StandardCharsets.UTF_8);

        // Get paths and determine whether it is the root AsciiDoc file.
        Path absPath = sourcePath.toAbsolutePath().normalize();
        Path relPath = sourceRootPath.relativize(sourcePath);
        boolean isRootAsciiDocFile = sourcePath.getParent().equals(sourceRootPath)
                && sourcePath.getFileName().toString().equals(rootBaseName + ".asciidoc");

        // Get page title and source id.
        String sourceId;
        String title;
        if (isRootAsciiDocFile) {
            sourceId = null;
            title = "Home";
        } else {
            // Get source id.
            int idIndex = IntStream.range(0, sourceContent.size()).filter(i -> sourceContent.get(i).startsWith("[["))
                    .findFirst().getAsInt();
            String idLine = sourceContent.get(idIndex);
            Assert.check(idLine.endsWith("]]"), idLine);
            sourceId = Strings.slice(idLine, 2, -2); // [[id]]

            // Sanity check: no markup in id.
            Assert.check(sourceId.matches("[a-z0-9-]+"), sourceId);

            // Get title.
            int titleIndex = IntStream.range(0, sourceContent.size()).filter(i -> sourceContent.get(i).startsWith("="))
                    .findFirst().getAsInt();
            String titleLine = sourceContent.get(titleIndex);
            Assert.check(titleLine.startsWith("== "), titleLine);
            title = titleLine.substring(3); // == Title

            // Sanity check: source id is the id for the page title header.
            Assert.areEqual(idIndex + 1, titleIndex);

            // Sanitize title.
            // - Check for balanced backticks and remove backticks.
            Assert.check(StringUtils.countMatches(title, "`") % 2 == 0, title);
            title = title.replace("`", "");

            // Sanity check: stripped title.
            Assert.areEqual(title, title.strip());

            // Sanity check: no markup in title.
            String patternTitleWordNormalChars = "[a-zA-Z0-9, ]";
            String patternTitleWordWithSpecialChar = "[a-zA-Z0-9][\\-/'][a-zA-Z0-9]";
            String patternTitleWord = fmt("%s|%s", patternTitleWordNormalChars, patternTitleWordWithSpecialChar);
            String patternTitleWordWithParentheses = fmt("\\((%s)+\\)", patternTitleWord);
            String patternTitle = fmt("(%s|%s)+", patternTitleWord, patternTitleWordWithParentheses);
            Assert.check(title.matches(patternTitle), title);
        }

        // Return the information.
        return new AsciiDocSourceFile(absPath, relPath, sourceId, title, isRootAsciiDocFile);
    }
}
