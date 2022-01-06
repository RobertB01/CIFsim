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

package org.eclipse.escet.setext.generator.parser;

import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.filter;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;
import static org.eclipse.escet.common.java.Sets.union;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.setext.generator.parser.EmptySymbol.EMPTY_SYMBOL;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.setext.generator.scanner.RegExToDfa;
import org.eclipse.escet.setext.parser.ast.Specification;
import org.eclipse.escet.setext.parser.ast.Symbol;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.parser.ParserRulePart;
import org.eclipse.escet.setext.parser.ast.parser.StartSymbol;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;
import org.eclipse.escet.setext.typechecker.SeTextTypeChecker;

/**
 * LALR(1) parser generator.
 *
 * <p>
 * Based on the book "Compilers: Principles, Techniques, & Tools" (Second Edition), by Alfred V. Aho et al., 2007.
 * </p>
 */
public class LALR1ParserGenerator {
    /** Dummy terminal used by the {@link #fillPropagationMap} method. */
    public static final Terminal DUMMY_TERM = new Terminal("\\u00D7", new RegExChar('\u00D7', null), null, null, null,
            null);

    /** Grammar items mapping. Filled by the {@link #constructGrammarItems} method. */
    public GrammarItemsMap itemsMap;

    /**
     * 'first' mapping. Filled by the {@link #calcFirst} method. Maps the grammar's symbols to their first sets. The
     * keys do not include {@link #EMPTY_SYMBOL}, while the value sets may.
     */
    public Map<Symbol, Set<Symbol>> firstMap;

    /** Total number of conflicts found. */
    public int conflicts = 0;

    /** Total number of shift/reduce conflicts found. */
    public int shiftReduceConflicts = 0;

    /** Total number of reduce/reduce conflicts found. */
    public int reduceReduceConflicts = 0;

    /** Total number of accept/reduce conflicts found. */
    public int acceptReduceConflicts = 0;

    /**
     * Generates an LALR(1) parser for the given specification and start symbol.
     *
     * <p>
     * The implementation of this method is partly based on Algorithm 4.63 and Algorithm 4.56 of the book.
     * </p>
     *
     * @param spec The SeText specification.
     * @param start The start symbol.
     * @return The generated LALR(1) parser automaton.
     */
    public LALR1Automaton generate(Specification spec, StartSymbol start) {
        // Find reachable non-terminals.
        Set<NonTerminal> nonterminalSet = SeTextTypeChecker.getReachableNonTerms(start.symbol);
        List<NonTerminal> nonterminals = sortedgeneric(nonterminalSet, NonTerminal.NAME_COMPARER);

        // Construct augmented grammar.
        Grammar grammar = new Grammar(nonterminals, start.symbol);

        // Construct grammar items.
        constructGrammarItems(grammar);

        // Construct LR(0) automaton.
        LR0Automaton aut0 = constructLR0Automaton(spec.terminals, grammar);

        // Compute first mapping, for all terminals including the dummy term
        // used when adding/propagating lookaheads.
        List<Terminal> terminals = concat(spec.terminals, DUMMY_TERM);
        calcFirst(terminals, grammar);

        // Get end-of-file terminals.
        List<Terminal> eofTerminals = spec.getEofTerminals();

        // Convert LR(0) automaton to LALR(1) automaton, without lookaheads.
        LALR1Automaton aut1 = aut0.toLALR1();

        // Fill the lookahead propagation mappings for all the states.
        for (LALR1AutomatonState state: aut1.states) {
            fillPropagationMap(state);
        }

        // Add spontaneous lookaheads for all the states, and propagate them.
        for (LALR1AutomatonState state: aut1.states) {
            addLookaheads(state, eofTerminals);
        }

        // Compute grammar items closure for the LALR(1) automaton.
        for (LALR1AutomatonState state: aut1.states) {
            state.itemsClosure = closureLookahead(state.items.values());
        }

        // Fill parsing table.
        fillParsingTable(aut1, terminals, nonterminals);

        // Return LALR(1) parsing automaton.
        return aut1;
    }

    /**
     * Constructs the grammar items for the given grammar, and stores them in {@link #itemsMap}.
     *
     * @param grammar The grammar for which to construct the grammar items.
     */
    public void constructGrammarItems(Grammar grammar) {
        Assert.check(itemsMap == null);
        itemsMap = new GrammarItemsMap();

        for (NonTerminal nonterminal: grammar.nonterminals) {
            Map<ParserRule, List<GrammarItem>> ntMap = map();
            for (ParserRule rule: nonterminal.rules) {
                List<GrammarItem> items = list();
                for (int i = 0; i <= rule.symbols.size(); i++) {
                    items.add(new GrammarItem(nonterminal, rule, i));
                }
                ntMap.put(rule, items);
            }
            itemsMap.put(nonterminal, ntMap);
        }
    }

