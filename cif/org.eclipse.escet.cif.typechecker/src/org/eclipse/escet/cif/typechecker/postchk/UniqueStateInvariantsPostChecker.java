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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifValueUtils;
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
    /** The collection of state invariants, grouped based on the hash of the predicate. Is filled during checking. */
    private final Map<ExprStructuralEqHashWrap, List<Invariant>> stateInvariants = map();

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
                check(loc.getInvariants(), false, env);
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
        Map<ExprStructuralEqHashWrap, List<Invariant>> previousEncounteredInvariants = checkGlobalDuplication
                ? stateInvariants : map();

        for (Invariant invariant: invariants) {
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

            // Wrap the predicate of the invariant, for efficient comparing based on hash equality.
            ExprStructuralEqHashWrap wrappedPred = new ExprStructuralEqHashWrap(invariant.getPredicate());
            List<Invariant> hashEqualInvariants = previousEncounteredInvariants.getOrDefault(wrappedPred, list());

            // Loop over previously encountered invariants with equal predicate hash and warn for duplicates.
            boolean duplicateInvariant = false;
            for (Invariant previousInvariant: hashEqualInvariants) {
                if (CifValueUtils.areStructurallySameExpression(invariant.getPredicate(),
                        previousInvariant.getPredicate()))
                {
                    // Duplicate invariant encountered.
                    duplicateInvariant = true;

                    // Add warning to this invariant.
                    env.addProblem(ErrMsg.INV_DUPL_STATE, invariant.getPosition());

                    // Add warning to previously encountered invariant.
                    env.addProblem(ErrMsg.INV_DUPL_STATE, previousInvariant.getPosition());

                    // Skip checking as one duplicate is enough.
                    break;
                }
            }

            // Since areStructurallySameExpression is transitive, only save non-duplicate invariants.
            if (!duplicateInvariant) {
                hashEqualInvariants.add(invariant);
                previousEncounteredInvariants.put(wrappedPred, hashEqualInvariants);
            }
        }
    }
}
