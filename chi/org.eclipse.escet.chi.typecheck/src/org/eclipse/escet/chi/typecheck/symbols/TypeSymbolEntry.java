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

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTypeDeclaration;
import static org.eclipse.escet.chi.typecheck.CheckType.transNonvoidType;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.TypeDeclaration;
import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.chi.typecheck.Message;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Symbol table entry for a type definition. */
public class TypeSymbolEntry extends SymbolEntry {
    /** The original type definition. */
    private Declaration origType;

    /** The new type definition. */
    private Declaration newType;

    /**
     * Constructor of the {@link TypeSymbolEntry} class.
     *
     * @param type Type definition to store and check.
     * @param ctxt Type checker context for checking the symbol.
     */
    public TypeSymbolEntry(Declaration type, CheckContext ctxt) {
        super(true, ctxt);
        this.origType = type;
        if (origType instanceof EnumDeclaration) {
            // Enum declarations are checked beforehand.
            newType = type;
            checkState = TypeCheckState.FULL_CHECK_DONE;
        } else {
            newType = null;
        }
    }

    @Override
    public String getName() {
        return origType.getName();
    }

    @Override
    public Position getPosition() {
        return origType.getPosition();
    }

    @Override
    public void typeCheckForUse() {
        if (checkState == TypeCheckState.NOT_STARTED) {
            checkState = TypeCheckState.USE_CHECK_DONE;
        }
        fullTypeCheck();
    }

    @Override
    public void fullTypeCheck() {
        // If already type checked, return.
        if (checkState == TypeCheckState.FULL_CHECK_DONE) {
            return;
        }

        if (origType instanceof TypeDeclaration) {
            declareBusy();
            try {
                TypeDeclaration otd = (TypeDeclaration)origType;
                newType = transTypeDeclaration(otd, ctxt);
            } finally {
                declareFinished();
            }
            return;
        } else if (origType instanceof EnumDeclaration) {
            checkState = TypeCheckState.FULL_CHECK_DONE;
            return;
        }
        Assert.fail("Unknown type of symbol");
    }

    /**
     * Transform a type declaration.
     *
     * @param decl Declaration to transform.
     * @param ctxt Type checker context.
     * @return Type checked and converted type declaration.
     */
    private static Declaration transTypeDeclaration(TypeDeclaration decl, CheckContext ctxt) {
        Type newType = transNonvoidType(decl.getType(), ctxt);
        return newTypeDeclaration(decl.getName(), copyPosition(decl), newType);
    }

    @Override
    public void checkUsage(CheckContext ctxt) {
        if (isUsed) {
            return;
        }
        ctxt.addWarning(Message.UNUSED_TYPE, getPosition(), getName());
    }

    /**
     * Get the type-checked type definition symbol.
     *
     * @return The type definition contained in the symbol, or {@code null} if it does not contain a type definition.
     */
    public TypeDeclaration getTypeDeclaration() {
        Assert.check(checkState == TypeCheckState.FULL_CHECK_DONE);
        if (!(origType instanceof TypeDeclaration)) {
            return null;
        }
        if (newType == null) {
            throw new SemanticException();
        }
        return (TypeDeclaration)newType;
    }

    /**
     * Get the type-checked enumeration type symbol.
     *
     * @return The enumeration type contained in the symbol, or {@code null} if it does not contain an enumeration type.
     */
    public EnumDeclaration getEnumTypeDeclaration() {
        Assert.check(checkState == TypeCheckState.FULL_CHECK_DONE);
        if (!(origType instanceof EnumDeclaration)) {
            return null;
        }
        Assert.notNull(newType);
        return (EnumDeclaration)newType;
    }
}
