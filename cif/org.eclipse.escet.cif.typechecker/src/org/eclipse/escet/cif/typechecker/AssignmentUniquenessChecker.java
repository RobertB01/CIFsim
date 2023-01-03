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

package org.eclipse.escet.cif.typechecker;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.typechecker.scopes.FunctionScope;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Maps;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Assignment uniqueness checker. Supports both assignments on edges and assignments in functions. */
public class AssignmentUniquenessChecker {
    /** Constructor for the {@link AssignmentUniquenessChecker} class. */
    private AssignmentUniquenessChecker() {
        // Static class.
    }

    /**
     * Checks the given updates for uniqueness with respect to the (parts of the) variables that are assigned.
     *
     * @param updates The updates to check.
     * @param asgnMap Mapping from the (discrete, continuous, and function local) variables that have already been
     *     assigned so far, to all assignments that they were assigned in, with information about both the position
     *     information for the addressable variable reference (no projections), and the statically evaluated, normalized
     *     projection values that were used to address parts of the variables. Projection values may be {@code null} if
     *     they can not be statically computed or normalized. May be modified in-place.
     * @param tchecker The CIF type checker to use.
     * @param errMsg The error message to use.
     */
    public static void checkUniqueAsgns(List<Update> updates,
            Map<Declaration, Set<Pair<Position, List<Object>>>> asgnMap, CifTypeChecker tchecker, ErrMsg errMsg)
    {
        // Process the updates in order.
        for (Update update: updates) {
            // Case distinction on the type of update.
            if (update instanceof Assignment) {
                Assignment asgn = (Assignment)update;

                checkUniqueAsgns(asgn.getAddressable(), asgnMap, tchecker, errMsg);
            } else if (update instanceof IfUpdate) {
                IfUpdate ifUpd = (IfUpdate)update;
                Map<Declaration, Set<Pair<Position, List<Object>>>> asgnMapNew;
                asgnMapNew = map();

                Map<Declaration, Set<Pair<Position, List<Object>>>> thensMap;
                thensMap = Maps.copy(asgnMap);
                checkUniqueAsgns(ifUpd.getThens(), thensMap, tchecker, errMsg);
                mergeUniqueAsgnInfos(asgnMapNew, thensMap);

                Map<Declaration, Set<Pair<Position, List<Object>>>> elsesMap;
                elsesMap = Maps.copy(asgnMap);
                checkUniqueAsgns(ifUpd.getElses(), elsesMap, tchecker, errMsg);
                mergeUniqueAsgnInfos(asgnMapNew, elsesMap);

                for (ElifUpdate elifUpd: ifUpd.getElifs()) {
                    Map<Declaration, Set<Pair<Position, List<Object>>>> elifMap;
                    elifMap = Maps.copy(asgnMap);
                    checkUniqueAsgns(elifUpd.getThens(), elifMap, tchecker, errMsg);
                    mergeUniqueAsgnInfos(asgnMapNew, elifMap);
                }

                asgnMap = asgnMapNew;
            } else {
                throw new RuntimeException("Unknown update: " + update);
            }
        }
    }

    /**
     * Merges two unique assignments information mappings.
     *
     * @param first The first mapping. Is modified in-place.
     * @param second The second mapping. Is merged into the first.
     * @see #checkUniqueAsgns(List, Map, CifTypeChecker, ErrMsg)
     */
    private static void mergeUniqueAsgnInfos(Map<Declaration, Set<Pair<Position, List<Object>>>> first,
            Map<Declaration, Set<Pair<Position, List<Object>>>> second)
    {
        for (Entry<Declaration, Set<Pair<Position, List<Object>>>> se: second.entrySet()) {
            Declaration var = se.getKey();
            Set<Pair<Position, List<Object>>> sv = se.getValue();
            Set<Pair<Position, List<Object>>> fv = first.get(var);
            if (fv == null) {
                first.put(var, sv);
            } else {
                fv.addAll(sv);
            }
        }
    }

