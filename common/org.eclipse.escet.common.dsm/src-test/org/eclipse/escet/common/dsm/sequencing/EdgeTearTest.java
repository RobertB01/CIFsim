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

package org.eclipse.escet.common.dsm.sequencing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.escet.common.dsm.sequencing.graph.Cycle;
import org.eclipse.escet.common.dsm.sequencing.graph.Edge;
import org.junit.Test;

/** Tests for tearing edges of cycles. */
@SuppressWarnings("javadoc")
public class EdgeTearTest {
    private int countTearedEdges(List<Edge> edges) {
        int count = 0;
        for (Edge e: edges) {
            count = count + (e.teared ? 1 : 0);
        }
        return count;
    }

    @Test
    public void tearSingleCycle() {
        Edge edge78 = new Edge(7, 8);
        Edge edge89 = new Edge(8, 9);
        Edge edge97 = new Edge(9, 7);
        Cycle cycle789 = new Cycle(List.of(edge78, edge89, edge97));
        Sequencer.tearCycles(List.of(cycle789));
        assertEquals(1, countTearedEdges(cycle789.edges));
    }

    @Test
    public void tearSharedVertexCollection() {
        // Edge tearing sees this collection as two independent cycles since it only cares about edges.
        Edge edge12 = new Edge(1, 2);
        Edge edge21 = new Edge(2, 1);
        Cycle cycle12 = new Cycle(List.of(edge12, edge21));
        Edge edge23 = new Edge(2, 3);
        Edge edge35 = new Edge(3, 5);
        Edge edge52 = new Edge(5, 2);
        Cycle cycle235 = new Cycle(List.of(edge23, edge35, edge52));
        Sequencer.tearCycles(List.of(cycle12, cycle235));
        assertEquals(1, countTearedEdges(cycle12.edges));
        assertEquals(1, countTearedEdges(cycle235.edges));
    }

    @Test
    public void tearSharedEdgeCollection() {
        Edge edge23 = new Edge(2, 3);
        Edge edge35 = new Edge(3, 5);
        Edge edge52 = new Edge(5, 2);
        Cycle cycle235 = new Cycle(List.of(edge23, edge35, edge52)); // Shares edge 3-5
        Edge edge56 = new Edge(5, 6);
        Edge edge63 = new Edge(6, 3);
        Cycle cycle356 = new Cycle(List.of(edge63, edge35, edge56)); // Shares edge 3-5.
        Sequencer.tearCycles(List.of(cycle235, cycle356));
        assertEquals(1, countTearedEdges(cycle235.edges));
        assertEquals(1, countTearedEdges(cycle356.edges));
        assertTrue(edge35.teared); // Heuristic forces tearing this edge.
    }
}
