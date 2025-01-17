//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.checks.nonblockingundercontrol;

import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.bdd.settings.ExplorationStrategy;
import org.eclipse.escet.cif.bdd.spec.CifBddEdge;
import org.eclipse.escet.cif.bdd.spec.CifBddEdgeApplyDirection;
import org.eclipse.escet.cif.bdd.spec.CifBddEdgeKind;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.bdd.utils.BddUtils;
import org.eclipse.escet.cif.bdd.utils.CifBddReachability;
import org.eclipse.escet.cif.controllercheck.checks.ControllerCheckerBddBasedCheck;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.java.output.DebugNormalOutput;

import com.github.javabdd.BDD;

/** Class for checking a CIF specification is non-blocking under control. */
public class NonBlockingUnderControlCheck
        extends ControllerCheckerBddBasedCheck<NonBlockingUnderControlCheckConclusion>
{
    /** The name of the property being checked. */
    public static final String PROPERTY_NAME = "non-blocking under control";

    @Override
    public String getPropertyName() {
        return PROPERTY_NAME;
    }

    @Override
    public NonBlockingUnderControlCheckConclusion performCheck(CifBddSpec cifBddSpec) {
        DebugNormalOutput dbg = cifBddSpec.settings.getDebugOutput();
        Termination termination = cifBddSpec.settings.getTermination();

        // 1) Compute predicate 'not gc' that indicates when no controllable event is enabled. That is, the negation of
        // the disjunction of the guards of the edges with controllable events.
        dbg.line("Computing the condition for no controllable event to be enabled:");
        dbg.inc();

        BDD notGc = computeNotGc(cifBddSpec);

        if (termination.isRequested()) {
            return null;
        }
        dbg.line("Condition under which no controllable event is enabled: %s", BddUtils.bddToStr(notGc, cifBddSpec));
        dbg.dec();

        // 2) Compute the 'ccp' states, the states on controllable-complete paths. This is computed by performing a
        // backwards reachability computation from 'marked and not gc', using all edges. For the edges of the
        // uncontrollable events, use 'guard and not gc' instead of the 'guard' of the edge.
        dbg.line();
        dbg.line("Computing the controllable-complete path states:");
        dbg.inc();

        BDD ccp = computeCcp(cifBddSpec, notGc);

        if (termination.isRequested()) {
            return null;
        }
        dbg.line();
        dbg.line("Controllable-complete path states: %s", BddUtils.bddToStr(ccp, cifBddSpec));
        dbg.dec();

        // 3) Compute the 'bad' states: the not-'ccp' states and states that can reach such states. This is computed by
        // performing a backwards reachability computation on 'not ccp', using all edges. Unlike in step 2, the
        // original/unchanged guards of the edges are used.
        dbg.line();
        dbg.line("Computing the bad states:");
        dbg.inc();

        BDD bad = computeBad(cifBddSpec, ccp);

        if (termination.isRequested()) {
            return null;
        }
        dbg.line();
        dbg.line("Bad states: %s", BddUtils.bddToStr(bad, cifBddSpec));
        dbg.dec();

        // 4) Check whether non-blocking under control holds. It holds if the initial states are not 'bad'. That is,
        // check whether '(initial and bad) = false' holds.
        //
        // We can use 'initial' rather than 'initialInv', since preconditions forbid state invariants.
        dbg.line();
        dbg.line("Computing the result of the non-blocking under control check:");
        dbg.inc();

        BDD initialAndBad = cifBddSpec.initial.id().andWith(bad);

        if (termination.isRequested()) {
            return null;
        }
        dbg.line("Initial states: %s", BddUtils.bddToStr(cifBddSpec.initial, cifBddSpec));
        dbg.line("Bad initial states: %s", BddUtils.bddToStr(initialAndBad, cifBddSpec));
        dbg.dec();

        boolean isNonBlockingUnderControl = initialAndBad.isZero();
        initialAndBad.free();

        dbg.line();
        dbg.line("Non-blocking under control: %s", isNonBlockingUnderControl ? "yes" : "no");

        // Return check result.
        return new NonBlockingUnderControlCheckConclusion(isNonBlockingUnderControl);
    }

    /**
     * Perform step 1: compute predicate 'not gc' that indicates when no controllable event is enabled. That is, the
     * negation of the disjunction of the guards of the edges with controllable events.
     *
     * @param cifBddSpec The CIF/BDD specification.
     * @return Predicate 'not gc', or {@code null} if termination is requested.
     */
    private BDD computeNotGc(CifBddSpec cifBddSpec) {
        Termination termination = cifBddSpec.settings.getTermination();

        // Compute 'gc'.
        BDD gc = cifBddSpec.factory.zero();
        for (CifBddEdge edge: cifBddSpec.edges) {
            // We should only consider edges with controllable events, thus drop edges with uncontrollable events.
            if (!edge.event.getControllable()) {
                continue;
            }

            // Extend the condition.
            gc = gc.orWith(edge.guard.id());

            // Check for termination request.
            if (termination.isRequested()) {
                return null;
            }
        }

        // Compute 'not gc'.
        BDD notGc = gc.not();
        gc.free();

        // Return 'not gc'.
        return notGc;
    }

    /**
     * Perform step 2: compute the 'ccp' states, the states on controllable-complete paths. This is computed by
     * performing a backwards reachability computation from 'marked and not gc', using all edges. For the edges of the
     * uncontrollable events, use 'guard and not gc' instead of the 'guard' of the edge.
     *
     * @param cifBddSpec The CIF/BDD specification.
     * @param notGc The 'not gc' predicate. Is freed by this method.
     * @return The 'ccp' states, or {@code null} if termination is requested.
     */
    private BDD computeCcp(CifBddSpec cifBddSpec, BDD notGc) {
        Termination termination = cifBddSpec.settings.getTermination();

        // Get edges with uncontrollable events.
        List<CifBddEdge> unctrlEdges = cifBddSpec.edges.stream().filter(e -> !e.event.getControllable()).toList();

        // Preserve current guards of edges with uncontrollable events.
        Map<CifBddEdge, BDD> unctrlEdgeGuards = mapc(unctrlEdges.size());
        for (CifBddEdge unctrlEdge: unctrlEdges) {
            unctrlEdgeGuards.put(unctrlEdge, unctrlEdge.guard);
        }

        if (termination.isRequested()) {
            return null;
        }

        // Modify guards of edges with uncontrollable events.
        for (CifBddEdge unctrlEdge: unctrlEdges) {
            // Change edge 'guard' to 'guard and not gc'.
            unctrlEdge.guard = unctrlEdge.guard.and(notGc);

            if (termination.isRequested()) {
                return null;
            }

            // Re-initialize the modified edge for being applied.
            unctrlEdge.reinitApply();

            if (termination.isRequested()) {
                return null;
            }
        }

        // Prepare settings for reachability computation.
        String predName = "controllable-complete path states"; // Name of the predicate to compute.
        String initValName = "controllable-complete path end states"; // Name of the initial value of the predicate.
        String restrictionName = null; // Name of the restriction predicate, if applicable.
        BDD restriction = null; // The restriction predicate, if applicable.
        CifBddEdgeApplyDirection direction = CifBddEdgeApplyDirection.BACKWARD; // Apply backward reachability.
        Set<CifBddEdgeKind> edgeKinds = EnumSet.allOf(CifBddEdgeKind.class); // Kinds of edges to apply.
        boolean dbgEnabled = cifBddSpec.settings.getDebugOutput().isEnabled(); // Whether debug output is enabled.
        CifBddReachability reachability = new CifBddReachability(cifBddSpec, predName, initValName, restrictionName,
                restriction, direction, edgeKinds, dbgEnabled);

        // If the saturation strategy is used, configure the saturation instance number.
        if (cifBddSpec.settings.getExplorationStrategy() == ExplorationStrategy.SATURATION) {
            reachability.setSaturationInstance(SATURATION_INSTANCE_NONBLOCKING_CCP);
        }

        // Get the initial predicate for the reachability computation. We use 'marked' rather than 'markedInv', since
        // preconditions forbid state invariants.
        BDD initPred = cifBddSpec.marked.id().andWith(notGc);

        if (termination.isRequested()) {
            return null;
        }

        // Perform forward reachability.
        BDD reachabilityResult = reachability.performReachability(initPred);

        if (termination.isRequested()) {
            return null;
        }

        // Restore original guards of edges with uncontrollable events.
        for (Entry<CifBddEdge, BDD> entry: unctrlEdgeGuards.entrySet()) {
            // Restore previous guard.
            CifBddEdge edge = entry.getKey();
            edge.guard.free();
            edge.guard = entry.getValue();

            if (termination.isRequested()) {
                return null;
            }

            // Re-initialize the modified edge for being applied.
            edge.reinitApply();

            if (termination.isRequested()) {
                return null;
            }
        }

        // Return the result of the reachability computation.
        return reachabilityResult;
    }

    /**
     * Perform step 3: compute the 'bad' states: the not-'ccp' states and states that can reach such states. This is
     * computed by performing a backwards reachability computation on 'not ccp', using all edges. Unlike in step 2, the
     * original/unchanged guards of the edges are used.
     *
     * @param cifBddSpec The CIF/BDD specification.
     * @param ccp The 'ccp' predicate. Is freed by this method.
     * @return The 'bad' states, or {@code null} if termination is requested.
     */
    private BDD computeBad(CifBddSpec cifBddSpec, BDD ccp) {
        Termination termination = cifBddSpec.settings.getTermination();

        // Prepare settings for reachability computation.
        String predName = "bad states"; // Name of the predicate to compute.
        String initValName = "not controllable-complete path states"; // Name of the initial value of the predicate.
        String restrictionName = null; // Name of the restriction predicate, if applicable.
        BDD restriction = null; // The restriction predicate, if applicable.
        CifBddEdgeApplyDirection direction = CifBddEdgeApplyDirection.BACKWARD; // Apply backward reachability.
        Set<CifBddEdgeKind> edgeKinds = EnumSet.allOf(CifBddEdgeKind.class); // Kinds of edges to apply.
        boolean dbgEnabled = cifBddSpec.settings.getDebugOutput().isEnabled(); // Whether debug output is enabled.
        CifBddReachability reachability = new CifBddReachability(cifBddSpec, predName, initValName, restrictionName,
                restriction, direction, edgeKinds, dbgEnabled);

        // If the saturation strategy is used, configure the saturation instance number.
        if (cifBddSpec.settings.getExplorationStrategy() == ExplorationStrategy.SATURATION) {
            reachability.setSaturationInstance(SATURATION_INSTANCE_NONBLOCKING_BAD);
        }

        // Get the initial predicate for the reachability computation.
        BDD initPred = ccp.not();

        if (termination.isRequested()) {
            return null;
        }

        ccp.free();

        if (termination.isRequested()) {
            return null;
        }

        // Perform forward reachability.
        BDD reachabilityResult = reachability.performReachability(initPred);
        return reachabilityResult;
    }
}
