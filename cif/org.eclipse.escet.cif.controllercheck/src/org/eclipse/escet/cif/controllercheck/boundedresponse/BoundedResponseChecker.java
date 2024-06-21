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

package org.eclipse.escet.cif.controllercheck.boundedresponse;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.escet.cif.bdd.spec.CifBddEdge;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.bdd.utils.BddUtils;
import org.eclipse.escet.cif.bdd.utils.CifBddReachability;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;

import com.github.javabdd.BDD;

/** Class for checking a CIF specification has bounded response. */
public class BoundedResponseChecker {
    /**
     * Performs the bounded response check for a CIF specification.
     *
     * @param cifBddSpec The CIF/BDD specification to check.
     * @return The conclusion of the bounded response check, or {@code null} if the check is aborted.
     */
    public BoundedResponseCheckConclusion checkSystem(CifBddSpec cifBddSpec) {
        // Compute reachable states.
        cifBddSpec.settings.getDebugOutput().line("Computing reachable states...");
        BDD reachableStates = computeReachableStates(cifBddSpec);
        if (cifBddSpec.settings.getShouldTerminate().get()) {
            return null;
        }

        // Compute bounds.
        cifBddSpec.settings.getDebugOutput().line();
        cifBddSpec.settings.getDebugOutput().line("Computing bound for uncontrollable events...");
        Bound uncontrollablesBound = computeBound(cifBddSpec, reachableStates, false);
        if (cifBddSpec.settings.getShouldTerminate().get()) {
            return null;
        }

        cifBddSpec.settings.getDebugOutput().line();
        cifBddSpec.settings.getDebugOutput().line("Computing bound for controllable events...");
        Bound controllablesBound = computeBound(cifBddSpec, reachableStates, true);
        if (cifBddSpec.settings.getShouldTerminate().get()) {
            return null;
        }

        // Clean up.
        cifBddSpec.settings.getDebugOutput().line();
        cifBddSpec.settings.getDebugOutput().line("Bounded response check completed.");
        reachableStates.free();

        // Return check result.
        return new BoundedResponseCheckConclusion(uncontrollablesBound, controllablesBound);
    }

    /**
     * Compute the reachable states of the CIF/BDD specification.
     *
     * @param cifBddSpec The CIF/BDD specification.
     * @return The predicate indicating the reachable states, or {@code null} if termination was requested.
     */
    private BDD computeReachableStates(CifBddSpec cifBddSpec) {
        // Prepare settings for forward reachability.
        String predName = "reachable states"; // Name of the predicate to compute.
        String initName = "initial states"; // Name of the initial value of the predicate.
        String restrictionName = null; // Name of the restriction predicate, if applicable.
        BDD restriction = null; // The restriction predicate, if applicable.
        boolean applyForward = true; // Whether to apply forward reachability (true) or backward reachability (false).
        boolean inclCtrl = true; // Whether to use edges with controllable events.
        boolean inclUnctrl = true; // Whether to use edges with uncontrollable events.
        boolean dbgEnabled = cifBddSpec.settings.getDebugOutput().isEnabled(); // Whether debug output is enabled.
        BDD initPred = cifBddSpec.initial.id(); // The initial predicate. Note: preconditions forbid state invariants.
        CifBddReachability reachability = new CifBddReachability(cifBddSpec, predName, initName, restrictionName,
                restriction, applyForward, inclCtrl, inclUnctrl, dbgEnabled);

        // Perform forward reachability.
        BDD reachabilityResult = reachability.performReachability(initPred);
        return reachabilityResult;
    }

