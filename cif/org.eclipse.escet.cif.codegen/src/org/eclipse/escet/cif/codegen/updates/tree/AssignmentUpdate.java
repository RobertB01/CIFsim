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

package org.eclipse.escet.cif.codegen.updates.tree;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.typeinfos.TupleTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.codegen.updates.FindDeclarationUsage;
import org.eclipse.escet.cif.codegen.updates.ReadWriteDeclarations;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Assignment update in the tree. */
public class AssignmentUpdate extends UpdateData {
    /** Addressable side of the assignment. */
    public final Expression addressable;

    /** Value to assign. */
    public final Expression value;

    /**
     * Constructor of the {@link AssignmentUpdate} class.
     *
     * @param addressable Addressable side of the assignment.
     * @param value Value to assign.
     */
    public AssignmentUpdate(Expression addressable, Expression value) {
        this.addressable = addressable;
        this.value = value;
    }

    @Override
    public ReadWriteDeclarations collectVariableUsage(CodeContext ctxt) {
        Set<VariableWrapper> read = set();
        Set<VariableWrapper> written = set();
        ReadWriteDeclarations rwDecls = new ReadWriteDeclarations(read, written);

        // Inspect the right hand side and left hand side to find the read and
        // written variables.
        FindDeclarationUsage.collectUse(value, read);
        FindDeclarationUsage.collectAssign(addressable, rwDecls);

        // Extend the written set with the affected algebraic and derivatives,
        // as these are written indirectly by the assignment.
        for (VariableWrapper wVar: copy(written)) {
            written.addAll(ctxt.getAffectedAlgebraicDerivativeExpressions(wVar));
        }

        return rwDecls;
    }

    @Override
    protected void addLocalCopies(Set<VariableWrapper> copiedVars) {
        // Nothing to do.
    }

    @Override
    public void genCode(CodeBox code, boolean safeScope, CodeContext readCtxt, CodeContext writeCtxt) {
        // Find the single variable assignments to perform.
        List<SingleVariableAssignment> assigns = getSingleAssignments(addressable, value.getType(), null);

        int reservedRange = readCtxt.reserveTempVariables();
        CodeBox localCode = readCtxt.makeCodeBox();

        // One assignment (thus no rhs projections), no
        if (assigns.size() == 1) {
            SingleVariableAssignment asg = assigns.get(0);
            if (asg.lhsProjections == null && !asg.needsRangeBoundCheck()) {
                // One assignment (which means no rhs projections), no lhs projection,
                // and no overflow or underflow.
                writeCtxt.performSingleAssign(localCode, assigns.get(0), value, readCtxt);

                if (readCtxt.countCreatedTempVariables() > 0) {
                    if (!safeScope) {
                        readCtxt.addUpdatesBeginScope(code);
                    }
                    code.add(localCode);
                    if (!safeScope) {
                        readCtxt.addUpdatesEndScope(code);
                    }
                } else {
                    code.add(localCode);
                }
                readCtxt.unreserveTempVariables(reservedRange);
                return;
            }
        }

        // The general case. Store rhs, then assign and perform underflow
        // and overflow checking from that value.

        // Store the rhs.
        VariableInformation tmpVar = writeCtxt.makeTempVariable(value.getType(), "rhs");
        Destination dest = writeCtxt.makeDestination(tmpVar);
        ExprCode rhsCode = readCtxt.exprToTarget(value, null);
        localCode.add(rhsCode.getCode());
        tmpVar.typeInfo.declareInit(localCode, rhsCode.getRawDataValue(), dest);

        for (SingleVariableAssignment varAsgn: assigns) {
            String rhsText = tmpVar.targetName;
            // Add rhs projections, to select a part of the value. These are
            // always tuple projections.
            if (varAsgn.rhsProjections != null) {
                TypeInfo rhsTi = tmpVar.typeInfo;
                boolean safe = false;
                for (int fieldNumber: varAsgn.rhsProjections) {
                    TupleTypeInfo tIt = (TupleTypeInfo)rhsTi;
                    rhsText = tIt.appendProjection(rhsText, safe, fieldNumber);
                    safe = true;
                }
            }
            writeCtxt.performAssign(localCode, varAsgn, rhsText, readCtxt);
        }

        if (readCtxt.countCreatedTempVariables() > 0) {
            if (!safeScope) {
                readCtxt.addUpdatesBeginScope(code);
            }
            code.add(localCode);
            if (!safeScope) {
                readCtxt.addUpdatesEndScope(code);
            }
        } else {
            code.add(localCode);
        }
        readCtxt.unreserveTempVariables(reservedRange);
    }

