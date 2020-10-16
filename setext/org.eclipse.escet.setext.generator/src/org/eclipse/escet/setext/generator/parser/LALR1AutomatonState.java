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

package org.eclipse.escet.setext.generator.parser;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.setext.parser.ast.Symbol;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;

/** A state of a {@link LALR1Automaton}. */
public class LALR1AutomatonState {
    /**
     * The kernel grammar lookahead items that this state represents, as a mapping from kernel grammar items to the
     * corresponding lookahead items. The values are modified in-place, as the conversion from LR(0) items to LALR(1)
     * items is performed.
     */
    public Map<GrammarItem, LookaheadItem> items;

    /**
     * The kernel and non-kernel grammar lookahead items that this state represents, obtained by computing the closure
     * of the kernel grammar items. This field is set during generation of the LALR(1) automaton. Is not modified after
     * it is set.
     */
    public Set<LookaheadItem> itemsClosure;

    /**
     * Mapping from grammar items to the propagations for those grammar items. The values are modified in-place, as the
     * conversion from LR(0) items to LALR(1) items is performed.
     */
    public Map<GrammarItem, Set<PropagationItem>> propagations = map();

    /** The unique id of this state in the automaton. */
    public final int id;

    /**
     * The outgoing edges of this state. Mapping from the symbols that must be recognized before the edge may be taken,
     * to the target state of the edge. Mapping is filled in-place.
     */
    public Map<Symbol, LALR1AutomatonState> edges = map();

    /** The parsing table function 'ACTION' for this state. */
    public Map<Terminal, Set<ParserAction>> actions = map();

    /** The parsing table function 'GOTO' for this state. */
    public Map<NonTerminal, LALR1AutomatonState> gotos = map();

    /** The symbol that is on all edges leading to this state. Is {@code null} for the initial state. */
    public Symbol entrySymbol;

    /**
     * Constructor for the {@link LALR1AutomatonState} class.
     *
     * @param items The grammar lookahead items that this state represents, as a mapping from grammar items to the
     *     corresponding lookahead items.
     * @param id The unique id of this state in the automaton.
     */
    public LALR1AutomatonState(Map<GrammarItem, LookaheadItem> items, int id) {
        this.items = items;
        this.id = id;
        Assert.check(!items.isEmpty());
        for (LookaheadItem item: items.values()) {
            Assert.check(item.item.isKernelItem());
        }
        Assert.check(id >= 0);
    }

    /**
     * Adds an outgoing edge to this state.
     *
     * @param symbol The symbol that must be recognized before this edge may be taken.
     * @param state The target state of the edge.
     * @throws IllegalStateException If an edge with that symbol already exists.
     */
    public void addEdge(Symbol symbol, LALR1AutomatonState state) {
        // Prevent duplicate edges (paranoia check).
        if (edges.containsKey(symbol)) {
            throw new IllegalStateException("Duplicate edge.");
        }

        // Add the actual edge.
        edges.put(symbol, state);

        // Set the unique entry symbol for the target state.
        if (state.entrySymbol == null) {
            state.entrySymbol = symbol;
        } else {
            Assert.check(state.entrySymbol == symbol);
        }
    }

    /**
     * Returns the outgoing edges of this state, sorted first in ascending order by target state id, and then in
     * ascending order by name of the symbol.
     *
     * @return The sorted, outgoing edges of this state.
     */
    public List<Pair<Symbol, LALR1AutomatonState>> getSortedEdges() {
        // Note that all edges have unique code points.
        List<Pair<Symbol, LALR1AutomatonState>> sortedEdges = list();
        for (Entry<Symbol, LALR1AutomatonState> entry: edges.entrySet()) {
            sortedEdges.add(pair(entry.getKey(), entry.getValue()));
        }

        Comparator<Pair<Symbol, LALR1AutomatonState>> cmp;
        cmp = new Comparator<Pair<Symbol, LALR1AutomatonState>>() {
            @Override
            public int compare(Pair<Symbol, LALR1AutomatonState> t1, Pair<Symbol, LALR1AutomatonState> t2) {
                // Ascending order by target state id.
                int targetId1 = t1.right.id;
                int targetId2 = t2.right.id;
                if (targetId1 < targetId2) {
                    return -1;
                }
                if (targetId1 > targetId2) {
                    return 1;
                }

                // Ascending order by name of the symbol.
                return Strings.SORTER.compare(t1.left.name, t2.left.name);
            }
        };
        Collections.sort(sortedEdges, cmp);

        return sortedEdges;
    }

