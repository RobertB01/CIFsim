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

package org.eclipse.escet.setext.generator.parser;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;

/** {@link GrammarItem} extended with a set of lookahead terminals. */
public class LookaheadItem {
    /** The grammar item. */
    public final GrammarItem item;

    /** The lookaheads for the grammar item. */
    public final Set<Terminal> lookaheads;

    /**
     * Constructor for the {@link LookaheadItem} class.
     *
     * @param item The grammar item.
     * @param lookaheads The lookaheads for the grammar item.
     */
    public LookaheadItem(GrammarItem item, Set<Terminal> lookaheads) {
        this.item = item;
        this.lookaheads = lookaheads;
    }

    @Override
    public int hashCode() {
        return LookaheadItem.class.hashCode() ^ item.hashCode() ^ lookaheads.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LookaheadItem)) {
            return false;
        }
        LookaheadItem other = (LookaheadItem)obj;
        return item.equals(other.item) && lookaheads.equals(other.lookaheads);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * Converts the lookahead item to a textual representation.
     *
     * @param simple Whether to generate a simple representation.
     * @return The textual representation of the lookahead item.
     */
    public String toString(boolean simple) {
        StringBuilder rslt = new StringBuilder();
        if (!simple) {
            rslt.append("[");
        }
        rslt.append(item.nonterminal.name.toString());
        rslt.append(" :");
        for (int i = 0; i < item.rule.symbols.size(); i++) {
            if (i == item.progress) {
                rslt.append(" .");
            }
            rslt.append(" " + item.rule.symbols.get(i).name);
        }
        if (item.isAfter()) {
            rslt.append(" .");
        }
        if (!simple) {
            rslt.append(",");
        }
        rslt.append(" ");
        List<String> lookaheadTxts = list();
        for (Terminal terminal: lookaheads) {
            String terminalName = terminal.name;
            if (terminalName == null && terminal.isEof()) {
                terminalName = "\u00B6";
            }
            Assert.notNull(terminalName);
            lookaheadTxts.add(terminalName);
        }
        Collections.sort(lookaheadTxts, Strings.SORTER);
        rslt.append("{");
        rslt.append(StringUtils.join(lookaheadTxts, ", "));
        rslt.append("}");
        if (!simple) {
            rslt.append("]");
        }
        return rslt.toString();
    }
}
