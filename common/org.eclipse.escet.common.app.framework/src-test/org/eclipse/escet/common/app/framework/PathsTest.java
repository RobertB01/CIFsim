//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework;

import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link Paths} class. */
@SuppressWarnings("javadoc")
public class PathsTest {
    @BeforeEach
    public void before() {
        AppEnv.registerSimple();
    }

    @AfterEach
    public void after() {
        AppEnv.unregisterApplication();
    }

    @Test
    public void testSeparators() {
        assertEquals(IS_OS_WINDOWS ? '\\' : '/', File.separatorChar);
        assertEquals(IS_OS_WINDOWS ? '\\' : '/', Paths.getPlatformSeparator());
        assertEquals(IS_OS_WINDOWS ? '/' : '\\', Paths.getNonPlatformSeparator());
    }

    @Test
    public void testIsAbsolute() {
        assertEquals(true, Paths.isAbsolute("/"));
        assertEquals(true, Paths.isAbsolute("\\"));
        assertEquals(true, Paths.isAbsolute("/a"));
        assertEquals(true, Paths.isAbsolute("\\a"));
        assertEquals(true, Paths.isAbsolute("/a/"));
        assertEquals(true, Paths.isAbsolute("\\a\\"));
        assertEquals(true, Paths.isAbsolute("/a\\"));
        assertEquals(true, Paths.isAbsolute("\\a/"));
        assertEquals(true, Paths.isAbsolute("a:\\"));
        assertEquals(true, Paths.isAbsolute("a:/"));
        assertEquals(true, Paths.isAbsolute("c:\\"));
        assertEquals(true, Paths.isAbsolute("c:/"));
        assertEquals(true, Paths.isAbsolute("a:\\d"));
        assertEquals(true, Paths.isAbsolute("a:/d"));
        assertEquals(true, Paths.isAbsolute("c:\\d"));
        assertEquals(true, Paths.isAbsolute("c:/d"));

        assertEquals(false, Paths.isAbsolute("."));
        assertEquals(false, Paths.isAbsolute("a"));
        assertEquals(false, Paths.isAbsolute("a/"));
        assertEquals(false, Paths.isAbsolute("a\\"));
        assertEquals(false, Paths.isAbsolute("a/b"));
        assertEquals(false, Paths.isAbsolute("a\\b"));
        assertEquals(false, Paths.isAbsolute("a/b/"));
        assertEquals(false, Paths.isAbsolute("a\\b\\"));
        assertEquals(false, Paths.isAbsolute("a/b\\"));
        assertEquals(false, Paths.isAbsolute("a\\b/"));
    }

    @Test
    public void testResolveAbsolute() {
        String[][] tests = {
                // workdir, absPath/expected
                {"c:\\dummy", "c:\\some\\path"}, //
                {"c:\\dummy", "/some/path"}, //
                {"/dummy", "c:\\some\\path"}, //
                {"/dummy", "/some/path"}, //
        };

        for (String[] test: tests) {
            assertEquals(2, test.length);
            String workdir = test[0];
            String absPath = test[1];
            String expected = absPath.replace(Paths.getNonPlatformSeparator(), Paths.getPlatformSeparator());
            assertEquals(expected, Paths.resolve(absPath, workdir));
        }
    }

    @Test
    public void testResolveRelative() {
        String[][] tests = {
                // workdir, relPath, expected
                {"c:\\dummy", "a/b", "c:\\dummy\\a\\b"}, //
                {"c:\\dummy", "a\\b", "c:\\dummy\\a\\b"}, //
                {"/dummy", "a/b", "/dummy/a/b"}, //
                {"/dummy", "a\\b", "/dummy/a/b"}, //
        };

        for (String[] test: tests) {
            assertEquals(3, test.length);
            String workdir = test[0];
            String absPath = test[1];
            String expected = test[2];
            expected = expected.replace(Paths.getNonPlatformSeparator(), Paths.getPlatformSeparator());
            assertEquals(expected, Paths.resolve(absPath, workdir));
        }
    }