    /**
     * Returns the terminal to actions mapping of this state, sorted first in ascending order by actions, and then in
     * ascending order by terminal ids.
     *
     * <p>
     * Only works if each terminal is associated to at most one action.
     * </p>
     *
     * @param terminals The terminals of the SeText specification.
     * @param nonterminals The non-terminals of the SeText specification.
     * @return The sorted terminal to actions mapping of this state.
     */
    public List<Pair<Terminal, ParserAction>> getSortedActions(final List<Terminal> terminals,
            final List<NonTerminal> nonterminals)
    {
        List<Pair<Terminal, ParserAction>> sortedActions = list();
        for (Entry<Terminal, Set<ParserAction>> entry: actions.entrySet()) {
            Set<ParserAction> entryActs = entry.getValue();
            Assert.check(entryActs.size() == 1);
            sortedActions.add(pair(entry.getKey(), entryActs.iterator().next()));
        }

        Comparator<Pair<Terminal, ParserAction>> cmp;
        cmp = new Comparator<Pair<Terminal, ParserAction>>() {
            @Override
            public int compare(Pair<Terminal, ParserAction> t1, Pair<Terminal, ParserAction> t2) {
                // Ascending order by action type.
                ParserAction act1 = t1.right;
                ParserAction act2 = t2.right;
                int c = Integer.valueOf(act1.getType()).compareTo(act2.getType());
                if (c != 0) {
                    return c;
                }

                // Shift: ascending order by target state id.
                if (act1 instanceof ParserShiftAction) {
                    int target1 = ((ParserShiftAction)act1).target.id;
                    int target2 = ((ParserShiftAction)act2).target.id;
                    c = Integer.valueOf(target1).compareTo(target2);
                    if (c != 0) {
                        return c;
                    }
                }

                // Reduce: ascending order by non-terminal id, and then
                // ascending order by rule index.
                if (act1 instanceof ParserReduceAction) {
                    NonTerminal nt1 = ((ParserReduceAction)act1).nonterminal;
                    NonTerminal nt2 = ((ParserReduceAction)act2).nonterminal;
                    int idx1 = nonterminals.indexOf(nt1);
                    int idx2 = nonterminals.indexOf(nt2);
                    c = Integer.valueOf(idx1).compareTo(idx2);
                    if (c != 0) {
                        return c;
                    }

                    ParserRule rule1 = ((ParserReduceAction)act1).rule;
                    ParserRule rule2 = ((ParserReduceAction)act2).rule;
                    idx1 = nt1.rules.indexOf(rule1);
                    idx2 = nt2.rules.indexOf(rule2);
                    c = Integer.valueOf(idx1).compareTo(idx2);
                    if (c != 0) {
                        return c;
                    }
                }

                // Ascending order by terminal id. Note that all actions have
                // unique terminals.
                int termIdx1 = terminals.indexOf(t1.left);
                int termIdx2 = terminals.indexOf(t2.left);
                return Integer.valueOf(termIdx1).compareTo(termIdx2);
            }
        };
        Collections.sort(sortedActions, cmp);

        return sortedActions;
    }

    /**
     * Prints the state.
     *
     * @param s The stream to which to write the output.
     * @param initial Is this the initial state?
     */
    public void print(AppStream s, boolean initial) {
        // Get item texts.
        List<String> itemTxts = list();
        for (LookaheadItem item: items.values()) {
            itemTxts.add(item.toString());
        }
        Collections.sort(itemTxts, Strings.SORTER);

        // Print state header.
        s.println(fmt("%sstate %d (%s):", initial ? "initial " : "", id, itemTxts));

        // Print edges.
        List<Pair<Symbol, LALR1AutomatonState>> edges = getSortedEdges();
        for (Pair<Symbol, LALR1AutomatonState> edge: edges) {
            Symbol symbol = edge.left;
            LALR1AutomatonState target = edge.right;
            String edgeTxt = symbol.name + " -> " + target.id;
            s.println("  " + edgeTxt);
        }
    }