    /**
     * Constructs the LR(0) automaton for a given augmented grammar.
     *
     * <p>
     * Implements the algorithm of Figure 4.33 of the book.
     * </p>
     *
     * <p>
     * Implementation very similar to the {@link RegExToDfa#regExToDfa} method.
     * </p>
     *
     * @param terminals The terminals.
     * @param grammar The augmented grammar.
     * @return The LR(0) automaton for the augmented grammar.
     */
    public LR0Automaton constructLR0Automaton(List<Terminal> terminals, Grammar grammar) {
        // Get grammar symbols.
        List<Symbol> symbols = concat(terminals, grammar.nonterminals);

        // Construct items set for initial state.
        ParserRule startRule = first(grammar.startSymbol.rules);
        GrammarItem startItem = itemsMap.get(grammar.startSymbol).get(startRule).get(0);
        Set<GrammarItem> initialItems = closure(set(startItem));

        // Construct automaton, with initial state.
        LR0AutomatonState initialState = new LR0AutomatonState(initialItems);
        LR0Automaton automaton = new LR0Automaton(initialState);

        // Construct remainder of automaton.
        Deque<LR0AutomatonState> todoStates;
        todoStates = new LinkedList<>();
        todoStates.push(initialState);
        while (!todoStates.isEmpty()) {
            // Get next unprocessed state.
            LR0AutomatonState state = todoStates.pop();

            // Process all symbols of the grammar.
            for (Symbol symbol: symbols) {
                // Calculate target items.
                Set<GrammarItem> targetItems = getGoto(state.items, symbol);

                // If no target items, then we don't have an edge.
                if (targetItems.isEmpty()) {
                    continue;
                }

                // We have an edge.
                LR0AutomatonState target = new LR0AutomatonState(targetItems);

                // Add target state to the automaton. We get back the
                // representative for that state.
                LR0AutomatonState representative = automaton.addState(target);

                // If target is a new state, add it as 'unprocessed' (todo)
                // state.
                if (representative == target) {
                    todoStates.push(target);
                }

                // Add edge.
                state.addEdge(symbol, representative);
            }
        }

        // Return the LR(0) automaton.
        return automaton;
    }

    /**
     * Computes the closure of a set of items.
     *
     * <p>
     * Implements the algorithm of Figure 4.32 of the book.
     * </p>
     *
     * @param items The set of items for which to compute the closure.
     * @return The closure of the set of items.
     *
     * @see #closureLookahead
     */
    public Set<GrammarItem> closure(Set<GrammarItem> items) {
        Set<GrammarItem> rslt = copy(items);
        Set<NonTerminal> addedNonTerminals = set();
        while (true) {
            int size = rslt.size();

            Set<GrammarItem> toAdd = set();
            for (GrammarItem item: rslt) {
                // Skip items without a non-terminal as next symbol.
                if (item.isAfter()) {
                    continue;
                }

                Symbol nextSymbol = item.getNextSymbol();
                if (!(nextSymbol instanceof NonTerminal)) {
                    continue;
                }

                // Skip non-terminals for which we already added items.
                NonTerminal nonterminal = (NonTerminal)nextSymbol;
                if (addedNonTerminals.contains(nonterminal)) {
                    continue;
                }

                // Add start items for all the productions of the non-terminal.
                Map<ParserRule, List<GrammarItem>> ntItemsMap;
                ntItemsMap = itemsMap.get(nonterminal);
                for (List<GrammarItem> itemToAdd: ntItemsMap.values()) {
                    toAdd.add(itemToAdd.get(0));
                }

                // Mark non-terminal as added.
                addedNonTerminals.add(nonterminal);
            }
            rslt.addAll(toAdd);

            // Stop if steady state is reached.
            if (rslt.size() == size) {
                break;
            }
        }

        // Return the closure.
        return rslt;
    }

