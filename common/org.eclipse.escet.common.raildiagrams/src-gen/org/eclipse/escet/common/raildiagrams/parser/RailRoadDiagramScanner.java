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

package org.eclipse.escet.common.raildiagrams.parser;

import java.io.IOException;

import org.eclipse.escet.setext.runtime.Scanner;
import org.eclipse.escet.setext.runtime.Token;

/**
 * RailRoadDiagramScanner.
 *
 * <p>This scanner is generated by SeText.</p>
 */
public final class RailRoadDiagramScanner extends Scanner {
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
        true, // 10
        true, // 11
        false, // 12
        false, // 13
        false, // 14
        false, // 15
    };

    /** Textual representations of the terminals, for debugging. */
    private static final String[] TERMINALS = new String[] {
        "SEMI=\";\"", // 0
        "COLON=\":\"", // 1
        "STAR=\"\\*\"", // 2
        "PLUS=\"\\+\"", // 3
        "PIPE=\"\\|\"", // 4
        "QUEST=\"\\?\"", // 5
        "BSLASH_BSLASH=\"\\\\\"", // 6
        "PAROPEN=\"\\(\"", // 7
        "PARCLOSE=\"\\)\"", // 8
        "IDENTIFIER=\"[\\-A-Za-z0-9_]+\"", // 9
        "DQUOTE_STRING=\"\\\"([^\\\\\"\\n]|\\[\\\\\"])*\\\"\"", // 10
        "SQUOTE_STRING=\"'([^\\'\\n]|\\[\\'])*'\"", // 11
        "BR_STRING=\"\\[([^\\\\]\\n]|\\[\\\\]])*\\]\"", // 12
        "\"#.*\"", // 13
        "\"[ \\t\\r\\n]+\"", // 14
        "\"\u00B6\"", // 15
    };

    /** Names of the terminals (may be {@code null}), for exceptions. */
    private static final String[] TERMINAL_NAMES = new String[] {
        "SEMI", // 0
        "COLON", // 1
        "STAR", // 2
        "PLUS", // 3
        "PIPE", // 4
        "QUEST", // 5
        "BSLASH_BSLASH", // 6
        "PAROPEN", // 7
        "PARCLOSE", // 8
        "IDENTIFIER", // 9
        "DQUOTE_STRING", // 10
        "SQUOTE_STRING", // 11
        "BR_STRING", // 12
        null, // 13
        null, // 14
        null, // 15
    };

    /** Descriptions of the terminals (may be {@code null}), for exceptions. */
    private static final String[] TERMINAL_DESCRIPTIONS = new String[] {
        "\";\"", // 0
        "\":\"", // 1
        "\"*\"", // 2
        "\"+\"", // 3
        "\"|\"", // 4
        "\"?\"", // 5
        "\"\\\\\"", // 6
        "\"(\"", // 7
        "\")\"", // 8
        "a name", // 9
        "a double quoted string", // 10
        "a single quoted string", // 11
        "a bracketed label", // 12
        null, // 13
        null, // 14
        "end-of-file", // 15
    };

    /** Scanner call back hook methods. */
    public final RailRoadDiagramHooks hooks;

    /** The current DFA state of the scanner. */
    private int state;

    /** Constructor for the {@link RailRoadDiagramScanner} class. */
    public RailRoadDiagramScanner() {
        scannerStates = SCANNER_STATES;
        terminalNeedsPost = TERMINAL_NEEDS_POST;
        terminals = TERMINALS;
        terminalNames = TERMINAL_NAMES;
        terminalDescriptions = TERMINAL_DESCRIPTIONS;
        hooks = new RailRoadDiagramHooks();
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
                case 7:
                    rslt = nextToken7(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 8:
                    return acceptOrError();
                case 9:
                    return acceptOrError();
                case 10:
                    rslt = nextToken10(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 11:
                    rslt = nextToken11(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 12:
                    rslt = nextToken12(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 13:
                    rslt = nextToken13(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 14:
                    rslt = nextToken14(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 15:
                    rslt = nextToken15(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 16:
                    return acceptOrError();
                case 17:
                    rslt = nextToken17(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 18:
                    return acceptOrError();
                case 19:
                    rslt = nextToken19(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 20:
                    return acceptOrError();
                case 21:
                    rslt = nextToken21(codePoint);
                    if (rslt != null) {
                        return rslt;
                    }
                    break;
                case 22:
                    return acceptOrError();
                case 23:
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
            case ';':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 0;
                state = 1;
                break;
            case ':':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 1;
                state = 2;
                break;
            case '*':
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
            case '|':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 4;
                state = 5;
                break;
            case '?':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 5;
                state = 6;
                break;
            case '\\':
                state = 7;
                break;
            case '(':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 7;
                state = 8;
                break;
            case ')':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 8;
                state = 9;
                break;
            case '-':
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
                accept = 9;
                state = 10;
                break;
            case '"':
                state = 11;
                break;
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 14;
                state = 12;
                break;
            case '#':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 13;
                state = 13;
                break;
            case '\'':
                state = 14;
                break;
            case '[':
                state = 15;
                break;
            case -1:
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 15;
                state = 16;
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
    private final Token nextToken7(int codePoint) {
        switch (codePoint) {
            case '\\':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 6;
                state = 23;
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
    private final Token nextToken10(int codePoint) {
        switch (codePoint) {
            case '-':
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
                accept = 9;
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
    private final Token nextToken11(int codePoint) {
        switch (codePoint) {
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
            case '#':
            case '$':
            case '%':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
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
                break;
            case '\\':
                state = 21;
                break;
            case '"':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 10;
                state = 22;
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
    private final Token nextToken12(int codePoint) {
        switch (codePoint) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 14;
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
    private final Token nextToken13(int codePoint) {
        switch (codePoint) {
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
            case '*':
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
                accept = 13;
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
            case '(':
            case ')':
            case '*':
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
                break;
            case '\\':
                state = 19;
                break;
            case '\'':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 11;
                state = 20;
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
    private final Token nextToken15(int codePoint) {
        switch (codePoint) {
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
            case '*':
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
                break;
            case '\\':
                state = 17;
                break;
            case ']':
                acceptOffset = curOffset;
                acceptLine = curLine;
                acceptColumn = curColumn;
                accept = 12;
                state = 18;
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
    private final Token nextToken17(int codePoint) {
        switch (codePoint) {
            case '\\':
            case ']':
                state = 15;
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
    private final Token nextToken19(int codePoint) {
        switch (codePoint) {
            case '\'':
            case '\\':
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
    private final Token nextToken21(int codePoint) {
        switch (codePoint) {
            case '"':
            case '\\':
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
                hooks.dropOuter(token);
                return;
            case 11:
                hooks.dropOuter(token);
                return;
            case 12:
                return;
            case 13:
                return;
            case 14:
                return;
            case 15:
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

    /** Scanner call back hooks for {@link RailRoadDiagramScanner}. */
    public interface Hooks {
        /**
         * Call back hook "dropOuter" for {@link RailRoadDiagramScanner}.
         * May perform in-place modifications to the scanned text of the token.
         *
         * @param token The scanned token.
         */
        public void dropOuter(Token token);
    }
}