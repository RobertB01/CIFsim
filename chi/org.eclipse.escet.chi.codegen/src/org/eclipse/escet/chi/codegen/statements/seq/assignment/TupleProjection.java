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

import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.FieldReference;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.common.java.Assert;

/** Tuple projection at the lhs. */
public class TupleProjection extends ChainedAsgNode {
    /** Type of the tuple. */
    public final TupleType tupleType;

    /** Field being used for projection. */
    public final FieldReference field;

    /**
     * Constructor of the {@link TupleProjection} class.
     *
     * @param tupleType Type of the tuple.
     * @param field Field being used for projection.
     * @param child Child assignment.
     */
    public TupleProjection(TupleType tupleType, FieldReference field, AssignmentNode child) {
        super(child);
        this.tupleType = tupleType;
        this.field = field;
    }

    @Override
    public boolean performsFullAssignment() {
        return false;
    }

    @Override
    public void saveUsedValues(boolean oneAssignment, Set<VariableDeclaration> assigneds, CodeGeneratorContext ctxt,
            JavaFile currentClass, List<String> lines)
    {
        // Field does not access any variable.
        child.saveUsedValues(oneAssignment, assigneds, ctxt, currentClass, lines);
    }

    @Override
    public void assignValue(String lhsVar, List<Integer> rhsIndices, CodeGeneratorContext ctxt, JavaFile currentClass,
            List<String> lines)
    {
        int fieldIndex = tupleType.getFields().indexOf(field.getField());
        Assert.check(fieldIndex >= 0, "Cannot find tuple field.");

        TypeID varTid = createTypeID(field.getType(), ctxt);
        String tmpVar = ctxt.makeUniqueName("tmp");

        // Get assigned value from the child.
        // 'tmp = lhs.varX;'
        if (child.performsFullAssignment()) {
            lines.add(fmt("%s %s;", varTid.getJavaType(), tmpVar));
        } else {
            lines.add(fmt("%s %s = %s.var%d;", varTid.getJavaType(), tmpVar, lhsVar, fieldIndex));
        }
        child.assignValue(tmpVar, rhsIndices, ctxt, currentClass, lines);

        // Update the parent by making a shallow copy, and inserting the new value.
        // 'lhs = new Tuple(lhs, false);'
        TypeID tupTid = createTypeID(tupleType, ctxt);
        lines.add(fmt("%s = %s;", lhsVar, tupTid.getDeepCopyName(lhsVar, currentClass, false)));
        // 'lhs.varX = tmp;'
        lines.add(fmt("%s.var%d = %s;", lhsVar, fieldIndex, tmpVar));
    }
}
