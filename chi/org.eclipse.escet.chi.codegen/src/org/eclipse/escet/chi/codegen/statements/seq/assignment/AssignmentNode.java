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

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;

/**
 * A node in an assignment.
 *
 * <p>
 * A Chi assignment may perform both packing and unpacking at the same time. These cases are unfolded to a sequence of
 * (partial) assignments first. An assignment that cannot be unfolded has a lhs or a rhs that is not a literal tuple.
 * Such an assignment must be performed together (either the rhs is a single value, or the lhs must be assigned as a
 * single value). To generate code for such an assignment, it is first expanded into a set of {@link AssignmentNode}
 * objects.
 * </p>
 *
 * <p>
 * Available nodes are:
 * <ul>
 * <li>Tuple unpacking nodes to distribute the assignment over multiple variables.</li>
 * <li>Root lhs variables, variables that are actually changed in the assignment.</li>
 * <li>Lhs projection variables to select a part of the lhs variable to modify.</li>
 * <li>Rhs values.</li>
 * </ul>
 * The tuple unpacking and root lhs nodes implement {@link LhsHead}, while the projection lhs and the rhs nodes
 * implement {@link AssignmentNode}. Both types of nodes are however very similar.
 * </p>
 *
 * <p>
 * The nodes from the above list are chained from left to right. The core method call is {@link #assignValue} which
 * expresses 'generate code to assign the value to the variable'. The tuple unpack nodes may select a part of the rhs
 * value to assign. (This implies the rhs value at the right may get several of these calls, each time for a different
 * part of the value.)
 * </p>
 *
 * <p>
 * A rhs node will provide a value for the provided variable, while a projection node will only assign a part of it. For
 * the latter case the provided variable needs to be filled, while in the former case there is no need to do that. The
 * {@link #performsFullAssignment} call provides information what the node will do.
 * </p>
 *
 * <p>
 * Since lhs root variables may be used in projection indices or in the rhs value, the projection indices and the rhs
 * must be 'rescued' first. Current approach is to compute all of them beforehand, but there is room for further
 * improvements here. The {@link #saveUsedValues} method is called before {@link #assignValue} to give nodes the chance
 * to take care of it.
 * </p>
 */
public interface AssignmentNode {
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
     * Return whether the node fully assigns the provided lhs in {@link #assignValue}.
     *
     * @return {@code true} if the provided variable is fully written, else {@code false}.
     */
    public boolean performsFullAssignment();

    /**
     * Generate code that assigns the rhs part indicated by the indices, to the provided lhs variable.
     *
     * @param lhsVar Variable to assign to.
     * @param rhsIndices Indices for selecting part of the rhs, {@code null} means 'entire rhs'.
     * @param ctxt Code generator context.
     * @param currentClass Current java class.
     * @param lines Collected lines of code (appended in-place).
     */
    public void assignValue(String lhsVar, List<Integer> rhsIndices, CodeGeneratorContext ctxt, JavaFile currentClass,
            List<String> lines);
}
