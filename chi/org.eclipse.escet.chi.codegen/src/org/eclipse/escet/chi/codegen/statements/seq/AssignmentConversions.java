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

package org.eclipse.escet.chi.codegen.statements.seq;

import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.dropTypeReferences;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.statements.seq.assignment.AssignmentNode;
import org.eclipse.escet.chi.codegen.statements.seq.assignment.DictionaryProjection;
import org.eclipse.escet.chi.codegen.statements.seq.assignment.LhsHead;
import org.eclipse.escet.chi.codegen.statements.seq.assignment.LhsUnpacking;
import org.eclipse.escet.chi.codegen.statements.seq.assignment.LhsVariableReference;
import org.eclipse.escet.chi.codegen.statements.seq.assignment.ListProjection;
import org.eclipse.escet.chi.codegen.statements.seq.assignment.RhsExpression;
import org.eclipse.escet.chi.codegen.statements.seq.assignment.RhsVarName;
import org.eclipse.escet.chi.codegen.statements.seq.assignment.TupleProjection;
import org.eclipse.escet.chi.metamodel.chi.AssignmentStatement;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FieldReference;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.TupleExpression;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.VariableReference;
import org.eclipse.escet.common.java.Assert;

/** Conversions for assigning values to variables. */
public class AssignmentConversions {
    /** Constructor of the {@link AssignmentConversions} class. */
    private AssignmentConversions() {
        // Static class.
    }

    /**
     * Convert the assignment statement to Java code.
     *
     * @param stat Statement to convert.
     * @param ctxt Code generator context.
     * @param currentClass Target class for the code generation.
     * @return The assignment code in the target language.
     */
    public static SeqCode convertAssignment(AssignmentStatement stat, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        List<LhsHead> asgs = makeSingleAssignments(stat.getLhs(), stat.getRhs());
        List<String> lines = generateAssignments(asgs, ctxt, currentClass);
        return new SeqCode(lines, stat);
    }

    /**
     * Convert the assignment of the rhs Java value to the lhs variables.
     *
     * @param lhs Variables to assign to.
     * @param rhsName Name of the Java variable containing the rhs value.
     * @param rhsType Type of the rhs value.
     * @param ctxt Code generator context.
     * @param currentClass Target class for the code generation.
     * @return Generated lines of code for the assignment.
     */
    public static List<String> convertAssignment(Expression lhs, String rhsName, Type rhsType,
            CodeGeneratorContext ctxt, JavaFile currentClass)
    {
        AssignmentNode rhs = new RhsVarName(rhsName, rhsType);
        List<LhsHead> asgs = list(buildAssignment(lhs, rhs));
        List<String> lines = generateAssignments(asgs, ctxt, currentClass);
        return lines;
    }

    /**
     * Decode the lhs and rhs of an assignment to the elementary assignment nodes (where the lhs or the rhs is not a
     * literal tuple that can be split into smaller assignments), and convert them to assignment node trees.
     *
     * @param lhs Left-hand side expression of the assignment.
     * @param rhs Right-hand side expression of the assignment.
     * @return The collection of elementary assignments as assignment structures.
     */
    private static List<LhsHead> makeSingleAssignments(Expression lhs, Expression rhs) {
        if ((lhs instanceof TupleExpression) && (rhs instanceof TupleExpression)) {
            List<Expression> tlf = ((TupleExpression)lhs).getFields();
            List<Expression> trf = ((TupleExpression)rhs).getFields();
            Assert.check(tlf.size() == trf.size());

            List<LhsHead> singles = list();
            for (int i = 0; i < tlf.size(); i++) {
                singles.addAll(makeSingleAssignments(tlf.get(i), trf.get(i)));
            }
            return singles;
        }
        AssignmentNode rhsAsg = new RhsExpression(rhs);
        return list(buildAssignment(lhs, rhsAsg));
    }

    /**
     * Construct an assignment node tree to assign the rhs to the lhs.
     *
     * @param lhs Lhs to assign to.
     * @param rhs Rhs value to assign (may be shared between lhs-es).
     * @return Assignment structure expressing the assignment.
     */
    private static LhsHead buildAssignment(Expression lhs, AssignmentNode rhs) {
        // Tuple unpacking at lhs.
        if (lhs instanceof TupleExpression) {
            TupleExpression tup = (TupleExpression)lhs;
            LhsUnpacking lhsUnpacked = new LhsUnpacking();
            for (Expression elm: tup.getFields()) {
                lhsUnpacked.elements.add(buildAssignment(elm, rhs));
            }
            return lhsUnpacked;
        }

        // Lhs projections.
        while (lhs instanceof BinaryExpression) {
            BinaryExpression bep = (BinaryExpression)lhs;
            Type tp = dropTypeReferences(bep.getLeft().getType());
            if (tp instanceof TupleType) {
                rhs = new TupleProjection((TupleType)tp, (FieldReference)bep.getRight(), rhs);
                lhs = bep.getLeft();
                continue;
            } else if (tp instanceof ListType) {
                rhs = new ListProjection((ListType)tp, bep.getRight(), rhs);
                lhs = bep.getLeft();
                continue;
            }
            Assert.check(tp instanceof DictType);
            rhs = new DictionaryProjection((DictType)tp, bep.getRight(), rhs);
            lhs = bep.getLeft();
            continue;
        }

        // Root variable reference.
        Assert.check(lhs instanceof VariableReference);
        VariableReference ref = (VariableReference)lhs;
        return new LhsVariableReference(ref.getVariable(), rhs);
    }

    /**
     * Generate code for the assignments.
     *
     * @param asgs Assignments to perform.
     * @param ctxt Code generator context.
     * @param currentClass Java class being generated.
     * @return Code implementing the requested assignments.
     */
    private static List<String> generateAssignments(List<LhsHead> asgs, CodeGeneratorContext ctxt,
            JavaFile currentClass)
    {
        Set<VariableDeclaration> assignedVars = set();
        for (LhsHead single: asgs) {
            single.getLhsRootVariables(assignedVars);
        }

        List<String> lines = list();
        for (LhsHead single: asgs) {
            single.saveUsedValues(single.isOneAssignment(), assignedVars, ctxt, currentClass, lines);
        }
        for (LhsHead single: asgs) {
            single.assignValue(null, ctxt, currentClass, lines);
        }

        if (!lines.isEmpty()) {
            lines.add(""); // For easier showing where the assignment ends.
        }
        return lines;
    }
}
