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

import java.util.function.Supplier;

import org.eclipse.escet.cif.bdd.spec.CifBddEdge;
import org.eclipse.escet.cif.bdd.spec.CifBddEdgeApplyDirection;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.common.java.Strings;

import com.github.javabdd.BDD;

/** CIF/BDD helper class to apply plant invariants. */
public class CifBddApplyPlantInvariants {
    /** Constructor for the {@link CifBddApplyPlantInvariants} class. */
    private CifBddApplyPlantInvariants() {
        // Static class.
    }

    /**
     * Applies the state/event exclusion plant invariants to the guards of the edges of a CIF/BDD specification.
     *
     * @param cifBddSpec The CIF/BDD specification. Is modified in-place.
     * @param behaviorName The name of the behavior to which the invariants are applied, e.g.,
     *     {@code "uncontrolled system"}.
     * @param sysBehTextSupplier Supplier that supplies the system behavior predicate for debug output. E.g.
     *     {@code "  State: (controlled-behavior: ...)"}. If the supplier supplies {@code null}, the system behavior is
     *     not printed as part of the debug output.
     * @param dbgEnabled Whether debug output is enabled.
     */
    public static void applyStateEvtExclPlantsInvs(CifBddSpec cifBddSpec, String behaviorName,
            Supplier<String> sysBehTextSupplier, boolean dbgEnabled)
    {
        // Update guards to block transitions that not allowed by the state/event exclusion plant invariants.
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput()
                    .line("Restricting %s behavior using state/event exclusion plant invariants:", behaviorName);
            cifBddSpec.settings.getDebugOutput().inc();
        }

        boolean guardChanged = false;
        for (CifBddEdge edge: cifBddSpec.edges) {
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }

            // Get additional condition for the edge. Skip for internal events that are not in the original
            // specification and for trivially true conditions.
            BDD plant = cifBddSpec.stateEvtExclPlants.get(edge.event);
            if (plant == null || plant.isOne() || edge.guard.isZero()) {
                continue;
            }

            // Enforce the additional condition by restricting the guard.
            BDD newGuard = edge.guard.and(plant);
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }

            if (edge.guard.equals(newGuard)) {
                newGuard.free();
            } else {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().line("Edge %s: guard: %s -> %s [plant: %s].",
                            edge.toString(0, cifBddSpec.settings.getIndentAmount(), ""),
                            bddToStr(edge.guard, cifBddSpec), bddToStr(newGuard, cifBddSpec),
                            bddToStr(plant, cifBddSpec));
                }
                edge.guard.free();
                edge.guard = newGuard;
                guardChanged = true;
            }
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }
            return;
        }
        if (dbgEnabled) {
            if (guardChanged) {
                String sysBehText = sysBehTextSupplier.get();
                if (sysBehText != null || !cifBddSpec.edges.isEmpty()) {
                    cifBddSpec.settings.getDebugOutput().line();
                    cifBddSpec.settings.getDebugOutput().line("%s:", Strings.makeInitialUppercase(behaviorName));
                    if (sysBehText != null) {
                        sysBehText = Strings.spaces(cifBddSpec.settings.getIndentAmount()) + sysBehText;
                        cifBddSpec.settings.getDebugOutput().line(sysBehText);
                    }
                    if (!cifBddSpec.edges.isEmpty()) {
                        int indentLevel = (sysBehText == null) ? 1 : 2;
                        for (String line: cifBddSpec.getEdgesText(indentLevel)) {
                            cifBddSpec.settings.getDebugOutput().line(line);
                        }
                    }
                }
            } else {
                cifBddSpec.settings.getDebugOutput().line("No guards changed.");
            }
            cifBddSpec.settings.getDebugOutput().dec();
        }
    }

    /**
     * Applies the state plant invariants to the guards of the edges of a CIF/BDD specification.
     *
     * @param cifBddSpec The CIF/BDD specification. Is modified in-place.
     * @param behaviorName The name of the behavior to which the invariants are applied, e.g.,
     *     {@code "uncontrolled system"}.
     * @param dbgEnabled Whether debug output is enabled.
     */
    public static void applyStatePlantInvs(CifBddSpec cifBddSpec, String behaviorName, boolean dbgEnabled) {
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Restricting %s behavior using state plant invariants.",
                    behaviorName);
        }

        boolean guardUpdated = false;
        for (CifBddEdge edge: cifBddSpec.edges) {
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }

            // The guards of the edge are restricted such that transitioning to a state that violates the plant
            // invariants is not possible. The update to the predicate is obtained by applying the edge's update
            // backward to the state plant invariant.
            BDD updPred = cifBddSpec.plantInv.id();
            updPred = edge.apply(updPred, // pred
                    CifBddEdgeApplyDirection.BACKWARD, // backward
                    null); // restriction

            if (cifBddSpec.settings.getTermination().isRequested()) {
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

            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }

            // Store.
            BDD newGuard = edge.guard.id().andWith(updPred);
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }

            if (edge.guard.equals(newGuard)) {
                newGuard.free();
            } else {
                if (dbgEnabled) {
                    if (!guardUpdated) {
                        cifBddSpec.settings.getDebugOutput().line();
                    }
                    cifBddSpec.settings.getDebugOutput().line("Edge %s: guard: %s -> %s.",
                            edge.toString(0, cifBddSpec.settings.getIndentAmount(), ""),
                            bddToStr(edge.guard, cifBddSpec), bddToStr(newGuard, cifBddSpec));
                }
                edge.guard.free();
                edge.guard = newGuard;
                guardUpdated = true;
            }
        }
    }
}
