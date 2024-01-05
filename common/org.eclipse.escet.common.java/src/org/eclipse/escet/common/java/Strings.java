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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.Collection;
import java.util.Comparator;
import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

/** Helper methods for working with strings. */
public final class Strings {
    /**
     * Regular expression pattern for a single whitespace character.
     *
     * @see Character#isWhitespace
     */
    private static final Pattern WHITESPACE_CHAR_PAT = Pattern.compile("\\s");

    /** Cache with commonly used prefix indent strings. */
    private static final String[] INDENT_CACHE;

    static {
        INDENT_CACHE = new String[60];
        for (int i = 0; i < INDENT_CACHE.length; i++) {
            INDENT_CACHE[i] = StringUtils.leftPad("", i);
        }
    }

    /** Constructor for the {@link Strings} class. */
    private Strings() {
        // Static class.
    }

    /** Line separator for the current platform. Is equal to: {@code System.getProperty("line.separator")}. */
    public static final String NL = System.getProperty("line.separator");

    /**
     * Returns a formatted string using the specified format string and arguments.
     *
     * <p>
     * No localization is applied, which defaults to the {@link Locale#US} locale.
     * </p>
     *
     * <p>
     * This method behaves exactly identical to {@code String.format(null, format, args)}.
     * </p>
     *
     * @param format A {@link Formatter} format string.
     * @param args The arguments referenced by the format specifiers in the format string. If there are more arguments
     *     than format specifiers, the extra arguments are ignored. The number of arguments is variable and may be zero.
     *     The maximum number of arguments is limited by the maximum dimension of a Java array as defined by the Java
     *     Virtual Machine Specification. The behavior on a {@code null} argument depends on the {@link Formatter}
     *     conversion.
     * @return A formatted string.
     * @throws IllegalFormatException If a format string contains an illegal syntax, a format specifier is incompatible
     *     with the given arguments, insufficient arguments are given for the format string, or other illegal
     *     conditions. For a specification of all possible formatting errors, see the details section of the
     *     {@link Formatter} class specification.
     * @throws NullPointerException If {@code format} is {@code null}.
     * @see Formatter
     * @see String#format(java.util.Locale, String, Object...)
     */
    public static String fmt(String format, Object... args) {
        return String.format(null, format, args);
    }

    /**
     * Indents a given string with a given number of spaces.
     *
     * @param text The text to indent.
     * @param indentLength The number of spaces to indent with, non-positive lengths are ignored.
     * @return The indented text.
     */
    public static String indent(String text, int indentLength) {
        return spaces(indentLength) + text;
    }

    /**
     * Returns a string with the desired number of spaces in it.
     *
     * @param desiredLength The desired number of spaces, non-positive values give zero spaces.
     * @return A string with the given desired number of spaces in it.
     */
    public static String spaces(int desiredLength) {
        // Ignore non-positive requests.
        if (desiredLength <= 0) {
            return "";
        }

        // Grab an existing string if possible.
        if (desiredLength < INDENT_CACHE.length) {
            return INDENT_CACHE[desiredLength];
        }

        // Build a new string of the desired length.
        StringBuilder stringBuilder = new StringBuilder(desiredLength);
        while (desiredLength > 0) {
            int addLength = Math.min(desiredLength, INDENT_CACHE.length - 1);
            stringBuilder.append(INDENT_CACHE[addLength]);
            desiredLength -= addLength;
        }
        return stringBuilder.toString();
    }

    /**
     * String sorter that compares strings in a smart case insensitive manner. That is, strings are sorted in the normal
     * case insensitive manner, and if they compare equal, in a case sensitive manner. This ensures deterministic
     * output.
     */
    public static final Comparator<String> SORTER = new StringComparator();

