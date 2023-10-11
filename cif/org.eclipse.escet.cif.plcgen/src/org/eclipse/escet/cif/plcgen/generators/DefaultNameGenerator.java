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
                } else if (needsUnderscore) {
                    cleanedName.append('_');
                }
                cleanedName.append(c);
                needsUnderscore = false;
            } else {
                needsUnderscore = !cleanedName.isEmpty();
            }
        }
        if (cleanedName.isEmpty()) {
            cleanedName.append(DEFAULT_CHAR);
        }

        // Make the name unique.
        String lowerCleanedName = cleanedName.toString().toLowerCase(Locale.US);
        int maxUsedNumber = globalSuffixes.getOrDefault(lowerCleanedName, -1);
        if (localSuffixes != null) {
            maxUsedNumber = Math.max(maxUsedNumber, localSuffixes.getOrDefault(lowerCleanedName, -1));
        }

        if (maxUsedNumber < 0) {
            // First use of a name without numeric suffix -> use as-is.
            //
            // Store it as 0 suffix, next use will get "__1" appended.
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

            cleanedName.append("__");
            cleanedName.append(maxUsedNumber);
            String newName = cleanedName.toString();
            if (initialIsCifName && warnOnRename) {
                warnOutput.line("Renaming \"%s\" to \"%s\".", initialName, newName);
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
