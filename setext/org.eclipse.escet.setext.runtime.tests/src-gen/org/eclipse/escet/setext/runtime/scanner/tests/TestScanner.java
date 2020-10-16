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

// Disable Eclipse Java formatter for generated code file:
// @formatter:off

package org.eclipse.escet.setext.runtime.scanner.tests;

import java.io.IOException;

import org.eclipse.escet.setext.runtime.Scanner;
import org.eclipse.escet.setext.runtime.Token;

/**
 * TestScanner.
 *
 * <p>This scanner is generated by SeText.</p>
 */
public final class TestScanner extends Scanner {
    /** Textual representations of the scanner states, for debugging. */
    private static final String[] SCANNER_STATES = new String[] {
        "", // 0
        "BLOCK_COMMENT", // 1
    };

    /** For each terminal, whether it needs post processing. */
    private static final boolean[] TERMINAL_NEEDS_POST = new boolean[] {
        false, // 0
        true, // 1
        false, // 2
        true, // 3
        false, // 4
        true, // 5
        false, // 6
        false, // 7
    };

    /** Textual representations of the terminals, for debugging. */
    private static final String[] TERMINALS = new String[] {
        "TERM1=\"a*b\"", // 0
        "TERM2=\"a*bc\"", // 1
        "\"\\n+\"", // 2
        "\"/\\*\"", // 3
        "\"\u00B6\"", // 4
        "\"\\*/\"", // 5
        "\".\"", // 6
        "\"\\n\"", // 7
    };

    /** Names of the terminals (may be {@code null}), for exceptions. */
    private static final String[] TERMINAL_NAMES = new String[] {
        "TERM1", // 0
        "TERM2", // 1
        null, // 2
        null, // 3
        null, // 4
        null, // 5
        null, // 6
        null, // 7
    };

    /** Descriptions of the terminals (may be {@code null}), for exceptions. */
    private static final String[] TERMINAL_DESCRIPTIONS = new String[] {
        "descr1", // 0
        "descr2", // 1
        null, // 2
        "\"/*\"", // 3
        "end-of-file", // 4
        "\"*/\"", // 5
        null, // 6
        null, // 7
    };

    /** Scanner call back hook methods. */
    public final TestScannerHooks hooks;

    /** The current DFA state of the scanner. */
    private int state;

    /** Constructor for the {@link TestScanner} class. */
    public TestScanner() {
        scannerStates = SCANNER_STATES;
        terminalNeedsPost = TERMINAL_NEEDS_POST;
        terminals = TERMINALS;
        terminalNames = TERMINAL_NAMES;
        terminalDescriptions = TERMINAL_DESCRIPTIONS;
        hooks = new TestScannerHooks();
    }

    @Override
    public final Token nextTokenInternal() throws IOException {
        switch (scannerState) {
            case 0: state = 0; break;
            case 1: state = 8; break;
            default:
                String msg = "Unknown scanner state: " + scannerState;
                throw new RuntimeException(msg);
        }
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
                    rslt = nextToken1(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 2:
                    rslt = nextToken2(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 3:
                    rslt = nextToken3(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 4:
                    rslt = nextToken4(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 5:
                    return acceptOrError();
                case 6:
                    return acceptOrError();
                case 7:
                    return acceptOrError();
                // Scanner state "BLOCK_COMMENT".
                case 8:
                    rslt = nextToken8(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 9:
                    rslt = nextToken9(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 10:
                    return acceptOrError();
                case 11:
                    return acceptOrError();
                case 12:
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
                state = 1;
                break;
            case 'b':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 0;
                state = 2;
                break;
            case '\n':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 2;
                state = 3;
                break;
            case '/':
                state = 4;
                break;
            case -1:
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 4;
                state = 5;
                break;
            default:
                return acceptOrError();
        }
        if (debugScanner) {
            debugScanner(codePoint, state);
        }
        return null;
    }

    @SuppressWarnings("javadoc")
    private final Token nextToken1(int codePoint) {
        switch (codePoint) {
            case 'a':
                break;
            case 'b':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 0;
                state = 2;
                break;
            default:
                return acceptOrError();
        }
        if (debugScanner) {
            debugScanner(codePoint, state);
        }
        return null;
    }

    @SuppressWarnings("javadoc")
    private final Token nextToken2(int codePoint) {
        switch (codePoint) {
            case 'c':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 1;
                state = 7;
                break;
            default:
                return acceptOrError();
        }
        if (debugScanner) {
            debugScanner(codePoint, state);
        }
        return null;
    }

    @SuppressWarnings("javadoc")
    private final Token nextToken3(int codePoint) {
        switch (codePoint) {
            case '\n':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 2;
                break;
            default:
                return acceptOrError();
        }
        if (debugScanner) {
            debugScanner(codePoint, state);
        }
        return null;
    }

    @SuppressWarnings("javadoc")
    private final Token nextToken4(int codePoint) {
        switch (codePoint) {
            case '*':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 3;
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

    @SuppressWarnings("javadoc")
    private final Token nextToken8(int codePoint) {
        switch (codePoint) {
            case '*':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 6;
                state = 9;
                break;
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case '\t':
            case 11:
            case 12:
            case '\r':
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case ' ':
            case '!':
            case '"':
            case '#':
            case '$':
            case '%':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case '{':
            case '|':
            case '}':
            case '~':
            case 127:
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 6;
                state = 10;
                break;
            case '\n':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 7;
                state = 11;
                break;
            default:
                return acceptOrError();
        }
        if (debugScanner) {
            debugScanner(codePoint, state);
        }
        return null;
    }

    @SuppressWarnings("javadoc")
    private final Token nextToken9(int codePoint) {
        switch (codePoint) {
            case '/':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 5;
                state = 12;
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
                hooks.changeCtoZ(token);
                return;
            case 2:
                return;
            case 3:
                scannerState = 1;
                return;
            case 4:
                return;
            case 5:
                scannerState = 0;
                return;
            case 6:
                return;
            case 7:
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
        String msg = "Unknown keyword category: " + keywordCategory;
        throw new IllegalArgumentException(msg);
    }

    /** Scanner call back hooks for {@link TestScanner}. */
    public interface Hooks {
        /**
         * Call back hook "changeCtoZ" for {@link TestScanner}.
         * May perform in-place modifications to the scanned text of the token.
         *
         * @param token The scanned token.
         */
        public void changeCtoZ(Token token);
    }
}
