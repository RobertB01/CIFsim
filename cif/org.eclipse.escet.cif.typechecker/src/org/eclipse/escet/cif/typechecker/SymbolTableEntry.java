//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.cif.typechecker.scopes.SymbolScopeMerger;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Symbol table entry. */
public abstract class SymbolTableEntry {
    /** The CIF type checker to use. */
    public final CifTypeChecker tchecker;

    /** The completeness status of type checking of this symbol table entry. */
    public CheckStatus status = CheckStatus.NONE;

    /**
     * Constructor for the {@link SymbolTableEntry} class.
     *
     * @param tchecker The CIF type checker to use.
     */
    public SymbolTableEntry(CifTypeChecker tchecker) {
        this.tchecker = tchecker;
    }

    /**
     * Has the symbol table entry been checked 'for use'?
     *
     * @return {@code true} if the symbol table entry has been checked 'for use', {@code false} otherwise.
     */
    public boolean isCheckedForUse() {
        return status == CheckStatus.USE || status == CheckStatus.FULL;
    }

    /**
     * Has the symbol table entry been fully checked?
     *
     * @return {@code true} if the symbol table entry has been fully checked, {@code false} otherwise.
     */
    public boolean isCheckedFull() {
        return status == CheckStatus.FULL;
    }

    /**
     * Type checks the symbol table entry, 'for use' only. That is, checks the entry only as far as it is necessary 'for
     * use' by other entries that refer to it.
     *
     * @see #tcheckForUseImpl
     * @see #tcheckFull
     */
    public final void tcheckForUse() {
        // If it failed before, it will fail each time.
        if (status == CheckStatus.USE_FAILED) {
            throw new SemanticException();
        }

        // If already done, no need to do it again.
        if (status != CheckStatus.NONE) {
            return;
        }

        // Do actual checking.
        try {
            tcheckForUseImpl();
        } catch (SemanticException ex) {
            // It failed, so mark it as such, to avoid trying again.
            status = CheckStatus.USE_FAILED;
            throw ex;
        }
    }

    /**
     * Type checks the symbol table entry, 'for use' only. That is, checks the entry only as far as it is necessary 'for
     * use' by other entries that refer to it.
     *
     * <p>
     * This method should only be invoked by the {@link #tcheckForUse} method, as that method wraps this method, and
     * takes care of failures that result from type checking 'for use', and propagation of such failures. It also
     * prevents checking 'for use' multiple times, etc.
     * </p>
     *
     * <p>
     * Implementations of this method may assume that if the method is invoked, 'for use' checking has not yet been
     * performed before for this entry. They may set the status to {@link CheckStatus#USE} or {@link CheckStatus#FULL},
     * depending on whether they completed the 'for use' check or a 'full' check. They may not set it to any of the
     * other statuses.
     * </p>
     *
     * @see #tcheckForUse
     * @see #tcheckFull
     */
    protected abstract void tcheckForUseImpl();

    /**
     * Fully type checks the symbol table entry (recursively), by adding all type information, resolving references,
     * etc.
     *
     * @see #tcheckForUse
     */
    public abstract void tcheckFull();

    /**
     * Returns the name of this symbol table entry.
     *
     * @return The name of this symbol table entry.
     */
    public abstract String getName();

    /**
     * Returns the absolute name of this symbol table entry. The names should only be used in error messages, etc.
     *
     * <p>
     * Implementations of this method should generate names using {@link CifTextUtils#getAbsName}, with escaping. If
     * they don't use that method, they should generate compatible output.
     * </p>
     *
     * @return The absolute name of this symbol table entry.
     */
    public abstract String getAbsName();

    /**
     * Returns the position information for this symbol table entry.
     *
     * @return The position information for this symbol table entry.
     */
    public abstract Position getPosition();

    /**
     * Returns the CIF metamodel object associated with this symbol table entry.
     *
     * @return The CIF metamodel object associated with this symbol table entry.
     */
    public abstract PositionObject getObject();

    /**
     * Changes the parent of this symbol table entry. Should only be used by the {@link SymbolScopeMerger#mergeScopes}
     * method.
     *
     * @param parent The new parent scope.
     */
    public abstract void changeParent(ParentScope<?> parent);
}
