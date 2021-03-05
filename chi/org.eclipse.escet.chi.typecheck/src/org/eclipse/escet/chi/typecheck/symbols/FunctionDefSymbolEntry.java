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

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFunctionDeclaration;
import static org.eclipse.escet.chi.typecheck.CheckType.transNonvoidType;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.chi.typecheck.CheckContext.ContextItem;
import org.eclipse.escet.chi.typecheck.Message;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Symbol for a function definition. */
public class FunctionDefSymbolEntry extends DeclarationSymbolEntry {
    /** Original function definition. */
    private FunctionDeclaration oldFuncDef;

    /** New function definition (may be partially filled). */
    private FunctionDeclaration useFuncDef;

    /** New function definition (fully checked). */
    private FunctionDeclaration fullFuncDef;

    /**
     * Constructor of the {@link FunctionDefSymbolEntry} class.
     *
     * @param funcDef Function definition to store.
     * @param ctxt Type checker context for checking the symbol.
     */
    public FunctionDefSymbolEntry(FunctionDeclaration funcDef, CheckContext ctxt) {
        super(true, ctxt);
        this.oldFuncDef = funcDef;
        useFuncDef = null;
        fullFuncDef = null;
    }

    @Override
    protected BehaviourDeclaration getOriginalDecl() {
        return oldFuncDef;
    }

    @Override
    public FunctionDeclaration getNewDecl() {
        // Any state at or after USE_CHECK_DONE is fine.
        Assert.check(useCheckDone());
        if (useFuncDef == null) {
            throw new SemanticException();
        }
        return useFuncDef;
    }

    @Override
    public String getName() {
        return oldFuncDef.getName();
    }

    @Override
    public Position getPosition() {
        return oldFuncDef.getPosition();
    }

    @Override
    public void typeCheckForUse() {
        if (useCheckDone()) {
            return;
        }

        // Set the new state before performing type-checking.
        checkState = TypeCheckState.USE_CHECK_DONE;

        // Signature not yet constructed.
        List<VariableDeclaration> newParms = checkParameters(false);
        Type rType = transNonvoidType(oldFuncDef.getReturnType(), ctxt);
        useFuncDef = newFunctionDeclaration(oldFuncDef.getName(), copyPosition(oldFuncDef), rType, null, newParms);
    }

    @Override
    public void fullTypeCheck() {
        if (checkState == TypeCheckState.FULL_CHECK_DONE) {
            return;
        }

        // Construct the new function definition if it does not yet exist.
        try {
            typeCheckForUse();
        } finally {
            // Prevent performing this full check again.
            checkState = TypeCheckState.FULL_CHECK_DONE;
        }

        if (useFuncDef == null) {
            return; // Usage check already failed.
        }

        Type rType = useFuncDef.getReturnType();
        CheckContext retCtxt = parameterCtxt.newFunctionContext(rType);
        retCtxt = retCtxt.add(ContextItem.NO_DELAY, ContextItem.NO_SELECT, ContextItem.NO_RUN, ContextItem.NO_EXIT,
                ContextItem.NO_COMM, ContextItem.NO_TIME, ContextItem.NO_SAMPLE, ContextItem.NO_PROCESSES,
                ContextItem.NO_MODELS);
        Assert.check(retCtxt.exitType == null);
        checkBody(useFuncDef, retCtxt);
        fullFuncDef = useFuncDef;
    }

    @Override
    public void checkUsage(CheckContext ctxt) {
        if (isUsed) {
            return;
        }
        ctxt.addWarning(Message.UNUSED_FUNCTION, getPosition(), getName());
    }

    /**
     * Get the type-checked process definition.
     *
     * @return The type-checked process definition.
     */
    public FunctionDeclaration getFunction() {
        Assert.check(checkState == TypeCheckState.FULL_CHECK_DONE);
        if (fullFuncDef == null) {
            throw new SemanticException();
        }
        return fullFuncDef;
    }
}
