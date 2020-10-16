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

package org.eclipse.escet.chi.typecheck.symbols;

import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.chi.typecheck.Message;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Symbol entry for a variable. */
public class VariableSymbolEntry extends SymbolEntry {
    /** Variable declaration. */
    private VariableDeclaration vdef;

    /**
     * Constructor of the {@link VariableSymbolEntry} class.
     *
     * @param mustBeUsed The variable symbol must be used (if {@code true} and the symbol is not used, a warning is
     *     produced).
     * @param vdef Variable symbol to store.
     * @param ctxt Type checker context for checking the symbol.
     */
    public VariableSymbolEntry(boolean mustBeUsed, VariableDeclaration vdef, CheckContext ctxt) {
        super(mustBeUsed, TypeCheckState.FULL_CHECK_DONE, ctxt);
        this.vdef = vdef;
    }

    @Override
    public String getName() {
        return vdef.getName();
    }

    @Override
    public Position getPosition() {
        return vdef.getPosition();
    }

    @Override
    public void typeCheckForUse() {
        // Nothing to do, as it is already type checked.
    }

    @Override
    public void fullTypeCheck() {
        // Nothing to do, as it is already type checked.
    }

    @Override
    public void checkUsage(CheckContext ctxt) {
        if (isUsed) {
            return;
        }
        ctxt.addWarning(Message.UNUSED_VARIABLE, getPosition(), getName());
    }

    /**
     * Get the type-checked symbol from the symbol entry.
     *
     * @return The type-checked symbol.
     */
    public VariableDeclaration getVariable() {
        Assert.check(checkState == TypeCheckState.FULL_CHECK_DONE);
        Assert.notNull(vdef);
        return vdef;
    }
}
