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

import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.statements.seq.assignment.AssignmentFunctions.expressionCollectVariables;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;

/** Assignment class that assigns the rhs value to the lhs variable. */
public class RhsExpression implements AssignmentNode {
    /** Name of the variable containing the rhs value, or {@code null} if it is not created yet. */
    private String rhsVar = null;

    /** Flag whether initialization has been performed. */
    private boolean initDone = false;

    /** Rhs expression to assign. */
    public final Expression rhs;

    /**
     * Constructor of the {@link RhsExpression} class.
     *
     * @param rhs Right-hand-side of the assignment.
     */
    public RhsExpression(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public boolean performsFullAssignment() {
        return true;
    }

    @Override
    public void saveUsedValues(boolean oneAssignment, Set<VariableDeclaration> assigneds, CodeGeneratorContext ctxt,
            JavaFile currentClass, List<String> lines)
    {
        if (initDone) {
            return;
        }
        initDone = true; // Skip second and further calls.

        boolean needGenerate = !oneAssignment; // More than one assignment -> Create intermediate variable.

        if (!needGenerate) { // Check if an assigned variable is also used.
            Set<VariableDeclaration> useds = set();
            expressionCollectVariables(rhs, useds);
            needGenerate = !isEmptyIntersection(useds, assigneds);
        }

        if (needGenerate) {
            rhsVar = ctxt.makeUniqueName("rhs");
            ExpressionBase v = convertExpression(rhs, ctxt, currentClass);
            v.setCurrentPositionStatement(lines);
            lines.addAll(v.getCode());
            TypeID tid = createTypeID(rhs.getType(), ctxt);
            lines.add(fmt("%s %s = %s;", tid.getJavaType(), rhsVar, v.getValue()));
        }
    }

    @Override
    public void assignValue(String lhsVar, List<Integer> rhsIndices, CodeGeneratorContext ctxt, JavaFile currentClass,
            List<String> lines)
    {
        String valName = rhsVar;
        if (valName == null) {
            ExpressionBase v = convertExpression(rhs, ctxt, currentClass);
            v.setCurrentPositionStatement(lines);
            lines.addAll(v.getCode());
            valName = "(" + v.getValue() + ")";
        }

        lines.add(fmt("%s = %s;", lhsVar, AssignmentFunctions.makeTupleSelection(valName, rhsIndices)));
    }
}
