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

package org.eclipse.escet.common.asciidoc.source.checker;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

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
        List<String> actualOutput = new ArrayList<>();
        AsciiDocSourceChecker.checkSources(List.of(inputFilePath), actualOutput::add);

        // Get expected output.
        Path outputFilePath = Paths.get("src").resolve("test").resolve("java").resolve(resourceBaseName + ".out");
        List<String> expectedOutput = Files.readAllLines(outputFilePath, StandardCharsets.UTF_8);

        // Compare expected/actual output.
        assertEquals(String.join("\n", expectedOutput), String.join("\n", actualOutput));
    }
}
