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

package org.eclipse.escet.setext.parser.ast.parser;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.Decl;
import org.eclipse.escet.setext.parser.ast.Identifier;
import org.eclipse.escet.setext.parser.ast.Symbol;

/**
 * Start symbol declaration. Note that a start symbol is not a {@link Symbol}. It <em>refers</em> to an actual symbol,
 * thereby declaring that symbol a start symbol.
 */
public class StartSymbol extends Decl {
    /**
     * Whether this start symbol is a main symbol. Only main start symbols are checked for completeness (unused rules
     * etc).
     */
    public final boolean main;

    /** The name of the non-terminal start symbol. */
    public final Identifier name;

    /** The non-terminal start symbol. Only available after type checking. */
    public NonTerminal symbol = null;

    /** The Java class for the generated parser for this start symbol. */
    public final JavaType javaType;

    /**
     * Constructor for the {@link StartSymbol} class.
     *
     * @param main Whether this start symbol is a main symbol.
     * @param name The name of the non-terminal start symbol.
     * @param javaType The Java class for the generated parser for this start symbol.
     * @param position Position information.
     */
    public StartSymbol(boolean main, Identifier name, JavaType javaType, Position position) {
        super(position);
        this.main = main;
        this.name = name;
        this.javaType = javaType;
    }

    /**
     * Returns the type of the start symbol.
     *
     * @return {@code "main"} for main start symbols, and {@code "start"} for non-main start symbols.
     */
    public String getStartType() {
        return main ? "main" : "start";
    }
}
