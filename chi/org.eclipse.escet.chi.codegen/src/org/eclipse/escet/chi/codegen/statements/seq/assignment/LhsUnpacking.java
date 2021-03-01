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

package org.eclipse.escet.chi.codegen.statements.seq.assignment;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.common.java.Assert;

/** Class expressing unpacking of a tuple at the lhs. */
public class LhsUnpacking implements LhsHead {
    /** Elements of the tuple. */
    public List<LhsHead> elements = list();

    @Override
    public boolean isOneAssignment() {
        return false;
    }

    @Override
    public void getLhsRootVariables(Set<VariableDeclaration> assignedVars) {
        for (LhsHead element: elements) {
            element.getLhsRootVariables(assignedVars);
        }
    }

    @Override
    public void saveUsedValues(boolean oneAssignment, Set<VariableDeclaration> assigneds, CodeGeneratorContext ctxt,
            JavaFile currentClass, List<String> lines)
    {
        Assert.check(!elements.isEmpty());
        for (LhsHead element: elements) {
            element.saveUsedValues(oneAssignment, assigneds, ctxt, currentClass, lines);
        }
    }

    @Override
    public void assignValue(List<Integer> rhsIndices, CodeGeneratorContext ctxt, JavaFile currentClass,
            List<String> lines)
    {
        Assert.check(!elements.isEmpty());

        if (rhsIndices == null) {
            rhsIndices = list();
        }

        int removeIndex = rhsIndices.size();
        for (int i = 0; i < elements.size(); i++) {
            rhsIndices.add(i);
            elements.get(i).assignValue(rhsIndices, ctxt, currentClass, lines);
            rhsIndices.remove(removeIndex);
        }
    }
}
