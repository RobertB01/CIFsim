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

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newModelDeclaration;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ModelDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.chi.typecheck.CheckContext.ContextItem;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Symbol for a model definition. */
public class ModelDefSymbolEntry extends DeclarationSymbolEntry {
    /** Stored model definition. */
    private ModelDeclaration oldModelDef;

    /** Type-checked model definition (for usage). */
    private ModelDeclaration useModelDef;

    /** Type-checked model definition (fully checked). */
    private ModelDeclaration fullModelDef;

    /**
     * Constructor of the {@link ModelDefSymbolEntry} class.
     *
     * @param modelDef Model definition to store.
     * @param ctxt Type checker context for checking the symbol.
     */
    public ModelDefSymbolEntry(ModelDeclaration modelDef, CheckContext ctxt) {
        super(false, ctxt);
        this.oldModelDef = modelDef;
        useModelDef = null;
        fullModelDef = null;
    }

    @Override
    protected BehaviourDeclaration getOriginalDecl() {
        return oldModelDef;
    }

    @Override
    public ModelDeclaration getNewDecl() {
        // Any state at or after USE_CHECK_DONE is fine.
        Assert.check(useCheckDone());
        if (useModelDef == null) {
            throw new SemanticException();
        }
        return useModelDef;
    }

    @Override
    public String getName() {
        return oldModelDef.getName();
    }

    @Override
    public Position getPosition() {
        return oldModelDef.getPosition();
    }

    @Override
    public void typeCheckForUse() {
        if (useCheckDone()) {
            return;
        }

        // Set the new state before performing type-checking.
        checkState = TypeCheckState.USE_CHECK_DONE;

        // Signature not yet constructed.
        List<VariableDeclaration> newParms = checkParameters(true);
        // Convert and check exit type.
        Type rtype = checkExitType(oldModelDef.getReturnType(), ctxt);
        useModelDef = newModelDeclaration(oldModelDef.getName(), copyPosition(oldModelDef), rtype, null, newParms);
    }

    @Override
    public void fullTypeCheck() {
        if (checkState == TypeCheckState.FULL_CHECK_DONE) {
            return;
        }

        // Construct the new model definition if it does not yet exist.
        try {
            typeCheckForUse();
        } finally {
            // Prevent performing this full check again.
            checkState = TypeCheckState.FULL_CHECK_DONE;
        }

        if (useModelDef == null) {
            return; // Usage check already failed.
        }

        CheckContext chkCtxt = parameterCtxt.add(ContextItem.NO_MODELS);
        chkCtxt = chkCtxt.newExitContext(useModelDef.getReturnType());
        Assert.check(chkCtxt.funcReturnType == null);
        Assert.check(!chkCtxt.contains(ContextItem.NO_EXIT));
        checkBody(useModelDef, chkCtxt);
        fullModelDef = useModelDef;
    }

    @Override
    public void checkUsage(CheckContext ctxt) {
        // Never warn for unused model definitions.
    }

    /**
     * Get the type-checked model definition.
     *
     * @return The type-checked model definition.
     */
    public ModelDeclaration getModel() {
        Assert.check(checkState == TypeCheckState.FULL_CHECK_DONE);
        if (fullModelDef == null) {
            throw new SemanticException();
        }
        return fullModelDef;
    }
}
