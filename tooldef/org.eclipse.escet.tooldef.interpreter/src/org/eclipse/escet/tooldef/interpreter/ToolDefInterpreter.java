//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.runtime.ExitException;

/** ToolDef interpreter. */
public class ToolDefInterpreter {
    /** Constructor for the {@link ToolDefInterpreter} class. */
    private ToolDefInterpreter() {
        // Static class.
    }

    /**
     * Executes a ToolDef script.
     *
     * @param script The ToolDef script to execute.
     * @param path The absolute local file system path to the script. The path contains separators for the current
     *     platform.
     * @param app The ToolDef interpreter application.
     * @return The exit code.
     */
    public static int execute(Script script, String path, ToolDefInterpreterApp app) {
        // Store script path. Used by the 'scriptpath' built-in tool.
        final String propName = "org.eclipse.escet.tooldef.interpreter.scriptpath";
        AppEnv.setProperty(propName, path);

        // Create new execution context.
        ExecContext ctxt = new ExecContext(app);

        // Execute statements.
        int exitCode;
        try {
            List<Declaration> statements = script.getDeclarations();
            ToolDefReturnValue retValue = ToolDefExec.execute(statements, ctxt);
            Assert.check(retValue == null);
            exitCode = 0;
        } catch (ExitException ex) {
            exitCode = ex.exitCode;
        }

        // Produce correct exit code.
        return exitCode;
    }
}
