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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.WarnOutput;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Generator for obtaining clash-free names in the generated code. */
public class NameGenerator {
    /**
     * All names in the PLC language standard, as an unmodifiable set. These are to be avoided for generated names.
     *
     * <p>
     * The keywords in the PLC language are case insensitive. This set only contains the names in lower-case ASCII.
     * </p>
     */
    private static final Set<String> PLC_LANGUAGE_KEYWORDS;

    /** Iff set, warn the user about renaming names. */
    private final boolean warnOnRename;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /** Numeric suffix value already given out to a caller, ordered by the name prefix. */
    private Map<String, Integer> maxSuffixes;

    /**
     * Constructor of the {@link NameGenerator} class.
     *
     * @param settings Configuration to use.
     */
    public NameGenerator(PlcGenSettings settings) {
        warnOnRename = settings.warnOnRename;
        warnOutput = settings.warnOutput;

        maxSuffixes = map();
        for (String name: PLC_LANGUAGE_KEYWORDS) {
            maxSuffixes.put(name, 0);
        }
    }

    /**
     * Convert the given object to something that does not clash with the PLC language or with previously generated names.
     *
     * @param posObject Named CIF object.
     * @return A safe name that does not clash with either the PLC language keywords or names generated earlier.
     */
    public String generateName(PositionObject posObject) {
        Assert.check(CifTextUtils.hasName(posObject),
                fmt("Missing name for \"%s\".", (posObject == null) ? "null" : posObject.toString()));
        return generateName(CifTextUtils.getAbsName(posObject, false), true);
    }

    /**
     * Convert the given name to something that does not clash with the PLC language or with previously generated names.
     *
     * @param initialName Suggested name to to use.
     * @param initialIsCifName Whether the initial name is known by the CIF user. For objects that have no name in CIF,
     *     producing rename warnings is meaningless.
     * @return A safe name that does not clash with PLC language keywords or previously generated names.
     */
    public String generateName(String initialName, boolean initialIsCifName) {
        // Construct all parts of an identifier from the input text and extract the found values.
        NameCleaner nameCleaner = new NameCleaner(initialName);
        String baseName = nameCleaner.getNamePrefix();
        String lowerBaseName = nameCleaner.getLowerCaseNamePrefix();
        int currentSuffix = nameCleaner.getNumericSuffix();

        // Make the identifier unique by finding an unused numeric suffix, and attach it.
        Assert.check(currentSuffix >= 0); // Protects for using -1 below.
        int maxUsedNumber = maxSuffixes.getOrDefault(lowerBaseName, -1);
        if (maxUsedNumber < 0 && !nameCleaner.hasSuffix()) {
            // First use of a name without numeric suffix -> use as-is.
            //
            // Store it as 0 suffix, next use will get at least value 1 appended.
            maxSuffixes.put(lowerBaseName, 0);
            return baseName;
        } else if (maxUsedNumber >= currentSuffix) {
            // Handed out a higher number already -> use the next free higher number.
            maxSuffixes.put(lowerBaseName, maxUsedNumber + 1);
            String newName = baseName + String.valueOf(maxUsedNumber + 1);

            if (initialIsCifName && warnOnRename) {
                warnOutput.warn("Renaming \"%s\" to \"%s\".", initialName, newName);
            }
            return newName;
        } else {
            // The initial name uses a higher number suffix -> use it.
            maxSuffixes.put(lowerBaseName, currentSuffix);
            return baseName + String.valueOf(currentSuffix);
        }
    }

    /**
     * Class to clean an arbitrary sequence of characters to an ASCII identifier. The class is tailored towards the
     * needs of the {@link NameGenerator} class.
     *
     * <p>
     * Everything other than upper-case ASCII letters, lower-case ASCII letters and ASCII decimal digits are considered
     * garbage and replaced by an underscore character. Sequences of underscore characters as well as a leading or
     * trailing underscore character does not occur in the result. To meet the requirements of an identifier, an
     * initial letter may be added.
     * </p>
     *
     * <p>
     * The result of the cleaning process is a {@link #getNamePrefix name prefix} and a {@link #getNumericSuffix
     * numeric suffix}. It always returns a numeric suffix although you can {@link #hasSuffix query its existence} in
     * the input. For uniqueness checking the class also provides a lower-case variant of the prefix that can be queried
     * with the {@link #getLowerCaseNamePrefix} method.
     * </p>
     *
     * <p>
     * Both the prefix and the suffix must be requested separately. The resulting identifier can be constructed with
     * {@code getNamePrefix() + String.valueOf(getNumericSuffix())}.
     * </p>
     */
    static class NameCleaner {
        /** Default single lower-case letter name to use if no prefix can be constructed. */
        static final char DEFAULT_CHAR = 'x';

