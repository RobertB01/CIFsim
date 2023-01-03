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

package org.eclipse.escet.chi.typecheck.symbols;

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newXperDeclaration;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.XperDeclaration;
import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.chi.typecheck.CheckContext.ContextItem;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Symbol for an experiment definition. */
public class XperDefSymbolEntry extends DeclarationSymbolEntry {
    /** Stored experiment definition. */
    private XperDeclaration oldXperDef;

    /** Type-checked experiment definition (for usage). */
    private XperDeclaration useXperDef;

    /** Type-checked experiment definition (fully checked). */
    private XperDeclaration fullXperDef;

    /**
     * Constructor of the {@link XperDefSymbolEntry} class.
     *
     * @param xperDef Experiment definition to store.
     * @param ctxt Type checker context for checking the symbol.
     */
    public XperDefSymbolEntry(XperDeclaration xperDef, CheckContext ctxt) {
        super(false, ctxt);
        this.oldXperDef = xperDef;
        useXperDef = null;
        fullXperDef = null;
    }

    @Override
    protected BehaviourDeclaration getOriginalDecl() {
        return oldXperDef;
    }

    @Override
    protected BehaviourDeclaration getNewDecl() {
        // Any state at or after USE_CHECK_DONE is fine.
        Assert.check(useCheckDone());
        if (useXperDef == null) {
            throw new SemanticException();
        }
        return useXperDef;
    }

    @Override
    public String getName() {
        return oldXperDef.getName();
    }

    @Override
    public Position getPosition() {
        return oldXperDef.getPosition();
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
        useXperDef = newXperDeclaration(oldXperDef.getName(), copyPosition(oldXperDef.getPosition()), null, newParms);
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

        if (useXperDef == null) {
            return; // Usage check already failed.
        }

        CheckContext chkCtxt = parameterCtxt.add(ContextItem.NO_DELAY, ContextItem.NO_SELECT, ContextItem.NO_RUN,
                ContextItem.NO_COMM, ContextItem.NO_TIME, ContextItem.NO_SAMPLE, ContextItem.NO_PROCESSES,
                ContextItem.NO_EXIT);
        Assert.check(chkCtxt.exitType == null);
        Assert.check(chkCtxt.funcReturnType == null);
        checkBody(useXperDef, chkCtxt);
        fullXperDef = useXperDef;
    }

    @Override
    public void checkUsage(CheckContext ctxt) {
        // Never warn for unused experiments.
    }

    /**
     * Get the type-checked experiment definition.
     *
     * @return The type-checked experiments definition.
     */
    public XperDeclaration getXper() {
        Assert.check(checkState == TypeCheckState.FULL_CHECK_DONE);
        if (fullXperDef == null) {
            throw new SemanticException();
        }
        return fullXperDef;
    }
}
