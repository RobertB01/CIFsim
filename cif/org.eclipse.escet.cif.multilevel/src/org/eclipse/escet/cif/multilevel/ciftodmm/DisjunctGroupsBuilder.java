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

package org.eclipse.escet.cif.multilevel.ciftodmm;

import static org.eclipse.escet.common.java.BitSets.copy;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

/** Class for constructing groups that do not share relevant relation elements. */
public class DisjunctGroupsBuilder {
    /** Constructor for the static {@link DisjunctGroupsBuilder} class. */
    private DisjunctGroupsBuilder() {
        // Static class.
    }

    /**
     * Construct plant groups from the provided plant elements.
     *
     * @param elements Plant elements to combine into groups.
     * @return The constructed groups of plants.
     */
    public static List<OwnedAndAccessedElements> createPlantGroups(Collection<OwnedAndAccessedElements> elements) {
        BiPredicate<OwnedAndAccessedElements, OwnedAndAccessedElements> checkPlantShareRelation = (
                rel1, rel2
        ) -> rel1.ownedElements.intersects(rel2.accessedElements)
                || rel1.accessedElements.intersects(rel2.ownedElements)
                || rel1.accessedElements.intersects(rel2.accessedElements);

        return set2list(createGroups(elements, checkPlantShareRelation));
    }

    /**
     * Construct requirement groups from the provided requirement elements.
     *
     * @param elements Requirement elements to combine into groups.
     * @param irrelevantAccess Access to registered elements that should be ignored.
     * @return The constructed groups of requirements.
     */
    public static List<OwnedAndAccessedElements> createRequirementGroups(Collection<OwnedAndAccessedElements> elements,
            BitSet irrelevantAccess)
    {
        return set2list(createGroups(elements, new RequirementsShareRelation(irrelevantAccess)));
    }

    /** Check class for deciding whether two requirement groups share a relation element. */
    private static class RequirementsShareRelation
            implements BiPredicate<OwnedAndAccessedElements, OwnedAndAccessedElements>
    {
        /**
         * Accesses that should not be taken into account to decide sharing of relations between two requirement groups.
         */
        private final BitSet irrelevantAccesses;

        /**
         * Constructor of the {@link RequirementsShareRelation} class.
         *
         * @param irrelevantAccesses Accesses that should not be taken into account to decide sharing of relations
         *     between two requirement groups.
         */
        public RequirementsShareRelation(BitSet irrelevantAccesses) {
            this.irrelevantAccesses = irrelevantAccesses;
        }

        @Override
        public boolean test(OwnedAndAccessedElements rel1, OwnedAndAccessedElements rel2) {
            // If one side owns a relation that the other side accessed, sharing of relations exists.
            if (rel1.ownedElements.intersects(rel2.accessedElements)
                    || rel1.accessedElements.intersects(rel2.ownedElements))
            {
                return true;
            }
            // If both sides only access a relation, at least one shared relation must not be ignored.
            BitSet access1 = copy(rel1.accessedElements);
            access1.andNot(irrelevantAccesses);
            return access1.intersects(rel2.accessedElements);
        }
    }

    /**
     * Construct groups of plant or requirement elements such that any two different groups do not have relevant shared
     * relations.
     *
     * @param elements Elements to group.
     * @param hasSharedRelation Predicate function to decide whether its arguments have a shared relation.
     * @return Groups of elements.
     */
    private static Set<OwnedAndAccessedElements> createGroups(Collection<OwnedAndAccessedElements> elements,
            BiPredicate<OwnedAndAccessedElements, OwnedAndAccessedElements> hasSharedRelation)
    {
        Set<OwnedAndAccessedElements> nonSharingGroups = set(); // Groups that do not share relations with each other.

        // Try adding elements one at a time, examining the non-sharing groups so far.
        //
        for (OwnedAndAccessedElements element: elements) {
            // Replaces 'element' as soon as merging with an overlapping non-sharing group must take place.
            // In this way, the original element is preserved.
            OwnedAndAccessedElements newElement = null;

            // Find groups that share a relation with the new element and merge them into the new element.
            Iterator<OwnedAndAccessedElements> nonSharingGroupIter = nonSharingGroups.iterator();
            while (nonSharingGroupIter.hasNext()) {
                OwnedAndAccessedElements nonSharingGroup = nonSharingGroupIter.next();

                // If overlap is found, merge them.
                // Note that continuing testing against the original new element after a merge is correct as there is no
                // sharing between the already created groups.
                if (hasSharedRelation.test(element, nonSharingGroup)) {
                    if (newElement == null) {
                        newElement = new OwnedAndAccessedElements(element);
                    }
                    newElement.merge(nonSharingGroup);
                    nonSharingGroupIter.remove();
                }
            }

            // Add the new group.
            nonSharingGroups.add((newElement == null) ? element : newElement);
        }
        return nonSharingGroups;
    }
}
