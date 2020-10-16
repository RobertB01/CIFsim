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

import java.util.Comparator;
import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.Symbol;

/** Non-terminal symbol. */
public class NonTerminal extends Symbol {
    /** The return type of the parser call-back method(s) invoked when this non-terminal is reduced. */
    public final JavaType returnType;

    /** The rules (alternative productions) for this non-terminal. Must not be empty. */
    public final List<ParserRule> rules;

    /** Whether this non-terminal was generated from a group of keywords. */
    public final boolean generated;

    /**
     * Constructor for the {@link NonTerminal} class.
     *
     * @param returnType The return type of the parser call-back method(s) invoked when this non-terminal is reduced.
     * @param name The name of the non-terminal.
     * @param rules The rules (alternatives) for this non-terminal.
     * @param generated Whether this non-terminal was generated from a group of keywords.
     * @param position Position information.
     */
    public NonTerminal(JavaType returnType, String name, List<ParserRule> rules, boolean generated, Position position) {
        super(name, position);
        this.returnType = returnType;
        this.rules = rules;
        this.generated = generated;

        Assert.check(!rules.isEmpty());
    }

    /**
     * Is this the augmented start symbol, i.e. is this the "S'" non-terminal for the "S' -> S" production, which is
     * introduced when augmenting the original grammar, which has start symbol S.
     *
     * @return {@code true} if this is the special start non-terminal, {@code false} otherwise.
     */
    public final boolean isAugmentedStartSymbol() {
        // The check on the non-terminal name works, because normal
        // non-terminals are not allowed to have apostrophes in their names.
        return name.equals("S'");
    }

    /** Singleton instance of the {@link NameComparer} class. */
    public static final NameComparer NAME_COMPARER = new NameComparer();

    /**
     * Comparator for {@link NonTerminal} instances which orders ascendingly by name.
     *
     * @see Strings#SORTER
     */
    protected static class NameComparer implements Comparator<NonTerminal> {
        @Override
        public int compare(NonTerminal o1, NonTerminal o2) {
            return Strings.SORTER.compare(o1.name, o2.name);
        }
    }
}
