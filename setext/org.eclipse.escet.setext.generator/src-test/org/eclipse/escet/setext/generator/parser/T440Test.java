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

package org.eclipse.escet.setext.generator.parser;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.eclipse.escet.common.app.framework.io.MemAppStream;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;
import org.eclipse.escet.setext.parser.ast.parser.ParserRulePart;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;
import org.junit.jupiter.api.Test;

/**
 * Unit tests based on the test grammar taken from Example 4.40 of the book "Compilers: Principles, Techniques, & Tools"
 * (Second Edition), by Alfred V. Aho et al., 2007.
 */
public class T440Test {
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
     * Returns the augmented grammar from Example 4.40 of the book.
     *
     * @return The augmented grammar from Example 4.40 of the book.
     */
    private Grammar getGrammar440() {
        // E' : E
        // E : E '+' T | T ;
        // T : T '*' F | F ;
        // F : '(' E ')' | 'a' ;

        // Non-terminals.
        ParserRulePart e11 = new ParserRulePart("E", false, null);
        ParserRulePart e12 = new ParserRulePart("PLUS", false, null);
        ParserRulePart e13 = new ParserRulePart("T", false, null);
        ParserRulePart e21 = new ParserRulePart("T", false, null);
        ParserRulePart t11 = new ParserRulePart("T", false, null);
        ParserRulePart t12 = new ParserRulePart("TIMES", false, null);
        ParserRulePart t13 = new ParserRulePart("F", false, null);
        ParserRulePart t21 = new ParserRulePart("F", false, null);
        ParserRulePart f11 = new ParserRulePart("PAROPEN", false, null);
        ParserRulePart f12 = new ParserRulePart("E", false, null);
        ParserRulePart f13 = new ParserRulePart("PARCLOSE", false, null);
        ParserRulePart f21 = new ParserRulePart("ID", false, null);

        List<ParserRule> eRules = list(new ParserRule(list(e11, e12, e13)), new ParserRule(list(e21)));
        List<ParserRule> tRules = list(new ParserRule(list(t11, t12, t13)), new ParserRule(list(t21)));
        List<ParserRule> fRules = list(new ParserRule(list(f11, f12, f13)), new ParserRule(list(f21)));

        NonTerminal e = new NonTerminal(null, "E", eRules, false, null);
        NonTerminal t = new NonTerminal(null, "T", tRules, false, null);
        NonTerminal f = new NonTerminal(null, "F", fRules, false, null);

        e11.symbol = e;
        e12.symbol = PLUS;
        e13.symbol = t;
        e21.symbol = t;
        t11.symbol = t;
        t12.symbol = TIMES;
        t13.symbol = f;
        t21.symbol = f;
        f11.symbol = PAROPEN;
        f12.symbol = e;
        f13.symbol = PARCLOSE;
        f21.symbol = ID;

        // Create augmented grammar.
        return new Grammar(list(e, t, f), e);
    }

    /** Test the {@link LALR1ParserGenerator#closure} function using Example 4.40 of the book. */
    @Test
    public void testClosure() {
        // Get grammar.
        Grammar grammar = getGrammar440();

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
                "[S' : . E]", //
                "[E : . E PLUS T]", //
                "[E : . T]", //
                "[T : . T TIMES F]", //
                "[T : . F]", "[F : . PAROPEN E PARCLOSE]", //
                "[F : . ID]", //
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

    /** Test the {@link LALR1ParserGenerator#getGoto} function using Example 4.41 of the book. */
    @Test
    public void testGoto() {
        // Get grammar.
        Grammar grammar = getGrammar440();

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();
        generator.constructGrammarItems(grammar);

        // Compute goto({[S' -> S .], [E -> E . + T]}).
        NonTerminal s = grammar.startSymbol;
        NonTerminal e = grammar.nonterminals.get(1);
        ParserRule sRule = first(s.rules);
        ParserRule eRule = first(e.rules);
        GrammarItem sItem = generator.itemsMap.get(s).get(sRule).get(0);
        GrammarItem eItem = generator.itemsMap.get(e).get(eRule).get(1);
        Set<GrammarItem> items = set(sItem, eItem);
        Set<GrammarItem> rslt = generator.getGoto(items, PLUS);

        // Get expected/actual.
        String[] expected = { //
                "[E : E PLUS . T]", //
                "[T : . T TIMES F]", //
                "[T : . F]", //
                "[F : . PAROPEN E PARCLOSE]", //
                "[F : . ID]", //
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

    /** Test the {@link LALR1ParserGenerator#constructLR0Automaton} function using Example 4.42 of the book. */
    @Test
    public void testConstructLR0Automaton() {
        // Get grammar.
        Grammar grammar = getGrammar440();

        // Construct generator.
        LALR1ParserGenerator generator = new LALR1ParserGenerator();
        generator.constructGrammarItems(grammar);

        // Construct LR(0) automaton.
        List<Terminal> terminals = list(PLUS, TIMES, ID, PAROPEN, PARCLOSE);
        LR0Automaton aut = generator.constructLR0Automaton(terminals, grammar);

        // Get expected output (which matches Figure 4.31 in the book).
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

        // Get actual output.
        MemAppStream s = new MemAppStream();
        aut.print(s);
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
        // 6 : 8
        // 7 : 6
        // 8 : 10
        // 9 : 9
        // 10 : 7
        // 11 : 11
    }
}
