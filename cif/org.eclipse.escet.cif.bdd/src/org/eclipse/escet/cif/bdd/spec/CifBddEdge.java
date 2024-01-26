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
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

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
import org.eclipse.escet.common.java.Strings;

import com.github.javabdd.BDD;
import com.github.javabdd.BDDFactory;

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
     * the CIF specification to the CIF/BDD representation.
     */
    public BDD guard;

    /** Precomputed '{@link #guard} and {@link #error}'. Is {@code null} if not available. */
    public BDD guardError;

    /** Per {@link #edges edge}, the CIF assignments that are applied by this CIF/BDD edge. */
    public List<List<Assignment>> assignments;

    /**
     * The update predicate that relates old and new values of variables, indicating which combinations of old and new
     * values are allowed by these assignments. Is {@code null} if not available.
     */
    public BDD update;

    /** Precomputed '{@link #guard} and {@link #update}'. Is {@code null} if not available. */
    public BDD updateGuard;

    /** Precomputed '{@link #guard} and {@link #update} and {@link #errorNot}'. Is {@code null} if not available. */
    public BDD updateGuardErrorNot;

    /**
     * Precomputed '{@link #updateGuardErrorNot} and restriction+'. Is {@code null} if not available. Is only available
     * for forward reachability.
     */
    public BDD updateGuardRestricted;

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

    /** Precomputed 'not {@link #error}'. Is {@code null} if not available. */
    public BDD errorNot;

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
     * invoked before any invocation of {@link #preApply} or {@link #apply}. If the {@link #guard} is changed after
     * invoking this method, {@link #reinitApply} must be invoked to re-initialize the edge for applying, unless
     * {@link #cleanupApply} has already been invoked.
     *
     * @param doForward Whether to also initialize this edge for forward reachability, making it possible to
     *     {@link #apply} this edge both forwards and backwards ({@code true}), or not initialize this edge for forward
     *     reachability, making it only possible to apply this edge backwards ({@code false}).
     */
    public void initApply(boolean doForward) {
        // Precompute 'errorNot'.
        errorNot = error.not();

        // We can include the guard in the update, assuming it won't anymore. That is, the guard may differ from the
        // uncontrolled system guard as preparations for state/event exclusion invariants for edges with controllable
        // events may have changed it, etc. But during the actual computations on the CIF/BDD specification, it
        // shouldn't change. If the edge guard does change, the edge must be re-initialized for application.
        //
        // For instance, during the actual synthesis the guard won't change. It could then still change again
        // afterwards, after synthesis has completed and the controlled system guards are determined.
        Assert.check(update != null);
        Assert.check(updateGuard == null);
        updateGuard = update.and(guard);
        update.free();
        update = null;

        // Precompute 'guardError'.
        guardError = guard.and(error);

        // If we allow forward reachability, precompute 'updateGuardErrorNot'.
        Assert.check(updateGuardErrorNot == null);
        if (doForward) {
            updateGuardErrorNot = updateGuard.and(errorNot);
        }
    }

    /**
     * Global edge re-initialization. Edges must be reinitialized when the guards have been updated after
     * {@link #initApply} was invoked. Must be invoked only once per edge.
     *
     * @param doForward Whether to also re-initialize this edge for forward reachability, making it possible to
     *     {@link #apply} this edge both forwards and backwards ({@code true}), or not re-initialize this edge for
     *     forward reachability, making it only possible to apply this edge backwards ({@code false}).
     */
    public void reinitApply(boolean doForward) {
        Assert.check(update == null);
        Assert.check(updateGuard != null);
        BDD updateGuardNew = updateGuard.and(guard);
        updateGuard.free();
        updateGuard = updateGuardNew;

        // If we allow forward reachability, update 'updateGuardErrorNot'.
        if (doForward) {
            updateGuardErrorNot.free();
            updateGuardErrorNot = updateGuard.and(errorNot);
        }
    }

    /**
     * Local edge initialization for {@link #apply applying} the edge. Must be invoked only once per reachability loop.
     * Must be invoked after an invocation of {@link #initApply}. Must be invoked before any invocation of
     * {@link #apply} in that same reachability loop.
     *
     * @param forward Whether to apply forward ({@code true}) or backward ({@code false}).
     * @param restriction The predicate that indicates the upper bound on the reached states. That is, restrict the
     *     result to these states. May be {@code null} to not impose a restriction, which is semantically equivalent to
     *     providing 'true'.
     */
    public void preApply(boolean forward, BDD restriction) {
        Assert.check(updateGuard != null);
        Assert.check(updateGuardRestricted == null);
        if (forward) {
            if (restriction == null) {
                updateGuardRestricted = updateGuard.id();
            } else {
                BDD restrictionNew = restriction.replace(cifBddSpec.oldToNewVarsPairing);
                updateGuardRestricted = updateGuardErrorNot.and(restrictionNew);
                restrictionNew.free();
            }
        }
    }

    /**
     * Local edge cleanup for no longer {@link #apply applying} the edge. Must be invoked only once per reachability
     * loop. Must be invoked after {@link #preApply} and after any invocations of {@link #apply} in that same
     * reachability loop. Must be invoked before any invocation of {@link #cleanupApply}.
     *
     * @param forward Whether to apply forward ({@code true}) or backward ({@code false}).
     */
    public void postApply(boolean forward) {
        Assert.check(updateGuard != null);
        if (forward) {
            Assert.check(updateGuardRestricted != null);
            updateGuardRestricted.free();
            updateGuardRestricted = null;
        } else {
            Assert.check(updateGuardRestricted == null);
        }
    }

    /**
     * Global edge cleanup for no longer {@link #apply applying} the edge. Must be invoked after {@link #preApply}. May
     * be invoked more than once.
     */
    public void cleanupApply() {
        Assert.check(update == null);
        if (errorNot != null) {
            errorNot.free();
            errorNot = null;
        }
        if (updateGuard != null) {
            updateGuard.free();
            updateGuard = null;
        }
        if (guardError != null) {
            guardError.free();
            guardError = null;
        }
        if (updateGuardErrorNot != null) {
            updateGuardErrorNot.free();
            updateGuardErrorNot = null;
        }
        Assert.check(updateGuardRestricted == null);
    }

    /**
     * Applies the assignments of the edge, to a given predicate. The assignments can be applied forward (normally) or
     * backward (reversed).
     *
     * @param pred The predicate to which to apply the assignments. This predicate is {@link BDD#free freed} by this
     *     method.
     * @param bad Whether the given predicate represents bad states ({@code true}) or good states ({@code false}). If
     *     applying forward, bad states are currently not supported.
     * @param forward Whether to apply forward ({@code true}) or backward ({@code false}).
     * @param restriction The predicate that indicates the upper bound on the reached states. That is, restrict the
     *     result to these states. May be {@code null} to not impose a restriction, which is semantically equivalent to
     *     providing 'true'.
     * @param applyError Whether to apply the runtime error predicates. If applying forward, applying runtime error
     *     predicates is currently not supported.
     * @return The resulting predicate.
     */
    public BDD apply(BDD pred, boolean bad, boolean forward, BDD restriction, boolean applyError) {
        // Apply the edge.
        if (forward) {
            // Forward reachability for bad state predicates is currently not
            // supported. We don't need it, so we can't test it.
            Assert.check(!bad);

            // Applying error predicates during forward reachability is not supported.
            Assert.check(!applyError);

            // rslt = Exists{x, y, z, ...}(guard && update && pred && !error && restriction)
            BDD rslt = updateGuardRestricted.applyEx(pred, BDDFactory.and, cifBddSpec.varSetOld);
            pred.free();
            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return rslt;
            }

            // rsltOld = rslt[x/x+, y/y+, z/z+, ...]
            BDD rsltOld = rslt.replaceWith(cifBddSpec.newToOldVarsPairing);

            // Return the result of applying the update.
            return rsltOld;
        } else {
            // predNew = pred[x+/x, y+/y, z+/z, ...]
            BDD predNew = pred.replaceWith(cifBddSpec.oldToNewVarsPairing);
            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return predNew;
            }

            // rslt = Exists{x+, y+, z+, ...}(guard && update && predNew)
            BDD rslt = updateGuard.applyEx(predNew, BDDFactory.and, cifBddSpec.varSetNew);
            predNew.free();
            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return rslt;
            }

            // Apply the runtime error predicate.
            if (applyError) {
                if (bad) {
                    rslt = rslt.orWith(guardError.id());
                } else {
                    rslt = rslt.andWith(errorNot.id());
                }
            }

            if (restriction != null) {
                rslt = rslt.andWith(restriction.id());
            }

            // Return the result of reverse applying the update.
            return rslt;
        }
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
        StringBuilder txt = new StringBuilder();
        txt.append(Strings.duplicate(" ", 2 * indent));
        txt.append(prefix);
        txt.append(fmt("(event: %s)", CifTextUtils.getAbsName(event)));
        String origGuardTxt = bddToStr(origGuard, cifBddSpec);
        String guardTxt = bddToStr(guard, cifBddSpec);
        String guardsTxt;
        if (origGuard.equals(guard)) {
            guardsTxt = fmt("%s", guardTxt);
        } else {
            guardsTxt = fmt("%s -> %s", origGuardTxt, guardTxt);
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

        // Merge updates. The updates need to be made conditional on their original guards, to ensure that the updates
        // of the correct original edge are applied when the guard of that original edge holds. To support
        // non-determinism, if both guards hold, either of the updates may be applied. If neither of the guards holds,
        // we do not care what the update is, as the guard and update predicates are always combined before the edge is
        // applied. So, create: '(guard1 and update1) or (guard2 and update2)'.
        BDD update1 = edge1.guard.id().andWith(edge1.update);
        BDD update2 = edge2.guard.id().andWith(edge2.update);
        mergedEdge.update = update1.orWith(update2);

        // Merge errors. The errors are combined in a similar way as the updates.
        BDD error1 = edge1.guard.id().andWith(edge1.error);
        BDD error2 = edge2.guard.id().andWith(edge2.error);
        mergedEdge.error = error1.orWith(error2);

        // Merge guards.
        mergedEdge.origGuard = edge1.origGuard.orWith(edge2.origGuard);
        mergedEdge.guard = edge1.guard.orWith(edge2.guard);

        // Return the merged edge.
        return mergedEdge;
    }
}
