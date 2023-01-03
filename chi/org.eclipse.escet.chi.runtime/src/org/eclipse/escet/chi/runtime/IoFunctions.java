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

package org.eclipse.escet.chi.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.data.io.ChiFileHandle;
import org.eclipse.escet.common.java.Assert;

/** Wrapper class for the runtime input/output functions. */
public abstract class IoFunctions {
    /**
     * Read integer value from an input stream.
     *
     * @param input Input stream to read from.
     * @return Read integer value.
     * @throws ChiSimulatorException If no integer decimal value found at the stream.
     */
    public static int readInt(ChiFileHandle input) {
        input.skipWhitespace();

        int kar = input.read();
        // Read prefix +/- characters.
        boolean seenNegate = false;
        while (kar == '+' || kar == '-') {
            if (kar == '-') {
                seenNegate = !seenNegate;
            }
            input.markStream();
            kar = input.read();
        }

        // Read the value.
        int value = 0;
        boolean seenDigit = false;
        while (kar >= '0' && kar <= '9') {
            value = value * 10 + kar - '0';
            seenDigit = true;
            input.markStream();
            kar = input.read();
        }
        input.resetStream();

        if (!seenDigit) {
            String msg = "Expected integer number but found no digits while reading input.";
            throw new ChiSimulatorException(msg);
        }

        return seenNegate ? -value : value;
    }

    /**
     * Write integer value to the output stream.
     *
     * @param output Output stream to write to.
     * @param value Value to output.
     */
    public static void writeInt(ChiFileHandle output, int value) {
        output.write(String.valueOf(value));
    }

    /**
     * Read a boolean text literal from the input stream, and return its value.
     *
     * @param input Input stream.
     * @return Value of the read boolean text literal.
     * @throws ChiSimulatorException if no boolean literal value found at the stream.
     */
    public static boolean readBool(ChiFileHandle input) {
        input.skipWhitespace();
        input.markStream(5);

        int kar = input.read();
        if (kar == 't') {
            if (input.read() == 'r' && input.read() == 'u' && input.read() == 'e') {
                return true;
            }
        } else if (kar == 'f') {
            if (input.read() == 'a' && input.read() == 'l' && input.read() == 's' && input.read() == 'e') {
                return false;
            }
        }
        input.resetStream();
        String msg = "Expected boolean literal (true or false) but found other text while reading input.";
        throw new ChiSimulatorException(msg);
    }

    /**
     * Write boolean value to the output stream.
     *
     * @param output Output stream to write to.
     * @param value Value to output.
     */
    public static void writeBool(ChiFileHandle output, boolean value) {
        output.write(String.valueOf(value));
    }

    /**
     * Read a real number text literal from the input stream, and return its value.
     *
     * @param input Input stream.
     * @return Value of the read real number literal.
     * @throws ChiSimulatorException If the text does not contain a real number.
     */
    public static double readReal(ChiFileHandle input) {
        input.skipWhitespace();

        // Initial states: 1
        // Accepting states: 3 4 7 8 10 (3, 4 for integer recognition)
        // Edges:
        // 1 -- "0" -> 3
        // 1 -- "+-" -> 2
        // 1 -- "[1-9]" -> 4
        // 2 -- "0" -> 3
        // 2 -- "[1-9]" -> 4
        // 3 -- "." -> 5
        // 3 -- "Ee" -> 6
        // 4 -- "[0-9]" -> 4
        // 4 -- "." -> 5
        // 4 -- "Ee" -> 6
        // 5 -- "[0-9]" -> 7
        // 6 -- "+-" -> 9
        // 6 -- "0" -> 10
        // 6 -- "[1-9]" -> 8
        // 7 -- "Ee" -> 6
        // 7 -- "[0-9]" -> 7
        // 8 -- "[0-9]" -> 8
        // 9 -- "[1-9]" -> 8
        // 9 -- "0" -> 10

        int state = 1;
        String txt = "";
        while (true) {
            if (state == 3 || state == 4 || state == 7 || state == 8 || state == 10) {
                input.markStream();
            }
            int k = input.read();

            switch (state) {
                // 1 -- "0" -> 3
                // 1 -- "+-" -> 2
                // 1 -- "[1-9]" -> 4
                case 1:
                    if (k == '0') {
                        state = 3;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k == '+' || k == '-') {
                        state = 2;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k >= '1' && k <= '9') {
                        state = 4;
                        txt += Character.toString((char)k);
                        continue;
                    }
                    break;

                // 2 -- "0" -> 3
                // 2 -- "[1-9]" -> 4
                case 2:
                    if (k == '0') {
                        state = 3;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k >= '1' && k <= '9') {
                        state = 4;
                        txt += Character.toString((char)k);
                        continue;
                    }
                    break;

                // 3 -- "." -> 5
                // 3 -- "Ee" -> 6
                case 3:
                    if (k == '.') {
                        state = 5;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k == 'E' || k == 'e') {
                        state = 6;
                        txt += Character.toString((char)k);
                        continue;
                    }
                    input.resetStream();
                    return Double.parseDouble(txt);

                // 4 -- "[0-9]" -> 4
                // 4 -- "." -> 5
                // 4 -- "Ee" -> 6
                case 4:
                    if (k >= '0' && k <= '9') {
                        // state = 4;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k == '.') {
                        state = 5;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k == 'E' || k == 'e') {
                        state = 6;
                        txt += Character.toString((char)k);
                        continue;
                    }
                    input.resetStream();
                    return Double.parseDouble(txt);

                // 5 -- "[0-9]" -> 7
                case 5:
                    if (k >= '0' && k <= '9') {
                        state = 7;
                        txt += Character.toString((char)k);
                        continue;
                    }
                    break;

                // 6 -- "+-" -> 9
                // 6 -- "0" -> 10
                // 6 -- "[1-9]" -> 8
                case 6:
                    if (k == '+' || k == '-') {
                        state = 9;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k == '0') {
                        state = 10;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k >= '1' && k <= '9') {
                        state = 8;
                        txt += Character.toString((char)k);
                        continue;
                    }
                    break;

                // 7 -- "Ee" -> 6
                // 7 -- "[0-9]" -> 7
                case 7:
                    if (k == 'E' || k == 'e') {
                        state = 6;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k >= '0' && k <= '9') {
                        // state = 7;
                        txt += Character.toString((char)k);
                        continue;
                    }
                    input.resetStream();
                    return Double.parseDouble(txt);

                // 8 -- "[0-9]" -> 8
                case 8:
                    if (k >= '0' && k <= '9') {
                        // state = 8;
                        txt += Character.toString((char)k);
                        continue;
                    }
                    input.resetStream();
                    return Double.parseDouble(txt);

                // 9 -- "[1-9]" -> 8
                // 9 -- "0" -> 10
                case 9:
                    if (k >= '1' && k <= '9') {
                        state = 8;
                        txt += Character.toString((char)k);
                        continue;
                    } else if (k == '0') {
                        state = 10;
                        txt += Character.toString((char)k);
                        continue;
                    }
                    break;

                case 10:
                    input.resetStream();
                    return Double.parseDouble(txt);

                default:
                    Assert.fail("Unexpected state reached in readReal.");
                    break;
            }
            String msg = "Expected a real number but did not find one while reading input.";
            throw new ChiSimulatorException(msg);
        }
    }