    /**
     * Prints the parsing table (as state).
     *
     * @param s The stream to which to write the output.
     * @param initial Is this the initial state?
     * @param generator The generator used to construct this state.
     */
    public void printTable(AppStream s, boolean initial, LALR1ParserGenerator generator) {
        // Print state header.
        s.println(fmt("%sstate %d:", initial ? "initial " : "", id));

        // Print item texts for the closure of the items.
        List<String> kernelTxts = list();
        List<String> nonKernelTxts = list();
        for (LookaheadItem item: itemsClosure) {
            if (item.item.isKernelItem()) {
                kernelTxts.add(item.toString(true));
            } else {
                nonKernelTxts.add(item.toString(true));
            }
        }
        Collections.sort(kernelTxts, Strings.SORTER);
        Collections.sort(nonKernelTxts, Strings.SORTER);
        for (String itemTxt: kernelTxts) {
            s.println("  + " + itemTxt);
        }
        for (String itemTxt: nonKernelTxts) {
            s.println("  - " + itemTxt);
        }
        s.println();

        // Print actions.
        List<Terminal> terminals = sortedgeneric(actions.keySet(), Terminal.NAME_COMPARER);

        int maxTermLen = 1;
        for (Terminal terminal: terminals) {
            if (terminal.name == null) {
                continue;
            }
            maxTermLen = Math.max(maxTermLen, terminal.name.length());
        }

        List<String> shiftTxts = list();
        List<String> reduceTxts = list();
        List<String> acceptTxts = list();
        boolean stateHasConflicts = false;
        for (Terminal terminal: terminals) {
            String name = terminal.name;
            if (name == null && terminal.isEof()) {
                name = "\u00B6";
            }
            Assert.notNull(name);
            name = StringUtils.rightPad(name, maxTermLen);

            Set<ParserAction> terminalActs = actions.get(terminal);
            boolean conflict = terminalActs.size() > 1;
            int shifts = 0;
            int reduces = 0;
            int accepts = 0;
            for (ParserAction action: terminalActs) {
                String txt = name + " -> " + action.toString();
                String prefix = conflict ? "! " : "  ";
                txt = prefix + txt;
                if (action instanceof ParserShiftAction) {
                    shifts++;
                    shiftTxts.add(txt);
                } else if (action instanceof ParserReduceAction) {
                    reduces++;
                    reduceTxts.add(txt);
                } else {
                    Assert.check(action instanceof ParserAcceptAction);
                    accepts++;
                    acceptTxts.add(txt);
                }
            }

            if (conflict) {
                stateHasConflicts = true;
                generator.conflicts++;

                if (shifts > 0 && reduces > 0) {
                    generator.shiftReduceConflicts++;
                    Assert.check(accepts == 0);
                    s.println("! shift/reduce conflict on " + name.trim());
                    warn("shift/reduce conflict on %s in state %d.", name.trim(), id);
                } else if (reduces > 0 && accepts > 0) {
                    generator.acceptReduceConflicts++;
                    Assert.check(shifts == 0);
                    s.println("! accept/reduce conflict on " + name.trim());
                    warn("accept/reduce conflict on %s in state %d.", name.trim(), id);
                } else {
                    generator.reduceReduceConflicts++;
                    Assert.check(shifts == 0);
                    Assert.check(accepts == 0);
                    s.println("! reduce/reduce conflict on " + name.trim());
                    warn("reduce/reduce conflict on %s in state %d.", name.trim(), id);
                }
            }
        }

        Assert.check(generator.conflicts == generator.shiftReduceConflicts + generator.reduceReduceConflicts
                + generator.acceptReduceConflicts);

        if (stateHasConflicts) {
            s.println();
        }

        Collections.sort(shiftTxts, Strings.SORTER);
        Collections.sort(reduceTxts, Strings.SORTER);
        Collections.sort(acceptTxts, Strings.SORTER);

        for (String shiftTxt: shiftTxts) {
            s.println(shiftTxt);
        }

        if (!shiftTxts.isEmpty() && !reduceTxts.isEmpty()) {
            s.println();
        }

        for (String reduceTxt: reduceTxts) {
            s.println(reduceTxt);
        }

        if ((!shiftTxts.isEmpty() || !reduceTxts.isEmpty()) && !acceptTxts.isEmpty()) {
            s.println();
        }

        for (String acceptTxt: acceptTxts) {
            s.println(acceptTxt);
        }

        // Separator.
        if (!actions.isEmpty() && !gotos.isEmpty()) {
            s.println();
        }

        // Print gotos.
        List<NonTerminal> nonterminals = sortedgeneric(gotos.keySet(), NonTerminal.NAME_COMPARER);

        int maxNonTermLen = 1;
        for (NonTerminal nonterminal: nonterminals) {
            maxNonTermLen = Math.max(maxNonTermLen, nonterminal.name.length());
        }

        for (Entry<NonTerminal, LALR1AutomatonState> entry: gotos.entrySet()) {
            NonTerminal nonterminal = entry.getKey();
            LALR1AutomatonState target = entry.getValue();

            String name = nonterminal.name;
            name = StringUtils.rightPad(name, maxNonTermLen);

            s.println("  " + name + " -> goto state " + target.id);
        }
    }
}
