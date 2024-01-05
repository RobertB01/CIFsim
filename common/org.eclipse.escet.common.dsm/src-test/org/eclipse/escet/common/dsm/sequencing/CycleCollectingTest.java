//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.sequencing;

import static org.eclipse.escet.common.java.Sets.set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.common.dsm.sequencing.graph.Cycle;
import org.eclipse.escet.common.dsm.sequencing.graph.Edge;
import org.junit.jupiter.api.Test;

/** Tests for grouping related cycles into collections. */
@SuppressWarnings("javadoc")
public class CycleCollectingTest {
    private final Edge edge12 = new Edge(1, 2);

    private final Edge edge21 = new Edge(2, 1);

    private final Cycle cycle12 = new Cycle(List.of(edge12, edge21));

    private final Edge edge23 = new Edge(2, 3);

    private final Edge edge35 = new Edge(3, 5);

    private final Edge edge52 = new Edge(5, 2);

    private final Cycle cycle235 = new Cycle(List.of(edge23, edge35, edge52)); // Shares edge 3-5.

    private final Edge edge56 = new Edge(5, 6);

    private final Edge edge63 = new Edge(6, 3);

    private final Cycle cycle356 = new Cycle(List.of(edge63, edge35, edge56)); // Shares edge 3-5.

    private final Edge edge78 = new Edge(7, 8);

    private final Edge edge89 = new Edge(8, 9);

    private final Edge edge97 = new Edge(9, 7);

    private final Cycle cycle789 = new Cycle(List.of(edge78, edge89, edge97));

    @Test
    public void testNoCycle() {
        Set<Cycle> cycles = set();
        List<List<Cycle>> collections = Sequencer.makeCycleCollections(cycles);
        assertEquals(0, collections.size());
    }

    @Test
    public void testIndependentCycles() {
        Set<Cycle> cycles = set(cycle12, cycle789);
        List<List<Cycle>> collections = Sequencer.makeCycleCollections(cycles);
        assertEquals(2, collections.size());
        assertTrue(collections.contains(List.of(cycle12)));
        assertTrue(collections.contains(List.of(cycle789)));
        assertFalse(collections.contains(List.of(cycle356)));
    }

    @Test
    public void testMergeSharedVertexCycles() {
        Set<Cycle> cycles = set(cycle12, cycle235);
        List<List<Cycle>> collections = Sequencer.makeCycleCollections(cycles);
        assertEquals(1, collections.size());
        assertEquals(2, collections.get(0).size());
    }

    @Test
    public void testMergeSharedEdgeCycles() {
        Set<Cycle> cycles = set(cycle235, cycle356);
        List<List<Cycle>> collections = Sequencer.makeCycleCollections(cycles);
        assertEquals(1, collections.size());
        assertEquals(2, collections.get(0).size());
    }

    @Test
    public void testMerge3Cycles() {
        Set<Cycle> cycles = set(cycle12, cycle235, cycle356);
        List<List<Cycle>> collections = Sequencer.makeCycleCollections(cycles);
        assertEquals(1, collections.size());
        assertEquals(3, collections.get(0).size());
    }

    @Test
    public void testAllCyclesTest() {
        Set<Cycle> cycles = set(cycle12, cycle789, cycle235, cycle356);
        List<List<Cycle>> collections = Sequencer.makeCycleCollections(cycles);
        assertEquals(2, collections.size());
        assertTrue(collections.contains(List.of(cycle789)));
    }
}
