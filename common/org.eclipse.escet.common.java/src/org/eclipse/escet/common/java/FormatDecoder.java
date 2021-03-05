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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.java.FormatDescription.Conversion;

/** Format pattern decoder. */
public class FormatDecoder {
    /** The supported formatter flags. */
    private static final String FLAGS = "-+ 0,";

    /** The supported formatter flags. */
    private static final String[] FLAG_CHARS = {"-", "+", " ", "0", ","};

    /** End-of-file marker. */
    private static final char EOF = (char)-1;

    /** The input text being decoded, without backslash escaping, with percentage escaping. */
    private String text;

    /** The resulting format descriptions. */
    private List<FormatDescription> result;

    /** The input text that has been scanned for the format description. */
    private String parsed = "";

    /** The input text that has been scanned for the explicit index. */
    private String index;

    /** The input text that has been scanned for the flags. */
    private String flags = "";

    /** The input text that has been scanned for the width. */
    private String width = "";

    /** The input text that has been scanned for the precision. */
    private String precision = "";

    /** The input text that has been scanned for the width. */
    private String conversion;

    /** The 0-based offset of the current character, into the input text. */
    private int idx = 0;

    /** The 0-based offset of the current format description, into the original text. */
    private int offset = -1;

    /** The length of the original text of the current format description. */
    private int length = 0;

