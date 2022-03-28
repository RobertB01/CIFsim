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

package org.eclipse.escet.setext.parser.ast.parser;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.setext.parser.ast.SeTextObject;

/** A single parser rule (or production) for a {@link NonTerminal non-terminal}. */
public class ParserRule extends SeTextObject {
    /**
     * The symbols of the rule, excluding the non-terminal for which this rule is one of the rules. May be an empty
     * sequence.
     */
    public final List<ParserRulePart> symbols;

    /**
     * Constructor for the {@link ParserRule} class.
     *
     * @param symbols The symbols of the rule, excluding the non-terminal for which this rule is one of the rules. May
     *     be an empty sequence.
     */
    public ParserRule(List<ParserRulePart> symbols) {
        super(null);
        this.symbols = symbols;
    }

    @Override
    public String toString() {
        List<String> txts = list();
        for (ParserRulePart part: symbols) {
            txts.add(part.toString());
        }
        return String.join(" ", txts);
    }
}
