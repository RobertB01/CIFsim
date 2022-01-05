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

package org.eclipse.escet.setext.typechecker;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.setext.parser.ast.SeTextObject;

/** SeText symbol table. Maps names to AST objects. */
public class SymbolTable {
    /** Mapping from names to AST objects. */
    private final Map<String, SeTextObject> table = map();

    /** The type checker to use. */
    private final SeTextTypeChecker tchecker;

    /**
     * Constructor for the {@link SymbolTable} class.
     *
     * @param tchecker The type checker to use.
     */
    public SymbolTable(SeTextTypeChecker tchecker) {
        this.tchecker = tchecker;
    }

    /**
     * Adds an entry to the symbol table.
     *
     * @param name The name of the entry.
     * @param value The AST object for the entry.
     */
    public void add(String name, SeTextObject value) {
        Assert.notNull(name);
        Assert.notNull(value);

        if (table.containsKey(name)) {
            tchecker.addProblem(Message.SYMTABLE_DUPL_DECL, get(name).position, name);
            tchecker.addProblem(Message.SYMTABLE_DUPL_DECL, value.position, name);
            throw new SemanticException();
        }

        table.put(name, value);
    }

    /**
     * Is there an entry for the given name in the symbol table?
     *
     * @param name The name of the entry.
     * @return {@code true} if there is such an entry, {@code false} otherwise.
     */
    public boolean contains(String name) {
        return table.containsKey(name);
    }

    /**
     * Return the AST value for the given entry.
     *
     * @param name The name of the entry.
     * @return The AST value of the entry.
     */
    public SeTextObject get(String name) {
        Assert.check(table.containsKey(name));
        return table.get(name);
    }
}
