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

package org.eclipse.escet.setext.generator.parser.tests;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.slice;
import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.app.framework.io.MemAppStream;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.setext.generator.parser.Grammar;
import org.eclipse.escet.setext.generator.parser.GrammarItem;
import org.eclipse.escet.setext.generator.parser.LALR1Automaton;
import org.eclipse.escet.setext.generator.parser.LALR1ParserGenerator;
import org.eclipse.escet.setext.generator.parser.LR0Automaton;
import org.eclipse.escet.setext.generator.parser.LR0AutomatonState;
import org.eclipse.escet.setext.generator.parser.LookaheadItem;
import org.eclipse.escet.setext.parser.ast.Specification;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.parser.ParserRulePart;
import org.eclipse.escet.setext.parser.ast.parser.StartSymbol;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;
import org.junit.Test;

/**
 * Unit tests based on the test grammar (definition 4.49) taken from Example 4.48 of the book "Compilers: Principles,
 * Techniques, & Tools" (Second Edition), by Alfred V. Aho et al., 2007. Note that this grammar is also given in
 * augmented form, in Example 4.61 of the book.
 */
public class T448Test {
    /** Terminal EQUALS='='. */
    static final Terminal EQUALS = new Terminal("EQUALS", new RegExChar('=', null), null, null, null, null);

    /** Terminal STAR='*'. */
    static final Terminal STAR = new Terminal("STAR", new RegExChar('*', null), null, null, null, null);

    /** Terminal ID='a'. */
    static final Terminal ID = new Terminal("ID", new RegExChar('a', null), null, null, null, null);

    /**
     * Returns the augmented grammar from Example 4.48 of the book.
     *
     * @return The augmented grammar from Example 4.48 of the book.
     */
    private Grammar getGrammar448() {
        // S' : S ;
        // S : L '=' R | R ;
        // L : '*' R | 'a' ;
        // R : L ;

        // Non-terminals.
        ParserRulePart s11 = new ParserRulePart("L", false, null);
        ParserRulePart s12 = new ParserRulePart("EQUALS", false, null);
        ParserRulePart s13 = new ParserRulePart("R", false, null);
        ParserRulePart s21 = new ParserRulePart("R", false, null);
        ParserRulePart l11 = new ParserRulePart("STAR", false, null);
        ParserRulePart l12 = new ParserRulePart("R", false, null);
        ParserRulePart l21 = new ParserRulePart("ID", false, null);
        ParserRulePart r11 = new ParserRulePart("L", false, null);

        List<ParserRule> sRules = list(new ParserRule(list(s11, s12, s13)), new ParserRule(list(s21)));
        List<ParserRule> lRules = list(new ParserRule(list(l11, l12)), new ParserRule(list(l21)));
        List<ParserRule> rRules = list(new ParserRule(list(r11)));

        NonTerminal s = new NonTerminal(null, "S", sRules, false, null);
        NonTerminal l = new NonTerminal(null, "L", lRules, false, null);
        NonTerminal r = new NonTerminal(null, "R", rRules, false, null);

        s11.symbol = l;
        s12.symbol = EQUALS;
        s13.symbol = r;
        s21.symbol = r;
        l11.symbol = STAR;
        l12.symbol = r;
        l21.symbol = ID;
        r11.symbol = l;

        // Create augmented grammar.
        return new Grammar(list(s, l, r), s);
    }

    /**
     * Test the {@link LALR1ParserGenerator#closure} function using Figure 4.39 of the book (set of items
     * I<sub>0</sub>).
     */
    @Test
    public void testClosure() {
        // Get grammar.
        Grammar grammar = getGrammar448();

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();
        generator.constructGrammarItems(grammar);

        // Compute closure({[S' -> .E]}).
        NonTerminal startSymbol = grammar.startSymbol;
        ParserRule startRule = first(startSymbol.rules);
        Set<GrammarItem> items = set(generator.itemsMap.get(startSymbol).get(startRule).get(0));
        Set<GrammarItem> rslt = generator.closure(items);

        // Get expected/actual.
        String[] expected = { //
                "[S' : . S]", //
                "[S : . L EQUALS R]", //
                "[S : . R]", //
                "[L : . STAR R]", //
                "[L : . ID]", //
                "[R : . L]", //
        };
        Arrays.sort(expected);

        String[] actual = new String[rslt.size()];
        int idx = 0;
        for (GrammarItem item: rslt) {
            actual[idx] = item.toString();
            idx++;
        }
        Arrays.sort(actual);

        // Compare expected/actual.
        assertArrayEquals(expected, actual);
    }