    /**
     * Checks the given addressable expression for uniqueness with respect to the (parts of the) variables that are
     * assigned.
     *
     * @param addr The addressable expression to check.
     * @param asgnMap Mapping from the (discrete, continuous, and function local) variables that have already been
     *     assigned so far, to all assignments that they were assigned in, with information about both the position
     *     information for the addressable variable reference (no projections), and the statically evaluated, normalized
     *     projection values that were used to address parts of the variables. Projection values may be {@code null} if
     *     they can not be statically computed or normalized. May be modified in-place.
     * @param tchecker The CIF type checker to use.
     * @param errMsg The error message to use.
     */
    public static void checkUniqueAsgns(Expression addr, Map<Declaration, Set<Pair<Position, List<Object>>>> asgnMap,
            CifTypeChecker tchecker, ErrMsg errMsg)
    {
        // Multi-assignment.
        if (addr instanceof TupleExpression) {
            TupleExpression taddr = (TupleExpression)addr;
            for (Expression elem: taddr.getFields()) {
                checkUniqueAsgns(elem, asgnMap, tchecker, errMsg);
            }
            return;
        }

        // (Partial) variable assignment.
        checkUniqueAsgn(addr, asgnMap, tchecker, errMsg);
    }

    /**
     * Checks the given addressable expression for uniqueness with respect to the (parts of the) variables that are
     * assigned.
     *
     * @param addr The addressable expression to check. Must not be a {@link DiscVariableExpression},
     *     {@link ContVariableExpression}, or {@link ProjectionExpression}, but not a {@link TupleExpression}.
     * @param asgnMap Mapping from the (discrete, continuous, and function local) variables that have already been
     *     assigned so far, to all assignments that they were assigned in, with information about both the position
     *     information for the addressable variable reference (no projections), and the statically evaluated, normalized
     *     projection values that were used to address parts of the variables. Projection values may be {@code null} if
     *     they can not be statically computed or normalized. May be modified in-place.
     * @param tchecker The CIF type checker to use.
     * @param errMsg The error message to use.
     * @see FunctionScope#typeCheckStatement
     */
    private static void checkUniqueAsgn(Expression addr, Map<Declaration, Set<Pair<Position, List<Object>>>> asgnMap,
            CifTypeChecker tchecker, ErrMsg errMsg)
    {
        // Get variable.
        Expression varRef = CifAddressableUtils.stripProjs(addr);
        Declaration var;
        if (varRef instanceof DiscVariableExpression) {
            var = ((DiscVariableExpression)varRef).getVariable();
        } else if (varRef instanceof ContVariableExpression) {
            var = ((ContVariableExpression)varRef).getVariable();
        } else if (varRef instanceof ReceivedExpression) {
            throw new RuntimeException("Not allowed by parser.");
        } else {
            String msg = "Unknown addr ref expr: " + varRef;
            throw new RuntimeException(msg);
        }

        // Get projections, in correct order.
        List<ProjectionExpression> projs = CifAddressableUtils.collectProjs(addr);

        // Try to statically evaluate projection indices.
        List<Object> indices = listc(projs.size());
        for (int i = 0; i < projs.size(); i++) {
            ProjectionExpression proj = projs.get(i);
            Object index = null;

            // Get index expression and normalized child type.
            Expression idxExpr = proj.getIndex();
            CifType nctype = CifTypeUtils.normalizeType(proj.getChild().getType());

            // Try to statically evaluate index.
            if (idxExpr instanceof FieldExpression) {
                // Special case for tuple field projections.
                Field field = ((FieldExpression)idxExpr).getField();
                TupleType ttype = (TupleType)field.eContainer();
                index = ttype.getFields().indexOf(field);
                Assert.check((int)index >= 0);
            } else {
                // Evaluate, if statically possible.
                if (CifExprsTypeChecker.checkStaticEvaluable(idxExpr, null)) {
                    try {
                        index = CifEvalUtils.eval(idxExpr, false);
                    } catch (CifEvalException e) {
                        tchecker.addProblem(ErrMsg.EVAL_FAILURE, e.expr.getPosition(), e.getMessage());
                        throw new SemanticException();
                    }
                }
            }

            // Handle index value, based on type of container being projected.
            if (index == null) {
                // Nothing to handle.
            } else if (nctype instanceof TupleType) {
                // Index is 0-based index already. Field refs handled above.
            } else if (nctype instanceof ListType) {
                int idx = (int)index;
                if (idx < 0) {
                    // Normalize reverse index, only for arrays.
                    ListType ltype = (ListType)nctype;
                    int lower = CifTypeUtils.getLowerBound(ltype);
                    int upper = CifTypeUtils.getUpperBound(ltype);
                    if (lower == upper) {
                        // Normalize.
                        index = lower + idx;
                    } else {
                        // Can't normalize.
                        index = null;
                    }
                }
            } else if (nctype instanceof DictType) {
                // Index is key value. No need to normalize.
            } else if (nctype instanceof StringType) {
                // Ignore assignments with projections on strings, as the type
                // checker disallows that elsewhere, but continues checking.
                return;
            } else {
                throw new RuntimeException("Unexpected proj: " + nctype);
            }

            // Add index value, or 'null' if can't eval/normalize.
            indices.add(index);
        }

        // Get other assignments to (parts of the) variable.
        Set<Pair<Position, List<Object>>> others = asgnMap.get(var);
        if (others == null) {
            others = set();
            asgnMap.put(var, others);
        }

        // Check for duplicate assignment to (part of the) variable.
        boolean reported = false;
        for (Pair<Position, List<Object>> other: others) {
            // Check for overlap.
            List<Object> otherIndices = other.right;
            int cnt = Math.max(indices.size(), otherIndices.size());
            boolean overlap = true;
            for (int i = 0; i < cnt; i++) {
                if (i >= indices.size()) {
                    // Current assignment has no index, other does.
                    break;
                } else if (i >= otherIndices.size()) {
                    // Current assignment has index, other doesn't.
                    break;
                } else {
                    // Both current and other assignment have index.
                    Object thisIdx = indices.get(i);
                    Object otherIdx = otherIndices.get(i);
                    if (thisIdx == null || otherIdx == null) {
                        // An unknown index, so potentially still overlap.
                    } else if (thisIdx.equals(otherIdx)) {
                        // Same index, so definitely still overlap.
                    } else {
                        // Different indices, so definitely no overlap.
                        overlap = false;
                        break;
                    }
                }
            }

            // Report overlap.
            if (overlap) {
                reported = true;

                String varName = CifTextUtils.getAbsName(var);
                String curTxt = varProjsToStr(varName, indices);
                String otherTxt = varProjsToStr(varName, otherIndices);
                tchecker.addProblem(errMsg, other.left, varName, otherTxt, curTxt);
                tchecker.addProblem(errMsg, varRef.getPosition(), varName, curTxt, otherTxt);
                // Non-fatal error.

                break;
            }
        }

        // If no overlap at all, add it.
        if (!reported) {
            others.add(pair(varRef.getPosition(), indices));
        }
    }

    /**
     * Converts a variable with projection values to a textual representation to use for reporting errors for duplicate
     * variable (part) assignments.
     *
     * @param varName The variable name.
     * @param idxs The projection indices. Each index may be {@code null} if it could not be statically
     *     evaluated/normalized.
     * @return The textual representation.
     */
    private static String varProjsToStr(String varName, List<Object> idxs) {
        if (idxs.isEmpty()) {
            return varName;
        }
        StringBuilder txt = new StringBuilder(varName);
        for (Object idx: idxs) {
            txt.append("[");
            txt.append((idx == null) ? "..." : CifEvalUtils.objToStr(idx));
            txt.append("]");
        }
        return txt.toString();
    }
}
