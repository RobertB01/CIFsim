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
import static org.eclipse.escet.common.java.Strings.fmt;

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

    /**
     * Last read but not processed character. Negative if not valid.
     *
     * <p>
     * This is the one-character read-ahead buffer often containing the next character that needs to be dealt with in
     * the parser. The parser must always first check for EOF before moving to a next character, thus it makes sense to
     * ensure this buffer is full after an {@link #isEof} test if not at EOF.
     * </p>
     * <p>
     * That decision also solves a second problem inside {@link #isEof}. In the {@link #isEof} function, if the
     * {@link #nextChar} buffer is empty, then trying to read from the input is the only way to detect that EOF has been
     * reached in the file. However, if the file has not reached EOF, the read action unavoidably produces the next
     * character from the file. That next character must be dealt with then. The decision to let the {@link #isEof}
     * function handle filling {@link #nextChar} gives a simple way to solve that.
     * </p>
     */
    private int nextChar = -1;

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
        nextChar = -1;
        numFields = -1;
    }

    /**
     * Parse the CSV file text provided earlier as a list of lines of fields. Each line has the same number of fields,
     * and there is at least one field and one line.
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
     * @throws CsvParseError If the input data does not follow the RFC-4180 standard.
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
                char k = get();
                if (isPrintable(k)) {
                    throw new CsvParseError(fmt("Unexpected character ('%c', value %d) found.", k, (int)k));
                } else {
                    throw new CsvParseError(fmt("Unexpected character (value %d) found.", (int)k));
                }
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
            if (isTextData(k) || k == COMMA || k == CR || k == LF) {
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
            if (isTextData(k)) {
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
    private boolean isTextData(char k) {
        // ABNF: TEXTDATA = %x20-21 / %x23-2B / %x2D-7E
        // ABNF: COMMA = %x2C
        // ABNF: DQUOTE = %x22 ;as per section 6.1 of RFC 2234 [2]
        return k >= 0x20 && k <= 0x7E && k != DQUOTE && k != COMMA;
    }

    /**
     * Check if the given character is printable.
     *
     * @param k Character to test.
     * @return Whether the character is printable.
     */
    private boolean isPrintable(char k) {
        return k >= 0x20 && k <= 0x7E;
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
        Assert.check(nextChar >= 0); // Side-effect of isEof() returning false.

        return (char)nextChar;
    }

    /** Advance to the next input. The end of the input must not have been reached. */
    private void advance() {
        Assert.check(!isEof());
        Assert.check(nextChar >= 0); // Side-effect of isEof() returning false.

        // Invalidate read-ahead buffer, next call to a low-level IO function in the parser will fill it again if the
        // file is not at EOF.
        nextChar = -1;
    }

    /**
     * Check if the end of the input has been reached.
     *
     * <p>
     * This function also fills the read-ahead buffer {@link #nextChar}. For details, see {@link #nextChar}.
     * </p>
     *
     * @return Whether the end has been reached.
     */
    private boolean isEof() {
        if (nextChar >= 0) { // Data is available -> not EOF.
            return false;
        }
        if (handle == null) { // No data and no input stream -> EOF.
            return true;
        }

        // Read the next character.
        try {
            nextChar = handle.read();
        } catch (IOException ex) {
            throw new CsvParseError("Read error.", ex);
        }

        if (nextChar < 0) { // Read failed -> EOF found, disable further reading.
            handle = null;
            return true;
        }
        return false; // Read succeeded, not at EOF.
    }

    /** Class for reporting a CSV parse error to the user. */
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
