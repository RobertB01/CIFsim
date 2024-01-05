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

package org.eclipse.escet.setext.generator.scanner;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.list2set;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.setext.generator.parser.LALR1ParserGenerator;
import org.eclipse.escet.setext.parser.ast.regex.RegEx;
import org.eclipse.escet.setext.parser.ast.regex.RegExAlts;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.eclipse.escet.setext.parser.ast.regex.RegExCharClass;
import org.eclipse.escet.setext.parser.ast.regex.RegExCharSeq;
import org.eclipse.escet.setext.parser.ast.regex.RegExDot;
import org.eclipse.escet.setext.parser.ast.regex.RegExOpt;
import org.eclipse.escet.setext.parser.ast.regex.RegExPlus;
import org.eclipse.escet.setext.parser.ast.regex.RegExSeq;
import org.eclipse.escet.setext.parser.ast.regex.RegExShortcut;
import org.eclipse.escet.setext.parser.ast.regex.RegExStar;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;

/**
 * Regular expression to DFA converter.
 *
 * <p>
 * Based on the book "Compilers: Principles, Techniques, & Tools" (Second Edition), by Alfred V. Aho et al., 2007.
 * </p>
 */
public class RegExToDfa {
    /**
     * Converts one or more terminals to a {@link Automaton DFA}.
     *
     * <p>
     * Implements Algorithm 3.36 of the book, extended to allow multiple regular expressions (and thus multiple end
     * markers), report unused (never accepted) terminals, etc. Also {@link #minimizeDfa minimizes} the resulting
     * automaton.
     * </p>
     *
     * @param terminals The terminals to convert.
     * @return The constructed DFA.
     */
    public static Automaton terminalsToDfa(List<Terminal> terminals) {
        // Paranoia checks.
        Assert.check(!terminals.isEmpty());

        // Create dummy position information object.
        TextPosition dummyPos = TextPosition.createDummy("/dummy.file");

        // Construct end marker per terminal, and add them at the end of the
        // regular expression.
        Map<RegExChar, Terminal> markerMap = map();
        List<RegEx> regExAlts = list();
        int markerNr = -1;
        for (Terminal terminal: terminals) {
            // Construct marker.
            markerNr--;
            RegExChar marker = new RegExChar(markerNr, dummyPos);

            // Store marker/terminal pair in mapping.
            markerMap.put(marker, terminal);

            // Simplify regular expression, and add marker. We simplify first,
            // as we don't want to simplify (and thus clone) the marker.
            RegEx regEx = simplify(terminal.regEx);
            regExAlts.add(new RegExSeq(list(regEx, marker)));
        }

        // Combine regular expressions.
        RegEx combiRegEx = makeAlts(regExAlts);

        // Convert regular expression to DFA.
        Automaton dfa = regExToDfa(combiRegEx);

        // Decide on acceptance of terminals, based on end markers.
        Set<Terminal> unusedTerminals = list2set(terminals);
        decideAcceptance(dfa, markerMap, unusedTerminals);

        // Report unused (never accepted) terminals.
        if (!unusedTerminals.isEmpty()) {
            List<String> warnings = list();
            for (Terminal terminal: unusedTerminals) {
                String txt = "\"" + terminal.regEx.toString() + "\"";
                if (terminal.name != null) {
                    txt = terminal.name + "=" + txt;
                }
                txt = fmt("Terminal %s (priority=%d) is not used (it is not accepted in any of the states of the "
                        + "scanner DFA).", txt, terminal.priority);
                warnings.add(txt);
            }
            Collections.sort(warnings, Strings.SORTER);
            for (String warning: warnings) {
                warn(warning);
            }
        }

        // Minimize DFA.
        dfa = minimizeDfa(dfa);
        return dfa;
    }

    /**
     * Constructs a simplified regular expression from alternatives.
     *
     * @param alts Non-empty list of alternatives.
     * @return The simplified regular expression for the alternatives.
     * @see #simplify
     */
    private static RegEx makeAlts(List<RegEx> alts) {
        // Handle single alternative.
        Assert.check(!alts.isEmpty());
        if (alts.size() == 1) {
            return first(alts);
        }

        // Handle multiple alternatives. Use only binary alternative nodes
        // (left-recursive).
        RegEx rslt = first(alts);
        for (int i = 1; i < alts.size(); i++) {
            RegEx alt = alts.get(i);
            rslt = new RegExAlts(list(rslt, alt));
        }
        return rslt;
    }

