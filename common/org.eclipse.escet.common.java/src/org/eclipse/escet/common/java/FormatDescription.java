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

package org.eclipse.escet.common.java;

import java.util.Objects;

/** Description of a component of a format pattern. */
public class FormatDescription {
    /** Conversion to perform. */
    public enum Conversion {
        /** Literal text, no conversion. */
        LITERAL,

        /** Output as boolean. */
        BOOLEAN,

        /** Output as integer number. */
        INTEGER,

        /** Output as real number. */
        REAL,

        /** Output as string. */
        STRING,

        /** Invalid format description. */
        ERROR,
    }

    /** The type of conversion to perform. */
    public final Conversion conversion;

    /**
     * The explicit 1-base index to use, {@code ""} if not specified, or {@code null} if not applicable. Not applicable
     * to the {@link Conversion#LITERAL} and {@link Conversion#ERROR} conversions.
     */
    public final String index;

    /**
     * The flags of the {@link #conversion} and {@link #text conversion character}, or {@code null} if not applicable
     * for the conversion. Not applicable to the {@link Conversion#LITERAL} and {@link Conversion#ERROR} conversions.
     *
     * <p>
     * Supported flags are {@code "-"}, {@code "+"}, {@code " "} (space), {@code "0"}, and {@code ","}. They may be
     * specified in any order, but are always stored in the same order. Duplicates are not allowed.
     * </p>
     *
     * <p>
     * The allowed flags per conversion are:
     * </p>
     *
     * <p>
     * <table border="1">
     * <tr>
     * <th>Conversion</th>
     * <th>Characters</th>
     * <th>{@code -}</th>
     * <th>{@code +}</th>
     * <th>space</th>
     * <th>{@code 0}</th>
     * <th>{@code ,}</th>
     * </tr>
     * <tr>
     * <td>{@link Conversion#BOOLEAN}</td>
     * <td>{@code b}, {@code B}</td>
     * <td>yes</td>
     * <td>-</td>
     * <td>-</td>
     * <td>-</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#INTEGER}</td>
     * <td>{@code d}</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#INTEGER}</td>
     * <td>{@code x}, {@code X}</td>
     * <td>yes</td>
     * <td>-</td>
     * <td>-</td>
     * <td>yes</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#REAL}</td>
     * <td>{@code e}, {@code E}</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#REAL}</td>
     * <td>{@code f}, {@code g}, {@code G}</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#STRING}</td>
     * <td>{@code s}, {@code S}</td>
     * <td>yes</td>
     * <td>-</td>
     * <td>-</td>
     * <td>-</td>
     * <td>-</td>
     * </tr>
     * </table>
     * </p>
     */
    public final String flags;

    /**
     * The text of the format description. Different information is stored, depending on the {@link #conversion}:
     *
     * <p>
     * <table border="1">
     * <tr>
     * <th>Conversion</th>
     * <th>Text</th>
     * </tr>
     * <tr>
     * <td>{@link Conversion#LITERAL}</td>
     * <td>The literal text, without {@link Strings#escape escaping} applied for {@code "\"} and {@code "%"}. The
     * {@link FormatDecoder} puts escaped percentage characters into a literal of their own, as a single percentage, and
     * never combines percentages with other literal text.</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#BOOLEAN}</td>
     * <td>The conversion character: {@code b} or {@code B}.</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#INTEGER}</td>
     * <td>The conversion character: {@code d}, {@code x}, or {@code X}.</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#REAL}</td>
     * <td>The conversion character: {@code e}, {@code E}, {@code F}, {@code g}, or {@code G}.</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#STRING}</td>
     * <td>The conversion character: {@code s} or {@code S}.</td>
     * </tr>
     * <tr>
     * <td>{@link Conversion#ERROR}</td>
     * <td>The error message.</td>
     * </tr>
     * </table>
     * </p>
     */
    public final String text;

    /**
     * The width of the {@link #conversion}, {@code ""} if unspecified, or {@code null} if not applicable. Not
     * applicable to the {@link Conversion#LITERAL} and {@link Conversion#ERROR} conversions.
     */
    public final String width;

    /**
     * The precision of the {@link #conversion}, {@code ""} if unspecified, of {@code null} if not applicable. Is only
     * applicable to {@link Conversion#REAL} conversions.
     */
    public final String precision;

    /**
     * The 0-based offset of the start of the format description, relative to the start of the format pattern that was
     * decoded into this and potentially other format descriptions.
     *
     * <p>
     * This offset applies to the original text that was scanned by a scanner, which recognizes {@code \\}, {@code \"},
     * {@code \n}, and {@code \t} as backslash escaped text, and doesn't support any other backslash escaped text. The
     * scanner then puts the unescaped text in its output, providing it to the {@link FormatDecoder#decode} method. The
     * {@link FormatDecoder} calculates this offset based on the original text as provided to the scanner. The offset
     * can be used to construct position information covering the original text, which may include backslash escaped
     * text.
     * </p>
     */
    public final int offset;

    /**
     * The length of the part of the text of the format pattern that was decoded into this format description.
     *
     * <p>
     * This length applies to the original text that was scanned by a scanner, which recognizes {@code \\}, {@code \"},
     * {@code \n}, and {@code \t} as backslash escaped text, and doesn't support any other backslash escaped text. The
     * scanner then puts the unescaped text in its output, providing it to the {@link FormatDecoder#decode} method. The
     * {@link FormatDecoder} calculates this length based on the original text as provided to the scanner. The length
     * can be used to construct position information covering the original text, which may include backslash escaped
     * text.
     * </p>
     */
    public final int length;