    /**
     * Decode a format pattern to its components. The following assumptions are made:
     * <ul>
     * <li>Original text was scanned by a scanner.</li>
     * <li>Scanner recognizes {@code \\}, {@code \"}, {@code \n}, and {@code \t} as backslash escaped text, and no other
     * backslash escaped text is allowed.</li>
     * <li>Scanner put unescaped form in its output, which is then provided as to this format decoder, as input
     * {@link #text}.</li>
     * <li>Format decoder calculates {@link #offset} and {@link #length} values based on the original text.</li>
     * <li>The {@link #offset} and {@link #length} values can be used to construct position information covering the
     * original text.</li>
     * </ul>
     *
     * @param text The input {@link #text} of the format pattern to decode.
     * @return A sequence of format descriptions. May include format descriptions with an {@link Conversion#ERROR}
     *     {@link FormatDescription#conversion} if the format pattern is invalid.
     */
    public List<FormatDescription> decode(String text) {
        // Initialize.
        this.text = text;
        result = list();
        parsed = "";
        idx = 0;
        offset = 0;
        length = 0;

        // Decode all characters.
        OUTER:
        while (true) {
            // Get current character, and stop scanning if end is reached.
            char c = readChar();
            if (c == EOF) {
                break;
            }

            // Literal text. For backslash escaped input, assume that a scanner
            // was used that turned backslash escaped input into unescaped
            // form. Such backslash escaped input is stored in unescaped form
            // in the format description, but registers as length two, to apply
            // to the original backslash escaped input.
            if (c != '%') {
                if (c == '\\' || c == '\"' || c == '\n' || c == '\t') {
                    length++;
                }
                parsed += String.valueOf(c);
                continue;
            }

            // Add literal, which is terminated by starting a format specifier.
            length--;
            if (length > 0) {
                addLiteral();
            }

            // Update for already read '%', and move on.
            length = 1;
            c = readChar();

            // Explicit index.
            index = "";
            width = "";
            boolean indexMissing = false;
            while ((c >= '1' && c <= '9') || (!index.isEmpty() && c == '0')) {
                index += String.valueOf(c);
                c = readChar();
            }

            if (c == '$') {
                c = readChar();
                if (index.isEmpty()) {
                    indexMissing = true;
                }
            } else if (!index.isEmpty()) {
                width = index;
                index = "";
            }

            // Flags, in any order.
            flags = "";
            if (width.isEmpty()) {
                while (StringUtils.contains(FLAGS, c)) {
                    flags += String.valueOf(c);
                    c = readChar();
                }
            }

            // Width.
            if (width.isEmpty()) {
                while ((c >= '1' && c <= '9') || (!width.isEmpty() && c == '0')) {
                    width += String.valueOf(c);
                    c = readChar();
                }
            }

            // Precision.
            precision = "";
            boolean precisionMissing = false;
            if (c == '.') {
                c = readChar();
                while (c >= '0' && c <= '9') {
                    precision += String.valueOf(c);
                    c = readChar();
                }
                if (precision.isEmpty()) {
                    precisionMissing = true;
                }
            }

            // Done scanning format specifier text, including its conversion
            // character. Now that we scanned the complete thing, we can detect
            // errors, without the risk of skipping reading the remainder.

            // Check for missing information.
            if (indexMissing) {
                addError("Invalid format specifier: missing explicit index before \"$\".");
                continue;
            }

            if (precisionMissing) {
                addError("Invalid format specifier: missing format precision after \".\".");
                continue;
            }

            // Check for duplicate flags.
            String scanFlags = flags;
            flags = "";
            for (int i = 0; i < FLAG_CHARS.length; i++) {
                int cnt = StringUtils.countMatches(scanFlags, FLAG_CHARS[i]);
                if (cnt > 1) {
                    addError(fmt("Invalid format specifier: duplicate \"%s\" flag.", FLAG_CHARS[i]));
                    continue OUTER;
                }
                if (cnt == 1) {
                    flags += FLAG_CHARS[i];
                }
            }

            // Check for general invalid flag/width combinations.
            if (flags.contains("-") && flags.contains("0")) {
                addError("Invalid format specifier: flags \"-\" and \"0\" cannot be combined.");
                continue;
            }

            if (flags.contains("+") && flags.contains(" ")) {
                addError("Invalid format specifier: flags \"+\" and \" \" (space) cannot be combined.");
                continue;
            }

            if (flags.contains("-") && width.isEmpty()) {
                addError("Invalid format specifier: flag \"-\" requires a width.");
                continue;
            }

            if (flags.contains("0") && width.isEmpty()) {
                addError("Invalid format specifier: flag \"0\" requires a width.");
                continue;
            }

            // Conversion character.
            conversion = String.valueOf(c);
            switch (c) {
                case EOF:
                    addError("Invalid format specifier: the specifier is incomplete.");
                    break;

                case 'b':
                case 'B':
                    for (char ch: flags.toCharArray()) {
                        if (ch != '-') {
                            addError(fmt("Invalid format specifier: a \"%%%c\" specifier can not have a \"%c\" flag.",
                                    c, ch));
                            continue OUTER;
                        }
                    }
                    if (!precision.isEmpty()) {
                        addError(fmt(
                                "Invalid format specifier: a \"%%%c\" specifier can not have a formatting precision.",
                                c));
                        continue;
                    }
                    addBoolean();
                    continue;

                case 's':
                case 'S':
                    for (char ch: flags.toCharArray()) {
                        if (ch != '-') {
                            addError(fmt("Invalid format specifier: a \"%%%c\" specifier can not have a \"%c\" flag.",
                                    c, ch));
                            continue OUTER;
                        }
                    }
                    if (!precision.isEmpty()) {
                        addError(fmt(
                                "Invalid format specifier: a \"%%%c\" specifier can not have a formatting precision.",
                                c));
                        continue;
                    }
                    addString();
                    continue;

                case 'x':
                case 'X':
                    for (char ch: flags.toCharArray()) {
                        if (ch != '-' && ch != '0') {
                            addError(fmt("Invalid format specifier: a \"%%%c\" specifier can not have a \"%c\" flag.",
                                    c, ch));
                            continue OUTER;
                        }
                    }
                    if (!precision.isEmpty()) {
                        addError(fmt(
                                "Invalid format specifier: a \"%%%c\" specifier can not have a formatting precision.",
                                c));
                        continue;
                    }
                    addInteger();
                    continue;

                case 'd':
                    if (!precision.isEmpty()) {
                        addError(fmt(
                                "Invalid format specifier: a \"%%%c\" specifier can not have a formatting precision.",
                                c));
                        continue;
                    }
                    addInteger();
                    continue;

                case 'e':
                case 'E':
                    for (char ch: flags.toCharArray()) {
                        if (ch == ',') {
                            addError(fmt("Invalid format specifier: a \"%%%c\" specifier can not have a \"%c\" flag.",
                                    c, ch));
                            continue OUTER;
                        }
                    }
                    addReal();
                    continue;

                case 'f':
                case 'g':
                case 'G':
                    addReal();
                    continue;

                case '%':
                    if (!index.isEmpty()) {
                        addError("Invalid format specifier: a \"%%\" specifier can not have an index.");
                        continue;
                    }
                    if (!flags.isEmpty()) {
                        addError("Invalid format specifier: a \"%%\" specifier can not have a formatting flag.");
                        continue;
                    }
                    if (!width.isEmpty()) {
                        addError("Invalid format specifier: a \"%%\" specifier can not have a formatting width.");
                        continue;
                    }
                    if (!precision.isEmpty()) {
                        addError("Invalid format specifier: a \"%%\" specifier can not have a formatting precision.");
                        continue;
                    }
                    parsed = "%";
                    addLiteral();
                    continue;

                default:
                    addError(fmt("Invalid format specifier: unknown \"%%%c\" formatting conversion.", c));
                    continue;
            }
        }

        // Add literal, which is terminated by EOF.
        if (length > 0) {
            addLiteral();
        }

        // Return decoding result.
        return result;
    }

