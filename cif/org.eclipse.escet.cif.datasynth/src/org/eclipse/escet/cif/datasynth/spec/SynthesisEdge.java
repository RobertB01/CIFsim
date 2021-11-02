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

package org.eclipse.escet.cif.datasynth.spec;

import static org.eclipse.escet.cif.datasynth.bdd.BddUtils.bddToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

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

/** Data-based synthesis algorithm edge. */
public class SynthesisEdge {
    /** The synthesis automaton that contains this edge. */
    public final SynthesisAutomaton aut;

    /** The CIF edge that corresponds to this synthesis edge. Is {@code null} for edges created for input variables. */
    public Edge edge;

    /** The event on the edge. */
    public Event event;

    /** The original guard of the edge. */
    public BDD origGuard;

    /** The current guard of the edge. Is updated during the synthesis. */
    public BDD guard;

    /** Precomputed '{@link #guard} and {@link #error}'. Is {@code null} if not available. */
    public BDD guardError;

    /** The CIF assignments that correspond to these synthesis assignments. */
    public Assignment[] assignments;

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
     * Runtime errors include assignments leading to values outside of the BDD representable ranges, division by zero,
     * etc. Runtime errors may or may not include assignments leading to values that are outside of the valid CIF range,
     * which that can still be represented by BDDs, as those situations are also taken care of by the range invariants.
     * </p>
     */
    public BDD error;

    /** Precomputed 'not {@link #error}'. Is {@code null} if not available. */
    public BDD errorNot;

    /**
     * Constructor for the {@link SynthesisEdge} class.
     *
     * @param aut The synthesis automaton that contains this edge.
     */
    public SynthesisEdge(SynthesisAutomaton aut) {
        this.aut = aut;
    }

    /**
     * Global edge initialization for {@link #apply applying} the edge. Must be invoked only once per edge. Must be
     * invoked before any invocation of {@link #preApply} or {@link #apply}.
     *
     * @param doForward Whether to do forward reachability during synthesis.
     */
    public void initApply(boolean doForward) {
        // Precompute 'errorNot'.
        errorNot = error.not();

        // We can include the guard in the update, as it won't change during
        // synthesis. That is, it may differ from the uncontrolled system
        // guard as preparations for state/event exclusion invariants for
        // edges with controllable events may have changed them, etc. But
        // during the actual synthesis the guard won't change. It will then
        // only change again afterwards, after synthesis has completed and the
        // controlled system guards are determined.
        Assert.check(update != null);
        Assert.check(updateGuard == null);
        updateGuard = update.and(guard);
        update.free();
        update = null;

        // Precompute 'guardError'.
        guardError = guard.and(error);

        // If we do forward reachability, precompute 'updateGuardErrorNot'.
        Assert.check(updateGuardErrorNot == null);
        if (doForward) {
            updateGuardErrorNot = updateGuard.and(errorNot);
        }
    }