    /**
     * Calculates and returns the outgoing edges of an LR(0) automaton state, given the items that represent the source
     * state, and a grammar symbol.
     *
     * <p>
     * Implements the function 'GOTO' from page 246 of the book.
     * </p>
     *
     * @param items The items that represent the source state.
     * @param symbol The grammar symbol.
     * @return The set of items that represents the target state.
     */
    public Set<GrammarItem> getGoto(Set<GrammarItem> items, Symbol symbol) {
        Set<GrammarItem> rslt = set();
        for (GrammarItem item: items) {
            // Skip items without a next symbol.
            if (item.isAfter()) {
                continue;
            }

            // Skip items for the wrong next symbol.
            Symbol nextSymbol = item.getNextSymbol();
            if (nextSymbol != symbol) {
                continue;
            }

            // Add closure of the next item.
            Set<GrammarItem> toAdd = closure(set(item.getNextItem(itemsMap)));
            rslt.addAll(toAdd);
        }
        return rslt;
    }

    /**
     * Calculates first(X) for all symbols X in the given grammar. The results are stored in {@link #firstMap}. Must be
     * called before calls to the {@link #getFirst} method.
     *
     * <p>
     * Implements the 'FIRST(X)' function from Section 4.4.2 of the book, for X a single symbol from the grammar.
     * </p>
     *
     * @param terminals The terminals from the specification.
     * @param grammar The grammar.
     */
    public void calcFirst(List<Terminal> terminals, Grammar grammar) {
        // Initialize to empty.
        Assert.check(firstMap == null);
        firstMap = map();

        // Add initial entry for all symbols. Singleton set with the terminal
        // itself for terminals, Empty set for non-terminals without an empty
        // production, and singleton set with empty symbol for non-terminals
        // with an empty production.
        List<Symbol> symbols = concat(terminals, grammar.nonterminals);
        for (Symbol symbol: symbols) {
            Set<Symbol> firstSet = set();
            if (symbol instanceof Terminal) {
                firstSet.add(symbol);
            } else {
                Assert.check(symbol instanceof NonTerminal);
                NonTerminal nonterminal = (NonTerminal)symbol;
                boolean hasEmptyProd = false;
                for (ParserRule rule: nonterminal.rules) {
                    if (rule.symbols.isEmpty()) {
                        hasEmptyProd = true;
                        break;
                    }
                }
                if (hasEmptyProd) {
                    firstSet.add(EMPTY_SYMBOL);
                }
            }
            firstMap.put(symbol, firstSet);
        }

        // Iteratively extend the first sets of the non-terminals.
        while (true) {
            // No progress yet in this iteration.
            boolean progress = false;

            // Process all non-empty productions.
            for (NonTerminal nonterminal: grammar.nonterminals) {
                Set<Symbol> nontermFirst = firstMap.get(nonterminal);
                for (ParserRule rule: nonterminal.rules) {
                    if (rule.symbols.isEmpty()) {
                        continue;
                    }

                    // Process each symbol (rule part) in the productions
                    // (rules). Keep processing symbols until we hit one that
                    // does not have empty in its first set.
                    boolean allEmpty = true;
                    for (ParserRulePart part: rule.symbols) {
                        Set<Symbol> partFirst = firstMap.get(part.symbol);

                        // Add all non-empty symbols to the non-terminal's
                        // first set.
                        for (Symbol addSymbol: partFirst) {
                            if (addSymbol == EMPTY_SYMBOL) {
                                continue;
                            }
                            progress |= nontermFirst.add(addSymbol);
                        }

                        // If we still have empty in the symbol (rule part),
                        // continue with the next one. Otherwise, we stop
                        // with this production (rule), and also mark the
                        // production as not having only empty symbols (rule
                        // parts).
                        if (!partFirst.contains(EMPTY_SYMBOL)) {
                            allEmpty = false;
                            break;
                        }
                    }

                    // If all the symbols for the production have empty in
                    // their first set, add empty to the first set of the
                    // non-terminal.
                    if (allEmpty) {
                        progress |= nontermFirst.add(EMPTY_SYMBOL);
                    }
                }
            }

            // Stop when no progress in last iteration (steady state reached).
            if (!progress) {
                break;
            }
        }
    }

    /**
     * Returns the first set, for a given grammar symbol.
     *
     * <p>
     * Make sure the {@link #calcFirst} method is invoked on grammar G, before this method is invoked on a symbol that
     * is part of grammar G.
     * </p>
     *
     * <p>
     * Implements the 'FIRST(X)' function from Section 4.4.2 of the book, for X a single symbol from the grammar.
     * </p>
     *
     * @param symbol The symbol for which to return the first set. Must not be {@link #EMPTY_SYMBOL}.
     * @return The first set for the symbol. May include {@link #EMPTY_SYMBOL}.
     */
    public Set<Symbol> getFirst(Symbol symbol) {
        Set<Symbol> rslt = firstMap.get(symbol);
        Assert.notNull(rslt);
        return rslt;
    }

