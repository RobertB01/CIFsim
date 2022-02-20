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

package org.eclipse.escet.chi.typecheck.symbols;

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newConstantDeclaration;
import static org.eclipse.escet.chi.typecheck.CheckExpression.transExpression;
import static org.eclipse.escet.chi.typecheck.CheckType.transNonvoidType;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import org.eclipse.escet.chi.metamodel.chi.ConstantDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.chi.typecheck.CheckContext.ContextItem;
import org.eclipse.escet.chi.typecheck.CheckType;
import org.eclipse.escet.chi.typecheck.Message;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Symbol entry for a constant definition. */
public class ConstantSymbolEntry extends SymbolEntry {
    /** Original constant declaration. */
    private ConstantDeclaration oldCd;

    /** New constant declaration. */
    private ConstantDeclaration newCd;

    /**
     * Constructor of the {@link ConstantSymbolEntry} class.
     *
     * @param cd Constant definition stored in the symbol.
     * @param ctxt Type checker context for checking the symbol.
     */
    public ConstantSymbolEntry(ConstantDeclaration cd, CheckContext ctxt) {
        super(true, ctxt);
        this.oldCd = cd;
        newCd = null;
    }

    @Override
    public String getName() {
        return oldCd.getName();
    }

    @Override
    public Position getPosition() {
        return oldCd.getPosition();
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
        // Skip if already done.
        if (checkState == TypeCheckState.FULL_CHECK_DONE) {
            return;
        }

        declareBusy();
        try {
            newCd = transConstantDeclaration(oldCd, ctxt);
        } finally {
            declareFinished();
        }
    }

    @Override
    public void checkUsage(CheckContext ctxt) {
        if (isUsed) {
            return;
        }
        ctxt.addWarning(Message.UNUSED_CONSTANT, getPosition(), getName());
    }

    /**
     * Get the type-checked constant declaration.
     *
     * @return The type-checked constant declaration.
     */
    public ConstantDeclaration getConstant() {
        Assert.check(checkState == TypeCheckState.FULL_CHECK_DONE);
        if (newCd == null) {
            throw new SemanticException();
        }
        return newCd;
    }

    /**
     * Transform a constant declaration.
     *
     * @param decl Declaration to transform.
     * @param ctxt Type checker context.
     * @return Type checked and converted constant declaration.
     */
    private static ConstantDeclaration transConstantDeclaration(ConstantDeclaration decl, CheckContext ctxt) {
        ctxt = ctxt.add(ContextItem.NO_SAMPLE, ContextItem.NO_TIME, ContextItem.NO_REAL_TIMER_CAST,
                ContextItem.NO_VARIABLES, ContextItem.NO_MODELS);
        Type newCType = transNonvoidType(decl.getType(), ctxt);
        Expression newVal = transExpression(decl.getValue(), ctxt);
        if (!CheckType.matchType(newVal.getType(), newCType)) {
            ctxt.throwError(Message.CONST_HAS_INVALID_VALUE, decl.getPosition(), decl.getName(),
                    CheckType.toString(newVal.getType()), CheckType.toString(newCType));
        }

        return newConstantDeclaration(decl.getName(), copyPosition(decl), newCType, newVal);
    }
}
