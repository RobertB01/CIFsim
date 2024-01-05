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

package org.eclipse.escet.setext.runtime;

/** {@link Scanner} and {@link Parser} debug modes. */
public enum DebugMode {
    /** No debugging. */
    NONE,

    /** Scanner-only debugging. */
    SCANNER,

    /** Parser-only debugging. */
    PARSER,

    /** Scanner/parser debugging. */
    BOTH;

    /**
     * Returns a {@link DebugMode} given separate indications of whether to debug the scanner and the parser.
     *
     * @param debugScanner Should the scanner be debugged?
     * @param debugParser Should the parser be debugged?
     * @return The {@link DebugMode}.
     */
    public static DebugMode getDebugMode(boolean debugScanner, boolean debugParser) {
        if (debugScanner && debugParser) {
            return DebugMode.BOTH;
        }
        if (debugScanner) {
            return DebugMode.SCANNER;
        }
        if (debugParser) {
            return DebugMode.PARSER;
        }
        return DebugMode.NONE;
    }
}
