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

package org.eclipse.escet.cif.cif2mcrl2.options;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Option to specify the variables that need to have a 'value' action. */
public class GenerateValueActionsOption extends StringOption {
    /** The regular expression pattern for checking the pattern elements of the option. */
    private static final Pattern FILTER_PATTERN = Pattern
            .compile("[-+]?[a-zA-Z_*][a-zA-Z0-9_*]*(\\.[a-zA-Z_*][a-zA-Z0-9_*]*)*");

    /** Name of the option. */
    private static final String NAME = "Generate 'value' actions";

    /** Description in the option dialog. */
    private static final String OPT_DIALOG_DESCR = "Option to specify which variables should get a 'value' action in"
            + "the generated mCRL2 code. Specify a comma-separated list of variable names. The \"*\" character can be "
            + "used as wildcard, and indicates zero or more characters. Prefixing a name with a \"+\" adds the "
            + "variable(s) matching the name, while a \"-\" prefix removes the variable(s) matching the name. If "
            + "neither a \"+\" nor a \"-\" prefix is given, \"+\" (adding) is assumed.";

    /** Default value of the option. */
    private static final String DEFAULT_VALUE = "+*";

    /** Description of the option. */
    private static final String DESCRIPTION = fmt("%s [DEFAULT=%s]", OPT_DIALOG_DESCR, DEFAULT_VALUE);

    /** Whether to return {@code null} as option value if it was left empty. */
    private static final boolean EMPTY_AS_NULL = true;

    /** Short option name. */
    private static final Character CMD_SHORT = 'r';

    /** Long option name. */
    private static final String CMD_LONG = "read-values";

    /** Name of the option value. */
    private static final String CMD_VALUE = "PATTERNS";

    /** Whether to display the option in the option dialog. */
    private static final boolean SHOW_IN_DIALOG = true;

    /** Text next to the label in the option dialog. */
    private static final String OPT_DIALOG_LABEL_TEXT = NAME + ":";

    /** Constructor of the {@link GenerateValueActionsOption} class. */
    public GenerateValueActionsOption() {
        super(NAME, DESCRIPTION, CMD_SHORT, CMD_LONG, CMD_VALUE, DEFAULT_VALUE, EMPTY_AS_NULL, SHOW_IN_DIALOG,
                OPT_DIALOG_DESCR, OPT_DIALOG_LABEL_TEXT);
    }

    /**
     * Get the patterns specified in the option.
     *
     * @return The patterns of the option.
     */
    public static List<OptionPattern> getValueActionsOptionPatterns() {
        String optValue = Options.get(GenerateValueActionsOption.class);
        if (optValue == null) {
            return list(new OptionPattern(true, "+*", "^.*$"));
        }

        // Split on ",", check each element for validity, and build a list to return.
        List<OptionPattern> resultPatterns = list();
        String[] valuePatterns = StringUtils.split(optValue, ",");
        for (int i = 0; i < valuePatterns.length; i++) {
            String s = valuePatterns[i].trim();
            if (!FILTER_PATTERN.matcher(s).matches()) {
                String msg = fmt("Value actions pattern \"%s\" has invalid syntax.", s);
                throw new InvalidOptionException(msg);
            }

            // Handle the '+' or '-' prefix.
            boolean addMatch;
            String regex;
            if (s.charAt(0) == '+') {
                addMatch = true;
                regex = s.substring(1);
            } else if (s.charAt(0) == '-') {
                addMatch = false;
                regex = s.substring(1);
            } else {
                addMatch = true;
                regex = s;
            }

            // Convert to a normal regular expression pattern and add it to the reslut list.
            regex = "^" + regex.replace(".", "\\.").replace("*", ".*") + "$";
            resultPatterns.add(new OptionPattern(addMatch, s, regex));
        }
        return resultPatterns;
    }

    /** Class containing a pattern. */
    public static class OptionPattern {
        /** If set, add names that match, else remove them. */
        public final boolean addMatch;

        /** Original pattern text as entered by the user. */
        public final String originalText;

        /** Compiled pattern to match against. */
        public final Pattern pattern;

        /** Whether the pattern ever had a successful match from {@link #isMatch}. */
        public boolean wasMatched;

        /** Whether a match on the pattern ever caused a change in the state of the matched text. */
        public boolean causedChange;

        /**
         * Constructor of the {@link OptionPattern} class.
         *
         * @param addMatch If set, a match with the pattern will cause the matched object to be added to the result,
         *     else a match will cause removal (pending further matches in other patterns).
         * @param originalText Original text of the pattern, for reporting purposes.
         * @param patternText Pattern text to be used in regular expression matching.
         */
        public OptionPattern(boolean addMatch, String originalText, String patternText) {
            this.addMatch = addMatch;
            this.originalText = originalText;
            this.pattern = Pattern.compile(patternText);
            reset();
        }

        /** Reset the {@link #wasMatched} and {@link #causedChange} flags. */
        public void reset() {
            wasMatched = false;
            causedChange = false;
        }

        /**
         * Does the given name match against the pattern?
         *
         * @param name Text to match.
         * @param makesChange Flag whether a match will cause a change in state for the matched name.
         * @return Whether the given name matches against the pattern. A match is also recorded in {@link #wasMatched}
         *     and {@link #causedChange} is updated to record effectiveness of the pattern.
         */
        public boolean isMatch(String name, boolean makesChange) {
            boolean match = pattern.matcher(name).matches();
            if (match) {
                wasMatched = true;
                causedChange |= makesChange;
            }
            return match;
        }
    }

    /**
     * Match a list of names against the patterns, and return all names that were either never added by the patterns, or
     * added and removed again.
     *
     * <p>
     * Function also warns the user for entries in the patterns that did not give any match, as that may indicate an
     * error in the option value.
     * </p>
     *
     * @param names Names to match against the patterns.
     * @return Names that were added and not removed by the patterns.
     */
    public static Set<String> matchNames(Set<String> names) {
        List<OptionPattern> patterns = getValueActionsOptionPatterns();

        // Matching empty lists is silly. Also it causes false positive warnings
        // on failure to match names in the patterns.
        if (names.isEmpty()) {
            return names;
        }

        // Walk over the provided names, and add names that should be added into
        // the result. As a side effect, the patterns record ever matching a name,
        // or ever making an effective change.
        Set<String> matchedNames = set();
        for (String name: names) {
            boolean added = false;
            for (OptionPattern pat: patterns) {
                boolean matchCausesChange = (added != pat.addMatch);
                if (pat.isMatch(name, matchCausesChange)) {
                    added = pat.addMatch;
                }
            }

            if (added) {
                matchedNames.add(name);
            }
        }

        // Examine the patterns, reporting patterns to the user that were not useful.
        for (OptionPattern pat: patterns) {
            if (!pat.wasMatched) {
                String msg = fmt(
                        "Value actions option pattern \"%s\" does not match any variable in the specification.",
                        pat.originalText);
                warn(msg);
            } else if (!pat.causedChange) {
                String reason;
                if (pat.addMatch) {
                    reason = "Matched variables were already added earlier.";
                } else {
                    reason = "Matched variables were never added or already removed earlier.";
                }
                String msg = fmt("Value actions option pattern \"%s\" does not "
                        + "make any change to the selected variables. %s", pat.originalText, reason);
                warn(msg);
            }
        }

        return matchedNames;
    }
}