    /**
     * Converts a regular expression to a {@link Automaton DFA}. The caller is responsible for making sure that the
     * appropriate custom markers are present in the regular expression, for accepting states.
     *
     * <p>
     * Implements the algorithm of Figure 3.62 of the book.
     * </p>
     *
     * <p>
     * Implementation very similar to the {@link LALR1ParserGenerator#constructLR0Automaton} method.
     * </p>
     *
     * @param regEx The regular expression to convert.
     * @return The constructed DFA.
     */
    public static Automaton regExToDfa(RegEx regEx) {
        // Pre-calculate 'followpos' on entire regular expression.
        RegExToDfa converter = new RegExToDfa();
        converter.calcFollowpos(regEx);

        // Get the alphabet.
        Set<Integer> alphabet = regEx.getCodePoints();

        // Initialize DFA.
        Set<RegExChar> initialFirst = firstpos(regEx);
        AutomatonState initialState = new AutomatonState(initialFirst);
        Automaton automaton = new Automaton(initialState);

        // Construct DFA.
        Deque<AutomatonState> todoStates = new LinkedList<>();
        todoStates.push(initialState);
        while (!todoStates.isEmpty()) {
            // Get next 'unmarked' state (state to process).
            AutomatonState state = todoStates.pop();

            // Process the entire alphabet.
            for (Integer codePoint: alphabet) {
                // Calculate target positions.
                Set<RegExChar> targetPositions = set();
                for (RegExChar position: state.positions) {
                    if (position.character != codePoint) {
                        continue;
                    }
                    Set<RegExChar> followSet = converter.followpos(position);
                    targetPositions.addAll(followSet);
                }

                // If no target positions, then we don't have an edge.
                if (targetPositions.isEmpty()) {
                    continue;
                }

                // We have an edge. It should not be for a custom marker.
                Assert.check(codePoint >= -1);
                AutomatonState target = new AutomatonState(targetPositions);

                // Add target state to the automaton. We get back the
                // representative for that state.
                AutomatonState representative = automaton.addState(target);

                // If target is a new state, add it as 'unmarked' (todo) state.
                if (representative == target) {
                    todoStates.push(target);
                }

                // Add edge.
                state.addEdge(codePoint, representative);
            }
        }

        // Return DFA (includes the end markers).
        return automaton;
    }

    /**
     * Decide on the acceptance of states, based on the markers of the state. Also determines the unused (never
     * accepted) terminals.
     *
     * <p>
     * Implements a part of Algorithm 3.36 of the book.
     * </p>
     *
     * @param dfa The DFA. It is modified in-place.
     * @param markerMap Mapping from markers to the corresponding terminal.
     * @param unusedTerminals The set of all terminals. It is modified in-place by this method, by removing all
     *     terminals that are accepted at least once. What remains are the unused (never accepted) terminals.
     */
    private static void decideAcceptance(Automaton dfa, Map<RegExChar, Terminal> markerMap,
            Set<Terminal> unusedTerminals)
    {
        for (AutomatonState state: dfa.states.keySet()) {
            // Get terminals for the end markers of this state.
            List<Terminal> terminals = list();
            for (RegExChar c: state.positions) {
                if (c.isCustomMarker()) {
                    Terminal terminal = markerMap.get(c);
                    Assert.notNull(terminal);
                    terminals.add(terminal);
                }
            }

            // If we have no terminals, there is nothing to decide.
            if (terminals.isEmpty()) {
                continue;
            }

            // Sort terminals based on priority.
            Collections.sort(terminals, Terminal.PRIORITY_COMPARER);
            int highestPrio = first(terminals).priority;
            int highestCnt = 0;
            while (highestCnt < terminals.size() && terminals.get(highestCnt).priority == highestPrio) {
                highestCnt++;
            }

            // If we have only one terminal with highest priority, use it.
            if (highestCnt == 1) {
                Terminal acceptedTerminal = first(terminals);
                state.accept = acceptedTerminal;
                unusedTerminals.remove(acceptedTerminal);
                continue;
            }

            // We have multiple terminals with the same priority. We can't make
            // a decision, so we inform the user.
            List<String> termTxts = list();
            for (int i = 0; i < highestCnt; i++) {
                Terminal term = terminals.get(i);
                String termTxt = "\"" + term.regEx.toString() + "\"";
                if (term.name != null) {
                    termTxt = term.name + "=" + termTxt;
                }
                termTxts.add(termTxt);
            }
            Collections.sort(termTxts, Strings.SORTER);
            String msg = fmt("Two or more terminals have the same priority and they are overlapping (their languages "
                    + "have at least one string in common): %s.", String.join(", ", termTxts));
            throw new InvalidInputException(msg);
        }
    }

