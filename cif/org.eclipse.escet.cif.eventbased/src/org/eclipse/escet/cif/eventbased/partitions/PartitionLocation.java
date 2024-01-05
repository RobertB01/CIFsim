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

package org.eclipse.escet.cif.eventbased.partitions;

import org.eclipse.escet.cif.eventbased.automata.Location;

/**
 * Class to store a location in a partition.
 *
 * <p>
 * Contains a double linked list of {@link PartitionLocation} objects in a partition. Also has a link to the original
 * location in the automaton, and a link to the partition owning the location.
 * </p>
 *
 * <p>
 * TODO: An alternative solution to store this information is to make a derived {@link Location} class. While this
 * should be faster (no second object for each location, no additional dereferencing when switching between the location
 * in the automaton and this class), it also causes that a lot of code becomes generic over the type of locations. While
 * it should be feasible, the additional complexity seems to be too big for now. See also
 * {@link PartitionRefinement#locationMapping}.
 * </p>
 */
public class PartitionLocation {
    /** Back-link to the location. */
    public final Location loc;

    /**
     * Next location in the partition, or {@code null} if this is the last location in the partition. Managed by
     * {@link Partition}.
     */
    public PartitionLocation nextPLoc = null;

    /**
     * Previous location in the partition, or {@code null} if this is the first location in the partition. Managed by
     * {@link Partition}.
     */
    public PartitionLocation prevPLoc = null;

    /** Partition of this location. */
    public Partition partition = null;

    /**
     * Constructor of {@link PartitionLocation}.
     *
     * @param loc Referenced location.
     */
    public PartitionLocation(Location loc) {
        this.loc = loc;
    }
}
