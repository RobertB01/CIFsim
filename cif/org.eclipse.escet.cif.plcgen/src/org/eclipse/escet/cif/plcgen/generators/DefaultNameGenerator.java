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

import static org.eclipse.escet.common.java.Lists.list;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.generators.names.NameScope;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.output.WarnOutput;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * Generator for obtaining clash-free names in the generated code.
 *
 * <p>
 * The name generator assumes two levels of scopes, one global scope and zero or more local scopes. The names in the
 * global scope are available in all scopes. The names in a local scope are only available for that scope. Different
 * local scopes however are allowed to use equal names, except they then represent different PLC elements. Names in the
 * global scope are disjoint from names in every local scope.
 * </p>
 */
public class DefaultNameGenerator implements NameGenerator {
    /** Default single lower-case letter name to use if no prefix can be constructed. */
    static final char DEFAULT_CHAR = 'x';

    /** The global name scope. */
    private final NameScope globalScope = new NameScope();

    /** The union of all local scopes. Is disjunct with the {@link #globalScope global scope}. */
    private final NameScope unionLocalScopes = new NameScope();

    /** If the value holds the user should be warned about changing the name, else the user should not be warned. */
    private final boolean warnOnRename;

    /** Callback to send warnings to the user. */
    private final WarnOutput warnOutput;

    /**
     * Constructor of the {@link DefaultNameGenerator} class.
     *
     * @param settings Configuration to use.
     */
    public DefaultNameGenerator(PlcGenSettings settings) {
        warnOnRename = settings.warnOnRename;
        warnOutput = settings.warnOutput;

        disallowReservedPlcNames();
    }

    /**
     * Declare the names in the provided collection as unavailable in the PLC code.
     *
     * @param names Names to declare as unavailable in the PLC code.
     */
    private void addDisallowedNames(Collection<String> names) {
        for (String name: names) {
            globalScope.addName(name);
        }
    }

    /**
     * Declare the names in the provided array as unavailable in the PLC code.
     *
     * @param names Names to declare as unavailable in the PLC code.
     */
    private void addDisallowedNames(String[] names) {
        for (String name: names) {
            globalScope.addName(name);
        }
    }

    @Override
    public String generateGlobalName(PositionObject posObject) {
        return generateGlobalNames(Set.of(""), posObject);
    }

    @Override
    public String generateGlobalNames(Set<String> prefixes, PositionObject posObject) {
        Assert.check(CifTextUtils.hasName(posObject), "Missing name for \"" + String.valueOf(posObject) + "\".");
        return generateGlobalNames(prefixes, CifTextUtils.getAbsName(posObject, false), true);
    }

    @Override
    public String generateGlobalName(String initialName, boolean isCifName) {
        return generateGlobalNames(Set.of(""), initialName, isCifName);
    }

    @Override
    public String generateGlobalNames(Set<String> prefixes, String initialName, boolean isCifName) {
        // - The new global name must not already exist in the global scope and it must be added to the global scope
        //   afterwards.
        // - The new global name must not already exist in any local scope, and thus not in the union of local
        //   scopes.
        return generateNames(prefixes, initialName, isCifName, globalScope, unionLocalScopes, null);
    }

    @Override
    public String generateLocalName(String initialName, NameScope localScope) {
        return generateLocalNames(Set.of(""), initialName, localScope);
    }

    @Override
    public String generateLocalNames(Set<String> prefixes, String initialName, NameScope localScope) {
        // - The new local name must not already exist in the local scope and it must be added to the local scope
        //   afterwards.
        // - The new local name must not already exist in the global scope.
        // - The new local name is added to a local scope, and thus it must be added to the union of local scopes as
        //   well.
        return generateNames(prefixes, initialName, false, localScope, globalScope, unionLocalScopes);
    }

    /**
     * Construct a good base name to use.
     *
     * @param prefixes The set of prefixes in front of the created name that must be available.
     * @param initialName The initial name to use as starting point for a good name.
     * @param isCifName Whether the good name represents a CIF element recognizable by the user.
     * @param usageScope The scope that will use the returned good name.
     * @param testScope A scope that should not have the good name already, but is not updated.
     * @param updateScope A scope to update as well for the created good name. Can be {@code null}.
     * @return A good name to use.
     */
    private String generateNames(Set<String> prefixes, String initialName, boolean isCifName,
            NameScope usageScope, NameScope testScope, NameScope updateScope)
    {
        // Cleanup the name.
        String cleanedName = cleanName(initialName);

        // Make the name unique and create lower case versions of the prefixes.
        String lowerCleanedName = cleanedName.toString().toLowerCase(Locale.US);
        List<String> lowerPrefixes = prefixes.stream().map(s -> s.toLowerCase(Locale.US)).toList();

        // Find a number that causes no clashes with existing names in the usage and test scopes.
        int number = 0;
        while (true) {
            // Construct a candidate name, and check for clashes with other names in the scopes, for all prefixes.
            String candidateLowerCleaned = (number == 0) ? lowerCleanedName : (lowerCleanedName + "_" + number);
            boolean isUsed = lowerPrefixes.stream().map(prefix -> prefix + candidateLowerCleaned)
                    .anyMatch(testName -> usageScope.isNameUsed(testName) || testScope.isNameUsed(testName));

            if (isUsed) {
                // At least one name clash exists. Try again with the next number.
                number++;
                continue;
            }

            // Number is good. Add the new names to the scopes that must be updated, and break out of the loop.
            for (String prefix: lowerPrefixes) {
                String addedLowerCleanedName = prefix + candidateLowerCleaned;
                usageScope.addName(addedLowerCleanedName);
                if (updateScope != null) {
                    updateScope.addName(addedLowerCleanedName);
                }
            }
            break;
        }

        // Construct the good name.
        String goodName = (number == 0) ? cleanedName : (cleanedName + "_" + number);

        // Print a rename warning if applicable.
        if (isCifName && warnOnRename && number > 0) {
            warnOutput.line("Renaming \"%s\" to \"%s\".", initialName, goodName);
        }

        // Return the result.
        return goodName;
    }

