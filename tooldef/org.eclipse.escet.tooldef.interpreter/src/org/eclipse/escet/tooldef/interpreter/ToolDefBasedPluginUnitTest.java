//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.interpreter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Base class for JUnit plug-in tests that execute ToolDef scripts.
 *
 * <p>
 * Use the {@link #test(String)} method to start a test. A test succeeds if the ToolDef script successfully completes
 * execution and has a zero exit code, and fails otherwise. It is assumed that a non-zero exit code indicates the number
 * of failed tests.
 * </p>
 *
 * <p>
 * Example Unit test class:
 *
 * <pre>
 * public class MyTest extends ToolDefBasedPluginUnitTest {
 *     &#64;Test
 *     public void testSomething() {
 *         test("project/relative/path/to/tooldef/test/script.tooldef");
 *     }
 * }
 * </pre>
 * </p>
 */
public abstract class ToolDefBasedPluginUnitTest {
    /**
     * Perform test.
     *
     * @param scriptLocation The project relative path to the ToolDef script file.
     */
    public void test(String scriptLocation) {
        Path scriptPath = Paths.get(scriptLocation).toAbsolutePath().normalize();
        Path scriptDir = scriptPath.getParent();
        String scriptFilename = scriptPath.getFileName().toString();

        ToolDefInterpreterApp app = new ToolDefInterpreterApp();
        app.getAppEnvData().getProperties().set("user.dir", scriptDir.toString());
        int exitCode = app.run(new String[] {scriptFilename}, false);
        if (exitCode != 0) {
            throw new AssertionError(Integer.toString(exitCode) + " tests failed. See console output for details.");
        }
    }
}
