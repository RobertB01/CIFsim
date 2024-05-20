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

package org.eclipse.escet.cif.bdd.utils;

import static org.eclipse.escet.cif.bdd.utils.BddUtils.bddToStr;

import org.eclipse.escet.cif.bdd.spec.CifBddEdge;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;

import com.github.javabdd.BDD;

/** CIF/BDD helper class to apply plant invariants. */
public class CifBddApplyPlantInvariants {
    /** Constructor for the {@link CifBddApplyPlantInvariants} class. */
    private CifBddApplyPlantInvariants() {
        // Static class.
    }

    /**
     * Applies the state/event exclusion plant invariants, as preprocessing step for synthesis.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis. Is modified in-place.
     * @param synthResult The synthesis result.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void applyStateEvtExclPlants(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            boolean dbgEnabled)
    {
        // Update guards to ensure that transitions not allowed by the state/event exclusion plant invariants, are
        // blocked.
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Restricting behavior using state/event exclusion plants.");
        }

        boolean firstDbg = true;
        boolean guardChanged = false;
        for (CifBddEdge edge: cifBddSpec.edges) {
            // Get additional condition for the edge. Skip for internal events that are not in the original
            // specification and for trivially true conditions.
            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return;
            }
            BDD plant = cifBddSpec.stateEvtExclPlants.get(edge.event);
            if (plant == null || plant.isOne() || edge.guard.isZero()) {
                continue;
            }

            // Enforce the additional condition by restricting the guard.
            BDD newGuard = edge.guard.and(plant);
            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return;
            }

            if (edge.guard.equals(newGuard)) {
                newGuard.free();
            } else {
                if (dbgEnabled) {
                    if (firstDbg) {
                        firstDbg = false;
                        cifBddSpec.settings.getDebugOutput().line();
                    }
                    cifBddSpec.settings.getDebugOutput().line("Edge %s: guard: %s -> %s [plant: %s].",
                            edge.toString(0, ""), bddToStr(edge.guard, cifBddSpec), bddToStr(newGuard, cifBddSpec),
                            bddToStr(plant, cifBddSpec));
                }
                edge.guard.free();
                edge.guard = newGuard;
                guardChanged = true;
            }
        }

        if (cifBddSpec.settings.getShouldTerminate().get()) {
            return;
        }
        if (dbgEnabled && guardChanged) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Uncontrolled system:");
            cifBddSpec.settings.getDebugOutput().line(synthResult.getCtrlBehText(1));
            if (!cifBddSpec.edges.isEmpty()) {
                cifBddSpec.settings.getDebugOutput().line(cifBddSpec.getEdgesText(2));
            }
        }
    }

    /**
     * Applies the state plant invariants, as preprocessing step for synthesis.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void applyStatePlantInvs(CifBddSpec cifBddSpec, boolean dbgEnabled) {
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput()
                    .line("Restricting uncontrolled behavior using state plant invariants.");
        }

        boolean guardUpdated = false;
        for (CifBddEdge edge: cifBddSpec.edges) {
            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return;
            }

            // The guards of the edge are restricted such that transitioning to a state that violates the plant
            // invariants is not possible. The update to the predicate is obtained by applying the edge's update
            // backward to the state plant invariant.
            BDD updPred = cifBddSpec.plantInv.id();
            edge.preApply(false, null);
            updPred = edge.apply(updPred, // pred
                    false, // bad
                    false, // forward
                    null, // restriction
                    false); // don't apply error. The supervisor should restrict that.
            edge.postApply(false);

            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return;
            }

            // Compute 'guard and plantInv => updPred'. If the backwards applied state plant invariant is already
            // implied by the current guard and state plant invariant in the source state, we don't need to strengthen
            // the guard. In that case, replace the predicate by 'true', to not add any restriction.
            BDD guardAndPlantInv = edge.guard.and(cifBddSpec.plantInv);
            BDD implication = guardAndPlantInv.imp(updPred);
            boolean skip = implication.isOne();
            guardAndPlantInv.free();
            implication.free();
            if (skip) {
                updPred.free();
                updPred = cifBddSpec.factory.one();
            }

            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return;
            }

            // Store.
            BDD newGuard = edge.guard.id().andWith(updPred);
            if (cifBddSpec.settings.getShouldTerminate().get()) {
                return;
            }

            if (edge.guard.equals(newGuard)) {
                newGuard.free();
            } else {
                if (dbgEnabled) {
                    if (!guardUpdated) {
                        cifBddSpec.settings.getDebugOutput().line();
                    }
                    cifBddSpec.settings.getDebugOutput().line("Edge %s: guard: %s -> %s.", edge.toString(0, ""),
                            bddToStr(edge.guard, cifBddSpec), bddToStr(newGuard, cifBddSpec));
                }
                edge.guard.free();
                edge.guard = newGuard;
                guardUpdated = true;
            }
        }
    }
}