    /**
     * Split the destination into one or more assignments to individual assignments, possibly with left hand side or
     * right hand side projections.
     *
     * @param addressable Destination of the assignment to split.
     * @param valueType Type of the right hand side value.
     * @param projections Right hand side projections found so far. Call with {@code null}.
     * @return The found assignments to single variables, possibly with projections at both sides.
     */
    private List<SingleVariableAssignment> getSingleAssignments(Expression addressable, CifType valueType,
            List<Integer> projections)
    {
        if (addressable instanceof TupleExpression) {
            // Lhs is a literal tuple, unfold it recursively.

            if (projections == null) {
                projections = list();
            }

            TupleExpression lhsTuple = (TupleExpression)addressable;
            // Since the left hand side is a tuple, the right hand side must at least have a tuple type.
            TupleType valueTupleType = (TupleType)valueType;
            List<SingleVariableAssignment> result = list();

            int projIndex = projections.size();
            projections.add(0);
            for (int i = 0; i < lhsTuple.getFields().size(); i++) {
                projections.set(projIndex, i);
                Expression fieldValue = lhsTuple.getFields().get(i);
                CifType fieldType = valueTupleType.getFields().get(i).getType();
                result.addAll(getSingleAssignments(fieldValue, fieldType, projections));
            }
            projections.remove(projIndex);
            return result;
        } else {
            // Lhs is not a literal tuple, must be a (possibly projected) variable.
            int[] rhsProjections = null;
            if (projections != null) {
                // Copy right hand side projections.
                rhsProjections = new int[projections.size()];
                for (int i = 0; i < projections.size(); i++) {
                    rhsProjections[i] = projections.get(i);
                }
            }
            return list(new SingleVariableAssignment(addressable, valueType, rhsProjections));
        }
    }

    /**
     * Split the assignment in its elementary parts, and construct an {@link AssignmentUpdate} for each of them.
     *
     * @param asg Assignment to split.
     * @return The generated elementary assignments.
     */
    public static List<UpdateData> newAssignmentUpdate(Assignment asg) {
        List<UpdateData> updates = list();
        newAssignmentUpdate(asg.getAddressable(), asg.getValue(), updates);
        Assert.check(!updates.isEmpty());
        return updates;
    }

    /**
     * Split the assignment in its elementary parts, and construct an {@link AssignmentUpdate} for each of them.
     *
     * @param asg Assignment to split.
     * @return The generated elementary assignments.
     */
    public static List<UpdateData> newAssignmentUpdate(AssignmentFuncStatement asg) {
        List<UpdateData> updates = list();
        newAssignmentUpdate(asg.getAddressable(), asg.getValue(), updates);
        Assert.check(!updates.isEmpty());
        return updates;
    }

    /**
     * Split the assignment in its elementary parts, and construct an {@link AssignmentUpdate} for each of them.
     *
     * @param addressable Addressable side of the assignment.
     * @param value Value to assign.
     * @param updates Destination of the generated assignments, updated in-place.
     */
    private static void newAssignmentUpdate(Expression addressable, Expression value, List<UpdateData> updates) {
        // If both sides are a literal tuple, recursively split the assignment in its elements.
        if (addressable instanceof TupleExpression && value instanceof TupleExpression) {
            TupleExpression addrTuple = (TupleExpression)addressable;
            TupleExpression valTuple = (TupleExpression)value;
            for (int i = 0; i < addrTuple.getFields().size(); i++) {
                newAssignmentUpdate(addrTuple.getFields().get(i), valTuple.getFields().get(i), updates);
            }
            return;
        }

        updates.add(new AssignmentUpdate(addressable, value));
    }
}
