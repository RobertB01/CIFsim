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

package org.eclipse.escet.chi.typecheck;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.chi.typecheck.symbols.SymbolEntry;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Symbol scope class. */
public class SymTable {
    /** Parent symbol table ({@code null} if not available). */
    private SymTable parent;

    /** Available symbols in the current scope. */
    private Map<String, SymbolEntry> table;

    /**
     * Constructor of the {@link SymTable} class.
     *
     * @param parent Parent symbol table.
     */
    public SymTable(SymTable parent) {
        this.parent = parent;
        table = map();
    }

    /**
     * Add a new symbol entry to the table.
     *
     * @param se Symbol entry to add.
     * @param ctxt Type checker context (for reporting problems).
     */
    public void addSymbol(SymbolEntry se, CheckContext ctxt) {
        // Is the symbol already defined?
        SymbolEntry other = getSymbol(se.getName());
        if (other != null) {
            ctxt.addError(Message.DUPLICATE_DECLARATION, other.getPosition(), se.getName());
            ctxt.addError(Message.DUPLICATE_DECLARATION, se.getPosition(), se.getName());
            throw new SemanticException();
        }

        // Add symbol to the current scope.
        table.put(se.getName(), se);
    }

    /**
     * Retrieve a symbol from the table.
     *
     * @param name Name to retrieve.
     * @return Symbol information of the queried symbol if it exists, else {@code null}.
     */
    public SymbolEntry getSymbol(String name) {
        SymTable st = this;
        do {
            SymbolEntry other = st.table.get(name);
            if (other != null) {
                return other;
            }
            st = st.parent;
        } while (st != null);
        return null;
    }

    /**
     * Verify whether <b>this</b> symbol table has unused symbols, and add a warning about them.
     *
     * @param ctxt Type checker context (for reporting warnings).
     */
    public void checkSymbolUsage(CheckContext ctxt) {
        for (SymbolEntry se: table.values()) {
            se.checkUsage(ctxt);
        }
    }
}
