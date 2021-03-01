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

import static org.eclipse.escet.common.java.Lists.concat;
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
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.eclipse.escet.common.app.framework.io.MemAppStream;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.setext.generator.parser.Grammar;
import org.eclipse.escet.setext.generator.parser.GrammarItem;
import org.eclipse.escet.setext.generator.parser.LALR1Automaton;
import org.eclipse.escet.setext.generator.parser.LALR1ParserGenerator;
import org.eclipse.escet.setext.generator.parser.LookaheadItem;
import org.eclipse.escet.setext.parser.ast.Specification;
import org.eclipse.escet.setext.parser.ast.Symbol;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.parser.ParserRulePart;
import org.eclipse.escet.setext.parser.ast.parser.StartSymbol;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;
import org.junit.Test;

/**
 * Unit tests based on the augmented test grammar (definition 4.55) taken from Example 4.54 of the book "Compilers:
 * Principles, Techniques, & Tools" (Second Edition), by Alfred V. Aho et al., 2007.
 */
public class T454Test {
    /** Terminal c='c'. */
    static final Terminal TC = new Terminal("c", new RegExChar('c', null), null, null, null, null);

    /** Terminal d='d'. */
    static final Terminal TD = new Terminal("d", new RegExChar('d', null), null, null, null, null);

    /**
     * Returns the augmented grammar from Example 4.54 of the book.
     *
     * @return The augmented grammar from Example 4.54 of the book.
     */
    private Grammar getGrammar454() {
        // S' : S ;
        // S : C C ;
        // C : 'c' C | d ;

        // Non-terminals.
        ParserRulePart s11 = new ParserRulePart("C", false, null);
        ParserRulePart s12 = new ParserRulePart("C", false, null);
        ParserRulePart c11 = new ParserRulePart("c", false, null);
        ParserRulePart c12 = new ParserRulePart("C", false, null);
        ParserRulePart c21 = new ParserRulePart("d", false, null);

        List<ParserRule> sRules = list(new ParserRule(list(s11, s12)));
        List<ParserRule> cRules = list(new ParserRule(list(c11, c12)), new ParserRule(list(c21)));

        NonTerminal s = new NonTerminal(null, "S", sRules, false, null);
        NonTerminal c = new NonTerminal(null, "C", cRules, false, null);

        s11.symbol = c;
        s12.symbol = c;
        c11.symbol = TC;
        c12.symbol = c;
        c21.symbol = TD;

        // Create augmented grammar.
        return new Grammar(list(s, c), s);
    }

