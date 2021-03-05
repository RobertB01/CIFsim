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
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.common.java.Assert;

/** Lhs list projection class. */
public class ListProjection extends ChainedAsgNode {
    /** Type of the list. */
    public final ListType type;

    /** Expression denoting the index of the list. */
    public final Expression indexExpr;

    /** Name of the variable holding the index value. */
    private String indexName = null;

    /** Whether initialization has been performed. */
    private boolean initDone = false;

    /**
     * Constructor of the {@link ListProjection} class.
     *
     * @param type Type of the list.
     * @param indexExpr Expression denoting the index of the list.
     * @param child Child assignment.
     */
    public ListProjection(ListType type, Expression indexExpr, AssignmentNode child) {
        super(child);
        this.type = type;
        this.indexExpr = indexExpr;
    }

    @Override
    public boolean performsFullAssignment() {
        return false;
    }

    @Override
    public void saveUsedValues(boolean oneAssignment, Set<VariableDeclaration> assigneds, CodeGeneratorContext ctxt,
            JavaFile currentClass, List<String> lines)
    {
        Assert.check(!initDone);
        initDone = true;

        Set<VariableDeclaration> useds = set();
        expressionCollectVariables(indexExpr, useds);
        if (!isEmptyIntersection(useds, assigneds)) {
            // Index value will change by assignments, calculate it beforehand.
            indexName = ctxt.makeUniqueName("index");
            ExpressionBase exprCode = convertExpression(indexExpr, ctxt, currentClass);
            exprCode.setCurrentPositionStatement(lines);
            lines.addAll(exprCode.getCode());
            lines.add(fmt("int %s = %s;", indexName, exprCode.getValue()));
        }

        child.saveUsedValues(oneAssignment, assigneds, ctxt, currentClass, lines);
    }

    @Override
    public void assignValue(String lhsVar, List<Integer> rhsIndices, CodeGeneratorContext ctxt, JavaFile currentClass,
            List<String> lines)
    {
        String indexValue = indexName;
        if (indexValue == null) {
            ExpressionBase exprCode = convertExpression(indexExpr, ctxt, currentClass);
            exprCode.setCurrentPositionStatement(lines);
            lines.addAll(exprCode.getCode());
            indexValue = exprCode.getValue();
        }

        TypeID elmTid = createTypeID(type.getElementType(), ctxt);
        String tmpName = ctxt.makeUniqueName("tmp");

        if (child.performsFullAssignment()) {
            lines.add(fmt("%s %s;", elmTid.getJavaType(), tmpName));
        } else {
            lines.add(fmt("%s %s = %s.get(%s);", elmTid.getJavaType(), tmpName, lhsVar, indexValue));
        }
        child.assignValue(tmpName, rhsIndices, ctxt, currentClass, lines);

        lines.add(fmt("%s = %s.modify(%s, %s);", lhsVar, lhsVar, indexValue, tmpName));
    }
}
