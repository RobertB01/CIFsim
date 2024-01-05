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

package org.eclipse.escet.chi.typecheck.symbols;

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newVariableDeclaration;
import static org.eclipse.escet.chi.typecheck.CheckExpression.transExpression;
import static org.eclipse.escet.chi.typecheck.CheckStatement.transStatementList;
import static org.eclipse.escet.chi.typecheck.CheckType.isPrintable;
import static org.eclipse.escet.chi.typecheck.CheckType.transNonvoidType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.VoidType;
import org.eclipse.escet.chi.typecheck.CheckContext;
import org.eclipse.escet.chi.typecheck.CheckContext.ContextItem;
import org.eclipse.escet.chi.typecheck.CheckType;
import org.eclipse.escet.chi.typecheck.Message;
import org.eclipse.escet.common.java.Assert;

/** Base class for function, process, and model symbols. */
public abstract class DeclarationSymbolEntry extends SymbolEntry {
    /** Type checking context of the formal parameters, if created. */
    protected CheckContext parameterCtxt;

    /**
     * Constructor of the {@link DeclarationSymbolEntry} class.
     *
     * @param mustBeUsed Whether the declaration should be used (If it should be used but it is not, a warning is
     *     produced).
     * @param ctxt Type checker context for checking the symbol.
     */
    public DeclarationSymbolEntry(boolean mustBeUsed, CheckContext ctxt) {
        super(mustBeUsed, ctxt);
        parameterCtxt = null;
    }

    /**
     * Check the formal parameters of the declaration, and output the resulting symbol table in {@link #parameterCtxt}.
     *
     * @param printable Whether the parameters must be printable.
     * @return The parameter variable declarations.
     */
    protected List<VariableDeclaration> checkParameters(boolean printable) {
        // Running it more than once gives weird results.
        Assert.check(parameterCtxt == null);

        List<VariableDeclaration> newVars = list();
        parameterCtxt = ctxt.newSymbolContext();
        for (VariableDeclaration vd: getOriginalDecl().getVariables()) {
            if (!vd.isParameter()) {
                continue;
            }
            Type nType = transNonvoidType(vd.getType(), ctxt);
            Assert.check(vd.getInitialValue() == null);

            // For model parameters check that they can be entered as well.
            boolean cond = !printable || isPrintable(nType);
            ctxt.checkThrowError(cond, Message.INCORRECT_MODEL_PARAM_TYPE, vd.getType().getPosition(),
                    CheckType.toString(nType));

            VariableDeclaration nv = newVariableDeclaration(null, vd.getName(), true, copyPosition(vd), nType);
            newVars.add(nv);
            SymbolEntry se = new VariableSymbolEntry(true, nv, ctxt);
            parameterCtxt.addSymbol(se);
        }
        return newVars;
    }

    /**
     * Check the exit type of a process or model.
     *
     * @param eTp Exit type to check.
     * @param ctxt Type checking context.
     * @return Converted and checked exit type.
     */
    protected Type checkExitType(Type eTp, CheckContext ctxt) {
        if (eTp == null) {
            return null;
        }
        ctxt = ctxt.remove(ContextItem.NO_VOID);
        eTp = CheckType.transType(eTp, ctxt);
        if (isPrintable(eTp)) {
            return eTp;
        }
        Type tp = CheckType.dropReferences(eTp);
        if (tp instanceof VoidType) {
            return eTp;
        }
        ctxt.throwError(Message.EXIT_TYPE_IS_NOT_A_PRINTABLE_VALUE, eTp.getPosition(), CheckType.toString(eTp));
        return null; // Never reached.
    }

    /**
     * Perform type checking of the internals of the behavior declaration.
     *
     * @param newDecl (Empty) body of the type-checked declaration.
     * @param ctxt Type checker context.
     */
    protected void checkBody(BehaviourDeclaration newDecl, CheckContext ctxt) {
        CheckContext xtxt = ctxt.newSymbolContext();

        // Check local variables.
        for (VariableDeclaration vd: getOriginalDecl().getVariables()) {
            if (vd.isParameter()) {
                continue;
            }
            Type nType = transNonvoidType(vd.getType(), xtxt);
            Expression newInitial = null;
            if (vd.getInitialValue() != null) {
                newInitial = transExpression(vd.getInitialValue(), xtxt);

                // Check type compatibility of the declaration.
                boolean cond = CheckType.matchType(newInitial.getType(), nType);
                xtxt.checkThrowError(cond, Message.CANNOT_ASSIGN, vd.getPosition(),
                        CheckType.toString(newInitial.getType()), CheckType.toString(nType));
            }
            VariableDeclaration nv = newVariableDeclaration(newInitial, vd.getName(), false, copyPosition(vd), nType);
            newDecl.getVariables().add(nv);
            SymbolEntry se = new VariableSymbolEntry(true, nv, ctxt);
            xtxt.addSymbol(se);
        }

        // Check statements.
        List<Statement> newStats;
        newStats = transStatementList(getOriginalDecl().getStatements(), xtxt);
        newDecl.getStatements().addAll(newStats);

        xtxt.checkSymbolUsage();
        parameterCtxt.checkSymbolUsage();
    }

    /**
     * Get the original declaration stored in the symbol entry.
     *
     * @return The original declaration.
     */
    protected abstract BehaviourDeclaration getOriginalDecl();

    /**
     * Get the newly constructed declaration stored in the symbol entry.
     *
     * @return The newly constructed declaration (may be partially constructed).
     */
    protected abstract BehaviourDeclaration getNewDecl();

    /**
     * Retrieve the parameter types from the declarations.
     *
     * @return Types of the parameters without parent.
     */
    public List<Type> getParameterTypes() {
        List<Type> parms = list();
        for (VariableDeclaration vd: getNewDecl().getVariables()) {
            if (!vd.isParameter()) {
                continue;
            }
            parms.add(CheckType.copyType(vd.getType()));
        }
        return parms;
    }
}
