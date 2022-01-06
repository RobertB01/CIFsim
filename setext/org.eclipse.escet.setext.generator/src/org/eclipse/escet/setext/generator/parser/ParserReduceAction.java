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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.setext.parser.ast.parser.NonTerminal;
import org.eclipse.escet.setext.parser.ast.parser.ParserRule;

/** Parsing table reduce action. */
public class ParserReduceAction extends ParserAction {
    /** The non-terminal to reduce. */
    public final NonTerminal nonterminal;

    /** The rule to use to reduce the non-terminal. */
    public final ParserRule rule;

    /**
     * Constructor for the {@link ParserReduceAction} class.
     *
     * @param nonterminal The non-terminal to reduce.
     * @param rule The rule to use to reduce the non-terminal.
     */
    public ParserReduceAction(NonTerminal nonterminal, ParserRule rule) {
        this.nonterminal = nonterminal;
        this.rule = rule;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public int hashCode() {
        return ParserReduceAction.class.hashCode() ^ nonterminal.hashCode() ^ rule.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ParserReduceAction)) {
            return false;
        }
        ParserReduceAction other = (ParserReduceAction)obj;
        return nonterminal == other.nonterminal && rule == other.rule;
    }

    @Override
    public String toString() {
        return fmt("reduce %s : %s;", nonterminal.name, rule);
    }
}
