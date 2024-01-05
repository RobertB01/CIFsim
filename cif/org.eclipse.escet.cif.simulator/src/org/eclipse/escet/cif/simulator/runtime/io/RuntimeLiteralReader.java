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

package org.eclipse.escet.cif.simulator.runtime.io;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.RuntimeEnumUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InputOutputException;

/** Runtime literal reader. */
public class RuntimeLiteralReader {
    /** Constructor for the {@link RuntimeLiteralReader} class. */
    private RuntimeLiteralReader() {
        // Static class.
    }

    /**
     * Reads a CIF boolean literal from a resource.
     *
     * @param loader The class loader to use to obtain the resource stream.
     * @param path The resource path.
     * @return The value read from the resource.
     * @throws InputOutputException In case of an I/O error, or a boolean literal could not be read.
     */
    public static boolean readBoolLiteral(ClassLoader loader, String path) {
        try {
            try (LiteralStream stream = new LiteralStream(loader, path)) {
                return readBoolLiteral(stream);
            }
        } catch (InputOutputException ex) {
            // Currently literal resource reading is only used internally, so
            // we expect no exceptions here.
            String msg = "Failed to read literal of type \"bool\".";
            throw new RuntimeException(msg, ex);
        }
    }

    /**
     * Reads a CIF boolean literal from a string.
     *
     * @param valueText The literal text from which to read the value.
     * @return The value read from the string.
     * @throws InputOutputException If a boolean literal could not be read.
     */
    public static boolean readBoolLiteral(String valueText) {
        try {
            try (LiteralStream stream = new LiteralStream(valueText)) {
                return readBoolLiteral(stream);
            }
        } catch (InputOutputException ex) {
            String msg = "Failed to read literal of type \"bool\".";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF boolean literal from the given stream.
     *
     * @param stream The stream.
     * @return The value read from the stream.
     * @throws InputOutputException In case of an I/O error, or a boolean literal could not be read.
     */
    public static boolean readBoolLiteral(LiteralStream stream) {
        try {
            return readBoolLiteralInternal(stream);
        } catch (InputOutputException ex) {
            String msg = "Failed to read literal of type \"bool\".";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF boolean literal from the given stream.
     *
     * @param stream The stream.
     * @return The value read from the stream.
     * @throws InputOutputException In case of an I/O error, or a boolean literal could not be read.
     */
    private static boolean readBoolLiteralInternal(LiteralStream stream) {
        // Prepare.
        stream.skipWhitespace();

        // Read 'true' or 'false'.
        int c = stream.read();
        if (c == 't') {
            stream.expectCharacter('r');
            stream.expectCharacter('u');
            stream.expectCharacter('e');
            return true;
        } else if (c == 'f') {
            stream.expectCharacter('a');
            stream.expectCharacter('l');
            stream.expectCharacter('s');
            stream.expectCharacter('e');
            return false;
        }

        // Failure.
        String msg = "Expected boolean literal (\"true\" or \"false\"), but did not find it";
        String lineColMsg = stream.getLineColMsg();
        if (lineColMsg != null) {
            msg += ", " + lineColMsg;
        }
        msg += ".";
        throw new InputOutputException(msg);
    }

    /**
     * Reads a CIF integer literal from a resource.
     *
     * @param loader The class loader to use to obtain the resource stream.
     * @param path The resource path.
     * @return The value read from the resource.
     * @throws InputOutputException In case of an I/O error, or an integer literal could not be read.
     */
    public static int readIntLiteral(ClassLoader loader, String path) {
        try {
            try (LiteralStream stream = new LiteralStream(loader, path)) {
                return readIntLiteral(stream);
            }
        } catch (InputOutputException ex) {
            // Currently literal resource reading is only used internally, so
            // we expect no exceptions here.
            String msg = "Failed to read literal of type \"int\".";
            throw new RuntimeException(msg, ex);
        }
    }

    /**
     * Reads a CIF integer literal from a string.
     *
     * @param valueText The literal text from which to read the value.
     * @return The value read from the string.
     * @throws InputOutputException If an integer literal could not be read.
     */
    public static int readIntLiteral(String valueText) {
        try {
            try (LiteralStream stream = new LiteralStream(valueText)) {
                return readIntLiteral(stream);
            }
        } catch (InputOutputException ex) {
            String msg = "Failed to read literal of type \"int\".";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF integer literal from the given stream.
     *
     * @param stream The stream.
     * @return The value read from the stream.
     * @throws InputOutputException In case of an I/O error, or an integer literal could not be read.
     */
    public static int readIntLiteral(LiteralStream stream) {
        try {
            return readIntLiteralInternal(stream);
        } catch (InputOutputException ex) {
            String msg = "Failed to read literal of type \"int\".";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF integer literal from the given stream.
     *
     * @param stream The stream.
     * @return The value read from the stream.
     * @throws InputOutputException In case of an I/O error, or an integer literal could not be read.
     */
    private static int readIntLiteralInternal(LiteralStream stream) {
        // Prepare.
        stream.skipWhitespace();

        // Read prefix +/- characters.
        int c = stream.read();
        boolean negate = false;
        while (c == '+' || c == '-') {
            if (c == '-') {
                negate = !negate;
            }
            c = stream.read();
        }

        // Read integer value.
        int value = 0;
        boolean hasDigits = false;
        while (c >= '0' && c <= '9') {
            value = value * 10 + c - '0';
            if (value < 0) {
                String msg = "Integer overflow while reading integer number";
                String lineColMsg = stream.getLineColMsg();
                if (lineColMsg != null) {
                    msg += ", " + lineColMsg;
                }
                msg += ".";
                throw new InputOutputException(msg);
            }

            hasDigits = true;
            stream.mark(1);
            c = stream.read();
        }

        // Success.
        if (hasDigits) {
            stream.reset();
            return negate ? -value : value;
        }

        // Failure.
        String msg = "Expected integer number but found no digits";
        String lineColMsg = stream.getLineColMsg();
        if (lineColMsg != null) {
            msg += ", " + lineColMsg;
        }
        msg += ".";
        throw new InputOutputException(msg);
    }

    /**
     * Reads a CIF integer or real literal from a resource.
     *
     * @param loader The class loader to use to obtain the resource stream.
     * @param path The resource path.
     * @return The value read from the resource.
     * @throws InputOutputException In case of an I/O error, or an integer or real literal could not be read.
     */
    public static double readRealLiteral(ClassLoader loader, String path) {
        try {
            try (LiteralStream stream = new LiteralStream(loader, path)) {
                return readRealLiteral(stream);
            }
        } catch (InputOutputException ex) {
            // Currently literal resource reading is only used internally, so
            // we expect no exceptions here.
            String msg = "Failed to read literal of type \"real\".";
            throw new RuntimeException(msg, ex);
        }
    }

    /**
     * Reads a CIF integer or real literal from a string.
     *
     * @param valueText The literal text from which to read the value.
     * @return The value read from the string.
     * @throws InputOutputException If an integer or real literal could not be read.
     */
    public static double readRealLiteral(String valueText) {
        try {
            try (LiteralStream stream = new LiteralStream(valueText)) {
                return readRealLiteral(stream);
            }
        } catch (InputOutputException ex) {
            String msg = "Failed to read literal of type \"real\".";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF integer or real literal from the given stream.
     *
     * @param stream The stream.
     * @return The value read from the stream.
     * @throws InputOutputException In case of an I/O error, or an integer or real literal could not be read.
     */
    public static double readRealLiteral(LiteralStream stream) {
        try {
            return readRealLiteralInternal(stream);
        } catch (InputOutputException ex) {
            String msg = "Failed to read literal of type \"real\".";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF integer or real literal from the given stream.
     *
     * @param stream The stream.
     * @return The value read from the stream.
     * @throws InputOutputException In case of an I/O error, or an integer or real literal could not be read.
     */
    private static double readRealLiteralInternal(LiteralStream stream) {
        // Prepare.
        stream.skipWhitespace();

        // Initial states: 1
        // Accepting states: 3 4 7 9 (3, 4 for integer recognition)
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
        // 6 -- "+-" -> 8
        // 6 -- "[0-9]" -> 9
        // 7 -- "Ee" -> 6
        // 7 -- "[0-9]" -> 7
        // 8 -- "[0-9]" -> 9
        // 9 -- "[0-9]" -> 9

        int state = 1;
        StringBuilder txt = new StringBuilder();
        while (true) {
            if (state == 3 || state == 4 || state == 7 || state == 9) {
                stream.mark(1);
            }
            int c = stream.read();

            switch (state) {
                // 1 -- "0" -> 3
                // 1 -- "+-" -> 2
                // 1 -- "[1-9]" -> 4
                case 1:
                    if (c == '0') {
                        state = 3;
                        txt.append((char)c);
                        continue;
                    } else if (c == '+' || c == '-') {
                        state = 2;
                        txt.append((char)c);
                        continue;
                    } else if (c >= '1' && c <= '9') {
                        state = 4;
                        txt.append((char)c);
                        continue;
                    }
                    break;

                // 2 -- "0" -> 3
                // 2 -- "[1-9]" -> 4
                case 2:
                    if (c == '0') {
                        state = 3;
                        txt.append((char)c);
                        continue;
                    } else if (c >= '1' && c <= '9') {
                        state = 4;
                        txt.append((char)c);
                        continue;
                    }
                    break;

                // 3 -- "." -> 5
                // 3 -- "Ee" -> 6
                case 3: {
                    if (c == '.') {
                        state = 5;
                        txt.append((char)c);
                        continue;
                    } else if (c == 'E' || c == 'e') {
                        state = 6;
                        txt.append((char)c);
                        continue;
                    }
                    stream.reset();

                    double rslt = Double.parseDouble(txt.toString());
                    Assert.check(!Double.isNaN(rslt));
                    if (rslt == -0.0) {
                        rslt = 0.0;
                    }
                    if (Double.isInfinite(rslt)) {
                        String msg = "Real overflow while reading real number";
                        String lineColMsg = stream.getLineColMsg();
                        if (lineColMsg != null) {
                            msg += ", " + lineColMsg;
                        }
                        msg += ".";
                        throw new InputOutputException(msg);
                    }
                    return rslt;
                }

                // 4 -- "[0-9]" -> 4
                // 4 -- "." -> 5
                // 4 -- "Ee" -> 6
                case 4: {
                    if (c >= '0' && c <= '9') {
                        // state = 4;
                        txt.append((char)c);
                        continue;
                    } else if (c == '.') {
                        state = 5;
                        txt.append((char)c);
                        continue;
                    } else if (c == 'E' || c == 'e') {
                        state = 6;
                        txt.append((char)c);
                        continue;
                    }
                    stream.reset();

                    double rslt = Double.parseDouble(txt.toString());
                    Assert.check(!Double.isNaN(rslt));
                    if (rslt == -0.0) {
                        rslt = 0.0;
                    }
                    if (Double.isInfinite(rslt)) {
                        String msg = "Real overflow while reading real number";
                        String lineColMsg = stream.getLineColMsg();
                        if (lineColMsg != null) {
                            msg += ", " + lineColMsg;
                        }
                        msg += ".";
                        throw new InputOutputException(msg);
                    }
                    return rslt;
                }

                // 5 -- "[0-9]" -> 7
                case 5:
                    if (c >= '0' && c <= '9') {
                        state = 7;
                        txt.append((char)c);
                        continue;
                    }
                    break;

                // 6 -- "+-" -> 8
                // 6 -- "[0-9]" -> 9
                case 6:
                    if (c == '+' || c == '-') {
                        state = 8;
                        txt.append((char)c);
                        continue;
                    } else if (c >= '0' && c <= '9') {
                        state = 9;
                        txt.append((char)c);
                        continue;
                    }
                    break;

                // 7 -- "Ee" -> 6
                // 7 -- "[0-9]" -> 7
                case 7: {
                    if (c == 'E' || c == 'e') {
                        state = 6;
                        txt.append((char)c);
                        continue;
                    } else if (c >= '0' && c <= '9') {
                        // state = 7;
                        txt.append((char)c);
                        continue;
                    }
                    stream.reset();

                    double rslt = Double.parseDouble(txt.toString());
                    Assert.check(!Double.isNaN(rslt));
                    if (rslt == -0.0) {
                        rslt = 0.0;
                    }
                    if (Double.isInfinite(rslt)) {
                        String msg = "Real overflow while reading real number";
                        String lineColMsg = stream.getLineColMsg();
                        if (lineColMsg != null) {
                            msg += ", " + lineColMsg;
                        }
                        msg += ".";
                        throw new InputOutputException(msg);
                    }
                    return rslt;
                }

                // 8 -- "[0-9]" -> 9
                case 8: {
                    if (c >= '0' && c <= '9') {
                        state = 9;
                        txt.append((char)c);
                        continue;
                    }
                    break;
                }

                // 9 -- "[0-9]" -> 9
                case 9:
                    if (c >= '0' && c <= '9') {
                        // state = 9;
                        txt.append((char)c);
                        continue;
                    }
                    stream.reset();

                    double rslt = Double.parseDouble(txt.toString());
                    Assert.check(!Double.isNaN(rslt));
                    if (rslt == -0.0) {
                        rslt = 0.0;
                    }
                    if (Double.isInfinite(rslt)) {
                        String msg = "Real overflow while reading real number";
                        String lineColMsg = stream.getLineColMsg();
                        if (lineColMsg != null) {
                            msg += ", " + lineColMsg;
                        }
                        msg += ".";
                        throw new InputOutputException(msg);
                    }
                    return rslt;

                default:
                    throw new RuntimeException("Unexpected state: " + state);
            }

            // Failure.
            String msg = "Expected a real number but did not find one";
            String lineColMsg = stream.getLineColMsg();
            if (lineColMsg != null) {
                msg += ", " + lineColMsg;
            }
            msg += ".";
            throw new InputOutputException(msg);
        }
    }

    /**
     * Reads a CIF string literal from a resource.
     *
     * @param loader The class loader to use to obtain the resource stream.
     * @param path The resource path.
     * @return The text represented by the CIF string literal, read from the resource. The returned text is not escaped,
     *     and does not contain the surrounding double quotes.
     * @throws InputOutputException In case of an I/O error, or a string literal could not be read.
     */
    public static String readStringLiteral(ClassLoader loader, String path) {
        try {
            try (LiteralStream stream = new LiteralStream(loader, path)) {
                return readStringLiteral(stream);
            }
        } catch (InputOutputException ex) {
            // Currently literal resource reading is only used internally, so
            // we expect no exceptions here.
            String msg = "Failed to read literal of type \"string\".";
            throw new RuntimeException(msg, ex);
        }
    }

    /**
     * Reads a CIF string literal from a string.
     *
     * @param valueText The literal text from which to read the value.
     * @return The text represented by the CIF string literal, read from the string. The returned text is not escaped,
     *     and does not contain the surrounding double quotes.
     * @throws InputOutputException If a string literal could not be read.
     */
    public static String readStringLiteral(String valueText) {
        try {
            try (LiteralStream stream = new LiteralStream(valueText)) {
                return readStringLiteral(stream);
            }
        } catch (InputOutputException ex) {
            String msg = "Failed to read literal of type \"string\".";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF string literal from the given stream.
     *
     * @param stream The stream.
     * @return The text represented by the CIF string literal, read from the stream. The returned text is not escaped,
     *     and does not contain the surrounding double quotes.
     * @throws InputOutputException In case of an I/O error, or a string literal could not be read.
     */
    public static String readStringLiteral(LiteralStream stream) {
        try {
            return readStringLiteralInternal(stream);
        } catch (InputOutputException ex) {
            String msg = "Failed to read literal of type \"string\".";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF string literal from the given stream.
     *
     * @param stream The stream.
     * @return The text represented by the CIF string literal, read from the stream. The returned text is not escaped,
     *     and does not contain the surrounding double quotes.
     * @throws InputOutputException In case of an I/O error, or a string literal could not be read.
     */
    private static String readStringLiteralInternal(LiteralStream stream) {
        // Prepare.
        stream.skipWhitespace();

        // Read opening double quote.
        stream.expectCharacter('"');

        // Read data.
        StringBuilder txt = new StringBuilder();
        boolean inEscape = false;
        while (true) {
            int c = stream.read();

            if (!inEscape) {
                if (c == '\\') {
                    inEscape = true;
                } else if (c == '"') {
                    return txt.toString();
                } else if (c != -1) {
                    txt.append((char)c);
                } else {
                    String msg = "Encountered end-of-file";
                    String lineColMsg = stream.getLineColMsg();
                    if (lineColMsg != null) {
                        msg += ", " + lineColMsg + ",";
                    }
                    msg += " while reading a string literal. Perhaps a closing double quote is missing?";
                    throw new InputOutputException(msg);
                }
            } else {
                inEscape = false;
                if (c == '\\' || c == '"') {
                    txt.append((char)c);
                } else if (c == 't') {
                    txt.append('\t');
                } else if (c == 'n') {
                    txt.append('\n');
                } else if (c == -1) {
                    String msg = "Encountered end-of-file";
                    String lineColMsg = stream.getLineColMsg();
                    if (lineColMsg != null) {
                        msg += ", " + lineColMsg + ",";
                    }
                    msg += " while reading an escaped character of a string literal.";
                    throw new InputOutputException(msg);
                } else {
                    String msg = fmt("Encountered \"%c\"", c);
                    String lineColMsg = stream.getLineColMsg();
                    if (lineColMsg != null) {
                        msg += ", " + lineColMsg + ",";
                    }
                    msg += " while reading an escaped character of a string literal.";
                    throw new InputOutputException(msg);
                }
            }
        }
    }

    /**
     * Reads a CIF enumeration literal from a resource.
     *
     * @param <T> The type of the enumeration for which to read a literal.
     * @param loader The class loader to use to obtain the resource stream.
     * @param path The resource path.
     * @param enumClass The class of the enumeration for which to read a literal.
     * @return The enumeration literal read from the resource.
     * @throws InputOutputException In case of an I/O error, or an enumeration literal (of the correct type) could not
     *     be read.
     */
    public static <T extends Enum<T>> T readEnumLiteral(ClassLoader loader, String path, Class<T> enumClass) {
        try {
            try (LiteralStream stream = new LiteralStream(loader, path)) {
                return readEnumLiteral(stream, enumClass);
            }
        } catch (InputOutputException ex) {
            // Currently literal resource reading is only used internally, so
            // we expect no exceptions here.
            String name = RuntimeEnumUtils.getEnumCifName(enumClass);
            String msg = fmt("Failed to read literal of type \"%s\".", name);
            throw new RuntimeException(msg, ex);
        }
    }

    /**
     * Reads a CIF enumeration literal from a string.
     *
     * @param <T> The type of the enumeration for which to read a literal.
     * @param valueText The literal text from which to read the value.
     * @param enumClass The class of the enumeration for which to read a literal.
     * @return The enumeration literal read from the string.
     * @throws InputOutputException If an enumeration literal (of the correct type) could not be read.
     */
    public static <T extends Enum<T>> T readEnumLiteral(String valueText, Class<T> enumClass) {
        try {
            try (LiteralStream stream = new LiteralStream(valueText)) {
                return readEnumLiteral(stream, enumClass);
            }
        } catch (InputOutputException ex) {
            String name = RuntimeEnumUtils.getEnumCifName(enumClass);
            String msg = fmt("Failed to read literal of type \"%s\".", name);
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF enumeration literal from the given stream.
     *
     * @param <T> The type of the enumeration for which to read a literal.
     * @param stream The stream.
     * @param enumClass The class of the enumeration for which to read a literal.
     * @return The enumeration literal read from the stream.
     * @throws InputOutputException In case of an I/O error, or an enumeration literal (of the correct type) could not
     *     be read.
     */
    public static <T extends Enum<T>> T readEnumLiteral(LiteralStream stream, Class<T> enumClass) {
        try {
            return readEnumLiteralInternal(stream, enumClass);
        } catch (InputOutputException ex) {
            String name = RuntimeEnumUtils.getEnumCifName(enumClass);
            String msg = fmt("Failed to read literal of type \"%s\".", name);
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Reads a CIF enumeration literal from the given stream.
     *
     * @param <T> The type of the enumeration for which to read a literal.
     * @param stream The stream.
     * @param enumClass The class of the enumeration for which to read a literal.
     * @return The enumeration literal read from the stream.
     * @throws InputOutputException In case of an I/O error, or an enumeration literal (of the correct type) could not
     *     be read.
     */
    private static <T extends Enum<T>> T readEnumLiteralInternal(LiteralStream stream, Class<T> enumClass) {
        // Read name of the literal (an identifier).
        String name;
        try {
            name = readIdentifier(stream);
        } catch (InputOutputException ex) {
            String msg = "Failed to read enumeration literal.";
            throw new InputOutputException(msg, ex);
        }

        // Look up the literal from the enumeration Java class.
        for (T enumConstant: enumClass.getEnumConstants()) {
            if (enumConstant.toString().equals(name)) {
                return enumConstant;
            }
        }

        // Result not found.
        String msg = fmt("Identifier \"%s\" is not the name of an expected enumeration literal", name);
        String lineColMsg = stream.getLineColMsg();
        if (lineColMsg != null) {
            msg += ", " + lineColMsg;
        }
        msg += ".";
        throw new InputOutputException(msg);
    }

    /**
     * Reads a CIF identifier from the given stream.
     *
     * @param stream The stream.
     * @return The identifier read from the stream.
     * @throws InputOutputException In case of an I/O error, or an identifier could not be read.
     */
    private static String readIdentifier(LiteralStream stream) {
        // Prepare.
        stream.skipWhitespace();

        // Read identifier.
        StringBuilder txt = new StringBuilder();
        while (true) {
            // Read next character.
            stream.mark(1);
            int c = stream.read();

            // Process next character.
            if (c == -1) {
                // End of input. Return identifier, if valid.
                if (txt.length() > 0) {
                    return txt.toString();
                }

                // Invalid identifier.
                String msg = "Encountered end-of-file while reading an identifier";
                String lineColMsg = stream.getLineColMsg();
                if (lineColMsg != null) {
                    msg += ", " + lineColMsg;
                }
                msg += ".";
                throw new InputOutputException(msg);
            } else if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_') {
                // Valid identifier start/continuation.
                txt.append((char)c);
                continue;
            } else if (c >= '0' && c <= '9') {
                // Valid identifier continuation.
                if (txt.length() > 0) {
                    txt.append((char)c);
                    continue;
                }

                // Invalid identifier start.
                String msg = fmt("Encountered \"%c\"", c);
                String lineColMsg = stream.getLineColMsg();
                if (lineColMsg != null) {
                    msg += ", " + lineColMsg + ",";
                }
                msg += " while reading the first character of an identifier.";
                throw new InputOutputException(msg);
            } else {
                // End of identifier input. Return identifier, if valid.
                if (txt.length() > 0) {
                    stream.reset();
                    return txt.toString();
                }

                // Invalid identifier.
                String msg = fmt("Encountered \"%c\"", c);
                String lineColMsg = stream.getLineColMsg();
                if (lineColMsg != null) {
                    msg += ", " + lineColMsg + ",";
                }
                msg += " while reading the first character of an identifier.";
                throw new InputOutputException(msg);
            }
        }
    }
}