    @Test
    public void testJoin() {
        String[][][] tests = {
                // input, output
                {
                        {"a", "b"},
                        {"a/b"}},
                {
                        {"a/b", "c/d"},
                        {"a/b/c/d"}},
                {
                        {".", "a/b"},
                        {"a/b"}},
                {
                        {"a", "."},
                        {"a"}},
                {
                        {"a/", "."},
                        {"a"}},

                {
                        {"a/", "b"},
                        {"a/b"}},
                {
                        {"a", "/b"},
                        {"a/b"}}, // "/b" is relative!
                {
                        {"a//", "b"},
                        {"a/b"}},
                {
                        {"a", "b//c"},
                        {"a/b/c"}},
                {
                        {"../../a", "../../b"},
                        {"../../../b"}},

                {
                        {"a/b", "c/../d"},
                        {"a/b/d"}},
                {
                        {"a", "b/"},
                        {"a/b"}},
                {
                        {"a", "b//"},
                        {"a/b"}},
                {
                        {"", "."},
                        {"."}},
                {
                        {"", "a"},
                        {"a"}},

                {
                        {"a", ""},
                        {"a"}},
                {
                        {"", "."},
                        {"."}},
                {
                        {"", ""},
                        {"."}},
                {
                        {"a", "/"},
                        {"a"}},
                {
                        {"", "/"},
                        {"."}},

                {
                        {".", "/"},
                        {"."}},
                {
                        {".", "."},
                        {"."}},
                {
                        {"././a/./", "././b/."},
                        {"a/b"}},
                {
                        {"./../a/b", "../c/./"},
                        {"../a/c"}},
                {
                        {".", ".."},
                        {".."}},

                {
                        {"..", ".."},
                        {"../.."}},
                {
                        {"..", "."},
                        {".."}},

                {
                        {"/", "."},
                        {"/"}},
                {
                        {"/", "a"},
                        {"/a"}},
                {
                        {"/a", "b/c"},
                        {"/a/b/c"}},
                {
                        {"/", ".."},
                        {"/.."}},
                {
                        {"/a", "../b"},
                        {"/b"}},
                {
                        {"/a", "../../b"},
                        {"/../b"}},

                {
                        {"//", "."},
                        {"//"}},
                {
                        {"//", "a"},
                        {"//a"}},
                {
                        {"//a", "b/c"},
                        {"//a/b/c"}},
                {
                        {"//", ".."},
                        {"//.."}},
                {
                        {"//a", "../b"},
                        {"//b"}},
                {
                        {"//a", "../../b"},
                        {"//../b"}},

                {
                        {"c:/", "."},
                        {"c:/"}},
                {
                        {"c:/", "a"},
                        {"c:/a"}},
                {
                        {"c:/a", "b/c"},
                        {"c:/a/b/c"}},
                {
                        {"c:/", ".."},
                        {"c:/.."}},
                {
                        {"c:/a", "../b"},
                        {"c:/b"}},
                {
                        {"c:/a", "../../b"},
                        {"c:/../b"}}, //
        };

        for (int i = 0; i < tests.length; i++) {
            // Get inputs and expected output.
            String[][] test = tests[i];
            assertEquals(2, test.length);
            assertEquals(1, test[1].length);
            String[] inputs1 = test[0];
            String expectedOutput = test[1][0].replace('/', File.separatorChar);

            // Get inputs with flipped separators.
            String[] inputs2 = new String[inputs1.length];
            for (int j = 0; j < inputs1.length; j++) {
                inputs2[j] = inputs1[j].replace('/', '\\');
            }

            // Get actual output for both cases.
            String actualOutput1 = Paths.join(inputs1);
            String actualOutput2 = Paths.join(inputs2);

            // Get test messages.
            String msg1 = fmt("Case %d: join(%s)", i, Arrays.deepToString(inputs1));
            String msg2 = fmt("Case %d: join(%s)", i, Arrays.deepToString(inputs2));

            // Test it.
            assertEquals(expectedOutput, actualOutput1, msg1);
            assertEquals(expectedOutput, actualOutput2, msg2);
        }
    }

    @Test
    public void testCreateJavaUri() {
        String absPath = IS_OS_WINDOWS ? "c:\\a\\b.c" : "/a/b.c";
        String expected = IS_OS_WINDOWS ? "file:/c:/a/b.c" : "file:/a/b.c";
        assertEquals(expected, Paths.createJavaURI(absPath).toString());
    }

    @Test
    public void testCreateEmfUriAbsFilePath() {
        String absPath = IS_OS_WINDOWS ? "c:\\a\\b.c" : "/a/b.c";
        String expected = IS_OS_WINDOWS ? "file:/c:/a/b.c" : "file:/a/b.c";
        assertEquals(expected, Paths.createEmfURI(absPath).toString());
    }