        /**
         * Cleaned name as a sequence of letters, digits, and underscore characters. Array is valid until
         * {@link #cleanedLength}.
         */
        private char[] cleaned;

        /**
         * Same as {@link #cleaned}, but letters are converted to lower-case. Just like {@link #cleaned}, array is valid
         * until {@link #cleanedLength}.
         */
        private char[] cleanedLower;

        /** Number of valid characters in {@link #cleaned} and {@link #cleanedLower}. */
        private int cleanedLength;

        /**
         * Index of the first digit of the last digit sequence in {@cleaned} or {@link Integer#MAX_VALUE} if there is no
         * such digit.
         */
        private int digitsStartIndex;

        /**
         * Denotes that an underscore character should be inserted before the next character in {@link #cleaned} and
         * {@link #cleanedLower}.
         */
        private boolean pendingUnderscore;

        /**
         * Constructor of the {@link NameCleaner} class.
         *
         * @param initialName Name to clean.
         */
        NameCleaner(String initialName) {
            // Allocate enough space to store the entire initial name.
            // Make it one longer as it might insert a default letter at the start.
            int cleanedSize = 1 + Math.max(1, initialName.length());
            cleaned = new char[cleanedSize];
            cleanedLower = new char[cleanedSize];

            cleanedLength = 0;
            digitsStartIndex = Integer.MAX_VALUE;
            pendingUnderscore = false;
            for (int idx = 0; idx < initialName.length(); idx++) {
                char k = initialName.charAt(idx);
                if (isUpperCaseAsciiLetter(k)) {
                    if (pendingUnderscore) {
                        appendCleaned('_', '_');
                        pendingUnderscore = false;
                    }
                    appendCleaned(k, toLowerCaseAscii(k));
                    digitsStartIndex = Integer.MAX_VALUE;
                } else if (isLowerCaseAsciiLetter(k)) {
                    if (pendingUnderscore) {
                        appendCleaned('_', '_');
                        pendingUnderscore = false;
                    }
                    appendCleaned(k, k);
                    digitsStartIndex = Integer.MAX_VALUE;
                } else if (isAsciiDigit(k)) {
                    if (cleanedLength == 0) {
                        // First letter is actually a digit, insert a letter prefix first.
                        appendCleaned(DEFAULT_CHAR, DEFAULT_CHAR);
                        pendingUnderscore = false;
                    } else if (pendingUnderscore) {
                        appendCleaned('_', '_');
                        pendingUnderscore = false;
                        digitsStartIndex = Integer.MAX_VALUE;
                    }
                    digitsStartIndex = Math.min(cleanedLength, digitsStartIndex);
                    appendCleaned(k, k);
                } else {
                    pendingUnderscore = (cleanedLength > 0);
                }
            }

            // Fallback in case the input has no valid characters at all.
            if (cleanedLength == 0) {
                cleaned[0] = DEFAULT_CHAR;
                cleanedLower[0] = toLowerCaseAscii(DEFAULT_CHAR);
                cleanedLength = 1;
            }

            // If necessary, reposition start of the digits suffix just after the last valid character to make it a
            // valid index.
            digitsStartIndex = Math.min(digitsStartIndex, cleanedLength);

            // We should have a name prefix at the very least.
            Assert.check(cleanedLength > 0);
            Assert.check(digitsStartIndex > 0);
        }

        /**
         * Append a character to both {@link #cleaned} and {@link #cleanedLower}.
         *
         * @param cleanedChar Character to append to {@link #cleaned}.
         * @param cleanedLowerChar Character to append to {@link #cleanedLower}.
         */
        private void appendCleaned(char cleanedChar, char cleanedLowerChar) {
            cleaned[cleanedLength] = cleanedChar;
            cleanedLower[cleanedLength] = cleanedLowerChar;
            cleanedLength++;
        }

        /**
         * Retrieve the cleaned name with preserved case without the last digit sequence at the end.
         *
         * @return The name prefix of the cleaned name with preserved case without the last digit sequence at the end.
         */
        String getNamePrefix() {
            return new String(cleaned, 0, digitsStartIndex);
        }

        /**
         * Retrieve the cleaned name converted to lower case ASCII without the last digit sequence at the end.
         *
         * @return The name prefix of the cleaned name converted to lower case without the last digit sequence at the
         *     end.
         */
        String getLowerCaseNamePrefix() {
            return new String(cleanedLower, 0, digitsStartIndex);
        }