    /**
     * Returns the first set, for a given sequence of grammar symbols.
     *
     * <p>
     * Make sure the {@link #calcFirst} method is invoked on grammar G, before this method is invoked on symbols that
     * are part of grammar G.
     * </p>
     *
     * <p>
     * Implements the 'FIRST(X<sub>1</sub> X<sub>2</sub> ... X<sub>n</sub>)' function from Section 4.4.2 of the book,
     * for a sequence of grammar symbols.
     * </p>
     *
     * @param symbols The sequence of grammar symbols for which to return the first set. Must not include
     *     {@link #EMPTY_SYMBOL}.
     * @return The first set for the sequence of symbols. May include {@link #EMPTY_SYMBOL}.
     */
    public Set<Symbol> getFirst(List<Symbol> symbols) {
        Assert.check(!symbols.isEmpty());

        // Start with empty, and progress through the sequence.
        Set<Symbol> rslt = set();
        boolean allEmpty = true;
        for (Symbol symbol: symbols) {
            // Get first(symbol).
            Set<Symbol> symbolFirst = getFirst(symbol);
            boolean hasEmpty = false;
            for (Symbol addSymbol: symbolFirst) {
                if (addSymbol != EMPTY_SYMBOL) {
                    // Add all non-empty symbols to the result.
                    rslt.add(addSymbol);
                } else {
                    // This symbol in the sequence has empty in its first set.
                    hasEmpty = true;
                }
            }

            if (!hasEmpty) {
                // This symbol in the sequence has no empty in its first set.
                allEmpty = false;
                break;
            }
        }

        // If all symbols in sequence have empty in first set, so does result.
        if (allEmpty) {
            rslt.add(EMPTY_SYMBOL);
        }

        // Return the first set for the sequence of symbols.
        return rslt;
    }

    /**
     * Computes the closure of a set of lookahead items.
     *
     * <p>
     * Implements the algorithm of Figure 4.40 of the book.
     * </p>
     *
     * @param items The set of lookahead items for which to compute the closure.
     * @return The closure of the set of lookahead items.
     *
     * @see #closure
     */
    public Set<LookaheadItem> closureLookahead(Collection<LookaheadItem> items) {
        Map<GrammarItem, LookaheadItem> rslt = map();
        for (LookaheadItem item: items) {
            LookaheadItem prev = rslt.put(item.item, item);
            Assert.check(prev == null); // No two items for same grammar item.
        }

        while (true) {
            boolean progress = false;

            Set<GrammarItem> curRsltKeys = copy(rslt.keySet());
            for (GrammarItem item: curRsltKeys) {
                // Skip items without a non-terminal as next symbol.
                if (item.isAfter()) {
                    continue;
                }

                Symbol nextSymbol = item.getNextSymbol();
                if (!(nextSymbol instanceof NonTerminal)) {
                    continue;
                }

                NonTerminal nonterminal = (NonTerminal)nextSymbol;

                // Process for each lookahead terminal.
                Set<Terminal> lookaheads = rslt.get(item).lookaheads;
                for (Terminal terminal: lookaheads) {
                    // Compute FIRST(beta alpha).
                    List<Symbol> beta = item.remainderAfterNext();
                    Symbol alpha = terminal;
                    Set<Symbol> first = getFirst(concat(beta, alpha));
                    first = copy(first);
                    first.remove(EMPTY_SYMBOL);

                    // Keep only terminals in first set.
                    Set<Terminal> firstTerms = filter(first, Terminal.class);
                    if (firstTerms.isEmpty()) {
                        continue;
                    }

                    // Process all productions for the non-terminal.
                    Map<ParserRule, List<GrammarItem>> ntItemsMap;
                    ntItemsMap = itemsMap.get(nonterminal);
                    for (List<GrammarItem> itemToAdd: ntItemsMap.values()) {
                        // Add the first item of the production, for non-empty
                        // terminals in the first set.
                        GrammarItem addItem = itemToAdd.get(0);
                        LookaheadItem curLAI = rslt.get(addItem);
                        if (curLAI == null) {
                            // Add new grammar item.
                            progress = true;
                            LookaheadItem newLAI = new LookaheadItem(addItem, firstTerms);
                            rslt.put(addItem, newLAI);
                        } else {
                            // Update existing grammar item.
                            Set<Terminal> curLA = curLAI.lookaheads;
                            Set<Terminal> updLA = union(curLA, firstTerms);
                            if (updLA.size() != curLA.size()) {
                                progress = true;
                                LookaheadItem updLAI = new LookaheadItem(addItem, updLA);
                                rslt.put(addItem, updLAI);
                            }
                        }
                    }
                }
            }

            // Stop if no progress for this iteration (steady state reached).
            if (!progress) {
                break;
            }
        }

        // Return the closure.
        return new LinkedHashSet<>(rslt.values());
    }

