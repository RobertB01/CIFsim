//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

// Disable Eclipse Java formatter for generated code file:
// @formatter:off

package org.eclipse.escet.setext.runtime.expterms;

import java.io.IOException;

import org.eclipse.escet.setext.runtime.Scanner;
import org.eclipse.escet.setext.runtime.Token;

/**
 * ExpectedTermsScanner.
 *
 * <p>This scanner is generated by SeText.</p>
 */
public final class ExpectedTermsScanner extends Scanner {
    /** Textual representations of the scanner states, for debugging. */
    private static final String[] SCANNER_STATES = new String[] {
        "", // 0
    };

    /** For each terminal, whether it needs post processing. */
    private static final boolean[] TERMINAL_NEEDS_POST = new boolean[] {
        false, // 0
        false, // 1
        false, // 2
        false, // 3
        false, // 4
        false, // 5
    };

    /** Textual representations of the terminals, for debugging. */
    private static final String[] TERMINALS = new String[] {
        "AKW=\"a\"", // 0
        "BKW=\"b\"", // 1
        "CKW=\"c\"", // 2
        "DKW=\"d\"", // 3
        "\"[ \\t\\r\\n]\"", // 4
        "\"\u00B6\"", // 5
    };

    /** Names of the terminals (may be {@code null}), for exceptions. */
    private static final String[] TERMINAL_NAMES = new String[] {
        "AKW", // 0
        "BKW", // 1
        "CKW", // 2
        "DKW", // 3
        null, // 4
        null, // 5
    };

    /** Descriptions of the terminals (may be {@code null}), for exceptions. */
    private static final String[] TERMINAL_DESCRIPTIONS = new String[] {
        "\"a\"", // 0
        "\"b\"", // 1
        "\"c\"", // 2
        "\"d\"", // 3
        null, // 4
        "end-of-file", // 5
    };

    /** The current DFA state of the scanner. */
    private int state;

    /** Constructor for the {@link ExpectedTermsScanner} class. */
    public ExpectedTermsScanner() {
        scannerStates = SCANNER_STATES;
        terminalNeedsPost = TERMINAL_NEEDS_POST;
        terminals = TERMINALS;
        terminalNames = TERMINAL_NAMES;
        terminalDescriptions = TERMINAL_DESCRIPTIONS;
    }

    @Override
    public final Token nextTokenInternal() throws IOException {
        state = 0;
        startOffset = curOffset;
        startLine = curLine;
        startColumn = curColumn;
        acceptOffset = -1;
        acceptLine = -1;
        acceptColumn = -1;
        accept = -1;

        while (true) {
            // Read next code point (the one for 'curOffset').
            int codePoint = getNextCodePoint();

            // Process the code point.
            Token rslt;
            switch (state) {
                // Scanner state "".
                case 0:
                    rslt = nextToken0(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 1:
                    return acceptOrError();
                case 2:
                    return acceptOrError();
                case 3:
                    return acceptOrError();
                case 4:
                    return acceptOrError();
                case 5:
                    return acceptOrError();
                case 6:
                    return acceptOrError();
                default:
                    String msg = "Unknown scanner DFA state: " + state;
                    throw new RuntimeException(msg);
            }

            // The code point has been processed. Move on to the next one.
            // Also update line/column tracking information.
            curOffset++;
            if (codePoint == '\n') {
                curLine++;
                curColumn = 1;
            } else {
                curColumn++;
            }
        }
    }

    @SuppressWarnings("javadoc")
    private final Token nextToken0(int codePoint) {
        switch (codePoint) {
            case 'a':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 0;
                state = 1;
                break;
            case 'b':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 1;
                state = 2;
                break;
            case 'c':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 2;
                state = 3;
                break;
            case 'd':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 3;
                state = 4;
                break;
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 4;
                state = 5;
                break;
            case -1:
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 5;
                state = 6;
                break;
            default:
                return acceptOrError();
        }
        if (debugScanner) {
            debugScanner(codePoint, state);
        }
        return null;
    }

    @Override
    protected final void tokenAccepted(Token token) {
        switch (token.id) {
            case 0:
                return;
            case 1:
                return;
            case 2:
                return;
            case 3:
                return;
            case 4:
                return;
            case 5:
                return;
            default:
                throw new RuntimeException("Unknown terminal id: " + token.id);
        }
    }

    /**
     * Returns the keywords in the given category.
     *
     * @param keywordCategory The name of the keyword category.
     * @return The keywords in the given category.
     * @throws IllegalArgumentException If the category does not exist for
     *      this scanner.
     */
    public static String[] getKeywords(String keywordCategory) {
        if (keywordCategory.equals("Keywords")) {
            return new String[] {
                "a",
                "b",
                "c",
                "d",
            };
        }

        String msg = "Unknown keyword category: " + keywordCategory;
        throw new IllegalArgumentException(msg);
    }
}