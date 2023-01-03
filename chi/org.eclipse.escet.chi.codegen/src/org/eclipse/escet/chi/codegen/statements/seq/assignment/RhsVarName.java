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

package org.eclipse.escet.chi.codegen.statements.seq.assignment;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;

/** Rhs already assigned to a Java variable. */
public class RhsVarName implements AssignmentNode {
    /** Name of the already assigned rhs variable. */
    public final String varName;

    /** Type of the full rhs. */
    public final Type type;

    /**
     * Constructor of the {@link RhsVarName} class.
     *
     * @param varName Name of the already assigned rhs variable.
     * @param type Type of the full rhs.
     */
    public RhsVarName(String varName, Type type) {
        this.varName = varName;
        this.type = type;
    }

    @Override
    public boolean performsFullAssignment() {
        return true;
    }

    @Override
    public void saveUsedValues(boolean oneAssignment, Set<VariableDeclaration> assigneds, CodeGeneratorContext ctxt,
            JavaFile currentClass, List<String> lines)
    {
        // Nothing to save.
    }

    @Override
    public void assignValue(String lhsVar, List<Integer> rhsIndices, CodeGeneratorContext ctxt, JavaFile currentClass,
            List<String> lines)
    {
        lines.add(fmt("%s = %s;", lhsVar, AssignmentFunctions.makeTupleSelection(varName, rhsIndices)));
    }
}