        /**
         * Construct a safe numeric value from the last digit sequence at the end of the cleaned name if it exists, else
         * return {@code 0}.
         *
         * <p>
         * To avoid numeric overflow only the first 6 digits of the sequence are used for the numeric value.
         * </p>
         *
         * @return A safe numeric value of the digit sequence at the end of the cleaned name, or {@code 0} if there is
         *     no numeric suffix.
         */
        int getNumericSuffix() {
            long value = 0;
            int index = digitsStartIndex;
            while (index < cleanedLength && index - digitsStartIndex < 6) {
                value = value * 10 + cleaned[index] - '0';
                index++;
            }
            return (int)value;
        }

        /**
         * Does a numeric suffix exist?
         *
         * @return Whether a numeric suffix exists.
         */
        boolean hasSuffix() {
            return digitsStartIndex < cleanedLength;
        }

        /**
         * Test whether the given character is an upper-case ASCII letter.
         *
         * @param c Character to test.
         * @return Whether the given character is an upper-case ASCII letter.
         */
        static boolean isUpperCaseAsciiLetter(char c) {
            return c >= 'A' && c <= 'Z';
        }

        /**
         * Test whether the given character is a lower-case ASCII letter.
         *
         * @param c Character to test.
         * @return Whether the given character is a lower-case ASCII letter.
         */
        static boolean isLowerCaseAsciiLetter(char c) {
            return c >= 'a' && c <= 'z';
        }

        /**
         * Convert upper-case ASCII letters to lower-case letters.
         *
         * @param c Character to convert.
         * @return Lower-case equivalent if the input was an upper-case ASCII letter, else unchanged.
         */
        static char toLowerCaseAscii(char c) {
            return isUpperCaseAsciiLetter(c) ? (char)(c + 'a' - 'A') : c;
        }

        /**
         * Test whether the given character is an ASCII digit.
         *
         * @param c Character to test.
         * @return Whether the given character is an ASCII digit.
         */
        private static boolean isAsciiDigit(char c) {
            return c >= '0' && c <= '9';
        }
    }

    static {
        // Keywords of the language, note that "en" and "eno" special parameter names have been left out.
        String[] languageKeywords = new String[] {"action", "end_action", "array", "of", "at", "case", "of", "else",
                "end_case", "configuration", "end_configuration", "constant", "exit", "false", "f_edge", "for", "to",
                "by", "do", "end_for", "function", "end_function", "function_block", "end_function_block", "if", "then",
                "elsif", "else", "end_if", "initial_step", "end_step", "not", "mod", "and", "xor", "or", "program",
                "with", "program", "end_program", "r_edge", "read_only", "read_write", "repeat", "until", "end_repeat",
                "resource", "on", "end_resource", "retain", "end_retain", "return", "step", "end_step", "struct",
                "end_struct", "task", "transition", "from", "to", "end_transition", "true", "type", "end_type", "var",
                "end_var", "var_input", "var_output", "var_in_out", "var_temp", "var_external", "var_access",
                "var_config", "var_gloval", "while", "do", "end_while", "with"};

        String[] typeKeywords = new String[] {"bool", "sint", "int", "dint", "lint", "usint", "uint", "ulint", "udint",
                "real", "lreal", "time", "date", "time_of_day", "tod", "date_and_time", "dt", "string", "byte", "word",
                "dword", "lword", "wstring"};

        String[] genericTypeKeywords = new String[] {"any", "and_derived", "any_elementary", "any_magnitude", "any_num",
                "any_real", "any_int", "any_bit", "any_string", "any_date"};

        // Construct a set container of appropriate size.
        int numTypes = genericTypeKeywords.length;
        int keywordCount = languageKeywords.length + typeKeywords.length + genericTypeKeywords.length
                + numTypes * (numTypes - 1);
        Set<String> keywords = setc(keywordCount);

        // Add everything.
        addAll(keywords, languageKeywords);
        addAll(keywords, typeKeywords);
        addAll(keywords, genericTypeKeywords);

        // Casts (X_TO_Y functions).
        for (int i = 0; i < typeKeywords.length; i++) {
            for (int j = 0; j < typeKeywords.length; j++) {
                if (i == j) {
                    continue;
                }
                keywords.add(typeKeywords[i] + "_to_" + typeKeywords[j]);
            }
        }

        // TODO: Add standard library function names.
        // TODO: Add standard function block names.

        PLC_LANGUAGE_KEYWORDS = Collections.unmodifiableSet(keywords);
    }

    /**
     * Add all values into {@code dest}.
     *
     * @param dest Destination of all values. Modified in-place.
     * @param values New values to add.
     */
    private static void addAll(Set<String> dest, String[] values) {
        // Set.addAll doesn't work with array.
        for (int i = 0; i < values.length; i++) {
            dest.add(values[i]);
        }
    }
}
