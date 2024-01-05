//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.declwrap;

import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.SymbolTableEntry;
import org.eclipse.escet.cif.typechecker.scopes.ParentScope;
import org.eclipse.escet.cif.typechecker.scopes.SymbolScope;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * Wrapper class for {@link SymbolScope} declarations. Holds the declaration and any additional information needed to
 * type check the symbol table entry with all necessary type information.
 *
 * @param <TM> The type of the metamodel representation of the declaration.
 */
public abstract class DeclWrap<TM extends PositionObject> extends SymbolTableEntry {
    /** The parent scope of this declaration. */
    protected ParentScope<?> scope;

    /** The CIF metamodel representation of the declaration. */
    protected final TM mmDecl;

    /** Whether this declaration is used (referenced from anywhere). */
    public boolean used = false;

    /**
     * Constructor for the {@link DeclWrap} class.
     *
     * @param tchecker The CIF type checker to use.
     * @param scope The parent scope of this declaration.
     * @param mmDecl The CIF metamodel representation of the declaration.
     */
    public DeclWrap(CifTypeChecker tchecker, ParentScope<?> scope, TM mmDecl) {
        super(tchecker);
        this.scope = scope;
        this.mmDecl = mmDecl;
    }

    /**
     * Returns the position information of the CIF metamodel object.
     *
     * @return The position information of the CIF metamodel object.
     */
    @Override
    public Position getPosition() {
        return mmDecl.getPosition();
    }

    @Override
    public TM getObject() {
        return mmDecl;
    }

    /**
     * Returns the parent scope.
     *
     * @return The parent scope.
     */
    public ParentScope<?> getParent() {
        return scope;
    }

    @Override
    public void changeParent(ParentScope<?> parent) {
        scope = parent;
    }

    /**
     * Checks the name for validity. Names starting with {@code "e_"}, {@code "c_"}, or {@code "u_"} are reserved for
     * events. Declarations that are actually events should override this check.
     *
     * @see SymbolScope#checkName
     */
    protected void checkName() {
        String name = getName();
        if (name.startsWith("e_") || name.startsWith("c_") || name.startsWith("u_")) {
            tchecker.addProblem(ErrMsg.RESERVED_NAME_PREFIX, getPosition(), getAbsName());
            // Non-fatal error.
        }
    }
}
