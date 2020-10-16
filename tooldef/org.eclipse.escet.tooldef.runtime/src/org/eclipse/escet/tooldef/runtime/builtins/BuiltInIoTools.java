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

package org.eclipse.escet.tooldef.runtime.builtins;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.tooldef.runtime.ToolDefException;

/** ToolDef built-in I/O tools. */
public class BuiltInIoTools {
    /** Constructor for the {@link BuiltInIoTools} class. */
    private BuiltInIoTools() {
        // Static class.
    }

    /**
     * Prints {@link BuiltInDataTools#fmtToolDef formatted text} (based on a pattern and arguments) to stdout.
     *
     * @param pattern The format pattern.
     * @param args The arguments.
     * @throws ToolDefException If the format pattern is invalid or if the pattern and arguments don't match.
     */
    public static void out(String pattern, Object... args) {
        String text = BuiltInDataTools.fmtToolDef(pattern, args);
        AppEnv.getStreams().out.print(text);
    }

    /**
     * Prints {@link BuiltInDataTools#fmtToolDef formatted text} (based on a pattern and arguments) to stderr.
     *
     * @param pattern The format pattern.
     * @param args The arguments.
     * @throws ToolDefException If the format pattern is invalid or if the pattern and arguments don't match.
     */
    public static void err(String pattern, Object... args) {
        String text = BuiltInDataTools.fmtToolDef(pattern, args);
        AppEnv.getStreams().err.print(text);
    }

    /**
     * Prints {@link BuiltInDataTools#fmtToolDef formatted text} (based on a pattern and arguments) to stdout, printing
     * an additional new line at the end.
     *
     * @param pattern The format pattern.
     * @param args The arguments.
     * @throws ToolDefException If the format pattern is invalid or if the pattern and arguments don't match.
     */
    public static void outln(String pattern, Object... args) {
        String text = BuiltInDataTools.fmtToolDef(pattern, args);
        AppEnv.getStreams().out.println(text);
    }

    /**
     * Prints {@link BuiltInDataTools#fmtToolDef formatted text} (based on a pattern and arguments) to stderr, printing
     * an additional new line at the end.
     *
     * @param pattern The format pattern.
     * @param args The arguments.
     * @throws ToolDefException If the format pattern is invalid or if the pattern and arguments don't match.
     */
    public static void errln(String pattern, Object... args) {
        String text = BuiltInDataTools.fmtToolDef(pattern, args);
        AppEnv.getStreams().err.println(text);
    }
}
