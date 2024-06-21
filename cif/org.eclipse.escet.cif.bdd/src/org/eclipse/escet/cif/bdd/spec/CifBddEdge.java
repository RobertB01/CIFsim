//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.bdd.spec;

import static org.eclipse.escet.cif.bdd.utils.BddUtils.bddToStr;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.bdd.conversion.CifBddBitVector;
import org.eclipse.escet.cif.bdd.utils.BddUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Strings;

import com.github.javabdd.BDD;
import com.github.javabdd.BDDVarSet;

/** A CIF/BDD edge. Represents an edge of a linearized CIF specification in a BDD representation. */
public class CifBddEdge {
    /** The CIF/BDD specification that contains this edge. */
    public final CifBddSpec cifBddSpec;

    /**
     * The linearized CIF edges that corresponds to this CIF/BDD edge. Contains a {@code null} value for edges created
     * for input variables. There is always at least one edge (or {@code null}). If there are multiple edges, then this
     * CIF/BDD edge represents the disjunction of multiple linearized edges.
     */
    public List<Edge> edges;

    /** The event on the edge. */
    public Event event;

    /** The original guard of the edge. */
    public BDD origGuard;

    /**
     * The current guard of the edge. May be different from {@link #origGuard} if it is changed after the conversion of
     * the CIF specification to the CIF/BDD representation. This guard prevents the edge from being taken from states
     * where taking the edge would lead to runtime errors, i.e., this guard implies 'not {@link #error}'.
     */
    public BDD guard;

    /** Per {@link #edges edge}, the CIF assignments that are applied by this CIF/BDD edge. */
    public List<List<Assignment>> assignments;

    /** The set of variables that are being assigned on this CIF/BDD edge. */
    public final Set<CifBddVariable> assignedVariables = set();

    /**
     * The update predicate that relates old and new values of variables, indicating which combinations of old and new
     * values are allowed by these assignments. Is {@code null} if not available.
     */
    public BDD update;

    /** Precomputed '{@link #guard} and {@link #update}'. Is {@code null} if not available. */
    public BDD updateGuard;

    /** Precomputed BDD variable support for {@link #updateGuard}. Is {@code null} if not available. */
    private BDDVarSet updateGuardSupport;

    /**
     * The runtime error predicate. Indicates the states prior to taking the edge will result in a runtime error when
     * taking the edge.
     *
     * <p>
     * Runtime errors include assignments leading to values outside of the BDD-representable ranges, division by zero,
     * etc. Runtime errors may or may not include assignments leading to values that are outside of the valid CIF range,
     * which can still be represented by BDDs, as those situations are also taken care of by the range invariants.
     * </p>
     */
    public BDD error;

    /**
     * Constructor for the {@link CifBddEdge} class.
     *
     * @param cifBddSpec The CIF/BDD specification that contains this edge.
     */
    public CifBddEdge(CifBddSpec cifBddSpec) {
        this.cifBddSpec = cifBddSpec;
    }

    /**
     * Global edge initialization for {@link #apply applying} the edge. Must be invoked only once per edge. Must be
     * invoked before any invocation of {@link #apply}. If the {@link #guard} is changed after invoking this method,
     * {@link #reinitApply} must be invoked to re-initialize the edge for applying, unless {@link #cleanupApply} has
     * already been invoked.
     */
    public void initApply() {
        // We can include the guard in the update, assuming it won't change anymore. That is, the guard may differ
        // from the uncontrolled system guard as preparations for state/event exclusion invariants for edges with
        // controllable events may have changed it, etc. But during the actual computations on the CIF/BDD
        // specification, it shouldn't change. If the edge guard does change, the edge must be re-initialized for
        // application.
        //
        // For instance, during the actual synthesis the guard won't change. It could then still change again
        // afterwards, after synthesis has completed and the controlled system guards are determined.
        Assert.check(update != null);
        Assert.check(updateGuard == null);
        updateGuard = update.and(guard);
        update.free();
        update = null;

        // Precompute the BDD variable support for the 'updateGuard' relation.
        Assert.check(updateGuardSupport == null);
        updateGuardSupport = getSupportFor(updateGuard);
    }

