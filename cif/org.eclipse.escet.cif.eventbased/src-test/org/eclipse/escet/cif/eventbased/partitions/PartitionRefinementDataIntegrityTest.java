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

import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Event.EventControllability;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class PartitionRefinementDataIntegrityTest {
    public Location loc1;

    public Location loc2;

    public Location loc3;

    @Before
    public void setUp() throws Exception {
        Event e = new Event("e", EventControllability.CONTR_EVENT);
        Automaton aut = new Automaton(set(e));
        loc1 = new Location(aut, null);
        loc2 = new Location(aut, null);
        loc3 = new Location(aut, null);
    }

    @Test
    public void testEmpty() {
        // Verify sane default reference values (add 0 partitions).
        Set<Set<Location>> partitions = set();

        PartitionRefinement oc = new PartitionRefinement(null, null, partitions);

        assertEquals(0, oc.locationMapping.size());
        assertEquals(null, oc.computePartitions);
    }

    public PartitionRefinement addSinglePartitionSingleLocation() {
        Set<Location> part1 = set();
        part1.add(loc1);
        Set<Set<Location>> partitions = set();
        partitions.add(part1);

        PartitionRefinement oc = new PartitionRefinement(null, null, partitions);

        assertEquals(1, oc.locationMapping.size());
        PartitionLocation ploc = oc.locationMapping.get(loc1);
        assertNotNull(ploc);
        // Verify partition location.
        assertEquals(loc1, ploc.loc);
        assertNull(ploc.prevPLoc);
        assertNull(ploc.nextPLoc);
        Partition p = ploc.partition;
        // Verify partition.
        assertNotNull(p);
        assertEquals(ploc, p.locations);
        assertNull(p.newPartition);
        assertNull(p.prevCompute);
        assertNull(p.nextCompute);
        assertEquals(true, p.inComputeList);

        assertEquals(p, oc.computePartitions);
        return oc;
    }

    @Test
    public void testSinglePartitionSingleLocation() {
        addSinglePartitionSingleLocation();
    }

    public PartitionRefinement addSinglePartitionTwoLocations() {
        Set<Location> part1 = set();
        // 'part1' preserves insertion order, linked list of oc reverses order.
        part1.add(loc2);
        part1.add(loc1);
        Set<Set<Location>> partitions = set();
        partitions.add(part1);

        PartitionRefinement oc = new PartitionRefinement(null, null, partitions);

        assertEquals(2, oc.locationMapping.size());
        PartitionLocation ploc1 = oc.locationMapping.get(loc1);
        PartitionLocation ploc2 = oc.locationMapping.get(loc2);
        // Verify partition location 1.
        assertNotNull(ploc1);
        assertEquals(loc1, ploc1.loc);
        assertNull(ploc1.prevPLoc);
        assertEquals(ploc2, ploc1.nextPLoc);
        Partition p = ploc1.partition;
        // Verify partition location 2.
        assertNotNull(ploc2);
        assertEquals(loc2, ploc2.loc);
        assertEquals(ploc1, ploc2.prevPLoc);
        assertNull(ploc2.nextPLoc);
        assertEquals(p, ploc2.partition);
        // Verify partition.
        assertNotNull(p);
        assertEquals(ploc1, p.locations);
        assertNull(p.newPartition);
        assertNull(p.prevCompute);
        assertNull(p.nextCompute);
        assertEquals(true, p.inComputeList);

        assertEquals(p, oc.computePartitions);
        return oc;
    }

    @Test
    public void testSinglePartitionTwoLocations() {
        addSinglePartitionTwoLocations();
    }

    public PartitionRefinement addTwoPartitionsSingleLocation() {
        Set<Location> part1 = set();
        part1.add(loc1);
        Set<Location> part2 = set();
        part2.add(loc2);
        // 'partitions' preserves insertion order, linked list of oc reverses
        // order.
        Set<Set<Location>> partitions = set();
        partitions.add(part2);
        partitions.add(part1);

        PartitionRefinement oc = new PartitionRefinement(null, null, partitions);

        assertEquals(2, oc.locationMapping.size());
        PartitionLocation ploc1 = oc.locationMapping.get(loc1);
        PartitionLocation ploc2 = oc.locationMapping.get(loc2);
        // Verify partition location 1.
        assertNotNull(ploc1);
        assertEquals(loc1, ploc1.loc);
        assertNull(ploc1.prevPLoc);
        assertNull(ploc1.nextPLoc);
        Partition p1 = ploc1.partition;
        // Verify partition location 2.
        assertNotNull(ploc2);
        assertEquals(loc2, ploc2.loc);
        assertNull(ploc2.prevPLoc);
        assertNull(ploc2.nextPLoc);
        Partition p2 = ploc2.partition;
        // Verify partitions.
        assertNotNull(p1);
        assertNotNull(p2);
        assertNotSame(p1, p2);

        assertEquals(ploc1, p1.locations);
        assertNull(p1.newPartition);
        assertNull(p1.prevCompute);
        assertEquals(p2, p1.nextCompute);
        assertEquals(true, p1.inComputeList);

        assertEquals(ploc2, p2.locations);
        assertNull(p2.newPartition);
        assertEquals(p1, p2.prevCompute);
        assertNull(p2.nextCompute);
        assertEquals(true, p2.inComputeList);

        assertEquals(p1, oc.computePartitions);
        return oc;
    }

    @Test
    public void testTwoPartitionsSingleLocation() {
        addTwoPartitionsSingleLocation();
    }

    @Test
    public void testMoveEntirePartitionSingleLocation() {
        // Move entire partition with one location.
        PartitionRefinement oc = addSinglePartitionSingleLocation();
        PartitionLocation ploc = oc.locationMapping.get(loc1);
        Partition origPart = ploc.partition;
        assertNotNull(origPart);

        // Move the one location of the one partition to a new partition.
        Partition affected = oc.moveLocations(set(loc1));

        // Location mapping and ploc data (location side) should not be
        // changed.
        assertEquals(1, oc.locationMapping.size());
        assertEquals(ploc, oc.locationMapping.get(loc1));
        assertEquals(loc1, ploc.loc);
        Partition newPart = ploc.partition;
        // Partition should be new.
        assertNotNull(newPart);
        assertNotSame(origPart, newPart);
        // Original should be empty.
        assertNull(origPart.locations);
        assertNull(ploc.prevPLoc);
        assertNull(ploc.nextPLoc);
        // New one should be non-empty.
        assertEquals(ploc, newPart.locations);
        // Original partition is affected and points to new partition.
        assertNull(origPart.nextAffected);
        assertEquals(origPart, affected);
        assertEquals(newPart, origPart.newPartition);
        // Compute lists are not changed yet.
        assertNull(origPart.prevCompute);
        assertNull(origPart.nextCompute);
        assertEquals(true, origPart.inComputeList);
        assertEquals(origPart, oc.computePartitions);
        assertEquals(false, newPart.inComputeList);
    }

    @Test
    public void testMoveEntirePartitionTwoLocation() {
        // Move entire partition with two locations.
        PartitionRefinement oc = addSinglePartitionTwoLocations();
        PartitionLocation ploc1 = oc.locationMapping.get(loc1);
        PartitionLocation ploc2 = oc.locationMapping.get(loc2);
        Partition origPart = ploc1.partition;
        assertEquals(origPart, ploc2.partition);
        assertNotNull(origPart);

        // Move the one location of the one partition to a new partition.
        Set<Location> locs = set();
        locs.add(loc2);
        locs.add(loc1);
        Partition affected = oc.moveLocations(locs);

        // Location mapping and ploc data (location side) should not be
        // changed.
        assertEquals(2, oc.locationMapping.size());
        assertEquals(ploc1, oc.locationMapping.get(loc1));
        assertEquals(ploc2, oc.locationMapping.get(loc2));
        assertEquals(loc1, ploc1.loc);
        assertEquals(loc2, ploc2.loc);
        Partition newPart = ploc1.partition;
        assertEquals(newPart, ploc2.partition);
        // Partition should be new.
        assertNotNull(newPart);
        assertNotSame(origPart, newPart);
        // Original should be empty.
        assertNull(origPart.locations);
        // New one should be non-empty.
        assertEquals(ploc1, newPart.locations);
        assertEquals(ploc2, newPart.locations.nextPLoc);
        // Original partition is affected and points to new partition.
        assertNull(origPart.nextAffected);
        assertEquals(origPart, affected);
        assertEquals(newPart, origPart.newPartition);
        // Compute lists are not changed yet.
        assertNull(origPart.prevCompute);
        assertNull(origPart.nextCompute);
        assertEquals(true, origPart.inComputeList);
        assertEquals(origPart, oc.computePartitions);
        assertEquals(false, newPart.inComputeList);
    }

    @Test
    public void testMovePartialPartition() {
        PartitionRefinement oc = addSinglePartitionTwoLocations();
        PartitionLocation ploc1 = oc.locationMapping.get(loc1);
        PartitionLocation ploc2 = oc.locationMapping.get(loc2);
        Partition origPart = ploc1.partition;
        assertEquals(origPart, ploc2.partition);

        Partition affected = oc.moveLocations(set(loc1));
        // Location mapping should be the same.
        assertEquals(ploc1, oc.locationMapping.get(loc1));
        assertEquals(ploc2, oc.locationMapping.get(loc2));
        // Location1 partition should be new.
        Partition newPart = ploc1.partition;
        assertNotSame(origPart, newPart);
        assertEquals(origPart, ploc2.partition);

        // Original partition is affected.
        assertEquals(origPart, affected);
        assertNull(origPart.nextAffected);
        // Old partition point to the new one.
        assertEquals(newPart, origPart.newPartition);

        // Location backlinks of the partitions should hold.
        assertEquals(ploc1, newPart.locations);
        assertEquals(ploc2, origPart.locations);
        assertNull(ploc1.nextPLoc);
        assertNull(ploc1.prevPLoc);
        assertNull(ploc2.nextPLoc);
        assertNull(ploc2.prevPLoc);

        // Compute lists are not changed yet.
        assertNull(origPart.prevCompute);
        assertNull(origPart.nextCompute);
        assertEquals(true, origPart.inComputeList);
        assertEquals(origPart, oc.computePartitions);
        assertEquals(false, newPart.inComputeList);
    }

    @Test
    public void testMergeEntirePartition() {
        // One partition with one location, add a set with that location.
        // -> 'nothing' should happen (only the partition object is new).
        PartitionRefinement oc = addSinglePartitionSingleLocation();
        PartitionLocation ploc = oc.locationMapping.get(loc1);
        Partition origPart = ploc.partition;

        Partition affected = oc.moveLocations(set(loc1));
        Partition newPart = ploc.partition;

        Partition current = oc.mergeLocations(affected, origPart);

        // Back to a single partition with a single location.
        // == Checks below are mostly from addSinglePartitionSingleLocation ==
        assertEquals(1, oc.locationMapping.size());
        assertEquals(ploc, oc.locationMapping.get(loc1));
        // Verify partition location.
        assertEquals(loc1, ploc.loc);
        assertNull(ploc.prevPLoc);
        assertNull(ploc.nextPLoc);
        assertEquals(newPart, ploc.partition);
        // Verify partition.
        assertNotNull(newPart);
        assertEquals(ploc, newPart.locations);
        assertNull(newPart.newPartition);
        assertNull(newPart.prevCompute);
        assertNull(newPart.nextCompute);
        assertEquals(true, newPart.inComputeList);

        assertEquals(newPart, oc.computePartitions);

        // 'current partition' should be moved to the new partition as well.
        assertEquals(newPart, current);
    }

    @Test
    public void testMergePartialPartition() {
        // Add single partition two locations, move one location to a
        // new partition.
        // -> results in two partitions, each with one location.
        PartitionRefinement oc = addSinglePartitionTwoLocations();
        PartitionLocation ploc1 = oc.locationMapping.get(loc1);
        PartitionLocation ploc2 = oc.locationMapping.get(loc2);
        Partition origPart = ploc1.partition;

        Partition affected = oc.moveLocations(set(loc1));
        Partition newPart = ploc1.partition;

        Partition current = oc.mergeLocations(affected, origPart);
        assertNull(current);

        // Back to two partitions both with a single variable.
        // == Checks below are mostly from addTwoPartitionsSingleLocation ==

        assertEquals(2, oc.locationMapping.size());
        assertEquals(ploc1, oc.locationMapping.get(loc1));
        assertEquals(ploc2, oc.locationMapping.get(loc2));
        // Verify partition location 1.
        assertNotNull(ploc1);
        assertEquals(loc1, ploc1.loc);
        assertNull(ploc1.prevPLoc);
        assertNull(ploc1.nextPLoc);
        assertEquals(newPart, ploc1.partition);
        // Verify partition location 2.
        assertNotNull(ploc2);
        assertEquals(loc2, ploc2.loc);
        assertNull(ploc2.prevPLoc);
        assertNull(ploc2.nextPLoc);
        assertEquals(origPart, ploc2.partition);
        // Verify partitions.
        assertNotNull(newPart);
        assertNotNull(origPart);
        assertNotSame(newPart, origPart);

        assertEquals(ploc1, newPart.locations);
        assertNull(newPart.newPartition);
        assertNull(newPart.prevCompute);
        assertEquals(origPart, newPart.nextCompute);
        assertEquals(true, newPart.inComputeList);

        assertEquals(ploc2, origPart.locations);
        assertNull(origPart.newPartition);
        assertEquals(newPart, origPart.prevCompute);
        assertNull(origPart.nextCompute);
        assertEquals(true, origPart.inComputeList);

        assertEquals(newPart, oc.computePartitions);
    }

    @Test
    public void testAffectedChain() {
        PartitionRefinement oc = addTwoPartitionsSingleLocation();
        PartitionLocation ploc1 = oc.locationMapping.get(loc1);
        Partition part1 = ploc1.partition;
        PartitionLocation ploc2 = oc.locationMapping.get(loc2);
        Partition part2 = ploc2.partition;
        assertNotSame(part1, part2); // Already done.

        Set<Location> locs = set();
        locs.add(loc2);
        locs.add(loc1);
        Partition affected = oc.moveLocations(locs);
        // Chain of affected partitions should cover both old partitions.
        ploc1 = oc.locationMapping.get(loc1);
        Partition newPart1 = ploc1.partition;
        ploc2 = oc.locationMapping.get(loc2);
        Partition newPart2 = ploc2.partition;
        assertNotSame(part1, newPart1);
        assertNotSame(part2, newPart2);
        assertNotSame(newPart1, newPart2);
        assertEquals(part1, affected);
        assertEquals(part2, part1.nextAffected);
        assertNull(part2.nextAffected);
    }
}