    /**
     * Simplifies the regular expression by:
     * <ul>
     * <li>Replacing character classes by alternatives of the individual characters.</li>
     * <li>Replacing dot special characters by alternatives of the individual characters.</li>
     * <li>Replacing shortcuts by their definitions.</li>
     * <li>Replacing '{@code a+}' by '{@code a . a*}' for all {@code a}.</li>
     * <li>Replacing n-ary alternatives by binary alternatives.</li>
     * <li>Replacing n-ary sequences by binary sequences.</li>
     * </ul>
     *
     * <p>
     * This ensures that we have only single characters (and end-of-file) in the leaves. It also ensures we only have
     * the primitives as described in the book, and {@link RegExOpt}.
     * </p>
     *
     * <p>
     * An entirely new regular expression is created, to make sure that unfolded shortcuts don't share the same
     * {@link RegExChar} instances.
     * </p>
     *
     * @param re The regular expression to simplify.
     * @return The simplified regular expression.
     */
    public static RegEx simplify(RegEx re) {
        if (re instanceof RegExAlts) {
            // Simplify children.
            List<RegEx> alts = list();
            for (RegEx alt: ((RegExAlts)re).alts) {
                RegEx simpleAlt = simplify(alt);
                alts.add(simpleAlt);
            }

            // Convert n-ary children into binary children (left-recursive).
            RegEx rslt = first(alts);
            for (int i = 1; i < alts.size(); i++) {
                RegEx alt = alts.get(i);
                rslt = new RegExAlts(list(rslt, alt));
            }
            return rslt;
        } else if (re instanceof RegExCharClass) {
            // Unfold to alternative over the characters.
            RegExCharClass cls = (RegExCharClass)re;

            Set<Integer> chars = cls.getCodePoints();
            List<Integer> sortedChars = sortedgeneric(chars);

            List<RegEx> alts = list();
            for (int c: sortedChars) {
                alts.add(new RegExChar(c, cls.position));
            }
            Assert.check(!alts.isEmpty());
            RegEx rslt = (alts.size() == 1) ? alts.get(0) : new RegExAlts(alts);
            rslt = simplify(rslt);
            return rslt;
        } else if (re instanceof RegExChar) {
            // Copy character.
            RegExChar c = (RegExChar)re;
            return new RegExChar(c.character, c.position);
        } else if (re instanceof RegExCharSeq) {
            // RegExCharClass should handle this...
            throw new RuntimeException("Unexpected RegExCharSeq");
        } else if (re instanceof RegExDot) {
            // Simplify '.' by alternative over all ASCII codes except newline.
            List<RegEx> alts = list();
            for (int c = 0; c <= 127; c++) {
                if (c == '\n') {
                    continue;
                }
                alts.add(new RegExChar(c, re.position));
            }
            RegEx rslt = new RegExAlts(alts);
            rslt = simplify(rslt);
            return rslt;
        } else if (re instanceof RegExOpt) {
            // Simplify child.
            RegExOpt opt = (RegExOpt)re;
            return new RegExOpt(simplify(opt.child), opt.position);
        } else if (re instanceof RegExPlus) {
            // Replace 'a+' by 'a . a*'.
            RegExPlus plus = (RegExPlus)re;
            RegEx child1 = simplify(plus.child);
            RegEx child2 = simplify(plus.child);
            List<RegEx> seq = list();
            seq.add(child1);
            seq.add(new RegExStar(child2, plus.position));
            return new RegExSeq(seq);
        } else if (re instanceof RegExSeq) {
            // Simplify children.
            List<RegEx> parts = list();
            for (RegEx part: ((RegExSeq)re).sequence) {
                RegEx simplePart = simplify(part);
                parts.add(simplePart);
            }

            // Convert n-ary children into binary children (left-recursive).
            RegEx rslt = first(parts);
            for (int i = 1; i < parts.size(); i++) {
                RegEx part = parts.get(i);
                rslt = new RegExSeq(list(rslt, part));
            }
            return rslt;
        } else if (re instanceof RegExShortcut) {
            // Unfold shortcut.
            RegExShortcut shortcut = (RegExShortcut)re;
            return simplify(shortcut.shortcut.regEx);
        } else if (re instanceof RegExStar) {
            // Simplify child.
            RegExStar star = (RegExStar)re;
            return new RegExStar(simplify(star.child), star.position);
        } else {
            throw new RuntimeException("Unknown regex: " + re);
        }
    }

