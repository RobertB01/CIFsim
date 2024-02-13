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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.output.WarnOutput;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * Generator for obtaining clash-free names in the generated code.
 *
 * <p>
 * The generator can generate globally unique names using {@link #generateGlobalName}. On top of globally unique names
 * you can create local names that are both locally and globally unique in a scope using {@link #generateLocalName}.
 * However, as local names are not taken into account for global names (it is assumed all global names occurring in the
 * local scope are created before generating local names), safely creating new global names for use in the same local
 * scope is not supported after creating the first local names in a scope.
 * </p>
 */
public class DefaultNameGenerator implements NameGenerator {
    /** Default single lower-case letter name to use if no prefix can be constructed. */
    static final char DEFAULT_CHAR = 'x';

    /**
     * All names in the PLC language standard, as an unmodifiable set. These are to be avoided for generated names.
     *
     * <p>
     * The keywords in the PLC language are case insensitive. This set only contains the names in lower-case ASCII.
     * </p>
     */
    private static final Set<String> PLC_LANGUAGE_KEYWORDS;

    /** If the value holds the user should be warned about changing the name, else the user should not be warned. */
    private final boolean warnOnRename;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /** Numeric suffix values already given out to a caller for globally unique names, ordered by the name prefix. */
    private Map<String, Integer> globalSuffixes;

    /**
     * Constructor of the {@link DefaultNameGenerator} class.
     *
     * @param settings Configuration to use.
     */
    public DefaultNameGenerator(PlcGenSettings settings) {
        warnOnRename = settings.warnOnRename;
        warnOutput = settings.warnOutput;

        globalSuffixes = map();
        for (String name: PLC_LANGUAGE_KEYWORDS) {
            globalSuffixes.put(name, 0);
        }
    }

    @Override
    public String generateGlobalName(PositionObject posObject) {
        Assert.check(CifTextUtils.hasName(posObject), "Missing name for \"" + String.valueOf(posObject) + "\".");
        return generateName(CifTextUtils.getAbsName(posObject, false), true, null);
    }

    @Override
    public String generateGlobalName(String initialName, boolean initialIsCifName) {
        return generateName(initialName, initialIsCifName, null);
    }

    @Override
    public String generateLocalName(String initialName, Map<String, Integer> localSuffixes) {
        return generateName(initialName, false, localSuffixes);
    }

    /**
     * Convert the given name to a proper name that does not clash with the PLC language or with previously generated
     * names.
     *
     * @param initialName Suggested name to to use.
     * @param initialIsCifName Whether the initial name is known by the CIF user. Used to produce rename warnings. As
     *     producing such rename warnings for objects that have no name in CIF is meaningless to the user, this
     *     parameter should be {@code false} for those names.
     * @param localSuffixes Name suffix information of local names. Use the same map to generate all local names in a
     *     scope. Must be {@code null} when generating global names.
     * @return A proper name that does not clash with PLC language keywords or previously generated global names. If a
     *     {@code localSuffixes} map is supplied, the produced names also don't clash with all previously generated
     *     local names that used that same map.
     */
    private String generateName(String initialName, boolean initialIsCifName, Map<String, Integer> localSuffixes) {
        // Cleanup the name.
        StringBuilder cleanedName = cleanString(initialName);

        // Make the name unique.
        String lowerCleanedName = cleanedName.toString().toLowerCase(Locale.US);
        int maxUsedNumber = globalSuffixes.getOrDefault(lowerCleanedName, -1);
        if (localSuffixes != null) {
            maxUsedNumber = Math.max(maxUsedNumber, localSuffixes.getOrDefault(lowerCleanedName, -1));
        }

        if (maxUsedNumber < 0) {
            // First use of a name without numeric suffix -> use as-is.
            //
            // Store it as 0 suffix, next use will get "_1" appended.
            if (localSuffixes != null) {
                localSuffixes.put(lowerCleanedName, 0);
            } else {
                globalSuffixes.put(lowerCleanedName, 0);
            }
            return cleanedName.toString();
        } else {
            // Identifier already used, append a new suffix.
            maxUsedNumber++;
            if (localSuffixes != null) {
                localSuffixes.put(lowerCleanedName, maxUsedNumber);
            } else {
                globalSuffixes.put(lowerCleanedName, maxUsedNumber);
            }

            cleanedName.append("_");
            cleanedName.append(maxUsedNumber);
            String newName = cleanedName.toString();
            if (initialIsCifName && warnOnRename) {
                warnOutput.line("Renaming \"%s\" to \"%s\".", initialName, newName);
            }
            return newName;
        }
    }

    /**
     * Cleanup the name.
     *
     * <p>
     * A name consists of alternating good and bad parts, where a good part is a sequence of letters and digits, and a
     * bad part is a sequence of non-letter and non-digit characters. Each good part is forced to start with a letter.
     * </p>
     * <p>
     * The good parts ore copied, and get separated with an underscore character.
     * </p>
     *
     * @param text Input text to clean up.
     * @return The cleaned-up name, wrapped in a string builder to assist in further manipulation of the name.
     */
    private StringBuilder cleanString(String text) {
        // Construct the destination string builder. Likely sufficient length is all text, 4 inserted default
        // characters, an underscore, and an assumed 3 digit number.
        StringBuilder sb = new StringBuilder(text.length() + 4 + 1 + 3);

        // Copy the good parts of the input text separated by an underscore character.
        char[] data = text.toCharArray();
        int inputIndex = 0;
        while (inputIndex < data.length) {
            // Find a good characters sequence. Possibly except for the first iteration, this is always non-empty.
            int length = matchGoodChars(data, inputIndex);

            // Force starting with a non-digit character.
            // At the start of the name, that ensures the result to be an identifier. After an '_', it ensures the
            // sequence '_[0-9]' never happens.
            if (length > 0 && Character.isDigit(data[inputIndex])) {
                sb.append(DEFAULT_CHAR);
            }

            // Copy the good characters, update the read index, and bail out of the end has been reached.
            sb.append(data, inputIndex, length); // May do nothing in the first iteration.
            inputIndex += length;
            if (inputIndex == data.length) {
                break;
            }

            // Find a bad characters sequence. Is always non-empty.
            length = matchBadChars(data, inputIndex);
            inputIndex += length;
            if (!sb.isEmpty() && inputIndex < data.length) { // If a good part is before and after it, insert an '_'.
                sb.append('_');
            }
        }

        // Force a non-empty result identifier.
        if (sb.isEmpty()) {
            sb.append(DEFAULT_CHAR);
        }
        return sb;
    }

    /**
     * Find a sequence of good characters (letters or digits) in the 'data' array at 'index'.
     *
     * @param data Characters to explore.
     * @param index Index to start the search.
     * @return Number of found good characters starting from 'data[index]'.
     */
    private int matchGoodChars(char[] data, int index) {
        int endIndex = index;
        while (endIndex < data.length) {
            char c = data[endIndex];
            if (Character.isLetter(c) || Character.isDigit(c)) {
                endIndex++;
            } else {
                break;
            }
        }
        return endIndex - index;
    }

    /**
     * Find a sequence of bad characters (anything else but letters or digits) in the 'data' array at 'index'.
     *
     * @param data Characters to explore.
     * @param index Index to start the search.
     * @return Number of found bad characters starting from 'data[index]'.
     */
    private int matchBadChars(char[] data, int index) {
        int endIndex = index;
        while (endIndex < data.length) {
            char c = data[endIndex];
            if (Character.isLetter(c) || Character.isDigit(c)) {
                break;
            } else {
                endIndex++;
            }
        }
        return endIndex - index;
    }

    static {
        // Keywords of the language, note that "en" and "eno" special parameter names have been left out.
        String[] languageKeywords = new String[] {
"action",
"array",
"at",
"by",
"case",
"configuration",
"constant",
"do",
"else",
"elsif",
"end_action",
"end_case",
"end_configuration",
"end_for",
"end_function",
"end_function_block",
"end_if",
"end_program",
"end_repeat",
"end_resource",
"end_retain",
"end_step",
"end_step",
"end_struct",
"end_transition",
"end_type",
"end_var",
"end_while",
"exit",
"false",
"f_edge",
"for",
"from",
"function",
"function_block",
"if",
"initial_step",
"of",
"on",
"program",
"read_only",
"read_write",
"r_edge",
"repeat",
"resource",
"retain",
"return",
"step",
"struct",
"task",
"then",
"to",
"transition",
"true",
"type",
"until",
"var",
"var_access",
"var_config",
"var_external",
"var_global",
"var_in_out",
"var_input",
"var_output",
"var_temp",
"while",
"with"
};

        String[] functionNames = new String[] {
"and",
"mod",
"not",
"or",
"xor"
        };

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
        keywords.addAll(Arrays.asList(languageKeywords));
        keywords.addAll(Arrays.asList(typeKeywords));
        keywords.addAll(Arrays.asList(genericTypeKeywords));

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
}