    /**
     * Returns the variable support for the given relation, including all variables that are assigned on this edge.
     *
     * @param relation The relation for which to compute the BDD variable support.
     * @return The BDD variable support for the given relation.
     */
    private BDDVarSet getSupportFor(BDD relation) {
        return assignedVariables.stream().map(v -> v.domainNew.set()).reduce(relation.support(), BDDVarSet::unionWith);
    }

    /**
     * Global edge re-initialization. Edges must be reinitialized when the guards have been updated after
     * {@link #initApply} was invoked.
     */
    public void reinitApply() {
        // Update the 'updateGuard' relation.
        Assert.check(update == null);
        Assert.check(updateGuard != null);
        BDD updateGuardNew = updateGuard.and(guard);
        updateGuard.free();
        updateGuard = updateGuardNew;

        // Update the BDD variable support for the 'updateGuard' relation.
        Assert.check(updateGuardSupport != null);
        updateGuardSupport.free();
        updateGuardSupport = getSupportFor(updateGuard);
    }

    /**
     * Global edge cleanup for no longer {@link #apply applying} the edge. Must be invoked after {@link #initApply}. May
     * be invoked more than once.
     */
    public void cleanupApply() {
        Assert.check(update == null);

        updateGuard = BddUtils.free(updateGuard);
        updateGuardSupport = BddUtils.free(updateGuardSupport);
    }

    /** Free all BDDs of this CIF/BDD edge. */
    public void freeBDDs() {
        origGuard = BddUtils.free(origGuard);
        guard = BddUtils.free(guard);
        update = BddUtils.free(update);
        updateGuard = BddUtils.free(updateGuard);
        updateGuardSupport = BddUtils.free(updateGuardSupport);
        error = BddUtils.free(error);
    }

    /**
     * Applies the assignments of the edge, to a given predicate. The assignments can be applied forward (normally) or
     * backward (reversed). This method may only be invoked after having already invoked {@link #initApply}, and
     * possibly any number of times {@link #reinitApply}. It may no longer be invoked after having applied
     * {@link #cleanupApply}.
     *
     * @param pred The predicate to which to apply the assignments. This predicate is {@link BDD#free freed} by this
     *     method.
     * @param forward Whether to apply forward ({@code true}) or backward ({@code false}).
     * @param restriction The predicate that indicates the upper bound on the reached states. That is, restrict the
     *     result to these states. May be {@code null} to not impose a restriction, which is semantically equivalent to
     *     providing 'true'.
     * @return The resulting predicate.
     */
    public BDD apply(BDD pred, boolean forward, BDD restriction) {
        BDD rslt;

        if (forward) {
            // rslt = Exists{x, y, z, ...}(guard && update && pred)[x/x+, y/y+, z/z+, ...] && restriction.
            if (restriction == null) {
                rslt = updateGuard.relnext(pred, updateGuardSupport);
            } else {
                rslt = updateGuard.relnextIntersection(pred, restriction, updateGuardSupport);
            }
        } else {
            // rslt = Exists{x+, y+, z+, ...}(guard && update && pred[x+/x, y+/y, z+/z, ...]) && restriction.
            if (restriction == null) {
                rslt = updateGuard.relprev(pred, updateGuardSupport);
            } else {
                rslt = updateGuard.relprevIntersection(pred, restriction, updateGuardSupport);
            }
        }

        pred.free();
        return rslt;
    }

    /**
     * Returns a textual representation of the CIF/BDD edge.
     *
     * @return The textual representation.
     */
    @Override
    public String toString() {
        return toString(0, "Edge: ");
    }

    /**
     * Returns a textual representation of the CIF/BDD edge.
     *
     * @param indent The indentation level.
     * @param prefix The prefix to use, e.g. {@code "Edge: "} or {@code ""}.
     * @return The textual representation.
     */
    public String toString(int indent, String prefix) {
        return toString(indent, prefix, false);
    }

