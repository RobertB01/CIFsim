//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

/** Base class of an lhs part of an assignment of a value. */
public interface LhsHead {
    /**
     * Is this assignment changing exactly one variable (that is, does it have a single lhs root variable)?
     *
     * <p>
     * One variable implies that the rhs is computed exactly one time.
     * </p>
     *
     * @return Whether this assignment changes one variable.
     */
    public boolean isOneAssignment();

    /**
     * Collect the variables that are changed as a result of this assignment.
     *
     * @param assignedVars The variables changed by this assignment, modified in-place.
     */
    public void getLhsRootVariables(Set<VariableDeclaration> assignedVars);

    /**
     * Save the values used in the assignment (rhs, projections indices) to temporary variables.
     *
     * @param oneAssignment Whether the rhs will be needed exactly one time.
     * @param assigneds Assigned variables by this assignment.
     * @param ctxt Code generator context.
     * @param currentClass Current java class.
     * @param lines Collected lines of code (appended in-place).
     */
    public void saveUsedValues(boolean oneAssignment, Set<VariableDeclaration> assigneds, CodeGeneratorContext ctxt,
            JavaFile currentClass, List<String> lines);

    /**
     * Generate code that assigns the rhs part indicated by the indices, to a variable.
     *
     * @param rhsIndices Indices for selecting part of the rhs, {@code null} means 'entire rhs'.
     * @param ctxt Code generator context.
     * @param currentClass Current java class.
     * @param lines Collected lines of code (appended in-place).
     */
    public void assignValue(List<Integer> rhsIndices, CodeGeneratorContext ctxt, JavaFile currentClass,
            List<String> lines);
}
