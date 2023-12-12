//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.postchk;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.ExprStructuralEqHashWrap;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.typechecker.ErrMsg;

/** 'Invariants.unique' constraint checker for state invariant, for the 'post' type checking phase. */
public class UniqueStateInvariantsPostChecker {
    /**
     * Per state predicate of state invariants, the first-encountered state invariant with that state predicate. Is
     * filled during checking.
     */
    private final Map<ExprStructuralEqHashWrap, Invariant> stateInvariants = map();

    /**
     * Checks the specification for violations of the 'Invariants.unique' constraint.
     *
     * <p>
     * We need to check this after the elimination of component definition/instantiation to ensure proper equality
     * checking of invariants, to not get any false positives or false negatives.
     * </p>
     *
     * @param comp The component to check, recursively. The component must not include any component
     *     definitions/instantiations.
     * @param env The post check environment to use.
     */
    public void check(ComplexComponent comp, CifPostCheckEnv env) {
        // Check invariants for global duplication.
        check(comp.getInvariants(), true, env);

        if (comp instanceof Group group) {
            // Check child components.
            for (Component child: group.getComponents()) {
                check((ComplexComponent)child, env);
            }
        } else if (comp instanceof Automaton aut) {
            // Check invariants in each location for local (in that location) duplication.
            for (Location loc: aut.getLocations()) {
                if (!loc.getInvariants().isEmpty()) {
                    check(loc.getInvariants(), false, env);
                }
            }
        }
    }

    /**
     * Checks the invariants for duplicates.
     *
     * @param invariants The invariants to check.
     * @param checkGlobalDuplication Whether to check for duplication in other collected invariants {@code true}, or
     *     only among the provided invariants {@code false}.
     * @param env The post check environment to use.
     */
    private void check(List<Invariant> invariants, boolean checkGlobalDuplication, CifPostCheckEnv env) {
        Map<ExprStructuralEqHashWrap, Invariant> previousEncounteredInvariants = checkGlobalDuplication
                ? stateInvariants : map();

        for (Invariant invariant: invariants) {
            // Check only state invariants.
            switch (invariant.getInvKind()) {
                case EVENT_DISABLES:
                    continue;
                case EVENT_NEEDS:
                    continue;
                case STATE:
                    break;
                default:
                    throw new RuntimeException("Unknown invariant kind: " + invariant.getInvKind());
            }

            // Wrap the predicate of the invariant, for proper value equality and efficient comparison.
            ExprStructuralEqHashWrap wrappedExpr = new ExprStructuralEqHashWrap(invariant.getPredicate());
            Invariant duplicate = previousEncounteredInvariants.get(wrappedExpr);

            // Check for duplicate.
            if (duplicate != null) {
                env.addProblem(ErrMsg.INV_DUPL_STATE, invariant.getPosition());
                env.addProblem(ErrMsg.INV_DUPL_STATE, duplicate.getPosition());
            } else {
                // No duplicate yet. Store it to find duplicates later.
                previousEncounteredInvariants.put(wrappedExpr, invariant);
            }
        }
    }
}