    /**
     * Constructor for the {@link FormatDescription} class.
     *
     * @param conversion The type of conversion to perform.
     * @param index The explicit 1-base index to use, {@code ""} if not specified, or {@code null} if not applicable.
     * @param flags The {@link #flags} of the {@link #conversion}, or {@code null} if not applicable for the conversion.
     * @param text The {@link #text} of the format description.
     * @param width The {@link #width} of the {@link #conversion}, {@code ""} if unspecified, or {@code null} if not
     *     applicable.
     * @param precision The {@link #precision} of the {@link #conversion}, {@code ""} if unspecified, of {@code null} if
     *     not applicable.
     * @param offset The 0-based {@link #offset} of the start of the format description, relative to the start of the
     *     format pattern that was decoded into this and potentially other format descriptions.
     * @param length The {@link #length} of the part of the text of the format pattern that was decoded in this format
     *     description.
     */
    public FormatDescription(Conversion conversion, String index, String flags, String text, String width,
            String precision, int offset, int length)
    {
        this.conversion = conversion;
        this.index = index;
        this.flags = flags;
        this.text = text;
        this.width = width;
        this.precision = precision;
        this.offset = offset;
        this.length = length;
    }

    /**
     * Get the explicit 1-based index.
     *
     * @return The explicit 1-based index, or {@code -1} in case of overflow. Note that negative numbers are not part of
     *     the lexical syntax.
     * @throws IllegalStateException If the format pattern specifier part does not have an explicit index.
     */
    public int getExplicitIndex() {
        if (index == null) {
            throw new IllegalStateException("no explicit idx");
        }
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException e) {
            // Integer overflow. Note that negative numbers are not part of the
            // lexical syntax.
            return -1;
        }
    }

    /**
     * Is the format description is simple enough to allow direct printing of its corresponding value, using
     * {@link String#valueOf} instead of having to use {@link Strings#fmt}? This method does not support the
     * {@link Conversion#ERROR} {@link #conversion}.
     *
     * @return {@code true} if the format description is simple enough to allow direct printing of its corresponding
     *     value, {@code false} otherwise.
     */
    public boolean isSimple() {
        switch (conversion) {
            case LITERAL:
                return true;

            case BOOLEAN:
                // All supported flags require a width.
                return width.isEmpty() && text.equals("b");

            case INTEGER:
                // Some supported flags don't require a width.
                return flags.isEmpty() && width.isEmpty() && text.equals("d");

            case REAL:
                // Double.toString does sophisticated decision making on the
                // number of decimals to print, and is different from %[efg].
                // Since this case concerns the latter, none of them are
                // simple, and we should always use a format pattern.
                return false;

            case STRING:
                // All supported flags require a width.
                return width.isEmpty() && text.equals("s");

            default:
                String msg = "Unexpected conversion: " + conversion;
                throw new RuntimeException(msg);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversion, index, flags, text, width, precision, offset, length);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FormatDescription)) {
            return false;
        }
        FormatDescription other = (FormatDescription)obj;
        return this.conversion == other.conversion && Objects.equals(this.index, other.index)
                && Objects.equals(this.flags, other.flags) && this.text.equals(other.text)
                && Objects.equals(this.width, other.width) && Objects.equals(this.precision, other.precision)
                && this.offset == other.offset && this.length == other.length;
    }

    /**
     * Converts the format description to a textual representation. For the {@link Conversion#ERROR}
     * {@link #conversion}, this returns the error message. For all other conversions, it returns the text to use in a
     * format pattern, without {@link Strings#escape escaping} applied for {@code "\"} and {@code "%"}.
     *
     * <p>
     * The {@link FormatDecoder} puts escaped percentage characters into a literal of their own, as a single percentage,
     * and never combines percentages with other literal text. That applies for the output of this method as well.
     * </p>
     *
     * <p>
     * This method uses {@link #toString(boolean)} and includes explicit indices if available.
     * </p>
     */
    @Override
    public String toString() {
        return toString(true);
    }

    /**
     * Converts the format description to a textual representation. For the {@link Conversion#ERROR}
     * {@link #conversion}, this returns the error message. For all other conversions, it returns the text to use in a
     * format pattern, without {@link Strings#escape escaping} applied for {@code "\"} and {@code "%"}.
     *
     * <p>
     * The {@link FormatDecoder} puts escaped percentage characters into a literal of their own, as a single percentage,
     * and never combines percentages with other literal text. That applies for the output of this method as well.
     * </p>
     *
     * @param includeIndex Whether to include an explicit index if available ({@code true}), or always omit it
     *     ({@code false}).
     * @return The textual representation.
     */
    public String toString(boolean includeIndex) {
        switch (conversion) {
            case INTEGER:
            case STRING:
            case BOOLEAN: {
                String idx = (!includeIndex || index.isEmpty()) ? "" : index + "$";
                return "%" + idx + flags + width + text;
            }

            case LITERAL:
            case ERROR:
                return text;

            case REAL: {
                String idx = (!includeIndex || index.isEmpty()) ? "" : index + "$";
                return "%" + idx + flags + width + (precision.isEmpty() ? "" : "." + precision) + text;
            }

            default:
                String msg = "Unknown conversion: " + conversion;
                throw new RuntimeException(msg);
        }
    }
}