    /**
     * Returns a textual representation of the CIF/BDD edge.
     *
     * @param indent The indentation level.
     * @param prefix The prefix to use, e.g. {@code "Edge: "} or {@code ""}.
     * @param includeOnlyOrigGuard Whether to include only the {@link #origGuard original edge guard}, or also the
     *     {@link #guard current edge guard}.
     * @return The textual representation.
     */
    public String toString(int indent, String prefix, boolean includeOnlyOrigGuard) {
        StringBuilder txt = new StringBuilder();
        txt.append(Strings.duplicate(" ", 2 * indent));
        txt.append(prefix);
        txt.append(fmt("(event: %s)", CifTextUtils.getAbsName(event)));

        String guardsTxt = bddToStr(origGuard, cifBddSpec);
        if (!includeOnlyOrigGuard && !origGuard.equals(guard)) {
            guardsTxt = fmt("%s -> %s", guardsTxt, bddToStr(guard, cifBddSpec));
        }

        txt.append(fmt(" (guard: %s)", guardsTxt));
        if (assignments.stream().anyMatch(as -> !as.isEmpty())) {
            txt.append(" (assignments: ");
            for (int i = 0; i < assignments.size(); i++) {
                if (i > 0) {
                    txt.append(" / ");
                }
                List<Assignment> edgeAssignments = assignments.get(i);
                if (edgeAssignments.isEmpty()) {
                    txt.append("none");
                } else {
                    for (int j = 0; j < edgeAssignments.size(); j++) {
                        if (j > 0) {
                            txt.append(", ");
                        }
                        Assignment asgn = edgeAssignments.get(j);
                        txt.append(assignmentToString(asgn));
                    }
                }
            }
            txt.append(")");
        }
        return txt.toString();
    }

    /**
     * Converts an assignment to an end user readable textual representation.
     *
     * @param asgn The assignment.
     * @return The end user readable textual representation.
     */
    private String assignmentToString(Assignment asgn) {
        Expression addr = asgn.getAddressable();
        Declaration addrVar = (Declaration)CifScopeUtils.getRefObjFromRef(addr);
        Expression rhs = asgn.getValue();
        for (CifBddVariable var: cifBddSpec.variables) {
            // Skip if precondition violation (conversion failure). Should not
            // occur here once conversion has finished, but check may be useful
            // when debugging conversion code.
            if (var == null) {
                continue;
            }

            // Case distinction based on kind of addressable variable.
            if (var instanceof CifBddDiscVariable) {
                // Check for match with addressable.
                CifBddDiscVariable cifBddDiscVar = (CifBddDiscVariable)var;
                if (cifBddDiscVar.var != addrVar) {
                    continue;
                }

                // Assignment from the original CIF model.
                return fmt("%s := %s", cifBddDiscVar.name, CifTextUtils.exprToStr(rhs));
            } else if (var instanceof CifBddLocPtrVariable) {
                // Check for match with addressable.
                CifBddLocPtrVariable cifBddLpVar = (CifBddLocPtrVariable)var;
                if (cifBddLpVar.var != addrVar) {
                    continue;
                }

                // Location pointer assignment.
                int locIdx = ((IntExpression)rhs).getValue();
                Location loc = cifBddLpVar.aut.getLocations().get(locIdx);
                return fmt("%s := %s", cifBddLpVar.name, CifTextUtils.getAbsName(loc));
            } else if (var instanceof CifBddInputVariable) {
                // Check for match with addressable.
                CifBddInputVariable cifBddInputVar = (CifBddInputVariable)var;
                if (cifBddInputVar.var != addrVar) {
                    continue;
                }

                // Input variable edge. No right hand side, as this is not a
                // 'normal' assignment.
                return fmt("%s+ != %s", cifBddInputVar.name, cifBddInputVar.name);
            } else {
                String msg = "Unexpected CIF/BDD variable for addressable: " + var;
                throw new RuntimeException(msg);
            }
        }
        throw new RuntimeException("No CIF/BDD variable found for addressable: " + addrVar);
    }

