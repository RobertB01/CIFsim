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

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newProcessDeclaration;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.chi.typecheck.CheckContext.ContextItem;
import org.eclipse.escet.chi.typecheck.Message;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Symbol for a process definition. */
public class ProcessDefSymbolEntry extends DeclarationSymbolEntry {
    /** Stored process definition. */
    private ProcessDeclaration oldProcDef;

    /** Type-checked process definition (for usage). */
    private ProcessDeclaration useProcDef;

    /** Type-checked process definition (fully checked). */
    private ProcessDeclaration fullProcDef;

    /**
     * Constructor of the {@link ProcessDefSymbolEntry} class.
     *
     * @param procDef Process definition to store.
     * @param ctxt Type checker context for checking the symbol.
     */
    public ProcessDefSymbolEntry(ProcessDeclaration procDef, CheckContext ctxt) {
        super(true, ctxt);
        this.oldProcDef = procDef;
        useProcDef = null;
        fullProcDef = null;
    }

    @Override
    protected BehaviourDeclaration getOriginalDecl() {
        return oldProcDef;
    }

    @Override
    public ProcessDeclaration getNewDecl() {
        // Any state at or after USE_CHECK_DONE is fine.
        Assert.check(useCheckDone());
        if (useProcDef == null) {
            throw new SemanticException();
        }
        return useProcDef;
    }

    @Override
    public String getName() {
        return oldProcDef.getName();
    }

    @Override
    public Position getPosition() {
        return oldProcDef.getPosition();
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
        // Convert and check exit type.
        Type rtype = checkExitType(oldProcDef.getReturnType(), ctxt);
        useProcDef = newProcessDeclaration(oldProcDef.getName(), copyPosition(oldProcDef), rtype, null, newParms);
    }

    @Override
    public void fullTypeCheck() {
        if (checkState == TypeCheckState.FULL_CHECK_DONE) {
            return;
        }

        try {
            typeCheckForUse();
        } finally {
            // Prevent performing this full check again.
            checkState = TypeCheckState.FULL_CHECK_DONE;
        }

        if (useProcDef == null) {
            return; // Usage check already failed.
        }

        CheckContext chkCtxt = parameterCtxt.add(ContextItem.NO_MODELS);
        chkCtxt = chkCtxt.newExitContext(useProcDef.getReturnType());
        Assert.check(chkCtxt.funcReturnType == null);
        Assert.check(!chkCtxt.contains(ContextItem.NO_EXIT));
        checkBody(useProcDef, chkCtxt);
        fullProcDef = useProcDef;
    }

    @Override
    public void checkUsage(CheckContext ctxt) {
        if (isUsed) {
            return;
        }
        ctxt.addWarning(Message.UNUSED_PROCESS, getPosition(), getName());
    }

    /**
     * Get the type-checked process definition.
     *
     * @return The type-checked process definition.
     */
    public ProcessDeclaration getProcess() {
        Assert.check(checkState == TypeCheckState.FULL_CHECK_DONE);
        if (fullProcDef == null) {
            throw new SemanticException();
        }
        return fullProcDef;
    }
}
