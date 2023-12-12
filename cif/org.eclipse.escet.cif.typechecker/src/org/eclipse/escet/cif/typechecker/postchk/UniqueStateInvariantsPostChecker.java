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

import java.util.List;

import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.typechecker.ErrMsg;

/** 'Invariants.unique' constraint checker for state invariant, for the 'post' type checking phase. */
public class UniqueStateInvariantsPostChecker {
    /** The set of state invariants. Is filled during checking. */
    List<Invariant> stateInvariants = list();

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

        if (comp instanceof Group) {
            // Check child components.
            for (Component child: ((Group)comp).getComponents()) {
                check((ComplexComponent)child, env);
            }
        } else if (comp instanceof Automaton) {
            // Check invariants in each location for local (in that location) duplication.
            for (Location loc: ((Automaton)comp).getLocations()) {
                check(loc.getInvariants(), false, env);
            }
        }
    }

    /**
     * Checks the invariants for duplicates.
     *
     * @param invariants The invariants to check.
     * @param checkGlobalDuplication Whether to check for duplication in other collected invariants {@code true}, or
     *     only in the provided list {@code false}.
     * @param env The post check environment to use.
     */
    private void check(List<Invariant> invariants, boolean checkGlobalDuplication, CifPostCheckEnv env) {
        List<Invariant> previousEncounteredInvariants = checkGlobalDuplication ? stateInvariants : list();

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

            // Loop over previously encountered invariants and warn for duplicates.
            boolean duplicateInvariant = false;
            for (Invariant previousInvariant: previousEncounteredInvariants) {
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
                previousEncounteredInvariants.add(invariant);
            }
        }
    }
}
