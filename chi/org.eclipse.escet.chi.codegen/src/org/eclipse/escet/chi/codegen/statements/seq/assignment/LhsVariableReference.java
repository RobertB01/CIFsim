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

package org.eclipse.escet.chi.codegen.statements.seq.assignment;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;

/**
 * Class holding the lhs root variable of the assignment. The node also forms the bridge from {@link LhsHead} to
 * {@link AssignmentNode}.
 */
public class LhsVariableReference implements LhsHead {
    /** Left-hand side variable to assign. */
    public final VariableDeclaration lhsVar;

    /** Assignment to perform. */
    public final AssignmentNode assignment;

    /**
     * Constructor of the {@link LhsVariableReference} class.
     *
     * @param lhsVar Left-hand side variable to assign.
     * @param assignment Assignment to perform.
     */
    public LhsVariableReference(VariableDeclaration lhsVar, AssignmentNode assignment) {
        this.lhsVar = lhsVar;
        this.assignment = assignment;
    }

    @Override
    public boolean isOneAssignment() {
        return true;
    }

    @Override
    public void getLhsRootVariables(Set<VariableDeclaration> assignedVars) {
        assignedVars.add(lhsVar);
    }

    @Override
    public void saveUsedValues(boolean oneAssignment, Set<VariableDeclaration> assigneds, CodeGeneratorContext ctxt,
            JavaFile currentClass, List<String> lines)
    {
        assignment.saveUsedValues(oneAssignment, assigneds, ctxt, currentClass, lines);
    }

    @Override
    public void assignValue(List<Integer> rhsIndices, CodeGeneratorContext ctxt, JavaFile currentClass,
            List<String> lines)
    {
        String varName = ctxt.getDefinition(lhsVar);
        assignment.assignValue(varName, rhsIndices, ctxt, currentClass, lines);
    }
}