    /**
     * Is the regular expression nullable? A regular expression is nullable if it has the empty string in its language.
     * In other words, if the regular expression can be 'made null' (if it accept the empty string), even though there
     * may be other strings it can represent as well.
     *
     * <p>
     * Implements the function described in Section 3.9.3 of the book, extended to allow {@link RegExOpt}.
     * </p>
     *
     * @param re The simplified regular expression.
     * @return {@code true} if the regular expression is nullable, {@code false} otherwise.
     * @see #simplify
     */
    public static boolean nullable(RegEx re) {
        if (re instanceof RegExAlts) {
            List<RegEx> alts = ((RegExAlts)re).alts;
            Assert.check(alts.size() == 2);
            return nullable(first(alts)) || nullable(last(alts));
        } else if (re instanceof RegExChar) {
            return false;
        } else if (re instanceof RegExOpt) {
            // nullable(a?)
            // = nullable(a | e)
            // = nullable(a) or nullable(e)
            // = nullable(a) or true
            // = true
            return true;
        } else if (re instanceof RegExSeq) {
            List<RegEx> seq = ((RegExSeq)re).sequence;
            Assert.check(seq.size() == 2);
            return nullable(first(seq)) && nullable(last(seq));
        } else if (re instanceof RegExStar) {
            return true;
        } else {
            throw new RuntimeException("Unknown/unexpected regex: " + re);
        }
    }

    /**
     * Returns the first positions subset for a regular expression. This is the set of positions (unique character
     * nodes) in the regular expression that correspond to the first symbol of at least one string in the language of
     * the regular expression.
     *
     * <p>
     * Implements the function described in Section 3.9.3 of the book, extended to allow {@link RegExOpt}.
     * </p>
     *
     * @param re The simplified regular expression.
     * @return The first positions subset of the regular expression.
     * @see #simplify
     */
    public static Set<RegExChar> firstpos(RegEx re) {
        if (re instanceof RegExAlts) {
            List<RegEx> alts = ((RegExAlts)re).alts;
            Assert.check(alts.size() == 2);
            return Sets.union(firstpos(first(alts)), firstpos(last(alts)));
        } else if (re instanceof RegExChar) {
            return set((RegExChar)re);
        } else if (re instanceof RegExOpt) {
            // firstpos(a?)
            // = firstpos(a | e)
            // = firstpos(a) \/ firstpos(e)
            // = firstpos(a) \/ {}
            // = firstpos(a)
            return firstpos(((RegExOpt)re).child);
        } else if (re instanceof RegExSeq) {
            List<RegEx> seq = ((RegExSeq)re).sequence;
            Assert.check(seq.size() == 2);
            RegEx first = first(seq);
            if (nullable(first)) {
                return Sets.union(firstpos(first(seq)), firstpos(last(seq)));
            } else {
                return firstpos(first(seq));
            }
        } else if (re instanceof RegExStar) {
            return firstpos(((RegExStar)re).child);
        } else {
            throw new RuntimeException("Unknown/unexpected regex: " + re);
        }
    }

    /**
     * Returns the last positions subset for a regular expression. This is the set of positions (unique character nodes)
     * in the regular expression that correspond to the last symbol of at least one string in the language of the
     * regular expression.
     *
     * <p>
     * Implements the function described in Section 3.9.3 of the book, extended to allow {@link RegExOpt}.
     * </p>
     *
     * @param re The simplified regular expression.
     * @return The last positions subset of the regular expression.
     * @see #simplify
     */
    public static Set<RegExChar> lastpos(RegEx re) {
        if (re instanceof RegExAlts) {
            List<RegEx> alts = ((RegExAlts)re).alts;
            Assert.check(alts.size() == 2);
            return Sets.union(lastpos(first(alts)), lastpos(last(alts)));
        } else if (re instanceof RegExChar) {
            return set((RegExChar)re);
        } else if (re instanceof RegExOpt) {
            // lastpos(a?)
            // = lastpos(a | e)
            // = lastpos(a) \/ lastpos(e)
            // = lastpos(a) \/ {}
            // = lastpos(a)
            return lastpos(((RegExOpt)re).child);
        } else if (re instanceof RegExSeq) {
            List<RegEx> seq = ((RegExSeq)re).sequence;
            Assert.check(seq.size() == 2);
            RegEx last = last(seq);
            if (nullable(last)) {
                return Sets.union(lastpos(last(seq)), lastpos(first(seq)));
            } else {
                return lastpos(last(seq));
            }
        } else if (re instanceof RegExStar) {
            return lastpos(((RegExStar)re).child);
        } else {
            throw new RuntimeException("Unknown/unexpected regex: " + re);
        }
    }

