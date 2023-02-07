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

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.WarnOutput;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Generator for obtaining clash-free names in the generated code. */
public class NameGenerator {
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
        StringBuilder cleanedName = new StringBuilder(initialName.length() + 1 + 2 + 8);
        boolean needsUnderscore = false;
        for (int index = 0; index < initialName.length(); index++) {
            char c = initialName.charAt(index);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                if (needsUnderscore) {
                    cleanedName.append('_');
                    needsUnderscore = false;
                }
                cleanedName.append(c);
            } else if (c >= '0' && c <= '9') {
                if (cleanedName.isEmpty()) {
                    cleanedName.append(DEFAULT_CHAR);
                }
                cleanedName.append(c);
                needsUnderscore = false;
            } else {
                needsUnderscore = true;
            }
        }

        String lowerCleanedName = cleanedName.toString().toLowerCase(Locale.US);
        int maxUsedNumber = maxSuffixes.getOrDefault(lowerCleanedName, -1);
        if (maxUsedNumber < 0) {
            // First use of a name without numeric suffix -> use as-is.
            //
            // Store it as 0 suffix, next use will get "__1" appended.
            maxSuffixes.put(lowerCleanedName, 0);
            return cleanedName.toString();

        } else {
            // Identifier already used, append a new suffix.
            maxUsedNumber++;
            maxSuffixes.put(lowerCleanedName, maxUsedNumber);

            cleanedName.append("__");
            cleanedName.append(maxUsedNumber);
            String newName = cleanedName.toString();
            if (initialIsCifName && warnOnRename) {
                warnOutput.warn("Renaming \"%s\" to \"%s\".", initialName, newName);
            }
            return newName;
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
