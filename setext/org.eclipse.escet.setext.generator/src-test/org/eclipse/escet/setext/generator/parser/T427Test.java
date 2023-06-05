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

package org.eclipse.escet.setext.generator.parser;

import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.Assert.assertArrayEquals;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.setext.parser.ast.Symbol;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.parser.ParserRulePart;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;
import org.junit.jupiter.api.Test;

/**
 * Unit tests based on the test grammar (definition 4.28) taken from Example 4.27 of the book "Compilers: Principles,
 * Techniques, & Tools" (Second Edition), by Alfred V. Aho et al., 2007.
 */
public class T427Test {
    /** Terminal PLUS='+'. */
    static final Terminal PLUS = new Terminal("PLUS", new RegExChar('+', null), null, null, null, null);

    /** Terminal TIMES='*'. */
    static final Terminal TIMES = new Terminal("TIMES", new RegExChar('*', null), null, null, null, null);

    /** Terminal ID='a'. */
    static final Terminal ID = new Terminal("ID", new RegExChar('a', null), null, null, null, null);

    /** Terminal PAROPEN='('. */
    static final Terminal PAROPEN = new Terminal("PAROPEN", new RegExChar('(', null), null, null, null, null);

    /** Terminal PARCLOSE=')'. */
    static final Terminal PARCLOSE = new Terminal("PARCLOSE", new RegExChar(')', null), null, null, null, null);

    /**
     * Returns the augmented grammar from Example 4.27 of the book.
     *
     * @return The augmented grammar from Example 4.27 of the book.
     */
    private Grammar getGrammar427() {
        // E : T E'
        // E' : '+' T E' | /*empty*/ ;
        // T : F T' ;
        // T' : '*' F T' | /*empty*/ ;
        // F : '(' E ')' | 'a' ;

        // Non-terminals.
        ParserRulePart e11 = new ParserRulePart("T", false, null);
        ParserRulePart e12 = new ParserRulePart("E'", false, null);
        ParserRulePart ep11 = new ParserRulePart("PLUS", false, null);
        ParserRulePart ep12 = new ParserRulePart("T", false, null);
        ParserRulePart ep13 = new ParserRulePart("E'", false, null);
        ParserRulePart t11 = new ParserRulePart("F", false, null);
        ParserRulePart t12 = new ParserRulePart("T'", false, null);
        ParserRulePart tp11 = new ParserRulePart("TIMES", false, null);
        ParserRulePart tp12 = new ParserRulePart("F", false, null);
        ParserRulePart tp13 = new ParserRulePart("T'", false, null);
        ParserRulePart f11 = new ParserRulePart("PAROPEN", false, null);
        ParserRulePart f12 = new ParserRulePart("E", false, null);
        ParserRulePart f13 = new ParserRulePart("PARCLOSE", false, null);
        ParserRulePart f21 = new ParserRulePart("ID", false, null);

        List<ParserRulePart> emptyPartsList = list();

        List<ParserRule> eRules = list(new ParserRule(list(e11, e12)));
        List<ParserRule> epRules = list(new ParserRule(list(ep11, ep12, ep13)), new ParserRule(emptyPartsList));
        List<ParserRule> tRules = list(new ParserRule(list(t11, t12)));
        List<ParserRule> tpRules = list(new ParserRule(list(tp11, tp12, tp13)), new ParserRule(emptyPartsList));
        List<ParserRule> fRules = list(new ParserRule(list(f11, f12, f13)), new ParserRule(list(f21)));

        NonTerminal e = new NonTerminal(null, "E", eRules, false, null);
        NonTerminal ep = new NonTerminal(null, "E'", epRules, false, null);
        NonTerminal t = new NonTerminal(null, "T", tRules, false, null);
        NonTerminal tp = new NonTerminal(null, "T'", tpRules, false, null);
        NonTerminal f = new NonTerminal(null, "F", fRules, false, null);

        e11.symbol = t;
        e12.symbol = ep;
        ep11.symbol = PLUS;
        ep12.symbol = t;
        ep13.symbol = ep;
        t11.symbol = f;
        t12.symbol = tp;
        tp11.symbol = TIMES;
        tp12.symbol = f;
        tp13.symbol = tp;
        f11.symbol = PAROPEN;
        f12.symbol = e;
        f13.symbol = PARCLOSE;
        f21.symbol = ID;

        // Create augmented grammar.
        return new Grammar(list(e, ep, t, tp, f), e);
    }

    /** Test the {@link LALR1ParserGenerator#getFirst(Symbol)} function using Example 4.30 of the book. */
    @Test
    public void testFirstForSymbol() {
        // Get grammar.
        Grammar grammar = getGrammar427();

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();

        // Compute first for all symbols.
        List<Terminal> terminals = list(PLUS, TIMES, ID, PAROPEN, PARCLOSE);
        List<Symbol> symbols = concat(terminals, grammar.nonterminals);
        generator.calcFirst(terminals, grammar);

        // Get expected/actual.
        String[] expected = { //
                "{PLUS}", // PLUS
                "{TIMES}", // TIMES
                "{ID}", // ID
                "{PAROPEN}", // PAROPEN
                "{PARCLOSE}", // PARCLOSE
                "{ID, PAROPEN}", // S'
                "{ID, PAROPEN}", // E
                "{PLUS, \u03F5}", // E'
                "{ID, PAROPEN}", // T
                "{TIMES, \u03F5}", // T'
                "{ID, PAROPEN}", // F
        };

        String[] actual = new String[expected.length];
        for (int i = 0; i < symbols.size(); i++) {
            Set<Symbol> first = generator.getFirst(symbols.get(i));
            List<String> firstTxts = list();
            for (Symbol firstSymbol: first) {
                firstTxts.add(firstSymbol.name);
            }
            Collections.sort(firstTxts, Strings.SORTER);
            actual[i] = "{" + String.join(", ", firstTxts) + "}";
        }

        // Compare expected/actual.
        assertArrayEquals(expected, actual);
    }

    /** Test the {@link LALR1ParserGenerator#getFirst(List)} function. */
    @Test
    public void testFirstForSequence() {
        // Get grammar.
        Grammar grammar = getGrammar427();

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();

        // Compute first for all symbols.
        List<Terminal> terminals = list(PLUS, TIMES, ID, PAROPEN, PARCLOSE);
        generator.calcFirst(terminals, grammar);

        // Get non-terminals.
        Symbol tp = grammar.nonterminals.get(4);
        Symbol ep = grammar.nonterminals.get(2);
        Symbol f = grammar.nonterminals.get(5);

        // Construct tests.
        List<?>[] tests = { //
                list(ID), // ID
                list(tp), // T'
                list(f, tp), // F T'
                list(tp, ep, f), // T' E' F
                list(tp, ep), // T' E'
        };

        // Get expected/actual.
        String[] expected = { //
                "{ID}", //
                "{TIMES, \u03F5}", //
                "{ID, PAROPEN}", //
                "{ID, PAROPEN, PLUS, TIMES}", //
                "{PLUS, TIMES, \u03F5}", //
        };

        String[] actual = new String[expected.length];
        for (int i = 0; i < tests.length; i++) {
            @SuppressWarnings("unchecked")
            List<Symbol> test = (List<Symbol>)tests[i];
            Set<Symbol> first = generator.getFirst(test);
            List<String> firstTxts = list();
            for (Symbol firstSymbol: first) {
                firstTxts.add(firstSymbol.name);
            }
            Collections.sort(firstTxts, Strings.SORTER);
            actual[i] = "{" + String.join(", ", firstTxts) + "}";
        }

        // Compare expected/actual.
        assertArrayEquals(expected, actual);
    }
}