    @Test
    public void testGetRelativePath() {
        String[][] tests = {
                // target rel dir result
                {"/", "/", "."},
                {"/", "//", "."},
                {"//", "/", "."},
                {"//", "//", "."},

                {"/", "/a", ".."},
                {"/", "/a/", ".."},
                {"/a", "/", "a"},
                {"/a/", "/", "a"},
                {"/a", "/a", "."},
                {"/a/", "/a/", "."},

                {"/a/b", "/a/b", "."},
                {"/a/c", "/a/b", "../c"},
                {"/a/b", "/a/c", "../b"},
                {"/a/c", "/a/c", "."},

                {"/a//b", "/a/b", "."},
                {"/a/c", "/a//b", "../c"},
                {"/a//b", "/a/c", "../b"},
                {"/a//c", "/a//c", "."},

                {"/a/b/c", "/a/b/c", "."},
                {"/a/b/c", "/a/b/d", "../c"},
                {"/a/b/c", "/a/d/c", "../../b/c"},
                {"/a/b/c", "/a/d/e", "../../b/c"},
                {"/a/b/c", "/d/b/c", "../../../a/b/c"},
                {"/a/b/c", "/d/e/f", "../../../a/b/c"},

                {"/a/b/c", "/a/d/e", "../../b/c"},
                {"/a/b/c/", "/a/d/e", "../../b/c"},
                {"/a/b/c", "/a/d/e/", "../../b/c"},
                {"/a/b/c/", "/a/d/e/", "../../b/c"},

                {"/", "/a/b", "../.."},
                {"//", "/a/b", "../.."},
                {"/a", "/a/c", ".."},
                {"/b", "/a/c", "../../b"},
                {"/b", "/a//c", "../../b"},

                {"/a/b", "/", "a/b"},
                {"/a/b", "//", "a/b"},
                {"/a/c", "/a", "c"},
                {"/a/c", "/b", "../a/c"},
                {"/a//c", "/b", "../a/c"},

                {"c:/", "c:/", "."},
                {"c:/a", "c:/", "a"},
                {"c:/", "c:/a", ".."},
                {"c:/a", "c:/a", "."},

                {"c:/", "d:/", "c:/"},
                {"c:/a", "d:/", "c:/a"},
                {"c:/", "d:/a", "c:/"},
                {"c:/a", "d:/a", "c:/a"},

                {"c:/a//b", "d:/", "c:/a/b"},
                {"c://a/c", "d:/", "c:/a/c"}, //
        };

        for (String[] test: tests) {
            assertEquals(3, test.length);
            String tgt1 = test[0];
            String tgt2 = tgt1.replace('/', '\\');
            String rel1 = test[1];
            String rel2 = rel1.replace('/', '\\');
            String expected = test[2];
            for (String tgt: new String[] {tgt1, tgt2}) {
                for (String rel: new String[] {rel1, rel2}) {
                    String actual = Paths.getRelativePath(tgt, rel);
                    String msg = fmt("getRelativePath(%s, %s)", tgt, rel);
                    assertEquals(expected, actual, msg);
                }
            }
        }
    }

    @Test
    public void testGetAbsFilePathDir() {
        String[][] tests = { //
                {"/a.txt", "/"},
                {"/b/a.txt", "/b"},
                {"/c/b/a.txt", "/c/b"},
                {"c:\\a.txt", "c:\\"},
                {"c:\\b\\a.txt", "c:\\b"},
                {"d:\\c\\b\\a.txt", "d:\\c\\b"},
                //
        };

        for (String[] test: tests) {
            assertEquals(2, test.length);
            String absFilePath = test[0];
            String expAbsDirPath = test[1];
            String realAbsDirPath = Paths.getAbsFilePathDir(absFilePath);
            String msg = fmt("getAbsFilePathDir(%s) = %s != %s", absFilePath, realAbsDirPath, expAbsDirPath);
            assertEquals(expAbsDirPath, realAbsDirPath, msg);
        }
    }

    @Test
    public void testGetFileName() {
        String[][] tests = { //
                {"/a.txt", "a.txt"},
                {"/b/a.txt", "a.txt"},
                {"/c/b/a.txt", "a.txt"},
                {"c:\\a.txt", "a.txt"},
                {"c:\\b\\a.txt", "a.txt"},
                {"d:\\c\\b\\a.txt", "a.txt"},

                {"/a", "a"},
                {"/b/a", "a"},
                {"/c/b/a", "a"},
                {"c:\\a", "a"},
                {"c:\\b\\a", "a"},
                {"d:\\c\\b\\a", "a"},

                {"a", "a"},
                {"a.txt", "a.txt"},
                //
        };

        for (String[] test: tests) {
            assertEquals(2, test.length);
            String filePath = test[0];
            String expFileName = test[1];
            String realFileName = Paths.getFileName(filePath);
            String msg = fmt("getFileName(%s) = %s != %s", filePath, realFileName, expFileName);
            assertEquals(expFileName, realFileName, msg);
        }
    }
}
