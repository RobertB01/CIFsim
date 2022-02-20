//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.options.processing;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;

/** Perform pre-processing on option values that are a names match specification. */
public class PatternMatchingOptionProcessing {
    /** The regular expression pattern for checking the pattern elements of the option. */
    private static final Pattern FILTER_PATTERN = Pattern
            .compile("[a-zA-Z0-9_*][a-zA-Z0-9_*]*(\\.[a-zA-Z0-9_*][a-zA-Z0-9_*]*)*");

    /** Constructor of the {@link PatternMatchingOptionProcessing} class. */
    private PatternMatchingOptionProcessing() {
        // Static class.
    }

    /** Class containing a pattern to match against. Also, keeps track of tried matches. */
    public static class OptionPattern {
        /** If set, add names that match, else remove them. */
        public final boolean addMatch;

        /** Original pattern text as entered by the user. */
        public final String originalText;

        /**
         * Compiled pattern to match against. Is {@code null} for literal names, use {@link #originalText} for matching.
         */
        public final Pattern pattern;

        /** Whether the pattern ever had a successful match. */
        public boolean wasMatched;

        /** Whether a match on the pattern ever caused a change in the state of the matched text. */
        public boolean causedChange;

        /**
         * Constructor of the {@link OptionPattern} class.
         *
         * @param addMatch If set, a match with the pattern will cause the matched object to be added to the result,
         *     else a match will cause removal (pending further matches in other patterns).
         * @param originalText Original text of the pattern, for reporting purposes.
         * @param patternText Pattern text to be used in regular expression matching. Is {@code null} for special names.
         */
        public OptionPattern(boolean addMatch, String originalText, String patternText) {
            this.addMatch = addMatch;
            this.originalText = originalText;
            this.pattern = (patternText == null) ? null : Pattern.compile(patternText);
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
         * @param literalMatch Whether a literal match has to be made (that is, no wildcards).
         * @return Whether the given name matches against the pattern. A match is also recorded in {@link #wasMatched}
         *     and {@link #causedChange} is updated to record effectiveness of the pattern.
         */
        public boolean isMatch(String name, boolean makesChange, boolean literalMatch) {
            boolean match;
            if (pattern == null) {
                match = originalText.equals(name);
            } else {
                match = !literalMatch && pattern.matcher(name).matches();
            }

            if (match) {
                wasMatched = true;
                causedChange |= makesChange;
            }
            return match;
        }
    }

    /** Class to compare normal and special names against the option value. */
    public static class OptionMatcher {
        /** Processed option value. */
        public final List<OptionPattern> patterns;

        /** If set, the names are initially assumed to be added, else they are initially assumed to be dropped. */
        public final boolean initiallyAdded;

        /**
         * Constructor of the {@link OptionMatcher} class.
         *
         * @param patterns Processed option value.
         * @param initiallyAdded If set, the names are initially assumed to be added.
         */
        public OptionMatcher(List<OptionPattern> patterns, boolean initiallyAdded) {
            this.patterns = patterns;
            this.initiallyAdded = initiallyAdded;
        }

        /**
         * Decide whether a given normal name should be added to the matching result.
         *
         * @param name Normal name to match with.
         * @param literalMatch Whether the match has to be made with a literal (no wildcards).
         * @return Whether the name should be added to the result.
         */
        public boolean matchName(String name, boolean literalMatch) {
            boolean added = initiallyAdded;
            for (OptionPattern pat: patterns) {
                boolean matchCausesChange = (added != pat.addMatch);
                if (pat.isMatch(name, matchCausesChange, literalMatch)) {
                    added = pat.addMatch;
                }
            }
            return added;
        }

        /**
         * Filter a list of normal names against the option.
         *
         * @param names Names to check.
         * @param literalMatch Whether the match has to be made with a literal (no wildcards).
         * @return The collection of names that matches positively.
         */
        public List<String> filterNames(List<String> names, boolean literalMatch) {
            List<String> result = list();
            for (String name: names) {
                if (matchName(name, literalMatch)) {
                    result.add(name);
                }
            }
            return result;
        }

        /**
         * Filter a set of normal names against the option.
         *
         * @param names Names to check.
         * @param literalMatch Whether the match has to be made with a literal (no wildcards).
         * @return The collection of names that matches positively.
         */
        public Set<String> filterNormalNames(Set<String> names, boolean literalMatch) {
            Set<String> result = set();
            for (String name: names) {
                if (matchName(name, literalMatch)) {
                    result.add(name);
                }
            }
            return result;
        }

        /** Report about usefulness of each pattern of the option after the performed matching process. */
        public void reportMatching() {
            for (OptionPattern pat: patterns) {
                if (!pat.wasMatched) {
                    String msg = fmt("Option pattern \"%s\" never matched in the specification.", pat.originalText);
                    warn(msg);
                } else if (!pat.causedChange) {
                    String reason;
                    if (pat.addMatch) {
                        reason = "Matches were already added earlier.";
                    } else {
                        reason = "Matches were never added or already removed earlier.";
                    }
                    String msg = fmt("Option pattern \"%s\" never made a change to the selection. %s", pat.originalText,
                            reason);
                    warn(msg);
                }
            }
        }
    }

    /**
     * Construct a matcher object for matching names against a list of patterns.
     *
     * @param optValue Value of the option.
     * @param allowWildcard Whether or not to allow specification of the {@code "*"} character denoting zero or more
     *     arbitrary characters in names. If not set, the option reduces to selection from a list of names.
     * @param allowRemove Whether to allow adding and removing of names with a {@code "+"} or {@code "-"} prefix in
     *     front of the actual pattern. If not set, only adding is possible without recognizing the prefixes.
     * @param initiallyAdded Whether names being matched are assumed to be initially added.
     * @return Matcher object for matching names against patterns.
     */
    public static OptionMatcher makeMatcher(String optValue, boolean allowWildcard, boolean allowRemove,
            boolean initiallyAdded)
    {
        if (optValue == null) {
            optValue = "";
        }

        List<OptionPattern> optPatterns = list();
        String[] valuePatterns = StringUtils.split(optValue, ",");
        for (int i = 0; i < valuePatterns.length; i++) {
            String originalText = valuePatterns[i].trim();

            boolean hasAdd = allowRemove && originalText.charAt(0) == '+';
            boolean hasRemove = allowRemove && originalText.charAt(0) == '-';
            String patternText = (hasAdd || hasRemove) ? originalText.substring(1) : originalText;
            boolean addMatch = !hasRemove;

            if (patternText.isEmpty()) {
                continue; // Silently drop empty names.
            }

            boolean hasWildcard = allowWildcard && patternText.contains("*");
            String regex;
            if (hasWildcard) {
                // For regular expressions, be more careful in what is acceptable.
                if (!FILTER_PATTERN.matcher(patternText).matches()) {
                    String msg = fmt("Value actions pattern \"%s\" has invalid syntax.", originalText);
                    throw new InvalidOptionException(msg);
                }

                regex = "^" + patternText.replace(".", "\\.").replace("*", ".*") + "$";
            } else {
                regex = null;
            }

            optPatterns.add(new OptionPattern(addMatch, originalText, regex));
        }

        return new OptionMatcher(optPatterns, initiallyAdded);
    }
}
