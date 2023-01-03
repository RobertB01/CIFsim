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

import static org.eclipse.escet.common.java.Sets.setc;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.updates.ReadWriteDeclarations;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.common.box.CodeBox;

/**
 * Base class of the storage about updates or assignments.
 *
 * <p>
 * There are two users of the class, updates on an edge, and assignment in a function.
 * </p>
 *
 * <p>
 * The intended use of the object is:
 * <ol>
 * <li>Create the object tree representing the update / assignment, using {@link SequenceUpdate}.</li>
 * <li>Call {@link #collectVariableUsage} to initialize usage information of the variables in the update /
 * assignment.</li>
 * <li>Call {@link #addLocalCopies} to add local copies of variables if required.</li>
 * <li>Call {@link #genCode} to generate code.</li>
 * <li>???</li>
 * <li>Profit!</li>
 * </ol>
 * </p>
 */
public abstract class UpdateData {
    /**
     * Perform bottom-up traversal of the update tree, collecting read and written variables from the sub-trees.
     *
     * @param ctxt Code generation context.
     * @return Cumulative read and written variables of the update node.
     */
    public abstract ReadWriteDeclarations collectVariableUsage(CodeContext ctxt);

    /** Introduce copies of conflicting variables at each level of updates. */
    public void addLocalCopies() {
        addLocalCopies(setc(0));
    }

    /**
     * Introduce copies of conflicting variables at each level of updates.
     *
     * @param copiedVars Variables that are already copied (at a higher level).
     */
    protected abstract void addLocalCopies(Set<VariableWrapper> copiedVars);

    /**
     * Generate code for the update.
     *
     * @param code Code storage for the generated code.
     * @param safeScope If set, the code environment is a scope that may be used for adding temporary variables.
     * @param readCtxt Context of the update code for reading variables.
     * @param writeCtxt Context of the update code for writing variables.
     */
    public abstract void genCode(CodeBox code, boolean safeScope, CodeContext readCtxt, CodeContext writeCtxt);

    /**
     * Generate an assignment statement in a function.
     *
     * @param asgn Assignment to perform.
     * @param code Storage for generated code (updated in-place).
     * @param safeScope If set, the code environment is a scope that may be used for adding temporary variables.
     * @param ctxt Code generation context.
     */
    public static void generateAssignment(AssignmentFuncStatement asgn, CodeBox code, boolean safeScope,
            CodeContext ctxt)
    {
        UpdateData update = new SequenceUpdate(asgn);
        update.collectVariableUsage(ctxt);
        update.addLocalCopies();
        update.genCode(code, safeScope, ctxt, ctxt);
    }

    /**
     * Generate assignment statements as part of execution of an edge.
     *
     * @param updates Updates to perform.
     * @param code Storage for generated code (updated in-place).
     * @param ctxt Code generation context.
     */
    public static void generateAssignment(List<Update> updates, CodeBox code, CodeContext ctxt) {
        UpdateData updateTree = new SequenceUpdate(updates);
        updateTree.collectVariableUsage(ctxt);
        updateTree.addLocalCopies();
        updateTree.genCode(code, false, ctxt, ctxt);
    }
}