    /**
     * Compute bounded response for the given CIF/BDD specification.
     *
     * @param cifBddSpec The CIF/BDD specification to check.
     * @param reachableStates The BDD predicate indicating the reachable states of the specification.
     * @param controllableEvents {@code true} to compute bounded response for controllable events, {@code false} to
     *     compute it for uncontrollable events.
     * @return The computed bound. Returns {@code null} if termination is requested.
     * @throws UnsupportedException If the bound is so high, it can't be represented as a integer.
     */
    private Bound computeBound(CifBddSpec cifBddSpec, BDD reachableStates, boolean controllableEvents) {
        // The algorithm works as follows:
        // - We start with all reachable states, to account for execution starting in any of the reachable states. For
        //   instance, at the start of a PLC cycle, the system could be in any reachable state.
        // - If there are no reachable states, the system can't be initialized, and we have a bound of '0', since no
        //   events can be executed (the state space is empty). Otherwise, the reachable states are the states were we
        //   can be after applying zero transitions from the start of the execution (bound zero).
        // - We find the bound by in each round taking transitions for the relevant events. In each round, we thus
        //   compute the states reachable after applying transitions 'round' times.
        // - If the specification has bounded response, all sequences are of finite length, and at some point they end.
        //   That is, each round the number of states of the sequence is reduced by one. After enough rounds, given the
        //   length of the sequence, no states are reachable anymore. If after applying transitions 'round' times the
        //   set of reachable states is empty, we have a bound of 'round - 1', since we could reach states after
        //   'round - 1' rounds, but not after 'round' rounds.
        // - If the specification does not have bounded response, then there is at least one cycle of states. The states
        //   within a cycle will with one transition go to the next state in their cycle. Cycles are thus preserved with
        //   each round. If all bounded sequences at some point end, then only the cycles (there could be more of them)
        //   remain. Then a fixed point is reached and there is no bounded response.

        // Get edges to apply.
        List<CifBddEdge> orderedEdges = cifBddSpec.orderedEdgesForward;
        Predicate<CifBddEdge> edgeShouldBeApplied = edge -> {
            if (controllableEvents) {
                return edge.event.getControllable();
            } else {
                return !edge.event.getControllable() && !cifBddSpec.inputVarEvents.contains(edge.event);
            }
        };
        List<CifBddEdge> edgesToApply = orderedEdges.stream().filter(edgeShouldBeApplied).toList();
        if (cifBddSpec.settings.getShouldTerminate().get()) {
            return null;
        }

        // Apply the algorithm.
        Integer round = 0; // Number of rounds done so far. Becomes 'null' if no bounded response.
        BDD prevRoundStates = cifBddSpec.factory.zero(); // Dummy value that gets overwritten in the first iteration.
        BDD roundStates = reachableStates.id();
        while (!roundStates.isZero()) {
            // Prepare for the next round.
            round++;
            prevRoundStates.free();
            prevRoundStates = roundStates;
            roundStates = null;

            // Check for too many rounds (integer overflow).
            if (round < 0) {
                throw new UnsupportedException("Failed to compute bounded response, as the bound is too high.");
            }

            // Output debug information.
            cifBddSpec.settings.getDebugOutput().line("Bounded response check round %,d (states before round: %s).",
                    round, BddUtils.bddToStr(prevRoundStates, cifBddSpec));

            // Do the next round. Take one transition for each of the relevant events, to get the states reachable after
            // taking transitions 'round' times from any of the reachable states.
            roundStates = cifBddSpec.factory.zero();
            for (CifBddEdge edge: edgesToApply) {
                // Apply edge.
                boolean forward = true;
                BDD restriction = null;
                BDD edgePred = edge.apply(prevRoundStates.id(), forward, restriction);
                if (cifBddSpec.settings.getShouldTerminate().get()) {
                    return null;
                }

                // Add states reachable by the edge to the states reachable by this round.
                roundStates = roundStates.id().orWith(edgePred);
                if (cifBddSpec.settings.getShouldTerminate().get()) {
                    return null;
                }
            }

            // Detect cycles.
            if (roundStates.equalsBDD(prevRoundStates)) {
                round = null;
                break;
            }
            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return null;
            }
        }

        // Cleanup.
        prevRoundStates.free();
        roundStates.free();

        // Return the result.
        if (round == null) {
            // Cycles found, so no bounded response.
            return new Bound(true, false, null);
        } else {
            // No cycles, so bounded response.
            return new Bound(round > 0, true, Math.max(0, round - 1));
        }
    }
}