    /**
     * Write a real number to the output.
     *
     * @param output Output stream.
     * @param value Value to print.
     */
    public static void writeReal(ChiFileHandle output, double value) {
        output.write(fmt("%s", value));
    }

    /**
     * Read a string literal from the input stream, and return its value.
     *
     * @param input Input stream.
     * @return Value of the read string literal.
     * @throws ChiSimulatorException If the text does not contain a string literal.
     */
    public static String readString(ChiFileHandle input) {
        // Initial states: 1
        // Accepting states: 3 5
        // Edges:
        // 1 -- " " -> 1 (skip white-space)
        // 1 -- """ -> 2
        // 1 -- "#" -> 3 (anything except white-space and ")
        // 2 -- "\" -> 4
        // 2 -- """ -> 5
        // 2 -- "*" -> 2 (anything except \ and ")
        // 3 -- "#" -> 3 (anything except white-space)
        // 4 -- ""tn\" -> 2
        int state = 1;
        String txt = "";
        for (;;) {
            // state == 5 is never actually reached.
            if (state == 3) {
                input.markStream();
            }

            int k = input.read();
            // 1 -- " " -> 1 (skip white-space)
            if (state == 1 && (k == ' ' || k == '\t' || k == '\r' || k == '\n')) {
                // state = 1;
                continue;
            }
            // 1 -- """ -> 2
            if (state == 1 && k == '\"') {
                state = 2;
                continue;
            }
            // 1 -- "#" -> 3 (anything except white-space and ")
            if (state == 1) {
                state = 3;
                txt += Character.toString((char)k);
                continue;
            }
            // 2 -- "\" -> 4
            if (state == 2 && k == '\\') {
                state = 4;
                continue;
            }
            // 2 -- """ -> 5
            if (state == 2 && k == '\"') {
                // state = 5;
                // continue;
                return txt; // State 5 reached: EXIT!
            }
            // 2 -- "*" -> 2 (anything except \, ", and EOF)
            if (state == 2 && k != -1) {
                // state = 2;
                txt += Character.toString((char)k);
                continue;
            }
            // 3 -- "#" -> 3 (anything except white-space and EOF)
            if (state == 3 && k != -1 && k != ' ' && k != '\t' && k != '\r' && k != '\n') {
                // state = 3;
                txt += Character.toString((char)k);
                continue;
            }
            // 4 -- ""tn\" -> 2
            if (state == 4) {
                if (k == '\\' || k == '\"') {
                    state = 2;
                    txt += Character.toString((char)k);
                    continue;
                }
                if (k == 't') {
                    state = 2;
                    txt += "\t";
                    continue;
                }
                if (k == 'n') {
                    state = 2;
                    txt += "\n";
                    continue;
                }
            }

            break;
        }

        // state == 5 never gets here.
        if (state == 2) {
            String msg = "Encountered end-of-file while reading a string literal, perhaps a closing quote is missing.";
            throw new ChiSimulatorException(msg);
        }
        if (state != 3) {
            String msg = "Missing string literal but found something else while reading input.";
            throw new ChiSimulatorException(msg);
        }

        input.resetStream();
        return txt;
    }

    /**
     * Write a string to the output. Outputs the string value rather than the string literal.
     *
     * @param output Output stream.
     * @param value Text to print.
     */
    public static void writeString(ChiFileHandle output, String value) {
        output.write(value);
    }
}
