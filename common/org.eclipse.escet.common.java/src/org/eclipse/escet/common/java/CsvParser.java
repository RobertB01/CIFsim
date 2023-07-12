//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.list;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Parse a CSV file.
 *
 * <p>
 * Provide the input to read through the constructor or {@link #setInput}, and
 * <ul>
 * <li>call repeatedly {@link #getLine} to get the next line until {@code null} is returned, or</li>
 * <li>call {@link #parseFile} to interpret the entire input as an RFC-4180 CSV file.</li>
 * </ul>
 * Input is not closed by the class.
 * </p>
 */
public class CsvParser {
    /** Carriage return (CR) character. */
    public static final char CR = '\r';

    /** Line feed (LF) character. */
    public static final char LF = '\n';

    /** Comma character. */
    public static final char COMMA = ',';

    /** Double quote character. */
    public static final char DQUOTE = '"';

    /**
     * Input handle for reading characters, {@code null} if EOF has been seen.
     *
     * <p>
     * Class does not handle closing the file handle, the caller is responsible for managing the file.
     * </p>
     */
    private Reader handle = null;

    /** Last read but not processed character, Negative if not valid. */
    private int lastChar = -1;

    /** Number of fields in a line, or a negative values if unknown. */
    private int numFields = -1;

    /**
     * Constructor of the {@link CsvParser} class.
     *
     * <p>
     * The input to process can also be set with {@link #setInput}.
     * </p>
     *
     * @param handle Input file to parse or {@code null} if it is set after construction. This class does not close the
     *     handle.
     */
    public CsvParser(Reader handle) {
        if (handle != null) {
            setInput(handle);
        }
    }

    /**
     * Setup file handle to read input.
     *
     * @param handle File handle to use for reading input, this class does not close the handle.
     */
    public void setInput(Reader handle) {
        this.handle = handle;
        lastChar = -1;
        numFields = -1;
    }

    /**
     * Parse the CSV file text provided earlier as a list of lines of fields. Each line has the same number of fields,
     * there is at least one field and one line.
     *
     * <p>
     * See RFC-4180 for details on the accepted syntax.
     * </p>
     *
     * @return Lines of the CSV text.
     * @throws CsvParseError If the input data does not follow the RFC-4180 standard,
     */
    public List<List<String>> parseFile() {
        List<List<String>> lines = list();
        while (true) {
            List<String> line = getLine();
            if (line == null) {
                break;
            }
            lines.add(line);
        }
        return lines;
    }

    /**
     * Read a line from the CSV file.
     *
     * @return Next line, or {@code null} if end of file has been reached.
     * @throws CsvParseError If the input data does not follow the RFC-4180 standard,
     */
    public List<String> getLine() {
        if (numFields >= 0) {
            // Non-first call, start with reading the CRLF sequence after the last returned line.
            boolean advanced = advanceChar(CR);
            advanced |= advanceChar(LF);
            if (isEof()) {
                return null;
            }
            // Not at EOF after optional CRLF, make sure we moved on CR or LF, or we may cycle here forever.
            if (!advanced) {
                throw new CsvParseError("Unexpected character found.");
            }
        }

        // (Starting point of the first call.)
        //
        // Read the next line and return it.
        List<String> line = readLine();
        if (numFields < 0 || numFields == line.size()) {
            numFields = line.size();
            return line;
        } else {
            throw new CsvParseError("Incorrect number of fields.");
        }
    }

    /**
     * Read the next line from the input, aborts reading on anything non-familiar.
     *
     * @return The fields of the read line.
     */
    private List<String> readLine() {
        // ABNF: record = field *(COMMA field)
        List<String> line = list();
        while (true) {
            String s = readField();
            line.add(s);

            if (!advanceChar(COMMA)) {
                break;
            }
        }
        return line;
    }

    /**
     * Read the next field from the input, aborts reading on anything non-familiar.
     *
     * @return The text of the read field, may be the empty string.
     */
    private String readField() {
        // ABNF: field = (escaped / non-escaped)
        if (isEof()) {
            return ""; // Technically an unquoted field but that draws the same conclusion.
        }
        char k = get();
        return (k == DQUOTE) ? getQuotedField() : getUnquotedField();
    }

    /**
     * Read a field protected with double quotes.
     *
     * @return The text of the field.
     */
    private String getQuotedField() {
        // ABNF: escaped = DQUOTE *(TEXTDATA / COMMA / CR / LF / 2DQUOTE) DQUOTE
        Assert.check(advanceChar(DQUOTE));
        String s = "";
        while (true) {
            if (isEof()) {
                throw new CsvParseError("Unexpected EOF while reading a quoted string.");
            }
            char k = get();
            if (testTextData(k) || k == COMMA || k == CR || k == LF) {
                s += k;
                advance();
                continue;
            }
            if (k == DQUOTE) {
                advance();
                if (isEof()) { // DQUOTE and EOF, finish after DQUOTE
                    return s;
                }
                if (advanceChar(DQUOTE)) { // 2DQUOTE, unescape it and continue.
                    s += DQUOTE;
                    advance();
                    continue;
                }
                return s; // DQUOTE and something else, finish after DQUOTE
            }
        }
    }

    /**
     * Collect the contents of an unquoted field, caller should check for the field being unquoted.
     *
     * @return The collected field text.
     */
    private String getUnquotedField() {
        // ABNF: non-escaped = *TEXTDATA
        String s = "";
        while (true) {
            if (isEof()) {
                break;
            }
            char k = get();
            if (testTextData(k)) {
                s += k;
                advance();
            } else {
                break;
            }
        }
        return s;
    }

    /**
     * Check if the given character is text data.
     *
     * @param k Character to test.
     * @return Whether the character is text data.
     */
    private boolean testTextData(char k) {
        // ABNF: TEXTDATA = %x20-21 / %x23-2B / %x2D-7E
        // ABNF: COMMA = %x2C
        // ABNF: DQUOTE = %x22 ;as per section 6.1 of RFC 2234 [2]
        return k >= 0x20 && k <= 0x7E && k != DQUOTE && k != COMMA;
    }

    /**
     * Check if the next character is {@code k}. If so, advance the input.
     *
     * @param k Character to test against.
     * @return Whether a match was found and the input is advanced.
     */
    private boolean advanceChar(char k) {
        if (!isEof() && get() == k) {
            advance();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the next character from the input. The end of the input must not have been reached, input is not advanced.
     *
     * @return The next character at the input.
     */
    private char get() {
        Assert.check(!isEof());
        Assert.check(lastChar >= 0); // Side-effect of isEof() returning false.

        char value = (char)lastChar;
        return value;
    }

    /** Advance to the next input. The end of the input must not have been reached. */
    private void advance() {
        Assert.check(!isEof());
        Assert.check(lastChar >= 0); // Side-effect of isEof() returning false.
        lastChar = -1;
    }

    /**
     * Check if the end of the input has been reached.
     *
     * @return Whether the end has been reached.
     */
    private boolean isEof() {
        if (lastChar >= 0) { // Data is available -> not EOF.
            return false;
        }
        if (handle == null) { // No data and no input stream -> EOF.
            return true;
        }

        // Read the next character.
        try {
            lastChar = handle.read();
        } catch (IOException ex) {
            throw new CsvParseError("Read error.", ex);
        }

        if (lastChar == -1) { // Read failed -> EOF found, disable further reading.
            handle = null;
            return true;
        }
        return false; // Read succeeded, not at EOF.
    }

    /** Class for reporting a CVS parse error to the user. */
    public static class CsvParseError extends RuntimeException {
        /**
         * Constructor of the {@link CsvParseError} class.
         *
         * @param message Message describing the error.
         */
        public CsvParseError(String message) {
            this(message, null);
        }

        /**
         * Constructor of the {@link CsvParseError} class.
         *
         * @param message Message describing the error.
         * @param cause Cause of the exception if available.
         */
        public CsvParseError(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
