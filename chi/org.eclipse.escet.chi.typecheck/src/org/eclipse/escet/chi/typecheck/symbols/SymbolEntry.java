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

package org.eclipse.escet.chi.typecheck.symbols;

import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Class representing a symbol in the symbol table. */
public abstract class SymbolEntry {
    /** Whether the entry has been used. */
    public boolean isUsed;

    /** State of the symbol in the type checking process. */
    protected TypeCheckState checkState;

    /** Type checker context for checking the symbol. */
    protected CheckContext ctxt;

    /**
     * Constructor of the symbol entry base class {@link SymbolEntry}.
     *
     * @param mustBeUsed Whether the symbol must be used in the specification.
     * @param ctxt Type checker context for checking the symbol.
     */
    public SymbolEntry(boolean mustBeUsed, CheckContext ctxt) {
        this(mustBeUsed, TypeCheckState.NOT_STARTED, ctxt);
    }

    /**
     * Constructor of the symbol entry base class {@link SymbolEntry}.
     *
     * @param mustBeUsed Whether the symbol must be used in the specification.
     * @param checkState State of the symbol in type-checking.
     * @param ctxt Type checker context for checking the symbol.
     */
    public SymbolEntry(boolean mustBeUsed, TypeCheckState checkState, CheckContext ctxt) {
        isUsed = !mustBeUsed;
        this.checkState = checkState;
        this.ctxt = ctxt;
    }

    /** Mark the symbol as being type-checked. */
    protected void declareBusy() {
        Assert.check(checkState != TypeCheckState.FULL_CHECK_DONE);

        ctxt.declareBusy(this, checkState == TypeCheckState.DOING_FULL_CHECK);
        checkState = TypeCheckState.DOING_FULL_CHECK;
    }

    /** Denote the symbol as being finished type checking. */
    protected void declareFinished() {
        ctxt.declareFinished(this);
        checkState = TypeCheckState.FULL_CHECK_DONE;
    }

    /** Mark the entry as being used. */
    public void setUsed() {
        isUsed = true;
    }

    /**
     * Get the name of the symbol.
     *
     * @return The name of the symbol.
     */
    public abstract String getName();

    /**
     * Get the position of the symbol.
     *
     * @return The position of the object.
     */
    public abstract Position getPosition();

    /** Perform type checking of this symbol such that it may be used. */
    public abstract void typeCheckForUse();

    /** Perform full type checking of this symbol. */
    public abstract void fullTypeCheck();

    /**
     * Check whether the symbol was used, and output a warning if not.
     *
     * @param ctxt Context object.
     */
    public abstract void checkUsage(CheckContext ctxt);

    /**
     * Is the symbol is at or beyond use-check in type checking?
     *
     * @return Whether the symbol has progressed to at least being checked for use.
     */
    public boolean useCheckDone() {
        return checkState != TypeCheckState.NOT_STARTED;
    }

    /** Stages of the type checking process for a symbol. */
    protected enum TypeCheckState {
        /** Type-checking has not started yet. */
        NOT_STARTED,

        /** Symbol has been checked for use. */
        USE_CHECK_DONE,

        /** Busy doing full type check. */
        DOING_FULL_CHECK,

        /** Symbol has been fully checked. */
        FULL_CHECK_DONE;
    }
}
