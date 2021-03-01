//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.parser.ParserRulePart;

/** An augmented grammar for the {@link LALR1ParserGenerator} . */
public class Grammar {
    /** The non-terminals of the grammar. */
    public final List<NonTerminal> nonterminals;

    /**
     * The start symbol of the augmented grammar. This non-terminal is also the first element of {@link #nonterminals}.
     */
    public final NonTerminal startSymbol;

    /**
     * Constructor for the {@link Grammar} class.
     *
     * @param nonterminals The non-terminals of the grammar.
     * @param startSymbol The original start symbol of the grammar.
     */
    public Grammar(List<NonTerminal> nonterminals, NonTerminal startSymbol) {
        Assert.check(nonterminals.contains(startSymbol));

        // Augment grammar by adding new start symbol S', with rule
        // (production) "S' : S;" for original start symbol S.
        ParserRulePart part = new ParserRulePart(startSymbol.name, false, startSymbol.position);
        part.symbol = startSymbol;
        List<ParserRule> rules = list(new ParserRule(list(part)));
        NonTerminal augmentedStart = new NonTerminal(startSymbol.returnType, "S'", rules, false, startSymbol.position);

        // Store augmented grammar.
        this.nonterminals = concat(augmentedStart, nonterminals);
        this.startSymbol = augmentedStart;
    }
}
