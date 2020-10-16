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

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.setext.parser.ast.Symbol;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;

/**
 * A grammar item, which represents a parser rule (production), and a position in that rule, which indicates its
 * progress.
 */
public class GrammarItem {
    /** The non-terminal for which the {@link #rule} is a rule (production). */
    public final NonTerminal nonterminal;

    /** The parser rule (production). */
    public final ParserRule rule;

    /**
     * The progress, in the range {@code [0..n]}, where {@code n} is the number of symbols in the rule (production).
     * Note that {@code 0} indicates that so far nothing of the rule has been matched, while {@code n} indicates that
     * the entire rule has been matched. For empty rules (productions), the progress is thus always {@code 0}.
     */
    public final int progress;

    /**
     * Constructor for the {@link GrammarItem} class.
     *
     * @param nonterminal The non-terminal for which the {@code rule} is a rule (production).
     * @param rule The parser rule (production).
     * @param progress The progress, in the range {@code [0..n]}, where {@code n} is the number of symbols in the rule
     *     (production). See also {@link #progress}.
     */
    public GrammarItem(NonTerminal nonterminal, ParserRule rule, int progress) {
        this.nonterminal = nonterminal;
        this.rule = rule;
        this.progress = progress;
    }

    /**
     * Is this grammar item a kernel item?
     *
     * @return {@code true} if this grammar item is a kernel item, {@code false} if it is a non-kernel item.
     */
    public boolean isKernelItem() {
        return !isBefore() || isInitialItem();
    }

    /**
     * Is this the initial item? The initial item is the "S' -> . S" grammar item for the "S' -> S" production, which is
     * introduced when augmenting the original grammar, which has start symbol S.
     *
     * @return {@code true} if this is the initial item, {@code false} otherwise.
     */
    public boolean isInitialItem() {
        return progress == 0 && nonterminal.isAugmentedStartSymbol();
    }

    /**
     * Is this an item representing that there is not yet any progress (so far nothing of the production has been
     * matched)?
     *
     * <p>
     * Note that for empty productions, this method per definition always returns {@code true}.
     * </p>
     *
     * @return {@code true} if the item represents that there is not yet any progress, {@code false} otherwise.
     */
    public boolean isBefore() {
        return progress == 0;
    }

    /**
     * Is this an item representing that the entire production has been processed?
     *
     * <p>
     * Note that for empty productions, this method per definition always returns {@code true}.
     * </p>
     *
     * @return {@code true} if the item represents that the entire production has been processed, {@code false}
     *     otherwise.
     */
    public boolean isAfter() {
        return progress == rule.symbols.size();
    }

    /**
     * Returns the next symbol in the production.
     *
     * @return The next symbol in the production.
     * @throws IllegalStateException If there is no next symbol in the production.
     */
    public Symbol getNextSymbol() {
        if (isAfter()) {
            throw new IllegalStateException();
        }
        return rule.symbols.get(progress).symbol;
    }

    /**
     * Returns the next item for the production.
     *
     * @param itemsMap The grammar items mapping to use to get the next item.
     * @return The next item for the production.
     * @throws IllegalStateException If there is no next item for the production.
     */
    public GrammarItem getNextItem(GrammarItemsMap itemsMap) {
        if (isAfter()) {
            throw new IllegalStateException();
        }
        return itemsMap.get(nonterminal).get(rule).get(progress + 1);
    }

    /**
     * Returns the remainder of the production.
     *
     * <p>
     * For instance, for a rule {@code [X : A B . C D E]}, the result is {@code [C, D, E]}.
     * </p>
     *
     * @return The remainder of the production.
     */
    public List<Symbol> remainder() {
        List<Symbol> rslt = listc(rule.symbols.size() - progress);
        for (int i = progress; i < rule.symbols.size(); i++) {
            rslt.add(rule.symbols.get(i).symbol);
        }
        return rslt;
    }

    /**
     * Returns the remainder of the production, after the {@link #getNextItem next item}.
     *
     * <p>
     * For instance, for a rule {@code [X : A B . C D E]}, the result is {@code [D, E]}.
     * </p>
     *
     * @return The remainder of the production, after the {@link #getNextItem next item}.
     * @throws IllegalStateException If there is no next item for the production.
     */
    public List<Symbol> remainderAfterNext() {
        if (isAfter()) {
            throw new IllegalStateException();
        }
        List<Symbol> rslt = listc(rule.symbols.size() - progress - 1);
        for (int i = progress + 1; i < rule.symbols.size(); i++) {
            rslt.add(rule.symbols.get(i).symbol);
        }
        return rslt;
    }

    /**
     * Returns a lookahead grammar item for this grammar item, without any lookahead symbols.
     *
     * @return a lookahead grammar item for this grammar item, without any lookahead symbols.
     */
    public LookaheadItem toLookaheadItem() {
        Set<Terminal> lookaheads = set();
        return new LookaheadItem(this, lookaheads);
    }

    @Override
    public String toString() {
        StringBuilder rslt = new StringBuilder();
        rslt.append("[");
        rslt.append(nonterminal.name.toString());
        rslt.append(" :");
        for (int i = 0; i < rule.symbols.size(); i++) {
            if (i == progress) {
                rslt.append(" .");
            }
            rslt.append(" " + rule.symbols.get(i).name);
        }
        if (isAfter()) {
            rslt.append(" .");
        }
        rslt.append("]");
        return rslt.toString();
    }
}
