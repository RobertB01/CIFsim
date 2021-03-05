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

package org.eclipse.escet.setext.parser.ast;

import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.parser.JavaType;
import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.StartSymbol;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;

/** SeText specification. */
public class Specification extends SeTextObject {
    /** The declarations of the specification. */
    public final List<Decl> decls;

    /** The terminals. Available after type checking. */
    public List<Terminal> terminals = null;

    /** The non-terminals. Available after type checking. */
    public List<NonTerminal> nonterminals = null;

    /** Scanner states. Always includes {@code ""} for the default state. Available after type checking. */
    public Set<String> states = null;

    /** The main symbols, if any. Available after type checking. */
    public List<StartSymbol> mainSymbols = null;

    /** The remaining start symbols, if any. Available after type checking. */
    public List<StartSymbol> startSymbols = null;

    /** The class with the scanner/parser hook methods. Available after type checking. */
    public JavaType hooksClass = null;

    /** The class to generate for the scanner. Available after type checking. */
    public JavaType scannerClass = null;

    /**
     * Constructor for the {@link Specification} class.
     *
     * @param decls The declarations of the specification.
     * @param position Position information.
     */
    public Specification(List<Decl> decls, Position position) {
        super(position);
        this.decls = decls;
    }

    /**
     * Returns the main/start symbols.
     *
     * @return The main/start symbols.
     */
    public List<StartSymbol> getStartSymbols() {
        return concat(mainSymbols, startSymbols);
    }

    /**
     * Returns the end-of-file terminals of the specification.
     *
     * @return The end-of-file terminals of the specification.
     */
    public List<Terminal> getEofTerminals() {
        List<Terminal> rslt = list();
        for (Terminal terminal: terminals) {
            if (terminal.isEof()) {
                rslt.add(terminal);
            }
        }
        Assert.check(!rslt.isEmpty());
        return rslt;
    }
}
