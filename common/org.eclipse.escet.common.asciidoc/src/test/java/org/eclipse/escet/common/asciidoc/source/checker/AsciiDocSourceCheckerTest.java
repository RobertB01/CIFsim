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

package org.eclipse.escet.common.asciidoc.source.checker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/** Base class for AsciiDoc source checker tests. */
public class AsciiDocSourceCheckerTest {
    /**
     * Test single sentence per line.
     *
     * @throws IOException In case of an I/O error.
     */
    @Test
    public void testSingleSentencePerLine() throws IOException {
        test("SingleSentencePerLine");
    }

    /**
     * Test source block indent.
     *
     * @throws IOException In case of an I/O error.
     */
    @Test
    public void testSourceBlockIndent() throws IOException {
        test("SourceBlockIndent");
    }

    /**
     * Perform the test.
     *
     * @param testName The name of the test.
     * @throws IOException In case of an I/O error.
     */
    private void test(String testName) throws IOException {
        // Get input file path.
        String resourceBaseName = getClass().getPackageName().replace(".", "/") + "/data/" + testName;
        Path inputFilePath = Paths.get("src").resolve("test").resolve("java").resolve(resourceBaseName + ".asciidoc");

        // Perform check.
        List<String> actualOutputs = new ArrayList<>();
        AsciiDocSourceChecker.checkSources(List.of(inputFilePath), actualOutputs::add);

        // Get expected output.
        Path outputFilePath = Paths.get("src").resolve("test").resolve("java").resolve(resourceBaseName + ".out");
        List<String> expectedOutputs = Files.readAllLines(outputFilePath, StandardCharsets.UTF_8);

        // Compare expected/actual output.
        String expectedOutput = String.join("\n", expectedOutputs);
        String actualOutput = String.join("\n", actualOutputs);
        actualOutput = normalizeFilePaths(actualOutput);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Normalize file paths.
     *
     * @param text The text to normalize.
     * @return The text with file paths normalized.
     */
    private String normalizeFilePaths(String text) {
        Pattern pattern = Pattern.compile("( - File [a-z/]+)\\\\");
        while (true) {
            Matcher matcher = pattern.matcher(text);
            if (!matcher.find()) {
                break;
            }
            text = matcher.replaceFirst(matcher.group(1) + "/");
        }
        return text;
    }
}
