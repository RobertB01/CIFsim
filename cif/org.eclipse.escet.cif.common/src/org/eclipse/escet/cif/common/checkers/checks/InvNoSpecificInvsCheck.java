//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.DisallowedInvariantsSubset;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.cif.common.checkers.checks.invcheck.PlaceKind;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;

/** CIF check that disallows subsets of invariants on invariant kind, supervisory kind and/or place of specification. */
public class InvNoSpecificInvsCheck extends CifCheck {
    /**
     * Stored disallowed invariant subsets.
     *
     * <p>
     * Outer list is a flattened 3-dimensional array on the supervisory kind, invariant kind and place kind aspects.
     * Inner list contains the list {@link DisallowedInvariantsSubset} to report when the check finds an invariant
     * mapped to that index in the three dimensions. Inner list is {@code null} if there is nothing to report about.
     * </p>
     */
    private List<List<DisallowedInvariantsSubset>> disallowedSubsets;

    /**
     * If {@code true}, invariants that can be statically analyzed to never block behavior are silently skipped. If
     * {@code false} all invariants are checked against the disallowed subsets and reported if violations are found.
     */
    private boolean ignoreNeverBlockingInvariants = false;

    /** Constructor of the {@link InvNoSpecificInvsCheck} class. */
    public InvNoSpecificInvsCheck() {
        int numEntries = NoInvariantSupKind.NUMBER_OF_VALUES * NoInvariantKind.NUMBER_OF_VALUES
                * NoInvariantPlaceKind.NUMBER_OF_VALUES;
        disallowedSubsets = listc(numEntries);
        for (int i = 0; i < numEntries; i++) {
            disallowedSubsets.add(null);
        }
    }

    /**
     * Add a disallowed invariant subset to the collection.
     *
     * <p>
     * The forbidden subset is the invariants that are covered in all three arguments.
     * </p>
     *
     * @param noSupKind Forbidden values for the supervisory kind in the invariants subset.
     * @param noInvKind Forbidden values for the invariant kind in the invariants subset.
     * @param noPlaceKind Forbidden values for the place kind in the invariants subset.
     * @return The check instance, for daisy-chaining.
     */
    public InvNoSpecificInvsCheck disallow(NoInvariantSupKind noSupKind, NoInvariantKind noInvKind,
            NoInvariantPlaceKind noPlaceKind)
    {
        DisallowedInvariantsSubset disallowedInvs = new DisallowedInvariantsSubset(noSupKind, noInvKind, noPlaceKind);
        for (SupKind supKind: SupKind.values()) {
            if (!noSupKind.isDisallowed(supKind)) {
                continue;
            }
            for (InvKind invKind: InvKind.values()) {
                if (!noInvKind.isDisallowed(invKind)) {
                    continue;
                }
                for (PlaceKind placeKind: PlaceKind.values()) {
                    if (!noPlaceKind.isDisallowed(placeKind)) {
                        continue;
                    }

                    // The new disallowed subset applies for this combination of aspect values, add it.
                    int index = computeIndex(supKind, invKind, placeKind);
                    List<DisallowedInvariantsSubset> currentReports = disallowedSubsets.get(index);

                    if (currentReports == null) {
                        // There are no other subsets yet, create a new list to store it.
                        disallowedSubsets.set(index, list(disallowedInvs));
                    } else {
                        // The entry already has other subsets. Compare report relevance.
                        // If the new entry is more relevant it triumphs over all existing entries,
                        // if it has the same relevance, append it.
                        int newRelevance = disallowedInvs.getReportRelevance();
                        int currentRelevance = first(currentReports).getReportRelevance();
                        if (newRelevance > currentRelevance) {
                            currentReports.clear();
                            currentReports.add(disallowedInvs);
                        } else if (newRelevance == currentRelevance) {
                            currentReports.add(disallowedInvs);
                        }
                    }
                }
            }
        }
        return this;
    }

    /**
     * Disable checking of invariants that never block any behavior after static analysis of the condition.
     *
     * @return The check instance, for daisy-chaining.
     */
    public InvNoSpecificInvsCheck ignoreNeverBlockingInvariants() {
        return ignoreNeverBlockingInvariants(true);
    }

    /**
     * Configure whether invariants that never block any behavior after static analysis of the condition should be
     * ignored.
     *
     * @param ignore If {@code true}, invariants that can be statically analyzed to never block behavior are silently
     *     skipped. If {@code false} all invariants are checked against the disallowed subsets and reported if
     *     violations are found.
     * @return The check instance, for daisy-chaining.
     */
    public InvNoSpecificInvsCheck ignoreNeverBlockingInvariants(boolean ignore) {
        ignoreNeverBlockingInvariants = ignore;
        return this;
    }

    @Override
    protected void preprocessInvariant(Invariant inv, CifCheckViolations violations) {
        // Extract the three aspects of the invariant to examine.
        SupKind supKind = inv.getSupKind();
        InvKind invKind = inv.getInvKind();
        PlaceKind placeKind = (inv.eContainer() instanceof Location) ? PlaceKind.LOCATION : PlaceKind.COMPONENT;

        // If never blocking invariants should be skipped, check if 'inv' is such an invariant.
        if (ignoreNeverBlockingInvariants) {
            switch (invKind) {
                case EVENT_DISABLES:
                    if (CifValueUtils.isTriviallyFalse(inv.getPredicate(), false, true)) {
                        return;
                    }
                    break;
                case EVENT_NEEDS:
                case STATE:
                    if (CifValueUtils.isTriviallyTrue(inv.getPredicate(), false, true)) {
                        return;
                    }
                    break;
                default:
                    throw new AssertionError("Unexpected invariant kind found.");
            }
        }

        // Compute the index in the flattened three dimensional array, and report for all found disallowed subsets.
        int index = computeIndex(supKind, invKind, placeKind);
        List<DisallowedInvariantsSubset> subsets = disallowedSubsets.get(index);
        if (subsets != null) {
            for (DisallowedInvariantsSubset subset: subsets) {
                subset.addViolation(inv, violations);
            }
        }
    }

    /**
     * Compute the index of the flattened 3-dimensional array in the outer list of {@link #disallowedSubsets}.
     *
     * @param supKind Value for the supervisory kind aspect.
     * @param invKind Value for the invariant kind aspect.
     * @param placeKind Value for the place kind aspect.
     * @return The computed index in the flattened array.
     */
    private int computeIndex(SupKind supKind, InvKind invKind, PlaceKind placeKind) {
        return placeKind.ordinal() + NoInvariantPlaceKind.NUMBER_OF_VALUES
                * (invKind.ordinal() + NoInvariantKind.NUMBER_OF_VALUES * supKind.ordinal());
    }
}