    /**
     * Calculates and returns the outgoing edges of an LR(1) automaton state, given the lookahead items that represent
     * the source state, and a grammar symbol.
     *
     * <p>
     * Implements the function 'GOTO' from Figure 4.40 (page 261) of the book.
     * </p>
     *
     * @param items The items that represent the source state.
     * @param symbol The grammar symbol.
     * @return The set of items that represents the target state.
     */
    public Set<LookaheadItem> getGotoLookahead(Set<LookaheadItem> items, Symbol symbol) {
        Set<LookaheadItem> rslt = set();
        for (LookaheadItem item: items) {
            // Skip items without a next symbol.
            if (item.item.isAfter()) {
                continue;
            }

            // Skip items for the wrong next symbol.
            Symbol nextSymbol = item.item.getNextSymbol();
            if (nextSymbol != symbol) {
                continue;
            }

            // Add next item.
            GrammarItem nextItem = item.item.getNextItem(itemsMap);
            LookaheadItem nextLAI = new LookaheadItem(nextItem, item.lookaheads);
            rslt.add(nextLAI);
        }
        return closureLookahead(rslt);
    }

    /**
     * Fills the propagation mapping for the given state.
     *
     * <p>
     * This method requires that the {@link #calcFirst} method has been previously invoked, with {@link #DUMMY_TERM}
     * term as one of the terminals.
     * </p>
     *
     * @param state The state for which to fill the propagation mapping.
     * @see LALR1AutomatonState#propagations
     */
    public void fillPropagationMap(LALR1AutomatonState state) {
        // Process all kernel grammar items.
        for (GrammarItem item: state.items.keySet()) {
            Assert.check(item.isKernelItem());

            // Compute closure(grammar item, DUMMY_TERM).
            LookaheadItem dummyItem = new LookaheadItem(item, set(DUMMY_TERM));
            Set<LookaheadItem> closure = closureLookahead(set(dummyItem));

            // Process all closure items.
            for (LookaheadItem cItem: closure) {
                // Skip items without a next symbol.
                if (cItem.item.isAfter()) {
                    continue;
                }
                Symbol nextSymbol = cItem.item.getNextSymbol();

                // Get target state.
                LALR1AutomatonState target = state.edges.get(nextSymbol);
                Assert.notNull(target);

                // Fill propagation mapping, if applicable.
                if (cItem.lookaheads.contains(DUMMY_TERM)) {
                    // Construct propagation item.
                    GrammarItem nextItem = cItem.item.getNextItem(itemsMap);
                    PropagationItem propItem = new PropagationItem(target, nextItem);

                    // Add propagation item.
                    Set<PropagationItem> propagations;
                    propagations = state.propagations.get(item);
                    if (propagations == null) {
                        propagations = set();
                        state.propagations.put(item, propagations);
                    }
                    propagations.add(propItem);
                }
            }
        }
    }

    /**
     * Adds spontaneously generated lookaheads to the items of the given state, and propagates them forward.
     *
     * <p>
     * This method requires that the {@link #calcFirst} method has been previously invoked, with {@link #DUMMY_TERM}
     * term as one of the terminals.
     * </p>
     *
     * @param state The state for which to add spontaneously generated lookaheads.
     * @param eofTerminals The end-of-file terminals to add as spontaneous lookaheads for the item of the initial state.
     */
    public void addLookaheads(LALR1AutomatonState state, List<Terminal> eofTerminals) {
        // Process all kernel grammar items.
        for (GrammarItem item: state.items.keySet()) {
            Assert.check(item.isKernelItem());

            // Compute closure(grammar item, DUMMY_TERM).
            LookaheadItem dummyItem = new LookaheadItem(item, set(DUMMY_TERM));
            Set<LookaheadItem> closure = closureLookahead(set(dummyItem));

            // Process all closure items.
            for (LookaheadItem cItem: closure) {
                // Skip items without a next symbol.
                if (cItem.item.isAfter()) {
                    continue;
                }
                Symbol nextSymbol = cItem.item.getNextSymbol();

                // Get target state.
                LALR1AutomatonState target = state.edges.get(nextSymbol);
                Assert.notNull(target);

                // Add spontaneously generated lookaheads, and propagate them.
                GrammarItem nextItem = cItem.item.getNextItem(itemsMap);
                for (Terminal lookahead: cItem.lookaheads) {
                    if (lookahead != DUMMY_TERM) {
                        // Spontaneously generated lookahead.
                        propagateLookahead(target, nextItem, lookahead);
                    }
                }
            }
        }

        // Special case for initial state and end-of-file terminals.
        if (state.id == 0) {
            // Get the single kernel item.
            Assert.check(state.items.size() == 1);
            GrammarItem item = state.items.keySet().iterator().next();
            Assert.check(item.isKernelItem());

            // Propagate end-of-file terminal lookaheads.
            for (Terminal eofTerminal: eofTerminals) {
                propagateLookahead(state, item, eofTerminal);
            }
        }
    }

