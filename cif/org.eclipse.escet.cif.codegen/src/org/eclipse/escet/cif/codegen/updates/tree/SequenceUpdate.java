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

package org.eclipse.escet.cif.codegen.updates.tree;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ReadRenameCodeContext;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.updates.ReadWriteDeclarations;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Sequence of child updates in the tree. */
public class SequenceUpdate extends UpdateData {
    /** Child updates. */
    public UpdateData[] childs;

    /**
     * Variables that are copied before performing execution of the child updates.
     *
     * <p>
     * Set during {@link #addLocalCopies}.
     * </p>
     */
    public Set<VariableWrapper> copiedVars = null;

    /** Read and written declarations by each child update. */
    private ReadWriteDeclarations[] childRWdecls;

    /**
     * Constructor of the {@link SequenceUpdate} class.
     *
     * @param updates Updates to perform.
     */
    public SequenceUpdate(List<Update> updates) {
        List<UpdateData> childs = list();

        for (Update childUpd: updates) {
            if (childUpd instanceof Assignment) {
                childs.addAll(AssignmentUpdate.newAssignmentUpdate((Assignment)childUpd));
            } else {
                Assert.check(childUpd instanceof IfUpdate);
                childs.add(new SelectionUpdate((IfUpdate)childUpd));
            }
        }
        this.childs = new UpdateData[childs.size()];
        childs.toArray(this.childs);
        Assert.check(this.childs.length != 0);
    }

    /**
     * Constructor of the {@link SequenceUpdate} class.
     *
     * @param asgn Assignment to perform.
     */
    public SequenceUpdate(AssignmentFuncStatement asgn) {
        List<UpdateData> childs = AssignmentUpdate.newAssignmentUpdate(asgn);
        this.childs = new UpdateData[childs.size()];
        childs.toArray(this.childs);
        Assert.check(this.childs.length != 0);
    }

    @Override
    public ReadWriteDeclarations collectVariableUsage(CodeContext ctxt) {
        childRWdecls = new ReadWriteDeclarations[childs.length];

        // Collect variable usage of child, and merge them for the parent.
        ReadWriteDeclarations rwDecls = null;
        for (int index = 0; index < childs.length; index++) {
            ReadWriteDeclarations childRW = childs[index].collectVariableUsage(ctxt);
            childRWdecls[index] = childRW;
            if (rwDecls == null) {
                rwDecls = new ReadWriteDeclarations(childRW);
            } else {
                rwDecls.addAll(childRW);
            }
        }
        Assert.check(rwDecls != null);
        return rwDecls;
    }

    @SuppressWarnings("null")
    @Override
    protected void addLocalCopies(Set<VariableWrapper> copiedVars) {
        // Already copied discrete and continuous variables in the environment,
        // gets expanded here while ordering our children.
        copiedVars = copy(copiedVars);

        this.copiedVars = set();

        // Construct a new order of the updates, while trying to minimize the
        // number of conflicts (avoid writing to a variable before reading it).
        //
        // Code tries to minimize conflicts while deciding each next update to
        // perform, which is not a global optimum.

        // Storage of new execution order. Previous 'childs' array is being
        // moved to this new array.
        UpdateData[] newChilds = new UpdateData[childs.length];
        for (int destIndex = 0; destIndex < childs.length; destIndex++) {
            // Find the update in 'childs' where its writing in continuous and
            // discrete variables has a low impact on other not-yet-moved childs.

            int bestChild = -1; // Index of 'childNumber' with best results (or negative for 'unknown').
            Set<VariableWrapper> leastContConflicts = null;

            for (int childNumber = 0; childNumber < childs.length; childNumber++) {
                if (childs[childNumber] == null) {
                    continue; // Child was already moved.
                }

                // Assume 'childNumber will be moved, how many conflicts will arise?
                Set<VariableWrapper> writtenVars = childRWdecls[childNumber].written;
                Set<VariableWrapper> conflicts = setc(writtenVars.size());

                // Check each other non-moved child for conflicts.
                for (int otherChild = 0; otherChild < childs.length; otherChild++) {
                    if (childs[otherChild] == null || childNumber == otherChild) {
                        continue;
                    }

                    Set<VariableWrapper> readContVars = childRWdecls[otherChild].read;
                    for (VariableWrapper var: writtenVars) {
                        if (copiedVars.contains(var)) {
                            continue; // Ignore already copied variables.
                        }
                        if (readContVars.contains(var)) {
                            conflicts.add(var);
                        }
                    }
                }

                // Take the new child if it is better.
                if (leastContConflicts == null || leastContConflicts.size() > conflicts.size()) {
                    bestChild = childNumber;
                    leastContConflicts = conflicts;
                }
            }

            // Move child to the new array.
            newChilds[destIndex] = childs[bestChild];
            childs[bestChild] = null;

            // Update the 'copied' sets.
            for (VariableWrapper var: leastContConflicts) {
                copiedVars.add(var);
                this.copiedVars.add(var);
            }
        }

        // Update 'childs', and destroy now meaningless information (since child order changed).
        childs = newChilds;
        childRWdecls = null;
        // this.copied{cont,Disc}Vars is already set correctly.

        // Push updated copied locals down to child updates. Note that
        // 'copiedVars' contains copied variables from the parent as well as all
        // locally copied variables, even if the child does not need all.
        for (int index = 0; index < childs.length; index++) {
            childs[index].addLocalCopies(copiedVars);
        }
    }

    @Override
    public void genCode(CodeBox code, boolean safeScope, CodeContext readCtxt, CodeContext writeCtxt) {
        // Add and initialize copies of the locally copied variables, updating
        // the read context for the new variables.
        ReadRenameCodeContext renamed = new ReadRenameCodeContext(readCtxt);

        int reservedRange = readCtxt.reserveTempVariables();
        CodeBox localCode = readCtxt.makeCodeBox();

        // Add locally copied variables.
        for (VariableWrapper var: copiedVars) {
            VariableInformation origInfo = readCtxt.getReadVarInfo(var);
            Assert.check(!origInfo.isTempVar); // We should not make temp copies of temp copies.

            // Construct new variable information object for the renamed variable.
            VariableInformation newInfo = writeCtxt.makeTempVariable(origInfo);
            newInfo.isTempVar = true; // Mark it as temporary copy of the real thing.
            newInfo.isReference = origInfo.isReference;
//            newInfo.readOnly = false;

            // Generate assignment to the copy.
            ExprCode readVarCode = readCtxt.getReadVariableCode(var);
            localCode.add(readVarCode.getCode());
            Destination dest = writeCtxt.makeDestination(newInfo);
            origInfo.typeInfo.declareInit(localCode, readVarCode.getRawDataValue(), dest);

            // Add renamed variable to the scope.
            renamed.addReadRename(var, newInfo);
        }

        // Call children. Scope is safe if the local scope is safe, and there is only one child.
        for (UpdateData child: childs) {
            child.genCode(localCode, safeScope && childs.length == 1, renamed, writeCtxt);
        }

        if (readCtxt.countCreatedTempVariables() > 0) {
            if (!safeScope) {
                readCtxt.addUpdatesBeginScope(code);
            }
            code.add(localCode);
            if (!safeScope) {
                readCtxt.addUpdatesEndScope(code);
            }
        } else {
            code.add(localCode);
        }
        readCtxt.unreserveTempVariables(reservedRange);
    }
}
