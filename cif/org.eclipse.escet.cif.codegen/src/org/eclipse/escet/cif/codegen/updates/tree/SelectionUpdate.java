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

package org.eclipse.escet.cif.codegen.updates.tree;

import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.IfElseGenerator;
import org.eclipse.escet.cif.codegen.updates.FindDeclarationUsage;
import org.eclipse.escet.cif.codegen.updates.ReadWriteDeclarations;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Selection between alternative updates. */
public class SelectionUpdate extends UpdateData {
    /** Guards of each alternative, final entry is {@code null} as it represents 'else'. */
    public final List<List<Expression>> guards;

    /** Alternative to choose between. */
    public final UpdateData[] childs;

    /**
     * Constructor of the {@link SelectionUpdate} class.
     *
     * @param ifUpd Selection update to store.
     */
    public SelectionUpdate(IfUpdate ifUpd) {
        int numAlternatives = 1 + ifUpd.getElifs().size() + (ifUpd.getElses().isEmpty() ? 0 : 1);
        guards = listc(numAlternatives);
        childs = new UpdateData[numAlternatives];

        int index = 0;
        // Add 'if' to the alternatives.
        guards.add(ifUpd.getGuards());
        childs[index] = new SequenceUpdate(ifUpd.getThens());
        index++;

        // Add the 'else if's to the alternatives.
        for (ElifUpdate elif: ifUpd.getElifs()) {
            guards.add(elif.getGuards());
            childs[index] = new SequenceUpdate(elif.getThens());
            index++;
        }

        if (!ifUpd.getElses().isEmpty()) {
            // Add 'else' to the alternatives.
            guards.add(null);
            childs[index] = new SequenceUpdate(ifUpd.getElses());
            index++;
        }

        Assert.check(index == numAlternatives);
    }

    @Override
    public ReadWriteDeclarations collectVariableUsage(CodeContext ctxt) {
        ReadWriteDeclarations rwDecls = null;

        // Merge variable usage of the children, and add variables used by the guards.
        for (int index = 0; index < childs.length; index++) {
            ReadWriteDeclarations childRW = childs[index].collectVariableUsage(ctxt);
            if (rwDecls == null) {
                rwDecls = new ReadWriteDeclarations(childRW);
            } else {
                rwDecls.addAll(childRW);
            }
            if (guards.get(index) != null) {
                FindDeclarationUsage.collectUse(guards.get(index), rwDecls.read);
            }
        }
        Assert.check(rwDecls != null);
        return rwDecls;
    }

    @Override
    protected void addLocalCopies(Set<VariableWrapper> copiedVars) {
        // Each of the branches should consider adding local copies.
        for (int index = 0; index < childs.length; index++) {
            childs[index].addLocalCopies(copiedVars);
        }
    }

    @Override
    public void genCode(CodeBox code, boolean safeScope, CodeContext readCtxt, CodeContext writeCtxt) {
        IfElseGenerator ifStatementGen = readCtxt.getIfElseUpdateGenerator();
        for (int index = 0; index < childs.length; index++) {
            if (guards.get(index) == null) {
                ifStatementGen.generateElse(code);
            } else {
                ExprCode guard = readCtxt.predsToTarget(guards.get(index));
                if (index == 0) {
                    ifStatementGen.generateIf(guard, code);
                } else {
                    ifStatementGen.generateElseIf(guard, code);
                }
            }
            childs[index].genCode(code, ifStatementGen.branchIsSafeScope(), readCtxt, writeCtxt);
        }
        ifStatementGen.generateEndIf(code);
    }
}
