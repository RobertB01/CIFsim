//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.runtime.parser;

import java.io.IOException;

import org.eclipse.escet.setext.runtime.Scanner;
import org.eclipse.escet.setext.runtime.Token;

/**
 * CalcTestScanner.
 *
 * <p>This scanner is generated by SeText.</p>
 */
public final class CalcTestScanner extends Scanner {
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
        false, // 6
        false, // 7
        false, // 8
        false, // 9
        false, // 10
        false, // 11
        false, // 12
    };

    /** Textual representations of the terminals, for debugging. */
    private static final String[] TERMINALS = new String[] {
        "PIKW=\"pi\"", // 0
        "NAME=\"[a-zA-Z_][a-zA-Z0-9_]*\"", // 1
        "NUMBER=\"[0-9]+\"", // 2
        "PLUS=\"\\+\"", // 3
        "MINUS=\"\\-\"", // 4
        "TIMES=\"\\*\"", // 5
        "DIVIDE=\"/\"", // 6
        "EQUALS=\"=\"", // 7
        "LPAREN=\"\\(\"", // 8
        "RPAREN=\"\\)\"", // 9
        "SEMICOL=\";\"", // 10
        "\"[ \\t\\n]\"", // 11
        "\"\u00B6\"", // 12
    };

    /** Names of the terminals (may be {@code null}), for exceptions. */
    private static final String[] TERMINAL_NAMES = new String[] {
        "PIKW", // 0
        "NAME", // 1
        "NUMBER", // 2
        "PLUS", // 3
        "MINUS", // 4
        "TIMES", // 5
        "DIVIDE", // 6
        "EQUALS", // 7
        "LPAREN", // 8
        "RPAREN", // 9
        "SEMICOL", // 10
        null, // 11
        null, // 12
    };

    /** Descriptions of the terminals (may be {@code null}), for exceptions. */
    private static final String[] TERMINAL_DESCRIPTIONS = new String[] {
        "\"pi\"", // 0
        "a name", // 1
        "an integer literal", // 2
        "\"+\"", // 3
        "\"-\"", // 4
        "\"*\"", // 5
        "\"/\"", // 6
        "\"=\"", // 7
        "\"(\"", // 8
        "\")\"", // 9
        "\";\"", // 10
        null, // 11
        "end-of-file", // 12
    };

    /** The current DFA state of the scanner. */
    private int state;

    /** Constructor for the {@link CalcTestScanner} class. */
    public CalcTestScanner() {
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
                    return acceptOrError();
                case 5:
                    return acceptOrError();
                case 6:
                    return acceptOrError();
                case 7:
                    return acceptOrError();
                case 8:
                    return acceptOrError();
                case 9:
                    return acceptOrError();
                case 10:
                    return acceptOrError();
                case 11:
                    return acceptOrError();
                case 12:
                    return acceptOrError();
                case 13:
                    return acceptOrError();
                case 14:
                    rslt = nextToken14(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
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
            case 'p':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 1;
                state = 1;
                break;
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
            case '_':
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
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 1;
                state = 2;
                break;
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
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 2;
                state = 3;
                break;
            case '+':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 3;
                state = 4;
                break;
            case '-':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 4;
                state = 5;
                break;
            case '*':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 5;
                state = 6;
                break;
            case '/':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 6;
                state = 7;
                break;
            case '=':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 7;
                state = 8;
                break;
            case '(':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 8;
                state = 9;
                break;
            case ')':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 9;
                state = 10;
                break;
            case ';':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 10;
                state = 11;
                break;
            case '\t':
            case '\n':
            case ' ':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 11;
                state = 12;
                break;
            case -1:
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 12;
                state = 13;
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
            case '_':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
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
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 1;
                state = 2;
                break;
            case 'i':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 0;
                state = 14;
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
            case '_':
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
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 1;
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
    private final Token nextToken14(int codePoint) {
        switch (codePoint) {
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
            case '_':
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
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 1;
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
            case 6:
                return;
            case 7:
                return;
            case 8:
                return;
            case 9:
                return;
            case 10:
                return;
            case 11:
                return;
            case 12:
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
        if (keywordCategory.equals("Math")) {
            return new String[] {
                "pi",
            };
        }

        String msg = "Unknown keyword category: " + keywordCategory;
        throw new IllegalArgumentException(msg);
    }
}