    /**
     * Cleanup the name.
     *
     * <p>
     * A name consists of alternating good and bad parts, where a good part is a sequence of letters and digits, and a
     * bad part is a sequence of non-letter and non-digit characters. Each good part is forced to start with a letter.
     * </p>
     * <p>
     * The good parts are copied, and get separated with an underscore character.
     * </p>
     *
     * @param text Input text to clean up.
     * @return The cleaned-up name.
     */
    private String cleanName(String text) {
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

            // Copy the good characters, update the read index, and bail out if the end has been reached.
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
        return sb.toString();
    }

    /**
     * Find a sequence of good characters (letters or digits) in the {@code data} array at {@code index}.
     *
     * @param data Characters to explore.
     * @param index Index to start the search.
     * @return Number of found good characters starting from {@code data[index]}.
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
     * Find a sequence of bad characters (anything else but letters or digits) in the {@code data} array at
     * {@code index}.
     *
     * @param data Characters to explore.
     * @param index Index to start the search.
     * @return Number of found bad characters starting from {@code data[index]}.
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

    /** Add reserved names in the PLC as disallowed in the name generator. */
    private void disallowReservedPlcNames() {
        // Keywords of the language, note that "en" and "eno" special parameter names have been left out.
        String[] languageKeywords = new String[] {"action", "array", "at", "by", "case", "configuration", "constant",
                "do", "else", "elsif", "end_action", "end_case", "end_configuration", "end_for", "end_function",
                "end_function_block", "end_if", "end_program", "end_repeat", "end_resource", "end_retain", "end_step",
                "end_struct", "end_transition", "end_type", "end_var", "end_while", "exit", "false", "f_edge", "for",
                "from", "function", "function_block", "if", "initial_step", "of", "on", "program", "read_only",
                "read_write", "r_edge", "repeat", "resource", "retain", "return", "step", "struct", "task", "then",
                "to", "transition", "true", "type", "until", "var", "var_access", "var_config", "var_external",
                "var_global", "var_in_out", "var_input", "var_output", "var_temp", "while", "with"};
        addDisallowedNames(languageKeywords);

        String[] functionNames = new String[] {"abs", "acos", "add", "and", "asin", "atan", "cos", "div", "eq", "exp",
                "expt", "ge", "gt", "le", "ln", "log", "lt", "max", "min", "mod", "mul", "ne", "not", "or", "sel",
                "sin", "sqrt", "sub", "tan", "xor"};
        addDisallowedNames(functionNames);

        String[] functionBlockNames = new String[] { //
                "rs", "sr", // Set/reset.
                "ton", "tof", "tp", // Timers.
                "iec_timer", "timer", // Timers S7.
                "dummyVar1", "dummyVar2", "dummyVar3", "dummyVar4", "dummyVar5", // Dummy variables S7.
                "f_trig", "r_trig", // Edge detection.
                "ctu", "ctu_dint", "ctu_lint", "ctu_udint", "ctu_ulint", // Up counters.
                "ctd", "ctd_dint", "ctd_lint", "ctd_udint", "ctd_ulint", // Down counters.
                "ctud", "ctud_dint", "ctud_lint", "ctud_udint", "ctud_ulint", // Up-down counters.
        };
        addDisallowedNames(functionBlockNames);

        String[] typeKeywords = new String[] {"bool", "sint", "int", "dint", "lint", "usint", "uint", "ulint", "udint",
                "real", "lreal", "time", "date", "time_of_day", "tod", "date_and_time", "dt", "string", "byte", "word",
                "dword", "lword", "wstring"};
        addDisallowedNames(typeKeywords);

        String[] genericTypeKeywords = new String[] {"any", "and_derived", "any_elementary", "any_magnitude", "any_num",
                "any_real", "any_int", "any_bit", "any_string", "any_date"};
        addDisallowedNames(genericTypeKeywords);

        // Casts (X_TO_Y functions).
        List<String> castNames = list();
        for (int i = 0; i < typeKeywords.length; i++) {
            for (int j = 0; j < typeKeywords.length; j++) {
                if (i == j) {
                    continue;
                }
                castNames.add(typeKeywords[i] + "_to_" + typeKeywords[j]);
            }
        }
        addDisallowedNames(castNames);
    }
}