    /**
     * Global edge guard update, to be applied after the guards have been updated because of the state plant invariants
     * and state/event exclusion requirement invariants. Must be invoked only once per edge. Must be invoked after an
     * invocation of {@link #initApply} and before any invocation of {@link #preApply}.
     *
     * @param doForward Whether to do forward reachability during synthesis.
     */
    public void updateGuardPred(boolean doForward) {
        Assert.check(update == null);
        Assert.check(updateGuard != null);
        BDD updateGuardNew = updateGuard.and(guard);
        updateGuard.free();
        updateGuard = updateGuardNew;

        // If we do forward reachability, update 'updateGuardErrorNot'.
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
                BDD restrictionNew = restriction.replace(aut.oldToNewVarsPairing);
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
     * <p>
     * Forward reachability for bad state predicates is currently not supported.
     * </p>
     *
     * @param pred The predicate to which to apply the assignment in reverse. This predicate is {@link BDD#free freed}
     *     by this method.
     * @param bad Whether the given predicate represents bad states ({@code true}) or good states ({@code false}).
     * @param forward Whether to apply forward ({@code true}) or backward ({@code false}).
     * @param restriction The predicate that indicates the upper bound on the reached states. That is, restrict the
     *     result to these states. May be {@code null} to not impose a restriction, which is semantically equivalent to
     *     providing 'true'.
     * @param applyError Whether to apply the runtime error predicates.
     * @return The resulting predicate.
     */
    public BDD apply(BDD pred, boolean bad, boolean forward, BDD restriction, boolean applyError) {
        // Apply the edge.
        if (forward) {
            // Forward reachability for bad state predicates is currently not
            // supported. We don't need it, so we can't test it.
            Assert.check(!bad);

            // rslt = Exists{x, y, z, ...}(guard && update && pred && !error && restriction)
            BDD rslt = updateGuardRestricted.applyEx(pred, BDDFactory.and, aut.varSetOld);
            pred.free();
            if (aut.env.isTerminationRequested()) {
                return rslt;
            }

            // rsltOld = rslt[x/x+, y/y+, z/z+, ...]
            BDD rsltOld = rslt.replaceWith(aut.newToOldVarsPairing);

            // Return the result of applying the update.
            return rsltOld;
        } else {
            // predNew = pred[x+/x, y+/y, z+/z, ...]
            BDD predNew = pred.replaceWith(aut.oldToNewVarsPairing);
            if (aut.env.isTerminationRequested()) {
                return predNew;
            }

            // rslt = Exists{x+, y+, z+, ...}(guard && update && predNew)
            BDD rslt = updateGuard.applyEx(predNew, BDDFactory.and, aut.varSetNew);
            predNew.free();
            if (aut.env.isTerminationRequested()) {
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
     * Returns a textual representation of the synthesis edge.
     *
     * @return The textual representation.
     */
    @Override
    public String toString() {
        return toString(0, "Edge: ");
    }

    /**
     * Returns a textual representation of the synthesis edge.
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
        String origGuardTxt = bddToStr(origGuard, aut);
        String guardTxt = bddToStr(guard, aut);
        String guardsTxt;
        if (origGuard.equals(guard)) {
            guardsTxt = fmt("%s", guardTxt);
        } else {
            guardsTxt = fmt("%s -> %s", origGuardTxt, guardTxt);
        }
        txt.append(fmt(" (guard: %s)", guardsTxt));
        if (assignments.length > 0) {
            txt.append(" (assignments: ");
            for (int i = 0; i < assignments.length; i++) {
                if (i > 0) {
                    txt.append(", ");
                }
                Assignment asgn = assignments[i];
                txt.append(assignmentToString(asgn));
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
    private Object assignmentToString(Assignment asgn) {
        Expression addr = asgn.getAddressable();
        Declaration addrVar = (Declaration)CifScopeUtils.getRefObjFromRef(addr);
        Expression rhs = asgn.getValue();
        for (SynthesisVariable var: aut.variables) {
            // Skip if precondition violation (conversion failure). Should not
            // occur here once conversion has finished, but check may be useful
            // when debugging conversion code.
            if (var == null) {
                continue;
            }

            // Case distinction based on kind of addressable variable.
            if (var instanceof SynthesisDiscVariable) {
                // Check for match with addressable.
                SynthesisDiscVariable synthDiscVar = (SynthesisDiscVariable)var;
                if (synthDiscVar.var != addrVar) {
                    continue;
                }

                // Assignment from the original CIF model.
                return fmt("%s := %s", synthDiscVar.name, CifTextUtils.exprToStr(rhs));
            } else if (var instanceof SynthesisLocPtrVariable) {
                // Check for match with addressable.
                SynthesisLocPtrVariable synthLpVar = (SynthesisLocPtrVariable)var;
                if (synthLpVar.var != addrVar) {
                    continue;
                }

                // Location pointer assignment.
                int locIdx = ((IntExpression)rhs).getValue();
                Location loc = synthLpVar.aut.getLocations().get(locIdx);
                return fmt("%s := %s", synthLpVar.name, CifTextUtils.getAbsName(loc));
            } else if (var instanceof SynthesisInputVariable) {
                // Check for match with addressable.
                SynthesisInputVariable synthInputVar = (SynthesisInputVariable)var;
                if (synthInputVar.var != addrVar) {
                    continue;
                }

                // Input variable edge. No right hand side, as this is not a
                // 'normal' assignment.
                return fmt("%s+ != %s", synthInputVar.name, synthInputVar.name);
            } else {
                String msg = "Unexpected synthesis variable for addressable: " + var;
                throw new RuntimeException(msg);
            }
        }
        throw new RuntimeException("No synthesis variable found for addressable: " + addrVar);
    }
}
