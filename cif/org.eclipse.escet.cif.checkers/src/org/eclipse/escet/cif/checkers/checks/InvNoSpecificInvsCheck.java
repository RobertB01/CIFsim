//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.BitSet;
import java.util.List;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.checkers.checks.invcheck.DisallowedInvariantsSubset;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantPlaceKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.NoInvariantSupKind;
import org.eclipse.escet.cif.checkers.checks.invcheck.PlaceKind;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.common.java.Assert;

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
     * The forbidden subset represents the invariants that are disallowed in all three aspects.
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
                        updateDisallowedSets(currentReports, disallowedInvs);
                    }
                }
            }
        }
        return this;
    }

    /**
     * Update the current sets for the newly added set.
     *
     * @param currentSets Sets already stored.
     * @param newSet Set to be added to the set.
     */
    private void updateDisallowedSets(List<DisallowedInvariantsSubset> currentSets, DisallowedInvariantsSubset newSet) {
        Assert.check(!currentSets.isEmpty());

        // Compare the new set with all existing sets to decide whether the new set is useful and which of the current
        // sets become obsolete.
        BitSet keepExistingIndices = new BitSet(currentSets.size()); // Existing entries to keep.
        int index = 0;
        for (DisallowedInvariantsSubset right: currentSets) {
            switch (newSet.compareSubset(right)) {
                case EQUAL:
                    return; // New entry is the same as the tested right side, we're done.
                case RIGHT_LARGER:
                    return; // New entry is a proper subset of the tested right side, we're done.

                case LEFT_LARGER:
                    // Drop the tested right side, it's a proper subset of the new entry.
                    break;

                case BOTH_LARGER:
                    keepExistingIndices.set(index);
                    break;

                default:
                    throw new AssertionError("Unexpected comparison result found.");
            }
            index++;
        }

        // If we get here, then the above loop never reached "EQUAL" or "RIGHT_LARGER", thus all existing entries are
        // "LEFT_LARGER" or "BOTH_LARGER".
        //
        // Walk again through the 'currentSets', moving entries that must be kept to the start of the list.
        int freeIndex = 0;
        for (index = 0; index < currentSets.size(); index++) {
            if (!keepExistingIndices.get(index)) {
                continue;
            }

            if (freeIndex < index) {
                currentSets.set(freeIndex, currentSets.get(index)); // Copy 'index' item to 'freeIndex' item.
            }
            // Else no copy operation was needed.

            freeIndex++; // The free index must be incremented in both cases though.
        }

        // Add the new set to the sets and possibly cleanup.
        if (freeIndex == currentSets.size()) {
            currentSets.add(newSet);
        } else {
            currentSets.set(freeIndex, newSet);
            freeIndex++;

            // Strip the last part of the list so the list is again completely filled.
            int currentSize = currentSets.size();
            while (freeIndex < currentSize) {
                currentSize--;
                currentSets.remove(currentSize); // Restores the "currentSize == currentSets.size()" invariant.
            }
        }
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

        // Compute the index in the flattened 3-dimensional array, and report for all found disallowed subsets.
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