    /**
     * String sorter that compares strings in a smart case insensitive manner. That is, strings are sorted in the normal
     * case insensitive manner, and if they compare equal, in a case sensitive manner. This ensures deterministic
     * output.
     */
    protected static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String arg0, String arg1) {
            // First compare in a case insensitive manner.
            int rslt = String.CASE_INSENSITIVE_ORDER.compare(arg0, arg1);
            if (rslt != 0) {
                return rslt;
            }

            // The are equal in a case insensitive manner, so compare them in
            // a case sensitive manner.
            return arg0.compareTo(arg1);
        }
    }

    /**
     * Converts a string to a Java string literal. Applies escaping, and adds the surrounding double quotes. Can also
     * handle {@code null} values.
     *
     * <p>
     * To only apply the Java string literal escaping, use the {@link StringEscapeUtils#escapeJava} method.
     * </p>
     *
     * @param s The string to convert to a Java string literal.
     * @return The string as Java string literal, or {@code "<null>"}.
     */
    public static String stringToJava(String s) {
        return (s == null) ? "<null>" : "\"" + StringEscapeUtils.escapeJava(s) + "\"";
    }

    /**
     * Converts an array of strings to a Java string literals. Can also handle {@code null} values.
     *
     * @param sa The array of strings to convert to a Java string literals.
     * @return The array of strings as Java string literals.
     */
    public static String stringArrayToJava(String[] sa) {
        List<String> rslt = listc(sa.length);
        for (String s: sa) {
            rslt.add(stringToJava(s));
        }
        return String.join(", ", rslt);
    }

    /**
     * Wraps text lines into text lines that satisfy the maximum width constraint.
     *
     * @param lineLength The maximum length of the output text lines.
     * @param texts The input text lines.
     * @return The output text lines, which satisfy the maximum width constraint.
     * @throws IllegalArgumentException If the text can not be wrapped due to no whitespace being available to wrap at.
     */
    public static String[] wrap(int lineLength, String... texts) {
        List<String> rslt = list();
        for (String text: texts) {
            while (text.length() > lineLength) {
                int i = text.lastIndexOf(' ', lineLength);
                if (i <= 0) {
                    String msg = fmt("No whitespace in \"%s\".",
                            ((text.length() < lineLength) ? text : text.substring(0, lineLength + 1)));
                    throw new IllegalArgumentException(msg);
                }
                rslt.add(text.substring(0, i));
                text = text.substring(i + 1);
            }
            rslt.add(text);
        }
        return rslt.toArray(new String[] {});
    }

    /**
     * Wraps text lines into text lines that satisfy the maximum width constraint of no more than 79 characters per
     * output line.
     *
     * @param texts The input text lines.
     * @return The output text lines, which satisfy the maximum width constraint.
     */
    public static String[] wrap(String... texts) {
        return wrap(79, texts);
    }

    /**
     * Wraps text lines into text lines that satisfy the maximum width constraint, and merges the output text lines into
     * a single string. The first line is prefixed with a given prefix, and the other lines are prefixed with another
     * given prefix.
     *
     * <p>
     * All lines are wrapped if too long. However, the maximum width of the lines is reduced with the maximum of the
     * length of both prefixes. Therefore, if the prefixes have different lengths, the formatting may not be optimal.
     * </p>
     *
     * @param lineLength The maximum length of the output text lines.
     * @param firstPrefix The prefix for the first line, or {@code null} for no prefix.
     * @param otherPrefix The prefix for the other lines (all lines except for the first line), or {@code null} for no
     *     prefix.
     * @param texts The input text lines.
     * @return The output text lines, which satisfy the maximum width constraint.
     */
    public static String wrapEx(int lineLength, String firstPrefix, String otherPrefix, String... texts) {
        String pre1 = (firstPrefix == null) ? "" : firstPrefix;
        String pre2 = (otherPrefix == null) ? "" : otherPrefix;

        int maxPreLen = Math.max(pre1.length(), pre2.length());
        String[] lines = wrap(lineLength - maxPreLen, texts);
        boolean first = true;
        StringBuilder rslt = new StringBuilder();
        for (String line: lines) {
            rslt.append(first ? pre1 : "\n" + pre2);
            first = false;
            rslt.append(line);
        }
        return rslt.toString();
    }

    /**
     * Wraps text lines into text lines that satisfy the maximum width constraint of no more than 79 characters per
     * output line, and merges the output text lines into a single string. The first line is prefixed with a given
     * prefix, and the other lines are prefixed with another given prefix.
     *
     * <p>
     * All lines are wrapped if too long. However, the maximum width of the lines is reduced with the maximum of the
     * length of both prefixes. Therefore, if the prefixes have different lengths, the formatting may not be optimal.
     * </p>
     *
     * @param firstPrefix The prefix for the first line, or {@code null} for no prefix.
     * @param otherPrefix The prefix for the other lines (all lines except for the first line), or {@code null} for no
     *     prefix.
     * @param texts The input text lines.
     * @return The output text lines, which satisfy the maximum width constraint.
     */
    public static String wrapEx(String firstPrefix, String otherPrefix, String... texts) {
        return wrapEx(79, firstPrefix, otherPrefix, texts);
    }

    /**
     * Returns a backslash escaped version of the string. That is:
     * <ul>
     * <li>backslash ({@code "\\"}) becomes {@code "\\\\"}</li>
     * <li>new line ({@code "\n"}) becomes {@code "\\n"}</li>
     * <li>tab ({@code "\t"}) becomes {@code "\\t"}</li>
     * <li>double quotes ({@code "\""}) becomes {@code "\\\""}</li>
     * </ul>
     *
     * @param s The string value to escape.
     * @return The backslash escaped string value.
     * @see #unescape
     */
    public static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t").replace("\"", "\\\"");
    }

    /**
     * Returns an unescaped string value, from a backslash escaped string. That is:
     * <ul>
     * <li>escaped backslash ({@code "\\\\"}) becomes {@code "\\"}</li>
     * <li>escaped new line ({@code "\\n"}) becomes {@code "\n"}</li>
     * <li>escaped tab ({@code "\\t"}) becomes {@code "\t"}</li>
     * <li>escaped double quotes ({@code "\\\""}) becomes {@code "\""}</li>
     * </ul>
     *
     * @param s The backslash escaped string value to unescape.
     * @return An unescaped string value.
     * @throws IllegalArgumentException If the backslash escaped string value contains any escape sequences other than
     *     the ones supported; or it ends with an escape character and thus ends prematurely.
     * @see #escape
     */
    public static String unescape(String s) {
        StringBuilder b = new StringBuilder(s);
        int idx = b.indexOf("\\");
        while (idx != -1) {
            if (idx == b.length() - 1) {
                // String ends with escape character: premature end of string.
                throw new IllegalArgumentException(
                        "String ends with escape character (premature end of string) in " + stringToJava(s));
            }

            char ss = b.charAt(idx + 1);
            switch (ss) {
                case '\\':
                    b.replace(idx, idx + 2, "\\");
                    break;
                case '\"':
                    b.replace(idx, idx + 2, "\"");
                    break;
                case 't':
                    b.replace(idx, idx + 2, "\t");
                    break;
                case 'n':
                    b.replace(idx, idx + 2, "\n");
                    break;
                default:
                    // Invalid escape sequence.
                    String msg = fmt("Invalid escape sequence at position %d in %s.", idx, stringToJava(s));
                    throw new IllegalArgumentException(msg);
            }

            idx++;
            idx = b.indexOf("\\", idx);
        }
        return b.toString();
    }

    /**
     * Converts a string into an array of Unicode code points.
     *
     * @param s The string to convert.
     * @return The array of Unicode code points.
     */
    public static int[] getCodePoints(String s) {
        int length = s.length();
        int count = s.codePointCount(0, length);
        int[] rslt = new int[count];
        int i = 0;
        for (int offset = 0; offset < length;) {
            int codepoint = s.codePointAt(offset);
            offset += Character.charCount(codepoint);
            rslt[i] = codepoint;
            i++;
        }
        return rslt;
    }

    /**
     * Converts a Unicode code point to a Java {@link String}, consisting of one or two {@link Character characters}.
     *
     * @param codePoint The Unicode code point.
     * @return The Java {@link String}.
     */
    public static String codePointToStr(int codePoint) {
        return String.valueOf(Character.toChars(codePoint));
    }

    /**
     * Is the Unicode code point a code point with basic type "Graphic", according to "The Unicode Standard Version 6.1
     * - Core Specification", Table 2-3 ("Types of Code Points"). Note that invalid code points (code points out of the
     * range of valid Unicode code points) are allowed.
     *
     * @param codePoint The Unicode code point.
     * @return {@code true} if the Unicode code point is of basic type "Graphic", {@code false} otherwise.
     */
    public static boolean isGraphicCodePoint(int codePoint) {
        switch (Character.getType(codePoint)) {
            // General Category "Letter".
            case Character.UPPERCASE_LETTER:
            case Character.LOWERCASE_LETTER:
            case Character.TITLECASE_LETTER:
            case Character.MODIFIER_LETTER:
            case Character.OTHER_LETTER:
                return true;

            // General Category "Mark".
            case Character.NON_SPACING_MARK:
            case Character.COMBINING_SPACING_MARK:
            case Character.ENCLOSING_MARK:
                return true;

            // General Category "Number".
            case Character.DECIMAL_DIGIT_NUMBER:
            case Character.LETTER_NUMBER:
            case Character.OTHER_NUMBER:
                return true;

            // General Category "Punctuation".
            case Character.CONNECTOR_PUNCTUATION:
            case Character.DASH_PUNCTUATION:
            case Character.START_PUNCTUATION:
            case Character.END_PUNCTUATION:
            case Character.INITIAL_QUOTE_PUNCTUATION:
            case Character.FINAL_QUOTE_PUNCTUATION:
            case Character.OTHER_PUNCTUATION:
                return true;

            // General Category "Symbol".
            case Character.MATH_SYMBOL:
            case Character.CURRENCY_SYMBOL:
            case Character.MODIFIER_SYMBOL:
            case Character.OTHER_SYMBOL:
                return true;

            // General Category "Separator".
            case Character.SPACE_SEPARATOR:
                return true;

            case Character.LINE_SEPARATOR:
            case Character.PARAGRAPH_SEPARATOR:
                return false;

            // General Category "Other".
            case Character.CONTROL:
            case Character.FORMAT:
            case Character.SURROGATE:
            case Character.PRIVATE_USE:
            case Character.UNASSIGNED:
                return false;

            // Invalid code points.
            default:
                Assert.check(!Character.isValidCodePoint(codePoint));
                return false;
        }
    }

    /**
     * Convert a string to an equal string with initial upper case letter if possible.
     *
     * @param s String to convert.
     * @return Converted string.
     */
    public static String makeInitialUppercase(String s) {
        if (s.isEmpty()) {
            return s;
        }
        String p = s.substring(0, 1).toUpperCase(Locale.US);
        return p + s.substring(1);
    }

    /**
     * Convert a string to an equal string with upper case letters if possible.
     *
     * @param s String to convert.
     * @return Converted string.
     */
    public static String makeUppercase(String s) {
        return s.toUpperCase(Locale.US);
    }

    /**
     * Construct a descriptive string listing the sorted names of the given elements.
     *
     * <p>
     * <ul>
     * <li>For a single element {@code K} it creates {@code "K"},</li>
     * <li>For two elements {@code K1}, {@code K2} it creates {@code "K1 or K2"},</li>
     * <li>For three or more elements {@code K1}, {@code K2}, {@code ..}, {@code Kn} it creates
     * {@code "K1, K2, ..., K(n-1) or Kn"},</li>
     * </ul>
     * </p>
     *
     * @param <T> Type of elements to list.
     * @param elements Elements to list, collection must be non-empty.
     * @param strFunc Conversion function of the elements to text, or {@code null} to use {@link Object#toString}.
     * @return The produced string.
     */
    public static <T> String makeElementsChoiceText(Collection<T> elements, Function<T, String> strFunc) {
        // Convert to text.
        Stream<String> converted;
        if (strFunc == null) {
            converted = elements.stream().map(Object::toString);
        } else {
            converted = elements.stream().map(strFunc::apply);
        }

        // Sort, construct, and return the string.
        StringBuilder sb = new StringBuilder();
        int numElements = elements.size();
        Assert.check(numElements > 0); // Else there is nothing to choose from.

        int numAdded = 0;
        for (String name: converted.sorted((n1, n2) -> n1.compareTo(n2)).toArray(String[]::new)) {
            if (numAdded == 0) {
                sb.append(name); // First name.
            } else if (numAdded + 1 < numElements) {
                sb.append(", ");
                sb.append(name); // Not first and not last name.
            } else {
                sb.append(" or ");
                sb.append(name); // Last name.
            }
            numAdded++;
        }
        return sb.toString();
    }

    /**
     * Duplicates a string a given number of times.
     *
     * @param str The string to duplicate.
     * @param cnt The number of times that the input string should occur in the output.
     * @return The concatenation of {@code cnt} times {@code str}.
     * @throws IllegalArgumentException If {@code cnt} is negative.
     */
    public static String duplicate(String str, int cnt) {
        if (cnt < 0) {
            throw new IllegalArgumentException("Negative cnt");
        }
        StringBuilder b = new StringBuilder(str.length() * cnt);
        for (int i = 0; i < cnt; i++) {
            b.append(str);
        }
        return b.toString();
    }

    /**
     * Returns the given string with all leading whitespace characters removed.
     *
     * @param str The string to left trim.
     * @return The left trimmed string.
     */
    public static String trimLeft(String str) {
        return StringUtils.stripStart(str, null);
    }

    /**
     * Returns the given string with all trailing whitespace characters removed.
     *
     * @param str The string to right trim.
     * @return The right trimmed string.
     */
    public static String trimRight(String str) {
        return StringUtils.stripEnd(str, null);
    }

    /**
     * Slices the given {@link String}. A slice is a sub-string of the original (input) string. The begin and end
     * indices can be thought of as the range of characters that should be maintained. That is, in principle, the range
     * of character indexes {@code [beginIndex .. endIndex - 1]} is maintained.
     *
     * <p>
     * Note that:
     * <ul>
     * <li>{@code slice(x, null, 2) + slice(x, 2, null) == x}, for any {@code x}. However,
     * {@code x.substring(0, 2) + x.substring(2)} gives the same result, as long as {@code x} is at least two characters
     * in length.</li>
     * <li>Out of range slice indices are handled gracefully. An index that is too large is replaced by the string size.
     * A lower bound larger than the upper bound results in an empty string. This applies to negative indices as
     * well.</li>
     * <li>Indices may be negative numbers, to start counting from the right instead of from the left. That is,
     * {@code -1} can be used instead of {@code str.length() - 1}, {@code -2} can be used instead of
     * {@code str.length() - 2}, etc. However, since {@code -0} is really the same as {@code 0}, it does not count from
     * the right.</li>
     * </ul>
     * </p>
     *
     * @param str The string to slice.
     * @param beginIndex The 0-based begin index (inclusive), or {@code null} for index {@code 0}.
     * @param endIndex The 0-based end index (exclusive), or {@code null} for the length of the input string.
     * @return The slice.
     */
    public static String slice(String str, Integer beginIndex, Integer endIndex) {
        // Get length once.
        int len = str.length();

        // Replace 'null' by defaults.
        int b = (beginIndex == null) ? 0 : beginIndex;
        int e = (endIndex == null) ? len : endIndex;

        // Handle negative indices.
        if (b < 0) {
            b = len + b;
        }
        if (e < 0) {
            e = len + e;
        }

        // Handle out of range and empty interval.
        if (b < 0) {
            b = 0;
        }
        if (e < 0) {
            e = 0;
        }

        if (b > len) {
            b = len;
        }
        if (e > len) {
            e = len;
        }

        if (b > e) {
            b = e;
        }

        // Use normal substring from Java to get actual result.
        return str.substring(b, e);
    }

    /**
     * Does the given string contain whitespace?
     *
     * @param str The string to check.
     * @return {@code true} if the given string contains at least one whitespace character, {@code false} otherwise.
     * @see Character#isWhitespace
     */
    public static boolean containsWhitespace(String str) {
        return WHITESPACE_CHAR_PAT.matcher(str).find();
    }

    /**
     * Converts the given object to a string using {@link String#valueOf}.
     *
     * @param obj The object. For primitive Java values auto-boxing is employed, unlike {@link String#valueOf}, which
     *     has multiple overloads.
     * @return The string representation of the object.
     */
    public static String str(Object obj) {
        return String.valueOf(obj);
    }

    /**
     * Replaces all occurrences of a regular expression pattern in the given input string by the given replacement.
     *
     * <p>
     * Is equal to {@code input.}{@link String#replaceAll replaceAll}{@code (pattern, replacement)}. This method thus
     * provides the same functionality, but as a static method.
     * </p>
     *
     * @param input The input string.
     * @param regex The regular expression pattern to find.
     * @param replacement The replacement. May contain references to captured subsequences.
     * @return The string with replacements performed.
     */
    public static String replaceRegex(String input, String regex, String replacement) {
        return input.replaceAll(regex, replacement);
    }

    /**
     * Truncate the given string to the given maximum length. If the given string is smaller than the maximum length,
     * nothing is changed. Otherwise, the trailing part of the input string that is too long is replaced by
     * {@code "..."} such that the returned string is precisely of the maximum length.
     *
     * @param input The string to truncate.
     * @param maxLength The maximum length of the result. Must be at least {@code 3}.
     * @return The truncated string.
     */
    public static String truncate(String input, int maxLength) {
        Assert.check(maxLength >= 3);

        if (input.length() <= maxLength) {
            return input;
        }
        String head = slice(input, 0, maxLength - 3);
        return head + "...";
    }

    /**
     * Construct a string that expresses value {@code val} with sufficient decimal digits to represent all non-negative
     * numbers up to and including {@code maxVal}.
     *
     * <p>
     * For example, {@code makeFixedLengthNumberText(15, 2345)} returns {@code "0015"} since 4 digits are needed for
     * value {@code 2345}.
     * </p>
     *
     * @param val Non-negative value to convert.
     * @param maxVal Maximum value to convert.
     * @return String with the value long enough to contain any non-negative value upto and including the maximum value.
     */
    public static String makeFixedLengthNumberText(int val, int maxVal) {
        Assert.check(val >= 0);
        Assert.check(maxVal >= val);
        return fmt("%0" + Integer.toString(maxVal).length() + "d", val);
    }
}
