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

package org.eclipse.escet.cif.codegen.updates.tree;

import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.checkTypeCompat;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.typeinfos.TupleTypeInfo;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.java.Assert;

/**
 * Assignment to a single variable, as part of a multi-assignment, possibly with projections at the left and right side.
 *
 * <p>
 * Projections at the right hand side originate from multi-assignments at the left (and not at the right, or the
 * assignment would be split further). Example:
 *
 * <pre>
 *     ((a, b), c) = x + 1;
 * </pre>
 *
 * It becomes:
 *
 * <pre>
 *     p = x + 1;
 *     a = p.0.0;
 *     b = p.0.1;
 *     c = p.1;
 * </pre>
 *
 * The projections at the right hand side are always constant non-negative integer numbers. These indices are stored in
 * {@link #rhsProjections}
 * </p>
 *
 * <p>
 * Projections at the left hand side originate from partial assignment to the variable. The type checker guarantees that
 * the same part is never assigned more than once in one statement. These indices are stored in {@link #rhsProjections}.
 * </p>
 */
public class SingleVariableAssignment {
    /**
     * Left hand side variable being (possibly partly) assigned, {@link #lhsProjections} must be applied before
     * assigning.
     */
    public final Declaration variable;

    /** Type of the assigned variable. */
    public final CifType variableType;

    /** Type of the left hand side being assigned to. */
    private final CifType addressableType;

    /**
     * Projections of the variable to select the sub-value being assigned. {@code null} if the entire variable is
     * assigned.
     */
    public final LhsProjection[] lhsProjections;

    /** Projections to use on the right hand side if any, {@code null} if there are no right hand side projections. */
    public final int[] rhsProjections;

    /** Type of the right hand side after applying right hand side projections. */
    public final CifType valueType;

    /**
     * Constructor of the {@link SingleVariableAssignment} class.
     *
     * @param addressable Left hand side addressable to assign to.
     * @param valueType Type of the right hand side value.
     * @param rhsProjections Projections to use on the right hand side if any. {@code null} if no projections are
     *     needed.
     */
    public SingleVariableAssignment(Expression addressable, CifType valueType, int[] rhsProjections) {
        lhsProjections = getLhsProjections(addressable);
        variable = getFullVariable(addressable);
        variableType = getFullVariableType(addressable);
        this.rhsProjections = rhsProjections;

        addressableType = addressable.getType();
        this.valueType = valueType;
    }

    /**
     * Perform the assignment of the right hand side value into the given left hand side destination, after applying the
     * right hand side projections. The code assumes the left hand side projections are already performed if necessary.
     *
     * @param lhs Left hand side destination.
     * @param rhsValue Unprojected right hand side value to assign.
     * @param rhsTi Type information of the right hand side.
     * @param readCtxt Code context for reading.
     * @return The generated assignment.
     */
    protected ExprCode doAssignment(Destination lhs, ExprCode rhsValue, TupleTypeInfo rhsTi, CodeContext readCtxt) {
        Assert.check(rhsValue.hasDataValue()); // Right hand side should be a value.
        if (rhsProjections != null) {
            int lastProj = rhsProjections.length - 1;
            for (int projIndex = 0; projIndex < rhsProjections.length; projIndex++) {
                rhsValue = rhsTi.getProjectedValue(rhsValue, rhsProjections[projIndex], null, readCtxt);
                if (projIndex < lastProj) {
                    // Next iteration also performs a tuple index operation.
                    rhsTi = (TupleTypeInfo)rhsTi.childInfos[rhsProjections[projIndex]];
                } else {
                    rhsTi = null; // Clear to ensure it cannot be used.
                    break; // Make Java happy about false positive null reference access.
                }
            }
        }

        ExprCode result = new ExprCode();
        result.add(rhsValue);
        result.setDestination(lhs);
        result.setDataValue(rhsValue.getRawDataValue());
        return result;
    }

    /**
     * Get the type of the left hand side that is assigned to.
     *
     * @return Type of the left hand side that is assigned to.
     */
    public CifType getAssignedType() {
        return addressableType;
    }