    /**
     * Merges two CIF/BDD edges for the same event, from the same CIF/BDD specification. The result is a single merged
     * edge that is the disjunction of the two edges. The edges being merged are no longer valid edges afterwards, and
     * any BDD instances they held will have been freed.
     *
     * @param edge1 The first edge to merge. Is modified in-place.
     * @param edge2 The second edge to merge. Is modified in-place.
     * @return The merged edge.
     */
    public static CifBddEdge mergeEdges(CifBddEdge edge1, CifBddEdge edge2) {
        // Ensure we merge edges for the same event, in the same CIF/BDD specification.
        Assert.areEqual(edge1.cifBddSpec, edge2.cifBddSpec);
        Assert.areEqual(edge1.event, edge2.event);
        Assert.check(!edge1.edges.contains(null)); // Input variables only have one edge, so they can't be merged.
        Assert.check(!edge2.edges.contains(null)); // Input variables only have one edge, so they can't be merged.

        // Create new CIF/BDD edge.
        CifBddEdge mergedEdge = new CifBddEdge(edge1.cifBddSpec);
        mergedEdge.event = edge1.event;

        // Merge the edges and assignments.
        mergedEdge.edges = concat(edge1.edges, edge2.edges);
        mergedEdge.assignments = concat(edge1.assignments, edge2.assignments);

        // Add all necessary unchanged variable predicates 'x+ = x' to both edges, to allow their updates to be merged.
        addUnchangedVariablePredicates(edge1, edge2);
        addUnchangedVariablePredicates(edge2, edge1);

        // Merge updates. The updates need to be made conditional on their original guards, to ensure that the updates
        // of the correct original edge are applied when the guard of that original edge holds. To support
        // non-determinism, if both guards hold, either of the updates may be applied. If neither of the guards holds,
        // we do not care what the update is, as the guard and update predicates are always combined before the edge is
        // applied. So, create: '(guard1 and update1) or (guard2 and update2)'.
        BDD update1 = edge1.guard.id().andWith(edge1.update);
        BDD update2 = edge2.guard.id().andWith(edge2.update);
        mergedEdge.update = update1.orWith(update2);

        // Merge the sets of assigned variables.
        mergedEdge.assignedVariables.addAll(edge1.assignedVariables);
        mergedEdge.assignedVariables.addAll(edge2.assignedVariables);
        edge1.assignedVariables.clear();
        edge2.assignedVariables.clear();

        // Merge errors. The errors are combined in a similar way as the updates.
        BDD error1 = edge1.origGuard.id().andWith(edge1.error);
        BDD error2 = edge2.origGuard.id().andWith(edge2.error);
        mergedEdge.error = error1.orWith(error2);

        // Merge guards.
        mergedEdge.origGuard = edge1.origGuard.orWith(edge2.origGuard);
        mergedEdge.guard = edge1.guard.orWith(edge2.guard);

        // Return the merged edge.
        return mergedEdge;
    }

    /**
     * Adds unchanged variable predicates 'x+ = x' to the update of {@code edge1} for every variable 'x' that is being
     * assigned on {@code edge2} but not on {@code edge1}.
     *
     * @param edge1 The first edge, whose {@link #update} may be modified in-place.
     * @param edge2 The second edge, which is used to determine all unchanged variable predicates.
     */
    private static void addUnchangedVariablePredicates(CifBddEdge edge1, CifBddEdge edge2) {
        List<BDD> predicates = list();

        // Define an 'x+ = x' predicate for every variable 'x' that is being assigned on 'edge2' but not on 'edge1'.
        // Instead of immediately adding these predicates to the update of 'edge1', they are collected in a list and
        // added later. This might improve performance in case the update relation BDD of 'edge1' is large.
        // In that case, it may help to first combine all collected predicates into a single predicate, before adding it
        // to the update relation, since then the relation BDD has to be traversed only once.
        for (CifBddVariable variable: Sets.difference(edge2.assignedVariables, edge1.assignedVariables)) {
            CifBddBitVector vectorOld = CifBddBitVector.createDomain(variable.domain);
            CifBddBitVector vectorNew = CifBddBitVector.createDomain(variable.domainNew);
            predicates.add(vectorOld.equalTo(vectorNew));
            vectorOld.free();
            vectorNew.free();
        }

        // If any unchanged variable predicates were defined, update 'edge1' accordingly.
        if (!predicates.isEmpty()) {
            edge1.update = edge1.update.andWith(predicates.stream().reduce(BDD::andWith).get());
        }
    }
}