    /**
     * Reads and returns the {@link #idx current} character. If the current character is beyond the end of the input
     * {@link #text}, {@link #EOF} is returned instead. If a character is read, the next character becomes the current
     * character, and the length of the format description {@link #length} is updated to reflect reading another
     * character.
     *
     * @return The current character, or {@code EOF}.
     */
    private char readChar() {
        // Detect EOF.
        if (idx >= text.length()) {
            return EOF;
        }

        // Read current character, move to next, and update length.
        char c = text.charAt(idx);
        idx++;
        length++;
        return c;
    }

    /**
     * Adds a decoded format description to the {@link #result}, and sets up for the next format description, by setting
     * {@link #parsed} to empty, increasing {@link #offset} by {@link #length}, and setting {@link #length} to zero.
     *
     * @param description The decoded format description.
     */
    private void add(FormatDescription description) {
        result.add(description);
        parsed = "";
        offset += length;
        length = 0;
    }

    /** Adds a format description for a decoded literal. */
    private void addLiteral() {
        FormatDescription rslt = new FormatDescription(Conversion.LITERAL, null, null, parsed, null, null, offset,
                length);
        add(rslt);
    }

    /** Adds a format description for a decoded boolean conversion. */
    private void addBoolean() {
        FormatDescription rslt = new FormatDescription(Conversion.BOOLEAN, index, flags, conversion, width, null,
                offset, length);
        add(rslt);
    }

    /** Adds a format description for a decoded string conversion. */
    private void addString() {
        FormatDescription rslt = new FormatDescription(Conversion.STRING, index, flags, conversion, width, null, offset,
                length);
        add(rslt);
    }

    /** Adds a format description for a decoded integer conversion. */
    private void addInteger() {
        FormatDescription rslt = new FormatDescription(Conversion.INTEGER, index, flags, conversion, width, null,
                offset, length);
        add(rslt);
    }

    /** Adds a format description for a decoded real conversion. */
    private void addReal() {
        FormatDescription rslt = new FormatDescription(Conversion.REAL, index, flags, conversion, width, precision,
                offset, length);
        add(rslt);
    }

    /**
     * Adds a format description for an error.
     *
     * @param text The text describing the error.
     */
    private void addError(String text) {
        FormatDescription rslt = new FormatDescription(Conversion.ERROR, null, null, text, null, null, offset, length);
        add(rslt);
    }
}