    /**
     * Test the {@link LALR1ParserGenerator#closureLookahead} function using Example 4.54 of the book (for set of items
     * I<sub>0</sub>).
     */
    @Test
    public void testClosureLookahead() {
        // Get grammar.
        Grammar grammar = getGrammar454();

        // Create fresh '$' terminal.
        Terminal eof = new Terminal("$", new RegExChar('$', null), null, null, null, null);
        List<Terminal> terminals = list(TC, TD, eof);

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();
        generator.constructGrammarItems(grammar);
        generator.calcFirst(terminals, grammar);

        // Compute closure({[S' -> . S, #]}).
        NonTerminal startSymbol = grammar.startSymbol;
        ParserRule startRule = first(startSymbol.rules);
        GrammarItem startItem = generator.itemsMap.get(startSymbol).get(startRule).get(0);
        LookaheadItem startLAI = new LookaheadItem(startItem, set(eof));
        Set<LookaheadItem> rslt = generator.closureLookahead(set(startLAI));

        // Get expected/actual.
        String[] expected = { //
                "[S' : . S, {$}]", //
                "[S : . C C, {$}]", //
                "[C : . c C, {c, d}]", //
                "[C : . d, {c, d}]", //
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
     * Test the {@link LALR1ParserGenerator#getGotoLookahead} function using Example 4.54 (and also Figure 4.41) of the
     * book.
     */
    @Test
    public void testGotoLookahead() {
        // Get grammar.
        Grammar grammar = getGrammar454();

        // Create fresh '$' terminal.
        Terminal eof = new Terminal("$", new RegExChar('$', null), null, null, null, null);
        List<Terminal> terminals = list(TC, TD, eof);

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();
        generator.constructGrammarItems(grammar);
        generator.calcFirst(terminals, grammar);

        // Compute closure({[S' -> . S, #]}). Same as 'testClosureLookahead'.
        NonTerminal startSymbol = grammar.startSymbol;
        ParserRule startRule = first(startSymbol.rules);
        GrammarItem startItem = generator.itemsMap.get(startSymbol).get(startRule).get(0);
        LookaheadItem startLAI = new LookaheadItem(startItem, set(eof));
        Set<LookaheadItem> i0 = generator.closureLookahead(set(startLAI));

        // Get symbols from grammar.
        NonTerminal s = grammar.nonterminals.get(1);
        NonTerminal c = grammar.nonterminals.get(2);
        List<Symbol> symbols = concat(terminals, grammar.nonterminals);

        // Check i0.
        String expected = Sets.toString(set( //
                "[S' : . S, {$}]", //
                "[S : . C C, {$}]", //
                "[C : . c C, {c, d}]", //
                "[C : . d, {c, d}]" //
        ));
        String actual = Sets.toString(i0);
        assertEquals(expected, actual);

        // Check i1 = GOTO(i0, S);
        Set<LookaheadItem> i1 = generator.getGotoLookahead(i0, s);
        expected = Sets.toString(set("[S' : S ., {$}]"));
        actual = Sets.toString(i1);
        assertEquals(expected, actual);

        // Check i2 = GOTO(i0, C);
        Set<LookaheadItem> i2 = generator.getGotoLookahead(i0, c);
        expected = Sets.toString(set( //
                "[S : C . C, {$}]", //
                "[C : . c C, {$}]", //
                "[C : . d, {$}]" //
        ));
        actual = Sets.toString(i2);
        assertEquals(expected, actual);

        // Check i3 = GOTO(i0, c);
        Set<LookaheadItem> i3 = generator.getGotoLookahead(i0, TC);
        expected = Sets.toString(set( //
                "[C : c . C, {c, d}]", //
                "[C : . c C, {c, d}]", //
                "[C : . d, {c, d}]" //
        ));
        actual = Sets.toString(i3);
        assertEquals(expected, actual);

        // Check i4 = GOTO(i0, d);
        Set<LookaheadItem> i4 = generator.getGotoLookahead(i0, TD);
        expected = Sets.toString(set("[C : d ., {c, d}]"));
        actual = Sets.toString(i4);
        assertEquals(expected, actual);

        // Check {} = GOTO(i0, S');
        Set<LookaheadItem> empty = generator.getGotoLookahead(i0, startSymbol);
        expected = "{}";
        actual = Sets.toString(empty);
        assertEquals(expected, actual);

        // Check {} = GOTO(i1, *);
        for (Symbol symbol: symbols) {
            empty = generator.getGotoLookahead(i1, symbol);
            expected = "{}";
            actual = Sets.toString(empty);
            assertEquals(expected, actual);
        }

        // Check i5 = GOTO(i2, C);
        Set<LookaheadItem> i5 = generator.getGotoLookahead(i2, c);
        expected = Sets.toString(set("[S : C C ., {$}]"));
        actual = Sets.toString(i5);
        assertEquals(expected, actual);

        // Check i6 = GOTO(i2, c);
        Set<LookaheadItem> i6 = generator.getGotoLookahead(i2, TC);
        expected = Sets.toString(set( //
                "[C : c . C, {$}]", //
                "[C : . c C, {$}]", //
                "[C : . d, {$}]" //
        ));
        actual = Sets.toString(i6);
        assertEquals(expected, actual);

        // Check i7 = GOTO(i2, d);
        Set<LookaheadItem> i7 = generator.getGotoLookahead(i2, TD);
        expected = Sets.toString(set("[C : d ., {$}]"));
        actual = Sets.toString(i7);
        assertEquals(expected, actual);

        // Check {} = GOTO(i2, {S', S});
        for (Symbol symbol: list(startSymbol, s)) {
            empty = generator.getGotoLookahead(i2, symbol);
            expected = "{}";
            actual = Sets.toString(empty);
            assertEquals(expected, actual);
        }

        // Check i8 = GOTO(i3, C);
        Set<LookaheadItem> i8 = generator.getGotoLookahead(i3, c);
        expected = Sets.toString(set("[C : c C ., {c, d}]"));
        actual = Sets.toString(i8);
        assertEquals(expected, actual);

        // Check i3 = GOTO(i3, c);
        Set<LookaheadItem> actualSet = generator.getGotoLookahead(i3, TC);
        expected = Sets.toString(i3);
        actual = Sets.toString(actualSet);
        assertEquals(expected, actual);

        // Check i4 = GOTO(i3, d);
        actualSet = generator.getGotoLookahead(i3, TD);
        expected = Sets.toString(i4);
        actual = Sets.toString(actualSet);
        assertEquals(expected, actual);

        // Check {} = GOTO(i3, {S', S});
        for (Symbol symbol: list(startSymbol, s)) {
            empty = generator.getGotoLookahead(i3, symbol);
            expected = "{}";
            actual = Sets.toString(empty);
            assertEquals(expected, actual);
        }

        // Check {} = GOTO(i4, *);
        for (Symbol symbol: symbols) {
            empty = generator.getGotoLookahead(i4, symbol);
            expected = "{}";
            actual = Sets.toString(empty);
            assertEquals(expected, actual);
        }

        // Check {} = GOTO(i5, *);
        for (Symbol symbol: symbols) {
            empty = generator.getGotoLookahead(i5, symbol);
            expected = "{}";
            actual = Sets.toString(empty);
            assertEquals(expected, actual);
        }

        // Check i6 = GOTO(i6, c);
        actualSet = generator.getGotoLookahead(i6, TC);
        expected = Sets.toString(i6);
        actual = Sets.toString(actualSet);
        assertEquals(expected, actual);

        // Check i7 = GOTO(i6, d);
        actualSet = generator.getGotoLookahead(i6, TD);
        expected = Sets.toString(i7);
        actual = Sets.toString(actualSet);
        assertEquals(expected, actual);

        // Check i9 = GOTO(i6, C);
        Set<LookaheadItem> i9 = generator.getGotoLookahead(i6, c);
        expected = Sets.toString(set("[C : c C ., {$}]"));
        actual = Sets.toString(i9);
        assertEquals(expected, actual);

        // Check {} = GOTO(i6, {S', S});
        for (Symbol symbol: list(startSymbol, s)) {
            empty = generator.getGotoLookahead(i6, symbol);
            expected = "{}";
            actual = Sets.toString(empty);
            assertEquals(expected, actual);
        }

        // Check {} = GOTO(i7, *);
        for (Symbol symbol: symbols) {
            empty = generator.getGotoLookahead(i7, symbol);
            expected = "{}";
            actual = Sets.toString(empty);
            assertEquals(expected, actual);
        }

        // Check {} = GOTO(i8, *);
        for (Symbol symbol: symbols) {
            empty = generator.getGotoLookahead(i8, symbol);
            expected = "{}";
            actual = Sets.toString(empty);
            assertEquals(expected, actual);
        }

        // Check {} = GOTO(i9, *);
        for (Symbol symbol: symbols) {
            empty = generator.getGotoLookahead(i9, symbol);
            expected = "{}";
            actual = Sets.toString(empty);
            assertEquals(expected, actual);
        }
    }

    /**
     * Test the {@link LALR1ParserGenerator#generate}, and {@link LALR1ParserGenerator#fillParsingTable} methods using
     * Example 4.60 (and Figure 4.43) of the book.
     */
    @Test
    public void testConstructLALR1ParsingTable() {
        // Get grammar.
        Grammar grammar = getGrammar454();

        // Get terminals.
        Terminal eofTerminal = new Terminal(null, new RegExChar(-1, null), null, null, null, null);
        List<Terminal> terminals = list(TC, TD, eofTerminal);

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

        // Get expected output (which matches Figure 4.43 in the book).
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
        aut1.printTable(s, generator);
        String actual = s.toString().replace("\r", "");

        // Compare expected/actual.
        assertEquals(expected, actual);

        // Automaton state mapping between book and implementation:
        //
        // book : impl
        // -----------
        // 0 : 0
        // 36 : 1
        // 47 : 2
        // 2 : 3
        // 1 : 4
        // 5 : 5
        // 89 : 6

        // Reduction mapping between book an implementation:
        //
        // book : impl
        // ------------
        // r1 : 'S : C C';
        // r2 : 'S : c C';
        // r3 : 'S : d';
    }
}
