//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Sets.setc;

import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Location;

/**
 * Partition of locations of an automaton.
 *
 * <p>
 * The {@link #locations} variable is managed by this class, it is public to give other classes read access.
 * </p>
 *
 * <p>
 * All other variables are managed by the partitioning algorithm.
 * <ul>
 * <li>The double linked list of compute partitions stored in {@link #nextCompute}, {@link #prevCompute}, and
 * {@link #inComputeList} are partitions that have been updated, but their new contents is not yet applied in the
 * algorithm.</li>
 * <li>If not {@code null}, the {@link #newPartition} points to the partition where locations of this partition are
 * being moved to. This ensures all moved locations end up in the same new partition, and it enables proper handling of
 * the case where all its locations were moved (which should be the same as none of its locations were moved).</li>
 * <li>The {@link #nextAffected} single linked list tracks which partitions have a new partition ({@link #newPartition}
 * is not {@code null}) in order to do post-processing on them.</li>
 * </ul>
 * </p>
 */
public class Partition {
    /** First location in this partition. */
    public PartitionLocation locations = null;

    /** Next partition to compute, or {@code null} if this is the last partition to compute. */
    public Partition nextCompute = null;

    /** Previous partition to compute, or {@code null} if this is the first partition to compute. */
    public Partition prevCompute = null;

    /** Flag whether the partition has been added to the compute list. */
    public boolean inComputeList = false;

    /** New partition where locations of this partition are moved to. */
    public Partition newPartition = null;

    /** Single linked list to partitions that have a new partition. */
    public Partition nextAffected = null;

    /**
     * Add a location to this partition. Location should not be in another partition.
     *
     * @param ploc Location to add.
     */
    public void addLocation(PartitionLocation ploc) {
        ploc.prevPLoc = null;
        ploc.nextPLoc = locations;
        if (locations != null) {
            locations.prevPLoc = ploc;
        }
        locations = ploc;
    }

    /**
     * Remove a location from this partition. Location should be in this partition.
     *
     * @param ploc Location to remove.
     */
    public void removeLocation(PartitionLocation ploc) {
        if (ploc.nextPLoc != null) {
            ploc.nextPLoc.prevPLoc = ploc.prevPLoc;
        }
        if (ploc.prevPLoc != null) {
            ploc.prevPLoc.nextPLoc = ploc.nextPLoc;
        }
        if (ploc == locations) {
            locations = ploc.nextPLoc;
        }
    }

    /**
     * Get the locations of a partition.
     *
     * @return Locations belonging to the partition.
     */
    public Set<Location> getLocations() {
        int count = 0;
        for (PartitionLocation pl = locations; pl != null; pl = pl.nextPLoc) {
            count++;
        }

        Set<Location> locs = setc(count);
        for (PartitionLocation pl = locations; pl != null; pl = pl.nextPLoc) {
            locs.add(pl.loc);
        }
        return locs;
    }
}