    /**
     * Mapping from position (unique character nodes) to the corresponding result of the {@link #followpos} method for
     * that position. Empty mapping until filled by the {@link #calcFollowpos} method.
     */
    private Map<RegExChar, Set<RegExChar>> followposMap = map();

    /**
     * Fills the {@link #followposMap} for the given regular expression. Must be called before calls to the
     * {@link #followpos} method.
     *
     * <p>
     * Implements the function described in Section 3.9.4 of the book, extended to allow {@link RegExOpt}.
     * </p>
     *
     * @param re The simplified regular expression.
     * @see #simplify
     */
    public void calcFollowpos(RegEx re) {
        if (re instanceof RegExAlts) {
            List<RegEx> alts = ((RegExAlts)re).alts;
            Assert.check(alts.size() == 2);

            calcFollowpos(first(alts));
            calcFollowpos(last(alts));
        } else if (re instanceof RegExChar) {
            // If no entry yet, set it to the empty set.
            RegExChar c = (RegExChar)re;
            Set<RegExChar> mapEntry = followposMap.get(c);
            if (mapEntry == null) {
                followposMap.put(c, Collections.emptySet());
            }
        } else if (re instanceof RegExOpt) {
            // a? = a | e
            // 'e' is not a position, but 'a' is.
            calcFollowpos(((RegExOpt)re).child);
        } else if (re instanceof RegExSeq) {
            List<RegEx> seq = ((RegExSeq)re).sequence;
            Assert.check(seq.size() == 2);

            calcFollowpos(first(seq));
            calcFollowpos(last(seq));

            Set<RegExChar> last = lastpos(first(seq));
            Set<RegExChar> first = firstpos(last(seq));
            for (RegExChar c: last) {
                Set<RegExChar> mapEntry = followposMap.get(c);
                mapEntry = (mapEntry == null) ? first : Sets.union(mapEntry, first);
                followposMap.put(c, mapEntry);
            }
        } else if (re instanceof RegExStar) {
            calcFollowpos(((RegExStar)re).child);

            Set<RegExChar> first = firstpos(re);
            Set<RegExChar> last = lastpos(re);
            for (RegExChar c: last) {
                Set<RegExChar> mapEntry = followposMap.get(c);
                mapEntry = (mapEntry == null) ? first : Sets.union(mapEntry, first);
                followposMap.put(c, mapEntry);
            }
        } else {
            throw new RuntimeException("Unknown/unknown regex: " + re);
        }
    }

    /**
     * Returns the following positions (unique character nodes) subset for the given position. That is, it returns all
     * positions that can follow on the given position, in a concrete string of the language.
     *
     * <p>
     * Make sure the {@link #calcFollowpos} method is invoked on regular expression R, before this method is invoked on
     * a position that is part of regular expression R.
     * </p>
     *
     * <p>
     * Implements the function described in Section 3.9.4 of the book, extended to allow {@link RegExOpt}.
     * </p>
     *
     * @param c The position (unique character node) for which to return the positions that can follow it.
     * @return The following positions (unique character nodes) subset for the given position.
     */
    public Set<RegExChar> followpos(RegExChar c) {
        Set<RegExChar> rslt = followposMap.get(c);
        Assert.notNull(rslt);
        return rslt;
    }

    /**
     * Minimizes the DFA.
     *
     * <p>
     * Implements Algorithm 3.39 of the book.
     * </p>
     *
     * @param dfa The DFA to minimize.
     * @return The minimized DFA.
     */
    public static Automaton minimizeDfa(Automaton dfa) {
        // TODO: Implement minimization (algorithm 3.39).
        return dfa;
    }
}