    /**
     * Test the {@link LALR1ParserGenerator#constructLR0Automaton} function using Figure 4.39 of the book (states only).
     */
    @Test
    public void testConstructLR0Automaton() {
        // Get grammar.
        Grammar grammar = getGrammar448();

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();
        generator.constructGrammarItems(grammar);

        // Construct LR(0) automaton.
        List<Terminal> terminals = list(EQUALS, ID, STAR);
        LR0Automaton aut = generator.constructLR0Automaton(terminals, grammar);

        // Get expected output (which matches Figure 4.39 in the book).
        String resourcePath = getClass().getPackage().getName();
        resourcePath = resourcePath.replace('.', '/');
        resourcePath += "/" + getClass().getSimpleName() + ".out";
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(expectedStream, writer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String expected = writer.toString().replace("\r", "");

        // Get actual output (states only).
        MemAppStream s = new MemAppStream();
        for (LR0AutomatonState state: aut.states.keySet()) {
            state.print(s, aut.initialState == state, true);
        }
        String actual = s.toString().replace("\r", "");

        // Compare expected/actual.
        assertEquals(expected, actual);

        // Automaton state mapping between book and implementation:
        //
        // book : impl
        // -----------
        // 0 : 0
        // 1 : 3
        // 2 : 4
        // 3 : 5
        // 4 : 2
        // 5 : 1
        // 6 : 6
        // 7 : 9
        // 8 : 7
        // 9 : 8
    }

    /** Test the {@link LR0AutomatonState#getKernelItems} function using Figure 4.44 of the book. */
    @Test
    public void testGetKernelItems() {
        // Get grammar.
        Grammar grammar = getGrammar448();

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();
        generator.constructGrammarItems(grammar);

        // Construct LR(0) automaton.
        List<Terminal> terminals = list(EQUALS, ID, STAR);
        LR0Automaton aut = generator.constructLR0Automaton(terminals, grammar);

        // Get expected kernel items per state.
        String[] expectedBook = { //
                "[S' : . S]", //
                "[S' : S .]", //
                "[R : L .], [S : L . EQUALS R]", //
                "[S : R .]", //
                "[L : STAR . R]", //
                "[L : ID .]", //
                "[S : L EQUALS . R]", //
                "[L : STAR R .]", //
                "[R : L .]", //
                "[S : L EQUALS R .]", //
        };
        String[] expectedImpl = { //
                expectedBook[0], //
                expectedBook[5], //
                expectedBook[4], //
                expectedBook[1], //
                expectedBook[2], //
                expectedBook[3], //
                expectedBook[6], //
                expectedBook[8], //
                expectedBook[9], //
                expectedBook[7], //
        };

        // Get actual.
        String[] actual = new String[aut.states.size()];
        int idx = 0;
        for (LR0AutomatonState state: aut.states.keySet()) {
            List<String> items = list();
            for (GrammarItem item: state.getKernelItems()) {
                items.add(item.toString());
            }
            Collections.sort(items, Strings.SORTER);
            actual[idx] = StringUtils.join(items, ", ");
            idx++;
        }

        // Compare expected/actual.
        assertArrayEquals(expectedImpl, actual);
    }

    /**
     * Test the {@link LALR1ParserGenerator#closureLookahead} function using Example 4.64 of the book (for set of items
     * I<sub>0</sub>).
     */
    @Test
    public void testClosureLookahead() {
        // Get grammar.
        Grammar grammar = getGrammar448();

        // Create fresh '#' terminal.
        Terminal eof = new Terminal("#", new RegExChar(-2, null), null, null, null, null);
        List<Terminal> terminals = list(EQUALS, ID, STAR, eof);

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();
        generator.constructGrammarItems(grammar);
        generator.calcFirst(terminals, grammar);

        // Compute closure({[S' -> . E, #]}).
        NonTerminal startSymbol = grammar.startSymbol;
        ParserRule startRule = first(startSymbol.rules);
        GrammarItem startItem = generator.itemsMap.get(startSymbol).get(startRule).get(0);
        LookaheadItem startLAI = new LookaheadItem(startItem, set(eof));
        Set<LookaheadItem> rslt = generator.closureLookahead(set(startLAI));

        // Get expected/actual.
        String[] expected = { //
                "[S' : . S, {#}]", //
                "[S : . L EQUALS R, {#}]", //
                "[S : . R, {#}]", //
                "[L : . STAR R, {#, EQUALS}]", //
                "[L : . ID, {#, EQUALS}]", //
                "[R : . L, {#}]", //
        };
        Arrays.sort(expected);

        String[] actual = new String[rslt.size()];
        int idx = 0;
        for (LookaheadItem item: rslt) {
            actual[idx] = item.toString();
            idx++;
        }
        Arrays.sort(actual);

        // Compare expected/actual.
        assertArrayEquals(expected, actual);
    }

    /**
     * Test the {@link LALR1ParserGenerator#generate}, {@link LALR1ParserGenerator#fillPropagationMap},
     * {@link LALR1ParserGenerator#addLookaheads} and {@link LALR1ParserGenerator#propagateLookahead} methods using
     * Example 4.64 of the book.
     */
    @Test
    public void testConstructLALR1() {
        // Get grammar.
        Grammar grammar = getGrammar448();

        // Get terminals.
        Terminal eofTerminal = new Terminal(null, new RegExChar(-1, null), null, null, null, null);
        List<Terminal> terminals = list(EQUALS, ID, STAR, eofTerminal);

        // Create specification.
        Specification spec = new Specification(null, null);
        spec.nonterminals = slice(grammar.nonterminals, 1, null);
        spec.terminals = terminals;

        // Create start symbol.
        StartSymbol start = new StartSymbol(true, null, null, null);
        start.symbol = spec.nonterminals.get(0);

        // Construct parser automaton.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();
        LALR1Automaton aut1 = generator.generate(spec, start);

        // Get expected output (which matches Figure 4.47 in the book).
        String resourcePath = getClass().getPackage().getName();
        resourcePath = resourcePath.replace('.', '/');
        resourcePath += "/" + getClass().getSimpleName() + "_2.out";
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(expectedStream, writer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String expected = writer.toString().replace("\r", "");

        // Get actual output (states only).
        MemAppStream s = new MemAppStream();
        aut1.print(s);
        String actual = s.toString().replace("\r", "");

        // Compare expected/actual.
        assertEquals(expected, actual);

        // Automaton state mapping between book and implementation:
        //
        // book : impl
        // -----------
        // 0 : 0
        // 1 : 5
        // 2 : 3
        // 3 : 4
        // 4 : 2
        // 5 : 1
        // 6 : 6
        // 7 : 9
        // 8 : 7
        // 9 : 8
    }
}
