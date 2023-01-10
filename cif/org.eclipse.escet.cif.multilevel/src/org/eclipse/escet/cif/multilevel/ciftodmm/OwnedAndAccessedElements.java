//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.BitSets.makeBitset;

import java.util.BitSet;

/** Owned and accessed elements of a group (one or more plant or requirement elements). */
public class OwnedAndAccessedElements {
    /** Elements of the plant or requirement groups. */
    public final BitSet groupElements;

    /** Elements owned by the {@link #groupElements}. */
    public final BitSet ownedElements;

    /** Elements accessed by the {@link #groupElements}. */
    public final BitSet accessedElements;

    /**
     * Constructor of the {@link OwnedAndAccessedElements} class for a single plant or requirement element.
     *
     * <p>
     * The {@link #ownedElements} and {@link #accessedElements} are empty after construction.
     * </p>
     *
     * @param elementIndex Index number of a plant or requirement group element.
     */
    public OwnedAndAccessedElements(int elementIndex) {
        this(makeBitset(elementIndex), new BitSet(), new BitSet());
    }

    /**
     * Copy constructor of the {@link OwnedAndAccessedElements} class.
     *
     * <p>
     * Make an independent copy of the given instance.
     * </p>
     *
     * @param other Instance to copy.
     */
    public OwnedAndAccessedElements(OwnedAndAccessedElements other) {
        this(copy(other.groupElements), copy(other.ownedElements), copy(other.accessedElements));
    }

    /**
     * Constructor of the {@link OwnedAndAccessedElements} class.
     *
     * @param elements Elements of the plant or requirement group.
     * @param ownedElements Elements owned by the {@link #groupElements}.
     * @param accessedElements Elements accessed by the {@link #groupElements}.
     */
    public OwnedAndAccessedElements(BitSet elements, BitSet ownedElements, BitSet accessedElements) {
        this.groupElements = elements;
        this.ownedElements = ownedElements;
        this.accessedElements = accessedElements;
    }

    /**
     * Mark an element as owned by at least one of the {@link #groupElements} of this instance.
     *
     * @param elementIndex Unique index number of the element.
     */
    public void setOwnedRelation(int elementIndex) {
        ownedElements.set(elementIndex);
    }

    /**
     * Mark an element as accessed by at least one of the {@link #groupElements} of this instance.
     *
     * @param elementIndex Unique index number of the element being marked.
     */
    public void setAccessedRelation(int elementIndex) {
        accessedElements.set(elementIndex);
    }

    /**
     * Merge all elements of the given instance into this instance.
     *
     * @param other Elements to add here.
     */
    public void merge(OwnedAndAccessedElements other) {
        groupElements.or(other.groupElements);
        ownedElements.or(other.ownedElements);
        accessedElements.or(other.accessedElements);
    }

    /**
     * Get all the owned and accessed elements of the group.
     *
     * @return All owned and accessed elements of the group.
     */
    public BitSet getRelations() {
        BitSet relations = copy(ownedElements);
        relations.or(accessedElements);
        return relations;
    }
}