    /**
     * Propagate a single lookahead symbol.
     *
     * @param state The state to which to propagate the lookahead symbol.
     * @param item The grammar item to which to propagate the lookahead symbol.
     * @param lookahead The lookahead symbol to propagate.
     */
    public void propagateLookahead(LALR1AutomatonState state, GrammarItem item, Terminal lookahead) {
        // Get current lookaheads.
        LookaheadItem laItem = state.items.get(item);
        Set<Terminal> curLA = laItem.lookaheads;

        // Try to add new lookahead. If already present, we are done.
        boolean added = curLA.add(lookahead);
        if (!added) {
            return;
        }

        // New lookahead, so propagate.
        Set<PropagationItem> propagations = state.propagations.get(item);
        if (propagations == null) {
            return;
        }
        Assert.check(!propagations.isEmpty());
        for (PropagationItem propagation: propagations) {
            propagateLookahead(propagation.state, propagation.item, lookahead);
        }
    }

    /**
     * Fills the LALR(1) parsing tables for the given LALR(1) parser automaton.
     *
     * <p>
     * Implements Algorithm 4.56 of the book.
     * </p>
     *
     * @param aut The LALR(1) parser automaton.
     * @param terminals The terminal symbols.
     * @param nonterminals The non-terminal symbols.
     */
    public void fillParsingTable(LALR1Automaton aut, List<Terminal> terminals, List<NonTerminal> nonterminals) {
        // Process all states.
        for (LALR1AutomatonState state: aut.states) {
            // Get items (including non-kernel ones) for the state.
            Set<LookaheadItem> items = state.itemsClosure;

            // Decide parser actions for each terminal.
            for (Terminal terminal: terminals) {
                Set<ParserAction> actions = set();

                // Process all lookahead items.
                for (LookaheadItem item: items) {
                    if (!item.item.isAfter()) {
                        // Shift if terminal matches next symbol.
                        Symbol nextSymbol = item.item.getNextSymbol();
                        if (nextSymbol != terminal) {
                            continue;
                        }

                        LALR1AutomatonState target = state.edges.get(terminal);
                        actions.add(new ParserShiftAction(target));
                    } else {
                        // Make sure lookahead matches terminal.
                        if (!item.lookaheads.contains(terminal)) {
                            continue;
                        }

                        // Reduce/accept.
                        if (item.item.nonterminal.isAugmentedStartSymbol()) {
                            // Accept.
                            actions.add(new ParserAcceptAction());
                        } else {
                            // Reduce.
                            actions.add(new ParserReduceAction(item.item.nonterminal, item.item.rule));
                        }
                    }
                }

                // If we have an action, store it in the 'actions' mapping.
                if (!actions.isEmpty()) {
                    state.actions.put(terminal, actions);
                }
            }

            // Copy 'edges' to 'gotos' for non-terminals.
            for (NonTerminal nonterminal: nonterminals) {
                LALR1AutomatonState target = state.edges.get(nonterminal);
                if (target == null) {
                    continue;
                }
                state.gotos.put(nonterminal, target);
            }
        }
    }

    /**
     * Returns a textual representation of the conflict totals.
     *
     * @return A textual representation of the conflict totals.
     */
    public String getConflictsText() {
        String conflictTxt = "";
        if (conflicts > 0) {
            conflictTxt = fmt(" (%d shift/reduce, %d reduce/reduce, %d accept/reduce)", shiftReduceConflicts,
                    reduceReduceConflicts, acceptReduceConflicts);
        }
        String msg = fmt("%d conflict(s) in total%s.", conflicts, conflictTxt);
        return msg;
    }
}