    /**
     * Retrieve whether the right hand side may violate range bound conditions on integers (since lists have fixed size
     * they cannot partly overlap).
     *
     * @return Whether a range check on (a part of) the assigned value is needed.
     */
    public boolean needsRangeBoundCheck() {
        // If entirely contained, no need for checking.
        return !checkTypeCompat(getAssignedType(), valueType, RangeCompat.CONTAINED);
    }

    /**
     * Unfold the left hand side projections, and return them in the order to get from the complete variable to the
     * assigned sub-value. Lack of projections is denoted by a {@code null} return value.
     *
     * @param lhs Left hand side to unfold.
     * @return The sequence of projections to get from the complete variable to its assigned sub-value, or {@code null}
     *     if no projections are found.
     */
    private static LhsProjection[] getLhsProjections(Expression lhs) {
        if (lhs instanceof ProjectionExpression) {
            List<LhsProjection> reverseProjs = list();
            while (lhs instanceof ProjectionExpression) {
                ProjectionExpression projExpr = (ProjectionExpression)lhs;
                Expression indexExpr = projExpr.getIndex();
                lhs = projExpr.getChild();
                CifType containerType = normalizeType(lhs.getType());
                if (containerType instanceof TupleType) {
                    reverseProjs.add(new LhsTupleProjection((TupleType)containerType, indexExpr));
                } else {
                    Assert.check(containerType instanceof ListType); // Pre-condition of the code generator.
                    reverseProjs.add(new LhsListProjection((ListType)containerType, indexExpr));
                }
            }
            LhsProjection[] projections = new LhsProjection[reverseProjs.size()];
            int last = reverseProjs.size() - 1;
            for (int i = 0; i < reverseProjs.size(); i++) {
                projections[i] = reverseProjs.get(last - i);
            }
            return projections;
        } else {
            // Left hand side is a plain variable.
            return null;
        }
    }

    /**
     * Get the variable that is eventually assigned.
     *
     * @param lhs Left hand side expression.
     * @return Variable declaration of the assigned variable.
     */
    private static Declaration getFullVariable(Expression lhs) {
        // Unwrap and discard the projections.
        while (lhs instanceof ProjectionExpression) {
            ProjectionExpression projExpr = (ProjectionExpression)lhs;
            lhs = projExpr.getChild();
        }

        // Convert to a variable declaration.
        if (lhs instanceof ContVariableExpression) {
            ContVariableExpression varExpr = (ContVariableExpression)lhs;
            return varExpr.getVariable();
        } else if (lhs instanceof DiscVariableExpression) {
            DiscVariableExpression varExpr = (DiscVariableExpression)lhs;
            return varExpr.getVariable();
        }
        throw new RuntimeException("Unexpected type of lhs variable encountered: " + str(lhs));
    }

    /**
     * Get the type of the variable that is eventually assigned.
     *
     * @param lhs Left hand side expression.
     * @return Type of the assigned variable.
     */
    private static CifType getFullVariableType(Expression lhs) {
        // Unwrap and discard the projections.
        while (lhs instanceof ProjectionExpression) {
            ProjectionExpression projExpr = (ProjectionExpression)lhs;
            lhs = projExpr.getChild();
        }

        return normalizeType(lhs.getType());
    }

    @Override
    public String toString() {
        String s = "SingleVarAsg var    : " + variable.getName() + "\n";
        String t = "addressable-type    : " + typeToStr(addressableType) + "\n";
        String u;
        if (lhsProjections == null) {
            u = "No lhs projections\n";
        } else {
            u = fmt("lhs-projections (#%d): ", lhsProjections.length);
            boolean first = true;
            for (LhsProjection lp: lhsProjections) {
                if (!first) {
                    u += " -> ";
                }
                first = false;
                u += lp.toString();
            }
            u += "\n";
        }
        String v = "rhs-type            : " + typeToStr(valueType) + "\n";
        String w;
        if (rhsProjections == null) {
            w = "No rhs projections";
        } else {
            w = fmt("rhs-projections (#%d):   ", rhsProjections.length);
            for (int rp: rhsProjections) {
                w += fmt("[%d]", rp);
            }
        }
        return s + t + u + v + w;
    }
}
